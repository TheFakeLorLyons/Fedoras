(ns fedoras.cs-class.dnd.dnd-ui
  "Session Four, the part that is on screen.

  CHOICES ARE THE READER'S OWN WORDS. Victor writes them in the first
  person and one tap says them, so a button is not a menu option; it is
  the reader speaking without having to type. The box stays for
  everything nobody offered, and `say!` cannot tell which it came from.

  DELEGATION. One handler on each container, resolving to whichever child
  was hit, rather than a handler per element rewired on every repaint.
  The index comes back off the element, which means a repaint mid-click
  cannot hand a stale closure the wrong option.

  THE PLATE. Where they are, what they have left, and whether it is
  thinking, on one line in the header, which is the one line a reader
  learns to check."
  (:require [clojure.string :as str]
            [fedoras.cs-class.dnd.dnd-table :as table]))

;; ---------------------------------------------------------------------
;; Marking what the table decided mattered
;; ---------------------------------------------------------------------

(def ledger-stopwords
  #{"The" "This" "That" "They" "Then" "There" "With" "When" "What" "Which"
    "Ledger" "Approved" "Column" "Note" "Party" "Nobody" "Something"})

(defn- escape-html [text]
  (-> (str text)
      (str/replace "&" "&amp;")
      (str/replace "<" "&lt;")
      (str/replace ">" "&gt;")))

