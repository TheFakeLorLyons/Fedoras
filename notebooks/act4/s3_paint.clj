^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 3 — Paint"}}}
(ns act4.s3-paint
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.campus.campus :as campus]
            [fedoras.cs-class.paint.paint :as paint]))

^:kindly/hide-code
(sp/assets)

;; # Scene 3 — Paint

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 13)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. STERLING CENTER — FOURTH FLOOR — MONDAY 23 MARCH, 9:00 A.M.")

 (sp/action "Systems is one long room with eleven desks in it and four people"
            "at them. DUNN is at the end with the specifications in a tray,"
            "and the tray has never been empty and has never had more than six"
            "things in it.")

 (sp/action "The woman from the atrium is standing by the window with a coffee"
            "she brought in with her. The lanyard says VANE.")

 (sp/dialogue "VANE"
              "Morning. Bring it and come next door.")

 (sp/action "Next door is a room on the same floor with no nameplate on it,"
            "which is the second room he has been in this year with no"
            "nameplate on it.")

 (sp/action "It has a table, two chairs on the same side of it, and a screen"
            "on the wall which is on and showing nothing.")

 (sp/dialogue "VANE"
              ["Right. I'm going to talk for two minutes and then"
               "you can ask me anything you like and I'll answer"
               "what I'm able to and tell you when I'm not, which"
               "is more than you'll get anywhere else in this"
               "building."])

 (sp/dialogue :pc
              "All right.")

 (sp/dialogue "VANE"
              ["The thing you made on Saturday. The scoring part."
               "You take a question, you take nine thousand lines,"
               "and you decide which line the question is about."
               "That is the part we want and it is the part nobody"
               "here can do."])

 (sp/dialogue :pc
              "It's forty lines. It's a first-year exercise.")

 (sp/dialogue "VANE"
              ["It is. We have a great deal of that sort of thing"
               "and we did not have anybody who had written one in"
               "this building in four years. That is not a"
               "compliment to you, it is a fact about the"
               "building."])

 (sp/beat "She drinks the coffee.")

 (sp/dialogue "VANE"
              ["Second thing. We need somebody to be able to mark"
               "a picture. Draw round a bit of it, put a note on"
               "it, and have the mark kept. That's the job. Four"
               "hours a week on top of what you do for Dunn, at"
               "twice what Dunn pays you, until the end of the"
               "year."])

 (sp/dialogue :pc
              "And after the end of the year?")

 (sp/dialogue "VANE"
              ["Then it's a different conversation and I would not"
               "insult you by having it now."])

 (sp/beat "Two minutes. She stops talking at two minutes.")

 (sp/dialogue "VANE"
              "Go on, then.")

 (sp/action "{pc} has had one question since the fourth of March and has not"
            "asked it of anybody, including the person who told him to.")

 (sp/dialogue :pc
              "What are the readings?")

 (sp/beat "Vane puts the coffee down. It is the first thing he has said that"
          "she was not expecting.")

 (sp/dialogue "VANE"
              "Which specification?")

 (sp/dialogue :pc
              ["Forty-four seventy-one K. Drop the readings"
               "outside the band, keep the order, don't report the"
               "ones you dropped."])

 (sp/dialogue "VANE"
              ["Terminal telemetry. Every keystroke on every"
               "machine in this institution, every pause between"
               "them, how long a page is open, what gets deleted"
               "before it is sent. Time-stamped, by seat."])

 (sp/dialogue :pc
              "By seat.")

 (sp/dialogue "VANE"
              ["By seat. Which is by person, and I am not going to"
               "sit here and pretend it is anything else."])

 (sp/dialogue :pc
              "And the band?")

 (sp/dialogue "VANE"
              ["Is what we expect. Anything outside it comes out"
               "before the analysis, because a run with four"
               "impossible numbers in it is worse than no run."])

 (sp/dialogue :pc
              "And you don't say which ones you took out.")

 (sp/dialogue "VANE"
              ["That's your line, not mine. You wrote it. It was"
               "on the sheet and you wrote it in nineteen minutes."])

 (sp/beat "That is the only unkind thing she says, and she says it flatly, and"
          "it is true.")

 (sp/dialogue "VANE"
              ["Look. Everything I have told you is on the consent"
               "notice every one of you agreed to in the second"
               "week of September. Nobody read it. That is not a"
               "secret, it is a fact about how long documents are."])

 (sp/dialogue :pc
              ["Then why has the room next door not got a name on"
               "it?"])

 (sp/dialogue "VANE"
              ["Because I have four rooms in three buildings and"
               "the nameplates take nine weeks."])

 (sp/beat "He believes her, which is the worst part of the morning.")

 (sp/transition "Cut to:")
 
 (art/handout
  "MERIDIAN SYSTEMS · MARKING · what the tool must do"
  "One picture at a time."
  "A person draws round part of the picture with a pointer. The mark is kept as the path they drew, not as the picture."
  "A note may be attached to a mark. Notes are free text and are not read by anything."
  "Marks are attributable. Who marked it, and when."
  "Marks are not deleted. A mark that is withdrawn is kept and flagged as withdrawn."
  "The tool is used by one person at a time for about six hours a day."
  "Estimated: two weeks."
  "Return by: 10 April.")

 (sp/slug "INT. THE ROOM WITH NO NAMEPLATE — MONDAY 30 MARCH")

 (sp/action "It has a toolbar now. VANE goes along it with a finger and stops"
            "at the end of it.")

 (sp/dialogue "VANE"
              "There's no rubber.")

 (sp/dialogue :pc
              ["There's no rubber because marks aren't deleted."
               "It's on the sheet."])

 (sp/dialogue "VANE"
              ["It is on the sheet. A person marking eleven"
               "thousand photographs for six hours a day is going"
               "to draw round the wrong thing about nine times an"
               "hour and they need a rubber, and the rubber does"
               "not delete anything, it withdraws it."])

 (sp/dialogue :pc
              "So it goes off the picture and stays in the file.")

 (sp/dialogue "VANE"
              "Yes.")

 (sp/beat "That takes him about forty minutes and he does it well, because he"
          "does everything on that floor well.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE ROOM WITH NO NAMEPLATE — WEDNESDAY 1 APRIL")

 (sp/action "She uses it for ten minutes and marks four things and takes two"
            "of them back, and the two she takes back go off the picture"
            "entirely, which is what he built.")

 (sp/dialogue "VANE"
              "No. I want to see them.")

 (sp/dialogue :pc
              "You withdrew them.")

 (sp/dialogue "VANE"
              ["I withdrew them. I did not stop being interested"
               "in them. Put them back on, lighter, so that they"
               "are there and they are not in the way."])

 (sp/dialogue :pc
              "Why?")

 (sp/beat "She does not pause over it and she does not soften it, because she"
          "has been answering him for a fortnight and has not started lying"
          "now.")

 (sp/dialogue "VANE"
              ["Because a mark somebody put on a picture and then"
               "took off again is a more interesting object than a"
               "mark, and if you make it invisible I will have to"
               "go and get it out of the file, and I would rather"
               "look at it while I am looking at the picture."])

 (sp/action "{pc} says right, and goes next door, and does it in eleven"
            "minutes, because it is one colour and one condition.")

 (sp/beat "It is the smallest thing he has written all year and it is the only"
          "one he thinks about on the walk back.")

 (sp/transition "Blackout."))

