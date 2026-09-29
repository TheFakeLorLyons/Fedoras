(ns fedoras.cs-class.paint.paint
  "The paint program, which keeps everything.

  Markup only. The behaviour is in `paint_ui.cljs`, the styling is in
  `paint.css`, and the numbers the reader is invited to change are in
  `paint_settings.cljs`, all three next door."
 (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or {id "paint"
          label "PAINT.CLJS · drag to mark · right-click for the rubber"
          settings-rows 24}}]
   (w/frame
    {:widget :paint
     :id id
     :label label
     :settings-rows settings-rows
     :foot "Nothing in this program deletes anything."}

    [:div.live-bar
     [:div.paint-tools]
     [:button.paint-fill {:style {:display "none"}} "fill"]
     [:div.paint-inks]]

    [:div.live-bar.paint-dials
     [:label.paint-control "line"
      [:input.paint-weight {:type "range" :step 1}]]
     [:label.paint-control "rub"
      [:input.paint-strength {:type "range" :step 1}]]
     [:label.paint-control "brush"
      [:input.paint-reach {:type "range" :step 1}]]
     [:label.paint-control "smudge"
      [:input.paint-spread {:type "range" :step 1}]]
     [:label.paint-control "keep"
      [:input.paint-tolerance {:type "range" :step 1}]]]

    [:canvas.paint-canvas]
    [:p.paint-points]
    [:div.paint-ledger]

    [:div.live-bar
     [:button.roll-go.paint-simplify "Refile everything"]
     [:button.roll-go.paint-copy "Copy the picture"]
     [:button.roll-go.paint-download "Download the file"]
     [:button.roll-rewrite.paint-fresh "Fresh sheet"]])))