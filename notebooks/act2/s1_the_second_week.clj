^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 1 — The Second Week"}}}
(ns act2.s1-the-second-week
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.sketch :as sketch]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(sp/assets)

;; # Scene 1 — The Second Week

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 2)

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. PLANT SERVICES — MONDAY, 6:00 A.M.")

 (sp/action "A long low room under the north end of campus. Concrete floor"
            "sloping to a drain. Four sets of waders hanging by the shoulders"
            "like people.")

 (sp/action "The whole of one wall is drawings. Left unframed. Rolled, in"
            "racks, in tubes, hundreds of them, each with a building and a"
            "date on the end of it in four different hands.")

 (sp/action "HOLLIS has one unrolled on the bench with a mug on one corner and"
            "a hammer on the other, and he does not look up when {pc} comes in"
            "and does not ask who he is.")

 (sp/dialogue "HOLLIS"
              ["You're the Provisional. Six o'clock is six"
               "o'clock."])

 (sp/dialogue :pc
              "Sorry.")

 (sp/dialogue "HOLLIS"
              ["It's two minutes past. I'm not making a thing of"
               "it, I'm telling you what it is."])

 (sp/beat "He goes back to the drawing.")

 (sp/dialogue "HOLLIS"
              "Do you know what this is?")

 (sp/dialogue :pc
              "A plan.")

 (sp/dialogue "HOLLIS"
              ["It's Meridian Hall, nineteen forty-one, services."
               "Every pipe and every run and every box in that"
               "building, drawn by a man who never saw it built."])

 (sp/action "He puts a finger on a line that goes off the edge of the sheet.")

 (sp/dialogue "HOLLIS"
              ["And that is where it stops being true, and the job"
               "is knowing where."])

 (sp/dialogue :pc
              "Isn't there a new one?")

 (sp/dialogue "HOLLIS"
              ["There's a new one for the bits they've done since."
               "There's one for forty-one, one for sixty-eight,"
               "two for the nineties and a folder of loose sheets"
               "that nobody signed. None of them agree and all of"
               "them are right about something."])

 (sp/beat "He rolls it and puts it back in the rack and takes another one out"
          "without looking at the labels.")

 (sp/dialogue "HOLLIS"
              ["Four hundred contactors under this campus. I've"
               "asked for eighty a year for eleven years. What I"
               "get is a map that's wrong and a lad in October,"
               "and the maps last longer."])

 (sp/transition "Cut to:")

 (sp/slug "INT. MERIDIAN HALL — BASEMENT — 6:40 A.M.")

 (sp/action "Wet. A torch that wet is worse than no torch. {pc} has a"
            "photocopy of a sheet of 1941 folded into his pocket and a"
            "galvanised bucket in his hand.")

 (sp/action "The job mainly consistst of two responsibilities: The bucket goes"
            "under the pipe by the stair, which has been coming through since"
            "June. Then he walks the run on the sheet and marks where the"
            "sheet is lying.")

 (sp/action "It lies twice in forty feet. A branch that is not on the sheet. A"
            "box on the sheet that is not on the wall.")

 (sp/action "There is already a bucket under the pipe. He puts his next to it.")

 (sp/action "The floor slopes away to the south, further than a floor needs"
            "to, and the sheet stops at a line marked in pencil and the pencil"
            "is not the same pencil as the rest of it.")

 (sp/dialogue :pc "calling up the stair"
              "The drawing stops.")

 (sp/off-screen "HOLLIS"
                "Yes.")

 (sp/dialogue :pc
              "Stops at what?")

 (sp/off-screen "HOLLIS"
                "At the fill.")

 (sp/dialogue :pc
              "What's the fill?")

 (sp/off-screen "HOLLIS"
                ["\"What's under it?\", \"What's it made of?\" As if"
                 "you're the first to ask. Bring the sheet up, don't"
                 "get it wet."])

 (sp/off-screen "HOLLIS"
                ["The fill is just that which the college doesn't"
                 "need anymore All the waste and refuse. It's best"
                 "to pay it no heed,"])

 (sp/off-screen "HOLLIS" "Laughing, the soung getting staticky"
                "\"Unless 'ye be Meridian...\"")

 (sp/transition "Cut to:")

 (sp/slug "INT. PLANT SERVICES — 7:20 A.M.")

 (sp/action "Hollis takes the sheet, looks at the two marks, and writes on the"
            "drawing in pencil, in the fourth hand.")

 (sp/dialogue "HOLLIS"
              "Right. That's the job. That and the bucket.")

 (sp/dialogue :pc
              "How long has that branch not been on there?")

 (sp/dialogue "HOLLIS"
              ["Since before me. I've been forty-one years and"
               "I've put about nine hundred marks on these and the"
               "man before me put more."])

 (sp/beat "He hangs it back in the rack.")

 (sp/dialogue "HOLLIS"
              ["What did you fix, then? They said you fixed"
               "something."])

 (sp/dialogue :pc
              ["The water on my block. It stopped in February and"
               "nobody came, and then it came back on."])

 (sp/dialogue "HOLLIS"
              "Came back on how?")

 (sp/beat "This is the part {pc} has never had a great answer to.")

 (sp/dialogue :pc "With a closed fist and a pounding gesture"
              "I hit it, kinda like this.")

 (sp/action "Hollis puts the mug down. It is the first time he has shown any"
            "real interest in the conversation.")

 (sp/dialogue "HOLLIS" "With hands on his hips, and another at his beard"
              "Whereabouts?")

 (sp/dialogue :pc
              "The flat panel on the side, near the bottom.")

 (sp/dialogue "HOLLIS"
              ["Yyyyep, thought so. That could definitely be a"
               "contactor. There'll be a spring in it. And by God"
               "do those springs stick- if you catch it near"
               "enough... *click!* it drops. Just like that, and"
               "the pump goes."])

 (sp/dialogue :pc
              "So that's what it was.")

 (sp/dialogue "HOLLIS"
              ["Could be. Could be it came back on and you were"
               "stood right there in front of it. Forty-one years"
               "and I've seen it both ways and I've never once"
               "been able to tell you which."])

 (sp/beat "He picks the mug up again.")

 (sp/dialogue "HOLLIS"
              ["What I'd say is you touched it. Nobody touches"
               "anything."])

 (sp/transition "Cut to:")

 (sp/slug "INT. MERIDIAN HALL — CS 101 — MONDAY, 8:00 A.M.")

 (sp/action "{pc} comes in wet to the knee. Nobody asks.")

 (sp/dialogue "REYES"
              ["We did the first three last week."
               "These three draw. Starting with program four:"])

 (sp/action "Dots appear on the page, one at a time, faster than the eye can"
            "keep up with. For a moment it is nothing. Then it forms equal"
            "triangles going in, and in, and in.")

 (sp/dialogue "A STUDENT"
              "Is it a picture of something?")

 (sp/dialogue "REYES"
              ["It's ten thousand dots and one rule. I'm not"
               "telling you the rule, because that is the point of"
               "the class."])

 (sp/dialogue "REYES"
              "Five.")

 (sp/action "Fifteen numbers and a hole. She slides four of them, then tries"
            "to slide one that is not next to the hole, and the page does not"
            "let her.")

 (sp/dialogue "REYES"
              ["See that? It said no."
               "That's the assignment."])

 (sp/dialogue "REYES"
              "Last one, and then you can go back to sleep.")

 (sp/action "A square. Inside it a smaller square, turned. Inside that, a"
            "smaller one, turned the same way again, and again, and the middle"
            "of the page is filled with them.")

 (sp/dialogue "A STUDENT"
              "How do you get it to turn?")

 (sp/dialogue "REYES"
              "Ask your helps how to move things on a canvas.")

 (sp/dialogue "A STUDENT"
              "Do we need the geometry?")

 (sp/beat "Reyes puts the page away and looks at the student.")

 (sp/dialogue "REYES"
              ["You only need one idea. There's a hard way that"
               "works and an easy way that works and both get the"
               "marks, so take whichever one you can get to the"
               "end of."])

 (sp/dialogue "A STUDENT"
              "Which did you do?")

 (sp/dialogue "REYES"
              "The hard way. Twice.")

 (sp/transition "Cut to:")

 (sp/slug "INT. WHITLOCK HALL — LECTURE ROOM 1 — MONDAY, 4:00 P.M.")

 (sp/action "Two hundred and forty seats. Nine people in them.")

 (sp/action "PROFESSOR WHITLOCK is old and takes a while to stand. There is a"
            "wooden box on the desk in front of him with photographs in it and"
            "he takes them out one at a time and holds them up, and nine"
            "people at the front of a room built for two hundred and forty"
            "lean forward to see a photograph the size of a hand.")

 (sp/action "MAYA is in the second row because Carter's bench work is on"
            "Tuesdays now. She has brought {pc}, who has not been to a lecture"
            "nobody made him go to.")

 (sp/action "LENA is in the front row and is not taking notes. She has a flat"
            "grey box on her knees with forty of the photographs in it in"
            "sleeves, which she made.")

 (sp/dialogue "WHITLOCK"
              ["This is the ground. Before any of it. 15 acres,"
               "flat, which is why they took it."])

 (sp/action "He holds up a photograph of a field.")

 (sp/dialogue "WHITLOCK"
              ["And this is the same ground the following spring,"
               "and you can see the line of the refectory going"
               "in, and - Miss Novak, is that the one?"])

 (sp/dialogue "LENA" "Leans in for a closer look"
              "Uhhh, yeah that's the one.")

 (sp/dialogue "WHITLOCK"
              ["Miss Novak has copied these because the originals"
               "are fading. They are fading because nobody has"
               "kept them at the right temperature for 50 odd"
               "years and not because of anything sinister, which"
               "I say because somebody asks every year."])

 (sp/beat "Nobody laughs, nor was he being funny.")

 (sp/action "He holds up the second photograph. It is the same field with a"
            "trench across it and a stack of brick beside the trench.")

 (sp/dialogue "MAYA" "quietly, to {pc}"
              "That's not 15 acres of nothing...")

 (sp/dialogue :pc
              "What?")

 (sp/dialogue "MAYA"
              ["There's a stack of brick in it. Nobody carries"
               "brick onto a field. You take brick off a field."])

 (sp/beat "Whitlock has heard her, because the room is nine people.")

 (sp/dialogue "WHITLOCK"
              ["Yes. There was something here. It is in every"
               "history of the institution, in one sentence, and"
               "the sentence is that the site was cleared."])

 (sp/dialogue "MAYA"
              "Cleared of what?")

 (sp/dialogue "WHITLOCK"
              ["That's a good question. I have been asking it"
               "since nineteen sixty-two, and I would like you to"
               "understand that I am not being mysterious with"
               "you. I do not know."])

 (sp/beat "He puts the photograph back in the box.")

 (sp/dialogue "WHITLOCK"
              ["Miss Novak has been through the whole box twice"
               "and she will tell you the same."])

 (sp/dialogue "LENA" "without turning round"
              "Forty-one photographs. Nothing after the trench.")

 (sp/transition "Cut to:")

 (sp/slug "EXT. WHITLOCK HALL — THE STEPS — 5:10 P.M.")