(defn- ledger-terms
  "Taken out of what Miles wrote, so a marked word in the transcript is
  always a word somebody chose to write down. Nothing is put in here by
  the page."
  []
  (->> @table/ledger
       (mapcat (fn [line] (re-seq #"[A-Z][A-Za-z'-]{3,}" line)))
       distinct
       (remove (fn [word] (contains? ledger-stopwords word)))
       vec))

(defn- mark-terms [escaped]
  (reduce (fn [acc term]
            (str/replace acc term (str "<span class='kept'>" term "</span>")))
          escaped
          (ledger-terms)))

;; ---------------------------------------------------------------------
;; The transcript
;; ---------------------------------------------------------------------

(defn- line-html [entry]
  (cond
    (:end-marker? entry)
    (str "<p class='dnd-end'>" (escape-html (:text entry)) "</p>")

    (= :slip (:speaker entry))
    (str "<p class='slip'>" (escape-html (:text entry)) "</p>")

    (= :table (:speaker entry))
    (str "<p class='roll-value dnd-die'>" (escape-html (:text entry)) "</p>")

    :else
    (str "<p class='" (if (= :pc (:speaker entry)) "roll-cue" "roll-line") "'>"
         (table/label-for (:speaker entry)) "</p>"
         "<p class='dnd-said'>" (mark-terms (escape-html (:text entry))) "</p>")))

(defn- transcript-html
  "The pending voice is named rather than described, because three calls
  go out per turn and `thinking` for the whole chain tells the reader
  nothing about how much of it is left."
  []
  (str (apply str (map line-html @table/log))
       (cond
         @table/speaking
         (str "<p class='roll-line'>" (table/label-for @table/speaking) "</p>"
              "<p class='dnd-thinking'>thinking</p>")

         @table/busy?
         "<p class='dnd-thinking'>the table is thinking</p>"

         :else "")))

(defn- companion-label
  "TED · marmot when the reader named it, marmot when they did not. The
  species is always there, because the name is a courtesy and the roll
  is the fact."
  []
  (if-let [{:keys [name animal named?]} (fedoras.reader/companion)]
    (if named?
      (str name " \u00b7 " (str/lower-case animal))
      (str/lower-case animal))
    "no companion"))

(defn- sheet-html
  "What the reader is, on one line, out of what fedoras.reader already
  knows. Standing appears here as what it actually does to a die, and
  never as the word standing."
  []
  (let [[given surname] (fedoras.reader/reader-name)
        modifier        (fedoras.reader/standing)]
    (str "<div class='dnd-sheet'>"
         "<span class='dnd-sheet-name'>" (escape-html (str given " " surname)) "</span>"
         "<span>RANGER</span>"
         "<span>" (escape-html (companion-label)) "</span>"
         "<span>Provisional #" (fedoras.reader/registration) "</span>"
         "<span class='dnd-sheet-mod'>"
         (cond
           (pos? modifier) (str "+" modifier " on every roll")
           (neg? modifier) (str modifier " on every roll")
           :else           "no modifier")
         "</span>"
         "</div>")))

(defn- animal-html []
  (if-let [doing @table/animal]
    (str "<p class='dnd-animal'>"
         (str/upper-case (or (fedoras.reader/animal-label) "the animal"))
         " &middot; " (escape-html doing) "</p>")
    ""))

;; ---------------------------------------------------------------------
;; The three things a reader can press
;; ---------------------------------------------------------------------

(defn- rolls-html []
  (cond
    (seq @table/roll-options)
    (str "<div class='dnd-rolls'>"
         "<p class='dnd-rolls-title'>HOW DO YOU WANT TO DO IT</p>"
         (apply str
                (map-indexed
                 (fn [index option]
                   (str "<div class='roll-bar dnd-approach' data-roll='" index "'>"
                        "<button class='roll-go'>Roll d20</button>"
                        "<span class='roll-approach'>" (escape-html (:label option)) "</span>"
                        "<span class='roll-dc'>DC " (:dc option) "</span>"
                        "</div>"))
                 @table/roll-options))
         "</div>")

    @table/roll-result
    (str "<div class='live-bar'>"
         "<button class='roll-rewrite dnd-reroll'>Roll it again</button>"
         "<span class='dnd-cost'>raises the Echo</span>"
         "</div>")

    :else ""))

(defn- choices-html []
  (if (seq @table/choices)
    (str "<div class='dnd-choices'>"
         (apply str
                (map-indexed
                 (fn [index text]
                   (str "<button class='dnd-choice' data-choice='" index "'>"
                        (escape-html text) "</button>"))
                 @table/choices))
         "</div>")
    ""))

(defn- ledger-html []
  (if (seq @table/ledger)
    (str "<div class='dnd-ledger'>"
         "<p class='dnd-ledger-title'>THE LEDGER</p>"
         (apply str (map (fn [line] (str "<p>" (escape-html line) "</p>"))
                         (take-last 12 @table/ledger)))
         "</div>")
    ""))

(defn- bar-html []
  (cond
    @table/ended?
    (str "<div class='sign'>"
         "<p>" (get table/ending-line (fedoras.reader/ending-band)) "</p>"
         "<p class='roll-value'>" (fedoras.reader/carry-code) "</p>"
         "<p>Write that down somewhere. Somebody is going to ask you for it.</p>"
         "</div>"
         "<div class='live-bar'><button class='roll-go dnd-save'>Save the ledger</button></div>")

    (nil? @table/api-key)
    (str "<p>You are the Ranger. The other five seats are live, and one of them is on "
         "the table. The key stays in this tab.</p>"
         "<div class='live-bar'>"
         "<input class='todo-input dnd-key' type='password' style='flex:1' "
         "placeholder='sk-ant-...' autocomplete='off'>"
         "<button class='roll-go dnd-connect'>Connect</button>"
         "</div>")

    (not (table/started?))
    (str "<div class='live-bar'><button class='roll-go dnd-begin'>Open the first door</button></div>")

    :else
    (str "<div class='live-bar'>"
         "<input class='todo-input dnd-say' style='flex:1' "
         "placeholder='or say something else' spellcheck='false'>"
         "<button class='roll-go dnd-send'>Say it</button>"
         (if (table/retellable?)
           "<button class='roll-rewrite dnd-retell'>Say it differently</button>"
           "")
         "<button class='roll-go dnd-save'>Save it</button>"
         "<button class='roll-rewrite dnd-forget'>Start again</button>"
         "</div>")))

;; ---------------------------------------------------------------------
;; The plate
;; ---------------------------------------------------------------------

(defn- plate []
  (let [where (str/replace (name (:id (table/current-station))) "-" " ")
        left  @table/nerve
        boxes (str (apply str (repeat left "\u25a0"))
                   (apply str (repeat (- (table/nerve-max) left) "\u25a1")))]
    (cond
      @table/ended?             (str "over  " boxes)
      (not (table/started?))    "press Run"
      @table/busy?              (str where "  " boxes "  thinking")
      :else                     (str where "  " boxes))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn- paint! [node]
  (fedoras.ui.widget-ui/state! node (plate))
  (fedoras.ui.widget-ui/html! node ".dnd-sheet-row" (sheet-html))
  (fedoras.llm/stick-scroll!
   (str (.-id node) "-log")
   (fn []
     (fedoras.ui.widget-ui/html! node ".dnd-log" (transcript-html))))
  (fedoras.ui.widget-ui/html! node ".dnd-under" (str (animal-html) (rolls-html) (choices-html)))
  (fedoras.ui.widget-ui/html! node ".dnd-bar" (bar-html))
  (fedoras.ui.widget-ui/html! node ".dnd-record" (ledger-html))
  (fedoras.ui.widget-ui/html! node ".dnd-foot"
                              (str "Echo: " (fedoras.reader/echo)
                                   "   &middot;   Fortune: " (fedoras.reader/fortune)
                                   "   &middot;   Score: " (fedoras.reader/score))))

;; ---------------------------------------------------------------------
;; Wiring
;;
;; Delegated onto containers that survive a repaint, so nothing here has
;; to be rewired when the markup underneath it is replaced.
;; ---------------------------------------------------------------------

(defn- send-box! [node]
  (when-let [box (fedoras.reader/q node ".dnd-say")]
    (table/say! (.-value box))
    (set! (.-value box) "")))

(defn- wire-under! [node]
  (when-let [container (fedoras.ui.widget-ui/q! node ".dnd-under")]
    (set! (.-onclick container)
          (fn [event]
            (let [target (.-target event)]
              (when-let [chosen (.closest target ".dnd-choice")]
                (table/say! (.-textContent chosen)))
              (when-let [approach (.closest target ".dnd-approach")]
                (let [index (js/parseInt (.getAttribute approach "data-roll") 10)]
                  (when-let [option (nth @table/roll-options index nil)]
                    (table/roll! option 0))))
              (when (.closest target ".dnd-reroll")
                (table/reroll!)))))))

(defn- wire-bar! [node]
  (when-let [container (fedoras.ui.widget-ui/q! node ".dnd-bar")]
    (set! (.-onclick container)
          (fn [event]
            (let [target (.-target event)]
              (cond
                (.closest target ".dnd-connect")
                (when-let [box (fedoras.reader/q node ".dnd-key")]
                  (let [key (str/trim (.-value box))]
                    (when (seq key)
                      (reset! table/api-key key)
                      (set! (.-value box) "")
                      (table/repaint!))))

                (.closest target ".dnd-begin")  (table/begin!)
                (.closest target ".dnd-send")   (send-box! node)
                (.closest target ".dnd-retell") (table/retell!)
                (.closest target ".dnd-save")   (table/save!)
                (.closest target ".dnd-forget") (table/forget-session!)
                :else nil))))
    (set! (.-onkeydown container)
          (fn [event]
            (when (and (= "Enter" (.-key event))
                       (.closest (.-target event) ".dnd-say"))
              (send-box! node))))))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount!
   ::dnd id
   (fn [node]
     (reset! table/repaint (fn [] (paint! node)))
     (table/resume!)
     (fedoras.ui.widget-ui/settings! node id table/configure!)
     (fedoras.ui.widget-ui/wire! node "the transcript controls" wire-under!)
     (fedoras.ui.widget-ui/wire! node "the input bar" wire-bar!))
   paint!))