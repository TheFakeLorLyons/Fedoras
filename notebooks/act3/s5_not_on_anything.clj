^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 5 — The Boy Who Isn't On Anything"}}}
(ns act3.s5-not-on-anything
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.sketch :as sketch]
            [fedoras.cs-class.slots.slots :as slots]))

^:kindly/hide-code
(sp/assets)

;; # Scene 5 — The Boy Who Isn't On Anything

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 28)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. UNDER MERIDIAN — BETWEEN TWO BAYS — SATURDAY 7 FEBRUARY, 12:34 A.M.")

 (sp/action "A space the width of a person, between two bays, that is not a"
            "space from the front.")

 (sp/action "Four of them in it, and something with hold of {pc}'s coat.")

 (sp/beat "The light goes past.")

 (sp/action "It takes four minutes and nobody moves for any of them.")

 (sp/action "{pc} puts his hand on the hand that has hold of him, because that"
            "is what you do, and it is not warm.")

 (sp/beat "He does not say anything about that.")

 (sp/action "It lets go.")

 (sp/off-screen "A VOICE"
                "Left. Then left.")

 (sp/beat "It comes from about knee height and it is not coming from a person.")

 (sp/dialogue "ELI"
              "Who's that?")

 (sp/off-screen "A VOICE"
                "Left. Then left. Mind the step.")

 (sp/transition "Cut to:")

 (sp/slug "INT. UNDER MERIDIAN — A CORRIDOR — CONTINUOUS")

 (sp/action "The bays end and there is a wall with a door in it, and the door"
            "is a door: hinges, a handle, a frame, and paint that somebody has"
            "kept up.")

 (sp/action "Under the door there is a line of light and it is steady.")

 (sp/beat "Nothing they have seen tonight has been steady.")

 (sp/dialogue "MAYA"
              "That's mains.")

 (sp/dialogue "LENA"
              "Whose mains?")

 (sp/beat "Nobody has an answer and the door is not locked.")

 (sp/transition "Cut to:")

 (sp/slug "INT. UNDER MERIDIAN — THE ROOM WITH THE LIGHT — CONTINUOUS")

 (sp/action "A long room with a bench down one side and a strip light over it"
            "that is on, and the light is white and does not flicker and has"
            "clearly been on for years.")

 (sp/action "On the bench, three machines the size of cats. Tracked. Wheels,"
            "arms, a lens each. None of them is moving.")

 (sp/action "At the end of the bench there is a screen with a board of keys in"
            "front of it.")

 (sp/beat "It is on.")

 (sp/dialogue "ELI"
              "What is that?")

 (sp/dialogue :pc
              "It's a screen and a keyboard.")

 (sp/dialogue "ELI"
              "That's a peripheral.")

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "MAYA"
              "Why is it outside?")

 (sp/dialogue :pc
              "Outside of what?")

 (sp/beat "Maya starts to answer and then does not, because she has worked out"
          "in the middle of the sentence that she was going to say outside of"
          "a person.")

 (sp/action "{pc} has a tower and a screen in his room in Ashcroft and has had"
            "since August, and a quarterback once asked him where the screen"
            "was, and he is the only person in this room who is not looking at"
            "this thing as though it were an animal.")

 (sp/action "He sits down at it, because there is a chair, and the chair is at"
            "the right height, and the chair has been at the right height for"
            "some time.")

 (sp/action "There is one thing on the screen and it is waiting to be played.")

 (sp/dialogue "LENA"
              "Somebody left that on for us.")

 (sp/dialogue "MAYA"
              "Somebody left it on.")

 (sp/dialogue "LENA"
              "That's what I said.")

 (sp/action "{pc} presses the key that plays it.")

 (sp/stage "The recording is below. Part one.")

