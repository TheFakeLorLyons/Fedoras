^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 5 — Held"}}}
(ns act5.s5-held
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.sessions.five :as session-five]
            [clojure.string :as str]
            [fedoras.cs-class.walk.walk :as walk]))

^:kindly/hide-code
(sp/assets)

;; # Scene 5 — Held

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 22)

;; ---

^:kindly/hide-code
(sp/scene
 (sp/action "Review is the third week of May. Nobody works. Everybody works."
            "The reading room is full at four in the morning for six nights and"
            "the Commons never closes, and there is a fortnight of the year in"
            "which this institution genuinely resembles the thing on the"
            "banner, and it is the same fortnight it was in December.")

 (art/email
  {:from "Office of the Registrar <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "22 May, 4:00 AM"
   :subject "Year — closed"}
  "Review is complete. Your standing is: no action."
  "Provisional standing is discharged. Your standing from the first day of next term is: enrolled."
  "Set three is marked. Set two assignment six is marked separately and is recorded as accepted."
  "Enrolment for next year is confirmed. Enrolment stands at 1,024."
  "Summer arrangements are attached separately."
  "Congratulations on the completion of your first year.")

 (sp/action "Provisional is discharged, which is the thing he came here to do,"
            "and which he has not thought about since about the second week of"
            "February.")

 (sp/action "He reads it twice looking for the sentence about Meridian and"
            "there isn't one.")

;; ---

 (sp/slug "INT. ASHCROFT HOUSE — HIS ROOM — A TUESDAY, LATE")

 (sp/action "The map on the wall by the door is the one every room has."
            "Buildings as rectangles, the loop in gold, and fourteen little"
            "circles that are not explained anywhere.")

 (sp/beat "He has walked past it about four hundred times.")

 (sp/action "What he notices, at some point after midnight and for no reason"
            "he could give you, is that it is a list. Eleven boxes, each with"
            "a corner and a width and a height, and somebody typed them in.")

 (sp/action "And if it is a list of boxes, then standing anywhere on it and"
            "asking what is in front of you is arithmetic.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "Meridian Hall, and the Annexe under it, and the gap between them"
            "that is four feet wide and has a plant door at the back of it.")

 (sp/action "The same spot, turned to face Whitlock.")

 (sp/action "It is the map. There is nothing in the picture that is not in the"
            "map, and he has not drawn a single building, and that is the part"
            "he sits with.")

;; ---

 (sp/action "Yours. Arrows to turn and to walk. It will not let you into a"
            "building, because a wall is a wall, and it says what you are"
            "looking at and how far off it is.")

 (walk/widget)

 (sp/action "The list of buildings in that cell is not a copy. It is written"
            "out of the plan when the book is built, so if somebody renames a"
            "building after a donor falls out of favour, it is renamed in here"
            "too, and nobody has to remember.")

;; ---

 (sp/slug "EXT. THE QUAD — THE FOLLOWING NIGHT")

 (sp/action "He walks it. Actually walks it, at two in the morning, from the"
            "middle of the quad to the corner of Meridian and back, with the"
            "machine shut in his room.")

 (sp/beat "It is the same.")

 (sp/action "Not similar. The gap between the Hall and the Annexe is where it"
            "is in the picture, and the Commons is behind them at the distance"
            "the picture gave, and he stands in the middle of the quad for a"
            "while.")

 (sp/beat "Then he turns round and faces south-west, at the low ground past"
          "Plant Services, and there is nothing there.")

 (sp/action "There is nothing there in the picture either. It is not drawn as"
            "empty. It is drawn as ground with a name on it, and the name is"
            "on the plan on the wall of every room in Ashcroft House, and it"
            "has been there since August.")

 (sp/transition "Blackout.")

;; ---

 (sp/slug "INT. MERIDIAN HALL — REYES 204 — FRIDAY 22 MAY")

 (sp/action "The door is open because she is in it. The corridor outside is"
            "full of people carrying boxes past it in both directions.")

 (sp/action "He puts it on the desk on paper because she will not take"
            "anything any other way. It is longer than it was in March and it"
            "runs the four hundred and nine pages and it does not fall over on"
            "any of them.")

 (sp/action "She reads it the way she reads things, which is fast and twice.")

 (sp/dialogue "REYES"
              "Is it finished?")

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "REYES"
              "Say why.")

 (sp/beat "Nobody has asked him to say why about anything all year.")

 (sp/dialogue :pc
              ["Because it does the thing it says on the front and"
               "it does it on all of them, and everything I've"
               "added since March has been me tidying it, and I"
               "stopped."])

 (sp/dialogue "REYES"
              "Right.")

 (sp/action "She marks it full, which takes about four seconds, and then"
            "writes one word in the margin.")

 (sp/beat "The word is AGAIN.")

 (sp/action "He looks at it for a moment and cannot tell whether it is an"
            "instruction or a criticism, and does not ask, and does not find"
            "out for about a week.")

 (sp/dialogue "REYES"
              ["Set one, next year, is six programs in three weeks"
               "and the first of them counts to a hundred and says"
               "fizz on the threes."])

 (sp/dialogue :pc
              "I know.")

 (sp/dialogue "REYES"
              ["Yes. You'll be in the room and there'll be"
               "thirty-one of them who aren't, and about four of"
               "them will be listening, and you will not be able"
               "to tell which four and neither can I."])

 (sp/beat "That is the closest she comes all year and she goes back to the"
          "screen in the middle of it.")

 (sp/dialogue "REYES"
              "Off you go. Have a summer.")

 (sp/transition "Blackout.")

 (sp/action "He keeps the paper. It is in the bag with the page that has been"
            "folded into eight since August.")

