(ns fedoras.cs-class.sand.sand
  "Grains, and what a heap of them does when the water gets into it.

  Markup only. The behaviour is in `sand_ui.cljs`, the styling is in
  `sand.css`, and the numbers the reader is invited to change are in
  `sand_settings.cljs`, all three next door.

  THE MODEL IS THREE QUESTIONS. A grain asks whether it can go down,
  then down and left, then down and right, and none of the three is
  about the pile, because a grain does not know there is a pile. The
  angle the heap stands at is nowhere in the program.

  Everything else on the palette is those three questions with one
  thing changed. A liquid asks two more. A gas asks them upside down.
  Stone does not ask. Fire asks about its neighbours instead of about
  the floor, and is the only rule in here that is about anybody else.

  THE PALETTE IS THE KEY. There is no separate legend: the thing that
  tells you what a substance does is the thing you press to get it,
  which is one fewer object on the page and one fewer thing to keep in
  step."
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or   {id "sand"
            label "SAND.CLJS · hold to pour · right-click to take away"
            settings-rows 20}}]
   (w/frame
    {:widget :sand
     :id id
     :label label
     :focus? true
     :settings-rows settings-rows
     :foot "Nothing in here knows there is a heap."}
    [:div.sand-palette]
    [:canvas.sand-canvas {:width 120 :height 72}]
    [:p.sand-counts]
    [:div.live-bar
     [:button.roll-go.sand-wet "Saturated: off"]
     [:button.roll-rewrite.sand-clear "Clear"]])))