(sp/action "Lena closes the camera with two elastic bands, because the clasp"
           "has been broken since before the camera was hers. She has been"
           "holding it shut like this since the day she got it and has stopped"
           "noticing she does it.")

 (sp/dialogue "MAYA"
              "Why are you doing it?")

 (sp/dialogue "LENA"
              ["Because he asked me. He came to the print shop"
               "himself, at his speed, up the four flights of"
               "stairs, and asked whether I would copy some"
               "photographs. I don't know what else I'd do."])

 (sp/beat "She puts the second band on.")

 (sp/dialogue "MAYA"
              "And the photographs?")

 (sp/dialogue "LENA"
              ["The photographs are the other reason. There are"
               "forty-one of them and there ought to be more."])

 (sp/dialogue :pc
              "Why ought there?")

 (sp/dialogue "LENA"
              ["Because they are numbered on the back and the"
               "numbers go to seventy-three."])

 (sp/action "She puts the box under her arm and goes.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "Tuesday he starts the squares program, using lines, for the"
            "simple fact that a square is four lines and he knows how to"
            "describe a line mathematically: where it starts and how steep it"
            "is."))

^:kindly/hide-code
(kind/code
 "(defn slope [[x1 y1] [x2 y2]]
  (/ (- y2 y1) (- x2 x1)))

