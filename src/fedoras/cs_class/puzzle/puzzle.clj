(ns fedoras.cs-class.puzzle.puzzle
  "Assignment five. Fifteen numbers and a hole.

  Markup only. The behaviour is in `puzzle_ui.cljs`, the styling is in
  `puzzle.css`, and the board it starts on is in
  `puzzle_settings.cljs`, all three next door.

  THE STYLESHEET IS NOT DECORATION HERE. Sixteen divs with no rules on
  them are sixteen numbers in a column, which is what this widget has
  been rendering as since it lost whatever sheet used to carry .puzzle.

  WHAT REYES MARKS is the refusing rather than the sliding. Any of them
  can move a tile. The assignment is that the board will not move one
  that is nowhere near the hole, and will say so.

  AND THE SOLVER SOLVES. The old one recorded its own shuffle and played
  it backwards, which is not solving and could not touch a board it had
  not made. This one takes any board, works out whether it can be got
  into order at all, and says so when it cannot — which is the same two
  lines he added on the second evening, grown up."
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or   {id "puzzle"
            label "SLIDE.CLJS · click a tile next to the hole"
            settings-rows 20}}]
   (w/frame
    {:widget :puzzle
     :id id
     :label label
     :settings-rows settings-rows
     :foot "Under 65 moves is no action. 65 to 79 is review. 80 and over is the other thing."}

    [:div.puzzle-grid]
    [:p.puzzle-note]
    [:div.live-bar
     [:button.roll-go.puzzle-shuffle "Shuffle"]
     [:button.roll-go.puzzle-solve "Solve it"]
     [:button.roll-rewrite.puzzle-reset "Put it in order"]])))