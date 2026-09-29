(ns fedoras.sketch
  "Editable ClojureScript that draws.

  The live cells elsewhere in the book evaluate an expression and print
  what it returned, which is fine for a list and useless for a picture.
  A sketch cell gives the code a canvas and five things to draw with,
  and then gets out of the way: what the reader edits is the same
  algorithm that ran on the JVM at build time, in the same language,
  and it runs in front of them.

  What the page provides, and the scene says so out loud because Reyes
  said it in class:

    (clear!)                wipe it
    (ink! \"#1c1f24\")        colour
    (dot x y)               one mark, in world coordinates
    (poly [[x y] ...])      a closed outline
    (frames! total per f)   call (f i) total times, per of them a frame

  Nothing else is injected. Everything above the fold in the cell is
  the reader's.

  WHERE THE CODE IS. All of it is here, in one file read twice. The
  build reads the :clj half, which writes the markup and, for each cell,
  a header binding the short names to that cell's own canvas or piece of
  page. The page reads the :cljs half, because the build ships this file
  to it as it stands: the drawing kit the headers call, and the runner
  behind every Run button."
  #?(:clj (:require [clojure.string :as str]
                    [fedoras.state :as state]
                    [scicloj.kindly.v4.kind :as kind])))

;; ---------------------------------------------------------------------
;; The build: headers and markup
;; ---------------------------------------------------------------------

