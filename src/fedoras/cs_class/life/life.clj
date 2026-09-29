(ns fedoras.cs-class.life.life
  "LIFE.CLJS, with more than one kind of alive in it.

  Markup only. The rules are in `life_ui.cljs`, the styling is in
  `life.css`, and every species, palette and pattern is in
  `life_settings.cljs`, all three next door."
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or {id "life"
          label "LIFE.CLJS · draw on it · space to run"
          settings-rows 24}}]
   (w/frame
    {:widget :life
     :id id
     :label label
     :focus? true
     :settings-rows settings-rows
     :foot "Nothing here wants anything. It is only counting."}
    [:div.life-species]
    [:canvas.life-canvas]
    [:p.life-note]
    [:div.live-bar
     [:button.roll-go.life-run "Run"]
     [:button.roll-go.life-step "Step"]
     [:button.roll-go.life-seed "Scatter"]
     [:button.roll-rewrite.life-clear "Clear"]]
    [:div.live-bar
     [:label.life-control "speed"
      [:input.life-speed {:type "range" :min 1 :max 30 :step 1}]]
     [:label.life-control "cell"
      [:input.life-scale {:type "range" :min 2 :max 16 :step 1}]]
     [:label.life-control "palette"
      [:select.life-palette]]
     [:label.life-control "pattern"
      [:select.life-pattern]]]
    [:div.life-tally])))