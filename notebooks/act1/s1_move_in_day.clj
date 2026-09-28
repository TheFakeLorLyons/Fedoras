^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 1 — Move-In Day"}}}
(ns act1.s1-move-in-day
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.live :as live]))

^:kindly/hide-code
(sp/assets)

;; # Scene 1 — Move-In Day

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 1)

^:kindly/hide-code
(sp/scene
 (sp/slug "New Carthage University — that is to say, anywhere.")

 (sp/action "A banner:")

 (sp/sign "WELCOME, CLASS OF 20XX!")

 (sp/action "Under it, a second banner:")

 (sp/sign "RANKED #1 IN STUDENT ENGAGEMENT*"
          "*According to New Carthage University.")

 (sp/beat "A tuba.")
 (sp/beat "A girl is sick into a hedge. Her mother films it.")

 (sp/action "{pc-full} enters with a suitcase and a desktop tower.")

 (sp/dialogue :pc
              "Okay.")

 (sp/action "A football hits him in the chest. He catches it with one hand, at"
            "the cost of the suitcase.")

 (sp/action "CHAD arrives. He is enormous. Two women hang off his arms, a"
            "third follows with a baby.")

 (sp/dialogue "CHAD"
              "Shit! Sorry, bro. Nice hands.")

 (sp/dialogue :pc
              "It hit me.")

 (sp/dialogue "CHAD"
              "You play?")

 (sp/dialogue :pc
              "Not intentionally.")

 (sp/dialogue "CHAD"
              "I'm Chad.")

 (sp/action "He looks at the tower.")

 (sp/dialogue "CHAD"
              "What's that?")

 (sp/dialogue :pc
              "A computer.")

 (sp/dialogue "CHAD"
              "Where's the screen?")

 (sp/dialogue :pc
              "It's the part that thinks.")

 (sp/beat "Chad looks at it for some time.")

 (sp/dialogue "CHAD"
              "Is there a Help in it?")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "CHAD"
              "Whoa, sick.")

 (sp/dialogue "GIRL ON CHAD'S LEFT"
              "He's Legacy.")

 (sp/dialogue "CHAD"
              "Legacy don't carry their own stuff.")

 (sp/dialogue :pc "Passing the football back"
              "I'm a scholarship.")

 (sp/dialogue "CHAD"
              "Restoration?")

 (sp/dialogue :pc
              "Provisional.")

 (sp/dialogue "CHAD" "Spinning the football on his finger, effortlessly"
              ["My cousin went Provisional."
               "He does water now."])

 (sp/action "The women nod. It is a good outcome.")

 (sp/dialogue "CHAD"
              ["Party Friday."
               "Bring the thinking part."])

 (sp/action "He throws the ball back over his shoulder toward the team.")

 (sp/dialogue "WOMAN WITH THE BABY" "flat"
              "He does that.")

 (sp/off-screen "VICTOR"
                "FRESHMAN.")

 (sp/action "VICTOR crosses the quad at a diagonal. Three men follow him in a"
            "wedge.")

 (sp/dialogue "VICTOR"
              ["Victor Ashcroft."
               "Senior. President. Mentor. Visionary."
               "Supreme —"])

 (sp/action "Miles coughs.")

 (sp/dialogue "VICTOR"
              ["— Moderator."
               "Not officially."
               "-not yet."])

 (sp/action "He puts out hand. {pc} takes it.")

 (sp/dialogue :pc
              "Your hand's wet.")

 (sp/dialogue "VICTOR"
              "Yes.")

 (sp/action "He does not let go.")

 (sp/dialogue "VICTOR"
              "Do you believe society has gone terribly wrong?")

 (sp/dialogue :pc
              "I got here thirty seconds ago.")

 (sp/dialogue "VICTOR"
              "Exactly when they get you.")

 (sp/dialogue "MILES"
              "His family was almost Legacy.")

 (sp/dialogue "VICTOR"
              "We were on the list.")

 (sp/dialogue "DEREK"
              "There was a list.")

 (sp/action "He points at a folding table. It is the Anime Society's table. A"
            "paper is taped to the corner of it.")

 (sp/sign "FEDORA SOCIETY"
          "Logic. Debate. Civilization."
          "FREE PIZZA")

 (sp/beat "There is no pizza.")

 (sp/dialogue "MILES"
              "Is there pizza?")

 (sp/dialogue "VICTOR"
              "Q2, Miles.")

 (sp/dialogue "DEREK" "with his mouth full"
              "This is what they did in Rome.")

 (sp/dialogue :pc
              "What's he eating?")

 (sp/dialogue "SIMON"
              "Nobody knows.")

 (sp/action "Derek drops it. Victor steps in it.")

 (sp/dialogue "VICTOR"
              "SHITTE.")

 (sp/dialogue "MILES" "reverent"
              "He says it the old way.")

 (sp/action "Victor scrapes his shoe on the Chess Club's table, then kicks the"
            "table over.")

 (sp/dialogue "VICTOR"
              ["Thursdays."
               "Bring a friend."
               "Bring several."])

 (sp/action "Nobody looks up. The Anime Society tries to pretend Victor doesn't exist.")

 (sp/off-screen "CHESS CLUB PRESIDENT"
                "Don't sign anything.")

 (sp/action "Victor is already forty feet away, saying the same sentence to a"
            "family who are here for a wedding.")

 (sp/beat "The tuba plays three notes.")
 (sp/beat "The third is wrong.")

 (sp/transition "Blackout.")

 (art/handout
  "The Fedora Society"
  "Logic. Debate. Civilization."
  (art/spec-table
   [["Meets" "Thursdays, 7:00 PM, Student Union 3C (pending)"]
    ["Dues" "None. Dues are a mechanism of control."]
    ["Contact" "Discord (link on request, request in person)"]])
  "WE DISCUSS: epistemology · rhetoric · the decline · LAN"
  "WE DO NOT DISCUSS: the stain"
  "FREE PIZZA")

 (sp/slug "INT. THE OLD GYMNASIUM — ENROLMENT — CONTINUOUS")

 (sp/action "Hundreds of new freshmen are present with their parents in a"
            "queue going around the room twice. Six tables, with a woman at"
            "each one, and a screen turned away from the queue.")

 (sp/action "It moves fast. Nobody is filling anything in.")

 (sp/dialogue "THE WOMAN"
              "Reading at the speed of somebody who has read it since eight"
              ["Your day is saved. Saving is continuous, and"
               "doesn't require anything from you."])

 (sp/dialogue :pc
              "Okay.")

 (sp/dialogue "THE WOMAN"
              ["Peripherals are separate."
               "If you've been issued a peripheral, it's on a"
               "different sheet, and I haven't got that sheet."])

 (sp/dialogue :pc
              "I haven't got a peripheral.")

 (sp/dialogue "THE WOMAN"
              ["Most people haven't."
               "You may nominate one person who can speak to you"
               "privately."])

 (sp/dialogue :pc
              "Privately?")

 (sp/dialogue "THE WOMAN"
              ["Without asking..."
               "Everybody else has to ask. You'll know they're"
               "asking, and you can say no."])

 (sp/beat "{pc} got here at six this morning and has spoken to a quarterback"
          "and whatever Victor was.")

 (sp/dialogue :pc
              "Can I do it later?")

 (sp/dialogue "THE WOMAN"
              ["You can do it whenever you like."
               "It's blank until you do."])

 (sp/action "She turns the screen a quarter of the way round. Not far enough"
            "to read, and taps it once.")

 (sp/dialogue "THE WOMAN"
              "Next.")

 (sp/transition "Cut to:")

 (art/handout
  "NEW CARTHAGE UNIVERSITY · ENROLMENT · ACKNOWLEDGEMENT"
  "Read aloud to the student. Not signed, not a form."
  "You confirm that your day is saved."
  "You confirm that saving is continuous and requires no action on your part."
  "Peripherals are issued separately and are not part of this acknowledgement."
  "You may nominate one person who may speak to you privately."
  "You may change the nomination at any time. You may leave it blank."
  "Nothing in this acknowledgement is a component of review."
  "Acknowledgement is recorded.")

 (sp/slug "INT. THE OLD GYMNASIUM — FOUR MINUTES EARLIER")

 (sp/action "The BOY at the front of the queue is nominating somebody and it"
            "is taking a while, because the field wants a code and the person"
            "he wants does not have one.")

 (sp/dialogue "THE BOY"
              "It's my mother.")

 (sp/dialogue "THE WOMAN"
              "She'd have a code if she were enrolled anywhere.")

 (sp/dialogue "THE BOY"
              "She isn't.")

 (sp/dialogue "THE WOMAN"
              "Right. Then I'll do it by name and district.")

 (sp/action "It takes her four goes because he doesn't know the district and"
            "has to be talked round to it by way of what the bus said on the"
            "front.")

 (sp/beat "The queue does nothing. No one speaks, and four hundred people wait"
          "for this boy to remember the bus line home.")

 (sp/dialogue "THE WOMAN"
              "There. She can talk to you whenever she likes.")

 (sp/dialogue "THE BOY"
              "Thanks.")

 (sp/transition "Cut to:")

 (sp/action "Classes start Monday. {pc} has looked up what CS 101 is twice and"
            "both times the answer was the same four lines.")

 (art/ai-chat
  {:model "HELP · personal · v9"}
  [{:role :user :text "what is cs 101"}
   {:role :assistant
    :text ["CS 101 · Introduction to Programming · A. Reyes · Meridian Hall · 8:00."
           "Would you like me to complete the first assignment?"]}
   {:role :user :text "there isnt one yet"}
   {:role :assistant :text "Would you like me to complete the first assignment?"}
   {:role :user :text "write something that says my name"}
   {:role :assistant
    :text ["(def registration \"3812\")"
           "(defn say-me [] (str \"hello, provisional # \" registration)"
           "(say-me)"
           "Dining Dollars do not roll over."]}])

 (art/terminal
  "$ clj -M -e \"(load-file \\\"say.clj\\\")\""
  "Syntax error reading source at (say.clj:3:1)."
  "EOF while reading, starting at line 2."
  "$")

 (sp/code "(def registration \"3812\")
 
 (defn say-me [] (str \"hello, provisional # \" registration)
 
 (say-me)")

 (sp/action "{pc} looks at it for about ten minutes. Then he puts a closing"
            "parenthesis at the end of the second line, because its the line"
            "that indicates a problem, and he doesn't know what else could be"
            "wrong.")

 (sp/action "{pc} takes the paren out again to see. It stops working, so he"
            "puts it back. It works. {pc} doesn't feel like he learned"
            "anything from this, but he plays around with it, entering a"
            "couple different names anyway.")

 (live/editable
  {:label "say.clj" :rows 6}
  "(def registration \"3812\")
  
 (defn say-me [] (str \"hello, provisional # \" registration))
  
 (say-me)")

 (sp/dialogue :pc "To himself"
              ["Wow, I don't feel like I learned anything from"
               "this."])

 (sp/dialogue "His help"
              ["I can tell you are having trouble with the first"
               "assignment, Would you like me to complete the"
               "first assignment?"])

 (sp/dialogue :pc "(sighing) To his help"
              "No... not right now.")

 (sp/transition "Blackout"))