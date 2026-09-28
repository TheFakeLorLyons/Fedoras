(ns fedoras.ui.dice
  "The d20, the Echo, and the rewrite button, in the page.

  Loaded once per chapter by `fedoras.dice/assets`. Each check on the
  page then emits one line calling `mount!` with its own data, so the
  outcome text is passed as Clojure rather than smuggled through
  data- attributes and parsed back out.

  Rolling stores the result. The reader may rewrite any
  result they like, and each rewrite raises the Echo by one, and the
  Echo is a running penalty on every check afterwards, in this chapter
  and every chapter after it, for as long as they keep their browser.

  Free the first time, cheap the second, and by the fifth rewrite they
  are playing a worse game than the one they started with, and nothing
  has warned them except a number going up."
  (:require [clojure.string :as str]
            [fedoras.reader :as read]))

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
;; Persistence
;; ---------------------------------------------------------------------

(defn- entry [id] (read/roll-entry id))

;; ---------------------------------------------------------------------
;; The passage
;; ---------------------------------------------------------------------

(defn- segment-node [seg]
  (let [cue? (str/starts-with? seg "@")
        node (.createElement js/document "p")]
    (set! (.-className node) (if cue? "roll-cue" "roll-line"))
    (set! (.-textContent node)
          (read/fill (if cue? (subs seg 1) seg)))
    node))

(defn- paint-passage! [node text]
  (let [box (read/q node ".roll-out")]
    (set! (.-innerHTML box) "")
    (doseq [seg (str/split (or text "") #"\|")]
      (.appendChild box (segment-node seg)))))

;; ---------------------------------------------------------------------
;; The two states a box can be in
;; ---------------------------------------------------------------------

(defn- clear! [node]
  (read/text! node ".roll-value" "")
  (set! (.-innerHTML (read/q node ".roll-out")) "")
  (read/text! node ".roll-note" "")
  (read/text! node ".roll-echo" "")
  (read/enable! node ".roll-go" true "Roll d20")
  (read/show! node ".roll-rewrite" false))

(defn- fill! [node {:keys [dc outcomes]} {:keys [roll mod rewrites]}]
  (let [b (band roll dc mod)]
    (read/text! node ".roll-value" (line roll dc mod))
    (paint-passage! node (or (get outcomes b) (get outcomes :fail)))
    (read/text! node ".roll-note"
                (if (zero? rewrites)
                  ""
                  (str "This is version " (inc rewrites) " of what happened.")))
    (read/enable! node ".roll-go" false "Rolled")
    (read/show! node ".roll-rewrite" true)))

(defn- paint! [node check]
  (if-let [e (entry (:id check))]
    (fill! node check e)
    (clear! node))
  (read/text! node ".echo-count" (str (read/echo)))
  (read/text! node ".score-count" (str (read/score))))

;; ---------------------------------------------------------------------
;; Two things a reader does
;; ---------------------------------------------------------------------

(defn- resolve!
  "One die, and what it costs and is worth.
 
  The score is not touched here. It is the sum of what is standing, and
  what is standing is what this line just wrote."
  [node check rewrites]
  (let [natural  (inc (rand-int 20))
        modifier (read/standing)]
    ;; a twenty is worth a deed, but only to somebody who has taken
    ;; nothing back. deeds are a set, so it is worth one, once, ever.
    (when (and (= 20 natural) (zero? (read/echo)))
      (read/did! :nerve))
    (read/remember-roll!
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
    (read/add-echo!)
    (resolve! node check (inc (:rewrites e)))
    (read/text! node ".roll-echo" "Something remembers.")))

;; ---------------------------------------------------------------------

(defn mount! [check]
  (read/widget
   (keyword "roll" (:id check))
   (str "roll-" (:id check))
   (fn [node]
     (paint! node check)
     (read/on-click! node ".roll-go"      (fn [_] (roll-once! node check)))
     (read/on-click! node ".roll-rewrite" (fn [_] (rewrite! node check))))))