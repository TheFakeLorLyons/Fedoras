(ns front.martial
  "Hover behaviour for the glitched ASCII tribute, run by Scittle. Every number worth
  tuning is in `defaults`; the page can override any of them by passing a map."
  (:require [clojure.string :as string]))

(def svg-ns "http://www.w3.org/2000/svg")
(def xlink-ns "http://www.w3.org/1999/xlink")

(def defaults
  {;; ---- colour ------------------------------------------------------------
   ;; Lights take hues from the arc of the colour wheel running clockwise from
   ;; :hue-from through red to :hue-to. 320 to 160 holds pinks, reds, oranges, yellows
   ;; and greens, and leaves out every blue and the purples. Both ends are pulled in by
   ;; :page-hue-sway, so the page's hue-rotate can never carry a light out of the arc.
   :hue-from 320
   :hue-to 160
   :hue-spread 40          ;; degrees of scatter around the wandering hue
   :hue-wander 0.01        ;; how fast that hue drifts back and forth along the arc

   ;; ---- lights under the pointer --------------------------------------------
   ;; Art units: one character cell is 9.6 wide and 19.2 tall.
   :light-radius 135       ;; how far from the pointer a character can catch
   :falloff 58             ;; gaussian sigma; smaller keeps the lights tight to the pointer
   :seed-rate 0.5          ;; chance a character right under the pointer catches per batch
   :seed-interval 90       ;; ms between batches, and batches only happen while moving
   :lit-ceiling 0.45       ;; most of the characters in range allowed to burn at once
   :life-min 850           ;; ms every light lasts
   :life-span 1250         ;; up to this many more ms, drawn per light

   ;; ---- electricity ----------------------------------------------------------
   :ambient-every [2500 8000] ;; ms between discharges, drawn from this range
   :current-chance 0.6     ;; share of discharges that are currents; the rest are specks
   :current-length [18 48] ;; cells a current runs
   :current-step 14        ;; ms for a current to advance one cell
   :current-fork 0.08      ;; chance per cell that a short branch splits off
   :current-life [220 420] ;; ms each cell of a current glows
   :current-white 0.55     ;; how far current colours are pushed toward white
   :speck-count [3 9]      ;; specks per flash
   :speck-life [120 300]   ;; ms each speck glows
   :speck-white 0.3
   :cell-width 9.6         ;; the art's grid, used to walk currents cell by cell
   :cell-height 19.2

   ;; ---- lens -----------------------------------------------------------------
   :lens-radius 105
   :lens-scale 1.16
   :lens-warp 6            ;; displacement in art units
   :warp-octaves 2         ;; 1 is cheaper and a little plainer

   ;; ---- motion, all driven by the pointer and still when it stops --------------
   :page-hue-sway 20       ;; degrees the art's hue swings as the pointer travels
   :lean 3                 ;; px the note tips in the direction the pointer moves
   :ramp 0.08              ;; share of the gap intensity closes per frame

   ;; ---- bookkeeping ------------------------------------------------------------
   :paint-hz 30            ;; times a second light colours are rewritten
   :bucket 48})            ;; spatial index cell, art units

;; The page's Clojure namespace can swap this map in when it embeds the file.
(def overrides {})

(def settings (merge defaults overrides))

(defn setting [key]
  (get settings key))

(defn between [[low high]]
  (+ low (rand (- high low))))

(defn pick [items]
  (get items (js/Math.floor (* (rand) (count items)))))

;; ---------------------------------------------------------------- dom helpers

(defn nodes
  "A DOM collection as a sequence. Older Scittle builds carry no array-seq."
  [collection]
  (map (fn [position] (.item collection position))
       (range (.-length collection))))

(defn dom
  "Build an SVG node from hiccup: [:circle {:r 4}] or [:g {} [:use {...}]]."
  [[tag attributes & children]]
  (let [element (.createElementNS js/document svg-ns (name tag))]
    (doseq [[attribute value] attributes]
      (if (string/starts-with? (name attribute) "xlink:")
        (.setAttributeNS element xlink-ns (name attribute) (str value))
        (.setAttribute element (name attribute) (str value))))
    (doseq [child children]
      (.appendChild element (dom child)))
    element))

;; ---------------------------------------------------------------- colour

(defn palette-hue
  "A hue on the allowed arc, with place running from 0 to 1 along it."
  [place]
  (let [margin (setting :page-hue-sway)
        width (mod (- (setting :hue-to) (setting :hue-from)) 360)
        usable (max 0 (- width (* 2 margin)))]
    (mod (+ (setting :hue-from) margin (* (max 0 (min 1 place)) usable)) 360)))

