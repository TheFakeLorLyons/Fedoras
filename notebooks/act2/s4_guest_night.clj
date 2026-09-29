^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 4 — Guest Night"}}}
(ns act2.s4-guest-night
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.puzzle.puzzle :as puzzle]))

^:kindly/hide-code
(sp/assets)

;; # Scene 4 — Guest Night

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 14)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. HALLORAN UNION — THE PRINT SHOP — THURSDAY, 5:40 P.M.")

 (sp/action "Two presses, a guillotine, and a counter with a sign on it that"
            "says WEIGHT OF PAPER IS NOT A DESIGN DECISION, which somebody has"
            "printed and mounted and hung themselves.")

 (sp/action "LENA NOVAK is running a poster job for the Theatre Guild and is"
            "not happy with the second colour. She is a transfer student. She"
            "has been here five weeks.")

 (sp/action "ELI BROOKS is leaning on the counter watching her work in the way"
            "people lean on counters watching somebody who is better at"
            "something than they are.")

 (sp/dialogue "ELI"
              "It's fine.")

 (sp/dialogue "LENA"
              ["It's four points off and it'll look like a mistake"
               "from across the corridor."])

 (sp/dialogue "ELI"
              "It is a mistake from across the corridor.")

 (sp/action "{pc} is at the far end of the counter waiting for a requisition"
            "slip for Plant Services, which takes half an hour and cannot be"
            "done any other way.")

 (sp/dialogue "ELI" "seeing him"
              "Two buckets!")

 (sp/dialogue :pc
              "Hi.")

 (sp/dialogue "ELI"
              ["Lena. This is the one from Bennett's who doesn't"
               "say anything."])

 (sp/dialogue "LENA" "not looking up"
              "What did you make of Bennett?")

 (sp/dialogue :pc
              "I don't know yet.")

 (sp/dialogue "LENA"
              "That's about right. Give it a term.")

 (sp/action "She pulls a sheet, holds it up to the light, and puts it in the"
            "bin, and starts the run again.")

 (sp/dialogue "ELI"
              ["Lena's from somewhere with better print shops than"
               "this."])

 (sp/dialogue "LENA"
              "Everywhere has better print shops than this.")

 (sp/dialogue "ELI"
              "Where, though.")

 (sp/beat "She does not answer. She has not answered it any of the four times"
          "he has asked, and he asks it the way you ask a question you have"
          "decided to keep asking.")

 (sp/dialogue "LENA" "to {pc}, not to Eli"
              "What's the slip for?")

 (sp/dialogue :pc
              "A torch bulb.")

 (sp/dialogue "LENA"
              "How long does a torch bulb take here?")

 (sp/dialogue :pc
              "Two weeks.")
 
 (sp/action "MAYA comes in for a printout, pays for it, and is reading page"
            "four at the counter before the door has shut behind her.")
 
  (sp/dialogue "MAYA" "not looking up"
               "Page four's wrong.")
 
  (sp/dialogue "LENA"
               "The press is fine.")
 
  (sp/dialogue "MAYA"
               "The number is.")
 
  (sp/action "{pc} is at the far end of the counter waiting for a requisition"
             "slip for Plant Services, which takes half an hour and cannot be"
             "done any other way. Eli sees him and lifts two fingers off the"
             "counter. {pc} lifts two back.")
 
  (sp/action "The slip comes through. It has taken half an hour to print and"
             "it is for a torch bulb, and along the bottom, in the same type"
             "as everything else, it says ALLOW FOURTEEN DAYS.")
 
  (sp/action "He folds it into his pocket and makes for the door.")
 
  (sp/dialogue "MAYA" "not looking up"
               "Where are you off to?")
 
  (sp/dialogue :pc
               "The stairwell.")
 
  (sp/beat "Maya looks up from page four for the first time since she came in.")
 
  (sp/dialogue "MAYA"
               "On a Thursday.")
 
  (sp/action "She goes back to page four. He goes.")
 
  (sp/transition "Cut to:")
 
  (sp/slug "INT. HALLORAN UNION — THE STAIRWELL LOUNGE — 6:50 P.M.")
 
  (sp/action "The couch. The burnt table. The wall of flattened pizza boxes."
             "Two lamps out of five.")
 
  (sp/action "BRACE is on the couch, in the middle of it, feet apart, coat"
             "still on. Second year. Dark curls, a good jaw, and the build a"
             "university photographs when it needs a photograph.")
 
  (sp/dialogue "BRACE"
               "So what am I, then?")
 
  (sp/dialogue "VICTOR"
               "You're a client.")
 
  (sp/dialogue "BRACE"
               "What's a client?")
 
  (sp/dialogue "VICTOR"
               "It's the correct word.")
 
  (sp/dialogue "BRACE"
               "Right, but what is it?")
 
  (sp/dialogue "VICTOR"
               ["You're attached to the Society through Derek."
                "You come, you sit, you're spoken to. You aren't a"
                "member and you aren't pretending to be, and nobody"
                "in this stairwell has to be unkind to you about"
                "it, which is the whole reason it's written down."])
 
  (sp/beat "Brace looks at Derek.")
 
  (sp/dialogue "DEREK"
               "It's Roman.")
 
  (sp/dialogue "BRACE"
               "Sound.")
 
  (sp/action "DEREK has been explaining the campaign to him for twenty"
             "minutes. MILES is refilling the trays. SIMON is sitting on the"
             "arm of the couch because Brace is in his place.")
 
  (sp/action "ELI, LENA and MAYA come down the stairs on their way out of the"
             "building with a tube of posters and stop, because {pc} is"
             "sitting there.")
 
  (sp/dialogue "ELI" "genuinely surprised"
               "Oh. This is you?")
 
  (sp/beat "{pc} does not have an answer to that.")
 
  (sp/dialogue "VICTOR" "standing, hand out, entirely pleasant"
               "Victor Ashcroft. You're welcome to sit down.")
 
  (sp/dialogue "LENA"
               "We're going. Thanks.")
 
  (sp/dialogue "VICTOR"
               "That's a Theatre Guild job. Who's setting those?")
 
  (sp/dialogue "LENA"
               "I am.")
 
  (sp/dialogue "VICTOR"
               ["You've got the kerning wrong on the date, then."
                "Second line."])
 
  (sp/dialogue "LENA"
               "Four points. I know.")
 
  (sp/beat "Victor had a second sentence ready and does not get to use it.")
 
  (sp/action "Brace has not been listening to any of this. He has been looking"
             "at Maya since she came down the stairs, the whole of her, taking"
             "his time about it.")
 
  (sp/dialogue "BRACE" "to Victor"
               "Is he with them?")
 
  (sp/beat "It takes a second for the room to catch up with what he has done.")
 
  (sp/dialogue "ELI"
               "She.")
 
  (sp/dialogue "BRACE" "still to Victor"
               "Is it? You're the one with the correct words.")
 
  (sp/dialogue "LENA" "flat, to Eli, already turning"
               "Don't.")
 
  (sp/dialogue "ELI"
               ["No, I'd like to hear him say it. Go on. There are"
                "four witnesses."])
 
  (sp/dialogue "VICTOR"
               ["I'm not going to be pushed into a scene about"
                "grammar. I don't dislike anybody in this stairwell"
                "and I've never claimed to. I use the words that"
                "are accurate and I'd say the same thing about a"
                "table."])
 
  (sp/dialogue "MAYA"
               "I'm not a table.")
 
  (sp/dialogue "VICTOR"
               "No. Obviously.")
 
  (sp/beat "That is the whole of Victor's part in it. He never raises his"
           "voice and he never says anything else.")
 
  (sp/dialogue "MAYA" "to Lena and Eli"
               "Come on.")
 
  (sp/action "She turns for the door. She does it without hurrying, the way"
             "you leave a room you have left before.")
 
  (sp/action "BRACE gets up off the couch. He has been waiting for a reason"
             "since about the fifth minute of Derek's campaign summary and"
             "this is one.")
 
  (sp/dialogue "BRACE" "to her back"
               "Night, lads.")
 
  (sp/action "{pc} is already up. He is not thinking about anything and he has"
             "no idea what he is going to do, and what he does is hit Brace in"
             "the mouth, which is the first time anybody in this book has hit"
             "anybody.")
 
  (sp/action "Brace hits him back once, high, mostly on the ear, and they both"
             "go down into the burnt table, and the table goes over, and the"
             "pizza-box wall goes with it. It takes four seconds.")
 
  (sp/action "MILES has his hands up. DEREK has not moved. SIMON pulls Brace"
             "off by the collar of the coat and is astonished at himself for"
             "the rest of the term.")
 
  (sp/action "Maya is standing on the bottom stair. She has not moved at any"
             "point, and she is the only person in the stairwell who is not"
             "surprised by any of it.")
 
  (sp/action "THE JANITOR comes out of the corridor with a bin liner in one"
             "hand and stands there.")
 
  (sp/dialogue "THE JANITOR"
               "Right.")
 
  (sp/transition "Blackout.")