(slope [0 0] [100 0])   ;; 0
(slope [0 0] [0 100])   ;; ?")

^:kindly/hide-code
(art/terminal
 "$ clj -M -e \"(slope [0 0] [0 100])\""
 "Execution error (ArithmeticException)"
 "Divide by zero"
 "$")

^:kindly/hide-code
(sp/scene
 (sp/action "Two of the four sides of a square go straight up. A line that"
            "goes straight up goes up any amount you like for none across, and"
            "the page will not have it. By Thursday there are four special"
            "cases and the corners are in the wrong order and the whole thing"
            "leans."))

^:kindly/hide-code
(art/discord
 {:server "FEDORA SOCIETY" :channel "#general"}
 [{:who "{pc}" :at "1:14 AM" :text "does anyone know how to do a line thats straight up"}
  {:who "Derek" :at "1:19 AM" :text "up how"}
  {:who "{pc}" :at "1:19 AM" :text "straight up. vertical"}
  {:who "Derek" :at "1:22 AM" :text "draw it sideways then turn the page"}
  {:who "Miles" :at "1:26 AM" :text "^"}
  {:who "Victor" :role "PRESIDENT" :at "8:02 AM"
   :text ["Coursework is not a matter for #general."
          "There is no channel for coursework. I will consider one."]}])

