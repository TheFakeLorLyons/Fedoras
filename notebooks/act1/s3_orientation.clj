^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 3 — Orientation"}}}
(ns act1.s3-orientation
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.live :as live]))

^:kindly/hide-code
(sp/assets)

;; # Scene 3 — Orientation

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 10)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. WHITLOCK HALL — THE BIG AUDITORIUM — MORNING")

 (sp/action "One thousand and twenty-four chairs.")

 (sp/action "A banner hangs across the stage.")

 (sp/sign "NEW CARTHAGE UNIVERSITY"
          "YOUR FUTURE IS ALREADY HAPPENING")

 (sp/action "Under it, smaller:")

 (sp/sign "ORIENTATION SUPPORTED BY MERIDIAN SYSTEMS")

 (sp/dialogue :pc
              "Who's Meridian?")

 (sp/dialogue "MAYA" "three seats over, not looking up"
              "They do the banners.")

 (sp/action "Two staff step out and pull the banner apart. The top gives. The"
            "middle doesn't. New tape over old tape over staples, and the old"
            "tape has gone crusty, dirt under the edges.")

 (sp/action "Sterling waits a moment and then starts pulling at the seam and"
            "trying to cut it with some dull scissors. He gets the middle"
            "open. He is red-faced and sweating by the time he steps through."
            "The two staff let go and go back behind the stage. The banner"
            "hangs crooked.")

 (sp/beat "Nobody in the auditorium reacts. This is the entrance.")

 (sp/dialogue "A STUDENT" "quietly"
              ["Same banner every year. They tape it Sunday and"
               "cut it open Monday."])

 (sp/dialogue "STERLING"
              ["Welcome. Welcome."
               "Look around you."
               "One thousand and twenty-four."])

 (sp/beat "Applause.")

 (sp/dialogue "STERLING"
              ["Same as last year."
               "Same as the year before."])

 (sp/beat "More applause.")

 (sp/dialogue "MAYA"
              "How?")

 (sp/dialogue :pc
              "How what?")

 (sp/dialogue "MAYA"
              "Same number. Every year. How?")

 (sp/action "Nobody near her answers. A boy in front turns around, looks at"
            "her, and turns back.")

 (sp/dialogue "STERLING"
              ["Your faculty. Two hundred and fifty-six of the"
               "finest —"])

 (sp/action "He looks at a card.")

 (sp/dialogue "STERLING"
              "— people.")

 (sp/action "The faculty are seated behind him in a long row. They stand as"
            "they are named.")

 (sp/dialogue "STERLING"
              "Professor Carter. Chemistry.")

 (sp/dialogue "CARTER"
              ["My door's open."
               "It's the one that's open."])

 (sp/dialogue "STERLING"
              "Professor Reyes. Computer Science.")

 (sp/action "REYES stands. She does not wave. She sits.")

 (sp/off-screen "A GIRL BEHIND {pc}"
                "She was given that.")

 (sp/off-screen "A BOY"
                "Given what?")

 (sp/off-screen "A GIRL BEHIND {pc}"
                "The chair.")

 (sp/dialogue "STERLING"
              "Professor Bennett. English and History.")

 (sp/dialogue "BENNETT"
              ["I assign reading."
               "All of it is on the list."])

 (sp/beat "Nobody laughs. Bennett appears to have expected this.")

 (sp/dialogue "STERLING"
              "And Professor Whitlock. History.")

 (sp/action "WHITLOCK stands. It takes a while. One thousand and twenty-four"
            "people wait. He sits.")

 (sp/beat "The applause is thunderous.")

 (sp/dialogue "STERLING"
              "Professor Whitlock has been with us since —")

 (sp/action "He checks the card. He turns it over. There is nothing on the"
            "back.")

 (sp/dialogue "STERLING"
              "— for some time.")

 (sp/slug "INT. THE BIG AUDITORIUM — LATER")

 (sp/action "A VICE DEAN reads from a tablet. He does not introduce himself"
            "and is not introduced.")

 (sp/dialogue "VICE DEAN"
              ["Review is at the end of each term."
               "Students who do not pass review are repossessed."
               "Please keep your Help charged."])

 (sp/beat "Nobody reacts.")

 (sp/dialogue :pc
              "What's review?")

 (sp/dialogue "MAYA"
              "Grades.")

 (sp/dialogue :pc
              "Oh.")

 (sp/dialogue "VICE DEAN"
              "Dining Dollars do not roll over.")

 (sp/action "A groan from the whole room. It is the loudest sound of the"
            "morning.")

 (sp/dialogue "STERLING"
              "Questions.")

 (sp/action "Hands go up across the room.")

 (sp/action "VICTOR is already standing. He has been standing since Welcome.")

 (sp/dialogue "STERLING" "to a girl in the fourth row"
              "Yes.")

 (sp/dialogue "VICTOR"
              "Yes.")

 (sp/beat "Sterling finds him.")

 (sp/dialogue "STERLING"
              "I'm sorry?")

 (sp/dialogue "VICTOR"
              "Yes. I have one.")

 (sp/beat "He does not continue.")

 (sp/action "Somewhere in the middle of the room a chair creaks. The girl in"
            "the fourth row keeps her hand up for a while and then puts it in"
            "her lap.")

 (sp/dialogue "STERLING"
              "Go ahead.")

 (sp/dialogue "VICTOR"
              "It's less a question.")

 (sp/dialogue "STERLING"
              "Ah.")

 (sp/dialogue "VICTOR"
              ["It's more an observation. About the direction "
               "of the institution."])

 (sp/beat "He looks at his notebook. He has written it out.")

 (sp/dialogue "VICTOR" ["Attendance at two home games is"
                        "recorded and goes into the"
                        "engagement figure."
                        " "
                        "That figure is on the banner at the"
                        "gate."
                        " "
                        "Attendance at a recognised society is"
                        "not recorded anywhere by anyone."])

 (sp/dialogue "STERLING"
              "Mm.")

 (sp/dialogue "VICTOR" ["The Society meets thirty-four times a"
                        "year. I keep the minutes myself."
                        " "
                        "I have the minutes for six years,"
                        "which is longer than the figure has"
                        "existed."
                        " "
                        "I am asking for a roster code so that"
                        "I can file them, and I am asking"
                        "who receives them."])

 (sp/action "In the faculty row, CARTER looks at the clock. REYES does not"
            "look up from whatever she is doing in her lap. A vice dean writes"
            "something down that is not a note about rosters.")

 (sp/dialogue "STERLING"
              "Thank you, Mr Ashcroft.")

 (sp/dialogue "VICTOR"
              "It's Victor.")

 (sp/dialogue "STERLING"
              "Thank you.")

 (sp/action "Victor sits down. Miles writes something down. Sterling has"
            "already turned to the girl in the fourth row, who no longer has"
            "her hand up.")

 (sp/off-screen "BENNETT" "in the faculty row, to Carter"
                "Every year.")

 (sp/off-screen "CARTER"
                "Every year.")

 (sp/beat "Three of the hands go down.")

 (sp/dialogue "MAYA" "standing"
              "I have one.")

 (sp/dialogue "STERLING"
              "We're out of time.")

 (sp/beat "They are not out of time.")

 (sp/transition "Cut to:")

 (sp/slug "INT. FINE ARTS BUILDING — THE DARKROOM — ORIENTATION WEEK")

 (sp/action "The tour stopped in front of a mural on the Fine Arts building."
            "It was painted before the founding, or painted after, or painted"
            "to look like it was painted before. {pc} does not know. What he"
            "knows is that the woman in the mural is standing at a window, and"
            "the light on her face is the light his mother used to sit in when"
            "she thought no one was watching.")

 (sp/action "He was still looking at it when the tour moved on. By the time he"
            "looked up, the corridor was empty, and he was alone with the"
            "mural and the map and the four lines about CS 101.")

 (sp/action "He walks. He is not supposed to be in this wing. The corridor is"
            "too quiet. The lights are on, but they are the wrong kind of on,"
            "the way lights are on in a building after hours.")

 (sp/action "At the end of the corridor, a door stands a little open. Above"
            "it, a small red light is on. It is the kind of light that means"
            "do not enter, or enter quietly, or enter only if you are already"
            "lost.")

 (sp/action "There is a skateboard leaning against the wall outside the door."
            "It is worn along the edges, and the grip has been scraped clean"
            "in the middle, and one wheel is newer than the others.")

 (sp/action "Inside, MAYA has an old book open on the developing table. LENA"
            "sits on a stool, camera on her knee, not taking pictures. They"
            "are not hiding exactly. They are just in a room that is not on"
            "the tour.")

 (sp/dialogue "LENA" "Quietly"
              "You're sure nobody uses this room on Tuesdays.")

 (sp/dialogue "MAYA" "At a matching volume"
              ["I'm sure nobody uses this room at all. The lock was already"
               "broken when I found it. I keep meaning to fix it."])

 (sp/dialogue "LENA"
              "You keep meaning to fix it.")

 (sp/dialogue "MAYA" "A hushed shout"
              "I'm busy!!!")

 (sp/beat "Maya turns a page. The paper is thick and soft at the corners. The"
          "book is old, and neither of them can stop looking at it.")

 (sp/dialogue "LENA"
              "I keep thinking I should look away.")

 (sp/dialogue "MAYA"
              "So look away...")

 (sp/dialogue "LENA"
              "I can't.")

 (sp/dialogue "MAYA"
              "Me neither.")

 (sp/action "There is a chair beside the door. It has been moved aside. {pc}"
            "steps through, because he is lost, and because the red light is"
            "still on, and because he has not seen another person in ten"
            "minutes.")

 (sp/action "Lena is on her feet. The red light over the door pops, once, and"
            "throws a white flash across the room. In that flash, {pc} sees"
            "the page: a knight on a horse, a windmill, a line of text in a"
            "script he does not recognize.")

 (sp/dialogue "LENA"
              "Close the door!")

 (sp/action "{pc} closes the door. Lena's camera is now in her hand, though"
            "she has not picked it up. Maya has closed the book. They are both"
            "looking at him.")

 (sp/dialogue "MAYA"
              "You're the provisional.")

 (sp/dialogue :pc
              "How do you know that?")

 (sp/dialogue "MAYA"
              ["You were at enrolment. You put a parenthesis in something,"
               "then took it out four times."])
 
 (sp/action "She closes the book. The cover has no title. She keeps one hand"
            "on it, not hiding it, not showing it. Claiming it.")

 (sp/dialogue "LENA"
              "Do we know him?")

 (sp/dialogue "MAYA"
              "No.")

 (sp/action "Out in the hall, the tour group moves past. Someone says"
            "something about the printmaking studio. The sound is far away.")

 (sp/dialogue :pc
              "I won't tell anyone.")

 (sp/dialogue "LENA"
              ["Good. Because if you do, they won't just repossess"
               "you. They'll ask you questions until you wish"
               "they had."])

 (sp/beat "She doesn't say 'we'd be next.' She doesn't have to. The camera in"
          "her hand says it for her.")

 (sp/beat "The red light comes back on by itself. None of them reaches for the"
          "switch.")

 (sp/dialogue "LENA"
              "You have to show Eli.")

 (sp/dialogue "MAYA"
              "Maybe. Not here... and not yet.")

 (sp/action "{pc} leaves. The last thing he sees is Lena setting the camera "
            "down extremely carefully.")

 (sp/action "In the corridor, the map is still in his hand. It has a room"
            "here. The room is here. But he doesn't go back.")

 (sp/transition "Cut to:")

