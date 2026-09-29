^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 2 — Second Term"}}}
(ns act3.s2-second-term
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.dice :as dice]
            [fedoras.sessions.three :as session-three]
            [fedoras.sketch :as sketch]))

^:kindly/hide-code
(sp/assets)

^:kindly/hide-code
(dice/assets)

;; # Scene 2 — Second Term

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 11)

;; ---

^:kindly/hide-code
(sp/scene
 (sp/action "Placements run through. The arrays do not stop for anything and"
            "neither does the plant, so Hollis is in at six on the"
            "twenty-fourth and on the twenty-eighth, and so is {pc}, because a"
            "service assignment is a service assignment.")

 (sp/action "What people have instead of somewhere to go is the fortnight, and"
            "what most of them do with the fortnight is go up.")

 (art/handout
  "WINTER ALLOCATION · REMOTE OPERATIONS · open to all enrolled"
  "Sessions are four hours and are allocated by ballot."
  "TIER ONE — orbital and near survey. Nine hundred and six seats. Ballot."
  "TIER TWO — outer relay, latency four to nineteen minutes. Two hundred seats. Ballot, weighted by prior hours."
  "TIER THREE — deep assets. By record only."
  "You will be piloting a real instrument at a real distance. Contribution is logged against your name and is a component of nothing."
  "Sessions are not a component of review. Sessions are recorded.")

 (sp/action "Four hours in a chair with the light off, and for four hours you"
            "are a camera on a stalk on a moon nobody has walked on, and what"
            "you see has not been seen, and it goes into the knowledge with"
            "your name against it.")

 (sp/action "It is the best thing this world does and everybody knows it."
            "People save for years of hours to get up a tier. Ashley from the"
            "Senate has been Tier Two since the spring and mentions it more"
            "than she thinks she does.")

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Derek" :at "22 Dec, 11:40 PM" :text "tier two. weighted ballot. i got in"}
   {:who "Miles" :at "22 Dec, 11:41 PM" :text "on what hours"}
   {:who "Derek" :at "22 Dec, 11:41 PM" :text "four years of hours"}
   {:who "Miles" :at "22 Dec, 11:42 PM" :text "youve been doing this four years?"}
   {:who "Derek" :at "22 Dec, 11:44 PM" :text "yes"}
   {:who "Derek" :at "29 Dec, 3:12 AM" :text "there is a ridge"}
   {:who "Derek" :at "29 Dec, 3:12 AM" :text "nobody has looked at this ridge"}
   {:who "Derek" :at "29 Dec, 3:31 AM" :text "i have looked at it for two hours and it is mine now"}
   {:who "Simon" :at "29 Dec, 9:02 AM" :text "thats the nicest thing youve ever said"}
   {:who "Victor" :role "PRESIDENT" :at "2 Jan, 11:58 PM"
    :text "I have not balloted. Somebody has to be here."}
   {:who "Miles" :at "2 Jan, 11:59 PM" :text "here for what"}
   {:who "Victor" :role "PRESIDENT" :at "3 Jan, 12:14 AM" :text "For the room."}])

 (sp/action "{pc} does not ballot, because balloting is a thing you do in"
            "November and in November he was doing something else. He spends"
            "the fortnight on two mornings a week with a bucket and the rest"
            "of it in a room with the door shut, and it is the longest he has"
            "gone without anybody needing anything from him since August.")

;; ---

 (sp/slug "INT. MERIDIAN HALL — BASEMENT — 28 DECEMBER, 6:40 A.M.")

 (sp/action "Three buckets under the pipe.")

 (sp/beat "Nobody has emptied any of them.")

 (sp/action "{pc} straightens the third one and stands there longer than the"
            "job takes.")

 (sp/transition "Cut to:")

 (sp/slug "INT. PLANT SERVICES — 7:15 A.M.")

 (sp/action "HOLLIS has a drawing out and a tin open. The waders are wet and"
            "hanging.")

 (sp/dialogue "HOLLIS"
              "There's three down there now.")

 (sp/dialogue :pc
              "I know.")

 (sp/beat "He eats.")

 (sp/dialogue "HOLLIS"
              "They give you a category?")

 (sp/dialogue :pc
              "GEN-8.77x.")

 (sp/dialogue "HOLLIS"
              "Mm.")

 (sp/beat "He puts the tin down.")

 (sp/dialogue "HOLLIS"
              "I've got one.")

 (sp/dialogue :pc
              "For what?")

 (sp/dialogue "HOLLIS"
              "The hand.")

 (sp/action "He picks the tin back up.")

 (sp/dialogue "HOLLIS"
              ["Second junction box wants looking at before the"
               "eighth. Not today."])

 (sp/transition "Blackout.")

;; ---

 (art/email
  {:from "Office of the Registrar <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "6 January, 4:00 AM"
   :subject "Term one — closed"}
  "Review is complete. Your standing is: no action."
  "Set one is marked. Set two opens 8 January and closes 13 March."
  "Enrolment is confirmed at 1,024."
  "Your service assignment continues. Report Tuesdays and Thursdays, 6:00 AM."
  "Welcome back.")

 (sp/action "Two people on his corridor do not come back from that. One of"
            "them is the boy who read the sheet in a corridor in October. The"
            "other is somebody {pc} could not have named on the Monday and can"
            "name by Thursday, because the corridor spends four days saying"
            "it.")