;; ---

 (sp/slug "INT. STERLING CENTER — AN INNER OFFICE — MONDAY 25 MAY, 11:00 A.M.")

 (sp/action "One window, one desk, and a screen turned away.")

 (sp/dialogue "PARR"
              "It's the last week.")

 (sp/dialogue :pc
              "I know. I'm not taking it.")

 (sp/beat "Parr types for about two seconds.")

 (sp/dialogue "PARR"
              "Right.")

 (sp/dialogue :pc
              "Don't you want to know why?")

 (sp/dialogue "PARR"
              ["There isn't a box for why. There's a box that says"
               "declined and there's a box that says lapsed and"
               "declined is better for you, so I've put declined."])

 (sp/beat "Four seconds.")

 (sp/dialogue :pc
              "What's the difference?")

 (sp/dialogue "PARR"
              ["Declined is a person doing something. Lapsed is a"
               "date going past."])

 (sp/action "He turns the card face down on the desk, which he did in February"
            "the other way round.")

 (sp/dialogue "PARR"
              "It'll be open again in February.")

 (sp/dialogue :pc
              "Every year?")

 (sp/dialogue "PARR"
              ["Meridian takes four from here a year and there are"
               "four every year, and if you keep declining it"
               "somebody will eventually want to know what you're"
               "for instead, and that isn't a threat, that's just"
               "how a list works."])

 (sp/dialogue :pc
              "Can I have a service assignment over the summer?")

 (sp/dialogue "PARR"
              ["There isn't one. The whole line's contracted and"
               "there is nothing in Plant for anybody."])

 (sp/dialogue :pc
              "Then I'll keep Systems.")

 (sp/dialogue "PARR"
              ["Systems is a post. It doesn't stop for a summer"
               "and it's paid and you'd have had to tell me if you"
               "weren't keeping it."])

 (sp/beat "So he keeps it, and this is the first thing he has taken in this"
          "building by saying yes to it out loud rather than by not saying no.")

 (sp/dialogue "PARR"
              "Are you going anywhere?")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "PARR"
              "That's most of them.")

 (sp/transition "Blackout.")