;; ---

 (sp/action "That night he asks his personal Help, because that's people turn"
            "to when they need a quick answer.")

 (art/ai-chat
  {:model "HELP · personal"}
  [{:role :user :text "what does repossessed mean"}
   {:role :assistant
    :text ["Repossession is the standard end-of-term process for students"
           "who do not pass review."
           "Would you like me to set a study reminder?"]}
   {:role :user :text "no. where do they go"}
   {:role :assistant
    :text ["Repossession is the standard end-of-term process for students"
           "who do not pass review."
           "Dining Dollars do not roll over."]}])

(sp/action "{pc} got lost on the way to the auditorium, but found that it "
           " was the only thing he wanted to hold on to from that morning. "
           "He thought about it again in the afternoon and wrote down what "
           "he had remembered of the walk. It was not much. He opened"
           "a file:")

 (sp/code
  "(def campus
  {:whitlock  {:exits {:out :quad :east :union}}
   :quad      {:exits {:north :prentiss :east :meridian :south 
                       :whitlock}}
   :prentiss  {:exits {:south :quad :north :chapel}}
   :chapel    {:exits {:south :prentiss}}
   :meridian  {:exits {:west :quad :north :sterling :down :annex}}
   :sterling  {:exits {:south :meridian}}
   :annex     {:exits {:up :meridian}}
   :union     {:exits {:west :whitlock :east :commons}}
   :commons   {:exits {:west :union}}})

