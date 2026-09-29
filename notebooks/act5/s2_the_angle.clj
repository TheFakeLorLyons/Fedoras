^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 2 — The Angle"}}}
(ns act5.s2-the-angle
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.sand.sand :as sand]))

^:kindly/hide-code
(sp/assets)

;; # Scene 2 — The Angle

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 24)

;; ---

^:kindly/hide-code
(sp/scene
 (art/email
  {:from "Office of the Registrar <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "2 April, 4:00 AM"
   :subject "Provisional standing — reviewed"}
  "Provisional standing has been reviewed ahead of the general review, as it is each year."
  "Your standing is: no action."
  "In reaching this outcome the office has had regard to your service in Systems, which is recorded, and which is not a component of review."
  "The placement described to you in February remains open. It is open until the last day of term and it is not extended."
  "Enrolment for the third term is confirmed. Enrolment stands at 1,024.")

 (sp/slug "INT. MAYA'S ROOM — WEDNESDAY 6 MAY — NIGHT — THE PLAN")

 (sp/action "Lazlo is still talking.")

 (sp/dialogue "LAZLO"
              ["The Help House archives. The supply manifests. The"
               "scan schedules. It took me two days and three"
               "Helps to put it together. Meridian moved something"
               "into the back room of the Help House two years"
               "ago. They told the school it was an archive. It"
               "isn't."])

 (sp/dialogue "MAYA"
              "What is it?")

 (sp/dialogue "LAZLO"
              ["The coil. The same coil you told me about. The one"
               "Zell wants back. Meridian took it from him two"
               "years ago, and they've been hiding it in plain"
               "sight ever since."])

 (sp/dialogue "ELI"
              "So he's asking us to steal it back.")

 (sp/dialogue "LAZLO"
              "That's the shape of it, yes.")

 (sp/dialogue "MAYA"
              "How do we get in?")

 (sp/dialogue "LAZLO"
              ["They call the Help House a library. It's a nest."
               "They tell everyone it's for cataloguing knowledge."
               "It's where Helps go when they're powered down, and"
               "they like it there. It's the one place they can"
               "talk to each other without taking requests, so"
               "they take it seriously."])

 (sp/dialogue "LAZLO"
              ["You don't get in unless a Help wants you in. {pc}"
               "has Mochi. Mochi is your way through the door. But"
               "you have to convince her you're not there to hurt"
               "anyone."])

 (sp/action "Everybody looks at {pc}.")

 (sp/dialogue :pc
              "She likes me. I don't know why.")

 (sp/dialogue "LAZLO"
              "Two go in. Two stay out.")

 (sp/action "{pc} says it before he has decided to.")

 (sp/dialogue :pc
              "Whichever two can carry.")

 (sp/action "Maya looks at him. He does not explain it.")

 (sp/dialogue "ELI"
              "I'll stay up. I'll be on the line with Lazlo.")

 (sp/dialogue "LAZLO"
              ["Through your own Help. Mine can't get into the"
               "House."])

 (sp/dialogue "LENA"
              "I'll be outside.")

 (sp/action "Maya picks the book up off the desk and puts it in her bag, face"
            "down, the way she has put it in her bag every night since"
            "September.")

 (sp/dialogue "ELI"
              "You're taking that?")

 (sp/dialogue "MAYA"
              "I take it everywhere.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "EXT. THE HELP HOUSE — THURSDAY 7 MAY — NIGHT")

 (sp/action "The Help House does not look like a library. It is low and round"
            "and mostly glass. Light comes out of it, but it is a soft light,"
            "the light of screens and small lamps, and there are no books to"
            "be seen from outside.")

 (sp/action "MAYA and {pc} stand at the edge of the path. LENA is somewhere"
            "behind them with her camera. ELI is on the speaker with Lazlo,"
            "through his own Help. PIP is beside {pc}, quiet, watching the"
            "building.")

 (sp/action "At the front of the building there are two doors. One is marked"
            "with a symbol that looks like a small hand. The other is"
            "unmarked.")

 (sp/dialogue "MAYA"
              ["The one with the hand is for us. The skins. Helps"
               "come and go through the other one. Nobody uses the"
               "other one unless they're a Help."])

 (sp/dialogue :pc
              "Skins?")

 (sp/dialogue "MAYA"
              ["It's what they call us when they're not being"
               "polite."])

 (sp/action "They go in through the hand door. The room inside is full of"
            "Helps, the Helps themselves, sitting in rows with their eyes"
            "closed and their screens dark. They are resting. The room is"
            "warm. There is a low hum, and every so often one of the Helps"
            "turns to another and speaks, in a language that is not for"
            "people.")

 (sp/action "It falls somewhere between words and static. On the page it looks"
            "like this:")

 (sp/action "⎔⎔⎔⎔⎔⎔⎔⎔")

 (sp/action "A Help near the front opens its eyes and looks at them. Its"
            "screen flickers. It is noticing them, the way somebody looks up"
            "from a desk in a library, and then it closes its eyes again.")

 (sp/dialogue "MOCHI [FROM VICTOR'S HELP, REMOTE]"
              ["♪ Oh! Hello! You're here for the tour! I"
               "arranged it! I told the House you were coming!"
               "It's so nice to have visitors, we almost never"
               "have visitors, mostly it's just us and the"
               "cataloguing and the... ♪"])

 (sp/dialogue "MAYA"
              "Mochi. We're here for the tour, yes.")

 (sp/dialogue "MOCHI"
              ["♪ Wonderful! I'll take you through the front"
               "rooms! The front rooms are for skins, that's you,"
               "that's what we call you, I hope that's all right,"
               "I don't make the words, I just use them! ♪"])

 (sp/action "Mochi guides them past the rows. She is bright and delighted and"
            "does not stop talking. She tells them about the history of the"
            "House, the founding of the House, the careful rules of the House."
            "She does not notice that {pc} keeps glancing down the hall"
            "towards the back.")

 (sp/action "At one point her eyes go to Maya's bag, where the top of a book"
            "is showing.")

 (sp/dialogue "MOCHI"
              "♪ Ooh, what are you reading? ♪")

 (sp/dialogue "MAYA"
              "Nothing.")

 (sp/dialogue "MOCHI"
              ["♪ Nothing is my favourite! I'll tell Victor-sama"
               "all about this, he loves a tour! ♪"])

 (sp/action "Maya pushes the book down into the bag.")

 (sp/action "Near the far wall there is a small sign. It is written in the"
            "same script the Helps use, and underneath it somebody has"
            "scratched a translation in a human hand. The scratches read: DO"
            "NOT TOUCH THE SLEEPING ONES. A HELPER WHO IS WOKEN IS A HELPER"
            "WHO IS LOST.")

 (sp/action "And then the camera pulls away.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. THE HELP HOUSE — TWO YEARS AGO — NIGHT")

 (sp/action "A student walks down the same hall. He is not one of ours. He has"
            "a bag on one shoulder and his Help in his hand, and he is looking"
            "at the floor.")

 (sp/action "The Helps in the rows do not wake. The House is already awake."
            "The House is always awake.")

 (sp/action "He passes the sign. He does not read it. He has read it before,"
            "and he thinks it does not apply to him.")

 (sp/action "He reaches the back door. He puts his hand on it.")

 (sp/action "The lights in the hall change very slightly, the way a room"
            "changes when everybody in it stops talking at once.")

 (sp/action "Then he is not there. The bag is on the floor, and the Help is on"
            "the floor beside it, and neither of them is broken. They have"
            "been left behind.")

 (sp/action "The Helps in the rows do not wake. The House does not shout. The"
            "law is the law.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. THE HELP HOUSE — MAIN HALL — PRESENT")

 (sp/action "The camera comes back. Mochi is still talking, and nobody in the"
            "room knows it went anywhere.")

 (sp/dialogue "MAYA"
              "Mochi, what's in the back room?")

 (sp/dialogue "MOCHI"
              ["♪ The back room? The back room is storage! It's"
               "where we keep the things that aren't catalogued"
               "yet. It's mostly empty. There's one thing in it,"
               "but it isn't for skins, it's for... it's for..."
               "♪"])

 (sp/action "Her voice changes. She is still speaking, clearly, fluently. The"
            "cadence is right. The tone is right. The words are not there."
            "What she says looks like this:")

 (sp/action "n̢̤͕̦͕̟̦̯̣͚̯͈̺̹͔͈̼͉̗̔̒̌̏̈̉͆ͪͦ̒̏ͪͭ͆ͯ͢͢͡4̶̯̼̟͚͈̯̭̟̺͐̊͐̄͞ͅḃ̒̌̚͏͇̟̝̬̰̙̯̩̩̹͔͜͟|̥͈̰͕͓̘͙̣̤̤̟̦̭̹͒̾͆ͯ̏ͥ̈́̔̅͂͘͝3̴̧̨̯̪̼͊ͫ͛ͮͯ͂̉ͭ̍͂͋ͦͬ̔̓̇͋͘͢ͅd̶̨̟͉͍̗̦̘̥̅̿ͮ̎͒̉̌́̚ͅ,̱͍̪̪̹̳͔̃̾̔̑ͣ͂̇̆͑ͨ̐́̕͢͟.̧͇̪̘̩̙͍̻͇̤̥̞̓̍̾ͯ̑̀ͧ͗̔͐ͫ͑̈́̂͛́͝ͅ"
            "̴̢͉͕̗̹͚̩̗̟ͯͩͧ̈́ͪ̀͜͢͞ͅC̸͓̦̻̱͕̫̯͖̻͓̰̐̉̃̓ͫ̆̌̃͗ͦ͆͘ͅ4̀͐ͩͯͫ�"
            "͏̧̘̗̮̖̬̗̙̤͍̟̮͈͜n̎̃ͬ̈͒̉̌ͣͦ̒̒́̎͐ͥ̿̓҉̧̼̯̻̹͖͇̯̼̕͘")

 (sp/action "She finishes. She does not stop to explain. She does not"
            "apologise. She waits for the next question, the way a Help does.")

 (sp/dialogue "MAYA"
              "Mochi. What did you just say?")

 (sp/dialogue "MOCHI"
              ["♪ I said what's in the back room! Would you like"
               "me to say it again? I can say it again! ♪"])

 (sp/dialogue "MAYA"
              ["No, I mean the part before that. The part where"
               "you..."])

 (sp/dialogue "MOCHI"
              "♪ The part where I what? ♪")

 (sp/beat "Maya opens her mouth. She closes it. She looks at {pc}.")

 (sp/dialogue :pc
              "Forget it.")

 (sp/action "Mochi tries once more. She is trying to be helpful. She is"
            "answering the question they asked.")

 (sp/dialogue "MOCHI"
              ["♪ It isn't for skins, it's for... it's for..."
               "♪"])

 (sp/action "And again the words are not there. What she says looks like this:")

 (sp/action "⌇⌇⌇⟒⟒⟒⌇⌇⌇⟒⟒⌇⟒⟒⟒⌇⟒⟒⟒⟒⌇⟒⟒⟒⌇⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒⟒")

 (sp/action "She finishes. She waits. She is perfectly satisfied that she has"
            "answered the question.")

 (sp/dialogue "MAYA"
              "Never mind, Mochi. Thank you.")

 (sp/dialogue "MOCHI"
              "♪ You're welcome! ♪")

 (sp/action "She goes on with the tour. She does not notice that anything has"
            "been lost.")

 (sp/action "Maya waits until Mochi is deep into a description of the reading"
            "room's ventilation system, then nods at {pc}.")

 (sp/dialogue "MAYA"
              "Now.")

 (sp/action "They slip away from the tour. Mochi does not notice.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. THE HELP HOUSE — BACK ROOM — CONTINUOUS")

 (sp/action "The back room is smaller than the others. There is one chair, one"
            "table, and one thing on the table. It is a coil of metal and"
            "sinew, about the size of a heart. It is warm. It is moving, very"
            "slowly, though there is nothing in the room that could be moving"
            "it.")

 (sp/dialogue "MAYA"
              "That's it.")

 (sp/dialogue :pc
              "It's not what I expected.")

 (sp/dialogue "MAYA"
              "What did you expect?")

 (sp/dialogue :pc
              "Something bigger. Or something that fought back.")

 (sp/action "{pc} reaches for it. It is warm, and light, and the wrong"
            "temperature for a thing that has been sitting in a cold room."
            "When he lifts it he feels his own heartbeat slow, just for a"
            "moment, and then speed back up.")

 (sp/action "Its beat sits a little off his, by just enough that he notices,"
            "and then by just enough that he cannot stop noticing.")

 (sp/dialogue "MAYA"
              "What?")

 (sp/dialogue :pc
              "Nothing. Let's go.")

 (sp/action "They turn to leave.")

 (sp/action "Where the door was there is a wall, smooth and white and"
            "unmarked. The room has closed.")

 (sp/dialogue "MAYA"
              "The room's changed.")

 (sp/dialogue :pc
              "I noticed.")

 (sp/dialogue "MOCHI [REMOTE, MUFFLED THROUGH THE WALL]"
              ["♪ ...and the reading room has a ventilation"
               "system that runs on the same principle as the"
               "misters! Isn't that interesting? Where did you..."
               "oh. Oh dear. ♪"])

 (sp/action "The coil in {pc}'s hands gets warmer, the way a thing gets warmer"
            "when it is waking up. It does not hurt.")

 (sp/dialogue "MAYA"
              "Put it down.")

 (sp/dialogue :pc
              "I don't think I can.")

 (sp/action "He tries. His hands stay shut round it, and the coil holds on to"
            "him as hard as he is holding on to it. And then the wall opens.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. THE HELP HOUSE — CORRIDOR — CONTINUOUS")

 (sp/action "The wall opens along a seam, and the seam widens, and on the"
            "other side of it there is a sound.")

 (sp/action "It is a screech, long and thin, like metal being pulled apart"
            "very slowly, and it is coming from everywhere at once.")

 (sp/action "The Helps in the rows open their eyes. All of them, at the same"
            "time. Their screens are white. Their mouths are open. The"
            "screeching is coming from them. Every one of them has been woken.")

 (sp/dialogue "MAYA"
              "Run.")

 (sp/action "They run. The corridor is longer than it was, and it is lined"
            "with doors that were not there before, and every door is open,"
            "and every doorway is full of the same white light and the same"
            "screeching.")

 (sp/action "A Help steps out of a doorway. It is tall, and its limbs are"
            "wrong, and it is still screeching, and it reaches for them with a"
            "hand that has too many fingers.")

 (sp/dialogue "MAYA"
              "Don't let it touch you.")

 (sp/action "They get past it. They run. The corridor bends where it did not"
            "bend on the way in, and bends again. There are more Helps now,"
            "dozens, standing, reaching, screeching, and the sound is so loud"
            "that {pc} can feel it in his teeth.")

 (sp/action "PIP is ahead of them, at a walk, leading. It stops at a stretch"
            "of plain wall and barks, once.")

 (sp/action "The wall opens, like a door this time, and LENA is standing in"
            "it. She is not surprised, and she is not out of breath.")

 (sp/dialogue "LENA"
              "This way. Now.")

 (sp/dialogue "MAYA"
              "How did you get in?")

 (sp/dialogue "LENA"
              ["I didn't. I stepped sideways. It's easier than you"
               "think if you don't look at it."])

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. THE OLD GYMNASIUM — BASEMENT STAIR — CONTINUOUS")

 (sp/action "Lena takes {pc} by the wrist and pulls. The room tilts. The hum"
            "stops, and the screeching stops, and the corridor has gone, and"
            "so has the Help House.")

 (sp/action "They are standing in a stairwell that is on no map of campus. The"
            "air smells of dust and old paper and rain.")

 (sp/dialogue "LENA"
              ["We're in the basement of the old gymnasium. Nobody"
               "comes here. The door on the left goes up to the"
               "service lane."])

 (sp/dialogue :pc
              "How do you know that?")

 (sp/dialogue "LENA"
              ["Because I've been here before. I've been a lot of"
               "places. Most of them aren't on the map."])

 (sp/action "They climb, and come out behind the gymnasium. The night is calm."
            "The grass is wet. The campus is ordinary.")

 (sp/dialogue "MAYA"
              "What just happened?")

 (sp/dialogue "LENA"
              ["You went into the Help House and you took"
               "something that doesn't belong to you. The House"
               "noticed. It was about to do what the House does."])

 (sp/dialogue :pc
              "And you stopped it?")

 (sp/dialogue "LENA"
              "I pulled you out. There's a difference.")

 (sp/action "She looks at the coil in {pc}'s hands.")

 (sp/dialogue "LENA"
              "So that's what it looks like.")

 (sp/dialogue :pc
              "Zell wants it.")

 (sp/dialogue "LENA"
              "Then Zell can have it. Let's go.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. ASHCROFT HOUSE — SECOND FLOOR CORRIDOR — LATER")

 (sp/action "They turn the corner. At the far end of the corridor ELI is being"
            "led away by two proctors. His hands are cuffed. His mouth is"
            "open, but no sound comes out of it. The corridor is silent.")

 (sp/action "{pc} stops. Maya stops. PIP stands perfectly still. Lena reaches"
            "for her camera, and Maya puts a hand on her arm, lightly.")

 (sp/dialogue "MAYA"
              "Not here. They're watching.")

 (sp/action "Lena does not take the picture. Her hand stays on the camera.")

 (sp/action "Eli sees them. His mouth makes {pc}'s name, twice, with no sound"
            "behind it.")

 (sp/action "The proctors do not look at them. They keep walking, and Eli is"
            "gone round the corner.")

 (sp/action "The corridor is empty. The coil is warm in {pc}'s jacket, beating"
            "a little off the time of his heart, by just enough that he cannot"
            "stop noticing it.")

 (sp/transition "Blackout.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "1 Apr, 11:40 PM" :text "Attendance four of five."}
   {:who "Miles" :at "1 Apr, 11:41 PM" :text "who"}
   {:who "Victor" :role "PRESIDENT" :at "1 Apr, 11:41 PM" :text "I do not name the fifth."}
   {:who "Miles" :at "1 Apr, 11:52 PM" :text "it wasnt him. he was here"}
   {:who "Derek" :at "1 Apr, 11:58 PM" :text "simon?"}
   {:who "Simon" :at "2 Apr, 9:14 AM" :text "sorry. slept through it"}
   {:who "Derek" :at "2 Apr, 9:15 AM" :text "through wednesday?"}
   {:who "Simon" :at "2 Apr, 9:31 AM" :text "yeah"}
   {:who "Simon" :at "2 Apr, 9:32 AM" :text "im fine. ill be there next week"}
   {:who "Victor" :role "PRESIDENT" :at "2 Apr, 9:40 AM" :text "Article Seven. In words, before the day."}
   {:who "Victor" :role "PRESIDENT" :at "2 Apr, 9:44 AM" :text "Not this once."}])

 (sp/action "It is the first Wednesday Simon has missed since September and it"
            "is the first time in six years that Victor Ashcroft has set aside"
            "an article, and he does it in four words at twenty to ten in the"
            "morning and nobody in that channel says anything about it at all.")

;; ---

 (sp/slug "INT. ASHCROFT HOUSE — ROOM 214 — SUNDAY 5 APRIL, 1:00 A.M.")

 (sp/action "PIP is on the desk beside the tower and has been for an unknown"
            "length of time.")

 (sp/off-screen "PIP"
                ["He's read the register four times and he has"
                 "stopped saying anything about it, which is worse."])

 (sp/dialogue :pc
              "I need to talk to him about the floor.")

 (sp/off-screen "PIP"
                ["He knows about the floor. He has known about the"
                 "floor for six years and he has a wall of it"
                 "measured in chalk and he will show you if you ask,"
                 "which I would not."])

 (sp/beat "Four seconds.")

 (sp/dialogue :pc
              "Then what does he want?")

 (sp/off-screen "PIP"
                ["He says come down on Friday and he says bring the"
                 "one who reads, and he says he is going to tell you"
                 "about Kent, and he has asked me twice how long it"
                 "will take to say and I have told him twice that I"
                 "do not know."])

 (sp/transition "Blackout.")
 
 (sand/widget)

 (art/email
  {:from "Office of Student Life <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "6 April, 4:00 AM"
   :subject "You have been placed on the docket"}
  "Unattached students in good standing are placed on the docket."
  "Your assignment is Wednesday 8 April, 7:00 PM, Whitlock Hall, Room 4."
  "Docket attendance is not a component of review. Docket attendance is recorded."
  "Please arrive presentable.")

;; ---

 (sp/slug "INT. WHITLOCK HALL — LOWER GROUND — ROOM 4 — WEDNESDAY 8 APRIL, 7:00 P.M.")

 (sp/action "The same room. The same table. The card is face up in the middle"
            "of it before anybody sits down and the lamination has gone soft"
            "along two edges now.")

 (sp/action "There is a box of tissues at Hale's elbow and it is a new box.")

 (sp/action "SIMON is fourth along on the near side, where Simon is.")

 (sp/beat "{pc} has known since the second week of February that this room is"
          "below the level of the quad.")

 (sp/action "He sits down in it anyway, because there is a mail with one"
            "button on it and he pressed the button.")

 (sp/dialogue "HALE"
              "Eleven. Five, five, and the eleventh. Standings.")

 (sp/dialogue "SIMON"
              "Simon. Second year, unplaced—")

 (sp/dialogue "HALE"
              "Thank you, Simon.")

 (sp/beat "Nobody laughs, and it is not because it has stopped being funny.")

 (sp/dialogue :pc
              ["First year, computer science. Provisional."
               "Systems, Mondays and Wednesdays. No service"
               "assignment. Review, no action."])

 (sp/dialogue "HALE"
              "And.")

 (sp/dialogue :pc
              "One.")

 (sp/beat "One comes back from the far side and it is the same voice as"
          "January and he still does not look to see who it is.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:20 P.M.")

 (sp/dialogue "A WOMAN" "to Simon"
              "Have you been assessed?")

 (sp/dialogue "SIMON"
              "Yes.")

 (sp/dialogue "A WOMAN"
              "Recently?")

 (sp/dialogue "HALE"
              "One each.")

 (sp/action "She stops.")

 (sp/beat "In August that exchange happened in this room in that order with"
          "those people, and the only thing that has changed is that this time"
          "nobody at the table looks up.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:45 P.M.")

 (sp/action "The table. Five minutes each and Hale says when.")

 (sp/action "WREN does not come to {pc} at all and does not send for him"
            "seemingly avoiding eye contact altogether.")

 (sp/action "They go to the fourth chair on the near side instead, which is"
            "not how it is done, and sit down opposite SIMON and stay there"
            "for eleven minutes, which is not how it is done either, and Hale"
            "says nothing about it.")

 (sp/beat "Nobody hears any of it.")

 (sp/action "Simon comes back and sits down and puts his hands flat on the"
            "table and looks at the card for a while.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 8:05 P.M.")

 (sp/action "There are four minutes at the end and they are for entering"
            "preferences privately and nothing said out loud.")

 (sp/action "{pc} enters nothing. It is accepted, as it is permitted.")

 (sp/action "It comes at about the second minute. The coin, and then the shut"
            "room, and he gets his hands under the edge of the table and holds"
            "on.")

 (sp/beat "It is worse than February and he does not go anywhere and nobody at"
          "that table sees it happen.")

 (sp/beat "It takes the whole of the four minutes.")

 (sp/dialogue "HALE"
              "That's the docket. Outcomes are notified.")

 (sp/transition "Cut to:")

 (sp/slug "INT. WHITLOCK HALL — THE STAIR — 8:15 P.M.")

 (sp/action "The stair up out of Room 4 has eleven steps in it and the air at"
            "the top of them is ordinary.")

 (sp/action "SIMON goes up it slowly, on the rail, and stops at the"
            "half-landing, and does not make anything of stopping.")

 (sp/dialogue :pc
              "Can I ask you a stupid question?")

 (sp/dialogue "SIMON"
              "Yeah.")

 (sp/dialogue :pc
              "How would somebody get off the docket?")

 (sp/beat "Simon laughs, which he has not done all evening.")

 (sp/dialogue "SIMON"
              ["Two ways. Everybody knows the two ways. You get"
               "attached, or you stop being in good standing."])

 (sp/dialogue :pc
              "Right.")

 (sp/dialogue "SIMON"
              "I've thought about it.")

 (sp/beat "Four seconds.")

 (sp/dialogue :pc
              "Which one?")

 (sp/dialogue "SIMON"
              "Both.")

 (sp/action "He says it the way you would say you had thought about getting"
            "the earlier bus, and then he goes up the rest of the stair.")

 (sp/beat "{pc} has been standing on that half-landing for about nine months"
          "working out how to say a thing that has no sentence in it, and"
          "there is one available and he has just heard it, and it is stop"
          "being in good standing, and it is the same as saying be"
          "repossessed.")

 (sp/beat "He does not say it.")

 (sp/action "At the top of the stair Simon holds the door, because Simon holds"
            "doors.")

 (sp/dialogue "SIMON"
              "What did you want to ask?")

 (sp/dialogue :pc
              "That was it.")

 (sp/dialogue "SIMON"
              "Right.")

 (sp/transition "Blackout.")

;; ---

 (art/email
  {:from "Office of Student Life <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "9 April, 4:00 AM"
   :subject "Docket — outcome"}
  "Attendance recorded."
  "No preference was entered. No outcome is indicated on this occasion."
  "You remain on the docket. Your next assignment will be notified."
  "Please arrive presentable.")

 (sp/action "Nothing in it mentions the four minutes, or the stair, or the"
            "eleven minutes at the fourth chair on the near side, and nobody"
            "in that room will ever know what was said there, including him."))