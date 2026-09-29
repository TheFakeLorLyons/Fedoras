^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 1 — Something That Draws Itself"}}}
(ns act4.s1-something-that-draws-itself2
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.tree.tree :as tree]))

^:kindly/hide-code
(sp/assets)

;; # Scene 1 — Something That Draws Itself

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 19)

^:kindly/hide-code
(sp/scene
 (art/email
  {:from "Systems <systems@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "9 February, 8:00 AM"
   :subject "Induction — fourth floor"}
  "Welcome to Systems."
  "Hours are Mondays and Wednesdays, 9:00 AM to 1:00 PM, and are paid at the standard rate."
  "Work is issued as specifications. A specification tells you what a component must do. It does not tell you what the component is for, and you should not ask, because the person issuing it does not know either."
  "Helps are permitted and encouraged on all Systems work."
  "There is no dress requirement on the fourth floor."
  "Systems hours are not a component of review. Systems hours are recorded.")

;; ---

 (sp/action "Systems is on the fourth floor of the Sterling Center and the"
            "fourth floor has carpet. Nothing else in this university has"
            "carpet.")

 (sp/action "The work comes on a page. The page says what a thing has to do"
            "and does not say what it is for, and the induction letter had"
            "told him at the start that the man who signs the pages does not"
            "know either, which he had taken for a joke until he met the man.")

 (sp/action "DUNN has been on that floor since before {pc} was born. He is not"
            "unkind and he is not stupid and in the second week he says that"
            "the trick to Systems is not to be interested. He says it the way"
            "you pass on a recipe.")

 (art/handout
  "SPECIFICATION 4471-K · issued 2 March · M. DUNN"
  "A function is required which accepts a list of readings and returns the same list with any reading outside the given band removed."
  "Readings are timestamped. Order must be preserved."
  "A removed reading is not an error and must not be reported."
  "The band is supplied at run time and must not be assumed."
  "Estimated: half a day."
  "Return by: 4 March.")

 (sp/action "It takes him twenty minutes. The estimate on the page says half a"
            "day. He sits with it afterwards for longer than it took to write,"
            "and then he does the next one, and there is nothing on the fourth"
            "floor to do once the page is done, so he goes and looks out of"
            "the window at the quad until one o'clock and gets paid for it.")

 (sp/action "The specifications are all like that. Take a list and give back a"
            "shorter list. Put things in an order. Hold things until something"
            "asks for them. He is good at it in the way that anybody is good"
            "at a thing that has been cut down to the size of one morning.")

;; ---

 (sp/action "And there is the other thing, which he has not said to anybody,"
            "because saying it would mean saying the rest of it.")

 (sp/action "He has not bled since the first week of February.")

 (sp/action "No smell. No gap. Nothing since the night they came up. He is on"
            "a carpeted floor above four other floors twice a week, and he"
            "sleeps, and he eats, and the thing that has been happening to him"
            "since a warm room in August is not happening.")

 (sp/action "He worked out why in about the second week, sitting in a chair"
            "with a page on his knee, and what he did about it was nothing,"
            "and what he said about it was nothing.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "18 Feb, 11:40 PM" :text "Attendance four of five."}
   {:who "Victor" :role "PRESIDENT" :at "25 Feb, 11:38 PM" :text "Four of five."}
   {:who "Miles" :at "25 Feb, 11:41 PM" :text "he has a job victor"}
   {:who "Victor" :role "PRESIDENT" :at "25 Feb, 11:44 PM"
    :text ["Wednesdays are Wednesdays. That is Article Six and it was"
           "ratified by five people, one of whom now has a job."]}
   {:who "Simon" :at "4 Mar, 1:10 AM" :text "hes been on fridays. hes not been on wednesdays"}
   {:who "Simon" :at "4 Mar, 1:11 AM" :text "im not making a point im just saying which"}
   {:who "Victor" :role "PRESIDENT" :at "4 Mar, 8:00 AM" :text "Noted."}])

