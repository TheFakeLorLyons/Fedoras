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
   2  "Every great thinker was misunderstood in his own time. I have arranged to be misunderstood in advance."
   3  "It takes a tough man to make a tender chicken."
   4  "Term limits exist for those who expect to be replaced."
   5  "Never eat lunch alone unless the alternative is eating lunch with someone worse."
   6  "Every organization needs a treasurer, a secretary, and a scapegoat. With careful planning, these can be one person."
   7  "Do not correct a superior. Suggest. Let the suggestion become his."
   8  "Plagiarism is only a crime if the original was better."
   9  "Every institution opposes you. This is how you know it is an institution."
   10 "An untrue utterance left unsaid is greater than five true utterances spoken."
   11 "The library is quiet because everyone in it is losing an argument with a book."
   12 "If a thing is worth doing, it is worth doing in front of someone who can promote you."
   13 "Bylaws are laws that happen to be nearby."
   14 "Membership dues are voluntary, in the sense that membership is."
   15 "The fastest way to end a conversation is to win it."
   16 "Never generalize. Generalizations are always wrong."
   17 "It's a smart idea to always be sociable. You don't even have to be sociable, just be sociable!"
   18 "Always arrive early. Latecomers are, by definition, followers."
   19 "The unexamined life is not worth living. Neither, statistically, is the examined one, but at least you'll know why."
   20 "Don't do business with a man who is kind to waiters, he is performing. Do business with the man who is kind to no one, because he is at least honest about it."
   21 "A man who has no enemies has not been paying attention."
   22 "Do not tell a man what you know. Tell him what he already said, and let him hear it in his own voice."
   23 "There is no such thing as a private opinion. There is only a public opinion you have not said yet."
   24 "Control what you can. Blame what you cannot."
   25 "Sleep is a concession to the body, which has never once won an argument."
   26 "Genius is one percent inspiration and ninety-nine percent making sure everyone heard about the inspiration."})

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
