^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 2 — Your Own"}}}
(ns act4.s2-your-own
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.sketch :as sketch]))

^:kindly/hide-code
(sp/assets)

;; # Scene 2 — Your Own

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 7)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. HALLORAN UNION — THE SENATE OFFICE — TUESDAY 10 MARCH, 1:15 P.M.")

 (sp/action "A room with a counter across the middle of it and three people"
            "behind the counter and a queue of nobody.")

 (sp/action "ASHLEY is at the end with a screen and a stack of forms squared"
            "off beside her.")

 (sp/dialogue "ASHLEY"
              "You're Bennett's back row.")

 (sp/dialogue :pc
              "I need a room.")

 (sp/dialogue "ASHLEY"
              "Which one and what for?")

 (sp/dialogue :pc
              ["Beckwith. Any room on the third floor with the"
               "cabinets in it. Reading."])

 (sp/beat "She is already typing.")

 (sp/dialogue "ASHLEY"
              ["Reading isn't a use. Quiet study is a use."
               "Tuesdays and Thursdays, six till nine?"])

 (sp/dialogue :pc
              "That'd do it.")

 (sp/dialogue "ASHLEY"
              ["It's yours to the end of term. You want it under"
               "your name or under a society?"])

 (sp/dialogue :pc
              "Mine.")

 (sp/dialogue "ASHLEY"
              ["Mine's better. A society booking gets looked at in"
               "April when they do the room audit and a student"
               "booking never does."])

 (sp/action "She turns the screen round for a second so that he can see it is"
            "done, which nobody in this university has ever done for him.")

 (sp/dialogue :pc
              "Thanks.")

 (sp/dialogue "ASHLEY"
              ["I said come and find me if you were stuck with the"
               "Senate."])

 (sp/dialogue :pc
              "You did.")

 (sp/dialogue "ASHLEY"
              ["People don't. I've been saying it since second"
               "year and about six people have."])

 (sp/beat "She squares the forms off again.")

 (sp/dialogue "ASHLEY"
              "Array Four in June. Have I told you that?")

 (sp/dialogue :pc
              "You have.")

 (sp/dialogue "ASHLEY"
              "Yes. Sorry.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "Beckwith 3 is a teaching room with nine cabinets along the back"
            "wall and bags on four of them at any hour of the day, because"
            "people put their bags on them.")

 (sp/action "The drawers come out about the width of a brick and there is no"
            "way of doing this quickly, and after the first evening he stops"
            "trying to do it quickly.")

;; ---

 (sp/slug "INT. BECKWITH 3 — THURSDAY 12 MARCH, 6:40 P.M.")

 (sp/action "LENA comes because he asked her and not because anybody made a"
            "plan about it.")

 (sp/action "She looks at what he is doing for about a minute and then takes"
            "the Help out of his hand.")

 (sp/dialogue "LENA"
              "You're shooting down at it.")

 (sp/dialogue :pc
              "It's on a table.")

 (sp/dialogue "LENA"
              ["Then the light's behind you and your own head is"
               "in every one of them. Stand it up."])

 (sp/action "She props a drawer open against a book, at an angle, facing the"
            "window, and shoots along it instead of down at it.")

 (sp/dialogue "LENA"
              ["Front, turn it, back, put it down in the same"
               "place. Front, turn it, back. If you break the"
               "rhythm you'll lose one and you won't know which."])

 (sp/dialogue :pc
              "How do you know all this?")

 (sp/dialogue "LENA"
              ["Because a poster is a thing you photograph badly"
               "eleven times before somebody tells you where to"
               "stand."])

 (sp/beat "They do a drawer together and it takes forty minutes.")

 (sp/dialogue "LENA"
              "How many drawers?")

 (sp/dialogue :pc
              "In this room, about a hundred and forty.")

 (sp/dialogue "LENA"
              "And in the building?")

 (sp/beat "He does not answer that and she does not ask again.")

 (sp/action "She stays until nine and comes back on the Tuesday without being"
            "asked, and on the Thursday after that, and neither of them ever"
            "calls it a shift.")

 (sp/transition "Blackout.")

