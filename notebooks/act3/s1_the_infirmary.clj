^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 1 — The Infirmary"}}}
(ns act3.s1-the-infirmary
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.sketch :as sketch]))

^:kindly/hide-code
(sp/assets)

;; # Scene 1 — The Infirmary

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 29)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. CAMPUS INFIRMARY — WARD 2 — SUNDAY 23 NOVEMBER")

 (sp/action "Eight beds, three of them made up, one of them occupied. A window"
            "at the far end that does not open and a strip light that is off,"
            "which is the only kindness in the building.")

 (sp/action "{pc} has been here since Saturday night. It is Sunday afternoon"
            "and he has been awake since about ten and has not been told very"
            "much.")

 (sp/action "MAYA is in the chair beside the bed with a coat over her knees."
            "She has been to her run twice and come back both times.")

 (sp/dialogue :pc
              "How long was it?")

 (sp/dialogue "MAYA"
              ["Ninety seconds. I counted it, because the sheet"
               "says to."])

 (sp/dialogue :pc
              "Which sheet?")

 (sp/dialogue "MAYA"
              ["The one on the wall in the teaching lab that"
               "nobody reads. It says count it, don't hold them"
               "down, and don't put anything in their mouth."])

 (sp/beat "She says all three of those the way you say a thing you have said"
          "to yourself several hundred times since Saturday.")

 (sp/dialogue :pc
              "Sorry.")

 (sp/dialogue "MAYA"
              ["Don't. That's the worst thing you could have said"
               "and you had a one in four chance."])

 (sp/action "There is a letter on the locker. It came at four in the morning"
            "and somebody printed it and left it there, and it was there"
            "before he was awake.")

 (sp/stage "The letter is below.")

;; ---

 (art/handout
  "NEW CARTHAGE UNIVERSITY · HEALTH SERVICE · ASSESSMENT OUTCOME"
  "SUBJECT: {pc-full} · PROVISIONAL 3812"
  "EVENT: single episode, witnessed, resolved without intervention. Duration reported as approximately ninety seconds."
  "FINDINGS: within range."
  "CATEGORY: GEN-8.77x"
  "SUBCLASS: x — recurrence not indicated on present evidence."
  "ADVICE: rest. Return if there is a further episode."
  "You may resume all normal activity, including recorded activity."
  "This outcome is not a diagnosis. Diagnosis is not a service provided by the Health Service."
  "Dining allocation is unaffected.")

 (sp/action "Findings within range. Category GEN-8.77x. Rest, and come back if"
            "it happens again. That is the clinical content of the piece of"
            "paper and by the middle of the afternoon he has it off by heart,"
            "including the x.")

 (art/ai-chat
  {:model "HELP · personal · v9"}
  [{:role :user :text "what is GEN-8.77x"}
   {:role :assistant
    :text ["GEN-8.77x is an assessment category in the general series."
           "The general series records single events of altered awareness"
           "in an enrolled subject. The numeral indicates the schedule"
           "under which the category was assigned. The subclass indicates"
           "whether recurrence is indicated on present evidence."]}
   {:role :user :text "so x means it wont happen again"}
   {:role :assistant
    :text ["x indicates that recurrence is not indicated on present"
           "evidence."]}
   {:role :user :text "thats not the same thing"}
   {:role :assistant :text "No."}
   {:role :user :text "how many categories are in the general series"}
   {:role :assistant :text "Nineteen."}
   {:role :user :text "what are they"}
   {:role :assistant
    :text ["Categories other than the assigned category are not disclosed"
           "to the subject."]}
   {:role :user
    :text "ok but is 8.77 a bad one. like is it in the bad half or the fine half. im not asking you to tell me the others im asking where mine sits"}
   {:role :assistant :text "Categories are not ranked."}
   {:role :user :text "then why are they numbered"}
   {:role :assistant :text "The numeral indicates the schedule."}
   {:role :user :text "what schedule. nobody has asked me anything"}
   {:role :assistant
    :text ["The schedule is the instrument used to determine the"
           "category."
           "Schedules are retained. Schedules are not released to the"
           "subject."]}
   {:role :user :text "so it was decided already"}
   {:role :assistant :text "Would you like me to book a follow-up?"}])

 (sp/action "Nineteen. It gives that one up in one word, having declined the"
            "question either side of it.")

 (sp/action "He goes back over it twice. The answer he keeps returning to is"
            "the short one in the middle, because it is the only time all year"
            "that anything has agreed with him about anything.")