;; ---

 (art/handout
  "RECORDING · no header · no date · found playing"
  "— a young man, seated, in a room with a strip light. Version four is written on the wall behind him in marker. —"
  "If you're watching this, somebody let you get this far, and that somebody is me, and I'd like that understood at the start."
  "This is the fourth version. The first three were too long. One of them made somebody cry, which was not the intention and was also not useful."
  "I'm going to tell you what the ground under you is. Then I'm going to tell you what the building over you is for. Then you can decide whether you want to meet me, and if you don't, the way out is the way you came in, and nothing will stop you, and I mean nothing rather than nobody."
  "THE GROUND."
  "There was a library here. It was not the university's. It was here first and it was open to anybody who could get to the door."
  "It closed. Nobody burned it and nobody raided it. A committee sat for eleven years and checked everything written before the founding, and what came through is the list, and the list is the one on your instructor's door."
  "What did not come through was not destroyed. Destroying is work. It was left where it was, and the ground was put over it, and the university was built on top, because the site was flat and it was already level."
  "Here is the part I would like you to sit with."
  "They did not move the catalogue."
  "Every cabinet in Beckwith Hall is an index to the room you are standing in. Nobody took the cards out because the cards were not the problem. People put their bags on them."
  "LOCATION: OAKES — withdrawn means it went with the ground."
  "LOCATION: C-3 — held means it is under your feet, and it is still there, and somebody in an office once wrote the word held next to it, and held is an honest word, and it is the only honest word anywhere in the whole system."
  "End of part one. There's a key with an arrow on it.")

;; ---

 (sp/slug "INT. THE ROOM WITH THE LIGHT — CONTINUOUS")

 (sp/action "Nobody says anything for a while.")

 (sp/dialogue "ELI"
              "I've had my bag on one of those.")

 (sp/dialogue "MAYA"
              "Everybody's had their bag on one of those.")

 (sp/action "{pc} is not looking at the screen. He is looking at the three"
            "machines on the bench, which are all facing the same way, and"
            "which have all stopped in a line.")

 (sp/dialogue "LENA"
              "Play the rest of it.")

 (sp/dialogue "ELI"
              "Do we want the rest of it?")

 (sp/dialogue "LENA"
              ["I've come down a hole in the floor at midnight."
               "Yes."])

 (sp/stage "Part two.")

;; ---

 (art/handout
  "RECORDING · part two"
  "THE BUILDING. This is the part people argue with, so I'll go slowly."
  "You've been told this is a university. It admits one thousand and twenty-four every year, and it has admitted one thousand and twenty-four every year for longer than any record I can reach."
  "That isn't a count. A count is what you get when you finish counting. That's a target. Somebody writes it down in September and then the year is arranged until it's true."
  "Review is the arranging. Repossession is the arranging. Placement is the arranging. The docket is the arranging."
  "Nobody is being punished. I want that to be clear, because when people work half of this out they always land on cruelty, and cruelty would be a relief. Nobody down the corridor from you hates you. It's an allocation problem and the allocation is going well."
  "Now ask what it's being arranged FOR."
  "Everything anybody has ever thought is in the knowledge. All of it. Every hour of every person who has ever been saved, which is everybody, and has been everybody for longer than anybody will tell you."
  "A thing that contains everything cannot make anything."
  "It has no new. It can't get new by asking itself, because asking itself is the one operation it has already done. New only ever happens to a person, and only when a person is stuck."
  "So it manufactures stuck."
  "No Helps on these six. The supplement is open by subject and closed by every other subject. Your torch bulb takes a fortnight. Your instructor has one copy and you may not photograph it. Every shortage you have ever stood in was built, and it was built well, and it was built for you."
  "There's a woman upstairs who says that sooner or later something you need will be shut and there will have to be somebody in the room who can open it. She says it every year. She means it. She has no idea what she is for and I would be grateful if you didn't tell her."
  "The docket puts eleven unattached people in a room below the level of the quad, for an hour, once a week, and hands them six questions on a card so that they cannot say anything else."
  "Ask yourself why that room."
  "THE FLOOR is fourteen seasons of a woman at a desk and the programme is warm about her and the warmth is accurate. It isn't propaganda. Propaganda's cheaper. It's a recruitment film that thinks it's a comedy, and it is genuinely funny, and that is why it works."
  "Remote operations is the only part of the whole apparatus that isn't a lie. It's the best thing they do. They do it because attention, with nobody watching, is the most expensive thing there is, and it's the one thing they can't fake and can't take."
  "Repossessed students are placed. That's true, it isn't a euphemism, and it isn't the whole of it. I have had nine years and I have not got the rest of it, and I'd rather say that than make something up for you."
  "That's the end of version four."
  "If you want to argue: left, then left, and mind the step, and don't touch the plants.")

