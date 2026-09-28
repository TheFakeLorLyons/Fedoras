^{:kindly/hide-code true
  :clay {:quarto {:title "Tribute"}}}
(ns front.tribute
  (:require [scicloj.kindly.v4.kind :as kind]))

;; # Tribute {#tribute .sidebar-title}

^:kindly/hide-code
(kind/hiccup
 [:div.tribute-page
  [:blockquote.tribute

   [:p.tribute-heading
    "§ 1.3  THE AUTHOR TO "
    [:em "[HER]"]
    " BOOK:"]

   [:p
    "You prefer, " [:strong "little book"]
    ", to dwell in the shops in the Argiletum, though my book-case has plenty of room for you. You are ignorant, alas! you are ignorant of the fastidiousness of Rome, the mistress of the world; the sons of Man, believe me, are much too critical. Nowhere are there louder sneers; young men and old, and even boys, have the nose of the rhinoceros. After you have heard a loud “Bravo!” and are expecting kisses, you will go, tossed to the skies, from the jerked toga. Yet, that you may not so often suffer the corrections of your master, and that "
    [:em "[her]"]
    " relentless pen may not so often mark your vagaries, you desire, frolicsome " [:strong "little book"] ", to fly through the air of heaven. Go, fly; but you would have been safer at home."]

   [:p.tribute-heading
    "§ 1.16  TO AVITUS:"]

   [:p
    "Of the epigrams which you read here, some are good, some middling, many bad; a book, Avitus, cannot be made in any other way."]

   [:p.tribute-heading
    "§ 1.66  TO A PLAGIARIST:"]

   [:p
    "You are mistaken, insatiable thief of my writings, who think a poet can be made for the mere expense which copying, and a cheap volume cost. The applause of the world is not acquired for six or even ten sesterces. Seek out for this purpose verses treasured up, and unpublished efforts, known only to one person, and which the father himself of the virgin sheet, that has not been worn and scrubbed by bushy chins, keeps sealed up in his desk. A well-known book cannot change its master. But if there is one to be found yet unpolished by the pumice-stone, yet unadorned with bosses and cover, buy it: I have such by me, and no one shall know it. Whoever recites another's compositions, and seeks for fame, must buy, not a book, but the author's silence."]]

  [:hr]

  [:div.tribute-meta
   [:p.tribute-date "Event Date: " [:em "100 LA"]]
   [:p.tribute-attribution "— Marcus Valerius Martialis"]
   [:p.tribute-source
    [:a {:href "https://topostext.org/work/677"
         :target "_blank"
         :rel "noopener"}
     "ToposText · Loeb translation"]]]])