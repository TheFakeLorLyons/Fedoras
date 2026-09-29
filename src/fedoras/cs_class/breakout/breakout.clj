(ns fedoras.cs-class.breakout.breakout
  "A bat, a ball, and a wall between them.

  Markup only. The behaviour is in `breakout_ui.cljs`, the styling is in
  `breakout.css`, and the numbers the reader is invited to change are in
  `breakout_settings.cljs`, all three next door.

  WHAT THE PROGRAM KNOWS. Nothing in it mentions a wall. There are
  thirty separate bricks and not one of them knows about any other one,
  and the wall is what thirty separate things look like from where he is
  sitting.

  AND THEN THE REQUESTS. Eli wanted steering and was right. Eli wanted a
  bigger bat. Derek wanted two balls, because two balls is more of the
  good thing. Miles wanted to hold it and aim. Victor said it should not
  be able to end.

  He could not take any of them out, because taking one out is taking a
  thing off somebody who asked for it. So he put them in the wall, and
  two of them are worse than nothing, and he did not mark those either.

  AND VICTOR GOT WHAT HE ASKED FOR. The wall comes back. It comes back
  with one more brick in it that cannot be broken, and it keeps the ones
  from last time, so by the twentieth wall two thirds of it is
  permanent and by the thirtieth there is nothing left to hit.

  It does not end. It stops being possible, which is a different thing,
  and nobody at that table would be able to tell you the difference."
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or   {id "brk"
            label "BRICKS.CLJS · move to aim · click or space to serve"
            settings-rows 20}}]
   (w/frame
    {:widget :breakout
     :id id
     :label label
     :focus? true
     :settings-rows settings-rows
     :foot "No brick in here knows about any other brick."}
    [:canvas.brk-canvas {:width 400 :height 280}]
    [:p.brk-counts]
    [:div.brk-key]
    [:div.live-bar
     [:button.roll-rewrite.brk-again "Start over (r)"]])))