;; ---

 (art/email
  {:from "Office of Student Conduct <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "Friday, 4:00 AM"
   :subject "Outcome — incident 4471"}
  "An incident was recorded in Halloran Union on Thursday evening. Statements were taken. No further statements are required."
  "You are assigned four sessions of supervised study, beginning Monday, 6:00 PM, Beckwith 2."
  "Supervised study is not a component of review. Supervised study is recorded."
  "This matter is closed.")

 (sp/action "Nothing in it says what he did, or what anybody else did, or"
            "which of the two of them the four sessions are for.")

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Miles" :at "9:12 AM" :text "is brace coming thursday"}
   {:who "Derek" :at "9:40 AM" :text "brace is off the roster"}
   {:who "Miles" :at "9:41 AM" :text "off the roster how"}
   {:who "Derek" :at "9:41 AM" :text "off it"}
   {:who "Miles" :at "9:44 AM" :text "he was second year"}
   {:who "Derek" :at "10:02 AM" :text "yes"}
   {:who "Simon" :at "10:30 AM" :text "and {pc} got four evenings"}
   {:who "Simon" :at "10:31 AM" :text "for the same thing"}
   {:who "Victor" :role "PRESIDENT" :at "11:58 AM"
    :text ["It was not the same thing and I would ask people to be"
           "careful about how that is put."
           "Article Three is suspended for the remainder of the term."
           "There will be no clients."]}
   {:who "Simon" :at "12:04 PM" :text "thats not what i meant"}
   {:who "Miles" :at "2:15 PM" :text "does the stairwell go in the minutes"}
   {:who "Victor" :role "PRESIDENT" :at "2:31 PM" :text "Article Five."}
   {:who "Miles" :at "2:32 PM" :text "so it didnt happen"}
   {:who "Victor" :role "PRESIDENT" :at "2:32 PM" :text "Article Eleven."}
   {:who "Miles" :at "2:40 PM" :text "ok"}])

 (sp/action "The minutes for that Thursday are kept short and three of them"
            "are about the projector. Miles wrote them and holds on to them"
            "and, he is also the only person who will ever read them.")

