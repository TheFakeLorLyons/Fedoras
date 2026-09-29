(ns fedoras.entities.monolith
  (:require [fedoras.sketch :as sketch]
            [fedoras.state :as state]
            [scicloj.kindly.v4.kind :as kind]))

(defn cell
  [{:keys [id label rows] :or {id "monolith" rows 30}}]
  (kind/hiccup
   [:div
    (state/load-spine "fedoras/entities/spine.edn")
    (state/load-entities ["fedoras/entities/monolith.edn"
                          "fedoras/entities/mc.edn"])
    (state/source "fedoras/llm.cljs")
    (sketch/page
     {:id id :label label :rows rows}
     (state/resource-text "fedoras/cells/monolith.cljs"))]))