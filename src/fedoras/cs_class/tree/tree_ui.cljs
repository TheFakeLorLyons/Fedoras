(ns fedoras.cs-class.tree.tree-ui
  (:require [clojure.string :as str]))

(def settings
  (atom {:pattern :tree
         :patterns
         {:tree      {:children [{:turn -0.42 :ratio 0.72} {:turn 0.42 :ratio 0.72}]}
          :fern      {:children [{:turn 0.03 :ratio 0.87}
                                 {:turn -0.65 :ratio 0.42}
                                 {:turn 0.65 :ratio 0.42}]}
          :vine      {:children [{:turn 0.22 :ratio 0.94}]}
          :crook     {:children [{:turn -0.24 :ratio 0.79} {:turn 0.62 :ratio 0.55}]}
          :trie      {:children [{:turn -0.52 :ratio 0.58}
                                 {:turn 0 :ratio 0.63}
                                 {:turn 0.52 :ratio 0.58}]}
          ;; grows out of both ends of itself instead of one
          :mirror    {:trunks 2
                      :children [{:turn 0.05 :ratio 0.84}
                                 {:turn -0.72 :ratio 0.44}
                                 {:turn 0.72 :ratio 0.44}]}
          ;; six trunks off one point, which is a hexagon before it is
          ;; anything else
          :snowflake {:trunks 6
                      :children [{:turn -0.55 :ratio 0.56} {:turn 0.55 :ratio 0.56}]}
          ;; eight, each one a vine, so the whole thing turns without
          ;; anything in it turning
          :rosette   {:trunks 8
                      :children [{:turn 0.3 :ratio 0.88}]}
          ;; turns of exactly a right angle's half, and each child
          ;; 1/root-2 of its parent. that is the Levy C curve, which
          ;; somebody found in 1938 and is not a tree in any sense
          :levy      {:children [{:turn -0.7854 :ratio 0.7071}
                                 {:turn 0.7854 :ratio 0.7071}]}
          ;; a spine that goes almost straight on, with teeth off one
          ;; side only, because nothing says the children have to be
          ;; arranged around anything
          :comb      {:children [{:turn 0 :ratio 0.93} {:turn 1.45 :ratio 0.3}]}}
         :spread-scale 1
         :ratio-scale 1
         :length 26
         :lean 0
         :jitter 0
         :sway 0.5
         :sway-min-px 5
         :draw-seconds 4
         :taper 2.2
         :tip-fade 0.78
         :colour-gamma 3.5
         :min-px 3.5
         :log-timing? true
         :zoom-step 1.6
         :max-depth 150
         :max-lines 60000}))

(defonce st (atom nil))

;; where the pointer is over the canvas, 0..1 each way, or nil when it
;; is not. read by child-steps, so moving the mouse bends the rule
;; itself rather than the picture of it.
(defonce sway (atom nil))

;; ---------------------------------------------------------------------
;; The rule
;;
;; One line from here. Then the same thing again from the far end of
;; it, shorter, and turned. That is the whole of it, and it is the same
;; size at depth four as it is at depth twelve.
;;
;; The accumulator is the only thing here that is not in the version on
;; the page above. It carries the lines gathered so far instead of
;; joining lists on the way back out, which the page does not need to
;; care about and a browser drawing twenty thousand of them does.
;; ---------------------------------------------------------------------

(defn- ms [] (.now js/performance))

(defn- log-timing! [label t0 extra]
  (when (:log-timing? @settings)
    (js/console.log (str "tree: " label " " (.toFixed (- (ms) t0) 1) "ms" extra))))

(defn- jit [j]
  (if (pos? j) (* j 2 (- (rand) 0.5)) 0))

(defn- trunks
  "How many of these start from the middle, evenly spaced around it.
  One is a tree. Two grows out of both ends of itself. Six is a
  hexagon before it is anything else, and the rule has not been told
  about hexagons -- it only ever gets asked the same question from six
  directions at once."
  []
  (let [{:keys [pattern patterns]} @settings]
    (max 1 (or (:trunks (get patterns pattern)) 1))))

