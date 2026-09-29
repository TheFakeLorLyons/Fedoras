(ns fedoras.cs-class.walk.walk
  "Standing on the quad, looking at it.

  WHAT CHANGED. This used to draw itself one screen column at a time,
  in ClojureScript, on a 2D canvas, which is how the first raycasters
  did it and is a fine thing to have written once. It now hands the
  whole quad to the graphics card as a pile of triangles and lets the
  card work out what covers what. Everything below is unaffected by
  that, because everything below is about the plan and not about the
  picture: this file still only says where things are.

  THE GROUND. Everything you are standing on is either paving or lawn,
  and which one is not written down anywhere: it is worked out from the
  buildings. A building has an apron of paving round it, and the walks
  join the aprons up, and the rest is lawn. So a building that moves
  takes its paving with it and nobody has to remember.

  WHY THE GROUND MATTERS. Not because grass is interesting. Because a
  flat colour underfoot does not move when you move, and a grained one
  does, and the whole of the difference between walking somewhere and
  looking at a picture of it is whether the ground goes past.

  WHAT ELSE IS DERIVED. The paving, the trees, the hedges, the
  crosswalks, and the help posts: every one of them is worked out from
  the buildings and the roads rather than hand-placed and hoped to stay
  correct. A hand-placed number is correct until the day something else
  moves, and then it is just wrong until somebody notices.

  ONE THING IS NOT FLAT. THE FILL has a height. The renderer builds it
  a mound and you can climb it, which is the only place on this campus
  where the ground is a decision rather than a floor."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [fedoras.campus :as campus]
            [fedoras.cs-class.walk.walk-shaders :as shaders]
            [fedoras.widget :as w]
            [scicloj.kindly.v4.kind :as kind]))

(def ^:private far 1e9)

(def rotunda-door
  "How wide a doorway in a round building is, in feet."
  9)

(def rotunda-rooms
  "Where the four blocks inside a rotunda sit and how big they are, as
  fractions of its own radius, and how tall in feet. The names are what
  the walk says when one of them is in front of you, going round from
  the south east."
  {:reach 0.40 :size 0.42 :height 12
   :names ["ELEVATOR" "STAIRS" "CLOSET" "STAIRS"]})

(defn centroid
  "The middle of the campus, taken as the average of every building's
  own middle. Used to decide which way a door faces, which is why it is
  the buildings and not the map that it averages."
  [buildings]
  (let [how-many (count buildings)]
    [(/ (reduce + (map (fn [{:keys [x w]}] (+ x (/ w 2.0))) buildings)) how-many)
     (/ (reduce + (map (fn [{:keys [y h]}] (+ y (/ h 2.0))) buildings)) how-many)]))

(defn door-side
  "Which face the door is on. The long way round the building is the
  wrong way, so the wider of the two offsets from the middle wins."
  [{:keys [x y w h door]} [middle-x middle-y]]
  (or door
      (let [toward-middle-x (- middle-x (+ x (/ w 2.0)))
            toward-middle-y (- middle-y (+ y (/ h 2.0)))]
        (if (> (Math/abs toward-middle-x) (Math/abs toward-middle-y))
          (if (pos? toward-middle-x) :east :west)
          (if (pos? toward-middle-y) :south :north)))))

(defn faces-of
  "The four walls of a box, wound so that walking from `a` to `b` keeps
  the building on your left and `normal` points away from it.

  There used to be a second copy of this in walk_world.cljs, in another
  language, with different key names and the opposite winding, and every
  wall, door, window and doorstep on the campus was built from that one
  while the shrubbery was planted from this one. Now it is worked out
  here, once, and travels in the plan.

  `slot` is the face's number, which is all the renderer needs to give
  two walls of one building different brick."
  [{:keys [x y w h]}]
  (let [east (+ x w)
        south (+ y h)]
    [{:side :north :slot 0 :a [x y]        :b [east y]     :normal [0 -1] :length w}
     {:side :south :slot 1 :a [east south] :b [x south]    :normal [0 1]  :length w}
     {:side :west  :slot 2 :a [x south]    :b [x y]        :normal [-1 0] :length h}
     {:side :east  :slot 3 :a [east y]     :b [east south] :normal [1 0]  :length h}]))

(defn middle-of
  "A box's own middle, and the largest circle that fits inside it.

  Worked out here and nowhere else: a round building carries its
  :middle and :radius in the plan, so the renderer that draws it and
  the walker that bumps into it read the same two numbers rather than
  each doing this sum and hoping."
  [{:keys [x y w h]}]
  [(+ x (/ w 2.0)) (+ y (/ h 2.0)) (/ (min w h) 2.0)])

