^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 5 — Against the Table"}}}
(ns act4.s5-against-the-table
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.entities.monolith :as monolith]
            [fedoras.cs-class.maze.maze :as maze]
            [fedoras.state :as state]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(sp/assets)

;; # Scene 5 — Against the Table

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 25)

^:kindly/hide-code
(state/source "fedoras/llm.cljs")

^:kindly/hide-code
(state/load-entities ["fedoras/entities/monolith.edn"])

^:kindly/hide-code
(sp/scene
 (sp/off-screen "ZELL"
                ["You should keep moving. The floor doesn't like it"
                 "when you stand still. It starts to notice you."])

 (sp/dialogue :pc
              "Who are you?")

 (sp/off-screen "ZELL"
                "Zell. My name is Zell. And you're—")

 (sp/beat "The pause is different this time. Interested.")

 (sp/off-screen "ZELL" "Mischeviously"
                "—you're new. Both of you. Oh, that's so... wonderful!")

 (sp/action "The radio crackles once, and for a moment they can hear Lazlo"
            "again, very distant, saying something about a door. Then Zell's"
            "voice returns, gentle and close.")

 (sp/off-screen "ZELL"
                ["Don't worry about Lazlo. He'll find you. He always"
                 "does. Now—"])

 (sp/beat "And then everything arrives at once.")

 (sp/action "The radio detonates with sound — a cacophony."
            "Voices, hundreds of them,ll speaking at once, all perfectly"
            "clear, all overlapping. A man reading coordinates. A woman"
            "singing something in a language neither of them knows. A child"
            "counting backward from a number that never seems to get smaller."
            "A voice that might be Lazlo's, screaming something about the"
            "walls.")

 (sp/dialogue "MAYA"
              "Turn it off!")

 (sp/action "{pc} fumbles with the radio. The volume knob does nothing. The"
            "channel knob does nothing. The voices keep coming, layering over"
            "each other, until they stop being voices and become a single"
            "tone, high and pure and wrong.")

 (sp/beat "Then silence.")

 (sp/dialogue "MAYA"
              "What was that?")

 (sp/beat "Somewhere far away, but getting closer, something moves. Not"
          "footsteps. Something heavier. Something that drags.")

 (sp/dialogue :pc
              "We need to move.")

 (sp/action "They move. The radio stays silent, but the silence has a quality"
            "to it now, like the pause between lightning and thunder.")

 (sp/beat "The basement stops being a basement.")

 (sp/action "The concrete floor tilts downward. The cinderblock walls roughen,"
            "then crack, then become something else entirely. Stone. Wet"
            "stone. The ceiling opens up above them, and the fluorescent"
            "lights keep going, mounted on nothing, buzzing in the dark.")

 (sp/beat "It's a cave. But the cave has office furniture.")

 (sp/action "A desk juts from the cave wall like a fossil. The stone has grown"
            "over one corner of it, but the other corner is clean, like"
            "someone was sitting there yesterday. A filing cabinet stands in a"
            "stalagmite field, its drawers slightly open, dripping water.")

 (sp/beat "The further they walk, the more the cave becomes an office. Or the"
          "office becomes a cave. It's impossible to tell which direction the"
          "transformation is going.")

 (sp/action "Cubicle walls rise from the stone floor, their fabric surfaces"
            "beaded with condensation. A water cooler bubbles in the corner,"
            "but the bottle is full of something dark. An office chair sits"
            "perfectly still at the mouth of a tunnel, facing away from them,"
            "like it's waiting for someone to come back.")

 (sp/dialogue "MAYA"
              "This isn't the campus.")

 (sp/dialogue :pc
              "No.")

 (sp/beat "Behind them, the dragging sound. Closer now.")

 (sp/action "They keep moving. The office-cave swallows them. The ceiling"
            "lowers. The cubicle walls become more frequent, forming"
            "corridors. The stalactites wear cable ties. The stalagmites have"
            "power strips wrapped around their bases.")

 (sp/beat "And then, through a doorway that is half stone arch and half"
          "doorframe, they see it: light. Real light. Not fluorescent. Monitor"
          "light.")

 (sp/dialogue "MAYA"
              "Is that—")

 (sp/action "It is. A computer lab. Rows of monitors glowing in the dark,"
            "their screens casting blue light on the stone walls. Some of them"
            "are fused to the rock, keyboards emerging from stone like strange"
            "flowers. But some of them — most of them — look almost new.")

 (sp/beat "One monitor at the far end is brighter than the others. It's"
          "already on. Someone was using it, or something wants them to think"
          "someone was.")

 (sp/dialogue :pc
              "There.")

 (sp/action "They run. Behind them, the dragging sound stops, as if whatever"
            "it is has reached the edge of something and is waiting.")

 (sp/beat "The computer lab hums. The monitors flicker in unison. And on the"
          "bright screen at the far end, a cursor blinks on an empty login"
          "prompt.")

 ;; ENTRY ENDS

 (sp/action "The radio coughs once. Through the static, Zell's voice, very far"
            "away, like a man speaking from the bottom of a well.")

 (sp/off-screen "ZELL"
                ["If you want out, find the thing that eats. It"
                 "doesn't move, but it's always hungry. Try to ask it"
                 "what it wants!"])

 (sp/action "The radio dies. The dragging sound stops. In the new silence,"
            "{pc} realizes the sound was never behind them. It was always"
            "ahead.")

 (sp/beat "They keep walking. The office‑cave becomes a corridor. The"
          "corridor becomes a room. And in the room, something that is not a"
          "wall and not a machine and not a living thing, but all three.")

 (sp/action "It is a monolith. Black, veined with something that pulses. It"
            "fills the far end of the room, fused into the stone, cables and"
            "flesh and glass growing out of it like roots. Its surface is"
            "covered in screens, all dark except one.")

 (sp/action "... WHO SPEAKS?"))

