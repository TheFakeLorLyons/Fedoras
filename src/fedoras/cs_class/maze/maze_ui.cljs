(ns fedoras.cs-class.maze.maze-ui
  (:require [clojure.string :as str]
            [fedoras.reader :as reader]
            [fedoras.ui.widget-ui :as wui]))

;; ---------------------------------------------------------------------
;; A predictable die
;; ---------------------------------------------------------------------

(def die-faces
  "How many positions the die has: two to the thirty-first. A position
  divided by it is a fraction from zero up to one."
  2147483648)

(def die-mask
  "The largest position, every one of the die's bits set. Masking with
  it keeps a number to the die's size."
  (dec die-faces))

(defn- turn-die
  "The die's next position: Marsaglia's xorshift, with shifts of 13, 17
  and 5, kept to the die's size. Never zero, if it did not start at
  zero."
  [position]
  (let [position (bit-and (bit-xor position (bit-shift-left position 13)) die-mask)
        position (bit-and (bit-xor position (unsigned-bit-shift-right position 17)) die-mask)]
    (bit-and (bit-xor position (bit-shift-left position 5)) die-mask)))

(defonce die (atom 1))

(defn- seed-die!
  "Seeds the die before a floor is laid. Laying out a floor throws the
  die many times, so one seed is always one building."
  [seed]
  (reset! die (bit-or 1 (bit-and seed die-mask))))

(defn- chance!
  "A number from zero up to one, not including one."
  []
  (/ (swap! die turn-die) die-faces))

(defn- roll!
  "A whole number from zero up to `n`, not including `n`."
  [n]
  (min (dec n) (int (* (chance!) n))))

(defn- pick!
  [coll]
  (when (seq coll)
    (let [items (vec coll)]
      (nth items (roll! (count items))))))

(defn- draw
  "One of `coll`, chosen with the die the floor carries, and the floor
  with its die turned. For the choices made during play, which have to
  come out the same on a restarted floor."
  [state coll]
  (let [position (turn-die (:die state))
        items (vec coll)]
    [(nth items (mod position (count items))) (assoc state :die position)]))

(defn- draw-chance
  "Whether something with probability `p` happens, by the floor's die,
  and the floor with its die turned."
  [state p]
  (let [position (turn-die (:die state))]
    [(< (/ position die-faces) p) (assoc state :die position)]))

;; ---------------------------------------------------------------------
;; The settings
;; ---------------------------------------------------------------------

(def storage-key :maze)

(def default-book
  "The settings file as it ships, so that nothing downstream reads a nil
  before the reader has pressed Run."
  {:rows 10
   :cols 10
   :density 0.78
   :seed nil
   :difficulty :clerk
   :difficulties
   {:visitor   {:rules 1 :locks 0 :entities 1 :pace 3 :release 5 :writes 14 :hp 4 :steps 120 :drops 2 :entity-powerups false}
    :clerk     {:rules 1 :locks 1 :entities 2 :pace 2 :release 4 :writes 10 :hp 3 :steps 90  :drops 1 :entity-powerups false}
    :auditor   {:rules 3 :locks 2 :entities 3 :pace 2 :release 3 :writes 8  :hp 4 :steps 100 :drops 1 :entity-powerups true}
    :inspector {:rules 4 :locks 2 :entities 4 :pace 2 :release 2 :writes 6  :hp 2 :steps 90  :drops 1 :entity-powerups true}}
   :descent {:rules [1 4] :locks [3 3] :entities [2 4] :release [3 2] :writes [1 6]}
   :writes-from 2
   :powerups {:chance 0.08 :lasts 12 :vanquish-range 5 :teleport-range 4
              :stun-radius 2 :stun-turns 4}
   :score {:floor 1000 :step 10 :hit 50 :vanquish 150 :stun 40 :quick 500 :slow 2}
   :clutter 1.0
   :furniture {:box 2 :cabinet 1 :copier 1 :disturbance 1 :pole 1}
   :traps 2
   :shields 1
   :portals true
   :one-way true
   :terrain {:first-floor-bare 0.6 :new-kind-every 3 :one-fewer 0.35 :share 0.28 :burns-per-hit 3}
   :specials {:every 3 :kinds [:fast :strong :thrower] :throw-range 2}
   :shield-turns 3
   :trap-holds 2
   :trap-holds-you 1
   :warrant-rule {:keep :present :a :warrant}
   :house-rules
   [{:keep :clear    :a :way-out}
    {:keep :apart    :a :box       :b :door}
    {:keep :together :a :bear-trap :b :cabinet}
    {:keep :apart    :a :shield    :b :disturbance}]
   :amendments
   [{:keep :apart    :a :box     :b :way-out}
    {:keep :together :a :cabinet :b :copier}
    {:keep :clear    :a :warrant}
    {:keep :apart    :a :shield  :b :way-out}
    {:keep :together :a :box     :b :disturbance}]})

(defonce book (atom nil))

(defn- merge-settings
  "The defaults with whatever the reader wrote over them, one level
  deep, so a count left out of `:furniture` or a line left out of
  `:descent` keeps its default instead of taking the whole map with it."
  [defaults given]
  (merge-with (fn [default mine]
                (if (and (map? default) (map? mine))
                  (merge default mine)
                  mine))
              defaults
              (if (map? given) given {})))

(defn- difficulty-row
  "The row the settings chose. A name picks a row from `:difficulties`,
  and a map written in place of the name is used as it stands."
  [settings]
  (let [chosen (:difficulty settings)
        rows (:difficulties settings)]
    (cond
      (map? chosen) chosen
      (contains? rows chosen) (get rows chosen)
      :else (or (get rows :clerk) (first (vals rows)) {}))))

(defn- difficulty-name
  [settings]
  (let [chosen (:difficulty settings)]
    (if (keyword? chosen) chosen :custom)))

(defn- descend
  "A number made worse by the way down. `spec` is [every limit]: every
  that many floors it gets one worse, and it never gets worse than the
  limit. Worse is more for some things and fewer for others, which is
  `direction`. A start already past the limit stays where it started."
  [start spec floors-down direction]
  (let [[every limit] (if (sequential? spec) spec [spec nil])
        steps (if (and (number? every) (pos? every)) (quot floors-down every) 0)]
    (case direction
      :more (let [worse (+ start steps)]
              (if (and limit (> worse limit)) (max start limit) worse))
      :fewer (let [worse (- start steps)]
               (if (and limit (< worse limit)) (min start limit) (max 1 worse))))))

(defn- special-for
  "The special entity a floor gets, if any: one on every floor that
  `:every` divides, the kinds taking turns."
  [settings floor]
  (let [{:keys [every kinds]} (:specials settings)]
    (when (and (number? every) (pos? every) (seq kinds) (zero? (mod floor every)))
      (nth (vec kinds) (mod (dec (quot floor every)) (count kinds))))))

(defn terms-for
  "What one floor runs on: the chosen row, made worse by the way down."
  [settings floor]
  (let [row (difficulty-row settings)
        {:keys [rules locks entities release writes]} (:descent settings)
        writes-from (or (:writes-from settings) 2)]
    {:rules (descend (or (:rules row) 1) rules (dec floor) :more)
     :locks (min 3 (descend (or (:locks row) 0) locks (dec floor) :more))
     :entities (descend (or (:entities row) 2) entities (dec floor) :more)
     :pace (max 1 (or (:pace row) 2))
     :release (when-let [start (:release row)]
                (descend start release (dec floor) :fewer))
     :writes (when (and (:writes row) (>= floor writes-from))
               (descend (:writes row) writes (- floor writes-from) :fewer))
     :hp (max 1 (or (:hp row) 3))
     :steps (max 1 (or (:steps row) 90))
     :drops (max 0 (or (:drops row) 1))
     :entity-powerups (boolean (:entity-powerups row))
     :shield-turns (or (:shield-turns settings) 3)
     :trap-holds (or (:trap-holds settings) 2)
     :trap-holds-you (or (:trap-holds-you settings) 1)
     :powerups (merge (:powerups default-book) (:powerups settings))
     :scoring (merge (:score default-book) (:score settings))
     :terrain (merge (:terrain default-book) (:terrain settings))
     :specials (merge (:specials default-book) (:specials settings))
     :special (special-for settings floor)
     :floor floor}))

(defn- proportions
  "Rows, columns and density, held to what can hold a building."
  [settings]
  {:rows (max 5 (min 20 (or (:rows settings) 10)))
   :cols (max 5 (min 20 (or (:cols settings) 10)))
   :density (max 0.3 (min 1.0 (or (:density settings) 0.78)))})

(defn- seed-for
  "The seed for a floor. With none in the settings, one nobody has seen,
  from the browser's own die. With one, the same sequence every time,
  beginning with the seed itself, so a seed copied from under a floor
  and written into the settings gives that building back."
  [settings run floor]
  (let [fixed-seed (:seed settings)
        floor-stride 7919   ; a prime, so the floors of a run never fall into step
        run-stride 104729]  ; a larger prime, so one run's floors never repeat another's
    (if (number? fixed-seed)
      (if (and (zero? run) (= 1 floor))
        (int fixed-seed)
        (bit-and (+ (int fixed-seed) (* floor-stride floor) (* run-stride run)) die-mask))
      (inc (rand-int (dec die-mask))))))

;; ---------------------------------------------------------------------
;; Words
;; ---------------------------------------------------------------------

(defn- word
  [value]
  (if (keyword? value) (name value) (str value)))

(defn- capitalized
  [text]
  (if (seq text)
    (str (str/upper-case (subs text 0 1)) (subs text 1))
    text))

(defn- turns-word
  [n]
  (if (= 1 n) "1 turn" (str n " turns")))

(def label
  {:door "door" :doorway "open door" :key "key" :box "box"
   :bear-trap "trap" :shield "shield" :disturbance "disturbance"
   :cabinet "cabinet" :copier "copier" :pole "pole"
   :portal-a "portal" :portal-b "portal"
   :portal-in "one-way portal" :portal-out "one-way exit"
   :warrant "warrant" :way-out "way out"
   :vanquish "vanquish" :teleport "teleport" :stun "stun"
   :sand "sand" :ice "ice" :fire "fire"
   :sand-boots "sand boots" :ice-boots "ice boots" :fire-boots "fire boots"})

(def entity-kind-names
  {:fast "fast" :strong "strong" :thrower "throwing"})

(defn- label-of
  [kind]
  (get label kind (word kind)))

(def power-kinds
  [:vanquish :teleport :stun])

(def power-keys
  {:vanquish "1" :teleport "2" :stun "3"})

;; The verb under :keep is always bound to `rule`. Bound to `keep`, it
;; would shadow clojure.core/keep inside the very functions that want it.
(defn sentence
  "A Requirement as it is written on the wall."
  [{rule :keep a :a b :b}]
  (case rule
    :apart (str (word a) " shall not adjoin " (word b))
    :together (str (word a) " shall adjoin " (word b))
    :clear (str "nothing shall adjoin " (word a))
    :present (str (word a) " shall be held on this floor")
    (str/join " " (map word (remove nil? [a rule b])))))

;; ---------------------------------------------------------------------
;; The record
;; ---------------------------------------------------------------------

(def record-length 7)

(defn- note
  "A remark about something that took no time, such as walking into a
  wall. It lasts until the next thing happens."
  [state line]
  (assoc state :note line))

(defn- record
  "Something that happened. It goes into this turn's events, which are
  drawn once, and into the record, which keeps the last few. `event`
  carries `:by`, which is :you, :floor or :entities, a `:kind` the
  drawing reads, and the `:line` the record shows."
  [state event]
  (let [event (assoc event :turn (:turn state))]
    (-> state
        (update :events (fnil conj []) event)
        (update :record (fn [lines]
                          (vec (take-last record-length
                                          (conj (or lines []) event))))))))

(defn- points
  "What the score gives or takes for one of `kind`."
  [state kind]
  (get-in state [:terms :scoring kind] 0))

(defn- hurt
  "One hit off the player, and off the floor's score."
  [state]
  (-> state
      (update :hp dec)
      (update :hits (fnil inc 0))))

;; ---------------------------------------------------------------------
;; The plan
;; ---------------------------------------------------------------------

(defn- magnitude
  [n]
  (max n (- n)))

(defn- steps-between
  [[row-a col-a] [row-b col-b]]
  (+ (magnitude (- row-a row-b)) (magnitude (- col-a col-b))))

(defn rooms
  [state]
  (set (keys (:plan state))))

(defn room?
  [state room]
  (contains? (:plan state) room))

(defn pit?
  [state room]
  (contains? (:pits state) room))

(defn fixture
  [state room]
  (let [held (get (:plan state) room)]
    (when-not (or (nil? held) (= held :empty)) held)))

(defn terrain
  "What the ground is like in a room: :sand, :ice, :fire, or nil for
  ordinary floor."
  [state room]
  (get-in state [:terrain room]))

(defn adjoining
  [state [row col]]
  (filter (fn [room] (room? state room))
          [[(dec row) col] [row (inc col)] [(inc row) col] [row (dec col)]]))

(defn holding
  [state kind]
  (keep (fn [[room held]] (when (= held kind) room)) (:plan state)))

(defn touching?
  [state a b]
  (boolean (some (fn [room]
                   (some (fn [next-door] (= b (fixture state next-door)))
                         (adjoining state room)))
                 (holding state a))))

(defn shut?
  [state room]
  (= :door (fixture state room)))

(defn- open-to-entities?
  "Whether an entity can step into a room. A locked door stops it, and so
  does a box, which entities cannot push, and which they go round when
  there is a way round."
  [state room]
  (not (or (shut? state room) (= :box (fixture state room)))))

(defn- passable
  "The rooms next to `room` an entity can step into."
  [state room]
  (filter (fn [next-door] (open-to-entities? state next-door)) (adjoining state room)))

(defn- occupants
  [state]
  (set (map :at (:entities state))))

(defn- drop-rooms
  [state]
  (set (map :at (:drops state))))

(defn- distances-from
  "Every room reachable from `from` and how far, where `neighbours` says
  what counts as a way through."
  [state from neighbours]
  (loop [seen {from 0} frontier [from] distance 0]
    (if (empty? frontier)
      seen
      (let [distance (inc distance)
            reached (->> frontier
                         (mapcat (fn [room] (neighbours state room)))
                         (remove seen)
                         distinct
                         vec)]
        (recur (into seen (map (fn [room] [room distance]) reached))
               reached
               distance)))))

