^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 4 — The First Week"}}}
(ns act1.s4-first-week
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.live :as live]))

^:kindly/hide-code
(sp/assets)

;; # Scene 4 — The First Week

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 9)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. MERIDIAN HALL — CS 101 — MONDAY, 8:00 A.M.")

 (sp/action "Forty desks. Thirty-one freshmen and one senior.")

 (sp/action "PROFESSOR AMELIA REYES writes four words on the board.")

 (sp/sign "PUT THE HELPS AWAY")

 (sp/beat "Nobody moves.")

 (sp/dialogue "REYES"
              "I'll wait.")

 (sp/action "They put the Helps away. A girl in the second row keeps hers in"
            "her lap until Reyes looks at her.")

 (sp/dialogue "REYES"
              "Who here has written a program.")

 (sp/action "Two hands. {pc} puts his up as far as his shoulder and leaves it"
            "there.")

 (sp/dialogue "REYES"
              "You. Half a hand.")

 (sp/dialogue :pc
              "I did one on Friday. It says my name.")

 (sp/dialogue "REYES"
              "Did you write it?")

 (sp/dialogue :pc
              ["The Help wrote it."
               "It didn't run. I fixed it."])

 (sp/dialogue "REYES"
              "How?")

 (sp/dialogue :pc
              "I moved one of the brackets.")

 (sp/beat "Reyes looks at him for slightly longer than she has looked at"
          "anybody else this morning.")

 (sp/dialogue "REYES"
              "Then you're a week ahead. Don't get comfortable.")

 (sp/dialogue :pc
              "It's four words.")

 (sp/dialogue "REYES" "shrugs"
              ["It's four words that ran."
               "Second question."])

 (sp/dialogue "REYES"
              "Who here is comfortable with their Help.")

 (sp/beat "Every hand in the room.")

 (sp/dialogue "REYES"
              "Yes.")

 (sp/action "She turns the board on. It is not a Help. It is a screen with a"
            "page on it and the page does what she types.")

 (sp/dialogue "REYES"
              ["Six programs, in three weeks from this morning, in"
               "any order you like, and between them they're a"
               "third of the term. I'll show you three of them"
               "now. I'll show you the other three next Monday, by"
               "which time you won't have finished these, and"
               "that's fine. That's what the three weeks are for."])

 (sp/dialogue "REYES"
              "One.")

 (sp/action "Numbers run down the page. Some of them are words.")

 (sp/sign "1 2 FIZZ 4 BUZZ FIZZ 7 8 FIZZ BUZZ 11 FIZZ"
          "…"
          "14 FIZZBUZZ 16")

 (sp/dialogue "REYES"
              ["Every third one says fizz. Every fifth says buzz."
               "You can work out the rest."])

 (sp/dialogue "A STUDENT"
              "What's it for?")

 (sp/dialogue "REYES"
              ["Nothing. It's for me to see whether you can be"
               "told a rule."])

 (sp/dialogue "REYES"
              "Two.")

 (sp/action "A word appears as dashes. She types letters at it. Wrong ones"
            "stack up on the right, under a heading everybody in the room can"
            "already recite.")

 (sp/dialogue "REYES"
              ["Six wrong and it stops. If it lets you have seven,"
               "that's marked. If it takes a letter twice, that's"
               "marked."])

 (sp/dialogue "REYES"
              "Three.")

 (sp/action "A list. She adds a thing to it, finishes a thing on it, removes a"
            "thing from it. Then she closes the page, opens it again, and the"
            "list is still there.")

 (sp/beat "Somebody says 'oh.'")

 (sp/dialogue "REYES"
              ["That last part is the assignment. Anyone can make"
               "a list. It has to still be there on Tuesday."])

 (sp/dialogue "A STUDENT"
              "Can we use our Helps?")

 (sp/dialogue "REYES"
              ["Not on these six. Not on the next six. Not on the"
               "last six."])

 (sp/dialogue "ANOTHER STUDENT"
              "On what, then?")

 (sp/dialogue "REYES"
              "Everything else. Use it constantly.")

 (sp/beat "Nobody writes that down. It is the only thing she says that morning"
          "that anybody repeats afterwards.")

 (sp/dialogue "A STUDENT"
              ["Why can't we, though."
               "For these."])

 (sp/dialogue "REYES"
              ["Because sooner or later something you need will be"
               "shut, and there will have to be somebody in the"
               "room who can open it."])

 (sp/beat "Thirty-one people look at her.")

 (sp/dialogue "REYES"
              ["It won't be most of you, and that's fine. Most of"
               "you will go and do something else and be perfectly"
               "happy, and when a thing is shut you'll wait, and"
               "somebody will come, and one day nobody will. I"
               "teach it every year in case two of you are"
               "listening."])

 (sp/beat "Nobody asks anything else.")

 (sp/dialogue "REYES"
              "Three.")

 (sp/dialogue "A STUDENT"
              "What if we get stuck?")

 (sp/dialogue "REYES"
              "Then you're stuck.")

 (sp/action "VICTOR is in the third row with a notebook, a pen, and a second"
            "pen. He is the only person in the room who is not eighteen.")

 (sp/beat "Reyes starts the class.")

 (sp/transition "Cut to:")

