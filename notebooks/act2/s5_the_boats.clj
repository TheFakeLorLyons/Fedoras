^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 5 — The Boats"}}}
(ns act2.s5-the-boats
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.shoot.shoot :as shoot]))

^:kindly/hide-code
(sp/assets)

;; # Scene 5 — The Boats

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 30)

^:kindly/hide-code
(sp/scene
 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "9 Oct, 11:48 PM"
    :text "Attendance five of five. Friday was the one with the boats in the ice."}
   {:who "Derek" :at "16 Oct, 11:12 PM" :text "that one came back four minutes short"}
   {:who "Victor" :role "PRESIDENT" :at "23 Oct, 11:50 PM"
    :text ["Attendance five of five. Wednesday ran to the Janitor, as usual."
           "Nine weeks of the campaign and we have not left the third floor."]}
   {:who "Miles" :at "30 Oct, 11:59 PM" :text "docket again. sorry"}
   {:who "Simon" :at "6 Nov, 1:02 AM" :text "the boy in 2 north didnt come back after review week"}
   {:who "Simon" :at "6 Nov, 1:03 AM" :text "someone else is in there now. hes fine. hes nice"}
   {:who "Victor" :role "PRESIDENT" :at "6 Nov, 8:01 AM" :text "Article Fourteen."}
   {:who "Victor" :role "PRESIDENT" :at "20 Nov, 11:44 PM"
    :text "Attendance five of five. Twelve weeks. Nobody has missed a Thursday since September."}])

 (sp/action "Twelve Thursdays. Twelve Wednesdays. Ten Fridays, because the"
            "projector was double-booked once and Victor took it to the"
            "counter and lost.")

 (sp/action "Two dockets, both of which he attended, both of which were forty"
            "minutes in a room with nine other people and a woman with a"
            "clipboard, and neither of which is worth writing down, which is"
            "what everybody says about them.")

 (sp/action "Sixteen mornings at six with Hollis. The pipe by the stair does"
            "not stop and the second bucket does not go away. In the middle of"
            "October somebody empties both of them, and it is not Hollis and"
            "it is not {pc}, and neither of them mentions it.")

 (sp/action "Set one goes in on the third Friday of October, six of six. Set"
            "two opens on the Monday. The four evenings of supervised study"
            "are spread across five weeks, because Beckwith 2 is only free"
            "when it is free.")

 (sp/action "The number holds. Somebody on the second floor does not come back"
            "after the October review and the room has somebody in it by the"
            "Friday.")

;; ---

 (sp/slug "INT. BECKWITH HALL — ROOM 2 — THE LAST EVENING, 20 NOVEMBER")

 (sp/action "Supervised study. Nine desks, four of them occupied, and a member"
            "of staff at the front who marks a sheet at six and a sheet at"
            "eight and does not look up in between.")

 (sp/action "No Helps. There is a poster about it, and under the poster a"
            "radiator that comes on at ten past six and goes off at seven.")

 (sp/action "The room was something else before it was this. Along the back"
            "wall there are nine wooden cabinets, chest height, in banks of"
            "drawers about the size of a brick, and people put their bags on"
            "them.")

 (sp/action "He opened one in October because it was there, and because a"
            "drawer that has not been opened in a long time comes out in the"
            "way that tells you.")

 (sp/action "They are full of cards. Thousands of them, upright, packed tight,"
            "each one typed, each one with a number in the top left corner.")

 (sp/beat "Tonight is the fourth evening and he has been along the same drawer"
          "three times.")

 (sp/dialogue "THE SUPERVISOR" "without looking up"
              "Don't take them out of order.")

 (sp/dialogue :pc
              "What are they?")

 (sp/dialogue "THE SUPERVISOR"
              ["It's a catalogue. There's a cabinet of them in"
               "half the rooms in this building. People put their"
               "bags on them."])

 (sp/dialogue :pc
              "A catalogue of what?")

 (sp/beat "The supervisor looks up for the first time in four evenings.")

 (sp/dialogue "THE SUPERVISOR"
              "Of the books, love.")

 (sp/action "{pc} takes the page out of his bag. It has been folded into eight"
            "since September and the folds have gone soft. On the reverse"
            "there is a paper pocket, and above the dates it says 005.13 ·"
            "OAKES.")

 (sp/action "He goes along the drawers until he finds the one with the range"
            "on the front that has 005 in it, and then along the cards.")

 (sp/beat "It takes forty minutes and he finds it at ten to eight.")

 (sp/transition "Cut to:")

