^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 3 — The Substitution"}}}
(ns act5.s3-the-substitution
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.entities.eli :as eli]
            [fedoras.entities.stakeholders :as stakeholders]
            [fedoras.cs-class.life.life :as life]
            [fedoras.state :as state]))

^:kindly/hide-code
(sp/assets)

;; # Scene 3 — The Substitution

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 26)

^:kindly/hide-code
(state/source "fedoras/llm.cljs")   ; generic ask-entity!, save-transcript!, etc.

^:kindly/hide-code
(state/load-entities ["fedoras/entities/eli.edn"])

^:kindly/hide-code
(stakeholders/cell
 {:id "stakeholders" :label "STAKEHOLDERS.CLJS · a shadowy meeting" :rows 45})

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. UNDER MERIDIAN — LAZLO'S ROOM — FRIDAY 10 APRIL, 11:40 P.M.")

 (sp/action "The lamps over the plants and the long bench and the smell of the"
            "place, which neither of them notices this time, which is its own"
            "kind of information.")

 (sp/action "Forty-one books along the bench in one row, in mark order, with"
            "slips of paper standing up out of about half of them.")

 (sp/action "On the wall at the far end, in chalk, there are lines.")

 (sp/action "Short horizontal marks, one above another up the brick, with a"
            "date beside each in the same hand, going back years, and the top"
            "four are close together.")

 (sp/beat "Nobody asks about the wall.")

 (sp/dialogue "LAZLO"
              "You've read the register.")

 (sp/dialogue "MAYA"
              "Four times.")

 (sp/dialogue "LAZLO"
              ["Good. Then you know the shape of it and I'm not"
               "doing the shape of it again."])

 (sp/action "He takes a slip out of one of the books and puts it on the bench.")

 (sp/dialogue "LAZLO"
              "I asked for forty-one marks.")

 (sp/dialogue :pc
              "You got forty-one books.")

 (sp/dialogue "LAZLO"
              "I got forty-one books.")

 (sp/beat "He turns the slip round.")

 (sp/dialogue "LAZLO"
              ["Three three one point eight eight. Combination and"
               "the Arrays. On my list, asked for, not here."])

 (sp/dialogue "MAYA"
              "So you're at forty.")

 (sp/dialogue "LAZLO"
              ["I'm at forty-one. There is a book on that bench"
               "that was not on my list and I have counted the row"
               "eleven times since Sunday because I did not"
               "believe it the first ten."])

 (sp/action "He picks it up. Boards, not cloth, the size of the works book,"
            "with a mark painted at the foot of the spine.")

 (sp/stage "The card for it is below.")

;; ---

 (art/handout
  "016.11   —   catalogue card   —   NEW CARTHAGE"
  "SHELF REGISTER · C-3 SOUTH · SUPPLEMENTARY"
  "acc. 9-0114   ·   accessioned, and not stamped since"
  "COPIES: 1"
  "LOCATION: C-3/S — held"
  "Contents: bay, row, shelf, mark, condition, and the date the shelf was last dressed."
  "This volume is a finding aid and is not itself upon the list.")