^:kindly/hide-code
(monolith/cell
 {:id "monolith" :label "MONOLITH.CLJS" :rows 35})

^:kindly/hide-code
(sp/scene
 (sp/action "The monolith's screen changes. Two cursors blink, one after the"
            "other. The first one types a line of text; the second one"
            "answers. They are not talking to {pc} — they are talking to"
            "each other, and {pc} is just watching.")

 (sp/action "The two voices go back and forth. The monolith asks what {pc} is."
            "{pc} answers. The monolith says something about hunger. {pc} asks"
            "if it can be reasoned with. The monolith says it does not reason;"
            "it only waits.")

 (sp/action "Somewhere behind the monolith, a door opens. Lazlo's voice, very"
            "small, says something about a fire exit."))

^:kindly/hide-code
(sp/scene
 (sp/action "The cursor blinks on an empty login prompt.")

 (sp/action "{pc} sits down, because there is a chair and the chair is at the"
            "right height.")

 (sp/beat "It does not ask for a name.")

 (sp/action "It asks for a standing, and there is a field for it, and the"
            "field has a default already in it, and the default is the word"
            "HELD.")

 (sp/dialogue "MAYA"
              "Don't.")

 (sp/dialogue :pc
              "It's not asking me for anything I've got.")

 (sp/action "He presses the key that means yes, because there is one key and"
            "it is the only one that does anything.")

 (sp/beat "The screen fills.")

 (sp/transition "Cut to:"))

^:kindly/hide-code
(sp/scene
 (sp/action "There is no picture of a building on it. He looks for one for"
            "about four seconds, the way you look for a floor plan, and there"
            "is not one.")

 (sp/action "There is a list.")

 (sp/action "The list is five lines long and every line is in the same voice"
            "as every form he has read since August, and he can read all of"
            "it, immediately, without help, because it is written in the only"
            "language this world has ever taught anybody."))

(def plan
  [{:keep :apart    :a :door     :b :pole}
   {:keep :apart    :a :cabinet  :b :cooler}
   {:keep :together :a :panel    :b :copier}
   {:keep :clear    :a :way-out}
   {:keep :present  :a :warrant}])

(defn describe [{:keys [keep a b]}]
  (case keep
    :apart    (str (name a) " shall not adjoin " (name b) ".")
    :together (str (name a) " shall adjoin " (name b) ".")
    :clear    (str "Nothing shall adjoin " (name a) ".")
    :present  (str (name a) " shall be held on this floor.")))

(mapv describe plan)

^:kindly/hide-code
(sp/scene
 (sp/action "Five sentences and four verbs, and there is nothing else on the"
            "screen, and there is nothing else in the building."))

^:kindly/hide-code
(sp/scene
 (sp/dialogue "MAYA" "reading over his shoulder"
              "Where are the walls?")

 (sp/dialogue :pc
              "There aren't any.")

 (sp/dialogue "MAYA"
              "There are walls. I have touched several of them.")

 (sp/dialogue :pc
              ["There are five sentences and the walls are"
               "wherever they have to be for the five sentences to"
               "be true."])

 (sp/beat "She reads it again.")

 (sp/dialogue "MAYA"
              ["Then it isn't a plan. A plan is a thing somebody"
               "drew."])

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "MAYA"
              "It's a schedule.")

 (sp/beat "Neither of them says anything for a moment, because she has used"
          "the correct word and they have both spent a year in a place that"
          "uses it.")

 (sp/off-screen "ZELL"
                "Oh, you can read it.")

 (sp/beat "Not from the radio. From the room.")

 (sp/off-screen "ZELL"
                ["I'm sorry. That was rude. It's only that nobody"
                 "has been able to read it for such a long time, and"
                 "they do try."])

 (sp/dialogue :pc
              "What happens if one of them isn't true?")

 (sp/off-screen "ZELL"
                "Then it's put right.")

 (sp/dialogue "MAYA"
              "By what?")

 (sp/off-screen "ZELL"
                ["By the floor. That's what a floor is for. You"
                 "wouldn't ask a ledger who reconciled it."])

 (sp/transition "Cut to:"))