(defn- child-steps
  "What the rule does next, as a list: how far each new line turns off
  the one it came from, and how much of it it is.

  This is the only thing that changed to get every pattern below out
  of the same four lines. It used to be one angle and one ratio for
  everybody, which can only ever make a tree. Letting each child carry
  its own two numbers gets a fern -- a long spine that barely turns,
  with short branches off it -- and a vine, which is one child that
  keeps almost all of its length and always turns the same way, and is
  a spiral for no reason anybody had to write down.

  :spread-scale multiplies every turn and :ratio-scale every length,
  so the two dials from before still work, on any pattern. The 0.98
  is not a style choice: a child that keeps all of its parent never
  gets smaller, and the drawing never finishes."
  []
  (let [{:keys [pattern patterns spread-scale ratio-scale lean]} @settings
        [sx sy] (or @sway [0.5 0.5])
        amt     (:sway @settings)
        ;; left/right leans it, up/down opens and closes it
        lean    (+ lean (* amt (- sx 0.5) 1.2))
        spread-scale (* spread-scale (+ 1 (* amt (- 0.5 sy) 1.2)))
        kids (or (:children (get patterns pattern))
                 (:children (get patterns :tree))
                 [{:turn -0.42 :ratio 0.72} {:turn 0.42 :ratio 0.72}])]
    (mapv (fn [{:keys [turn ratio]}]
            {:turn  (+ lean (* turn spread-scale))
             :ratio (min 0.98 (max 0.02 (* ratio ratio-scale)))})
          kids)))

;; refit! sits with the other view code, above the drawing it calls
(declare view->px clear! draw-range! paint!
         css-var parse-colour mix-colour)

;; ---------------------------------------------------------------------
;; Where it stops
;;
;; Not at a depth. The rule has no depth in it and putting one there
;; was always a lie told for the browser's benefit.
;;
;; It stops at the two places a drawing actually stops: when the next
;; line would be smaller than a pixel, and when everything still to
;; come from here is somewhere the frame is not. Both are facts about
;; the screen, not about the rule, which is why zooming in produces
;; more of it. It was always there.
;;
;; The second one needs a bound on how far a line can possibly reach
;; before it dies out. Each one is :ratio of its parent, so the whole
;; remaining future of a point is at most length + length*r +
;; length*r^2 + ... which is length/(1-r), and a circle of that radius
;; that misses the frame contains nothing that could ever enter it.
;; ---------------------------------------------------------------------

;; ---------------------------------------------------------------------
;; Fitting it in the frame
;;
;; Nothing in the rule knows how large the result is, so the frame is
;; measured against the tree rather than the other way round. Change
;; the ratio to 0.9 and the thing is four times the height it was and
;; still exactly fills the box.
;; ---------------------------------------------------------------------

(defn- bounds [segs]
  (reduce (fn [[lo-x lo-y hi-x hi-y] [[x1 y1] [x2 y2] _]]
            [(min lo-x x1 x2) (min lo-y y1 y2)
             (max hi-x x1 x2) (max hi-y y1 y2)])
          [1e9 1e9 -1e9 -1e9]
          segs))

(defn- fit-view [segs w h]
  (let [[lo-x lo-y hi-x hi-y] (bounds segs)
        pad   (* w 0.06)
        span-x (max 0.001 (- hi-x lo-x))
        span-y (max 0.001 (- hi-y lo-y))
        s     (min (/ (- w (* 2 pad)) span-x)
                   (/ (- h (* 2 pad)) span-y))
        off-x (+ pad (/ (- (- w (* 2 pad)) (* s span-x)) 2))
        off-y (+ pad (/ (- (- h (* 2 pad)) (* s span-y)) 2))]
    {:scale s :lo-x lo-x :lo-y lo-y :off-x off-x :off-y off-y :h h}))

(defn- px->view
  "Back the other way, for zooming at the pointer rather than at the
  middle of the frame."
  [{:keys [scale lo-x lo-y off-x off-y h]} [px py]]
  [(+ lo-x (/ (- px off-x) scale))
   (+ lo-y (/ (- h off-y py) scale))])

