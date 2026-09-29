(ns fedoras.cs-class.line.line
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or   {id "line"
            label "LINE.CLJS · arrows · space to pause · r to reset"
            settings-rows 22}}]
   (w/frame
    {:widget :line
     :id id
     :label label
     :focus? true
     :settings-rows settings-rows
     :foot "The number on the left is which drone this is. Squares get steeper as you go. Catch all three colours before one fades into the floor or the floor takes it, and that square's whole bonus lands again, once, on top."}

    [:div.line-legend-row
     [:div.line-legend]
     [:span.line-stats]]
    [:canvas.line-canvas {:width 420 :height 420}]
    [:p.line-counts]
    [:div.live-bar
     [:button.roll-go.line-next "Another drone (space)"]])))