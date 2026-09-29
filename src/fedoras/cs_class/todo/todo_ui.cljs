(ns fedoras.cs-class.todo.todo-ui
  "A list of things to do that survives being closed.

  KEYWORD KEYS. The items used to be written with string keys, from
  back when storage went through JSON by hand. The reader keywordizes
  on the way out, so `(get item \"done\")` was nil every single time: no
  row ever showed as ticked, and ticking one added a second key beside
  the real one. There is no migration here because there does not need
  to be — old items come back keywordized already, and it was only ever
  the code that was looking for the wrong thing.

  NO innerHTML IN THIS FILE. Every other widget builds its markup as a
  string, because every other widget's content is ours. This one's
  content is whatever the reader typed, and a list that renders what it
  is given is a list that will run it. The rows are built as elements
  and the text goes in through textContent, which cannot execute
  anything.

  ON THE RECORD. `:todo` is one of the few things that travels between
  chapters, because it is small and because a to-do list that forgot
  itself between chapters would be arguing against the scene it is in."
  (:require [clojure.string :as str]))

(def settings (atom {:seed [] :rows 8 :sink-done false :placeholder "add a thing"}))

;; ---------------------------------------------------------------------
;; The list
;; ---------------------------------------------------------------------

(defn items [] (vec (or (fedoras.ui.widget-ui/kept :todo) [])))

(defn- put! [v]
  (fedoras.ui.widget-ui/keep! :todo-seq
                              (inc (or (fedoras.ui.widget-ui/kept :todo-seq) 0)))
  (fedoras.ui.widget-ui/keep! :todo (vec v)))

(defn add!
  "Anything the reader puts on the list is theirs, and putting one thing
  on it is a deed. Nothing says so."
  [what]
  (when-not (str/blank? what)
    (fedoras.ui.widget-ui/did! :kept)
    (put! (conj (items) {:what (str/trim what) :done false}))))

(defn toggle! [i]
  (put! (update (items) i (fn [it] (update it :done not)))))

(defn remove-at! [i]
  (let [v (items)]
    (when (< -1 i (count v))
      (put! (into (subvec v 0 i) (subvec v (inc i)))))))

(defn- ordered
  "What order to show them in. The index goes with each one, because
  everything that acts on a row acts on where it is in the file rather
  than where it is on the screen."
  [v]
  (let [pairs (map-indexed vector v)]
    (if (:sink-done @settings)
      (concat (remove (fn [[_ it]] (:done it)) pairs)
              (filter (fn [[_ it]] (:done it)) pairs))
      pairs)))

;; ---------------------------------------------------------------------
;; Rows
;;
;; Built rather than written, and the reader's own words go in through
;; textContent. `row-node` is above `paint!` because it has to be — the
;; page evaluates this file from the top and a function cannot call one
;; that has not happened yet.
;; ---------------------------------------------------------------------

(defn- row-node [i it]
  (let [row  (.createElement js/document "div")
        box  (.createElement js/document "input")
        text (.createElement js/document "span")
        del  (.createElement js/document "button")]
    (set! (.-className row) (str "todo-row" (when (:done it) " todo-done")))
    (.setAttribute row "data-i" (str i))

    (set! (.-type box) "checkbox")
    (set! (.-className box) "todo-box")
    (set! (.-checked box) (boolean (:done it)))

    (set! (.-className text) "todo-what")
    (set! (.-textContent text) (str (:what it)))

    (set! (.-className del) "todo-del")
    (set! (.-textContent del) "\u00d7")
    (.setAttribute del "title" "take it off the list")

    (doto row (.appendChild box) (.appendChild text) (.appendChild del))))

(defn paint! [node]
  (let [v    (items)
        left (count (remove :done v))
        list (fedoras.ui.widget-ui/q! node ".todo-list")]
    (when list
      (set! (.-innerHTML list) "")
      (doseq [[i it] (ordered v)]
        (.appendChild list (row-node i it))))
    (.setProperty (.-style node) "--todo-rows" (str (:rows @settings)))
    (fedoras.ui.widget-ui/state!
     node
     (cond
       (zero? (count v)) "nothing on it"
       (zero? left)      (str "all " (count v) " done")
       :else             (str left " of " (count v) " outstanding")))
    (fedoras.reader/text!
     node ".todo-note"
     (if (zero? left) "" (str left " still on it.")))
    (when-let [field (fedoras.reader/q node ".todo-input")]
      (set! (.-placeholder field) (:placeholder @settings)))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- add-from-field! [node]
  (when-let [field (fedoras.reader/q node ".todo-input")]
    (add! (.-value field))
    (set! (.-value field) "")
    (.focus field)
    (paint! node)))

(defn- wire-add! [node]
  (fedoras.reader/on-click! node ".todo-add" (fn [_] (add-from-field! node)))
  (when-let [field (fedoras.ui.widget-ui/q! node ".todo-input")]
    (set! (.-onkeydown field)
          (fn [e] (when (= "Enter" (.-key e)) (add-from-field! node))))))

(defn- wire-list!
  "One handler on the list rather than two on every row, so a row is a
  thing that was drawn and not a thing that was wired."
  [node]
  (when-let [list (fedoras.ui.widget-ui/q! node ".todo-list")]
    (set! (.-onclick list)
          (fn [e]
            (when-let [row (.closest (.-target e) ".todo-row")]
              (let [i (js/parseInt (.getAttribute row "data-i") 10)
                    hit (.-target e)]
                (cond
                  (.matches hit ".todo-del") (remove-at! i)
                  (.matches hit ".todo-box") (toggle! i)
                  :else                      (toggle! i))
                (paint! node)))))))

(defn- seed!
  "Written once, the first time a reader ever opens the book, and never
  again. A reader who has cleared the whole list gets it back, because
  an empty list and a list that has never existed look the same from
  here and only one of those wants filling."
  []
  (when (empty? (items))
    (put! (mapv (fn [w] {:what w :done false}) (:seed @settings)))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current
                     (select-keys setting [:seed :rows :sink-done :placeholder]))))
     (seed!)
     (paint! node))))

(defn setup! [node]
  (let [id (.-id node)]
    (fedoras.ui.widget-ui/wire! node "add"      wire-add!)
    (fedoras.ui.widget-ui/wire! node "list"     wire-list!)
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount! ::todo (or id "todo") setup! paint!))