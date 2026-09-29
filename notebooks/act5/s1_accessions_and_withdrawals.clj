^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 1 — Accessions and Withdrawals"}}}
(ns act5.s1-accessions-and-withdrawals
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.line.line :as line]))

^:kindly/hide-code
(sp/assets)

;; # Scene 1 — Accessions and Withdrawals

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 4)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. MERIDIAN HALL — CS 101 — MONDAY 4 MAY")

 (sp/action "The first week of finals. The room is quiet in the way a"
            "room is quiet when everybody in it has slept four hours.")

 (sp/action "The board behind REYES has the six assignments they have"
            "already done, in her handwriting, each with a line through"
            "it. There is nothing written under the sixth.")

 (sp/dialogue "REYES"
              ["For the last assignment of the year, I want you to make"
               "something new."])

 (sp/action "Nobody reacts. Some of them write it down. Some of them"
            "are still asleep.")

 (sp/dialogue "REYES"
              ["New to the list, I mean. Whether it's new to you is your"
               "own business. Something that has never been approved"
               "because nobody has ever thought to consider it. Something"
               "you made. Something that works. Something you can explain."])

 (sp/dialogue "A STUDENT"
              ["What happens if we make something unapproved by accident?"])

 (sp/dialogue "REYES"
              ["Then you'll be reassociated. Twice, unless the assessment"
               "says three. You will not be repossessed. That's the"
               "amnesty. It covers this assignment and nothing else."])

 (sp/beat "Nobody laughs.")

 (sp/dialogue "REYES"
              ["There's no trick in it. It's the actual arrangement. The"
               "institution prefers students who try. Some of you will"
               "fail. Some of you will make something nobody wants. Some"
               "of you will make something the institution has never seen"
               "and cannot approve. All of you will walk out of this room"
               "at the end of it."])

 (sp/action "She looks at {pc} for a moment longer than she looks at"
            "anyone else.")

 (sp/dialogue "REYES"
              ["You present in three weeks. Don't use a Help to design it."
               "You may use a Help to build it. If you don't know the"
               "difference, you're about to learn."])

 (sp/stage "The sheet she hands round is below.")

;; ---

 ^:kindly/hide-code
 (art/handout
  "ASSIGNMENT SEVEN · CS 101 · presented in the third week"
  "Make something new."
  "New means new to the list. What it means to you is your own affair."
  "It must work, and you must be able to explain it, to me, in the room, out loud."
  "Design it without a Help. Build it with any Help you like."
  "The amnesty covers this assignment and no other. An unapproved result is reassociated twice, or three times if the assessment says so."
  "Nobody is repossessed for assignment seven.")

;; ---

 (sp/slug "INT. PRENTISS SCIENCE CENTER — TEACHING LAB — TUESDAY 5 MAY")

 (sp/action "The screen is down. A new Meridian video. It is shorter"
            "than the others, and it is about the misters.")

 (sp/sign "MERIDIAN SYSTEMS"
          "THE MISTERS: A PROMISE KEPT")

 (sp/off-screen "NARRATOR"
                ["Every day, on every approved campus, the misters run. They"
                 "deliver nutrition. They deliver humidity. They deliver a"
                 "quality of air that has been calibrated to the needs of"
                 "every approved space."])

 (sp/action "On screen, a corridor in a fine mist. A man in a white"
            "suit walks through it. He does not get wet. He does not"
            "acknowledge the mist, or anything else.")

 (sp/off-screen "NARRATOR"
                ["The misters are a Meridian innovation. They are maintained"
                 "by Meridian. They are scheduled by Meridian. They are, in"
                 "every way that matters, a Meridian system."])

 (sp/action "The man stops and looks at the camera for one second too"
            "long.")

 (sp/off-screen "NARRATOR" "Meridian. Here for you.*")

 (sp/action "*Meridian holds no legal responsibility for the"
            "consequences of approved atmospheric exposure.")

 (sp/dialogue "A STUDENT" "Why do they keep showing us these?")

 (sp/dialogue "CARTER"
              ["Because the misters will run in every building you work in"
               "for the rest of your life. You should know what they do."])

 (sp/dialogue "A STUDENT" "What do they do?")

 (sp/dialogue "CARTER"
              ["They keep the air clean. They keep the humidity in range."
               "They deliver a small amount of nutrition to anyone who"
               "breathes them long enough."])

 (sp/dialogue "A STUDENT" "Is that approved?")

 (sp/action "Carter does not look at the student. She looks at the"
            "screen, which has gone dark.")

 (sp/dialogue "CARTER"
              ["It's on the schedule. It's on the approved list. It's been"
               "in the environmental plan for twenty years. So yes. It's"
               "approved."])

 (sp/action "At the quarter hour the mister over the door comes on."
            "Everybody in the room goes on breathing.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. HALLORAN UNION — ROOM 3C — WEDNESDAY 6 MAY, 7:00 P.M.")

 (sp/action "Finals week. There is no session. Article Six says the"
            "Society meets on Wednesdays and does not say what for, so"
            "they meet and revise.")

 (sp/action "DEREK has a stack of index cards on Roman provincial"
            "administration. SIMON has a textbook he has not opened."
            "MILES is in the mask, going through a past paper in red."
            "VICTOR has his Help face up on the table in front of him,"
            "with MOCHI on it.")

 (sp/action "{pc} comes in at ten past. Mochi sees him before anybody"
            "else does.")

 (sp/dialogue "MOCHI"
              ["♪ {pc}! Hello! Hello! You're late, but it's fine, I kept"
               "your chair! ♪"])

 (sp/action "Nobody kept his chair. It is the chair he always has.")

 (sp/dialogue "VICTOR"
              ["She has asked after you every Wednesday since Easter."])

 (sp/dialogue "DEREK" "She likes him.")

 (sp/dialogue "VICTOR"
              ["She is a Help. She likes everybody. That is the function."])

 (sp/dialogue "MOCHI" "♪ I like {pc} best! ♪")

 (sp/action "Victor turns the Help face down on the table and goes on"
            "with what he was saying.")

 (sp/dialogue "VICTOR"
              ["Two dates for the minutes. The last film of the year is"
               "Friday the twenty-second. The last session is Wednesday"
               "the twenty-seventh. Attendance at both is required under"
               "Article Six, and I will not be setting Article Seven aside"
               "for either."])

 (sp/action "Miles writes both dates down in red, because red is what"
            "he has in his hand.")

 (sp/dialogue "SIMON" "What's the film?")

 (sp/dialogue "VICTOR" "It's approved.")

 (sp/beat "That is the whole answer.")

 (sp/action "Later, while Derek and Miles argue about whether a"
            "quaestor counts as a magistrate, Victor turns the Help"
            "back over and goes through everything Mochi has said since"
            "Easter, with his thumb, the way other people go through"
            "their post. Nobody at the table remarks on it. He does it"
            "every week.")

 (sp/action "From the Help, very quietly, as if nobody else can hear:")

 (sp/dialogue "MOCHI" "♪ Hi, {pc}. ♪")

 (sp/action "Victor scrolls past it.")

 (sp/action "They stop at ten, because it is finals week.")

 (sp/transition "Cut to:"))

