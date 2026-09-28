(ns fedoras.handbook
  "THE FEDORA HANDBOOK, by Victor Ashcroft. Unpublished. Self-edited.

  One rule opens each scene, and the scene then immediately contradicts
  it. Keeping the rules here rather than inline in the scene files means
  (a) you can reorder scenes without losing track of which rule you've
  already spent, and (b) the appendix that prints the whole Handbook is
  generated from the same data, so it can never drift out of sync with
  the epigraphs.

  Add rules freely. `unused` will tell you which ones are still on the
  shelf."
  (:require [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

(def rules
  {1  "Don't quote me on this."
   2  "[REDACTED]"
   3  "It takes a tough man to make a tender chicken."
   4  "[REDACTED]"
   5  "[REDACTED]"
   6  "[REDACTED]"
   7  "[REDACTED]"
   8  "[REDACTED]"
   9  "Every institution opposes you. This is how you know it is an institution."
   10 "An untrue utterance left unsaid is greater than five true utterances spoken."
   11 "[REDACTED]"
   12 "[REDACTED]"
   13 "[REDACTED]"
   14 "[REDACTED]"
   15 "[REDACTED]"
   16 "[REDACTED]"
   17 "It's a smart idea to always be sociable, you don't even have to be sociable, just be sociable."
   18 "[REDACTED]"
   19 "[REDACTED]"
   20 "[REDACTED]"
   21 "[REDACTED]"
   22 "[REDACTED]"
   23 "[REDACTED]"
   24 "[REDACTED]"
   25 "[REDACTED]"
   26 "[REDACTED]"})

(defn rule
  "The bare text of a rule, for use in dialogue."
  [n]
  (get rules n))

(defn epigraph
  "Render the epigraph plate that opens a scene."
  ([n] (epigraph n nil))
  ([n {:keys [attribution]}]
   (kind/hiccup
    [:blockquote.handbook
     [:p.rule-number (str "The Fedora Handbook, Rule #" n)]
     [:p.rule-text (str "\u201C" (rule n) "\u201D")]
     (when attribution [:footer attribution])])))

(defn all
  "The whole Handbook, for the appendix."
  []
  (kind/hiccup
   (into [:div.handbook-full]
         (for [[n text] (sort-by key rules)]
           [:div {:style {:margin-bottom "0.9rem"}}
            [:p {:style {:margin "0" :font-weight "700"}} (str "Rule #" n)]
            [:p {:style {:margin "0 0 0 1.2rem"}} (str "\u201C" text "\u201D")]]))))

(def spent
  "Rule numbers currently used as scene epigraphs, in scene order:

    Act I    1, 3, 4, 8, 7
    Act II   20, 6, 14, 12, 13
    Act III  15, 16, 17, 18
    Act IV   19, 5, 21, 22
    Act V    23, 24, 25, 26

  Keep this honest and `unused` stays useful."
  [1 3 4 8 7 20 6 14 12 13 15 16 17 18 19 5 21 22 23 24 25 26])

(defn unused
  "Rules not yet spent as an epigraph. Pass the set of numbers you've
  used -- or call (unused) with no args and update `spent` below as you
  go, whichever annoys you less."
  ([] (unused spent))
  ([used]
   (->> (keys rules) (remove (set used)) sort vec)))

(defn summary
  "A one-line sanity check for the REPL."
  []
  (format "%d rules, %d spent, %d on the shelf: %s"
          (count rules)
          (count spent)
          (count (unused))
          (str/join ", " (unused))))
