(ns fedoras.cs-class.survey.survey-ui
  "Ground with hazards in it, and an office that may move them.

  THE HAZARDS ARE NOT PLACED UNTIL THE FIRST PROBE, which is ordinary
  minesweeper courtesy and is also the first of the two lies. The ground
  did not have hazards in it that you found. The hazards were decided
  after you chose, subject to your choice being clear.

  A RESURVEY MOVES THEM AGAIN. It may only put a hazard on a square
  nobody has surveyed, so no resurvey has ever placed a hazard under
  anybody, and the office is telling the truth when it says so. What it
  does is change the numbers on squares you have already read, and it
  does not say which. Your marks stay where you left them.

  `deduce` reads those marks as fact, because they were fact when they
  were made. The arithmetic in `certainties` is correct. That is the
  point: after a resurvey it will walk a rover into a hazard by a chain
  of reasoning with nothing wrong in it.

  THE HAZARDS FIX THEMSELVES EVENTUALLY. A resurvey has to fit every
  hazard into the unsurveyed squares, so the more ground you hold the
  fewer arrangements exist, until only one does and the office can no
  longer change anything. Nothing enforces that. It is arithmetic."
  (:require [clojure.string :as str]
            [clojure.string :as s]))

;; ---------------------------------------------------------------------
;; The ground. No DOM in this section.
;; ---------------------------------------------------------------------

(def default-plan
  {:width 9
   :height 9
   :hazard-count 10
   :rover-count 3
   :resurvey-every 12})

(defn- shuffled
  "Fisher-Yates on a JavaScript array, written the long way for the same
  reason `maze/shuffled` is: it needs nothing from cljs.core that Scittle
  might not have bound."
  [coll]
  (let [v (js/Array.from (into-array coll))]
    (loop [i (dec (.-length v))]
      (if (pos? i)
        (let [j (js/Math.floor (* (js/Math.random) (inc i)))
              held (aget v i)]
          (aset v i (aget v j))
          (aset v j held)
          (recur (dec i)))
        (vec v)))))

(defn in-bounds? [{:keys [width height]} [x y]]
  (and (< -1 x width) (< -1 y height)))

(defn squares [{:keys [width height]}]
  (for [y (range height) x (range width)] [x y]))

(defn neighbours [state [x y]]
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :when (not= [0 0] [dx dy])
        :let [p [(+ x dx) (+ y dy)]]
        :when (in-bounds? state p)]
    p))