(defn- zoom-at
  "Multiply the scale and move the offsets so the world point under the
  pointer stays under the pointer."
  [view [px py] factor]
  (let [[wx wy] (px->view view [px py])
        v' (update view :scale * factor)
        [nx ny] (view->px v' [wx wy])]
    (-> v'
        (update :off-x + (- px nx))
        (update :off-y - (- py ny)))))

(defn view->px [{:keys [scale lo-x lo-y off-x off-y h]} [x y]]
  ;; y goes up in the rule and down on a screen, same as the drawing on
  ;; the page turning y into (- 100 y)
  [(+ off-x (* scale (- x lo-x)))
   (- h off-y (* scale (- y lo-y)))])

;; ---------------------------------------------------------------------
;; Building one
;; ---------------------------------------------------------------------

(defn- style-table
  "Colour, transparency and width for every depth, worked out once when
  the tree is built.

  This used to happen inside the drawing, which meant twice per frame:
  two getComputedStyle calls -- each one a forced style recalculation
  -- and a hundred and fifty colour strings rebuilt, sixty times a
  second, to produce exactly the same table every time. Nothing here
  changes between frames, so nothing here belongs in a frame."
  [node deepest]
  (let [trunk (parse-colour (css-var node "--tree-trunk" "#7d5a3c"))
        tip   (parse-colour (css-var node "--tree-tip" "#e6e4df"))
        {:keys [colour-gamma taper tip-fade]} @settings
        n     (+ deepest 2)
        cols  (array)
        alpha (array)
        width (array)]
    (dotimes [d n]
      (let [u (if (> deepest 1) (/ (dec (max 1 d)) (dec deepest)) 0)
            u (min 1 (max 0 u))
            t (- 1 u)]
        (.push cols (mix-colour trunk tip (js/Math.pow u colour-gamma)))
        (.push alpha (+ tip-fade (* (- 1 tip-fade) t)))
        (.push width (max 0.4 (* taper (+ 0.25 (* 0.75 t)))))))
    {:cols cols :alpha alpha :width width}))

(defn- regrow
  "Build whatever is visible at the current view.

  Everything the rule needs is pulled out of the settings once, here,
  and closed over -- it used to be read again at every one of twenty
  thousand nodes, which is twenty thousand map lookups to learn the
  same four numbers. The lines come back as one flat array of plain
  numbers, five per line, rather than eight thousand little vectors of
  vectors, for the same reason: the work that can happen once should.

  The rule itself is untouched. It is still a line, then the same
  thing again from the end of it, and everything below is bookkeeping
  around that."
  [view w h]
  (let [{:keys [min-px max-depth max-lines jitter length]} @settings
        steps   (mapv (fn [{:keys [turn ratio]}] [turn ratio]) (child-steps))
        widest  (apply max (map second steps))
        nkids   (count steps)
        ntrunk  (trunks)
        scale   (:scale view)
        lo-x    (:lo-x view)   lo-y  (:lo-y view)
        off-x   (:off-x view)  off-y (:off-y view)
        ;; compared against the length itself, so the multiply that
        ;; used to happen at every node happens once here
        min-len (/ min-px scale)
        reach-k (/ scale (- 1 widest))
        jitter? (pos? jitter)
        out     (array)
        deepest (volatile! 0)]
    (letfn [(walk [x y angle len d]
              (when (and (<= d max-depth)
                         (< (.-length out) (* 5 max-lines))
                         (>= len min-len))
                ;; view->px and near-frame?, inlined: two function
                ;; calls and eight map lookups per node otherwise
                (let [px (+ off-x (* scale (- x lo-x)))
                      py (- h off-y (* scale (- y lo-y)))
                      cx (if (< px 0) 0 (if (> px w) w px))
                      cy (if (< py 0) 0 (if (> py h) h py))
                      ex (- px cx)
                      ey (- py cy)
                      r  (* len reach-k)]
                  (when (<= (+ (* ex ex) (* ey ey)) (* r r))
                    (let [x2 (+ x (* len (js/Math.cos angle)))
                          y2 (+ y (* len (js/Math.sin angle)))]
                      (.push out x) (.push out y)
                      (.push out x2) (.push out y2) (.push out d)
                      (when (> d @deepest) (vreset! deepest d))
                      (dotimes [i nkids]
                        (let [kid (nth steps i)
                              turn (nth kid 0)
                              ratio (nth kid 1)]
                          (walk x2 y2
                                (if jitter? (+ angle turn (jit jitter)) (+ angle turn))
                                (* len ratio) (inc d)))))))))]
      (dotimes [i ntrunk]
        (walk 50 0 (+ (/ js/Math.PI 2) (* i (/ (* 2 js/Math.PI) ntrunk))) length 1))
      {:segments out
       :count (js/Math.floor (/ (.-length out) 5))
       :deepest @deepest})))

(defn- grow-rough
  "The same rule with nothing but a depth limit, used once to find out
  roughly how large the whole thing is so the frame can be fitted
  around it. Nine deep is enough to know the shape and cheap enough
  not to matter, so long as it is also stopped by a count: five
  children nine deep is two million lines, and this runs before
  anything is on screen to show for it. Four thousand is far more
  than enough to know where the edges are.

  No jitter here on purpose: this is measuring, and a frame that moved
  every time you redrew would be worse than one slightly too big."
  [out [x y] angle length d steps]
  (if (or (> d 9) (< length 0.02) (>= (count out) 4000))
    out
    (let [x2  (+ x (* length (js/Math.cos angle)))
          y2  (+ y (* length (js/Math.sin angle)))
          out (conj out [[x y] [x2 y2] d])]
      (reduce (fn [acc {:keys [turn ratio]}]
                (grow-rough acc [x2 y2] (+ angle turn) (* length ratio) (inc d) steps))
              out
              steps))))

(defn- build!
  "First build only. Fits the frame around a shallow, cheap version so
  there is a sensible starting view, then grows properly into it."
  [node cv]
  (let [t0 (ms)
        w (.-width cv) h (.-height cv)
        steps (child-steps)
        n     (trunks)
        rough (reduce (fn [acc i]
                        (grow-rough acc [50 0]
                                    (+ (/ js/Math.PI 2) (* i (/ (* 2 js/Math.PI) n)))
                                    (:length @settings) 1 steps))
                      []
                      (range n))
        view (fit-view rough w h)
        t1 (ms)
        {:keys [segments count deepest]} (regrow view w h)]
    (log-timing! "fit" t0 (str " (" (clojure.core/count rough) " rough lines)"))
    (log-timing! "grow" t1 (str " (" count " lines, " deepest " deep)"))
    (reset! st (merge {:segments segments :count count :view view :shown 0
                       :deepest deepest :home view :playing? true}
                      (style-table node deepest)))))

(defn- refit!
  "After a zoom or a pan: regrow into the new view and put the whole
  thing straight up, rather than animating twenty thousand lines every
  time the wheel moves a notch."
  [cv ctx node view]
  (let [w (.-width cv) h (.-height cv)
        {:keys [segments count deepest]} (regrow view w h)]
    (swap! st merge {:segments segments :count count :view view :deepest deepest
                     :shown count :playing? false}
           (style-table node deepest))
    (clear! cv ctx)
    (draw-range! node ctx 0 count)
    (paint! node)))

;; ---------------------------------------------------------------------
;; Drawing
;;
;; Only ever the lines that have not been drawn yet. Nothing on this
;; canvas moves once it is down, so clearing and redrawing all of it
;; sixty times a second would be twenty thousand lines of work to
;; produce a picture identical to the one already there.
;; ---------------------------------------------------------------------

(defn- css-var [node nm fallback]
  (or (try (let [v (.getPropertyValue (js/getComputedStyle node) nm)]
             (when-not (str/blank? v) (str/trim v)))
           (catch :default _ nil))
      fallback))

(defn- parse-colour
  "A CSS colour into [r g b]. Both forms show up: a stylesheet may say
  #e6e4df, but getComputedStyle usually hands back rgb(230, 228, 223),
  and a var that resolves to another var can arrive as either."
  [c]
  (let [c (str/trim (or c ""))]
    (cond
      (str/starts-with? c "#")
      (let [h (subs c 1)
            h (if (= 3 (count h)) (apply str (mapcat (fn [ch] [ch ch]) h)) h)]
        [(js/parseInt (subs h 0 2) 16)
         (js/parseInt (subs h 2 4) 16)
         (js/parseInt (subs h 4 6) 16)])

      (str/starts-with? c "rgb")
      (let [ns (map js/parseFloat (re-seq #"[0-9.]+" c))]
        (if (>= (count ns) 3) (mapv js/Math.round (take 3 ns)) [230 228 223]))

      :else [230 228 223])))

(defn- mix-colour [a b t]
  (str "rgb(" (js/Math.round (+ (* (nth a 0) (- 1 t)) (* (nth b 0) t))) ","
       (js/Math.round (+ (* (nth a 1) (- 1 t)) (* (nth b 1) t))) ","
       (js/Math.round (+ (* (nth a 2) (- 1 t)) (* (nth b 2) t))) ")"))

(defn- draw-range!
  "Draw lines [from,to) out of the flat array. Everything that does not
  change per line -- the style table, the transform, the settings --
  was worked out before this was called."
  [_node ctx from to]
  (let [{:keys [segments view cols alpha width]} @st
        scale (:scale view)
        lo-x  (:lo-x view)  lo-y  (:lo-y view)
        off-x (:off-x view) off-y (:off-y view)
        h     (:h view)
        top   (dec (.-length cols))]
    (set! (.-lineCap ctx) "round")
    (dotimes [k (- to from)]
      (let [i  (* 5 (+ from k))
            d  (aget segments (+ i 4))
            j  (if (> d top) top d)]
        (set! (.-strokeStyle ctx) (aget cols j))
        (set! (.-globalAlpha ctx) (aget alpha j))
        (set! (.-lineWidth ctx) (aget width j))
        (.beginPath ctx)
        (.moveTo ctx
                 (+ off-x (* scale (- (aget segments i) lo-x)))
                 (- h off-y (* scale (- (aget segments (+ i 1)) lo-y))))
        (.lineTo ctx
                 (+ off-x (* scale (- (aget segments (+ i 2)) lo-x)))
                 (- h off-y (* scale (- (aget segments (+ i 3)) lo-y))))
        (.stroke ctx)))
    (set! (.-globalAlpha ctx) 1)))

(defn- clear! [cv ctx]
  (.clearRect ctx 0 0 (.-width cv) (.-height cv)))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn paint! [node]
  (let [{:keys [count shown deepest view home playing?]} @st
        n count
        z (/ (:scale view) (:scale home))]
    (fedoras.reader/text!
     node ".tree-counts"
     (str (name (:pattern @settings)) " · " n " lines on screen · " deepest " deep"
          (when (< shown n) (str " · drawn " shown))
          (when (> z 1.05) (str " · zoom " (.toFixed z 1) "×"))))
    (fedoras.reader/text! node ".tree-pause"
                          (if playing? "Pause" "Go on"))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- on-button! [node sel f]
  (when-let [el (fedoras.reader/q node sel)]
    (set! (.-onclick el) (fn [e] (.preventDefault e) (f) false))))

(defn- canvas-px
  "Mouse position in canvas pixels, which is not the same as page
  pixels the moment CSS scales the canvas down to fit the column."
  [cv e]
  (let [r (.getBoundingClientRect cv)]
    [(* (- (.-clientX e) (.-left r)) (/ (.-width cv) (.-width r)))
     (* (- (.-clientY e) (.-top r)) (/ (.-height cv) (.-height r)))]))

(defn- sway-regrow!
  "Regrow at a coarser cutoff while the pointer is driving it. Eight
  thousand lines rebuilt sixty times a second is not something an
  interpreter in a page is going to do, and at :sway-min-px it is
  nearer two thousand, which it will."
  [node cv ctx]
  (let [real (:min-px @settings)]
    (swap! settings assoc :min-px (:sway-min-px @settings))
    (let [w (.-width cv) h (.-height cv)
          {:keys [segments count deepest]} (regrow (:view @st) w h)]
      (swap! settings assoc :min-px real)
      (swap! st merge {:segments segments :count count :deepest deepest
                       :shown count :playing? false}
             (style-table node deepest))
      (clear! cv ctx)
      (draw-range! node ctx 0 count))))

(defn- wire-view! [node cv ctx]
  ;; addEventListener with passive false, not (set! (.-onwheel cv) ...):
  ;; browsers are entitled to treat a wheel handler as passive, and a
  ;; passive handler's preventDefault does nothing, which is the page
  ;; scrolling out from under the thing you were trying to zoom.
  (.addEventListener
   cv "wheel"
   (fn [e]
     (.preventDefault e)
     (.stopPropagation e)
     (let [step (:zoom-step @settings)
           f    (if (neg? (.-deltaY e)) step (/ 1 step))]
       (refit! cv ctx node (zoom-at (:view @st) (canvas-px cv e) f))))
   #js {:passive false})

  (let [dragging (atom nil)]
    (set! (.-onmousedown cv)
          (fn [e] (reset! dragging (canvas-px cv e)) false))

    (set! (.-onmousemove cv)
          (fn [e]
            (let [[nx ny] (canvas-px cv e)]
              (if-let [[ox oy] @dragging]
                ;; a held button moves the frame
                (when (or (> (js/Math.abs (- nx ox)) 1) (> (js/Math.abs (- ny oy)) 1))
                  (reset! dragging [nx ny])
                  (refit! cv ctx node
                          (-> (:view @st)
                              (update :off-x + (- nx ox))
                              (update :off-y - (- ny oy)))))
                ;; a loose one bends the rule
                (when (pos? (:sway @settings))
                  (reset! sway [(/ nx (.-width cv)) (/ ny (.-height cv))])
                  (swap! st assoc :swaying? true))))
            false))

    (set! (.-onmouseup cv) (fn [_] (reset! dragging nil) false))
    (set! (.-onmouseleave cv)
          (fn [_]
            (reset! dragging nil)
            ;; let it fall back upright, and put the fine version back
            (when @sway
              (reset! sway nil)
              (refit! cv ctx node (:view @st)))
            false))))

(defn- wire-controls! [node cv ctx]
  (on-button! node ".tree-again"
              (fn [] (build! node cv) (clear! cv ctx) (paint! node)))
  (on-button! node ".tree-pause"
              (fn [] (swap! st update :playing? not) (paint! node)))
  (on-button! node ".tree-all"
              (fn []
                (let [n (:count @st)]
                  (draw-range! node ctx (:shown @st) n)
                  (swap! st assoc :shown n :playing? false)
                  (paint! node))))
  (on-button! node ".tree-home"
              (fn [] (refit! cv ctx node (:home @st))))
  (wire-view! node cv ctx))

(defn- wire-settings! [node id cv ctx]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current
                     (select-keys setting
                                  [:pattern :patterns :spread-scale :ratio-scale
                                   :length :lean :jitter :sway :sway-min-px
                                   :draw-seconds :taper :tip-fade :colour-gamma
                                   :min-px :zoom-step :max-depth :max-lines :log-timing?]))))
     (build! node cv)
     (clear! cv ctx)
     (paint! node))))

(defn setup! [node]
  (let [id  (.-id node)
        cv  (fedoras.reader/q node ".tree-canvas")
        ctx (.getContext cv "2d")]
    ;; the build is a few hundred thousand interpreted operations and
    ;; it used to run right here, which is the page sitting still until
    ;; it finished. a timeout of zero is enough: the browser gets to
    ;; finish loading and paint, and the tree turns up immediately
    ;; after rather than in front.
    (reset! st {:segments (array) :count 0 :shown 0 :deepest 1
                :view {:scale 1 :lo-x 0 :lo-y 0 :off-x 0 :off-y 0 :h (.-height cv)}
                :home {:scale 1} :playing? false
                :cols (array "#000") :alpha (array 1) :width (array 1)})
    (js/setTimeout (fn [] (build! node cv) (clear! cv ctx) (paint! node)) 0)
    (fedoras.ui.widget-ui/wire! node "controls" (fn [n] (wire-controls! n cv ctx)))
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id cv ctx)))

    (letfn [(frame []
              (try
                ;; one regrow per frame at most, however many times the
                ;; pointer moved since the last one
                (when (:swaying? @st)
                  (swap! st assoc :swaying? false)
                  (sway-regrow! node cv ctx))
                (let [{:keys [count shown playing?]} @st
                      n count]
                  (when (and playing? (< shown n))
                    ;; lines per frame, worked out from how long the
                    ;; whole thing should take rather than fixed --
                    ;; otherwise a tree of thirty thousand lines takes
                    ;; a minute and one of two thousand is over before
                    ;; you have looked at it
                    (let [per (max 1 (js/Math.ceil (/ n (* 60 (:draw-seconds @settings)))))
                          to  (min n (+ shown per))]
                      (draw-range! node ctx shown to)
                      (swap! st assoc :shown to)
                      ;; the moment it finishes, rather than up to
                      ;; twelve frames afterwards
                      (when (or (= to n) (zero? (mod to 60)))
                        (paint! node)))))
                (catch :default e
                  (js/console.error "tree: frame failed, skipping" e)))
              (js/requestAnimationFrame frame))]
      (frame))

    (paint! node)))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount! ::tree (or id "tree") setup! paint!))