;; ---

 (art/handout
  "CS 101 · Assignment Set One"
  "Six programs. Three weeks. Any order. One third of the term."
  (art/spec-table
   [["1 · FIZZBUZZ" "Count to a hundred. Every third one says fizz, every fifth says buzz. Both, both."]
    ["2 · NOTICES" "Guess a word. Six wrong and you're out. Six means six, and a letter twice is not a guess."]
    ["3 · A LIST OF THINGS TO DO" "Add, finish, remove. It has to still be there on Tuesday."]
    ["4 · CHAOS DOTS" "Ten thousand dots placed by one rule. I will not tell you the rule and I will know if you copied the shape."]
    ["5 · THE SLIDING PUZZLE" "Fifteen numbers and a hole. It must refuse illegal moves."]
    ["6 · SQUARES" "A square inside a square inside a square. Extra if it turns."]])
  "If it draws, it draws in a page. If it crashes in front of me, it did not run."
  "No Helps on these six. I will know."
  "On everything that is not these six, use your Help. That is what it is for.")

;; ---

 (sp/slug "INT. PRENTISS SCIENCE CENTER — TEACHING LAB — TUESDAY")

 (sp/action "Thirty-nine stools. By the door, a bin of goggles. Most of them"
            "are scratched to a fog.")

 (sp/action "PROFESSOR EVELYN CARTER goes round the room once and has every"
            "name.")

 (sp/dialogue "CARTER"
              ["Partners are alphabetical. I don't take requests."
               "Before we start, we have to watch this."])

 (sp/action "A screen comes down over the periodic table.")

 (sp/sign "MERIDIAN SYSTEMS"
          "AN INTRODUCTION TO CHEMISTRY"
          "approved · 4 min")

 (sp/off-screen "NARRATOR"
                ["Chemistry is the study of what things are made of,"
                 "and of how they can be improved."])

 (sp/action "A hand in a glove picks up a beaker. The liquid in it is blue."
            "Nothing in the room is blue.")

 (sp/off-screen "NARRATOR"
                ["Every chemical you will handle in this course is"
                 "an approved chemical. Approved chemicals are safe,"
                 "understood, and sufficient."])

 (sp/off-screen "NARRATOR"
                ["The creation of novel compounds is a regulated"
                 "activity and is beyond the scope of this course."])

 (sp/action "A diagram. Six shapes join to make a seventh. The seventh is not"
            "shown for long.")

 (sp/off-screen "NARRATOR"
                ["If a reaction does not proceed as described, stop,"
                 "and tell your instructor."])

 (sp/beat "A pause of about a second.")

 (sp/off-screen "NARRATOR"
                ["If a reaction proceeds as described and you did"
                 "not perform it, stop, and tell your instructor."])

 (sp/action "Nobody in the room moves. Two people are on their Helps.")

 (sp/off-screen "NARRATOR"
                "Chemistry is safe.")

 (sp/sign "MERIDIAN SYSTEMS" "keeping the ordinary, expected")

 (sp/action "Carter watches it with them, standing at the side with her arms"
            "folded. She has seen it. She watches it anyway, all four minutes,"
            "and when it finishes she waits for the screen to go up before she"
            "says anything.")

 (sp/dialogue "CARTER"
              "Right.")

 (sp/action "MAYA FLORES sits down next to {pc} and hands him a pair of"
            "goggles without looking at him.")

 (sp/dialogue "MAYA"
              "Those are the ones that don't fog.")

 (sp/dialogue :pc
              "How do you know?")

 (sp/dialogue "MAYA"
              "I came Sunday and tried all of them.")

 (sp/action "They heat a solution until it goes from clear to green."
            "Thirty-nine of them go green.")

 (sp/dialogue "MAYA" "reading the sheet"
              "Where does the correction factor come from?")

 (sp/dialogue "CARTER" "passing"
              "The table.")

 (sp/dialogue "MAYA"
              "Whose table?")

 (sp/dialogue "CARTER"
              "Meridian's.")

 (sp/dialogue "MAYA"
              "Can I see it?")

 (sp/dialogue "CARTER"
              "No.")

 (sp/beat "Carter keeps walking. Then she stops.")

 (sp/dialogue "CARTER"
              ["And don't put that in the write-up."
               "They mark against the table."])

 (sp/dialogue "MAYA"
              "So if the table's wrong—")

 (sp/dialogue "CARTER"
              "Then you're wrong.")

 (sp/action "She moves to the next bench. Maya writes down the number from the"
            "table.")

 (sp/transition "Cut to:")