;; ---

 (sp/slug "INT. WARD 2 — MONDAY 24 NOVEMBER")

 (sp/action "ELI arrives with a chair from another room because there is only"
            "one chair, and puts it down the wrong way round and sits on it"
            "like that.")

 (sp/dialogue "ELI"
              ["Right. I've been told not to tire you out, so I'm"
               "going to talk and you're going to lie there."])

 (sp/dialogue :pc
              "Fine.")

 (sp/dialogue "ELI"
              ["The Guild lost the black box for a fortnight"
               "because somebody in Music booked it on a form that"
               "doesn't exist any more, and I have spent four days"
               "on it and I have won, and nobody will ever know"
               "what it cost me."])

 (sp/beat "{pc} laughs, which hurts, which he had not expected.")

 (sp/action "LENA comes in at some point in the middle of it with three"
            "coffees, sits on the end of the empty bed opposite, and does not"
            "say anything for a while, which is her version of asking.")

 (sp/dialogue "LENA"
              "Have they told you what it was?")

 (sp/dialogue :pc
              "They've given me a letter.")

 (sp/dialogue "LENA"
              "Yes. They do that.")

 (sp/transition "Cut to:")

 (sp/slug "INT. WARD 2 — MONDAY, 2:00 P.M.")

 (sp/action "A woman comes in with a tablet and a chair she has carried in"
            "herself, and sits down at the end of the bed rather than beside"
            "it, and does not give a name or a department.")

 (sp/dialogue "THE ASSESSOR"
              ["This is the schedule that goes with the category."
               "Nine questions. There are no wrong answers, I'm"
               "not assessing you, I'm completing the schedule."])

 (sp/dialogue :pc
              "I've already got the category.")

 (sp/dialogue "THE ASSESSOR"
              "Yes.")

 (sp/beat "She is not embarrassed by that and does not appear to think it is a"
          "remark.")

 (sp/dialogue "THE ASSESSOR"
              "Were you asleep?")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "THE ASSESSOR"
              ["In the ten minutes before, did you notice an odour"
               "you couldn't place?"])

 (sp/beat "A coin held too long, and then a room that has been shut.")

 (sp/beat "It has happened in a warm room in August and twice since and he has"
          "never once told anybody about the smell, because the smell is not"
          "the part other people can see.")

 (sp/dialogue :pc
              "No.")

 (sp/action "She types it.")

 (sp/dialogue "THE ASSESSOR"
              ["In the last thirty days, have you been below"
               "ground level on institutional premises?"])

 (sp/dialogue :pc
              "Yes. Plant Services. It's my service assignment.")

 (sp/dialogue "THE ASSESSOR"
              "How long in total, would you say?")

 (sp/beat "He works it out, which takes a moment.")

 (sp/dialogue :pc
              "Fifty. About fifty.")

 (sp/action "She types for longer than any yes or no has taken.")

 (sp/dialogue "THE ASSESSOR"
              ["Do you recall a room, corridor or stair on"
               "institutional premises that you cannot place on a"
               "map?"])

 (sp/beat "There is a photograph of a catalogue card on his Help, in a bag, in"
          "the bench room, where it has been since Saturday.")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "THE ASSESSOR"
              ["Has any family member been assessed under this"
               "schedule?"])

 (sp/beat "{pc} looks at her.")

 (sp/dialogue :pc
              "I don't have that.")

 (sp/dialogue "THE ASSESSOR"
              ["Nobody does. It's on every schedule. It's older"
               "than the schedule. Say no."])

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "THE ASSESSOR"
              ["Was any person present who is not enrolled at this"
               "institution?"])

 (sp/dialogue :pc
              "No. One person was there. She's enrolled.")

 (sp/dialogue "THE ASSESSOR"
              "Name?")

 (sp/beat "He gives it, because there is no reason not to, and watches her"
          "type it.")

 (sp/dialogue "THE ASSESSOR"
              ["Last one. Since the event, have you wanted to know"
               "something you did not want to know before?"])

 (sp/beat "{pc} looks at her.")

 (sp/dialogue :pc
              "What sort of question is that?")

 (sp/dialogue "THE ASSESSOR"
              "It's question nine. There are no wrong answers.")

 (sp/dialogue :pc
              "No.")

 (sp/action "She types, stands, and takes the chair out with her.")

 (sp/dialogue "THE ASSESSOR" "at the door"
              "Thank you. That's the schedule.")

 (sp/transition "Cut to:")