;; ---

 (art/handout
  "005.13   —   catalogue card   —   NEW CARTHAGE"
  "STRUCTURE AND ORDER IN PROGRAMS · v.2 · ed. 4"
  "acc. 11-4402   ·   accessioned, stamped, and stamped again nine years later"
  "COPIES: 3"
  "LOCATION: OAKES — withdrawn"
  "LOCATION: OAKES — withdrawn"
  "LOCATION: C-3 — held"
  "In pencil, along the bottom edge, in a hand that is not the typist's: everything below C-2 went with the ground.")

 (sp/action "There is no building C. The current map goes B, D, and B and D"
            "are touching, and {pc} sat in a room on a Wednesday night in"
            "October and heard a man in a felt hat say exactly that about a"
            "place he had invented.")

 (sp/action "He puts the card back in order, because he was asked to, and"
            "takes a photograph of it on the way, and adds one line to a list"
            "that already has page 219 on it.")

;; ---

 (sp/slug "INT. HALLORAN UNION — ROOM 3C — FRIDAY 21 NOVEMBER")

 (sp/action "Lights off. Two boxes of pizza. The projector.")

 (sp/sign "THE BANKS"
          "approved · 96 min · as supplied, 96 min")

 (sp/dialogue "DEREK"
              "It's the only one that comes whole.")

 (sp/action "It opens on a man on a slipway reading a noticeboard with a race"
            "on it. The film gives him forty seconds of that and then goes"
            "somewhere else and does not come back to it for an hour and a"
            "half.")

 (sp/action "The race is an amateur thing. A dozen boats, run every year off a"
            "coast the film never names. The man and his wife sail one of the"
            "small ones.")

 (sp/action "There is a long stretch in the middle where nothing goes wrong.")

 (sp/action "They practise in flat water. They argue about a sail and stop"
            "arguing. She goes forward and does something to the mast with a"
            "spanner and comes back, and he does not ask.")

 (sp/action "Twice she calls a turn before he does and he takes it, and once"
            "he does not, and they lose the boat they were beating, and"
            "neither of them says anything about it for the rest of the"
            "afternoon.")

 (sp/action "In a kitchen she reads a tide table out loud and says a time. He"
            "is doing something else. The film does not go back to it.")

 (sp/beat "Miles stops eating at about the fortieth minute.")

 (sp/action "The water on {pc}'s block went off in February and stayed off for"
            "two days. It had never done that in his life. It had done it a"
            "great deal in his mother's, and she said so, and she said it the"
            "way you say a thing that is over."

            "That is the entire reason he is in this building, on this couch,"
            "in the dark, watching two people rig a boat on a trailer.")

 (sp/action "On the day the wind is up and they are late off the slipway, and"
            "the film does not say why and neither of them mentions it.")

 (sp/action "Some of the fleet takes the long way round the head. Some of it"
            "goes inside the bank. The ones who go inside are ahead by the"
            "second mark and the film shows the gap opening.")

 (sp/action "There is a shot of the hull from the water. There is a weed line"
            "on it and the weed line is out.")

 (sp/action "They hit.")

 (sp/action "There is no spectacle in it. A sound, the deck stops being level,"
            "and the mast goes over the side with the rigging and the mainsail"
            "attached to it.")

 (sp/action "He goes over the bow.")

 (sp/action "He comes up ten feet from the boat and finds his footing and the"
            "water is at his chest, and he has taken the coral across the shin"
            "and the inside of the leg on the way in and does not know it yet.")

 (sp/action "She is still aboard. She gets a rope to him on the second throw.")

 (sp/action "He gets a hand on it. He cannot use the leg and the bottom is"
            "coral heads and holes, and she takes his whole weight twice and"
            "holds it both times.")

 (sp/beat "For about forty seconds it is a film about a man being pulled out"
          "of the water by his wife.")

 (sp/action "Then the wave comes through.")

 (sp/action "It lifts the hull and puts it down in a different place, and she"
            "goes over the side with the rope still in both hands.")

 (sp/action "The mainsail is in the water and full of it, and she comes up"
            "underneath it.")

 (sp/action "He gets to her. It is twenty feet and he goes down twice in it.")

 (sp/action "Her head is out. He gets the sail off her shoulder and her head"
            "was already out, and it does not help.")

 (sp/action "Her leg is through the shrouds below the knee. The wire has taken"
            "a turn round a coral head and the hull is on the other end of it.")

 (sp/action "The water is going.")

 (sp/action "He works at the wire with both hands. The film stays on the hands"
            "for a long time and there is nothing else in the frame.")

 (sp/action "The hull settles about four inches.")

 (sp/beat "She stops helping him and puts a hand flat on his chest.")

 (sp/dialogue "THE WOMAN"
              ["Under the thwart. Port side. There's a knife under"
               "the thwart."])

 (sp/beat "It is the only line either of them has in the whole of it.")

 (sp/action "He goes back. It is twenty feet and it takes him longer than it"
            "took him coming.")

 (sp/action "The boat is on its side. He goes under twice and comes up twice"
            "and the second time he has it.")

 (sp/beat "The film goes with him and does not cut back to her once.")

 (sp/action "He cuts the wire. It takes three goes and the third one is"
            "two-handed.")

 (sp/action "He gets her free and gets her up onto the coral head and holds"
            "her there out of the water.")

 (sp/beat "He does not know yet.")

 (sp/action "The film stays with him not knowing for a length of time that"
            "people walked out of cinemas over.")

 (sp/action "The other boats are outside the bank. They cannot come in over"
            "the coral. Two of them go round the head. The film cuts to them"
            "four times and they are not much closer on the fourth.")

 (sp/action "Two men from the first of them get to him.")

 (sp/action "They take him. He is the one bleeding and he is the one they can"
            "reach, and he goes over the side of their boat with a man under"
            "each arm and one of them talking to him the whole way, kindly, in"
            "a voice you would use on a horse.")

 (sp/beat "He is shouting. It is the only shouting in the film.")

 (sp/action "Then it cuts back. The slipway. The noticeboard. Forty seconds of"
            "a man reading a list of rules and putting his name on it.")

 (sp/slug "THE FILM — INT. A KITCHEN — SIX WEEKS EARLIER")

 (sp/dialogue "THE MAN"
              "I've put us in.")

 (sp/dialogue "THE WOMAN"
              "You've what?")

 (sp/dialogue "THE MAN"
              ["It's forty boats. Half of them are worse than us"
               "and the other half will take the long way round"
               "because their insurance says to."])

 (sp/dialogue "THE WOMAN"
              "And the bank?")

 (sp/dialogue "THE MAN"
              ["Everybody goes inside the bank. It's four feet at"
               "the top of the tide and we draw three."])

 (sp/beat "She looks at him for a while.")

 (sp/dialogue "THE WOMAN"
              "What time's the start?")

 (sp/dialogue "THE MAN"
              "Ten.")

 (sp/dialogue "THE WOMAN"
              "And the top of the tide?")

 (sp/beat "He does not answer that one.")

 (sp/dialogue "THE MAN"
              "It'll be fine.")

 (sp/beat "She says all right. The film ends on her saying all right.")

 (sp/action "Nobody in Room 3C says anything for a while. Victor lets the"
            "credits run.")

 (sp/dialogue "MILES" "eventually"
              "He should have gone for the knife straight away.")

 (sp/dialogue "DEREK"
              "He had a leg open on coral, Miles.")

 (sp/dialogue "MILES"
              ["I know that. I'm saying he was in the water two"
               "minutes before she told him where it was. He'd"
               "have made it."])

 (sp/dialogue "DEREK"
              "You don't know that.")

 (sp/dialogue "MILES"
              "You could time it.")

 (sp/beat "Nobody answers that.")

 (sp/dialogue "VICTOR"
              ["What it's about is the men who took him out of the"
               "water. He's standing there holding her and they"
               "decide for him. Nobody in that film asks him"
               "anything."])

 (sp/action "Victor sits back. Derek nods slowly, twice.")

 (sp/dialogue "SIMON" "quietly"
              "It's about the kitchen.")

 (sp/dialogue "VICTOR"
              "The kitchen's two minutes long.")

 (sp/dialogue "SIMON"
              "Yes.")

 (sp/action "Miles puts the mask on. Nobody has asked him anything and nobody"
            "asks him to take it off, and he keeps it on for the rest of the"
            "evening, including on the stairs, including outside.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. PRENTISS SCIENCE CENTER — THE BENCH ROOM — SATURDAY 22 NOVEMBER, LATE")

 (sp/action "The bench room is where researchers work and eat, one long bench"
            "each, name on the end of it. Two lamps on out of eight.")

 (sp/action "MAYA has been here since four. There is a flask in a heated bath"
            "at the end of her bench with a thermometer in it, and it has to"
            "be read and written down every twenty minutes for nine hours, and"
            "between the readings it does not need anybody at all.")

 (sp/action "LENA is on the counter with her boots off. The press prints onto"
            "sheets bigger than the poster, so every one comes out with two"
            "inches of white round the edge that has to come off. The machine"
            "that does that lives in the print shop and the print shop shuts"
            "at ten, so she has forty of them, a steel rule and a scalpel.")

 (sp/dialogue "LENA"
              "Twenty past. Go and read your flask.")

 (sp/action "Maya reads the thermometer and writes the number in a column of"
            "the same number going back to four o'clock.")

 (sp/dialogue "ELI" "from the floor, where he is lying with a coat over him"
              "Somebody say something. I'm going under.")

 (sp/dialogue "MAYA"
              "Say something, Lena.")

 (sp/dialogue "LENA"
              ["All right. The first time I ever saw weather"
               "coming I thought somebody had done it."])

 (sp/dialogue "ELI" "still on the floor"
              "Done what?")

 (sp/dialogue "LENA"
              ["The weather. I thought somebody had put it on and"
               "I wanted to know who, and I asked, and it turns"
               "out that's a stupid question."])

 (sp/dialogue "ELI"
              "Where was this?")

 (sp/beat "She does not answer, which is the fifth time.")

 (sp/action "{pc} looks at the bench in front of him and does not say"
            "anything, because he knows the whole of it and was asked not to,"
            "and because he would like to keep the arrangement.")

 (sp/action "At one o'clock Eli goes to bed and Lena goes with the poster tube"
            "under her arm and one boot in each hand.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE BENCH ROOM — 1:40 A.M.")

 (sp/action "Two of them and a run that has to be looked at every twenty"
            "minutes.")

 (sp/dialogue "MAYA"
              "Rock, paper, scissors. Loser goes at two.")

 (sp/dialogue :pc
              "All right.")

 (sp/beat "She wins four out of five.")

 (sp/dialogue :pc
              "Again.")

 (sp/beat "She wins three out of four.")

 (sp/dialogue :pc
              ["That isn't possible. It's a third each. It's a"
               "third each every time."])

 (sp/dialogue "MAYA"
              ["It's a third each if you're a coin. You're not a"
               "coin, you're a person, and people do the same"
               "thing after they lose."])

 (sp/dialogue :pc
              "What do I do after I lose?")

 (sp/dialogue "MAYA"
              "You play the thing that would have beaten me.")

 (sp/beat "{pc} sits with that for a moment.")

 (sp/dialogue :pc
              "Hang on.")

 (sp/action "He opens the machine and writes something with her reading over"
            "his shoulder, and neither of them looks at the run at two"
            "o'clock, and it is fine, and nobody ever finds out.")

 (sp/stage "The program he wrote is below.")