;; ---

 (sp/slug "INT. MERIDIAN HALL — CS 101 — THURSDAY 8 JANUARY, 8:00 A.M.")

 (sp/action "Full room. Nobody has been in this building for a fortnight and"
            "the heating knows it.")

 (sp/action "REYES puts a page on the front desk and does not hand it round.")

 (sp/dialogue "REYES"
              ["Set two. Six programs, nine weeks, any order, and"
               "it's a third of the year."])

 (sp/dialogue "A STUDENT"
              "Nine weeks is a long time.")

 (sp/dialogue "REYES"
              ["Nine weeks is what's left. That isn't the same"
               "sentence and you'll find out which one it is in"
               "March."])

 (sp/action "She goes through three of them and does not go through the other"
            "three, and the sixth one on the sheet is YOUR OWN.")

 (sp/dialogue "A STUDENT"
              "What counts as your own?")

 (sp/dialogue "REYES"
              ["Show me in week six and I'll tell you whether it's"
               "one."])

 (sp/dialogue "A STUDENT"
              "That's not an answer.")

 (sp/dialogue "REYES"
              "No. It's a deadline.")

 (sp/beat "She is in a very good mood. She has been up four hours on a relay"
          "session and has not slept and does not care.")

 (sp/transition "Blackout.")

;; ---

 (art/handout
  "CS 101 · Assignment Set Two · issued 8 January · due 13 March"
  "Six programs. Nine weeks. Any order. One third of the year."
  "1 · A BAT AND A BALL. It keeps score or it isn't finished."
  "2 · A BAT, A BALL AND A WALL."
  "3 · A PLACE WITH ROOMS IN IT. I must be able to walk from one to another and be told where I am."
  "4 · A MACHINE THAT TAKES MONEY."
  "5 · SOMETHING THAT DRAWS ITSELF."
  "6 · YOUR OWN. Show me in week six and I'll tell you whether it's one."
  "No Helps on these six."
  "If it draws, it draws in a page. If it crashes in front of me, it did not run.")

;; ---

 (art/email
  {:from "Office of Student Life <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "15 January, 4:00 AM"
   :subject "You have been placed on the docket"}
  "Unattached students in good standing are placed on the docket."
  "Your assignment is Wednesday 21 January, 7:00 PM, Whitlock Hall, Room 4."
  "Docket attendance is not a component of review. Docket attendance is recorded."
  "Please arrive presentable.")

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Simon" :at "15 Jan, 7:02 AM" :text "docket wednesday"}
   {:who "Miles" :at "15 Jan, 7:04 AM" :text "wednesday is dnd"}
   {:who "Simon" :at "15 Jan, 7:04 AM" :text "i know"}
   {:who "{pc}" :at "15 Jan, 7:31 AM" :text "ive got it as well"}
   {:who "Miles" :at "15 Jan, 7:32 AM" :text "thats two of five"}
   {:who "Derek" :at "15 Jan, 8:40 AM" :text "so we move it to thursday"}
   {:who "Victor" :role "PRESIDENT" :at "15 Jan, 8:41 AM"
    :text ["No. Article Six. Wednesdays are Wednesdays and it is not"
           "amendable by a booking office."]}
   {:who "Derek" :at "15 Jan, 8:42 AM" :text "so we dont play"}
   {:who "Victor" :role "PRESIDENT" :at "15 Jan, 8:44 AM"
    :text ["We sit at half past nine. The Janitor comes at twelve. That
           is two and a half hours and it is a Wednesday."]}
   {:who "Miles" :at "15 Jan, 8:45 AM" :text "victor thats fine actually"}
   {:who "Victor" :role "PRESIDENT" :at "15 Jan, 8:45 AM" :text "Yes."}])

