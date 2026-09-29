(ns fedoras.cs-class.music.music
  "MUSIC.CLJS, a piano roll with the timings on the horizontal.

  Markup only. The instrument is in `music_ui.cljs`, the styling is in
  `music.css`, and the numbers the reader is invited to change are in
  `music_settings.cljs`, all three next door.

  Every class here is prefixed `music-`, because `screenplay.css` already
  owns `.roll-bar`, `.roll-head`, `.roll-note` and half a dozen more for
  the d20. `.roll-go` and `.roll-rewrite` are the exception and are
  borrowed deliberately: those are the book's buttons."
  (:require [fedoras.widget :as w]))

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or {id "music"
          label "MUSIC.CLJS · click to write · shift to erase"
          settings-rows 22}}]
   (w/frame
    {:widget :music
     :id id
     :label label
     :focus? true
     :settings-rows settings-rows
     :foot "One seat, one afternoon, in the gaps between the keystrokes."}
    [:div.music-voices]
    [:div.music-stage
     [:div.music-rows]
     [:div.music-surface
      [:div.music-grid]
      [:div.music-playhead]]]
    [:p.music-status]
    [:div.live-bar
     [:button.roll-go.music-play "Play"]
     [:button.roll-go.music-fill "Fill"]
     [:button.roll-go.music-fill-again "Fill again"]
     [:button.roll-rewrite.music-clear "Clear"]]
    [:div.live-bar
     [:label.music-control "tempo"
      [:input.music-tempo {:type "range" :min 60 :max 200 :step 2}]]
     [:label.music-control "scale"
      [:select.music-scale]]
     [:label.music-control "key"
      [:select.music-key]]
     [:label.music-control "columns"
      [:select.music-length
       [:option {:value "16"} "16"]
       [:option {:value "32"} "32"]
       [:option {:value "64"} "64"]
       [:option {:value "96"} "96"]]]]
    [:div.live-bar
     [:button.roll-go.music-afternoon "Load the afternoon"]
     [:button.roll-go.music-typing "Load only the typing"]]
    [:div.live-bar
     [:button.roll-go.music-keep "Keep it"]
     [:button.roll-go.music-restore "Restore"]
     [:button.roll-go.music-download "Download"]])))