;; ---

 (art/handout
  "SCHEDULE 8 · nine questions · completed by an assessor · not a diagnostic instrument"
  "1. Was the subject asleep?"
  "2. In the ten minutes preceding, did the subject notice an odour they could not place?"
  "3. In the preceding thirty days, has the subject been below ground level on institutional premises?"
  "3a. If yes, total hours."
  "4. Does the subject recall a room, corridor or stair on institutional premises that they cannot place on a map?"
  "5. Has any family member been assessed under this schedule?"
  "6. Was any person present who is not enrolled at this institution?"
  "7. Name of any person present."
  "8. (withdrawn)"
  "9. Since the event, has the subject wanted to know something they did not want to know before?"
  "Schedules are retained. Schedules are not released to the subject.")

;; ---

 (sp/slug "INT. WARD 2 — MONDAY, LATER")

 (sp/action "LENA has brought the coffees again and is sitting on the end of"
            "the empty bed opposite with her feet up.")

 (sp/dialogue :pc
              "Somebody came and did a schedule.")

 (sp/dialogue "LENA"
              "Nine questions?")

 (sp/beat "{pc} looks at her.")

 (sp/dialogue "LENA"
              ["They did four of them at me when I transferred."
               "Not the sleeping one. The one about the corridor"
               "and the one about the smell and the one about"
               "family, and then the last one."])

 (sp/dialogue :pc
              "Question nine.")

 (sp/dialogue "LENA"
              "That's the one.")

 (sp/dialogue :pc
              "What did you say?")

 (sp/dialogue "LENA"
              ["I said no, because I'd been asked it before and I"
               "knew what it was for."])

 (sp/dialogue :pc
              "What's it for?")

 (sp/beat "She drinks the coffee.")

 (sp/dialogue "LENA"
              "Nothing happens if you say no.")

 (sp/action "She changes the subject to the poster job and does not go back to"
            "it, and {pc} does not push, and neither of them mentions it again"
            "for the rest of the book.")

 (sp/transition "Cut to:")

 (sp/slug "INT. WARD 2 — MONDAY EVENING")

 (sp/action "ELI has been here two hours and has run out of the Guild and most"
            "of the second years.")

 (sp/action "He tears the back page off the notes at the end of the bed and"
            "draws four lines on it.")

 (sp/dialogue "ELI"
              "Noughts and crosses. You're crosses, you're ill.")

 (sp/action "They play nine games. Eli wins the first one because {pc} is not"
            "really there yet, and after that they draw eight in a row.")

 (sp/dialogue "ELI"
              "This is a terrible game.")

 (sp/dialogue :pc
              "It's a solved game.")

 (sp/dialogue "ELI"
              "What does that mean?")

 (sp/dialogue :pc
              ["If neither of us makes a mistake it's a draw."
               "Every time. Forever."])

 (sp/beat "Eli looks at the paper for a moment.")

 (sp/dialogue "ELI"
              ["So the whole of it is waiting for the other one to"
               "get it wrong."])

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "ELI"
              "God. All right. Best of one more.")

 (sp/transition "Blackout.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "Wednesday, 9:14 PM"
    :text ["Attendance tonight four of five. The session does not proceed"
           "at four. We hold the position and resume next week."]}
   {:who "Miles" :at "9:15 PM" :text "hes in the infirmary victor"}
   {:who "Victor" :role "PRESIDENT" :at "9:16 PM"
    :text "I am aware of where he is. The position is held either way."}
   {:who "Derek" :at "9:31 PM" :text "we could run the bit under the refectory without him"}
   {:who "Victor" :role "PRESIDENT" :at "9:32 PM" :text "No."}
   {:who "Simon" :at "10:02 PM" :text "has anyone actually gone over there"}
   {:who "Simon" :at "10:44 PM" :text "ive gone over there. theyll only let family in"}
   {:who "Simon" :at "10:45 PM" :text "i left the dice"}
   {:who "Victor" :role "PRESIDENT" :at "11:58 PM"
    :text "Article Two stands. Nobody has left anything."}])

 (sp/action "The tin is on the locker when he wakes up on Tuesday, with a note"
            "folded under it that says back Wednesday and nothing else, and"
            "Simon's handwriting is worse than anybody would have guessed.")

