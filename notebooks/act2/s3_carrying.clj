^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 3 — Carrying"}}}
(ns act2.s3-carrying
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.todo.todo :as todo]))

^:kindly/hide-code
(sp/assets)

;; # Scene 3 — Carrying

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 18)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. ASHCROFT HOUSE — THE CORRIDOR — MONDAY, 11:00 P.M.")

 (sp/action "PELL is on the floor outside 2C with his back against the door"
            "and a machine on his knees. He is second year. He has been on"
            "this corridor since {pc} arrived and they have said about forty"
            "words to each other, all of them about the shower.")

 (sp/dialogue "PELL"
              "You're the one that does the six o'clock bucket.")

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "PELL"
              ["Right. The list one. The one that has to still be"
               "there on Tuesday."])

 (sp/dialogue :pc
              "Assignment three.")

 (sp/dialogue "PELL"
              ["I've done it four times and it's empty every time."
               "I close it and it's gone. I've read the sheet nine"
               "times."])

 (sp/action "{pc} sits down on the floor because it is easier than standing up"
            "talking to somebody on the floor.")

 (sp/beat "It takes eleven minutes. Pell has written all of it and none of it"
          "goes anywhere that is not inside the program, which is the only"
          "thing the assignment is about.")

 (sp/dialogue :pc
              ["You have to put it somewhere that isn't the"
               "program."])

 (sp/dialogue "PELL"
              "Where?")

 (sp/dialogue :pc
              "A file.")

 (sp/beat "Pell looks at him.")

 (sp/dialogue "PELL"
              "Show me on yours.")

 (sp/action "That is the moment, and it does not look like one.")

 (sp/beat "{pc} turns the machine round and shows him the four lines that save"
          "the list and the four that read it back.")

 (sp/action "Pell reads them twice and copies them out by hand onto the back"
            "of a docket letter, which takes six minutes, and does not ask for"
            "the file and is not offered it.")

 (sp/dialogue "PELL"
              "Is that cheating?")

 (sp/dialogue :pc
              "I don't know.")

 (sp/dialogue "PELL"
              "No. Me neither.")

 (sp/beat "He goes on copying.")

 (sp/dialogue :pc
              "How many are you carrying?")

 (sp/dialogue "PELL"
              "Two.")

 (sp/beat "Nobody on that corridor has said the word carrying out loud since"
          "the first week.")

 (sp/dialogue "PELL"
              ["From last year. One of them's this course and one"
               "of them's Bennett's, which is a reading list, and"
               "I can read."])

 (sp/dialogue :pc
              "So read it.")

 (sp/dialogue "PELL"
              ["I do read it. I read all of it. Then I sit in the"
               "room and he asks what it's doing to me and I"
               "haven't got anything, and everybody else has got"
               "something, and I've read it and they haven't."])

 (sp/action "He finishes copying and squares the docket letter on his knee.")

 (sp/dialogue "PELL"
              "Thanks. That's the third one done.")

 (sp/dialogue :pc
              "Out of six.")

 (sp/dialogue "PELL"
              "Out of six.")

 (sp/transition "Cut to:")

 (sp/slug "INT. MERIDIAN HALL — CS 101 — THE FOLLOWING MONDAY")

 (sp/action "Reyes takes the register the way she takes the register.")

 (sp/beat "She reads out thirty-one names.")

 (sp/action "One of them is not answered and she does not read it twice and"
            "does not look up, and goes on to the next one, and starts the"
            "class four seconds later than she started it last week.")

 (sp/beat "It is not Pell's name. Pell is not in this class.")

 (sp/action "It is a girl in the second row who kept her Help in her lap in"
            "September until she was looked at.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ASHCROFT HOUSE — 2C — THE SAME EVENING")

 (sp/action "The door of 2C is open, which it has not been all term.")

 (sp/action "The room is empty. Not tidied. Empty. The bed is stripped and"
            "there is a chair in the middle of the floor at an angle that no"
            "chair is ever left at.")

 (sp/dialogue :pc "into the corridor"
              "Has anyone seen Pell?")

 (sp/off-screen "A VOICE"
                "Who?")

 (sp/dialogue :pc
              ["Two C. Pell. He was on the floor out here on"
               "Monday."])

 (sp/off-screen "A SECOND VOICE"
                "Oh. He got reviewed.")

 (sp/dialogue :pc
              "It's the sixth week.")

 (sp/off-screen "A SECOND VOICE"
                "He was carrying two.")

 (sp/beat "Nobody comes out into the corridor.")

 (sp/transition "Cut to:")