;; ---

 (art/handout
  "NEW CARTHAGE UNIVERSITY · SUMMER ARRANGEMENTS · {pc-full}"
  "Accommodation: Ashcroft House, room 214, retained. Single."
  "Systems: continues. Mondays and Wednesdays, 09:00–13:00. Paid."
  "Service assignment: none held."
  "Docket: not assigned."
  "Remote operations: no ballot lodged."
  "Nomination: recorded."
  "Dining allocation is unaffected. The Commons will operate on summer hours.")

 (sp/action "It is the sixth line and it is between the ballot he did not lodge"
            "and the dining allocation, and it does not say a name, because the"
            "form does not print the name, because the name is not for the"
            "subject.")

 (sp/action "He did it on the Tuesday, at four in the afternoon, in about"
            "eleven seconds, and he did not tell her and he is not going to.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Miles" :at "20 May, 6:02 PM" :text "review week. good luck everyone. genuinely"}
   {:who "Simon" :at "20 May, 6:40 PM" :text "good luck"}
   {:who "Derek" :at "26 May, 11:12 AM" :text "no action"}
   {:who "Miles" :at "26 May, 11:14 AM" :text "no action"}
   {:who "Simon" :at "26 May, 12:31 PM" :text "no action"}
   {:who "Victor" :role "PRESIDENT" :at "26 May, 12:33 PM" :text "Five of five."}
   {:who "Derek" :at "26 May, 12:34 PM" :text "victor thats not what that means"}
   {:who "Victor" :role "PRESIDENT" :at "26 May, 12:35 PM" :text "It is this once."}])

;; ---

 (art/email
  {:from "Office of the Dean <no-reply@newcarthage.edu>"
   :to "The Fedora Society <c/o V. ASHCROFT>"
   :date "26 May, 4:00 AM"
   :subject "Engagement — submission acknowledged"}
  "Thank you for your submission of November concerning the treatment of recognised societies within the engagement figure."
  "The submission was noted at the board. The arithmetic was checked and found to be correct."
  "The methodology of the engagement figure is under review. Societies are not at present within scope. The review has no reporting date."
  "The Dean has asked that the Society be thanked for its interest."
  "Engagement is not a component of review. Engagement is recorded.")

 ^:kindly/hide-code
 (sp/action "Victor reads it out at the last meeting, standing up, all of it,"
            "twice, and the second time slower.")

 ^:kindly/hide-code
 (sp/action "The arithmetic was checked and found to be correct. He says that"
            "part four times over the course of the evening and nobody at that"
            "table takes it off him, and Miles, who did the arithmetic, does"
            "not say a word about whose it was.")

;; ---

 (sp/slug "INT. HALLORAN UNION — ROOM 3C — WEDNESDAY 27 MAY, 7:00 P.M.")

 (sp/action "The folding table. The tin. The hat, which has been repaired"
            "twice this year and is worse each time and which nobody has said"
            "anything about since the second week of September.")

 (sp/action "Five chairs and five people in them. Simon is in his and has been"
            "at three of five since April and is here for this.")

 (sp/dialogue "VICTOR"
              ["Last one. I'm going to finish it properly because"
               "I have never finished one properly and I have run"
               "six."])

 (sp/action "He takes the screen down, which he has never done, and puts it"
            "flat on the table so that everybody can see the notes, which are"
            "four pages and are in pencil and have been gone over.")

 (sp/dialogue "VICTOR"
              ["The line in the ledger closed in November. The"
               "professor is teaching. The class is full."])

 (sp/dialogue "MILES"
              "We know.")

 (sp/dialogue "VICTOR"
              "You know. Here's the part I've not told you.")

 (sp/dialogue "VICTOR"
              ["The bursar's office has a line in the ledger they"
               "can't account for. Every month for three years the"
               "Collegium has paid a salary to a lecturer in the"
               "natural philosophy faculty, and last week somebody"
               "in accounts finally asked which lecturer."])

 (sp/beat "It is the first thing he said at the first session in September,"
          "word for word, and three of them get it at different speeds.")

 (sp/dialogue "DEREK"
              "It's starting again.")

 (sp/dialogue "VICTOR"
              ["It started again in November. It has been starting"
               "again the whole time. It is a different clerk and"
               "a different lecturer and the same line."])

 (sp/dialogue "SIMON" "quietly"
              "So nothing we did counted.")

 (sp/dialogue "VICTOR"
              "Everything you did counted. It's in the minutes.")

 (sp/beat "Miles puts his hand flat on the ledger and does not say anything.")

 (sp/dialogue "VICTOR"
              ["There's one thing that came out of that corridor"
               "that was not there when you went in and it is"
               "still with you, and it is the only thing in nine"
               "months that nobody has been able to file."])

 (sp/action "He looks at the sheet in front of {pc}, at the line marked"
            "COMPANION, where somebody wrote a name in in September.")

 (sp/stage "What your animal does at the end of it is below.")