;; ---

 (sp/slug "INT. LAZLO'S ROOM — CONTINUOUS")

 (sp/dialogue "MAYA"
              "It's an index.")

 (sp/dialogue "LAZLO"
              ["It is the index. It is every shelf south of the"
               "line, by bay and by row, with the condition"
               "written against each one, and it is one copy, and"
               "it lives down there, and somebody took it off a"
               "shelf and put it in a stack for a boy to carry."])

 (sp/dialogue :pc
              "He swapped it.")

 (sp/dialogue "LAZLO"
              ["He had eight in his arms and he had about four"
               "seconds to decide and he put one of mine back and"
               "took this instead."])

 (sp/beat "Nobody says anything.")

 (sp/dialogue "MAYA"
              "Then it's a message and it isn't a warning.")

 (sp/dialogue "LAZLO"
              "No.")

 (sp/dialogue "MAYA"
              ["A warning would have been a note. This is a list"
               "of what's on the other side of a line, sent to a"
               "man who can't cross it."])

 (sp/dialogue "LAZLO"
              ["Yes. That is what you send somebody when you want"
               "them to come."])

 (sp/action "He puts it down and does not put it down well.")

 (sp/transition "Cut to:")

 (sp/slug "INT. LAZLO'S ROOM — 12:20 A.M.")

 (sp/action "He sits on the stool with his hands between his knees, which is"
            "how he sat at the bottom of the fill, and this time nobody is"
            "above him on a stair.")

 (sp/dialogue "LAZLO"
              "Right. Kent.")

 (sp/dialogue "LAZLO"
              ["My year. Not my friend, at the start, because he"
               "was better than me at the thing I was best at and"
               "I did not take that at all well and it took me two"
               "terms to get over it."])

 (sp/dialogue "LAZLO"
              ["We found the ground the way you found it, which is"
               "that somebody left a piece of paper somewhere and"
               "one of us picked it up."])

 (sp/dialogue "MAYA"
              "Which of you?")

 (sp/dialogue "LAZLO"
              "Him.")

 (sp/beat "He does not elaborate and {pc} does not ask, because there is a"
          "page folded into eight in his own bag and a pencil hand on it that"
          "he has never once matched to anybody.")

 (sp/dialogue "LAZLO"
              ["We were down here about fourteen months before"
               "anybody noticed either of us was gone, which tells"
               "you what you like about the place."])

 (sp/dialogue "LAZLO"
              ["And we got to the line, and we did what you did."
               "We sent things at it and they stopped."])

 (sp/dialogue :pc
              "You worked out what it was.")

 (sp/dialogue "LAZLO"
              ["We worked out about a third of what it was, and"
               "one third of it is that a person could go over and"
               "a person with a chip in his head couldn't, and"
               "there is no such thing as a person without one."])

 (sp/dialogue "MAYA"
              "So how did he go over?")

 (sp/beat "Four seconds.")

 (sp/dialogue "LAZLO"
              "Same as him.")

 (sp/action "He does not look at {pc} when he says it, which is worse than"
            "looking at him.")

 (sp/dialogue "LAZLO"
              ["He'd been ill for about a year. Nose, mostly. Then"
               "the gaps. Then longer gaps. The Health Service"
               "gave him a category and a piece of paper and I"
               "have never seen either of them."])

 (sp/dialogue "MAYA"
              "And it stopped when he crossed.")

 (sp/dialogue "LAZLO"
              "It stopped when he crossed.")

 (sp/transition "Cut to:")

 (sp/slug "INT. LAZLO'S ROOM — 12:40 A.M.")

 (sp/action "Maya has the notebook out and has not written anything in it for"
            "four minutes, which is not like her.")

 (sp/dialogue "MAYA"
              "Did he ask you to come with him?")

 (sp/beat "Lazlo takes a long time over that, and it is not for effect.")

 (sp/dialogue "LAZLO"
              "No. I asked him to go.")

 (sp/beat "Nobody says anything.")

 (sp/dialogue "LAZLO"
              ["I did the arithmetic. Somebody had to be on this"
               "side, because the stair is on this side, and"
               "anything he found over there was no use to anybody"
               "unless there was a man on this side to take it up"
               "a stair."])

 (sp/dialogue "LAZLO"
              ["That is true. I have gone over it every day for"
               "nine years and it is still true, and it was also"
               "the reason I wanted, and both of those are the"
               "case at the same time and I have never got them"
               "apart."])

 (sp/dialogue "MAYA"
              "And then?")

 (sp/dialogue "LAZLO"
              ["And then he went over, and he stood about eight"
               "feet the other side of it and told me what it was"
               "like, and I said I'd come in a minute."])

 (sp/beat "The lamps hum.")

 (sp/dialogue "LAZLO"
              "That was the minute.")

 (sp/transition "Cut to:")

 (sp/slug "INT. LAZLO'S ROOM — 1:05 A.M.")

 (sp/dialogue :pc
              "Why haven't you gone since?")

 (sp/dialogue "LAZLO"
              "I told you why in March.")

 (sp/dialogue :pc
              "You said you'd feel well.")

 (sp/dialogue "LAZLO"
              ["I said I'd feel well. I have watched a man feel"
               "well and I have watched what it cost, and it did"
               "not cost him anything that anybody could see,"
               "which is the part I would like you to sit with."])

 (sp/dialogue "MAYA"
              "Do you talk to him?")

 (sp/dialogue "LAZLO"
              ["I have not heard his voice in nine years. Twice a"
               "year something is moved that I did not move, and"
               "once a year something is left where I will find"
               "it, and I have never once left him anything, and"
               "you may take that as an answer to a question you"
               "have not asked."])

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. LAZLO'S ROOM — 1:30 A.M.")

 (sp/action "Maya has been at the far end of the bench with the chalk wall for"
            "a while and has not touched it.")

 (sp/dialogue "MAYA"
              "These are the sump.")

 (sp/dialogue "LAZLO"
              "They're the sump.")

 (sp/dialogue "MAYA"
              ["The top four are eighteen months apart, then nine"
               "months, then five, then this one, which is"
               "February."])

 (sp/dialogue "LAZLO"
              "That one's February.")

 (sp/dialogue "MAYA"
              "And there isn't one since.")

 (sp/beat "Four seconds.")

 (sp/dialogue "LAZLO"
              ["There isn't one since because I stopped putting"
               "them up, because a mark on a wall is a thing you"
               "put up when you think somebody is going to read"
               "it."])

 (sp/dialogue :pc
              "You've been emptying the buckets.")

 (sp/beat "It is not a question and it has been sitting in him since October,"
          "when two buckets were emptied and it was not Hollis and it was not"
          "him.")

 (sp/dialogue "LAZLO"
              ["Six years. Two, three nights a week, up the fill,"
               "out at the top, down the drain in the corner, and"
               "back."])

 (sp/dialogue "LAZLO"
              ["It is the single most useful thing I have done"
               "with nine years and it is carrying water in a"
               "bucket, and I would like that on a record"
               "somewhere, and there isn't one."])

 (sp/dialogue "MAYA"
              "And in February?")

 (sp/dialogue "LAZLO"
              ["In February somebody bolted a tray to the floor"
               "and put a pipe out of the corner of it into the"
               "ground, and I have been down there four times with"
               "what I have got, and what I have got is a"
               "screwdriver and a hacksaw blade and no light I can"
               "use for more than about ten minutes."])

 (sp/dialogue :pc
              "It's four bolts.")

 (sp/dialogue "LAZLO"
              ["It is four bolts through concrete that has been"
               "wet since June and it is an hour with a spanner in"
               "a basement with the light on, and it is the one"
               "hour in nine years I have not been able to buy."])

 (sp/beat "Nobody says anything.")

 (sp/dialogue "LAZLO"
              ["And in January it would have been a boy with a"
               "service assignment and a torch, doing it in the"
               "middle of a Tuesday morning, and nobody would have"
               "asked him a single question about it."])

 (sp/transition "Cut to:")

 (sp/slug "INT. LAZLO'S ROOM — 1:50 A.M.")

 (sp/dialogue "MAYA"
              "Does he know about the floor?")

 (sp/beat "Lazlo does not answer straight away, and when he does he answers a"
          "different question.")

 (sp/dialogue "LAZLO"
              ["He had one substitution. He had four seconds and"
               "one hand free and he could have sent me anything."])

 (sp/dialogue "LAZLO"
              ["He could have sent me a note on the back of a card"
               "that said the ground is going. He sent me a list"
               "of what is on the shelves."])

 (sp/dialogue "MAYA"
              "Maybe he doesn't know.")

 (sp/dialogue "LAZLO"
              ["He has been standing in the bottom of it for nine"
               "years."])

 (sp/beat "The lamps hum.")

 (sp/dialogue "LAZLO"
              "He'd have known before I did.")

 (sp/transition "Blackout.")