;; ---

 (sp/slug "INT. UNDER MERIDIAN — THE LONG ROOM — 1:05 A.M.")

 (sp/action "Left, and then left, and there is a step, and the step is where"
            "he said it was.")

 (sp/action "Then a room with light in it that is not like light.")

 (sp/action "Eight lamps on a frame over four beds of plants, and the light is"
            "pink and it is very loud, in the way that too much of anything is"
            "loud.")

 (sp/action "Cable comes in through a hole in the brick, in a bundle, wrapped,"
            "dressed properly, and clipped every few feet by somebody who"
            "cared about the clipping.")

 (sp/action "A young man is standing under the lamps with his sleeves rolled"
            "and his hands in a tray.")

 (sp/action "Tall. Narrow. Hair he cuts himself and a beard to match. A Plant"
            "Services coat in a pattern that has not been issued in decades,"
            "wet to the knee. He has been down here a long time and the room"
            "reeks of him and of the plants he tends to.")

 (sp/dialogue "THE YOUNG MAN" "without turning round"
              "Which one of you put the book back?")

 (sp/beat "Nobody answers.")

 (sp/dialogue "THE YOUNG MAN"
              "It wasn't the loud one.")

 (sp/dialogue "ELI"
              "I'm the loud one.")

 (sp/dialogue "THE YOUNG MAN"
              "Yes.")

 (sp/action "He turns round and wipes his hands on the coat.")

 (sp/dialogue "LAZLO"
              "Lazlo.")

 (sp/dialogue "MAYA"
              "Maya.")

 (sp/dialogue "LAZLO"
              ["I know. He said it into a Help four feet from a"
               "grate and the grate is mine."])

 (sp/beat "Something comes out from under the bench at speed.")

 (sp/action "It is the size of a large mouse and it is tracked and it goes up"
            "Eli's leg and into the pocket of his coat.")

 (sp/dialogue "ELI" "very still"
              "There is a thing in my coat.")

 (sp/off-screen "PIP"
                ["Two coins. A pencil. A folded card for a poster"
                 "job on the eighteenth. Nothing sharp."])

 (sp/dialogue "ELI"
              "It talks.")

 (sp/dialogue "LAZLO"
              "Constantly.")

 (sp/dialogue "ELI"
              "What is it?")

 (sp/dialogue "LAZLO"
              ["That's Pip. Pip's mostly 'grown' more than"
               "'made'... buuuuut- -let's not do that conversation"
               "tonight."])

 (sp/off-screen "PIP"
                "He can't do it. He's tried four times.")

 (sp/dialogue "LAZLO"
              "Pip.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE LONG ROOM — CONTINUOUS")

 (sp/dialogue "MAYA"
              "How long have you been down here?")

 (sp/dialogue "LAZLO"
              "Nine years.")

 (sp/action "Four people look at him. He is their age, possibly a year younger"
            "even Lena.")

 (sp/dialogue "LAZLO"
              "Yes. Next question.")

 (sp/dialogue "MAYA"
              "That isn't how that works.")

 (sp/dialogue "LAZLO"
              ["No. Next question anyway, because the answer takes"
               "an hour and I've got until your Helps come back"
               "on."])

 (sp/dialogue "ELI"
              "Why can't you go up?")

 (sp/dialogue "LAZLO"
              "I wouldn't save a second of the world you know.")

 (sp/dialogue "ELI"
              "Why?")

 (sp/dialogue "LAZLO"
              ["There's a day of mine that stopped nine years ago,"
               "mid-sentence, which hasn't got a day after it. If"
               "I walk up that stair it gets one. A nine-year gap"
               "closing is not a thing that happens. It's the"
               "brightest and loudest object on the campus, and"
               "it's got my name on it, and it'd take about four"
               "minutes."])

 (sp/dialogue "MAYA"
              "Can't you take it out?")

 (sp/dialogue "LAZLO"
              ["No. Neither can you. No human that I know of has"
               "ever taken one out of anybody, it would be akin to"
               "removing a childhood."])

 (sp/action "He goes along the bench to the three stopped machines and turns"
            "one of them over.")

 (sp/dialogue "LAZLO"
              ["Rovers. That one's mine, that one's mine, that one"
               "I got from somebody down the tunnel and I've never"
               "got it to do what he said it did."])

 (sp/dialogue :pc
              "Why are they all facing the same way?")

 (sp/beat "Lazlo looks at him properly for the first time.")

 (sp/dialogue "LAZLO"
              ["Because they all stopped at the same mark. Not"
               "near it. On it. Four inches of each other, three"
               "different builds, nine months apart."])

 (sp/dialogue "MAYA"
              "Where's the mark?")

 (sp/dialogue "LAZLO"
              ["South. Past the flooded end. About two hundred"
               "feet, and it isn't a wall and it isn't water and"
               "there is nothing there to look at."])

 (sp/dialogue "LAZLO"
              ["Everything with a chip in it stops there. A person"
               "wouldn't. But every person up there is carrying"
               "one in their head, so a person doesn't either."])

 (sp/beat "Nobody says anything.")

 (sp/dialogue "LAZLO"
              ["That's the whole of my problem and I've had it for"
               "nine years and I've said it out loud four times"
               "and this is the fourth."])

 (sp/transition "Cut to:")

 (sp/slug "INT. THE LONG ROOM — 1:40 A.M.")

 (sp/action "There is a hatch at the far end with a draught coming out of it"
            "and it is not the same air as the room.")

 (sp/dialogue "ELI"
              "What's down there?")

 (sp/dialogue "LAZLO"
              "Somebody like me, about a mile and a half north.")

 (sp/dialogue "ELI"
              "You've got neighbours?")

 (sp/dialogue "LAZLO"
              ["Four that I know of and I've met two. The trouble"
               "isn't the neighbours."])

 (sp/dialogue "MAYA"
              "What's the trouble?")

 (sp/dialogue "LAZLO"
              ["The tunnel isn't ours. We use it. Nobody made it"
               "and nobody keeps it and it isn't hidden from"
               "anything, which is a different thing from being"
               "hidden from people."])

 (sp/beat "He does not elaborate and Pip does not say anything either, which"
          "is the first time all night.")

 (sp/dialogue "LAZLO"
              ["I've lost eleven rovers in that tunnel and I've"
               "got four of them back and two of the four had been"
               "somewhere I didn't send them."])

 (sp/dialogue "LENA"
              "Is any of it Meridian?")

 (sp/dialogue "LAZLO"
              ["Some of it. I don't know how much and I don't know"
               "where the edge is and I have stopped sending"
               "things out to find the edge, because that's how"
               "you tell somebody where you are."])

 (sp/action "H4RPER says something from a speaker on the wall that none of"
            "them can locate.")

 (sp/off-screen "H4RPER"
                "Fifty minutes.")

 (sp/dialogue "LAZLO"
              "I know.")

 (sp/off-screen "H4RPER"
                ["You said to say it at fifty and you said not to"
                 "argue about it at fifty."])

 (sp/dialogue "LAZLO"
              "I know, Harper.")

 (sp/dialogue "MAYA"
              "Is that a Help?")

 (sp/dialogue "LAZLO"
              "No.")

 (sp/dialogue "MAYA"
              "It sounds like a Help.")

 (sp/dialogue "LAZLO"
              ["It sounds like me. It's a net and I grew it off my"
               "own and it has been wrong about things with me for"
               "nine years, which is not what yours does."])

 (sp/dialogue "ELI"
              "What does ours do?")

 (sp/dialogue "LAZLO"
              ["Yours is very good and it is in your head and you"
               "do know that."])

 (sp/beat "Two of them say yes and one of them says nothing and one of them"
          "has never thought about it in that order.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. THE LONG ROOM — 2:10 A.M.")

 (sp/action "{pc} takes the page out of the front of his bag. It has been"
            "folded into eight since August and the folds have gone through in"
            "two places.")

 (sp/dialogue :pc
              "Do you know what this is?")

 (sp/action "Lazlo takes it and opens it and does not look at the printed"
            "side.")

 (sp/beat "He looks at the pencil.")

 (sp/dialogue "LAZLO"
              "I wondered where that went.")

 (sp/beat "Four seconds.")

 (sp/dialogue :pc
              "There's no page 219.")

 (sp/dialogue "LAZLO"
              "No. There isn't.")

 (sp/action "He folds it along the same folds and gives it back.")

 (sp/dialogue "LAZLO"
              ["Right. I want something and I'm going to ask for"
               "it badly, because I have not asked anybody for"
               "anything in nine years and I have got worse at it,"
               "not better."])

 (sp/dialogue "LAZLO"
              ["I want the catalogue. All of it. Every drawer in"
               "every cabinet in that building, photographed, in"
               "order, front and back, because the back is where"
               "the locations are."])

 (sp/dialogue "MAYA"
              "How many cards?")

 (sp/dialogue "LAZLO"
              ["I don't know. That's the point. I've got a room"
               "full of shelves and no way to know what's on them,"
               "and there's a room full of what's on them with"
               "people's coats on it."])

 (sp/dialogue "ELI"
              "And then what?")

 (sp/dialogue "LAZLO"
              ["And then I know what's down here, and then for the"
               "first time in nine years I can look something up."])

 (sp/beat "He says that the way other people say a much bigger word.")

 (sp/dialogue "MAYA"
              "What do we get?")

 (sp/dialogue "LAZLO"
              "Nothing. There isn't a currency down here.")

 (sp/dialogue "MAYA"
              "That's not an answer.")

 (sp/dialogue "LAZLO"
              ["You get the list too. That's all I've got and it's"
               "more than anybody up there has been given since"
               "the founding."])

 (sp/action "PIP comes along the bench and stops about a foot from {pc} and"
            "stays there.")

 (sp/dialogue "LAZLO"
              "Pip.")

 (sp/off-screen "PIP"
                "I know.")

 (sp/dialogue "LAZLO"
              "Do it again.")

 (sp/action "Pip goes to the end of the bench and comes back and stops in the"
            "same place.")

 (sp/beat "Lazlo does not say anything for a while.")

 (sp/dialogue "LAZLO"
              "How long have you been getting the nosebleeds?")

 (sp/beat "Maya looks at {pc} and {pc} does not look at anybody.")

 (sp/dialogue :pc
              "Since August.")

 (sp/dialogue "LAZLO"
              "How long are they now?")

 (sp/dialogue :pc
              "Four minutes. Tonight it was four minutes.")

 (sp/action "Lazlo puts the tray down, which he has been holding since they"
            "came in.")

 (sp/dialogue "LAZLO"
              "Right. Don't come back down here.")

 (sp/dialogue :pc
              "You just asked me for—")

 (sp/dialogue "LAZLO"
              ["I asked you for a catalogue that is on the third"
               "floor of Beckwith Hall in the dry, and I'm telling"
               "you not to come back down here, and both of those"
               "are the same sentence."])

 (sp/dialogue "MAYA"
              "Why?")

 (sp/beat "He does not answer her.")

 (sp/dialogue "LAZLO"
              "Harper. What was that.")

 (sp/off-screen "H4RPER"
                ["I'd want it four more times before I said"
                 "anything, and you know what I'd say."])

 (sp/dialogue "LAZLO"
              "Yes.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE LONG ROOM — 2:40 A.M.")

 (sp/dialogue "LAZLO"
              ["Now the part that keeps you alive, which I'd like"
               "someone to write down, and I'd prefer it to be"
               "her."])

 (sp/action "Maya has the notebook out before he has finished saying it.")

 (sp/dialogue "LAZLO"
              ["One. Grate down properly. Not nearly. If it sits"
               "proud, somebody's foot finds it in April and"
               "there's a work order, and a work order is a date,"
               "and a date is four names."])

 (sp/dialogue "LAZLO"
              ["Two. You didn't take anything. Keep not taking"
               "anything. A missing book is a person. Forty"
               "thousand books nobody has counted is weather."])

 (sp/dialogue "LAZLO"
              ["Three. Wet shoes. Go along the plant corridor and"
               "out the back and stand on the grass for two"
               "minutes before you go anywhere with a floor."])

 (sp/dialogue "LAZLO"
              ["Four, and this is the one that tends to trip"
               "everybody up-. -your Helps come back on about"
               "twenty feet up those stairs. There's a gap in your"
               "day and you cannot close it, nor should you try."])

 (sp/dialogue "ELI"
              "So what do we do about it?")

 (sp/dialogue "LAZLO"
              ["Nothing. Go to bed. A gap with sleep on both ends"
               "of it is a gap where somebody slept. A gap with a"
               "question on the end of it is not."])

 (sp/dialogue "LAZLO"
              ["So you don't ask it. Not what a C series is, not"
               "what's under the refectory, not what happens to a"
               "repossessed student, not tonight, not in March,"
               "not ever. It is very good and it is in your head"
               "and it will answer you, and the answer is not the"
               "thing that costs you."])

 (sp/beat "Maya writes all four down and then, underneath, writes something"
          "that is not one of the four.")

 (sp/dialogue "LAZLO"
              "What was that one?")

 (sp/dialogue "MAYA"
              "The time.")

 (sp/dialogue "LAZLO"
              "Good.")

 (sp/transition "Cut to:")

 (sp/slug "INT. UNDER MERIDIAN — THE STACKS — 3:50 A.M.")

 (sp/action "He walks them back as far as the end of the bays and does not"
            "come out into the row.")

 (sp/dialogue "LAZLO"
              "Beckwith. Front and back. Take your time over it.")

 (sp/dialogue :pc
              "How do I get it to you?")

 (sp/dialogue "LAZLO"
              "Something of mine will be near you.")

 (sp/dialogue "ELI"
              "That is a horrible sentence.")

 (sp/dialogue "LAZLO"
              "Yes.")

 (sp/action "PIP is not on the bench and has not been on the bench for a"
            "while.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "EXT. THE QUAD — 4:10 A.M.")

 (sp/action "Four of them come up out of the plant door at the back of"
            "Meridian Hall into an ordinary cold morning.")

 (sp/action "The quad is the quad. The banner is the banner. A light is on in"
            "the Commons because the Commons never closes.")

 (sp/beat "Nothing has happened to the campus.")

 (sp/action "About twenty feet up the stair their Helps came back on, one"
            "after another, and none of them said anything about it and all"
            "four of them noticed.")

 (sp/action "They stand on the grass for two minutes, which is longer than two"
            "minutes is.")

 (sp/action "{pc} locks the plant door. They walk down to Plant Services and"
            "he puts the keys back on the hook, and HOLLIS is asleep on the"
            "crate exactly as he was at ten, and has not moved, and is"
            "breathing.")

 (sp/dialogue "ELI" "outside, quietly"
              "Is he all right?")

 (sp/dialogue "LENA"
              "He's asleep, Eli.")

 (sp/beat "Nobody says anything on the way back across.")

 (sp/dialogue "ELI" "at the corner of Ashcroft"
              "So we tell people.")

 (sp/dialogue "LENA"
              "Tell them what?")

 (sp/dialogue "ELI"
              ["All of it. Any of it. That there's a room down"
               "there with—"])

 (sp/dialogue "LENA"
              "With shelves in it. And what have you got.")

 (sp/beat "Eli has a photograph on his Help of a doorway, taken in the dark,"
          "at an angle, which could be any doorway anywhere.")

 (sp/dialogue "LENA"
              ["I've done this. Not this. Something like it,"
               "twice. You go to somebody and you tell them, and"
               "they're decent about it, and they write it down"
               "somewhere you can't see, and then the thing you"
               "told them about is a thing you have a history of"
               "saying."])

 (sp/dialogue "ELI"
              "So we do nothing.")

 (sp/dialogue "LENA"
              "I didn't say that.")

 (sp/dialogue "MAYA"
              "We check it.")

 (sp/beat "Everybody looks at her.")

 (sp/dialogue "MAYA"
              ["Everything down there is somebody telling us"
               "something. He told us a great deal and he was"
               "honest about the bits he doesn't know, which is"
               "the only reason I'd give him anything at all, and"
               "it is still somebody telling us something."])

 (sp/dialogue "ELI"
              "So what isn't?")

 (sp/dialogue "MAYA"
              "The thing about him.")

 (sp/dialogue :pc
              "About me.")

 (sp/dialogue "MAYA"
              ["He said manufactured. He said every shortage you"
               "have stood in was built for you. That is a claim"
               "about the work, and the work is a thing we have"
               "got."])

 (sp/dialogue "ELI"
              "And you can test that?")

 (sp/dialogue "MAYA"
              ["You can test anything if you can count it. He"
               "writes a thing. We count what's in it. If there's"
               "something in it he didn't put there, then there's"
               "something in it he didn't put there, and that"
               "isn't a story anybody told us, that's a number."])

 (sp/beat "It is a quarter to five in the morning.")

 (sp/dialogue "MAYA"
              "Do number four.")

 (sp/transition "Cut to:")

;; ---

 (sp/action "Assignment four. A machine that takes money.")

 (sp/action "Three reels, eight positions on a reel, and the same eight"
            "symbols on each of them. Match three and it pays. That is the"
            "whole of what he sat down to write and he wrote it in about forty"
            "minutes without stopping, in the bench room, with three people"
            "watching him do it.")

 (def reel
   [:cherry :bell :bar :seven :plum :bell :cherry :bar])

 (defn pick [] (rand-nth reel))

 (defn weight [sym]
   (/ (count (filter #{sym} reel)) (count reel)))

 (defn next-along [sym]
   (nth reel (mod (inc (.indexOf reel sym)) (count reel))))

 (defn spin []
   (let [a (pick)
         b (pick)]
     [a b (if (= a b)
            (if (< (rand) (weight a)) a (next-along a))
            (pick))]))

 (spin)

 (sp/action "It works. It pays about one time in twenty, which is about right,"
            "and he handed it in on the Monday and it was marked and it was"
            "fine.")

 (sp/action "Then Maya put her finger on the middle of the screen and asked"
            "him what those three lines were for.")

 (sp/action "The first two reels are picked. The third one is not. If the"
            "first two have come up the same, the third reel is given the"
            "winning symbol as often as it would have come up on its own, and"
            "every other time it is given the next symbol along.")

 (sp/action "It pays exactly as often as an honest machine would pay. It also"
            "stops, over and over, one position short of paying.")

 (defn wins? [[a b c]] (= a b c))

 (defn one-off? [[a b c]]
   (and (= a b) (= c (next-along a))))

 (defn honest-spin []
   [(pick) (pick) (pick)])

 (defn measure [spinner n]
   (let [rows (repeatedly n spinner)]
     {:pays    (double (/ (count (filter wins? rows)) n))
      :one-off (double (/ (count (filter one-off? rows)) n))}))

 (measure honest-spin 200000)

 (measure spin 200000)

 (sp/action "Same money. Four times as many nearly.")

 (sp/action "He did not decide to do that. He does not remember typing it. He"
            "can read it and he can explain it and he can tell you exactly"
            "what it is doing and why it is a good idea, which is the part"
            "that goes through him like cold water, because he can explain it"
            "and he did not think of it.")

;; ---

 (sp/slug "INT. THE BENCH ROOM — 6:20 A.M.")

 (sp/dialogue "ELI"
              ["All right, but you've done four months of this."
               "You've read other people's. You picked it up"
               "somewhere and forgot where. That's what a person"
               "is."])

 (sp/dialogue "MAYA"
              "Yes. So I'll do one.")

 (sp/dialogue :pc
              "You've never written anything.")

 (sp/dialogue "MAYA"
              ["I have written nothing at all, and I have watched"
               "you do it for four months, and I have the sheet,"
               "and I am not going to look at yours."])

 (sp/action "She turns the screen away from him. LENA sits on the counter and"
            "does not say anything. ELI goes and gets coffee from the machine"
            "in the corridor, four times, over the next two hours.")

 (sp/action "It takes her two hours and does not run the first three times.")

 (sp/beat "Then it runs.")

 (sp/dialogue "MAYA"
              "Right. Count mine.")

 (sp/transition "Cut to:")

;; ---

 (sp/action "Hers is not like his. Her names are different, her reel is"
            "different, she has done the whole of it with numbers where he"
            "used words, and one part of it is genuinely better than his and"
            "he says so.")

 (def her-reel [0 1 2 3 0 1 4 5])

 (defn her-spin []
   (let [a (rand-nth her-reel)
         b (rand-nth her-reel)
         third (if (= a b)
                 (let [share (/ (count (filter #{a} her-reel)) (count her-reel))]
                   (if (< (rand) share)
                     a
                     (nth her-reel (mod (inc (.indexOf her-reel a)) (count her-reel)))))
                 (rand-nth her-reel))]
     [a b third]))

 (defn her-one-off? [[a b c]]
   (and (= a b)
        (= c (nth her-reel (mod (inc (.indexOf her-reel a)) (count her-reel))))))

 (let [rows (repeatedly 200000 her-spin)]
   {:pays    (double (/ (count (filter wins? rows)) 200000))
    :one-off (double (/ (count (filter her-one-off? rows)) 200000))})

 (sp/action "Same shape. Different words, different numbers, different order,"
            "and in the middle of it the same three lines doing the same"
            "thing, written by somebody who has never written anything before,"
            "in a room with the screen turned away.")

 (sp/action "Nobody says anything for a while. Eli is standing in the doorway"
            "with the fourth coffee and has been for some time.")

 (sketch/page
  {:id "slots-orig" :label "SLOTS.CLJS" :rows 26}
  "(def reel [:cherry :bell :bar :seven :plum :bell :cherry :bar])
(def tally (atom {:spins 0 :pays 0 :one-off 0}))

(defn pick [] (rand-nth reel))
(defn share [s] (/ (count (filter #{s} reel)) (count reel)))
(defn next-along [s] (nth reel (mod (inc (.indexOf reel s)) (count reel))))

;; take the three lines out and see what changes and what doesn't
(defn spin []
  (let [a (pick) b (pick)]
    [a b (if (= a b)
           (if (< (rand) (share a)) a (next-along a))
           (pick))]))

(defn show [row]
  (html!
    (str \"<p style='font-size:1.6rem;letter-spacing:0.3em'>\"
         (apply str (map (fn [s] (subs (name s) 0 2)) row))
         \"</p><p>\"
         (let [{:keys [spins pays one-off]} @tally]
           (str spins \" spins · \" pays \" paid · \" one-off \" one short\"))
         \"</p><div class='live-bar'><button id='go'>Spin</button>\"
         \"<button id='hundred'>Spin 100</button></div>\"))
  (on! \"go\" (fn [] (let [r (spin)]
                     (swap! tally update :spins inc)
                     (when (apply = r) (swap! tally update :pays inc))
                     (when (and (= (first r) (second r)) (not= (nth r 2) (first r)))
                       (swap! tally update :one-off inc))
                     (show r))))
  (on! \"hundred\" (fn []
                   (dotimes [_ 100]
                     (let [r (spin)]
                       (swap! tally update :spins inc)
                       (when (apply = r) (swap! tally update :pays inc))
                       (when (and (= (first r) (second r)) (not= (nth r 2) (first r)))
                         (swap! tally update :one-off inc))))
                   (show (spin)))))

(show [:bar :bar :seven])")

;; ---

 (slots/widget
  {:id "slots" :label "SLOTS.CLJS · spin it" :settings-rows 26})

 (art/email
  {:from "Office of the Registrar <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "Saturday 7 February, 4:00 AM"
   :subject "Service assignment — variation processed"}
  "Your service assignment has been varied with effect from Monday."
  "PLANT SERVICES — closed."
  "SYSTEMS — commences Monday, 9:00 AM, Sterling Center, fourth floor. This post is paid."
  "Thank you for confirming."
  "Service hours are not a component of review. Service hours are recorded.")
 
 (sp/action "Four in the morning is when the Registrar sends things. It was"
            "sent at four in the morning on the Saturday, which was while the"
            "four of them were under the refectory with their Helps out, and"
            "he has not confirmed anything, and there is nobody he can say"
            "that to.")

 (sp/action "He reads it four times looking for the sentence that is a"
            "problem.")

 (sp/action "There isn't one.")

 (sp/action "He is in bed by seven, which is what he was told to do, four"
            "hours later than he was told to do it."))