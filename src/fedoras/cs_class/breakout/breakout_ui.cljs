(ns fedoras.cs-class.breakout.breakout-ui
  "The wall, running.

  THIRTY BRICKS, three rows of ten, and the arithmetic that finds which
  one a ball is in is four lines and is the whole of the wall.

  THE FIXED ONES ACCUMULATE. Each wall keeps every brick that was fixed
  in the last one and adds another. Wall five has five. Wall twenty has
  twenty. Wall thirty has thirty and there is nothing on it to hit.

  A wall counts as cleared when nothing breakable is left, because
  otherwise it never would be, and the readout says how many are
  permanent so a reader can watch the number climb.

  THE BALL CANNOT GET STUCK. A held ball releases on a click, on a
  mousedown, on the space bar, and by itself after two seconds. Four
  ways out of one state, because a state a player cannot leave is not a
  feature and was the only bug anybody ever reported about this."
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; The board
;; ---------------------------------------------------------------------

(def W 100)
(def H 70)
(def ROWS 3)
(def COLS 10)
(def BW 10)
(def BH 6)
(def WALL-BOTTOM 52)

(defonce st    (atom nil))
(defonce known (atom #{}))

(def settings
  (atom {:drop-chance 0.22 :effect 600 :serve 120
         :fixed-first 1 :fixed-each 1
         :speed 1.0 :bat 10 :mark :always}))

;; ---------------------------------------------------------------------
;; What is in the wall
;;
;; Kind, letter, whether it is worse than nothing, and what it does.
;; Who asked for which is in the scene, which is where it belongs.
;; ---------------------------------------------------------------------

(def drops
  [{:kind :wide   :letter "W" :bad? false :is "a bigger bat, for a while"}
   {:kind :multi  :letter "M" :bad? false :is "three of it"}
   {:kind :pierce :letter "P" :bad? false :is "three bricks that do not turn it"}
   {:kind :catch  :letter "C" :bad? false :is "the bat holds it so you can aim"}
   {:kind :slow   :letter "S" :bad? false :is "a slower ball, for a while"}
   {:kind :narrow :letter "N" :bad? true  :is "a smaller bat, for a while"}
   {:kind :hurry  :letter "H" :bad? true  :is "a faster ball, for a while"}])

(def by-kind (into {} (map (fn [d] [(:kind d) d])) drops))

;; ---------------------------------------------------------------------
;; What the reader has found out the hard way
;; ---------------------------------------------------------------------

(defn- learn! [kind]
  (let [ks (conj @known kind)]
    (reset! known ks)
    (fedoras.reader/keep! :brk-known (mapv name ks))))

(defn- bad-now? [kind]
  (boolean
   (and (:bad? (by-kind kind))
        (case (:mark @settings)
          :always true
          :never  false
          (contains? @known kind)))))

;; ---------------------------------------------------------------------
;; The wall
;; ---------------------------------------------------------------------

(defn- lay-wall
  "Thirty bricks, and the ones that were permanent last time are still
  permanent, and one more has joined them."
  [fixed]
  (into {} (for [r (range ROWS) c (range COLS)]
             [[r c] (if (contains? fixed [r c]) :fixed :plain)])))

(defn- add-fixed
  "Pick another brick to make permanent, out of the ones that are not."
  [fixed n]
  (let [free (vec (for [r (range ROWS) c (range COLS)
                        :when (not (contains? fixed [r c]))]
                    [r c]))]
    (reduce (fn [acc _]
              (let [left (vec (remove acc free))]
                (if (seq left)
                  (conj acc (nth left (int (* (js/Math.random) (count left)))))
                  acc)))
            fixed (range n))))

(defn- fresh []
  (let [fixed (add-fixed #{} (:fixed-first @settings))]
    {:wall  (lay-wall fixed)
     :fixed fixed
     :bat   50
     :balls [{:p [50 6] :v [0.8 0.9] :held (:serve @settings) :pierce 0}]
     :drops []
     :fx    {}
     :tally {:broken 0 :lost 0 :walls 1}
     :frame 0}))

(defn- next-wall [s]
  (let [fixed (add-fixed (:fixed s) (:fixed-each @settings))]
    (-> s
        (assoc :fixed fixed :wall (lay-wall fixed))
        (update-in [:tally :walls] inc))))

(defn- breakable [s]
  (count (filter (fn [[_ k]] (= k :plain)) (:wall s))))

;; ---------------------------------------------------------------------
;; Effects
;; ---------------------------------------------------------------------

(defn- on? [s k] (pos? (get-in s [:fx k] 0)))

(defn- bat-half
  "Ten, six more for a wide one, four fewer for the other kind, and
  never less than five, because a bat you cannot hit anything with is
  not a bat."
  [s]
  (max 5 (+ (:bat @settings) (if (on? s :wide) 6 0) (if (on? s :narrow) -4 0))))

(defn- speed [s]
  (* (:speed @settings)
     (cond (on? s :slow) 0.72 (on? s :hurry) 1.35 :else 1.0)))

(defn- tick-fx [s]
  (update s :fx (fn [m] (into {} (keep (fn [[k v]] (when (> v 1) [k (dec v)])) m)))))

;; ---------------------------------------------------------------------
;; Bricks
;; ---------------------------------------------------------------------

(defn- brick-at [s x y]
  (when (and (>= x 0) (< x W) (>= y WALL-BOTTOM) (< y (+ WALL-BOTTOM (* ROWS BH))))
    (let [c (int (/ x BW)) r (int (/ (- y WALL-BOTTOM) BH))]
      (when (get (:wall s) [r c]) [r c]))))

(defn- centre [[r c]]
  [(+ (* c BW) (/ BW 2)) (+ WALL-BOTTOM (* r BH) (/ BH 2))])

(defn- break-brick
  "A permanent brick turns the ball and stays where it is. Nothing else
  happens and nothing is recorded, which is the point of it."
  [s brick]
  (if (= :fixed (get (:wall s) brick))
    s
    (cond-> (-> s (update :wall dissoc brick) (update-in [:tally :broken] inc))
      (< (js/Math.random) (:drop-chance @settings))
      (update :drops conj
              {:p (centre brick)
               :kind (:kind (nth drops (int (* (js/Math.random) (count drops)))))}))))

;; ---------------------------------------------------------------------
;; One ball
;;
;; The horizontal and the vertical are tested separately, so a ball that
;; comes in at the side of a brick turns sideways and one that comes in
;; underneath turns over, which is the difference between a game and a
;; thing that occasionally does what you expected.
;; ---------------------------------------------------------------------

(defn- move-ball [s {[x y] :p [dx dy] :v held :held pierce :pierce :as b}]
  (if (and held (pos? held))
    [s (assoc b :p [(:bat s) 6] :held (dec held))]
    (let [k (speed s)
          nx (+ x (* dx k)) ny (+ y (* dy k))
          half (bat-half s)
          hx (brick-at s nx y) hy (brick-at s x ny) hd (brick-at s nx ny)]
      (cond
        (or hx hy hd)
        (let [brick (or hy hx hd)
              hard  (= :fixed (get (:wall s) brick))
              s'    (break-brick s brick)]
          (if (and (pos? (or pierce 0)) (not hard))
            [s' (assoc b :p [nx ny] :pierce (dec pierce))]
            [s' (assoc b :p [nx ny]
                       :v (cond hy [dx (- dy)] hx [(- dx) dy] :else [dx (- dy)]))]))

        (or (< nx 0) (> nx W))
        [s (assoc b :p [(max 0 (min W nx)) ny] :v [(- dx) dy])]

        (> ny H) [s (assoc b :p [nx H] :v [dx (- dy)])]

        ;; the bat. where it lands decides where it goes, which was the
        ;; first thing anybody asked for and the only one that helped.
        (and (< ny 4) (neg? dy) (< (js/Math.abs (- nx (:bat s))) half))
        (let [off (/ (- nx (:bat s)) half)
              ndx (max -1.4 (min 1.4 (+ dx (* 0.9 off))))]
          [s (if (on? s :catch)
               (assoc b :p [nx 6] :v [ndx (js/Math.abs dy)] :held 240)
               (assoc b :p [nx 4] :v [ndx (js/Math.abs dy)]))])

        (< ny 0) [s (assoc b :dead true)]
        :else    [s (assoc b :p [nx ny])]))))

(defn- move-balls [s]
  (let [[s' bs] (reduce (fn [[acc out] b]
                          (let [[acc' b'] (move-ball acc b)]
                            [acc' (conj out b')]))
                        [s []] (:balls s))
        alive (vec (remove :dead bs))]
    (if (seq alive)
      (assoc s' :balls alive)
      (-> s'
          (update-in [:tally :lost] inc)
          (assoc :balls [{:p [(:bat s') 6] :v [0.8 0.9]
                          :held (:serve @settings) :pierce 0}])
          (assoc :drops [])))))

;; ---------------------------------------------------------------------
;; The drops
;; ---------------------------------------------------------------------

(defn- apply-drop [s kind]
  (when (:bad? (by-kind kind)) (learn! kind))
  (case kind
    :multi  (update s :balls
                    (fn [bs]
                      (if (>= (count bs) 5)
                        bs
                        (vec (mapcat (fn [{[dx dy] :v :as b}]
                                       [b (assoc b :v [(- dx) dy] :held nil)
                                        (assoc b :v [dy dx] :held nil)])
                                     bs)))))
    :pierce (update s :balls (fn [bs] (mapv (fn [b] (assoc b :pierce 3)) bs)))
    (assoc-in s [:fx kind] (:effect @settings))))

(defn- move-drops [s]
  (let [half (bat-half s)]
    (reduce
     (fn [acc {[x y] :p kind :kind :as d}]
       (let [ny (- y 0.55)]
         (cond
           ;; the bat is the only thing on this board that catches
           ;; anything and it cannot be selective
           (and (< ny 4) (< (js/Math.abs (- x (:bat acc))) half))
           (apply-drop acc kind)

           (< ny 0) acc
           :else (update acc :drops conj (assoc d :p [x ny])))))
     (assoc s :drops [])
     (:drops s))))

(defn tick [s]
  (let [s (-> s (update :frame inc) tick-fx move-balls move-drops)]
    (if (zero? (breakable s)) (next-wall s) s)))

(defn serve! []
  (swap! st update :balls (fn [bs] (mapv (fn [b] (assoc b :held nil)) bs))))

;; ---------------------------------------------------------------------
;; Drawing
;;
;; The ink comes off the canvas rather than out of this file, so the
;; palette is a stylesheet decision and the scheme toggle repaints.
;; ---------------------------------------------------------------------

(defn- css-var [node nm fallback]
  (or (try (let [v (.getPropertyValue (js/getComputedStyle node) nm)]
             (when-not (str/blank? v) (str/trim v)))
           (catch :default _ nil))
      fallback))

(defn draw! [node cv ctx]
  (let [s @st
        w (.-width cv) h (.-height cv)
        sx (/ w W) sy (/ h H)
        ink   (css-var node "--brk-ink" "#e6e4df")
        hard  (css-var node "--brk-fixed" "#7d7a74")
        warn  (css-var node "--brk-bad" "#c0453a")
        px (fn [x] (* x sx))
        py (fn [y] (* (- H y) sy))]
    (.clearRect ctx 0 0 w h)
    (set! (.-lineWidth ctx) 1)

    (doseq [[[r c] kind] (:wall s)]
      (let [x1 (px (+ (* c BW) 0.4))
            y1 (py (+ WALL-BOTTOM (* r BH) BH -0.4))
            bw (* (- BW 0.8) sx) bh (* (- BH 0.8) sy)]
        (if (= kind :fixed)
          ;; permanent bricks are filled, because a thing you cannot
          ;; take out should not look like a thing you can
          (do (set! (.-fillStyle ctx) hard)
              (.fillRect ctx x1 y1 bw bh))
          (do (set! (.-strokeStyle ctx) ink)
              (.strokeRect ctx x1 y1 bw bh)))))

    (set! (.-strokeStyle ctx) ink)
    (set! (.-fillStyle ctx) ink)
    (doseq [{[x y] :p pierce :pierce} (:balls s)]
      (if (pos? (or pierce 0))
        (.strokeRect ctx (px (- x 1.3)) (py (+ y 1.3)) (* 2.6 sx) (* 2.6 sy))
        (.fillRect ctx (px (- x 1)) (py (+ y 1)) (* 2 sx) (* 2 sy))))

    (set! (.-font ctx) "bold 9px sans-serif")
    (set! (.-textAlign ctx) "center")
    (doseq [{[x y] :p kind :kind} (:drops s)]
      (let [bad (bad-now? kind) col (if bad warn ink)]
        (set! (.-strokeStyle ctx) col)
        (set! (.-fillStyle ctx) col)
        (.setLineDash ctx (if bad #js [3 2] #js []))
        (.strokeRect ctx (px (- x 2.2)) (py (+ y 2)) (* 4.4 sx) (* 4 sy))
        (.setLineDash ctx #js [])
        (.fillText ctx (:letter (by-kind kind)) (px x) (+ (py y) 3))))

    ;; put it back, or the bat comes out red
    (set! (.-strokeStyle ctx) ink)
    (set! (.-fillStyle ctx) ink)
    (let [half (bat-half s)]
      (.fillRect ctx (px (- (:bat s) half)) (py 2.6) (* (* 2 half) sx) (* 2.2 sy)))))

;; ---------------------------------------------------------------------
;; The key
;; ---------------------------------------------------------------------

(defn- key-html []
  (str/join ""
            (for [{:keys [kind letter is]} drops]
              (str "<div class='legend-item brk-drop" (when (bad-now? kind) " brk-bad") "'>"
                   "<span class='legend-glyph'><span class='brk-letter'>" letter "</span></span>"
                   "<span class='legend-label'>" (name kind) "</span>"
                   "<span class='legend-note'>" is "</span></div>"))))

(defn paint! [node]
  (let [s @st
        {:keys [broken lost walls]} (:tally s)
        fx (->> (:fx s) keys (map (fn [k] (if (bad-now? k) (str (name k) "!") (name k))))
                sort (str/join ", "))]
    (fedoras.ui.widget-ui/html! node ".brk-key" (key-html))
    (fedoras.ui.widget-ui/state!
     node
     (str broken " broken · " lost " lost · " (count (:balls s)) " in play"
          (when (seq fx) (str " · " fx))))
    (fedoras.reader/text!
     node ".brk-counts"
     (str "wall " walls " · " (breakable s) " to break · "
          (count (:fixed s)) " that cannot be"
          (when (zero? (breakable s)) " · there is nothing left to hit")))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- wire-canvas! [node]
  (when-let [cv (fedoras.ui.widget-ui/q! node ".brk-canvas")]
    (set! (.-onmousemove cv)
          (fn [e] (swap! st assoc :bat (* W (/ (.-offsetX e) (.-clientWidth cv))))))
    ;; four ways out of one state
    (set! (.-onmousedown cv) (fn [e] (.preventDefault e) (serve!)))
    (set! (.-onclick cv)     (fn [_] (serve!)))))

(defn- wire-keys! [node]
  (fedoras.ui.widget-ui/keys!
   node {" " :serve "r" :again "ArrowLeft" :left "ArrowRight" :right}
   (fn [_ what]
     (case what
       :serve (serve!)
       :again (reset! st (fresh))
       :left  (swap! st update :bat (fn [b] (max 0 (- b 4))))
       :right (swap! st update :bat (fn [b] (min W (+ b 4))))))))

(defn- wire-controls! [node]
  (fedoras.reader/on-click! node ".brk-again" (fn [_] (reset! st (fresh)) (paint! node))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current
                     (select-keys setting
                                  [:drop-chance :effect :serve :fixed-first
                                   :fixed-each :speed :bat :mark]))))
     (paint! node))))

(defn setup! [node]
  (let [id (.-id node)
        cv (fedoras.reader/q node ".brk-canvas")
        ctx (.getContext cv "2d")]
    (reset! known (set (map keyword (or (fedoras.ui.widget-ui/kept :brk-known) []))))
    (reset! st (fresh))

    (fedoras.ui.widget-ui/wire! node "canvas"   wire-canvas!)
    (fedoras.ui.widget-ui/wire! node "keys"     wire-keys!)
    (fedoras.ui.widget-ui/wire! node "controls" wire-controls!)
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))

    (letfn [(frame []
              (swap! st tick)
              (draw! node cv ctx)
              (when (zero? (mod (:frame @st) 15)) (paint! node))
              (js/requestAnimationFrame frame))]
      (frame))))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount! ::brk (or id "brk") setup! paint!))