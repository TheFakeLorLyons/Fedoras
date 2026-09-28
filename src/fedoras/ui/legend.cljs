(ns fedoras.ui.legend
  "The key, built in the page.

  Both boards build their own key at runtime rather than taking a
  rendered one from the JVM, because Breakout's changes the moment
  somebody catches a bad drop. So the renderer lives here and both of
  them call it with data.

  An entry is {:svg or :letter, :label, :note, :tone}. Nothing here
  knows what any of the tones look like — that is the stylesheet's
  business and it is the reason a board never contains a hex code."
  (:require [clojure.string :as str]))

(defn- glyph [{:keys [svg letter tone] :or {tone :ink}}]
  (str "<span class='legend-glyph tone-" (name tone) "'>"
       (if svg
         (str "<svg viewBox='0 0 24 24'>" svg "</svg>")
         (str "<span class='legend-letter'>" letter "</span>"))
       "</span>"))

(defn html
  "The whole key as one string, ready for innerHTML."
  [entries]
  (str/join
   ""
   (for [{:keys [label note] :as e} entries]
     (str "<div class='legend-item'>"
          (glyph e)
          "<span class='legend-label'>" label "</span>"
          "<span class='legend-note'>" (or note "") "</span>"
          "</div>"))))

(defn paint!
  "Put a key into an element. `cols` is how many across.

    (legend/paint! (reader/q node \".floor-legend\") entries 2)"
  ([el entries] (paint! el entries 2))
  ([el entries cols]
   (when el
     (set! (.-className el) "legend")
     (.setProperty (.-style el) "--legend-cols" (str cols))
     (set! (.-innerHTML el) (html entries)))))

(defn tone-class
  "For the board itself, so a box on the grid is the same brown as the
  box in the key."
  [tone]
  (str "tone-" (name (or tone :ink))))