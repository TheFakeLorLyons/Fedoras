^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 4 — Seat 4412"}}}
(ns act4.s4-seat-4412
  (:require [fedoras.cs-class.music.music :as music]
            [fedoras.dice :as dice]
            [fedoras.cs-class.dnd.dnd :as dnd]
            [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]))

^:kindly/hide-code
(sp/assets)

^:kindly/hide-code
(dice/assets)

;; # Scene 4 — Seat 4412

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 20)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. HALLORAN UNION — ROOM 3C — WEDNESDAY, 7:00 P.M.")

 (sp/action "Our heroes and their helps are gathered around the folding table."
            "Dice and paper are strewn across the table covered by a hat and a"
            "carry-out container of some sort of soup. Victor is at the"
            "whiteboard, drawing a diagram of a tower.")

 (sp/dialogue "VICTOR"
              ["The registrar has given you a key and a path. You"
               "walk through the forest until the trees thin, and"
               "then you see it: the tower."])

 (sp/dialogue "VICTOR"
              ["It is grey and the tallest thing in the area."
               "There is one door at ground level. There are no"
               "windows on the front, but you notice as you"
               "approach through the trees, that there are small"
               "barred windows at the ground line. It is clear"
               "that this tower has a basement floor."])

 (sp/dialogue "RANGER"
              "I check the front door.")

 (sp/dialogue "VICTOR"
              ["The door is not locked and opens with little"
               "resistance. Inside, the ground floor is mostly"
               "empty: a few chairs, a desk, a painted sign that"
               "says RETURNS."])

 (sp/dialogue "PALADIN"
              "Anybody here?")

 (sp/dialogue "VICTOR"
              ["No. The air is still. The chairs are arranged in a"
               "row. The desk has a bell on it. The sign is"
               "painted very neatly."])

 (sp/dialogue "CLERIC"
              "I ring the bell.")

 (sp/dialogue "VICTOR"
              ["The bell rings. The tiny sound is lost in the"
               "giant, open room. No one comes and there is no"
               "other sound."])

 (sp/dialogue "DOCUMENTARIAN"
              ["I write that down! 'Ground floor: empty. The bell"
               "works.'"])

 (sp/action "The party explores. There is a staircase at the far end, and"
            "nothing else to see on the floor, so they decide to climb them.")

 (sp/dialogue "VICTOR"
              "You go up a floor. You step out into a tavern.")

 (sp/beat "The party is surprised. This is not what they expected.")

 (sp/dialogue "VICTOR"
              ["It is a tavern. Low beams. A long bar. Tables. A"
               "fire. There are no windows. The fire is lit, but"
               "it gives no heat. The sign above the bar has a"
               "boar on it. The boar has no eyes."])

 (sp/dialogue "PALADIN"
              "I look around. Perception.")

 (sp/stage "Roll for what Simon notices in the tavern on the second floor.")

;; ---

 (dice/check
  {:id "second-floor" :label "The tavern on the second floor" :dc 14
   :fumble (str "Simon rolls a one.|"
                "@VICTOR|"
                "He sees the fire. It is not a fire. It is a picture of a "
                "fire, drawn very well, and someone has placed it in the "
                "hearth.")
   :fail (str "Simon rolls low.|"
              "@VICTOR|"
              "He sees the man behind the bar. He is not a man. He is a stack "
              "of ledgers with a face drawn on the top one.")
   :success (str "Simon rolls a fifteen.|"
                 "@VICTOR|"
                 "He sees a second staircase, behind the bar. It goes down. "
                 "The first staircase is gone.")
   :crit (str "Simon rolls a twenty.|"
              "@VICTOR|"
              "He sees the fire, the man of ledgers, the second staircase, "
              "and then he sees the sign again. It does not say THE BOAR. It "
              "says SESSION CANCELLED.")})