;; ---

 (art/handout
  "005.13   —   catalogue card   —   NEW CARTHAGE"
  "STRUCTURE AND ORDER IN PROGRAMS · v.2 · ed. 4"
  "acc. 11-4402"
  "COPIES: 3"
  "LOCATION: OAKES — withdrawn"
  "LOCATION: OAKES — withdrawn"
  "LOCATION: C-3/S — held"
  "In pencil, along the bottom edge, in a hand that is not the typist's: everything below C-2 went with the ground.")

 (sp/action "The photographs are useless as photographs. Two thousand of them"
            "by the second week and no way of asking any of them anything.")

 (sp/action "Assignment six is YOUR OWN, and in week six he showed Reyes a"
            "thing that drew clocks and she looked at it for four seconds and"
            "said that is not one, and gave him to the end of term.")

;; ---

 (sp/slug "INT. MERIDIAN HALL — REYES 204 — MONDAY 16 MARCH")

 (sp/action "He puts it on her desk on paper because she will not take"
            "anything any other way.")

 (sp/action "She reads it the way she reads things, which is fast and twice.")

 (sp/dialogue "REYES"
              "This is one.")

 (sp/dialogue :pc
              "It's short.")

 (sp/dialogue "REYES"
              ["It's short because you found out what the job was"
               "before you started, which is the thing I have been"
               "trying to get out of thirty-one people since"
               "September."])

 (sp/beat "She turns the page over. There is nothing on the back.")

 (sp/dialogue "REYES"
              "What's it for?")

 (sp/beat "Two seconds.")

 (sp/dialogue :pc
              "It's a catalogue.")

 (sp/dialogue "REYES"
              "Whose?")

 (sp/beat "Three.")

 (sp/dialogue "REYES"
              "Right. Is it finished?")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "REYES"
              ["Then bring it when it is. Set two's closed and"
               "you're five of six and I've written down that the"
               "sixth is coming, which is a thing I do roughly"
               "once a year."])

 (sp/action "He is at the door when she says the other thing, and she does not"
            "look up for it either.")

 (sp/dialogue "REYES"
              ["Whatever it's a catalogue of, don't put it on a"
               "university machine."])

 (sp/dialogue :pc
              "Why?")

 (sp/dialogue "REYES"
              "Because they're university machines.")

 (sp/transition "Blackout.")

;; ---

