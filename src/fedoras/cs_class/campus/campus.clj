(ns fedoras.cs-class.campus.campus
  "The map, walked.

  Markup only. The behaviour is in `campus_ui.cljs`, the styling is in
  `campus.css`, and the map itself is in `campus_settings.cljs`, which
  is the point of this one.

  THE SETTINGS FILE IS THE MAP. Not a copy of it — the map the widget
  walks. A reader who opens that panel can put a door into Building 14
  and go and stand in it, and what is in there is a name, and nothing
  else, because there is nothing else in it.

  THE PAD SHOWS EVERY DIRECTION whether or not it goes anywhere. That is
  not an oversight. A direction that does nothing looks exactly like a
  direction that does something, right up until you press it, and then
  it still does."
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows start]
     :or   {id "campus"
            label "CAMPUS.CLJ · one step at a time"
            settings-rows 26
            start :whitlock}}]
   (w/frame
    {:widget :campus
     :id id
     :label label
     :focus? true
     :settings-rows settings-rows
     :mount {:start (name start)}
     :foot "A direction that is not there is not an error."}

    [:p.campus-where]

    [:div.campus-pad
     [:button.campus-go {:data-dir "north"} "north"]
     [:button.campus-go {:data-dir "up"}    "up"]
     [:button.campus-go {:data-dir "in"}    "in"]
     [:button.campus-go {:data-dir "west"}  "west"]
     [:button.campus-go {:data-dir "east"}  "east"]
     [:button.campus-go {:data-dir "out"}   "out"]
     [:button.campus-go {:data-dir "south"} "south"]
     [:button.campus-go {:data-dir "down"}  "down"]
     [:button.campus-go {:data-dir "back"}  "back"]]

    [:p.campus-note]
    [:pre.campus-log]

    [:div.live-bar
     [:button.roll-go.campus-read "How did I get here"]
     [:button.roll-go.campus-lost "What cannot I get to"]
     [:button.roll-rewrite.campus-start "Start again"]])))