;; ---

(sp/action "He asks eleven people over four days and gets nine answers.")

(art/ai-chat
 {:model "HELP · personal · v9"}
 [{:role :user :text "what happens to a student who is repossessed"}
  {:role :assistant
   :text ["Repossession is the standard end-of-term process for students"
          "who do not pass review."
          "Would you like me to set a study reminder?"]}
  {:role :user :text "where do they go"}
  {:role :assistant
   :text ["Repossession is the standard end-of-term process for students"
          "who do not pass review."]}
  {:role :user :text "you said that"}
  {:role :assistant :text "Dining Dollars do not roll over."}])

(sp/action "The corridor says home. A boy on the second floor says there is a"
           "transfer scheme and names an institution that Lena has been at and"
           "which Lena says does not take transfers. Ashley from the Senate"
           "says it is a financial thing and is quite sure. Somebody's"
           "roommate says his brother was repossessed four years ago and is"
           "fine, but cannot say where he is now.")

(sp/action "Miles says the word is a legal term about property and has been"
           "used wrongly by this institution for ninety years and that this"
           "annoys him more than anything else on the campus. Derek says it is"
           "Roman. Victor says it is a matter for #general (which it really"
           "isn't), because there is no other channel dedicated to it.")

(sp/transition "Cut to:")

 (sp/slug "INT. WHITLOCK HALL — LOWER GROUND — ROOM 4 — THURSDAY, 7:00 P.M.")

 (sp/action "Below the level of the quad. No window. One door. A long table"
            "with five chairs down one side, five down the other, and one at"
            "the end.")

 (sp/action "PROFESSOR HALE is at a lectern with a tablet. He has moderated"
            "this event every Thursday for six years and could do it without"
            "the tablet if he absolutely had to..")

 (sp/sign "DOCKET · ROOM 4 · 7:00"
          "ATTENDANCE IS RECORDED")

 (sp/dialogue "HALE"
              ["Eleven. Five, five, and the eleventh. Attendance"
               "is recorded and is not a component of review."
               "Standings first, in the order you're sitting."])

 (sp/action "The five on the near side sit up. Nobody on the far side does"
            "anything, because nobody on the far side is going to be asked.")

 (sp/dialogue "BOURNE"
              ["Bourne. Second year, metallurgy. Placed, Array"
               "Four, from June. No service assignment. Review, no"
               "action. Intends two."])

 (sp/beat "Somebody on the far side says two.")

 (sp/action "Not a question. She does not look up, and he does not turn his"
            "head.")

 (sp/dialogue "HALE"
              "Thank you.")

 (sp/dialogue "ALEX"
              ["Alex. Third year, transit. Placed. Review, no"
               "action. Intends three."])

 (sp/beat "Three comes back from further down.")

 (sp/dialogue "CARTER"
              ["Carter. Second year, food systems. Unplaced,"
               "applied. Service assignment, the glasshouses."
               "Review, no action. Intends one."])

 (sp/beat "Nothing comes back.")

 (sp/action "SIMON is fourth. He starts before Hale looks up.")

 (sp/dialogue "SIMON"
              "Simon. Second year, unplaced, but—")

 (sp/dialogue "HALE"
              "Thank you, Simon.")

 (sp/beat "Hale did'nt need to hear any further, nor did the rest of the room,"
          "apparently. Two people find it funny, while Simon finds it funny"
          "about a second or two later than they do.")

 (sp/dialogue "HALE"
              "Next.")

 (sp/dialogue :pc
              ["First year, computer science. Provisional. Service"
               "assignment pending. No review yet."])

 (sp/beat "He stops.")

 (sp/dialogue "HALE"
              "And.")

 (sp/dialogue :pc
              "Sorry?")

 (sp/dialogue "HALE"
              "How many do you intend.")

 (sp/dialogue :pc
              "I don't know.")

 (sp/beat "Nothing comes back from the far side, and it is a longer nothing"
          "than Carter's was.")

 (sp/action "MAYA is third along on the far side and does not look at him,"
            "which is a thing she is good at and has had to get used to.")

 (sp/dialogue "HALE"
              "That's a permitted answer. Questions.")

 (sp/action "He puts a printed card face up in the middle of the table. It has"
            "been laminated and the lamination has gone soft at one corner.")

 (sp/dialogue "HALE"
              ["One each, from the card, and I'd ask you to keep"
               "to the card, because the card is there so that"
               "nobody has to think of anything."])

 (sp/stage "The card is below.")

