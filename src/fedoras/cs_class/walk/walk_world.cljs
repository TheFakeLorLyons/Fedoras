(ns fedoras.cs-class.walk.walk-world
  "The plan, turned into the numbers a graphics card wants.

  Everything in here runs once, when the settings are run. Nothing in
  here runs while you are walking. That division is the entire point of
  the rewrite: the old renderer worked out where a brick was every time
  it drew one, sixty times a second, and this one works out where every
  brick on the campus is exactly once and then never thinks about it
  again.

  A mesh is a flat run of floats, packed the way the matching shader
  expects to read them. The layout each builder packs to is named in a
  comment above it and defined nowhere in this file: it comes off the
  shader's own attribute vector, worked out when the book was built and
  carried here in the plan, so a name or a width is written down once
  and the two sides cannot drift apart."
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; A repeatable number, matching walk.clj's `sprout` and the shaders'
;; hash21 in spirit if not in arithmetic. Same seed, same campus.
;; ---------------------------------------------------------------------

(defn spot
  "A repeatable number in 0..1 from a pair of numbers. The same pair
  always gives the same answer and neighbouring pairs give unrelated
  ones, which is the whole of what is wanted from it."
  [first-number second-number]
  (let [mixed (+ (* first-number 127.1) (* second-number 311.7))
        stretched (* (js/Math.sin mixed) 43758.5453)]
    (- stretched (js/Math.floor stretched))))

(defn- ease
  "Smoothstep. Takes a fraction and gives it back with the corners taken
  off, so two lattice cells meet without a crease down the join."
  [fraction]
  (let [held (max 0.0 (min 1.0 fraction))]
    (* held held (- 3.0 (* 2.0 held)))))

(defn- rolling
  "Value noise: the four lattice points round a spot, blended smoothly,
  so a hill built out of it has no creases in it."
  [px py scale]
  (let [east (/ px scale)
        south (/ py scale)
        cell-east (js/Math.floor east)
        cell-south (js/Math.floor south)
        across (ease (- east cell-east))
        down (ease (- south cell-south))
        north-west (spot cell-east cell-south)
        north-east (spot (inc cell-east) cell-south)
        south-west (spot cell-east (inc cell-south))
        south-east (spot (inc cell-east) (inc cell-south))]
    (+ (* north-west (- 1 across) (- 1 down))
       (* north-east across (- 1 down))
       (* south-west (- 1 across) down)
       (* south-east across down))))

;; ---------------------------------------------------------------------
;; What you can be standing on
;;
;; The order of this vector is the order of the palette strip and the
;; order of the numbered cases in the ground shader. Change one and
;; change all three, and there is no way for the compiler to tell you
;; that you did not.
;; ---------------------------------------------------------------------

(def materials
  [:lawn :field :paving :road :lot :centreline :rail :fill :water :crosswalk
   :streambed])

(def material-index
  (into {} (map-indexed (fn [slot material] [material slot]) materials)))

(def palette-slots
  "Which pair of stylesheet properties each slot reads. Near shade on
  one row of the texture, far shade on the other."
  [["--walk-lawn"      "--walk-lawn-far"]
   ["--walk-field"     "--walk-field-far"]
   ["--walk-paving"    "--walk-paving-far"]
   ["--walk-road"      "--walk-road-far"]
   ["--walk-lot"       "--walk-lot-far"]
   ["--walk-centreline"      "--walk-centreline-far"]
   ["--walk-rail"      "--walk-rail-far"]
   ["--walk-fill"      "--walk-fill-far"]
   ["--walk-brook"     "--walk-brook-far"]
   ["--walk-road-mark" "--walk-road-mark-far"]
   ["--walk-streambed" "--walk-streambed-far"]])

(def palette-width
  "Sixteen columns, and not all of them used. A power of two, because a texture
  that is not one has rules about how it may be sampled and there is no
  reason to go and learn them for this."
  16)

;; ---------------------------------------------------------------------
;; Colour, read back out of the stylesheet
;; ---------------------------------------------------------------------

(defn css-var
  "What the stylesheet says a custom property is, or the fallback. The
  page is the palette; nothing in here decides what a colour looks
  like."
  [node property fallback]
  (or (try
        (let [declared (.getPropertyValue (js/getComputedStyle node) property)]
          (when-not (str/blank? declared) (str/trim declared)))
        (catch :default _ nil))
      fallback))

(defn rgb
  "#rrggbb to [r g b] in 0..1. Anything else gives mid grey rather than
  throwing, so a missing custom property does not take the page down."
  [hex]
  (if (and hex (= "#" (subs hex 0 1)) (= 7 (count hex)))
    [(/ (js/parseInt (subs hex 1 3) 16) 255.0)
     (/ (js/parseInt (subs hex 3 5) 16) 255.0)
     (/ (js/parseInt (subs hex 5 7) 16) 255.0)]
    [0.5 0.5 0.5]))

(def swatches
  "Every colour the renderer asks for by name, and what to fall back on
  when the stylesheet has not got it."
  {:sky      ["--walk-sky" "#6d7f95"]
   :haze     ["--walk-haze" "#97a1ab"]
   :sun      ["--walk-sun" "#fff4dc"]
   :face     ["--walk-face" "#8d3f34"]
   :brick-a  ["--walk-brick-a" "#8d3f34"]
   :brick-b  ["--walk-brick-b" "#733029"]
   :brick-c  ["--walk-brick-c" "#a35a48"]
   :mortar   ["--walk-mortar" "#b9b0a2"]
   :roof     ["--walk-roof" "#43423f"]
   :leaf     ["--walk-leaf" "#3f6b34"]
   :leaf-lit ["--walk-leaf-lit" "#5c8f45"]
   :bark     ["--walk-bark" "#4a3a2c"]
   :bush     ["--walk-bush" "#3a5f30"]
   :bush-lit ["--walk-bush-lit" "#557f3f"]
   :water    ["--walk-water" "#3c6f86"]
   :foam     ["--walk-foam" "#d8e6ec"]
   :stone    ["--walk-stone" "#9a958a"]
   :post     ["--walk-post" "#5b5a55"]
   :slate    ["--walk-slate" "#e8e6e0"]
   :glass    ["--walk-glass" "#bed6e0"]
   :lamp     ["--walk-lamp" "#7fb2e0"]
   :lamp-lit ["--walk-lamp-lit" "#f2c14a"]})

(defn read-swatches
  [node]
  (into {} (map (fn [[swatch [property fallback]]]
                  [swatch (rgb (css-var node property fallback))])
                swatches)))

(defn palette-bytes
  "The palette strip: `palette-width` columns by two rows, near shades
  on the top row and far shades on the bottom."
  [node]
  (let [data (js/Uint8Array. (* palette-width 2 4))
        put! (fn [col row [r g b]]
               (let [at (* 4 (+ (* row palette-width) col))]
                 (aset data at (js/Math.round (* 255 r)))
                 (aset data (+ at 1) (js/Math.round (* 255 g)))
                 (aset data (+ at 2) (js/Math.round (* 255 b)))
                 (aset data (+ at 3) 255)))]
    (doseq [[col [near far]] (map-indexed vector palette-slots)]
      (put! col 0 (rgb (css-var node near "#808080")))
      (put! col 1 (rgb (css-var node far "#404040"))))
    data))

;; ---------------------------------------------------------------------
;; Where the ground is
;; ---------------------------------------------------------------------

(defn- within?
  [rect px py]
  (let [{:keys [x y w h]} rect]
    (and rect (>= px x) (<= px (+ x w)) (>= py y) (<= py (+ y h)))))

(defn- inset-of
  "How many feet in from its own nearest edge a point is."
  [{:keys [x y w h]} px py]
  (min (- px x) (- (+ x w) px) (- py y) (- (+ y h) py)))

(def floor-of
  "What the bottom of a shaped piece of ground is made of. A brook's
  floor is silt: the blue belongs to the water lying on top of it, and
  painting the bed that same blue is why the surface looked like there
  was not one. There was. It was one shade of water over another."
  {:water :streambed :fill :fill})

(def shaped
  "The materials that are not flat, and which way each one goes.

  Nothing is tagged in the plan for this. A brook is shaped because it
  is water, and a landfill is shaped because it is fill, and asking the
  surface what it is made of is one question with one answer. It used to
  be a `:channel?` key set in campus.clj, carried through a select-keys
  in walk.clj, put in the mount map under its own name and read back
  here, which is four places to write it down and four places to forget
  to."
  {:fill :up :water :down})

(defn- relief
  "How far off the flat campus one shaped rectangle is at a point. A
  landfill and a brook are the same arithmetic with the sign turned
  round: one is a hill somebody piled and the other is a hill somebody
  dug. Both come back to nothing at their own edges, so each meets the
  lawn without a step."
  [rect surface settings px py]
  (let [inset (inset-of rect px py)]
    (case surface
      :fill (let [{:keys [peak roughness]} (:fill settings)
                  skirt (ease (/ inset 26.0))
                  lumps (+ (* 0.55 (rolling px py 48))
                           (* 0.30 (rolling px py 19))
                           (* 0.15 (rolling px py 7)))]
              (* peak skirt (+ 0.45 (* roughness lumps))))
      :water (let [{:keys [depth bank]} (:water settings)
                   dish (ease (/ inset (max 1.0 bank)))
                   ripple (* 0.12 (- (rolling px py 23) 0.5))]
               (- (* depth dish (+ 0.94 ripple))))
      0.0)))

(defn showing?
  "Whether a shaped surface is being dug or piled at all. A reader who
  turns the Fill off gets flat ground where it was, not a hole."
  [settings surface]
  (boolean (get-in settings [surface :show])))

(defn- overlapping?
  [one other]
  (not (or (<= (+ (:x one) (:w one)) (:x other))
           (<= (+ (:x other) (:w other)) (:x one))
           (<= (+ (:y one) (:h one)) (:y other))
           (<= (+ (:y other) (:h other)) (:y one)))))

(defn terrain
  "The shaped ground, and for each piece of it only the things laid over
  it. Worked out once when the settings are run and handed to everything
  that asks how high the ground is.

  It used to be worked out per call, and the call is per vertex: the
  brook alone is four thousand of them, each walking two hundred
  rectangles to find the three roads that cross it. That is most of what
  a reader was waiting for. A road crossing a brook is a bridge, and the
  ground on a bridge is the bridge."
  [{:keys [surfaces]} settings]
  (let [above (filterv (fn [rect] (pos? (:layer rect 0))) surfaces)]
    (mapv (fn [rect]
            (assoc rect :bridges
                   (filterv (fn [over] (overlapping? rect over)) above)))
          (filter (fn [rect]
                    (and (shaped (:surface rect))
                         (showing? settings (:surface rect))))
                  surfaces))))

(defn round?
  [box]
  (= :round (:shape box)))

(defn ground-height
  "How far the ground at a point is above or below the flat campus.

  The renderer builds its triangles out of this and the walker takes its
  eye height from it, which is the only way the two can agree about what
  the ground is doing."
  [terrain settings px py]
  (or (some (fn [{:keys [bridges surface] :as rect}]
              (when (within? rect px py)
                (if (some (fn [over] (within? over px py)) bridges)
                  0.0
                  (relief rect surface settings px py))))
            terrain)
      0.0))

;; ---------------------------------------------------------------------
;; Looking up what is underfoot
;;
;; The same coarse grid the old renderer used, kept because the walker
;; still has to answer "what am I standing on" once a frame and there is
;; no reason to ask the graphics card a question ClojureScript can
;; answer in a dozen comparisons.
;; ---------------------------------------------------------------------

(def ^:private bucket 64.0)

(defn- bucket-of
  "Which bucket along one axis a foot position falls in."
  [foot origin]
  (int (/ (- foot origin) bucket)))

(defn index
  "Every surface rectangle filed under the buckets it covers, so working
  out what you are standing on is a look at the handful of rectangles
  near you rather than a walk through all of them.

  A rectangle keeps the order it was laid in, because two of them can
  cover the same spot and both be telling the truth. The later one wins,
  which is the same rule the depth lift enforces in the picture."
  [surfaces]
  (let [laid (vec (keep-indexed
                   (fn [laid-order rect]
                     (when-let [material (get material-index (:surface rect))]
                       {:west (:x rect)
                        :north (:y rect)
                        :east (+ (:x rect) (:w rect))
                        :south (+ (:y rect) (:h rect))
                        :material material
                        :laid-order laid-order}))
                   surfaces))
        origin-x (- (reduce min 0 (map :west laid)) bucket)
        origin-y (- (reduce min 0 (map :north laid)) bucket)
        covered (fn [rect]
                  (for [column (range (bucket-of (:west rect) origin-x)
                                      (inc (bucket-of (:east rect) origin-x)))
                        row (range (bucket-of (:north rect) origin-y)
                                   (inc (bucket-of (:south rect) origin-y)))]
                    [column row]))]
    {:buckets (reduce (fn [buckets rect]
                        (reduce (fn [filed cell]
                                  (update filed cell (fnil conj []) rect))
                                buckets
                                (covered rect)))
                      {}
                      laid)
     :origin-x origin-x
     :origin-y origin-y}))

(defn surface-at
  "The material index under a point, or 0 for open lawn."
  [{:keys [buckets origin-x origin-y]} px py]
  (let [nearby (get buckets [(bucket-of px origin-x) (bucket-of py origin-y)])
        underfoot (filter (fn [{:keys [west north east south]}]
                            (and (>= px west) (< px east)
                                 (>= py north) (< py south)))
                          nearby)]
    (if (seq underfoot)
      (:material (apply max-key :laid-order underfoot))
      0)))

;; ---------------------------------------------------------------------
;; Packing triangles
;; ---------------------------------------------------------------------

(def ^:private quad-order [0 1 2 0 2 3])

(defn- floats
  "The accumulator, handed to the card as the one thing it will take."
  [numbers]
  (js/Float32Array. numbers))

;; ---------------------------------------------------------------------
;; The ground
;;
;; layout: [["a_position" 3] ["a_patch" 4]]
;;
;; a_patch is (material, how far along this rectangle, how far off its
;; middle). The offset is signed rather than absolute, because a signed
;; offset is linear across the rectangle and an absolute one is a fold,
;; and a triangle can only carry the linear kind. The shader takes the
;; absolute value at the far end, where it costs nothing.
;; ---------------------------------------------------------------------

(defn- ground-quad!
  [vertices {:keys [x y w h axis surface style]} z]
  (let [material (get material-index surface 0)
        east-west (= :ew axis)
        crossing (= :crosswalk surface)
        middle (if east-west (+ y (/ h 2.0)) (+ x (/ w 2.0)))
        corners [[x y] [(+ x w) y] [(+ x w) (+ y h)] [x (+ y h)]]]
    (doseq [corner quad-order]
      (let [[px py] (nth corners corner)
            run (cond crossing (if east-west px py)
                      east-west (- px x)
                      :else (- py y))
            off-middle (if east-west (- py middle) (- px middle))]
        (.push vertices px) (.push vertices py) (.push vertices z)
        (.push vertices material) (.push vertices run)
        (.push vertices off-middle) (.push vertices (or style 0))))))

(defn- shaped!
  "A piece of ground that has a shape, as a grid of triangles. Every
  corner gets its height from the same function the walker's own eye
  uses, so the mound and the brook are one piece of arithmetic and
  neither can drift from what you are standing on."
  [vertices ground rect settings lift]
  (let [{:keys [x y w h surface]} rect
        step (max 1.0 (get-in settings [surface :step] 6))
        material (get material-index (get floor-of surface surface) 0)
        across (max 2 (js/Math.ceil (/ w step)))
        down (max 2 (js/Math.ceil (/ h step)))
        at (fn [column row]
             (let [px (+ x (* w (/ column across)))
                   py (+ y (* h (/ row down)))]
               [px py (+ lift (ground-height ground settings px py))]))]
    (dotimes [row down]
      (dotimes [column across]
        (let [corners [(at column row) (at (inc column) row)
                       (at (inc column) (inc row)) (at column (inc row))]]
          (doseq [corner quad-order]
            (let [[px py pz] (nth corners corner)]
              (.push vertices px) (.push vertices py) (.push vertices pz)
              (.push vertices material) (.push vertices 0.0)
              (.push vertices 0.0) (.push vertices 0.0))))))))

(defn- without
  "A rectangle with a hole cut in it, as up to four rectangles. The hole
  is clamped to the rectangle first, so a hole that hangs over an edge
  or misses altogether is not a special case anywhere else."
  [{:keys [x y w h] :as whole} hole]
  (let [east (+ x w)
        south (+ y h)
        cut-west (max x (:x hole))
        cut-north (max y (:y hole))
        cut-east (min east (+ (:x hole) (:w hole)))
        cut-south (min south (+ (:y hole) (:h hole)))]
    (if (or (>= cut-west cut-east) (>= cut-north cut-south))
      [whole]
      (remove (fn [piece] (or (<= (:w piece) 0) (<= (:h piece) 0)))
              [{:x x :y y :w w :h (- cut-north y)}
               {:x x :y cut-south :w w :h (- south cut-south)}
               {:x x :y cut-north :w (- cut-west x) :h (- cut-south cut-north)}
               {:x cut-east :y cut-north :w (- east cut-east)
                :h (- cut-south cut-north)}]))))

(defn ground-mesh
  "Every flat rectangle, the two shaped ones, and enough lawn under the
  lot of it to run out past the draw distance so the horizon is always
  ground meeting sky and never an edge.

  The lawn has the brook cut out of it. It used to be one unbroken quad
  at ground level, which meant the grass and the water were the same
  plane in the same place and the card picked between them per pixel,
  differently every time you moved. A channel that goes down needs the
  grass over it gone, not merely lifted off it."
  [{:keys [surfaces]} {:keys [ground] :as settings} shapes reach]
  (let [vertices (js/Array.)
        lift (:lift ground)
        edge (* reach 4)
        shaped-here (set (map (fn [rect] (dissoc rect :bridges)) shapes))
        sunken (filter (fn [rect] (= :down (shaped (:surface rect)))) shapes)
        horizon {:x (- edge) :y (- edge) :w (* 2 edge) :h (* 2 edge)}
        lawn (reduce (fn [pieces hole]
                       (mapcat (fn [piece] (without piece hole)) pieces))
                     [horizon]
                     sunken)
        total (max 1 (count surfaces))]
    (doseq [piece lawn]
      (ground-quad! vertices (assoc piece :axis :ew :surface :lawn) 0.0))
    ;; Within one layer, rectangles are nudged apart by a fraction of a
    ;; layer step, in the order they were laid. Two roads crossing are
    ;; both layer 2 and were exactly coplanar, so every intersection on
    ;; the campus was a coin toss the card made again each frame. The
    ;; spread stays under one lift, so it cannot reorder the layers.
    (doseq [[order rect] (map-indexed vector surfaces)]
      (when-not (shaped-here rect)
        (ground-quad! vertices rect
                      (* lift (+ (:layer rect 0) (* 0.5 (/ order total)))))))
    (doseq [rect shapes]
      (shaped! vertices shapes rect settings lift))
    (floats vertices)))

;; ---------------------------------------------------------------------
;; Buildings
;;
;; layout: [["a_position" 3] ["a_wall" 2] ["a_normal" 2] ["a_seed" 1]]
;;
;; a_wall is (feet along this wall from the building's own corner, feet
;; up). Courses stagger from the corner, so two segments of one wall can
;; never disagree about where the brick pattern starts, which was a real
;; bug in the old renderer and is now simply not expressible.
;; ---------------------------------------------------------------------

(defn box-top
  [box default]
  (cond
    (:height box) (:height box)
    (:one-story? box) (/ default 2)
    :else default))

(defn face-named
  "One wall of a building, by which side it is. The faces themselves are
  worked out in walk.clj and travel in the plan; there is no second copy
  of that arithmetic here for the two to disagree over."
  [box side]
  (some (fn [face] (when (= side (:side face)) face)) (:faces box)))

(defn wall-mesh
  [{:keys [boxes]} default-top]
  (let [vertices (js/Array.)]
    (doseq [box boxes
            :when (not (round? box))
            {:keys [a b normal length slot]} (:faces box)]
      (let [[from-x from-y] a
            [to-x to-y] b
            [facing-x facing-y] normal
            ;; Every face gets a number of its own, out of where its
            ;; near corner stands and which way it faces. Without it,
            ;; a_wall starts at zero on every wall in the campus and
            ;; every wall is laid brick for brick the same as the one
            ;; round the corner from it.
            seed (+ (* 0.017 from-x) (* 0.031 from-y) (* 7.3 slot))
            top (box-top box default-top)
            corners [[from-x from-y 0.0 0.0] [to-x to-y 0.0 length]
                     [to-x to-y top length] [from-x from-y top 0.0]]]
        (doseq [corner quad-order]
          (let [[px py pz along-wall] (nth corners corner)]
            (.push vertices px) (.push vertices py) (.push vertices pz)
            (.push vertices along-wall) (.push vertices pz)
            (.push vertices facing-x) (.push vertices facing-y)
            (.push vertices seed)))))
    (floats vertices)))

;; ---------------------------------------------------------------------
;; Doors and windows
;;
;; layout: [["a_position" 3] ["a_uv" 2] ["a_normal" 2]]
;; ---------------------------------------------------------------------

(def ^:private proud
  "How far a door stands out from the wall it is in. Enough that the
  depth buffer never has to choose between them, little enough that
  nobody notices from the side."
  0.09)

(defn- panel!
  [vertices {:keys [a b normal length]} where-along width height z-middle]
  (let [[from-x from-y] a
        [to-x to-y] b
        [facing-x facing-y] normal
        step-x (/ (- to-x from-x) length)
        step-y (/ (- to-y from-y) length)
        half-width (/ width 2.0)
        middle-foot (* where-along length)
        near-foot (- middle-foot half-width)
        far-foot (+ middle-foot half-width)
        bottom (- z-middle (/ height 2.0))
        top (+ z-middle (/ height 2.0))
        at (fn [foot z] [(+ from-x (* step-x foot) (* facing-x proud))
                         (+ from-y (* step-y foot) (* facing-y proud))
                         z])
        corners [[(at near-foot bottom) 0.0 0.0]
                 [(at far-foot bottom) 1.0 0.0]
                 [(at far-foot top) 1.0 1.0]
                 [(at near-foot top) 0.0 1.0]]]
    (doseq [corner quad-order]
      (let [[[px py pz] image-u image-v] (nth corners corner)]
        (.push vertices px) (.push vertices py) (.push vertices pz)
        (.push vertices image-u) (.push vertices image-v)
        (.push vertices facing-x) (.push vertices facing-y)))))

(defn- name-seed
  "A building's name as one number, so its windows follow from what it
  is called and stay where they were put."
  [building-name]
  (reduce + 0 (map (fn [at] (.charCodeAt building-name at))
                   (range (count building-name)))))

(defn window-plan
  "0, 1 or 2 windows for one wall of one building, each a {:u :floor}.
  Deterministic from the building's own name, so the same building has
  the same windows every time rather than a new set every frame."
  [box face]
  (let [seed (+ (name-seed (:name box)) (* 7 (:slot face 0)))
        roll (fn [nudge] (spot (+ seed nudge) 4001))
        how-many (let [first-roll (roll 0)]
                   (cond (< first-roll 0.30) 0
                         (< first-roll 0.70) 1
                         :else 2))]
    (vec
     (for [which (range how-many)]
       {:u (if (= how-many 2)
             (if (zero? which)
               (+ 0.12 (* 0.25 (roll (+ 17 which))))
               (+ 0.63 (* 0.25 (roll (+ 17 which)))))
             (+ 0.35 (* 0.30 (roll 17))))
        :floor (if (< (roll (+ 23 (* which 5))) 0.5) 0 1)}))))

(defn- doorways-of
  "Every face a building has a door in. Most have one; the library has
  two, and nothing else in here has to know which is which."
  [box]
  (if (round? box)
    (:faces box)
    (keep (fn [side] (face-named box side))
          (or (:doors box) [(:door box)]))))

(defn door-mesh
  [{:keys [boxes]} {:keys [doors buildings]}]
  (let [vertices (js/Array.)
        {:keys [width height sill] :or {sill 0.0}} doors]
    (when (:show doors)
      (doseq [box boxes
              face (doorways-of box)]
        (panel! vertices face 0.5 width height (+ sill (/ height 2.0)))))
    (floats vertices)))

(defn window-mesh
  [{:keys [boxes]} {:keys [windows buildings]}]
  (let [vertices (js/Array.)
        {:keys [width height]} windows
        default-top (:height buildings)
        storey (/ default-top 2.0)]
    (when (:show windows)
      (doseq [box boxes
              :when (not (round? box))
              face (:faces box)
              {:keys [u floor]} (window-plan box face)]
        (let [top (box-top box default-top)
              middle (+ (* floor storey) (/ storey 2.0))]
          (when (<= (+ middle (/ height 2.0)) top)
            (panel! vertices face u width height middle)))))
    (floats vertices)))

;; ---------------------------------------------------------------------
;; Trees and hedges
;;
;; layout: [["a_root" 3] ["a_corner" 2] ["a_shape" 3]]
;;
;; Four corners and a seed. The vertex shader turns the quad to face
;; you and the fragment shader grows the thing on it.
;; ---------------------------------------------------------------------

(def ^:private billboard-corners [[-1 0] [1 0] [1 1] [-1 1]])

(defn billboard-mesh
  [plants spread]
  (let [vertices (js/Array.)]
    (doseq [{:keys [x y radius height]} plants]
      (let [half-width (* radius spread)
            seed (+ (* 0.017 x) (* 0.031 y))]
        (doseq [corner quad-order]
          (let [[sideways upward] (nth billboard-corners corner)]
            (.push vertices x) (.push vertices y) (.push vertices 0.0)
            (.push vertices sideways) (.push vertices upward)
            (.push vertices half-width) (.push vertices height)
            (.push vertices seed)))))
    (floats vertices)))

;; ---------------------------------------------------------------------
;; Everything else with a shape
;;
;; layout: [["a_position" 3] ["a_colour" 3] ["a_normal" 3] ["a_water" 2]]
;;
;; a_water is (ripple spacing, how opaque). Nearly everything here sets
;; it to (0, 1) and means "plain and solid"; the pool sets the first
;; number and the water bell sets the second.
;; ---------------------------------------------------------------------

(defn- vertex!
  "One vertex of the solid layout, packed in declaration order. Every
  push in this section goes through here, so the order the numbers land
  in is written down once."
  [vertices [px py pz] [red green blue] [normal-x normal-y normal-z] ripple alpha]
  (.push vertices px) (.push vertices py) (.push vertices pz)
  (.push vertices red) (.push vertices green) (.push vertices blue)
  (.push vertices normal-x) (.push vertices normal-y) (.push vertices normal-z)
  (.push vertices ripple) (.push vertices alpha))

(defn- quad!
  [vertices corners colour normal ripple alpha]
  (doseq [corner quad-order]
    (vertex! vertices (nth corners corner) colour normal ripple alpha)))

(defn- box!
  "A rectangular block, four sides and a lid. The underside is never
  seen and is never built."
  [vertices x y z w h top colour]
  (let [east (+ x w)
        south (+ y h)
        lid (+ z top)]
    (quad! vertices [[x y z] [east y z] [east y lid] [x y lid]]
           colour [0 -1 0] 0.0 1.0)
    (quad! vertices [[east south z] [x south z] [x south lid] [east south lid]]
           colour [0 1 0] 0.0 1.0)
    (quad! vertices [[x south z] [x y z] [x y lid] [x south lid]]
           colour [-1 0 0] 0.0 1.0)
    (quad! vertices [[east y z] [east south z] [east south lid] [east y lid]]
           colour [1 0 0] 0.0 1.0)
    (quad! vertices [[x y lid] [east y lid] [east south lid] [x south lid]]
           colour [0 0 1] 0.0 1.0)))

(defn- arc-points
  "Points along part of a circle, as [x y angle], with one more point
  than there are steps so consecutive pairs cover the whole arc."
  [middle-x middle-y radius from-angle to-angle steps]
  (let [span (- to-angle from-angle)]
    (mapv (fn [step]
            (let [angle (+ from-angle (* span (/ step steps)))]
              [(+ middle-x (* radius (js/Math.cos angle)))
               (+ middle-y (* radius (js/Math.sin angle)))
               angle]))
          (range (inc steps)))))

(defn- ring-points
  "A closed ring, which is an arc that happens to go all the way round."
  [middle-x middle-y radius steps]
  (arc-points middle-x middle-y radius 0.0 6.2832 steps))

(defn- tube!
  "A cylinder wall between two heights, facing outward or in."
  [vertices middle-x middle-y radius z-bottom z-top steps colour outward?]
  (let [ring (ring-points middle-x middle-y radius steps)
        facing (if outward? 1.0 -1.0)]
    (dotimes [step steps]
      (let [[from-x from-y from-angle] (nth ring step)
            [to-x to-y to-angle] (nth ring (inc step))
            out-at (fn [angle] [(* facing (js/Math.cos angle))
                                (* facing (js/Math.sin angle))
                                0])
            corners [[from-x from-y z-bottom] [to-x to-y z-bottom]
                     [to-x to-y z-top] [from-x from-y z-top]]
            normals [(out-at from-angle) (out-at to-angle)
                     (out-at to-angle) (out-at from-angle)]]
        (doseq [corner quad-order]
          (vertex! vertices (nth corners corner) colour (nth normals corner)
                   0.0 1.0))))))

(defn- arc-wall!
  "A stretch of curved wall standing between two heights. Used for the
  library's glass, which is not a full ring: there is a doorway cut out
  of it at each end."
  [vertices ring z-bottom z-top colour alpha]
  (dotimes [step (dec (count ring))]
    (let [[from-x from-y from-angle] (nth ring step)
          [to-x to-y to-angle] (nth ring (inc step))
          out-at (fn [angle] [(js/Math.cos angle) (js/Math.sin angle) 0])
          corners [[from-x from-y z-bottom] [to-x to-y z-bottom]
                   [to-x to-y z-top] [from-x from-y z-top]]
          normals [(out-at from-angle) (out-at to-angle)
                   (out-at to-angle) (out-at from-angle)]]
      (doseq [corner quad-order]
        (vertex! vertices (nth corners corner) colour (nth normals corner)
                 0.0 alpha)))))

(defn- dome!
  "A quarter of an ellipsoid, in bands, sitting on the top of a wall.

  The top band's upper radius is nothing, so its quads collapse into the
  triangles a fan would have given, without needing a fan."
  [vertices middle-x middle-y radius z-base height steps rings colour]
  (let [quarter (/ 3.14159265 2.0)
        profile (fn [fraction]
                  (let [turn (* fraction quarter)]
                    {:radius (* radius (js/Math.cos turn))
                     :z (+ z-base (* height (js/Math.sin turn)))
                     :lean (js/Math.sin turn)
                     :rise (js/Math.cos turn)}))]
    (dotimes [band rings]
      (let [low (profile (/ band rings))
            high (profile (/ (inc band) rings))
            low-ring (ring-points middle-x middle-y (:radius low) steps)
            high-ring (ring-points middle-x middle-y (:radius high) steps)
            ;; The normal of a squashed dome leans out by the height and
            ;; up by the radius, which is the profile's own tangent
            ;; turned a quarter turn.
            skin (fn [{:keys [lean rise]} angle]
                   (let [flat (* height rise)
                         up (* radius lean)
                         length (js/Math.sqrt (+ (* flat flat) (* up up)))
                         scale (/ 1.0 (max length 0.0001))]
                     [(* flat scale (js/Math.cos angle))
                      (* flat scale (js/Math.sin angle))
                      (* up scale)]))]
        (dotimes [step steps]
          (let [[low-from-x low-from-y low-from-angle] (nth low-ring step)
                [low-to-x low-to-y low-to-angle] (nth low-ring (inc step))
                [high-to-x high-to-y] (nth high-ring (inc step))
                [high-from-x high-from-y] (nth high-ring step)
                corners [[low-from-x low-from-y (:z low)]
                         [low-to-x low-to-y (:z low)]
                         [high-to-x high-to-y (:z high)]
                         [high-from-x high-from-y (:z high)]]
                normals [(skin low low-from-angle) (skin low low-to-angle)
                         (skin high low-to-angle) (skin high low-from-angle)]]
            (doseq [corner quad-order]
              (vertex! vertices (nth corners corner) colour
                       (nth normals corner) 0.0 1.0))))))))

(defn- disc!
  "A flat circle, as a fan from its own middle."
  [vertices middle-x middle-y radius z steps colour ripple]
  (let [ring (ring-points middle-x middle-y radius steps)]
    (dotimes [step steps]
      (let [[from-x from-y _] (nth ring step)
            [to-x to-y _] (nth ring (inc step))]
        (vertex! vertices [middle-x middle-y z] colour [0 0 1] ripple 1.0)
        (vertex! vertices [from-x from-y z] colour [0 0 1] ripple 1.0)
        (vertex! vertices [to-x to-y z] colour [0 0 1] ripple 1.0)))))

(defn- annulus!
  "A flat ring, in wedges, each wedge shifted a little off the base
  colour. A paved circle is laid in pieces and pieces do not match."
  [vertices middle-x middle-y inner outer z steps colour]
  (let [ring (ring-points middle-x middle-y 1.0 steps)]
    (dotimes [step steps]
      (let [[_ _ from-angle] (nth ring step)
            [_ _ to-angle] (nth ring (inc step))
            shift (* 0.06 (- (spot step 6301) 0.5))
            shade (mapv (fn [channel] (max 0.0 (min 1.0 (+ channel shift))))
                        colour)
            at (fn [radius angle]
                 [(+ middle-x (* radius (js/Math.cos angle)))
                  (+ middle-y (* radius (js/Math.sin angle)))
                  z])]
        (quad! vertices
               [(at inner from-angle) (at outer from-angle)
                (at outer to-angle) (at inner to-angle)]
               shade [0 0 1] 0.0 1.0)))))

(defn crest
  "How high the water bell is at a fraction of the way out. The same
  curve the mist follows, which is why the two look like one thing."
  [nozzle rise t]
  (+ nozzle (* rise (- (* 2 t) (* 2.6 t t)))))

(defn fountain-parts
  "Every number the fountain is built from, worked out once so the
  solid mesh, the water sheet and the mist all read the same one."
  [{:keys [x y radius rim water pedestal spray]}]
  (let [{prad :radius phigh :height} pedestal
        {:keys [width height density] :or {width 1.0 height 1.0 density 1.0}} spray
        kerb (+ water rim)
        inner (- radius rim)
        nozzle (+ water phigh)]
    {:x x :y y :radius radius :inner inner :kerb kerb :water water
     :nozzle nozzle :pedestal-radius prad
     :rise (* phigh 0.55 (max 0.4 (min 2.5 height)))
     :reach (* inner 0.72 (max 0.4 (min 2.5 width)))
     :density (max 0.2 (min 2.5 density))}))

(defn fountain-mesh
  "Stone, kerb, pool, pedestal. Solid, opaque, and never redrawn."
  [fountain swatches ripples]
  (let [{:keys [x y radius inner kerb water nozzle pedestal-radius]} (fountain-parts fountain)
        steps 28
        stone (:stone swatches)
        pool (:water swatches)
        foam (:foam swatches)
        vertices (js/Array.)]
    (tube! vertices x y radius 0.0 kerb steps stone true)
    (annulus! vertices x y inner radius kerb steps stone)
    (tube! vertices x y inner water kerb steps stone false)
    (disc! vertices x y inner water steps pool (/ inner (max 1 ripples)))
    (tube! vertices x y pedestal-radius water nozzle 16 stone true)
    (disc! vertices x y pedestal-radius nozzle 16 stone 0.0)
    (tube! vertices x y (* pedestal-radius 0.45) nozzle (+ nozzle 0.9) 12 foam true)
    (disc! vertices x y (* pedestal-radius 0.45) (+ nozzle 0.9) 12 foam 0.0)
    (floats vertices)))

(defn water-mesh
  "The top of every body of water on the campus, as a grid of triangles
  at the waterline. Its ripple spacing is written negative, which is how
  the vertex shader knows to make it move: everything else with a
  spacing of nothing sits still, and a brook that sits still is a road.

  Drawn with the rest of the see-through things, so you look at the
  surface and through it to the bottom at the same time, which is what
  water does."
  [shapes settings swatches]
  (let [vertices (js/Array.)
        colour (:water swatches)
        {:keys [waterline step alpha] :or {waterline -0.6 step 6 alpha 0.72}}
        (:water settings)]
    (doseq [{:keys [x y w h surface bridges]} shapes
            :when (= :down (shaped surface))]
      (let [across (max 2 (js/Math.ceil (/ w step)))
            down (max 2 (js/Math.ceil (/ h step)))
            at (fn [column row]
                 [(+ x (* w (/ column across)))
                  (+ y (* h (/ row down)))
                  waterline])]
        (dotimes [row down]
          (dotimes [column across]
            (let [corners [(at column row) (at (inc column) row)
                           (at (inc column) (inc row)) (at column (inc row))]
                  [middle-x middle-y _] (at (+ column 0.5) (+ row 0.5))]
              (when-not (some (fn [over] (within? over middle-x middle-y))
                              bridges)
                (doseq [corner quad-order]
                  (vertex! vertices (nth corners corner) colour [0 0 1]
                           -1.0 alpha))))))))
    (floats vertices)))

(defn rotunda-mesh
  "Everything of the library you cannot see through: the floor, the four
  blocks standing on it, and the lid.

  The blocks are the reason the glass works. A building you can see
  clean through is a fish tank; a building with something solid in the
  middle of it is a building, and walking round it changes what you can
  see of the inside, which is the only thing a glass wall is for."
  [{:keys [boxes]} settings swatches]
  (let [vertices (js/Array.)
        {:keys [show wall dome facets rings]
         :or {show true wall 15 dome 12 facets 48 rings 7}} (:rotunda settings)
        slate (:slate swatches)
        floor (:stone swatches)]
    (when show
      (doseq [box boxes
              :when (round? box)]
        (let [[middle-x middle-y] (:middle box)
              radius (:radius box)]
          (disc! vertices middle-x middle-y radius 0.3 facets floor 0.0)
          (doseq [{:keys [x y w h height]} (:rooms box)]
            (box! vertices x y 0.3 w h (or height 12) slate))
          (dome! vertices middle-x middle-y radius wall dome facets rings
                 slate))))
    (floats vertices)))

(defn glass-mesh
  "The wall, which is all of it that is not a doorway.

  Two arcs rather than a ring: the span a door stands in is left out, so
  the way in is a gap in the glass and not a hole cut in it afterwards."
  [{:keys [boxes]} settings swatches]
  (let [vertices (js/Array.)
        {:keys [show wall facets glass door]
         :or {show true wall 15 facets 48 glass 0.26 door 9}}
        (:rotunda settings)
        colour (:glass swatches)]
    (when show
      (doseq [box boxes
              :when (round? box)]
        (let [[middle-x middle-y] (:middle box)
              radius (:radius box)
              gap (/ (/ door 2.0) (max radius 1.0))
              spans (if (< (count (:faces box)) 2)
                      [[gap (- 6.2832 gap)]]
                      [[gap (- 3.14159265 gap)]
                       [(+ 3.14159265 gap) (- 6.2832 gap)]])]
          (doseq [[from-angle to-angle] spans]
            (let [share (/ (- to-angle from-angle) 6.2832)
                  steps (max 4 (js/Math.ceil (* facets share)))]
              (arc-wall! vertices
                         (arc-points middle-x middle-y radius
                                     from-angle to-angle steps)
                         0.3 wall colour glass))))))
    (floats vertices)))

(defn step-mesh
  "A stone step at every door. A building has one, and it also covers
  the daylight under a door picture that has transparent space along its
  own bottom edge, which no amount of arithmetic in here can measure."
  [{:keys [boxes]} {:keys [doors]} swatches]
  (let [vertices (js/Array.)
        {:keys [show width]} doors
        {:keys [rise tread spread] :or {rise 0.6 tread 3.0 spread 1.5}}
        (:step doors)
        colour (:stone swatches)]
    (when (and show (get-in doors [:step :show] true))
      (doseq [box boxes
              face (doorways-of box)]
        (let [{:keys [a b normal length]} face
              [from-x from-y] a
              [to-x to-y] b
              [away-x away-y] normal
              along-x (/ (- to-x from-x) length)
              along-y (/ (- to-y from-y) length)
              half (+ (/ width 2.0) spread)
              middle-x (+ from-x (* along-x (/ length 2.0)) (* away-x tread 0.5))
              middle-y (+ from-y (* along-y (/ length 2.0)) (* away-y tread 0.5))
              reach-x (max (js/Math.abs (* along-x half))
                           (js/Math.abs (* away-x tread 0.5)))
              reach-y (max (js/Math.abs (* along-y half))
                           (js/Math.abs (* away-y tread 0.5)))]
          (box! vertices (- middle-x reach-x) (- middle-y reach-y) 0.0
                (* 2 reach-x) (* 2 reach-y) rise colour))))
    (floats vertices)))

(defn sheet-mesh
  "The bell of water: a curve turned all the way round, fading out as it
  falls away from the nozzle."
  [fountain swatches]
  (let [{:keys [x y nozzle rise reach]} (fountain-parts fountain)
        steps 24
        rungs 12
        foam (:foam swatches)
        vertices (js/Array.)
        at (fn [fraction angle] [(+ x (* reach fraction (js/Math.cos angle)))
                                 (+ y (* reach fraction (js/Math.sin angle)))
                                 (crest nozzle rise fraction)])
        fade (fn [fraction] (* 0.85 (- 1.0 (* fraction fraction))))]
    (dotimes [step steps]
      (let [from-angle (* 6.2832 (/ step steps))
            to-angle (* 6.2832 (/ (inc step) steps))]
        (dotimes [rung rungs]
          (let [nearer (/ rung rungs)
                further (/ (inc rung) rungs)
                corners [(at nearer from-angle) (at nearer to-angle)
                         (at further to-angle) (at further from-angle)]
                alphas [(fade nearer) (fade nearer)
                        (fade further) (fade further)]]
            (doseq [corner quad-order]
              (vertex! vertices (nth corners corner) foam [0 0 1] 0.0
                       (nth alphas corner)))))))
    (floats vertices)))

(defn post-mesh
  "Help posts: a shaft and a lamp on top of it. Small enough that a box
  is a better answer than a billboard, and boxes never turn to look at
  you when you walk round them."
  [{:keys [posts]}
   {:keys [height radius lamp] :or {height 8 radius 0.4 lamp 1.2}}
   swatches checked]
  (let [vertices (js/Array.)
        shaft-colour (:post swatches)
        waiting (:lamp swatches)
        answered (:lamp-lit swatches)]
    (doseq [[at [px py]] (map-indexed vector posts)]
      (box! vertices (- px radius) (- py radius) 0.0
            (* 2 radius) (* 2 radius) height shaft-colour)
      (box! vertices (- px (/ lamp 2)) (- py (/ lamp 2)) height
            lamp lamp lamp
            (if (contains? checked at) answered waiting)))
    (floats vertices)))

(defn roof-mesh
  [{:keys [boxes]} default-top swatches]
  (let [vertices (js/Array.)
        colour (:roof swatches)]
    (doseq [{:keys [x y w h] :as box} boxes
            :when (not (round? box))]
      (let [top (box-top box default-top)]
        (quad! vertices
               [[x y top] [(+ x w) y top] [(+ x w) (+ y h) top] [x (+ y h) top]]
               colour [0 0 1] 0.0 1.0)))
    (floats vertices)))

;; ---------------------------------------------------------------------
;; Points: cloud and mist
;;
;; Both are static buffers of seeds. The clock is a uniform, so nothing
;; is ever uploaded again once these exist, and standing still costs
;; the same as walking.
;; ---------------------------------------------------------------------

(defn cloud-lobes
  "Each cloud is several soft points rather than one: a bright middle
  and a scatter of dimmer ones around it, mostly sideways, because a
  cloud is wider than it is tall. The overlaps are what read as lumpy."
  [{:keys [cover rows size lobes] :or {cover 0.42 rows 9 size 0.19 lobes 10}} fov]
  (let [lobe-seeds (js/Array.)
        spread (js/Math.tan (/ fov 2))]
    (dotimes [row rows]
      (let [toward-horizon (/ (+ row 0.5) rows)
            lift (+ 0.08 (* 0.66 (- 1.0 toward-horizon)))
            scale (+ 0.4 (* 0.8 (- 1.0 toward-horizon)))
            across (max 6 (int (/ 20 scale)))
            brightness (* 0.20 (- 1.0 (* 0.5 toward-horizon)))]
        (dotimes [cell across]
          (let [occupied (spot cell (+ row 7))]
            (when (< occupied cover)
              (let [turn (* 6.2832 (/ (+ cell occupied) across))
                    wide (* size scale spread 1.5)]
                (dotimes [lobe lobes]
                  (let [middle? (zero? lobe)
                        which-way (spot (+ cell 501 (* lobe 37)) (+ row 900))
                        how-far (spot (+ cell 907 (* lobe 53)) (+ row 331))
                        how-big (spot (+ cell 613 (* lobe 19)) (+ row 771))
                        angle (* 6.2832 which-way)
                        off-middle (if middle? 0.0 (* wide 0.85 how-far))
                        lobe-turn (+ turn (* off-middle (js/Math.cos angle)))
                        lobe-lift (- lift (js/Math.abs
                                           (* off-middle 0.42 (js/Math.sin angle))))
                        girth (* wide (if middle?
                                        (+ 0.9 (* 0.4 how-big))
                                        (+ 0.4 (* 0.5 how-big))))
                        alpha (* brightness (if middle?
                                              1.0
                                              (+ 0.45 (* 0.5 how-big))))]
                    (.push lobe-seeds lobe-turn)
                    (.push lobe-seeds (max 0.02 lobe-lift))
                    (.push lobe-seeds girth)
                    (.push lobe-seeds alpha)))))))))
    (floats lobe-seeds)))

(def mist-life
  "How long a drop of mist lives, in milliseconds."
  650)

(defn mist-drops
  "Streams of drops thrown out of the nozzle, plus a few thrown straight
  up out of it.

  The jet used to be a cone of triangles standing over the fountain,
  which from any angle read as a stationary spike, because that is what
  it was. A jet is water in the air. The vertex shader already knows how
  to throw water in the air, and a stream whose reach is almost nothing
  goes almost straight up; the shader trades that missing reach for
  height, so the narrow ones climb."
  [density jets]
  (let [streams (max 4 (int (* 58 density)))
        upward (max 0 (int (* 12 jets)))
        drops-per-stream 13
        drop-seeds (js/Array.)
        throw! (fn [stream total angle reach-scale]
                 (dotimes [drop drops-per-stream]
                   (let [ordinal (+ (* stream drops-per-stream) drop)]
                     (.push drop-seeds angle)
                     (.push drop-seeds reach-scale)
                     (.push drop-seeds (spot ordinal (+ 511 total)))
                     (.push drop-seeds (+ 1.0 (* 0.37 ordinal))))))]
    (dotimes [stream streams]
      (let [wobble (* 0.08 (- (spot stream 401) 0.5))]
        (throw! stream streams
                (+ (* 6.2832 (/ stream streams)) wobble)
                (+ 0.85 (* 0.3 (spot stream 733))))))
    (dotimes [stream upward]
      (throw! (+ streams stream) upward
              (* 6.2832 (spot stream 877))
              (* 0.06 (+ 0.4 (spot stream 313)))))
    (floats drop-seeds)))

;; ---------------------------------------------------------------------
;; The screen-covering rectangle the sky is painted on
;; ---------------------------------------------------------------------

(def screen-mesh
  (js/Float32Array. #js [-1 -1  1 -1  1 1  -1 -1  1 1  -1 1]))