;; ---

 (session-five/what-it-did-last)

;; ---

 (sp/slug "INT. ROOM 3C — 10:40 P.M.")

 (sp/action "Nobody says anything for a while and it is not uncomfortable.")

 (sp/dialogue "VICTOR"
              ["Right. Next year is a new campaign and new"
               "characters and I would like everybody to think"
               "about what they want to be, over the summer, and"
               "to come back in September with an answer."])

 (sp/dialogue "DEREK"
              "Can I be the documentarian?")

 (sp/dialogue "MILES"
              "No.")

 (sp/dialogue "VICTOR"
              "No.")

 (sp/beat "Simon has not said anything.")

 (sp/dialogue "VICTOR"
              "Simon.")

 (sp/dialogue "SIMON"
              "Can I keep the paladin?")

 (sp/beat "Four seconds.")

 (sp/dialogue "VICTOR"
              "It's new characters.")

 (sp/dialogue "SIMON"
              "I know. Can I keep him anyway.")

 (sp/action "Victor looks at his notes for slightly longer than the question"
            "needs, which is the third time this year.")

 (sp/dialogue "VICTOR"
              "Yes.")

 (sp/action "Miles writes it down. It is the last entry in the ledger for the"
            "year and it is four words long.")

 (sp/transition "Cut to:")

 (sp/slug "INT. HALLORAN UNION — THE STAIRWELL — 11:50 P.M.")

 (sp/action "The pizza-box wall has been taken down because the Union does the"
            "corridors over the summer, and there is a clean patch of paint"
            "behind where it was, in a different colour, the size of a wall.")

 (sp/dialogue :pc
              "What are you doing over the summer?")

 (sp/dialogue "SIMON"
              ["Docket doesn't stop. Unattached students in good"
               "standing."])

 (sp/dialogue :pc
              "Every week?")

 (sp/dialogue "SIMON"
              "Every week.")

 (sp/beat "{pc} does not say any of the four things a person says.")

 (sp/dialogue :pc
              "Is that all right?")

 (sp/dialogue "SIMON"
              "It's a warm room.")

 (sp/beat "It is the same four words a member of faculty used to him in August"
          "about a nosebleed, and Simon has never heard them said that way and"
          "never will.")

 (sp/action "THE JANITOR is at the bottom of the stairwell with a bin liner."
            "Nobody hears the door.")

 (sp/dialogue "THE JANITOR"
              "Twelve.")

 (sp/dialogue "VICTOR" "from above"
              "Yes.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "Lena's letter comes on the twenty-eighth. She does not say what is"
            "in it and she does not say who it is from and she is in the print"
            "shop on the twenty-ninth and on the thirtieth and on the first of"
            "June.")

 (sp/action "Eli goes on the thirtieth with a bag and four books, and three of"
            "the four are the ones that went onto the list in June and one of"
            "them is by the woman who is on it twice.")

 (sp/action "Derek balloted in March on five years of hours and is Tier Two"
            "again and has eleven sessions over the summer, and the ridge has a"
            "name and he has still not told anybody what it is.")

 (sp/action "Maya is given a run of her own, which is nine hours a day of a"
            "thermometer and a column, for six weeks, and it is the most"
            "anybody has given her since she got here, and she is pleased about"
            "it, and it is a thermometer and a column.")

 (sp/action "Hollis goes on the last day of term. There is no leaving do"
            "because there is no mechanism for one. He puts his mug in the sink"
            "and washes it and leaves it upside down on the drainer, and takes"
            "nothing off the wall.")