;; ---

 (sp/slug "INT. PRENTISS SCIENCE CENTER — THE BENCH ROOM — SATURDAY")

 (sp/action "Where the researchers eat, which {pc} has never been in before"
            "and is not, strictly, allowed in.")

 (sp/action "MAYA is at the far bench with a printout. LENA is on the counter"
            "beside her with a coffee, which is also not allowed.")

 (sp/action "{pc} has an ear that has gone a colour and a slip in his pocket"
            "that says Beckwith 2, Monday, six o'clock.")

 (sp/dialogue "MAYA" "not looking up"
              "That was you, then.")

 (sp/dialogue :pc
              "It wasn't anything.")

 (sp/dialogue "MAYA"
              ["Four evenings isn't nothing. Sit down, you're"
               "making the room look untidy."])

 (sp/action "He sits down. A postgraduate comes in for something out of a"
            "cupboard, looks at the three of them, takes the thing, and goes.")

 (sp/dialogue "MAYA" "still not looking up"
              "Nobody's asked you for a card.")

 (sp/dialogue :pc
              "Should they?")

 (sp/dialogue "MAYA"
              "They asked me twice in my first month.")

 (sp/action "She turns a page of the printout.")

 (sp/beat "Nobody says anything for a while and it is not uncomfortable, which"
          "surprises him.")

 (sp/dialogue "LENA"
              ["I've been at three institutions. This is the"
               "fourth. Do you want to know what happens at all"
               "four of them?"])

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "LENA"
              ["Nothing happens. Somebody says something in a"
               "stairwell, and everybody in the stairwell agrees"
               "afterwards that it was a shame, and the person who"
               "said it goes on being in the stairwell every"
               "Thursday for six years."])

 (sp/beat "She drinks the coffee.")

 (sp/dialogue "LENA"
              ["The one who put his hands on me is gone by Friday"
               "morning and I'm not going to pretend to you that"
               "I'm sorry about it. And you got four evenings for"
               "standing still."])

 (sp/dialogue :pc
              "Right.")

 (sp/dialogue "LENA"
              ["No, work it out. Which of those three is the one"
               "the university minds about?"])

 (sp/beat "{pc} works it out and does not say it.")

 (sp/dialogue "MAYA"
              "He's got detention, Lena, not a viva.")

 (sp/dialogue "LENA"
              "He asked.")

 (sp/action "MAYA goes along the bench to deal with something that has been"
            "beeping, and stands with her back to them, and does not go any"
            "further than that.")

 (sp/beat "Lena watches him not say it for a while longer.")

 (sp/dialogue "LENA"
              "You didn't say it out loud.")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "LENA"
              ["Good. Then I'll tell you a thing and you can do"
               "the same with it."])

 (sp/beat "She puts the coffee down, which she has not done since he came in.")

 (sp/dialogue "LENA"
              "I'm not from anywhere.")

 (sp/dialogue :pc
              "Everybody's from somewhere.")

 (sp/dialogue "LENA"
              ["That's a sentence people say who are from"
               "somewhere."])

 (sp/action "She works out how to put it, and it takes her a while, and it is"
            "not because it is painful.")

 (sp/dialogue "LENA"
              ["It was rooms. That's the whole of what I've got."
               "Rooms, and then more of them, and the same carpet"
               "in all of them, and a noise the whole time that I"
               "didn't know was a noise until I came out and it"
               "stopped."])

 (sp/dialogue :pc
              "How many rooms?")

 (sp/dialogue "LENA"
              ["That's the question everybody asks and it isn't"
               "one. You're asking me how many rooms there are in"
               "a place where a room is what there is."])

 (sp/beat "He does not have anything for that.")

 (sp/dialogue "LENA"
              ["The lights never went off. There wasn't a night."
               "I had the word before I had the thing."])

 (sp/dialogue :pc
              "So how did you—")

 (sp/dialogue "LENA"
              ["There was a door that hadn't been there. I've gone"
               "over it about four thousand times and that's still"
               "all I've got: a door that hadn't been there, and"
               "people, and they were in grey, and they were kind,"
               "and they were in a hurry."])

 (sp/dialogue :pc
              "Who were they?")

 (sp/dialogue "LENA"
              ["I don't know. There was a form. The form said"
               "Meridian at the top and I thought Meridian was the"
               "word for them, the way you'd think a word. I was"
               "six."])

 (sp/beat "Behind them Maya has stopped doing anything to the thing that was"
          "beeping.")

 (sp/dialogue "LENA"
              ["Novak's off the form. So's Lena, near enough."
               "Somebody wrote both of them down in a van and I've"
               "answered to them since, and they're mine now,"
               "because what else would they be."])

 (sp/dialogue :pc
              "And then school.")

 (sp/dialogue "LENA"
              ["And then a different school, and then a different"
               "one, and now this. I've never applied to anything"
               "in my life. A letter comes and I go."])

 (sp/dialogue :pc
              "Can you say no?")

 (sp/beat "That is the first question she has liked.")

 (sp/dialogue "LENA"
              ["I don't know. I've never wanted to be anywhere"
               "badly enough to find out, and I'd rather not find"
               "out by accident."])

 (sp/dialogue :pc
              "What was it? The place.")

 (sp/dialogue "LENA"
              ["I've read everything on the supplement I'm"
               "approved for and a good deal that I'm not. There"
               "is a word for every single thing I saw in there."
               "There isn't one for the whole of it."])

 (sp/beat "She picks the coffee back up.")

 (sp/dialogue "LENA"
              ["And now you know more about me than Eli does, and"
               "he's asked four times, and I'd like that to stay"
               "the arrangement."])

 (sp/dialogue :pc
              "It will.")

 (sp/dialogue "LENA"
              ["We'll see. That isn't an insult, it's what the"
               "word means."])

 (sp/action "Maya comes back along the bench and turns the printout round so"
            "that {pc} can see it, for no reason connected with any of this,"
            "and puts a finger on a number on page four.")

 (sp/dialogue "MAYA"
              ["That's the correction factor and it's out. Not by"
               "a rounding. By a factor."])

 (sp/dialogue "LENA"
              "Tell Carter.")

 (sp/dialogue "MAYA"
              "No.")

 (sp/dialogue "LENA"
              "Why not?")

 (sp/dialogue "MAYA"
              ["Because I'd be right. And then I'd be the one who"
               "was right."])

 (sp/action "She turns the page over and starts telling him about something"
            "else on it, and he does not understand most of it, and stays"
            "until the room is dark.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE STAIRWELL LOUNGE — THE FOLLOWING THURSDAY")

 (sp/action "The table has been righted. The pizza-box wall has been rebuilt,"
            "badly, and is lower than it was.")

 (sp/dialogue "VICTOR" "handing him a tray"
              "How's the ear?")

 (sp/dialogue :pc
              "Fine.")

 (sp/dialogue "VICTOR"
              ["I want to say — and I'm not going to make a"
               "thing of it — that I thought what you did was"
               "correct. You stood up for a girl. That's not"
               "nothing. Most people here wouldn't."])

 (sp/beat "{pc} looks at him.")

 (sp/dialogue "VICTOR"
              ["Brace was never going to be a fit. I said that at"
               "the time."])

 (sp/beat "He did not say that at the time. He asked Derek one question about"
          "him and the question was whether Derek wanted him.")

 (sp/action "He sits down and gets the folder out and reads the minutes of the"
            "last meeting, and his hands are not entirely steady and nobody at"
            "the table mentions it, and he does not mention the stairwell"
            "again for the rest of the year.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "Monday. Beckwith 2, six o'clock. Four evenings of supervised"
            "study, which means sitting in a room with a member of staff who"
            "marks a sheet at the start and a sheet at the end and does not"
            "look up in between.")

 (sp/action "No Helps. There is a poster about it. What he has outstanding is"
            "assignment five, which is fifteen numbers and a hole, and he has"
            "had it in pieces for a fortnight.")

 (sp/action "Sixteen squares. Fifteen of them have a number in them and one"
            "has nothing in it, and that is the only place anything can go.")

 ^:kindly/hide-code
 (puzzle/widget))