(art/handout
 "DOCKET · ROOM 4 · PERMITTED QUESTIONS"
 "One question per person. Questions are asked in the order seated."
 "1. What is your placement?"
 "2. When does it begin?"
 "3. What is your service assignment?"
 "4. How many do you intend?"
 "5. Is there a nomination on your file?"
 "6. Have you been assessed?"
 "Questions not on this card are not permitted and are not recorded."
 "Outcomes are notified. Outcomes are not discussed in the room.")

 (sp/slug "INT. ROOM 4 — CONTINUOUS")

 (sp/dialogue "ASHLEY" "to Bourne"
              "When does it begin?")

 (sp/dialogue "BOURNE"
              "June.")

 (sp/dialogue "ASHLEY"
              "Mine's June.")

 (sp/dialogue "HALE"
              "That isn't on the card.")

 (sp/dialogue "ASHLEY"
              "No. Sorry.")

 (sp/beat "She is not sorry and Hale does not write it down.")

 (sp/dialogue "MADISON" "to Alex"
              "What is your placement?")

 (sp/dialogue "ALEX"
              "Transit. Northern.")

 (sp/dialogue "MADISON"
              "Freight or people?")

 (sp/dialogue "HALE"
              "One each.")

 (sp/dialogue "ALYX" "to {pc}"
              "Is there a nomination on your file?")

 (sp/dialogue :pc
              "No.")

 (sp/beat "She writes it down. It takes her longer to write than it took him"
          "to say.")

 (sp/dialogue "BRITTANY" "to Simon"
              "Have you been assessed?")

 (sp/dialogue "SIMON"
              "Yes.")

 (sp/dialogue "BRITTANY"
              "Recently?")

 (sp/dialogue "HALE"
              "One each, please.")

 (sp/action "She stops. Nobody in the room reacts to the answer, because a"
            "great many people have been assessed, and it is on the card.")

 (sp/dialogue "MAYA" "to Carter"
              "What is your service assignment?")

 (sp/dialogue "CARTER"
              "Glasshouses. Six till eight, Tuesdays and Fridays.")

 (sp/dialogue "MAYA"
              "Doing what?")

 (sp/dialogue "HALE"
              "One each.")

 (sp/dialogue "CARTER"
              "Carrying things.")

 (sp/beat "Maya writes that down and Hale lets her.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:25 P.M.")

 (sp/dialogue "HALE"
              ["The table. Five minutes each and I'll say when."
               "You'll speak to all of them and all of them will"
               "speak to you, and everybody speaks to Wren."])

 (sp/action "WREN is in the chair at the end and has been in it since their"
            "first term, which was two years ago.")

 (sp/dialogue "HALE"
              "Go on, then.")

 (sp/beat "Everybody stands up at once and it is briefly a room full of people"
          "rather than a docket.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — THE TABLE — 7:30 P.M.")

 (sp/action "ASHLEY, who everybody says is very nice, and who is.")

 (sp/dialogue "ASHLEY"
              ["Provisional. That's the hard one. My cousin was"
               "Provisional and she said the first term is the"
               "whole of it."])

 (sp/dialogue :pc
              "Is she still here?")

 (sp/dialogue "ASHLEY"
              ["She's placed. Array Six. She likes it and she"
               "isn't saying that for anybody's benefit, because"
               "she'd tell me."])

 (sp/dialogue :pc
              "You're going in June.")

 (sp/dialogue "ASHLEY"
              ["Array Four. I've had the seat number since"
               "November."])

 (sp/beat "She says the seat number.")

 (sp/dialogue "ASHLEY"
              ["Anyway. If you're stuck on anything with the"
               "Senate, come and find me, and I mean that, and"
               "people don't."])

 (sp/beat "She does mean it.")

 (sp/dialogue "HALE"
              "Move.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:35 P.M.")

 (sp/action "MADISON has a card with her name on it and puts it on the table"
            "between them before she sits down.")

 (sp/dialogue "MADISON"
              "Computer science. What's your throughput?")

 (sp/dialogue :pc
              "My what?")

 (sp/dialogue "MADISON"
              "Per week. What comes out of you per week.")

 (sp/dialogue :pc
              ["Six programs in three weeks. That's the"
               "assignment."])

 (sp/dialogue "MADISON"
              ["Two a week. That's a figure. Can you say two a"
               "week to somebody?"])

 (sp/dialogue :pc
              "To who?")

 (sp/dialogue "MADISON"
              ["I'll find you somebody. That's what I do, that's"
               "the whole of what I do, and I'm good at it, and"
               "I'd want a percentage."])

 (sp/dialogue :pc
              "Of what?")

 (sp/dialogue "MADISON"
              ["Of whatever it turns into. Look, the quarterback's"
               "got three and he's no use to me, because he's"
               "already the product."])

 (sp/dialogue :pc
              "Chad?")

 (sp/dialogue "MADISON"
              "You know him?")

 (sp/dialogue :pc
              "He threw a ball at me.")

 (sp/dialogue "MADISON"
              "Right. Well. Keep that.")

 (sp/action "She takes the card back off the table on her way out of the"
            "chair.")

 (sp/dialogue "HALE"
              "Move.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:40 P.M.")

 (sp/action "ALYX sits down and does not do any of the front part of a"
            "conversation.")

 (sp/dialogue "ALYX"
              "You didn't have a number.")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "ALYX"
              ["Everybody has a number. The number's free. You say"
               "two and nobody writes anything and you go home."])

 (sp/dialogue :pc
              "I didn't know it was going to be asked.")

 (sp/dialogue "ALYX"
              ["That's worse, actually. That's the thing they were"
               "listening for."])

 (sp/beat "She is not being unkind. She has been doing this for two years.")

 (sp/dialogue "ALYX"
              "Have you done Wren yet?")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "ALYX"
              ["You don't repeat what they say. Not to me, not to"
               "anybody in here, not after."])

 (sp/dialogue :pc
              "Is that a rule?")

 (sp/dialogue "ALYX"
              "No.")

 (sp/dialogue "HALE"
              "Move.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:45 P.M.")

 (sp/action "BRITTANY sits down and puts her hands flat on the table and does"
            "not say anything for a moment.")

 (sp/dialogue "BRITTANY"
              "Say the standing again.")

 (sp/dialogue :pc
              "The whole of it?")

 (sp/dialogue "BRITTANY"
              "Yes.")

 (sp/action "He says it again. She is recording it and is not pretending she"
            "isn't.")

 (sp/dialogue "BRITTANY"
              "Thanks.")

 (sp/beat "That is four of the five minutes and neither of them fills the rest"
          "of it.")

 (sp/dialogue "HALE"
              "Move.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:50 P.M.")

 (sp/action "MAYA sits down opposite him for the second time this week. On"
            "Tuesday she gave him a pair of goggles.")

 (sp/beat "Neither of them says anything about that.")

 (sp/dialogue "MAYA"
              "Pending doing what?")

 (sp/dialogue :pc
              "It doesn't say. It says pending.")

 (sp/dialogue "MAYA"
              ["Mine says the benches and the benches means one"
               "bench and one flask, and I found that out by going"
               "and looking on a Sunday."])

 (sp/dialogue :pc
              "Is that allowed?")

 (sp/dialogue "MAYA"
              "Nobody's said. That's not the same as no.")

 (sp/beat "Four seconds.")

 (sp/dialogue "MAYA"
              "I'm going to enter nothing.")

 (sp/dialogue :pc
              "You're allowed to?")

 (sp/dialogue "MAYA"
              "I'm going to find out.")

 (sp/action "It is the only time in the room that either of them says anything"
            "that is not about a placement.")

 (sp/dialogue "HALE"
              "Move.")

 (sp/transition "Cut to:")
 
 (sp/slug "INT. ROOM 4 — THE TABLE — CONTINUOUS")

 (sp/action "BOURNE has done this before and has an order he does it in.")

 (sp/dialogue "BOURNE"
              ["Array Four, June, seat's allocated, and I've seen"
               "the floor plan. It's a good floor."])

 (sp/dialogue "MAYA"
              "What makes a floor good?")

 (sp/dialogue "BOURNE"
              "Aftercall.")

 (sp/dialogue "MAYA"
              "Whose?")

 (sp/beat "He looks at her.")

 (sp/dialogue "BOURNE"
              "The floor's.")

 (sp/dialogue "MAYA"
              "Right.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:35 P.M.")

 (sp/action "ALEX sits down. ALYX is two chairs along and neither of them"
            "looks over.")

 (sp/dialogue "MAYA"
              "Is that going to be a problem?")

 (sp/dialogue "ALEX"
              ["It was a problem in first year. Now it's a thing"
               "people say to us at the start and we get it out of"
               "the way."])

 (sp/dialogue "MAYA"
              "Have you got it out of the way?")

 (sp/dialogue "ALEX"
              "Yeah.")

 (sp/beat "He has not got it out of the way.")

 (sp/dialogue "ALEX"
              ["Transit's northern. I'll be gone in June. I say"
               "that early because it's the thing that matters and"
               "people leave it till the fourth minute."])

 (sp/dialogue "MAYA"
              "That's fair.")

 (sp/dialogue "ALEX"
              "It isn't, but it's honest.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:40 P.M.")

 (sp/action "CARTER has an opinion and has been waiting all evening to put it"
            "somewhere.")

 (sp/dialogue "CARTER"
              ["Do you know what this used to be? Docket. It was a"
               "list of things a court was going to get to."])

 (sp/dialogue "MAYA"
              "I know.")

 (sp/dialogue "CARTER"
              "So we're the things it's going to get to.")

 (sp/dialogue "MAYA"
              "Or we're the court.")

 (sp/beat "Carter had not thought of that and does not like it.")

 (sp/dialogue "CARTER"
              ["I carry seed trays from one end of a glasshouse to"
               "the other end of a glasshouse. Six till eight."
               "Twice a week."])

 (sp/dialogue "MAYA"
              "And then?")

 (sp/dialogue "CARTER"
              ["And then somebody carries them back, and I've"
               "asked, and there's a reason."])

 (sp/dialogue "MAYA"
              "What is it?")

 (sp/dialogue "CARTER"
              "There's a reason.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 7:45 P.M.")

 (sp/action "SIMON sits down and pushes his chair in properly, which nobody"
            "else has done.")

 (sp/dialogue "SIMON"
              "Right. So I'm on this every week.")

 (sp/dialogue "MAYA"
              "I'd gathered.")

 (sp/dialogue "SIMON"
              ["Every week since term started. Nobody's got a"
               "reason for it. I asked at the office and they said"
               "unattached students in good standing are placed on"
               "the docket."])

 (sp/dialogue "MAYA"
              "And you're unattached and in good standing.")

 (sp/dialogue "SIMON"
              "Yes. So it's working.")

 (sp/beat "He says it cheerfully and it is the correct analysis.")

 (sp/dialogue "MAYA"
              "Doesn't that get old?")

 (sp/dialogue "SIMON"
              ["It's a warm room and there's eleven people in it"
               "and nobody's on their Help. That's Thursdays. I've"
               "got worse Thursdays."])

 (sp/dialogue "MAYA"
              "That's bleak, Simon.")

 (sp/dialogue "SIMON"
              "No, I mean it. Where else does that happen.")

 (sp/beat "Maya does not have an answer to that and it stays with her into Act"
          "II.")

 (sp/dialogue "SIMON"
              "Anyway. Intends one. That's the good bit.")

 (sp/dialogue "HALE"
              "Move.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — THE END OF THE TABLE — 7:55 P.M.")

 (sp/action "They go to Wren one at a time and the rest of the room gets"
            "quieter each time somebody does.")

 (sp/action "Nobody comes back from that end of the table saying anything."
            "Bourne comes back and sits down and looks at the card for a"
            "while.")

 (sp/action "{pc} is last. Nobody told him there was an order, and there is"
            "one.")

 (sp/dialogue "WREN"
              "Sit down. You get one that isn't on the card.")

 (sp/dialogue :pc
              "One what?")

 (sp/dialogue "WREN"
              ["Question. From me. There's no column for me so"
               "there's nothing for me to keep to."])

 (sp/dialogue :pc
              "Okay.")

 (sp/beat "Wren takes a moment over it, which is not for effect.")

 (sp/dialogue "WREN"
              ["What do you do when nobody's asked you to do"
               "anything?"])

 (sp/beat "It is the first question anybody has asked him since he got here"
          "that he cannot answer off a form.")

 (sp/dialogue :pc
              "I don't know yet.")

 (sp/dialogue "WREN"
              "No. You've been here five days.")

 (sp/action "They sit with it.")

 (sp/dialogue "WREN"
              ["All right. Somebody's going to leave you something"
               "that was meant for somebody else, and you're going"
               "to keep it."])

 (sp/dialogue :pc
              "Is that it?")

 (sp/dialogue "WREN"
              "That's it.")

 (sp/dialogue :pc
              "How do you—")

 (sp/dialogue "WREN"
              ["I don't. You don't repeat it, and I don't say it"
               "twice, and those are the two halves of the same"
               "thing."])

 (sp/action "They look past him at the room.")

 (sp/dialogue "WREN"
              "Go on. He'll want the preferences in before eight.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 4 — 8:00 P.M.")

 (sp/dialogue "HALE"
              ["Preferences. In your own time, privately, and"
               "nothing said out loud."])

 (sp/action "Eleven people stop looking at each other at the same moment. It"
            "is the quietest the room has been and it goes on for four"
            "minutes.")

 (sp/beat "{pc} enters nothing, unsure whether entering nothing is permitted."
          "It was accepted, as it is permitted.")

 (sp/action "Across the table Maya finishes before he does and puts her hands"
            "in her lap.")

 (sp/action "It is somewhere in the middle of the four minutes that he notices"
            "the smell.")

 (sp/action "It is not in the room. It is like a coin held too long, and then"
            "like a room that has been shut, and then it is not like anything"
            "and it is very strong.")

 (sp/dialogue "HALE"
              "That's the docket. Outcomes are notified.")

 (sp/action "Chairs go back.")

 (sp/dialogue "HALE"
              "Provisional.")

 (sp/beat "He does not answer.")

 (sp/dialogue "HALE"
              "Son.")

 (sp/beat "Three seconds.")

 (sp/action "Then he is back, and Hale is halfway round the table, and there"
            "is blood on the card.")

 (sp/dialogue "HALE"
              "Sit down. Head forward, not back.")

 (sp/action "He has tissues in the lectern. He does not go and look for them."
            "He knows where they are.")

 (sp/action "WREN puts one in his hand on the way past.")

 (sp/dialogue "WREN"
              "It happens in this room.")

 (sp/action "They go. Hale taps the tablet once, at the top, where the"
            "standing is, and not at the bottom, where the outcome is.")

 (sp/action "MAYA is at the door with her coat over her arm and stops there"
            "for a moment.")

 (sp/dialogue "MAYA"
              "Do you want somebody to stay?")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "MAYA"
              "All right.")

 (sp/beat "She goes, and she counts the stairs on the way up, and does not"
          "know she is doing it until she gets to the top.")

 (sp/dialogue "HALE"
              ["Sit there till it stops. Don't go up the stairs"
               "till it stops."])

 (sp/dialogue :pc
              "Does it happen a lot?")

 (sp/dialogue "HALE"
              "It's a dry room.")

 (sp/action "It stops on the landing. The corridor at the top of the stairs"
            "smells of nothing at all, and he stands in it for a moment"
            "working out whether that is a thing he has noticed.")

 (sp/transition "Blackout.")