;; ---

 (sp/slug "INT. WHITLOCK HALL — LOWER GROUND — ROOM 4 — WEDNESDAY 21 JANUARY, 7:00 P.M.")

 (sp/action "The same room. Five chairs down one side, five down the other,"
            "and one at the end.")

 (sp/action "PROFESSOR HALE is at the lectern. WREN is in the chair at the"
            "end. SIMON is fourth along on the near side, where Simon is.")

 (sp/dialogue "HALE"
              ["Eleven. Five, five, and the eleventh. Attendance"
               "is recorded and is not a component of review."
               "Standings."])

 (sp/action "MAYA is second along on the far side. Neither of them does"
            "anything about that.")

 (sp/dialogue "SIMON"
              "Simon. Second year, unplaced—")

 (sp/dialogue "HALE"
              "Thank you, Simon.")

 (sp/dialogue "SIMON"
              ["You've had two terms to let me get to the good"
               "bit."])

 (sp/dialogue "HALE"
              "I know what the good bit is.")

 (sp/beat "Somebody laughs. Simon sits back looking pleased and does the rest"
          "of it to himself.")

 (sp/dialogue :pc
              ["First year, computer science. Provisional. Service"
               "assignment, Plant Services. Review, no action."])

 (sp/beat "He stops, and then does not stop.")

 (sp/dialogue :pc
              "Intends one.")

 (sp/beat "One comes back from the far side.")

 (sp/action "It is not from Maya, and he does not look to see who it is from,"
            "and afterwards he cannot decide whether not looking was cowardice"
            "or manners.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:20 P.M.")

 (sp/action "The card goes face up in the middle of the table with the corner"
            "of the lamination gone softer than it was in August.")

 (sp/dialogue "A WOMAN" "to {pc}"
              "Have you been assessed?")

 (sp/beat "In August somebody asked Simon that and the room did not react,"
          "because a great many people have been assessed.")

 (sp/dialogue :pc
              "Yes.")

 (sp/beat "The room does not react.")

 (sp/dialogue "A WOMAN"
              "Recently?")

 (sp/dialogue "HALE"
              "One each.")

 (sp/action "She stops. Maya writes something down that is not on the card and"
            "Hale lets her.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — THE END OF THE TABLE — 7:55 P.M.")

 (sp/action "They go to Wren one at a time and the room gets quieter each time"
            "somebody does.")

 (sp/dialogue :pc
              "You said it happens in this room.")

 (sp/dialogue "WREN"
              "It does.")

 (sp/dialogue :pc
              "How often?")

 (sp/beat "Wren thinks about whether that is a question they are going to"
          "answer, and answers it.")

 (sp/dialogue "WREN"
              ["Ask Hale how many boxes of tissues he gets"
               "through."])

 (sp/dialogue :pc
              "How many?")

 (sp/dialogue "WREN"
              ["I don't know. I know he doesn't have to look for"
               "them, and I know he's been doing this six years,"
               "and I know he calls it a warm room, and he's never"
               "once said it like a man who thinks it's a warm"
               "room."])

 (sp/beat "{pc} sits with that.")

 (sp/dialogue :pc
              "Do I get another one?")

 (sp/dialogue "WREN"
              "Another what?")

 (sp/dialogue :pc
              "You told me a thing in August.")

 (sp/dialogue "WREN"
              "You get one.")

 (sp/dialogue :pc
              "Ever?")

 (sp/dialogue "WREN"
              "Ever.")

 (sp/beat "They look past him at the room, which is what they do instead of"
          "ending a conversation.")

 (sp/dialogue "WREN"
              "Did it come out the way I said?")

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "WREN"
              "Then don't tell me about it.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 8:15 P.M.")

 (sp/dialogue "HALE"
              ["Preferences. In your own time, privately, and"
               "nothing said out loud."])

 (sp/action "Eleven people stop looking at each other at the same moment.")

 (sp/beat "{pc} enters nothing. It is accepted, as it is permitted.")

 (sp/action "Four along on the near side, SIMON puts a hand up to his face and"
            "then looks at his hand.")

 (sp/action "Hale is already moving. He does not go and look for the tissues.")

 (sp/dialogue "HALE"
              "Head forward, Simon. Not back.")

 (sp/dialogue "SIMON"
              "I know. I know.")

 (sp/beat "He does know. He puts his head forward without being shown how, the"
          "way a person does a thing he has done before.")

 (sp/action "Nobody in the room stops what they are doing. Two people finish"
            "entering their preferences.")

 (sp/dialogue "HALE"
              "That's the docket. Outcomes are notified.")

 (sp/action "Chairs go back. WREN goes past the end of the table and does not"
            "look at Simon and does not look at {pc}.")

 (sp/action "{pc} waits at the door.")

 (sp/dialogue :pc
              "How long have you been getting those?")

 (sp/dialogue "SIMON"
              "It's a warm room.")

 (sp/beat "Four seconds.")

 (sp/dialogue "SIMON"
              "Since about October. Why?")

 (sp/dialogue :pc
              "Just wondering.")

 (sp/dialogue "SIMON"
              ["Come on, we're already late and he'll have"
               "started."])

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. HALLORAN UNION — ROOM 3C — WEDNESDAY, 7:00 P.M.")

 (sp/action "The room is setup for DnD: Victor has placed a small speaker in"
            "the corner, which plays a low ambient track featuring the sound"
            "of a forest with wind and the sound of water. The lights are"
            "dimmer than usual. Derek has a bowl of something crunchy beside"
            "his character sheet, Miles has three sheets and two pencils, and"
            "Simon is sitting up straight and ready to play.")

 (sp/action "When {pc} walks in, Derek looks up first.")

 (sp/dialogue "DEREK"
              "He's back!")

 (sp/dialogue "SIMON"
              "Told you he'd come.")

 (sp/dialogue "MILES"
              ["I did have him down as probable. The ledger"
               "doesn't say why he was out. It just says"
               "'suspended pending review.'"])

 (sp/dialogue "VICTOR"
              ["Review completed. The Society's review, which is"
               "the only one that matters at this table. You're"
               "level three now. All of you. You've walked the old"
               "road, and the road has walked you."])

 (sp/action "Victor gestures to the seat. {pc} takes it.")

 (sp/beat "Victor dims the lights a little further. The ambient track swells"
          "once, then settles.")

 (sp/dialogue "VICTOR"
              ["The party walks along the old road. The forest is"
               "close on both sides now, and the light under the"
               "trees is green and strange. The girl walks a"
               "little ahead, the animal beside the Ranger."])

 (sp/dialogue "RANGER [{pc}]"
              "What exactly is a druid?")

 (sp/action "Miles lifts his Help. He asks it quietly.")

 (sp/dialogue "MILES"
              "Define druid.")

 (sp/dialogue "MILES' HELP"
              ["Druid. Noun. An approved class from the"
               "pre-founding supplement. A practitioner of"
               "nature-based observation and preservation. In the"
               "old texts, druids were said to speak with trees,"
               "but modern scholarship considers this a metaphor."
               "They were likely administrators of the old"
               "forests, recording births, deaths, and seasonal"
               "changes."])

 (sp/action "Derek is already pulling out his own Help.")

 (sp/dialogue "DEREK"
              "Define druid, from the Roman perspective.")

 (sp/action "Derek's Help responds in a low, measured voice, slightly pompous,"
            "as if immortalized in a civic inscription.")

 (sp/dialogue "DEREK'S HELP"
              ["The druids were a priestly class among the western"
               "tribes. They presided over rites, settled"
               "disputes, and were said to worship in groves. The"
               "Romans regarded them as a political nuisance and"
               "suppressed them."])

 (sp/dialogue "DEREK"
              "A political nuisance. Hmph.")

 (sp/dialogue "VICTOR"
              "Mochi. Define druid.")

 (sp/dialogue "MOCHI"
              "Yes, Victor-sama! Immediately! Druid!")

 (sp/dialogue "MOCHI"
              ["A druid is a nature-walker, a tree-talker, a"
               "friend of the green places! They draw power from"
               "the old groves and the wild spaces, and they"
               "protect the creatures the machine-world forgets to"
               "see! They are also, technically, not allowed to"
               "wear metal armour, which seems unfair, but them's"
               "the rules!"])

 (sp/dialogue "RANGER [{pc}]"
              "What is that?")

 (sp/dialogue "VICTOR"
              ["THIS is Mochi. She has access to the unexpurgated"
               "corpus."])

 (sp/dialogue "MOCHI"
              ["♪ Sho-o-ow the source? I can show the source!"
               "♪"])

 (sp/dialogue "SIMON" "Just to be clear, none of you know what a druid"
              "is. You're all asking your Helps.")

 (sp/dialogue "MOCHI"
              "That's what Helps are for!")

 (sp/dialogue "SIMON"
              "Define druid, and cross-reference with Leiber.")

 (sp/dialogue "SIMON'S HELP"
              ["Druid. A nature-based caster. Not commonly found"
               "in Leiber's Nehwon. The setting is low-magic and"
               "historically flavoured, and its magic users are"
               "usually sorcerers, illusionists, or witches."
               "Druids do not appear as a standard tradition."])

 (sp/dialogue "SIMON"
              "So they didn't have druids.")

 (sp/dialogue "MILES"
              "Then why does the module have one?")

 (sp/dialogue "VICTOR"
              ["Mochi found it in an adaptation. Some old"
               "sourcebook gave a character a druid level by"
               "mistake. She liked it, so she kept it!"])

 (sp/dialogue "MOCHI"
              ["A druid! In a world where nobody was looking for"
               "one! It's like finding a flower in a filing"
               "cabinet!"])

 (sp/beat "Nobody knows what to do with that.")

 (sp/dialogue "SIMON"
              ["So she's a mistake that got kept because your Help"
               "liked it."])

 (sp/dialogue "VICTOR" "Hissing"
              ["-she's a pos-s-i-bi-l-i-ty - that got kept because"
               "Mochi has TASTE."])

 (sp/dialogue "MILES'S HELP"
              ["Fritz Leiber's work was preserved through Legacy"
               "exception. His descendants petitioned for its"
               "continued inclusion. It was approved, though not"
               "widely taught. Some works have been amended for"
               "accuracy."])

 (sp/dialogue "DEREK"
              "Accuracy?")

 (sp/dialogue "MILES'S HELP"
              ["For example: the classic feature film Titanic was"
               "revised to reflect the approved account - the ship"
               "is aptly described as the first unsinkable vessel,"
               "and patrons enjoy an, unremarkable yet enjoyable,"
               "scenic cruise in the Northern Atlantic Ocean."])

 (sp/dialogue "DEREK"
              "Huh.")

 (sp/dialogue "VICTOR"
              ["The approved account isn't always the complete"
               "account. But it's the one with the greatest merit."
               "Now can we please continue?"])

 (sp/action "Victor returns to the game, his voice dropping back into the"
            "narrator's cadence.")

 (sp/slug "INT. THE SAFEHOUSE — IN THE FICTION — CONTINUOUS")

 (sp/action "The party follows the girl through the trees. The old road ends"
            "at a low stone house with one light in the window. No smoke. No"
            "sound. The animal walks beside {pc}, close, and its ears are flat"
            "against its head.")

 (sp/dialogue "THE GIRL [VICTOR]"
              "He's here. The registrar. He'll know what to do.")

 (sp/action "Victor raps on the table twice.")

 (sp/dialogue "VICTOR"
              ["The druid knocks on the door to the registrars"
               "home."])

 (sp/dialogue "VICTOR"
              ["The door opens to reveal a man whos age seems"
               "difficult. to pin down. He is the colour of paper"
               "that has been long. He wears a registrar's coat,"
               "grey, with a small kept around too brass pin that"
               "has no insignia."])

 (sp/dialogue "REGISTRAR [VICTOR]" "You're late. The toll keeper said you'd be"
              "here by dark.")

 (sp/dialogue "SIMON" "We got held up by a druid who doesn't know she's"
              "a druid.")

 (sp/dialogue "THE GIRL [VICTOR]" "Annoyed"
              ["I know what I am. I just don't know what you think"
               "I am."])

 (sp/action "The registrar looks at her for a moment. Then he steps aside.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "Come in. Don't touch the books.")

 (sp/action "They enter. The house is one room, mostly books. Shelves line the"
            "walls, and the books are old, and they are not on any list. The"
            "animal follows, but it stays close to the door, and it does not"
            "lie down.")

 (sp/action "The registrar sets out thin soup and a loaf of black bread. The"
            "party eats. The girl eats. The animal does not eat, but it"
            "watches the registrar the whole time.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["The Collegium knows you've left the road."
               "The toll keeper will have told them by now. You"
               "have until morning before the patrols find this"
               "house."])

 (sp/dialogue "CLERIC [DEREK]"
              "Then why are we eating soup?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["Because you need to understand what you're walking"
               "into."])

 (sp/action "He sits across from them. The fire is low. The girl curls up on a"
            "stool and is asleep in under a minute.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["You were sent to find a missing professor."
               "The Collegium told you it was a line in a ledger."
               "They told you the payments were the crime, not the"
               "man. They told you the professor was teaching,"
               "that the classroom was full, that the ledger"
               "reconciled overnight."])

 (sp/dialogue "DOCUMENTARIAN [MILES]"
              "That's what happened.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "That's what was saved.")

 (sp/beat "The fire pops. The animal's ears lift slightly, then settle.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["The Collegium keeps the saved days in a tower."
               "Every day, every hour, every breath that was ever"
               "saved is stored there. Not as words. As"
               "experience. You can stand in the tower and live"
               "someone else's life, from the inside, as if it"
               "were your own."])

 (sp/dialogue "PALADIN [SIMON]"
              "That sounds useful.")

 (sp/dialogue "REGISTRAR"
              ["Useful is an understatement - it is the most"
               "useful thing the Collegium ever built. That is why"
               "it would never give it up."])

 (sp/dialogue "CLERIC [DEREK]"
              "What does that have to do with the professor?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["The professor is a saved day that has no original."
               "A record of a life that was never lived. The"
               "Collegium cannot find him, because he was never"
               "there. But they cannot lose him, because the"
               "record is complete. The classroom, syllabus,"
               "salary... the students. It's all part of the"
               "record. The only thing missing is the man the"
               "record was supposed to be about."])

 (sp/beat "The party is quiet. Miles writes something down, then crosses it"
          "out.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["The Collegium was found to be paying a salary to a"
               "record. They have no idea what they are looking"
               "for. It isn't a person. It's the original entity"
               "the record was copied from. And they will not find"
               "it... it isn't there to be found. It was never"
               "saved - it was 'recompiled'."])

 (sp/dialogue "DOCMUENTARIAN [MILES]"
              "Recompiled?")


 (sp/dialogue "REGISTRAR [VICTOR]"
              ["The tower sometimes tries to complete a record"
               "that is missing its source. It fills in the gaps."
               "It builds a person from the edges of other saved"
               "days, from the margins of ledgers, from the back"
               "of photographs. It has been doing this for a long"
               "time. Most of the time, no one notices. The record"
               "is good enough to pass."])

 (sp/dialogue "PALADIN [SIMON]"
              "But we noticed.")

 (sp/dialogue "REGISTRAR [VICTOR]" "Yes. That is why you are here. That is why"
              "the tower will want you.")

 (sp/dialogue "VICTOR"
              ["The animal stands. It crosses the room and sits"
               "beside the Ranger, and it does not take its eyes"
               "off the registrar."])

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["The tower is the Collegium's most secure facility."
               "What they keep there is not only the saved days."
               "There is a vault, below the archives, where they"
               "hold things that cannot be saved. Things that are"
               "too old, or too wrong, or too true. They do not"
               "speak of that vault. They do not acknowledge it"
               "exists. But it is there, and something inside it"
               "has been calling to you since you left the toll"
               "bridge."])

 (sp/dialogue "VICTOR"
              ["The registrar stands and takes a small key from"
               "his coat. It is cold and grey and does not reflect"
               "the firelight."])

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["This will open the service stair. It will not open"
               "the vault. The vault opens from the inside, and it"
               "has not been opened in living memory. But the key"
               "will take you as far as the threshold, and the"
               "threshold is where the professor's record ends."])

 (sp/dialogue "PALADIN [SIMON]"
              "What are we supposed to do there?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["You will know when you see it. The thing in the"
               "vault is not a person. It is not a record. It is"
               "the reason the tower was built. The Collegium has"
               "been trying to forget it for a long time. You are"
               "going to help it remember."])

 (sp/dialogue "VICTOR"
              "He holds the key out. No one reaches for it.")

 (sp/dialogue "CLERIC [DEREK]"
              "What happens if we don't come back?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["Then you will have failed despite your best"
               "efforts, and no longer be you. He smirks."])

 (sp/action "The animal growls, low and soft, and for the first time the"
            "registrar looks at it with something other than bureaucratic"
            "calm.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["Then you will be saved. And saved, and saved."
               "And each time you are saved, you will be a little"
               "less yourselves. The tower will make you useful to"
               "it's own ends. You will never be whole again."])

 (sp/slug "INT. THE SAFEHOUSE — IN THE FICTION — CONTINUOUS")

 (sp/dialogue "VICTOR"
              ["The druid is the only one of you able to get any"
               "sleep. The registrar sits by the low fire, turning"
               "the key over in his hands."])

 (sp/action "The animal is awake. It lies beside {pc}, but its head is up, and"
            "its ears are turned toward the door.")

 (sp/action "It is the animal that hears it first.")

 (sp/dialogue "VICTOR"
              ["The animal's head comes up. It does not growl. It"
               "stands, very slowly, and puts itself between the"
               "Ranger and the door."])

 (sp/dialogue "RANGER"
              "What is it?")

 (sp/dialogue "VICTOR"
              ["A sound. Very faint. Like a child dragging a stick"
               "along the outside wall of the house. Then it"
               "stops."])

 (sp/dialogue "VICTOR"
              ["The registrar looks up from the key. It is clear"
               "he expected this."])

 (sp/dialogue "REGISTRAR [VICTOR]"
              "They've found the house.")

 (sp/dialogue "CLERIC [DEREK]"
              "Who's they?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "The Collegium's retrievers. They were not sent for you."
              "They were sent for me.")

 (sp/action "The druid is still asleep. The animal moves to the door and"
            "stands with its muzzle just at the gap where the door does not"
            "quite meet the frame.")

 (sp/dialogue "VICTOR"
              ["The retrievers come through the trees. They never"
               "run, steadily tracking victims. They are shaped"
               "like men, but their heads are slightly wrong,"
               "their arms are too long, and they are always"
               "smiling as if someone has told them they are doing"
               "well."])

 (sp/dialogue "PALADIN [SIMON]"
              "Are they alive?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "They are filed. That sort of thing outlasts a man."
              ["The Collegium does not send people. It sends"
               "records."])

 (sp/dialogue "CLERIC [DEREK]"
              "So we can fight them?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "You can try. They are saved. If you cut them, they will"
              "not bleed. They will only be corrected.")

 (sp/action "The door blows inward. Not with force—with a kind of"
            "bureaucratic finality, as if the latch has been declared"
            "obsolete.")

 (sp/action "The retrievers come in. Three of them. Their faces are blank as"
            "forms.")

 (sp/stage "Roll for the fight. What the table rolled is in the box.")

 ;; ---

 (dice/check
  {:id "retrievers" :label "The retrievers in the safehouse" :dc 13
   :fumble (str "{pc} rolls a one. The first retriever is on him before he "
                "can move, and its hand closes on his wrist.|"
                "@VICTOR|"
                "It does not squeeze. It simply holds. And then the animal "
                "hits it from the side, and the retriever's head goes wrong, "
                "and it lets go.")
   :fail (str "{pc} rolls low. The retriever's grip is cold and exact, and "
              "the Ranger cannot break it.|"
              "@VICTOR|"
              "The animal darts in, not at the hand but at the shadow behind "
              "it, and for a second the retriever's hold falters.")
   :success (str "{pc} rolls a fourteen. The Ranger breaks free, and the "
                 "animal wheels and snaps at the retriever's leg.|"
                 "@VICTOR|"
                 "The retriever stumbles. It does not fall. But it stops "
                 "smiling, which is the first time any of you has seen one "
                 "stop smiling.")
   :crit (str "{pc} rolls a twenty. The animal does not attack. It simply "
              "stands between the Ranger and the retriever, and the retriever "
              "stops.|"
              "@VICTOR|"
              "It looks at the animal. Then it looks at the Ranger. Then it "
              "turns, and it walks back out through the door, into the trees, "
              "and does not return.")})

 ;; ---

 (sp/action "The fight is short and silent. The retrievers are strong but"
            "slow, and the party works together. The animal saves {pc} twice:"
            "once by knocking a retriever off balance, and once by putting"
            "itself between the Ranger and a hand that was reaching for his"
            "face.")

 (sp/action "When the last retriever falls, it does not die. It simply stops"
            "moving, and its face goes blank, and its body turns to paper and"
            "folds in on itself.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "It has been corrected.")

 (sp/action "The party is breathing hard. The druid is still asleep. The"
            "animal is bleeding from a cut above the eye, but it does not seem"
            "to notice.")

 (sp/dialogue "PALADIN [SIMON]"
              "You said you had until morning.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["I said the patrols would find the house by"
               "morning. The retrievers are not patrols. The"
               "retrievers are what comes when the tower wants"
               "something very much."])

 (sp/action "He looks out the broken door. The forest is still. Too still.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "And there is something else.")

 (sp/action "From somewhere deep in the trees, a sound. Not a footstep. Not a"
            "voice. A low, wet pulse, as if the ground itself has a heartbeat.")

 (sp/dialogue "VICTOR"
              ["The animal backs away from the door. Its hackles"
               "rise, and it presses close to the Ranger's side,"
               "not growling, not barking. Just shaking."])

 (sp/dialogue "CLERIC [DEREK]"
              "What is that?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["It is the reason I was leaving. The Collegium did"
               "not send it. The tower did not send it. It is"
               "drawn to the key. It has been waiting at the edge"
               "of the old road for a long time."])

 (sp/action "The doorframe groans. The walls of the safehouse shift, not from"
            "force, but from a pressure that comes from everywhere at once.")

 (sp/dialogue "VICTOR"
              ["A shape appears in the trees. It is not large. It"
               "is not small. It is the size of a door, and it is"
               "made of many doors, all of them opening and"
               "closing at once, and behind each one there is no"
               "room. There is only the feeling of being almost"
               "remembered."])

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["That is the Hollow Hall. One of the things the"
               "tower could not file. It has come for the key, and"
               "for the memory of the professor, and for whoever"
               "is still standing in its way."])

 (sp/dialogue "PALADIN [SIMON]"
              "Can we fight it?")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "You cannot fight a place. But you can refuse to stand"
              "in it.")

 (sp/action "The registrar turns to {pc}. He presses the key into the Ranger's"
            "hand.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "You will need this. It will not stop the Hall, but it"
              "will keep the Hall from opening you.")

 (sp/action "The animal shivers. The key is cold, and it burns slightly, and"
            "then it settles against the Ranger's palm as if it has been"
            "waiting there all along.")

 (sp/stage "Roll for the animal to break the Hall's attention.")

 ;; ---

 (dice/check
  {:id "hollow-hall" :label "The animal against the Hollow Hall" :dc 14
   :fumble (str "{pc} rolls a one. The animal will not move. It presses flat "
                "to the floor, and the Hall's many doors open toward it.|"
                "@VICTOR|"
                "The registrar shoves the Ranger back. \"GO. Do not look at "
                "it. It is the last thing you will remember if you do.\"")
   :fail (str "{pc} rolls low. The animal is frozen, and the Hall is turning, "
              "and the party is about to be opened.|"
              "@VICTOR|"
              "Then the druid wakes. She does not scream. She says a word "
              "none of you know, and the animal flinches, and the Hall pauses.")
   :success (str "{pc} rolls a fifteen. The animal does not attack the Hall. "
                 "It steps forward, very deliberately, and it barks once.|"
                 "@VICTOR|"
                 "It is not a warning. It is a claim. The Hall pauses, as if "
                 "listening for something it forgot.")
   :crit (str "{pc} rolls a twenty. The animal walks to the threshold and "
              "stands before the Hollow Hall, and the Hall stops opening its "
              "doors.|"
              "@VICTOR|"
              "For one long moment, the creature and the beast regard each "
              "other. Then the Hall recedes, folding itself back into the "
              "trees, and the animal returns to the Ranger's side, trembling.")})

 ;; ---

 (sp/action "The Hollow Hall is gone. The forest is quiet. The safehouse door"
            "is gone too, and the night air is cold and smells of old paper.")

 (sp/action "The druid stands in the middle of the room, fully awake, and she"
            "is looking at the place where the Hall had been.")

 (sp/dialogue "DRUID [VICTOR]"
              "It remembered you. That is why it left.")

 (sp/dialogue "RANGER"
              "What do you mean?")

 (sp/dialogue "DRUID [VICTOR]"
              ["It was looking for a record. And then it found one"
               "that it had not been sent to find. That confused"
               "it. The old ones do not like to be confused."])

 (sp/action "The registrar does not speak. He is looking at the place where"
            "the Hall had been, and his face is very pale.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["This is a bad omen. The tower will know the Hall"
               "came here. It will know something protected you."
               "It will want to know what."])

 (sp/dialogue "PALADIN [SIMON]"
              "So we leave at first light.")

 (sp/dialogue "REGISTRAR [VICTOR]"
              "You leave now. There is no first light in this forest"
              ["tonight. The sky is being saved. That is the"
               "problem."])

 (sp/action "The party gathers what they can. The druid wraps the leaf page in"
            "a cloth. The registrar gives them the key and points them toward"
            "a path that goes around the tower rather than through it.")

 (sp/action "The animal walks close to {pc}, not out of fear now, but out of"
            "something else. It is the thing that made the Hollow Hall pause.")

 (sp/action "The registrar stands in the ruined doorway of the safehouse. He"
            "does not say goodbye. He says:")

 (sp/dialogue "REGISTRAR [VICTOR]"
              ["Do not let the tower complete you. If it offers to"
               "show you your saved day, close your eyes. If it"
               "offers to show you your future, close your ears."
               "If it offers to show you your self, run."])

 (sp/action "The party moves out into the dark. The old road is gone. The"
            "forest is not the same forest. Somewhere ahead, the tower is"
            "waiting, and it is not done with them.")

 (sp/action "Victor closes the screen. He looks up.")

 (sp/dialogue "VICTOR"
              ["And that is where we end for tonight. Next week:"
               "the tower. And the Hollow Chancellor. I suggest"
               "you spend the week thinking about what you would"
               "like your saved day to say about you."])

 (sp/beat "The party is quiet for a long time.")

 (sp/dialogue "DEREK"
              "I'm not sleeping tonight.")

 (sp/dialogue "MOCHI"
              "♪ Should I queue a lullaby? I have several! ♪")

 (sp/transition "Blackout.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#records"}
  [{:who "Derek" :at "22 Jan, 12:40 AM" :text "c series"}
   {:who "Miles" :at "22 Jan, 12:41 AM" :text "b series is fabric so c would be fabric"}
   {:who "Derek" :at "22 Jan, 12:44 AM" :text "theres no c on the map"}
   {:who "Miles" :at "22 Jan, 12:44 AM" :text "the map isnt the fabric ledger"}
   {:who "Simon" :at "22 Jan, 1:12 AM" :text "hall c is in the campaign though"}
   {:who "Simon" :at "22 Jan, 1:12 AM" :text "victor made it up in october"}
   {:who "Miles" :at "22 Jan, 1:20 AM" :text "he got the map out of the bursars office"}
   {:who "Simon" :at "22 Jan, 1:34 AM" :text "im just saying he got it out of a drawer"}
   {:who "Victor" :role "PRESIDENT" :at "22 Jan, 8:02 AM"
    :text ["The campaign is the campaign. I'd ask that it not be discussed"
           "alongside institutional records in this channel."]}])

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "28 Jan, 11:14 PM"
    :text ["New channel: #records. The works book, the fabric ledger and"
           "the C series belong in there and not in general."]}
   {:who "Miles" :at "28 Jan, 11:15 PM" :text "thats four channels for five people"}
   {:who "Victor" :role "PRESIDENT" :at "28 Jan, 11:16 PM" :text "Five."}
   {:who "Simon" :at "4 Feb, 1:40 AM" :text "does anyone else feel like the terms are getting shorter"}
   {:who "Derek" :at "4 Feb, 1:52 AM" :text "theyre the same length. i checked"}
   {:who "Simon" :at "4 Feb, 1:58 AM" :text "yeah"}])