(sp/slug "THE FILM — THE KITCHEN — SEVEN WEEKS LATER")

 (sp/action "Two people. Her and one friend, and the friend has come over"
            "rather than her going out, and neither of them says anything"
            "about that.")

 (sp/dialogue "THE FRIEND"
              "You're doing the voice.")

 (sp/dialogue "TESS"
              "What voice?")

 (sp/dialogue "THE FRIEND"
              "That one. You're doing it now.")

 (sp/action "She is not doing it on purpose. She tries the next sentence in"
            "her own voice and the film lets her fail at it twice.")

 (sp/beat "The friend leaves at nine.")

 (sp/action "She washes up two cups and puts them away and stands in the"
            "kitchen. Forty seconds of standing in a kitchen with nothing to"
            "do in it.")

 (sp/action "The line feeds. She has stopped noticing when it happens. She"
            "notices now, because there is nothing else to notice.")

 (sp/action "She has the thought that she is being fed, and that the person"
            "feeding her is not there, and that this is the closest thing to"
            "company she has had in three weeks.")

 (sp/action "She goes to bed at twenty past nine because she is on at six.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — HER MOTHER'S FLAT — AN AFTERNOON")

 (sp/action "Her mother has the floor on. Not the programme. The floor, which"
            "runs continuously, and which people have on the way people used"
            "to have a window. She has it tuned to Array Nine, Line Four, and"
            "the camera is angled so she can see her daughter's desk.")

 (sp/action "She is on it. Middle distance, at a desk, for about two seconds.")

 (sp/action "Her mother rings four people.")

 (sp/action "The film cuts between the four calls. Her mother is delighted in"
            "all four of them.")

 (sp/beat "Tess does not know. She has never seen the floor. She has been on"
          "it since June, and her mother has been watching her the whole time.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — DR. BECK'S OFFICE — MONTH THREE")

 (sp/action "DR. RAINER BECK's office is clean. It is the cleanest room in the"
            "film so far. There are no personal effects.")

 (sp/dialogue "BECK"
              "Your numbers are excellent.")

 (sp/dialogue "TESS"
              "Thank you.")

 (sp/dialogue "BECK" 
              ["There are some upgrades available to colleagues at"
              "your level. They're optional. Most colleagues at your"
              "level take them."])

 (sp/action "He shows her a brochure. The paper is thick. The photos show"
            "people who look like her, smiling, with small improvements"
            "circled in red.")

 (sp/dialogue "BECK" "The eyes are the most popular. Better low-light"
              ["vision. No more squinting at the screen by hour"
               "four."])

 (sp/dialogue "TESS"
              "I don't squint.")

 (sp/dialogue "BECK"
              "You will.")

 (sp/action "She agrees to the eyes. She does not know why she agrees. She is"
            "revolted by the idea and she agrees anyway, and the revulsion and"
            "the agreement sit in her chest together like two things that have"
            "learned to share the space.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — DR. BECK'S OFFICE — MONTH FOUR")

 (sp/action "The eyes are done. They are the same color as her old eyes, but"
            "they do not water when she is tired, and she has stopped being"
            "tired.")

 (sp/dialogue "BECK" "The ears next. They filter out the misters. Most"
              "colleagues find it helps them focus.")

 (sp/dialogue "TESS"
              "I've stopped noticing the misters.")

 (sp/dialogue "BECK"
              "Then you won't miss them.")

 (sp/action "She agrees to the ears. The revulsion is still there, but it is"
            "smaller now, or she is larger, and she cannot tell which.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — DR. BECK'S OFFICE — MONTH FIVE")

 (sp/action "Beck is pleased with her progress. He says the word progress and"
            "she does not flinch.")

 (sp/dialogue "BECK" "There's a new port. Better throughput. You'd notice"
              "the difference immediately.")

 (sp/dialogue "TESS"
              "What's wrong with the old one?")

 (sp/dialogue "BECK"
              "Nothing. The new one is better.")

 (sp/action "She agrees. She does not know why she agrees. She has stopped"
            "asking herself that question.")

 (sp/action "The revulsion is a familiar thing now, like the damp on her"
            "forearms that she no longer feels.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — THE BOARD")

 (sp/action "One minute thirty.")

 (sp/action "Her name is at the top. The number against it is the smallest"
            "number on the board.")

 (sp/action "Melvin announces it in front of the pod and puts a hand on the"
            "back of her chair and does not touch her, and is proud.")

 (sp/action "She has not seen her own face in a mirror in six weeks.")

 (sp/action "When she does, in the reflection of a dark screen at the end of a"
            "shift, she does not recognize the woman looking back.")

 (sp/beat "There is nobody left who would want to be told.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — THE POD — A CONVERSATION")

 (sp/action "Somebody new, four desks down, at the end of a shift.")

 (sp/dialogue "THE NEW ONE"
              "What do you do on the two weeks?")

 (sp/action "The film gives her nine seconds and does not cut. It is the"
            "longest anybody looks at anything in the whole film.")

 (sp/dialogue "TESS"
              "I don't know. What do you do?")

 (sp/dialogue "THE NEW ONE"
              "I've got people at home.")

 (sp/dialogue "TESS"
              "That's nice.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — THE FLOOR — THE CALL")

 (sp/action "She takes a call. A woman needs a procedure. The procedure has"
            "not been approved.")

 (sp/action "The woman is calm at first. She has been told to be calm.")

 (sp/dialogue "THE WOMAN ON THE PHONE" "I'm calling about the procedure."
              "The one I asked about last week.")

 (sp/dialogue "TESS" "I can see that here. The request was reviewed and"
              "the answer was no.")

 (sp/dialogue "THE WOMAN ON THE PHONE" 
              ["I know. I know it was no. I'm"
              "asking if there's anything else. Any other way to do it."
              "Anything at all."])

 (sp/action "Tess knows what the woman is asking. It is not on the list. It"
            "has never been on the list. The word for it is not on the list"
            "either, and she is not permitted to say it, and she does not know"
            "how to talk around it in a way that is not a lie.")

 (sp/action "She thinks about the sac. She thinks about the rows of them, and"
            "the children inside them, and the way they are lifted out when"
            "they are ready. She thinks about what it would mean to carry one"
            "and not want it.")

 (sp/dialogue "TESS"
              "I'm sorry. There's nothing I can do.")

 (sp/dialogue "THE WOMAN ON THE PHONE" 
              ["Please. There has to be"
              "something. I can't—I can't do this. I can't go through"
              "with it. Please."])

 (sp/action "The woman is crying now. Tess is very good at her job. She is"
            "warm and she is exact. She does not pretend to have any power.")

 (sp/dialogue "TESS"
              "I'm sorry. The account has been noted.")

 (sp/action "The woman says something that is not on the list. Tess does not"
            "react. She is very good at her job.")

 (sp/action "The call ends.")

 (sp/beat "She does not take the next one.")

 (sp/action "The light on the pole goes from one state to another and stays"
            "there for a count of about forty. The two nearest desks notice."
            "Neither of them looks up.")

 (sp/action "Melvin comes down the aisle at the speed of somebody on a"
            "four-minute cycle.")

 (sp/action "He is very gentle. He is gentler than anybody has been to her in"
            "the whole film.")

 (sp/action "She is smiling.")

 (sp/beat "The misters come on.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — A ROOM OFF THE FLOOR")

 (sp/action "Beck has her file open. He is younger than she expected, and he"
            "has been her doctor for two years, and she has never seen him"
            "outside this room.")

 (sp/dialogue "BECK"
              "You stopped mid-shift.")

 (sp/dialogue "TESS"
              "I know.")

 (sp/dialogue "BECK"
              "For four minutes.")

 (sp/dialogue "TESS"
              "I know.")

 (sp/action "He turns the file round so she can see the graph. Her aftercall"
            "over five months. The line goes down and down and there is one"
            "spike in it and the spike is today.")

 (sp/dialogue "BECK" 
              ["There's an update for the response set. It's"
               "optional. It takes the edge off the ones that sit with"
               "you."])

 (sp/dialogue "TESS"
              "Which ones sit with me?")

 (sp/dialogue "BECK"
              "You just showed me.")

 (sp/beat "She looks at the spike.")

 (sp/dialogue "TESS"
              "All right.")

 (sp/transition "Cut to:"))