;; ---

 (sp/action "A third each, if you are a coin.")

 ^:kindly/hide-code
 (sp/code
  "(def beats-what
  {:rock :scissors
   :paper :rock
   :scissors :paper})

(defn judge [mine theirs]
  (cond
    (= mine theirs)                :draw
    (= theirs (beats-what mine))   :win
    :else                          :lose))

(judge :rock :scissors)

(defn coin [_history]
  (rand-nth [:rock :paper :scissors]))")

 (sp/action "That one cannot be beaten and cannot win. Over a long enough"
            "evening it is a third each, and the evening is the only thing"
            "that happens.")

 (sp/action "Then the other one, which is hers, written down by him.")

 ^:kindly/hide-code
 (sp/code
  "(defn what-beats [x]
  (first (filter (fn [k] (= x (beats-what k))) (keys beats-what))))

(defn reader
  \"They played a thing last time and it lost. People play the thing that
  would have beaten you. So play the thing that beats that.\"
  [history]
  (if-let [last-round (last history)]
    (let [{:keys [theirs mine]} last-round]
      (if (= :lose (judge theirs mine))
        (what-beats (what-beats theirs))
        (coin history)))
    (coin history)))

(defn play-round [history theirs]
  (let [mine (reader history)]
    (conj history {:mine mine 
                   :theirs theirs 
                   :result (judge mine theirs)})))