^:kindly/hide-code
(sp/scene
 (sp/action "Maya keys the short-wave and Lazlo answers immediately.")

 (sp/beat "His voice is clear. Too clear.")

 (sp/dialogue "LAZLO"
              ["There you are. There you are. Okay. Okay. I have"
               "you."])

 (sp/dialogue "MAYA"
              "We're through.")

 (sp/dialogue "LAZLO"
              ["I know. I know you're through. I'm looking at the"
               "board and I can see exactly where you are."])

 (sp/beat "He sounds almost giddy.")

 (sp/dialogue :pc
              "That's good, right?")

 (sp/dialogue "LAZLO"
              ["It's impossible. That's what it is. You're not"
               "anywhere. You're not on any of the boards."])

 (sp/beat "He says it like it's the best news he's ever delivered.")

 (sp/action "They walk. Lazlo keeps talking. He has a lot to say about the"
            "architecture, about the way the corridors don't quite meet, about"
            "a door he remembers from a building that isn't there anymore.")

 (sp/dialogue "MAYA"
              "Lazlo. Lazlo, slow down. Can you hear us?")

 (sp/beat "He can't. He's still talking. His voice is getting thinner, like"
          "someone turning down a dial that only goes one way.")

 (sp/dialogue "LAZLO"
              ["—and the tiles, you'll see the tiles, they're"
               "wrong but they're wrong the same way every time,"
               "which is almost worse—"])

 (sp/dialogue :pc
              "Lazlo?")

 (sp/beat "Static. Then his voice again, very small.")

 (sp/dialogue "LAZLO"
              "I don't— I didn't think—")

 (sp/beat "The static rises. Somewhere inside it, Lazlo is still speaking."
          "They can hear the shape of words but not the words themselves.")

 (sp/dialogue "MAYA"
              "Lazlo! Can you hear us? Lazlo!")

 (sp/beat "The static peaks. And then it doesn't fade. It simply stops being"
          "static and becomes something else. A silence with a voice already"
          "inside it.")

 (sp/off-screen "UNKNOWN"
                "Hello-o.")

 (sp/beat "Clear as a bell. Articulate. Immaculate. Like the voice is in the"
          "room with them, or in their heads, or in the walls.")

 (sp/off-screen "UNKNOWN"
                "And who might you be?")

 (sp/beat "Maya stops walking.")

 (sp/dialogue "MAYA"
              "Who is this?")

 (sp/off-screen "UNKNOWN"
                ["A friend. A fellow traveler. Someone who heard you"
                 "knocking and thought, well, it would be rude not"
                 "to answer."])

 (sp/beat "The voice is warm. Genuinely warm. The kind of voice that makes you"
          "want to answer it.")

 (sp/dialogue :pc
              "Where's Lazlo?")

 (sp/off-screen "UNKNOWN"
                ["Lazlo. Is that your friend on the radio? He's"
                 "still there. He's just—"])

 (sp/beat "A pause. Not for effect. Like the voice is listening to something.")

 (sp/off-screen "UNKNOWN"
                ["—worried. He's very worried. He didn't have much"
                 "of a plan beyond this part, did he?"])

 (sp/beat "Somewhere under the silence, like a radio in another room, they can"
          "hear Lazlo's voice. Still talking. Still trying to reach them.")

 (sp/dialogue "MAYA"
              "How do you know that?")

 (sp/off-screen "UNKNOWN"
                ["Because I've been where you are. Because I've been"
                 "listening. Because the walls here don't keep"
                 "secrets, they just keep everything."])

 (sp/beat "The voice laughs. Not unkindly.")

 (sp/off-screen "UNKNOWN"
                ["You should keep moving. The floor doesn't like it"
                 "when you stand still. It starts to notice you."])

 (sp/dialogue :pc
              "Who are you?")

 (sp/off-screen "UNKNOWN"
                "Zell. My name is Zell. And you're—")

 (sp/beat "The pause is different this time. Interested.")

 (sp/off-screen "ZELL"
                "—you're new. Both of you. Oh, that's wonderful.")

 (sp/action "The radio crackles once, and for a moment they can hear Lazlo"
            "again, very distant, saying something about a door. Then Zell's"
            "voice returns, gentle and close.")

 (sp/off-screen "ZELL"
                ["Don't worry about Lazlo. He'll find you. He always"
                 "does. Now—"])

 (sp/beat "Somewhere in the static, a sound like someone settling into a"
          "chair.")

 (sp/off-screen "ZELL"
                "—tell me everything."))

