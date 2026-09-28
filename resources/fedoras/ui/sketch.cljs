(ns fedoras.ui.sketch
  "The drawing kit a sketch cell is handed.

  This used to be forty lines of ClojureScript smuggled through string
  concatenation in `fedoras.sketch`, which meant a stray paren in the
  thing every animated cell in the book depends on was invisible until
  a browser refused it. It is a source file now, so the build reads it.

  Every function here takes a canvas map, made by `mount`. The short
  names the reader actually types -- `clear!`, `dot`, `poly`, `frames!`
  -- are bound per cell by a six-line header, because a cell needs its
  own canvas and its own frame counter and nothing else.")

(defn mount
  "Everything one cell needs, from the id of its canvas and the size of
  the world it draws in."
  [id world-w world-h]
  (let [cv  (.getElementById js/document (str id "-canvas"))
        ctx (.getContext cv "2d")
        cw  (.-width cv)
        ch  (.-height cv)]
    {:cv cv :ctx ctx :cw cw :ch ch
     :sx (/ cw world-w)
     :sy (/ ch world-h)
     ;; Each press of Run takes the next number. A loop whose number is
     ;; no longer current stops on its next frame, so a cell pressed
     ;; four times is not four loops racing each other.
     :run (atom 0)
     :i (atom 0)}))

(defn ink!
  "Both fill and stroke, because a cell that sets one and not the other
  is a cell that draws in two colours by accident."
  [{:keys [ctx]} colour]
  (set! (.-fillStyle ctx) colour)
  (set! (.-strokeStyle ctx) colour))

(defn page-ink
  "Whatever colour the surrounding prose is, so a drawing belongs to the
  page it is on and follows it into dark mode."
  [{:keys [cv]}]
  (.-color (js/getComputedStyle cv)))

(defn clear! [{:keys [ctx cw ch]}]
  (.clearRect ctx 0 0 cw ch))

(defn dot
  ([c x y] (dot c x y 1.2))
  ([{:keys [ctx sx sy ch]} x y size]
   (.fillRect ctx (* x sx) (- ch (* y sy)) size size)))

(defn poly
  "A closed outline through the points, in world coordinates."
  [{:keys [ctx sx sy ch]} pts]
  (when (seq pts)
    (let [[fx fy] (first pts)]
      (.beginPath ctx)
      (.moveTo ctx (* fx sx) (- ch (* fy sy)))
      (doseq [[x y] (rest pts)]
        (.lineTo ctx (* x sx) (- ch (* y sy))))
      (.closePath ctx)
      (.stroke ctx))))

(defn line-width! [{:keys [ctx]} w]
  (set! (.-lineWidth ctx) w))

(defn- tick! [{:keys [run i] :as c} generation total per f]
  (when (= generation @run)
    (dotimes [_ per]
      (when (< @i total) (f @i) (swap! i inc)))
    (when (< @i total)
      (js/requestAnimationFrame (fn [] (tick! c generation total per f)))))
  nil)

(defn frames!
  "Call (f i) total times, per of them per frame.

  Pressing Run again retires the loop that is already going. Without
  that, a cell run twice advances twice per frame and looks broken in a
  way nobody can debug from the outside."
  [{:keys [run i] :as c} total per f]
  (let [generation (swap! run inc)]
    (reset! i 0)
    (tick! c generation total per f))
  nil)

(defn on-pointer!
  "Pointer position in world coordinates, which is what a cell wants and
  never what the event gives it."
  [{:keys [cv sx sy] :as c} f]
  (set! (.-onmousemove cv)
        (fn [e]
          (f (/ (.-offsetX e) sx)
             (- (/ (.-height cv) sy) (/ (.-offsetY e) sy))))))