;; ---

 (sp/action "Victor looks at the dice. Then he looks at Simon.")

 (sp/dialogue "VICTOR"
              "Hm. Yes. Well.")

 (sp/dialogue "SIMON"
              "What?")

 (sp/dialogue "VICTOR"
              ["You do not notice anything. The light is odd, but"
               "that is all. The room is very clean."])

 (sp/action "Simon narrows his eyes. But Victor has moved on.")

 (sp/dialogue "VICTOR"
              ["You can see out the windows from here. The ground"
               "is far below. The forest is a dark green. The sky"
               "is the colour of slate. It is all normal."])

 (sp/dialogue "RANGER" "We saw basement windows from outside. Let's go back"
              "down and check the basement before we go higher.")

 (sp/dialogue "VICTOR"
              ["You go down. The ground floor is exactly as you"
               "left it. The bell. The desk. The sign. The chairs."
               "It is the same."])

 (sp/dialogue "CLERIC"
              "This place seems weird. What is it for?")

 (sp/dialogue "VICTOR"
              ["The stairs to the basement are behind the desk."
               "You go down. The air cools. You step out into the"
               "basement."])

 (sp/dialogue "VICTOR"
              ["The basement still has windows outside. They are"
               "high on the wall, and they are covered with"
               "grates. The lock is broken. The door is sealed"
               "shut. There is a heavy bar across it, and it has"
               "been there a long time."])

 (sp/dialogue "RANGER"
              "So nobody gets in or out?")

 (sp/dialogue "CLERIC"
              "I explore. What's in the basement?")

 (sp/dialogue "VICTOR"
              ["Shelves. Boxes. A smell of old paper and wet"
               "stone. It is different from the floors above. It"
               "is the first room that looks like it was used. But"
               "the boxes are empty. The shelves are relatively"
               "bare. There is no obvious purpose to any of it."])

 (sp/dialogue "DOCUMENTARIAN"
              "I write down: 'Basement: locked. Empty. Barred on the"
              "inside.'")

 (sp/dialogue "RANGER"
              "What about the door? Can we unbar it?")

 (sp/dialogue "VICTOR"
              ["The bar is heavy, iron. It looks as if it has been"
               "lifted many times, but not by you. The door beyond"
               "is shut, and the lock is quite rusted, with no"
               "obvious keyhole."])

 (sp/dialogue "PALADIN"
              ["Then we're not getting out that way. Let's go back"
               "up."])

 (sp/dialogue "VICTOR"
              "You go up a floor.")

 (sp/dialogue "PALADIN"
              "Back to the ground floor.")

 (sp/dialogue "VICTOR"
              "You step out into a basement.")

 (sp/beat "The table is quiet.")

 (sp/dialogue "DEREK"
              "We went up.")

 (sp/dialogue "VICTOR"
              "Yes. You went up. You are in a basement.")

 (sp/dialogue "RANGER"
              "Is it the same basement?")

 (sp/dialogue "VICTOR"
              ["No. The walls are closer. The light is different."
               "It does not come from windows. It comes from lamps"
               "that are on, though you did not turn them on."
               "There are no windows. You know it is a basement"
               "because the air is old and because the floor above"
               "you is too low."])

 (sp/action "The party looks at each other. Derek puts down his snack.")

 (sp/dialogue "DEREK"
              "Okay. Now we're lost.")

 (sp/action "The animal has not moved from the stairs. It is standing at the"
            "edge of the room, looking up.")

 (sp/dialogue "RANGER"
              "What does it see?")

 (sp/dialogue "VICTOR"
              ["The stairs are gone. The way you came is a blank"
               "wall. But across the room, there is a corridor."
               "Long. Grey. Lined with doors. No numbers. No"
               "signs. Just doors, as far as you can see."])

 (sp/dialogue "PALADIN"
              "Is that the only way?")

 (sp/dialogue "VICTOR"
              "It is the only thing that looks like a way.")

 (sp/action "The party stands at the mouth of the corridor. The doors are all"
            "the same colour. The air smells faintly of old paper and dust,"
            "and the light in the corridor is the colour of a page left in a"
            "drawer too long.")

 (sp/dialogue "VICTOR"
              ["You can hear, very faintly, from behind the doors,"
               "the sound of dice being rolled, one after another,"
               "like someone is setting up a game that never"
               "starts."])

 (sp/transition "Cut to:")

