^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 3 — Breakout"}}}
(ns act3.s3-breakout
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.breakout.breakout :as breakout]
            [fedoras.sketch :as sketch]))

^:kindly/hide-code
(sp/assets)

;; # Scene 3 — Breakout

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 23)

^:kindly/hide-code
(sp/scene
 (sp/action "Since the assessment there have been four. He has counted them"
            "the way you count anything you have decided not to tell anybody"
            "about: badly, and only afterwards.")

 (sp/action "They come the same way every time and the order does not change."
            "The smell first, which is a coin held too long. Then a stretch he"
            "is not there for, which was seconds in September and is not"
            "seconds now. Then the nose, which is the part other people see,"
            "and which is therefore the part that has to be managed.")

 (sp/action "Two of the four were in the basement. He has not put those two"
            "next to the other two, because to put them next to each other he"
            "would have to write them down.")

 (sp/slug "INT. STERLING CENTER — AN INNER OFFICE — FRIDAY 6 FEBRUARY, 3:00 P.M.")

 (sp/action "One window, one desk, a chair on each side of it, and a screen"
            "turned away.")

 (sp/action "VICE DEAN PARR. He gives his name at the start and it is the only"
            "thing about him that is not a form.")

 (sp/dialogue "PARR"
              "The schedule came back on the sixteenth.")

 (sp/dialogue :pc
              "Came back where?")

 (sp/dialogue "PARR"
              "Here.")

 (sp/action "He does not turn the screen round.")

 (sp/dialogue "PARR"
              ["Question three-a. A hundred and forty hours below"
               "ground level."])

 (sp/dialogue :pc
              "It's a service assignment. They sent me.")

 (sp/dialogue "PARR"
              "Yes. It's ended.")

 (sp/dialogue :pc
              "Ended how?")

 (sp/dialogue "PARR"
              ["The whole line's been contracted out. It isn't"
               "about you and I'd rather you didn't take it as"
               "though it were."])

 (sp/beat "He scrolls.")

 (sp/dialogue "PARR"
              "Question four. You said no.")

 (sp/dialogue :pc
              "I did.")

 (sp/dialogue "PARR"
              "Yes.")

 (sp/beat "He goes on scrolling.")

 (sp/dialogue "PARR"
              "That's the whole of question four.")

 (sp/action "{pc} does not say anything.")

 (sp/dialogue "PARR"
              ["Your standing is Provisional. Provisional gets"
               "looked at in March, ahead of the rest of it,"
               "because it's the one that costs money."])

 (sp/dialogue :pc
              "Looked at by who?")

 (sp/dialogue "PARR"
              "It's looked at.")

 (sp/action "He turns a card over on the desk and does not read it.")

 (sp/dialogue "PARR"
              ["Meridian takes four from here a year. It isn't a"
               "component of review. It replaces review."])

 (sp/dialogue :pc
              "What is it?")

 (sp/dialogue "PARR"
              "A placement.")

 (sp/dialogue :pc
              "Doing what?")

 (sp/beat "He looks at the card properly for the first time.")

 (sp/dialogue "PARR"
              "It doesn't say.")

 (sp/dialogue :pc
              "Where is it?")

 (sp/dialogue "PARR"
              "It doesn't say that either.")

 (sp/beat "There is nothing in his face that suggests he is holding anything"
          "back. He has read the whole card.")

 (sp/dialogue "PARR"
              ["And if it isn't for you there's an array. There's"
               "always an array. There's a seat on one the week"
               "after March and a submanager who'd know your name"
               "by the Friday."])

 (sp/action "The smell arrives while he is saying it.")

 (sp/beat "It is a coin held too long.")

 (sp/dialogue "PARR"
              ["I'd not have you think of that as a failure. It's"
               "honest, it's well paid, and people are proud of"
               "it."])

 (sp/action "{pc} is looking at the edge of the desk.")

 (sp/dialogue "PARR"
              "Are you with me?")

 (sp/beat "Two seconds.")

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "PARR"
              "Say the last thing I said.")

 (sp/dialogue :pc
              "The array. The week after March.")

 (sp/beat "It is the right answer and it is not the last thing he said.")

 (sp/action "Parr looks at him for a moment, and then does not pursue it, and"
            "afterwards {pc} will not be able to decide which of those two"
            "things was the kindness.")

 (sp/dialogue "PARR"
              "Right.")

 (sp/action "He scrolls once more and stops.")

 (sp/dialogue "PARR"
              "You've nominated nobody.")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "PARR"
              ["That isn't a criticism. It's a field on a page and"
               "it's blank and I'm obliged to ask whether that's"
               "current."])

 (sp/dialogue :pc
              "It's current.")

 (sp/dialogue "PARR"
              "So there's nobody who'd need to be told.")

 (sp/beat "He says it the way you would note that a room has no window.")

 (sp/dialogue "PARR"
              ["You've until the end of term. Say yes and March"
               "doesn't happen to you."])

 (sp/action "He puts the card in a drawer.")

 (sp/dialogue "PARR"
              ["One more thing and it isn't a condition. There's"
               "no assignment for you below ground any more and"
               "there's no reason for you to be down there. That's"
               "advice."])

 (sp/dialogue :pc
              "Is advice recorded?")

 (sp/dialogue "PARR"
              "Everything's recorded.")

 (sp/transition "Cut to:")