(def evening
  (reduce play-round [] [:rock :rock 
                         :paper :paper 
                         :scissors :rock 
                         :rock]))

(map :result evening))")

 (sp/action "It is not clever and against a coin it is worth nothing at all."
            "Against a person who has just lost, it is worth about four points"
            "in ten, and four points in ten is the difference between a game"
            "and a habit.")

 (sp/action "Here it is, playing. Throw against it. It has nothing but what"
            "you did last time, and for the first two or three throws that is"
            "nothing at all.")

;; The real game

 (shoot/widget)

;; ---

 (sp/slug "INT. THE BENCH ROOM — 2:20 A.M.")

 (sp/action "It beats her twice.")

 (sp/beat "Then she stops and looks at the ceiling for a moment.")

 (sp/dialogue "MAYA"
              "When you take one off me, it expects me to change.")

 (sp/dialogue :pc
              ["It expects you to play the thing that would have"
               "won."])

 (sp/dialogue "MAYA"
              ["Yes. So when you take one off me, I'll play the"
               "same thing again."])

 (sp/beat "She does that, and wins, and does it again, and wins.")

 (sp/action "She beats it for the rest of the run and is delighted about it"
            "and says so at length.")

 (sp/dialogue "MAYA"
              ["That's the whole of it, though. You made a thing"
               "that watches somebody. It only works while they"
               "don't know it's there."])

 (sp/dialogue :pc
              "Four points in ten.")

 (sp/dialogue "MAYA"
              "Four points in ten is enormous. Ask anybody here.")

 (sp/action "She turns the lamp off at her end because the run is finished,"
            "and does not go, and neither does he.")

 (sp/beat "Neither of them says anything for a while.")

 (sp/action "She kisses him. Neither of them makes it complicated.")

 (sp/beat "Then she is talking and he is not answering.")

 (sp/dialogue "MAYA"
              "Hey.")

 (sp/action "He is looking at a point somewhere past her left shoulder and his"
            "mouth is doing something small and repetitive that he does not"
            "appear to know about.")

 (sp/dialogue "MAYA"
              "Hey. Look at me.")

 (sp/beat "Four seconds.")

 (sp/action "Then he goes sideways off the stool and takes the stool with him.")

 (sp/action "He is already rigid before he lands. His head goes back and stays"
            "back. There is a sound that Maya will not describe to anybody"
            "afterwards, not to Lena, not to Eli, not to him.")

 (sp/action "She gets the stool away, drags the bag over, puts it under his"
            "head, and clears the bench beside him with her arm.")

 (sp/action "She does not hold him down and she does not put anything in his"
            "mouth, because there is a sheet on the wall of every teaching lab"
            "in this building and she has walked past it twice a week since"
            "September.")

 (sp/action "She looks at the clock and holds it, and this is the part that"
            "costs her: she stands still with her hand on the floor beside his"
            "shoulder and does nothing at all for ninety seconds while it"
            "happens.")

 (sp/beat "Then it stops.")

 (sp/action "He is breathing. His eyes are open. He is not there.")

 (sp/dialogue "MAYA"
              "Okay. Okay.")

 (sp/action "She gets her Help out and it takes her two goes to make it"
            "listen, because her hands are not steady and the first thing she"
            "says to it is not a sentence.")

 (sp/dialogue "MAYA" "into it, too loud"
              ["Prentiss. The bench room, third floor. Somebody"
               "needs to come now."])

 (sp/dialogue "MAYA"
              "Now. Now, please.")

 (sp/beat "Nobody comes for nineteen minutes.")

 (sp/action "She stays on the floor with him for all nineteen of them and"
            "talks to him the entire time, and he does not answer any of it,"
            "and she keeps going.")

 (sp/transition "Curtain."))