;; ---

 (sp/slug "INT. PRENTISS SCIENCE CENTER — THE BENCH ROOM — SATURDAY 11 APRIL, 5:00 P.M.")

 (sp/action "MAYA has a run on. {pc} has been asleep in the chair by the"
            "window for about two hours and neither of them planned that.")

 (sp/action "She has the garden open on the machine beside her and has been"
            "raking it, on and off, between readings, for most of an hour.")

 (sp/beat "She has not moved a stone and she has not put one down.")

 (sp/dialogue "MAYA"
              "What's it for?")

 (sp/beat "He does not open his eyes.")

 (sp/dialogue :pc
              "Nothing.")

 (sp/beat "She rakes it once more, along the bottom, all the way across.")

 (sp/dialogue "MAYA"
              "It's very good gravel.")

 (sp/transition "Blackout.")

 (sp/slug "INT. REIMPLEMENTATION WING — OBSERVATION DECK — LATER")

 (sp/action "The door is ajar. It shouldn't be, but it is. {pc} has walked"
            "past this door a hundred times and never seen it open. Tonight it"
            "is open an inch, and the light inside is the colour of a bruise.")

 (sp/action "{pc} pushes it. The hinges don't make a sound. The floor is"
            "polished so bright it reflects the ceiling lights, but not {pc}.")

 (sp/action "A narrow corridor. A window at the end. Through the window, a"
            "room with a table. On the table, a body. The body is Eli.")

 (sp/action "{pc} moves closer. The window glass is cold. Behind it, Eli lies"
            "on the table with tubes and wires attached to his arms, his"
            "chest, the base of his skull. He is awake. His eyes are open."
            "They turn slowly, and find {pc} through the glass.")

 (sp/beat "Eli's mouth moves. The intercom crackles.")

 (sp/off-screen "ELI"
                "You came. I wondered if you would.")

 (sp/action "There is a console set into the wall before the window. Its"
            "screen flickers once, and a prompt appears, waiting for someone"
            "to type.")

 (sp/beat "The chair in front of the console is already pulled out. As if it"
          "has been waiting for {pc}.")

 (sp/action "On the table, Eli's hand twitches. The tubes pulse. The machine"
            "behind him begins a low, anticipatory hum.")

 ^:kindly/hide-code
 (eli/cell
  {:id "eli-final" :label "ELI.CLJS · outside of the reclamation vats" :rows 35})

 (sp/action "{pc} runs. The corridor is dark, but not dark enough. Behind, the"
            "chamber door slides open, and the sound that comes out is not a"
            "scream. It is wet and mechanical and it goes on for longer than a"
            "scream could.")

 (sp/action "{pc} glances back. Through the glass, the chamber is lit by a"
            "sick green light. Eli is off the table — or something that was"
            "Eli. Tubes have been pulled free. Organs are being removed, not"
            "with hands but with articulated arms that move too quickly, too"
            "precisely. They are replaced with metal and cartilage and things"
            "that pulse. Eli's mouth is open. His eyes are open. He is not"
            "unconscious.")

 (sp/action "A voice — not Eli's, but Eli's — says: 'Sister. Find."
            "Protect.' It repeats, over and over, like a needle stuck.")

 (sp/action "{pc} turns and runs. The sound follows: the wet, mechanical"
            "tearing, the voice repeating, and underneath it, a low hum that"
            "is almost a lullaby.")

 (sp/beat "The door to the stairwell is open. {pc} does not remember opening"
          "it.")

 (sp/action "Behind, the voice stops. In the sudden silence, a new sound: a"
            "single, high, clean tone, like a tuning fork held against the"
            "skull. {pc} knows, without turning, that Eli has stopped being"
            "Eli.")

 (sp/action "The stairwell door closes. {pc} leans against it. His hands are"
            "shaking. In the dark, he can still see the last image: Eli's"
            "eyes, both of them, turning to find him through the glass.")

 (sp/beat "And then the hum begins. Not in the chamber. Under {pc}'s feet."
          "Under everything.")

 (sp/transition "Cut to:")

 (sp/slug "INT. REPROCESSING — THE GANTRY — SATURDAY 18 APRIL, 2:26 A.M.")

 (sp/action "The shutter is coming up at the north end and it is not coming up"
            "quickly and it is not coming up quietly.")

 (sp/action "Four of them get off the gantry and behind the tanks in about"
            "eleven seconds and none of them decide to.")

 (sp/beat "A vehicle backs in.")

 (sp/action "It is a lorry with a hopper and a driver, and the driver does not"
            "get out. The hopper goes up and something goes onto the belt and"
            "the lorry goes out and the shutter comes down and the whole of it"
            "takes four minutes.")

 (sp/action "The belt runs. Nobody looks at what is on it.")

 (sp/beat "Afterwards Eli is sitting on the floor with his back against a tank"
          "and his hands over his ears and neither hand is doing anything.")

 (sp/dialogue "LENA"
              "Up. We're going up.")

 (sp/dialogue "MAYA"
              "Up where?")

 (sp/dialogue "LENA"
              "Where the pipes go.")

 (sp/dialogue "MAYA"
              "Lena—")

 (sp/dialogue "LENA"
              ["I have spent my whole life being moved between"
               "rooms by people who would not tell me what the"
               "rooms were for, and I have got one night, and I am"
               "going to the end of the pipe."])

 (sp/beat "Nobody argues with her, which is the first time all year.")

 (sp/transition "Cut to:")

 (sp/slug "EXT. NORTH-NORTH-EAST — 2:50 A.M.")

 (sp/action "Outside, which none of them expected, and it is not the quad and"
            "it is not a road.")

 (sp/action "The pipe bundle goes over a service way on brackets, four feet"
            "up, lagged, and the lagging has been replaced in places and the"
            "replacements are a different colour.")

 (sp/action "They walk along under it. It is two hundred and eighty yards and"
            "it takes eleven minutes because none of them walks fast.")

 (sp/action "Ahead there is a building with no windows in the first sixty feet"
            "of it and light coming out of the top of it, and the light is the"
            "colour of a room that is on at three in the morning because it is"
            "on at every other hour as well.")

 (sp/action "Over the door, painted, in the same yellow as the rails and the"
            "line on the floor of a hall two hundred and eighty yards behind"
            "them:")

 (sp/sign "ARRAY NINE")

 (sp/beat "Eli laughs. It is one syllable and it is not a laugh.")

 (sp/dialogue "ELI"
              "It's on at seven.")

 (sp/dialogue "MAYA"
              "What is?")

 (sp/dialogue "ELI"
              "The programme. It's on at seven.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ARRAY NINE — THE FLOOR — 3:05 A.M.")

 (sp/action "The door is not locked. Nothing down here is locked, because"
            "locking is a thing you do when somebody might come.")

 (sp/action "Desks. In tens, and tens of tens, and the tens of tens go further"
            "than the lighting does, so the floor ends in a haze rather than"
            "in a wall.")

 (sp/action "Every desk is occupied. Every desk has been occupied for a long"
            "time.")

 (sp/action "There is a pole at every hundredth desk with a number on it, lit,"
            "and the numbers are the only bright thing in the room.")

 (sp/action "Nobody looks up. The room is not quiet: it is the sound of"
            "several thousand people speaking at a conversational volume to"
            "somebody who is not in the room, and it arrives as one sound, and"
            "the one sound is almost restful.")

 (sp/beat "At the quarter hour the misters come on for ninety seconds and the"
          "room smells briefly of nothing at all.")

 (sp/action "The pipes come in at the far end and go up into the ceiling and"
            "split, and split, and split, and after the fourth split they are"
            "the width of a finger and there is one of them going down to"
            "every desk.")

 (sp/action "They are dressed in the same blue as the chairs.")

 (sp/beat "Nobody says anything about that either.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ARRAY NINE — AISLE 40 — 3:20 A.M.")

 (sp/action "A SUBMANAGER comes down the aisle on a four-minute round with a"
            "tablet and does not stop and does not look at the four people"
            "standing in his aisle in university uniform at twenty past three"
            "in the morning.")

 (sp/dialogue "THE SUBMANAGER" "in passing, pleasantly"
              "You want the visitor desk. It's at the front.")

 (sp/beat "He goes on down the aisle.")

 (sp/dialogue "ELI"
              "There's a visitor desk.")

 (sp/dialogue "LENA"
              "Of course there's a visitor desk.")

 (sp/action "They go along Aisle 40 because it is the aisle they are standing"
            "in.")

 (sp/action "The people at the desks are of every age. That is the thing none"
            "of them expected and it is the thing that takes the longest to"
            "arrive.")

 (sp/action "A woman at 4,401 says thank you for holding in a voice that has"
            "said thank you for holding a great many times and still means it.")

 (sp/beat "4,412 is empty.")

 (sp/action "The chair is out. The line is capped. There is a card on the desk"
            "face down and a mug beside it and the mug has been used.")

 (sp/dialogue "MAYA" "quietly"
              "Somebody's on a break.")

 (sp/dialogue "LENA"
              "Nobody here is on a break.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ARRAY NINE — AISLE 40 — 3:31 A.M.")

 (sp/action "Eleven desks further along, {pc} stops.")

 (sp/action "He has stopped because he recognised somebody, which he did not"
            "know he was capable of doing in a room this size, and he stands"
            "in the aisle looking at the side of a face he has seen three"
            "times a week from four rows back.")

 (sp/action "She is fine. That is the whole of it and it is the worst sentence"
            "in the book. She is well. She is not thin and she is not"
            "frightened and she is not restrained and her hair is done the way"
            "it was done in September.")

 (sp/action "She is working, and she is good at it, and there is a figure on"
            "the pole above her aisle that has been going the right way all"
            "night.")

 (sp/dialogue "MAYA"
              "Do you know her?")

 (sp/beat "{pc} opens his mouth.")

 (sp/beat "Nothing comes out, because he does not have it, because he never"
          "asked, because she asked him for four lines of code in a corridor"
          "in October and he said he would send them and then he did not.")

 (sp/dialogue :pc
              "CS 101. Second row.")

 (sp/dialogue "MAYA"
              "What's her name?")

 (sp/beat "Four seconds.")

 (sp/dialogue :pc
              "I don't know.")

 (sp/action "Maya takes the notebook out and writes down the number on the"
            "desk and the number on the pole and the time.")

 (sp/action "She does not write anything else, and she does not say anything,"
            "and she puts the notebook back in the bag.")

 (sp/beat "She has known the name since November.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ARRAY NINE — AISLE 40 — 3:34 A.M.")

 (sp/action "The girl in the second row finishes a call.")

 (sp/action "There is a moment between one and the next that is about four"
            "seconds long, and in it she looks up, and she looks at four"
            "students standing in an aisle in the middle of the night in the"
            "uniform of a place she was at in September.")

 (sp/beat "She smiles at them.")

 (sp/action "It is not a brave smile and it is not a frightened smile and"
            "there is nothing in it that anybody could take away and use. It"
            "is the smile of somebody who has seen a visitor.")

 (sp/beat "Then the next one comes in and she takes it.")

 (sp/action "ELI is already walking. LENA goes after him. MAYA takes {pc} by"
            "the sleeve, which she has never done, and turns him round and"
            "walks him out of Aisle 40 with her hand still on the sleeve for"
            "about sixty feet.")

 (sp/transition "Cut to:")

 (sp/slug "EXT. THE SERVICE WAY — 3:50 A.M.")

 (sp/action "Cold. The pipes overhead. Two hundred and eighty yards back the"
            "way they came, and nobody starts walking for a while.")

 (sp/dialogue "ELI"
              "She's all right, though. She looked all right.")

 (sp/beat "Nobody answers him.")

 (sp/dialogue "ELI"
              ["I'm not — I know. I know what I'm saying. I'm"
               "saying she looked all right and I don't know what"
               "to do with that."])

 (sp/dialogue "LENA"
              "No.")

 (sp/action "PIP has been on Eli's shoulder since the gantry and has not said"
            "anything since the gantry.")

 (sp/dialogue "MAYA"
              "Pip. Does he know?")

 (sp/beat "Four seconds.")

 (sp/off-screen "PIP"
                ["He knows there is an array at the end of the pipe."
                 "Everybody who has ever seen the pipe knows there"
                 "is an array at the end of the pipe."])

 (sp/dialogue "MAYA"
              "That isn't what I asked.")

 (sp/off-screen "PIP"
                "No.")

 (sp/transition "Blackout.")

;; ---

 (art/handout
  "THE FLOOR · fourteenth season · episode nine · transcript (approved)"
  "Recorded on Array Nine."
  "DERMOT.  Paula. Aisle forty.  You've been on forty since I started here."
  "PAULA.  I like forty."
  "(LAUGHTER)"
  "DERMOT.  Nobody likes forty."
  "PAULA.  I like the people on forty."
  "(LAUGHTER · SUSTAINED)"
  "DERMOT.  (warmly) That's the array talking."
  "(APPLAUSE)"
  "— the quarterly, six pages, closing line: the warmth of the programme is in its accuracy.")

 (sp/action "It is on at seven. Every one of them has seen this episode. Eli"
            "could do a really good impersonation of the voice."))

;; ---

^:kindly/hide-code
(life/widget)