;; ---

 (sp/action "It stops on the stairs. He asks his Help about the array on the"
            "way down, because everyone asks their Help, and because it is the"
            "only part of the conversation that had a name in it.")

 (art/ai-chat
  {:model "HELP · personal · v9"}
  [{:role :user :text "whats an array placement actually like"}
   {:role :assistant
    :text ["You are given a desk, a seat and a feed line. Aftercall is"
           "the interval between one contact and the next and is the"
           "figure your submanager will discuss with you."
           "Sessions are four hours. Most placements are two sessions."
           "Hours accrue against your name and may be balloted."]}
   {:role :user :text "for how long"}
   {:role :assistant
    :text ["Placements are open-ended. The median placement on the"
           "arrays is thirty-one years."]}
   {:role :user :text "thirty one years"}
   {:role :assistant :text "Yes."}
   {:role :user :text "is that a long time for a job"}
   {:role :assistant
    :text ["It is the median. Longer placements are common and are"
           "recognised."]}
   {:role :user :text "recognised where"}
   {:role :assistant
    :text ["At the desk, by the array, and in the programme."]}
   {:role :user :text "what programme"}
   {:role :assistant
    :text ["THE FLOOR. Fourteen seasons. Approved."
           "Would you like me to add you to a list?"]}])

 (sp/action "Thirty-one years is the median, which means half of them are"
            "longer, and it says so pleasantly, and the last thing it says is"
            "an offer.")

;; ---

 (sp/slug "INT. PLANT SERVICES — 5:40 P.M.")

 (sp/action "The waders are off the wall and folded on the bench, which is the"
            "first thing {pc} notices and he could not say why.")

 (sp/action "HOLLIS is sitting on a stack of buckets with a mug. He has been"
            "drinking since about four and he is not loud with it.")

 (sp/dialogue "HOLLIS"
              "They've contracted it.")

 (sp/dialogue :pc
              "Contracted what?")

 (sp/dialogue "HOLLIS"
              "It.")

 (sp/action "He puts the mug down on the bench, misses the edge by an inch,"
            "and catches it.")

 (sp/dialogue "HOLLIS"
              ["Eighty a year I asked for. I had forty, once, and"
               "it's in the book, and you've read the book."])

 (sp/dialogue :pc
              "I have.")

 (sp/beat "Hollis looks up.")

 (sp/dialogue "HOLLIS"
              "Have you.")

 (sp/action "He does not follow it up. Then he looks a moment longer than he"
            "has looked at him all term.")

 (sp/dialogue "HOLLIS"
              "You're getting the nose.")

 (sp/beat "{pc} does not answer, which is an answer.")

 (sp/dialogue "HOLLIS"
              "Fenner had them.")

 (sp/dialogue :pc
              "Had them how often?")

 (sp/dialogue "HOLLIS"
              "Not to start with.")

 (sp/action "He drinks.")

 (sp/dialogue "HOLLIS"
              "Don't dig.")

 (sp/dialogue :pc
              "You said that in September.")

 (sp/dialogue "HOLLIS"
              "I did.")

 (sp/dialogue "HOLLIS"
              ["It's not advice. It's what I was told to say."
               "First morning, stood where you are."])

 (sp/dialogue :pc
              "Told by who?")

 (sp/dialogue "HOLLIS"
              "Fenner.")

 (sp/dialogue :pc
              "Where's Fenner?")

 (sp/dialogue "HOLLIS"
              "Dead.")

 (sp/beat "Neither of them puts those two answers next to each other out loud.")

 (sp/action "{pc} looks at the map on the wall, at the ink on the south sheet"
            "that stops two thirds of the way down in the middle of nothing.")

 (sp/dialogue "HOLLIS"
              ["Forty-one years. I have never been past the second"
               "junction box."])

 (sp/dialogue :pc
              "Why not?")

 (sp/beat "Hollis thinks about it, which is the part that is hard to watch.")

 (sp/dialogue "HOLLIS"
              "Nobody stopped me.")

 (sp/action "There is one ring on the hooks by the door and there has always"
            "been one ring on the hooks by the door.")

 (sp/action "{pc} takes it. Hollis watches him do it and does not put the mug"
            "down.")

 (sp/dialogue "HOLLIS"
              ["The long one's the stair. The one with the tape on"
               "it is the grate at the bottom, and it wants"
               "lifting, not turning."])

 (sp/dialogue :pc
              "Right.")

 (sp/beat "He is at the door before Hollis says the last of it.")

 (sp/dialogue "HOLLIS"
              "It got worse for him at the end.")

 (sp/transition "Blackout.")

