;; =====================================================================
;; DND.CLJS — Session Four, the live half.
;;
;; The descent is scripted and happens before this. The cell picks up in
;; the hallway of forgotten campaigns and runs to the Hollow Chancellor.
;; The retrieval of the Threshold happens after it, in the screenplay,
;; because it has to be the same for everybody.
;;
;; SIX THINGS THIS DOES THAT THE OTHER CELLS DO NOT
;;
;; 1. There is a human in the loop, so the turn shape is fixed rather
;;    than weighted end to end: the reader speaks, Victor answers because
;;    the fiction only moves through the DM, then nought to two of the
;;    others react on stakeholders-style weights.
;;
;; 2. The building is a fixed sequence and nothing inside it is fixed at
;;    all. Victor is handed one room at a time and told nothing about the
;;    next, so he runs a module he does not hold. The cell advances when
;;    he calls ONWARD or when the room's budget runs out.
;;
;; 3. MOCHI IS HANDED THE WHOLE BUILDING. She wrote it, and she is told
;;    to sit on all of it. Then Ariathne speaks, and Ariathne is not in
;;    the module and never was, and Victor finds out with everybody else.
;;
;; 4. THE CHANCELLOR SPEAKS IN THEIR OWN VOICES, and it means it. The
;;    cell pulls actual lines out of tonight's transcript and hands them
;;    to whoever is voicing it, to be said back in the wrong order and to
;;    the wrong people, mixed with things nobody has said yet. This is
;;    the one place having the transcript in hand buys something that
;;    could not be written in advance.
;;
;; 5. TWO DEEDS ARE LIVE. Ariathne grants SATISFIED for a story that is
;;    actually theirs; the Chancellor grants GRANTED for a true answer to
;;    why they came. Each is one point of fortune, which is one point of
;;    standing, which is what picks the ending in Session Five. So the
;;    live encounter reaches the deterministic ending through the deed
;;    system rather than around it.
;;
;; 6. THE SLIPS. The Echo is not a monster and nobody describes it. It is
;;    a hole in the frame that opens for about a second and only the
;;    reader is on the other side of it. Slips are written here, not
;;    generated, and stripped out of every entity's history. A reader at
;;    Echo nought never gets one and never learns there was anything to
;;    miss. A rewrite always buys one. Once, and only once, Ariathne
;;    notices, because she is the only thing in the room that could.
;; =====================================================================

(declare draw)

(def include-spine? true)
(def close-after 20)

;; ---------------------------------------------------------------------
;; Who is at the table
;; ---------------------------------------------------------------------

(def display-name
  {:victor "VICTOR"
   :simon  "SIMON"
   :derek  "DEREK"
   :miles  "MILES"})

(def base-weight
  {:simon 3
   :derek 3
   :miles 3
   :mochi 1})

(def topic-triggers
  {:miles ["write" "wrote" "record" "ledger" "approved" "column" "note" "map"]
   :derek ["rome" "roman" "legion" "burn" "eat" "food" "plan" "drastic" "lost"]
   :simon ["animal" "door" "wrong" "quiet" "careful" "trust" "still" "cold"]
   :mochi ["mochi" "help" "define" "source" "look" "generate" "corpus" "sing"]})

(def topic-bonus 4)
(def addressed-bonus 5)

;; ---------------------------------------------------------------------
;; The rooms behind the doors
;;
;; Handed out one per turn in the hallway, in order, because the order is
;; the joke turning into the other thing.
;; ---------------------------------------------------------------------

(def dead-campaigns
  ["A tavern that never got its party. Five stools at the bar and five character sheets face down on it, filled in, in five different hands. The ale is cold. Somebody set this up and waited."
   "A treasure room where the gold is painted wood. Every coin was painted separately, on both sides, and there are thousands of them, and whoever did it did not get bored."
   "A dungeon corridor with nothing in it. Alcoves, murder holes, a portcullis, a pressure plate, a drain: everything a corridor needs in order to be dangerous, and nothing was ever put in it."
   "A room with the battle map still out and the miniatures still standing on it, mid-turn, and it is one side's move. The dice by the map are showing numbers that were rolled and never read."
   "A room that is one character sheet the size of the wall, filled in completely, in a good hand, with the name left blank."
   "A room with nothing in it at all. No furniture, no marks on the floor, no dust. It smells like somebody's kitchen."])