(defn round-faces
  "A round building has no flat walls, so what it has instead is a
  tangent across each of its doorways: a straight length of wall the
  width of a door, standing where a door stands, facing out.

  Everything that builds a door or a doorstep wants exactly that and
  nothing else about a wall, so the rotunda gets its doors and its steps
  from the same code every other building on the campus uses, and the
  curved part it does not share is the part that is genuinely curved."
  [box width]
  (let [[middle-x middle-y radius] (middle-of box)
        bearing {:east 0.0 :south (/ Math/PI 2) :west Math/PI
                 :north (* 1.5 Math/PI)}
        doors (or (:doors box) [(:door box :east)])]
    (vec
     (map-indexed
      (fn [slot side]
        (let [angle (bearing side 0.0)
              out-x (Math/cos angle)
              out-y (Math/sin angle)
              at-x (* radius out-x)
              at-y (* radius out-y)
              half (/ width 2.0)]
          {:side side :slot slot
           :a [(+ middle-x at-x (* out-y half))
               (+ middle-y at-y (* (- out-x) half))]
           :b [(+ middle-x at-x (* (- out-y) half))
               (+ middle-y at-y (* out-x half))]
           :normal [out-x out-y]
           :length width}))
      doors))))

(defn rooms-of
  "The four blocks inside a rotunda. Whatever the sign on each of them
  says, all four of them are a way down, and none of them is a room you
  are going to stand in.

  They are worked out here and travel in the plan, because the renderer
  has to build them and the walker has to walk into them, and those two
  agreeing is not something to leave to chance."
  [box {:keys [reach size height names]
        :or {reach 0.40 size 0.42 height 12}}]
  (let [[middle-x middle-y radius] (middle-of box)
        half (* radius size 0.5)]
    (vec
     (for [quarter (range 4)
           :let [angle (+ (/ Math/PI 4) (* quarter (/ Math/PI 2)))
                 at-x (+ middle-x (* radius reach (Math/cos angle)))
                 at-y (+ middle-y (* radius reach (Math/sin angle)))]]
       {:name (get names quarter "A ROOM")
        :x (- at-x half) :y (- at-y half)
        :w (* 2 half) :h (* 2 half) :height height}))))

(defn- step-along
  "A unit step from one end of a face to the other."
  [{:keys [a b length]}]
  (let [[from-x from-y] a
        [to-x to-y] b]
    [(/ (- to-x from-x) length) (/ (- to-y from-y) length)]))

(defn walkable-plan
  "The buildings as the page needs them: the rectangle, the name, the
  side the door is on, and a picture for it if there is one."
  ([] (walkable-plan campus/buildings))
  ([buildings]
   (let [middle (centroid buildings)]
     (mapv (fn [building]
             (let [round? (= :round (:shape building))]
               (-> (select-keys building
                                [:name :x :y :w :h :door-image :height
                                 :one-story? :shape :doors])
                   (assoc :door (if round?
                                  (first (or (:doors building) [:east]))
                                  (door-side building middle))
                          :faces (if round?
                                   (round-faces building rotunda-door)
                                   (faces-of building))
                          :rooms (when round?
                                   (rooms-of building rotunda-rooms)))
                   (cond-> round?
                     (assoc :middle (vec (take 2 (middle-of building)))
                            :radius (nth (middle-of building) 2))))))
           buildings))))

;; ---------------------------------------------------------------------
;; A repeatable number
;;
;; Every scattered thing on this campus comes out of this and a seed,
;; so the same plan grows the same wood and the same hedge every time
;; it is built. The shaders use the same trick with the same shape of
;; arithmetic, which is why the ground grain does not swim when you
;; walk.
;; ---------------------------------------------------------------------

(defn sprout
  "A number in 0..1 from a pair of numbers. The same pair always gives
  the same answer and neighbouring pairs give unrelated ones, which is
  the whole of what is wanted from it."
  [first-number second-number]
  (let [mixed (+ (* first-number 127.1) (* second-number 311.7))
        stretched (* (Math/sin mixed) 43758.5453)]
    (- stretched (Math/floor stretched))))

;; ---------------------------------------------------------------------
;; The cast, kept for the static one-picture-per-page renderer and for
;; "what is ahead," which the live widget answers a different way but a
;; printed page cannot.
;; ---------------------------------------------------------------------