;; ---

 (art/email
  {:from "Office of Student Life <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "Wednesday, 6:02 AM"
   :subject "You have been placed on the docket"}
  "Congratulations on beginning your first year."
  "Unattached students in good standing are placed on the docket in their first term. Your assignment is Thursday. Attendance is recorded."
  "Please arrive presentable."
  "Docket attendance is not a component of review. Docket attendance is recorded.")

 (sp/action "He reads it twice. There is one button. He presses it.")

;; ---

 (sp/slug "INT. BECKWITH HALL — THURSDAY")

 (sp/action "PROFESSOR SAMUEL BENNETT does not hand anything out. Crew cut,"
            "black hair going grey at one temple only, a moustache that has"
            "been trimmed this morning. Thin, pale, and pressed at every fold.")

 (sp/dialogue "BENNETT"
              ["There's a reading list on the door and you'll read"
               "all of it, which is less impressive than it"
               "sounds, because it isn't long."])

 (sp/dialogue "A STUDENT"
              "What's the class actually for?")

 (sp/dialogue "BENNETT" ["There used to be several hundred languages."
                         "We have records of about eighty of them and"
                         "speakers of none, so what you have is this"
                         "one, and everything is written in it, and most"
                         "people get through their lives on about six"
                         "hundred words of it."
                         ""
                         "This class is the rest of the words."])

 (sp/dialogue "ELI BROOKS"
              "Can we read things that aren't on the list?")

 (sp/dialogue "BENNETT"
              ["Everything written before the founding was"
               "checked. A great deal of it was wrong, and some of"
               "it was worse than wrong, and somebody had to sit"
               "down and sort it, and it took them eleven years."
               "What came through is the list."])

 (sp/dialogue "A STUDENT"
              "Who checked it?")

 (sp/dialogue "BENNETT"
              ["The people who had read all of it. That is what"
               "the word means. You cannot sort a thing you have"
               "not read, and a man who has read the whole of it"
               "and can give you the fundamentals from memory is"
               "the man you send it to. Who else would you send it"
               "to?"])

 (sp/dialogue "BENNETT"
              ["Since then there are the supplements. Those are"
               "open by subject, with the approvals that go with"
               "the subject. A student of biology has the biology"
               "in front of her the day she needs it, and quite"
               "right too. If you want something outside your"
               "subject, you may ask me or any approved"
               "instructor, who writes to the office, and the"
               "office writes to whoever holds it. It comes back"
               "inside a fortnight and nobody is unkind about it."])

 (sp/dialogue "ELI"
              "What if you just want to read it?")

 (sp/beat "Bennett takes his glasses off and looks at them.")

 (sp/dialogue "BENNETT"
              ["There's one every year. Take a seat, you haven't"
               "done anything wrong."])

 (sp/beat "Eli had not stood up.")

 (sp/dialogue "BENNETT"
              ["You'd like to see a thing first and judge it"
               "afterwards. That's — well. That's a thing people"
               "say."])

 (sp/beat "He breathes on a lens.")

 (sp/dialogue "BENNETT"
              ["Consider what it is you're proposing, though."
               "You're proposing that a thought of yours, which"
               "you have had since about Tuesday, has not already"
               "been had by somebody who read the whole of it and"
               "held it up against the rest, and found that it"
               "stood, or found that it didn't. Do you think"
               "that's likely?"])

 (sp/dialogue "ELI"
              "It might not have been.")

 (sp/dialogue "BENNETT"
              ["It might not. That does happen, and when it"
               "happens it is written up and sent in and read, and"
               "if it stands it goes on the list with the name on"
               "it. You don't send it in yourself, of course. I"
               "send it in, if I think it will stand. That is what"
               "an approved instructor is for."])

 (sp/dialogue "ELI"
              "Have you sent anything in?")

 (sp/beat "Bennett stops polishing.")

 (sp/dialogue "BENNETT"
              "Two, in nineteen years. One of them stands.")

 (sp/dialogue "ELI"
              "What was it?")

 (sp/dialogue "BENNETT"
              ["A note on a translation. It's four lines, and it's"
               "on the list, and it will be on the list after I'm"
               "not."])

 (sp/beat "He would be embarrassed to be told that this is the proudest thing"
          "he has ever said out loud.")

 (sp/dialogue "BENNETT"
              ["The Readers can't take it from everyone, you"
               "understand. Imagine trying to think every thought"
               "there is, only to arrive at the one you needed."])

 (sp/dialogue "BENNETT"
              ["Can you name the twenty governances on the vetting"
               "of matter held in supplement?"])

 (sp/dialogue "ELI"
              "No.")

 (sp/dialogue "BENNETT"
              ["Nor could I at your age, and I would not have"
               "known which of them I was breaking. It's no mark"
               "against a soul to have had a thought. But a"
               "thought you haven't held up against the word of"
               "the people who checked it has nothing underneath"
               "it, and a thing with nothing underneath it wants"
               "trimming, and it's kinder to trim it early."])

 (sp/action "He puts the glasses back on.")

 (sp/dialogue "ELI"
              "I meant stories.")

 (sp/beat "The room is quiet in a way it has not been quiet yet.")

 (sp/dialogue "BENNETT"
              "People still write them. There's a shelf for it.")

 (sp/dialogue "ELI"
              "How big a shelf?")

 (sp/dialogue "BENNETT"
              ["It's a small shelf and it isn't on the list."
               "I've read some of it, which I mention because"
               "you'll be told that I haven't."])

 (sp/beat "Nobody had been going to tell Eli anything of the kind.")

 (sp/dialogue "ELI"
              "Was it good?")

 (sp/beat "Bennett considers the question for longer than the class expects.")

 (sp/dialogue "BENNETT"
              ["It was written by people who had time. That's not"
               "a criticism, it's a fact about the conditions."])

 (sp/dialogue "BENNETT"
              ["People write a great deal. Reports. Records."
               "Transcripts. A man sees a thing happen and writes"
               "it down properly and that document is still true"
               "in a hundred years. It's honourable work, it's"
               "well paid, and I'd be pleased if any of you did"
               "it."])

 (sp/dialogue "A STUDENT"
              "Like on the call floor show?")

 (sp/beat "Bennett brightens for the first time all hour.")

 (sp/dialogue "BENNETT"
              ["The quarterly did a very good piece on that"
               "programme. Six pages. Read that."])

 (sp/transition "Cut to:")