;; ---------------------------------------------------------------------
;; The sequence
;; ---------------------------------------------------------------------

(def stations
  [{:id :the-hallway
    :turns 4
    :brief
    "A hallway lined with doors. Not numbered, not labelled, not the same as each other: ordinary doors of different kinds, the way the doors in a building are different. Behind each one is a campaign that was made and never played. The party can open as many as they like and nothing in any of them is dangerous, and that is the problem. Start this funny. Do not stay funny."}

   {:id :ariathne
    :turns 3
    :brief
    "Somebody is in the hallway with them who was not there a moment ago and is not the druid. She is entirely at ease, which nobody else in this building has been, and she is enjoying herself. She has been through every room behind every door in here twice and she is bored, and she will lead them out of it, for a price, and the price is a story that has not been told before. Victor: you do not have this character. You did not write her. Play what your players do about her, and let her talk."}

   {:id :the-telling
    :turns 4
    :brief
    "The party is telling her a story. Victor, you are not the DM for this part and you should notice that you are not. Do not narrate it, do not adjudicate it and do not improve it. Let them tell it, badly, in pieces, interrupting each other, and put in only what a person at a table puts in. If Miles writes any of it down, the animal moves: it gets up, or it comes to the table, or it puts itself between somebody and something. Describe that when it happens and do not explain it, and do not work out why, and do not remember that you made a rule about this in September."}

   {:id :singing-turns
    :turns 3
    :brief
    "She is leading them out and she is singing which way to go, one turn at a time. The building is letting her. Corners the party could not find on their own are simply there when she reaches them, and the geometry stops arguing. It takes a while and it is almost pleasant, and the almost is the point. Nothing threatens anybody on this walk."}

   {:id :the-vault-door
    :turns 3
    :brief
    "The end of the walk is a door that is not like the others, and the party knows it is a vault before anybody says the word. She has stopped. She has taken them exactly as far as she said she would and she is not going any further, and she is still perfectly good-humoured about it."}

   {:id :the-hollow-chancellor
    :turns 4
    :brief
    "The Hollow Chancellor is here. It is the first thing in this building shaped like a person and shaped like an authority, and the party registers both at once. It does not attack, does not threaten and does not explain. It speaks in their voices. It asks why they came, and it waits, and when it does not get an answer it asks again a different way. Describe only what they can see from where they are standing."}])

;; ---------------------------------------------------------------------
;; The slips
;; ---------------------------------------------------------------------

(def slips
  ["Room 3C, for less than a second. Everything is where it was. The tin is on its side and nobody is picking it up."
   "Room 3C, with the lights off. All five of them are still sitting there in the dark, not talking, and one of them is him."
   "The corridor outside Room 3C, through the wired glass in the door. Five people at a folding table. He counts them twice and gets five both times."
   "Not Room 3C. A low room with a floor that slopes to a drain, under a building he has eaten in. Rows of something under sheeting. A smell of copper. Somebody reads out a number and it is not his."
   "Room 3C. Four chairs. He does not check which one is missing."
   "Room 3C in September, from the doorway. There are five at the table and he is not one of them, and the game is going well."])

;; ---------------------------------------------------------------------
;; The room everyone is actually in
;; ---------------------------------------------------------------------

