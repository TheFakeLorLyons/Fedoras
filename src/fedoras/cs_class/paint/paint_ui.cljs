(ns fedoras.cs-class.paint.paint-ui
  "A drawing surface that keeps everything.

  THE RUBBER DOES NOT DELETE. Right-click switches to it, and clicking a
  mark writes a time onto that mark. The mark stays in the vector, it
  stays on the picture in the faint ink, and the ledger goes on counting
  it. There is no function in this namespace that removes anything from
  `marks`, which is the joke, and is the reason the counts under the
  picture are worth reading.

  SIMPLIFICATION IS THE REAL DELETION. A dragged line arrives as a few
  hundred points and is filed as a dozen, by Ramer-Douglas-Peucker,
  because the whole path is more than anybody needs. `tolerance` decides
  how much more, it is on a slider, and refiling applies today's setting
  to marks made before it. Both counts are under the picture. Neither of
  them is the count that changed.

  THE ONLY BUTTON THAT LOSES A WHOLE MARK loses all of them. A single
  mark cannot be got rid of. The sheet can. That is the shape of most
  retention policies and it is not a coincidence."
 (:require [clojure.string :as str]))

(defonce measured (atom #{}))

(def world 100)

;; ---------------------------------------------------------------------
;; Geometry. No DOM in this section.
;; ---------------------------------------------------------------------

(defn- gap [[ax ay] [bx by]]
  (js/Math.sqrt (+ (* (- bx ax) (- bx ax)) (* (- by ay) (- by ay)))))

(defn distance-to-line
  [[px py] [ax ay] [bx by]]
  (let [dx (- bx ax) dy (- by ay)
        len (js/Math.sqrt (+ (* dx dx) (* dy dy)))]
    (if (zero? len) 0
        (/ (js/Math.abs (- (* dx (- ay py)) (* (- ax px) dy))) len))))

(defn simplify
  "Ramer-Douglas-Peucker. Keeps the corners and throws away the rest."
  [pts tol]
  (if (< (count pts) 3)
    (vec pts)
    (let [pts (vec pts) a (first pts) b (peek pts)
          worst (apply max-key (fn [i] (distance-to-line (nth pts i) a b))
                       (range 1 (dec (count pts))))]
      (if (> (distance-to-line (nth pts worst) a b) tol)
        (vec (concat (butlast (simplify (subvec pts 0 (inc worst)) tol))
                     (simplify (subvec pts worst) tol)))
        [a b]))))

(defn resample
  "A point every `step` or so along the path. The rubber needs something
  to take hold of in the middle of a long line, and a line filed as two
  points has nothing there."
  [shape step]
  (if (< (count shape) 2)
    (vec shape)
    (vec (concat
          (mapcat
           (fn [[[ax ay :as a] [bx by :as b]]]
             (let [n (max 1 (int (/ (gap a b) step)))]
               (map (fn [i]
                      (let [t (/ i n)]
                        [(+ ax (* t (- bx ax))) (+ ay (* t (- by ay)))]))
                    (range n))))
           (partition 2 1 shape))
          [(last shape)]))))

;; ---------------------------------------------------------------------
;; What is on the sheet
;; ---------------------------------------------------------------------

(def settings
  (atom {:tolerance 0.6 :reach 6.0 :strength 0.05 :spread 0.5
         :grain 1.5 :ledger 6 :weight 1.4
         :weight-min 0.4 :weight-max 4.0
         :strength-min 0.005 :strength-max 0.4
         :tolerance-min 0.02 :tolerance-max 6.0
         :show-withdrawn true :floor 0.12 :fill-alpha 0.35
         :inks [{:k :ink     :label "ink"    :var "--ink"}
                {:k :violet  :label "violet" :var "--violet"}
                {:k :gold    :label "gold"   :var "--gold"}
                {:k :coral   :label "coral"  :var "--coral"}
                {:k :green   :label "green"  :var "--green"}
                {:k :blurple :label "blue"   :var "--blurple"}]}))

(def tools
  [{:k :pen     :label "pen"       :glyph "<path d='M4 20l3-1 10-10-2-2L5 17z'/>"}
   {:k :line    :label "line"      :glyph "<path d='M5 19L19 5'/>"}
   {:k :rect    :label "rectangle" :glyph "<path d='M5 7h14v10H5z'/>"}
   {:k :ellipse :label "ellipse"   :glyph "<ellipse cx='12' cy='12' rx='8' ry='5.5'/>"}
   {:k :rubber  :label "rubber"    :glyph "<path d='M5 19h14M7 15l7-7 4 4-7 7z'/>"}])

(def closed? #{:rect :ellipse})

(defonce marks (atom []))
(defonce in-progress (atom nil))
(defonce tool (atom :pen))
(defonce ink (atom :ink))
(defonce filling (atom false))
(defonce sheet (atom 1))

(defn- pad [n] (if (< n 10) (str "0" n) (str n)))

(defn- clock []
  (let [now (js/Date.)]
    (str (pad (.getHours now)) ":" (pad (.getMinutes now)) ":" (pad (.getSeconds now)))))

(defn- shape-points
  [kind [ax ay] [bx by]]
  (case kind
    :line [[ax ay] [bx by]]
    :rect [[ax ay] [bx ay] [bx by] [ax by] [ax ay]]
    :ellipse (let [cx (/ (+ ax bx) 2) cy (/ (+ ay by) 2)
                   rx (/ (js/Math.abs (- bx ax)) 2)
                   ry (/ (js/Math.abs (- by ay)) 2)]
               (mapv (fn [i]
                       (let [t (* 2 js/Math.PI (/ i 32))]
                         [(+ cx (* rx (js/Math.cos t))) (+ cy (* ry (js/Math.sin t)))]))
                     (range 33)))
    nil))

(defn- traced
  "Give a mark a trace and a strength per point of it."
  [m]
  (let [t (resample (:shape m) (:grain @settings))]
    (assoc m :trace t :alpha (vec (repeat (count t) 1.0)))))

(defn record!
  [{:keys [kind pts]}]
  (let [tol (:tolerance @settings)
        shape (if (= kind :pen) (simplify (vec pts) tol) (vec pts))]
    (when (> (count shape) 1)
      (swap! marks conj
             (traced {:n (inc (count @marks))
                      :kind kind
                      :raw (count pts)
                      :path (vec pts)
                      :shape shape
                      :filed-at tol
                      :weight (:weight @settings)
                      :ink @ink
                      :fill (and (closed? kind) @filling)
                      :withdrawn nil})))))

;; ---------------------------------------------------------------------
;; The rubber
;; ---------------------------------------------------------------------

(defn- rub-mark
  "What comes off a point is partly given to the points either side, so
  the middle lightens and the edge of the rub darkens a little, and a
  line goes soft before it goes."
  [m point]
  (let [{:keys [reach strength spread]} @settings
        trace (:trace m)
        alpha (:alpha m)
        n (count alpha)
        off (mapv (fn [i]
                    (let [d (gap (nth trace i) point)]
                      (if (< d reach)
                        (min (nth alpha i) (* strength (- 1.0 (/ d reach))))
                        0.0)))
                  (range n))]
    (if (every? zero? off)
      m
      (let [next (mapv (fn [i]
                         (let [given (* spread 0.5
                                        (+ (nth off (max 0 (dec i)))
                                           (nth off (min (dec n) (inc i)))))]
                           (max 0.0 (min 1.0 (+ (- (nth alpha i) (nth off i)) given)))))
                       (range n))]
        (assoc m :alpha next :withdrawn (or (:withdrawn m) (clock)))))))

(defn withdraw-at [ms point] (mapv (fn [m] (rub-mark m point)) ms))

;; ---------------------------------------------------------------------

(defn refile!
  "Apply the current tolerance to every freehand mark, including the ones
  made before it was set. The mark count does not move. The point count
  does.
 
  The strengths go back to full, because they were a strength per point
  of a trace and there is a different trace now. So what was withdrawn
  comes back, and the time it was withdrawn at stays in the ledger, and
  nothing in this program says that is going to happen."
  []
  (let [tol (:tolerance @settings)]
    (swap! marks
           (fn [ms]
             (mapv (fn [m]
                     (if (= :pen (:kind m))
                       (traced (assoc m :shape (simplify (:path m) tol) :filed-at tol))
                       (traced m)))
                   ms)))))

(defn fresh-sheet! []
  (reset! marks [])
  (reset! in-progress nil)
  (swap! sheet inc))

(defn- withdrawn? [m] (every? (fn [a] (< a 0.02)) (:alpha m)))

(defn tally []
  (let [ms @marks]
    {:total (count ms)
     :gone  (count (filter withdrawn? ms))
     :shown (count (remove withdrawn? ms))
     :drawn (reduce + 0 (map :raw ms))
     :kept  (reduce + 0 (map (fn [m] (count (:shape m))) ms))}))

(defn as-text
  [state-sheet ms]
  (str ";; sheet " state-sheet ", " (count ms) " marks, none deleted\n"
       (pr-str (mapv (fn [m]
                       {:n (:n m) :kind (:kind m) :raw (:raw m)
                        :kept (count (:shape m)) :traced (count (:trace m))
                        :filed-at (:filed-at m) :ink (:ink m) :fill (:fill m)
                        :withdrawn (:withdrawn m)
                        :strength (:alpha m) :shape (:shape m)})
                     ms))
       "\n"))

;; ---------------------------------------------------------------------
;; Ink
;; ---------------------------------------------------------------------

(defn- css-var [node property fallback]
  (let [v (.getPropertyValue (js/getComputedStyle node) property)]
    (if (str/blank? v) fallback (str/trim v))))

(defn- ink-var [k]
  (or (:var (first (filter (fn [i] (= k (:k i))) (:inks @settings)))) "--ink"))

(defn- fit! [canvas]
  (let [dpr (or (.-devicePixelRatio js/window) 1)
        w (.-clientWidth canvas) h (.-clientHeight canvas)]
    (when (and (pos? w) (pos? h))
      (let [ww (js/Math.round (* w dpr)) hh (js/Math.round (* h dpr))]
        (when (or (not= ww (.-width canvas)) (not= hh (.-height canvas)))
          (set! (.-width canvas) ww)
          (set! (.-height canvas) hh))
        {:w w :h h :dpr dpr}))))


(defn keep->tolerance
  "A hundred is keep everything. Nought is keep the corners.
 
  Geometric rather than straight, so that the half of the slider a
  reader will actually use is not the last inch of it."
  [pos]
  (let [{:keys [tolerance-min tolerance-max]} @settings
        k (/ pos 100.0)]
    (* tolerance-min (js/Math.pow (/ tolerance-max tolerance-min) (- 1.0 k)))))

(defn tolerance->keep
  "And back, so a tolerance set in the settings file puts the slider
  where it belongs."
  [t]
  (let [{:keys [tolerance-min tolerance-max]} @settings
        t (max tolerance-min (min tolerance-max t))]
    (js/Math.round
     (* 100 (- 1.0 (/ (js/Math.log (/ t tolerance-min))
                      (js/Math.log (/ tolerance-max tolerance-min))))))))

(defn- runs
  "Consecutive segments at the same strength, so an untouched mark draws
  as one path rather than as sixty."
  [alpha]
  (let [band (fn [i] (js/Math.round (* 16 (min (nth alpha i) (nth alpha (inc i))))))]
    (loop [i 0 start 0 out []]
      (cond
        (>= i (dec (count alpha)))
        (if (> i start) (conj out [start i (band (dec i))]) out)
        (and (> i start) (not= (band i) (band (dec i))))
        (recur (inc i) i (conj out [start i (band (dec i))]))
        :else (recur (inc i) start out)))))

(defn- path! [ctx sx sy pts from to]
  (.beginPath ctx)
  (doseq [i (range from (inc to))]
    (let [[x y] (nth pts i) px (* x sx) py (* (- world y) sy)]
      (if (= i from) (.moveTo ctx px py) (.lineTo ctx px py))))
  nil)

(defn- trace-mark! [ctx canvas sx sy m]
  (let [{:keys [show-withdrawn floor fill-alpha]} @settings
        colour (css-var canvas (ink-var (:ink m)) "#e6e4df")
        trace (:trace m) alpha (:alpha m)
        mean (/ (reduce + 0 alpha) (max 1 (count alpha)))]
    (set! (.-strokeStyle ctx) colour)
    (set! (.-fillStyle ctx) colour)
    (set! (.-lineWidth ctx) (or (:weight m) 1.4))

    ;; the fill first, at the strength the mark has left on average, so
    ;; rubbing part of a shape thins the whole of what is inside it
    (when (and (:fill m) (> mean 0.02))
      (set! (.-globalAlpha ctx) (* mean fill-alpha))
      (path! ctx sx sy (:shape m) 0 (dec (count (:shape m))))
      (.closePath ctx)
      (.fill ctx))

    (doseq [[from to band] (runs alpha)]
      (let [raw (/ band 16.0)
            a   (if show-withdrawn (max raw floor) raw)]
        (when (> a 0.02)
          (set! (.-globalAlpha ctx) a)
          (path! ctx sx sy trace from to)
          (.stroke ctx))))
    (set! (.-globalAlpha ctx) 1)))

(defn- draw! [canvas]
  (when-let [{:keys [w h dpr]} (fit! canvas)]
    (let [ctx (.getContext canvas "2d")
          sx (/ w world) sy (/ h world)]
      (.setTransform ctx dpr 0 0 dpr 0 0)
      (.clearRect ctx 0 0 w h)
      (set! (.-lineCap ctx) "round")
      (set! (.-lineJoin ctx) "round")
      (doseq [m @marks] (trace-mark! ctx canvas sx sy m))
      (when-let [{:keys [kind pts]} @in-progress]
        (let [v (vec pts) colour (css-var canvas (ink-var @ink) "#e6e4df")]
          (set! (.-strokeStyle ctx) colour)
          (set! (.-fillStyle ctx) colour)
          (set! (.-lineWidth ctx) (:weight @settings))
          (when (and @filling (closed? kind) (> (count v) 2))
            (set! (.-globalAlpha ctx) (* 0.7 (:fill-alpha @settings)))
            (path! ctx sx sy v 0 (dec (count v)))
            (.closePath ctx)
            (.fill ctx))
          (set! (.-globalAlpha ctx) 0.7)
          (path! ctx sx sy v 0 (dec (count v)))
          (.stroke ctx)
          (set! (.-globalAlpha ctx) 1))))))

;; ---------------------------------------------------------------------
;; Copying the picture
;;
;; Composited onto the paper first, because the canvas has no background
;; and a transparent PNG on a clipboard is a rectangle of nothing on
;; most of the places anybody would paste it.
;; ---------------------------------------------------------------------

(defn- on-paper [canvas]
  (let [w (.-width canvas) h (.-height canvas)
        off (.createElement js/document "canvas")]
    (set! (.-width off) w)
    (set! (.-height off) h)
    (let [c (.getContext off "2d")]
      (set! (.-fillStyle c) (css-var canvas "--paper" "#faf8f4"))
      (.fillRect c 0 0 w h)
      (.drawImage c canvas 0 0))
    off))

(defn- save-png! [canvas]
  (let [a (.createElement js/document "a")]
    (set! (.-href a) (.toDataURL (on-paper canvas) "image/png"))
    (set! (.-download a) (str "sheet-" @sheet ".png"))
    (.click a)))

(defn copy-picture!
  "The clipboard wants a secure context. A book opened from a folder of
  files is not one, so this falls back to putting the picture in the
  downloads and says which it did."
  [node canvas]
  (let [clip (.-clipboard js/navigator)]
    (if (and clip (.-write clip) (exists? js/ClipboardItem))
      (.toBlob (on-paper canvas)
               (fn [blob]
                 (-> (.write clip (array (js/ClipboardItem. (js-obj "image/png" blob))))
                     (.then (fn [_] (fedoras.ui.widget-ui/state! node "copied")))
                     (.catch (fn [_]
                               (save-png! canvas)
                               (fedoras.ui.widget-ui/state! node "saved instead"))))))
      (do (save-png! canvas)
          (fedoras.ui.widget-ui/state! node "saved — the clipboard needs a served page")))))

;; ---------------------------------------------------------------------
;; The palettes
;; ---------------------------------------------------------------------

(defn- tools-html []
  (str/join ""
            (for [{:keys [k label glyph]} tools]
              (str "<button class='paint-pick" (when (= k @tool) " paint-chosen")
                   "' data-tool='" (name k) "' title='" label "'>"
                   "<svg viewBox='0 0 24 24'>" glyph "</svg></button>"))))

(defn- inks-html []
  (str/join ""
            (for [{:keys [k label var]} (:inks @settings)]
              (str "<button class='paint-swatch" (when (= k @ink) " paint-chosen")
                   "' data-ink='" (name k) "' title='" label
                   "' style='background: var(" var ")'></button>"))))

(defn palettes! [node]
  (fedoras.ui.widget-ui/html! node ".paint-tools" (tools-html))
  (fedoras.ui.widget-ui/html! node ".paint-inks" (inks-html))
  ;; the fill button is only there for the two tools it means anything to
  (when-let [b (fedoras.reader/q node ".paint-fill")]
    (set! (.-display (.-style b)) (if (closed? @tool) "inline-flex" "none"))
    (set! (.-className b) (str "paint-fill" (when @filling " paint-chosen"))))
  ;; the old markup had one button that said Rubber. if it is still
  ;; there, keep it working.
  (fedoras.reader/enable! node ".paint-mode-go" true
                          (if (= :rubber @tool) "Pen" "Rubber")))

;; ---------------------------------------------------------------------
;; The ledger
;; ---------------------------------------------------------------------

(defn- record-html [m]
  (str "<div class='paint-record" (when (withdrawn? m) " paint-struck") "'>"
       "<span class='paint-n'>" (:n m) "</span>"
       "<span class='paint-what'>" (:raw m) " points, " (count (:shape m)) " kept"
       (when (:fill m) " · filled") "</span>"
       "<span class='paint-when'>"
       (cond (withdrawn? m) (str "withdrawn at " (:withdrawn m))
             (:withdrawn m) (str "rubbed at " (:withdrawn m))
             :else          "on the picture")
       "</span></div>"))

(defn- ledger-html []
  (let [ms @marks depth (:ledger @settings) total (count ms)]
    (if (empty? ms)
      "<div class='paint-record paint-nothing'>nothing yet</div>"
      (str (when (> total depth)
             (str "<div class='paint-record paint-nothing'>"
                  (- total depth) " earlier, still in the file</div>"))
           (str/join "" (map record-html (take-last depth ms)))))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn paint! [node]
  (let [{:keys [total gone shown drawn kept]} (tally)
        {:keys [tolerance show-withdrawn]} @settings
        canvas (fedoras.reader/q node ".paint-canvas")]
    (when canvas (draw! canvas))
    (when (and canvas
               (not (contains? @measured (.-id node)))
               (pos? (.-clientWidth canvas)))
      (swap! measured conj (.-id node))
      (js/requestAnimationFrame (fn [_] (draw! canvas))))

    (.toggle (.-classList node) "paint-withdrawing" (= :rubber @tool))
    (fedoras.ui.widget-ui/state!
     node
     (str (name @tool) " · " shown " on the picture · " gone " withdrawn · "
          total " in the file · 0 deleted"))
    (fedoras.reader/text!
     node ".paint-points"
     (str drawn " points drawn · " kept " kept · " (- drawn kept) " gone"
          " · keep " (tolerance->keep tolerance) "%"
          " (" (.toFixed tolerance 2) ")"
          " · sheet " @sheet
          (when-not show-withdrawn " · the picture is not showing everything")))
    (fedoras.ui.widget-ui/html! node ".paint-ledger" (ledger-html))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- at [canvas event]
  [(* world (/ (.-offsetX event) (.-clientWidth canvas)))
   (- world (* world (/ (.-offsetY event) (.-clientHeight canvas))))])

(defn- capture! [canvas event]
  (try (.setPointerCapture canvas (.-pointerId event)) (catch :default _ nil)))

(defn- finish! [node]
  (when-let [p @in-progress]
    (record! p)
    (reset! in-progress nil)
    (paint! node)))

(defn- choose! [node what k]
  (case what
    :tool (reset! tool k)
    :ink  (reset! ink k)
    :fill (swap! filling not))
  (palettes! node)
  (paint! node))

(defn- wire-canvas! [node]
  (when-let [canvas (fedoras.ui.widget-ui/q! node ".paint-canvas")]
    (set! (.-onpointerdown canvas)
          (fn [e]
            (when (zero? (.-button e))
              (.preventDefault e)
              (capture! canvas e)
              (let [p (at canvas e)]
                (if (= :rubber @tool)
                  (swap! marks withdraw-at p)
                  (reset! in-progress {:kind @tool :anchor p :pts [p]})))
              (paint! node))))

    (set! (.-onpointermove canvas)
          (fn [e]
            (let [p (at canvas e)]
              (cond
                (and (= :rubber @tool) (pos? (.-buttons e)))
                (do (swap! marks withdraw-at p) (paint! node))

                @in-progress
                (do (swap! in-progress
                           (fn [{:keys [kind anchor pts] :as s}]
                             (if (= kind :pen)
                               (assoc s :pts (conj pts p))
                               (assoc s :pts (shape-points kind anchor p)))))
                    (paint! node))))))

    (set! (.-onpointerup canvas)     (fn [_] (finish! node)))
    (set! (.-onpointercancel canvas) (fn [_] (finish! node)))
    (set! (.-oncontextmenu canvas)
          (fn [e]
            (.preventDefault e)
            (choose! node :tool (if (= :rubber @tool) :pen :rubber))
            false))))

(defn- wire-palettes! [node]
  (palettes! node)
  (when-let [row (fedoras.reader/q node ".paint-tools")]
    (set! (.-onclick row)
          (fn [e]
            (when-let [b (.closest (.-target e) ".paint-pick")]
              (choose! node :tool (keyword (.getAttribute b "data-tool")))))))
  (when-let [row (fedoras.reader/q node ".paint-inks")]
    (set! (.-onclick row)
          (fn [e]
            (when-let [b (.closest (.-target e) ".paint-swatch")]
              (choose! node :ink (keyword (.getAttribute b "data-ink")))))))
  (fedoras.reader/on-click! node ".paint-fill" (fn [_] (choose! node :fill nil)))
  (fedoras.reader/on-click! node ".paint-mode-go"
                            (fn [_] (choose! node :tool (if (= :rubber @tool) :pen :rubber)))))

(defn- slider!
  "One slider. `read` turns a position into the value the setting wants,
  `write` puts it back, and `from` and `to` are positions."
  [node selector k read write from to]
  (when-let [c (fedoras.reader/q node selector)]
    (set! (.-min c) (str from))
    (set! (.-max c) (str to))
    (set! (.-value c) (str (write (get @settings k))))
    (set! (.-oninput c)
          (fn [_]
            (swap! settings assoc k (read (js/parseInt (.-value c) 10)))
            (paint! node)))))

(defn- wire-sliders! [node]
  (let [{:keys [weight-min weight-max strength-min strength-max]} @settings
        round (fn [n] (js/Math.round n))]
    (slider! node ".paint-weight" :weight
             (fn [p] (/ p 20.0)) (fn [v] (round (* v 20)))
             (round (* weight-min 20)) (round (* weight-max 20)))
    (slider! node ".paint-strength" :strength
             (fn [p] (/ p 1000.0)) (fn [v] (round (* v 1000)))
             (round (* strength-min 1000)) (round (* strength-max 1000)))
    (slider! node ".paint-reach" :reach
             (fn [p] (/ p 10.0)) (fn [v] (round (* v 10))) 10 200)
    (slider! node ".paint-spread" :spread
             (fn [p] (/ p 100.0)) (fn [v] (round (* v 100))) 0 100)
    (slider! node ".paint-tolerance" :tolerance
             keep->tolerance tolerance->keep 0 100)))

(defn- wire-sheet! [node]
  (fedoras.reader/on-click! node ".paint-fresh"
                            (fn [_] (fresh-sheet!) (paint! node)))
  (fedoras.reader/on-click! node ".paint-simplify"
                            (fn [_] (refile!) (paint! node)))
  (fedoras.reader/on-click! node ".paint-copy"
                            (fn [_] (when-let [c (fedoras.reader/q node ".paint-canvas")]
                                      (copy-picture! node c))))
  (fedoras.reader/on-click! node ".paint-download"
                            (fn [_] (fedoras.ui.widget-ui/download!
                                     (as-text @sheet @marks) (str "sheet-" @sheet ".edn")))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current
                     (select-keys setting
                                  [:tolerance :reach :strength :spread :grain
                                   :ledger :weight :weight-min :weight-max
                                   :strength-min :strength-max
                                   :inks :show-withdrawn :floor :fill-alpha]))))
     ;; the grain may have changed, so every trace is out of date
     (swap! marks (fn [ms] (mapv traced ms)))
     (wire-sliders! node)
     (palettes! node)
     (paint! node))))

(defn setup! [node]
  (let [id (.-id node)]
    (fedoras.ui.widget-ui/wire! node "canvas"   wire-canvas!)
    (fedoras.ui.widget-ui/wire! node "palettes" wire-palettes!)
    (fedoras.ui.widget-ui/wire! node "sliders"  wire-sliders!)
    (fedoras.ui.widget-ui/wire! node "sheet"    wire-sheet!)
    (fedoras.ui.widget-ui/wire!
     node "resize"
     (fn [n] (.addEventListener js/window "resize"
                                (fn [_] (when (fedoras.ui.widget-ui/running? (.-id n))
                                          (paint! n))))))
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount! ::paint (or id "paint") setup! paint!))