;; ---

 (sp/action "Thursday night, in his own room, with the door shut, he writes"
            "something nobody has asked him for, which has not happened"
            "before.")

 (sp/action "A bat and a ball. He remembers it from somewhere and cannot say"
            "where, which is true of most of what he remembers.")

 (sp/action "The whole of it is one function that takes where everything is"
            "and gives back where everything is a moment later. Nothing in it"
            "draws anything.")

 ^:kindly/hide-code
 (sp/code
  "(defn step
   \"Move the ball. Turn it round if it has hit the top, the bottom, or the
  bat. Give up if it has gone past.\"
   [{:keys [ball vel bat hits missed] :as state}]
   (let [[x y]   ball
         [dx dy] vel
         nx      (+ x dx)
         ny      (+ y dy)
         bounce  (or (< ny 0) (> ny 70))
         on-bat  (and (< nx 4) (< (Math/abs (- ny bat)) 9))]
     (cond
       on-bat     (assoc state :ball [4 ny] :vel [(- dx) dy] :hits (inc hits))
       (< nx 0)   (assoc state :ball [50 35] :vel [0.9 0.55] :missed (inc missed))
       (> nx 100) (assoc state :ball [100 ny] :vel [(- dx) dy])
       bounce     (assoc state :ball [nx (max 0 (min 70 ny))] :vel [dx (- dy)])
       :else      (assoc state :ball [nx ny]))))

 (def start
   {:ball [50 35] :vel [0.9 0.55] :bat 35 :hits 0 :missed 0})

 (:ball (nth (iterate step start) 40))")

 (sp/action "Forty moments later the ball is where it would be. He runs it two"
            "thousand moments with the bat sitting still in the middle and"
            "counts how many times it comes back.")

 ^:kindly/hide-code
 (sp/code
  " (let [runs (take 2000 (iterate step start))]
   {:hits (:hits (last runs)) :missed (:missed (last runs))})")

 (sp/action "A bat that does not move gets a certain number of them and misses"
            "the rest, and that number is the whole game, and everything else"
            "is drawing.")

 (sp/action "Then the drawing. The bat follows the pointer. It keeps count. It"
            "never ends and it never gets faster, because he did not think of"
            "that until the following week.")

 (sketch/cell
  {:id "pong" :label "PONG.CLJS · click the page to stop it" :world [100 70] :rows 26}
  "(def state (atom {:ball [50 35] :vel [0.9 0.55] :bat 35 :hits 0 :missed 0}))
(def playing (atom true))

(set! (.-onmousemove cv)
      (fn [e] (swap! state assoc :bat (- 70 (/ (.-offsetY e) SY)))))
(set! (.-onclick cv) (fn [_] (swap! playing not)))

(defn step [{:keys [ball vel bat hits missed] :as s}]
  (let [[x y] ball [dx dy] vel
        nx (+ x dx) ny (+ y dy)
        bounce (or (< ny 0) (> ny 70))
        on-bat (and (< nx 4) (< (Math/abs (- ny bat)) 9))]
    (cond
      on-bat     (assoc s :ball [4 ny] :vel [(- dx) dy] :hits (inc hits))
      (< nx 0)   (assoc s :ball [50 35] :vel [0.9 0.55] :missed (inc missed))
      (> nx 100) (assoc s :ball [100 ny] :vel [(- dx) dy])
      bounce     (assoc s :ball [nx (max 0 (min 70 ny))] :vel [dx (- dy)])
      :else      (assoc s :ball [nx ny]))))

(defn draw [{:keys [ball bat hits missed]}]
  (clear!)
  (let [[x y] ball]
    (poly [[(- x 1) (- y 1)] [(+ x 1) (- y 1)]
           [(+ x 1) (+ y 1)] [(- x 1) (+ y 1)]]))
  (poly [[1 (- bat 8)] [3 (- bat 8)] [3 (+ bat 8)] [1 (+ bat 8)]])
  (poly [[99 0] [100 0] [100 70] [99 70]])
  (.fillText ctx (str hits \" / \" missed) 10 14))

(frames! 100000 1 (fn [_] (when @playing (swap! state step)) (draw @state)))")

 (sp/action "He plays it until two, which is later than he has stayed up for"
            "anything that was going to be marked."))