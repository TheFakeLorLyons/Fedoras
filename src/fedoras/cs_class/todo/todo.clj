(ns fedoras.cs-class.todo.todo
  "A list of things to do that survives being closed.

  Markup only. The behaviour is in `todo_ui.cljs`, the styling is in
  `todo.css`, and what was on it to begin with is in
  `todo_settings.cljs`, all three next door.

  Assignment three, working, in the page. The reader adds items, ticks
  them, takes them off, and closes the browser, and when they come back
  it is still there, which was the whole of the assignment and is the
  whole of this world's theory of what is real.

  IT IS THE ONE LIST IN THIS BOOK ANYBODY CAN TAKE A THING OFF. The
  paint program keeps every mark and flags the ones you withdrew. The
  minutes keep everything. The register has never had a line removed
  from it in ninety years. This is a list a person made for themselves,
  and the cross at the end of a row does exactly what it looks like it
  does.

  Do not make a joke of that. It is the only place in the book where
  deleting works and it should pass without comment."
  (:require [fedoras.widget :as w]
            [scicloj.kindly.v4.kind :as kind]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or   {id "todo"
            label "THINGS TO DO"
            settings-rows 14}}]
   (w/frame
    {:widget :todo
     :id id
     :label label
     :settings-rows settings-rows
     :foot "It has to still be there on Tuesday."}
    [:div.todo-list]
    [:div.live-bar
     [:input.todo-input {:type "text" :spellcheck "false"}]
     [:button.roll-go.todo-add "Add"]]
    [:p.todo-note])))

(defn render
  "The list, from the JVM side, the way he sees it on the page. For a
  chapter that wants to show one without running one."
  [items]
  (kind/hiccup
   [:div.todo.widget-todo
    [:div.todo-list
     (for [{:keys [what done]} items]
       [:div {:class (str "todo-row" (when done " todo-done"))}
        [:span.todo-tick (if done "\u00d7" "\u00b7")]
        [:span.todo-what what]])]]))