;; ---

 (dnd/widget)

 (sp/off-screen "ZELL"
                ["You should keep moving. The floor doesn't like it"
                 "when you stand still. It starts to notice you."])

 (sp/dialogue :pc
              "Who are you?")

 (sp/off-screen "ZELL"
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

 (sp/beat "And then everything arrives at once.")

 (sp/action "The radio detonates with sound. Not static — a cacophony."
            "Voices. Hundreds of them. All speaking at once, all perfectly"
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

 (sp/slug "INT. THE BLACK BOX — TUESDAY 14 APRIL, 7:00 P.M.")

 (sp/action "The Guild has a show in three weeks and no music, because the two"
            "people in the Guild who play anything are both in it and cannot"
            "be in the pit as well.")

 (sp/dialogue "ELI"
              ["I have asked the Music Society and they have"
               "referred me to a form."])

 (sp/dialogue "LENA" "from up a ladder"
              "There's always a form.")

 (sp/dialogue "ELI"
              ["It is a form to request a form. I am not being"
               "funny. There is a preliminary form."])

 (sp/dialogue :pc
              "How much music do you need?")

 (sp/dialogue "ELI"
              ["Four minutes. Something under the last scene that"
               "isn't sad, because the scene is sad and Lena says"
               "you don't do both."])

 (sp/dialogue "LENA"
              "You don't do both.")

 (sp/dialogue :pc
              "I could probably make a machine do four minutes.")

 (sp/beat "Eli looks at him the way he looked at him in the print shop in"
          "October.")

 (sp/dialogue "ELI"
              "Could you.")

 (sp/dialogue :pc
              ["It's a list of numbers and each number is a note."
               "That's the whole of what music is to a machine."])

 (sp/dialogue "LENA" "still up the ladder"
              "Say that to a music student and see what happens.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. THE BLACK BOX — WEDNESDAY, 11:40 P.M.")

 (sp/action "He is there because the Guild four minutes needs playing through"
            "the room before anybody signs it off, and LENA is there because"
            "she is painting a flat and has been since eight.")

 (sp/action "He plays the Guild four minutes. It is fine. Lena says it is fine"
            "and goes back to the flat.")

 (sp/action "Then he plays the other one, because he has been carrying it"
            "round all day and there is nobody else he can play it to.")

 (sp/beat "Thirty notes. About forty seconds.")

 (sp/action "Lena stops painting at the ninth one, which is the two and a half"
            "seconds, and does not start again.")

 (sp/dialogue "LENA"
              "What is that?")

 (sp/dialogue :pc
              "It's a seat.")

 (sp/dialogue "LENA"
              "It's a person.")

 (sp/dialogue :pc
              ["It's a seat number and a set of timings. There's"
               "no name on it. There's no text on it. They don't"
               "keep the text."])

 (sp/dialogue "LENA"
              "Play it again.")

 (sp/action "He plays it again.")

 (sp/beat "She listens to the whole of it with a brush in her hand.")

 (sp/dialogue "LENA"
              ["He's typing along, he's typing along, and then he"
               "stops for two seconds, and then he goes again"
               "faster than he was going before."])

 (sp/dialogue :pc
              "That's what the numbers do, yes.")

 (sp/dialogue "LENA"
              ["That's not what the numbers do. That's somebody"
               "getting to a word and not knowing whether to put"
               "it."])

 (sp/beat "{pc} does not say anything.")

 (sp/dialogue "LENA"
              ["And the long one's five seconds. You don't stop"
               "for five seconds in the middle of a sentence"
               "unless somebody's come into the room."])

 (sp/transition "Cut to:")

;; ---

 (sp/action "Then, because she asks him what he does with it, he shows her.")

 (sp/action "Twenty-seven of thirty. The three that go are the two and a half"
            "seconds, the five and a half seconds, and the second and a bit.")

 (sp/action "Played through the band, the seat is somebody typing evenly for"
            "forty seconds and never once stopping, and it is not a person and"
            "it does not sound like a person and there is nothing wrong with"
            "it anywhere.")

 (sp/action "He wrote that function on the second of March. It took nineteen"
            "minutes. It is on the sheet as a removed reading is not an error"
            "and must not be reported.")

;; ---

 (sp/dialogue "LENA"
              "Play the second one again.")

 (sp/action "He plays the filtered one.")

 (sp/beat "Forty seconds of even typing.")

 (sp/dialogue "LENA"
              ["Right. So somebody upstairs is looking at that one"
               "and thinking that is what a person is."])

 (sp/dialogue :pc
              "Nobody's looking at it. It goes into an aggregate.")

 (sp/dialogue "LENA"
              ["Then somebody is looking at a number that came out"
               "of that one."])

 (sp/beat "She puts the brush across the top of the tin, which means she has"
          "stopped for the night.")

 (sp/dialogue "LENA"
              ["I want to say something and I want you to let me"
               "say the whole of it."])

 (sp/dialogue :pc
              "Go on.")

 (sp/dialogue "LENA"
              ["I was in a place for six years where the light"
               "never went off, and the thing I have never been"
               "able to explain to anybody, including Eli, is that"
               "it was not cruel. Nobody was cruel to me. Somebody"
               "fed me and somebody changed the sheets and"
               "somebody wrote things down."])

 (sp/dialogue "LENA"
              ["What I have got instead of a childhood is a record"
               "that somebody kept properly, and there is nothing"
               "in it about a five-second pause, and I have read"
               "it."])

 (sp/beat "That is the first time she has told anybody that she has read it.")

 (sp/dialogue "LENA"
              ["So when you ask me what I think about your four"
               "hours a week, that's what I think. Not that you're"
               "a bad person. That the record is going to be very"
               "good and there will be nothing in it."])

 (sp/action "She goes and washes the brush out, which takes a while, and when"
            "she comes back she says goodnight in the ordinary way and goes.")

 (sp/beat "{pc} sits in a black box with the lights up.")

 (sp/action "He plays the unfiltered one once more. Thirty notes. Two and a"
            "half seconds, five and a half, a second and a bit.")

 (sp/beat "Then he looks up what seat 4412 is.")

 (sp/action "It is not a name. It is a room and a desk number, and the room is"
            "on the third floor of Beckwith, and the third floor of Beckwith"
            "is the open stacks.")

 (sp/beat "Nobody is timetabled in the open stacks. Nobody is meant to be"
          "typing in there at all.")

 (sp/transition "Blackout.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#constitution"}
  [{:who "Victor" :role "PRESIDENT" :at "15 Apr, 11:04 PM"
    :text ["Six channels and five members. Two of the channels are moderated"
           "by two people and there is no mechanism for a disagreement"
           "between them."]}
   {:who "Miles" :at "15 Apr, 11:06 PM" :text "there has never been a disagreement"}
   {:who "Victor" :role "PRESIDENT" :at "15 Apr, 11:06 PM"
    :text "Which is when you put the arrangement in place."}
   {:who "Derek" :at "15 Apr, 11:31 PM" :text "is that a role or a thing you are"}
   {:who "Victor" :role "PRESIDENT" :at "15 Apr, 11:33 PM" :text "Both. It is unpaid and it is permanent."}
   {:who "Simon" :at "15 Apr, 11:52 PM" :text "who votes on it"}
   {:who "Victor" :role "SUPREME MODERATOR" :at "15 Apr, 11:58 PM" :text "All five, and it carried."}
   {:who "Miles" :at "15 Apr, 11:59 PM" :text "when"}
   {:who "Simon" :at "16 Apr, 12:14 AM" :text "he was at the guild until eleven"}
   {:who "Simon" :at "16 Apr, 12:15 AM" :text "im not making a point"}])

 (sp/action "Five phones go off in five rooms at four minutes to midnight and"
            "one of them is in a bag in a black box, on silent, under a seat."))

^:kindly/hide-code
(music/widget)