^:kindly/hide-code
(sp/scene
 (sp/action "Friday, in the basement, with the second bucket, he is thinking"
            "about a man putting a pencil mark on a drawing that is wrong, in"
            "the fourth hand, without stopping to work out why it is wrong.")

 (sp/action "He doesn't necessarily need the angle of anything. He needs a"
            "mark a little way along each side, and the four marks are the"
            "next square, and then he does the same thing to those.")

 (sp/action "First, though, the thing he knows he is going to want, which is a"
            "way to do something, then do it again to what came out, and keep"
            "every step on the way, because he would like to watch the squares"
            "arrive one at a time rather than all at once at the end."))

^:kindly/hide-code
(sp/code
 "(defn each
   \"Start with a thing. Do the thing to it, n times. Keep all of them.\"
   [start f n]
   (reduce (fn [acc _] (conj acc (f (peek acc)))) [start] (range n)))

 (each 1 (fn [x] (* 2 x)) 8)")

^:kindly/hide-code
(sp/scene
 (sp/action "Eleven lines of nothing, written on a Tuesday, used for the rest"
            "of the year. In the page it is called frames!, because the page"
            "draws between the steps."))

^:kindly/hide-code
(sp/code
 "(defn corners [size]
  [[0 0] [size 0] [size size] [0 size]])

(defn inset
  \" Walk a little way along each side, in order, and keep where you got
  to. The corner you are walking towards is the next one round.\"
  [pts t]
  (mapv (fn [[x1 y1] [x2 y2]]
          [(+ x1 (* t (- x2 x1)))
           (+ y1 (* t (- y2 y1)))])
        pts
        (conj (subvec pts 1) (nth pts 0))))

(inset (corners 100) 0.12)

(def rings
  (each (corners 100) (fn [square] (inset square 0.12)) 39))

(count rings)

(kind/hiccup
 [:svg {:viewBox \" -6 -6 112 112 \" :width 380
        :xmlns \"http://www.w3.org/2000/svg\"}
  [:rect {:x -6 :y -6 :width 112 :height 112 :style {:fill \"var(--paper)\"}}]
  (for [pts rings]
    [:polygon {:points (str/join \" \" (map (fn [[x y]] (str x \",\" y)) pts))
               :stroke-width 0.4
               :style {:fill \"none\" :stroke \"var(--ink)\"}}])])")

^:kindly/hide-code
(defn each
  "Start with a thing. Do the thing to it, n times. Keep all of them. "
  [start f n]
  (reduce (fn [acc _] (conj acc (f (peek acc)))) [start] (range n)))

^:kindly/hide-code
(each 1 (fn [x] (* 2 x)) 8)

^:kindly/hide-code
(defn corners [size]
  [[0 0] [size 0] [size size] [0 size]])

^:kindly/hide-code
(defn inset
  " Walk a little way along each side, in order, and keep where you got
   to. The corner you are walking towards is the next one round. "
  [pts t]
  (mapv (fn [[x1 y1] [x2 y2]]
          [(+ x1 (* t (- x2 x1)))
           (+ y1 (* t (- y2 y1)))])
        pts
        (conj (subvec pts 1) (nth pts 0))))

^:kindly/hide-code
(inset (corners 100) 0.12)

^:kindly/hide-code
(def rings
  (each (corners 100) (fn [square] (inset square 0.12)) 39))

^:kindly/hide-code
(count rings)

^:kindly/hide-code
(kind/hiccup
 [:svg {:viewBox " -6 -6 112 112 " :width 380
        :xmlns "http://www.w3.org/2000/svg"}
  [:rect {:x -6 :y -6 :width 112 :height 112 :style {:fill "var(--paper)"}}]
  (for [pts rings]
    [:polygon {:points (str/join " " (map (fn [[x y]] (str x "," y)) pts))
               :stroke-width 0.4
               :style {:fill "none" :stroke "var(--ink)"}}])])

^:kindly/hide-code
(sp/scene
 (sp/action "The page gives you somewhere to draw and four things to draw"
            "with. There is nothing in it that is hidden.")

 (sketch/cell
  {:id "squares" :label "SQUARES.CLJS" :world [100 100] :rows 16}
  "(defn inset [pts t]
  (mapv (fn [[x1 y1] [x2 y2]]
          [(+ x1 (* t (- x2 x1)))
           (+ y1 (* t (- y2 y1)))])
        pts
        (conj (subvec pts 1) (nth pts 0))))

(def square (atom [[0 0] [100 0] [100 100] [0 100]]))

;; 40 squares, one a frame. Try 0.4 instead of 0.12.
(frames! 40 1
  (fn [_]
    (poly @square)
    (swap! square inset 0.12)))")

 (sp/action "No slope in it anywhere and it doesn't maintain any conception of"
            "which way a side is pointing. It turns. He did not tell it to"
            "turn and he cannot entirely say why it turns, and he sits looking"
            "at it for longer than the assignment took."))