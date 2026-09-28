(ns fedoras.ui.companions
  "The ranger's animal, in the page.

  Loaded once by `fedoras.companions/table`, which then emits one line
  handing over the roster, so the twenty arrival passages are passed as
  Clojure rather than stringified into the markup."
  (:require [clojure.string :as str]))

(defn- foot-note
  "The line under the box."
  [echo]
  (cond
    (zero? echo) "Written on the sheet in ink."
    (< echo 3)   "Written on the sheet in ink. There is something under it."
    :else        (str "Written on the sheet in ink, over " echo " other things.")))

(defn- shown-name
  [{:keys [name animal]}]
  (cond
    (str/blank? animal)                              (str/upper-case name)
    (= (str/lower-case name) (str/lower-case animal)) (str/upper-case name)
    :else (str (str/upper-case name) " · " (str/lower-case animal))))

(defn- kind-line
  "What the ledger would have written in the other column. Only shown
  once the roster has landed."
  [{:keys [taxonomy plural collective]}]
  (when-not (str/blank? taxonomy)
    (str (str/capitalize taxonomy)
         (when-not (str/blank? collective)
           (str ". More than one is " (str/lower-case collective) "."))
         (when (and (str/blank? collective) (not (str/blank? plural)))
           (str ". Plural: " (str/lower-case plural) ".")))))

(defn- clear! [node]
  (fedoras.reader/text! node ".companion-roll" "")
  (fedoras.reader/text! node ".companion-name" "")
  (fedoras.reader/text! node ".companion-kind" "")
  (fedoras.reader/text! node ".companion-intro" "")
  (fedoras.reader/enable! node ".companion-go" true "Roll for your animal")
  (fedoras.reader/enable! node ".companion-reroll" false nil)
  (fedoras.reader/show! node ".companion-naming" false))

(defn- fill! [node roster {:keys [n given] :as c}]
  (let [intro (get-in roster [n :intro] "")
        input (fedoras.reader/q node ".companion-input")]
    (fedoras.reader/enable! node ".companion-go" false "Rolled")
    (fedoras.reader/enable! node ".companion-reroll" true nil)
    (fedoras.reader/show! node ".companion-naming" true)
    ;; not while they are typing in it. this runs on every render, and a
    ;; render can be triggered by something else on the page.
    (when (and input (not= input (.-activeElement js/document)))
      (set! (.-value input) (or given "")))
    (fedoras.reader/text! node ".companion-roll" (str "d20 → " n))
    (fedoras.reader/text! node ".companion-name" (shown-name c))
    (fedoras.reader/text! node ".companion-kind" (or (kind-line c) ""))
    ;; the shared filler, so an arrival passage can use every token a
    ;; scene can. it used to know about {pc} and nothing else.
    (fedoras.reader/text! node ".companion-intro" (fedoras.reader/fill intro))))

(defn mount!
  "Wire the roll box. `roster` is the twenty entries, as Clojure."
  [roster]
  (fedoras.reader/widget
   ::companion "companion"
   (fn [node]
     (if-let [c (fedoras.reader/companion)] (fill! node roster c) (clear! node))
     (fedoras.reader/text! node ".roll-foot" (foot-note (fedoras.reader/echo)))

     (fedoras.reader/on-click! node ".companion-go"
                               (fn [_]
                                 (when-not (fedoras.reader/companion)
                                   (fedoras.reader/set-companion! (inc (rand-int 20)) ""))))

     (fedoras.reader/on-click! node ".companion-reroll"
                               (fn [_]
                                 (fedoras.reader/set-echo! (inc (fedoras.reader/echo)))
                                 (fedoras.reader/set-companion! (inc (rand-int 20)) "")))

     (fedoras.reader/on-click! node ".companion-name-go"
                               (fn [_]
                                 (when-let [cur (fedoras.reader/companion)]
                                   (let [given (.-value (fedoras.reader/q node ".companion-input"))]
                                     (fedoras.reader/set-companion! (:n cur) given)
             ;; naming it is a deed. it is not a large one and nothing
             ;; anywhere says it happened, which is the arrangement.
                                     (when-not (str/blank? given)
                                       (fedoras.reader/did! :named-it)))))))))