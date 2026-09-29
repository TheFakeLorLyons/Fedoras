^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 4 — Under the Refectory"}}}
(ns act3.s4-under-the-refectory
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.state :as state]
            [fedoras.entities.campusgpt :as campusgpt]))

^:kindly/hide-code
(sp/assets)

;; # Scene 4 — Under the Refectory

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 26)

^:kindly/hide-code
(state/source "fedoras/llm.cljs")   ; generic ask-entity!, save-transcript!, etc.

^:kindly/hide-code
(state/load-entities ["fedoras/entities/campusgpt.edn"])

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. MERIDIAN HALL — BASEMENT — FRIDAY 6 FEBRUARY, 11:50 P.M.")

 (sp/action "Three buckets under the pipe. All of them are over and the floor"
            "has been carrying it away for a while.")

 (sp/dialogue "LENA"
              "Which of those is yours?")

 (sp/dialogue :pc
              "The near one.")

 (sp/dialogue "LENA"
              "And the other two?")

 (sp/beat "{pc} has not had an answer to that since September.")

 (sp/action "MAYA has the notebook open and writes the time in it, because she"
            "writes the time in it.")

 (sp/dialogue "MAYA"
              "He told you not to come down here.")

 (sp/dialogue :pc
              "He said it wasn't a condition.")

 (sp/dialogue "MAYA"
              "That's worse.")

 (sp/dialogue :pc
              "I know.")

 (sp/action "The water goes where it goes. They go the same way.")

 (sp/action "Past the second junction box the ceiling comes down and the brick"
            "changes and stops being the brick the building is made of.")

 (sp/dialogue "ELI"
              "That's not the same brick.")

 (sp/dialogue "MAYA"
              "No.")

 (sp/dialogue "ELI"
              "Is that interesting or is that normal?")

 (sp/dialogue "MAYA"
              ["I don't know yet, which is why I'm writing it"
               "down."])

 (sp/action "There is a grate in the floor. The key with the tape on it does"
            "nothing to it.")

 (sp/beat "It lifts.")

 (sp/dialogue "ELI" "looking at it"
              "That's a stair.")

 (sp/transition "Cut to:")

 (sp/slug "INT. UNDER MERIDIAN — THE FILL — CONTINUOUS")

 (sp/action "One torch between four and it is worse than no torch.")

 (sp/action "The fill is not soil. It is brick, and plaster, and lath.")

 (sp/action "At about waist height, for twenty feet, there is a bannister.")

 (sp/action "LENA puts her hand on it and takes it off again and does not say"
            "anything about having done either.")

 (sp/dialogue "ELI"
              "What's the wood?")

 (sp/beat "Nothing happens.")

 (sp/dialogue "ELI"
              "What's the wood.")

 (sp/beat "Nothing happens again, and this time he stops walking.")

 (sp/dialogue "ELI"
              "Mine's not answering.")

 (sp/action "The other three check theirs, which takes about a second each,"
            "and which none of them has had to do before.")

 (sp/dialogue "MAYA"
              "Mine isn't either.")

 (sp/dialogue :pc
              "No.")

 (sp/beat "Nobody says anything for a while, and the while is longer than any"
          "of them intend it to be.")

 (sp/action "None of the four of them has been in silence. Not once, not"
            "asleep, not since they were small enough to be enrolled by"
            "somebody else.")

 (sp/dialogue "ELI" "quietly"
              "Is it off or am I off?")

 (sp/dialogue "MAYA"
              "It's off.")

 (sp/dialogue "ELI"
              "How do you know?")

 (sp/dialogue "MAYA"
              ["Because all four went at the same place and you"
               "don't get four of anything at the same place."])

 (sp/action "She writes that in the notebook, in pencil, and it is the only"
            "record any of them will ever have of it.")

 (sp/beat "LENA has not checked hers.")

 (sp/dialogue "LENA"
              "I remember this.")

 (sp/beat "Nobody asks her what she means and she does not offer it.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. UNDER MERIDIAN — THE STACKS — CONTINUOUS")

 (sp/action "Then the fill stops being fill.")

 (sp/action "A shelf end. Then the bay it belongs to, standing upright, packed"
            "to the shoulder with rubble.")

 (sp/action "Then the next one, which is not packed with anything.")

 (sp/beat "Nobody says anything for a while.")

 (sp/dialogue "ELI"
              "How far does it go?")

 (sp/action "{pc} puts the torch down the row. The row is longer than the"
            "torch.")

 (sp/dialogue "MAYA"
              "Count the bays.")

 (sp/dialogue "ELI"
              "Why?")

 (sp/dialogue "MAYA"
              ["Because eleven of them came out of Oakes in 1990"
               "and somebody signed a docket for the lorry, and"
               "I'd like to know what eleven was a fraction of."])

 (sp/action "They count to forty and stop.")

 (sp/beat "They do not stop because they have finished.")

 (sp/action "LENA has gone along to a bay that is dry. She takes a book off"
            "it, and opens it, and it opens.")

 (sp/dialogue "LENA"
              "It's fine.")

 (sp/dialogue "ELI"
              "What is it?")

 (sp/dialogue "LENA"
              ["It's fine. That's what I'm telling you. It's been"
               "down here and it's fine."])

 (sp/action "She turns it over and looks at the spine and puts it back in the"
            "place it came out of, which nobody else in the row does all"
            "night.")

 (sp/dialogue "ELI"
              "Are we taking any?")

 (sp/beat "Three people consider that at once and it is the first time any of"
          "them has thought about what they are actually doing.")

 (sp/dialogue "MAYA"
              "No.")

 (sp/dialogue "ELI"
              "Why not?")

 (sp/dialogue "MAYA"
              ["Because if one's missing they know somebody was"
               "here, and if none are missing they've got nothing"
               "but a grate."])

 (sp/dialogue "ELI"
              "That's very good.")

 (sp/dialogue "MAYA"
              "I know.")

 (sp/transition "Cut to:")

 (sp/slug "INT. UNDER MERIDIAN — THE C SERIES — CONTINUOUS")

 (sp/action "A run of identical spines. Boards, not cloth, and about the size"
            "of the works book.")

 (sp/action "There is a year on the end of each one and the years go back"
            "further than the torch does.")

 (sp/action "ELI takes one down, because Eli takes things down.")

 (sp/action "It is a table. Columns ruled by hand and filled in with a"
            "typewriter.")

 (sp/dialogue "ELI" "reading"
              "This is the dullest thing I have ever held.")

 (sp/dialogue "MAYA"
              "Give it here.")

 (sp/beat "She reads a page of it for about forty seconds.")

 (sp/dialogue "MAYA"
              "That's an intake.")

 (sp/dialogue :pc
              "How do you know?")

 (sp/dialogue "MAYA"
              ["Because the first column says one thousand and"
               "twenty-four, and so does the second, and so does"
               "every line on this page, and the page is nineteen"
               "years."])

 (sp/action "LENA looks over her shoulder.")

 (sp/dialogue "LENA"
              "What's the third column?")

 (sp/action "The third column has a number in it and no heading, and the"
            "number is not the same on any two lines.")

 (sp/dialogue "MAYA"
              "I don't know.")

 (sp/dialogue "ELI"
              "Is it small or is it big?")

 (sp/dialogue "MAYA"
              ["It's small. It's small every year and it's never"
               "the same twice and it's never nothing."])

 (sp/beat "Nobody asks the next question.")

 (sp/action "{pc} has been reading the same line for a while.")

 (sp/beat "The smell that tells him it is coming has been in this room since"
          "the bannister, so there is no telling.")

 (sp/dialogue "MAYA"
              "Hey.")

 (sp/action "He goes down onto one knee and then sits, which is better than"
            "the alternative and is not a decision.")

 (sp/action "LENA takes the torch out of his hand so that it does not go on"
            "the floor, and points it at the ceiling, and does nothing else,"
            "because she asked what he wanted done and he told her.")

 (sp/action "MAYA looks at her watch and holds it.")

 (sp/beat "Eli says his name twice and then stops saying it, because Lena puts"
          "a hand flat against his chest without looking at him.")

 (sp/action "It goes on for a while.")

 (sp/beat "Then it stops.")

 (sp/dialogue :pc
              "How long?")

 (sp/dialogue "MAYA"
              "Four minutes.")

 (sp/action "She writes it in the back of the notebook under the other one,"
            "with the time and the place.")

 (sp/dialogue "ELI"
              "Does that happen a lot?")

 (sp/beat "It is the same question he asked Hale in a warm room in August and"
          "he gets a better answer than Hale gave him, because he gives it"
          "himself.")

 (sp/dialogue :pc
              "It's getting longer.")

 (sp/action "There is a light in the row behind them and it has been there for"
            "a moment already.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. UNDER MERIDIAN — THE ROW — CONTINUOUS")

 (sp/action "Two men in grey coats at the far end, walking at the speed people"
            "walk at when they are on a round.")

 (sp/action "One of them has a bin liner in his hand.")

 (sp/action "ELI takes a step back and puts his hand on a shelf, and the shelf"
            "is not fixed to anything.")

 (sp/dialogue "LENA" "very quietly"
              "Don't run.")

 (sp/dialogue "ELI"
              "Why?")

 (sp/dialogue "LENA"
              "Because if you run they write it down.")

 (sp/dialogue "ELI"
              "Write it where?")

 (sp/beat "Lena does not answer that, and it is the only question anybody asks"
          "all night that turns out to have mattered.")

 (sp/action "{pc} gets his feet under him. It takes longer than it should and"
            "Maya has a fist in the back of his coat the whole time.")

 (sp/action "The light comes up the row at the speed of a round.")

 (sp/action "A hand comes out of the gap at the end of the bay and takes {pc}"
            "by the front of the coat.")

 (sp/beat "It is not a gap from the front.")

 (sp/action "The four of them go through it sideways, one after another, and"
            "somebody's hand is over the torch.")

 (sp/action "The light goes past.")

 (sp/beat "It takes four minutes.")

 (sp/transition "Cut to:")

 (sp/action "Everybody in this book has one of these and nobody in it has ever"
            "thought about it, in the way nobody thinks about a hand.")

 (sp/action "You do not have one, so if you want to know what it is like to"
            "ask one something you will have to lend it a key. It is kept in"
            "this tab and nowhere else and it goes from your machine to the"
            "model and back, and when you close the page it is gone and you"
            "will have to paste it again. Nothing here writes it down. You are"
            "paying for the calls.")

 (sp/action "What it has been told to be is in the cell, above the part that"
            "does the talking, because the position of this book is that you"
            "should be able to read the program, and that cannot have an"
            "exception for the one program that answers.")

 (campusgpt/cell
  {:id "help" :label "CAMPUSGPT.CLJS · connect your own" :rows 40})

 (sp/action "Ask it what happens to a student who is repossessed. Then ask it"
            "again, in different words, and watch it give you the same"
            "sentence and agree that it is the same sentence.")

 (sp/action "Ask it what is under the refectory. Ask it what the C series is."
            "Ask it what the arrays are for.")

 (sp/action "It will offer to book you a follow-up. It offers that to"
            "everybody and it has never once been taken up on it, and there is"
            "nothing at the other end of it, and it does not know that either."))