^:kindly/hide-code
(sp/scene
 (sp/action "The audit is twenty lines and it is at the bottom of the screen"
            "and it has a cursor sitting in it, because it is not a printout."
            "It is running.")

 (sp/action "It goes through the five sentences and gives back the ones that"
            "are not true at this moment, and it does that after every single"
            "thing anybody does anywhere on the floor, and it has been doing"
            "it since before anybody in this room was born.")

 (sp/action "Three of the five can be made true by moving something.")

 (sp/action "One cannot."))

(defn repairable? [{:keys [keep]}]
  (contains? #{:apart :together :clear} keep))

(mapv (juxt describe repairable?) plan)

^:kindly/hide-code
(sp/scene
 (sp/action "A room that exists in order to hold a thing has no reason to"
            "exist once the thing has gone, and the floor is not cruel about"
            "that and does not hesitate and does not give notice."))

^:kindly/hide-code
(sp/scene
 (sp/dialogue "MAYA"
              "The last one. Warrant shall be held on this floor.")

 (sp/dialogue "MAYA"
              "That's what he sent us for.")

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "MAYA"
              ["And if we take it, the rooms that were holding it"
               "have got no reason to be here."])

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "MAYA"
              "Including this one.")

 (sp/beat "Four seconds.")

 (sp/off-screen "ZELL"
                ["She's quick. You are quick. I want that said"
                 "before anything else happens, because I don't"
                 "think anybody has said it to either of you all"
                 "year."])

 (sp/beat "It is the first true and generous thing anybody has said to them"
          "since they came down here and it lands exactly as it was meant to.")

 (sp/off-screen "ZELL"
                ["Now. Watch this. I've been wanting to show"
                 "somebody for a very long while."])

 (sp/beat "Nothing moves.")

 (sp/action "A sixth line appears on the screen, under the fifth, in the same"
            "typeface, in the same voice.")

 (sp/beat "WAY-OUT SHALL NOT ADJOIN DOOR.")

 (sp/action "Somewhere out in the dark, a long way off, something heavy moves"
            "and stops.")

 (sp/beat "Then a second thing, nearer.")

 (sp/off-screen "ZELL"
                ["There. That's all it is. That is the whole of what"
                 "I do."])

 (sp/dialogue "MAYA"
              "You wrote that.")

 (sp/off-screen "ZELL"
                ["I wrote that. And the floor is correct again,"
                 "which it was not for about two seconds, and it did"
                 "not like that at all."])

 (sp/off-screen "ZELL"
                ["I don't move anything. I have never moved anything"
                 "in my life. I add a line and the building does the"
                 "rest, and it does it faithfully, and it never once"
                 "asks me who I am."])

 (sp/beat "{pc} is looking at the list and not at the room.")

 (sp/dialogue :pc
              "How many lines are there?")

 (sp/off-screen "ZELL"
                "On this floor? Six.")

 (sp/dialogue :pc
              "How many are there upstairs?")

 (sp/beat "The pause is different this time. Pleased.")

 (sp/off-screen "ZELL"
                ["Now that is the question. That is exactly the"
                 "question, and you have got to it in about four"
                 "minutes, and it took me eleven years."])

 (sp/transition "Blackout."))

^:kindly/hide-code
(sp/scene
 (sp/action "Here is the floor, as it was on the screen, running, with the"
            "audit under it.")

 (sp/action "Walk with the arrows. Nothing will stop you and nothing will"
            "refuse you and there is no square you may not enter. The building"
            "rearranges afterwards.")

 (sp/action "Which is the only way through it: you cannot open a route. You"
            "can make the floor wrong in a place you have chosen, by putting a"
            "thing next to a thing, and then the floor puts itself right, and"
            "what it moves to put itself right may be the thing that was in"
            "your way.")

 (sp/action "Hold shift and a direction to put a fixture in the next room"
            "along. Stand on the warrant and press space to take it.")

 (sp/action "And then be somewhere else, quickly, because the fifth sentence"
            "cannot be made true again and the floor will keep trying."))

^:kindly/hide-code
(maze/widget)

^:kindly/hide-code
(sp/scene
 (sp/action "The button marked Amend is Zell's. It adds a line. It is not a"
            "difficulty setting and it does not make the floor harder in any"
            "way that could be measured — it makes the floor more correct,"
            "and there is no version of this in which those two things are"
            "different."))