;; ---

 (sp/action "The last thing he writes this year is a thing that makes a card.")

 (sp/action "In March he wrote something that reads a card, which took an"
            "evening and most of the evening was an em dash. This one goes the"
            "other way and it took eleven minutes.")

 ^:kindly/hide-code
 (defn card
   "A catalogue card. Mark, title, accession, copies, then a location
  line for every copy, then whatever somebody wrote on it afterwards."
   [{:keys [mark title acc copies locations pencil]}]
   (concat
    [(str mark "   —   catalogue card   —   NEW CARTHAGE")
     title
     (str "acc. " acc)
     (str "COPIES: " copies)]
    (map (fn [[where status]] (str "LOCATION: " where " — " status)) locations)
    (when pencil
      [(str "— in pencil, along the bottom edge, in a hand that is not "
            "the typist's: " pencil)])))

 ^:kindly/hide-code
 (defn render [c] (str/join "\n" (card c)))

 (sp/action "That is the whole of it and there is nothing clever in it"
            "anywhere, and Lena sets it in the print shop on the first of June"
            "on card stock because a card is card, and it takes her four goes"
            "to get the spacing the way the old ones are.")

 (sp/action "They do not put the index on it. The index is four hundred pages"
            "and four hundred pages does not go in a drawer.")

 (sp/action "They print the index twice, on paper, at night, and one copy goes"
            "down the fill on the second of June, and the other one goes under"
            "the short leg of the desk in room two fourteen, folded, where it"
            "holds a desk level, which is the only reason anything in this book"
            "survived at all.")

 (sp/action "And then they type one card, and file it in the drawer with the"
            "016s in it, in order, because they were asked to.")

 (art/terminal
  (render
   {:mark "016.13"
    :title "A FINDING AID TO THE HOLDINGS UNDER THIS BUILDING · compiled from the cards"
    :acc "—"
    :copies 2
    :locations [["C-3/S" "held"]
                ["ASHCROFT 214" "held"]]
    :pencil "the water goes in a bucket. every day. the drain's in the corner."}))

 (sp/action "There is no accession number on it because they do not have one"
            "and will not make one up. There is no date. Nobody in that"
            "building will look at it, and somebody will put a bag on the"
            "cabinet within a week, and it will sit in order in a drawer in a"
            "room where people have supervised study on Mondays and Wednesdays.")

 (sp/action "It is the only thing either of them does all year that is meant to"
            "be found by somebody who is not born.")

;; ---

 (sp/slug "INT. MERIDIAN HALL — BASEMENT — MONDAY 8 JUNE, 6:10 A.M.")

 (sp/action "The campus is empty in the way it is empty in June, which is not"
            "empty at all, and is quieter.")

 (sp/action "The tray is gone. There is a hole in the slab with four inches of"
            "pipe in it and a sheared bolt beside it and a galvanised bucket"
            "under the pipe.")

 (sp/beat "It is nearly full.")

 (sp/action "{pc} picks it up with both hands, because that is how you pick up"
            "a full bucket, and carries it nine feet to the drain in the"
            "corner and pours it away, and puts it back under the pipe.")

 (sp/beat "It takes about forty seconds.")

 (sp/action "He does it again on Tuesday and on Wednesday and on Thursday.")

 (sp/transition "Cut to:")

 (sp/slug "EXT. THE QUAD — 8:00 A.M.")

 (sp/action "The banner has come down for the summer and the frame is still"
            "up.")

 (sp/action "A man on a ladder is doing something to the gate and two people"
            "are painting the railings and the grass has been cut.")

 (sp/action "Somewhere on the north side a tuba is being played very badly by"
            "somebody who has been given a room for six weeks and is going to"
            "use all of it.")

 (sp/beat "There is nothing wrong with the campus.")

 (sp/action "{pc} goes up to the fourth floor of the Sterling Center, where"
            "there is carpet, and does a page, and it takes twenty minutes,"
            "and he looks out of the window at the quad until one o'clock and"
            "gets paid for the morning.")

 (sp/transition "Blackout.")

;; ---

 (art/handout
  "NEW CARTHAGE UNIVERSITY"
  "WELCOME, CLASS OF 20XX!"
  "RANKED #1 IN STUDENT ENGAGEMENT*"
  "*According to New Carthage University."
  "INTAKE: 1,024"
  "ORIENTATION SUPPORTED BY MERIDIAN SYSTEMS")

 (sp/action "The banner goes back up on the eighteenth of August, on the same"
            "frame, and the number on it is the same number.")

 (sp/action "One thousand and twenty-four of them come up the drive with"
            "suitcases and one of them will be given room two fourteen in"
            "Ashcroft House, which is a single, and which has a desk in it that"
            "does not rock.")

 (sp/action "Curtain."))