;; ---

 ^:kindly/hide-code
 (art/handout
  "THE FLOOR · fourteenth season · episode three · transcript (approved)"
  "Recorded on Array Nine. Desks in tens, tens of tens, further than the lighting goes. A management pole at every hundredth desk. Submanagers walk the aisles on a four-minute cycle. The misters run at the quarter hour."
  "PAULA is at desk 4,412. PAULA's feed line is dressed in the same blue as her chair."
  "DERMOT.  Paula. Your aftercall is four seconds over."
  "PAULA.  I know, Dermot."
  "DERMOT.  Four seconds is ninety by Friday. Ninety is a name on a list, and I don't write the list, I only read it out."
  "(LAUGHTER)"
  "PAULA.  I'm bringing it down."
  "DERMOT.  You've said that."
  "PAULA.  I want to bring it down. I want the array to make the number."
  "DERMOT.  (warmly) There it is. That's the mission talking."
  "(LAUGHTER)"
  "DERMOT.  Do you have your handbook?"
  "PAULA.  I have the handbook."
  "DERMOT.  Are you familiar with the script?"
  "PAULA.  I'm familiar with the script."
  "DERMOT.  Because it's all in the script, Paula. The script is four seconds shorter than what you're saying."
  "(LAUGHTER)"
  "DERMOT.  Would you like me to read it with you?"
  "PAULA.  ...Yes."
  "(LAUGHTER · SUSTAINED)"
  "(APPLAUSE)"
  "The production notes with pride that PAULA has not left the array in fourteen seasons."
  "— the quarterly, six pages, closing line: the warmth of the programme is in its accuracy.")

 (sp/action "Everyone in the room has seen it. Everyone in the room has an"
            "aunt or an uncle on an array somewhere, and when one of them"
            "comes up in conversation the number of years at the desk comes up"
            "in the same sentence, the way a rank would.")

