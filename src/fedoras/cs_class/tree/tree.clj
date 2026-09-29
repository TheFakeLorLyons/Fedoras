(ns fedoras.cs-class.tree.tree
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or   {id "tree"
            label "TREE.CLJS · one rule, mentioned twice"
            settings-rows 24}}]
   (w/frame
    {:widget :tree
     :id id
     :label label
     :focus? false
     :settings-rows settings-rows
     :foot "Drawn in the order it thought of them: all the way down one side before it comes back for the other. Scroll to zoom, drag to move. It keeps going."}

    [:canvas.tree-canvas {:width 640 :height 640}]
    [:p.tree-counts]
    [:div.live-bar
     [:button.roll-go.tree-again "Draw it again"]
     [:button.roll-go.tree-pause "Pause"]
     [:button.roll-go.tree-all "All at once"]
     [:button.roll-go.tree-home "Back out"]])))