(defn hit
  "How far along a ray the first face of a box is, and which kind of face
  it was.

  The slab method: a ray is inside an axis-aligned box exactly when it
  is between the two east-west edges and between the two north-south
  edges at the same time, so work out the stretch of the ray that
  satisfies each pair and intersect the two. If anything is left, the
  ray hits, and it hits at the start of what is left.

  Gives back [distance face] or nil."
  [[from-x from-y] [toward-x toward-y] {:keys [x y w h]}]
  (let [per-foot-east (if (zero? toward-x) far (/ 1.0 toward-x))
        per-foot-south (if (zero? toward-y) far (/ 1.0 toward-y))
        west-edge (* (- x from-x) per-foot-east)
        east-edge (* (- (+ x w) from-x) per-foot-east)
        north-edge (* (- y from-y) per-foot-south)
        south-edge (* (- (+ y h) from-y) per-foot-south)
        entering-east-west (min west-edge east-edge)
        leaving-east-west (max west-edge east-edge)
        entering-north-south (min north-edge south-edge)
        leaving-north-south (max north-edge south-edge)
        entering (max entering-east-west entering-north-south)
        leaving (min leaving-east-west leaving-north-south)]
    (when (and (>= leaving (max entering 0.0)) (> entering 0.001))
      [entering (if (> entering-east-west entering-north-south) :side :face)])))

(defn nearest
  "The first building in the way, out of all of them."
  [origin toward boxes]
  (reduce (fn [best box]
            (if-let [[distance face] (hit origin toward box)]
              (if (or (nil? best) (< distance (first best)))
                [distance face box]
                best)
              best))
          nil boxes))

(defn view
  "One entry per column of the picture."
  ([origin facing] (view origin facing 1.05 200 campus/buildings))
  ([origin facing fov columns boxes]
   (for [column (range columns)
         :let [off-middle (- (* fov (/ (double column) (dec columns))) (/ fov 2))
               aimed (+ facing off-middle)
               found (nearest origin
                              [(Math/cos aimed) (Math/sin aimed)]
                              boxes)]]
     (when found
       (let [[distance face box] found]
         {:column column
          :dist (* distance (Math/cos off-middle))
          :face face
          :name (:name box)})))))

(defn ahead
  "What you are looking at, which is whatever is in the middle column."
  [origin facing]
  (:name (nth (view origin facing) 100)))

;; ---------------------------------------------------------------------
;; The ground
;; ---------------------------------------------------------------------

(def apron
  "How far the paving reaches past a building's own wall. Zero, so
  grass runs right up to the brick rather than a skirt of pavement
  standing between them."
  0)

(defn aprons
  "A band of paving round each building."
  [buildings]
  (mapv (fn [{:keys [x y w h]}]
          {:x (- x apron) :y (- y apron)
           :w (+ w apron apron) :h (+ h apron apron)})
        buildings))

(defn walks
  "A straight run of paving between two buildings that are roughly in
  line with each other."
  [buildings]
  (let [middle-of (fn [{:keys [x y w h]}] [(+ x (/ w 2.0)) (+ y (/ h 2.0))])
        half-wide 9.0
        apart (fn [one other] (Math/abs (- one other)))]
    (for [from buildings
          to buildings
          :when (neg? (compare (:name from) (:name to)))
          :let [[from-x from-y] (middle-of from)
                [to-x to-y] (middle-of to)
                east-west (apart from-x to-x)
                north-south (apart from-y to-y)]
          :when (or (< east-west 70) (< north-south 70))]
      (if (< east-west north-south)
        {:x (Math/round (- (/ (+ from-x to-x) 2.0) half-wide))
         :y (Math/round (min from-y to-y))
         :w (Math/round (* half-wide 2))
         :h (Math/round north-south)}
        {:x (Math/round (min from-x to-x))
         :y (Math/round (- (/ (+ from-y to-y) 2.0) half-wide))
         :w (Math/round east-west)
         :h (Math/round (* half-wide 2))}))))

(def extra-paving
  "Hand-placed paving for a spot the algorithm does not reach cleanly:
  a gap between two buildings too far out of alignment for walks to
  connect them, or a corridor that needs to be wider than the
  campus-wide default without widening every other walk to match it."
  [])

(defn paving
  "Everything that is not lawn."
  ([] (paving campus/buildings))
  ([buildings] (vec (concat (aprons buildings) (walks buildings) extra-paving))))

