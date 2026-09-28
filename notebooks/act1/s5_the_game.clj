^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 5 — The Game"}}}
(ns act1.s5-the-game
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.dice :as dice]
            [fedoras.companions :as companions]
            [fedoras.sessions.one :as session-one]
            [fedoras.live :as live]))

^:kindly/hide-code
(sp/assets)

^:kindly/hide-code
(dice/assets)

;; # Scene 5 — The Game

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 17)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. HALLORAN UNION — ROOM 3C — WEDNESDAY, 7:00 P.M.")

 (sp/action "The folding table. Five chairs. A tin of dice that used to hold"
            "something else.")

 (sp/action "VICTOR sits behind a screen turned away from everybody. He is"
            "broad and soft and takes up a chair and a half. The ponytail is"
            "long, black and wet-looking at the root. The beard has come in on"
            "the neck and nowhere else and he keeps it. There is a shine on"
            "him under the strip light, at the hairline and along the sides of"
            "the nose. The T-shirt has a history and the room is downwind of"
            "it.")

 (sp/action "On his head is a tall hat of green felt glued over card, which he"
            "made himself. It is enormous. It juts. It is for no purpose"
            "except being seen from the other end of a room, and it leans, and"
            "when he turns his head it arrives a moment later.")

 (sp/beat "Nobody has said anything about the hat since {pc} had arrived, 
           and he wasn't about to be the first.")

 (sp/action "MILES has three sheets, two pencils and a plague doctor's mask,"
            "which he is wearing. The sheets are his by right. He is the"
            "documentarian, and the documentarian is the only person at the"
            "table apart from the DM who is permitted to write anything down:"
            "what was found, what was said, what was agreed and by whom, and"
            "whether it was approved."

            "Anything the documentarian has not recorded did not happen, and"
            "Victor rules on this the way other people breathe.")

 (sp/action "DEREK and SIMON are not allowed to keep notes and are visibly"
            "relieved about it. DEREK has a mug he brought from somewhere and"
            "a dice set in a felt bag. SIMON has a dice set lined up in front"
            "of him by size and is the only one sitting up straight.")

 (sp/action "{pc} has paper in front of him tonight only because they are"
            "still setting him up, and Miles has looked at it twice already.")

 (sp/action "The lid of a pizza box has a grid drawn on it in green marker,"
            "twelve by twelve, and four counters on the grid that are bottle"
            "caps.")

 (sp/action "{pc} has a sheet with RANGER at the top in Victor's handwriting"
            "and nine numbers on it that somebody else filled in for him. Near"
            "the bottom there is a line marked COMPANION and something written"
            "on it.")

 (sp/dialogue :guildmaster
              ["So. The bursar's office has a line in the ledger"
               "they can't account for. Every month for three"
               "years the Collegium has paid a salary to a"
               "lecturer in the natural philosophy faculty, and"
               "every month it has cleared, and last week somebody"
               "in accounts finally asked which lecturer."])

 (sp/dialogue :cleric
              "And nobody knows who it is.")

 (sp/dialogue :guildmaster
              ["Nobody knows. There's a classroom with his number"
               "on the door, and a syllabus, and thirty students"
               "who turn up to it, and not one of them can tell"
               "you the man's name or what he looks like or the"
               "last time he stood in front of them."])

 (sp/dialogue :documentarian "muffled, through the mask"
              "Mmf mmff mm mmfff.")

 (sp/beat "Nobody responds.")

 (sp/dialogue :derek
              "Mask, Miles.")

 (sp/action "Miles pushes the mask up.")

 (sp/dialogue :miles
              ["I said if he isn't in the records then he isn't a"
               "lecturer and the payments are the crime, not the"
               "man."])

 (sp/dialogue :guildmaster
              ["The payments are why you were sent. Nobody has"
               "sent you to find a person. They've sent you to"
               "close a line in a ledger."])

 (sp/action "Miles writes that down. It takes a while, because it goes in the"
            "ledger and the ledger has columns.")

 (sp/dialogue :victor
              "Read me the last thing you have.")

 (sp/action "Miles pushes the mask up.")

 (sp/dialogue :miles "reading"
              ["Payments, three years, unaccounted."
               "Faculty of natural philosophy."
               "Not approved."])

 (sp/dialogue :victor
              "Good.")

 (sp/action "Miles pulls the mask down. Simon has not written anything and"
            "does not want to.")

 (sp/dialogue :simon
              "Is anybody actually worried about him?")

 (sp/dialogue :victor
              ["Nobody's worried. Nobody has been worried for"
               "three years. What they have is a number that won't"
               "reconcile, and they'd like it to reconcile by the"
               "end of the month."])

 (sp/beat "Simon writes nothing down and does not stop looking at Victor.")

 (sp/dialogue :victor
              ["You're at the end of a corridor on the third"
               "floor. There's a storage room at the end of it and"
               "the door is locked from the inside."])

 (sp/dialogue :miles
              "I hit the door.")

 (sp/dialogue :victor
              "You may hit the door.")

 (sp/action "Miles rolls. It is not enough. Everybody at the table can see"
            "that it is not enough.")

 (sp/dialogue :victor
              ["The door is administrative. You'd need a form and"
               "the form is on the other side of it."])

 (sp/dialogue :derek
              "Is that a joke?")

 (sp/dialogue :victor
              "No.")

 (sp/action "SIMON leans over to {pc} and taps the sheet twice, near the top,"
            "where it says what a ranger is for.")

 (sp/dialogue :pc
              "I investigate at the floor.")

 (sp/beat "Victor stops.")

 (sp/dialogue :victor
              "Say that again.")

 (sp/dialogue :pc
              ["The corridor floor. If he's been in there, he came"
               "along the corridor. Is there anything on the"
               "floor?"])

 (sp/action "Victor looks at his screen for a moment longer than the question"
            "needs.")

 (sp/dialogue "VICTOR"
              "Roll for it.")

 (sp/stage "Roll it. What the table rolled is in the box.")

;; ---

 (dice/check
  {:id "corridor" :label "Tracking · the third-floor corridor" :dc 12
   :fumble (str "{pc} rolls a one. The tin goes over and Derek picks the dice "
                "up off the floor.|"
                "@VICTOR|"
                "There's nothing on the floor. There's never been anything on "
                "the floor, and you "
                "know that the way you know your own name, and you stop "
                "looking.")
   :fail (str "{pc} rolls {result-mod} and Miles says the number out loud before "
              "anybody has asked him to.|"
              "@VICTOR|"
              "Dust, and it's even, and it's even all the way to the door. "
              "Which is worse than a "
              "footprint would be, and you couldn't tell me why yet.")
   :success (str "At the table, {pc} rolls a {result-mod}. The tin is loud on the "
                 "folding table and "
                 "everybody looks at it.|"
                 "@VICTOR|"
                 "Three years of dust and one track through it, going in, and "
                 "nothing coming back "
                 "out. The track is eleven minutes old.")
   :crit (str "{pc} rolls a twenty. Nobody at this table has rolled a twenty "
              "since March and "
              "Victor stands up.|"
              "@VICTOR|"
              "One track going in and nothing coming out. And there, under "
              "the door, where the dust is pressed flat: "
              "somebody has been sitting with their back against it, for a "
              "long time, and recently.")})

;; ---

 (sp/dialogue "DEREK"
              "Three years and eleven minutes.")

 (sp/dialogue "VICTOR"
              "Yes.")

 (sp/dialogue :documentarian
              ["Hang on. Both of those can't be true at once, so"
               "one of them's new."])

 (sp/beat "Victor takes a moment.")

 (sp/dialogue :victor
              "Now you're playing.")

 (sp/beat "Nobody says anything for a moment.")

 (sp/dialogue :simon
              "I open the door.")

 (sp/dialogue :victor
              ["It opens. He's sitting on the floor with his back"
               "where the door was. He says he has been in here"
               "for three years and he would like to know why"
               "nobody came."])

 (sp/dialogue :documentarian
              "He's been missing eleven minutes.")

 (sp/dialogue :victor
              "He has.")

 (sp/dialogue :pc
              "Which one's true?")

 (sp/beat "Victor puts his hand flat on the screen.")

 (sp/dialogue :victor
              "Both. That's the campaign.")

 (sp/action "The storage room is deeper than a storage room. Behind the man"
            "there are shelves, and behind the shelves, on the floor, there is"
            "a cage with a card wired to the front of it. The card has a date"
            "on it and nothing else.")

 (sp/dialogue :pc
              "What's in the cage?")

 (sp/dialogue :victor
              ["Something that has been in a cage in a locked room"
               "for three years and is still alive, which ought to"
               "be your first question and isn't."])

 (sp/dialogue :cleric
              "Don't open the cage.")

 (sp/dialogue :paladin
              "Open the cage.")

 (sp/dialogue :documentarian "muffled"
              "Mmff mm mmfff mm mmffm.")

 (sp/dialogue :derek
              "Mask.")

 (sp/action "Miles pushes the mask up.")

 (sp/dialogue :documentarian
              ["I said I can't record it as approved if nobody"
               "approved it."])

 (sp/dialogue :victor
              "Then record it as not approved.")

 (sp/beat "Miles does, and enjoys it.")

 (sp/dialogue :pc
              "I open the cage.")

 (sp/action "Victor sits back and lets it happen without asking for a roll,"
            "which he has not done once all evening.")

 (sp/stage "Roll for what comes out. The table's is in the box beneath.")

;; ---

 (companions/table)

 (sp/dialogue "DEREK"
              "It came out on its own. Nobody told it to do that.")

 (sp/dialogue "VICTOR"
              "It's yours now. It doesn't need telling.")

 (sp/action "Something comes up the corridor behind them. Victor takes his"
            "time over it: the sound first, then the size of it, then the"
            "smell, and only afterwards the shape."

            "It is a dog. It is the wrong height at the shoulder and it has"
            "been made by whatever makes forms rather than by whatever makes"
            "dogs. It seems to be... rippling?")
 
 (session-one/what-it-did)

 (sp/dialogue "MILES"
              "We haven't written anything down yet.")

 (sp/dialogue "VICTOR"
              "No. Not yet.")

 (sp/action "The fight takes twenty minutes and is mostly Miles asking what"
            "the modifiers are. When it is over, {ac} is still there,"
            "and stays with the party for the rest of the evening, and is fed"
            "twice.")

 (sp/dialogue "VICTOR"
              ["The thing goes back down the corridor the way it"
               "came in. And then the corridor starts filling with"
               "dust."])

 (sp/dialogue "MILES"
              "From where?")

 (sp/dialogue "VICTOR"
              ["From nowhere. It's coming down like it's been"
               "coming down for years, and it's about an inch deep"
               "at the far end already, and it's getting deeper"
               "towards you."])

 (sp/dialogue "SIMON"
              "We run.")

 (sp/dialogue "VICTOR"
              ["You run, and you get to the stair, and it's up to"
               "your knees at the top step, and then you're in"
               "your beds and it's morning."])

 (sp/beat "Nobody says anything.")

 (sp/dialogue "VICTOR"
              ["The professor is teaching. The class is full."
               "There's no cage in that room and there's no room"
               "at that end of the corridor, and the corridor is"
               "clean, and the ledger reconciled overnight."])

 (sp/dialogue "SIMON"
              "Does anybody remember any of it?")

 (sp/dialogue "VICTOR"
              "You four do.")

 (sp/dialogue "MILES"
              "And the animal.")

 (sp/beat "Victor looks at the sheet in front of {pc}, where somebody has"
          "written the animal's name in.")

 (sp/dialogue "VICTOR"
              "And the animal.")

 (sp/action "The door opens. A man in a grey coat with a bin liner in one hand"
            "stands there and takes in the room: four students, a screen, a"
            "felt hat and a plague mask.")

 (sp/dialogue "THE JANITOR"
              "Twelve.")

 (sp/dialogue "VICTOR"
              "We're in the middle of something.")

 (sp/dialogue "THE JANITOR"
              "It's twelve.")

 (sp/dialogue "VICTOR" "to the room, not to Ted"
              ["The Help has it. Ask it for the last four minutes"
               "on Wednesday and we'll pick it up there."])

 (sp/action "Derek is already asking his for the last four minutes before Ted"
            "has finished holding the door open.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "EXT. CORLISS FIELD — SATURDAY — AFTERNOON")

 (sp/action "Cold. The whole campus, because attendance at two home games is"
            "recorded and this is the second.")

 (sp/action "{pc} came to get the attendance recorded and go, and MILES found"
            "him at the gate and walked him to the fourth row before he had"
            "worked out how to say no to it.")

 (sp/action "The FEDORA SOCIETY sit together. There is room either side of"
            "them.")

 (sp/action "MILES has the programme, because Miles has whatever there is to"
            "have.")

 (sp/dialogue "DEREK"
              "Bread and circuses.")

 (sp/dialogue "VICTOR"
              ["Three hundred and forty hours, Derek."
               "Look around you. This is two."])

 (sp/action "Chad throws. The stadium makes a noise. SIMON is watching the"
            "game, actually watching it.")

 (sp/dialogue "SIMON" "quietly"
              "He's good.")

 (sp/dialogue "VICTOR"
              "At this.")

 (sp/beat "Chad throws again. The noise is bigger.")

 (sp/dialogue "SIMON" "still watching"
              "At this.")

 (sp/action "{pc} takes the first game's programme out of his bag, because he"
            "keeps things, and puts the two side by side on his knee to see"
            "who is new.")

 (sp/beat "Nobody is new.")

 (sp/action "Somebody is gone. Number 30 is on the first sheet, with a name"
            "and a year and a town, and on the second sheet the number 30 is"
            "not there at all. The list runs 29, 31.")

 (sp/dialogue :pc
              "Who's thirty?")

 (sp/dialogue "MILES"
              "There isn't a thirty.")

 (sp/dialogue :pc
              "There was in September.")

 (sp/action "Miles takes the first sheet, reads it, and turns it over, and"
            "turns it back.")

 (sp/dialogue "MILES"
              "That's a misprint.")

 (sp/dialogue :pc
              "It has a town on it.")

 (sp/dialogue "MILES"
              "It's a misprint.")

 (sp/action "He hands it back and watches the game. Derek did not look up."
            "Victor is writing something in a notebook and did not hear any of"
            "it.")

 (sp/action "SIMON looks at the sheet for a long time and does not say"
            "anything, and gives it back, and looks at the field.")

 (sp/beat "{pc} puts both programmes in his bag.")

 (sp/beat "He does not throw either of them away.")

 (sp/transition "Cut to:")

 (sp/dialogue "VICTOR" "at the gate, on the way out"
              ["Film's tonight instead of Friday. On account of"
               "the game."])

 (sp/dialogue :pc
              "I was going to—")

 (sp/dialogue "VICTOR" "Grabbing {pc} by the shoulder"
              ["Movie night is a time honored tradition, and we"
               "are watching approved films. You can rest assured"
               "of their quality."])

 (sp/beat "{pc} goes.")

 (sp/slug "INT. HALLORAN UNION — ROOM 3C — SATURDAY, 6:00 P.M.")

 (sp/action "Lights off. The projector is booked to the Society under a form"
            "Victor renews in person, on the day, every six weeks.")

 (sp/action "Two boxes of pizza on the folding table. The empties go down to"
            "the wall in Whitlock on Monday, which is where the wall comes"
            "from.")

 (sp/sign "THE LONG SUMMER"
          "approved · 118 min · as supplied, 104 min")

 (sp/action "A man walks up a road with a coat over his arm. It is a long shot"
            "and it lasts a long time and nothing is said in it.")

 (sp/dialogue "MILES" "with his mouth full"
              "What is it?")

 (sp/dialogue "DEREK"
              "It's on the list.")

 (sp/dialogue "MILES"
              "That isn't what I asked.")

 (sp/action "It is old and it has been approved twice, which shows: the"
            "picture jumps and the sound arrives late for a while afterwards.")

 (sp/action "Before the first jump there is a long stretch in a living room. A"
            "woman is painting the piano, and the wall behind it The window is"
            "open on a soft summer afternoon. There is birdsong. Nothing in"
            "the room is wrong. She works and the camera doesn't move, going"
            "on long enough that the audience begins looking for the thing"
            "they are supposed to have noticed.")

 (sp/action "The picture jumps again and the sound arrives fragmented. She is"
            "no longer in the room, and the piano is finished, with the window"
            "shut.")

 (sp/action "At the second jump the film loses three minutes of itself. One"
            "shot is a man at a door and the next is the same man in a road,"
            "and there is nothing between them.")

 (sp/action "DEREK talks over the gap, from memory, without being asked, and"
            "he is doing it for {pc}, who is the only one in the room who has"
            "not seen it.")

 (sp/dialogue "DEREK"
              ["He goes to the house. The brother's already there,"
               "in the kitchen, with his coat still on. They don't"
               "say anything. They stand there. They don't show"
               "you that any more."])

 (sp/dialogue :pc
              "Why not?")

 (sp/dialogue "DEREK"
              "It came back short.")

 (sp/beat "Nobody in the room finds this remarkable.")

 (sp/action "The film ends at the gate of the house. The man has his hand on"
            "the gate, yet does not open it. Resting on this image, the picture "
            "fades slowly, panning towards a deepening sunset.")

 (sp/dialogue "MILES"
              "Why doesn't he go in?")

 (sp/dialogue "DEREK"
              "That's in the three minutes.")

 (sp/action "Victor lets the credits run to the end before he puts the lights"
            "on.")

 (sp/action "They stack the chairs. Simon takes the boxes out into the"
            "corridor and comes back for the other two.")

 (sp/dialogue "MILES"
              "What's on for next week?")

 (sp/action "Derek has the list up before Miles has finished asking. There are"
            "nine films on it and despite having seen all of them, will see"
            "all of them again.")

 (sp/dialogue "DEREK"
              ["There's a new one. It's a documentary. It's about"
               "rooms."])

 (sp/dialogue "MILES"
              "Rooms?")

 (sp/dialogue "DEREK"
              ["Places with nothing in them. They go in and film"
               "them and come out."])

 (sp/dialogue "MILES"
              "Why?")

 (sp/dialogue "DEREK"
              ["Ninety-one minutes. Approved in June. That's all"
               "it says."])

 (sp/beat "Victor writes it in the diary for Friday.")

 (sp/action "{pc} is still laughing about something Simon said when he gets to"
            "the stairwell, and he is on his own by then.")

 (sp/transition "Blackout.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Derek" :at "10:41 PM" :text "the janitor is called Ted by the way. i asked him"}
   {:who "Miles" :at "10:43 PM" :text "why"}
   {:who "Derek" :at "10:44 PM" :text "so we can ask him for the extra half hour"}
   {:who "Derek" :at "10:44 PM" :text "he said no but he said it nicely"}
   {:who "Miles" :at "10:58 PM" :text "we could start at six"}
   {:who "Simon" :at "10:59 PM" :text "cant. docket"}
   {:who "Miles" :at "11:01 PM" :text "again?"}
   {:who "Simon" :at "11:14 PM" :text "theyve got me down for the whole term"}
   {:who "Miles" :at "11:15 PM" :text "thats not what the docket is"}
   {:who "Simon" :at "11:31 PM" :text "i know what the docket is miles"}
   {:who "Victor" :role "PRESIDENT" :at "8:00 AM"
    :text ["Attendance this week: Wednesday five of five, Thursday five of"
           "five, Friday five of five, Saturday five of five."
           "Four for four, and I have written it down, and it is going in"
           "the letter."]}])