(defn go [at dir]
  (or (get-in campus [at :exits dir]) at))

(reduce go :whitlock [:out :east])")

 (sp/action "Whitlock, out, east, Meridian. He does it again with a wrong turn"
            "in it to see what happens.")

 (sp/code "(reduce go :whitlock [:out :west :east])")

 (sp/action "The program doesn't indicate anything about 'invalid movement'. "
            "It returns the last 'valid' position and says nothing else. "
            "For a fleeting moment, {pc} has an insight that you can "
            "really only be lost in a place if you have a place to be.")

 (live/editable
  {:label "campus.clj" :rows 16}
  "(def campus
  {:whitlock  {:exits {:out :quad :east :union}}
   :quad      {:exits {:north :prentiss :east :meridian :south :whitlock}}
   :prentiss  {:exits {:south :quad :north :chapel}}
   :chapel    {:exits {:south :prentiss}}
   :meridian  {:exits {:west :quad :north :sterling :down :annex}}
   :sterling  {:exits {:south :meridian}}
   :annex     {:exits {:up :meridian}}
   :union     {:exits {:west :whitlock :east :commons}}
   :commons   {:exits {:west :union}}})

(defn go [at dir]
  (or (get-in campus [at :exits dir]) at))

(reduce go :whitlock [:out :east :down])"))