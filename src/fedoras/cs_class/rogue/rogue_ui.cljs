(ns fedoras.cs-class.rogue.rogue-ui
  "A roguelike with the map taken out.

  WHAT IS LEFT WHEN YOU TAKE THE MAP OUT. Two doors, and behind each one
  a room from a bag that was packed before you got here. The bag holds
  the same things every floor: three fights, a shop, a treasure, some
  quests, and rather more empty rooms than anybody would put in a
  building on purpose. You draw two, you take one, and the other is
  never spoken of again.

  It is a large table and a small interpreter. Every item, upgrade,
  enemy, boss, sacrifice and quest is data in `rogue_settings.cljs`, and
  the fight reads that data as flags, so a reader who wants a new item
  writes a map and does not touch this file. That is the whole design,
  and it is also why the file is short and the settings file is not.

  THE SACRIFICES ARE NOT BALANCED and are not meant to be. They are what
  you remember about a run, and the price is whatever you can least
  afford, which is a design decision that games make and then describe
  as difficulty."
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; A predictable die
;; ---------------------------------------------------------------------

(defonce seed-a (atom 1))

(defn- seed! [n] (reset! seed-a (bit-or 1 (bit-and n 0x7fffffff))))

(defn- rnd []
  (let [x @seed-a
        x (bit-and (bit-xor x (bit-shift-left x 13)) 0x7fffffff)
        x (bit-and (bit-xor x (unsigned-bit-shift-right x 17)) 0x7fffffff)
        x (bit-and (bit-xor x (bit-shift-left x 5)) 0x7fffffff)]
    (reset! seed-a x)
    (/ x 2147483647.0)))

(defn- roll
  "An integer from 0 up to n, not including n."
  [n]
  (min (dec n) (int (* (rnd) n))))

(defn- pick [coll]
  (when (seq coll)
    (let [v (vec coll)] (nth v (roll (count v))))))

(defn- shuffled [coll]
  (let [v (js/Array.from (into-array coll))]
    (loop [i (dec (.-length v))]
      (if (pos? i)
        (let [j (roll (inc i))
              held (aget v i)]
          (aset v i (aget v j))
          (aset v j held)
          (recur (dec i)))
        (vec v)))))

(defn- chance? [percent] (< (* 100 (rnd)) percent))

;; ---------------------------------------------------------------------
;; State
;; ---------------------------------------------------------------------

(defonce book (atom nil))
(defonce game (atom nil))
(defonce reel (atom nil))

(def storage-key :rogue)
(defn living [state] (filterv (fn [e] (pos? (:hp e))) (:enemies state)))

(defn refresh-price
  "What the next look out the back costs. It doubles, and the shop does
  not say that it doubles."
  [n]
  (let [{:keys [refresh-price refresh-price-mult]} (:tuning @book)]
    (js/Math.round (* (or refresh-price 10)
                      (js/Math.pow (or refresh-price-mult 2) n)))))

(defn refresh-chance
  "How likely it is that there is anything out the back. It falls, and
  the shop does not say that it falls either."
  [n]
  (let [{:keys [refresh-chance refresh-chance-drop]} (:tuning @book)]
    (max 0 (- (or refresh-chance 85) (* (or refresh-chance-drop 20) n)))))

(def default-book
  "Enough to run on before the settings are read, so nothing downstream
  destructures a nil."
  {:start {:hp 300 :strength 4 :speed 10 :dodge 0 :crit 5}
   :tuning {:energy-gain 5 :strong-at 30 :strong-mult 2 :crit-mult 2
            :item-price 25 :heal-price 10 :heal-amount 12
            :paid-heal [14 20] :free-heal [3 7] :scavenge [4 11]
            :defend-reduce 0.5 :enemy-gold 2
            :xp-curve [5 15 30 75 120 200 320]
            :level-reach 2 :max-party 3 :min-enemy-growth 2
            :superboss-every 7 :fortune-level 5
            :sacrifice-chance 50
            :refresh-price 10 :refresh-price-mult 2
            :refresh-chance 85 :refresh-chance-drop 20
            :poison-tick 1 :poison-fade 1 :summon-cap 5
            :near-miss 0.25}
   :floor {:steps 6 :encounters 3 :shops 1 :treasures 1 :quests 2 :empties 4}
   :items [] :upgrades [] :sacrifices []
   :minors [] :bosses [] :superbosses [] :quests []
   :quest-rewards {:gold 30 :xp 4}})

;; ---------------------------------------------------------------------
;; Reading the kit
;;
;; Every item, upgrade and sacrifice you hold is one map, and what they
;; do is the sum of them. There is no ordering and no interaction: two
;; things that both say +3 Strength give you six, and that is the whole
;; of the rules engine.
;; ---------------------------------------------------------------------

(defn kit
  "Everything the player is carrying, as one seq of effect maps."
  [state]
  (concat (:items state) (:upgrades state) (:sacrifices state)))

(defn total
  "Add up one numeric effect across the kit."
  [state k]
  (reduce + 0 (keep k (kit state))))

(defn flag?
  "True when anything in the kit sets a flag."
  [state k]
  (boolean (some k (kit state))))

(defn best
  "The largest value of one effect across the kit, or `floor` if nothing
  sets it. For things that replace rather than accumulate."
  [state k floor]
  (reduce max floor (keep k (kit state))))

(defn least
  "The smallest value, for thresholds that things lower."
  [state k ceiling]
  (reduce min ceiling (keep k (kit state))))

(defn max-hp [state]
  (let [base (+ (get-in @book [:start :hp])
                (total state :max-hp)
                (* (total state :sacrifice-hp) (count (:sacrifices state))))
        mult (reduce * 1 (keep :hp-mult (kit state)))]
    (js/Math.round (* base mult))))

(defn strength [state]
  (+ (get-in @book [:start :strength])
     (total state :strength)
     (* (total state :gold-strength) (quot (:gold state) 10))
     (* (total state :item-strength) (count (:items state)))
     (* (total state :level-strength) (:level state))
     (* (total state :sacrifice-strength) (count (:sacrifices state)))))

(defn speed [state]
  (+ (get-in @book [:start :speed]) (total state :speed)))

(defn dodge [state]
  (min 75 (+ (get-in @book [:start :dodge]) (total state :dodge))))

(defn crit [state]
  (min 100 (+ (get-in @book [:start :crit]) (total state :crit))))

(defn crit-mult [state]
  (best state :crit-mult (get-in @book [:tuning :crit-mult])))

(defn strong-at [state]
  (least state :strong-at (get-in @book [:tuning :strong-at])))

(defn energy-gain [state]
  (+ (get-in @book [:tuning :energy-gain]) (total state :energy-gain)))

;; ---------------------------------------------------------------------
;; Levelling
;; ---------------------------------------------------------------------

(defn xp-needed
  "What the next level costs. Past the end of the curve it doubles, which
  is a thing games do so that the number can keep going up without
  anybody having to decide what should happen."
  [level]
  (let [curve (get-in @book [:tuning :xp-curve])]
    (if (<= level (count curve))
      (nth curve (dec level))
      (* 2 (last curve)))))

(defn xp-for
  "What one kill is worth. A minor gives its level, a boss gives twice
  its level and one more, so the numbers grow and the ratio does not."
  [state boss?]
  (let [level (:level state)
        base (if boss? (+ (* 2 level) 1) level)]
    (js/Math.round
     (* (+ base (total state :xp-flat))
        (reduce * 1 (keep :xp-mult (kit state)))))))

;; ---------------------------------------------------------------------
;; The log
;; ---------------------------------------------------------------------

(defn say
  "Add a line to the log. The log is short on purpose: a roguelike that
  tells you everything it did is a spreadsheet."
  [state & parts]
  (update state :log
          (fn [lines] (vec (take-last 9 (conj (or lines []) (str/join "" parts)))))))

;; ---------------------------------------------------------------------
;; The floor, as a bag
;; ---------------------------------------------------------------------

(def never-together
  "Pairs the game will not offer you at the same time, because a choice
  between two things you want is a choice and a choice between a thing
  you want and a thing you want is theatre."
  #{#{:treasure :quest} #{:treasure :shop}})

(defn pack-floor
  "The bag for one floor. Everything that is guaranteed goes in first and
  the rest is empty rooms, and there are always more rooms than steps,
  because you have to be able to leave one behind."
  [state]
  (let [{:keys [steps encounters shops treasures quests empties]} (:floor @book)]
    (shuffled
     (concat (repeat encounters :encounter)
             (repeat shops :shop)
             (repeat treasures :treasure)
             (repeat quests :quest)
             (repeat empties :empty)
             (repeat (max 0 (- (* 2 steps)
                               encounters shops treasures quests empties))
                     :empty)))))

(defn- not-together?
  "Whether these two rooms may be offered as a pair.

  `hash-set` rather than the `#{}` literal, because a set literal checks
  for duplicates at runtime and throws, and two doors drawing the same
  room is ordinary. That crash lands inside `draw-doors`, which every
  room exit goes through, so it reads as a button that does nothing."
  [a b]
  (contains? never-together (hash-set a b)))

(defn draw-doors
  "Two rooms from the bag, avoiding the pairs that are not really a
  choice. The bag is rotated rather than redrawn, and given up on after
  a few tries, because a bag that has run down to two treasures is a bag
  that has earned it."
  [state]
  (loop [bag (vec (:bag state)) tries 0]
    (let [a (first bag)
          b (second bag)]
      (cond
        (nil? a) (assoc state :doors [:empty :empty] :bag [])
        (nil? b) (assoc state :doors [a :empty] :bag [])
        (and (< tries 4) (not-together? a b))
        (recur (vec (concat (rest bag) [a])) (inc tries))
        :else (assoc state :doors [a b] :bag (vec (drop 2 bag)))))))

(defn next-step
  "Move to the next door, or to the boss if the floor is spent."
  [state]
  (let [step (inc (:step state))
        steps (get-in @book [:floor :steps])]
    (cond
      (> step steps)
      (assoc state :scene :boss-door :step step)

      (= step steps)
      (-> state (assoc :step step) (assoc :doors [:heal :empty]) (assoc :scene :doors))

      :else
      (-> state (assoc :step step) draw-doors (assoc :scene :doors)))))

;; ---------------------------------------------------------------------
;; Enemies
;; ---------------------------------------------------------------------

(def marks
  "What a card can be carrying, as legend entries.

  The same shapes go in the key, so a reader meets each one twice: once
  where it is explained and once where it matters. Which is the whole
  reason the key is a shared thing rather than a paragraph."
  [{:id :poisoned :letter "P" :tone :good :label "poisoned"
    :note "loses health at the end of each round"}
   {:id :shield :letter "S" :tone :shield :label "shielded"
    :note "the number in front of its health"}
   {:id :summons :letter "+" :tone :bad :label "summons"
    :note "brings more of them, every few rounds"}
   {:id :multi :letter "X" :tone :bad :label "strikes twice"
    :note "or more than twice"}
   {:id :turned :letter "T" :tone :aim :label "reassigned"
    :note "fights for you until it dies"}])

(defn marks-on
  "Which marks one enemy is carrying, in the order they are listed."
  [enemy]
  (filterv (fn [{:keys [id]}]
             (case id
               :poisoned (pos? (or (:poisoned enemy) 0))
               :shield (pos? (or (:shield enemy) 0))
               :summons (some? (:summons enemy))
               :multi (pos? (or (:extra-strikes enemy) 0))
               :turned (:turned enemy)
               false))
           marks))

(defn- mark-html
  [{:keys [letter tone]} value]
  (str "<span class='rogue-mark legend-glyph tone-" (name tone) "'>"
       "<span class='legend-letter'>" letter (when value value) "</span>"
       "</span>"))

(defn make-enemy
  [state template boss?]
  (let [weaken (min 0.8 (total state :enemy-weaken))
        floor (:floor state)
        scale (+ 1 (* 0.25 (dec floor)))
        hp (max 1 (js/Math.round (* (:hp template) scale (- 1 weaken))))]
    (assoc template
           :hp hp
           :max-hp hp
           :shield (js/Math.round (* (or (:shield template) 0) scale))
           :poisoned 0
           :strength (js/Math.round (* (:strength template) scale))
           :boss boss?)))

(defn- between
  "An integer in an inclusive range, given as [low high] in the settings.
  A bare number is a range of one, so a reader can write either."
  [range fallback]
  (let [[low high] (cond
                     (and (vector? range) (= 2 (count range))) range
                     (number? range) [range range]
                     :else fallback)]
    (+ low (roll (inc (max 0 (- high low)))))))

(defn encounter-budget
  "How much room this encounter has, in CR.

  A budget rather than a party size, because a room with one heavy thing
  in it and a room with six light ones should both be a fair fight, and
  which you get should be a matter of what came out of the bag rather
  than a rule anybody could state."
  [state]
  (let [{:keys [encounter-cr cr-per-floor cr-per-level]} (:tuning @book)]
    (+ (between encounter-cr [4 7])
       (* (or cr-per-floor 1.6) (dec (:floor state)))
       (* (or cr-per-level 0.9) (dec (:level state))))))

(defn eligible
  "Who you can meet.

  Anything whose `:min-level` your level plus the reach has caught up
  with, so there is always something above you out there and it is
  always the thing worth the most. A minor with no `:min-level` is
  available from the start.

  Falls back to the whole pool rather than returning nothing, because a
  settings file where every minor is gated above level one should give
  you a strange fight rather than an empty room."
  [state]
  (let [ceiling (+ (:level state) (or (get-in @book [:tuning :level-reach]) 2))
        pool (filterv (fn [e] (<= (or (:min-level e) 1) ceiling)) (:minors @book))]
    (if (seq pool) pool (:minors @book))))

(defn make-encounter
  "One encounter, drawn against a budget.

  Anything whose CR fits in what is left may be drawn, so a large budget
  spent on the first draw is one heavy thing and the same budget spent
  evenly is a crowd. The shape of a room is a consequence of arithmetic
  rather than a rule, which is why no two floors give the same rooms and
  why a summoner sometimes arrives with company and sometimes alone."
  [state]
  (let [most (or (get-in @book [:tuning :max-party]) 6)
        pool (eligible state)]
    (loop [left (encounter-budget state)
           picked []]
      (let [affordable (filterv (fn [e] (<= (or (:cr e) 1) left)) pool)]
        (if (or (>= (count picked) most) (empty? affordable))
          (vec (map-indexed (fn [i template]
                              (assoc (make-enemy state template false) :slot i))
                            (if (seq picked) picked [(pick pool)])))
          (let [chosen (pick affordable)]
            (recur (- left (or (:cr chosen) 1))
                   (conj picked chosen))))))))

(defn make-boss [state]
  (let [pool (:bosses @book)
        which (nth pool (mod (dec (:floor state)) (max 1 (count pool))))]
    [(assoc (make-enemy state which true) :slot 0)]))

(defn superboss-floor?
  "Whether this floor's boss is something else. Every seventh, and then
  every seventh after that, because the game does not end and saying so
  is more honest than a credits screen."
  [state]
  (let [every (get-in @book [:tuning :superboss-every])]
    (and (pos? every) (zero? (mod (:floor state) every)))))

(defn make-superboss
  [state]
  (let [pool (:superbosses @book)
        every (max 1 (get-in @book [:tuning :superboss-every]))
        times (quot (:floor state) every)
        which (if (seq pool)
                (nth pool (mod (dec times) (count pool)))
                (pick (:bosses @book)))]
    [(assoc (make-enemy state which true) :slot 0 :superboss true)]))

;; ---------------------------------------------------------------------
;; The fight
;; ---------------------------------------------------------------------

(defn start-fight
  [state enemies]
  (let [heal (total state :fight-heal)]
    (-> state
        (assoc :enemies enemies)
        (assoc :energy (total state :start-energy))
        (assoc :shield (total state :shield))
        (assoc :poisoned 0)
        (assoc :round 1)
        (assoc :untouched true)
        (assoc :defending false)
        (assoc :hp (min (max-hp state) (+ (:hp state) heal)))
        (assoc :scene :fight)
        (say "A fight. " (str/join ", " (map :name enemies)) "."))))

(defn flash
  "Mark something as having just been hit, so the paint can show it.

  A frame of the exchange is a state, and states have no memory of what
  changed between them. Rather than diffing two states to work out what
  moved, the fight says so on the way past. Cleared at the start of each
  step, so a flash lasts exactly one frame."
  [state who kind]
  (assoc-in state [:flash who] kind))

(defn unflash
  [state]
  (dissoc state :flash))

(defn take-damage
  "Damage against a thing with a shield in front of it.

  One function because shield has to come off before health does, and
  before this there were four places that subtracted from health and
  would each have needed the same three lines. Returns the thing."
  [who amount]
  (let [shield (or (:shield who) 0)
        stopped (min shield amount)
        through (- amount stopped)]
    (-> who
        (assoc :shield (- shield stopped))
        (update :hp - through))))

(defn poison-of
  "How much poison one of your hits lays on. Zero unless something in the
  kit says otherwise, which is most of the time, which is what makes the
  items that say otherwise worth having."
  [state]
  (total state :poison))

(defn hurt-enemy
  "Damage one enemy by slot, through its shield, and say which of the two
  took it so the card can flash the right colour."
  [state slot amount]
  (let [target (first (filter (fn [e] (= (:slot e) slot)) (:enemies state)))
        shield (or (:shield target) 0)
        on-shield (pos? (min shield amount))]
    (-> state
        (update :enemies
                (fn [es]
                  (mapv (fn [e]
                          (if (= slot (:slot e))
                            (let [hit (take-damage e amount)]
                              (assoc hit :hp (max 0 (:hp hit))))
                            e))
                        es)))
        (flash slot (if on-shield :shield :hurt)))))

(defn poison-enemy
  [state slot amount]
  (if-not (pos? amount)
    state
    (update state :enemies
            (fn [es]
              (mapv (fn [e]
                      (if (= slot (:slot e))
                        (update e :poisoned (fnil + 0) amount)
                        e))
                    es)))))

(defn damage-to
  "What one attack does to one enemy, and whether it crits."
  [state enemy strong?]
  (let [base (strength state)
        low (if (< (:hp enemy) (/ (:max-hp enemy) 2))
              (+ 1 (total state :low-bonus))
              1)
        first-round (if (= 1 (:round state)) (+ 1 (total state :first-bonus)) 1)
        mult (reduce * 1 (keep :damage-mult (kit state)))
        strong (if strong? (get-in @book [:tuning :strong-mult]) 1)
        crit? (chance? (crit state))
        crit-x (if crit? (crit-mult state) 1)]
    [(max 1 (js/Math.round (* base low first-round mult strong crit-x)))
     crit?]))

(defn land-hit
  "One landed attack on one enemy, with everything that follows: the
  shield taking it first, execution, lifesteal, and the poison that
  comes off your hands whether or not the hit killed."
  [state enemy amount]
  (let [execute (total state :execute)
        after-shield (max 0 (- amount (or (:shield enemy) 0)))
        finished (and (pos? execute)
                      (<= (- (:hp enemy) after-shield)
                          (* execute (:max-hp enemy))))
        dealt (if finished (+ (:hp enemy) (or (:shield enemy) 0)) amount)
        steal (+ (total state :flat-steal)
                 (js/Math.round (* dealt (total state :lifesteal))))
        venom (poison-of state)
        spreads (flag? state :poison-cleave)]
    (-> state
        (hurt-enemy (:slot enemy) dealt)
        (as-> s (if spreads
                  (reduce (fn [acc e] (poison-enemy acc (:slot e) venom))
                          s (living s))
                  (poison-enemy s (:slot enemy) venom)))
        (update :hp (fn [hp] (min (max-hp state) (+ hp steal))))
        (as-> s (if finished (say s (:name enemy) " is finished off.") s)))))

(defn swing
  "One attack, at one target or at all of them."
  [state target strong?]
  (let [targets (if (flag? state :cleave) (living state) [target])]
    (reduce (fn [acc enemy]
              (let [fresh (first (filter (fn [e] (= (:slot e) (:slot enemy)))
                                         (:enemies acc)))]
                (if (and fresh (pos? (:hp fresh)))
                  (let [[amount crit?] (damage-to acc fresh strong?)]
                    (-> acc
                        (land-hit fresh amount)
                        (say (:name fresh) " takes " amount
                             (cond strong? " (strong)" crit? " (crit)" :else ""))))
                  acc)))
            state
            targets)))

(defn extra-strikes
  "How many swings past the first. Summed, so two of a thing that gives
  you an extra swing gives you two extra swings, which is what anybody
  would guess."
  [state]
  (total state :extra-strikes))

(defn strike-power
  "What the extra swings hit for, as a fraction. Multiplied together, so
  two forks at 60% give you two extra swings at 36% each rather than one
  free upgrade to full power."
  [state]
  (reduce * 1 (keep :double-power (kit state))))

(defn ability
  "The one active thing you are carrying, or nil.

  One at a time, because an ability you have to choose between is a
  decision and a row of them is a toolbar."
  [state]
  (:ability state))

(defn ready?
  "Whether it can be used this round."
  [state]
  (and (some? (ability state))
       (zero? (or (:cooldown-left state) 0))))

(defn cool-down
  "One round off the cooldown. Called at the end of a round, so an
  ability with a cooldown of one is usable every other turn rather than
  every turn."
  [state]
  (update state :cooldown-left (fn [n] (max 0 (dec (or n 0))))))

(defn use-ability
  "What the one ability you are carrying does.

  `keep!` is threaded through so that anything hitting more than one
  thing shows as more than one beat. A chain that lands three times in a
  single frame is a number changing; three frames is lightning."
  [state keep!]
  (let [{:keys [effect amount cooldown bounces falloff max-cr name]}
        (ability state)
        alive (living state)]
    (-> (case effect
          :heal
          (keep! (-> state
                     (update :hp (fn [hp] (min (max-hp state) (+ hp amount))))
                     (assoc :poisoned 0)
                     (flash :you :poison)
                     (say name ". " amount " back, and the poison with it.")))

          :shield
          (keep! (-> state
                     (update :shield (fnil + 0) amount)
                     (flash :you :shield)
                     (say name ". " amount " shield.")))

          :convert
          (let [takeable (filterv (fn [e]
                                    (and (not (:turned e))
                                         (<= (or (:cr e) 1) (or max-cr 3))))
                                  alive)
                target (first (sort-by (fn [e] (or (:cr e) 1)) takeable))]
            (if-not target
              (say state "Nothing here is junior enough to reassign.")
              (keep! (-> state
                         (update :enemies
                                 (fn [es]
                                   (mapv (fn [e]
                                           (if (= (:slot e) (:slot target))
                                             (assoc e :turned true)
                                             e))
                                         es)))
                         (flash (:slot target) :shield)
                         (say name ". " (:name target) " is on your side now.")))))

          :chain
          (let [order (take (or bounces 3) (cycle (sort-by :hp alive)))]
            (first
             (reduce
              (fn [[acc power] enemy]
                (let [fresh (first (filter (fn [e] (= (:slot e) (:slot enemy)))
                                           (living acc)))]
                  (if-not fresh
                    [acc power]
                    (let [hit (max 1 (js/Math.round (* (strength acc) power)))]
                      [(keep! (-> (unflash acc)
                                  (hurt-enemy (:slot fresh) hit)
                                  (say (:name fresh) " takes " hit " from the arc")))
                       (* power (or falloff 0.65))]))))
              [(say state name ".") 1.5]
              order)))

          :weaken
          (keep! (-> state
                     (update :enemies
                             (fn [es]
                               (mapv (fn [e]
                                       (update e :strength
                                               (fn [s]
                                                 (max 1 (js/Math.round (* s (- 1 amount)))))))
                                     es)))
                     (as-> s (reduce (fn [acc e] (flash acc (:slot e) :shield)) s alive))
                     (say name ". Everything here is suddenly less sure of itself.")))

          :plague
          (keep! (-> (reduce (fn [acc e] (poison-enemy acc (:slot e) amount)) state alive)
                     (as-> s (reduce (fn [acc e] (flash acc (:slot e) :poison)) s alive))
                     (say name ". Poison, on all of it.")))

          :extra-turn
          (-> state (assoc :free-swing true) (say name "."))

          state)
        (assoc :cooldown-left cooldown))))

(defn player-attack
  "One turn's attacking: the first swing, then however many extras the
  kit has bought you, each at the compounded power."
  [state slot strong?]
  (let [alive (living state)
        target (or (first (filter (fn [e] (= (:slot e) slot)) alive))
                   (first alive))]
    (if-not target
      state
      (let [extras (extra-strikes state)
            power (strike-power state)
            first-swing (swing state target strong?)
            after (reduce
                   (fn [acc _]
                     (let [again (or (first (filter (fn [e] (= (:slot e) slot))
                                                    (living acc)))
                                     (first (living acc)))]
                       (if-not again
                         acc
                         (let [[amount crit?] (damage-to acc again strong?)]
                           (-> acc
                               (land-hit again
                                         (max 1 (js/Math.round (* amount power))))
                               (say (:name again) " again"
                                    (if crit? " (crit)" "")))))))
                   first-swing
                   (range extras))]
        (if strong?
          (assoc after :energy 0)
          (update after :energy + (energy-gain state)))))))

(defn player-attack-framed
  "One turn's attacking, with a frame kept after each strike.

  The same as `player-attack` except that it hands each landed blow to
  `keep!` before the next one, so a reader with two extra strikes sees
  three beats rather than one."
  [state slot strong? keep!]
  (let [alive (living state)
        target (or (first (filter (fn [e] (= (:slot e) slot)) alive))
                   (first alive))]
    (if-not target
      state
      (let [extras (extra-strikes state)
            power (strike-power state)
            first-swing (keep! (swing (unflash state) target strong?))
            after (reduce
                   (fn [acc _]
                     (let [again (or (first (filter (fn [e] (= (:slot e) slot))
                                                    (living acc)))
                                     (first (living acc)))]
                       (if-not again
                         acc
                         (let [[amount crit?] (damage-to acc again strong?)]
                           (keep!
                            (-> (unflash acc)
                                (land-hit again
                                          (max 1 (js/Math.round (* amount power))))
                                (say (:name again) " again"
                                     (if crit? " (crit)" ""))))))))
                   first-swing
                   (range extras))]
        (if strong?
          (assoc after :energy 0)
          (update after :energy + (energy-gain state)))))))

(defn enemy-swing
  "One enemy hit landing on you, through dodge and shield.

  `power` is a fraction, so the extra swings of a multi-striker hit for
  less than its first, exactly as yours do. A single-striker is called
  with 1 and nothing changes for it."
  [state enemy power]
  (let [fresh (first (filter (fn [e] (= (:slot e) (:slot enemy))) (:enemies state)))]
    (if-not (and fresh (pos? (:hp fresh)))
      state
      (if (chance? (dodge state))
        (-> state
            (update :hp (fn [hp] (min (max-hp state) (+ hp (total state :dodge-heal)))))
            (flash (:slot fresh) :missed)
            (say "You slip past " (:name fresh) "."))
        (let [raw (* (:strength fresh) power)
              defended (if (:defending state)
                         (* raw (get-in @book [:tuning :defend-reduce]))
                         raw)
              reduced (* defended (- 1 (min 0.8 (total state :reduce))))
              soaked (max 1 (js/Math.round (- reduced (total state :soak))))
              shield (or (:shield state) 0)
              stopped (min shield soaked)
              through (- soaked stopped)
              venom (or (:poison fresh) 0)
              thorns (total state :thorns)]
          (-> state
              (assoc :shield (- shield stopped))
              (update :hp - through)
              (assoc :untouched false)
              (flash :you (if (pos? stopped) :shield :hurt))
              (say (:name fresh) " hits for " soaked
                   (when (pos? stopped) (str " (" stopped " on the shield)")))
              (as-> s (if (pos? venom)
                        (-> s (update :poisoned (fnil + 0) venom) (say "Poisoned."))
                        s))
              (as-> s (if (pos? thorns)
                        (-> s
                            (hurt-enemy (:slot fresh) thorns)
                            (say (:name fresh) " takes " thorns " back"))
                        s))))))))

(defn enemy-turn-one
  "One enemy's whole turn: its swing, and however many extras it has.

  The same arithmetic as yours, so NIGHT PORTER hitting twice for half is
  the thing a reader already understands from carrying the fork."
  [state enemy]
  (let [fresh (first (filter (fn [e] (= (:slot e) (:slot enemy))) (:enemies state)))]
    (if-not (and fresh (pos? (:hp fresh)))
      state
      (let [extras (or (:extra-strikes fresh) 0)
            power (or (:double-power fresh) 1)]
        (reduce (fn [acc _] (enemy-swing acc fresh power))
                (enemy-swing state fresh 1)
                (range extras))))))

(defn enemy-turn
  "Every living enemy takes one swing at you, through your shield, and
  some of them leave something behind."
  [state]
  (reduce
   (fn [acc enemy]
     (let [fresh (first (filter (fn [e] (= (:slot e) (:slot enemy))) (:enemies acc)))]
       (if-not (and fresh (pos? (:hp fresh)))
         acc
         (if (chance? (dodge acc))
           (-> acc
               (update :hp (fn [hp] (min (max-hp acc) (+ hp (total acc :dodge-heal)))))
               (flash (:slot fresh) :missed)
               (say "You slip past " (:name fresh) "."))
           (let [raw (:strength fresh)
                 defended (if (:defending acc)
                            (* raw (get-in @book [:tuning :defend-reduce]))
                            raw)
                 reduced (* defended (- 1 (min 0.8 (total acc :reduce))))
                 soaked (max 1 (js/Math.round (- reduced (total acc :soak))))
                 shield (or (:shield acc) 0)
                 stopped (min shield soaked)
                 through (- soaked stopped)
                 venom (or (:poison fresh) 0)
                 thorns (total acc :thorns)]
             (-> acc
                 (assoc :shield (- shield stopped))
                 (update :hp - through)
                 (assoc :untouched false)
                 (flash :you (if (pos? stopped) :shield :hurt))
                 (say (:name fresh) " hits for " soaked
                      (when (pos? stopped) (str " (" stopped " on the shield)")))
                 (as-> s (if (pos? venom)
                           (-> s
                               (update :poisoned (fnil + 0) venom)
                               (say "Poisoned."))
                           s))
                 (as-> s (if (pos? thorns)
                           (-> s
                               (hurt-enemy (:slot fresh) thorns)
                               (say (:name fresh) " takes " thorns " back"))
                           s))))))))
   state
   (living state)))

(defn summon!
  "Whatever the room is producing this round.

  The cap is a headcount, not a budget: the budget was spent when the
  room was built and a summoner is a thing that spends past it. Which is
  the point of a summoner and the reason it is worth its own CR — you are
  paying for the room it will become, not the room it is.

  Counted against the living, so a summoner whose spawn you have killed
  can produce again. A summoner that could only ever produce five things
  in total would be a timer rather than a threat."
  [state]
  (let [cap (or (get-in @book [:tuning :summon-cap]) 6)
        round (:round state)
        pool (:minors @book)
        summoners (filter (fn [e]
                            (and (pos? (:hp e))
                                 (:summons e)
                                 (zero? (mod round (max 1 (:summon-every e))))))
                          (:enemies state))]
    (reduce
     (fn [acc summoner]
       (let [alive (count (living acc))
             room (max 0 (- cap alive))
             how-many (min room (or (:summon-count summoner) 1))
             template (first (filter (fn [m] (= (:id m) (:summons summoner))) pool))]
         (if (or (zero? how-many) (nil? template))
           acc
           (let [next-slot (inc (reduce max -1 (map :slot (:enemies acc))))
                 fresh (mapv (fn [i]
                               (assoc (make-enemy acc template false)
                                      :slot (+ next-slot i)))
                             (range how-many))]
             (-> acc
                 (update :enemies into fresh)
                 (as-> s (reduce (fn [st e] (flash st (:slot e) :shield)) s fresh))
                 (say (:name summoner) " produces "
                      (str/join ", " (map :name fresh)) "."))))))
     state
     summoners)))

(defn tick-poison
  "Poison, at the end of a round, on everything that has it.

  It goes through shields, because a shield is a thing you hold in front
  of you and poison is already inside.

  Yours fades at the rate the settings give, less whatever you carry to
  slow it. Theirs fades at the flat rate, because the enemies are not
  carrying anything."
  [state]
  (let [per (+ (get-in @book [:tuning :poison-tick]) (total state :poison-power))
        base-fade (get-in @book [:tuning :poison-fade])
        my-fade (max 1 (- base-fade (total state :poison-fade)))
        their-fade (max 0 (- base-fade (total state :poison-linger)))
        mine (or (:poisoned state) 0)
        bitten (keep (fn [e] (when (pos? (or (:poisoned e) 0)) (:slot e)))
                     (:enemies state))]
    (-> state
        (as-> s
              (if (pos? mine)
                (-> s
                    (update :hp - (* (get-in @book [:tuning :poison-tick]) mine))
                    (update :poisoned (fn [n] (max 0 (- n my-fade))))
                    (flash :you :poison)
                    (say "Poison takes "
                         (* (get-in @book [:tuning :poison-tick]) mine) "."))
                s))
        (as-> s (reduce (fn [acc slot] (flash acc slot :poison)) s bitten))
        (update :enemies
                (fn [es]
                  (mapv (fn [e]
                          (let [stacks (or (:poisoned e) 0)]
                            (if (pos? stacks)
                              (-> e
                                  (update :hp (fn [hp] (max 0 (- hp (* per stacks)))))
                                  (update :poisoned (fn [n] (max 0 (- n their-fade)))))
                              e)))
                        es))))))
(defn end-round
  [state]
  (-> state
      (update :hp (fn [hp] (min (max-hp state) (+ hp (total state :regen)))))
      (update :round inc)
      (assoc :defending false)))

(defn count-kills
  "Tally what died this round, for the quests."
  [state before]
  (let [dead (remove (fn [e]
                       (some (fn [f] (and (= (:slot f) (:slot e)) (pos? (:hp f))))
                             (:enemies state)))
                     (filter (fn [e] (pos? (:hp e))) before))]
    (reduce (fn [acc enemy]
              (-> acc
                  (update-in [:kills (:kind enemy)] (fnil inc 0))
                  (update :xp + (xp-for acc (:boss enemy)))
                  (update :gold + (js/Math.round
                                   (* (get-in @book [:tuning :enemy-gold])
                                      (+ 1 (total acc :gold-mult)))))))
            state
            dead)))

(defn win-fight
  "The end of a fight, and the bookkeeping that follows it.

  Unflashed, because a flash is a thing that happened on one frame and
  the final state is the one that stays on screen. Without this the
  widget keeps the colour of whatever landed last, for as long as the
  reader is looking at it."
  [state]
  (let [boss? (boolean (some :boss (:enemies state)))
        super? (boolean (some :superboss (:enemies state)))
        hp (:hp state)
        most (max-hp state)
        near (* most (get-in @book [:tuning :near-miss]))]
    (-> state
        unflash
        (update :hp (fn [h] (min most (+ h (total state :won-heal)))))
        (update :bosses-beaten (fn [n] (if boss? (inc (or n 0)) (or n 0))))
        (update :clean-wins (fn [n] (if (:untouched state) (inc (or n 0)) (or n 0))))
        (assoc :beat-superboss super?)
        (assoc :enemies [])
        (say "The room is quiet.")
        (as-> s (if (< hp near)
                  (say s "You finished that on " hp ". You will not remember it that way.")
                  s))
        (assoc :scene (if boss? :after-boss :advance)))))

(defn- worth-showing?
  "A frame with nothing lit on it is a frame with nothing to see. The
  poison tick and the summon step usually change nothing, and holding
  them for a beat each turns one exchange into four."
  [frame]
  (seq (:flash frame)))

;; ---------------------------------------------------------------------
;; Quests
;; ---------------------------------------------------------------------

(defn quest-done?
  [state quest]
  (let [{:keys [goal kind count]} quest]
    (case goal
      :kill (>= (get-in state [:kills kind] 0) count)
      :boss (>= (or (:bosses-beaten state) 0) count)
      :items (>= (clojure.core/count (:items state)) count)
      :upgrades (>= (clojure.core/count (:upgrades state)) count)
      :gold (>= (:gold state) count)
      :clean (>= (or (:clean-wins state) 0) count)
      false)))

(defn finished-quests [state]
  (filterv (fn [q] (quest-done? state q)) (:quests state)))

;; ---------------------------------------------------------------------
;; The score
;; ---------------------------------------------------------------------

(defn score
  "What a run is worth, which is a number, which is the point."
  [state]
  (+ (* 100 (dec (:floor state)))
     (* 25 (:level state))
     (* 5 (+ (count (:items state)) (count (:upgrades state))))
     (* 40 (count (:sacrifices state)))
     (:gold state)))

(defn keep-score!
  [state]
  (let [now (score state)
        best-yet (or (fedoras.reader/stored storage-key) 0)]
    (when (> now best-yet)
      (fedoras.reader/store! storage-key now))
    now))

(defn bank!
  "Stop, and keep it.

  This button has been on the screen since the first room. It is the
  other half of the loop and the half nobody uses, which is the whole of
  what this game has to say."
  [state]
  (let [now (keep-score! state)]
    (-> state
        (assoc :banked now)
        (assoc :final-score now)
        (say "You stop, and it counts.")
        (assoc :scene :banked))))

(defn award-fortune! []
  (let [held (or (fedoras.reader/stored :fortune) 0)]
    (fedoras.reader/store! :fortune (inc held))
    (inc held)))

;; ---------------------------------------------------------------------
;; Rooms
;; ---------------------------------------------------------------------

(defn sacrifice-cost
  "What this one asks for. An item or an upgrade if you have one, then
  life, then gold, so the thing it takes is always the thing you were
  going to use."
  [state offer]
  (let [{:keys [item upgrade life gold]} (:cost offer)]
    (cond
      (and item (>= (count (:items state)) item)) [:item item]
      (and upgrade (>= (count (:upgrades state)) upgrade)) [:upgrade upgrade]
      (and life (> (:hp state) life)) [:life life]
      (and gold (>= (:gold state) gold)) [:gold gold]
      :else nil)))

(defn spoils
  "Three things after a boss, from both pools at once.

  Mixed rather than one or the other, because choosing between an item
  and an upgrade is a choice about what kind of run this is, and choosing
  between three items is a choice about arithmetic."
  [state]
  (let [pool (concat (map (fn [thing] (assoc thing :from :items)) (:items @book))
                     (map (fn [thing] (assoc thing :from :upgrades)) (:upgrades @book)))]
    (vec (take 3 (shuffled pool)))))

(defn advance
  "Past the room you were in, and on to the next pair of doors, or to the
  next floor if the boss is done.

  After a boss it is a sacrifice or it is the spoils, and the spoils are
  what you get when there is no sacrifice you could pay for. Which means
  a reader with nothing is never offered nothing: the game only takes
  from people who have something to take."
  [state]
  (cond
    (= :after-boss (:scene state))
    (let [needed (get-in @book [:tuning :fortune-level])
          earned (and (:beat-superboss state) (>= (:level state) needed))
          now (score state)
          best (if (> now (get (:best-bank state) :score 0))
                 {:score now :floor (:floor state)}
                 (:best-bank state))
          state (-> state
                    (assoc :best-bank best)
                    (dissoc :beat-superboss))]
      (if earned
        (assoc state :scene :fortune)
        (let [offer (when (< (roll 100) (get-in @book [:tuning :sacrifice-chance]))
                      (pick (:sacrifices @book)))]
          (if (and offer (sacrifice-cost state offer))
            (-> state (assoc :offer offer) (assoc :scene :sacrifice))
            (-> state
                (assoc :spoils (spoils state))
                (assoc :scene :spoils))))))

    (= :next-floor (:scene state))
    (let [state (-> state (update :floor inc) (assoc :step 0))]
      (-> state
          (assoc :bag (pack-floor state))
          (say "Floor " (:floor state) ".")
          draw-doors
          (assoc :scene :doors)))

    :else (next-step state)))

(def room-plural
  {:encounter "fights" :shop "shops" :treasure "treasures"
   :quest "noticeboards" :empty "quiet rooms" :heal "places to sit down"})

(defn- left-behind
  "What is on the other side of every door you did not take. Counted
  because it is counted anyway, and never mentioned until it is too late
  to do anything about it."
  [state]
  (let [skipped (:skipped state)]
    (if (empty? skipped)
      "There was nothing you turned down."
      (str "You left behind "
           (str/join ", "
                     (map (fn [[room n]]
                            (str n " " (get room-plural room (name room))))
                          (sort-by (fn [[_ n]] (- n)) skipped)))
           "."))))

(defn- could-have
  [state]
  (if-let [{:keys [score floor]} (:best-bank state)]
    (str "You could have stopped on floor " floor " with " score ".")
    "You never got as far as a boss."))

(defn open
  "What is behind the door you took."
  [state room]
  (case room
    :encounter (start-fight state (make-encounter state))

    :empty
    (-> state
        (say "Nothing here. Somebody has been through already.")
        (assoc :scene :advance))

    :shop
    (-> state
        (assoc :stock (vec (take 3 (shuffled (:items @book)))))
        (assoc :restocks 0)
        (dissoc :shop-shut)
        (assoc :scene :shop))

    :treasure
    (let [what (roll 3)]
      (case what
        0 (let [found (pick (:items @book))]
            (-> state
                (update :items conj found)
                (say "A " (or (:name found) (:desc found)) ".")
                (assoc :scene :advance)))
        1 (let [amount (+ 20 (roll 20))]
            (-> state
                (update :gold + amount)
                (say amount " gold, unattended.")
                (assoc :scene :advance)))
        (-> state
            (assoc :owed 1)
            (assoc :held-scene :advance)
            (assoc :offers (vec (take 3 (shuffled (:upgrades @book)))))
            (assoc :scene :upgrade)
            (say "Something here teaches you a thing."))))

    :quest
    (let [done (finished-quests state)]
      (if (seq done)
        (let [{:keys [gold xp]} (:quest-rewards @book)
              quest (first done)]
          (-> state
              (update :quests (fn [qs] (vec (remove (fn [q] (= (:id q) (:id quest))) qs))))
              (update :gold + gold)
              (update :xp + xp)
              (say "Turned in: " (:desc quest) ". " gold " gold.")
              (assoc :scene :advance)))
        (if (>= (count (:quests state)) 2)
          (-> state
              (say "The board is full. Nothing for you.")
              (assoc :scene :advance))
          (let [held (set (map :id (:quests state)))
                available (remove (fn [q] (contains? held (:id q))) (:quests @book))]
            (if-let [offer (pick available)]
              (-> state (assoc :offer offer) (assoc :scene :quest))
              (assoc state :scene :advance))))))

    :heal (assoc state :scene :heal)

    (assoc state :scene :advance)))

;; ---------------------------------------------------------------------
;; A run
;; ---------------------------------------------------------------------

(defn fresh-run
  [now]
  (seed! now)
  (let [state {:scene :doors
               :floor 1
               :step 0
               :level 1
               :xp 0
               :gold 0
               :owed 0
               :items []
               :upgrades []
               :sacrifices []
               :ability nil 
               :cooldown-left 0
               :quests []
               :kills {}
               :skipped {}
               :bosses-beaten 0
               :clean-wins 0
               :banked 0
               :best-bank nil
               :restocks 0
               :poisoned 0
               :shield 0
               :enemies []
               :energy 0
               :log ["You are here. There are two doors."]}
        state (assoc state :hp (max-hp state))]
    (-> state
        (assoc :bag (pack-floor state))
        draw-doors)))

(defn check-level
  "Levelling is checked after anything that gives XP, and it can happen
  more than once at a stroke, which is a thing that feels good and costs
  nothing to allow."
  [state]
  (if (< (:xp state) (xp-needed (:level state)))
    state
    (-> state
        (update :xp - (xp-needed (:level state)))
        (update :level inc)
        (assoc :offers (vec (take 3 (shuffled (:upgrades @book)))))
        (assoc :held-scene (:scene state))
        (assoc :scene :upgrade)
        (say "Level " (inc (:level state)) "."))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn- label-of [thing]
  (or (:name thing) (:desc thing)))

(defn- button
  [act label & {:keys [class disabled]}]
  (str "<button class='roll-go rogue-act" (when class (str " " class)) "'"
       " data-act='" act "'" (when disabled " disabled") ">" label "</button>"))

(defn- bar
  [now most class]
  (let [pct (max 0 (min 100 (js/Math.round (* 100 (/ now (max 1 most))))))]
    (str "<span class='rogue-bar " class "'>"
         "<span class='rogue-bar-fill' style='width:" pct "%'></span>"
         "<span class='rogue-bar-text'>" now " / " most "</span></span>")))

(def room-names
  {:encounter "SOMETHING IS IN THERE"
   :shop "A SHOP"
   :treasure "TREASURE"
   :quest "A NOTICEBOARD"
   :empty "A ROOM"
   :heal "A QUIET ROOM · 10 gold"})

(defn- status-html [state]
  (str "<div class='rogue-line'>"
       "<span class='rogue-key'>FLOOR</span> " (:floor state)
       " · <span class='rogue-key'>LEVEL</span> " (:level state)
       " · <span class='rogue-key'>GOLD</span> " (:gold state)
       " · <span class='rogue-key'>STR</span> " (strength state)
       " · <span class='rogue-key'>SPD</span> " (speed state)
       " · <span class='rogue-key'>DODGE</span> " (dodge state) "%"
       " · <span class='rogue-key'>CRIT</span> " (crit state) "%"
       (when (pos? (poison-of state))
         (str " · <span class='rogue-key'>VENOM</span> " (poison-of state)))
       "</div>"
       (when (or (pos? (or (:shield state) 0)) (pos? (or (:poisoned state) 0)))
         (str "<div class='rogue-line rogue-status-line'>"
              (when (pos? (or (:shield state) 0))
                (str "<span class='rogue-shield'>SHIELD " (:shield state) "</span>"))
              (when (pos? (or (:poisoned state) 0))
                (str "<span class='rogue-poison'>POISONED " (:poisoned state) "</span>"))
              "</div>"))
       "<div class='rogue-line'>"
       (bar (:hp state) (max-hp state) "rogue-hp") " "
       (bar (:energy state) (strong-at state) "rogue-energy") " "
       (bar (:xp state) (xp-needed (:level state)) "rogue-xp")
       "</div>"))

(defn- enemies-html [state]
  (str "<div class='rogue-foes'>"
       (str/join
        ""
        (map (fn [e]
               (let [down (not (pos? (:hp e)))
                     lit (get (:flash state) (:slot e))
                     carried (marks-on e)]
                 (str "<div class='rogue-foe"
                      (when down " rogue-down")
                      (when (:boss e) " rogue-boss")
                      (when (:turned e) " rogue-turned")
                      (when lit (str " rogue-lit rogue-lit-" (name lit)))
                      "'>"
                      "<div class='rogue-foe-head'>"
                      "<span class='rogue-foe-name'>" (:name e) "</span>"
                      (when (seq carried)
                        (str "<span class='rogue-marks'>"
                             (str/join ""
                                       (map (fn [m]
                                              (mark-html
                                               m
                                               (case (:id m)
                                                 :poisoned (:poisoned e)
                                                 :shield (:shield e)
                                                 nil)))
                                            carried))
                             "</span>"))
                      "</div>"
                      (bar (:hp e) (:max-hp e) "rogue-hp")
                      "<div class='rogue-foe-note'>str " (:strength e) "</div>"
                      (when-not down
                        (str "<div class='rogue-foe-acts'>"
                             (button (str "attack-" (:slot e)) "Hit"
                                     :class "rogue-hit")
                             (button (str "strong-" (:slot e)) "Strong"
                                     :class "rogue-hit"
                                     :disabled (< (:energy state) (strong-at state)))
                             "</div>"))
                      "</div>")))
             (:enemies state)))
       "</div>"))

(def ^:private no-stopping
  "Scenes with no way out. A fight is the one place the button is not
  offered, which is the only fair thing in here."
  #{:fight :dead :banked})

(defn- scene-html
  "The room, as markup.

  The default clause goes last. A `case` with a stray form in the middle
  shifts every pair after it, so a default written halfway down does not
  merely sit in the wrong place: it becomes the test value for whatever
  follows, and two scenes below it stop matching at all."
  [state]
  (case (:scene state)
    :doors
    (let [[a b] (:doors state)]
      (str "<div class='rogue-doors'>"
           (button "door-0" (get room-names a "A ROOM") :class "rogue-door")
           (button "door-1" (get room-names b "A ROOM") :class "rogue-door")
           "</div>"))

    :boss-door
    (str "<div class='rogue-doors'>"
         (button "boss"
                 (if (superboss-floor? state)
                   "THE DOOR AT THE END"
                   "THE ONLY DOOR LEFT")
                 :class "rogue-door rogue-door-boss")
         "</div>")

    :fight
    (str (enemies-html state)
         "<div class='live-bar'>"
         (button "defend" "Brace")
         "</div>")

    :shop
    (let [price (js/Math.round (* (get-in @book [:tuning :item-price])
                                  (- 1 (min 0.8 (total state :discount)))))
          n (or (:restocks state) 0)
          shut (:shop-shut state)
          again (refresh-price n)]
      (str "<div class='rogue-shelf'>"
           (str/join ""
                     (map-indexed
                      (fn [i thing]
                        (str "<div class='rogue-stock'>"
                             "<span class='rogue-thing'>" (label-of thing) "</span>"
                             (button (str "buy-" i) (str "Buy · " price " gold")
                                     :disabled (< (:gold state) price))
                             "</div>"))
                      (:stock state)))
           (when (and (empty? (:stock state)) shut)
             "<p class='rogue-price'>Shelves are bare. Somebody will be along.</p>")
           "</div>"
           "<div class='live-bar'>"
           (button "restock"
                   (str "Restock · " again " gold · " (refresh-chance n) "%")
                   :disabled (or shut (< (:gold state) again)))
           (button "leave" "Leave")
           "</div>"))

    :upgrade
    (str (when (> (or (:owed state) 1) 1)
           (str "<p class='rogue-price'>" (:owed state) " to choose.</p>"))
         "<div class='rogue-shelf'>"
         (str/join ""
                   (map-indexed
                    (fn [i thing]
                      (str "<div class='rogue-stock'>"
                           "<span class='rogue-thing'>" (label-of thing) "</span>"
                           (button (str "take-" i) "Take")
                           "</div>"))
                    (:offers state)))
         "</div>")

    :spoils
    (str "<div class='rogue-offer'>"
         "<p class='rogue-thing'>Whatever it was carrying.</p>"
         (if (seq (:spoils state))
           (str "<div class='rogue-shelf'>"
                (str/join ""
                          (map-indexed
                           (fn [i thing]
                             (str "<div class='rogue-stock'>"
                                  "<span class='rogue-thing'>" (label-of thing)
                                  "<span class='rogue-from'>"
                                  (if (= :upgrades (:from thing)) " upgrade" " item")
                                  "</span></span>"
                                  (button (str "spoil-" i) "Take")
                                  "</div>"))
                           (:spoils state)))
                "</div>")
           (str "<p class='rogue-price'>Nothing, as it turns out.</p>"
                "<div class='live-bar'>" (button "on" "On") "</div>"))
         "</div>")

    :quest
    (str "<div class='rogue-offer'>"
         "<p class='rogue-thing'>" (:desc (:offer state)) "</p>"
         "<div class='live-bar'>"
         (button "accept" "Accept")
         (button "decline" "Walk on")
         "</div></div>")

    :sacrifice
    (let [offer (:offer state)
          [what amount] (sacrifice-cost state offer)]
      (str "<div class='rogue-offer rogue-dark'>"
           "<p class='rogue-thing'>" (:desc offer) "</p>"
           "<p class='rogue-price'>It wants "
           (case what
             :item (str amount " item" (when (> amount 1) "s"))
             :upgrade (str amount " upgrade" (when (> amount 1) "s"))
             :life (str amount " maximum health")
             :gold (str amount " gold")
             "nothing")
           ".</p>"
           "<div class='live-bar'>"
           (button "pay" "Pay it")
           (button "refuse" "Refuse")
           "</div></div>"))

    :heal
    (let [{:keys [heal-price paid-heal free-heal scavenge]} (:tuning @book)
          span (fn [range fallback]
                 (let [[low high] (cond
                                    (and (vector? range) (= 2 (count range))) range
                                    (number? range) [range range]
                                    :else fallback)]
                   (if (= low high) (str low) (str low "–" high))))
          hurt (- (max-hp state) (:hp state))]
      (str "<div class='rogue-offer'>"
           "<p class='rogue-thing'>Somewhere to sit down. Nobody comes in.</p>"
           (when (zero? hurt)
             "<p class='rogue-price'>There is nothing wrong with you.</p>")
           "<div class='live-bar'>"
           (button "rest" (str "Pay to rest · " (or heal-price 10)
                               " gold · +" (span paid-heal [14 20]))
                   :disabled (< (:gold state) (or heal-price 10)))
           (button "sit" (str "Sit a while · +" (span free-heal [3 7])))
           (button "scavenge" (str "Scavenge · +" (span scavenge [4 11]) " gold"))
           "</div></div>"))

    :dead
    (str "<div class='rogue-offer rogue-dark'>"
         "<p class='rogue-thing'>You stop.</p>"
         "<p class='rogue-price'>Banked 0. " (could-have state) "</p>"
         "<p class='rogue-price'>" (left-behind state) "</p>"
         "<p class='rogue-price'>Best kept: "
         (or (fedoras.reader/stored storage-key) 0) "</p>"
         "<div class='live-bar'>" (button "again" "Again") "</div></div>")

    :banked
    (str "<div class='rogue-offer'>"
         "<p class='rogue-thing'>Banked " (:banked state)
         " on floor " (:floor state) ".</p>"
         "<p class='rogue-price'>" (left-behind state) "</p>"
         "<p class='rogue-price'>Best kept: "
         (or (fedoras.reader/stored storage-key) 0) "</p>"
         "<div class='live-bar'>" (button "again" "Again") "</div></div>")

    :fortune
    (str "<div class='rogue-offer'>"
         "<p class='rogue-thing'>You got past it. A point of fortune.</p>"
         "<p class='rogue-price'>Fortune held: " (:fortune-held state)
         " · there is another one seven floors from here.</p>"
         "<div class='live-bar'>"
         (button "carry-on" "Keep going")
         "</div></div>")

    (str "<div class='rogue-offer rogue-dark'>"
         "<p class='rogue-thing'>Nothing is here. Scene: "
         (name (:scene state)) "</p>"
         "<div class='live-bar'>" (button "on" "On") "</div></div>")))

(defn- stage-html
  "The room, and under it the way out.

  The way out is under every room that is not a fight, at every moment,
  saying exactly what stopping would be worth. It is the cheapest thing
  in the file and the only part of the game that is about anything."
  [state]
  (str (scene-html state)
       (when-not (contains? no-stopping (:scene state))
         (str "<div class='live-bar rogue-quit'>"
              (button "stop" (str "Stop here · bank " (score state))
                      :class "rogue-stop")
              "</div>"))))

(defn- kit-html [state]
  (let [line (fn [title things]
               (when (seq things)
                 (str "<div class='rogue-kit-row'>"
                      "<span class='rogue-key'>" title "</span> "
                      (str/join " · " (map label-of things))
                      "</div>")))]
    (str (or (line "ITEMS" (:items state)) "")
         (or (line "UPGRADES" (:upgrades state)) "")
         (or (line "SACRIFICES" (:sacrifices state)) "")
         (when (seq (:quests state))
           (str "<div class='rogue-kit-row'>"
                "<span class='rogue-key'>QUESTS</span> "
                (str/join " · "
                          (map (fn [q]
                                 (str (:desc q)
                                      (when (quest-done? state q) " ✓")))
                               (:quests state)))
                "</div>")))))

(defn paint! [node]
  (let [state @game
        lit (get (:flash state) :you)]
    (doseq [kind ["hurt" "shield" "poison"]]
      (.remove (.-classList node) (str "rogue-you-" kind)))
    (when lit (.add (.-classList node) (str "rogue-you-" (name lit))))
    (fedoras.ui.widget-ui/state!
     node (str "on the table " (score state)
               " · banked " (:banked state)
               " · best " (or (fedoras.reader/stored storage-key) 0)))
    (fedoras.ui.widget-ui/html! node ".rogue-status" (status-html state))
    (fedoras.ui.widget-ui/html! node ".rogue-stage" (stage-html state))
    (fedoras.ui.widget-ui/html! node ".rogue-log"
                                (str/join "" (map (fn [l] (str "<p>" l "</p>"))
                                                  (:log state))))
    (fedoras.ui.widget-ui/html! node ".rogue-kit" (kit-html state))
    (fedoras.ui.widget-ui/html!
     node ".rogue-legend"
     (if (= :fight (:scene state))
       (let [showing (set (mapcat (fn [e] (map :id (marks-on e))) (:enemies state)))]
         (fedoras.ui.legend/html
          (filterv (fn [m] (contains? showing (:id m))) marks)))
       ""))))

(defn- play!
  "Play a queue of states back, each held for a beat and then cleared.

  Every path out of here lands on an unflashed state. A pending timeout
  that finds the reel gone used to simply bail, which left whatever frame
  was on screen lit up for good, and pressing a button during playback
  did exactly that."
  [node states done]
  (let [hold (or (get-in @book [:tuning :flash-hold]) 260)
        gap (or (get-in @book [:tuning :flash-gap]) 90)]
    (reset! reel {:queue (vec states) :pending done})
    (letfn [(finish []
              (reset! reel nil)
              (reset! game done)
              (paint! node))
            (next-frame []
              (if-let [r @reel]
                (if-let [frame (first (:queue r))]
                  (do (reset! game frame)
                      (swap! reel update :queue (fn [q] (vec (rest q))))
                      (paint! node)
                      (js/setTimeout
                       (fn []
                         (if @reel
                           (do (swap! game unflash)
                               (paint! node)
                               (js/setTimeout next-frame gap))
                           (finish)))
                       hold))
                  (finish))
                (finish)))]
      (if (seq states)
        (next-frame)
        (finish)))))

(defn- skip!
  "Jump to the end of an exchange. Lands unflashed, like every other way
  out of a playback."
  [node]
  (when-let [r @reel]
    (let [done (:pending r)]
      (reset! reel nil)
      (reset! game done)
      (paint! node))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn holding?
  [state thing]
  (boolean (some (fn [held] (= (:id held) (:id thing)))
                 (concat (:items state) (:upgrades state)))))

(defn take-thing
  "Add one thing to the kit, and honour the ones that do something the
  moment you take them.

  Most things stack, because two of a thing being twice as good is the
  simplest rule and the one a reader will assume. A thing that cannot
  stack says `:unique true`, and there are few of those on purpose."
  [state where thing]
  (if (and (:unique thing) (holding? state thing))
    (say state "You already have one of those.")
    (-> state
        (update where conj thing)
        (as-> s (if (:heal-now thing) (assoc s :hp (max-hp s)) s))
        (as-> s (assoc s :hp (min (max-hp s) (:hp s)))))))

(defn resolve-turn
  "One exchange, a frame per blow.

  A frame is one thing landing, not one phase of the round. Three enemies
  hitting you is three frames; your two strikes are two. Anything that
  changes nothing visible is not a frame at all, so a round with no
  poison and no summons has exactly as many beats as it had blows.

  The cooldown ticks at the end of the round rather than at the start, so
  an ability with a cooldown of one is usable every other turn. An
  ability usable every turn is a fourth button, not an ability."
  [state what slot]
  (let [before (:enemies state)
        frames (atom [])
        keep! (fn [s]
                (when (seq (:flash s)) (swap! frames conj s))
                s)
        opened (unflash state)
        after-you
        (case what
          :attack (player-attack-framed opened slot false keep!)
          :strong (player-attack-framed opened slot true keep!)
          :defend (-> opened (assoc :defending true) (say "You brace."))
          opened)
        poisoned (keep! (tick-poison (unflash after-you)))
        tallied (count-kills poisoned before)]
    (cond
      (<= (:hp tallied) 0)
      {:frames @frames :final (-> tallied unflash (assoc :scene :dead :hp 0))}

      (empty? (living tallied))
      {:frames @frames :final (win-fight tallied)}

      :else
      (let [struck (reduce (fn [acc enemy]
                             (keep! (enemy-turn-one (unflash acc) enemy)))
                           tallied
                           (living tallied))
            summoned (keep! (summon! (unflash struck)))
            counted (count-kills summoned (:enemies struck))
            ended (cool-down (unflash (end-round counted)))]
        (cond
          (<= (:hp ended) 0)
          {:frames @frames :final (assoc ended :scene :dead :hp 0)}

          (empty? (living ended))
          {:frames @frames :final (win-fight ended)}

          :else {:frames @frames :final ended})))))

(defn handle
  "One press.

  The prefixed ones are in the `cond`, because they carry a number on the
  end and `case` matches whole values; the fixed ones are in the `case`
  below it.

  Most branches return a state. The three that start a fight exchange
  return `{:exchange true :frames [...] :final state}` instead, so `act!`
  can show the exchange happening rather than only its result."
  [state what]
  (cond
    (str/starts-with? what "door-")
    (let [i (js/parseInt (subs what 5) 10)
          taken (nth (:doors state) i)
          skipped (nth (:doors state) (- 1 i))]
      (-> state
          (update-in [:skipped skipped] (fnil inc 0))
          (open taken)))

    (str/starts-with? what "buy-")
    (let [i (js/parseInt (subs what 4) 10)
          thing (nth (:stock state) i)
          price (js/Math.round (* (get-in @book [:tuning :item-price])
                                  (- 1 (min 0.8 (total state :discount)))))]
      (if (< (:gold state) price)
        state
        (-> state
            (update :gold - price)
            (take-thing :items thing)
            (update :stock (fn [s] (vec (concat (take i s) (drop (inc i) s)))))
            (say "Bought " (label-of thing) "."))))

    (str/starts-with? what "take-")
    (let [i (js/parseInt (subs what 5) 10)
          thing (nth (:offers state) i)
          owed (max 0 (dec (or (:owed state) 1)))
          taken (-> state
                    (take-thing :upgrades thing)
                    (say "Learned " (label-of thing) ".")
                    (assoc :owed owed))]
      (if (pos? owed)
        (assoc taken :offers (vec (take 3 (shuffled (:upgrades @book)))))
        (-> taken
            (dissoc :offers)
            (assoc :scene (or (:held-scene state) :advance))
            (dissoc :held-scene))))

    (str/starts-with? what "spoil-")
    (let [i (js/parseInt (subs what 6) 10)
          thing (nth (:spoils state) i)
          where (:from thing)]
      (-> state
          (take-thing where (dissoc thing :from))
          (say "Took " (label-of thing) ".")
          (dissoc :spoils)
          (assoc :scene :next-floor)))

    (str/starts-with? what "attack-")
    (let [slot (js/parseInt (subs what 7) 10)
          {:keys [frames final]} (resolve-turn state :attack slot)]
      {:exchange true
       :frames frames
       :final (if (= :dead (:scene final))
                (assoc final :final-score (keep-score! final))
                (check-level final))})

    (str/starts-with? what "strong-")
    (let [slot (js/parseInt (subs what 7) 10)]
      (if (< (:energy state) (strong-at state))
        state
        (let [{:keys [frames final]} (resolve-turn state :strong slot)]
          {:exchange true
           :frames frames
           :final (if (= :dead (:scene final))
                    (assoc final :final-score (keep-score! final))
                    (check-level final))})))

    :else
    (case what
      "boss" (start-fight state (if (superboss-floor? state)
                                  (make-superboss state)
                                  (make-boss state)))

      "defend"
      (let [{:keys [frames final]} (resolve-turn state :defend nil)]
        {:exchange true
         :frames frames
         :final (if (= :dead (:scene final))
                  (assoc final :final-score (keep-score! final))
                  (check-level final))})

      "cast"
      (if-not (ready? state)
        state
        (let [frames (atom [])
              keep! (fn [s]
                      (when (seq (:flash s)) (swap! frames conj s))
                      s)
              after (use-ability state keep!)]
          {:exchange true :frames @frames :final (unflash after)}))

      "leave" (assoc state :scene :advance)

      "restock"
      (let [n (or (:restocks state) 0)
            price (refresh-price n)]
        (if (< (:gold state) price)
          state
          (let [paid (-> state
                         (update :gold - price)
                         (update :restocks (fnil inc 0)))]
            (if (chance? (refresh-chance n))
              (-> paid
                  (assoc :stock (vec (take 3 (shuffled (:items @book)))))
                  (say "New stock. " price " gold."))
              (-> paid
                  (assoc :stock [])
                  (assoc :shop-shut true)
                  (say "Nothing left to restock with. That was " price " gold."))))))

      "rest"
      (let [price (or (get-in @book [:tuning :heal-price]) 10)]
        (if (< (:gold state) price)
          state
          (let [amount (between (get-in @book [:tuning :paid-heal]) [14 20])]
            (-> state
                (update :gold - price)
                (update :hp (fn [hp] (min (max-hp state) (+ hp amount))))
                (say "You sit down properly. " price " gold, and " amount " back.")
                (assoc :scene :advance)))))

      "sit"
      (let [amount (between (get-in @book [:tuning :free-heal]) [3 7])]
        (-> state
            (update :hp (fn [hp] (min (max-hp state) (+ hp amount))))
            (say "You sit down for a bit. " amount " back.")
            (assoc :scene :advance)))

      "scavenge"
      (let [rolled (between (get-in @book [:tuning :scavenge]) [4 11])
            amount (max 1 (js/Math.round (* rolled (+ 1 (total state :gold-mult)))))]
        (-> state
            (update :gold + amount)
            (say "You go through the drawers. " amount " gold.")
            (assoc :scene :advance)))

      "accept"
      (-> state
          (update :quests conj (:offer state))
          (say "Taken on: " (:desc (:offer state)))
          (dissoc :offer)
          (assoc :scene :advance))

      "decline"
      (-> state
          (say "You read the board and keep walking.")
          (dissoc :offer)
          (assoc :scene :advance))

      "pay"
      (let [offer (:offer state)
            [kind amount] (sacrifice-cost state offer)
            paid (case kind
                   :item (update state :items (fn [xs] (vec (drop amount xs))))
                   :upgrade (update state :upgrades (fn [xs] (vec (drop amount xs))))
                   :life (-> state
                             (update :sacrifices conj {:max-hp (- amount)})
                             (update :hp (fn [hp] (max 1 (- hp amount)))))
                   :gold (update state :gold - amount)
                   state)]
        (-> paid
            (update :sacrifices conj offer)
            (say "It takes what it asked for.")
            (dissoc :offer)
            (assoc :scene :next-floor)))

      "refuse"
      (-> state
          (say "You keep what you have.")
          (dissoc :offer)
          (assoc :spoils (spoils state))
          (assoc :scene :spoils))

      "carry-on"
      (-> state (dissoc :fortune-held) (assoc :scene :next-floor))

      "stop" (bank! state)

      "again" (fresh-run (.now js/Date))

      "on" (assoc state :scene :advance)

      state)))

(defn- act!
  "One press, and everything that follows from it without another press.

  `handle` returns either a state or an exchange, which is a list of
  frames to show in order. A press while one is playing skips to its end,
  because a reader who is clicking wants it over with."
  [node what]
  (if @reel
    (skip! node)
    (let [outcome (handle @game what)]
      (if (:exchange outcome)
        (let [final (:final outcome)
              moved (if (contains? #{:advance :after-boss :next-floor} (:scene final))
                      (advance final)
                      final)
              moved (if (= :fortune (:scene moved))
                      (assoc moved :fortune-held (award-fortune!))
                      moved)]
          (play! node (:frames outcome) moved))
        (let [moved (if (contains? #{:advance :after-boss :next-floor} (:scene outcome))
                      (advance outcome)
                      outcome)]
          (reset! game (if (= :fortune (:scene moved))
                         (assoc moved :fortune-held (award-fortune!))
                         moved))
          (paint! node))))))

(defn- wire-stage! [node]
  (when-let [stage (fedoras.ui.widget-ui/q! node ".rogue-stage")]
    (set! (.-onclick stage)
          (fn [event]
            (when-let [pressed (.closest (.-target event) ".rogue-act")]
              (when-not (.-disabled pressed)
                (act! node (.getAttribute pressed "data-act"))))))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (reset! book (merge default-book setting))
     (reset! game (fresh-run (.now js/Date)))
     (paint! node))))

(defn setup!
  "Once per element. The settings callback lays out a fresh run, because
  changing the rules mid-run would be the one unfair thing in a game
  built entirely out of unfairness."
  [node]
  (let [id (.-id node)]
    (fedoras.ui.widget-ui/wire! node "stage" wire-stage!)
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))))

(defn mount!
  [{:keys [id]}]
  (when (nil? @book) (reset! book default-book))
  (when (nil? @game) (reset! game (fresh-run 4471)))
  (fedoras.ui.widget-ui/mount!
   ::rogue (or id "rogue") setup! paint!))