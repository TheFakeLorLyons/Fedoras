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

  WHERE THE CODE IS. The drawing itself lives in
  `resources/fedoras/ui/sketch.cljs`, which is a source file the
  build reads and checks. This namespace writes the markup and a
  six-line header per cell binding the short names to that cell's own
  canvas. It used to hold forty lines of ClojureScript assembled by
  string concatenation, which is a bad place to keep the one thing
  every animated cell in the book depends on."
  (:require [clojure.string :as str]
            [fedoras.state :as state]
            [scicloj.kindly.v4.kind :as kind]))

;; ---------------------------------------------------------------------
;; The per-cell header
;;
;; Generated as Clojure forms rather than as a string, so it cannot be
;; unbalanced and can be read back and printed.
;; ---------------------------------------------------------------------

(defn canvas-header
  [id world-w world-h]
  [(list 'def 'C (list 'fedoras.ui.sketch/mount id world-w world-h))
   '(def cv (:cv C))
   '(def ctx (:ctx C))
   '(def CW (:cw C))
   '(def CH (:ch C))
   '(def SX (:sx C))
   '(def SY (:sy C))
   '(defn clear! [] (fedoras.ui.sketch/canvas-clear! C))
   '(defn ink! [c] (fedoras.ui.sketch/canvas-ink! C c))
   '(defn dot ([x y] (fedoras.ui.sketch/canvas-dot C x y)) ([x y s] (fedoras.ui.sketch/canvas-dot C x y s)))
   '(defn poly [pts] (fedoras.ui.sketch/canvas-poly C pts))
   '(defn frames! [total per f] (fedoras.ui.sketch/canvas-frames! C total per f))
   '(defn on-pointer! [f] (fedoras.ui.sketch/canvas-on-pointer! C f))
   '(def page-ink (fedoras.ui.sketch/page-ink C))
   '(clear!)
   '(ink! page-ink)
   '(fedoras.ui.sketch/line-width! C 0.8)])

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

;; ---------------------------------------------------------------------
;; Running a cell
;;
;; The one place a string of JavaScript is still correct: this is the
;; boundary where text the reader typed is handed to an evaluator, and
;; `scittle.core.eval_string` is a JavaScript function.
;; ---------------------------------------------------------------------

(def ^:private runner "
(function(){
  window.FEDORAS_PREAMBLE = window.FEDORAS_PREAMBLE || {};
  window.fedorasSketch = function(id){
    var src = document.getElementById(id + '-src').value;
    var out = document.getElementById(id + '-res') || document.getElementById(id + '-out');
    if (typeof scittle === 'undefined'){
      out.textContent = '(ClojureScript runtime unavailable.)';
      return;
    }
    try {
      var v = scittle.core.eval_string(window.FEDORAS_PREAMBLE[id] + '\\n' + src);
      out.textContent = (v === null || v === undefined) ? '' : String(v);
    } catch(e){
      out.textContent = String(e);
    }
  };
})();
")

(defn- register-header [id forms]
  [:script {:type "application/javascript"}
   (format "window.FEDORAS_PREAMBLE=window.FEDORAS_PREAMBLE||{};window.FEDORAS_PREAMBLE[%s]=%s;"
           (pr-str id) (pr-str (header-text forms)))])

;; ---------------------------------------------------------------------
;; The cells
;; ---------------------------------------------------------------------

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
      (state/source "fedoras/ui/sketch.cljs")
      [:script {:type "application/javascript"} runner]
      (register-header id (canvas-header id ww wh))
      [:div.sketch {:id id}
       [:div.roll-head
        [:span.roll-label (or label "SKETCH")]
        [:span.roll-dc (str ww " × " wh)]]
       [:textarea {:id (str id "-src") :class "live-src" :rows rows :spellcheck "false"}
        (str/trim code)]
       [:div.live-bar
        [:button.live-run {:onclick (str "window.fedorasSketch('" id "')")} "Run"]]
       [:canvas {:id (str id "-canvas") :width w :height h}]
       [:pre {:id (str id "-out") :class "live-out"}]]])))

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
    [:script {:type "application/javascript"} runner]
    (register-header id (page-header id))
    [:div.sketch {:id id}
     [:div.roll-head
      [:span.roll-label (or label "PAGE")]
      [:span.roll-dc "cljs"]]
     [:textarea {:id (str id "-src") :class "live-src" :rows rows :spellcheck "false"}
      (str/trim code)]
     [:div.live-bar
      [:button.live-run {:onclick (str "window.fedorasSketch('" id "')")} "Run"]]
     [:div {:id (str id "-out") :class "sketch-page"}]
     [:pre {:id (str id "-res") :class "live-out"}]]]))