(ns fedoras.cs-class.shoot.shoot
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or   {id "shoot"
            label "SHOOT.CLJS · rock, paper, scissors"
            settings-rows 10}}]
   (w/frame
    {:widget :shoot
     :id id
     :label label
     :focus? false
     :settings-rows settings-rows
     :foot "The gold tag means it wasn't guessing that round. Everywhere else, it's a coin."}

    [:div.live-bar
     [:button.roll-go.shoot-rock "rock"]
     [:button.roll-go.shoot-paper "paper"]
     [:button.roll-go.shoot-scissors "scissors"]]
    [:p.shoot-tally]
    [:p.shoot-reads]
    [:div.shoot-log])))