;; ---

 ^:kindly/hide-code
 (sp/action "Sunday. Assignment one, which is the one that counts to a hundred"
            "and says fizz on the threes and buzz on the fives.")

 (sp/code
  "(defn word [n]
(cond
  (zero? (rem n 3)) \"fizz\"
  (zero? (rem n 5)) \"buzz\"
  :else n))
(take 15 (map word (range 1 16)))")

 (sp/action "Fourteen of the first fifteen are right. Fifteen says fizz, and"
            "fifteen is supposed to say both, and it says fizz because that is"
            "the first question he asked it and it stopped asking questions as"
            "soon as one of them was true.")

 (sp/code
  "(defn better-word [n]
(cond
  (zero? (rem n 15)) \"fizzbuzz\"
  (zero? (rem n 3))  \"fizz\"
  (zero? (rem n 5))  \"buzz\"
  :else n))
(map better-word (range 1 21))")

 (sp/action "The order of the questions is the whole assignment. He does not"
            "put it that way. He puts it that fifteen has to go first because"
            "fifteen is both, and that is the same sentence with less in it.")

 (sp/code "(map better-word [15 30 45 60 75 90])")

 ^:kindly/hide-code
 (live/editable
  {:label "FIZZ.CLJ" :rows 14}
  "(defn word-final [n]
  (cond
    (zero? (rem n 15)) \"fizzbuzz\"
    (zero? (rem n 3))  \"fizz\"
    (zero? (rem n 5))  \"buzz\"
    :else n))

;; move the 15 line to the bottom and see what happens to 15, 30, 45
(mapv word-final (range 1 21))"))