#?(:clj
   (do
     (defn canvas-header
       "The short names a sketch cell may use, bound to its own canvas.
       Built as Clojure forms rather than as a string, so it cannot be
       unbalanced and can be read back and printed."
       [id world-w world-h]
       [(list 'def 'C (list 'fedoras.sketch/mount id world-w world-h))
        '(def cv (:cv C))
        '(def ctx (:ctx C))
        '(def CW (:cw C))
        '(def CH (:ch C))
        '(def SX (:sx C))
        '(def SY (:sy C))
        '(defn clear! [] (fedoras.sketch/clear! C))
        '(defn ink! [c] (fedoras.sketch/ink! C c))
        '(defn dot ([x y] (fedoras.sketch/dot C x y)) ([x y s] (fedoras.sketch/dot C x y s)))
        '(defn poly [pts] (fedoras.sketch/poly C pts))
        '(defn frames! [total per f] (fedoras.sketch/frames! C total per f))
        '(defn on-pointer! [f] (fedoras.sketch/on-pointer! C f))
        '(def page-ink (fedoras.sketch/page-ink C))
        '(clear!)
        '(ink! page-ink)
        '(fedoras.sketch/line-width! C 0.8)])

     (defn page-header
       "The names a page cell may use. No canvas: a piece of the page."
       [id]
       [(list 'def 'root (list '.getElementById 'js/document (str id "-out")))
        '(defn html! [s] (set! (.-innerHTML root) s))
        '(defn on! [id f]
           (let [e (.getElementById js/document id)]
             (when e (set! (.-onclick e) (fn [_] (f))))))
        '(defn fetch [k]
           (try
             (let [v (.getItem js/localStorage (str "fedoras." k))]
               (when v (read-string v)))
             (catch :default _ nil)))
        '(defn store! [k v]
           (try (.setItem js/localStorage (str "fedoras." k) (pr-str v))
                (catch :default _ nil))
           v)])

     (defn- header-text [forms]
       (str/join "\n" (map pr-str (cons '(in-ns 'fedoras.sketch-runner) forms))))

     (defn primitives
       "What the page hands a cell, printed, so that nothing in the book is
       doing something the reader cannot look at."
       ([] (primitives 100 100))
       ([world-w world-h]
        (kind/code (header-text (canvas-header "the-page" world-w world-h)))))

     (defn cell
       "An editable ClojureScript sketch with a canvas under it.

         (sketch/cell {:id \"chaos\" :label \"CHAOS.CLJS\" :world [100 86.6]}
                      \"(dot 50 40)\")"
       [{:keys [id label world w h rows]
         :or   {id "sketch" world [100 100] w 420 h 380 rows 16}}
        code]
       (let [[ww wh] world]
         (kind/hiccup
          [:div
           (state/source "fedoras/sketch.cljc")
           [:div.sketch {:id id}
            [:div.roll-head
             [:span.roll-label (or label "SKETCH")]
             [:span.roll-dc (str ww " × " wh)]]
            [:textarea {:id (str id "-src") :class "live-src" :rows rows :spellcheck "false"}
             (str/trim code)]
            [:div.live-bar
             [:button.live-run "Run"]]
            [:canvas {:id (str id "-canvas") :width w :height h}]
            [:pre {:id (str id "-out") :class "live-out"}]]
           (state/call 'fedoras.sketch/cell! id (header-text (canvas-header id ww wh)))])))

     (defn page
       "An editable ClojureScript cell with a piece of page under it instead
       of a canvas. The header gives the code five things:

         root            the div it owns
         (html! s)       put markup in it
         (on! id f)      wire a click
         (fetch k)       read something the reader saved
         (store! k v)    save something, and it is still there tomorrow

       Everything else in the cell belongs to the reader."
       [{:keys [id label rows] :or {id "page" rows 20}} code]
       (kind/hiccup
        [:div
         (state/source "fedoras/sketch.cljc")
         [:div.sketch {:id id}
          [:div.roll-head
           [:span.roll-label (or label "PAGE")]
           [:span.roll-dc "cljs"]]
          [:textarea {:id (str id "-src") :class "live-src" :rows rows :spellcheck "false"}
           (str/trim code)]
          [:div.live-bar
           [:button.live-run "Run"]]
          [:div {:id (str id "-out") :class "sketch-page"}]
          [:pre {:id (str id "-res") :class "live-out"}]]
         (state/call 'fedoras.sketch/cell! id (header-text (page-header id)))]))))

;; ---------------------------------------------------------------------
;; The page: the drawing kit and the runner
;; ---------------------------------------------------------------------

#?(:cljs
   (do
     (defonce counters
       (atom {}))

     (defn- counters-for
       "The frame counters of one canvas: made the first time it is mounted
       and kept for every Run after, so that each press of Run takes the
       next number and the loop the last press started sees it and stops."
       [id]
       (or (get @counters id)
           (let [made {:run (atom 0) :i (atom 0)}]
             (swap! counters assoc id made)
             made)))

     (defn mount
       "Everything one cell needs, from the id of its canvas and the size of
       the world it draws in. Every drawing function below takes the map
       this returns."
       [id world-w world-h]
       (let [cv  (.getElementById js/document (str id "-canvas"))
             ctx (.getContext cv "2d")
             cw  (.-width cv)
             ch  (.-height cv)]
         (merge {:cv cv :ctx ctx :cw cw :ch ch
                 :sx (/ cw world-w)
                 :sy (/ ch world-h)}
                (counters-for id))))

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
       [{:keys [cv sx sy]} f]
       (set! (.-onmousemove cv)
             (fn [e]
               (f (/ (.-offsetX e) sx)
                  (- (/ (.-height cv) sy) (/ (.-offsetY e) sy))))))

     (defonce headers
       (atom {}))

     (defn- element
       [id]
       (.getElementById js/document id))

     (defn- line-count
       [text]
       (count (re-seq #"\n" text)))

     (defn- error-text
       "An error as the reader should see it: the message, and the line of
       their own code it came from, when there is one."
       [error header-lines]
       (let [line (:line (ex-data error))]
         (str (or (ex-message error) (str error))
              (when (and line (> line header-lines))
                (str " (line " (- line header-lines) ")")))))

     (defn run-cell!
       "Cell `id`'s header, then the reader's code, with what came back
       printed under the cell, or the error, counted in the reader's lines."
       [id]
       (let [header (get @headers id)
             out (or (element (str id "-res")) (element (str id "-out")))
             source (.-value (element (str id "-src")))]
         (set! (.-textContent out)
               (try
                 ;; load-string is Scittle's own; plain ClojureScript has none
                 (let [value #_{:clj-kondo/ignore [:unresolved-symbol]} (load-string (str header "\n" source))]
                   (if (nil? value) "" (str value)))
                 (catch :default error
                   (error-text error (inc (line-count header))))))))

     (defn- prepare-canvas!
       "Give a sketch cell's canvas its drawing context while the page
       loads, so that the first Run draws on a canvas the browser has
       already shown once. A context made at the moment of the first
       drawing loses that drawing in Firefox: the first frames of the
       first Run never appear."
       [id]
       (when-let [cv (element (str id "-canvas"))]
         (.clearRect (.getContext cv "2d") 0 0 (.-width cv) (.-height cv))))

     (defn cell!
       "Keep cell `id`'s header and wire its Run button, and the first time
       the cell is seen, prepare its canvas, if it has one. Only the first
       time, because preparing clears, and a canvas that is already drawing
       must not be cleared under it."
       [id header]
       (let [first-time? (not (contains? @headers id))]
         (swap! headers assoc id header)
         (when-let [button (some-> (element id) (.querySelector ".live-run"))]
           (set! (.-onclick button) (fn [_] (run-cell! id))))
         (when first-time?
           (prepare-canvas! id))))))