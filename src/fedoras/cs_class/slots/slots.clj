(ns fedoras.cs-class.slots.slots
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or   {id "slots"
            label "SLOTS.CLJS · spin it"
            settings-rows 30}}]
   (w/frame
    {:widget :slots
     :id id
     :label label
     :focus? false
     :settings-rows settings-rows
     :foot "The money belongs to the machine. Everything under it belongs to you, and comes with you to the next one."}

    [:p.slots-row]
    [:p.slots-purse]
    [:p.slots-career]
    [:p.slots-tally]
    [:p.slots-rates]
    [:div.live-bar
     [:button.roll-go.slots-go "Spin $1"]
     [:button.roll-go.slots-batch "Spin 100"]
     [:button.roll-go.slots-stake "Stake $1 ▸"]
     [:button.roll-go.slots-next "Another machine"]
     [:button.roll-go.slots-walk "Walk out"]])))