(defn hue->rgb [hue]
  (let [sector (/ (mod hue 360) 60)
        rising (js/Math.round (* 255 (mod sector 1)))
        falling (- 255 rising)
        ramp [[255 rising 0] [falling 255 0] [0 255 rising]
              [0 falling 255] [rising 0 255] [255 0 falling]]]
    (vec (map (fn [channel] (js/Math.round (+ (* channel 0.75) 64)))
              (get ramp (mod (js/Math.floor sector) 6))))))

(defn whiten [rgb amount]
  (vec (map (fn [channel] (js/Math.round (+ channel (* (- 255 channel) amount)))) rgb)))

(defn wandering-colour
  "A colour near the hue currently wandering along the arc."
  [tone]
  (let [width (max 1 (- (mod (- (setting :hue-to) (setting :hue-from)) 360)
                        (* 2 (setting :page-hue-sway))))
        place (+ (/ (- 1 (js/Math.cos tone)) 2)
                 (* (- (rand) 0.5) (/ (setting :hue-spread) width)))]
    (hue->rgb (palette-hue place))))

(defn parse-ink [value]
  (let [numbers (map js/parseFloat (re-seq #"[0-9.]+" (or value "")))]
    (if (= 3 (count (take 3 numbers)))
      (vec (take 3 numbers))
      [26 26 26])))

;; ---------------------------------------------------------------- lens
;; A magnified, warped copy of the art under a soft disc. The mask and the filter both
;; follow the disc, so each pointer move only redraws a lens-sized patch; with the mask
;; spanning the whole art, every move repainted a full-size copy of the piece.

(defn lens-parts [suffix art-id ink]
  (let [id (fn [part] (str "tribute-" part suffix))]
    {:defs [[:radialGradient {:id (id "fade")}
             [:stop {:offset 0 :stop-color "#fff"}]
             [:stop {:offset 0.6 :stop-color "#fff" :stop-opacity 0.92}]
             [:stop {:offset 1 :stop-color "#000"}]]
            [:mask {:id (id "mask") :maskUnits "userSpaceOnUse" :x 0 :y 0 :width 1 :height 1}
             [:circle {:id (id "disc") :r (setting :lens-radius)
                       :fill (str "url(#" (id "fade") ")")}]]
            [:filter {:id (id "warp") :filterUnits "userSpaceOnUse" :x 0 :y 0 :width 1 :height 1}
             [:feTurbulence {:type "fractalNoise" :baseFrequency "0.018 0.05"
                             :numOctaves (setting :warp-octaves) :seed 7 :result "noise"}]
             [:feColorMatrix {:in "noise" :type "matrix" :result "map"
                              :values "1 0 0 0 0  0 1 0 0 0  0 0 0 0 0  0 0 0 0 1"}]
             [:feDisplacementMap {:in "SourceGraphic" :in2 "map" :scale (setting :lens-warp)
                                  :xChannelSelector "R" :yChannelSelector "G"}]]]
     :layer [:g {:id (id "lens")
                 :mask (str "url(#" (id "mask") ")")
                 :filter (str "url(#" (id "warp") ")")
                 :opacity 0
                 :style "pointer-events:none;transition:opacity 220ms ease-out"}
             [:g {:id (id "zoom")}
              [:use {:href (str "#" art-id) :xlink:href (str "#" art-id) :fill ink}]]]}))

(defn install-lens! [svg art suffix ink]
  (let [{:keys [defs layer]} (lens-parts suffix (.getAttribute art "id") ink)
        holder (or (.querySelector svg "defs")
                   (.insertBefore svg (dom [:defs {}]) (.-firstChild svg)))
        find (fn [part] (.querySelector svg (str "#tribute-" part suffix)))]
    (doseq [definition defs]
      (.appendChild holder (dom definition)))
    (.appendChild svg (dom layer))
    {:lens (find "lens") :zoom (find "zoom") :disc (find "disc")
     :mask (find "mask") :warp (find "warp")}))

(defn place-region! [element x y reach]
  (.setAttribute element "x" (- x reach))
  (.setAttribute element "y" (- y reach))
  (.setAttribute element "width" (* 2 reach))
  (.setAttribute element "height" (* 2 reach)))

(defn move-lens! [{:keys [lens zoom disc mask warp]} x y]
  (let [radius (setting :lens-radius)]
    (.setAttribute disc "cx" x)
    (.setAttribute disc "cy" y)
    (place-region! mask x y radius)
    (place-region! warp x y (+ radius (* 4 (setting :lens-warp))))
    (.setAttribute zoom "transform"
                   (str "translate(" x " " y ") scale(" (setting :lens-scale) ") "
                        "translate(" (- x) " " (- y) ")"))
    (.setAttribute lens "opacity" 1)))

;; ---------------------------------------------------------------- the art

(defn read-glyphs [art]
  (vec (map (fn [element]
              {:element element
               :x (js/parseFloat (.getAttribute element "x"))
               :y (js/parseFloat (.getAttribute element "y"))})
            (nodes (.querySelectorAll art "text")))))

(defn spatial-index [glyphs]
  (let [size (setting :bucket)]
    (reduce (fn [index [position {:keys [x y]}]]
              (let [key [(js/Math.floor (/ x size)) (js/Math.floor (/ y size))]]
                (assoc index key (conj (get index key []) position))))
            {}
            (map vector (range (count glyphs)) glyphs))))

(defn cell-grid
  "Glyph positions by [column row], for walking currents cell to cell."
  [glyphs]
  (reduce (fn [grid [position {:keys [x y]}]]
            (assoc grid [(js/Math.round (/ x (setting :cell-width)))
                         (js/Math.round (/ y (setting :cell-height)))]
                   position))
          {}
          (map vector (range (count glyphs)) glyphs)))

(defn nearby [index x y]
  (let [size (setting :bucket)
        reach (js/Math.ceil (/ (setting :light-radius) size))
        column (js/Math.floor (/ x size))
        row (js/Math.floor (/ y size))]
    (vec (for [across (range (- reach) (inc reach))
               down (range (- reach) (inc reach))
               position (get index [(+ column across) (+ row down)])]
           position))))

;; ---------------------------------------------------------------- lights
;; A light is a glyph recoloured for a while. :born can lie in the future, which is how
;; a current travels: each cell along it is born a little after the one before.

(defn light [now delay life colour]
  {:born (+ now delay) :life life :colour colour :level -1})

(defn ignite
  "One batch under the pointer: a gaussian roll per character in range, capped."
  [{:keys [glyphs index alive tone] :as state} x y now]
  (let [candidates (nearby index x y)
        ceiling (max 1 (js/Math.floor (* (count candidates) (setting :lit-ceiling))))
        sigma (setting :falloff)
        reach (setting :light-radius)]
    (loop [positions (shuffle candidates)
           burning (count (filter alive candidates))
           lights alive]
      (let [position (first positions)
            glyph (when position (get glyphs position))
            span (when glyph (+ (* (- (:x glyph) x) (- (:x glyph) x))
                                (* (- (:y glyph) y) (- (:y glyph) y))))]
        (cond
          (or (nil? position) (>= burning ceiling))
          (assoc state :alive lights)

          (or (get lights position)
              (> span (* reach reach))
              (> (rand) (* (setting :seed-rate) (js/Math.exp (- (/ span (* 2 sigma sigma)))))))
          (recur (rest positions) burning lights)

          :else
          (recur (rest positions)
                 (inc burning)
                 (assoc lights position
                        (light now 0 (+ (setting :life-min) (rand (setting :life-span)))
                               (wandering-colour tone)))))))))

(def headings [[1 0] [1 1] [0 1] [-1 1] [-1 0] [-1 -1] [0 -1] [1 -1]])

(defn walk
  "Cells visited by a path that mostly holds its heading and now and then turns 45
  degrees, each tagged with the step it is reached on."
  [start heading length first-step]
  (loop [[column row] start
         heading heading
         step first-step
         remaining length
         visited []]
    (if (<= remaining 0)
      visited
      (let [roll (rand)
            heading (mod (+ heading (cond (< roll 0.18) 1 (< roll 0.36) -1 :else 0)) 8)
            [across down] (get headings heading)
            cell [(+ column across) (+ row down)]]
        (recur cell heading (inc step) (dec remaining) (conj visited [cell step]))))))

(defn current
  "A bolt across the art: a trunk heading left or right, with short forks."
  [grid]
  (let [heading (pick [0 4])
        trunk (walk (pick (vec (keys grid))) heading
                    (js/Math.round (between (setting :current-length))) 0)
        forks (mapcat (fn [[cell step]]
                        (when (< (rand) (setting :current-fork))
                          (walk cell (mod (+ heading (pick [2 6])) 8)
                                (+ 3 (js/Math.floor (rand 6))) step)))
                      trunk)]
    (concat trunk forks)))

(defn discharge
  "Lights for one burst of electricity: a travelling current, or a scatter of specks."
  [{:keys [grid]} now]
  (if (< (rand) (setting :current-chance))
    (let [colour (whiten (hue->rgb (palette-hue (rand))) (setting :current-white))]
      (reduce (fn [lights [cell step]]
                (let [position (get grid cell)]
                  (if position
                    (assoc lights position
                           (light now (* step (setting :current-step))
                                  (between (setting :current-life)) colour))
                    lights)))
              {}
              (current grid)))
    (let [positions (vec (vals grid))]
      (reduce (fn [lights _]
                (assoc lights (pick positions)
                       (light now (rand 400) (between (setting :speck-life))
                              (whiten (hue->rgb (palette-hue (rand))) (setting :speck-white)))))
              {}
              (range (js/Math.round (between (setting :speck-count))))))))

(defn brightness
  "Quick catch, slow burn down."
  [age]
  (if (< age 0.18)
    (/ age 0.18)
    (js/Math.pow (- 1 (/ (- age 0.18) 0.82)) 1.6)))

(defn advance
  "Repaint lights whose brightness moved, douse the finished ones, leave the unborn."
  [{:keys [glyphs alive ink] :as state} now]
  (assoc state :alive
         (reduce (fn [lights [position {:keys [born life colour level] :as entry}]]
                   (let [age (/ (- now born) life)
                         style (.-style (:element (get glyphs position)))]
                     (cond
                       (< age 0) (assoc lights position entry)
                       (>= age 1) (do (.removeProperty style "fill") lights)
                       :else
                       (let [share (/ (js/Math.round (* 24 (brightness age))) 24)]
                         (when (not= share level)
                           (.setProperty style "fill"
                                         (str "rgb(" (string/join "," (map (fn [base lit]
                                                                             (js/Math.round (+ base (* (- lit base) share))))
                                                                           ink colour))
                                              ")")))
                         (assoc lights position (assoc entry :level share))))))
                 {}
                 alive)))

;; ---------------------------------------------------------------- page response

(defn publish!
  "Feed the CSS variables, writing only the ones whose rounded value changed."
  [{:keys [parts intensity travel velocity-x velocity-y published] :as state}]
  (let [reach (setting :lean)
        lean (fn [velocity] (max (- reach) (min reach (* velocity (/ reach 8)))))
        lean-x (* intensity (lean velocity-x))
        values {"--tribute-intensity" (.toFixed intensity 2)
                "--tribute-hue" (str (.toFixed (* intensity (setting :page-hue-sway)
                                                  (js/Math.sin (/ travel 400)))
                                               0) "deg")
                "--tribute-x" (str (.toFixed lean-x 1) "px")
                "--tribute-y" (str (.toFixed (* intensity (lean velocity-y)) 1) "px")
                "--tribute-rotation" (str (.toFixed (* lean-x 0.12) 2) "deg")}
        style (.-style (:note parts))]
    (doseq [[property value] values]
      (when (not= value (get published property))
        (.setProperty style property value)))
    (assoc state :published values)))

;; ---------------------------------------------------------------- frame loop

(defn to-art [svg client-x client-y]
  (let [box (.getBoundingClientRect svg)
        view (.. svg -viewBox -baseVal)]
    [(+ (.-x view) (* (/ (- client-x (.-left box)) (.-width box)) (.-width view)))
     (+ (.-y view) (* (/ (- client-y (.-top box)) (.-height box)) (.-height view)))]))

(defn frame!
  "One frame. Nothing here runs unless something is burning, the pointer is moving, or
  the intensity is still easing; then the loop parks until the next event."
  [state]
  (let [now (js/performance.now)
        {:keys [parts hovering moved client-x client-y last-x last-y last-seed last-paint
                intensity velocity-x velocity-y tone travel]
         :as snapshot} @state
        step-x (if (and moved last-x) (- client-x last-x) 0)
        step-y (if (and moved last-y) (- client-y last-y) 0)
        velocity-x (+ (* 0.8 velocity-x) (* 0.2 step-x))
        velocity-y (+ (* 0.8 velocity-y) (* 0.2 step-y))
        moving (> (+ (js/Math.abs velocity-x) (js/Math.abs velocity-y)) 0.05)
        [x y] (when moved (to-art (:svg parts) client-x client-y))
        target (if hovering 1 0)
        eased (+ intensity (* (setting :ramp) (- target intensity)))
        settled (< (js/Math.abs (- target eased)) 0.005)
        seeding (and moved hovering (>= (- now last-seed) (setting :seed-interval)))
        painting (>= (- now last-paint) (/ 1000 (setting :paint-hz)))
        stepped (cond-> (assoc snapshot
                               :moved false
                               :last-x (when hovering client-x)
                               :last-y (when hovering client-y)
                               :velocity-x (if moving velocity-x 0)
                               :velocity-y (if moving velocity-y 0)
                               :travel (+ travel (js/Math.sqrt (+ (* step-x step-x) (* step-y step-y))))
                               :tone (+ tone (setting :hue-wander))
                               :intensity (if settled target eased))
                  seeding (-> (assoc :last-seed now) (ignite x y now))
                  painting (-> (assoc :last-paint now) (advance now)))
        next-state (publish! stepped)]
    (when (and moved hovering (:lens parts))
      (move-lens! (:lens parts) x y))
    (if (or (seq (:alive next-state)) (not settled) moving)
      (do (reset! state next-state)
          (js/requestAnimationFrame (fn [] (frame! state))))
      (reset! state (assoc next-state :running false)))))

(defn wake! [state]
  (when-not (:running @state)
    (swap! state assoc :running true)
    (js/requestAnimationFrame (fn [] (frame! state)))))

(defn keep-discharging!
  "Every few seconds, while the art is on screen, let a current or some specks loose."
  [state]
  (js/setTimeout
   (fn []
     (when (and (:visible @state) (not (.-hidden js/document)))
       (swap! state (fn [snapshot]
                      (assoc snapshot :alive (merge (:alive snapshot)
                                                    (discharge snapshot (js/performance.now))))))
       (wake! state))
     (keep-discharging! state))
   (between (setting :ambient-every))))

;; ---------------------------------------------------------------- wiring

(defn attach-once! [note suffix]
  (let [svg (.querySelector note "svg.tribute-art")
        art (when svg (.querySelector svg "[id^='art']"))
        painted (when svg (.querySelector svg "g[fill]"))
        glyphs (when art (read-glyphs art))]
    (when (seq glyphs)
      (let [ink (or (when painted (.getAttribute painted "fill")) "currentColor")
            lens (install-lens! svg art suffix ink)
            state (atom {:parts {:note note :svg svg :lens lens}
                         :glyphs glyphs
                         :index (spatial-index glyphs)
                         :grid (cell-grid glyphs)
                         :alive {}
                         :ink (parse-ink (when painted (.-fill (js/getComputedStyle painted))))
                         :tone (rand 6.28)
                         :travel 0
                         :intensity 0
                         :velocity-x 0
                         :velocity-y 0
                         :hovering false
                         :moved false
                         :visible true
                         :last-seed 0
                         :last-paint 0
                         :published {}
                         :running false})]
        (js/console.log (str "tribute: " (count glyphs) " characters live on " suffix))
        (.addEventListener note "pointermove"
                           (fn [event]
                             (swap! state assoc :hovering true :moved true
                                    :client-x (.-clientX event) :client-y (.-clientY event))
                             (wake! state)))
        (.addEventListener note "pointerleave"
                           (fn [_]
                             (.setAttribute (:lens lens) "opacity" 0)
                             (swap! state assoc :hovering false :moved false :last-x nil :last-y nil)
                             (wake! state)))
        (when (.-IntersectionObserver js/window)
          (.observe (js/IntersectionObserver.
                     (fn [entries]
                       (swap! state assoc :visible
                              (.-isIntersecting (aget entries (dec (.-length entries)))))))
                    note))
        (keep-discharging! state)))))

(defn attach! [note suffix]
  (when-not (.getAttribute note "data-tribute")
    (.setAttribute note "data-tribute" suffix)
    (attach-once! note suffix)))

(defn calm?
  "Readers who asked their system for less motion get the art at rest."
  []
  (.-matches (js/matchMedia "(prefers-reduced-motion: reduce)")))

(defn init! []
  (when-not (calm?)
    (doseq [[position note] (map vector
                                 (range)
                                 (nodes (.querySelectorAll js/document ".tribute-note")))]
      (attach! note (str "-t" position)))))

(init!)
