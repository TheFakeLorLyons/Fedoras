(ns fedoras.cs-class.rogue.rogue
  "ROGUE.CLJS, which is as generic roguelike as possible.

  Markup only. The game is in `rogue_ui.cljs`, the styling is in
  `rogue.css`, and every item, upgrade, enemy, boss, sacrifice and quest
  is in `rogue_settings.cljs`, all three next door.

  There is no map because the map was never the part anybody came for.
  What is left is two doors, a number that goes up, and the reason you
  are still here."
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or {id "rogue"
          label "ROGUE.CLJS · two doors · one of them"
          settings-rows 26}}]
   (w/frame
    {:widget :rogue
     :id id
     :label label
     :settings-rows settings-rows
     :foot "Your score is kept. Nothing else is."}
    [:div.rogue-status]
    [:div.rogue-stage]
    [:div.rogue-log]
    [:div.rogue-kit]
    [:div.rogue-legend])))