;; ---
 
 (sp/slug "INT. BECKWITH HALL — CONTINUOUS")

 (sp/action "Bennett goes to the door and looks at the list himself, the way"
            "you check a number you already know. He straightens it on its"
            "pin, which does not need straightening.")

 (sp/dialogue "BENNETT"
              "It's four longer than it was last year.")

 (sp/dialogue "ELI"
              "Four what?")

 (sp/dialogue "BENNETT"
              ["Four titles. Six went to the office in November,"
               "four came back in June, and three of the four are"
               "by the same woman, in two disciplines."])

 (sp/dialogue "ELI"
              "What about the two that didn't come back?")

 (sp/dialogue "BENNETT"
              "They didn't stand.")

 (sp/dialogue "ELI"
              "That's it?")

 (sp/dialogue "BENNETT"
              ["That's the whole of it. People think the list"
               "doesn't move. The list moved four this year and it"
               "moved six the year before, and I've read every one"
               "of them, and I'd like you to read the four."])

 (sp/action "He comes back to the front of the room. The hour has eleven"
            "minutes left in it and he uses four of them to read out the first"
            "paragraph of the first item on the list, twice, in two different"
            "ways, and the second way is better and everybody hears that it is"
            "better and nobody can say why.")

 (sp/dialogue "A STUDENT"
              "Is the list on the review?")

 (sp/dialogue "BENNETT"
              "Everything's on the review.")

 (sp/action "The room writes that down. Bennett watches them write down the"
            "only thing he has said all hour that was not worth writing down.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE COMMONS — THURSDAY, 7:00 P.M.")

 (sp/action "Hudreds gather in the cafeteria, known on campus as \"The Commons\". "
            "They never close and are never empty.")

 (sp/action "{pc} sits at the end of a long table with three people he does"
            "not know and who are not eating together either.")

 (sp/action "CHAD goes past with a tray on his shoulder and lifts a hand at"
            "him.")

 (sp/dialogue "CHAD"
              "Thinking part!")

 (sp/action "{pc} lifts a hand back. Chad has already gone by. He is going to"
            "a table of eleven.")

 (sp/action "Two tables down, ELI is telling a story standing up.")

 (sp/action "Maya is not here. Researchers eat at the benches. Nobody has told"
            "{pc} this and he has not asked anybody.")

 (sp/action "The three people at the end of the table are arguing about the"
            "line.")

 (sp/dialogue "A BOY"
              "It's sixty.")

 (sp/dialogue "A SECOND BOY"
              "Sixty on the review. Labs are separate.")

 (sp/dialogue "A GIRL" "It's sixty on everything and attendance is"
              "weighted.")

 (sp/dialogue "A BOY"
              "Weighted how?")

 (sp/beat "None of them know. They keep going for eleven minutes.")

 (sp/action "{pc} eats and listens and does not join in.")

 (sp/action "At ten past seven he is finished and there is nothing else to do,"
            "so he goes back to his room.")

 (sp/transition "Cut to:")

 (sp/slug "INT. ASHCROFT HOUSE — ROOM 214 — FRIDAY")

 (sp/action "Two beds. One made. One stripped to the frame.")

 (sp/action "There was a bag on it on Monday, and a coat on the hook. {pc} has"
            "not met the person they belong to.")

 (sp/dialogue :pc "into the corridor"
              "Has anyone seen Grigg?")

 (sp/off-screen "A VOICE"
                "Who?")

 (sp/dialogue :pc
              "Two fourteen.")

 (sp/off-screen "A VOICE"
                "Oh. He got reviewed.")

 (sp/off-screen "A SECOND VOICE"
                "He was carrying two.")

 (sp/off-screen "A VOICE"
                "You can only carry one.")

 (sp/beat "Nobody comes out into the corridor.")

 (sp/action "{pc} opens housing. Two fourteen is a single. In August it was a"
            "double and the name on it was GRIGG, N.")

 (sp/action "He moves the tower to the other desk, because it is bigger.")

 (sp/action "The desk rocks.")

 (sp/action "{pc} tips it up. Under the short leg there is a wad of paper,"
            "folded into eight and gone furry along the folds.")

 (sp/beat "It is a page out of a book.")

 (sp/action "One side is printed. Somebody has written on it in pencil, and"
            "the pencil is fainter than the printing.")

 (sp/action "He puts the desk down on his shoe and reads it with the corner"
            "still in his hand.")

 (sp/transition "Blackout.")

