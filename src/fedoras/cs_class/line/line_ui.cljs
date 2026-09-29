(ns fedoras.cs-class.line.line-ui
  (:require [clojure.string :as str]
            [fedoras.reader :as read]
            [fedoras.ui.widget-ui :as wui]))

(def settings
  (atom {:size 20 :speed 8 :paid 4
         :find-pays 3 :quest-pays 1 :quest-multiplier 500
         :silt-per-find 2 :silt-every 0
         :quest-fast-min 160 :quest-fast-max 220
         :quest-mid-min  260 :quest-mid-max  340
         :quest-slow-min 400 :quest-slow-max 520
         :quest-speed 8
         :number-from 11}))

(defonce st     (atom nil))
(defonce number (atom nil))
(defonce ticks  (atom 0))
(defonce best   (atom nil)) ;; the best :score any drone here has reached
(defonce paused? (atom false))

;; ---------------------------------------------------------------------
;; Squares
;; ---------------------------------------------------------------------

(defn- size [] (:size @settings))

(defn ahead [[x y] [dx dy]]
  (let [n (size)] [(mod (+ x dx) n) (mod (+ y dy) n)]))

(defn- on-cable? [s p] (boolean (some #{p} (take (:paid s) (:cable s)))))

(defn- taken?
  "Everything a drone cannot be in. Its own line back to what has been
  paid out, and whatever has silted up since it came down."
  [s p]
  (or (on-cable? s p) (contains? (:silt s) p)))

(defn- somewhere-free
  "A square with nothing in it, or nil when there is no such thing left,
  which is a state this tunnel can actually reach."
  [s]
  (let [n (size)]
    (loop [tries 0]
      (let [p [(int (* (js/Math.random) n)) (int (* (js/Math.random) n))]]
        (cond
          (> tries 300) nil
          (or (taken? s p)
              (= p (:drone s))
              (= p (:find s))
              (some #{p} (map :at (:quests s)))) (recur (inc tries))
          :else p)))))

;; ---------------------------------------------------------------------
;; The silt
;;
;; It fills from the south, one square at a time, in a column where
;; there is a low square nobody is using. It tries a few columns before
;; giving up on a square, because taking the first column it thought of
;; and then finding the cable in it meant the silt quietly under-
;; delivered and the floor came up more slowly than the settings said.
;; ---------------------------------------------------------------------

(defn- raise-one [s]
  (let [w (size)]
    (loop [tries 0]
      (if (> tries 8)
        s
        (let [col (int (* (js/Math.random) w))
              row (first (remove (fn [r] (contains? (:silt s) [col r])) (range w)))
              p   [col row]]
          (if (and row
                   (not= p (:drone s))
                   (not= p (:find s))
                   (not (some #{p} (map :at (:quests s))))
                   (not= p (:shaft s))
                   (not (on-cable? s p)))
            (update s :silt conj p)
            (recur (inc tries))))))))

(defn- raise-silt [s n] (reduce (fn [acc _] (raise-one acc)) s (range n)))

;; ---------------------------------------------------------------------
;; The drone
;; ---------------------------------------------------------------------

(defn turn
  "Against the heading it last moved on, not the one it was last given."
  [{:keys [moved-on] :as s} [dx dy]]
  (if (= [dx dy] [(- (first moved-on)) (- (second moved-on))])
    s
    (assoc s :heading [dx dy])))

(defn- retrieve [s p pays]
  (assoc s :drone p
         :cable (cons p (:cable s))
         :paid (+ (:paid s) pays)
         :got (inc (:got s))
         :moved-on (:heading s)))

(declare spawn-quests) ;; the three waiting on whatever square comes next

(defn step
  [{:keys [drone heading find quests fouled] :as s}]
  (if fouled
    s
    (let [p      (ahead drone heading)
          caught (first (filter #(= p (:at %)) quests))]
      (cond
        (taken? s p)
        (assoc s :fouled true :why :line :moved-on heading)

        ;; a square, worth one more than the last one was — one, three,
        ;; six, ten, the kind of number that gets steeper because the
        ;; floor does too
        (= p find)
        (let [n  (inc (:squares s))
              s' (-> (retrieve s p (:find-pays @settings))
                     (assoc :squares n)
                     (update :score + n)
                     (raise-silt (:silt-per-find @settings))
                     (spawn-quests))]
          (if-let [next-find (somewhere-free s')]
            (assoc s' :find next-find)
            ;; there is nowhere left to put anything, which is the other
            ;; way this ends and the only one that is not a mistake
            (assoc s' :fouled true :why :filled :find nil)))

        ;; a foot for reaching it, on its own. the real prize is the
        ;; third one — clear the floor of all three before any of them
        ;; catch up with it, and that square's whole bonus lands once,
        ;; on top of everything else
        caught
        (let [s1     (-> (retrieve s p (:quest-pays @settings))
                         (update :quests-caught inc)
                         (update :quests (fn [qs] (remove #(= (:at %) p) qs))))
              done?  (= 3 (:quests-caught s1))
              span   (if done? 30 18)
              s2     (update s1 :sparks conj {:at p :life span :span span :hue (:hue caught)})]
          (if done?
            (-> s2
                (update :score + (* (:quest-multiplier @settings) (inc (:squares s))))
                (update :quests-completed inc))
            s2))

        :else
        (assoc s :drone p
               :cable (take (:paid s) (cons p (:cable s)))
               :moved-on heading)))))

;; ---------------------------------------------------------------------
;; The quests
;;
;; Three of them, one to a floor, each a different colour and each
;; fading at its own rate — which rate goes with which colour is
;; reshuffled every floor, so it is never the same twice in a row and
;; the player has to actually look. Whichever fades fastest is worth
;; going to first; the legend above the field shows that order, left
;; to right, the moment the floor starts.
;;
;; They start at full colour and fade toward the floor as their time
;; runs out - brightness is the clock. Each one on its own is worth a
;; foot, same as always. The quest is getting all three before any of
;; them catch up with the floor: do that and the floor's whole
;; square-bonus lands once, on top. Miss one and the other two still
;; pay out, they just don't add up to anything more than themselves.
;;
;; Each one only ever lands somewhere with a way out from it; an open
;; square next door, not silt or cable on every side. As the floor
;; fills in that gets harder to arrange, and eventually stops being
;; possible at all, at which point it stops pretending and just uses
;; whatever is free.
;; ---------------------------------------------------------------------

(defn- has-escape?
  "At least one square next door that isn't silt or cable — reaching
  this one shouldn't be how the line runs out of floor."
  [s p]
  (boolean (some (fn [d] (not (taken? s (ahead p d)))) [[1 0] [-1 0] [0 1] [0 -1]])))

(defn- somewhere-free-and-escapable
  "Like somewhere-free, but only a square that still has a way out from
  it. If the floor is too far gone for that to exist anywhere, falls
  back to anywhere free at all — that's its own kind of answer."
  [s]
  (let [n (size)]
    (loop [tries 0]
      (let [p [(int (* (js/Math.random) n)) (int (* (js/Math.random) n))]]
        (cond
          (> tries 300) (somewhere-free s)
          (or (taken? s p)
              (= p (:drone s))
              (= p (:find s))
              (some #{p} (map :at (:quests s)))
              (not (has-escape? s p)))
          (recur (inc tries))
          :else p)))))

(defn- rand-between [lo hi]
  (+ lo (int (* (js/Math.random) (inc (- hi lo))))))

(defn- spawn-quests [s]
  (let [{:keys [quest-fast-min quest-fast-max
                quest-mid-min  quest-mid-max
                quest-slow-min quest-slow-max]} @settings
        durations (shuffle [(rand-between quest-fast-min quest-fast-max)
                            (rand-between quest-mid-min  quest-mid-max)
                            (rand-between quest-slow-min quest-slow-max)])
        ;; the reading order for the legend: fastest fading to slowest,
        ;; whichever hue that lands on this floor
        legend (->> (map vector [0 1 2] durations)
                    (sort-by second)
                    (mapv first))]
    (reduce
     (fn [acc [hue life]]
       (if-let [p (somewhere-free-and-escapable acc)]
         (update acc :quests conj {:at p :left life :life life :hue hue})
         acc))
     (assoc s :quests [] :legend legend :quests-caught 0)
     (map vector [0 1 2] durations))))

(defn- quests-turn [s]
  (if (:fouled s)
    s
    (let [quest-speed (:quest-speed @settings)
          step-one
          (fn [q]
            (let [q (update q :left dec)]
              (cond
                ;; buried, or its time is up — either way, gone, and
                ;; nothing marks which
                (or (<= (:left q) 0) (contains? (:silt s) (:at q)))
                nil

                (not (zero? (mod @ticks quest-speed)))
                q

                :else
                (let [ps (shuffle (map (fn [d] (ahead (:at q) d)) [[1 0] [-1 0] [0 1] [0 -1]]))
                      p  (first (remove (fn [x] (or (taken? s x)
                                                    (= x (:drone s))
                                                    (= x (:find s))))
                                        ps))]
                  (if p (assoc q :at p) q)))))]
      (update s :quests (fn [qs] (vec (remove nil? (map step-one qs))))))))

(defn- drip
  "The optional slow one, for a reader who wants the floor to come up
  whether they are working or not. Off by default."
  [s]
  (let [every (:silt-every @settings)]
    (if (and (pos? every) (zero? (mod @ticks every)) (not (:fouled s)))
      (raise-one s)
      s)))

(defn- tick-sparks
  "The little ring where something living just stopped moving. It does
  not last, on purpose; it is a moment, not a mark on the floor."
  [s]
  (update s :sparks
          (fn [sparks]
            (->> sparks
                 (map #(update % :life dec))
                 (remove #(<= (:life %) 0))
                 vec))))

;; ---------------------------------------------------------------------
;; Drones
;; ---------------------------------------------------------------------

(defn- fresh []
  (let [n (size) mid (int (/ n 2))]
    (spawn-quests
     {:drone [mid mid] :shaft [mid mid]
      :cable (list [mid mid]) :paid (:paid @settings)
      :heading [1 0] :moved-on [1 0]
      :find [(min (dec n) (+ mid 5)) mid]
      :silt #{} :quests [] :legend [] :got 0 :quests-caught 0 :quests-completed 0
      :squares 0 :score 0 :sparks []
      :fouled false :why nil})))

(defn next-drone!
  "A clean floor. The silt goes with the drone that raised it, and the
  number goes with nothing."
  []
  (swap! number inc)
  (wui/keep! :line-number @number)
  (reset! paused? false)
  (reset! st (fresh)))

;; ---------------------------------------------------------------------
;; Drawing
;; ---------------------------------------------------------------------

(defn- css-var [node nm fallback]
  (or (try (let [v (.getPropertyValue (js/getComputedStyle node) nm)]
             (when-not (str/blank? v) (str/trim v)))
           (catch :default _ nil))
      fallback))

(defn- quest-palette [node]
  [(css-var node "--line-quest-1" "#d8a657")
   (css-var node "--line-quest-2" "#6fa8a0")
   (css-var node "--line-quest-3" "#b8788f")])

(defn- wrapped?
  "True when a step from one square to the next only makes sense
  through the side, not across the middle of the board."
  [[x1 y1] [x2 y2]]
  (or (> (js/Math.abs (- x1 x2)) 1)
      (> (js/Math.abs (- y1 y2)) 1)))

(defn draw! [node cv ctx]
  (let [s  @st
        n  (size)
        w  (.-width cv)
        u  (/ w n)
        ink   (css-var node "--line-ink" "#e6e4df")
        cable (css-var node "--line-cable" "#9d8dff")
        mud   (css-var node "--line-silt" "#4a4640")
        quest-colors (quest-palette node)
        hue-color (fn [h] (nth quest-colors (or h 0)))
        pause-scrim (css-var node "--line-pause-scrim" "rgba(8,8,10,0.6)")
        pause-text  (css-var node "--line-pause-text" "#f2efe8")
        px (fn [x] (+ (* x u) (/ u 2)))
        py (fn [y] (+ (* (- n 1 y) u) (/ u 2)))
        box (fn [[x y] col fill?]
              (let [a (+ (* x u) 1) b (+ (* (- n 1 y) u) 1) d (- u 2)]
                (if fill?
                  (do (set! (.-fillStyle ctx) col) (.fillRect ctx a b d d))
                  (do (set! (.-strokeStyle ctx) col) (.strokeRect ctx a b d d)))))]

    (.clearRect ctx 0 0 w w)
    (set! (.-lineWidth ctx) 1)

    ;; what has come up since the drone went down
    (doseq [p (:silt s)] (box p mud true))

    ;; the shaft. the cable does not reach it and is not going to.
    (let [[sx sy] (:shaft s)]
      (set! (.-strokeStyle ctx) cable)
      (set! (.-globalAlpha ctx) 0.5)
      (.beginPath ctx)
      (.arc ctx (px sx) (py sy) (* u 0.42) 0 6.284)
      (.stroke ctx)
      (set! (.-globalAlpha ctx) 1))

    ;; the cable, which is the only thing on this board that remembers.
    ;; two squares that are next to each other through the side are
    ;; still next to each other, not a line drawn clean across the
    ;; floor between them.
    (when (seq (:cable s))
      (set! (.-strokeStyle ctx) cable)
      (set! (.-lineWidth ctx) (max 2 (* u 0.28)))
      (set! (.-lineJoin ctx) "round")
      (set! (.-lineCap ctx) "round")
      (.beginPath ctx)
      (loop [pts (:cable s) prev nil]
        (when (seq pts)
          (let [[x y] (first pts)]
            (if (or (nil? prev) (wrapped? prev [x y]))
              (.moveTo ctx (px x) (py y))
              (.lineTo ctx (px x) (py y)))
            (recur (rest pts) [x y]))))
      (.stroke ctx)
      (set! (.-lineWidth ctx) 1))

    ;; the thing it is looking for, and the three that will not wait —
    ;; each one's brightness is its own clock, counting down to the
    ;; colour of the floor
    (when-let [f (:find s)] (box f ink false))
    (doseq [{:keys [at left life hue]} (:quests s)]
      (set! (.-globalAlpha ctx) (max 0 (min 1 (/ left (max 1 life)))))
      (let [[x y] at]
        (set! (.-fillStyle ctx) (hue-color hue))
        (.beginPath ctx)
        (.arc ctx (px x) (py y) (* u 0.26) 0 6.284)
        (.fill ctx))
      (set! (.-globalAlpha ctx) 1))

    ;; the ring where something loose just stopped being loose
    (doseq [{:keys [at life span hue]} (:sparks s)]
      (let [[x y] at
            t (- 1 (/ life (max 1 span)))]
        (set! (.-globalAlpha ctx) (max 0 (- 1 t)))
        (set! (.-strokeStyle ctx) (hue-color hue))
        (set! (.-lineWidth ctx) 2)
        (.beginPath ctx)
        (.arc ctx (px x) (py y) (* u (+ 0.3 (* t 0.55))) 0 6.284)
        (.stroke ctx))
      (set! (.-globalAlpha ctx) 1)
      (set! (.-lineWidth ctx) 1))

    (box (:drone s) ink true)

    (when @paused?
      (set! (.-fillStyle ctx) pause-scrim)
      (.fillRect ctx 0 0 w w)
      (set! (.-textAlign ctx) "center")
      (set! (.-textBaseline ctx) "middle")
      (set! (.-fillStyle ctx) pause-text)
      (set! (.-font ctx) (str "bold " (int (* w 0.11)) "px monospace"))
      (.fillText ctx "PAUSED" (/ w 2) (/ w 2))
      (set! (.-font ctx) (str (int (* w 0.032)) "px monospace"))
      (set! (.-globalAlpha ctx) 0.7)
      (.fillText ctx "space to resume" (/ w 2) (+ (/ w 2) (* w 0.09)))
      (set! (.-globalAlpha ctx) 1))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn- render-legend!
  "The three colours in the order this floor fades them, fastest to
  slowest — read it once when the floor starts, then just glance."
  [node s]
  (when-let [el (read/q node ".line-legend")]
    (let [palette (quest-palette node)]
      (set! (.-innerHTML el) "")
      (doseq [h (:legend s)]
        (let [chip (.createElement js/document "span")]
          (set! (.-className chip) "line-legend-chip")
          (set! (.. chip -style -background) (nth palette h))
          (.appendChild el chip))))))

(defn paint! [node]
  (let [s @st
        floor (* (size) (size))
        stats (str "drone " @number " · " (:score s) " points"
                   (when (and @best (pos? @best)) (str " · best " @best))
                   " · " (:paid s) " feet of line"
                   (case (:why s)
                     :line   " · FOULED"
                     :filled " · NOWHERE LEFT TO PUT ANYTHING"
                     ""))]
    (render-legend! node s)
    ;; the numbers live next to the dots here, not in the frame's own
    ;; header — this one wants to be read as one row, not two places
    (wui/state! node "")
    (read/text! node ".line-stats" stats)
    (read/text!
     node ".line-counts"
     (str (count (:silt s)) " of " floor " squares have come up"
          (when (pos? (:quests-completed s)) (str " · " (:quests-completed s) " quests done"))
          (when (seq (:quests s)) " · something is moving")
          (when (:fouled s) " · space for another one, and a clean floor")))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(def keymap
  {"ArrowUp" [0 1] "w" [0 1]
   "ArrowDown" [0 -1] "s" [0 -1]
   "ArrowLeft" [-1 0] "a" [-1 0]
   "ArrowRight" [1 0] "d" [1 0]})

(defn- wire-keys! [node]
  (set! (.-onkeydown node)
        (fn [e]
          (let [k (.-key e)]
            (cond
              (get keymap k) (do (.preventDefault e) (swap! st turn (get keymap k)))
              (= " " k)      (do (.preventDefault e)
                                 (if (:fouled @st)
                                   (do (next-drone!) (paint! node))
                                   (do (swap! paused? not) (paint! node))))
              (= "r" k)      (do (.preventDefault e) (next-drone!) (paint! node))
              :else nil)))))

(defn- wire-controls! [node]
  (read/on-click! node ".line-next"
                            (fn [_] (when (:fouled @st) (next-drone!)) (paint! node))))

(defn- wire-settings! [node id]
  (wui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current
                     (select-keys setting
                                  [:size :speed :paid :find-pays
                                   :quest-pays :quest-multiplier
                                   :silt-per-find :silt-every :number-from
                                   :quest-fast-min :quest-fast-max
                                   :quest-mid-min  :quest-mid-max
                                   :quest-slow-min :quest-slow-max
                                   :quest-speed]))))
     (reset! paused? false)
     (reset! st (fresh))
     (paint! node))))

(defn setup! [node]
  (let [id  (.-id node)
        cv  (read/q node ".line-canvas")
        ctx (.getContext cv "2d")]
    ;; it did not start at one, and it was not his
    (reset! number (or (wui/kept :line-number)
                       (inc (:number-from @settings))))
    (reset! best (or (wui/kept :line-best) 0))
    (reset! paused? false)
    (reset! st (fresh))

    (wui/wire! node "keys"     wire-keys!)
    (wui/wire! node "controls" wire-controls!)
    (wui/wire! node "settings" (fn [n] (wire-settings! n id)))
    (set! (.-onmousedown cv) (fn [_] (.focus node)))

    (letfn [(frame []
              (try
                (if @paused?
                  ;; frozen, on purpose — nothing moves, nothing fades,
                  ;; and the clock itself doesn't run either, so nothing
                  ;; catches up on the frame it comes back
                  (draw! node cv ctx)
                  (do
                    (swap! ticks inc)
                    ;; six moments a second, which is as fast as a thing
                    ;; on tracks goes
                    (let [was (:fouled @st)]
                      (when (zero? (mod @ticks (:speed @settings)))
                        (swap! st step))
                      (swap! st quests-turn)
                      (swap! st drip)
                      (swap! st tick-sparks)
                      ;; the number that outlives the drone
                      (when (> (:score @st) @best)
                        (reset! best (:score @st))
                        (wui/keep! :line-best @best))
                      (draw! node cv ctx)
                      ;; the moment it fouls, rather than up to twelve
                      ;; frames afterwards
                      (when (or (not= was (:fouled @st)) (zero? (mod @ticks 12)))
                        (paint! node)))))
                (catch :default e
                  ;; a bad frame should cost a frame, not the whole run
                  (js/console.error "line: frame failed, skipping" e)))
              (js/requestAnimationFrame frame))]
      (frame))))

(defn mount! [{:keys [id]}]
  (wui/mount! ::line (or id "line") setup! paint!))