;; ---

 (sp/slug "INT. WHITLOCK HALL — LOWER GROUND — ROOM 4 — WEDNESDAY 4 MARCH, 7:00 P.M.")

 (sp/action "The same room. The same table. The card is on it before anybody"
            "sits down.")

 (sp/action "There is a box of tissues at Hale's elbow. In August they were in"
            "the lectern.")

 (sp/dialogue "HALE"
              "Eleven. Five, five, and the eleventh. Standings.")

 (sp/action "SIMON is fourth along and gets a line and a half out before Hale"
            "thanks him, and it lands the way it has landed since September,"
            "and Simon takes it the way he takes it.")

 (sp/action "He is grey under the eyes and he has been grey under the eyes for"
            "a while and nobody has mentioned it because there is nothing to"
            "mention.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:50 P.M.")

 (sp/action "The end of the table.")

 (sp/dialogue :pc
              "It's stopped.")

 (sp/dialogue "WREN"
              "What has?")

 (sp/dialogue :pc
              "The thing that happened in here in August.")

 (sp/beat "Wren waits.")

 (sp/dialogue :pc
              "Five weeks. Nothing. I've been working upstairs.")

 (sp/dialogue "WREN"
              "How far upstairs?")

 (sp/dialogue :pc
              "Fourth floor of Sterling.")

 (sp/action "Wren does not say anything for a moment, and it is not the kind"
            "of moment where somebody is deciding whether to tell you"
            "something.")

 (sp/dialogue "WREN"
              "All right.")

 (sp/dialogue :pc
              "That's it?")

 (sp/dialogue "WREN"
              ["You've told me a thing about a floor. I'm in this"
               "chair every week and I've been in it two years and"
               "I'm not going to be your control group."])

 (sp/beat "{pc} does not have anything for that.")

 (sp/dialogue :pc
              "How do you get off the docket?")

 (sp/dialogue "WREN"
              ["Unattached students in good standing. That's the"
               "sentence. There's two ways off it and one of them"
               "is to stop being in good standing."])

 (sp/dialogue :pc
              "And Simon's done neither.")

 (sp/dialogue "WREN"
              ["Simon's been trying the other one since his first"
               "term and he's cheerful about it, which is worse"
               "than if he weren't."])

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 8:15 P.M.")

 (sp/dialogue "HALE"
              ["Preferences. In your own time, privately, and"
               "nothing said out loud."])

 (sp/beat "The room goes quiet.")

 (sp/action "It comes at about the second minute and it comes the way it"
            "always came: a coin held too long, and then a room that has been"
            "shut.")

 (sp/beat "He has not smelled it since the sixth of February.")

 (sp/action "He gets his hands under the edge of the table and holds on.")

 (sp/beat "It does not go further than that. It sits in the room with him for"
          "the rest of the four minutes and then it goes.")

 (sp/action "Four along on the near side, Simon puts a hand to his face and"
            "Hale is already moving, and the box is on the table now, so there"
            "is nothing to fetch.")

 (sp/dialogue "HALE"
              "Head forward.")

 (sp/dialogue "SIMON"
              "Yep.")

 (sp/action "Nobody stops what they are doing.")

 (sp/dialogue "HALE"
              "That's the docket. Outcomes are notified.")

 (sp/action "{pc} goes up the stair and stands in the corridor at the top of"
            "it, where the air is ordinary, and works out what he has just"
            "proved.")

 (sp/transition "Blackout.")

