;; =====================================================================
;; DND.CLJ — the widget, plus two patches.
;;
;; The first section is a file: src/fedoras/cs_class/dnd.clj.
;; The two below it go into files that already exist and are marked with
;; where. The `require-source` and `load-entities` patches from the
;; previous round are unchanged and still needed.
;; =====================================================================

;; ---------------------------------------------------------------------
;; src/fedoras/cs_class/dnd.clj
;;
;; The widget frame handles the shell, the settings panel, the shared
;; runtimes and the mount call, and works out the load order of the
;; widget's own parts by reading their ns forms. What it does not know
;; about is everything this widget needs from outside its own directory:
;; the cast, the spine, the conversation runtime and the dice. Those are
;; emitted here, before the frame, because Scittle resolves a require
;; only against what has already run on the page.
;;
;; `require-source` rather than `source` for the two runtimes, because
;; `source`'s emitted-set is only reset by `state/assets` and a chapter
;; that renders a widget before calling it inherits the previous
;; chapter's set and silently drops the file.
;; ---------------------------------------------------------------------

(ns fedoras.cs-class.dnd.dnd
  (:require [clojure.java.io :as io]
            [fedoras.state :as state]
            [fedoras.screenplay :as sp]
            [fedoras.widget :as widget]
            [scicloj.kindly.v4.kind :as kind]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label rows]
     :or {id "dnd"
          label "DND.CLJS · a realm of forgotten games"
          rows 22}}]
   (kind/hiccup
    [:div
     (state/load-spine "fedoras/entities/spine.edn")
     (state/load-entities ["fedoras/entities/dnd.edn"])
     (state/source "fedoras/llm.cljs")
     (state/source "fedoras/ui/dice.cljs")
     (widget/frame
      {:widget :dnd
       :id id
       :label label
       :focus? true
       :settings-rows rows
       :foot "Echo · Fortune · Score"}
      [:div.dnd-sheet-row]
      [:div.dnd-log {:id (str id "-log")}]
      [:div.dnd-under]
      [:div.dnd-bar]
      [:div.dnd-record]
      [:p.dnd-foot])])))

;; ---------------------------------------------------------------------
;; Every other path in that namespace derives from this one, so a widget
;; that is not a CS-class widget needs exactly this changed and nothing
;; else. The roots are tried in order and the first that exists wins, so
;; the existing widgets keep working without being told anything.
;; ---------------------------------------------------------------------

(def widget-roots
  ["fedoras/cs_class/" "fedoras/widgets/"])

(defn directory
  "Where this widget's files are.

  Derived rather than declared, like everything else here, so a widget is
  still added by making the directory. The fallback is the first root, so
  a widget that does not exist yet produces a path that names where it
  should have been rather than nil three functions away."
  [widget]
  (let [slug-name (sp/slug widget)]
    (or (some (fn [root]
                (let [dir (str root slug-name)]
                  (when (io/resource dir) (str dir "/"))))
              widget-roots)
        (str (first widget-roots) slug-name "/"))))

;; ---------------------------------------------------------------------
;; PATCH TWO — replaces `keys!` in resources/fedoras/ui/widget_ui.cljs.
;;
;; Not needed by this widget, and it is a real bug that this widget would
;; have hit first. `preventDefault` ran before anything could say the
;; keystroke was not the widget's, so a widget with a text box in it
;; could not have shortcuts at all: mapping "1" to anything stopped the
;; reader typing a one.
;; ---------------------------------------------------------------------

(defn keys!
  "Wire a keymap onto the widget itself. The shell carries the tabindex,
  so the keys work when the widget has focus and nowhere else, and the
  page still scrolls when it does not.

  Keystrokes that land in a text box belong to the text box."
  [node keymap f]
  (set! (.-onkeydown node)
        (fn [event]
          (let [tag (some-> (.-target event) .-tagName)]
            (when-not (contains? #{"INPUT" "TEXTAREA" "SELECT"} tag)
              (when-let [what (get keymap (.-key event))]
                (.preventDefault event)
                (f event what)))))))