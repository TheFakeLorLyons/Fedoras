(ns fedoras.cs-class.puzzle.puzzle-ui
  "Fifteen numbers and a hole.

  THE SOLVER IS NINE SEARCHES AND NOT ONE. Searching a whole board is
  ten trillion arrangements and no interpreter is going to do it. But
  putting one tile on one square does not depend on where any other
  tile is, so the state for that question is the tile, the hole, and
  nothing else — fifteen times fourteen. Two tiles is about two
  thousand. Nine of those in a row and the board is in order.

  It is not the shortest way. It is between forty and a hundred and
  thirty moves where the best is under eighty, and the worst stage
  looks at about two thousand arrangements, which is the difference
  between a program that answers and a program that hangs.

  THE OLD ONE DID NOT SOLVE ANYTHING. It kept a record of the shuffle it
  had just done and played it backwards, so it could only ever undo its
  own work and had nothing to say about a board it had not made.

  AND IT KNOWS WHEN IT CANNOT. Half of all arrangements of fifteen
  numbers cannot be got into order by sliding, and nothing about looking
  at one tells you which half it is in. There is a parity that does, and
  it is four lines, and the board says so rather than searching for
  something that is not there."
  (:require [clojure.string :as str]))

(def settings
  (atom {:board nil :scramble 200 :speed 90
         :bands [[65 "no action"] [80 "review"]] :beyond "repossessed"}))

(def solved (conj (vec (range 1 16)) nil))

(defonce board  (atom solved))
(defonce moves  (atom 0))
(defonce live   (atom false))
(defonce note   (atom "shuffle it, or put a board in the settings"))
(defonce playing (atom false))

;; ---------------------------------------------------------------------
;; The board
;; ---------------------------------------------------------------------

(defn hole [b] (first (keep-indexed (fn [i v] (when (nil? v) i)) b)))

(defn where [b tile] (first (keep-indexed (fn [i v] (when (= v tile) i)) b)))

(defn neighbours
  "The four squares next to i, minus the ones that fall off the edge."
  [i]
  (let [r (quot i 4) c (rem i 4)]
    (cond-> []
      (> r 0) (conj (- i 4))
      (< r 3) (conj (+ i 4))
      (> c 0) (conj (dec i))
      (< c 3) (conj (inc i)))))