(defn adjacent-hazards [state p]
  (let [hazards (or (:hazards state) #{})]
    (count (filter hazards (neighbours state p)))))

(defn place-hazards
  "Choose where the hazards are. `pinned` stay where they are, because a
  rover has already stopped on them and the reader watched it happen.
  `protected` may not receive one, because somebody has been there.

  Both of those are settings, and both are on by default, and together
  they are the whole basis of the office's claim. Turn either off in the
  settings file and the claim stops being true, and nothing in the program
  says so.

  Returns nil when there is nowhere left to put them, and reports
  `:freedom`, the number of squares that were available for the ones
  that had to move, which is what the endgame is made of."
  [state pinned protected]
  (let [pinned (if (:pin-found state) (set pinned) #{})
        protected (if (:protect-surveyed state) (set protected) #{})
        barred (into pinned protected)
        free (remove barred (squares state))
        wanted (- (:hazard-count state) (count pinned))]
    (when (>= (count free) wanted)
      {:hazards (into pinned (take wanted (shuffled free)))
       :freedom (- (count free) wanted)
       :forced (= (count free) wanted)})))

(defn probe
  "Survey one square, and everything that follows from it. A square with
  no hazard beside it surveys its neighbours too, which is the only
  reason this is a game rather than eighty-one separate coin flips."
  [state p]
  (cond
    (contains? (:seen state) p) state
    (contains? (:hazards state) p) (update state :seen conj p)
    :else
    (let [state (update state :seen conj p)]
      (if (zero? (adjacent-hazards state p))
        (reduce probe state (neighbours state p))
        state))))

(defn clear-squares [state]
  (- (* (:width state) (:height state)) (:hazard-count state)))

(defn surveyed-clear [state]
  (count (remove (:hazards state) (:seen state))))

(defn settle
  "Every ending, checked in one place."
  [state]
  (cond
    (:over state) state

    (not (pos? (:rovers state)))
    (assoc state :over :spent
           :note "There are no rovers left. The survey is suspended.")

    (and (:hazards state) (= (surveyed-clear state) (clear-squares state)))
    (assoc state :over :complete
           :note "Every clear square is surveyed. The ground is done.")

    :else state))

;; ---------------------------------------------------------------------
;; The office
;; ---------------------------------------------------------------------

(defn resurvey
  "Move the hazards. `reason` is :ordered when the reader asked and
  :scheduled when the calendar did, and the only difference is the
  sentence underneath."
  [state reason]
  (cond
    (:over state) state
    (nil? (:hazards state)) (assoc state :note "There is nothing to resurvey yet.")
    :else
    (let [pinned (filter (:hazards state) (:seen state))
          protected (remove (:hazards state) (:seen state))
          placed (place-hazards state pinned protected)]
      (if (nil? placed)
        (assoc state :fixed true :freedom 0
               :note "A resurvey was attempted. There is nowhere left to move anything.")
        (-> state
            (assoc :hazards (:hazards placed))
            (assoc :fixed (:forced placed))
            (assoc :freedom (:freedom placed))
            (update :resurveys inc)
            (assoc :note
                   (cond
                     (:forced placed)
                     "Resurvey complete. Only one arrangement fits now, so it is the one you have."

                     (= reason :ordered)
                     "Resurvey complete. Your marks are still where you left them."

                     :else
                     "The office has resurveyed this ground. Your marks are still where you left them.")))))))

(defn- scheduled-resurvey
  "Every `resurvey-every` probes, whether or not anybody wanted one. The
  count is in the header, so it is a thing you can watch coming and
  cannot stop."
  [state]
  (if (and (nil? (:over state))
           (not (:fixed state))
           (pos? (:probes state))
           (zero? (mod (:probes state) (:resurvey-every state))))
    (resurvey state :scheduled)
    state))

;; ---------------------------------------------------------------------
;; What the reader does
;; ---------------------------------------------------------------------

(defn- open-ground
  "Place the hazards around the first probe, so the first probe is clear
  and has clear neighbours, so there is an opening to work from."
  [state p]
  (if (:hazards state)
    state
    (let [protected (conj (set (neighbours state p)) p)
          placed (place-hazards state #{} protected)]
      (assoc state :hazards (:hazards (or placed {:hazards #{}}))))))

(defn probe-at [state p]
  (cond
    (:over state) state

    (contains? (:marked state) p)
    (assoc state :note "That one is marked. Unmark it first.")

    (contains? (:seen state) p)
    (assoc state :note "That square is already surveyed.")

    :else
    (let [state (open-ground state p)
          stopped (contains? (:hazards state) p)
          state (if stopped
                  (-> state
                      (update :seen conj p)
                      (update :rovers dec)
                      (assoc :note (str "A rover stopped at " (first p) "," (second p) ".")))
                  (let [state (probe state p)]
                    (assoc state :note
                           (str (surveyed-clear state) " of " (clear-squares state)
                                " clear squares surveyed."))))]
      (-> state
          (update :probes inc)
          scheduled-resurvey
          settle))))

(defn toggle-mark [state p]
  (cond
    (:over state) state

    (contains? (:seen state) p)
    (assoc state :note "That square is surveyed. There is nothing to mark.")

    :else
    (-> state
        (update :marked (fn [m] (if (contains? m p) (disj m p) (conj m p))))
        (assoc :note "Marked. A mark is a claim about the ground as it was."))))

;; ---------------------------------------------------------------------
;; What follows
;; ---------------------------------------------------------------------

(defn- known-hazard? [state p]
  (and (contains? (:seen state) p) (contains? (:hazards state) p)))

(defn certainties
  "The only two things anybody can say for certain, read off each
  surveyed square. If the unknown neighbours number exactly as many
  hazards as are still unaccounted for, all of them are hazards. If none
  are unaccounted for, all of them are clear.

  Both rules take `marked` as fact. They have to. A mark is the only
  record there is of what the numbers used to say."
  [state]
  (reduce
   (fn [acc p]
     (let [around (neighbours state p)
           unknown (remove (fn [q] (or (contains? (:seen state) q)
                                       (contains? (:marked state) q)))
                           around)
           wanted (adjacent-hazards state p)
           accounted (count (filter (fn [q] (or (contains? (:marked state) q)
                                                (known-hazard? state q)))
                                    around))]
       (cond
         (empty? unknown) acc
         (= wanted (+ accounted (count unknown))) (update acc :hazard into unknown)
         (= wanted accounted) (update acc :clear into unknown)
         :else acc)))
   {:hazard #{} :clear #{}}
   (filter (:seen state) (squares state))))

(defn- follow
  "Send a rover to a square the reader's own arithmetic says is clear. It
  is clear if the marks were right, and the marks were right when they
  were made. This does not increment `:probes`, because the calendar
  counts probes the reader ordered."
  [state p]
  (if (contains? (:hazards state) p)
    (-> state
        (update :seen conj p)
        (update :rovers dec)
        (update :misled inc))
    (probe state p)))

(defn deduce [state]
  (cond
    (:over state) state
    (nil? (:hazards state)) (assoc state :note "Probe something first.")
    :else
    (let [{:keys [hazard clear]} (certainties state)]
      (if (and (empty? hazard) (empty? clear))
        (assoc state :note "Nothing follows. You will have to guess, or order a resurvey.")
        (let [before (:misled state)
              after (reduce follow (update state :marked into hazard) clear)
              lost (- (:misled after) before)]
          (-> after
              (assoc :note
                     (if (pos? lost)
                       (str lost (if (= 1 lost) " rover" " rovers")
                            " stopped on ground that followed from your marks."
                            " The arithmetic was correct.")
                       (str (count hazard) " certain, " (count clear) " certainly clear.")))
              settle))))))

;; ---------------------------------------------------------------------
;; The file
;; ---------------------------------------------------------------------

(def default-settings
  {:width 9
   :height 9
   :hazard-count 10
   :rover-count 3
   :resurvey-every 12
   :protect-surveyed true
   :pin-found true
   :show-freedom false})

(defonce settings (atom default-settings))
(defonce game (atom nil))
(defonce wired (atom #{}))

(defn new-ground
  "Lay out fresh ground on the current settings."
  []
  (let [s @settings]
    (assoc s
           :hazards nil
           :seen #{}
           :marked #{}
           :rovers (:rover-count s)
           :probes 0
           :resurveys 0
           :misled 0
           :freedom nil
           :fixed false
           :over nil
           :note "Probe anywhere. The first one is always clear.")))

(defn from-settings
  "Keep the keys this program has, ignore the rest, and lay out again.
  Changing the terms mid-game would be exactly the thing this program is
  about, so it does not do it."
  [current-settings]
  (swap! settings
         (fn [setting]
           (merge setting
                  (select-keys current-settings
                               [:width :height :hazard-count :rover-count
                                :resurvey-every :protect-surveyed
                                :pin-found :show-freedom]))))
  (reset! game (new-ground)))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn- face
  "What one square shows. At the end the marks are judged, which is the
  only moment in the program where anybody says whether you were right."
  [state p]
  (let [{:keys [seen marked hazards over]} state]
    (cond
      (contains? marked p)
      {:class (str "survey-marked"
                   (when over (if (contains? hazards p) " survey-right" " survey-wrong")))
       :text "x"}

      (contains? seen p)
      (if (contains? hazards p)
        {:class "survey-stopped" :text "!"}
        (let [n (adjacent-hazards state p)]
          {:class "survey-clear" :text (if (zero? n) "" (str n)) :n n}))

      (and over (contains? hazards p))
      {:class "survey-missed" :text "!"}

      :else {:class "survey-unknown" :text ""})))

(defn- grid-html [state]
  (str/join
   ""
   (for [y (reverse (range (:height state)))
         x (range (:width state))
         :let [{:keys [class text n]} (face state [x y])]]
     (str "<button class='survey-square " class "'"
          " data-x='" x "' data-y='" y "'"
          (when n (str " data-n='" n "'"))
          (when (:over state) " disabled")
          ">" text "</button>"))))

(defn- state-line [state]
  (let [freedom (when (and (:show-freedom state) (:freedom state))
                  (str " · " (:freedom state) " free"))]
    (case (:over state)
      :complete (str "complete · " (:resurveys state) " resurveys")
      :spent (str "suspended · " (:resurveys state) " resurveys")
      (str (surveyed-clear state) " of " (clear-squares state) " · "
           (:rovers state) (if (= 1 (:rovers state)) " rover · " " rovers · ")
           (if (:fixed state)
             "hazards fixed"
             (str "resurvey in "
                  (- (:resurvey-every state)
                     (mod (:probes state) (:resurvey-every state)))))
           freedom))))


(defn- record-line [state]
  (str (count (:marked state)) " marked · "
       (:resurveys state) " resurveys · "
       (:misled state) " lost to your own arithmetic"))

(defn paint! [node]
  (let [state @game
        playing (nil? (:over state))]
    (fedoras.ui.widget-ui/grid!
     node ".survey-grid" (:width state) (grid-html state))
    (fedoras.ui.widget-ui/state! node (state-line state))
    (fedoras.reader/text! node ".survey-note" (:note state))
    (fedoras.reader/text! node ".survey-record" (record-line state))
    (fedoras.reader/enable! node ".survey-deduce" playing nil)
    (fedoras.reader/enable! node ".survey-resurvey"
                            (and playing
                                 (not (:fixed state))
                                 (some? (:hazards state)))
                            nil)))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- square-at [event]
  (fedoras.ui.widget-ui/data event ".survey-square" "x" "y"))

(defn- act! [node f & args]
  (swap! game (fn [state] (apply f state args)))
  (paint! node))

(defn- wire-grid! [node]
  (when-let [grid (fedoras.ui.widget-ui/q! node ".survey-grid")]
    (set! (.-onclick grid)
          (fn [event]
            (when-let [p (square-at event)]
              (if (.-shiftKey event)
                (act! node toggle-mark p)
                (act! node probe-at p)))))
    (set! (.-oncontextmenu grid)
          (fn [event]
            (.preventDefault event)
            (when-let [p (square-at event)]
              (act! node toggle-mark p))
            false))))

(defn- wire-buttons! [node]
  (fedoras.reader/on-click! node ".survey-deduce"
                            (fn [_] (act! node deduce)))
  (fedoras.reader/on-click! node ".survey-resurvey"
                            (fn [_] (act! node resurvey :ordered)))
  (fedoras.reader/on-click! node ".survey-new"
                            (fn [_]
                              (reset! game (new-ground))
                              (paint! node))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [tuning]
     (from-settings tuning)
     (paint! node))))

(defn setup!
  "Once per element. Each part is wired on its own, so one that fails
  says so and takes nothing else with it."
  [node]
  (let [id (.-id node)]
    (fedoras.ui.widget-ui/wire! node "grid" wire-grid!)
    (fedoras.ui.widget-ui/wire! node "buttons" wire-buttons!)
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))))

(defn mount!
  [{:keys [id]}]
  (when (nil? @game) (reset! game (new-ground)))
  (fedoras.ui.widget-ui/mount!
   ::survey (or id "survey") setup! paint!))