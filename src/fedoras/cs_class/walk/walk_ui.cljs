(ns fedoras.cs-class.walk.walk-ui
  "First-person walkthrough of the quad.

  This file is the part that changes while you are walking, and there
  is deliberately not much of it. It holds where you are, what keys are
  down, and the order the programs run in for one frame. Everything
  else was decided before you pressed a key: the plan is in walk.clj,
  the triangles are in walk_world.cljs, the look is in walk_shaders.cljs
  and walk.css, and the machinery is in walk_gl.cljs.

  ONE FRAME, IN ORDER. Sky first, with no depth test, so it can never
  hide anything. Clouds on top of it, added rather than painted over,
  which is what makes them look lit from behind. Then the depth buffer
  comes on and everything solid goes down in whatever order is
  convenient, because the card is sorting it out per pixel now and the
  painter's algorithm and its sorting are gone. Then the things that
  see through themselves: doors, the water bell, the mist. Those go
  last, with depth testing still on and depth writing off, which is the
  whole of what used to be four hand-written occlusion tests around the
  fountain."
  (:require [clojure.string :as str]
            [fedoras.reader :as reader]
            [fedoras.ui.widget-ui :as widget]
            [fedoras.cs-class.walk.walk-gl :as gl]
            [fedoras.cs-class.walk.walk-world :as world]))

(def default-settings
  {:resolution [420 236]
   :fov 1.05
   :eye-height 5.6
   :draw 520
   :bound 1000
   :walk-speed 2.6
   :back-speed 2.0
   :side-speed 2.0
   :turn-speed 0.045
   :sun {:azimuth 2.2 :height 0.30 :size 44 :ambient 0.62}
   :ground {:slab 4 :joint 0.5 :tuft 4 :patch 17 :grain 12
            :mow 9 :bay 9 :tie 2.4 :lift 0.05}
   :haze {:clarity 1.6 :horizon 0.82}
   :fill {:show true :peak 16 :roughness 0.9 :step 5}
   :water {:show true :depth 5 :bank 14 :step 6
           :waterline -0.6 :alpha 0.72 :swell 0.35 :wavelength 9
           :breath 12}
   :brick {:width 2.2 :height 0.9 :mortar 0.14 :streak 9 :detail 150}
   :doors {:show true :width 7 :height 12 :sill -0.8
           :step {:show true :rise 0.6 :tread 3.0 :spread 1.5}}
   :windows {:show true :width 3.4 :height 3.4}
   :buildings {:height 30}
   :rotunda {:show true :wall 15 :dome 12 :facets 48 :rings 7
             :glass 0.26 :door 9}
   :fountain {:show true :animate true :ripples 3
              :x 455 :y 330 :radius 16 :rim 1.4 :water 1.6
              :pedestal {:radius 2.4 :height 4.5}
              :spray {:width 1.0 :height 1.0 :density 1.0 :jets 1.0}}
   :trees {:show true :spread 1.15 :trunk 0.75 :fork 0.34
           :taper 0.45 :limbs 0.5}
   :bushes {:show true :spread 1.15}
   :posts {:show true :height 8 :radius 0.4 :lamp 1.2 :reach 9}
   :clouds {:show true :cover 0.42 :drift 0.00004 :rows 9 :size 0.19
            :lobes 10 :ragged 0.22 :flat 0.6}
   :wraith {:show false :speed 1.7 :width 2.4 :height 7.5
            :wake 260 :touch 6 :start [900 700] :quicken 0.35
            :sound {:on false :level 0.35 :pitch 70 :file nil}}
   :lines {:dash 18 :on 10 :width 0.4 :bay-width 0.7
           :centre-inner 0.3 :centre-outer 0.9
           :rail-inner 1.9 :rail-outer 2.5
           :zebra 5 :bar 2}})