;; ---

 (art/email
  {:from "Office of the Registrar <no-reply@newcarthage.edu>"
   :to "{pc-full} <{pc}@newcarthage.edu>"
   :date "Tuesday, 4:00 AM"
   :subject "Absence — recorded"}
  "An absence of six days has been recorded. Supporting documentation has been received from the Health Service."
  "Recorded absence is not a component of review."
  "Coursework deadlines are not adjusted for recorded absence."
  "Set one closes Friday. Your outstanding supervised study sessions (2) remain outstanding.")

 (sp/action "Six days recorded, two evenings still owed, and set one due on"
            "the Friday the way it was due on the Friday before any of this.")

;; ---

 (sp/slug "INT. WARD 2 — TUESDAY 25 NOVEMBER")

 (sp/action "He is dressed and sitting on the made bed with the letter in his"
            "hand, waiting for somebody to come and tell him he can go, which"
            "takes an hour and forty minutes.")

 (sp/action "MAYA comes at ten with his bag, which has been in the bench room"
            "since Saturday, and the page folded into eight is still in the"
            "front pocket of it, and so is the card he photographed in"
            "Beckwith.")

 (sp/dialogue "MAYA"
              "Anything?")

 (sp/dialogue :pc
              "GEN-8.77x.")

 (sp/dialogue "MAYA"
              "Which means?")

 (sp/dialogue :pc
              ["Findings within range. The x means recurrence"
               "isn't indicated on present evidence."])

 (sp/dialogue "MAYA"
              "That isn't the same as it not happening again.")

 (sp/dialogue :pc
              "No. I asked. It said no as well.")

 (sp/beat "She reads the letter twice and gives it back.")

 (sp/dialogue "MAYA"
              ["Somebody wrote that schedule. Somebody set the"
               "range, on a day, and put a name at the bottom of"
               "it, and it went into a building and it is in that"
               "building now."])

 (sp/dialogue :pc
              "I know.")

 (sp/dialogue "MAYA"
              ["I'm not being nice to you. I've had this all term"
               "with a correction factor."])

 (sp/action "She has to go to the bench at half past and goes, and stops at"
            "the door.")

 (sp/dialogue "MAYA"
              "About Saturday.")

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "MAYA"
              ["I'm not going to say anything sensible about it in"
               "a ward. Come and find me when you're not grey."])

 (sp/transition "Blackout.")