(defn in-box? [{:keys [x y w h]} px py]
  (and (>= px x) (< px (+ x w)) (>= py y) (< py (+ y h))))

(defn ground-at
  "Paving or lawn. There is nothing else out there."
  ([px py] (ground-at (paving) px py))
  ([slabs px py]
   (if (some #(in-box? % px py) slabs) :paving :lawn)))

;; ---------------------------------------------------------------------
;; One picture, for the page
;; ---------------------------------------------------------------------

(def ^:private shades
  ["#e6e4df" "#c9c7c2" "#a8a6a2" "#8a8884" "#6d6b68" "#54524f"])

(def ^:private ground-shades
  ["#3a3f38" "#333831" "#2d322c" "#282c27" "#242822" "#20241f"])

(defn frame
  "One picture from one place, as hiccup. The printed page gets this;
  the screen gets the widget."
  ([origin facing] (frame origin facing 640 300))
  ([origin facing picture-width picture-height]
   (let [columns 200
         column-width (/ (double picture-width) columns)
         middle-height (/ picture-height 2.0)
         band-height (/ picture-height 12.0)
         seen (remove nil? (view origin facing 1.05 columns campus/buildings))
         band-of (fn [seen-column] (min 5 (int (/ (:dist seen-column) 90))))
         slab (fn [{:keys [column dist]}]
                (let [tall (min (* picture-height 1.4)
                                (/ 22000.0 (max dist 1.0)))
                      at (* column column-width)]
                  (format "M%.1f,%.1f L%.1f,%.1f"
                          at (double (- middle-height (/ tall 2.0)))
                          at (double (+ middle-height (/ tall 2.0))))))]
     (kind/hiccup
      (into
       [:svg {:viewBox (str "0 0 " picture-width " " picture-height)
              :width picture-width
              :xmlns "http://www.w3.org/2000/svg"}
        [:rect {:width picture-width :height picture-height
                :style {:fill "#141618"}}]
        (into [:g]
              (for [band (range 6)
                    :let [top (+ middle-height (* band-height (- 5 band)))]]
                [:rect {:y top :width picture-width :height band-height
                        :style {:fill (nth ground-shades band)}}]))]
       (for [band (range 6)
             :let [in-band (filter (fn [seen-column]
                                     (= band (band-of seen-column)))
                                   seen)]
             :when (seq in-band)]
         [:path {:d (str/join " " (map slab in-band))
                 :stroke (nth shades band)
                 :stroke-width (+ column-width 0.4)}]))))))

;; ---------------------------------------------------------------------
;; Roads, rail, the loop, and the paving as rectangles
;; ---------------------------------------------------------------------

(defn path-points
  "The points out of one of the plan's path strings."
  [path]
  (->> (re-seq #"-?\d+(?:\.\d+)?" path)
       (map (fn [number] (Double/parseDouble number)))
       (partition 2)
       (mapv vec)))

(defn segment-rect
  "One straight run, as a rectangle `wide` feet across."
  [[from-x from-y] [to-x to-y] wide]
  (let [half (/ wide 2.0)
        east-west (Math/abs (- to-x from-x))
        north-south (Math/abs (- to-y from-y))]
    (if (< east-west north-south)
      {:x (- (min from-x to-x) half) :y (min from-y to-y)
       :w wide :h north-south}
      {:x (min from-x to-x) :y (- (min from-y to-y) half)
       :w east-west :h wide})))

(def world-bound
  "Feet from the origin to the edge of the walkable world. Has to match
  :bound in walk_settings.cljs, since that is what actually stops (or
  rather wraps) the eye; this just tells the paths where the edge is."
  1000)

(defn- stretch-end
  "The far point pushed out along the line from the near point through
  it, until it reaches whichever edge of the square world it hits
  first."
  [[from-x from-y] [to-x to-y] bound]
  (let [step-x (- to-x from-x)
        step-y (- to-y from-y)]
    (if (and (zero? step-x) (zero? step-y))
      [to-x to-y]
      (let [steps-to-east-west
            (if (zero? step-x)
              ##Inf
              (/ (- (* bound (Math/signum (double step-x))) from-x) step-x))
            steps-to-north-south
            (if (zero? step-y)
              ##Inf
              (/ (- (* bound (Math/signum (double step-y))) from-y) step-y))
            steps (min (Math/abs steps-to-east-west)
                       (Math/abs steps-to-north-south))]
        [(+ from-x (* step-x steps)) (+ from-y (* step-y steps))]))))

(defn- extend-open-path
  "The first and last point of an open path, pushed straight out to the
  edge of the world, so a road that stops where somebody stopped
  drawing it does not stop in the middle of the quad."
  [points]
  (if (< (count points) 2)
    points
    (-> (vec points)
        (assoc 0 (stretch-end (second points) (first points) world-bound))
        (assoc (dec (count points))
               (stretch-end (nth points (- (count points) 2))
                            (last points)
                            world-bound)))))

(defn path-rects
  "A path as a run of rectangles. `closed?` joins the last point back to
  the first, for the Loop. `extend?` stretches an open path's own two
  ends out to the edge of the world first."
  ([path wide] (path-rects path wide false))
  ([path wide closed?] (path-rects path wide closed? false))
  ([path wide closed? extend?]
   (let [points (cond-> (path-points path)
                  extend? extend-open-path)
         runs (if closed?
                (partition 2 1 (concat points [(first points)]))
                (partition 2 1 points))]
     (mapv (fn [[from to]] (segment-rect from to wide)) runs))))

(def road-width
  "A through road, in feet, and a street between buildings. A street is
  narrower because it is doing less."
  {:road 26 :street 20})
(def shuttle-stops
  "How many feet apart the route's own points are kept. Nothing reads it
  yet; the shuttle will."
  120)
(def rail-width 12)

(defn axis-of
  "Which way a rectangle runs. The long side wins, because a road
  segment is hundreds of feet long and twenty-six wide and that ratio
  never lies."
  [{:keys [w h]}]
  (if (>= w h) :ew :ns))

(defn- overlap
  "The rectangle two rectangles share, or nil when they do not."
  [one other]
  (let [west (max (:x one) (:x other))
        north (max (:y one) (:y other))
        east (min (+ (:x one) (:w one)) (+ (:x other) (:w other)))
        south (min (+ (:y one) (:h one)) (+ (:y other) (:h other)))]
    (when (and (> east west) (> south north))
      {:x west :y north :w (- east west) :h (- south north)})))

(def marking-style
  "Which set of markings a road wears. The renderer carries this on every
  vertex of the road rather than in a material of its own, because a
  street and a road are the same tarmac and differ only in what is
  painted on them."
  {:road 0 :street 1})

(defn road-rects
  "Every road, as rectangles, each carrying which way it runs and which
  markings it wears. A crosswalk cannot work its own direction out from
  its shape, since its shape is just the walk's width by the road's
  width, so it inherits this one."
  []
  (mapcat (fn [{:keys [d kind]}]
            (map (fn [rect]
                   (assoc rect
                          :surface :road
                          :axis (axis-of rect)
                          :style (marking-style kind 0)))
                 (path-rects d (road-width kind 26) false true)))
          campus/roads))

(defn crosswalks
  "Where a walk crosses a road: the overlap between the two, tagged with
  the road's own direction, so the stripes paint across the traffic and
  not along it."
  [walk-rects roads]
  (vec
   (for [walk walk-rects
         road roads
         :let [shared (overlap walk road)]
         :when shared]
     (assoc shared :surface :crosswalk :axis (:axis road)))))

(def surface-order
  "The order the ground is laid in. Two rectangles that cover the same
  spot are both telling the truth, so the later one is lifted a whisker
  above the earlier one and the depth buffer settles it. A crosswalk is
  laid last of all because it really is a road and it really is where
  people cross it.

  The shuttle used to have a layer here, painted along the roads it ran
  on, which is how a road ended up marked two ways at once. A shuttle
  drives on roads. It does not have its own colour of tarmac."
  [:ground :lot :road :rail :paving :crosswalk])

(def surface-layer
  (into {} (map-indexed (fn [layer surface] [surface layer]) surface-order)))

(defn surfaces
  "Every rectangle you can stand on, each with which way it runs and
  which layer it was laid in."
  []
  (let [walk-rects (paving)
        roads (road-rects)
        lay (fn [layer rect]
              (assoc rect :layer (surface-layer layer) :axis (axis-of rect)))]
    (vec (concat
          (keep (fn [ground]
                  (when (:surface ground)
                    (-> (select-keys ground [:x :y :w :h :surface :name])
                        (->> (lay :ground)))))
                campus/grounds)
          (map (fn [lot]
                 (lay :lot (assoc (select-keys lot [:x :y :w :h]) :surface :lot)))
               campus/lots)
          (map (fn [rect] (lay :road rect)) roads)
          (map (fn [rect] (lay :rail (assoc rect :surface :rail)))
               (path-rects campus/rail rail-width false true))
          (map (fn [rect] (lay :paving (assoc rect :surface :paving))) walk-rects)
          (map (fn [crossing] (assoc crossing :layer (surface-layer :crosswalk)))
               (crosswalks walk-rects roads))))))

;; ---------------------------------------------------------------------
;; What grows on it
;; ---------------------------------------------------------------------

(def tree-kinds
  [{:kind :oak   :height 38 :radius 15 :trunk 1.4}
   {:kind :maple :height 30 :radius 12 :trunk 1.1}
   {:kind :birch :height 26 :radius 8  :trunk 0.8}])

(def bush-kinds
  [{:kind :box     :height 3.6 :radius 3.0}
   {:kind :hedge   :height 2.8 :radius 4.2}
   {:kind :spirea  :height 5.2 :radius 3.4}
   {:kind :juniper :height 2.2 :radius 5.0}])

(defn clear-of?
  "True when a point is `margin` feet outside every one of `rects`."
  [rects px py margin]
  (not-any? (fn [{:keys [x y w h]}]
              (and (> px (- x margin)) (< px (+ x w margin))
                   (> py (- y margin)) (< py (+ y h margin))))
            rects))

(defn no-go
  "Everything a growing thing has to keep out of: the buildings, the
  paving, the roads, the lots, the rail, and every named piece of ground
  that is not lawn. Each one of these was missing from the list until
  somebody noticed a tree standing in the brook."
  [buildings slabs roads lots]
  (vec (concat buildings slabs roads lots
               (path-rects campus/rail rail-width false true)
               (keep (fn [ground]
                       (when (not= :lawn (:surface ground))
                         (select-keys ground [:x :y :w :h])))
                     campus/grounds))))

(defn scattered-trees
  "Trees on the outskirts, from a seed, so the same campus grows the
  same wood every time it is built."
  ([] (scattered-trees campus/buildings (paving) (road-rects) campus/lots {}))
  ([buildings slabs roads lots {:keys [seed how-many margin apart extent]
                                :or {seed 4471 how-many 70 margin 12 apart 24
                                     extent [40 40 940 740]}}]
   (let [rolls (java.util.Random. seed)
         blocked (no-go buildings slabs roads lots)
         [west north east south] extent]
     (loop [tries 0
            found []]
       (if (or (>= (count found) how-many) (> tries (* how-many 40)))
         found
         (let [px (+ west (* (.nextDouble rolls) (- east west)))
               py (+ north (* (.nextDouble rolls) (- south north)))
               kind (nth tree-kinds
                         (int (* (.nextDouble rolls) (count tree-kinds))))
               planted (mapv (fn [{:keys [x y]}] {:x x :y y :w 0 :h 0}) found)]
           (if (and (clear-of? blocked px py 7)
                    (clear-of? buildings px py margin)
                    (clear-of? planted px py apart))
             (recur (inc tries)
                    (conj found (assoc kind
                                       :x (Math/round px)
                                       :y (Math/round py))))
             (recur (inc tries) found))))))))

(def quad-trees
  "Four in the quad itself, off the walk and out of the way of the
  fountain."
  [{:kind :oak   :height 34 :radius 13 :trunk 1.3 :x 372 :y 292}
   {:kind :oak   :height 31 :radius 12 :trunk 1.2 :x 372 :y 368}
   {:kind :maple :height 29 :radius 12.5 :trunk 1.0 :x 538 :y 292}
   {:kind :maple :height 32 :radius 12 :trunk 1.1 :x 538 :y 368}])

(defn trees
  ([] (trees campus/buildings (paving) (road-rects) campus/lots {}))
  ([buildings slabs roads lots options]
   (vec (concat quad-trees (scattered-trees buildings slabs roads lots options)))))

;; ---------------------------------------------------------------------
;; Hedges
;;
;; A bush is not scattered like a tree. Somebody planted these against
;; the walls, so they are worked out from the walls: step along each
;; face, stand off a few feet, skip the corners, skip the doorway, and
;; skip whatever lands on pavement.
;; ---------------------------------------------------------------------

(defn foundation-bushes
  "Planting against the buildings. Deterministic, so it stays put."
  ([] (foundation-bushes campus/buildings (paving) (road-rects) campus/lots {}))
  ([buildings slabs roads lots {:keys [gap stand-off inset thin]
                                :or {gap 15 stand-off 4.0 inset 10 thin 0.42}}]
   (let [middle (centroid buildings)
         blocked (no-go buildings slabs roads lots)]
     (vec
      (for [building buildings
            :when (not= :round (:shape building))
            {:keys [side a normal length] :as face} (faces-of building)
            feet-along (range inset (- length inset) gap)
            :let [[corner-x corner-y] a
                  [along-x along-y] (step-along face)
                  [away-x away-y] normal
                  px (+ corner-x (* along-x feet-along) (* away-x stand-off))
                  py (+ corner-y (* along-y feet-along) (* away-y stand-off))
                  roll (sprout px py)
                  doorway? (and (= side (door-side building middle))
                                (< (Math/abs (- feet-along (/ length 2.0))) 10))]
            :when (and (not doorway?)
                       (> roll thin)
                       (clear-of? blocked px py 1.5)
                       (clear-of? buildings px py 1.0))
            :let [kind (nth bush-kinds
                            (int (* (sprout py px) (count bush-kinds))))
                  scale (+ 0.75 (* 0.5 roll))]]
        (assoc kind
               :x (Math/round px) :y (Math/round py)
               :height (* (:height kind) scale)
               :radius (* (:radius kind) scale)))))))

(def quad-bushes
  "A ring round the fountain, kept back far enough that you can still
  walk round it."
  (vec
   (for [around (range 8)
         :let [angle (* 2 Math/PI (/ around 8.0))
               radius 25.0
               px (+ 455 (* radius (Math/cos angle)))
               py (+ 330 (* radius (Math/sin angle)))]]
     {:kind :box :x (Math/round px) :y (Math/round py)
      :height (+ 2.6 (* 1.4 (sprout px py)))
      :radius (+ 2.8 (* 1.2 (sprout py px)))})))

(defn bushes
  ([] (bushes campus/buildings (paving) (road-rects) campus/lots {}))
  ([buildings slabs roads lots options]
   (vec (concat quad-bushes (foundation-bushes buildings slabs roads lots options)))))

(def fountain
  "In the middle of the quad."
  {:x 455 :y 330 :radius 16 :rim 1.4 :water 1.6
   :pedestal {:radius 2.4 :height 4.5}})

;; ---------------------------------------------------------------------
;; Help posts, authored at roughly the right spot and corrected for
;; whatever the roads and buildings actually do there
;; ---------------------------------------------------------------------

(defn- distance
  [[from-x from-y] [to-x to-y]]
  (Math/sqrt (+ (* (- to-x from-x) (- to-x from-x))
                (* (- to-y from-y) (- to-y from-y)))))

(defn- nearest-on
  "The closest point on a rectangle's own boundary to a point: the clamp
  onto its edges if the point is already outside, or a snap to whichever
  edge is nearest if the point is inside it."
  [[px py] {:keys [x y w h]}]
  (if (and (> px x) (< px (+ x w)) (> py y) (< py (+ y h)))
    (let [to-west (- px x)
          to-east (- (+ x w) px)
          to-north (- py y)
          to-south (- (+ y h) py)
          nearest-edge (min to-west to-east to-north to-south)]
      (cond
        (= nearest-edge to-west) [x py]
        (= nearest-edge to-east) [(+ x w) py]
        (= nearest-edge to-north) [px y]
        :else [px (+ y h)]))
    [(max x (min px (+ x w))) (max y (min py (+ y h)))]))

(defn- toward-paving
  "A cleared point, nudged onto the edge of the nearest paving within
  reach, so a post reads as standing beside a sidewalk rather than
  wherever it happened to land once it was merely clear of the road."
  [[px py] slabs reach]
  (let [candidates (keep (fn [slab]
                           (let [[edge-x edge-y] (nearest-on [px py] slab)
                                 how-far (distance [px py] [edge-x edge-y])]
                             (when (<= how-far reach)
                               [how-far edge-x edge-y])))
                         slabs)]
    (if (seq candidates)
      (let [[_ edge-x edge-y] (apply min-key first candidates)]
        [edge-x edge-y])
      [px py])))

(defn- push-clear
  "A point that is inside a rectangle, moved to just outside it, by
  whichever edge is nearest, plus a margin."
  [[px py] {:keys [x y w h]} margin]
  (let [east-side (+ x w)
        south-side (+ y h)
        to-west (- px x)
        to-east (- east-side px)
        to-north (- py y)
        to-south (- south-side py)
        nearest-edge (min to-west to-east to-north to-south)]
    (cond
      (= nearest-edge to-west) [(- x margin) py]
      (= nearest-edge to-east) [(+ east-side margin) py]
      (= nearest-edge to-north) [px (- y margin)]
      :else [px (+ south-side margin)])))

(defn- clear-post
  "A help post's authored spot, nudged out of any road or building it
  happens to land in, then drawn toward the nearest paving within reach
  so it settles at a sidewalk's edge. Coordinates come in as plain
  integer literals from campus/help-posts, which Clojure reads as Long,
  and Math/round has no overload for that, only float and double, so
  both are forced to double right where round is actually called."
  [[px py] roads buildings slabs margin]
  (let [[clear-x clear-y]
        (loop [at-x px
               at-y py
               tries 0]
          (let [road-here (some (fn [road] (when (in-box? road at-x at-y) road))
                                roads)
                building-here (some (fn [building]
                                      (when (in-box? building at-x at-y) building))
                                    buildings)]
            (cond
              (> tries 8)
              [at-x at-y]

              road-here
              (let [[pushed-x pushed-y] (push-clear [at-x at-y] road-here margin)]
                (recur pushed-x pushed-y (inc tries)))

              building-here
              (let [[pushed-x pushed-y] (push-clear [at-x at-y] building-here margin)]
                (recur pushed-x pushed-y (inc tries)))

              :else
              [at-x at-y])))
        [beside-x beside-y] (toward-paving [clear-x clear-y] slabs 22)
        still-blocked? (or (some (fn [road] (in-box? road beside-x beside-y)) roads)
                           (some (fn [building]
                                   (in-box? building beside-x beside-y))
                                 buildings))]
    (if still-blocked?
      [(Math/round (double clear-x)) (Math/round (double clear-y))]
      [(Math/round (double beside-x)) (Math/round (double beside-y))])))

;; ---------------------------------------------------------------------
;; The two pictures
;;
;; A door is a drawing, and a drawing has to reach the page as text. The
;; book renders to file:// as often as it renders to a server, and there
;; a fetched image is cross-origin: it loads, it taints the canvas it is
;; drawn on, and the graphics card then refuses to read that canvas and
;; throws from inside a load callback long after anything that could be
;; blamed. Inlined here and turned into a data URL in the browser, the
;; picture is same-origin and taints nothing.
;;
;; A missing file is not an error. There is no door on that building
;; until somebody puts one there, and the wall behind it was already
;; correct. It does say so on the way past, because a door that is
;; silently absent looks exactly like a door that is broken.
;; ---------------------------------------------------------------------

(def door-picture
  "The door, as a classpath path. `resources` is on :paths, so the file
  at resources/images/door.svg is images/door.svg from here."
  "images/door.svg")

(def window-picture
  "images/window.svg")

(defn picture-text
  "The text of an SVG on the classpath, or nil and a word about it."
  [path]
  (if-let [url (io/resource path)]
    (slurp url)
    (do (println "fedoras: no" path "on the classpath, so the walk goes without it.")
        nil)))

(defn cleared-help-posts
  "campus/help-posts, corrected. The plan authors them where a post
  would stand; this catches the day somebody moves a building and does
  not think to move the post beside it."
  []
  (let [roads (road-rects)
        buildings campus/buildings
        slabs (paving)]
    (mapv (fn [authored] (clear-post authored roads buildings slabs 5))
          campus/help-posts)))

;; ---------------------------------------------------------------------
;; The widget
;; ---------------------------------------------------------------------

(defn widget
  "Arrows to turn and to walk, A and D to sidestep. It will not let you
  into a building, because a wall is a wall. It will let you up the
  Fill, because the Fill is a hill."
  ([] (widget {}))
  ([{:keys [id at facing settings-rows]
     :or {id "walk" at [365 320] facing 0.0 settings-rows 22}}]
   (w/frame
    {:widget :walk
     :id id
     :label "THE QUAD"
     :settings-rows settings-rows
     :foot "It will not let you into a building. It will let you on the grass."
     :mount {:at at :facing facing
             :boxes (walkable-plan)
             :surfaces (surfaces)
             :trees (trees)
             :bushes (bushes)
             :posts (cleared-help-posts)
             :route (path-points campus/shuttle-route)
             :shaders shaders/programs
             :layouts shaders/layouts
             :door-picture (picture-text door-picture)
             :window-picture (picture-text window-picture)}}
    [:canvas.walk-canvas]
    [:p.walk-ahead])))