(defonce plan (atom nil))
(defonce settings (atom default-settings))
(defonce eye (atom {:at [450 330] :facing 0.0 :held #{} :stand 0.0 :sank nil}))
(defonce scene (atom nil))
(defonce dirty (atom true))
(defonce ticks (atom 0))
(defonce wraith (atom nil))
(defonce checked (atom #{}))
(defonce noise (atom nil))
(defonce loop-id (atom 0))

(def ^:private walking-keys
  #{"arrowup" "arrowdown" "arrowleft" "arrowright" "a" "d"})

(def ^:private near-plane 1.0)

;; ---------------------------------------------------------------------
;; The programs, built once per mount
;; ---------------------------------------------------------------------

(defn- build-programs
  "Every program, compiled from the GLSL the build worked out and sent
  along in the plan, each told its own name so a card that refuses one
  says which one it refused.

  Nothing here writes GLSL or reads it. The shaders were written as
  Clojure in walk_shaders.clj, typechecked there, and turned into text
  by fedoras.glsl before this page existed."
  [gl shaders]
  (into {}
        (map (fn [[which {:keys [vertex fragment]}]]
               [which (gl/program gl (name which) vertex fragment)]))
        shaders))

;; ---------------------------------------------------------------------
;; Pictures
;;
;; A door arrives as an SVG, which a card cannot sample directly, so it
;; is drawn once onto a canvas of a size the card is happy with and
;; that canvas is what becomes the texture. Nothing waits for it: until
;; it lands there is simply no door mesh drawn, and the wall behind is
;; already correct.
;; ---------------------------------------------------------------------

(defn- data-url
  "SVG text as something an Image will load. Percent-encoded rather than
  base64, because an SVG is text and btoa refuses anything outside
  Latin-1, which one curly quote in a label is enough to trigger."
  [svg]
  (str "data:image/svg+xml;charset=utf-8," (js/encodeURIComponent svg)))

(defn- load-picture!
  "Rasterize an SVG to a power-of-two canvas and hand the result to the
  card.

  The SVG arrives as text, inlined by the build, and is turned into a
  data URL here. Fetching it as a file instead is what a browser calls
  cross-origin when the book is opened from disc: the image loads, the
  canvas it is drawn on is quietly tainted, and the upload to the card
  then throws SecurityError from inside a load callback, several
  seconds after anything that could be blamed for it. A data URL is
  same-origin and taints nothing.

  Width and height are set on the element before the source, because an
  SVG without an intrinsic size draws as nothing."
  [gl svg picture-width picture-height keep!]
  (when (and (string? svg) (seq svg))
    (let [image (js/Image.)]
      (set! (.-width image) picture-width)
      (set! (.-height image) picture-height)
      (set! (.-onload image)
            (fn [_]
              (try
                (let [canvas (.createElement js/document "canvas")]
                  (set! (.-width canvas) picture-width)
                  (set! (.-height canvas) picture-height)
                  (.drawImage (.getContext canvas "2d") image 0 0
                              picture-width picture-height)
                  (keep! (gl/picture-texture gl canvas true))
                  (reset! dirty true))
                (catch :default problem
                  (js/console.error "walk: a picture would not go on the card"
                                    problem)))))
      (set! (.-onerror image)
            (fn [_] (js/console.error "walk: a picture did not decode")))
      (set! (.-src image) (data-url svg)))))

;; ---------------------------------------------------------------------
;; The meshes, rebuilt whenever the settings run
;; ---------------------------------------------------------------------

(defn- drop-meshes!
  [gl meshes]
  (doseq [[_ mesh] meshes]
    (when mesh (gl/drop-mesh! gl mesh))))

(defn- build-meshes
  "Every buffer the card will hold, built once from the plan and the
  settings as they stand. Nothing here is touched again until the
  reader presses Run."
  [gl node]
  (let [settings-now @settings
        plan-now @plan
        layouts (:layouts plan-now)
        ground (world/terrain plan-now settings-now)
        swatches (world/read-swatches node)
        default-top (:height (:buildings settings-now))
        reach (:draw settings-now)
        buffer (fn [layout numbers] (gl/mesh gl layout numbers))
        showing (fn [which plants] (if (:show (which settings-now)) plants []))]
    {:swatches swatches
     :terrain ground
     :meshes
     {:screen   (buffer (:sky layouts) world/screen-mesh)
      :wraith   (buffer (:wraith layouts) world/screen-mesh)
      :ground   (buffer (:ground layouts)
                        (world/ground-mesh plan-now settings-now ground reach))
      :wall     (buffer (:wall layouts)
                        (world/wall-mesh plan-now default-top))
      :door     (buffer (:panel layouts)
                        (world/door-mesh plan-now settings-now))
      :window   (buffer (:panel layouts)
                        (world/window-mesh plan-now settings-now))
      :trees    (buffer (:billboard layouts)
                        (world/billboard-mesh (showing :trees (:trees plan-now))
                                              (:spread (:trees settings-now))))
      :bushes   (buffer (:billboard layouts)
                        (world/billboard-mesh (showing :bushes (:bushes plan-now))
                                              (:spread (:bushes settings-now))))
      :step     (buffer (:solid layouts)
                        (world/step-mesh plan-now settings-now swatches))
      :water    (buffer (:solid layouts)
                        (world/water-mesh ground settings-now swatches))
      :roof     (buffer (:solid layouts)
                        (world/roof-mesh plan-now default-top swatches))
      :rotunda  (buffer (:solid layouts)
                        (world/rotunda-mesh plan-now settings-now swatches))
      :glass    (buffer (:solid layouts)
                        (world/glass-mesh plan-now settings-now swatches))
      :posts    (buffer (:solid layouts)
                        (world/post-mesh (assoc plan-now :posts
                                                (showing :posts (:posts plan-now)))
                                         (:posts settings-now) swatches
                                         @checked))
      :fountain (buffer (:solid layouts)
                        (world/fountain-mesh (:fountain settings-now) swatches
                                             (:ripples (:fountain settings-now))))
      :sheet    (buffer (:solid layouts)
                        (world/sheet-mesh (:fountain settings-now) swatches))
      :cloud    (buffer (:cloud layouts)
                        (world/cloud-lobes (:clouds settings-now) (:fov settings-now)))
      :mist     (buffer (:mist layouts)
                        (world/mist-drops
                        (:density (:spray (:fountain settings-now)))
                        (:jets (:spray (:fountain settings-now)) 1.0)))}}))

;; ---------------------------------------------------------------------
;; Where the eye is
;; ---------------------------------------------------------------------

(defn- standing-height
  "How high the ground is where you are. Up THE FILL, down into SPRING
  BROOK, flat everywhere else."
  [px py]
  (world/ground-height (:terrain @scene) @settings px py))

(def ^:private cushion
  "How far off a wall you are stopped, in feet. You are wider than a
  point and the glass is closer than it looks."
  3)

(defn- through-a-doorway?
  "Whether a point lines up with one of a round building's ways in. A
  doorway is a span of angle, and standing in one means the wall is not
  there."
  [box toward-x toward-y radius]
  (let [gap (/ (/ (get-in @settings [:rotunda :door] 9) 2.0) (max radius 1.0))
        bearing (js/Math.atan2 toward-y toward-x)]
    (some (fn [{:keys [normal]}]
            (let [[out-x out-y] normal
                  facing (js/Math.atan2 out-y out-x)
                  apart (js/Math.abs
                         (- (mod (+ (- bearing facing) 9.42478) 6.28318)
                            3.14159))]
              (> apart (- 3.14159 gap))))
          (:faces box))))

(defn- stopped-by-rotunda?
  "The library stops you at its glass, and inside it at whichever of the
  four blocks you have walked into. The way in is the two doorways, and
  once you are through one of them the wall behind you is not in the way
  either: the test is the ring of glass, not the circle it encloses."
  [box px py]
  (let [[middle-x middle-y] (:middle box)
        radius (:radius box)
        toward-x (- px middle-x)
        toward-y (- py middle-y)
        out (js/Math.sqrt (+ (* toward-x toward-x) (* toward-y toward-y)))]
    (or (some (fn [{:keys [x y w h]}]
                (and (> px (- x 1.5)) (< px (+ x w 1.5))
                     (> py (- y 1.5)) (< py (+ y h 1.5))))
              (:rooms box))
        ;; The inner edge of the ring is never thinner than a single
        ;; step. A reader who winds :walk-speed up past the thickness of
        ;; the glass would otherwise walk straight through it, which is
        ;; the one failure a ring has that a disc does not.
        (let [band (max cushion (:walk-speed @settings 2.6))]
          (and (> out (- radius band))
               (< out (+ radius cushion))
               (not (through-a-doorway? box toward-x toward-y radius)))))))

(defn- blocked?
  "A building plus three feet, or the fountain plus three feet. The Fill
  is not in this list, because the Fill is a hill and you are meant to
  go up it, and neither is the library, which you are meant to go into."
  [[px py]]
  (let [{:keys [boxes]} @plan
        fountain (:fountain @settings)]
    (boolean
     (or (some (fn [{:keys [x y w h] :as box}]
                 (if (= :round (:shape box))
                   (stopped-by-rotunda? box px py)
                   (and (> px (- x cushion)) (< px (+ x w cushion))
                        (> py (- y cushion)) (< py (+ y h cushion)))))
               boxes)
         (when (:show fountain)
           (let [toward-x (- px (:x fountain))
                 toward-y (- py (:y fountain))
                 out (+ (:radius fountain) cushion)]
             (< (+ (* toward-x toward-x) (* toward-y toward-y)) (* out out))))))))

(defn- move!
  "Applies held keys to position and facing for one tick, wrapping at
  the world bound with a facing mirror so walking off one edge faces
  you back into the world rather than away from it. Gives back true if
  anything moved."
  []
  (let [{:keys [held at facing]} @eye
        {:keys [walk-speed back-speed side-speed turn-speed bound]
         :or {bound 1000}} @settings
        [x y] at
        turn (cond (held "arrowleft") (- turn-speed)
                   (held "arrowright") turn-speed
                   :else 0)
        forward (cond (held "arrowup") walk-speed
                      (held "arrowdown") (- back-speed)
                      :else 0)
        sideways (cond (held "a") (- side-speed)
                       (held "d") side-speed
                       :else 0)]
    (if (and (zero? turn) (zero? forward) (zero? sideways))
      false
      (let [aimed (+ facing turn)
            step-x (+ x (* forward (js/Math.cos aimed)) (* sideways (- (js/Math.sin aimed))))
            step-y (+ y (* forward (js/Math.sin aimed)) (* sideways (js/Math.cos aimed)))
            over-x (> (js/Math.abs step-x) bound)
            over-y (> (js/Math.abs step-y) bound)
            wrapped-x (if over-x (- (* 2 bound (js/Math.sign step-x)) step-x) step-x)
            wrapped-y (if over-y (- (* 2 bound (js/Math.sign step-y)) step-y) step-y)
            mirrored (cond-> aimed
                       over-x (as-> a (- 3.14159265 a))
                       over-y (as-> a (- a)))]
        (swap! eye assoc :facing mirrored)
        (when-not (blocked? [wrapped-x y]) (swap! eye assoc-in [:at 0] wrapped-x))
        (when-not (blocked? [(get-in @eye [:at 0]) wrapped-y])
          (swap! eye assoc-in [:at 1] wrapped-y))
        (let [[nx ny] (:at @eye)]
          (swap! eye assoc :stand (standing-height nx ny)))
        true))))

;; ---------------------------------------------------------------------
;; Whatever it is that follows you
;;
;; It walks at very nearly your own speed, which is the whole design. You
;; can open a gap and you cannot keep it, and standing still to read a
;; building's name costs you every foot you had. It does not open doors,
;; go round the far side of a building cleverly, or hide. It comes.
;; ---------------------------------------------------------------------

;; ---------------------------------------------------------------------
;; The noise it makes
;;
;; Two ways of making one, and neither is on unless a reader asks. With
;; no file named, a square wave at :pitch, which needs nothing off a
;; disc and is already unpleasant. With a file named, that file, on a
;; loop, played by a plain audio element rather than through the audio
;; graph: routing a file through the graph is what taints it when the
;; book is opened from disc, and a silent monster is worse than a simple
;; one.
;;
;; Either way it is silent until the thing is within :wake, and loudest
;; when it reaches you.
;; ---------------------------------------------------------------------

(defn- hush!
  "Stop whatever is making the noise and forget it. Safe to call when
  there is nothing making a noise, which is most of the time."
  []
  (when-let [{:keys [source element context]} @noise]
    (try (when source (.stop source)) (catch :default _ nil))
    (try (when element (.pause element)) (catch :default _ nil))
    (try (when context (.close context)) (catch :default _ nil)))
  (swap! noise dissoc :source :element :context :gain))

(defn- start-noise!
  "Build whichever noise the settings ask for, silent, ready to be turned
  up. A browser will not let anything make a sound before the reader has
  touched the page, so this only gets as far as building it; `wake-noise!`
  starts it on the first key or click.

  Nothing is rebuilt unless the settings that decide what the noise is
  have actually changed. This runs on every render, and tearing an audio
  context down and putting it back up again is both audible and enough
  to lose the permission the reader already gave."
  []
  (let [{:keys [show] :as beast} (:wraith @settings)
        {:keys [on pitch file] :or {pitch 70}} (:sound beast)
        wanted [show on pitch file]]
    (when (not= wanted (:wanted @noise))
      (hush!)
      (reset! noise {:wanted wanted})
      (when (and show on)
        (if (and (string? file) (seq file))
          (let [element (js/Audio. file)]
            (set! (.-loop element) true)
            (set! (.-volume element) 0)
            (swap! noise assoc :element element))
          (try
            (let [context (js/AudioContext.)
                  oscillator (.createOscillator context)
                  gain (.createGain context)]
              (set! (.-type oscillator) "square")
              (set! (.-value (.-frequency oscillator)) pitch)
              (set! (.-value (.-gain gain)) 0)
              (.connect oscillator gain)
              (.connect gain (.-destination context))
              (.start oscillator)
              (swap! noise assoc
                     :context context :source oscillator :gain gain))
            (catch :default problem
              (js/console.warn "walk: this browser will not make a noise"
                               problem))))))))

(defn- wake-noise!
  "A browser starts an audio context asleep and will not wake it until
  the reader has done something. The first key or click is that."
  []
  (when-let [{:keys [context]} @noise]
    (try
      (when (= "suspended" (.-state context)) (.resume context))
      (catch :default _ nil))))

(defn- howl!
  "How loud it is, worked out from how close it is. Silent at :wake, all
  the way up at :touch, and it climbs faster than the distance closes so
  that the last thirty feet are the ones you notice."
  [gap]
  (when-let [{:keys [gain source element]} @noise]
    (let [{:keys [wake touch] :or {wake 260 touch 6}} (:wraith @settings)
          {:keys [level pitch] :or {level 0.35 pitch 70}}
          (:sound (:wraith @settings))
          nearness (if gap
                     (max 0.0 (min 1.0 (/ (- wake gap) (max 1.0 (- wake touch)))))
                     0.0)
          loudness (* level nearness nearness)]
      (try
        (if gain
          (do (set! (.-value (.-gain gain)) loudness)
              (set! (.-value (.-frequency source))
                    (* pitch (+ 1.0 (* 1.6 nearness)))))
          (do (set! (.-volume element) (max 0.0 (min 1.0 loudness)))
              (when (and (.-paused element) (pos? loudness))
                (.catch (.play element) (fn [_] nil)))))
        (catch :default _ nil)))))

;; ---------------------------------------------------------------------
;; The emergency posts
;;
;; Twenty of them, already standing, already lit, and until now the only
;; thing on the campus that did nothing at all. Walk up to one and it
;; answers. That is the whole of it: there is no puzzle, no order to do
;; them in, and no reward for finishing beyond finding out that all
;; twenty of them work.
;;
;; They are also the reason to be careful about what else is out there.
;; Every post you answer makes the thing that follows you a little
;; faster, so the only thing on this campus that gives you a reason to
;; walk about is also the thing that makes walking about worse.
;; ---------------------------------------------------------------------

(defn- relamp!
  "Rebuild the posts so the ones that have answered are lit differently.
  Thirteen hundred numbers; cheaper than any way of avoiding it."
  []
  (when-let [{:keys [gl meshes swatches]} @scene]
    (when-let [layout (get-in @plan [:layouts :solid])]
      (gl/drop-mesh! gl (:posts meshes))
      (swap! scene assoc-in [:meshes :posts]
             (gl/mesh gl layout
                      (world/post-mesh @plan (:posts @settings)
                                       swatches @checked))))))

(defn- answer-posts!
  "Any post you are standing next to has now been answered."
  []
  (let [posts (:posts @plan)
        reach (get-in @settings [:posts :reach] 9)
        [eye-x eye-y] (:at @eye)
        found (keep-indexed
               (fn [at [px py]]
                 (when (and (not (contains? @checked at))
                            (< (+ (* (- px eye-x) (- px eye-x))
                                  (* (- py eye-y) (- py eye-y)))
                               (* reach reach))
                            (:show (:posts @settings)))
                   at))
               posts)]
    (when (seq found)
      (swap! checked into found)
      (relamp!)
      (reset! dirty true)
      (when (= (count @checked) (count posts))
        ;; A point of fortune, once, silently, the same as anything else
        ;; a reader actually finishes. Guarded because the deed has to be
        ;; registered in fedoras.endings.standing/deeds before it counts
        ;; for anything, and a walk that throws because nobody has got
        ;; round to that yet is worse than a walk that awards nothing.
        (try (widget/did! ::every-post)
             (catch :default problem
               (js/console.warn "walk: no deed registered for the posts"
                                problem)))))))

(defn- follow!
  "One step of whatever it is. It walks straight at you, and when a
  building is in the way it takes whichever of the two axes is still
  clear, which is enough to get round a rectangle and not enough to look
  like it is thinking."
  []
  (let [{:keys [show speed start quicken] :or {speed 1.7 quicken 0.0}}
        (:wraith @settings)
        answered (/ (count @checked) (max 1 (count (:posts @plan))))
        speed (* speed (+ 1.0 (* quicken answered)))
        [eye-x eye-y] (:at @eye)]
    (when show
      (when (nil? @wraith)
        (reset! wraith (vec (or start [900 700]))))
      (let [[at-x at-y] @wraith
            toward-x (- eye-x at-x)
            toward-y (- eye-y at-y)
            gap (js/Math.sqrt (+ (* toward-x toward-x) (* toward-y toward-y)))]
        (when (> gap 0.001)
          (let [step-x (* speed (/ toward-x gap))
                step-y (* speed (/ toward-y gap))
                straight [(+ at-x step-x) (+ at-y step-y)]
                sideways [(+ at-x step-x) at-y]
                lengthways [at-x (+ at-y step-y)]]
            (reset! wraith
                    (cond
                      (not (blocked? straight)) straight
                      (not (blocked? sideways)) sideways
                      (not (blocked? lengthways)) lengthways
                      :else @wraith))))))))

(defn- how-close
  "Feet between you and it, or nil when there is nothing to measure."
  []
  (when-let [[at-x at-y] @wraith]
    (let [[eye-x eye-y] (:at @eye)]
      (js/Math.sqrt (+ (* (- at-x eye-x) (- at-x eye-x))
                       (* (- at-y eye-y) (- at-y eye-y)))))))

(defn- caught!
  "What happens when it reaches you, which is nothing much. You are put
  back on the quad and it is put back where it started, and neither of
  you says anything about it."
  []
  (let [{:keys [touch start] :or {touch 6}} (:wraith @settings)
        gap (how-close)]
    (when (and gap (< gap touch))
      (let [[start-x start-y] (:at @plan)]
        (swap! eye assoc
               :at [start-x start-y]
               :held #{}
               :sank nil
               :stand (standing-height start-x start-y))
        (reset! wraith (vec (or start [900 700])))
        (reset! dirty true)))))

(defn- in-the-water?
  "Whether the ground you are standing on is below the waterline."
  [stand]
  (< stand (- (get-in @settings [:water :waterline] -0.6) 0.2)))

(defn- breathe!
  "Starts the clock when you step into the brook, stops it when you get
  out, and puts you back where you started if it runs out.

  There is nothing here that could hurt you. Being returned to the quad
  is the entire consequence, and the campus does not comment on it
  afterwards."
  [now]
  (let [{:keys [stand sank]} @eye
        breath (* 1000 (get-in @settings [:water :breath] 12))]
    (cond
      (not (in-the-water? stand))
      (when sank (swap! eye assoc :sank nil))

      (nil? sank)
      (swap! eye assoc :sank now)

      (> (- now sank) breath)
      (let [[start-x start-y] (:at @plan)]
        (swap! eye assoc
               :at [start-x start-y]
               :sank nil
               :held #{}
               :stand (standing-height start-x start-y))
        (reset! dirty true)))))

(defn- held-breath
  "Seconds of air left, or nil when you are on dry land."
  [now]
  (when-let [sank (:sank @eye)]
    (let [breath (get-in @settings [:water :breath] 12)]
      (max 0 (js/Math.ceil (- breath (/ (- now sank) 1000)))))))

;; ---------------------------------------------------------------------
;; What is ahead
;; ---------------------------------------------------------------------

(defn- hit-box
  "How far along a ray the first face of a box is, or nil.

  The slab method. A ray is inside a box exactly when it is between the
  two east-west edges and between the two north-south edges at the same
  time, so work out the stretch of the ray that satisfies each pair and
  intersect the two. Whatever is left is the time inside the box, and
  it enters at the start of it."
  [[from-x from-y] [toward-x toward-y] {:keys [x y w h]}]
  (let [per-foot-east (if (zero? toward-x) 1e9 (/ 1.0 toward-x))
        per-foot-south (if (zero? toward-y) 1e9 (/ 1.0 toward-y))
        west-edge (* (- x from-x) per-foot-east)
        east-edge (* (- (+ x w) from-x) per-foot-east)
        north-edge (* (- y from-y) per-foot-south)
        south-edge (* (- (+ y h) from-y) per-foot-south)
        entering (max (min west-edge east-edge) (min north-edge south-edge))
        leaving (min (max west-edge east-edge) (max north-edge south-edge))]
    (when (and (>= leaving (max entering 0.0)) (> entering 0.001))
      entering)))

(defn- hit-circle
  "How far along a ray the edge of a circle is, or nil. The ray's own
  direction is a unit step, so the quadratic has no leading term to
  divide by. From inside the circle there is no edge in front of you to
  meet, which is the right answer: standing in the library, the library
  is not something you are looking at."
  [[from-x from-y] [toward-x toward-y] {:keys [middle radius]}]
  (let [[middle-x middle-y] middle
        offset-x (- from-x middle-x)
        offset-y (- from-y middle-y)
        along (+ (* offset-x toward-x) (* offset-y toward-y))
        clearance (- (+ (* offset-x offset-x) (* offset-y offset-y))
                     (* radius radius))
        room (- (* along along) clearance)]
    (when (and (>= room 0.0) (pos? clearance))
      (let [entering (- (- along) (js/Math.sqrt room))]
        (when (> entering 0.001) entering)))))

(defn- hit
  [origin toward {:keys [shape] :as landmark}]
  (if (= :circle shape)
    (hit-circle origin toward landmark)
    (hit-box origin toward landmark)))

(defn- gather-landmarks
  "Everything on the campus with a name that stands up out of the ground
  far enough to be looked at.

  It used to be the buildings and nothing else, so a reader could stand
  in front of the fountain, or at the foot of a sixteen foot heap of
  landfill, and be told that there was nothing in front of them. The
  lawns are left out on purpose: you are nearly always standing on one,
  and being told so ahead as well as underfoot is noise.

  The fountain comes from the settings rather than the plan because a
  reader can move it."
  [plan settings terrain]
  (let [fountain (:fountain settings)]
    (vec
     (concat
      (mapcat (fn [box]
                (if (= :round (:shape box))
                  (cons (assoc (select-keys box [:name :middle :radius])
                               :shape :circle)
                        (map (fn [room] (assoc room :shape :box)) (:rooms box)))
                  [(assoc (select-keys box [:name :x :y :w :h]) :shape :box)]))
              (:boxes plan))
      (when (:show fountain)
        [{:name "THE FOUNTAIN" :shape :circle
          :middle [(:x fountain) (:y fountain)] :radius (:radius fountain)}])
      (keep (fn [rect]
              (when (:name rect)
                (assoc (select-keys rect [:name :x :y :w :h]) :shape :box)))
            terrain)))))

(defn- nearest
  "The first named thing along a ray, and how far off it is."
  [origin toward]
  (reduce (fn [best landmark]
            (if-let [distance (hit origin toward landmark)]
              (if (or (nil? best) (< distance (first best)))
                [distance landmark]
                best)
              best))
          nil
          (:landmarks @scene)))

(defn- hiccup->dom
  "A small hiccup subset: [tag attrs? & children], plain strings, or
  nil, turned into real DOM nodes."
  [node]
  (cond
    (nil? node) nil
    (string? node) (.createTextNode js/document node)
    (vector? node)
    (let [[tag maybe-attrs & rest-children] node
          has-attrs? (map? maybe-attrs)
          attrs (if has-attrs? maybe-attrs {})
          children (if has-attrs? rest-children (cons maybe-attrs rest-children))
          element (.createElement js/document (name tag))]
      (doseq [[attribute value] attrs]
        (.setAttribute element (name attribute) (str value)))
      (doseq [child children]
        (when-let [child-node (hiccup->dom child)]
          (.appendChild element child-node)))
      element)))

(defn- render-into!
  [element & pieces]
  (while (.-firstChild element)
    (.removeChild element (.-firstChild element)))
  (doseq [piece pieces]
    (when-let [dom-node (hiccup->dom piece)]
      (.appendChild element dom-node))))

(defn- say!
  [node now]
  (let [{:keys [at facing stand]} @eye
        [x y] at
        found (nearest at [(js/Math.cos facing) (js/Math.sin facing)])
        under (if-let [grid (:index @scene)]
                (name (nth world/materials (world/surface-at grid x y)))
                "lawn")
        on-lawn? (= under "lawn")
        air (held-breath now)
        gap (when (get-in @settings [:wraith :show]) (how-close))
        aside (cond air [:span {:class "walk-keep-off"}
                         (str "· hold your breath · " air)]
                    (and gap (< gap 90)) [:span {:class "walk-keep-off"}
                                          (str "· " (int gap) " feet behind you")]
                    on-lawn? [:span {:class "walk-keep-off"}
                              "· keep off the grass"]
                    :else nil)]
    (widget/state! node
                   (str (int x) ", " (int y)
                        (when (:show (:posts @settings))
                          (str " · posts " (count @checked)
                               "/" (count (:posts @plan))))))
    (when-let [el (reader/q node ".walk-ahead")]
      (render-into! el
                    (if found
                      (str (:name (second found)) " · " (int (first found)) " feet")
                      "nothing in front of you")
                    (str (if air " · standing in " " · standing on ") under
                         (when (> stand 0.5) (str " · " (int stand) " feet up"))
                         (when aside " "))
                    aside
                    (when (and (:show (:posts @settings))
                               (seq (:posts @plan))
                               (= (count @checked) (count (:posts @plan))))
                      [:span {:class "walk-keep-off"}
                       " · every post on this campus works"])))))

;; ---------------------------------------------------------------------
;; One frame
;; ---------------------------------------------------------------------

(defn- sun-direction
  [{:keys [azimuth height]}]
  (let [lift (max -0.98 (min 0.98 height))
        flat (js/Math.sqrt (- 1.0 (* lift lift)))]
    [(* flat (js/Math.cos azimuth)) (* flat (js/Math.sin azimuth)) lift]))

(defn draw!
  "One frame, in the order the header of this file gives."
  [canvas now]
  (when-let [{:keys [gl programs meshes palette pictures swatches biggest]} @scene]
    (let [settings-now @settings
          {:keys [at facing stand]} @eye
          canvas-width (.-width canvas)
          canvas-height (.-height canvas)
          aspect (/ canvas-width canvas-height)
          fov (:fov settings-now)
          spread (js/Math.tan (/ fov 2))
          focal (/ (/ canvas-width 2.0) spread)
          reach (:draw settings-now)
          eye-at [(nth at 0) (nth at 1) (+ stand (:eye-height settings-now))]
          camera (gl/times (gl/perspective fov aspect near-plane (max 2600 (* reach 3)))
                           (gl/look eye-at facing))
          eastward (js/Math.cos facing)
          southward (js/Math.sin facing)
          sun (:sun settings-now)
          fountain (world/fountain-parts (:fountain settings-now))
          {:keys [ground brick clouds trees lines water]} settings-now
          {:keys [clarity horizon]} (:haze settings-now)
          ;; Every program that fogs takes the same three numbers, and
          ;; only the middle one is its own business: for the ground it
          ;; is how far out the haze takes over, and for a wall it is how
          ;; far out a brick stops being a brick and becomes a moire
          ;; pattern.
          reach-of (fn [own] [reach own clarity])
          ;; What every program is told about this frame, whether it asks
          ;; or not. A program ignores a uniform it does not declare, so
          ;; telling it costs nothing, and a fact about the frame is
          ;; written down once instead of once per draw. What a program
          ;; is told on top of this is only what is true of it alone.
          shared {"u_camera" camera
                  "u_eye" eye-at
                  "u_right" [(- southward) eastward 0]
                  "u_haze" (:haze swatches)
                  "u_sky" (:sky swatches)
                  "u_sun" (:sun swatches)
                  "u_reach" (reach-of 0.0)
                  "u_sunward" (sun-direction sun)
                  "u_ambient" (:ambient sun)
                  "u_foam" (:foam swatches)
                  "u_middle" [(:x fountain) (:y fountain) (:nozzle fountain)]
                  "u_swell" [(:swell water) (:wavelength water)]
                  "u_focal" focal
                  "u_biggest" biggest
                  "u_now" (mod (* now 0.0007) 1.0)}
          draw (fn [program mesh own & [mode]]
                 (gl/draw! gl (get programs program) (get meshes mesh)
                           (merge shared own) (or mode gl/TRIANGLES)))
          haze (:haze swatches)]

      (.viewport gl 0 0 canvas-width canvas-height)
      (.clearColor gl (nth haze 0) (nth haze 1) (nth haze 2) 1)
      (.clear gl (bit-or gl/COLOR_BUFFER_BIT gl/DEPTH_BUFFER_BIT))
      (.disable gl gl/CULL_FACE)
      (.disable gl gl/DEPTH_TEST)
      (.disable gl gl/BLEND)

      (draw :sky :screen {"u_up" [0 0 1]
                          "u_forward" [eastward southward 0]
                          "u_spread" [spread (/ spread aspect)]
                          "u_size" (:size sun)})

      (when (:show clouds)
        (.enable gl gl/BLEND)
        (.blendFunc gl gl/SRC_ALPHA gl/ONE)
        (draw :cloud :cloud {"u_now" now
                             "u_drift" (:drift clouds)
                             "u_dome" 2000.0
                             "u_shape" [(:ragged clouds) (:flat clouds)]
                             "u_tint" [1 1 1]}
              gl/POINTS)
        (.disable gl gl/BLEND))

      (.enable gl gl/DEPTH_TEST)
      (.depthFunc gl gl/LESS)
      (.depthMask gl true)

      (gl/sampler! gl (:ground programs) "u_palette" 0 palette)
      (draw :ground :ground {"u_reach" (reach-of horizon)
                             "u_grain" [(:slab ground) (:joint ground)
                                        (:tuft ground) (:patch ground)]
                             "u_marks" [(:mow ground) (:bay ground)
                                        (:tie ground) (:grain ground)]
                             "u_lines" [(:dash lines) (:on lines)
                                        (:width lines) (:bay-width lines)]
                             "u_edges" [(:centre-inner lines) (:centre-outer lines)
                                        (:rail-inner lines) (:rail-outer lines)]
                             "u_zebra" [(:zebra lines) (:bar lines)]})

      (draw :wall :wall {"u_reach" (reach-of (:detail brick))
                         "u_brick" [(:width brick) (:height brick)
                                    (:mortar brick) (:streak brick)]
                         "u_brickA" (:brick-a swatches)
                         "u_brickB" (:brick-b swatches)
                         "u_brickC" (:brick-c swatches)
                         "u_mortar" (:mortar swatches)
                         "u_face" (:face swatches)})

      (doseq [mesh [:roof :posts :step :rotunda]]
        (draw :solid mesh {}))
      (when (:show (:fountain settings-now))
        (draw :solid :fountain {}))

      (draw :billboard :trees {"u_leaf" (:leaf swatches)
                               "u_leafLit" (:leaf-lit swatches)
                               "u_bark" (:bark swatches)
                               "u_trunk" [(:trunk trees) (:fork trees)
                                          (:taper trees) (:limbs trees)]})
      (draw :billboard :bushes {"u_leaf" (:bush swatches)
                                "u_leafLit" (:bush-lit swatches)
                                "u_bark" (:bark swatches)
                                "u_trunk" [0.0 0.0 1.0 0.0]})

      (.enable gl gl/BLEND)
      (.blendFunc gl gl/SRC_ALPHA gl/ONE_MINUS_SRC_ALPHA)

      ;; Doors and windows are pictures, and a picture's clear parts are
      ;; thrown away rather than blended, so what is left of one is
      ;; solid and has to say so to the depth buffer. It used to be the
      ;; only see-through thing that did not, which let the far side of
      ;; the library's glass, drawn after it, paint straight over its
      ;; door. From outside, the doors looked like more glass.
      (.depthMask gl true)
      (doseq [kind [:door :window]]
        (when-let [picture (get pictures kind)]
          (gl/sampler! gl (:panel programs) "u_image" 0 picture)
          (draw :panel kind {})))
      (.depthMask gl false)

      ;; The library's wall, then the top of the brook, then the
      ;; fountain's own water. None of the three is ever in front of
      ;; another, and the order is the order a reader is likeliest to be
      ;; standing inside them.
      (draw :solid :glass {})
      (when (:show water)
        (draw :solid :water {}))
      (when (:show (:fountain settings-now))
        (draw :solid :sheet {})
        (.blendFunc gl gl/SRC_ALPHA gl/ONE)
        (draw :mist :mist {"u_bell" [(min (* (:reach fountain) 1.35)
                                          (* (:inner fountain) 0.95))
                                     (* (:rise fountain) 1.6)
                                     world/mist-life
                                     (:inner fountain)]
                           "u_now" now
                           "u_shape" [0.0 0.0]
                           "u_tint" (:foam swatches)}
              gl/POINTS))

      ;; Drawn last of everything that can be seen through, and only
      ;; once it is near enough to be worth the alarm. The three numbers
      ;; in u_glitch all climb as it closes, so the nearer it is the
      ;; more of it there is, which is the only warning anybody gets.
      (when-let [[at-x at-y] (and (:show (:wraith settings-now)) @wraith)]
        (let [{:keys [wake width height] :or {wake 260 width 2.4 height 7.5}}
              (:wraith settings-now)
              gap (js/Math.sqrt (+ (* (- at-x (nth at 0)) (- at-x (nth at 0)))
                                   (* (- at-y (nth at 1)) (- at-y (nth at 1)))))
              near (- 1.0 (min 1.0 (/ gap (max 1.0 wake))))]
          (when (pos? near)
            (draw :wraith :wraith {"u_root" [at-x at-y (standing-height at-x at-y)]
                                   "u_size" [width height]
                                   "u_tint" haze
                                   "u_glitch" [(* 0.30 (+ 0.25 near))
                                               (+ 0.45 (* 0.5 near))
                                               (+ 0.25 (* 0.7 near))]
                                   "u_now" (mod (* now 0.001) 1.0)}))))

      (.depthMask gl true)
      (.disable gl gl/BLEND))))

;; ---------------------------------------------------------------------
;; Input
;; ---------------------------------------------------------------------

(defn- wire-keys!
  [canvas]
  (set! (.-tabIndex canvas) 0)
  (set! (.-onkeydown canvas)
        (fn [event]
          (let [pressed (str/lower-case (.-key event))]
            (wake-noise!)
            (when (contains? walking-keys pressed)
              (.preventDefault event)
              (swap! eye update :held conj pressed)))))
  (set! (.-onkeyup canvas)
        (fn [event]
          (swap! eye update :held disj (str/lower-case (.-key event)))))
  (set! (.-onblur canvas) (fn [_] (swap! eye assoc :held #{}) (reset! dirty true)))
  (set! (.-onfocus canvas) (fn [_] (reset! dirty true)))
  (set! (.-onpointerdown canvas) (fn [_] (wake-noise!) (.focus canvas))))

;; ---------------------------------------------------------------------
;; Setting up and running
;; ---------------------------------------------------------------------

(defn- deep-merge
  "Like merge, but a key whose value is a map in both gets merged rather
  than replaced. Plain merge silently drops any key a reader's own
  settings map does not happen to repeat, so a settings file written
  before :streak existed would wipe out the default every time, even
  though the reader never asked for that and only never knew to
  restate it."
  [shipped asked-for]
  (if (and (map? shipped) (map? asked-for))
    (merge-with deep-merge shipped asked-for)
    asked-for))

(defn paint!
  "Called when the settings are run and on every render. Sizes the
  canvas, rebuilds every mesh and the palette, and asks for a frame."
  [node]
  (when-let [canvas (reader/q node ".walk-canvas")]
    (let [[asked-width asked-height] (:resolution @settings)]
      (when (or (not= asked-width (.-width canvas))
                (not= asked-height (.-height canvas)))
        (set! (.-width canvas) asked-width)
        (set! (.-height canvas) asked-height)))
    (when-let [{:keys [gl meshes palette]} @scene]
      (drop-meshes! gl meshes)
      (when palette (.deleteTexture gl palette))
      (let [{:keys [swatches terrain] :as built} (build-meshes gl node)]
        (swap! scene assoc
               :meshes (:meshes built)
               :swatches swatches
               :terrain terrain
               :landmarks (gather-landmarks @plan @settings terrain)
               :palette (gl/bytes-texture gl world/palette-width 2
                                          (world/palette-bytes node))
               :index (world/index (:surfaces @plan)))))
    (start-noise!)
    (reset! dirty true)))

(defn- advance!
  "Everything that happens in a tick whether or not anybody is looking:
  you move, the posts you are standing by answer, the thing that follows
  you takes its step and makes its noise, and the brook counts. Gives
  back whether you moved, which is the only one of these the frame needs
  to hear about directly; the rest mark the frame dirty themselves when
  they change anything a reader would see."
  [now]
  (let [moved (move!)]
    (answer-posts!)
    (follow!)
    (howl! (how-close))
    (caught!)
    (breathe! now)
    moved))

(defn- worth-drawing?
  "Whether this tick changes anything on the screen. Standing still with
  nothing moving costs no frame at all; standing still with the clouds
  drifting costs one every third tick, which is as often as a cloud
  moves far enough to see."
  [moved tick]
  (let [settings-now @settings]
    (or moved
        @dirty
        (:sank @eye)
        (get-in settings-now [:wraith :show])
        (and (get-in settings-now [:fountain :animate])
             (get-in settings-now [:fountain :show]))
        (and (get-in settings-now [:clouds :show])
             (zero? (mod tick 3))))))

(defn- start-loop!
  "One tick per animation frame, and a drawing only when the tick changed
  something. loop-id lets a loop left over from a previous mount notice
  it has been superseded and stop rescheduling itself."
  [node canvas]
  (let [this-loop (swap! loop-id inc)]
    (letfn [(frame []
              (when (= this-loop @loop-id)
                (try
                  (when (widget/running? (.-id node))
                    (let [now (.now js/performance)
                          moved (advance! now)]
                      (when (worth-drawing? moved (swap! ticks inc))
                        (reset! dirty false)
                        (draw! canvas now)
                        (say! node now))))
                  (catch :default problem
                    (js/console.error "walk: frame" problem)))
                (js/requestAnimationFrame frame)))]
      (frame))))

(defn setup!
  "One-time per-mount setup: resets the eye from the plan, gets a
  context, compiles the programs, loads the pictures, wires the
  keyboard, registers the settings callback, and starts the loop."
  [node]
  (let [{:keys [at facing]} @plan]
    (reset! eye {:at (vec at) :facing facing :held #{}
                 :stand (standing-height (first at) (second at))}))
  (when-let [canvas (widget/q! node ".walk-canvas")]
    (if-let [gl (gl/context canvas)]
      (do
        (reset! scene {:gl gl
                       :programs (build-programs gl (:shaders @plan))
                       :meshes {}
                       :pictures {}
                       :swatches (world/read-swatches node)
                       :biggest (gl/biggest-point gl)
                       :index (world/index (:surfaces @plan))})
        (load-picture! gl (:door-picture @plan) 256 512
                       (fn [texture] (swap! scene assoc-in [:pictures :door] texture)))
        (load-picture! gl (:window-picture @plan) 256 256
                       (fn [texture] (swap! scene assoc-in [:pictures :window] texture)))
        (wire-keys! canvas)
        (widget/settings!
         node (.-id node)
         (fn [setting]
           (swap! settings
                  (fn [current]
                    (deep-merge current
                                (select-keys setting
                                             [:resolution :fov :eye-height :draw :bound
                                              :walk-speed :back-speed :side-speed
                                              :turn-speed :sun :ground :haze :fill :water
                                              :brick :doors :windows :buildings
                                              :fountain :trees :bushes :posts
                                              :clouds :lines :wraith
                                              :rotunda]))))
           (paint! node)
           (.focus canvas)))
        (paint! node)
        (start-loop! node canvas))
      (js/console.error "walk: no WebGL context on this browser"))))

(defn mount!
  "Registers this widget with the host app's mount system."
  [{:keys [id] :as derived-plan}]
  (reset! plan derived-plan)
  (widget/mount! ::walk (or id "walk") setup! paint!))