(art/email
 {:from "Office of Student Life <no-reply@newcarthage.edu>"
  :to "{pc-full} <{pc}@newcarthage.edu>"
  :date "Friday, 4:00 AM"
  :subject "Docket — outcome"}
 "Attendance recorded."
 "No preference was entered. No outcome is indicated on this occasion."
 "You remain on the docket. Your next assignment will be notified."
 "Docket attendance is not a component of review. Docket attendance is recorded."
 "Please arrive presentable.")

(sp/action "Nothing in it mentions the card, or the tissues, or the four"
           "minutes. He reads it twice and there is one button and he presses"
           "it.")

(art/discord
 {:server "FEDORA SOCIETY" :channel "#records"}
 [{:who "Simon" :at "11:49 PM" :text "sorry i was missing, i had docket..."}
  {:who "Victor" :role "PRESIDENT" :at "11:49 PM" :text "I said I would not name you."}
  {:who "Derek" :at "11:52 PM" :text "the docket is a Roman institution actually"}
  {:who "Miles" :at "11:53 PM" :text "it isnt"}
  {:who "Derek" :at "11:53 PM" :text "the word is"}
  {:who "Simon" :at "11:58 PM" :text "im on it again next week"}
  {:who "Derek" :at "11:59 PM" :text "youre on it every week"}
  {:who "Simon" :at "12:01 AM" :text "yeah"}
  {:who "Miles" :at "12:02 AM" :text "thats not what the docket is"}
  {:who "Simon" :at "12:04 AM" :text "i know what the docket is miles"}
  {:who "Simon" :at "12:05 AM" :text "the new guy was on it. he did alright"}
  {:who "Victor" :role "PRESIDENT" :at "12:31 AM" :text "He is promising."}
  {:who "Simon" :at "12:33 AM" :text "i meant like. as a person"}])

(todo/widget))