;; ---

^:kindly/hide-code
(sp/action "The last fifty minutes of the film are thirty-four years and there"
           "is no dialogue in any of it.")

^:kindly/hide-code
(sp/action "The updates come every eight or nine months and they stop being"
           "surgical after the fourth one. There is nothing to open. She sits"
           "in the chair in the room off the floor and Beck says the name of"
           "it and she says all right, and there is a period of about forty"
           "seconds where she is looking at nothing.")

^:kindly/hide-code
(sp/action "After the sixth she stops asking what they are called.")

^:kindly/hide-code
(sp/action "The film marks the years by the board. Her name is at the top of"
           "it for thirty-one of them. The names underneath it change"
           "completely four times over and hers does not move.")

^:kindly/hide-code
(sp/action "Somewhere in the second decade the calls stop having any effect on"
           "her face. The film is careful about when: it is not sudden, and"
           "there is no scene where it happens, and by the time you notice it"
           "you cannot say which year it was.")

^:kindly/hide-code
(sp/action "She is at the desk in the same posture for thirty-four years. The"
           "chair is replaced twice and she is not off it for either.")

^:kindly/hide-code
(sp/action "Her mother is at the flat with the floor on, and then her mother"
           "is not at the flat, and the film does not say what happened and"
           "neither does anybody in it.")

^:kindly/hide-code
(sp/action "The woman on the phone is at the flat. She is not at the flat. The"
           "film does not say what happened to her either.")

;; ---

^:kindly/hide-code
(art/handout
 "ARRAY NINE · SEAT 4,412 · notice of discontinuation"
 "The components at this seat are no longer manufactured."
 "Substitution has been evaluated and is not recommended."
 "The seat will be released at the end of the current shift."
 "Aftercall at release: 1:30."
 "Thank you for thirty-four years of continuous service.")

;; ---