;; ---

 (art/handout
  "218"
  "A set is a bag with no repeats in it. Asking a set whether it has a thing is one question, however big the bag is."
  "(def letters (set \"banana\"))"
  "(contains? letters \\a)   ;; true"
  "(contains? letters \\z)   ;; false"
  "A string can be walked through one character at a time, and each character can be swapped for something else."
  "(map (fn [c] (if (contains? letters c) c \\-)) \"cabana\")"
  "Counting is not a special operation. Counting is asking how long the list of things you kept is."
  "EXERCISE 6.4 · The word game. Show the letters that have been guessed and hide the ones that have not. Stop after the sixth wrong guess. The player may not guess the same letter twice and be charged twice for it."
  "— in pencil, in the margin, in handwriting that slopes: rest of it on 219. dont lose this one"
  "— on the reverse, a paper pocket with a card in it, and the card is stamped eleven times, and the last stamp is a date. Above the dates: 005.13 · OAKES")

 (sp/action "There is no page 219. {pc} looks for it for a while, in the"
            "drawer, behind the drawer, and under the other desk, which does"
            "not rock.")

 (sp/action "The page gives him three things and he works out the fourth on"
            "Saturday and gets it wrong. On Sunday he gets it wrong in a"
            "different way. On Sunday night it runs.")

 (sp/code
  "(notices/render (notices/play \"REYES\" \"AEIORSY\"))
(notices/render (notices/play \"WHITLOCK\" \"ABCDEFG\"))")

 (sp/action "It is not a good version. It counts a letter twice if you give it"
            "the same letter twice, which is the thing she said was marked,"
            "and he does not find that until Tuesday.")

 (sp/action "He keeps the page in the book he does not have, which is to say"
            "folded into eight, in his bag, where it stays for the rest of the"
            "year.")

 (live/editable
  {:label "NOTICES.CLJ · his version, before Tuesday" :rows 20}
  "(def secret \"BENNETT\")

(def review [\"REVIEW SCHEDULED\" \"FILE OPENED\" \"FIRST NOTICE\"
             \"SECOND NOTICE\" \"FINAL NOTICE\" \"REPOSSESSED\"])
(defn play [guesses]
  (let [g (set guesses)
        shown (apply str (interpose \" \" (map #(if (g %) % \"-\") 
               secret)))
        wrong (remove (set secret) g)]
    [shown
     (apply str \"wrong: \" (interpose \" \" wrong))
     (cond
       (>= (count wrong) 6)    \"REPOSSESSED\"
       (every? g (set secret)) \"PASSED\"
       (zero? (count wrong))   \"in good standing\"
       :else (nth review (dec (count wrong))))]))

;; Observe your results, and mark them in your observation logs.
(play \"ZZQ\")
(play \"NBT\")")

;; ---

 ^:kindly/hide-code
 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "11:48 PM"
    :text ["Attendance Thursday was four of five."
           "I will not name the fifth."]}
   {:who "Simon" :at "11:49 PM" :text "it was me. i had docket"}
   {:who "Victor" :role "PRESIDENT" :at "11:49 PM" :text "I said I would not name you."}
   {:who "Derek" :at "11:52 PM" :text "the docket is a Roman institution actually"}
   {:who "Simon" :at "11:58 PM" :text "did anyone in your halls get reviewed"}
   {:who "Simon" :at "12:04 AM" :text "ok"}
   {:who "Victor" :role "DM" :at "12:31 AM"
    :text ["Wednesday is DnD. You are the ranger. I have assigned you the ranger."]}]))