;; ---

 (sp/action "A bat, a ball, and a wall between them.")

 (def wall
   (set (for [row (range 3) col (range 10)] [row col])))

 (defn brick-at
   "Which brick is at this point, if any. The wall is the top of the
  board cut into thirty pieces and the arithmetic is the whole of it."
   [bricks x y]
   (when (and (>= x 0) (< x 100) (>= y 52) (< y 70))
     (let [col (int (/ x 10))
           row (int (/ (- y 52) 6))]
       (when (bricks [row col]) [row col]))))

 (defn step
   "Where everything is, a moment later."
   [{:keys [ball vel bat bricks broken lost] :as state}]
   (let [[x y]   ball
         [dx dy] vel
         nx (+ x dx)
         ny (+ y dy)
         hit (brick-at bricks nx ny)]
     (cond
       hit                      (assoc state :bricks (disj bricks hit)
                                       :vel   [dx (- dy)]
                                       :ball  [nx (- y dy)]
                                       :broken (inc broken))
       (or (< nx 0) (> nx 100)) (assoc state :ball [(max 0 (min 100 nx)) ny] :vel [(- dx) dy])
       (> ny 70)                (assoc state :ball [nx 70] :vel [dx (- dy)])
       (and (< ny 3)
            (< (Math/abs (- nx bat)) 10))
       (assoc state :ball [nx 3] :vel [dx (- dy)])
       (< ny 0)                 (assoc state :ball [50 20] :vel [0.8 0.9] :lost (inc lost))
       :else                    (assoc state :ball [nx ny]))))

 (def start
   {:ball [50 20] :vel [0.8 0.9] :bat 50 :bricks wall :broken 0 :lost 0})

 (count wall)

 (sp/action "Thirty bricks, and a bat that does not move, and four thousand"
            "moments.")

 (let [finish (last (take 4000 (iterate step start)))]
   {:broken (:broken finish)
    :left   (count (:bricks finish))
    :lost   (:lost finish)})

 (sp/action "It takes the whole wall down without anybody touching it, and"
            "drops the ball fifteen times doing it.")

 (defn when-each-fell
   "The moment each brick came out."
   [state]
   (->> (iterate step state)
        (take 4000)
        (partition 2 1)
        (keep-indexed (fn [i [a b]] (when (> (:broken b) (:broken a)) i)))
        vec))

 (let [ms (when-each-fell start)]
   {:first-twenty-six (nth ms 25)
    :all-thirty       (nth ms 29)})

 (sp/action "Twenty-six of them take one thousand five hundred and four"
            "moments. The last four take sixteen.")

 (sp/action "Somewhere around the fifteen hundredth the ball goes up through a"
            "gap it made itself and gets above the wall, and there is nothing"
            "up there for it to lose to, and it comes down through the bricks"
            "and goes back up through them and there is nothing left to do.")

 (sp/action "He watches that happen with his hands off the keyboard and does"
            "not think about it again for four months.")

 ^:kindly/hide-code
 (sketch/cell
  {:id "breakout" :label "BREAKOUT.CLJS · as handed in" :world [100 70] :rows 30}
  "(defn full-wall [] (set (for [r (range 3) c (range 10)] [r c])))
 
(def wall  (atom (full-wall)))
(def state (atom {:ball [50 20] :vel [0.8 0.9] :bat 50 :broken 0 :lost 0}))
 
(set! (.-onmousemove cv) (fn [e] (swap! state assoc :bat (/ (.-offsetX e) SX))))
(set! (.-onclick cv)
      (fn [_] (reset! wall (full-wall))
              (reset! state {:ball [50 20] :vel [0.8 0.9] :bat (:bat @state) :broken 0 :lost 0})))
 
(defn brick-at [x y]
  (when (and (>= x 0) (< x 100) (>= y 52) (< y 70))
    (let [col (int (/ x 10)) row (int (/ (- y 52) 6))]
      (when (@wall [row col]) [row col]))))
 
(defn step [{:keys [ball vel bat broken lost] :as s}]
  (let [[x y] ball [dx dy] vel
        nx (+ x dx) ny (+ y dy)
        hit (brick-at nx ny)]
    (cond
      hit (do (swap! wall disj hit)
              (assoc s :vel [dx (- dy)] :ball [nx (- y dy)] :broken (inc broken)))
      (or (< nx 0) (> nx 100)) (assoc s :ball [(max 0 (min 100 nx)) ny] :vel [(- dx) dy])
      (> ny 70) (assoc s :ball [nx 70] :vel [dx (- dy)])
      (and (< ny 3) (< (Math/abs (- nx bat)) 10)) (assoc s :ball [nx 3] :vel [dx (- dy)])
      (< ny 0) (assoc s :ball [50 20] :vel [0.8 0.9] :lost (inc lost))
      :else (assoc s :ball [nx ny]))))
 
(defn box [x1 y1 x2 y2] (poly [[x1 y1] [x2 y1] [x2 y2] [x1 y2]]))
 
(defn draw [{:keys [ball bat broken lost]}]
  (clear!)
  (doseq [[r c] @wall]
    (box (+ (* c 10) 0.5) (+ 52 (* r 6) 0.5)
         (+ (* c 10) 9.5) (+ 52 (* r 6) 5.5)))
  (let [[x y] ball] (box (- x 1) (- y 1) (+ x 1) (+ y 1)))
  (box (- bat 10) 0.5 (+ bat 10) 2.5)
  (.fillText ctx (str broken \" / 30   lost \" lost
                      (when (empty? @wall) \"   cleared — click to build it again\"))
             8 14))
 
(frames! 100000 1 (fn [_] (swap! state step) (draw @state)))")

 (sp/slug "INT. HALLORAN UNION — THE PRINT SHOP — THE FOLLOWING WEEK")

 (sp/action "He has it open on the counter because Lena asked what he had been"
            "doing and he did not have a better answer.")

 (sp/action "ELI plays it for four minutes and loses eleven times.")

 (sp/dialogue "ELI"
              "I can't steer it.")

 (sp/dialogue :pc
              "You move the bat.")

 (sp/dialogue "ELI"
              ["I move the bat and it comes off the bat the same"
               "way every time. That's not a bat, that's a wall."])

 (sp/beat "That is the best note anybody gives him all year and it takes"
          "eleven minutes to do.")

 (sp/action "Where it lands on the bat now decides where it goes, and the game"
            "becomes a game, and Eli plays it for forty minutes and clears it"
            "and is insufferable.")

 (sp/transition "Cut to:")

 (sp/slug "INT. HALLORAN UNION — ROOM 3C — WEDNESDAY, BEFORE")

 (sp/action "Four of them have played it now, on the machine, before Victor"
            "starts.")

 (sp/dialogue "ELI"
              "It's still too hard.")

 (sp/dialogue :pc
              "You cleared it.")

 (sp/dialogue "ELI"
              ["Once. Make the bat bigger and I'd clear it every"
               "time and I'd enjoy myself, which is what it's for."])

 (sp/dialogue "DEREK"
              "What if there were two balls?")

 (sp/dialogue :pc
              "Then you'd lose both of them.")

 (sp/dialogue "DEREK"
              ["No, that's the point. Two balls is more of the"
               "good thing."])

 (sp/dialogue "MILES"
              ["I'd want to be able to hold it and aim. Not react."
               "Hold it, look at what's left, and put it where I"
               "want it."])

 (sp/dialogue "VICTOR"
              "It ends.")

 (sp/dialogue :pc
              "It's meant to end.")

 (sp/dialogue "VICTOR"
              ["Everything on that sheet ends. This is the one you"
               "did because you wanted to. It shouldn't be able to"
               "end."])

 (sp/beat "Nobody argues with him and afterwards nobody can say why not.")

 (sp/action "MAYA plays it once, on the Thursday, at the bench, between"
            "readings.")

 (sp/dialogue "MAYA"
              "How many did I get?")

 (sp/dialogue :pc
              "It says on the—")

 (sp/dialogue "MAYA"
              ["It says how many are out of thirty. I want how"
               "many I got, and how many I lost getting them, and"
               "I want it after, not while."])

 (sp/beat "That is the only request anybody makes that is not a feature.")

 (sp/action "SIMON plays it on the Wednesday for about ten minutes and does"
            "not ask for anything.")

 (sp/dialogue "SIMON"
              "That's good, that.")

 (sp/dialogue :pc
              "What would you change?")

 (sp/dialogue "SIMON"
              "Nothing.")

 (sp/beat "It is the only note he cannot act on and it is the only one he"
          "thinks about afterwards.")

 (sp/transition "Blackout.")

 (sp/action "He does all of them. He does them over about a fortnight, in the"
            "order they were asked for, and he does not take any of them out,"
            "because taking one out would be taking a thing off somebody who"
            "asked for it.")

 (sp/action "Here it is with all of them in it. The list is at the top and it"
            "is the only part of the program that is not the program.")

 (sp/action "Take things out of it and find out which one it was.")

 ^:kindly/hide-code
 (breakout/widget)

 (sp/action "It has a wide bat and three balls and you can hold it and aim it"
            "and the wall comes back and there is a running count of"
            "everything, and every one of those was asked for by somebody who"
            "plays it.")

 (sp/action "It is not a game.")

 (sp/action "He cannot say which one did it. He has gone back through them and"
            "taken them out one at a time and put each one back, and there is"
            "no single one of them that is the problem, and every one of them"
            "was reasonable, and the list has never once got shorter.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Derek" :at "6 Feb, 7:04 PM" :text "film in 3c. its the rooms one again"}
   {:who "Miles" :at "6 Feb, 7:22 PM" :text "wheres the ranger"}
   {:who "Derek" :at "6 Feb, 7:40 PM" :text "no idea"}
   {:who "Simon" :at "6 Feb, 9:15 PM" :text "hes fine. hes in his room"}
   {:who "Miles" :at "6 Feb, 9:16 PM" :text "how do you know"}
   {:who "Simon" :at "6 Feb, 9:31 PM" :text "because i went and looked"}
   {:who "Victor" :role "PRESIDENT" :at "6 Feb, 11:58 PM"
    :text ["Attendance Friday four of five."
           "Absence is given before the day, in words. That is Article"
           "Seven and it has not been amended."]}
   {:who "Victor" :role "PRESIDENT" :at "6 Feb, 11:59 PM" :text "Nothing further."}
   {:who "Simon" :at "7 Feb, 1:14 AM" :text "random question does anyone else get nosebleeds"}
   {:who "Simon" :at "7 Feb, 2:40 AM" :text "ignore that"}])

 (sp/action "Nobody answers Simon, because it is a quarter past one in the"
            "morning, and because it is a question about a nosebleed.")

;; ---

 (sp/slug "INT. PRENTISS SCIENCE CENTER — THE BENCH ROOM — 11:00 P.M.")

 (sp/action "MAYA has a run on and a column of the same number going back to"
            "six o'clock.")

 (sp/action "She looks up when he comes in and then looks at him for about a"
            "second longer than looking up takes.")

 (sp/dialogue "MAYA"
              "How many is that now?")

 (sp/beat "He had not decided whether he was going to tell her and now does"
          "not have to.")

 (sp/dialogue :pc
              "Five.")

 (sp/dialogue "MAYA"
              "Since when?")

 (sp/dialogue :pc
              "Since the assessment.")

 (sp/dialogue "MAYA"
              "Since January.")

 (sp/dialogue :pc
              "Yes.")

 (sp/action "She writes the flask reading down, and the time next to it, and"
            "then turns to a clean page at the back of the notebook and writes"
            "something that is not a flask reading.")

 (sp/dialogue "MAYA"
              "Tonight's one. Time and length.")

 (sp/dialogue :pc
              "Thirteen minutes.")

 (sp/beat "Her hand stops.")

 (sp/dialogue "MAYA"
              "Thirteen.")

 (sp/dialogue :pc
              "It was seconds in September.")

 (sp/action "She writes it down. She does not say any of the four things that"
            "a person says here, and afterwards he will think that was the"
            "single kindest thing anybody did to him all year.")

 (sp/dialogue "MAYA"
              "What else.")

 (sp/action "He tells her all of it, in about four minutes, badly, and she"
            "does not interrupt once.")

 (sp/dialogue "MAYA"
              ["Twelve cards in one drawer say a building that"
               "isn't on the map, and every one of them says held."])

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "MAYA"
              ["And two of your five were in that basement, and"
               "you've worked that out and you've been sitting on"
               "it."])

 (sp/dialogue :pc
              "Yes.")

 (sp/dialogue "MAYA"
              ["Then going down there tonight is the stupidest"
               "available thing and I want that in the book before"
               "I get my coat."])

 (sp/action "She writes that down too, and dates it, and gets her coat.")

 (sp/transition "Cut to:")

 (sp/slug "INT. HALLORAN UNION — THE PRINT SHOP — 11:20 P.M.")

 (sp/action "LENA is trimming a run with a steel rule and a scalpel, because"
            "the machine that does it lives behind a door that shuts at ten.")

 (sp/dialogue "LENA"
              "Is there a way back up that isn't the way down?")

 (sp/dialogue :pc
              "I don't know.")

 (sp/dialogue "LENA"
              "No. All right.")

 (sp/action "She puts the scalpel in her pocket, and the rule, and turns the"
            "light off with her elbow.")

 (sp/dialogue "LENA"
              "Three institutions. I've never been under one.")

 (sp/beat "That is the only reason she gives and it is the true one.")

 (sp/action "At the door she stops.")

 (sp/dialogue "LENA"
              "When it happens down there. What do you want done?")

 (sp/beat "Nobody has asked him that.")

 (sp/dialogue :pc
              "Nothing. Wait for it.")

 (sp/dialogue "LENA"
              "Right.")

 (sp/action "She does not ask him anything else about it, that night or at any"
            "point afterwards, and it is the reason he tells her things first"
            "for the rest of the year.")

 (sp/transition "Cut to:")

 (sp/slug "EXT. THE QUAD — 11:30 P.M.")

 (sp/action "ELI is coming back from the Guild with a tube of posters under"
            "his arm and hears about forty seconds of it.")

 (sp/dialogue "ELI"
              "Is it on the list?")

 (sp/dialogue :pc
              "No.")

 (sp/dialogue "ELI"
              "Then yes.")

 (sp/beat "He puts the tube down against a bin and leaves it there.")

 (sp/dialogue "ELI"
              ["I'd like to see a thing first and judge it"
               "afterwards."])

 (sp/dialogue :pc
              "That's a thing people say.")

 (sp/dialogue "ELI"
              ["It is. I've been waiting two terms to say it to"
               "somebody."])

 (sp/transition "Cut to:")

 (sp/slug "EXT. MERIDIAN HALL — 11:44 P.M.")

 (sp/action "Four of them at a door with one torch and a ring of keys that is"
            "not his.")

 (sp/action "{pc} opens the nomination field, which he has not looked at since"
            "the gymnasium in August.")

 (sp/beat "It takes one name.")

 (sp/action "He looks at it for a moment and closes it, and unlocks the door,"
            "and holds it for the other three.")

 (sp/transition "Cut to:"))