;; ---

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. MAYA'S ROOM — WEDNESDAY 6 MAY — NIGHT")

 (sp/action "MAYA is at her desk. LENA is on the bed. ELI is sitting on"
            "the floor with his back against the wall. {pc} is at the"
            "window.")

 (sp/action "There is a book on the desk, face down, the one Maya has"
            "had since the darkroom.")

 (sp/action "LAZLO's voice comes through a small speaker on the desk,"
            "patched in from wherever he is.")

 (sp/dialogue "LAZLO"
              ["I've been going through the logs since you came back."])

 (sp/transition "Cut to:")

 (sp/slug "INT. UNDER MERIDIAN — LAZLO'S ROOM — LATE")

 (sp/action "A drum of line on the floor with about nine hundred feet on it"
            "and a hand-written figure on the side that has been crossed out"
            "four times.")

 (sp/dialogue :pc
              "Why is it on a wire at all?")

 (sp/dialogue "LAZLO"
              ["Because I am not putting a transmitter under two"
               "hundred feet of fill and hoping. A wire is a wire."
               "It carries the picture up and it carries the power"
               "down and it is the only part of the arrangement"
               "that has never once failed."])

 (sp/dialogue :pc
              "So what fails?")

 (sp/dialogue "LAZLO"
              ["It fouls. It goes somewhere, and then it goes"
               "somewhere else, and at some point the way back is"
               "across a piece of line it has already put down,"
               "and it will not cross its own line and it is right"
               "not to."])

 (sp/dialogue :pc
              "What happens then?")

 (sp/dialogue "LAZLO"
              ["Then it is a hundred and eighty feet away and it"
               "has stopped, and I have a drum with a rover on the"
               "end of it, and either I go and get it or I cut the"
               "line and it is down there."])

 (sp/beat "He does not say how many he has cut.")

 (sp/off-screen "PIP"
                "Eleven.")

 (sp/dialogue "LAZLO"
              "Pip.")

 (sp/transition "Blackout.")

 (sp/slug "INT. LAZLO'S ROOM — TWENTY MINUTES LATER")

 (sp/action "Lazlo has been at it for twenty minutes and has not spoken and"
            "has fouled four of them.")

 (sp/off-screen "PIP"
                "Nine.")

 (sp/dialogue "LAZLO"
              "I know what it is.")

 (sp/off-screen "PIP"
                ["You are at nine retrieved. I got fourteen while"
                 "you were arguing about the drum."])

 (sp/dialogue "LAZLO"
              "You are a rover.")

 (sp/off-screen "PIP"
                "Yes.")

 (sp/beat "He plays another one.")

 (sp/dialogue "LAZLO"
              ["The thing about it is that it is exactly right and"
               "it is no help at all. There is nothing in there I"
               "did not know. I have known all of that for nine"
               "years with a drum in my hands."])

 (sp/dialogue :pc
              "So why are you still playing it?")

 (sp/beat "Four seconds.")

 (sp/dialogue "LAZLO"
              ["Because down here I only get one go at it, and in"
               "there I can foul it and press a key."])

 (sp/action "He plays another one. He gets to eleven and fouls it in the"
            "middle of the board with most of the room still open, which is"
            "how they all go.")

 ^:kindly/hide-code
 (line/widget))