;; ---

 (sp/slug "INT. THE BLACK BOX — FRIDAY 6 MARCH — 9:40 P.M.")

 (sp/action "ELI has the room for the fortnight because somebody in Music"
            "booked it on a form that does not exist any more, and he has told"
            "everybody the story and it has got better each time.")

 (sp/action "He has put four lamps on the floor pointing up, which is the"
            "whole of his design and is better than the ceiling rig.")

 (sp/action "LENA made the poster. It is on the door and on both boards in"
            "Halloran and it is the best-looking object on this campus.")

 (sp/action "The band has no name. There was an argument about it for three"
            "weeks and the poster has the date and the room on it and nothing"
            "else.")

 (sp/action "MAYA has been playing a borrowed bass since January and is not"
            "good at it. She looks at her hands the entire time.")

 (sp/action "They are not good, and it does not matter for two songs, and in"
            "the third one the drummer and the bass find each other for about"
            "forty seconds and everybody in the room feels it happen.")

 (sp/beat "She looks up once, at the end, at the back of the room.")

 (sp/transition "Cut to:")

 (sp/slug "EXT. BEHIND THE BLACK BOX — 11:20 P.M.")

 (sp/action "Cold. A fire door propped with a chair. Music students carrying"
            "things past them the whole time.")

 (sp/dialogue :pc
              "It was good.")

 (sp/dialogue "MAYA"
              "It was forty seconds of good.")

 (sp/dialogue :pc
              "That's more than most things get.")

 (sp/action "She takes the notebook out of her coat. It is the same notebook"
            "and it has been in that coat since November.")

 (sp/dialogue "MAYA"
              ["Twelfth of November. Fourth of December, twice."
               "Ninth of January. Twenty-second of January. Fourth"
               "of February. Sixth of February, four minutes,"
               "under the refectory."])

 (sp/beat "She closes it.")

 (sp/dialogue "MAYA"
              "Nothing since.")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "MAYA"
              "Where have you been for five weeks?")

 (sp/beat "He does not answer.")

 (sp/dialogue "MAYA"
              ["Sterling. Fourth floor. Carpet. As far off the"
               "ground as anybody puts anybody on this campus."])

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "MAYA"
              "When did you work that out?")

 (sp/dialogue :pc
              "The second week.")

 (sp/dialogue "MAYA"
              "And you didn't say.")

 (sp/dialogue :pc
              "No.")

 (sp/action "A music student comes past with an amplifier and neither of them"
            "says anything until he has gone.")

 (sp/dialogue "MAYA"
              ["I'm not asking you to be ill. I want that said"
               "before anything else, because I can hear how the"
               "rest of it sounds."])

 (sp/dialogue :pc
              "All right.")

 (sp/dialogue "MAYA"
              "You haven't started the cards.")

 (sp/beat "There it is.")

 (sp/dialogue :pc
              "I haven't got a way into that building.")

 (sp/dialogue "MAYA"
              ["You haven't looked for one. You've had five weeks"
               "and you haven't opened a drawer and you know the"
               "range you need, because you found it in October"
               "with a piece of paper in your hand."])

 (sp/dialogue :pc
              "He told me not to come back down.")

 (sp/dialogue "MAYA"
              ["He told you to photograph a catalogue on the third"
               "floor of Beckwith in the dry. He said those were"
               "the same sentence. You've kept the half of it that"
               "lets you sit still."])

 (sp/beat "Four seconds.")

 (sp/dialogue :pc
              ["It's four hours twice a week and it's easy and I'm"
               "good at it and I like it."])

 (sp/beat "It is the first time he has said that out loud and he hears it at"
          "the same moment she does.")

 (sp/dialogue "MAYA"
              "Yes.")

 (sp/dialogue :pc
              "Don't.")

 (sp/dialogue "MAYA"
              ["I'm not doing anything. In February you wrote a"
               "machine with three lines in it you didn't put"
               "there, and I wrote the same three lines, and we"
               "counted them at six in the morning. Then they put"
               "you upstairs and you stopped bleeding and started"
               "being good at something."])

 (sp/dialogue :pc
              "So what am I supposed to do about it?")

 (sp/dialogue "MAYA"
              ["Ask what the readings are. That's it. That's the"
               "whole of what I'm asking for tonight and you"
               "haven't done it and it would take a minute."])

 (sp/action "She goes back in through the fire door. ELI is holding it open"
            "and has heard the end of it and does not say anything, and goes"
            "in after her.")

 (sp/action "LENA is against the wall a little way down and has heard all of"
            "it.")

 (sp/dialogue "LENA"
              "She's right and it isn't what she's angry about.")

 (sp/dialogue :pc
              "What's she angry about?")

 (sp/dialogue "LENA"
              ["That it's working. And that they put it in front"
               "of you and not her, and she'd have taken it, and"
               "she knows she would."])

 (sp/beat "She goes in.")

 (sp/action "He stands out there on his own for a while and it is very cold"
            "and the low notes are coming through the wall from the practice"
            "rooms two corridors away.")

 (sp/transition "Blackout.")

;; ---

 (sp/slug "INT. STERLING CENTER — FOURTH FLOOR — MONDAY 9 MARCH, 9:20 A.M.")

 (sp/action "Carpet. Eight desks and four people at them.")

 (sp/dialogue :pc
              "Can I ask you something about the spec?")

 (sp/dialogue "DUNN"
              "It's a bad spec?")

 (sp/dialogue :pc
              "It's a fine spec. What are the readings?")

 (sp/beat "Dunn looks up.")

 (sp/dialogue "DUNN"
              "Readings of what?")

 (sp/dialogue :pc
              "That's the question.")

 (sp/action "Dunn sits back. He is not annoyed. He is doing something he has"
            "not done for a long time, which is thinking about a page he has"
            "already signed.")

 (sp/dialogue "DUNN"
              "Nobody's asked me that.")

 (sp/dialogue :pc
              "In twenty-two years?")

 (sp/dialogue "DUNN"
              "In twenty-two years. I'll ask upstairs.")

 (sp/dialogue :pc
              "Is there an upstairs?")

 (sp/dialogue "DUNN"
              "There's always an upstairs.")

 (sp/action "He writes it on a slip and puts the slip in a tray, and the tray"
            "is emptied on Thursdays.")

 (sp/beat "He has not come back by the end of the week.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "Assignment five. Something that draws itself.")

 (sp/action "A line. At the far end of the line, two shorter lines going off"
            "at an angle. At the far end of each of those, the same again.")

 (sp/action "The whole of it is that the rule mentions itself. There is"
            "nothing else in it, and it took him nine minutes, and he has not"
            "told anybody that either.")

 (sp/action "He did not tell it to be a tree. He told it to do the same thing"
            "again from where it got to.")

 (sp/action "Change the angle and everything changes and nothing in the rule"
            "changes. Take it deeper and it gets slower and does not get any"
            "cleverer.")

 (sp/action "He submits it on the Friday and it is marked full and there is"
            "one program left in the set and it is the one that is his own.")

 (tree/widget))