^:kindly/hide-code
(sp/scene
 (sp/action "A mark is a shape and a person and a time. Withdrawing one adds a"
            "fourth thing to it and takes nothing away."))

^:kindly/hide-code
(sp/code
 "(defn mark [pts by at]
    {:shape (simplify pts 0.5) :by by :at at :withdrawn nil})

  (defn withdraw [m at]
    (assoc m :withdrawn at))
  
  (defn shown [ms]
    (remove :withdrawn ms))
  
  (def sheet
    [(mark stroke \"3812\" \"09:14\")
     (mark stroke \"3812\" \"09:16\")
     (mark stroke \"3812\" \"09:21\")])
  
  (def after
     (update sheet 1 withdraw \"09:22\"))
  
  {:on-the-picture (count (shown after))
   :in-the-file    (count after)}")

^:kindly/hide-code
(sp/scene
 (sp/action "Two on the picture and three in the file, and the second number"
            "does not go down, and there is no operation anywhere in what he"
            "wrote that makes it go down.")

 (sp/action "He did not put that in. It was on the sheet and the sheet was"
            "four lines long and he read all four of them."))

^:kindly/hide-code
(paint/widget)

^:kindly/hide-code
(campus/widget)

^:kindly/hide-code
(sp/scene
 (sp/action "Two counts and neither of them is wrong. The one that says what"
            "is on the picture goes up and down. The one that says what is in"
            "the file only goes up."))