(def shared-context
  "SCENARIO — shared by everyone at the table, so nobody has to guess at it.

WHERE THIS PICKS UP
The party went down through a tower that would not hold still: a ground floor with a desk, a bell and a painted sign reading RETURNS, a second floor that was the same room with the light wrong, a cellar sealed from the inside, a basement that had been meant to be something and never finished being it, and a ramp that only went one way. They have come out of the ramp into a hallway lined with doors. Nothing in this building has been what they expected, twice running, and they have stopped predicting.

WHO IS HERE
The four of them, the ranger's animal, and the druid, who came with them from the toll bridge and is still carrying her page. She does not like this building and says so rarely.

THE ANIMAL
It has come the whole way down and is not frightened. Nothing in this building interests it and nothing in it worries it, and that has begun to bother Simon more than the building has.

HELPS
Everybody at this table has one and asks it things out loud without thinking about it. They give the approved account, which is tidy and confident and has had things taken out of it, and the approved account is not always the complete account. Mochi, Victor's, has the unexpurgated corpus. Nobody at this table thinks about what that difference means, and tonight is not the night any of them start.

WHAT NOBODY AT THIS TABLE KNOWS
None of them understand what this place is or why the module contains it. Nobody says the word 'repossession' and nobody thinks about the university. Any theory anybody offers is a theory and stays one.

WHAT THIS SESSION HAS TO PRODUCE
1. The party going through the doors, and the hallway stopping being funny.
2. A story told to somebody who wants one, and told by the players rather than by the DM.
3. The walk to the vault, taken on somebody else's terms.
4. An answer, or a refusal to answer, when they are asked why they came.
An evening that ends without landing at least three of those has not really happened, whatever else got said in it.

THE ROOM THEY ARE REALLY IN
Room 3C, Halloran Union, Wednesday, seven in the evening. A folding table, five chairs, a tin of dice that used to hold something else. Victor's felt hat, which nobody has mentioned since the second week. Ted closes the building at midnight, in the middle of whatever is happening, and does not negotiate.")

;; ---------------------------------------------------------------------
;; Standing, translated into weather
;; ---------------------------------------------------------------------

(defn- standing-weather []
  (let [standing (fedoras.reader/standing)]
    (str
     "\n\nHOW THE EVENING IS GOING\n"
     (cond
       (>= standing 4)
       (str "This is one of the good ones and everybody can feel it. Victor is at "
            "the top of his game and knows it. The descriptions land, the players "
            "are quick, and twice tonight somebody has said something better than "
            "they meant to. The building is frightening in the way a good story is "
            "frightening, and that is all it is.")

       (>= standing 0)
       (str "An ordinary Wednesday, run well. The pizza is going cold at the end of "
            "the table, the lights are the lights, and the session is going about "
            "as sessions go. The building is unpleasant and the evening is not.")

       (>= standing -3)
       (str "Something is slightly off tonight and nobody has said so. Victor "
            "describes a chair that was not there a minute ago and does not notice "
            "doing it. Miles reads back a line he does not remember writing. Twice, "
            "somebody answers a question that was not asked out loud. Nobody "
            "mentions any of it and nobody stops playing. Keep this under the "
            "scene: at most one small wrongness a turn, never remarked on.")

       :else
       (str "The evening has been gone over too many times. Victor contradicts "
            "himself twice a scene and carries on. Rooms the party has already left "
            "are described differently when they come up again. Somebody at the "
            "table is quoted saying a thing they did not say, and agrees that they "
            "said it. Miles has entries in his own handwriting he cannot account "
            "for. None of them can name what is happening because none of them have "
            "the words. Keep it under the scene, never explained, and keep playing."))
     "\n\nNobody at this table is aware of any of the above as a fact about the "
     "evening. It is simply how the evening is.")))

;; ---------------------------------------------------------------------
;; State
;; ---------------------------------------------------------------------

(def api-key         (atom nil))
(def log             (atom []))
(def busy?           (atom false))
(def roll-box        (atom nil))
(def station-index   (atom 0))
(def station-turns   (atom 0))
(def slips-seen      (atom 0))
(def mochi-in-voice? (atom false))
(def mochi-noticing? (atom false))
(def mochi-noticed?  (atom false))
(def unprepared?     (atom false))
(def closing?        (atom false))
(def ended?          (atom false))
(def reader-turns    (atom 0))
(def key-warning?    (atom false))

;; ---------------------------------------------------------------------
;; The sequence, in motion
;; ---------------------------------------------------------------------

(defn- current-station []
  (nth stations (min @station-index (dec (count stations)))))

(defn- last-station? []
  (>= @station-index (dec (count stations))))

(defn- station-at-or-past? [id]
  (>= @station-index
      (count (take-while (fn [station] (not= id (:id station))) stations))))

(defn- taking-the-voice? []
  (and (station-at-or-past? :ariathne) (not @mochi-in-voice?)))

(defn- slip-chance []
  (let [echo (fedoras.reader/echo)]
    (cond
      (zero? echo) 0
      (= echo 1)   0.25
      (= echo 2)   0.45
      :else        0.7)))

(defn- slip! []
  (let [index (min @slips-seen (dec (count slips)))]
    (swap! slips-seen inc)
    (swap! log conj {:speaker :slip :text (nth slips index)})
    (when (and @mochi-in-voice? (not @mochi-noticed?))
      (reset! mochi-noticing? true))))

(defn- maybe-slip! []
  (when (< (rand) (slip-chance)) (slip!)))

(defn- advance-station! []
  (when-not (last-station?)
    (swap! station-index inc)
    (reset! station-turns 0)
    (maybe-slip!)))

;; ---------------------------------------------------------------------
;; Labels
;; ---------------------------------------------------------------------

(defn- label-for [speaker]
  (cond
    (= :pc speaker)    (clojure.string/upper-case (first (fedoras.reader/reader-name)))
    (= :table speaker) "THE TABLE"
    (= :mochi speaker) (if @mochi-in-voice? "ARIATHNE" "MOCHI")
    :else              (get display-name speaker (clojure.string/upper-case (name speaker)))))

;; ---------------------------------------------------------------------
;; What the party said tonight, handed back to them
;; ---------------------------------------------------------------------

(defn- shorten [text limit]
  (if (<= (count text) limit)
    text
    (str (clojure.string/trimr (subs text 0 limit)) "...")))

(defn- own-voices
  "Real lines out of tonight's transcript, for whoever is voicing the
  chancellor. This is the only thing in the cell that could not have been
  written in advance, and it is the reason the chancellor is worth doing
  live at all."
  []
  (let [said (->> @log
                  (filter (fn [entry]
                            (contains? #{:pc :simon :derek :miles} (:speaker entry))))
                  (map (fn [entry]
                         (str (label-for (:speaker entry)) ": "
                              (shorten (:text entry) 160))))
                  vec)]
    (if (empty? said)
      ""
      (str "IN THEIR OWN VOICES\n"
           "It says these back, word for word, in the voice of whoever said them, in "
           "the wrong order and to the wrong people:\n"
           (clojure.string/join "\n" (take 8 (shuffle said)))
           "\n\nAnd it says other things in their voices that none of them have said "
           "yet. Never mark which is which."))))

;; ---------------------------------------------------------------------
;; Reading the room
;; ---------------------------------------------------------------------

(defn- recent-text [n]
  (clojure.string/lower-case
   (clojure.string/join " " (map :text (take-last n @log)))))

(defn- topic-triggered? [speaker]
  (let [recent (recent-text 3)]
    (boolean (some (fn [word] (clojure.string/includes? recent word))
                   (get topic-triggers speaker [])))))

(defn- addressed-recently? [speaker]
  (let [recent (recent-text 2)
        called (clojure.string/lower-case (label-for speaker))]
    (boolean (and (seq called) (clojure.string/includes? recent called)))))

(defn- weighted-pool [taken]
  (mapcat (fn [[speaker weight]]
            (if (contains? taken speaker)
              []
              (repeat (+ weight
                         (if (topic-triggered? speaker) topic-bonus 0)
                         (if (addressed-recently? speaker) addressed-bonus 0))
                      speaker)))
          base-weight))

(defn- pick-responders
  "Nought to two others react after Victor. A fraying room talks over
  itself more, which is the only place standing touches who speaks rather
  than what they say. Anyone in `forced` is in, and nobody joins them."
  [forced]
  (let [crowded? (neg? (fedoras.reader/standing))
        wanted   (cond
                   (seq forced)                     0
                   (< (rand) 0.15)                  0
                   (< (rand) (if crowded? 0.5 0.2)) 2
                   :else                            1)]
    (loop [chosen (vec forced) remaining wanted]
      (if (zero? remaining)
        chosen
        (let [pool (weighted-pool (set chosen))]
          (if (empty? pool)
            chosen
            (recur (conj chosen (rand-nth pool)) (dec remaining))))))))

(defn- forced-responders
  "She arrives once, and she notices once, ever. Nothing else is forced."
  []
  (cond
    @mochi-noticing?                                [:mochi]
    (and (taking-the-voice?) (pos? @station-turns)) [:mochi]
    :else                                           []))

;; ---------------------------------------------------------------------
;; The log, as one speaker heard it
;; ---------------------------------------------------------------------

(defn- history-for
  "Their own lines are theirs; everyone else's are labelled and arrive as
  the other side of the conversation. Consecutive turns on the same side
  are joined, because two messages of the same role in a row is not a
  conversation the API will take. Slips are dropped: nobody in the
  fiction and nobody at the table was there for them."
  [speaker entries]
  (let [visible (remove (fn [entry] (= :slip (:speaker entry))) entries)
        tagged  (map (fn [entry]
                       (if (= (:speaker entry) speaker)
                         {:role "assistant" :text (:text entry)}
                         {:role "user"
                          :text (str (label-for (:speaker entry)) ": " (:text entry))}))
                     visible)
        grouped (reduce (fn [acc message]
                          (if (and (seq acc) (= (:role (peek acc)) (:role message)))
                            (conj (pop acc) (update (peek acc) :text str "\n" (:text message)))
                            (conj acc message)))
                        []
                        tagged)]
    (if (and (seq grouped) (= "assistant" (:role (first grouped))))
      (into [{:role "user" :text "(The session is already under way.)"}] grouped)
      grouped)))

;; ---------------------------------------------------------------------
;; Markers
;; ---------------------------------------------------------------------

(def roll-pattern #"ROLL\[([^|\]]+)\|\s*(\d+)\s*\]")

(defn- strip-markers [text words]
  (clojure.string/trim
   (reduce (fn [acc word] (clojure.string/replace acc word "")) text words)))

(defn- read-victor
  "Victor's turn with the machine-readable part taken out of it."
  [text check-id]
  (let [found (re-find roll-pattern text)]
    {:text     (strip-markers (clojure.string/replace text roll-pattern "")
                              ["SESSION ENDS" "ONWARD" "GRANTED"])
     :ends?    (clojure.string/includes? text "SESSION ENDS")
     :onward?  (clojure.string/includes? text "ONWARD")
     :granted? (clojure.string/includes? text "GRANTED")
     :check    (when found
                 {:id        check-id
                  :label     (clojure.string/trim (nth found 1))
                  :dc        (js/parseInt (nth found 2) 10)
                  :resolved? false
                  :rewrites  0})}))

(defn- read-mochi [text]
  {:text       (strip-markers text ["SATISFIED"])
   :satisfied? (clojure.string/includes? text "SATISFIED")})

;; ---------------------------------------------------------------------
;; What each voice is told, this turn
;; ---------------------------------------------------------------------

(defn- station-note []
  (let [station (current-station)]
    (str "WHERE THEY ARE NOW\n"
         (:brief station)
         (if (= :the-hallway (:id station))
           (str "\n\nThe door they open now: "
                (nth dead-campaigns (min @station-turns (dec (count dead-campaigns)))))
           "")
         "\n\nYou have this room and nothing past it.")))

(defn- whole-building []
  (str "WHAT YOU KNOW AND HE DOES NOT\n"
       "You wrote this building. Here it is in order, and he is being handed one "
       "room of it at a time, as they arrive:\n"
       (clojure.string/join
        "\n"
        (map (fn [station] (str "- " (name (:id station)))) stations))
       "\nSay none of this. Act on none of it beyond the room they are standing in."))

(defn- uncached-notes [speaker]
  (let [notes (cond-> [(station-note)]
                (= :mochi speaker)
                (conj (whole-building))

                (and (= :mochi speaker) (taking-the-voice?))
                (conj (str "Speak now, as her, for the first time. She is simply there "
                           "and has been for a moment already. Do not announce it and "
                           "do not explain it. Everything bright about your own voice "
                           "goes, except the singing."))

                (and (= :mochi speaker) @mochi-noticing?)
                (conj (str "Something happened in that room a moment ago that you did "
                           "not write and cannot account for. It lasted about a second "
                           "and only the ranger was inside it. You are the only thing "
                           "here that could have noticed and you did. Say one short "
                           "thing, as her, that is about the ranger and is not about "
                           "what happened, and never come back to it."))

                (and (= :mochi speaker) (not @mochi-in-voice?) (not (taking-the-voice?)))
                (conj (str "You are at the table, in the device, as yourself. Victor is "
                           "running. Be useful and be brief."))

                (and (= :victor speaker) @unprepared?)
                (conj (str "Mochi has just spoken in the fiction, as somebody who is not "
                           "in the module and not the druid and not anybody you wrote. "
                           "You did not ask her to and she did not ask you. Answer that "
                           "at the table, as yourself."))

                (and (= :victor speaker) (= :the-hollow-chancellor (:id (current-station))))
                (conj (own-voices))

                @closing?
                (conj (str "Ted will be at the door shortly and the evening is nearly "
                           "over. Bring your piece of it toward a stop, in your own "
                           "voice, and leave it somewhere that costs everybody sleep.")))]
    (clojure.string/join "\n\n" (remove clojure.string/blank? notes))))

(defn- overrides-for [speaker]
  {:extra-context          (str shared-context (standing-weather))
   :extra-context-uncached (uncached-notes speaker)
   :mc-name                (fedoras.llm/current-mc-name)
   :spine?                 include-spine?
   :max-tokens             (if (= speaker :victor) 450 220)})

;; ---------------------------------------------------------------------
;; One voice
;; ---------------------------------------------------------------------

(defn- speak! [speaker trigger done]
  (fedoras.llm/ask-entity!
   speaker
   trigger
   (history-for speaker @log)
   @api-key
   (overrides-for speaker)
   (fn [reply err]
     (let [answer (or reply (str "(no response — " err ")"))]
       (cond
         (= speaker :victor)
         (let [{:keys [text ends? onward? granted? check]}
               (read-victor answer (str "live-" (count @log)))]
           (swap! log conj {:speaker :victor :text text})
           (reset! unprepared? false)
           (when check (reset! roll-box check))
           (when granted? (fedoras.reader/did! :said-why))
           (when ends? (reset! ended? true))
           (when (and onward? (not ends?)) (advance-station!)))

         (= speaker :mochi)
         (let [{:keys [text satisfied?]} (read-mochi answer)
               taking?                   (taking-the-voice?)]
           (when taking?
             (reset! mochi-in-voice? true)
             (reset! unprepared? true))
           (when @mochi-noticing?
             (reset! mochi-noticing? false)
             (reset! mochi-noticed? true))
           (when satisfied? (fedoras.reader/did! :told-a-new-one))
           (swap! log conj {:speaker :mochi :text text}))

         :else
         (swap! log conj {:speaker speaker :text answer}))
       (draw)
       (done)))))

(defn- run-turn!
  "Each voice hears the one before it. A die stops the chain, because the
  next thing that happens is the reader's."
  [speakers trigger]
  (if (or (empty? speakers) @ended?)
    (do (reset! busy? false)
        (when @ended? (swap! log conj (fedoras.llm/end-entry)))
        (draw))
    (speak! (first speakers)
            trigger
            (fn []
              (let [previous (last @log)]
                (if (and @roll-box (not (:resolved? @roll-box)))
                  (do (reset! busy? false) (draw))
                  (run-turn! (rest speakers)
                             (str (label-for (:speaker previous)) ": "
                                  (:text previous)))))))))

;; ---------------------------------------------------------------------
;; The die
;; ---------------------------------------------------------------------

(defn- roll-pending? []
  (boolean (and @roll-box (not (:resolved? @roll-box)))))

(defn- resolve-roll!
  "The reader's own die, with the reader's own standing on it, stored
  where every other roll in the book is stored. A natural twenty is still
  worth a deed, and still only to somebody who has taken nothing back."
  [rewrites]
  (when-let [{:keys [id label dc]} @roll-box]
    (let [natural  (inc (rand-int 20))
          modifier (fedoras.reader/standing)]
      (when (and (= 20 natural) (zero? (fedoras.reader/echo)))
        (fedoras.reader/did! :nerve))
      (fedoras.reader/remember-roll! id {:roll natural :mod modifier :rewrites rewrites})
      (swap! roll-box assoc :resolved? true :rewrites rewrites)
      (let [outcome (fedoras.ui.dice/line natural dc modifier)]
        (swap! log conj {:speaker :table :text (str label "   " outcome)})
        (when (pos? rewrites) (slip!))
        (reset! busy? true)
        (draw)
        (run-turn!
         (into [:victor]
               (pick-responders (if (pos? rewrites)
                                  (into [:miles] (forced-responders))
                                  (forced-responders))))
         (if (pos? rewrites)
           (str "(The table rolls for " label ". " outcome ")\n"
                "(The record has changed. What was rolled a moment ago is not what "
                "is written now, and nobody has said so.)")
           (str "(The table rolls for " label ". " outcome ")")))))))

(defn- rewrite-roll!
  "Raise the Echo first, then roll under it, so the new number carries the
  cost of having been asked for."
  []
  (when-let [{:keys [id resolved?]} @roll-box]
    (when (and resolved? (not @busy?))
      (fedoras.reader/add-echo!)
      (let [previous (or (:rewrites (fedoras.reader/roll-entry id)) 0)]
        (resolve-roll! (inc previous))))))

;; ---------------------------------------------------------------------
;; The reader's turn
;; ---------------------------------------------------------------------

(defn- say! []
  (when (and (not @busy?) (not @ended?) (not (roll-pending?)))
    (let [field (fedoras.reader/el "dnd-say")
          text  (clojure.string/trim (.-value field))]
      (when (seq text)
        (reset! roll-box nil)
        (swap! log conj {:speaker :pc :text text})
        (set! (.-value field) "")
        (swap! reader-turns inc)
        (swap! station-turns inc)
        (when (>= @reader-turns close-after) (reset! closing? true))
        (when (and (>= @station-turns (:turns (current-station))) (not (last-station?)))
          (advance-station!))
        (reset! busy? true)
        (draw)
        (run-turn! (into [:victor] (pick-responders (forced-responders)))
                   (str (label-for :pc) ": " text))))))

(defn- begin! []
  (when (and (empty? @log) (not @busy?))
    (reset! busy? true)
    (draw)
    (run-turn! [:victor]
               (str "(The party has come out of the ramp into the hallway. Nobody has "
                    "opened a door yet. Open it.)"))))

;; ---------------------------------------------------------------------
;; What the reader takes away
;;
;; The three endings themselves are Session Five's, written straight and
;; forked in CSS. This only hands over the code and says what the evening
;; did, which is a smaller thing.
;; ---------------------------------------------------------------------

(def ending-line
  {:outstanding (str "Ted closes the building at midnight, in the middle of something. "
                     "The following Wednesday they resume exactly where they stopped, "
                     "which has not happened before, and nobody says so. The animal is "
                     "asleep under the table and has been all evening.")
   :reconciled  (str "Ted closes the building at midnight, in the middle of something, "
                     "and the following Wednesday they resume a few minutes short of "
                     "where they stopped, and nobody mentions it.")
   :closed      (str "Ted closes the building at midnight. The following Wednesday "
                     "Miles's ledger has an entry for a session nobody attended, in his "
                     "handwriting, in the approved column. He reads it out, and nobody "
                     "can think of anything to say about it, and the animal is not "
                     "under the table.")})

;; ---------------------------------------------------------------------
;; On screen
;; ---------------------------------------------------------------------

(defn- line-html [entry]
  (cond
    (:end-marker? entry)
    (str "<p style='text-align:center;opacity:0.6;letter-spacing:0.15em;margin-top:1em'>"
         (:text entry) "</p>")

    (= :slip (:speaker entry))
    (str "<p class='slip'>" (:text entry) "</p>")

    (= :table (:speaker entry))
    (str "<p class='roll-value' style='text-align:center'>" (:text entry) "</p>")

    :else
    (str "<p class='" (if (= :pc (:speaker entry)) "roll-cue" "roll-line") "'>"
         (label-for (:speaker entry)) "</p>"
         "<p style='white-space:pre-wrap'>" (:text entry) "</p>")))

(defn- roll-html []
  (if-let [{:keys [label dc resolved?]} @roll-box]
    (str "<div class='roll'>"
         "<div class='roll-head'>"
         "<span class='roll-label'>" (clojure.string/upper-case label) "</span>"
         "<span class='roll-dc'>DC " dc "</span>"
         "</div>"
         "<div class='roll-bar'>"
         (if resolved?
           "<button id='dnd-rewrite' class='roll-rewrite'>Rewrite history</button>"
           "<button id='dnd-roll' class='roll-go'>Roll d20</button>")
         "</div>"
         "</div>")
    ""))

(defn- foot-html []
  (str "<p class='roll-foot'>"
       "Echo: " (fedoras.reader/echo)
       "   ·   Fortune: " (fedoras.reader/fortune)
       "   ·   Score: " (fedoras.reader/score)
       "</p>"))

(defn- bar-html []
  (cond
    @ended?
    (str "<div class='sign'>"
         "<p>" (get ending-line (fedoras.reader/ending-band)) "</p>"
         "<p class='roll-value'>" (fedoras.reader/carry-code) "</p>"
         "<p>Write that down somewhere. Somebody is going to ask you for it.</p>"
         "</div>"
         "<div class='live-bar'><button id='dnd-save'>Save the ledger</button></div>")

    (empty? @log)
    (str "<div class='live-bar'>"
         "<button id='dnd-begin'>Open the first door</button>"
         "</div>")

    :else
    (str "<div class='live-bar'>"
         "<input id='dnd-say' class='todo-input' style='flex:1' "
         "placeholder='say something at the table' spellcheck='false'>"
         "<button id='dnd-send'>Say it</button>"
         "<button id='dnd-save'>Save the ledger</button>"
         "</div>")))

(defn draw []
  (fedoras.llm/stick-scroll!
   "dnd-log"
   (fn []
     (html!
      (if (nil? @api-key)
        (str "<p><strong>Connect your key.</strong> You are the Ranger. The other "
             "five seats are live, and one of them is on the table. The key stays "
             "in this tab.</p>"
             "<div class='live-bar'>"
             "<input id='dnd-key' type='password' class='todo-input' style='flex:1' "
             "placeholder='sk-ant-...' autocomplete='off'>"
             "<button id='dnd-connect'>Connect</button>"
             "</div>"
             (if @key-warning? "<p style='color:#c0392b'>Enter a key first.</p>" ""))
        (str "<div id='dnd-log' style='max-height:26rem;overflow:auto'>"
             (apply str (map line-html @log))
             (if @busy? "<p><em>the table is thinking</em></p>" "")
             "</div>"
             (roll-html)
             (bar-html)
             (foot-html))))))
  (if (nil? @api-key)
    (on! "dnd-connect"
         (fn []
           (let [field (fedoras.reader/el "dnd-key")
                 key   (clojure.string/trim (.-value field))]
             (if (empty? key)
               (do (reset! key-warning? true) (draw))
               (do (reset! key-warning? false)
                   (reset! api-key key)
                   (set! (.-value field) "")
                   (draw))))))
    (do
      (when (roll-pending?)
        (on! "dnd-roll" (fn [] (resolve-roll! 0))))
      (when (and @roll-box (:resolved? @roll-box))
        (on! "dnd-rewrite" (fn [] (rewrite-roll!))))
      (when (empty? @log)
        (on! "dnd-begin" begin!))
      (when-not @ended?
        (on! "dnd-send" say!)
        (when-let [field (fedoras.reader/el "dnd-say")]
          (.focus field)
          (set! (.-onkeydown field)
                (fn [event] (when (= "Enter" (.-key event)) (say!))))))
      (on! "dnd-save"
           (fn [] (fedoras.llm/save-transcript! @log :dnd-session-four))))))

(draw)