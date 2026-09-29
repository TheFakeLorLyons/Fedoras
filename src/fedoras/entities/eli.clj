(ns fedoras.entities.eli
  (:require [fedoras.sketch :as sketch]
            [fedoras.state :as state]))

(defn cell
  [{:keys [id label rows] :or {id "eli" rows 30}}]
  [:div
   (state/load-spine "fedoras/entities/spine.edn")
   (state/load-entities ["fedoras/entities/eli.edn"])
   (state/source "fedoras/llm.cljs")
   (sketch/page
    {:id id :label label :rows rows}
    (state/resource-text "fedoras/cells/eli.cljs"))])