(defn legal? [b i] (boolean (some #{(hole b)} (neighbours i))))

(defn move
  "Slide the tile at i into the hole. If it is not next to the hole,
  give back the board you had."
  [b i]
  (if (legal? b i) (assoc b (hole b) (nth b i) i nil) b))

(defn done? [b] (= b solved))

(defn solvable?
  "Half of all arrangements cannot be got into order and nothing about
  looking at one says which half. Count the pairs that are the wrong way
  round, add the row the hole is in counting up from the bottom, and if
  that is odd it can be done."
  [b]
  (let [xs (vec (remove nil? b))
        n  (count xs)
        inv (count (for [i (range n) j (range (inc i) n)
                         :when (> (nth xs i) (nth xs j))] 1))]
    (odd? (+ inv (- 4 (quot (hole b) 4))))))

;; ---------------------------------------------------------------------
;; The solver
;; ---------------------------------------------------------------------

(def plan
  "Nine questions, each of them small. Every square that has been
  answered is frozen and the next search goes round it."
  [[[1 0]] [[2 1]] [[3 2] [4 3]]
   [[5 4]] [[6 5]] [[7 6] [8 7]]
   [[9 8] [13 12]]
   [[10 9] [14 13]]
   [[11 10] [12 11] [15 14]]])

(defn- place
  "The moves that put these tiles on these squares.

  Every other tile is irrelevant to the question, so a state is where
  these tiles are and where the hole is, and nothing else. Breadth
  first, a layer at a time, so it is the fewest moves for this stage
  even though the nine together are not the fewest overall."
  [b pairs frozen]
  (let [tiles   (mapv first pairs)
        targets (mapv second pairs)
        start   [(mapv (fn [t] (where b t)) tiles) (hole b)]]
    (if (= (first start) targets)
      []
      (loop [layer [[start []]] seen #{start} depth 0]
        (cond
          (empty? layer) nil
          (> depth 90)   nil
          :else
          (let [{:keys [seen out hit]}
                (reduce
                 (fn [acc [[tp h] path]]
                   (reduce
                    (fn [acc nc]
                      (if (or (:hit acc) (contains? frozen nc))
                        acc
                        (let [ntp (mapv (fn [p] (if (= p nc) h p)) tp)
                              st  [ntp nc]
                              pth (conj path nc)]
                          (cond
                            (= ntp targets)         (assoc acc :hit pth)
                            (contains? (:seen acc) st) acc
                            :else (-> acc
                                      (update :seen conj st)
                                      (update :out conj [st pth]))))))
                    acc (neighbours h)))
                 {:seen seen :out [] :hit nil} layer)]
            (or hit (recur out seen (inc depth)))))))))

(defn solve
  "Any board, or nil if it is one of the half that cannot be done."
  [start]
  (when (solvable? start)
    (loop [b start out [] frozen #{} stages plan]
      (if-let [pairs (first stages)]
        (when-let [ms (place b pairs frozen)]
          (recur (reduce move b ms)
                 (into out ms)
                 (into frozen (map second pairs))
                 (rest stages)))
        (when (done? b) out)))))

;; ---------------------------------------------------------------------
;; Shuffling
;; ---------------------------------------------------------------------

(defn shuffled
  "Walked away from order, so it can always be got back."
  [n]
  (reduce (fn [b _]
            (let [opts (neighbours (hole b))]
              (move b (nth opts (int (* (js/Math.random) (count opts)))))))
          solved (range n)))

(defn- valid-board?
  [b]
  (and (vector? b) (= 16 (count b))
       (= (set b) (set solved))))

;; ---------------------------------------------------------------------
;; The verdict
;; ---------------------------------------------------------------------

(defn verdict
  "The Registrar's language, applied to a sliding puzzle by a man who
  has been marked in it all term."
  [n]
  (or (some (fn [[under word]] (when (< n under) word)) (:bands @settings))
      (:beyond @settings)))

(defn- best [] (fedoras.ui.widget-ui/kept :puzzle-best))

(defn- best! [n]
  (let [b (best)]
    (when (or (nil? b) (< n b))
      (fedoras.ui.widget-ui/keep! :puzzle-best n))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn- grid-html [b]
  (str/join ""
            (map-indexed
             (fn [i v]
               (if (nil? v)
                 "<div class='puzzle-cell puzzle-hole'></div>"
                 (str "<button class='puzzle-cell"
                      (when (= v (inc i)) " puzzle-home")
                      "' data-cell='" i "'>" v "</button>")))
             b)))

(defn paint! [node]
  (let [b @board bst (best)]
    (fedoras.ui.widget-ui/html! node ".puzzle-grid" (grid-html b))
    (fedoras.ui.widget-ui/state!
     node
     (str "moves " @moves
          (when bst (str " · best " bst " (" (verdict bst) ")"))))
    (fedoras.reader/text! node ".puzzle-note" @note)))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- try-move! [node i]
  (cond
    @playing nil
    (not @live) (do (reset! note "shuffle it first") (paint! node))
    (legal? @board i)
    (do (swap! board move i)
        (swap! moves inc)
        (if (done? @board)
          (do (reset! live false)
              (best! @moves)
              (reset! note (str @moves " moves — " (verdict @moves))))
          (reset! note ""))
        (paint! node))
    :else
    ;; the marked part. it says what it would not do and why.
    (do (reset! note (str "tile " (nth @board i) " is not next to the hole"))
        (paint! node))))

(defn- play! [node ms]
  (reset! playing true)
  (let [total (count ms)]
    (letfn [(step [left n]
              (if-let [cell (first left)]
                (do (swap! board move cell)
                    (reset! note (str "solving — " n " of " total))
                    (paint! node)
                    (js/setTimeout (fn [] (step (rest left) (inc n)))
                                   (:speed @settings)))
                (do (reset! playing false)
                    (reset! live false)
                    (reset! note (str "in order — " total " moves, not the fewest"))
                    (paint! node))))]
      (step ms 1))))

(defn- start! [node b why]
  (reset! board b)
  (reset! moves 0)
  (reset! live (not (done? b)))
  (reset! note why)
  (paint! node))

(defn- wire-grid! [node]
  (when-let [g (fedoras.ui.widget-ui/q! node ".puzzle-grid")]
    (set! (.-onclick g)
          (fn [e]
            (when-let [c (.closest (.-target e) ".puzzle-cell")]
              (when-let [d (.getAttribute c "data-cell")]
                (try-move! node (js/parseInt d 10))))))))

(defn- wire-controls! [node]
  (fedoras.reader/on-click! node ".puzzle-shuffle"
                            (fn [_] (when-not @playing
                                      (start! node (shuffled (:scramble @settings)) ""))))

  (fedoras.reader/on-click! node ".puzzle-reset"
                            (fn [_] (when-not @playing
                                      (start! node solved "in order"))))

  (fedoras.reader/on-click! node ".puzzle-solve"
                            (fn [_]
                              (when-not @playing
                                (cond
                                  (done? @board)
                                  (do (reset! note "it is already in order") (paint! node))

                                  (not (solvable? @board))
                                  (do (reset! note "this board cannot be got into order by sliding")
                                      (paint! node))

                                  :else
                                  (if-let [ms (solve @board)]
                                    (play! node ms)
                                    (do (reset! note "it did not find a way, which it should have")
                                        (paint! node))))))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current
                     (select-keys setting [:board :scramble :speed :bands :beyond]))))
     (let [b (:board @settings)]
       (cond
         (nil? b) (start! node (shuffled (:scramble @settings)) "")
         (not (valid-board? b))
         (start! node solved "that is not fifteen numbers and a hole")
         (not (solvable? b))
         (start! node (vec b) "this board cannot be got into order by sliding")
         :else (start! node (vec b) ""))))))

(defn setup! [node]
  (let [id (.-id node)]
    (fedoras.ui.widget-ui/wire! node "grid"     wire-grid!)
    (fedoras.ui.widget-ui/wire! node "controls" wire-controls!)
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount! ::puzzle (or id "puzzle") setup! paint!))