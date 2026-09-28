(ns fedoras.legend
  "The key that goes under a board.

  Every program in cs_class that has things on a grid needs the same
  object: a glyph, a name, a short note, and a colour that means
  something. Breakout, the floor plan, and whatever comes after them
  were each growing their own, and the third one was going to be a
  quarter of an hour of copying somebody else's CSS badly.

  AN ENTRY is a map. The glyph is either :svg — the inside of a
  viewBox 0 0 24 24 — or :letter, a character in a box, which is what
  Breakout's capsules are.

    {:svg   \"<path d='...'/>\"
     :label \"trap\"
     :note  \"holds whoever is in it\"
     :tone  :bad}"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

;; ---------------------------------------------------------------------
;; The tones
;; ---------------------------------------------------------------------

(def tones
  {:ink    "whatever the page is set in. the default and most things."
   :bad    "costs you something. red."
   :good   "helps. green, and off by default because most things that
            help do not need announcing."
   :aim    "what you came for and how you leave. amber."
   :box    "a thing you push. brown."
   :shield "the one that protects. blue."
   :you    "you."
   :entity "the thing that is trying."})

(mapv (juxt key val) tones)

;; ---------------------------------------------------------------------

(defn entry
  "One line of a key. `tone` defaults to :ink."
  [{:keys [svg letter label note tone] :or {tone :ink}}]
  (cond-> {:label label :tone tone}
    svg    (assoc :svg svg)
    letter (assoc :letter letter)
    note   (assoc :note note)))

(defn entries
  "A vector of entries out of a vector of maps, so a program can hold
  its key as plain data next to its sprites."
  [ms]
  (mapv entry ms))

;; ---------------------------------------------------------------------
;; Static, for print
;; ---------------------------------------------------------------------

(defn- glyph-hiccup [{:keys [svg letter tone]}]
  [:span {:class (str "legend-glyph tone-" (name tone))}
   (if svg
     [:span {:dangerouslySetInnerHTML {:__html (str "<svg viewBox='0 0 24 24'>" svg "</svg>")}}]
     [:span.legend-letter letter])])

(defn render
  "The key, as hiccup, for a chapter that is not running anything."
  ([es] (render es 2))
  ([es cols]
   (kind/hiccup
    (into [:div.legend {:style {:--legend-cols cols}}]
          (for [{:keys [label note] :as e} es]
            [:div.legend-item
             (glyph-hiccup e)
             [:span.legend-label label]
             [:span.legend-note (or note "")]])))))

;; ---------------------------------------------------------------------
;; Assets
;; ---------------------------------------------------------------------

(def ^:private stylesheet "fedoras/ui/legend.css")

(defn- resource-text [path]
  (if-let [url (io/resource path)]
    (slurp url)
    (throw (ex-info (str "fedoras: cannot find " path " on the classpath.\n"
                         "Looked in: "
                         (->> (str/split (System/getProperty "java.class.path") #"[:;]")
                              (remove #(str/ends-with? % ".jar"))
                              (str/join ", ")))
                    {:path path}))))

(defn styles
  "Emit once per chapter that has a key on it. Safe to call more than
  once — the second one is a duplicate rule set and costs nothing — but
  the tidy thing is to call it in the widget that owns the board."
  []
  (kind/hiccup [:style (resource-text stylesheet)]))