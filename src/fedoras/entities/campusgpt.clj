(ns fedoras.entities.campusgpt
  (:require [fedoras.sketch :as sketch]
            [fedoras.state :as state]
            [scicloj.kindly.v4.kind :as kind]))

(defn cell
  [{:keys [id label rows] :or {id "campusgpt" rows 30}}]
  (kind/hiccup
   [:div
    (state/load-spine "fedoras/entities/spine.edn")
    (state/load-entities ["fedoras/entities/campusgpt.edn"])
    (state/source "fedoras/llm.cljs")
    (sketch/page
     {:id id :label label :rows rows}
     (state/resource-text "fedoras/cells/campusgpt.cljs"))]))