(defn- component
  "Every room that can be reached from `from`, doors or no doors."
  [state from]
  (loop [seen #{from} frontier [from]]
    (if (empty? frontier)
      seen
      (let [reached (->> frontier
                         (mapcat (fn [room] (adjoining state room)))
                         (remove seen)
                         distinct
                         vec)]
        (recur (into seen reached) reached)))))

(defn- pieces
  "The separate parts of a building, largest first."
  [state]
  (loop [left (rooms state) found []]
    (if-let [start (first left)]
      (let [piece (component state start)]
        (recur (reduce disj left piece) (conj found piece)))
      (vec (sort-by (comp - count) found)))))

(def directions
  {:north [-1 0] :south [1 0] :west [0 -1] :east [0 1]})

(defn- beside
  [[row col] direction]
  (let [[row-step col-step] (get directions direction)]
    [(+ row row-step) (+ col col-step)]))

(defn- direction-of
  [from to]
  (some (fn [direction] (when (= to (beside from direction)) direction))
        (keys directions)))

;; ---------------------------------------------------------------------
;; Portals
;; ---------------------------------------------------------------------

(def portal-pairs
  "Where each portal puts whoever steps in it. The room the one-way
  pair goes to is an ordinary room to whoever is standing in it."
  {:portal-a :portal-b :portal-b :portal-a :portal-in :portal-out})

(def teleporters
  (set (keys portal-pairs)))

(defn- portal-map
  "Every portal room on the floor, and the room it sends whoever steps
  in to."
  [state]
  (into {} (for [[kind pair] portal-pairs
                 from (holding state kind)
                 :let [to (first (holding state pair))]
                 :when to]
             [from to])))

(defn- through
  "Where a portal puts whoever steps into it, or nil. A portal is a
  door into one room and out of another: nobody walks through the room
  it stands in. Entities use portals too, which is what makes one a
  risk as well as a shortcut."
  [state room]
  (get (portal-map state) room))

(defn- reachable-from
  "Every room somebody could come to stand in, starting from `from`,
  stepping into what `open?` allows and arriving wherever the step puts
  them."
  ([state from sends]
   (reachable-from state from sends (fn [_ _] true)))
  ([state from sends open?]
   (loop [seen #{from} frontier [from]]
     (if (empty? frontier)
       seen
       (let [reached (->> frontier
                          (mapcat (fn [room]
                                    (keep (fn [next-door]
                                            (when (open? state next-door)
                                              (get sends next-door next-door)))
                                          (adjoining state room))))
                          (remove seen)
                          distinct
                          vec)]
         (recur (into seen reached) reached))))))

(defn- distances-to
  "How many steps each room is from `goal`, for somebody who may step
  into the rooms `open?` allows, where a step into a portal is a step to
  wherever it sends them. Counted backwards from the goal, which is the
  only honest way to count a portal. With `step-in?`, stepping into the
  goal reaches it even when the goal is a portal, which is what a reader
  clicking on one means."
  [state goal open? step-in?]
  (let [sends (portal-map state)
        senders (reduce (fn [senders room]
                          (reduce (fn [senders next-door]
                                    (if (open? state next-door)
                                      (update senders
                                              (if (and step-in? (= next-door goal))
                                                goal
                                                (get sends next-door next-door))
                                              (fnil conj [])
                                              room)
                                      senders))
                                  senders
                                  (adjoining state room)))
                        {}
                        (sort (rooms state)))]
    (loop [seen {goal 0} frontier [goal] distance 0]
      (if (empty? frontier)
        seen
        (let [distance (inc distance)
              reached (->> frontier
                           (mapcat (fn [room] (get senders room)))
                           (remove seen)
                           distinct
                           vec)]
          (recur (into seen (map (fn [room] [room distance]) reached))
                 reached
                 distance))))))

;; ---------------------------------------------------------------------
;; The audit
;; ---------------------------------------------------------------------

(defn met?
  [state {rule :keep a :a b :b}]
  (case rule
    :apart (not (touching? state a b))
    :together (touching? state a b)
    :clear (not-any? (fn [room]
                       (some (fn [next-door] (fixture state next-door))
                             (adjoining state room)))
                     (holding state a))
    :present (boolean (seq (holding state a)))
    true))

(defn unmet
  [state]
  (filterv (fn [requirement] (not (met? state requirement))) (:reqs state)))

(defn repairable?
  "Three of the four verbs can be met by moving something. One cannot,
  and what the floor does about that one is the whole endgame."
  [{rule :keep}]
  (contains? #{:apart :together :clear} rule))

(def ^:private fixed
  "The floor will not shift these to satisfy anything. A door that
  wandered would not be a door."
  #{:door :doorway :way-out :warrant :key
    :portal-a :portal-b :portal-in :portal-out
    :sand-boots :ice-boots :fire-boots})

(defn- free?
  "A room with nothing and nobody in it."
  [state room]
  (and (room? state room)
       (nil? (fixture state room))
       (not= room (:at state))
       (not (contains? (occupants state) room))
       (not (contains? (drop-rooms state) room))))

(defn- free-rooms
  [state]
  (filter (fn [room] (free? state room)) (sort (rooms state))))

(def lasting
  "What stays where it is put: furniture and portals. Keys, shields,
  traps, boots and the warrant are used up."
  #{:box :cabinet :copier :pole :disturbance :portal-a :portal-b :portal-in :portal-out})

(def portal-kinds
  #{:portal-a :portal-b :portal-in :portal-out})

(defn- approach?
  "Whether a room is right beside a door or the way out. Nothing that
  stays is put in one, so the way to every door and to the way out is
  always clear."
  [state room]
  (boolean (some (fn [next-door] (contains? #{:door :doorway :way-out} (fixture state next-door)))
                 (adjoining state room))))

(defn- move-fixture
  "Move one `kind` to the nearest free room where fewer Requirements are
  broken, or nil if there is no such room. Each one of that kind is
  tried in turn, because the one in the way is not always the first."
  [state kind because]
  (when-not (contains? fixed kind)
    (let [broken (count (unmet state))
          free (vec (cond->> (free-rooms state)
                      (contains? lasting kind) (remove (fn [room] (approach? state room)))))]
      (first
       (for [from (holding state kind)
             :let [to (->> free
                           (sort-by (fn [room] (steps-between from room)))
                           (filter (fn [room]
                                     (< (count (unmet (-> state
                                                          (assoc-in [:plan from] :empty)
                                                          (assoc-in [:plan room] kind))))
                                        broken)))
                           first)]
             :when to]
         (-> state
             (assoc-in [:plan from] :empty)
             (assoc-in [:plan to] kind)
             (record {:by :floor :kind :moved :fixture kind :from from :to to
                      :line (str "The " (label-of kind) " moved, because "
                                 (sentence because) ".")})))))))

(defn- repair
  "One attempt at one broken Requirement, as a floor, or nil."
  [state {rule :keep a :a b :b :as requirement}]
  (case rule
    (:apart :together) (or (move-fixture state b requirement)
                           (move-fixture state a requirement))
    :clear (first (for [room (holding state a)
                        next-door (adjoining state room)
                        :let [kind (fixture state next-door)]
                        :when kind
                        :let [moved (move-fixture state kind requirement)]
                        :when moved]
                    moved))
    nil))

(defn audit
  "Repairs what can be repaired, one move at a time, and stops when no
  move would help. What cannot be repaired is left alone; the tide deals
  with that."
  [state]
  (loop [state state moves 0]
    (let [repaired (when (< moves 8)
                     (some (fn [requirement] (repair state requirement))
                           (filter repairable? (unmet state))))]
      (if repaired
        (recur repaired (inc moves))
        state))))

;; ---------------------------------------------------------------------
;; The tide
;; ---------------------------------------------------------------------

(defn- strands?
  "Whether losing `room` would cut the player off: from the way out,
  from a key still lying somewhere, or, by way of a portal, onto an
  island with no way back. A portal that opened onto an island would
  strand the player as surely as the tide could."
  [state room]
  (let [without (update state :plan dissoc room)
        sends (portal-map without)
        from-player (reachable-from without (:at without) sends)
        way-out (first (holding without :way-out))]
    (boolean
     (or (not (contains? from-player way-out))
         (not-every? (fn [key-room] (contains? from-player key-room))
                     (holding without :key))
         (some (fn [landing-room]
                 (and (contains? from-player landing-room)
                      (not (contains? (reachable-from without landing-room sends) way-out))))
               (distinct (vals sends)))))))

(defn next-to-go
  "The room the tide takes next: the one furthest from the way out, the
  edges first among equals, never the player's, never an entity's,
  never one with anything in it, and never one the player needs to get
  out. Nil when there is nothing left it may take except the room the
  player is standing in."
  [state]
  (let [out (first (holding state :way-out))
        from-out (if out (distances-from state out adjoining) {})]
    (->> (free-rooms state)
         (sort-by (fn [room] [(- (get from-out room 999))
                              (count (adjoining state room))]))
         (remove (fn [room] (strands? state room)))
         first)))

(declare pick-up)

(defn- release-a-room
  [state]
  (if-let [room (next-to-go state)]
    (-> state
        (update :plan dissoc room)
        (update :terrain dissoc room)
        (update :pits conj room)
        (record {:by :floor :kind :released :room room
                 :line "A room was released."}))
    (let [here (:at state)
          without (-> state (update :plan dissoc here) (update :terrain dissoc here))
          out (first (holding without :way-out))
          from-out (if out (distances-from without out adjoining) {})
          landing (->> (adjoining state here)
                       (filter (fn [room] (contains? from-out room)))
                       (sort-by (fn [room] [(get from-out room)
                                            (if (shut? state room) 1 0)]))
                       first)]
      (if landing
        (-> without
            (update :pits conj here)
            (assoc :at landing)
            (cond-> (shut? state landing) (assoc-in [:plan landing] :doorway))
            hurt
            (record {:by :floor :kind :fell :room here
                     :line (str "Your room was released under you. -" (points state :hit))})
            pick-up)
        (-> state
            (assoc :over :closed)
            (record {:by :floor :kind :closed :line "Nowhere left to stand."}))))))

(defn- tide-due?
  "Whether a room goes at the end of this turn."
  [state]
  (let [every (get-in state [:terms :release])
        since (- (:turn state) (or (:held-at state) 0))]
    (boolean (and (:held state) every (pos? every) (pos? since)
                  (zero? (mod since every))))))

(defn- tide
  [state]
  (if (tide-due? state) (release-a-room state) state))

;; ---------------------------------------------------------------------
;; Entities
;; ---------------------------------------------------------------------

(defn- entities-move?
  [state]
  (zero? (mod (:turn state) (get-in state [:terms :pace] 2))))

(defn- toward
  "Where one step toward the player puts an entity, a step into a
  portal being a step to wherever it sends it, and keeping out of
  rooms another entity already stands in. Where it already is, when
  there is no way."
  [state distances sends from taken]
  (let [here (get distances from)]
    (or (when here
          (->> (passable state from)
               (map (fn [next-door] (get sends next-door next-door)))
               (filter (fn [landing-room]
                         (let [there (get distances landing-room)]
                           (and there (< there here)))))
               (remove taken)
               (sort-by distances)
               first))
        from)))

(defn- entity-powers?
  "Whether this floor lets entities take powerups."
  [state]
  (boolean (get-in state [:terms :entity-powerups])))

(defn- entity-stun-turns
  "How long an entity's stun holds the player: half as long as the
  player's stun holds an entity, rounded up."
  [terms]
  (max 1 (quot (inc (get-in terms [:powerups :stun-turns] 4)) 2)))

(defn- toughness
  "How many blows an entity takes to end. A strong one takes two."
  [entity]
  (if (= :strong (:kind entity)) 2 1))

(defn- cool-for-entities?
  "Whether an entity steps into a room when it has any choice: anywhere
  it can, except fire, unless the player is standing in it."
  [state room]
  (and (open-to-entities? state room)
       (or (= room (:at state)) (not= :fire (terrain state room)))))

(defn- chasing
  "The distances an entity walks by this turn. Toward a powerup when
  the floor lets entities take them, when it holds none, and when the
  powerup is nearer to it than the player is and will still be there
  when it arrives; toward the player otherwise."
  [state index to-player to-drops]
  (let [{:keys [at power]} (get-in state [:entities index])
        pace (get-in state [:terms :pace] 2)
        player-distance (get to-player at)]
    (or (when (and (entity-powers? state) (nil? power))
          (->> (:drops state)
               (keep (fn [{:keys [left] :as lying}]
                       (let [distances (get to-drops (:at lying))
                             steps (get distances at)]
                         (when (and steps
                                    (or (nil? player-distance) (< steps player-distance))
                                    (<= (* steps pace) left))
                           [steps distances]))))
               (sort-by first)
               first
               second))
        to-player)))

(defn- entity-picks-up
  "An entity standing on a powerup, holding none, takes it, when the
  floor lets entities take them."
  [state index]
  (let [{:keys [at power]} (get-in state [:entities index])
        lying (first (filter (fn [lying] (= at (:at lying))) (:drops state)))]
    (if (and lying (nil? power) (entity-powers? state))
      (-> state
          (assoc-in [:entities index :power] (:kind lying))
          (update :drops (fn [drops] (vec (remove (fn [other] (= at (:at other))) drops))))
          (record {:by :entities :kind :gone :room at :fixture (:kind lying)
                   :line (str "An entity picked up the " (label-of (:kind lying)) ".")}))
      state)))

(defn- hit-from-afar
  "Something an entity in `source` sends the player's way, arriving: a
  vanquish, or a throw. A shield takes it the way it takes any hit."
  [state what source]
  (if (:shield state)
    (-> state
        (assoc :shield nil)
        (record {:by :entities :kind :sheltered :room (:at state) :source source
                 :line (str "An entity " what ". The shield took it.")}))
    (-> state
        hurt
        (record {:by :entities :kind :hurt :room (:at state) :source source
                 :line (str "An entity " what ". -" (points state :hit))}))))

(defn- entity-teleport-room
  "Where an entity's teleport would put it: the free room within reach,
  on its side of every lock, that leaves it nearest the player. Nil
  unless that is nearer than a step would take it."
  [state index to-player]
  (let [{:keys [at]} (get-in state [:entities index])
        reach (get-in state [:terms :powerups :teleport-range] 4)
        now (get to-player at)]
    (when now
      (->> (reachable-from state at (portal-map state) open-to-entities?)
           (filter (fn [room] (and (<= 1 (steps-between at room) reach)
                                   (free? state room)
                                   (some? (get to-player room))
                                   (< (get to-player room) (dec now)))))
           (sort-by (fn [room] [(get to-player room) room]))
           first))))

(defn- entity-uses-power
  "What an entity holding a powerup does with it this turn: a vanquish
  when the player is within its reach, a stun when the player is
  close, a teleport when the player is far. Nil when it would rather
  walk."
  [state index to-player]
  (let [{:keys [at power]} (get-in state [:entities index])
        {:keys [vanquish-range stun-radius]} (get-in state [:terms :powerups])
        apart (steps-between at (:at state))
        spent (fn [state] (assoc-in state [:entities index :power] nil))]
    (case power
      :vanquish (when (<= apart vanquish-range)
                  (-> state spent (hit-from-afar "used a vanquish on you" at)))
      :stun (when (<= apart stun-radius)
              (let [held (entity-stun-turns (:terms state))]
                (-> state
                    spent
                    (update :stuck max held)
                    (assoc :held-by :stun)
                    (record {:by :entities :kind :stunned-you :room (:at state)
                             :line (str "An entity stunned you for " (turns-word held) ".")}))))
      :teleport (when (> (get to-player at 0) 3)
                  (when-let [room (entity-teleport-room state index to-player)]
                    (-> state
                        spent
                        (assoc-in [:entities index :at] room)
                        (record {:by :entities :kind :teleported :room room
                                 :line "An entity teleported nearer."}))))
      nil)))

(def longest-slide
  "No slide on ice goes further than this many rooms, however much ice
  there is."
  40)

(def slide-stoppers
  "What stops anything sliding on ice short of it, like a wall. Whoever
  steps onto one on ice is held still."
  #{:box :cabinet :copier :pole :disturbance})

(defn- slidable?
  "Whether something sliding on ice can carry on into `room`."
  [state room]
  (and (room? state room)
       (not (shut? state room))
       (not (contains? slide-stoppers (fixture state room)))))

(defn- burn-entity
  "One singe for an entity. Enough of them add up to a blow."
  [state index]
  (let [per-hit (get-in state [:terms :terrain :burns-per-hit] 3)
        burns (inc (get-in state [:entities index :burns] 0))]
    (if (>= burns per-hit)
      (-> state
          (assoc-in [:entities index :burns] 0)
          (update-in [:entities index :wounds] (fnil inc 0)))
      (assoc-in state [:entities index :burns] burns))))

(defn- entity-lands
  "What the room an entity has just come into does to it: a trap holds
  it, fire singes it, and a powerup it can take, it takes."
  [state index]
  (let [at (get-in state [:entities index :at])]
    (cond-> state
      (= :bear-trap (fixture state at))
      (-> (assoc-in [:entities index :stuck] (get-in state [:terms :trap-holds] 2))
          (assoc-in [:plan at] :empty)
          (record {:by :entities :kind :gone :room at :fixture :bear-trap
                   :line "A trap caught an entity."}))

      (= :fire (terrain state at))
      (burn-entity index)

      true
      (entity-picks-up index))))

(defn- entity-slide
  "An entity on ice, carried on the way it was going until the ice
  ends, something stops it, or it reaches the player."
  [state index direction]
  (loop [state state slid 0]
    (let [{:keys [at stuck]} (get-in state [:entities index])
          next-door (beside at direction)]
      (if (and (= :ice (terrain state at))
               (not (contains? slide-stoppers (fixture state at)))
               (zero? (or stuck 0))
               (not= at (:at state))
               (< slid longest-slide)
               (slidable? state next-door)
               (open-to-entities? state next-door)
               (not (contains? teleporters (fixture state next-door)))
               (not (contains? (disj (occupants state) at) next-door)))
        (recur (-> state
                   (assoc-in [:entities index :at] next-door)
                   (entity-lands index))
               (inc slid))
        state))))

(defn- entity-step
  "One step for an entity along `route`, then whatever the room does to
  it, and on ice, a slide."
  [state index route sends]
  (let [at (get-in state [:entities index :at])
        landing (toward state route sends at (disj (occupants state) at))]
    (if (= landing at)
      state
      (let [moved (-> state
                      (assoc-in [:entities index :at] landing)
                      (entity-lands index))
            direction (direction-of at landing)]
        (if (and direction (zero? (get-in moved [:entities index :stuck] 0)))
          (entity-slide moved index direction)
          moved)))))

(defn- entity-acts
  "One entity's turn. A thrower throws when the player is near enough
  and it did not throw last time. One holding a powerup uses it when
  it would reach the player. Otherwise it steps, and a fast one steps
  twice, toward a powerup it would rather have, or toward the player,
  going round fire when it can."
  [state index to-player to-player-cool to-drops sends]
  (let [{:keys [at stuck power kind threw]} (get-in state [:entities index])
        reach (get-in state [:terms :specials :throw-range] 2)]
    (cond
      (pos? (or stuck 0))
      (update-in state [:entities index :stuck] dec)

      (and (= :thrower kind) (not threw) (<= (steps-between at (:at state)) reach))
      (-> state
          (assoc-in [:entities index :threw] true)
          (hit-from-afar "threw something at you" at))

      :else
      (let [state (assoc-in state [:entities index :threw] false)]
        (or (when power (entity-uses-power state index to-player))
            (let [route (chasing state index (if (get to-player-cool at) to-player-cool to-player) to-drops)
                  once (entity-step state index route sends)
                  now (get-in once [:entities index])]
              (if (and (= :fast kind)
                       (not= (:at now) at)
                       (not= (:at now) (:at once))
                       (zero? (:stuck now 0)))
                (entity-step once index route sends)
                once)))))))

(defn- bury-the-burned
  "Entities the fire has ended, gone."
  [state]
  (let [{gone true kept false} (group-by (fn [entity] (>= (:wounds entity 0) (toughness entity)))
                                         (:entities state))]
    (reduce (fn [state {:keys [at]}]
              (record state {:by :floor :kind :ended :room at :line "The fire ended an entity."}))
            (assoc state :entities (vec kept))
            gone)))

(defn- move-entities
  "Every entity free to act takes its turn, on the turns entities move
  at all."
  [state]
  (if-not (entities-move? state)
    state
    (let [to-player (distances-to state (:at state) open-to-entities? false)
          to-player-cool (into {} (remove (fn [[room _]] (and (not= room (:at state)) (= :fire (terrain state room))))
                                       (distances-to state (:at state) cool-for-entities? false)))
          to-drops (when (and (entity-powers? state) (seq (:drops state)))
                     (into {} (for [{:keys [at]} (:drops state)]
                                [at (distances-to state at open-to-entities? false)])))
          sends (portal-map state)]
      (bury-the-burned
       (reduce (fn [state index] (entity-acts state index to-player to-player-cool to-drops sends))
               state
               (range (count (:entities state))))))))

(defn- shield-hot?
  [state]
  (boolean (and (:shield state)
                (< (- (:turn state) (:shield state))
                   (get-in state [:terms :shield-turns] 3)))))

(defn- put-back
  "Whatever reached the player is put back a room, where there is
  anywhere to put it, and spends a turn getting over it."
  [state]
  (let [here (:at state)]
    (reduce (fn [state index]
              (if (not= here (get-in state [:entities index :at]))
                state
                (let [away (first (remove (occupants state) (passable state here)))]
                  (-> state
                      (assoc-in [:entities index :at] (or away here))
                      (assoc-in [:entities index :stuck] 1)))))
            state
            (range (count (:entities state))))))

(defn- blow
  "One blow to every entity standing in `room`. Those it ends are gone;
  a strong one it only wounds stays, wounded. Returns the floor, how
  many were ended, and how many only wounded."
  [state room]
  (let [{struck true spared false} (group-by (fn [entity] (= room (:at entity))) (:entities state))
        struck (map (fn [entity] (update entity :wounds (fnil inc 0))) struck)
        {ended true wounded false} (group-by (fn [entity] (>= (:wounds entity) (toughness entity))) struck)]
    [(assoc state :entities (vec (concat spared wounded))) (count ended) (count wounded)]))

(defn- contact
  "Whatever entity is in the player's room when the turn ends. A hot
  shield deals it a blow, a cool one takes the hit for the player, and
  otherwise the player takes it. Anything left standing is put back a
  room."
  [state]
  (let [here (:at state)
        present (count (filter (fn [entity] (= here (:at entity))) (:entities state)))]
    (cond
      (zero? present) state

      (shield-hot? state)
      (let [[struck ended wounded] (blow state here)]
        (cond-> (-> struck (assoc :shield nil) (update :kills (fnil + 0) ended))
          (pos? ended)
          (record {:by :entities :kind :ended :room here
                   :line (str "The shield ended "
                              (if (= 1 ended) "an entity" (str ended " entities"))
                              ". +" (* ended (points state :vanquish)))})

          (pos? wounded)
          (-> put-back
              (record {:by :entities :kind :wounded :room here
                       :line "The shield wounded the strong entity. One more blow ends it."}))))

      (:shield state)
      (-> state
          (assoc :shield nil)
          put-back
          (record {:by :entities :kind :sheltered :room here
                   :line "The shield took the hit and is gone."}))

      :else
      (-> state
          hurt
          put-back
          (record {:by :entities :kind :hurt :room here
                   :line (str "An entity reached you. -" (points state :hit))})))))

;; ---------------------------------------------------------------------
;; Powerups
;; ---------------------------------------------------------------------

(defn- pick-up
  "Whatever powerup is lying where the player stands, into the player's
  hands."
  [state]
  (let [here (:at state)
        found (filter (fn [lying] (= here (:at lying))) (:drops state))]
    (reduce (fn [state {:keys [kind]}]
              (-> state
                  (update-in [:powers kind] (fnil inc 0))
                  (record {:by :you :kind :gone :room here :fixture kind
                           :line (str "Picked up " (label-of kind) ". Press "
                                      (get power-keys kind) " to use it.")})))
            (update state :drops (fn [drops]
                                   (vec (remove (fn [lying] (= here (:at lying))) drops))))
            found)))

(defn- age-drops
  "Every powerup on the floor is a turn nearer to fading."
  [state]
  (let [aged (mapv (fn [lying] (update lying :left dec)) (:drops state))
        faded (filter (fn [lying] (<= (:left lying) 0)) aged)]
    (reduce (fn [state {:keys [kind at]}]
              (record state {:by :floor :kind :gone :room at :fixture kind
                             :line (str "The " (label-of kind) " powerup faded.")}))
            (assoc state :drops (vec (remove (fn [lying] (<= (:left lying) 0)) aged)))
            faded)))

(defn- drop-powerup
  "Perhaps a powerup, in an empty room a little way from the player, of
  a kind that has not already turned up as often as the floor allows."
  [state]
  (let [{:keys [chance lasts]} (get-in state [:terms :powerups])
        [happens? state] (draw-chance state (or chance 0))
        most (get-in state [:terms :drops] 1)
        kinds (filterv (fn [kind] (< (get-in state [:dropped kind] 0) most)) power-kinds)
        here (:at state)
        places (filterv (fn [room] (and (> (steps-between here room) 2) (nil? (terrain state room))))
                        (free-rooms state))]
    (if (or (not happens?) (:over state) (empty? kinds) (empty? places))
      state
      (let [[kind state] (draw state kinds)
            [room state] (draw state places)]
        (-> state
            (update :drops conj {:kind kind :at room :left (or lasts 12)})
            (update-in [:dropped kind] (fnil inc 0))
            (record {:by :floor :kind :dropped :room room
                     :line (str "A " (label-of kind) " powerup appeared.")}))))))

(defn vanquish-targets
  "Rooms holding an entity close enough to vanquish."
  [state]
  (let [reach (get-in state [:terms :powerups :vanquish-range] 5)
        here (:at state)]
    (vec (sort (filter (fn [room] (<= (steps-between here room) reach))
                       (occupants state))))))

(defn- region
  "Every room the player could walk to without unlocking anything."
  [state]
  (reachable-from state (:at state) (portal-map state)
                  (fn [state room] (not (shut? state room)))))

(defn teleport-targets
  "Rooms the player could teleport to: close enough, on the player's
  side of every lock, and neither a portal, a locked door, nor an
  entity."
  [state]
  (let [reach (get-in state [:terms :powerups :teleport-range] 4)
        here (:at state)
        taken (occupants state)]
    (vec (sort (filter (fn [room]
                         (and (<= 1 (steps-between here room) reach)
                              (not (shut? state room))
                              (not (contains? teleporters (fixture state room)))
                              (not (contains? taken room))))
                       (region state))))))

(declare arrive finished? tread)

(defn- use-power
  "A powerup, used. It takes no turn: nothing else moves."
  [state kind target]
  (if-not (pos? (get-in state [:powers kind] 0))
    (note state "You have none of those.")
    (case kind
      :vanquish
      (let [targets (vanquish-targets state)
            here (:at state)
            chosen (if (some (fn [room] (= room target)) targets)
                     target
                     (first (sort-by (fn [room] (steps-between here room)) targets)))]
        (if-not chosen
          (note state "Nothing close enough to vanquish.")
          (let [[struck ended _] (blow (update-in state [:powers :vanquish] dec) chosen)]
            (if (pos? ended)
              (-> struck
                  (update :kills (fnil + 0) ended)
                  (record {:by :you :kind :ended :room chosen
                           :line (str "Vanquished an entity. +" (* ended (points state :vanquish)))}))
              (record struck {:by :you :kind :wounded :room chosen
                              :line "Wounded the strong entity. One more blow ends it."})))))

      :teleport
      (if-not (some (fn [room] (= room target)) (teleport-targets state))
        (note state "Pick a highlighted room to teleport to.")
        (-> state
            (update-in [:powers :teleport] dec)
            (assoc :at target :stuck 0)
            (record {:by :you :kind :teleported :room target :line "Teleported."})
            arrive
            (tread nil)
            (dissoc :slowed)
            contact
            finished?))

      :stun
      (let [{:keys [stun-radius stun-turns]} (get-in state [:terms :powerups])
            here (:at state)
            caught (filterv (fn [index]
                              (<= (steps-between here (get-in state [:entities index :at]))
                                  stun-radius))
                            (range (count (:entities state))))]
        (if (empty? caught)
          (note state "Nothing close enough to stun.")
          (-> (reduce (fn [state index]
                        (update-in state [:entities index :stuck] (fn [stuck] (max stuck stun-turns))))
                      state
                      caught)
              (update-in [:powers :stun] dec)
              (update :stunned (fnil + 0) (count caught))
              (record {:by :you :kind :stunned :room here
                       :rooms (mapv (fn [index] (get-in state [:entities index :at])) caught)
                       :line (str "Stunned "
                                  (if (= 1 (count caught)) "an entity" (str (count caught) " entities"))
                                  ". +" (* (count caught) (points state :stun)))}))))

      state)))

;; ---------------------------------------------------------------------
;; Rooms that do something
;; ---------------------------------------------------------------------

(defn- arrive-at-fixture
  [state]
  (let [here (:at state)
        kind (fixture state here)]
    (case kind
      (:cabinet :copier)
      (-> state
          hurt
          (record {:by :you :kind :hurt :room here
                   :line (str "Walked into the " (label-of kind) ". -" (points state :hit))}))

      :disturbance
      (if (:shield state)
        (-> state
            (assoc :shield nil)
            (record {:by :you :kind :sheltered :room here
                     :line "The shield took the disturbance and is gone."}))
        (-> state
            hurt
            (record {:by :you :kind :hurt :room here
                     :line (str "Walked into the disturbance. -" (points state :hit))})))

      :bear-trap
      (-> state
          (assoc-in [:plan here] :empty)
          (assoc :stuck (get-in state [:terms :trap-holds-you] 1) :held-by :trap)
          (record {:by :you :kind :gone :room here :fixture :bear-trap
                   :line "Caught in a trap."}))

      :shield
      (-> state
          (assoc-in [:plan here] :empty)
          (assoc :shield (:turn state))
          (record {:by :you :kind :gone :room here :fixture :shield
                   :line "Picked up the shield."}))

      :key
      (-> state
          (assoc-in [:plan here] :empty)
          (update :keys (fnil inc 0))
          (record {:by :you :kind :gone :room here :fixture :key
                   :line "Picked up a key."}))

      (:sand-boots :ice-boots :fire-boots)
      (-> state
          (assoc-in [:plan here] :empty)
          (update :boots (fnil conj #{}) kind)
          (record {:by :you :kind :gone :room here :fixture kind
                   :line (str "Put on the " (label-of kind) ".")}))

      (:portal-a :portal-b :portal-in)
      (if-let [there (through state here)]
        (-> state
            (assoc :at there)
            (record {:by :you :kind :through :room there :line "Through the portal."}))
        state)

      state)))

(defn- arrive
  "What a room does to the player on arriving in it."
  [state]
  (pick-up (arrive-at-fixture state)))

;; ---------------------------------------------------------------------
;; Walking, pushing, taking
;; ---------------------------------------------------------------------

(def pushable
  "What the player can put in something's way. Not the door, not the
  way out, not the thing the player came for."
  #{:box :cabinet :copier :bear-trap :disturbance})

(defn- step-into
  [state room]
  (cond
    (pit? state room)
    (note state "That room is gone.")

    (not (room? state room))
    (note state "No room that way.")

    (shut? state room)
    (if (pos? (:keys state 0))
      (-> state
          (assoc-in [:plan room] :doorway)
          (update :keys dec)
          (record {:by :you :kind :opened :room room
                   :line "Unlocked the door. Entities can come through it now."}))
      (note state "Locked. There is a key somewhere on this side."))

    :else (assoc state :at room)))

(defn- box-can-enter?
  "Whether something pushed can go into `room`."
  [state room]
  (and (room? state room)
       (nil? (fixture state room))
       (not= room (:at state))
       (not (contains? (occupants state) room))
       (not (contains? (drop-rooms state) room))))

(defn- box-slide
  "Where something pushed onto ice comes to rest: carried on the way it
  was pushed until the ice ends or the next room cannot take it."
  [state from direction]
  (loop [here from slid 0]
    (let [onward (beside here direction)]
      (if (and (= :ice (terrain state here)) (< slid longest-slide) (box-can-enter? state onward))
        (recur onward (inc slid))
        here))))

(def opposite
  {:north :south :south :north :east :west :west :east})

(defn- pull
  "Shift and an arrow away from something right behind the player: the
  player steps forward, and it follows into the room the player left.
  Never on ice, where things can only be pushed."
  [state direction]
  (let [here (:at state)
        ahead (beside here direction)
        behind (beside here (opposite direction))
        kind (fixture state behind)]
    (cond
      (not (contains? pushable kind))
      (note state "Nothing to push or pull that way.")

      (some (fn [room] (= :ice (terrain state room))) [behind here ahead])
      (note state "On ice, things can only be pushed.")

      (or (fixture state here)
          (not (room? state ahead))
          (fixture state ahead)
          (contains? (occupants state) ahead))
      (note state "No room to pull it that way.")

      :else
      (-> state
          (assoc-in [:plan behind] :empty)
          (assoc-in [:plan here] kind)
          (assoc :at ahead)
          (record {:by :you :kind :pushed :fixture kind :from behind :to here
                   :line (str "You pulled the " (label-of kind) ".")})))))

(defn- push
  "Shift and an arrow: whatever is next door that way goes one room
  further, and the player steps in behind it; pushed onto ice, it
  slides until the ice ends or something stops it. With nothing there
  to push, what is right behind the player is pulled instead."
  [state direction]
  (let [next-door (beside (:at state) direction)
        beyond (beside next-door direction)
        kind (fixture state next-door)]
    (cond
      (not (contains? pushable kind))
      (pull state direction)

      (not (box-can-enter? state beyond))
      (note state "It will not go any further that way.")

      :else
      (let [resting (box-slide state beyond direction)]
        (-> state
            (assoc-in [:plan next-door] :empty)
            (assoc-in [:plan resting] kind)
            (assoc :at next-door)
            (record {:by :you :kind :pushed :fixture kind :from next-door :to resting
                     :line (if (= resting beyond)
                             (str "You pushed the " (label-of kind) ".")
                             (str "You pushed the " (label-of kind) ", and it slid across the ice."))}))))))

(defn- take-warrant
  [state]
  (let [here (:at state)
        every (get-in state [:terms :release])]
    (if (= :warrant (fixture state here))
      (-> state
          (assoc-in [:plan here] :empty)
          (assoc :held :warrant :held-at (:turn state))
          (record {:by :you :kind :gone :room here :fixture :warrant
                   :line (if every
                           (str "You have the warrant. A room goes every " every " turns now.")
                           "You have the warrant. Now get out.")}))
      (note state "Nothing to take here."))))

;; ---------------------------------------------------------------------
;; The ground
;; ---------------------------------------------------------------------

(def terrain-kinds
  [:sand :ice :fire])

(def boots-for
  {:sand :sand-boots :ice :ice-boots :fire :fire-boots})

(def harshness
  "How hard each kind of ground is on the player without its boots."
  {:sand 0 :ice 1 :fire 2})

(defn- shod?
  "Whether the player has the boots for a kind of ground."
  [state ground]
  (contains? (:boots state) (boots-for ground)))

(defn- slide
  "The player on ice, carried on the way the step was going until the
  ice ends, something stops the slide, or it comes into a room with an
  entity in it. Every room on the way does to the player what it does."
  [state direction]
  (loop [state state slid 0]
    (let [here (:at state)
          next-door (beside here direction)]
      (if (and (= :ice (terrain state here))
               (not (contains? slide-stoppers (fixture state here)))
               (not (shod? state :ice))
               (zero? (:stuck state 0))
               (not (contains? (occupants state) here))
               (< slid longest-slide)
               (slidable? state next-door))
        (let [moved (-> state (assoc :at next-door) arrive)]
          (if (= next-door (:at moved))
            (recur moved (inc slid))
            moved))
        (cond-> state
          (pos? slid)
          (record {:by :you :kind :slid :room here :line "You slid across the ice."}))))))

(defn- burn
  "One singe from the fire. Enough of them add up to a hit, which a
  shield takes the way it takes any hit."
  [state]
  (let [per-hit (get-in state [:terms :terrain :burns-per-hit] 3)
        burns (inc (:burns state 0))]
    (cond
      (< burns per-hit)
      (-> state
          (assoc :burns burns)
          (record {:by :floor :kind :singed :room (:at state)
                   :line (str "The fire singed you, " burns " of " per-hit " toward a hit.")}))

      (:shield state)
      (-> state
          (assoc :burns 0 :shield nil)
          (record {:by :floor :kind :sheltered :room (:at state)
                   :line "The fire burned through to the shield, and the shield is gone."}))

      :else
      (-> state
          (assoc :burns 0)
          hurt
          (record {:by :floor :kind :hurt :room (:at state)
                   :line (str "The fire burned you. -" (points state :hit))})))))

(defn- tread
  "What the ground does to the player standing on it. Ice carries the
  player on the way the step was going, fire singes, and sand makes
  the step take two turns, unless the player has the boots for it.
  With no direction, as after a teleport, ice has nowhere to carry
  anyone."
  [state direction]
  (let [state (if direction (slide state direction) state)
        ground (terrain state (:at state))]
    (cond
      (or (nil? ground) (shod? state ground)) state
      (= :fire ground) (burn state)
      (= :sand ground) (assoc state :slowed true)
      :else state)))

;; ---------------------------------------------------------------------
;; Whether a floor can be finished on foot
;; ---------------------------------------------------------------------

(defn- comes-to-rest
  "Where a step from `room` toward `direction` leaves the player on
  foot, wearing `boots`: in the room next door, out of the other end
  when it is a two-way portal, and on across any ice without ice boots
  until the ice ends or something stops the slide. Nil for a step into
  a wall, a locked door, a one-way portal, or fire without fire boots."
  [state room direction boots sends]
  (let [unshod? (fn [ground] (not (contains? boots (boots-for ground))))
        barred? (fn [room] (or (= :portal-in (fixture state room))
                               (and (= :fire (terrain state room)) (unshod? :fire))))
        next-door (beside room direction)]
    (when (and (room? state next-door) (not (shut? state next-door)) (not (barred? next-door)))
      (loop [here (get sends next-door next-door) slid 0]
        (let [onward (beside here direction)]
          (cond
            (or (not (and (= :ice (terrain state here)) (unshod? :ice)))
                (contains? slide-stoppers (fixture state here))) here
            (or (>= slid longest-slide) (not (slidable? state onward))) here
            (barred? onward) nil
            (contains? sends onward) (get sends onward)
            :else (recur onward (inc slid))))))))

(defn- on-foot
  "Every room the player can come to rest in from where the player
  stands, walking and sliding, wearing `boots`."
  [state boots]
  (let [sends (portal-map state)
        start (:at state)]
    (loop [seen #{start} frontier [start]]
      (if (empty? frontier)
        seen
        (let [reached (distinct (for [room frontier
                                      direction [:north :east :south :west]
                                      :let [stop (comes-to-rest state room direction boots sends)]
                                      :when (and stop (not (seen stop)))]
                                  stop))]
          (recur (into seen reached) (vec reached)))))))

(defn- reach-in-the-end
  "Every room the player could come to rest in, in the end, on foot:
  walking and sliding from where the player stands, picking up every
  key and pair of boots within reach and opening every locked door
  within reach until nothing more opens, never stepping into fire
  without fire boots or into a one-way portal."
  [state]
  (loop [state state boots #{} held-keys 0]
    (let [reach (on-foot state boots)
          found-keys (filter reach (holding state :key))
          state (reduce (fn [state room] (assoc-in state [:plan room] :empty)) state found-keys)
          held-keys (+ held-keys (count found-keys))
          new-boots (remove boots (filter (fn [kind] (some reach (holding state kind))) (vals boots-for)))
          door (first (filter (fn [door] (some reach (adjoining state door)))
                              (sort (holding state :door))))]
      (cond
        (seq new-boots) (recur state (into boots new-boots) held-keys)
        (and door (pos? held-keys)) (recur (assoc-in state [:plan door] :doorway) boots (dec held-keys))
        :else reach))))

(defn- finishable?
  "Whether the floor as laid can be finished on foot: the warrant and the
  way out both within reach in the end."
  [state]
  (let [reach (reach-in-the-end state)]
    (boolean (and (some reach (holding state :way-out))
                  (some reach (holding state :warrant))))))

;; ---------------------------------------------------------------------
;; Zell
;; ---------------------------------------------------------------------

(defn- zell-due?
  [state]
  (let [every (get-in state [:terms :writes])]
    (boolean (and every (pos? every) (pos? (:turn state))
                  (zero? (mod (:turn state) every))))))

(defn- amend
  [state]
  (let [unwritten (vec (remove (set (:reqs state)) (:amendments state)))]
    (if (empty? unwritten)
      state
      (let [[chosen state] (draw state unwritten)]
        (-> state
            (update :reqs conj chosen)
            (assoc :new-rule chosen)
            (record {:by :floor :kind :wrote :rule chosen
                     :line (str "New requirement: " (sentence chosen) ".")}))))))

(defn- zell
  [state]
  (if (zell-due? state) (amend state) state))

;; ---------------------------------------------------------------------
;; Laying a floor out
;; ---------------------------------------------------------------------

(defn- carve
  "Rooms at the given density, then only the largest piece of them."
  [rows cols density]
  (let [kept (into {} (for [row (range rows)
                            col (range cols)
                            :when (< (chance!) density)]
                        [[row col] :empty]))
        largest (first (pieces {:plan kept}))]
    (into {} (map (fn [room] [room :empty])) largest)))

(defn- might-divide?
  "Whether taking `room` away could split the building at all. When its
  neighbours can still reach one another through the eight rooms around
  it, it cannot, and there is no need to search the whole floor to find
  that out."
  [plan [row col :as room]]
  (let [ring (into {} (for [row-step [-1 0 1]
                            col-step [-1 0 1]
                            :let [cell [(+ row row-step) (+ col col-step)]]
                            :when (and (not= cell room) (contains? plan cell))]
                        [cell :empty]))
        neighbours (filter (fn [cell] (contains? ring cell))
                           [[(dec row) col] [row (inc col)] [(inc row) col] [row (dec col)]])]
    (and (> (count neighbours) 1)
         (let [reached (component {:plan ring} (first neighbours))]
           (not-every? reached neighbours)))))

(defn- natural-doors
  "Rooms that already cut the building in two, with at least `least` on
  each side. The ones that might are found by looking at their
  surroundings, and only those are searched for, one whole floor each."
  [plan least]
  (let [state {:plan plan}]
    (vec (for [room (sort (keys plan))
               :when (might-divide? plan room)
               :let [[near far] (pieces (update state :plan dissoc room))]
               :when (and far (>= (count near) least) (>= (count far) least))]
           {:door room :near near :far far}))))

(defn- natural-lock
  "One lock in a room that already divides the building fairly, or nil."
  [plan least]
  (when-let [{:keys [door near far]} (pick! (natural-doors plan least))]
    {:plan plan
     :doors [door]
     :zones (if (< (chance!) 0.5) [near far] [far near])}))

(defn- wall-line
  "One wall's position in each row it crosses: starting at `around` and
  wandering a room either way as it goes, never outside `low` and
  `high`."
  [length around low high]
  (loop [line [(max low (min high around))]]
    (if (>= (count line) length)
      line
      (recur (conj line (max low (min high (+ (peek line) (dec (roll! 3))))))))))

(defn- chain
  "The zones in the order the player passes through them, and the doors
  between, given the zones and which two each door joins. Nil unless
  they make a single line with a zone at each end."
  [zones joins]
  (let [touching (fn [zone] (count (filter (fn [[_ a b]] (or (= a zone) (= b zone))) joins)))
        ends (filter (fn [zone] (= 1 (touching zone))) zones)]
    (when (= 2 (count ends))
      (loop [order [(if (< (chance!) 0.5) (first ends) (second ends))]
             doors []
             left joins]
        (if (empty? left)
          (when (= (count order) (count zones)) {:zones order :doors doors})
          (let [here (peek order)
                [door a b :as join] (first (filter (fn [[_ a b]] (or (= a here) (= b here))) left))]
            (when join
              (recur (conj order (if (= a here) b a))
                     (conj doors door)
                     (remove (fn [other] (= other join)) left)))))))))

(defn- walled
  "The building with `locks` walls across it, each wandering in its own
  band with one room left standing in it for its door, or nil when this
  carving will not take them. Every zone between the walls has to hold
  at least `least` rooms."
  [plan rows cols locks least]
  (let [upright? (< (chance!) 0.5)
        length (if upright? rows cols)
        span (if upright? cols rows)
        cell (fn [index across] (if upright? [index across] [across index]))
        either-side (fn [[row col]]
                      (if upright?
                        [[row (dec col)] [row (inc col)]]
                        [[(dec row) col] [(inc row) col]]))
        centres (mapv (fn [k]
                        (+ (quot (* span k) (inc locks))
                           (if (= 1 locks) (dec (roll! 3)) 0)))
                      (range 1 (inc locks)))
        walls (mapv (fn [around]
                      (let [line (wall-line length around (max 1 (dec around)) (min (- span 2) (inc around)))]
                        (set (map-indexed cell line))))
                    centres)
        every-wall (reduce into #{} walls)
        doors (mapv (fn [wall]
                      (pick! (filter (fn [spot]
                                       (every? (fn [side]
                                                 (and (contains? plan side)
                                                      (not (contains? every-wall side))))
                                               (either-side spot)))
                                     (sort wall))))
                    walls)]
    (when (every? some? doors)
      (let [rebuilt (reduce (fn [plan door] (assoc plan door :empty))
                            (apply dissoc plan every-wall)
                            doors)
            standing (select-keys rebuilt (component {:plan rebuilt} (first doors)))
            zones (pieces {:plan (apply dissoc standing doors)})
            zone-of (fn [room] (first (filter (fn [zone] (contains? zone room)) zones)))
            joins (mapv (fn [door] (into [door] (map zone-of (either-side door)))) doors)]
        (when (and (every? (fn [door] (contains? standing door)) doors)
                   (= (count zones) (inc locks))
                   (every? (fn [zone] (>= (count zone) least)) zones)
                   (every? (fn [[_ a b]] (and a b (not= a b))) joins))
          (when-let [{:keys [zones doors]} (chain zones joins)]
            {:plan standing :doors doors :zones zones}))))))

(defn- divided
  "A building with this many locks, or nil when this carving will not
  take them. One lock may be a room that already divides the building
  fairly; more than one are always walls."
  [plan rows cols locks]
  (let [least (max 6 (quot (count plan) (if (= 1 locks) 4 (* 3 (inc locks)))))]
    (cond
      (zero? locks) (when (seq plan) {:plan plan :doors [] :zones [(set (keys plan))]})
      (and (= 1 locks) (< (chance!) 0.5)) (or (natural-lock plan least)
                                              (walled plan rows cols locks least))
      :else (walled plan rows cols locks least))))

(defn- building
"Rooms, their locks, and the zones between them. Carved at the density
  asked for up to a dozen times, then built solid, and if even a solid
  building will not take that many locks, one fewer. Always returns one."
  [rows cols density locks]
  (loop [locks locks]
    (or (loop [tries 0]
          (when (< tries 14)
            (or (divided (carve rows cols (if (< tries 12) density 1.0)) rows cols locks)
                (recur (inc tries)))))
        (recur (max 0 (dec locks))))))

(defn- put
  [state room kind]
  (if room (assoc-in state [:plan room] kind) state))

(defn- further-half
  "The rooms of `candidates` at least as far as the middle one, by
  `distances`, so that a choice among them is never right next door."
  [distances candidates]
  (let [ranked (sort-by (fn [room] (get distances room 0)) (sort candidates))]
    (drop (quot (count ranked) 2) ranked)))

(defn- somewhere
  "A free room in one of `zones`, the zone chosen by the die, where
  `fitting?` holds, falling back to any of those zones, then to anywhere
  on the floor."
  [state zones fitting?]
  (let [free-in (fn [zone] (filter (fn [room] (and (free? state room) (fitting? room))) (sort zone)))]
    (or (pick! (free-in (pick! zones)))
        (pick! (mapcat free-in zones))
        (pick! (free-rooms state)))))

(defn- scatter
  "`how-many` of `kind`, each somewhere in `zones` where `fitting?` holds."
  [state zones fitting? how-many kind]
  (reduce (fn [state _] (put state (somewhere state zones fitting?) kind))
          state
          (range (or how-many 0))))

(defn- place-entities
  "Entities go anywhere at least four rooms from where the player
  starts and not next to one another, so some are loose from the first
  turn and some are waiting behind a door."
  [state zones from-start how-many kind]
  (let [every-room (sort (apply concat zones))]
    (loop [state state left how-many]
      (if (zero? left)
        state
        (let [taken (occupants state)
              apart? (fn [room] (not-any? taken (adjoining state room)))
              room (or (pick! (filter (fn [room]
                                        (and (free? state room)
                                             (>= (get from-start room 0) 4)
                                             (apart? room)))
                                      every-room))
                       (pick! (filter (fn [room]
                                        (and (free? state room)
                                             (>= (get from-start room 0) 2)))
                                      every-room)))]
          (if room
            (recur (update state :entities conj (cond-> {:at room :was room :stuck 0} kind (assoc :kind kind)))
                   (dec left))
            state))))))

(defn- sound?
  "Whether everything that has to be reached can be, from where the
  player starts, and whether the way out can still be reached from
  wherever a portal might put the player. A portal in a corridor is a
  wall with a door in it somewhere else, and some corridors cannot
  take one."
  [state]
  (let [sends (portal-map state)
        from-start (reachable-from state (:at state) sends)
        way-out (first (holding state :way-out))]
    (and (every? (fn [kind] (every? from-start (holding state kind)))
                 [:key :warrant :door :way-out])
         (every? (fn [landing-room]
                   (or (not (contains? from-start landing-room))
                       (contains? (reachable-from state landing-room sends) way-out)))
                 (distinct (vals sends))))))

(declare winnable?)

(defn- place-pair
  "A portal and where it goes, at least four rooms apart. Where the
  floor has locks, the die decides whether it crosses one, and a one-
  way portal that crosses a lock can leave the player where nothing
  opens the way back. Left out when it would cut the floor off from
  itself, or when the floor could not be finished without taking a
  one-way portal: its room is a wall to anyone walking, so it must
  never stand where the only way on goes through it. Taking it is
  always the player's own gamble."
  [state zones from-start entry-kind exit-kind]
  (let [free-in (fn [zone]
                  (filter (fn [room] (and (free? state room)
                                          (not (approach? state room))
                                          (>= (get from-start room 0) 2)))
                          (sort zone)))
        entry-zone (pick! (filter (fn [zone] (seq (free-in zone))) zones))
        entry (pick! (free-in entry-zone))
        from-entry (if entry (distances-from state entry adjoining) {})
        lock-crossing-chance 0.6   ; how often a portal on a floor with locks crosses one
        crossing? (and (> (count zones) 1) (< (chance!) lock-crossing-chance))
        exit-zones (if crossing? (remove (fn [zone] (= zone entry-zone)) zones) [entry-zone])
        exit (pick! (filter (fn [room] (and (not= room entry) (>= (get from-entry room 0) 4)))
                            (mapcat free-in exit-zones)))
        placed (-> state (put entry entry-kind) (put exit exit-kind))
        on-foot (if (= :portal-in entry-kind) (update placed :plan dissoc entry) placed)]
    (if (and entry exit (sound? placed) (winnable? on-foot)) placed state)))

(defn- place-by-zone
  "Everything that decides the floor. The doors are where the building
  was divided. The player starts in the first zone, some way from its
  door, and the way out is in the last, some way from its. Each key is
  in a zone before the door it opens. The rest goes where the die puts
  it."
  [state {:keys [doors zones]} terms settings]
  (let [first-zone (first zones)
        last-zone (last zones)
        every-room (sort (apply concat zones))
        start (pick! (if (seq doors)
                       (further-half (distances-from state (first doors) adjoining) first-zone)
                       every-room))
        from-start (distances-from state start adjoining)
        out (pick! (further-half (if (seq doors)
                                   (distances-from state (peek doors) adjoining)
                                   from-start)
                                 (remove #{start} last-zone)))
        placed (-> (reduce (fn [state door] (put state door :door)) state doors)
                   (assoc :at start :was-at start)
                   (put out :way-out))
        with-keys (reduce (fn [state index]
                            (put state
                                 (somewhere state (subvec zones 0 (inc index))
                                            (fn [room] (>= (get from-start room 0) 2)))
                                 :key))
                          placed
                          (range (count doors)))
        from-out (distances-from with-keys out adjoining)
        with-warrant (put with-keys
                          (somewhere with-keys zones
                                     (fn [room] (and (>= (get from-start room 0) 3)
                                                     (>= (get from-out room 0) 3))))
                          :warrant)]
    (-> with-warrant
        (place-entities zones from-start (:entities terms) nil)
        (cond-> (:special terms) (place-entities zones from-start 1 (:special terms)))
        (scatter zones (fn [room] (>= (get from-start room 0) 2)) (:shields settings) :shield)
        (scatter zones (fn [room] (>= (get from-start room 0) 3)) (:traps settings) :bear-trap)
        (cond-> (:portals settings) (place-pair zones from-start :portal-a :portal-b))
        (cond-> (:one-way settings) (place-pair zones from-start :portal-in :portal-out)))))

(defn- furnish
  "The furniture, anywhere there is room for it except right beside a
  door or the way out. The counts are for a building ten rooms by ten
  and are scaled to this one."
  [state settings]
  (let [scale (* (or (:clutter settings) 1)
                 (/ (* (:rows state) (:cols state)) 100))
        every-piece (mapcat (fn [[kind how-many]]
                              (repeat (Math/round (* how-many scale)) kind))
                            (sort-by (fn [[kind _]] (name kind)) (:furniture settings)))]
    (reduce (fn [state kind]
              (if-let [room (pick! (remove (fn [room] (approach? state room)) (free-rooms state)))]
                (put state room kind)
                state))
            state
            every-piece)))

(defn- patch-rooms
  "The patches of one kind of ground, each a set of rooms that touch."
  [state ground]
  (let [of-ground (set (keep (fn [[room kind]] (when (and (= kind ground) (room? state room)) room))
                             (:terrain state)))]
    (loop [left of-ground found []]
      (if-let [start (first (sort left))]
        (let [patch (loop [seen #{start} frontier [start]]
                      (if (empty? frontier)
                        seen
                        (let [reached (->> frontier
                                           (mapcat (fn [room] (adjoining state room)))
                                           (filter of-ground)
                                           (remove seen)
                                           distinct
                                           vec)]
                          (recur (into seen reached) reached))))]
          (recur (reduce disj left patch) (conj found patch)))
        found))))

(defn- ground-kinds
  "Which kinds of ground a floor gets, gentlest first: none or one on
  floor one, none being likelier, and one more kind possible every
  `:new-kind-every` floors after that, the die now and then taking one
  away."
  [ground-settings floor]
  (let [{:keys [first-floor-bare new-kind-every one-fewer]} ground-settings
        most (min (count terrain-kinds) (inc (quot (dec floor) (max 1 (or new-kind-every 3)))))
        how-many (cond
                   (<= floor 1) (if (< (chance!) (or first-floor-bare 0)) 0 1)
                   (< (chance!) (or one-fewer 0)) (dec most)
                   :else most)
        shuffled (->> terrain-kinds
                      (map (fn [kind] [(chance!) kind]))
                      (sort-by first)
                      (map second))]
    (vec (sort-by harshness (take how-many shuffled)))))

(defn- corners-away
  "The corners of the building, farthest from where the player starts
  first."
  [state]
  (let [start (:at state)
        last-row (dec (:rows state))
        last-col (dec (:cols state))]
    (sort-by (fn [corner] (- (steps-between start corner)))
             [[0 0] [0 last-col] [last-row 0] [last-row last-col]])))

(def smallest-mass
  "The fewest rooms a mass of ground may have. A kind that cannot grow
  that big is taken up again."
  4)

(def may-lie-on-ground
  "What can stand on sand, ice or fire: furniture, traps and shields.
  Nothing that decides the floor."
  #{:box :cabinet :copier :pole :disturbance :bear-trap :shield})

(defn- groundable?
  "Whether a room can take `ground`: plain floor, or holding only what
  may lie on the ground, with nobody in it and not right beside a door
  or the way out; and for ice, not right beside a portal either, so that
  no slide ever ends in one."
  [state room ground]
  (and (room? state room)
       (nil? (terrain state room))
       (not= room (:at state))
       (not (contains? (occupants state) room))
       (not (approach? state room))
       (let [kind (fixture state room)]
         (or (nil? kind) (contains? may-lie-on-ground kind)))
       (or (not= :ice ground)
           (not-any? (fn [next-door] (contains? portal-kinds (fixture state next-door)))
                     (adjoining state room)))))

(defn- room-to-spread
  "Every room `ground` could spread to from `seed`, through rooms that
  can take it."
  [state seed ground from-start]
  (loop [seen #{seed} frontier [seed]]
    (if (empty? frontier)
      seen
      (let [reached (distinct (for [room frontier
                                    next-door (adjoining state room)
                                    :when (and (not (seen next-door))
                                               (groundable? state next-door ground)
                                               (>= (get from-start next-door 0) 2))]
                                next-door))]
        (recur (into seen reached) (vec reached))))))

(defn- seed-near
  "Where a mass of `size` rooms of `ground` starts: the room nearest
  `corner`, at least three rooms from where the player starts, with
  room enough around it to grow that big; failing that, of the
  nearest, the one with the most."
  [state corner ground from-start size]
  (let [corner-candidates 12   ; how many of the rooms nearest the corner are weighed
        nearest (->> (rooms state)
                     (filter (fn [room] (and (groundable? state room ground)
                                             (>= (get from-start room 0) 3))))
                     (sort-by (fn [room] [(steps-between corner room) room]))
                     (take corner-candidates))
        roomy (fn [room] (count (room-to-spread state room ground from-start)))]
    (or (first (filter (fn [room] (>= (roomy room) size)) nearest))
        (first (sort-by (fn [room] (- (roomy room))) nearest)))))

(defn- mass-size
  "How many rooms one mass of ground aims for: `share` of the floor, give
  or take a fifth, and less each when kinds share the floor."
  [state share kinds]
  (max smallest-mass (Math/round (/ (* (count (rooms state)) share (+ 0.8 (* 0.4 (chance!))))
                        (Math/pow (max 1 kinds) 0.7)))))

(defn- grow-mass
  "A mass of `ground` grown from `seed` through rooms that can take it,
  up to `size`. A room with more of the mass around it is likelier to be
  taken next, which keeps the mass together."
  [state seed ground size from-start]
  (loop [mass [seed] inside #{seed}]
    (let [candidates (vec (for [room mass
                                next-door (adjoining state room)
                                :when (and (not (inside next-door))
                                           (groundable? state next-door ground)
                                           (>= (get from-start next-door 0) 2))]
                            next-door))]
      (if (or (>= (count mass) size) (empty? candidates))
        mass
        (let [room (pick! candidates)]
          (recur (conj mass room) (conj inside room)))))))

(defn- scatter-bits
  "A few rooms of `ground` a room off the edge of `mass`, as though some
  of it had spread."
  [state mass ground from-start]
  (let [inside (set mass)
        spots (vec (distinct (for [room mass
                                   next-door (adjoining state room)
                                   beyond (adjoining state next-door)
                                   :when (and (not (inside next-door))
                                              (nil? (terrain state next-door))
                                              (groundable? state beyond ground)
                                              (>= (get from-start beyond 0) 2)
                                              (not-any? inside (adjoining state beyond)))]
                               beyond)))]
    (reduce (fn [state _]
              (if-let [spot (pick! (filterv (fn [room] (nil? (terrain state room))) spots))]
                (assoc-in state [:terrain spot] ground)
                state))
            state
            (range (roll! 4)))))

(defn- lay-masses
  "One mass of each kind in `kinds`, each grown from a corner far from
  where the player starts that no other mass has taken, the first such
  corner with room for it to reach its size, or failing that the
  roomiest; and when it grew large enough to have an edge, a little of
  it scattered off that edge. Each `attempt` turns the corners round
  by one, so a floor laid again is laid differently."
  [state kinds share from-start attempt]
  (let [mass-with-an-edge 6   ; big enough to scatter a little of itself off its edge
        corners (vec (corners-away state))
        corners (if (< (chance!) 0.5)
                  corners
                  (vec (concat [(second corners) (first corners)] (drop 2 corners))))
        corners (vec (take (count corners) (drop (mod attempt (count corners)) (cycle corners))))]
    (loop [state state left kinds unused corners]
      (if-let [ground (first left)]
        (let [size (mass-size state share (count kinds))
              options (keep (fn [corner]
                              (when-let [seed (seed-near state corner ground from-start size)]
                                [corner seed (count (room-to-spread state seed ground from-start))]))
                            unused)
              [corner seed] (or (first (filter (fn [[_ _ room]] (>= room size)) options))
                                (first (sort-by (fn [[_ _ room]] (- room)) options)))]
          (if seed
            (let [mass (grow-mass state seed ground size from-start)
                  grown (reduce (fn [state room] (assoc-in state [:terrain room] ground)) state mass)]
              (recur (if (>= (count mass) mass-with-an-edge) (scatter-bits grown mass ground from-start) grown)
                     (rest left)
                     (remove (fn [taken] (= taken corner)) unused)))
            (recur state (rest left) unused)))
        state))))

(defn- pocket-rooms
  "Empty rooms of the largest mass of `ground`, where boots could lie
  with that ground all round them, the most surrounded first."
  [state ground]
  (let [mass (first (sort-by (fn [patch] (- (count patch))) (patch-rooms state ground)))
        surround (fn [room] (count (filter (fn [next-door] (= ground (terrain state next-door)))
                                           (adjoining state room))))]
    (->> mass
         (filter (fn [room] (free? state room)))
         (sort-by (fn [room] [(- (surround room)) room])))))

(defn- boots-in-pocket
  "The floor with `boots` in a pocket of `gentler` ground: a room of it
  cleared to plain floor, the ground all round it, that the player
  could reach without those boots and whose clearing leaves the mass
  whole. Nil when no pocket will do."
  [state gentler boots]
  (let [patches (count (patch-rooms state gentler))]
    (pick! (vec (take 3 (for [room (pocket-rooms state gentler)
                              :let [cleared (update state :terrain dissoc room)]
                              :when (and (= patches (count (patch-rooms cleared gentler)))
                                         (contains? (reach-in-the-end cleared) room))]
                          (put cleared room boots)))))))

(defn- edge-rooms
  "Empty rooms of plain floor the player could reach without the boots
  for `ground`, nearest its edge first."
  [state ground]
  (let [of-ground (keep (fn [[room kind]] (when (= kind ground) room)) (:terrain state))
        distance (fn [room] (apply min 99 (map (fn [other] (steps-between room other)) of-ground)))]
    (->> (reach-in-the-end state)
         (filter (fn [room] (and (free? state room) (nil? (terrain state room)))))
         sort
         (sort-by (fn [room] [(distance room) room])))))

(defn- place-all-boots
  "Boots for every kind of ground on the floor, gentlest first. Where
  the floor has a gentler kind, the boots for the harsher one lie in a
  pocket of it, as the fire boots lay in the ice in Chip's building
  and the ice boots in the sand. The gentlest kind's boots lie near
  its edge, where the player can reach them without them."
  [state kinds]
  (reduce (fn [state ground]
            (let [boots (boots-for ground)
                  gentler (last (filter (fn [other] (< (harshness other) (harshness ground))) kinds))]
              (or (when gentler (boots-in-pocket state gentler boots))
                  (put state (pick! (vec (take 3 (edge-rooms state ground)))) boots))))
          state
          kinds))

(defn- lay-terrain
  "The ground: a mass of each kind the floor gets, toward the corners
  away from the player. A kind whose mass could not grow past a few
  rooms is taken up again, so no floor has boots for ground it does
  not have. Then the boots. A floor that could not be finished on foot
  is laid again, and after a few tries with one kind fewer, so every
  floor can be finished without walking through fire unshod."
  [state settings]
  (let [ground-settings (merge (:terrain default-book) (:terrain settings))
        from-start (distances-from state (:at state) adjoining)
        kinds (ground-kinds ground-settings (get-in state [:terms :floor] 1))]
    (loop [attempt 0]
      (let [trying (vec (drop-last (quot attempt 3) kinds))
            grown (lay-masses state trying (:share ground-settings 0.2) from-start attempt)
            present (filterv (fn [ground] (>= (apply max 0 (map count (patch-rooms grown ground))) smallest-mass))
                             trying)
            kept (assoc grown :terrain (into {} (filter (fn [[_ kind]] (some #{kind} present))
                                                        (:terrain grown))))
            laid (place-all-boots kept present)]
        (if (or (empty? trying) (finishable? laid))
          laid
          (recur (inc attempt)))))))

(defn- shortest-way
  "Roughly the fewest turns a floor can be done in: in each zone its keys
  and the warrant, nearest first, then its door, and at the end the way
  out. It only has to be fair, since all it measures is the quick bonus."
  [state doors zones]
  (let [distance (fn [from to] (get (distances-from state from adjoining) to 0))
        warrant (first (holding state :warrant))
        out (first (holding state :way-out))]
    (loop [here (:at state) index 0 total 0]
      (let [zone (nth zones index)
            stops (cond-> (vec (filter (fn [room] (= :key (fixture state room))) (sort zone)))
                    (contains? zone warrant) (conj warrant))
            [here total] (loop [here here total total stops stops]
                           (if (empty? stops)
                             [here total]
                             (let [nearest (first (sort-by (fn [stop] (distance here stop)) stops))]
                               (recur nearest
                                      (+ total (distance here nearest))
                                      (remove (fn [stop] (= stop nearest)) stops)))))]
        (if (< index (count doors))
          (let [door (nth doors index)]
            (recur door (inc index) (+ total (distance here door) 1)))
          (+ total (if out (distance here out) 0) 1))))))

(defn- plan-for
  "What is on the wall on a floor with these terms: as many house rules
  as the terms allow, in the order they are introduced, and the warrant
  always."
  [settings terms]
  (vec (remove nil? (conj (vec (take (:rules terms) (:house-rules settings)))
                          (:warrant-rule settings)))))

(defn- layout
  "A whole floor from a seed. Always returns one."
  [settings terms seed]
  (seed-die! seed)
  (let [{:keys [rows cols density]} (proportions settings)
        divided (building rows cols density (:locks terms))
        state {:rows rows :cols cols :plan (:plan divided) :pits #{} :terrain {}
               :held nil :held-at nil :keys 0 :shield nil :stuck 0 :boots #{} :burns 0
               :hp (:hp terms) :steps (:steps terms) :turn 0
               :reqs (plan-for settings terms)
               :amendments (vec (:amendments settings))
               :terms terms :entities [] :drops [] :dropped {}
               :powers {:vanquish 0 :teleport 0 :stun 0}
               :hits 0 :kills 0 :stunned 0 :banked 0
               :note nil :new-rule nil :over nil
               :seed seed :difficulty (difficulty-name settings)
               :events [] :record []}
        placed (-> state
                   (place-by-zone divided terms settings)
                   (furnish settings)
                   audit
                   (lay-terrain settings))
        special (:special terms)]
    (cond-> (assoc placed
                   :events []
                   :record []
                   :die @die
                   :locks (count (:doors divided))
                   :par (shortest-way placed (:doors divided) (:zones divided)))
      (and special (some (fn [entity] (= special (:kind entity))) (:entities placed)))
      (record {:by :floor :kind :arrived
               :line (str "A " (get entity-kind-names special) " entity is on this floor.")}))))

;; ---------------------------------------------------------------------
;; The score
;; ---------------------------------------------------------------------

(defn floor-points
  "What this floor is worth so far."
  [state]
  (let [scoring (get-in state [:terms :scoring])]
    (+ (get scoring :floor 0)
       (- (* (get scoring :step 0) (:turn state)))
       (- (* (get scoring :hit 0) (:hits state 0)))
       (* (get scoring :vanquish 0) (:kills state 0))
       (* (get scoring :stun 0) (:stunned state 0)))))

(defn- quick-bonus
  "All of `:quick` for the shortest way round, none of it at `:slow`
  times that, and in proportion between."
  [state]
  (let [{:keys [quick slow]} (get-in state [:terms :scoring])
        par (max 1 (or (:par state) 1))
        slow (max 1.1 (or slow 2))
        fraction (/ (- (* slow par) (:turn state)) (* (- slow 1) par))]
    (Math/round (* (or quick 0) (max 0 (min 1 fraction))))))

;; ---------------------------------------------------------------------
;; Turns
;; ---------------------------------------------------------------------

(defn- winnable?
  "Whether the floor can still be finished from where the player
  stands: every key in reach picked up and every locked door in reach
  opened, until nothing more can be, and then the warrant and the way
  out both in reach. Portals count, each the way it goes, which is how
  a one-way portal across a lock can make this false."
  [state]
  (let [sends (portal-map state)
        way-out (first (holding state :way-out))
        unlocked (fn [state room] (not (shut? state room)))]
    (loop [state state]
      (let [reach (reachable-from state (:at state) sends unlocked)
            state (reduce (fn [state room]
                            (-> state (assoc-in [:plan room] :empty) (update :keys (fnil inc 0))))
                          state
                          (filter reach (holding state :key)))
            door (first (filter (fn [door] (some reach (adjoining state door)))
                                (sort (holding state :door))))]
        (if (and door (pos? (:keys state 0)))
          (recur (-> state (assoc-in [:plan door] :doorway) (update :keys dec)))
          (let [reach (reachable-from state (:at state) sends unlocked)]
            (boolean (and (contains? reach way-out)
                          (or (:held state) (some reach (holding state :warrant)))))))))))

(defn- finished?
  [state]
  (cond
    (:over state) state
    (<= (:hp state) 0) (assoc state :over :spent)
    (and (:held state) (= :way-out (fixture state (:at state))))
    (assoc state
           :over :out
           :exit {:points (max 0 (floor-points state)) :quick (quick-bonus state)})
    (<= (:steps state) 0) (assoc state :over :late)
    (not (winnable? state)) (assoc state :over :stranded)
    :else state))

(defn- turn-passes
  "Everything that is not the player, once, in the order it happens:
  the entities move, the powerups age, the floor releases what it
  must, Zell writes if it is time, the audit repairs what it can,
  whatever has reached the player's room meets the player, and perhaps
  something appears."
  [state]
  (-> state
      (update :steps dec)
      move-entities
      age-drops
      tide
      zell
      audit
      contact
      drop-powerup
      finished?))

(defn- took-time?
  [before after]
  (or (not= (:at before) (:at after))
      (not= (:plan before) (:plan after))
      (not= (:keys before) (:keys after))
      (not= (:held before) (:held after))))

(defn act
  "One press. Anything that changes the floor costs a turn, and a turn is
  when everything else happens. A wall costs nothing, and says so, and
  so does a powerup, which is the point of one.

  The turn is counted before the press is carried out, so everything one
  press causes is recorded under one number."
  [state [verb target]]
  (let [state (assoc state :note nil)]
    (cond
      (:over state) state

      (some (fn [kind] (= kind verb)) power-kinds)
      (use-power state verb target)

      (pos? (:stuck state))
      (-> state
          (update :turn inc)
          (update :stuck dec)
          (record {:by :you :kind :stuck :room (:at state)
                   :line (if (= :stun (:held-by state)) "Still stunned." "Still caught.")})
          turn-passes)

      :else
      (let [ticking (update state :turn inc)
            after (case verb
                    :walk (step-into ticking (beside (:at ticking) target))
                    :push (push ticking target)
                    :take (take-warrant ticking)
                    ticking)
            after (if (= (:at ticking) (:at after))
                    after
                    (-> after arrive (tread target)))]
        (if-not (took-time? ticking after)
          (assoc after :turn (:turn state))
          (let [passed (turn-passes (dissoc after :slowed))]
            (if (and (:slowed after) (not (:over passed)))
              (-> passed
                  (update :turn inc)
                  (cond-> (not= :sand (terrain state (:at state)))
                    (record {:by :floor :kind :sand :room (:at passed)
                             :line "Sand. That step took two turns."}))
                  turn-passes)
              passed)))))))

(defn- quiet
  "The floor once it has been drawn. What moved has finished moving and
  what happened has been shown, so drawing it again plays nothing twice."
  [state]
  (-> state
      (assoc :was-at (:at state) :events [])
      (update :entities (fn [entities]
                        (mapv (fn [entity] (assoc entity :was (:at entity))) entities)))))

;; ---------------------------------------------------------------------
;; Sprites
;; ---------------------------------------------------------------------

(def sprite
  {:sand-boots  "<path d='M9 4h5v9l4 2v3H7v-3l2-2z'/><path d='M7 21h1M10 21h1M13 21h1M16 21h1'/>"
   :ice-boots   "<path d='M9 4h5v9l4 2v3H7v-3l2-2z'/><path d='M6 21h13M17 21l1.5-1.5'/>"
   :fire-boots  "<path d='M9 4h5v9l4 2v3H7v-3l2-2z'/><path d='M17 9c-1.2-1-1.2-2.2 0-3.4 1.2 1.2 1.2 2.4 0 3.4z'/>"
   :door        "<path d='M6 20V6h9v14'/><circle cx='12.8' cy='13' r='1'/>"
   :doorway     "<path d='M6 20V6h12v14'/><path d='M6 6l5 2.5v12l-5-0.5'/>"
   :key         "<circle cx='8' cy='8' r='3'/><path d='M10.2 10.2L19 19M16 16l2-2M13.5 13.5l1.5-1.5'/>"
   :box         "<path d='M5 7h14v12H5zM5 10.5h14M12 7v3.5'/>"
   :pole        "<path d='M12 4v13M8 20h8'/>"
   :cabinet     "<path d='M5 5h14v14H5zM5 12h14M9 8.5h2M9 15.5h2'/>"
   :copier      "<path d='M4 9h16v8H4zM7 9V6h10v3M7 13h10'/>"
   :disturbance "<path d='M4 8c2-2 4 2 6 0s4 2 6 0 3 1 4 1M4 14c2-2 4 2 6 0s4 2 6 0 3 1 4 1M4 20c2-2 4 2 6 0s4 2 6 0 3 1 4 1'/>"
   :bear-trap   "<path d='M4 12h16M6 12l1.5-3M10 12l1.5-3M14 12l1.5-3M18 12l1.5-3M6 12l1.5 3M10 12l1.5 3M14 12l1.5 3M18 12l1.5 3'/>"
   :shield      "<path d='M12 3l7 3v6c0 4-3 7-7 9-4-2-7-5-7-9V6z'/>"
   :warrant     "<path d='M7 4h10v16H7zM10 8h4M10 10.5h4M10 13h2.5'/><circle cx='14' cy='16.5' r='1.5'/>"
   :way-out     "<path d='M6 20V10.5a6 6 0 0 1 12 0V20M12 20v-5'/>"
   :portal-a    "<path d='M12 4c4 0 6 3.5 6 8s-2 8-6 8-6-3.5-6-8 2-8 6-8z'/><circle cx='12' cy='12' r='2'/>"
   :portal-b    "<path d='M12 4c4 0 6 3.5 6 8s-2 8-6 8-6-3.5-6-8 2-8 6-8z'/><circle cx='12' cy='12' r='2'/>"
   :portal-in   "<path d='M12 4c4 0 6 3.5 6 8s-2 8-6 8-6-3.5-6-8 2-8 6-8z'/><path d='M8 12h7M12 9l3 3-3 3'/>"
   :portal-out  "<path d='M12 4c4 0 6 3.5 6 8s-2 8-6 8-6-3.5-6-8 2-8 6-8z'/><path d='M16 12H9M13 9l-3 3 3 3'/>"
   :vanquish    "<path d='M12 3l2.4 6.6L21 12l-6.6 2.4L12 21l-2.4-6.6L3 12l6.6-2.4z'/>"
   :teleport    "<circle cx='12' cy='12' r='8' stroke-dasharray='2.5 3'/><circle cx='12' cy='12' r='2.5'/>"
   :stun        "<circle cx='12' cy='12' r='2.5'/><path d='M12 3v3.5M12 17.5V21M3 12h3.5M17.5 12H21M5.6 5.6l2.5 2.5M15.9 15.9l2.5 2.5M5.6 18.4l2.5-2.5M15.9 8.1l2.5-2.5'/>"})

(def player-sprite
  "<circle cx='12' cy='7.5' r='3'/><path d='M6.5 20.5c0-3.4 2.5-5.6 5.5-5.6s5.5 2.2 5.5 5.6'/>")

(def glitch-pools
  "What an entity is made of from one moment to the next: the letters of
  its name, and the marks a document leaves when it goes wrong. The
  special ones draw on marks of their own."
  {:entity  "ENTYΞΣΨЖЯ#%@§¶*+=/?▓▒░"
   :fast    "»›→⇢≫~≈ENTY/"
   :strong  "█▓▒■▣◼ΞЖЯENTY"
   :thrower "*✶✳✴⁂+•°ENTY"})

(def glitch-words
  "What an entity now and then almost says."
  ["ENT" "TTY" "ERR" "NUL" "N/A" "§§§"])

(defn- glitch-text
  [length pool]
  (apply str (repeatedly length (fn [] (rand-nth pool)))))

(defn- flicker
  "An entity's text a moment later: now and then a word, otherwise the
  same letters with about a third of them changed."
  [text pool]
  (if (< (rand) 0.06)
    (let [word (rand-nth glitch-words)]
      (subs word 0 (min (count word) (max 1 (count text)))))
    (apply str (map (fn [glyph] (if (< (rand) 0.35) (rand-nth pool) glyph)) text))))

(defn- entity-html
  "An entity: `length` letters that will not hold still, from the marks
  its `kind` is made of, dealt at random here and changed a few times a
  second by the widget's one timer. The floor's own die is never
  touched."
  [length classes kind]
  (let [kind (or kind :entity)
        text (glitch-text length (get glitch-pools kind))]
    (str "<span class='" (str/join " " (concat ["maze-entity" (str "is-" (name kind))] classes)) "'"
         " data-kind='" (name kind) "' data-text='" text "'>" text "</span>")))

(defn- svg
  [class-name inner]
  (str "<svg viewBox='0 0 24 24' class='" class-name "' aria-hidden='true'>" inner "</svg>"))

(defn- fixture-svg
  ([kind] (fixture-svg kind nil))
  ([kind extra]
   (svg (str "maze-sprite maze-" (name kind) (when extra (str " " extra)))
        (get sprite kind ""))))

;; ---------------------------------------------------------------------
;; Drawing the floor
;; ---------------------------------------------------------------------

(def walk-reach
  "How far away something can have been and still be drawn sliding in:
  two rooms, the most anything walks in a turn. Anything further came by
  portal or teleport, and simply arrives."
  2)

(defn- slide-style
  "The offset a sprite slides from, as CSS variables. The grid is drawn
  afresh every turn, so movement is shown by drawing each sprite where
  it is now and sliding it home from where it was, with nothing but
  CSS. Only from `reach` rooms away or nearer: further than that is a
  portal or a teleport, and those are arrived at rather than slid to."
  [from to reach delay]
  (when (and from (not= from to))
    (let [[from-row from-col] from
          [to-row to-col] to
          rows-apart (- from-row to-row)
          cols-apart (- from-col to-col)
          apart (+ (magnitude rows-apart) (magnitude cols-apart))
          base-ms 120      ; any slide, before its length is counted
          ms-per-room 50]  ; and this much longer for each room it crosses
      (when (<= apart reach)
        (str " style='--dr:" rows-apart ";--dc:" cols-apart
             ";--slide:" (+ base-ms (* ms-per-room apart)) "ms;--delay:" delay "ms'")))))

(defn- mover
  [inner style arriving?]
  (str "<span class='maze-mover"
       (cond style " is-sliding" arriving? " is-arriving" :else "")
       "'" style ">" inner "</span>"))

(defn- count-label
  [n extra]
  (str "<span class='maze-count" extra "'>" n "</span>"))

(defn- wrong-rooms
  "Rooms holding something a broken, repairable Requirement is about.
  The audit has already failed to fix these, which is worth seeing."
  [state]
  (set (for [{rule :keep a :a b :b} (filter repairable? (unmet state))
             room (concat (holding state a)
                          (when b (holding state b))
                          (when (= :clear rule)
                            (for [room-of-a (holding state a)
                                  next-door (adjoining state room-of-a)
                                  :when (fixture state next-door)]
                              next-door)))]
         room)))

(defn- what-was-seen
  "Everything the drawing needs from this turn, worked out once.
  `pointing` is the set of kinds the reader is pointing at, on the wall
  or in the key, and `aiming` the powerup being aimed, if any."
  [state pointing aiming]
  (let [events (:events state)
        of-kind (fn [kinds] (filter (fn [event] (contains? kinds (:kind event))) events))
        next-turn (update state :turn inc)
        rearrange-reach 99       ; further than rooms lie apart, so moved furniture always slides
        rearrange-delay-ms 170]  ; the beat the building waits after the player moves
    {:arrivals (into {} (for [{:keys [kind from to]} (of-kind #{:moved :pushed})]
                          [to (if (= :moved kind)
                                {:from from :reach rearrange-reach :delay rearrange-delay-ms}
                                {:from from :reach longest-slide :delay 0})]))
     :rearranged (set (map :to (of-kind #{:moved})))
     :vanished (into {} (for [{:keys [room fixture]} (of-kind #{:gone})]
                          [room fixture]))
     :ended (set (map :room (of-kind #{:ended})))
     :wounded (set (map :room (of-kind #{:wounded})))
     :released (set (map :room (of-kind #{:released :fell})))
     :stunned (set (concat (mapcat :rooms (of-kind #{:stunned}))
                           (map :room (of-kind #{:stunned-you}))))
     :stunning (set (map :room (of-kind #{:stunned})))
     :throwing (set (keep :source events))
     :appeared (set (map :room (of-kind #{:dropped})))
     :player-reach (if (and (seq (of-kind #{:slid})) (empty? (of-kind #{:through}))) longest-slide walk-reach)
     :lit (or pointing #{})
     :targets (set (case aiming
                     :vanquish (vanquish-targets state)
                     :teleport (teleport-targets state)
                     []))
     :wrong (wrong-rooms state)
     :going (when (and (:held state) (not (:over state)))
              (when (get-in state [:terms :release]) (next-to-go state)))
     :going-now? (tide-due? next-turn)
     :restless? (and (not (:over state)) (entities-move? next-turn))}))

(defn- wall-classes
  "The sides of a room that face no room, where the building's walls are
  drawn."
  [state [row col]]
  (for [[side neighbour] [["n" [(dec row) col]] ["e" [row (inc col)]]
                          ["s" [(inc row) col]] ["w" [row (dec col)]]]
        :when (not (room? state neighbour))]
    (str "wall-" side)))

(defn- lit?
  "Whether a room holds something the reader is pointing at, on the
  wall or in the key: a fixture, the ground, a powerup lying there, an
  entity of a kind pointed at, or the player."
  [seen state room kind lying present here?]
  (let [lit (:lit seen)]
    (boolean (or (contains? lit kind)
                 (contains? lit (terrain state room))
                 (and lying (contains? lit (:kind lying)))
                 (some (fn [entity] (contains? lit (or (:kind entity) :entity))) present)
                 (and here? (contains? lit :you))))))

(defn- jitter
  "Small offsets that differ from room to room, so a large stretch of
  ground does not show the same tile over and over and its fire does not
  flicker in step."
  [[row col]]
  (str "--jitter-x:" (* 2 (mod (+ (* 3 col) (* 5 row)) 8)) "px;"
       "--jitter-y:" (* 2 (mod (+ (* 3 row) (* 7 col)) 8)) "px;"
       "--jitter-t:-" (* 150 (mod (+ (* 5 row) (* 3 col)) 8)) "ms"))

(defn- cell-html
  [state seen room]
  (let [[row col] room]
    (cond
      (pit? state room)
      (str "<div class='maze-pit"
           (when (contains? (:released seen) room) " is-fresh")
           (when (contains? (:lit seen) :released) " is-lit")
           "'></div>")

      (not (room? state room))
      "<div class='maze-void'></div>"

      :else
      (let [kind (fixture state room)
            ground (terrain state room)
            here? (= room (:at state))
            arrival (get (:arrivals seen) room)
            gone (get (:vanished seen) room)
            lying (first (filter (fn [powerup] (= room (:at powerup))) (:drops state)))
            present (filter (fn [entity] (= room (:at entity))) (:entities state))
            classes (cond-> (into ["maze-room"] (wall-classes state room))
                      ground (conj (str "terrain-" (name ground)))
                      (= room (:going seen)) (conj "is-going")
                      (and (= room (:going seen)) (:going-now? seen)) (conj "is-going-now")
                      (lit? seen state room kind lying present here?) (conj "is-lit")
                      (contains? (:wrong seen) room) (conj "is-wrong")
                      (and (= :way-out kind) (:held state)) (conj "is-open-way")
                      (and (= :door kind) (pos? (:keys state 0))) (conj "is-ready-door")
                      (contains? (:rearranged seen) room) (conj "is-rearranged")
                      (or (contains? (:ended seen) room) (contains? (:wounded seen) room)) (conj "is-struck")
                      (contains? (:stunned seen) room) (conj "is-stunned")
                      (contains? (:stunning seen) room) (conj "is-stunning")
                      (contains? (:throwing seen) room) (conj "is-throwing")
                      (contains? (:targets seen) room) (conj "is-target")
                      here? (conj "is-here"))]
        (str "<div class='" (str/join " " classes) "' data-row='" row "' data-col='" col "'"
             (when ground (str " style='" (jitter room) "'"))
             ">"
             (when kind
               (if-let [style (when arrival
                                (slide-style (:from arrival) room (:reach arrival) (:delay arrival)))]
                 (mover (fixture-svg kind) style false)
                 (fixture-svg kind)))
             (when lying
               (str (mover (fixture-svg (:kind lying) "maze-drop")
                           nil
                           (contains? (:appeared seen) room))
                    (count-label (:left lying) "")))
             (when gone
               (fixture-svg gone "maze-ghost"))
             (when (contains? (:ended seen) room)
               (entity-html 3 ["maze-ghost"] nil))
             (str/join ""
                       (for [entity present
                             :let [was (:was entity)
                                   style (slide-style was room walk-reach 0)
                                   held (:stuck entity 0)
                                   classes (cond-> []
                                             (:restless? seen) (conj "is-restless")
                                             (pos? held) (conj "is-held")
                                             (pos? (:wounds entity 0)) (conj "is-wounded"))]]
                         (str (mover (entity-html 3 classes (:kind entity))
                                     style
                                     (and was (not= was room) (nil? style)))
                              (when (pos? held) (count-label held " is-held"))
                              (when-let [power (:power entity)]
                                (fixture-svg power "maze-held-power")))))
             (when here?
               (let [was (:was-at state)
                     style (slide-style was room (:player-reach seen) 0)]
                 (mover (svg "maze-you" player-sprite)
                        style
                        (and was (not= was room) (nil? style)))))
             "</div>")))))

(defn- grid-html
  [state pointing aiming]
  (let [seen (what-was-seen state pointing aiming)]
    (str/join "" (for [row (range (:rows state))
                       col (range (:cols state))]
                   (cell-html state seen [row col])))))

;; ---------------------------------------------------------------------
;; Drawing everything else
;; ---------------------------------------------------------------------

(defn- pips
  [left most]
  (str "<span class='maze-pips' role='img' aria-label='" (max 0 left) " of " most " hits left'>"
       (str/join "" (for [index (range most)]
                      (str "<span class='maze-pip" (when (< index left) " is-full") "'></span>")))
       "</span>"))

(defn- metric
  [label-text value]
  (str "<span class='maze-metric'><span class='maze-metric-name'>" label-text "</span>"
       "<span class='maze-metric-value'>" value "</span></span>"))

(defn- best-kept
  []
  (let [kept (reader/stored storage-key)]
    (if (map? kept) kept {})))

(defn- best-for
  [difficulty]
  (let [kept (best-kept)]
    (or (get kept difficulty) (get kept (word difficulty)) 0)))

(defn- keep-best!
  [difficulty score]
  (when (> score (best-for difficulty))
    (reader/store! storage-key (assoc (best-kept) difficulty score))))

(defn- this-floor
  [state]
  (if (= :out (:over state))
    (+ (get-in state [:exit :points] 0) (get-in state [:exit :quick] 0))
    (max 0 (floor-points state))))

(defn- bar-html
  "The numbers that matter, over the floor."
  [state]
  (let [{:keys [hp steps floor banked terms difficulty burns]} state
        per-hit (get-in terms [:terrain :burns-per-hit] 3)]
    (str "<div class='maze-bar-part'>" (pips hp (:hp terms))
         (when (pos? (or burns 0))
           (str "<span class='maze-singed'>singed " burns " of " per-hit "</span>"))
         "<span class='maze-steps'>" steps " steps left</span></div>"
         "<div class='maze-bar-part'>"
         (metric "Floor" floor)
         (metric "This floor" (this-floor state))
         (metric "Banked" banked)
         (metric "Best" (best-for difficulty))
         "</div>")))

(defn- turns-until
  [turn every]
  (- every (mod turn every)))

(defn- clock
  [what turns]
  (str "<div class='maze-clock" (when (= 1 turns) " is-next") "'>"
       what (if (= 1 turns) " next turn" (str " in " turns " turns")) "</div>"))

(defn- hands
  [state]
  (let [held-keys (:keys state 0)
        carried (cond-> []
                  (pos? held-keys) (conj (if (= 1 held-keys) "a key" (str held-keys " keys")))
                  (:shield state) (conj (if (shield-hot? state)
                                          (str "the shield, ending entities for "
                                               (- (get-in state [:terms :shield-turns] 3)
                                                  (- (:turn state) (:shield state)))
                                               " more turns")
                                          "the shield"))
                  (:held state) (conj "the warrant"))
        worn (keep (fn [boots] (when (contains? (:boots state) boots) (label-of boots)))
                   [:fire-boots :ice-boots :sand-boots])]
    (str (cond
           (pos? (:stuck state))
           (str (if (= :stun (:held-by state))
                  (str "Stunned for " (turns-word (:stuck state)))
                  "Caught in a trap")
                (when (seq carried) (str ", holding " (str/join ", " carried)))
                ".")
           (seq carried) (str "Holding " (str/join ", " carried) ".")
           :else "Holding nothing.")
         (when (seq worn) (str " Wearing " (str/join " and " worn) ".")))))

(defn- over-line
  [state]
  (case (:over state)
    :spent "Out of hits. Press n for a new run, or r to try this floor again."
    :late "Out of steps. Press n for a new run, or r to try this floor again."
    :closed "The floor closed around you. Press n for a new run."
    :stranded "Stranded. Nothing on this side opens the way back. Press r to try this floor again, or n for a new run."
    :out (str "Out with the warrant. " (get-in state [:exit :points] 0) " for the floor, "
              (get-in state [:exit :quick] 0) " for speed.")
    ""))

(defn- status-html
  [state]
  (let [{:keys [turn terms entities over held held-at]} state
        {:keys [pace writes release]} terms]
    (str "<div class='maze-hands'>" (hands state) "</div>"
         (when-not over
           (str (when (seq entities) (clock "Entities move" (turns-until turn pace)))
                (when writes (clock "New requirement" (turns-until turn writes)))
                (when (and held release)
                  (clock "Next room released" (turns-until (- turn held-at) release)))))
         (when over
           (str "<div class='maze-over is-" (name over) "'>" (over-line state) "</div>")))))

(defn- powers-html
  "Held powerups, as buttons, and what to do while one is being aimed."
  [state aiming]
  (str (str/join ""
                 (for [kind power-kinds
                       :let [held (get-in state [:powers kind] 0)]
                       :when (pos? held)]
                   (str "<button class='roll-go maze-power" (when (= kind aiming) " is-on") "'"
                        " data-power='" (name kind) "'>"
                        (fixture-svg kind)
                        (capitalized (label-of kind))
                        (when (> held 1) (str " ×" held))
                        " (" (get power-keys kind) ")</button>")))
       (case aiming
         :vanquish "<p class='maze-hint'>Click an entity, or press 1 again for the nearest. Esc cancels.</p>"
         :teleport "<p class='maze-hint'>Click a lit room, or press an arrow to go as far as you can that way. Esc cancels.</p>"
         nil)))

(defn- record-html
  [state]
  (let [this-turn (set (:events state))]
    (str (str/join ""
                   (for [{:keys [turn by line] :as event} (:record state)]
                     (str "<p class='maze-line by-" (word by)
                          (when (contains? this-turn event) " is-now") "'>"
                          "<span class='maze-turn'>" turn "</span>" line "</p>")))
         (when-let [line (:note state)]
           (str "<p class='maze-remark'>" line "</p>")))))

(defn- requirements-html
  [state]
  (let [broken (set (unmet state))
        written (some (fn [event] (when (= :wrote (:kind event)) (:rule event)))
                      (:events state))]
    (str/join ""
              (for [{a :a b :b :as requirement} (:reqs state)]
                (str "<div class='maze-req"
                     (cond
                       (and (contains? broken requirement) (repairable? requirement)) " is-unmet"
                       (contains? broken requirement) " is-broken"
                       (= requirement (:new-rule state)) " is-new"
                       :else "")
                     (when (= requirement written) " is-written")
                     "'"
                     (when a (str " data-a='" (word a) "'"))
                     (when b (str " data-b='" (word b) "'"))
                     ">" (capitalized (sentence requirement)) ".</div>")))))

(def key-entries
  "Every entry the key can show, in the order it shows them: the player
  and the entities; the job; what costs the player; the ground and its
  boots; what helps the player; what moves the player; and the
  building. Alike things sit together, and need no labels to say so."
  [:you :entity :fast :strong :thrower
   :warrant :way-out :key :door :doorway
   :disturbance :cabinet :copier :bear-trap :fire
   :ice :sand :fire-boots :ice-boots :sand-boots
   :shield :vanquish :teleport :stun
   :portal-a :portal-in :portal-out
   :box :pole :released])

(def entity-kinds
  #{:entity :fast :strong :thrower})

(defn- entry-lights
  "The kinds of room an entry in the key lights on the floor."
  [entry]
  (cond
    (nil? entry) nil
    (= :portal-a entry) #{:portal-a :portal-b}
    :else #{entry}))

(defn- entry-name
  [entry]
  (case entry
    :you "You"
    :entity "Entity"
    :released "Released"
    (:fast :strong :thrower) (str (capitalized (get entity-kind-names entry)) " entity")
    (capitalized (label-of entry))))

(defn- entry-picture
  [entry]
  (cond
    (= :you entry) (svg "maze-you" player-sprite)
    (contains? entity-kinds entry) (entity-html 1 [] entry)
    (= :released entry) "<span class='maze-legend-pit'></span>"
    (contains? boots-for entry) (str "<span class='maze-swatch terrain-" (name entry) "'></span>")
    :else (fixture-svg entry)))

(defn- entry-note
  "What an entry does, in full, for the window."
  [entry terms]
  (let [{:keys [pace release shield-turns trap-holds trap-holds-you powerups entity-powerups specials]} terms
        {:keys [lasts vanquish-range teleport-range stun-radius stun-turns]} powerups
        per-hit (get-in terms [:terrain :burns-per-hit] 3)
        how-often (case pace 1 "every turn" 2 "every other turn" (str "every " pace " turns"))
        fades (str " If nobody picks it up, it fades after " (turns-word lasts) "."
                   (when entity-powerups " Entities can pick it up too."))]
    (case entry
      :you "Arrows or a click to walk. Shift and an arrow pushes what is ahead of you, or pulls what is behind you. Space to take. 1, 2 and 3 use powerups."
      :entity (str "Entities come for you " how-often ". Each one that reaches you costs you a hit. Entities cannot open locked doors or push boxes, and they go round fire when they can."
                   (when entity-powerups
                     (str " On this floor they take any powerup nearer to them than you are, and use it on you:"
                          " a vanquish costs you a hit from up to " vanquish-range " rooms away, a stun holds you for "
                          (turns-word (entity-stun-turns terms)) ", and a teleport brings them nearer.")))
      :fast (str "Comes for you " how-often ", two rooms at a time, one step after the other, so it cannot pass a wall any more than the others can.")
      :strong "Takes two blows to end: a hot shield and a vanquish, or two of either. The first leaves it wounded, and breaks its ring."
      :thrower (str "Throws something at you whenever you are within " (get specials :throw-range 2)
                    " rooms of it, walls or no walls, and then has to move before it can throw again. A throw costs you a hit, unless a shield takes it.")
      :warrant (str "What you came for. Space takes it."
                    (when release
                      (str " From then on the building releases a room every " (turns-word release)
                           ", so leave it for last if you can.")))
      :way-out "Walk in carrying the warrant to finish the floor."
      :key "Opens one locked door. Walk into it to pick it up."
      :door "Locked. Walk into it holding a key to open it. Entities cannot get through until it is open."
      :doorway "An unlocked door. Anyone can come through now, entities included."
      :disturbance "Costs you a hit if you walk in, unless a shield takes it. You can push or pull it."
      (:cabinet :copier) "Costs you a hit if you walk in. You can push or pull it."
      :bear-trap (str "Holds whoever steps in: you for " (turns-word trap-holds-you)
                      ", an entity for " (turns-word trap-holds) ". Entities walk straight into traps. You can push or pull it.")
      :fire (str "Every step into fire singes you, and " per-hit " singes cost a hit. It burns entities the same way. Fire boots make it ordinary floor to you.")
      :ice "Step onto ice and you slide the way you were going until the ice ends or something stops you: a wall, a door, or furniture, which you stop just short of. Step onto furniture on the ice and it holds you still. Entities slide too, and so does anything you push onto it. Ice boots let you walk on it."
      :sand "Every step into sand takes two turns, so everything else gets two turns to your one. Entities cross it at their usual pace. Sand boots make it ordinary floor to you."
      :fire-boots "Walk into them to put them on. Fire is ordinary floor to you for the rest of this floor."
      :ice-boots "Walk into them to put them on. You walk on ice instead of sliding, for the rest of this floor."
      :sand-boots "Walk into them to put them on. Sand is ordinary floor to you for the rest of this floor."
      :shield (str "Takes one hit for you. For " (turns-word shield-turns)
                   " after you pick it up, it ends any entity it meets.")
      :vanquish (str "Press 1 to end an entity within " vanquish-range " rooms. A strong one takes two blows. It takes no turn." fades)
      :teleport (str "Press 2, then click a lit room or press an arrow, to jump up to " teleport-range
                     " rooms. Never past a locked door. It takes no turn." fades)
      :stun (str "Press 3 to stop every entity within " stun-radius " rooms for " (turns-word stun-turns)
                 ". It takes no turn." fades)
      :portal-a "Step into either end to come out of the other, even past a lock. Entities use portals too."
      :portal-in "Step in to come out at its exit, which may be past a lock. It only goes one way: take it without the key or the warrant you need over there, and there is no way back."
      :portal-out "Where the one-way portal lets out. It does not go back."
      :box "Shift and an arrow pushes it, or pulls it after you. Entities cannot move it, so they go round. On ice it can only be pushed, and it slides until something stops it."
      :pole "Furniture that will not move. On ice it stops a slide like a wall, and stepping onto it holds you still."
      :released "Gone. Nobody can stand here. While you carry the warrant, the striped room goes next."
      "")))

(defn- entry-count
  "How many of an entry the floor holds."
  [state entry]
  (cond
    (= :you entry) 1
    (contains? entity-kinds entry)
    (count (filter (fn [entity] (= entry (or (:kind entity) :entity))) (:entities state)))
    (= :released entry) (count (:pits state))
    (contains? boots-for entry)
    (count (filter (fn [[room ground]] (and (= ground entry) (room? state room))) (:terrain state)))
    :else
    (let [lights (entry-lights entry)]
      (+ (count (filter (fn [held] (contains? lights held)) (vals (:plan state))))
         (count (filter (fn [lying] (= entry (:kind lying))) (:drops state)))))))

(defn- entry-present?
  [state entry]
  (or (pos? (entry-count state entry))
      (pos? (get-in state [:powers entry] 0))
      (contains? (:boots state) entry)))

(defn- count-words
  [state entry]
  (let [on-floor (entry-count state entry)
        in-hand (get-in state [:powers entry] 0)
        armed (if (contains? entity-kinds entry)
                (count (filter (fn [entity] (and (:power entity) (= entry (or (:kind entity) :entity))))
                               (:entities state)))
                0)]
    (str (if (pos? on-floor) (str on-floor " on this floor") "None on this floor")
         (when (pos? in-hand) (str ", " in-hand " in hand"))
         (when (contains? (:boots state) entry) ", and you are wearing them")
         (when (pos? armed) (str ", " armed " holding a powerup"))
         ".")))

(defn- legend-html
  "The key as one table, alike things next to one another, holding only
  what is on this floor or in the player's hands, because a key to
  things that are not there is a list of worries. The entry chosen is
  marked."
  [state chosen]
  (str/join ""
            (for [entry key-entries
                  :when (entry-present? state entry)
                  :let [chosen? (= entry chosen)]]
              (str "<button type='button' class='maze-legend-item" (when chosen? " is-selected")
                   "' data-entry='" (name entry) "' aria-pressed='" chosen? "'>"
                   "<span class='maze-legend-box'>" (entry-picture entry) "</span>"
                   "<span class='maze-legend-name'>" (entry-name entry) "</span>"
                   "</button>"))))

(defn- about-layer
  "One description in the window, or the hint when `entry` is nil."
  [state entry showing?]
  (str "<div class='maze-about-layer" (when showing? " is-showing") "'>"
       (if entry
         (str "<div class='maze-about-head'><span class='maze-legend-box'>" (entry-picture entry) "</span>"
              "<span class='maze-about-name'>" (entry-name entry) "</span></div>"
              "<p class='maze-about-note'>" (entry-note entry (:terms state)) "</p>"
              (when-not (= :you entry)
                (str "<p class='maze-about-count'>" (count-words state entry) "</p>")))
         "<p class='maze-about-hint'>Open the key above and select anything to see what it does and where it is.</p>")
       "</div>"))

(defn- about-html
  "The window beside the Requirements. Every description is in it,
  stacked in one place, and only the one pointed at or chosen is shown,
  so the window is always as tall as the longest and never changes
  size."
  [state shown]
  (str (about-layer state nil (nil? shown))
       (str/join "" (for [entry key-entries]
                      (about-layer state entry (= entry shown))))))

(defn- header-line
  [state]
  (str "floor " (:floor state) " · banked " (:banked state)
       " · best " (best-for (:difficulty state))))

(defn- score-line
  [state]
  (str (word (:difficulty state)) " · seed " (:seed state)))

(defn- can-take?
  [state]
  (and (not (:over state)) (= :warrant (fixture state (:at state)))))

;; ---------------------------------------------------------------------
;; State
;; ---------------------------------------------------------------------

(defonce game (atom nil))
(defonce start-of-floor (atom nil))
(defonce run (atom 0))
(defonce pushing (atom false))
(defonce aiming (atom nil))
(defonce leaving (atom nil))
(defonce pointing (atom nil))
(defonce glitch-timer (atom nil))
(defonce hovering (atom nil))
(defonce selected (atom nil))

(defn- layout!
  "A floor on the settings as they stand, keeping what is banked."
  [floor banked hp]
  (let [settings @book
        terms (terms-for settings floor)
        laid (-> (layout settings terms (seed-for settings @run floor))
                 (assoc :floor floor
                        :banked banked
                        :hp (min (:hp terms) (or hp (:hp terms)))))]
    (reset! start-of-floor laid)
    (reset! game laid)))

(defn- new-run!
  []
  (reset! run 0)
  (layout! 1 0 nil))

(defn- advance!
  "Out, with it. The floor is banked, and the next one laid out, with one
  hit back."
  []
  (let [{:keys [floor banked hp difficulty exit]} @game
        banked (+ banked (:points exit 0) (:quick exit 0))]
    (keep-best! difficulty banked)
    (layout! (inc floor) banked (inc hp))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn- paint-grid!
  [node]
  (let [state @game]
    (wui/grid! node ".maze-grid" (:cols state)
                                (grid-html state (or @pointing (entry-lights @selected)) @aiming))))

(defn- mark!
  "Classes on the widget itself. The hurt flash has two names, one for
  odd turns and one for even, so that two hurts in a row flash twice."
  [node state]
  (let [classes (.-classList node)
        hurt? (some (fn [event] (= :hurt (:kind event))) (:events state))
        over (:over state)]
    (.remove classes "maze-hurt-a" "maze-hurt-b")
    (when hurt?
      (.add classes (if (even? (:turn state)) "maze-hurt-a" "maze-hurt-b")))
    (.toggle classes "maze-is-over" (boolean (and over (not= :out over))))
    (.toggle classes "maze-is-out" (= :out over))
    (.toggle classes "maze-is-aiming" (some? @aiming))
    (when-let [button (reader/q node ".maze-push")]
      (.toggle (.-classList button) "is-on" (boolean @pushing)))))

(defn paint!
  [node]
  (let [state @game]
    (paint-grid! node)
    (wui/html! node ".maze-bar" (bar-html state))
    (wui/html! node ".maze-status" (status-html state))
    (wui/html! node ".maze-powers" (powers-html state @aiming))
    (wui/html! node ".maze-record" (record-html state))
    (wui/html! node ".maze-reqs" (requirements-html state))
    (wui/html! node ".maze-legend" (legend-html state @selected))
    (wui/html! node ".maze-about" (about-html state (or @hovering @selected)))
    (reader/text! node ".maze-score" (score-line state))
    (wui/state! node (header-line state))
    (reader/enable! node ".maze-take" (can-take? state) "Take (space)")
    (reader/enable! node ".maze-push" (not (:over state))
                            (if @pushing "Pushing: pick a room" "Push (shift)"))
    (mark! node state)))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- settle!
  "Draw the floor, then let it settle, so that what just happened is
  played once."
  [node]
  (paint! node)
  (swap! game quiet))

(defn- cancel-leaving!
  []
  (when-let [timer @leaving]
    (js/clearTimeout timer)
    (reset! leaving nil)))

(defn- hurry-out!
  "The next floor now, for a reader who does not want to watch the last
  one go."
  [node]
  (cancel-leaving!)
  (advance!)
  (settle! node))

(defn- leave!
  [node]
  (let [out-beat 900]  ; how long the floor is shown being left before the next one is laid
    (settle! node)
    (reset! leaving (js/setTimeout (fn []
                                     (reset! leaving nil)
                                     (advance!)
                                     (settle! node))
                                   out-beat))))

(defn- act!
  [node action]
  (if @leaving
    (hurry-out! node)
    (do (reset! aiming nil)
        (swap! game act action)
        (reset! pushing false)
        (let [state @game]
          (cond
            (= :out (:over state)) (leave! node)
            (:over state) (do (keep-best! (:difficulty state) (:banked state))
                              (settle! node))
            :else (settle! node))))))

(defn- restart!
  [node]
  (if @leaving
    (hurry-out! node)
    (do (reset! pushing false)
        (reset! aiming nil)
        (reset! game @start-of-floor)
        (settle! node))))

(defn- again!
  "Another floor. After a run has ended, that is the first floor of a new
  one; during a run, it is this floor in a different building."
  [node]
  (if @leaving
    (hurry-out! node)
    (do (reset! pushing false)
        (reset! aiming nil)
        (if (:over @game)
          (new-run!)
          (do (swap! run inc)
              (layout! (:floor @game) (:banked @game) (:hp @game))))
        (settle! node))))

(defn- power!
  "A powerup's key or button. Stun goes off at once. Vanquish goes off at
  once when there is only one thing it could mean, and otherwise waits
  to be aimed. Teleport always waits to be aimed."
  [node kind]
  (let [state @game]
    (cond
      @leaving (hurry-out! node)
      (:over state) nil
      (not (pos? (get-in state [:powers kind] 0))) nil
      (= :stun kind) (act! node [:stun nil])
      (= :vanquish kind)
      (let [targets (vanquish-targets state)]
        (if (or (= :vanquish @aiming) (<= (count targets) 1))
          (act! node [:vanquish (when (= 1 (count targets)) (first targets))])
          (do (reset! aiming :vanquish)
              (paint! node))))
      :else
      (do (reset! aiming (when-not (= :teleport @aiming) :teleport))
          (paint! node)))))

(defn- blink-target
  "The furthest room the player could teleport to in a straight line
  that way."
  [state direction]
  (let [targets (set (teleport-targets state))
        reach (get-in state [:terms :powerups :teleport-range] 4)]
    (some (fn [distance]
            (let [room (nth (iterate (fn [room] (beside room direction)) (:at state)) distance)]
              (when (contains? targets room) room)))
          (range reach 0 -1))))

(def keymap
  {"ArrowUp" :north "w" :north "W" :north
   "ArrowDown" :south "s" :south "S" :south
   "ArrowLeft" :west "a" :west "A" :west
   "ArrowRight" :east "d" :east "D" :east
   " " :take "r" :restart "R" :restart "n" :again "N" :again
   "1" :vanquish "2" :teleport "3" :stun "Escape" :cancel})

(defn- typing?
  "Whether a key was meant for a field, such as the settings above the
  floor. A reader typing `w` into the settings has not asked to walk."
  [target]
  (boolean (and target
                (.-closest target)
                (.closest target "input, textarea, select, [contenteditable]:not([contenteditable='false'])"))))

(defn- first-step
  "The direction of the first step from where the player stands toward
  `room`, going round what costs the player and the ground the player
  has no boots for when there is a way round, through a portal when
  that is shorter, through a locked door only with a key, and never
  into a one-way portal unless it is the room asked for. Nil when
  there is no way."
  [state room]
  (let [here (:at state)
        sends (portal-map state)
        costly #{:cabinet :copier :disturbance :bear-trap}
        open-to-player (fn [state next-door]
                      (and (or (pos? (:keys state 0)) (not (shut? state next-door)))
                           (or (= next-door room) (not= :portal-in (fixture state next-door)))))
        careful (fn [state next-door]
                  (and (open-to-player state next-door)
                       (or (= next-door room)
                           (and (not (contains? costly (fixture state next-door)))
                                (let [ground (terrain state next-door)]
                                  (or (nil? ground) (shod? state ground)))))))
        onward (fn [open?]
                 (let [distances (distances-to state room open? true)]
                   (when-let [far-off (get distances here)]
                     (first (filter (fn [next-door]
                                      (and (open? state next-door)
                                           (= (dec far-off)
                                              (get distances (if (= next-door room)
                                                               room
                                                               (get sends next-door next-door))))))
                                    (adjoining state here))))))]
    (when-let [step (or (onward careful) (onward open-to-player))]
      (direction-of here step))))

(defn- hold-focus!
  "Give the keys back to the floor, so that the space bar takes the
  warrant instead of pressing whichever button was clicked last. The
  reader is already here, so nothing scrolls."
  [node]
  (.focus node #js {:preventScroll true}))

(defn- wire-keys!
  "Its own handler rather than `keys!`, because shift is part of the
  verb here, and a key typed into the settings belongs to the settings.
  Any other key the floor knows is the floor's, even when a button has
  the focus: the focus comes back to the floor first, so the button
  never sees the key. Nothing here calls focus until the reader has
  pressed something."
  [node]
  (when-not (.hasAttribute node "tabindex")
    (.setAttribute node "tabindex" "0"))
  (set! (.-onkeydown node)
        (fn [event]
          (let [pressed (get keymap (.-key event))
                target (.-target event)]
            (when (and pressed
                       (or (not= :cancel pressed) @aiming @selected)
                       (not (or (.-ctrlKey event) (.-metaKey event) (.-altKey event)))
                       (not (typing? target)))
              (.preventDefault event)
              (when-not (identical? target node) (hold-focus! node))
              (case pressed
                :restart (restart! node)
                :again (again! node)
                :take (act! node [:take])
                :cancel (do (if @aiming (reset! aiming nil) (reset! selected nil)) (paint! node))
                (:vanquish :teleport :stun) (power! node pressed)
                (if (= :teleport @aiming)
                  (if-let [room (blink-target @game pressed)]
                    (act! node [:teleport room])
                    (do (swap! game note "Nowhere to land that way.")
                        (paint! node)))
                  (act! node [(if (or (.-shiftKey event) @pushing) :push :walk) pressed]))))))))

(defn- wire-grid!
  "A click on a room next to the player's walks into it, or pushes what
  is in it with shift or the Push button. A click further off takes
  one step toward it. A click on the player's own room takes what is
  there. While a powerup is being aimed, a click on a lit room uses
  it, and anywhere else puts it away."
  [node]
  (when-let [grid (wui/q! node ".maze-grid")]
    (set! (.-onclick grid)
          (fn [event]
            (when-let [room (wui/data event ".maze-room" "row" "col")]
              (let [state @game
                    here (:at state)
                    next-door? (= 1 (steps-between here room))
                    lit? (fn [targets] (some (fn [target] (= target room)) targets))]
                (cond
                  @leaving (hurry-out! node)
                  (:over state) nil
                  (= :vanquish @aiming) (if (lit? (vanquish-targets state))
                                          (act! node [:vanquish room])
                                          (do (reset! aiming nil) (paint! node)))
                  (= :teleport @aiming) (if (lit? (teleport-targets state))
                                          (act! node [:teleport room])
                                          (do (reset! aiming nil) (paint! node)))
                  (= room here) (act! node [:take])
                  :else
                  (when-let [direction (if next-door?
                                         (direction-of here room)
                                         (first-step state room))]
                    (act! node [(if (and next-door? (or (.-shiftKey event) @pushing))
                                  :push
                                  :walk)
                                direction])))))))))

(defn- wire-wall!
  "Pointing at a Requirement lights the things it is about."
  [node]
  (when-let [wall (wui/q! node ".maze-reqs")]
    (set! (.-onpointerover wall)
          (fn [event]
            (when-let [line (.closest (.-target event) ".maze-req")]
              (let [named (set (keep (fn [attribute]
                                       (when-let [value (.getAttribute line attribute)]
                                         (keyword value)))
                                     ["data-a" "data-b"]))]
                (when (not= named @pointing)
                  (reset! pointing named)
                  (paint-grid! node))))))
    (set! (.-onpointerleave wall)
          (fn [_]
            (when @pointing
              (reset! pointing nil)
              (paint-grid! node))))))

(defn- show-about!
  "The floor's lighting and the window, drawn again after the pointer has
  moved in the key."
  [node]
  (paint-grid! node)
  (wui/html! node ".maze-about" (about-html @game (or @hovering @selected))))

(defn- entry-at
  "The entry in the key an event happened on, or nil."
  [event]
  (when-let [item (.closest (.-target event) ".maze-legend-item")]
    (keyword (.getAttribute item "data-entry"))))

(defn- less-motion?
  []
  (boolean (when (.-matchMedia js/globalThis)
             (.-matches (.matchMedia js/globalThis "(prefers-reduced-motion: reduce)")))))

(defn- scramble!
  "Every entity in the widget that is free to move, a moment later. Held
  and stunned ones keep still, which is one way to tell."
  [node]
  (doseq [element (.querySelectorAll node ".maze-entity:not(.is-held)")]
    (let [text (flicker (.-textContent element)
                        (get glitch-pools (keyword (.getAttribute element "data-kind")) (:entity glitch-pools)))]
      (set! (.-textContent element) text)
      (.setAttribute element "data-text" text))))

(defn- start-glitching!
  "One timer for every entity in the widget, started once, and not at all
  for a reader who has asked for less motion."
  [node]
  (let [glitch-ms 120]  ; how often the entities change their letters
    (when (and (nil? @glitch-timer) (not (less-motion?)))
      (reset! glitch-timer (js/setInterval (fn [] (scramble! node)) glitch-ms)))))

(defn- wire-key!
  "Pointing at an entry in the key lights where it is on the floor and
  shows what it does in the window beside the Requirements. A click keeps
  it there, lit, until the same entry is clicked again or Esc is pressed."
  [node]
  (when-let [key-section (wui/q! node ".maze-key-panel")]
    (when (true? (reader/stored :maze-key-open))
      (set! (.-open key-section) true))
    (set! (.-ontoggle key-section)
          (fn [_] (reader/store! :maze-key-open (boolean (.-open key-section)))))
    (when-let [summary (.querySelector key-section "summary")]
      (set! (.-onclick summary) (fn [_] (hold-focus! node)))))
  (when-let [legend (wui/q! node ".maze-legend")]
    (set! (.-onpointerover legend)
          (fn [event]
            (when-let [entry (entry-at event)]
              (when (not= entry @hovering)
                (reset! hovering entry)
                (reset! pointing (entry-lights entry))
                (show-about! node)))))
    (set! (.-onpointerleave legend)
          (fn [_]
            (when (or @hovering @pointing)
              (reset! hovering nil)
              (reset! pointing nil)
              (show-about! node))))
    (set! (.-onclick legend)
          (fn [event]
            (when-let [entry (entry-at event)]
              (swap! selected (fn [chosen] (when-not (= chosen entry) entry)))
              (paint! node)
              (hold-focus! node))))))

(defn- wire-powers!
  [node]
  (when-let [powers (wui/q! node ".maze-powers")]
    (set! (.-onclick powers)
          (fn [event]
            (when-let [button (.closest (.-target event) ".maze-power")]
              (hold-focus! node)
              (power! node (keyword (.getAttribute button "data-power"))))))))

(defn- wire-buttons!
  "Every button hands the keys back to the floor once it has done its
  work."
  [node]
  (let [then-focus (fn [work] (fn [_] (work) (hold-focus! node)))]
    (reader/on-click! node ".maze-take" (then-focus (fn [] (act! node [:take]))))
    (reader/on-click! node ".maze-push"
                              (then-focus (fn []
                                            (swap! pushing not)
                                            (paint! node))))
    (reader/on-click! node ".maze-restart" (then-focus (fn [] (restart! node))))
    (reader/on-click! node ".maze-again" (then-focus (fn [] (again! node))))))

(defn- wire-settings!
  "Run lays out a new first floor, on the new settings."
  [node id]
  (wui/settings!
   node id
   (fn [setting]
     (cancel-leaving!)
     (reset! pushing false)
     (reset! aiming nil)
     (reset! book (merge-settings default-book setting))
     (new-run!)
     (settle! node))))

(defn setup!
  "Once per element. Each part is wired on its own, so one that fails
  says so and takes nothing else with it."
  [node]
  (let [id (.-id node)]
    (start-glitching! node)
    (wui/wire! node "keys" wire-keys!)
    (wui/wire! node "grid" wire-grid!)
    (wui/wire! node "wall" wire-wall!)
    (wui/wire! node "key" wire-key!)
    (wui/wire! node "powers" wire-powers!)
    (wui/wire! node "buttons" wire-buttons!)
    (wui/wire! node "settings" (fn [element] (wire-settings! element id)))))

(defn mount!
  "No focus is taken here. A page that jumps to a game the reader has not
  reached yet has made a decision on the reader's behalf."
  [{:keys [id]}]
  (when (nil? @book) (reset! book default-book))
  (when (nil? @game) (new-run!))
  (wui/mount! ::maze (or id "maze") setup! paint!))