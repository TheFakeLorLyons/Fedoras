^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 2 — In Season"}}}
(ns act2.s2-in-season
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.dice :as dice]
            [fedoras.sessions.two :as two]
            [fedoras.sketch :as sketch]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(sp/assets)

^:kindly/hide-code
(dice/assets)

;; # Scene 2 — In Season

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 6)

^:kindly/hide-code
(sp/scene
  (sp/slug "INT. A GRAY ROOM WITH NO WINDOWS — TUESDAY, 10:52 A.M.")
 
 (sp/action "The room is already set with twelve chairs arranged neatly around"
            "an ornate conference table. Marcy Antellier is laying out pens,"
            "one at each place, and a small bowl of mints in the middle of the"
            "table, while Bartholomew Tranche-Lard (facilities) impatiently"
            "waits for the quarterly meeting to begin.")
 
 (sp/dialogue "BARTHOLOMEW"
              "What are those?")
 
 (sp/dialogue "MARCY"
              "Mints.")
 
 (sp/dialogue "BARTHOLOMEW"
              "It's a meeting, not a hotel.")
 
 (sp/dialogue "MARCY" "Smiling"
              "It's a meeting! So take a mint.")
 
 (sp/action "Bartholomew Tranche-Lard takes a mint and holds it up to inspect"
            "it, the way you might a fine jewel. The man is gigantic, and he"
            "looks like a drill sergeant who has been retired to the school as"
            "their gym coach. Everything he does is too loud, and not on"
            "purpose.")
 
 (sp/action "Dalton Hillcrux comes in behind him, on a call, and does not hang"
            "up until he has sat down.")
 
 (sp/dialogue "DALTON"
              ["No. No. Tell them no. Tell them we'll do it in"
               "April."])
 
 (sp/action "He hangs up. He puts the phone face down on the table and looks"
            "at the mints.")
 
 (sp/dialogue "DALTON"
              "Whose idea?")
 
 (sp/dialogue "MARCY" "Still beaming"
              "Mine.")
 
 (sp/dialogue "DALTON"
              "It's fine.")
 
 (sp/action "Dean Magnus Sterling comes in with Priya Kessler-Vance. He is"
            "mid-sentence. She is walking half a step behind him and not in a"
            "subordinate way.")
 
 (sp/dialogue "STERLING" "Red faced"
              ["-and I said to her, I was saying, the thing about"
               "this institution is that we are in CHARGE of"
               "knowledge!! That is the WHOLE of what we are."])
 
 (sp/dialogue "KESSLER-VANCE"
              "Sit down, Magnus.")
 
 (sp/action "They sit down. He is at the head of the table because it did not"
            "occur to anybody else to sit there. She is at the middle of the"
            "table because that is where the agenda is.")
 
 (sp/dialogue "BARTHOLOMEW"
              "The House.")
 
 (sp/dialogue "KESSLER-VANCE"
              "What about it.")
 
 (sp/dialogue "BARTHOLOMEW"
              ["The floor plan has changed twice this month. The"
               "south corridor is a foot longer on Tuesday than it"
               "is on Monday. The Helps on the west side have"
               "stopped going into the reading room. Nobody has"
               "told them to stop and nobody knows why they have."])
 
 (sp/dialogue "KESSLER-VANCE"
              "It's contained.")
 
 (sp/dialogue "BARTHOLOMEW"
              ["It's contained the way a tiger is contained in"
               "your house while you remain locked outside. The"
               "thing in the back room is doing something to the"
               "building. I would like it on the record that I"
               "said that."])
 
 (sp/dialogue "KESSLER-VANCE"
              "It's on the record.")
 
 (sp/action "The door opens without a knock. Montgomery comes in. Black suit,"
            "plain, no tie. Black sunglasses, and a wire at the collar that is"
            "hard to miss and no one mentions.")
 
 (sp/action "He stands behind the chair at the far end of the table and looks"
            "at Bartholomew.")
 
 (sp/dialogue "MONTGOMERY"
              ["It isn't a leak and it isn't contained. It is a"
               "thing you were given and you do not understand,"
               "and I am not here to explain it to you. I am here"
               "because the alternative to checking on it is"
               "finding out."])

(sp/dialogue "MONTGOMERY"
             ["The floor plan is going to keep changing. The"
              "Helps are going to keep avoiding the reading room."
              "Every quarter somebody at this table is going to"
              "tell me the building is doing something new, and"
              "every quarter I am going to come down here and"
              "tell you the same thing, and the same thing is:"
              "leave it alone and write it down. That is not a"
              "difficulty. That is a job. You are supposed to be"
              "able to do it."])

(sp/beat "Bartholomew puts the mint in his mouth. It is the loudest thing that"
         "happens for a while.")

(sp/dialogue "MONTGOMERY"
             ["Meridian does not come down here because we enjoy"
              "the drive. We come down here because the last time"
              "we stopped, somebody moved something."])
 
 (sp/action "Montgomery takes a mint from the bowl. He puts it in his pocket."
            "He does not eat it.")
 
 (sp/dialogue "MONTGOMERY"
              "Term one is over. Where are we.")
 
 (sp/dialogue "KESSLER-VANCE"
              ["Where we expected to be. The November drafts came"
               "in on time and the December list will settle where"
               "it always settles."])
 
 (sp/dialogue "MONTGOMERY"
              "And the exception.")
 
 (sp/dialogue "KESSLER-VANCE"
              "Still an exception. Still small.")
 
 (sp/dialogue "MONTGOMERY"
              ["Small is not a category of exception that Meridian"
               "recognises."])
 
 (sp/dialogue "KESSLER-VANCE" "It's handled. It will come out at the front end"
              "and nothing downstream will see it.")
 
 (sp/dialogue "MONTGOMERY"
              "Then it isn't handled, yet. It's just going unnoticed"
              "for now.")
 
 (sp/dialogue "KESSLER-VANCE"
              "Yes.")
 
 (sp/dialogue "MONTGOMERY"
              "Fix it.")
 
 (sp/action "The door opens again. Odile comes in first, and then Dr. Alfrès"
            "Jairis-Égari, and the room does something the room does every"
            "time. It stops. Not because anyone decides to. Because the room"
            "has been having this meeting for a very long time and it has"
            "never once had this meeting without him.")
 
 (sp/action "He is old in a way that serves as a point of reference. He has"
            "long ceased to walk freely - one part carried and another part"
            "guided, yet still he is still legally the man who owns the"
            "buildings. Odile sits him down and sits beside him and does not"
            "touch the pens and does not take a mint.")
 
 (sp/dialogue "ODILE"
              "He can hear you. He would like you to continue.")
 
 (sp/action "Nobody has said anything yet.")
 
 (sp/dialogue "MONTGOMERY"
              "We were just finishing.")
 
 (sp/dialogue "ODILE"
              "He would like to hear the exception.")
 
 (sp/beat "Kessler-Vance looks at Sterling. Sterling looks at the table."
          "Nobody looks at Odile, which is the arrangement.")
 
 (sp/dialogue "KESSLER-VANCE"
              ["One provisional. His hours below ground are"
               "running longer than the band. He does not seem to"
               "notice. He is doing well in his coursework and"
               "poorly at being unremarkable."])
 
 (sp/dialogue "ODILE"
              "Name.")
 
 (sp/dialogue "KESSLER-VANCE"
              "{pc}.")
 
 (sp/beat "Odile does not react. Dr. Jairis-Égari does not react. The room,"
          "which has been holding its breath without knowing it, puts the"
          "breath back.")
 
 (sp/dialogue "ODILE"
              "He would like the file.")
 
 (sp/dialogue "KESSLER-VANCE"
              "Of course, no one is stopping him.")
 
 (sp/dialogue "ODILE"
              "He will have it before Friday.")
 
 (sp/dialogue "KESSLER-VANCE"
              "Before Friday.")
 
 (sp/dialogue "ODILE"
              "And the photographer.")
 
 (sp/dialogue "KESSLER-VANCE"
              "Which photographer?")
 
 (sp/dialogue "ODILE"
              ["There is only one person on campus authorized a"
               "camera."])
 
 (sp/dialogue "KESSLER-VANCE"
              ["Her approval is state-issued. It is renewed"
               "annually. It is for anomaly documentation. It is"
               "not ours to review and it is not ours to revoke."])
 
 (sp/dialogue "ODILE"
              "He knows whose it is. He is asking if she has used it for"
              "anything else.")
 
 (sp/dialogue "KESSLER-VANCE"
              ["She has used it for everything else. She"
               "photographs whatever she looks at. The state"
               "approves it because the state would rather the"
               "photographs exist than not."])
 
 (sp/dialogue "ODILE"
              "Does she keep them.")
 
 (sp/dialogue "KESSLER-VANCE"
              ["In her room, in a box under her bed. She has been"
               "advised that it is inapprobate to display them"
               "where they can be seen."])
 
 (sp/dialogue "ODILE"
              "Has she taken one of the House.")
 
 (sp/beat "Kessler-Vance does not answer immediately. This is the first time"
          "in the meeting that she has had to think about what to say.")
 
 (sp/dialogue "KESSLER-VANCE"
              "She has photographed the outside of the building. She"
              ["has not been inside. The House does not let her"
               "in."])
 
 (sp/dialogue "ODILE"
              "He would like to be told if that changes.")
 
 (sp/dialogue "KESSLER-VANCE"
              "You'll be told.")
 
 (sp/action "Dr. Jairis-Égari's hand moves. It is the first time it has moved"
            "since he sat down. Odile takes it and holds it and does not look"
            "at anybody.")
 
 (sp/dialogue "MONTGOMERY"
              "Then we're done.")
 
 (sp/action "He leaves. He does not say goodbye. He does not say anything"
            "else. The mint is still in his pocket.")
 
 (sp/dialogue "STERLING"
              "Well. I think that went well.")
 
 (sp/beat "Nobody answers him.")
 
 (sp/action "The door opens one more time. The Caretaker comes in.")
 
 (sp/action "He is not hurried, does not take a mint, does not sit down. He"
            "stands at the middle of the room and looks at the empty chair at"
            "the far end - the one Montgomery has just left, and then he looks"
            "at Marcy.")
 
 (sp/dialogue "THE CARETAKER"
              "The box under the bed.")
 
 (sp/dialogue "MARCY"
              "Yes.")
 
 (sp/dialogue "THE CARETAKER"
              "Is it still under the bed.")
 
 (sp/dialogue "MARCY"
              "It has never been under the bed.")
 
 (sp/dialogue "THE CARETAKER"
              "Where is it.")
 
 (sp/dialogue "MARCY"
              ["Inside the wardrobe, on the doors, where they have"
               "always been. The box under the bed has four"
               "photographs in it and they are photographs she has"
               "already shown somebody. The ones that matter are"
               "in the wardrobe."])
 
 (sp/action "The room does not react. This is obviously not new information.")
 
 (sp/dialogue "THE CARETAKER"
              "Good.")
 
 (sp/action "He leaves. He is in the room for less than a minute. Nobody in"
            "the room says his name and nobody in the room writes down that he"
            "was here.")
 
 (sp/action "Bartholomew takes another mint and this time he eats it"
            "immediately, which he does not normally do, and which nobody"
            "comments on.")
 
 (sp/action "The room begins to end in the order it began. Dalton is already"
            "on another call. Sterling is still at the head of the table and"
            "is the last to stand up. Marcy is collecting the pens. Odile is"
            "watching Jairis-Égari and does not look at anybody and does not"
            "take a pen.")
 
 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. HALLORAN UNION — ROOM 3C — WEDNESDAY, 7:00 P.M.")

 (sp/action "A folding table, dice tin, and hats.")

 (sp/action "The grid on the pizza box lid has been redrawn because the old"
            "one wore off, and it is not the same size as the old one, and"
            "Miles has noted that in the ledger.")

(sp/action "Victor stands at the whiteboard, drawing a diagram of the south"
           "road. It has more arrows than it needs.")

 (sp/dialogue "GUILDMASTER [VICTOR]"
              ["The Collegium has received a complaint about the"
               "toll bridge on the south road. The keeper has not"
               "filed his returns for three weeks. This is a"
               "matter of civic importance, and the Collegium has"
               "entrusted it to us."])

 (sp/dialogue "DEREK"
              "Us? Or you?")

 (sp/dialogue "VICTOR"
              ["Us, Derek. The Fedora Society acts as one body."
               "That is what a society is."])

 (sp/dialogue "MILES" "I thought a society was just a club with a"
              "constitution.")

 (sp/dialogue "VICTOR"
              ["It is a body, Miles. A body with a constitution."
               "A constitution is the skeleton of the body. We are"
               "the muscles."])

 (sp/beat "Derek raises an eyebrow but says nothing. Simon leans back in his"
          "chair, relaxed, like a man who has been on many toll roads and"
          "found them all the same.")

 (sp/dialogue "SIMON" "So we go south, collect the missing returns,"
              "and come back. Easy.")

 (sp/dialogue "VICTOR"
              ["It is never easy, Simon. The toll keeper is a"
               "small man named Undertaker. He has worked that"
               "bridge for years, and he knows every inch of the"
               "road. He will talk. He will delay. He will offer"
               "you tea. Do not drink the tea."])

 (sp/dialogue "DEREK"
              "Why not?")

 (sp/dialogue "VICTOR"
              ["Because tea is for civilians. You are collectors."
               "Collectors do not drink tea."])

 (sp/action "Miles writes down: 'No tea.' He writes it in a new column that he"
            "draws without asking.")

 (sp/dialogue "VICTOR"
              ["You leave at dawn. The animal will accompany you."
               "It has been restless all week, which means the"
               "road is not right."])

 (sp/action "At the mention of the animal, {ac} looks up from the floor"
            "beside {pc}'s chair. Then it puts its head back down and closes"
            "its eyes.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE SOUTH ROAD — IN THE FICTION")

 (sp/action "The road is old and wet. The party walks two by two. Victor"
            "describes the countryside: low hills, grey grass, the sound of"
            "water somewhere out of sight.")

 (sp/dialogue "DEREK"
              ["This reminds me of the Via Appia. The Romans built"
               "roads like this, straight and true. They lasted"
               "forever."])

 (sp/dialogue "SIMON"
              "This road is curved.")

 (sp/dialogue "DEREK"
              ["It curves because of the hills. The Via Appia went"
               "around hills too. The Romans were not stupid."])

 (sp/dialogue "MILES" "Are we sure this road is on the map? I don't"
              "remember a south road.")

 (sp/dialogue "VICTOR"
              ["It is on the map, Miles. You are just not looking"
               "at the right map."])

 (sp/action "Miles writes down: 'Map discrepancy noted.'")

 (sp/action "The animal stops. It stands in the middle of the road, looking"
            "toward the bend ahead. Then it sits down.")

 (sp/dialogue "PALADIN [SIMON]"
              "What's wrong with it?")

 (sp/dialogue "RANGER [{pc}]"
              "I think it sees something.")

 (sp/dialogue "VICTOR"
              ["Roll for the animal's instincts. It knows the road"
               "better than any of you."])

 (sp/stage "Roll for what the animal senses on the road."))

