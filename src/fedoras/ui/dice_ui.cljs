(ns fedoras.ui.dice-ui
  "A d20 the reader can roll once.

  ROLL ONCE. It used to let you press the button all afternoon. The
  handler had no guard and the box painted nothing from storage, so a
  roll was neither kept nor refused. It is kept now, the button is spent
  once it has been pressed, and the only other door is the expensive
  one.

  STANDING IS THE MODIFIER. Fortune less echo, added to every roll. It
  is stored with the roll rather than read live, because raising the
  Echo in Act IV must not quietly reword a scene the reader finished in
  Act II.

  THE PASSAGE MAY SAY THE NUMBER. Every outcome string is rendered with
  the die in scope, so a line can say {result} and mean the number that
  was actually rolled rather than the number the author guessed. This is
  the reason the passages used to lie: a success wrote `rolls a
  fourteen` whatever the reader rolled, and a reader who rolled
  seventeen was told, in their own book, that they had not.

  A passage is still allowed to be vague. `rolls low` is a choice now
  rather than the only option.

  SCORE. Every die adds its face to the score, including the ones rolled
  again. That is the temptation: a rewrite raises the score and raises
  the Echo, and only one of those two is fatal.

  FORTUNE. A natural twenty is worth a deed, but only while the Echo is
  nought. So the reader who has never taken anything back is the only
  one who can still be lucky, and nothing anywhere says so."
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; What a roll is worth
;; ---------------------------------------------------------------------

(defn band
  "One and twenty are themselves. Everything between them is a sum."
  [natural dc modifier]
  (cond
    (= natural 1)                 :fumble
    (= natural 20)                :crit
    (>= (+ natural modifier) dc)  :success
    :else                         :fail))

(defn line
  "What was rolled, what it came to, and what that was."
  [natural dc modifier]
  (str "d20 → " natural
       (cond
         (pos? modifier) (str "   fortune +" modifier "   →  " (+ natural modifier))
         (neg? modifier) (str "   echo " modifier "   →  " (+ natural modifier))
         :else "")
       "   "
       (case (band natural dc modifier)
         :fumble "FUMBLE" :crit "CRIT" :success "success" :fail "failure")))

;; ---------------------------------------------------------------------
;; The number, in words and in figures
;; ---------------------------------------------------------------------

(def ^:private spelled
  ["zero" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"
   "ten" "eleven" "twelve" "thirteen" "fourteen" "fifteen" "sixteen"
   "seventeen" "eighteen" "nineteen" "twenty"])

(defn- in-words [n]
  (if (and (number? n) (<= 0 n 20)) (nth spelled n) (str n)))

(defn- magnitude [n] (if (neg? n) (- n) n))

(defn- signed [n]
  (cond (pos? n) (str "+" n)
        (neg? n) (str "−" (magnitude n))
        :else    ""))

(defn- arithmetic
  "17 + 1 = 18, and just 17 when there is nothing to add, so a line that
  uses this reads correctly for a reader whose standing is nought — most
  readers, most of the time."
  [natural modifier]
  (if (zero? modifier)
    (str natural)
    (str natural " " (if (pos? modifier) "+" "−") " " (magnitude modifier)
         " = " (+ natural modifier))))

(defn roll-tokens
  "What a passage is allowed to say about the die that produced it.

  {result} is the face, because that is what somebody at a table says
  out loud. {total} is the number the DC was measured against, which is
  a different fact and usually the less interesting one."
  [dc {:keys [roll mod]}]
  (let [total (+ roll mod)]
    (fn [head tail]
      (case head
        "result" (case (or tail "")
                   ""      (str roll)
                   "-word" (in-words roll)
                   "-mod"  (arithmetic roll mod)
                   nil)
        "total"  (case (or tail "")
                   ""      (str total)
                   "-word" (in-words total)
                   nil)
        "mod"    (when (nil? tail) (signed mod))
        "dc"     (when (nil? tail) (str dc))
        nil))))

;; ---------------------------------------------------------------------
;; Remembering
;; ---------------------------------------------------------------------

(defn- entry [id] (fedoras.reader/roll-entry id))

;; ---------------------------------------------------------------------
;; The passage
;; ---------------------------------------------------------------------

(defn- segment-node [tokens seg]
  (let [cue? (str/starts-with? seg "@")
        node (.createElement js/document "p")]
    (set! (.-className node) (if cue? "roll-cue" "roll-line"))
    (set! (.-textContent node)
          (fedoras.reader/fill-with tokens (if cue? (subs seg 1) seg)))
    node))

(defn- paint-passage! [node tokens text]
  (let [box (fedoras.reader/q node ".roll-out")]
    (set! (.-innerHTML box) "")
    (doseq [seg (str/split (or text "") #"\|")]
      (.appendChild box (segment-node tokens seg)))))

;; ---------------------------------------------------------------------
;; The two states a box can be in
;; ---------------------------------------------------------------------

(defn- clear! [node]
  (fedoras.reader/text! node ".roll-value" "")
  (set! (.-innerHTML (fedoras.reader/q node ".roll-out")) "")
  (fedoras.reader/text! node ".roll-note" "")
  (fedoras.reader/text! node ".roll-echo" "")
  (fedoras.reader/enable! node ".roll-go" true "Roll d20")
  (fedoras.reader/show! node ".roll-rewrite" false))

(defn- fill! [node {:keys [dc outcomes]} {:keys [roll mod rewrites] :as e}]
  (let [b (band roll dc mod)]
    (fedoras.reader/text! node ".roll-value" (line roll dc mod))
    (paint-passage! node (roll-tokens dc e)
                    (or (get outcomes b) (get outcomes :fail)))
    (fedoras.reader/text! node ".roll-note"
                          (if (zero? rewrites)
                            ""
                            (str "This is version " (inc rewrites) " of what happened.")))
    (fedoras.reader/enable! node ".roll-go" false "Rolled")
    (fedoras.reader/show! node ".roll-rewrite" true)))

(defn- paint! [node check]
  (if-let [e (entry (:id check))]
    (fill! node check e)
    (clear! node))
  (fedoras.reader/text! node ".echo-count" (str (fedoras.reader/echo)))
  (fedoras.reader/text! node ".score-count" (str (fedoras.reader/score))))

;; ---------------------------------------------------------------------
;; The two things a reader can do
;; ---------------------------------------------------------------------

(defn- resolve!
  "One die, and what it costs and is worth.

  The score is not touched here. It is the sum of what is standing, and
  what is standing is what this line just wrote."
  [node check rewrites]
  (let [natural  (inc (rand-int 20))
        modifier (fedoras.reader/standing)]
    ;; a twenty is worth a deed, but only to somebody who has taken
    ;; nothing back. deeds are a set, so it is worth one, once, ever.
    (when (and (= 20 natural) (zero? (fedoras.reader/echo)))
      (fedoras.reader/did! :nerve))
    (fedoras.reader/remember-roll!
     (:id check) {:roll natural :mod modifier :rewrites rewrites})
    (paint! node check)))

(defn- roll-once! [node check]
  (when-not (entry (:id check))
    (resolve! node check 0)))

(defn- rewrite!
  "Raise the Echo first, then roll under it, so the new number is
  penalised by the cost of having asked for it."
  [node check]
  (when-let [e (entry (:id check))]
    (fedoras.reader/add-echo!)
    (resolve! node check (inc (:rewrites e)))
    (fedoras.reader/text! node ".roll-echo" "Something remembers.")))

;; ---------------------------------------------------------------------

(defn mount! [check]
  (fedoras.reader/widget
   (keyword "roll" (:id check))
   (str "roll-" (:id check))
   (fn [node]
     (paint! node check)
     (fedoras.reader/on-click! node ".roll-go"      (fn [_] (roll-once! node check)))
     (fedoras.reader/on-click! node ".roll-rewrite" (fn [_] (rewrite! node check))))))