^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 2 — The Activities Fair"}}}
(ns act1.s2-the-activities-fair
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.live :as live]))

^:kindly/hide-code
(sp/assets)

;; # Scene 2 — The Activities Fair

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 3)

^:kindly/hide-code
(sp/scene
 (sp/slug "EXT. THE QUAD — ACTIVITIES FAIR — AFTERNOON")

 (sp/action "Sixty folding tables. Four hundred freshmen. Somewhere, the tuba.")

 (sp/action "The FEDORA SOCIETY table is a bedsheet over a card table. The"
            "bedsheet has a stain on it, roughly the size and colour of a"
            "continent nobody has visited.")

 (sp/action "VICTOR works the crowd with the desperation of a man whose"
            "charter requires five members and who currently has four.")

 (sp/dialogue "VICTOR" "to a passing freshman, gesturing at nothing"
              ["We meet Thursdays. We discuss the collapse of"
               "Western civilization. And LAN."])

 (sp/action "The freshman keeps walking. Victor watches him go with "
            "fading hope.")

 (sp/dialogue "VICTOR" "LOUDER"
              "THERE IS GOING TO BE PIZZA!!")

 (sp/dialogue "MILES" "quietly"
              "Is there?")

 (sp/dialogue "VICTOR"
              "Miles.")

 (sp/dialogue "MILES"
              "I'm only asking about the pizza.")

 (sp/dialogue "VICTOR"
              ["The pizza is a Q2 concern."
               "Hold up the chart."])

 (sp/action "MILES holds up the chart. It is laminated. It was laminated at a"
            "copy shop by a disaffectionate adult, who charged a small amount"
            "of money for it and said nothing during the exchange.")

 (sp/sign "DATING MARKET VALUE"
          "A THROUGH D, WITH SUBTIERS"
          "(source: podcast)")

 (sp/action "Across the aisle, the GOTH COLLECTIVE table is immaculate. Black"
            "cloth, ironed smooth. They have dozens of names on the sign-up"
            "sheet. Two of them are handing out juice boxes to overwhelmed"
            "freshmen and asking, with what appears to be real interest, how"
            "everybody's move-in went.")

 (sp/dialogue "GOTH" "warmly"
              ["Hi! Do you want a juice?"
               "There's no obligation. It's just juice."])

 (sp/action "Beside them the THEATRE GUILD is projecting.")

 (sp/dialogue "THEATRE KID"
              "at conversational volume, 
which is to say, very loudly"
              "WE ARE SO GLAD YOU CAME OVER.")

 (sp/action "BRITTANY films the entire aisle without appearing to look at any"
            "of it. ASHLEY moves table to table with a clipboard, shaking"
            "hands, remembering names, terrifying everyone. MADISON hands a"
            "business card to a squirrel-adjacent sophomore and calls it a"
            "touchpoint.")

 (sp/action "{pc} drifts through all of it holding a tote bag he did not ask"
            "for, which now contains: a stress ball, a condom, a pamphlet"
            "about mould, and a second, smaller tote bag.")

 (sp/action "SIMON is handing out flyers, badly, to nobody. He has been doing"
            "this for two hours. When {pc} takes one, Simon's whole face"
            "changes for about a quarter of a second.")

 (sp/dialogue "SIMON"
              ["Oh - thanks."
               "You don't have to read it."])

 (sp/action "Victor spots {pc} from thirty feet.")
 (sp/beat "His entire posture changes.")

 (sp/dialogue "VICTOR"
              ["{pc-full}."
               "Computer Science."])

 (sp/dialogue :pc
              "We met for like nine seconds yesterday.")

 (sp/dialogue "VICTOR"
              ["I remember everyone."
               "It's a discipline."])

 (sp/action "He does not remember everyone. Taped to the table, facing him, is"
            "a sheet of paper:")

 (sp/sign "MEMBERSHIP: 4" "REQUIRED: 5" "STATUS: URGENT")

 (sp/action "Behind him, DEREK has cornered a freshman at the CAMPUSGPT kiosk"
            "and has been explaining Rome to her for fifteen minutes. She has"
            "moved eighteen inches toward the exit in that time, the way a"
            "tide goes out.")

 (sp/stage "The scene continues after the following artifact.")

 (sp/action "A kiosk sits as one of hundreds which the university installed in"
            "June, at cost, instead of fixing the third-floor plumbing in"
            "Whitlock Hall.")

 (art/ai-chat
  {:model "CAMPUSGPT 4 · New Carthage University · \"Ask me anything!\""
   :system "You are CAMPUSGPT, a helpful assistant for New Carthage University. Answer student questions accurately and concisely. Do not speculate. If asked about campus dining, mention that Dining Dollars do not roll over."}
  [{:role :user :text "generate an image of the decline of civilization"}
   {:role :assistant
    :text ["[image]"
           "A photograph of a laminated sign on the door of the campus"
           "IT office reading: WI-FI OUTAGE — NORTH CAMPUS — WE ARE AWARE."
           "Dining Dollars do not roll over."]}
   {:role :user :text "no. civilizationally. the DECLINE"}
   {:role :assistant :text "[image] The same photograph, at a slight angle."}])

 (sp/slug "EXT. THE QUAD — ACTIVITIES FAIR — CONTINUOUS")

 (sp/dialogue "DEREK" "to the freshman, vindicated"
              "There. Look at it.")

 (sp/action "Victor produces a clipboard and a pen and places both in {pc}'s"
            "hands, in that order, without asking. He does not blink, and"
            "hasn't in some time.")

 (sp/dialogue "VICTOR" "Insistant"
              "It's just a name.")

 (sp/action "{pc} looks around for an exit and finds four hundred families and"
            "a tuba.")

 (sp/beat "He signs.")

 (sp/action "Victor takes the clipboard, crosses out the 4, and writes a 5"
            "with the deliberation of a man signing a treaty. He holds the"
            "pose. Miles applauds, once, and stops.")

 (sp/dialogue "VICTOR"
              "Quorum.")

 (sp/action "Chad passes with two other players and a crate of oranges.")

 (sp/dialogue "CHAD" "delighted"
              "Hey! {pc}!")

 (sp/action "Chad is gone before anyone can respond. The Fedora Society"
            "watches him go in complete silence.")

 (sp/dialogue "MILES" "quietly"
              "He knew your name.")

 (sp/dialogue "DEREK"
              "He KNEW your NAME.")

 (sp/dialogue "VICTOR" "after a long moment, evenly"
              "He's been briefed.")

 (sp/beat "Nobody asks by whom.")

 (sp/action "At the STUDENT SENATE table, ASHLEY accepts the Fedora Society's"
            "funding paperwork, reads it in four seconds, and smiles with the"
            "top half of her face.")

 (sp/dialogue "ASHLEY"
              ["Wonderful. Thank you so much."
               "This is Form 14."
               "Form 14 requires Form 14-B, which authorizes Form"
               "14."])

 (sp/dialogue "VICTOR"
              "Where is Form 14-B?")

 (sp/dialogue "ASHLEY"
              ["Attached to Form 14."
               "You've filled out Form 14."
               "So we'll need another one."])

 (sp/action "A silence of real spiritual weight.")

 (sp/dialogue "VICTOR"
              "SHITTE.")

 (sp/action "He overturns the Senate's card table. Pens, forms, and a bowl of"
            "individually wrapped mints go across the grass.")

 (sp/action "ASHLEY does not stop smiling. She looks at the secretary beside"
            "her, who is already writing.")

 (sp/dialogue "ASHLEY"
              ["Motion to overturn the table."
               "Seconded."
               "Carried."])

 (sp/dialogue "SECRETARY" "writing"
              "Carried at three-eighteen.")

 (sp/action "Two freshmen pick up the mints. The fair continues. Victor puts"
            "two fingers to the brim of his hat, slightly nods his head, and"
            "takes them away again.")

 (sp/dialogue "MILES" "after a while"
              "So is there pizza, or —")

 (sp/dialogue "VICTOR"
              "Q2, Miles.")

 (sp/transition "Cut to:")

;; ---

;; Later, in the Discord, at an hour when four of the five members have
;; class in six hours.

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "1:04 AM"
    :text ["Membership is at five. Charter is secure."
           "The Senate has declared war on the club by procedural means."]}
   {:who "Derek" :at "1:06 AM" :text "this is Carthage all over again"}
   {:who "Miles" :at "1:06 AM" :text "we ARE new carthage"}
   {:who "Derek" :at "1:07 AM" :text "EXACTLY"}
   {:who "Victor" :role "PRESIDENT" :at "1:09 AM"
    :text "New channel: #form-14-b. Discussion of Form 14-B outside #form-14-b is now off-topic."
    :reacts ["🫡 3" "🍕 1"]}
   {:who "Simon" :at "1:11 AM" :text "the new guy seemed alright"}
   {:who "Victor" :role "PRESIDENT" :at "1:11 AM" :text "He is promising."}
   {:who "Simon" :at "1:12 AM" :text "i meant like. as a person"}
   {:who "Victor" :role "PRESIDENT" :at "1:34 AM" :text "Agenda item one remains unresolved."}
   {:who "Victor" :role "DM" :at "1:37 AM" :text "Wednesday is still DnD. Bring your own dice. I am not doing dice again."}])

 (sp/slug "EXT. THE QUAD — THE PLACEMENT TABLE — CONTINUOUS")

 (sp/action "One table has better paper than everybody else's. Not a bedsheet"
            "and not a card table: a cloth, a stand, and a leaflet with a fold"
            "in it that somebody paid for.")

 (sp/action "A WOMAN with a lanyard is talking a queue of second years through"
            "the terms, cheerfully, one at a time, and getting through them"
            "quickly because she is good at it.")

 (sp/dialogue "THE WOMAN"
              ["You're saving your day anyway. That's the bit"
               "people miss. You're saving it in a corridor and"
               "you're saving it asleep."])

 (sp/dialogue "A SECOND YEAR"
              "So?")

 (sp/dialogue "THE WOMAN"
              ["So save it at a desk that pays for it."
               "Same day. Same saving. One of them has hours on"
               "the end of it."])

 (sp/dialogue "A SECOND YEAR"
              "What are the hours for?")

 (sp/dialogue "THE WOMAN"
              "Hours ballot. Ask anybody who's been up.")

 (sp/beat "Three people in the queue nod at that, and one of them moves up a"
          "place without being asked.")

 (sp/action "ASHLEY comes past with the clipboard, stops, and says something"
            "to the woman that they both find funny.")

 (sp/dialogue "ASHLEY" "to {pc}, who did not ask"
              "I'm placed. Array Four.")

 (sp/dialogue :pc
              "Congratulations.")

 (sp/beat "Proudly, with her chin up.")

 (sp/dialogue "ASHLEY"
              "Thank you.")

 (sp/action "She goes. CHAD comes past the other way with the crate of oranges"
            "and reads the top of the leaflet upside down.")

 (sp/dialogue "CHAD"
              "Array's good. My cousin's on Six.")

 (sp/dialogue :pc
              "Are you doing it?")

 (sp/dialogue "CHAD"
              "Nah, I'm placed.")

 (sp/dialogue :pc
              "Where?")

 (sp/dialogue "CHAD"
              "Here.")

 (sp/beat "{pc} doesn't bother asking what it means but nods his head as if he"
          "understands.")

 (sp/transition "Cut to:")

 ^:kindly/hide-code
 (art/handout
  "ARRAY PLACEMENT · OFFICE OF ALLOCATION"
  "Open to enrolled students in their second year and above. Sessions are four hours."
  "You are saving your day already. On an array you save it at a desk, in a seat that is yours, on a floor with a submanager who knows your name."
  "AFTERCALL is the time between one and the next. It is the only number that will ever be discussed with you."
  "HOURS accrue against your name. Hours are yours. Hours count towards your hours ballot."
  "Placement is not a component of review. Placement is recorded."
  "Array Nine is at capacity. Arrays Four, Six and Twelve are taking now.")

 (sp/action "11:40 p.m. He empties the tote bag onto the bed. Stress ball."
            "Condom. The pamphlet about mould. The second tote bag, and"
            "nothing in it.")

 (sp/action "He asks the Help what there is.")

 (art/ai-chat
  {:model "HELP · personal"}
  [{:role :user :text "what is there to do on campus"}
   {:role :assistant
    :text ["There are ninety-four recognized student organizations."
           "There are twelve facilities lines."
           "A facilities line is a group assigned to a building."]}
   {:role :user :text "so theyre just people who hang around a building"}
   {:role :assistant
    :text ["A facilities line is a recognized organization."
           "It has a budget and a supervisor."
           "It does not have meetings."
           "The goth collective is a facilities line."]}])

 (sp/action "He reads that twice. Then he opens a file and writes down what he"
            "knows.")

 (live/editable
  {:label "what-i-know.clj" :rows 40}
  "(def what-i-know
   {:saving
    [\"everyone's day is saved, continuously, and it doesn't require anything from you\"
     :the-old-gymnasium
     \"the woman at the screen\"]

    :peripherals
    [\"issued separately from the Help, on a different sheet\"
     :the-old-gymnasium
     \"the woman at the screen\"]

    :nomination
    [\"one person may speak to you privately, and you may leave it blank\"
     :the-old-gymnasium
     \"the woman at the screen\"]

    :approved-spaces
    [\"students stay in approved spaces\"
     :the-old-gymnasium
     \"the woman at the screen\"]

    :dining-dollars
    [\"do not roll over\"
     :the-help
     \"the Help\"]

    :the-help
    [\"a personal assistant, always on, always approved\"
     :the-help
     \"the Help\"]

    :the-parenthesis
    [\"a bracket missing from the end of the second line\"
     :the-help
     \"the broken program\"]

    :chad
    [\"a quarterback\"
     :the-quad
     \"a football to the chest\"]

    :victor
    [\"a senior, the president of the Fedora Society\"
     :the-quad
     \"himself\"]

    :fedora-society
    [\"Logic. Debate. Civilization.\"
     :the-quad
     \"a sign on a folding table\"]

    :form-14
    [\"requires Form 14-B, which authorizes Form 14\"
     :the-old-gymnasium
     \"Ashley\"]

    :goth-collective
    [\"a facilities line\"
     :the-help
     \"the Help\"]

    :the-floor
    [\"a programme about a call array\"
     :the-quad
     \"a sign at the next table\"]

    :repossession
    [\"a word\"
     :the-help
     \"the Help, in passing\"]})

 (defn from [who]
   (for [[topic [_ _ w]] what-i-know
         :when (= w who)]
     topic))

 (defn at [place]
   (for [[topic [_ p _]] what-i-know
         :when (= p place)]
     topic))

 (defn what [topic]
   (first (get what-i-know topic)))

 (count what-i-know)

 (from \"the woman at the screen\")

 (from \"the Help\")

 (at :the-quad)

 (at :the-refectory)

 (what :repossession)")

 (sp/action "Fourteen things. Four from the woman at the screen. Four from the"
            "Help. Three from the quad. The rest from people he did not expect"
            "to hear anything from.")

 (sp/action "The last line asks about a place he has not been. Nothing comes"
            "back. Nothing tells him why.")

 (art/commit
  {:hash "a3f1c09" :author "{pc-full} <{pc}@newcarthage.edu>" :date "Aug 22, 11:58 PM"}
  "first commit"
  ""
  "nobody has spoken to me since six except a quarterback and whatever that was")

 (sp/action "Somewhere in a private repository called civilization, someone"
            "else is committing. The repository has one contributor and 1,847"
            "commits, of which 1,846 are titled fix.")

 (art/commit
  {:hash "5c1d77e" :author "Victor Ashcroft <vex@newcarthage.edu>" :date "Aug 23, 3:52 AM"}
  "fix"
  ""
  "TODO: explain why this fails on every machine except mine")

 (sp/action "The function is called always-right?. It returns true.")

 (live/editable
  {:label "what-i-know.clj" :rows 10}
  "(defn always-right?
   \"TODO: explain why this fails on every machine except mine.\"
   []
   (and (true? true)
        (true? (not false))
        (true? (= 1 1))
        (true? (= (* 2 2) (Math/sqrt 16)))))
  
  (always-right?) ; Its always right, right?"))