;; ---

 (sp/action "Tuesday. He is not allowed to do anything strenuous and the"
            "sliding puzzle is not going anywhere, so he writes the terrible"
            "game instead, because he has just told somebody it cannot be lost"
            "and he would like that to be true where he can check it.")

 (def empty-board (vec (repeat 9 nil)))

 (defn free? [board i] (nil? (nth board i)))

 (defn place [board i mark]
   (if (free? board i)
     (assoc board i mark)
     board))

 (def lines
   [[0 1 2] [3 4 5] [6 7 8]
    [0 3 6] [1 4 7] [2 5 8]
    [0 4 8] [2 4 6]])

 (defn winner [board]
   (some (fn [[a b c]]
           (let [v (nth board a)]
             (when (and v (= v (nth board b)) (= v (nth board c)))
               v)))
         lines))

 (defn full? [board] (every? some? board))

 (winner (-> empty-board (place 0 :x) (place 4 :x) (place 8 :x)))

 (sp/action "That is the whole of the rules and it took forty minutes, and it"
            "is the part he expected to be hard.")

 (sp/action "Then the other half, which is a thing that plays. It works"
            "backwards from the end: if a board is finished it is worth one,"
            "nothing, or minus one, and if it is not finished it is worth"
            "whatever the best reply to it is worth, and the other side is"
            "looking for the worst.")

 (defn other-side [mark] (if (= mark :x) :o :x))

 (defn outcome [board me]
   (let [w (winner board)]
     (cond
       (= w me)   1
       (some? w) -1
       (full? board) 0
       :else nil)))

 (defn value [board me turn]
   (or (outcome board me)
       (let [replies (for [i (range 9) :when (free? board i)]
                       (value (place board i turn) me (other-side turn)))]
         (if (= turn me) (apply max replies) (apply min replies)))))

 (defn best-move [board me]
   (apply max-key
          (fn [i] (value (place board i me) me (other-side me)))
          (filter (partial free? board) (range 9))))

 (sp/action "It reads every game that is still possible from where it is"
            "standing, which is a lot of games at the start and almost none by"
            "the middle, which is why it thinks for a second on the first move"
            "and instantly after that.")

 (def opening (-> empty-board (place 4 :x) (place 0 :o) (place 8 :x)))

 (best-move opening :o)


 (sp/action "Six. It has to be six, and it is not blocking because it is"
            "clever, it is blocking because every board that does not block is"
            "worth minus one to it.")

 (value (place empty-board 4 :x) :x :o)

 (sp/action "Nought. From an empty board, with both sides playing as well as"
            "they can play, the game is worth nought to everybody, which is"
            "what he told Eli on a piece of paper torn off the end of a bed.")

 (sp/action "Play it. It blocks, it takes a win when there is one, and it will"
            "not lose to anybody on that corridor.")

 (sketch/page
  {:id "noughts" :label "NOUGHTS.CLJS" :rows 30}
  "(def board (atom (vec (repeat 9 nil))))

(def lines [[0 1 2] [3 4 5] [6 7 8]
            [0 3 6] [1 4 7] [2 5 8]
            [0 4 8] [2 4 6]])

(defn winner [b]
  (some (fn [l]
          (let [v (nth b (first l))]
            (when (and v (apply = (map (fn [i] (nth b i)) l))) v)))
        lines))

(defn free-squares [b]
  (filter (fn [i] (nil? (nth b i))) (range 9)))

(defn wins-for [b mark]
  (first (filter (fn [i] (= mark (winner (assoc b i mark)))) (free-squares b))))

(defn reply [b]
  (or (wins-for b :o)                       ;; take the win
      (wins-for b :x)                       ;; else stop theirs
      (first (filter (fn [i] (some #{i} (free-squares b))) [4 0 2 6 8 1 3 5 7]))))

(defn show []
  (html!
    (str \"<div style='display:grid;grid-template-columns:repeat(3,3rem);gap:4px'>\"
         (apply str
           (map (fn [i]
                  (let [v (nth @board i)]
                    (str \"<button id='c\" i \"' style='height:3rem'>\"
                         (case v :x \"X\" :o \"O\" \"&nbsp;\") \"</button>\")))
                (range 9)))
         \"</div><p>\"
         (let [w (winner @board)]
           (cond (= w :x) \"you win. that should not happen.\"
                 (= w :o) \"it wins.\"
                 (empty? (free-squares @board)) \"drawn, as advertised.\"
                 :else \"your move. you are crosses.\"))
         \"</p>\"))
  (doseq [i (range 9)]
    (on! (str \"c\" i)
      (fn []
        (when (and (nil? (nth @board i)) (nil? (winner @board)))
          (swap! board assoc i :x)
          (when (and (nil? (winner @board)) (seq (free-squares @board)))
            (swap! board assoc (reply @board) :o))
          (show))))))

(show)")

 (sp/action "Then three weeks, which go the way weeks go.")

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "26 Nov, 11:40 PM"
    :text "Attendance four of five. The session holds. It resumes when it is five."}
   {:who "Simon" :at "26 Nov, 11:52 PM" :text "hes back tomorrow"}
   {:who "Victor" :role "PRESIDENT" :at "3 Dec, 11:38 PM" :text "Five of five."}
   {:who "Derek" :at "10 Dec, 11:20 PM" :text "friday is the one with the boats again. thats twice this term"}
   {:who "Derek" :at "10 Dec, 11:21 PM" :text "nine films"}
   {:who "Miles" :at "12 Dec, 6:02 PM" :text "review week. good luck everyone. genuinely"}
   {:who "Simon" :at "12 Dec, 6:40 PM" :text "good luck"}])

 (sp/action "Set one goes in on the twelfth of December, six of six, and the"
            "last of the six is the terrible game, which is marked as an"
            "assignment about a rule that cannot be broken, which it is.")

 (sp/action "Review is the week after. Nobody works. Everybody works. The"
            "reading room is full at four in the morning for six nights and"
            "the Commons never closes, and there is a fortnight of the year in"
            "which this institution genuinely resembles the thing on the"
            "banner.")

;; ---

 (sp/slug "INT. HALLORAN UNION — ROOM 3C — FRIDAY 17 APRIL")

 (sp/sign "FOUR THOUSAND AND SIX"
          "approved · 138 min · as supplied, 138 min")

 (sp/dialogue "DEREK"
              "Whole.")

 (sp/dialogue "VICTOR"
              ["They're always whole. Have you noticed that these"
               "are always whole?"])

 (sp/dialogue "DEREK"
              "No.")

 (sp/action "It opens on a card and the card is a number and the number is the"
            "title.")

 (sp/sign "FOUR THOUSAND AND SIX APPROVED ACTIVITIES"
          "AND THE NUMBER HAS NEVER GONE DOWN")

 (sp/action "Then a settlement. It is not named and it is not placed and the"
            "countryside around it is not any countryside.")

 (sp/action "The people in it are gathering. That is the offence and the film"
            "is clear about it and says it four times: they have been"
            "gathering, and the gathering is not on the list, and it has been"
            "going on for two years.")

 (sp/beat "The film never says what they gather about.")

 (sp/action "It cannot. A film that reproduced the content would have to have"
            "the content approved and the content is the reason there is a"
            "film.")

 (sp/action "So they talk, at length, in three long scenes, and they are shot"
            "beautifully, and there are no words on the screen and there is no"
            "dubbing and the sound of the room is left in.")

 (sp/action "You watch a hundred people make an argument for nine minutes and"
            "you do not learn one thing about it.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — THE HALL — NIGHT")

 (sp/action "They sing.")

 (sp/beat "The audio is not there.")

 (sp/action "It is not muted and it is not covered with score. It is removed,"
            "and the room tone goes with it, so the picture runs on and there"
            "is nothing coming out of the wall for about forty seconds while a"
            "hundred people sing.")

 (sp/action "Their mouths go on. Some of them have their eyes shut.")

 (sp/dialogue "MILES" "in the dark, quietly"
              "Did the sound go?")

 (sp/dialogue "DEREK"
              "No.")

 (sp/transition "Cut to:")

 (sp/slug "THE FILM — THE ASSIGNMENT — CONTINUOUS")

 (sp/action "It is an assignment. It is not a war, because war is unapproved"
            "and has been since the war, and the film uses the correct word"
            "about eleven times.")

 (sp/action "The unit is nine people and they are extremely likeable and one"
            "of them is funny in a way Derek repeats twice afterwards.")

 (sp/action "The clearance takes twenty-six minutes of screen time.")

 (sp/action "It is well made. The camera stays where the unit is. It does not"
            "linger and it does not look away and it is excited, and the score"
            "under it is the best thing in the film.")

 (sp/action "There is a corridor with two doors on it and the second door"
            "takes four seconds longer than the first, and the film makes"
            "those four seconds thrilling, and they are.")

 (sp/action "Afterwards the unit tidies.")

 (sp/action "The floor of the hall is hosed. There is a vehicle at the door"
            "and the loading is done properly and quickly by people who have"
            "done it before, and the film shows the whole of it because the"
            "whole of it is the point: this is a job, and these are the people"
            "who do it, and they are tired and they are decent to each other.")

 (sp/beat "One of them is sick behind the vehicle and another one carries his"
          "kit for him and neither of them says anything about it.")

 (sp/action "That is the best scene in the film and everybody in Room 3C knows"
            "it.")

 (sp/transition "Cut to:")

 ;; ---

 (sp/action "There is a shot in the transport on the way back of the unit in"
            "rows, in webbing, in the amber light they use in a hold, and one"
            "of them is asleep with his head against the wall.")

 (sp/action "Rows, and amber, and being held.")

 (sp/action "He has about four things from the first eighteen months and he"
            "has never told anybody any of them, because everybody has them"
            "and there is nothing to say.")

 (sp/action "Warm. Amber, through a wall you could see through and could not"
            "see out of.")

 (sp/action "There was another one, close, and it moved when he moved, and it"
            "was a long time before he understood that this was because it was"
            "the same distance from him as he was from it, and not because it"
            "was him.")

 (sp/action "Things arrived. Not from anybody and not in a voice. The word for"
            "a chair, and then the word for a door, and then the words for how"
            "many there are of a thing. He did not learn them, so much had"
            "been given them.")

 (sp/action "And hands, sometimes in gloves, from outside the wall, turning"
            "him so that something could be put against him. It was cold and"
            "it was not unkind and it happened on a schedule and he could tell"
            "when it was coming.")

 (sp/action "And the taste.")

 (sp/action "It changed twice that he can remember. Once it got better and"
            "stayed better for a long time, and once it changed and did not"
            "get better, and he has no idea how long either of those was"
            "because there was no outside and no day.")


 (sp/action "The Commons does the hot line from half six. He has been through"
            "it four hundred times this year and he has never once stood in"
            "that queue and thought about the sac.")

 (sp/action "He thinks about it now, in a dark room, over a shot of nine"
            "people in a hold, and then the film cuts to something else and so"
            "does he.")

 ;; ---
 
 (sp/slug "THE FILM — THE END")

 (sp/action "The unit goes home. There is a woman at a gate and a child and it"
            "is not overdone.")

 (sp/action "Then the closing card.")

 (sp/sign "FOUR THOUSAND AND ELEVEN APPROVED ACTIVITIES"
          "AND THE NUMBER HAS NEVER GONE DOWN")

 (sp/transition "Cut to:")

 (sp/slug "INT. ROOM 3C — CONTINUOUS")

 (sp/dialogue "DEREK"
              ["That's the best thing we've watched all year and"
               "I'm not going to be talked out of it."])

 (sp/dialogue "VICTOR"
              ["It's very well made. It is extremely well made."
               "The corridor is four seconds and they are the four"
               "best seconds on that list."])

 (sp/action "Miles has not said anything since the card.")

 (sp/dialogue "MILES"
              "It's not the same number.")

 (sp/dialogue "DEREK"
              "What isn't?")

 (sp/dialogue "MILES"
              ["The front card and the back card. It opens on four"
               "thousand and six and it closes on four thousand"
               "and eleven."])

 (sp/dialogue "DEREK"
              "So it went up.")

 (sp/dialogue "MILES"
              ["It's the same film. It's a hundred and"
               "thirty-eight minutes. It went up five during the"
               "film."])

 (sp/beat "Derek thinks about it.")

 (sp/dialogue "DEREK"
              ["Then five things got approved while we were"
               "watching it. That's good, Miles."])

 (sp/action "Miles writes it down. He puts it in the column he ruled in"
            "October, which is HEARD, and it is the only thing in that column"
            "that came off a screen.")

 (sp/dialogue "SIMON" "quietly"
              "They sang for forty seconds.")

 (sp/dialogue "VICTOR"
              "They did.")

 (sp/dialogue "SIMON"
              ["No — I mean somebody sat down and took it out."
               "Somebody had it and took it out and put nothing in"
               "the hole. That's a job. Somebody has that job."])

 (sp/beat "Nobody has anything for that.")

 (sp/dialogue "DEREK"
              "The corridor, though.")

 (sp/dialogue "VICTOR"
              "The corridor was very good.")

 (sp/transition "Cut to:")

 (sp/slug "EXT. THE QUAD — 22 DECEMBER")

 (sp/action "The campus thins in about four hours. {pc} watches a family load"
            "a car and it takes them nine minutes and they are laughing the"
            "whole time.")

 (sp/action "MAYA and LENA are going. ELI is going and has said goodbye four"
            "times and come back twice.")

 (sp/dialogue "MAYA"
              "Are you going anywhere?")

 (sp/beat "He says nothing for slightly too long.")

 (sp/dialogue "MAYA"
              "Right.")

 (sp/dialogue :pc
              "The service assignment doesn't stop.")

 (sp/dialogue "MAYA"
              ["The service assignment is two mornings a week and"
               "you know that isn't why."])

 (sp/beat "She goes. At the gate she turns round and says something he does"
          "not hear and does not ask her to repeat, and it is the last thing"
          "anybody says to him for six days.")

 (sp/transition "Cut to:")

 (sp/slug "INT. MERIDIAN HALL — REYES 204 — 29 DECEMBER")

 (sp/action "The building is locked and the door of 204 is open because she is"
            "in it, and she is the only person on that floor.")

 (sp/dialogue :pc
              "Professor?")

 (sp/dialogue "REYES"
              "Term's over. Come back the eighth.")

 (sp/dialogue :pc
              "I wanted to ask what you did. To get here.")

 (sp/action "She looks up and decides how much of her afternoon this is going"
            "to be.")

 (sp/dialogue "REYES"
              "Provisional?")

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "REYES"
              "What did you fix?")

 (sp/dialogue :pc
              "The water on my block.")

 (sp/dialogue "REYES"
              "How?")

 (sp/beat "He does not have an answer to this and never has.")

 (sp/dialogue :pc
              "I hit it.")

 (sp/dialogue "REYES"
              ["I was a Provisional. It isn't a story. There were"
               "four of us in my year and I'm the one who's still"
               "here."])

 (sp/dialogue :pc
              "What happened to the others?")

 (sp/dialogue "REYES"
              "They stopped being here.")

 (sp/beat "She goes back to the screen.")

 (sp/dialogue "REYES"
              ["Don't come to me for this. I've got thirty-one of"
               "you and I like about four, and liking you is not a"
               "service the university provides."])

 (sp/dialogue :pc
              "Okay.")

 (sp/dialogue "REYES"
              "The eighth. Bring the assignment.")

 (sp/action "He is at the door when she speaks again, without turning round.")

 (sp/dialogue "REYES"
              "You'll be fine. Statistically most of you are.")

 (sp/transition "Blackout."))