;; ---

^:kindly/hide-code
(dice/check
 {:id "road-instinct" :label "The animal, on the south road" :dc 11
  :fumble (str "The animal lies down and refuses to move. Victor says, \"It "
               "isn't tired. It's warning you.\"|"
               "@VICTOR|"
               "The road ahead is quiet. Too quiet. Nothing moves, and the "
               "wind has stopped.")
  :fail (str "The animal whines, once, then walks on. It doesn't look back.|"
             "@VICTOR|"
             "The road is just a road. But there are no birds, and the grass "
             "is very still.")
  :success (str "The animal walks ahead, then stops at the bend and looks "
                "back at {pc}.|"
                "@VICTOR|"
                "There's a toll booth ahead, and a light on inside it, and a "
                "smell like old water and pine sap.")
  :crit (str "The animal moves to the far side of {pc} and presses against "
             "his leg.|"
             "@VICTOR|"
             "Something has walked this road recently. Something that didn't "
             "leave footprints. The animal can't tell you what, because it "
             "doesn't know words like that. But it knows the road is old, and "
             "that the old road never forgot how to be dangerous.")})

^:kindly/hide-code
(sp/scene
 (sp/action "The toll booth is a stone hut with a wooden window, and the"
            "window is closed. The road bends around it. The animal stops"
            "again, this time at the threshold of the booth, and will not go"
            "further.")

 (two/at-the-booth)

 (sp/dialogue "DEREK"
              "Why won't it go in?")

 (sp/dialogue "SIMON"
              "Maybe it smells the tea.")

 (sp/dialogue "VICTOR"
              ["The animal does not drink tea. It does not enter"
               "toll booths. It knows the toll is not a toll."])

 (sp/action "They knock. The window opens. Undertaker is a small man, or a"
            "thin man folded into a small shape. He looks at them like a man"
            "who has already paid.")

 (sp/dialogue "UNDERTAKER"
              "The Collegium sent you.")

 (sp/dialogue "DEREK"
              "We're here to collect.")

 (sp/dialogue "UNDERTAKER"
              ["Then you'll need the form. They always send the"
               "form. They send the form and the two men who carry"
               "it, and one of them reads while the other one"
               "watches my hands."])

 (sp/dialogue "MILES"
              "We don't have a form.")

 (sp/dialogue "UNDERTAKER"
              "Then you're can't be from the Collegium.")

 (sp/beat "The window closes. The road is too quiet again.")

 (sp/dialogue "VICTOR"
              ["He is correct. The Collegium did not send a form."
               "That is why they sent you."])

 (sp/dialogue "SIMON"
              "Because we don't know any better.")

 (sp/dialogue "VICTOR"
              "Because you can be sent back.")

 (sp/action "The window opens again. Undertaker looks at the animal sitting in"
            "the road.")

 (sp/dialogue "UNDERTAKER"
              "It won't come to the window. It knows the toll.")

 (sp/dialogue :pc
              "What is the toll?")

 (sp/dialogue "UNDERTAKER" "A name. One from each of you. It can be"
              "anyone. Someone you would give up.")

 (sp/beat "The party looks at one another. The animal has not moved.")

 (sp/dialogue "DEREK"
              "Marcus Antonius. He was a liar and a thief.")

 (sp/dialogue "UNDERTAKER"
              ["That name is already in the box. You'll need"
               "another."])

 (sp/stage "Roll for the toll. What the table rolled is in the box.")

 (sp/action "Derek thinks. He thinks the way a man thinks when he has already"
            "spent his best name and is now being asked to spend another.")

 (sp/dialogue "DEREK"
              "Gaius Octavius.")

 (sp/dialogue "UNDERTAKER"
              "Also in the box.")

 (sp/dialogue "DEREK"
              "How many Romans do you have in that box?")

 (sp/dialogue "UNDERTAKER"
              "Most of them.")

 (sp/beat "Simon leans forward. He has not been worried this whole time, and"
          "he is not worried now.")

 (sp/dialogue "SIMON" "I don't have a name to give. I've never wanted to"
              "give anyone up.")

 (sp/dialogue "UNDERTAKER [VICTOR]"
              ["Then you may pass. The toll is not for everyone."
               "Only for those who have someone they would give."])

 (sp/dialogue "MILES"
              "That seems unfair.")

 (sp/dialogue "UNDERTAKER"
              ["The road is unfair. It is old. It was here before"
               "the Collegium, and it will be here after All the"
               "Collegium is, is a paragraph in a ledger."])

 (sp/beat "Miles writes that down. It takes him a moment longer than it"
          "should, because he is not sure if it should be written in the"
          "ledger or somewhere else.")

 (sp/dialogue "MILES" "I could give the name of the man who approved the"
              "road. He's probably dead, but a name is a name.")

 (sp/dialogue "UNDERTAKER"
              ["Dead names are acceptable. Some say they're the"
               "best kind."])

 (sp/action "The window closes. Then it opens again, and the undertaker sets a"
            "small box on the sill. It is a wooden box, very old, with a slot"
            "in the top.")

 (sp/dialogue "UNDERTAKER"
              ["Speak into the box. If you lie, it will know. If"
               "you tell the truth, it will know that too. It is"
               "the only part of the road that still remembers"
               "what the road was for."])

 (sp/action "Derek looks at the box. Then he looks at Victor.")

 (sp/dialogue "DEREK"
              "Is this in the rules?")

 (sp/dialogue "VICTOR"
              "It is in the road.")

 (sp/stage "Roll for what happens when the box is opened.")


^:kindly/hide-code
(dice/check
 {:id "toll-box" :label "Listening · the box on the sill" :dc 13
  :fumble (str "{pc} rolls a one.|"
               "@VICTOR|"
               "You bend to the slot and say hm, the noise anybody makes "
               "bending down, and the box takes it. The slot closes on the "
               "sound. Somewhere inside, very small, your own voice says hm, "
               "and then says it again, a little more like you each time.")
  :fail (str "{pc} rolls {result-word}.|"
             "@VICTOR|"
             "It is a box. It is very old, and the edges of the slot are worn "
             "smooth the way a step is worn, by a great many people going the "
             "same way. Whatever it does, it does not do it where you can "
             "hear.")
  :success (str "{pc} rolls {result-word}.|"
                "@VICTOR|"
                "Inside the box, a long way down for a box that size, there "
                "are voices. Hundreds of them, each saying one name, and then "
                "saying it again with the same breath in the same place, the "
                "way a saved day says it.")
  :crit (str "{pc} rolls a twenty, and Victor puts his pen down.|"
             "@VICTOR|"
             "Inside the box there are voices, hundreds of them, each saying "
             "a name. One of them is yours. It is saying a name you have not "
             "said yet.")})

 (sp/action "The party is still deciding what to say into the box when the"
            "curtain behind Undertaker shifts. Undertaker does not turn"
            "around, but his hands go still on the window ledge.")

 (sp/action "A girl steps out. She is their age, maybe a little younger. She"
            "is dressed in layers of green and brown that do not look like"
            "they belong to the road. She holds a small, folded page against"
            "her chest, as if it is the only part of her that is still dry.")

 (sp/dialogue "THE GIRL"
              ["Don't speak into the box. It doesn't just remember"
               "names. It remembers voices. Once it has your"
               "voice, it can call you back to the road whenever"
               "it wants."])

 (sp/beat "The party is quiet. Victor is not quiet.")

 (sp/dialogue "VICTOR"
              "She's not in the module.")

 (sp/dialogue "DEREK"
              "She's not in the ledger, either.")

 (sp/dialogue "MILES"
              "How do you know?")

 (sp/dialogue "DEREK"
              "She's not dressed like someone in a ledger.")

 (sp/action "The undertaker sighs. It is the sigh of a man who has kept a"
            "secret for too long and is almost relieved to be found out.")

 (sp/dialogue "UNDERTAKER"
              ["She came across the bridge three weeks ago,"
               "carrying a page from a book and a cough that"
               "wouldn't stop. I let her stay. The forms for"
               "letting her stay are worse than the forms for"
               "filing returns, so I filed the returns wrong."
               "That's all. That's the whole crime."])

 (sp/dialogue "SIMON"
              "And the toll of names?")

 (sp/dialogue "UNDERTAKER" "That's part of the bridge. I don't make the bridge."
              "I only keep it.")

 (sp/action "The girl takes a step forward. The animal, which has not moved"
            "from the threshold, rises and walks to stand beside her. It does"
            "not growl. It does not wag. It just stands there, as if it has"
            "been waiting for her to come out.")

 (sp/dialogue "SIMON"
              "What's your name?")

 (sp/dialogue "THE GIRL" "I don't give it. Not to collectors. Not to men who"
              "carry boxes with slots in them.")

 (sp/dialogue "DEREK"
              "Then what do we call you?")

 (sp/dialogue "THE GIRL" "I'm a druid. You can call me that. It's not a"
              "name, but it's the truest thing I have.")

 (sp/beat "Derek looks at Miles. Miles looks at Victor. Victor looks at the"
          "ceiling, as if the word 'druid' is one he has seen in a footnote"
          "and never expected to say out loud.")

 (sp/dialogue "VICTOR" "Druid. That's a class in the old sense. The one"
              "that talks to trees.")

 (sp/dialogue "DEREK" "Trees? Like the things in the margins of the"
              "old maps?")

 (sp/dialogue "SIMON"
              "I thought those were a metaphor.")

 (sp/dialogue "MILES" "The word 'forest' appears in the ledger three"
              "times. All in margins. All in different hands.")

 (sp/action "The girl looks at them for a long moment. She looks at Miles,"
            "then at Derek, then at Simon, then at {pc}. When she speaks"
            "again, her voice is quieter, but no less sure.")

 (sp/dialogue "THE GIRL"
              "You really don't know what a forest is.")

 (sp/beat "It is not a question.")

 (sp/dialogue "THE GIRL"
              ["It's not a metaphor. It's not a map. It's a place"
               "with a thousand voices and no names, and it's"
               "dying. That's what I'm trying to save. The last"
               "one. And the page is the only part of it that can"
               "still speak."])

 (sp/action "She holds out the page, just enough for them to see. It is not"
            "paper. It is a leaf from a codex, broad and veined, with letters"
            "that look like they have grown there. Derek leans in, and the"
            "girl pulls it back against her chest.")

 (sp/dialogue "DEREK"
              "Is it a leaf or a page?")

 (sp/dialogue "THE GIRL"
              "Yes.")

 (sp/action "The patrol arrives before anyone can ask what that means.")

 (sp/action "There is no sound. No marching feet. No horns. Just a change in"
            "the air, and then a man in the road, and then two more behind"
            "him. They are wearing the colours of the Collegium, and they are"
            "carrying a form.")

 (sp/dialogue "PATROL LEADER"
              ["Collectors. The road is closed. The bridge is to"
               "be cleared. The girl is to come with us."])

 (sp/dialogue "SIMON"
              "She doesn't seem to want to go.")

 (sp/dialogue "PATROL LEADER"
              "She isn't asked.")

 (sp/action "The animal turns. It does not growl. It simply places itself"
            "between the girl and the patrol, and it does not move.")

 (sp/dialogue "PATROL LEADER"
              "Call off your beast.")

 (sp/dialogue :pc
              "It isn't a beast. It's my companion.")

 (sp/dialogue "PATROL LEADER" "It is the same thing. Call it off or we"
              "will.")

 (sp/stage "Roll for the standoff. What the table rolled is in the box.")

 (dice/check
  {:id "standoff" :label "The standoff at the toll bridge" :dc 13
   :fumble (str "{pc} rolls {result-mod} and the animal does not move. The "
                "patrol "
                "leader smiles.|"
                "@VICTOR|"
                "The patrol leader says, \"It's loyal. That's worse.\" Then he "
                "raises his hand, and the two behind him lower their spears.")
   :fail (str "{pc} rolls low ({result-mod}). The patrol leader steps "
              "forward, and the "
              "animal holds, but it is outnumbered.|"
              "@VICTOR|"
              "The standoff does not break. It bends. The party is going to "
              "have to move quickly.")
   :success (str "{pc} rolls {result-mod}. The animal stands perfectly still, "
                 "and the patrol leader hesitates.|"
                 "@VICTOR|"
                 "For a long moment, nobody moves. Then the patrol leader "
                 "says, \"This isn't the road. It's the bridge.\" And he turns "
                 "his men away.")
   :crit (str "{pc} rolls a twenty. The animal takes a single step forward, "
              "and the patrol leader's horse shies.|"
              "@VICTOR|"
              "The patrol leader does not shout. He does not order an attack. "
              "He just looks at the animal, and then at {pc}, and he says, "
              "\"You have until the next bell. Then I come back with more than "
              "a form.\"")})

 (sp/action "The patrol is gone. The road is quiet again, but not the good"
            "kind of quiet. The kind that comes before more men.")

 (sp/dialogue "THE GIRL" "We have to go. The bridge won't keep them"
              "long. It's old, but it isn't loyal.")

 (sp/dialogue "DEREK"
              "Loyal?")

 (sp/dialogue "THE GIRL" "The bridge only keeps what it's been paid to"
              "keep. The Collegium underpaid it years ago.")

 (sp/action "The undertaker is already at the window, pulling a latch that"
            "opens a small gate at the side of the booth.")

 (sp/dialogue "UNDERTAKER"
              ["There's a path behind the booth. It follows the"
               "old road, not the new one. The new road goes"
               "south. The old road goes somewhere else."])

 (sp/dialogue "SIMON"
              "Where does it go?")

 (sp/dialogue "UNDERTAKER"
              ["It goes to the place the road used to be before"
               "the Collegium decided where roads should go."])

 (sp/action "The girl looks at the party. Then she looks at {pc}.")

 (sp/dialogue "THE GIRL"
              ["Your animal already knows the way. It won't cross"
               "the bridge because it can smell the old road"
               "underneath. That's why it sat down."])

 (sp/dialogue "MILES"
              "I thought it was because of the tea.")

 (sp/dialogue "THE GIRL" "There is no tea. Tea hasn't crossed this"
              "bridge in twenty years.")

 (sp/beat "Derek looks at the empty cups on the undertaker's sill.")

 (sp/dialogue "DEREK"
              "Then what were we supposed not to drink?")

 (sp/dialogue "UNDERTAKER"
              ["The water. The water comes from the old road. It"
               "remembers the road. If you drink it, you remember"
               "it too."])

 (sp/action "The girl is already at the side gate. She motions for the party"
            "to follow.")

 (sp/dialogue "THE GIRL"
              "The animal knows. Let it lead.")

 (sp/stage "Roll for the animal to find the old road.")

 (dice/check
  {:id "old-road" :label "The animal and the old road" :dc 12
   :fumble (str "{ac} starts down the path, then stops and circles back. It "
                "sits at the toll booth and refuses to move.|"
                "@VICTOR|"
                "The path is a dead end. The old road is under the bridge, "
                "not behind it, and the girl looks like she's about to cry.")
   :fail (str "{ac} leads them off the path and into the brush. The going is "
              "slow, and the girl keeps looking back at the road.|"
              "@VICTOR|"
              "They find a track, but it's narrow and flooded, and the patrol "
              "will have no trouble following.")
   :success (str "{ac} moves to the side of the toll booth, sniffs the "
                 "ground, and then slips through a gap in the hedge.|"
                 "@VICTOR|"
                 "Beyond it, there is an old drovers' track, half-hidden by "
                 "brambles. The girl exhales. \"It knows.\"")
   :crit (str "{ac} walks to the edge of the bridge, then turns and follows "
              "the old road under the arch. There is no path there, but the "
              "animal doesn't hesitate.|"
              "@VICTOR|"
              "The girl laughs. It is not a happy sound. It is the sound of "
              "someone who has been alone for a long time and has just "
              "remembered what company feels like.")})

 (sp/action "They follow the animal. The old road is not a road in the"
            "traditional sense. It is a memory of a road, and it goes where"
            "the new road cannot.")

 (sp/action "Behind them, the toll booth is still. The undertaker has closed"
            "the window. There is no sound from the bridge, and there will not"
            "be until the next bell.")

 (sp/action "The girl walks beside {pc}. The leaf is still against her chest."
            "She does not speak for a while.")

 (sp/dialogue "THE GIRL" "I didn't think I'd ever see a real animal"
              "again. Not one that knew the old roads.")

 (sp/dialogue :pc
              "What do you mean, a real animal?")

 (sp/dialogue "THE GIRL"
              ["The ones in the Collegium are made. They're copies"
               "of the idea of an animal. Yours isn't. Yours came"
               "from somewhere else. It knows the old road because"
               "it was born on it."])

 (sp/beat "Derek, from behind them:")

 (sp/dialogue "DEREK" "Are we still collecting the toll? Because I"
              "think we've gone a little past the toll.")

 (sp/dialogue "SIMON" "We're not collecting anything. We're running."
              "We're doing it quietly, but we're running.")

 (sp/dialogue "MILES"
              ["I have to record that. 'The party is no longer on"
               "the south road. The party is on the old road. This"
               "was not in the ledger.'"])

 (sp/dialogue "VICTOR"
              ["Write it in the new column. The one we made for"
               "things that are true but not approved."])

 (sp/action "Miles does. It is the first time the column has been used for"
            "something that is not a joke.")

 (sp/action "The old road goes on. It is not straight. It curves like the land"
            "had curves before the Collegium graded them. The girl stops once,"
            "and the animal stops with her. They are standing at the edge of a"
            "treeline.")

 (sp/action "The trees are not like the trees in the margins of old maps. They"
            "are taller, and darker, and the leaves are heavy with water that"
            "has not fallen.")

 (sp/dialogue "THE GIRL"
              "We're here.")

 (sp/dialogue "SIMON"
              "It's a forest.")

 (sp/dialogue "THE GIRL"
              "It's the last one.")

 (sp/beat "The party stands at the edge of the forest. The animal walks a few"
          "steps into the trees, then turns and looks back at them, as if to"
          "say, 'This is what I've been trying to tell you.'")

 (sp/dialogue "VICTOR"
              ["And that is where we will end for tonight."
               "Because I need to read what a druid actually does,"
               "and I don't want to do it in front of you."])

 (sp/beat "Nobody laughs. Derek is still looking at the trees.")

 (sp/dialogue "DEREK"
              "I thought forests were a myth.")

 (sp/dialogue "SIMON"
              "So did I.")

 (sp/dialogue "MILES" "The word 'forest' appears in the ledger three"
              "times. All in margins. All in different hands.")

 (sp/action "The girl looks back at them from the treeline. She is not"
            "smiling. But she is not running anymore.")

 (sp/transition "Blackout.")

;; ---

 (sp/slug "EXT. CORLISS FIELD — FRIDAY, 4:30 P.M.")

 (sp/action "{pc} is crossing the field because it is the short way back from"
            "Plant Services and he has a torch in his pocket and a bucket in"
            "his hand.")

 (sp/action "A machine at the far end fires punts. It is on a trolley and"
            "somebody has to keep feeding it and nobody is feeding it, so it"
            "goes off every forty seconds whether or not anybody is ready.")

 (sp/action "CHAD is under them. Two freshmen are trying to be.")

 (sp/action "It fires. A FRESHMAN runs to a place on the grass and stands on"
            "it and puts his hands up, and the ball comes down eight feet"
            "behind him.")

 (sp/dialogue "CHAD"
              "Again.")

 (sp/action "Chad takes the next one himself. Two short steps back, a wait,"
            "one more, a wait, about a foot to his left, and he has it against"
            "his chest without appearing to have gone anywhere at all.")

 (sp/dialogue "A FRESHMAN"
              "How do you know where it's going?")

 (sp/dialogue "CHAD"
              ["I don't. It's still going up. Nobody knows where"
               "it is yet, including it."])

 (sp/dialogue "CHAD"
              ["Take half of what's left. Then look at it again."
               "Then take half of that."])

 (sp/beat "He says it the way you say a thing you have said four thousand"
          "times.")

 (sp/action "He notices {pc} on the touchline.")

 (sp/dialogue "CHAD"
              "Thinking part!")

 (sp/action "{pc} has both programmes in his bag. He has had them in his bag"
            "since September and has taken them out about eleven times.")

 (sp/dialogue :pc
              "Can I ask you something.")

 (sp/dialogue "CHAD"
              "Go on.")

 (sp/action "He gets the September sheet out and holds it where Chad can see"
            "it and puts his thumb on the line.")

 (sp/dialogue :pc
              "Thirty.")

 (sp/beat "Chad looks at it for about two seconds.")

 (sp/dialogue "CHAD"
              "Yeah. Bell.")

 (sp/dialogue :pc
              "He's not on the October one.")

 (sp/dialogue "CHAD"
              "No. He went in September.")

 (sp/dialogue :pc
              "Went where?")

 (sp/action "The machine fires. Chad does not look at it. It comes down forty"
            "feet away and rolls.")

 (sp/dialogue "CHAD"
              ["Reviewed. He was carrying from last year and he"
               "didn't get it back."])

 (sp/dialogue :pc
              "In September? Review's in December.")

 (sp/dialogue "CHAD"
              "Review's in December for you. We go in June.")

 (sp/dialogue :pc
              "Why June?")

 (sp/dialogue "CHAD"
              ["Because you can't review a squad in season. They"
               "did it years ago and lost four in a November and"
               "the season went, so now it's June, and by June"
               "you've had two more terms to be fine in."])

 (sp/beat "He says this the way you would say the away kit is white.")

 (sp/dialogue :pc
              "So Bell got reviewed in September because—")

 (sp/dialogue "CHAD"
              ["Because he came off the squad in August. Knee."
               "Once you're off, you're on the December one like"
               "everybody else, except he was carrying two and it"
               "was September, so they didn't wait."])

 (sp/beat "{pc} stands there with a programme in his hand.")

 (sp/dialogue :pc
              "They didn't wait.")

 (sp/dialogue "CHAD"
              "No.")

 (sp/dialogue :pc
              "Chad. Where did he go?")

 (sp/action "The machine fires. Chad takes a short step and waits and takes"
            "another and catches it, and holds it, and does not throw it back.")

 (sp/dialogue "CHAD"
              "I want to say home. Everybody says home.")

 (sp/dialogue :pc
              "Do you think it's home?")

 (sp/beat "Four seconds, which is longer than Chad has taken over anything all"
          "year.")

 (sp/dialogue "CHAD"
              ["I think if it was home he'd have said something to"
               "somebody. He had a locker with a picture in it."
               "Somebody cleared the locker and it wasn't him."])

 (sp/beat "The machine fires. Neither of them looks at it.")

 (sp/dialogue "CHAD"
              ["Sixty a day since I was nine. It's not a skill,"
               "it's just the sixty."])

 (sp/action "He goes back under them. {pc} folds the programme and puts it in"
            "the bag with the other one and carries a bucket back across a"
            "field.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "Friday night. He has Victor's dice tin, which he was given on"
            "Wednesday and has not given back. A corner is a one, a two, or a"
            "three.")

 (def corners
   [[0 0] [100 0] [50 86.6]])

 (defn halfway [[x1 y1] [x2 y2]]
   [(/ (+ x1 x2) 2.0) (/ (+ y1 y2) 2.0)])

 (sp/action "Start in the middle. Take half of what is left. Look again. He"
            "rolls the die thirty times and types in what it says, and it is a"
            "way of not thinking about a locker.")

 (def rolls
   [3 1 2 2 3 1 1 3 2 3 3 2 1 2 1 3 3 1 2 2 3 1 3 2 1 1 2 3 2 3])

 (defn dots-from-rolls [rolls]
   (reduce (fn [acc r] (conj acc (halfway (peek acc) (nth corners (dec r)))))
           [[50 40]]
           rolls))

 (count (dots-from-rolls rolls))

 (sp/action "Thirty dots is a mess with no shape in it, which is what thirty"
            "of anything looks like.")

 (sp/action "So he asks the page for the die instead, and gives it a number,"
            "so that he can put in as many as he likes and see what happens at"
            "each size.")

 ^:kindly/hide-code
 (defn dots
   "n dots. Start in the middle, pick a corner, go half the way to it,
  mark it, and do it again from there.

  The first dozen are thrown away: the walk starts wherever you put it
  and takes a few steps to arrive at the shape, and until it has
  arrived it leaves marks in places the shape does not go."
   [n]
   (->> (reduce (fn [acc _] (conj acc (halfway (peek acc) (rand-nth corners))))
                [[50 40]]
                (range (+ n 12)))
        (drop 13)
        vec))

 ^:kindly/hide-code
 (defn draw [n]
   (kind/hiccup
    [:svg {:viewBox "-4 -4 108 95" :width 400
           :xmlns "http://www.w3.org/2000/svg"}
     [:rect {:x -4 :y -4 :width 108 :height 95 :style {:fill "var(--paper)"}}]
     [:path {:stroke-width 0.7 :stroke-linecap "round"
             :style {:fill "none" :stroke "var(--ink)"}
             :d (str/join " " (map (fn [[x y]]
                                     (str "M" (format "%.2f" (double x))
                                          "," (format "%.2f" (double (- 86.6 y)))
                                          "h0.01"))
                                   (dots n)))}]]))

 (draw 200)

 (sp/action "Two hundred. Nothing yet, or nearly nothing.")

 (draw 4000)

 (sp/action "Four thousand, and there it is, and there is no drawing of any"
            "kind anywhere in it. Nobody told it to make triangles. It was"
            "told to take half of what was left and look again, four thousand"
            "times, and the shape is what is left when you do that for long"
            "enough.")

 (sp/action "The first version had four dots in the middle where the middle is"
            "supposed to be empty, and he sat with that for most of an hour"
            "before working out that they were the first four, from before it"
            "had got where it was going. He throws away the first dozen. He"
            "does not throw away the hour.")

 (sketch/cell
  {:id "chaos" :label "CHAOS.CLJS" :world [100 86.6] :rows 16}
  "(def corners [[0 0] [100 0] [50 86.6]])

(defn halfway [[x1 y1] [x2 y2]]
  [(/ (+ x1 x2) 2) (/ (+ y1 y2) 2)])

(def here (atom [50 40]))

;; 4000 dots, 40 of them a frame
(frames! 4000 40
  (fn [i]
    (swap! here halfway (rand-nth corners))
    (when (> i 12)
      (dot (first @here) (second @here)))))")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "{pc}" :at "11:58 PM" :text "got the dots one"}
   {:who "Miles" :at "11:59 PM" :text "what was it"}
   {:who "{pc}" :at "12:00 AM" :text "you go half way to a corner. then half way to another corner"}
   {:who "Derek" :at "12:01 AM" :text "and that makes a shape?"}
   {:who "{pc}" :at "12:01 AM" :text "yeah"}
   {:who "Derek" :at "12:02 AM" :text "why"}
   {:who "{pc}" :at "12:09 AM" :text "idk"}
   {:who "{pc}" :at "12:14 AM" :text "does anyone know a Bell. football. number 30"}
   {:who "Miles" :at "12:31 AM" :text "no"}
   {:who "Derek" :at "12:33 AM" :text "no"}
   {:who "Simon" :at "1:02 AM" :text "no but i know what youre doing"}
   {:who "Simon" :at "1:02 AM" :text "ive got two of them"}
   {:who "Victor" :role "PRESIDENT" :at "8:00 AM"
    :text ["Order is what is left when you stop interfering. It's the"
           "oldest result there is and they don't teach it because of"
           "what it implies."
           "Well done. Bring it Thursday."]}]))