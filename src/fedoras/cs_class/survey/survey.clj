(ns fedoras.cs-class.survey.survey
  "SURVEY.CLJS, which is minesweeper until the office resurveys.

  Markup only. The ground is in `ui.cljs` next door, the styling is in
  `style.css` next door, and neither of them is anywhere else."
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or {id "survey"
          label "SURVEY.CLJS · click to probe · right-click to mark"
          settings-rows 18}}]
   (w/frame
    {:widget :survey
     :id id
     :label label
     :settings-rows settings-rows
     :foot "No resurvey has ever placed a hazard on ground already surveyed."}
    [:div.survey-grid]
    [:p.survey-note]
    [:div.live-bar
     [:button.roll-go.survey-deduce "What follows"]
     [:button.roll-go.survey-resurvey "Order a resurvey"]
     [:button.roll-rewrite.survey-new "New ground"]]
    [:p.survey-record])))