^:kindly/hide-code
(sp/scene
 (sp/slug "THE FILM — THE FLOOR — THE LAST SHIFT")

 (sp/action "She takes calls. She is very good at it.")

 (sp/action "At the fifth hour a submanager who was not born when she started"
            "comes down the aisle and stands at the end of her row and waits"
            "for her to finish.")

 (sp/action "She finishes. The account has been noted.")

 (sp/beat "The light on the pole goes amber.")

 (sp/action "The film holds on her face.")

 (sp/action "For about a second and a half there is something in it.")

 (sp/action "Relief.")

 (sp/beat "It is gone before anybody in Room 3C can name it.")

 (sp/action "She is smiling. The film holds on that for a long time.")

 (sp/beat "The misters come on.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — A CEMETERY")

 (sp/action "A stone with her name on it and one line under the name.")

 (sp/beat "1:30")

 (sp/action "Four people from the pod are there and they are on their break"
            "and go back.")

 (sp/action "Then a card, white on black.")

 (sp/sign "The pod's aftercall average rose by four seconds"
          "in the quarter following.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 3C — CONTINUOUS")

 (sp/action "Victor lets the credits run. It is twenty past ten.")

 (sp/dialogue "MILES"
              "Thirty-one years at the top of an array board.")

 (sp/dialogue "DEREK"
              "Is that a record?")

 (sp/dialogue "MILES" "It has to be. There are four thousand seats on"
              "Nine and they turn over about every nine years.")

 (sp/action "He has worked that out during the credits and is pleased with it.")

 (sp/dialogue "MILES" "And Beck never once made her do anything. Every"
              "single one of those was offered and she took it.")

 (sp/dialogue "SIMON"
              "She'd have been about sixty at the end.")

 (sp/dialogue "MILES" "Fifty-seven. And still at one thirty. That is not"
              "a job any more, that's a—")

 (sp/beat "He looks for the word.")

 (sp/dialogue "MILES"
              "That's a calling.")

 (sp/dialogue "DEREK" "The Romans had four of those and they were all"
              "priesthoods and every one of them was for life.")

 (sp/dialogue "MILES"
              "There you are, then.")

 (sp/dialogue "DEREK" 
              ["No, listen, because the interesting part is that"
               "you could not leave. Not that you would not. There was no"
               "procedure for it. A man went in at six and came out dead"
               "and the state did not have a form for anything else."])

 (sp/dialogue "VICTOR" "Then it is a good arrangement and it lasted a"
              "thousand years.")

 (sp/dialogue "DEREK" "It lasted four hundred and then they had to start"
              "buying the priests.")

 (sp/beat "Victor lets that go, which he does not usually do.")

 (sp/action "SIMON has been on the arm of the couch since the second hour and"
            "has moved twice.")

 (sp/dialogue "SIMON"
              "It was a bit long.")

 (sp/dialogue "MILES"
              "It was three and a half hours.")

 (sp/dialogue "SIMON" "I'm not saying it was bad. It was well made. The"
              "bit with the eyes was well made.")

 (sp/beat "Nobody asks him which bit with the eyes.")

 (sp/dialogue "SIMON" 
              ["I just think if you're going to do thirty-four"
               "years you could do it in ninety minutes and get the same"
               "across."])

 (sp/dialogue "VICTOR"
              "You could not.")

 (sp/dialogue "SIMON"
              "Probably not.")

 (sp/action "He says that agreeably and goes back to looking at the screen,"
            "which is black.")

 (sp/dialogue "MILES" "The one that gets me is the young woman on the"
              "phone at the end of the first half.")

 (sp/dialogue "DEREK"
              "The one who was crying.")

 (sp/dialogue "MILES" 
              ["The way she went on. Tess had told her twice and"
              "she went on for quarter of an hour, and Tess stayed on the"
              "line the whole time, and did not raise her voice once."])

 (sp/dialogue "MILES" "I would not have been able to do it. I would have"
              "said something.")

 (sp/beat "He does not say what he would have said.")

 (sp/dialogue "SIMON" 
              ["She got through to somebody, though. That's the"
               "thing about the floor. You ring up and there is an actual"
               "person on the end of it who stays on for as long as you"
               "want."])

 (sp/dialogue "MILES"
              "She said no to her.")

 (sp/dialogue "SIMON"
              "She said no to her properly.")

 (sp/beat "Nobody follows that up.")

 (sp/action "MILES puts the mask on.")

 (sp/beat "{pc} has not said anything since the room off the floor with the"
          "chair in it.")

 (sp/action "He is thinking about the call. The woman who needed a procedure"
            "that was not approved. The woman who cried and then thanked her,"
            "and meant it, and he does not know what to do with that.")

 (sp/action "He is thinking about the sac. He is thinking about the rows of"
            "them, and the children inside them, and the way they are lifted"
            "out when they are ready. He is thinking about his mother, who"
            "carried him, and the eight years he did not know her, and the way"
            "she looked at him when he finally came home.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "Half past one. He gets in and there is something on the desk"
            "beside the tower, sitting up, tracked, the size of a large mouse,"
            "and it has been there for an unknown length of time.")

 (art/terminal
  "PIP"
  "Don't say anything out loud."
  "Put it on this and I'll take it down."
  "He read the two thousand you sent on the ninth. He has been up for"
  "most of three days and Harper has stopped arguing with him about it."
  "There are forty-one marks he wants and I have them here."
  "$")

 (sp/action "Forty-one marks. He runs them against the index, which is a thing"
            "he can do now, and which takes about a second.")

 ^:kindly/hide-code
 (sp/code
  "(def wanted
   [\"005.13\" \"016.09\" \"016.11\" \"016.4\"  \"021.7\"
    \"150.19\" \"301.4\"  \"323.44\" \"331.88\" \"355.02\"])")

 (sp/action "That is ten of them and the other thirty-one are the same and it"
            "does not matter what they are called.")

 (sp/action "Every one of them is held. Every one of them is C-3.")

 (sp/action "Every one of them is S.")

 (sp/action "South. Past the flooded end. About two hundred feet, and it is"
            "not a wall and it is not water and there is nothing there to look"
            "at.")

 (art/terminal
  "PIP"
  "Yes."
  "He knows. That's why he's been up for three days."
  "He says he is not going to ask you and he says you already know that"
  "he is not going to ask you, and he asked me to say both halves."
  "$")

 (sp/action "It goes off the desk and under the door in a way a door should"
            "not allow and he sits in his room with the light off for a while.")

 (sp/action "Then he gets his Help out to look up when the Collegium"
            "visitation is, and stops, because there is no Collegium, because"
            "the Collegium is a thing a man in a felt hat made up in October.")

 (sp/action "He puts it down and does not ask it anything.")

;; ---

 (sp/slug "INT. UNDER MERIDIAN — LAZLO'S ROOM — A SATURDAY")

 (sp/action "Eleven rovers on the bench in various states and four of them are"
            "in pieces because he has had them open looking for something that"
            "is not in them.")

 (sp/dialogue "LAZLO"
              ["I have got a hundred and eighty feet of run I"
               "cannot use and I do not know why, and I have been"
               "doing it by sending things at it and counting what"
               "comes back."])

 (sp/dialogue :pc
              "What comes back?")

 (sp/dialogue "LAZLO"
              ["A number. If it gets there it tells me how many of"
               "the squares round it are bad. If it does not get"
               "there it does not tell me anything, and I have one"
               "fewer rover."])

 (sp/dialogue :pc
              "That's enough.")

 (sp/dialogue "LAZLO"
              "Enough for what?")

 (sp/dialogue :pc
              ["To work out where they all are without sending"
               "anything else at them."])

 (sp/beat "Lazlo looks at him for a moment.")

 (sp/dialogue "LAZLO"
              "No, it isn't. I have done that. Show me anyway.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "It takes him an evening. There is nothing in it that is difficult"
            "and there is one thing in it that is nice, and the nice thing is"
            "what a square with nothing next to it does.")

 (sp/action "A square with nothing next to it is not one piece of information."
            "It is a piece of information about all eight of its neighbours,"
            "and each of those may be another one, so the honest way to write"
            "it is to let it call itself and stop when it has something to"
            "say.")

 ^:kindly/hide-code
 (sp/code
  "(def width 9)
 (def height 9)

 (defn neighbours [[x y]]
   (for [dx [-1 0 1] dy [-1 0 1]
         :when (not= [0 0] [dx dy])
         :let [p [(+ x dx) (+ y dy)]]
         :when (and (< -1 (first p) width) (< -1 (second p) height))]
     p))

 (defn near [bad p] (count (filter bad (neighbours p))))

 (defn probe
   \"Look at a square. If nothing is next to it, look at everything round
  it as well, and keep going.\"
   [bad seen p]
   (cond
     (seen p) seen
     (bad p)  (conj seen p)
     :else    (let [seen (conj seen p)]
                (if (zero? (near bad p))
                  (reduce (fn [s q] (probe bad s q)) seen (neighbours p))
                  seen))))")

 (sp/action "And then the part Lazlo asked for, which is the only two things"
            "anybody can say for certain from a number.")

 ^:kindly/hide-code
 (sp/code
  "(defn deduce
  \"If a square's number equals the unknowns around it, all of them are
 bad. If it equals what is already marked around it, everything else
 round it is clear.

 That is all. There is a third kind of deduction that takes two squares
 at once and he does not find it this term.\"
  [bad seen marked]
  (reduce
   (fn [acc p]
     (let [unknown (remove (fn [q] (or (seen q) (marked q))) (neighbours p))
           n (near bad p)
           already (count (filter marked (neighbours p)))]
       (cond
         (empty? unknown) acc
         (= n (+ already (count unknown))) (update acc :mark into unknown)
         (= n already)                     (update acc :safe into unknown)
         :else acc)))
   {:mark #{} :safe #{}}
   (filter seen (for [x (range width) y (range height)] [x y]))))")

 (sp/action "Nine by nine and ten of them. Probe once in the middle and see"
            "what falls out.")

 ^:kindly/hide-code
(sketch/page
 {:id "survey" :label "SURVEY.CLJS · click to probe · shift-click to mark"
  :rows 34}
 (str "(def width 9)
"
      "(def height 9)
"
      "(def how-many 10)
"
      "
"
      "(defn neighbours [[x y]]
"
      "  (for [dx [-1 0 1] dy [-1 0 1]
"
      "        :when (not= [0 0] [dx dy])
"
      "        :let [p [(+ x dx) (+ y dy)]]
"
      "        :when (and (< -1 (first p) width) (< -1 (second p) height))]
"
      "    p))
"
      "
"
      "(defn survey [first-square]
"
      "  (let [safe (conj (set (neighbours first-square)) first-square)]
"
      "    (loop [found (hash-set)]
"
      "      (if (= how-many (count found))
"
      "        found
"
      "        (let [p [(rand-int width) (rand-int height)]]
"
      "          (recur (if (safe p) found (conj found p))))))))
"
      "
"
      "(defn near [bad p] (count (filter bad (neighbours p))))
"
      "
"
      "(defn probe [bad seen p]
"
      "  (cond
"
      "    (seen p) seen
"
      "    (bad p)  (conj seen p)
"
      "    :else    (let [seen (conj seen p)]
"
      "               (if (zero? (near bad p))
"
      "                 (reduce (fn [s q] (probe bad s q)) seen (neighbours "
      "p))
"
      "                 seen))))
"
      "
"
      ";; "
      "---------------------------------------------------------------------
"
      "
"
      "(def bad    (atom nil))
"
      "(def seen   (atom (hash-set)))
"
      "(def marked (atom (hash-set)))
"
      "(def lost   (atom 0))
"
      "(def note   (atom \"Probe anywhere. The first one is always clear.\"))
"
      "
"
      "(declare draw)
"
      "
"
      "(defn tap [p]
"
      "  (when (nil? @bad) (reset! bad (survey p)))
"
      "  (cond
"
      "    (@marked p) (reset! note \"That one is marked. Unmark it first.\")
"
      "    (@bad p)    (do (swap! seen conj p)
"
      "                    (swap! lost inc)
"
      "                    (reset! note (str \"Rover \" @lost \" stopped at \"
"
      "                                      (first p) \",\" (second p) \".\")))
"
      "    :else       (do (swap! seen (fn [s] (probe @bad s p)))
"
      "                    (reset! note (str (count @seen) \" of \"
"
      "                                      (* width height) \" squares "
      "surveyed.\"))))
"
      "  (draw))
"
      "
"
      "(defn mark [p]
"
      "  (swap! marked (fn [m] (if (m p) (disj m p) (conj m p))))
"
      "  (draw))
"
      "
"
      ";; the only two things anybody can say for certain
"
      "(defn deduce []
"
      "  (let [b @bad]
"
      "    (if (nil? b)
"
      "      (reset! note \"Probe something first.\")
"
      "      (let [found (reduce
"
      "                   (fn [acc p]
"
      "                     (let [unknown (remove (fn [q] (or (@seen q) "
      "(@marked q)))
"
      "                                           (neighbours p))
"
      "                           n (near b p)
"
      "                           already (count (filter @marked (neighbours "
      "p)))]
"
      "                       (cond
"
      "                         (empty? unknown) acc
"
      "                         (= n (+ already (count unknown)))
"
      "                         (update acc :mark into unknown)
"
      "                         (= n already) (update acc :safe into unknown)
"
      "                         :else acc)))
"
      "                   {:mark (hash-set) :safe (hash-set)}
"
      "                   (filter @seen (for [x (range width) y (range "
      "height)] [x y])))]
"
      "        (swap! marked into (:mark found))
"
      "        (doseq [p (:safe found)] (swap! seen (fn [s] (probe b s p))))
"
      "        (reset! note (str (count (:mark found)) \" certain, \"
"
      "                          (count (:safe found)) \" certainly clear.\"
"
      "                          (when (and (empty? (:mark found)) (empty? "
      "(:safe found)))
"
      "                            \"  Nothing follows. You will have to "
      "guess.\"))))))
"
      "  (draw))
"
      "
"
      ";; the point of the whole thing
"
      "(defn resurvey []
"
      "  (reset! bad nil)
"
      "  (reset! seen (hash-set))
"
      "  (reset! note \"Same ground. Your marks are still where you left "
      "them.\")
"
      "  (draw))
"
      "
"
      ";; "
      "---------------------------------------------------------------------
"
      "
"
      "(defn face [p]
"
      "  (cond
"
      "    (@marked p)      \"x\"
"
      "    (not (@seen p))  \"&middot;\"
"
      "    (and @bad (@bad p)) \"!\"
"
      "    :else (let [n (near @bad p)] (if (zero? n) \"\" (str n)))))
"
      "
"
      "(defn draw []
"
      "  (html!
"
      "    (str \"<div style='display:grid;grid-template-columns:repeat(\" "
      "width
"
      "         "
      "\",1.7rem);gap:2px;font-family:ui-monospace;justify-content:start'>\"
"
      "         (apply str
"
      "           (for [y (reverse (range height)) x (range width)
"
      "                 :let [p [x y] id (str \"c\" x \"_\" y)]]
"
      "             (str \"<button id='\" id \"' "
      "style='height:1.7rem;padding:0'>\"
"
      "                  (face p) \"</button>\")))
"
      "         \"</div><p>\" @note \"</p>\"
"
      "         \"<div class='live-bar'>\"
"
      "         \"<button id='deduce'>What follows</button>\"
"
      "         \"<button id='again'>Survey it again</button></div>\"
"
      "         \"<p class='roll-foot'>Click to probe. Shift-click to mark. \"
"
      "         \"Rovers stopped: \" @lost \"</p>\"))
"
      "
"
      "  (doseq [y (range height) x (range width)]
"
      "    (let [p [x y] el (js/document.getElementById (str \"c\" x \"_\" y))]
"
      "      (when el
"
      "        (set! (.-onclick el)
"
      "              (fn [e] (if (.-shiftKey e) (mark p) (tap p)))))))
"
      "  (on! \"deduce\" deduce)
"
      "  (on! \"again\" resurvey))
"
      "
"
      "(draw)"))

;; ---

 (sp/slug "INT. LAZLO'S ROOM — THE FOLLOWING SATURDAY")

 (sp/action "It works. Lazlo runs it for about forty minutes without saying"
            "anything, which from him is a review.")

 (sp/action "Then he clears it and runs the survey again over the same ground.")

 (sp/beat "It is a different board.")

 (sp/dialogue :pc
              "That's the same hundred and eighty feet.")

 (sp/dialogue "LAZLO"
              "Yes.")

 (sp/dialogue :pc
              "Then something's wrong with how you're seeding it.")

 (sp/dialogue "LAZLO"
              ["Nothing is wrong with how I am seeding it. I sent"
               "four rovers down that run in March and I sent four"
               "more down it in April and they did not stop in the"
               "same places, and I have the numbers, and I have"
               "had them a long time."])

 (sp/beat "{pc} sits with that.")

 (sp/dialogue :pc
              "Then what is it that's moving?")

 (sp/dialogue "LAZLO"
              ["I don't know that it is a thing and I don't know"
               "that it is moving. Those are two assumptions and I"
               "have got neither of them."])

 (sp/dialogue "LAZLO"
              ["What I have got is that a square is bad on Tuesday"
               "and clear on Thursday and bad again in May, and"
               "that a rover that goes into one does not come out,"
               "and that is the whole of nine years."])

 (sp/action "He presses it again. It is a different board again.")

 (sp/dialogue "LAZLO"
              ["Your program is correct. That is not the same"
               "thing as it being any use, and I would like you to"
               "hold both of those at once, because I have not"
               "managed it."])

 (sp/transition "Blackout.")

 (sp/action "Survey it again. Your marks stay where you put them, because a"
            "mark is a thing you wrote down and writing it down does not make"
            "it so."))