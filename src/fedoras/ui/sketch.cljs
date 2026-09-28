(ns fedoras.ui.sketch
  "The drawing kit a sketch cell is handed.

  This used to be forty lines of ClojureScript smuggled through string
  concatenation in `fedoras.sketch`, which meant a stray paren in the
  thing every animated cell in the book depends on was invisible until
  a browser refused it. It is a source file now, so the build reads it.

  Every function here takes a canvas map, made by `mount`. The short
  names the reader actually types -- `clear!`, `dot`, `poly`, `frames!`
  -- are bound per cell by a six-line header, because a cell needs its
  own canvas and its own frame counter and nothing else.

  WORLD COORDINATES. A cell says how big its world is and draws in
  those units, with y going up, because a screenplay about people who
  cannot see what is under them should not also ask its reader to
  remember that the screen counts downwards."
  (:require [clojure.string :as str]))

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

(defn- tick! [{:keys [run i] :as c} generation total per f]
  (when (= generation @run)
    (dotimes [_ per]
      (when (< @i total) (f @i) (swap! i inc)))
    (when (< @i total)
      (js/requestAnimationFrame (fn [] (tick! c generation total per f)))))
  nil)

(defn canvas-clear! [{:keys [ctx cw ch]}]
  (.clearRect ctx 0 0 cw ch))

(defn canvas-ink! [{:keys [ctx]} colour]
  (set! (.-fillStyle ctx) colour)
  (set! (.-strokeStyle ctx) colour))

(defn canvas-dot
  ([c x y] (canvas-dot c x y 1.2))
  ([{:keys [ctx sx sy ch]} x y size]
   (.fillRect ctx (* x sx) (- ch (* y sy)) size size)))

(defn canvas-poly [{:keys [ctx sx sy ch]} pts]
  (when (seq pts)
    (let [[fx fy] (first pts)]
      (.beginPath ctx)
      (.moveTo ctx (* fx sx) (- ch (* fy sy)))
      (doseq [[x y] (rest pts)]
        (.lineTo ctx (* x sx) (- ch (* y sy))))
      (.closePath ctx)
      (.stroke ctx))))

(defn canvas-frames! [{:keys [run i] :as c} total per f]
  (let [generation (swap! run inc)]
    (reset! i 0)
    (tick! c generation total per f))
  nil)

(defn canvas-on-pointer! [{:keys [cv sx sy] :as c} f]
  (set! (.-onmousemove cv)
        (fn [e]
          (f (/ (.-offsetX e) sx)
             (- (/ (.-height cv) sy) (/ (.-offsetY e) sy))))))

(defn page-ink [{:keys [cv]}]
  (.-color (js/getComputedStyle cv)))

(defn line-width! [{:keys [ctx]} w]
  (set! (.-lineWidth ctx) w))