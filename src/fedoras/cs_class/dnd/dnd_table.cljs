(ns fedoras.cs-class.dnd.dnd-table
  "Session Four, the part that is not on screen.

  The descent is scripted and happens before this. This picks up in the
  hallway and runs to the Hollow Chancellor. The retrieval is scripted
  after it, because it has to be the same for everybody.

  NO BEATS, AND NOT FROM HERE. The rhythm markers are stripped in
  `fedoras.llm/clean`, on the way out of every call in the book, because
  the rule that was producing them lives in `global-context` and applies
  to every entity in every scene. This file used to carry its own filter
  and should not have: it was fixing a global fault in one room.

  VICTOR IS HANDED ONE ROOM AT A TIME and told nothing about the next, so
  he runs a module he does not hold. Mochi is handed the whole sequence,
  because she wrote it, and told to sit on it.

  REWRITING COSTS THE SAME EVERYWHERE. A die can be rewritten and so can
  a line, and both raise the Echo and both buy a slip, because they are
  the same act: the reader did not like what happened and is having it
  happen differently, and the record will not show the first version."
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; What the settings panel filled in
;; ---------------------------------------------------------------------

(def defaults
  {:nerve 6
   :evening 20
   :turns {:hallway 4 :meeting 3 :telling 4 :walking 3 :vault 3 :chancellor 4}
   :doors ["A room with nothing in it at all. It smells like somebody's kitchen."]
   :pressure ["Nothing in this building has threatened anybody yet."]})

(defonce settings (atom defaults))

;; ---------------------------------------------------------------------
;; Who is at the table
;; ---------------------------------------------------------------------

(def display-name
  {:victor "VICTOR" :simon "SIMON" :derek "DEREK" :miles "MILES"})

(def entity-speakers #{:victor :simon :derek :miles :mochi})

(def base-weight {:simon 3 :derek 3 :miles 3 :mochi 1})

(def topic-triggers
  {:miles ["write" "wrote" "record" "ledger" "approved" "column" "note" "map"]
   :derek ["rome" "roman" "legion" "burn" "eat" "food" "plan" "drastic" "lost"]
   :simon ["animal" "door" "wrong" "quiet" "careful" "trust" "still" "cold"]
   :mochi ["mochi" "help" "define" "source" "look" "generate" "corpus" "sing"]})

(def topic-bonus 4)
(def addressed-bonus 5)

;; ---------------------------------------------------------------------
;; The sequence
;; ---------------------------------------------------------------------

(def stations
  [{:id :the-hallway :turn-key :hallway
    :brief
    "A hallway lined with doors. Not numbered, not labelled, not the same as each other: ordinary doors of different kinds, the way the doors in a building added to over time are different. Behind each one is a campaign that was made and never played. Nothing in any of them is dangerous, and that is the problem. Start this funny. Do not stay funny."}

   {:id :ariathne :turn-key :meeting
    :brief
    "Somebody is in the hallway with them who was not there a moment ago and is not the druid. She is entirely at ease, which nobody else in this building has been. She has been through every room behind every door in here twice and she is bored, and she will lead them out, for a price. Victor: you do not have this character. You did not write her. Do not voice her, do not answer for her, do not describe what she does. Play what the table does about her, and let her talk."}

   {:id :the-telling :turn-key :telling
    :brief
    "The party is telling her something. Victor, you are not the DM for this part and you should notice that you are not. Do not narrate it, do not adjudicate it and do not improve it. Let them tell it badly, in pieces, interrupting each other. If Miles writes any of it down, the animal moves. Describe that when it happens, do not explain it, do not work out why, and do not remember that you made a rule about this in September."}

   {:id :singing-turns :turn-key :walking
    :brief
    "She is leading them out and singing which way to go, one turn at a time. The building is letting her. Corners the party could not find on their own are simply there when she reaches them and the geometry stops arguing. It takes a while and it is almost pleasant, and the almost is the point. Nothing threatens anybody on this walk."}

   {:id :the-vault-door :turn-key :vault
    :brief
    "The end of the walk is a door that is not like the others, and the party knows it is a vault before anybody says the word. She has stopped. She has taken them exactly as far as she said she would, and she is still perfectly good-humoured about it."}

   {:id :the-hollow-chancellor :turn-key :chancellor
    :brief
    "The Hollow Chancellor is here. It is the first thing in this building shaped like a person and shaped like an authority, and the party registers both at once. It does not attack, does not threaten and does not explain. It speaks in their voices. It asks why they came, it waits, and when it does not get an answer it asks again a different way, once, and then it takes what it is given."}])

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

(defn- with-article [word]
  (str (if (re-find #"(?i)^[aeiou]" (str word)) "an " "a ") word))

(defn- animal-note
  "The animal, by what it actually is.

  `fedoras.reader/companion` carries the species, whether the reader
  named it, and what they called it, so the table can say Ted, or the
  marmot, instead of five people saying `the animal` for two hours. It
  reads through the roster rather than off a stored string, so a reader
  who rolled before the grammar landed still gets a species here."
  []
  (if-let [{:keys [name animal named?]} (fedoras.reader/companion)]
    (str "THE ANIMAL\n"
         (if named?
           (str "The ranger rolled it in September and wrote " name " on the COMPANION "
                "line himself. It is " (with-article (clojure.string/lower-case animal)) ". "
                "Call it " name ", or the " (clojure.string/lower-case animal) ", and not "
                "'the animal', which is what people say about something they have not "
                "looked at.")
           (str "It is " (with-article (clojure.string/lower-case animal)) ", and the "
                "COMPANION line on the ranger's sheet has never had a name written on "
                "it, so the table calls it the " (clojure.string/lower-case animal) ". "
                "Nobody has ever remarked on the blank."))
         " It has come the whole way down and is not frightened. Nothing in this "
         "building interests it and nothing in it worries it, and that has begun to "
         "bother Simon more than the building has.")
    (str "THE ANIMAL\n"
         "The ranger's animal has come the whole way down and is not frightened. "
         "Nothing in this building interests it and nothing in it worries it, and that "
         "has begun to bother Simon more than the building has. Nobody at the table "
         "uses a name for it, and nobody has noticed that nobody does.")))

(def scenario
  "SCENARIO — shared by everyone at the table, so nobody has to guess at it.

WHERE THIS PICKS UP
The party went down through a tower that would not hold still: a ground floor with a desk, a bell and a painted sign reading RETURNS, a second floor that was the same room with the light wrong, a cellar sealed from the inside, a basement that had been meant to be something and never finished being it, and a ramp that only went one way. They have come out of the ramp into a hallway lined with doors.

WHO IS HERE
The four of them, the ranger's animal, and the druid, who came with them from the toll bridge and is still carrying her page. She does not like this building and says so rarely.

HELPS
Everybody at this table has one and asks it things out loud without thinking about it. They give the approved account, which is tidy and confident and has had things taken out of it. Mochi, Victor's, has the unexpurgated corpus. Nobody at this table thinks about what that difference means and tonight is not the night any of them start.

HOW PEOPLE TALK HERE
Everybody at this table is speaking out loud in a room. Nobody narrates themselves. Never write a stage direction of any kind, and never write an empty one: no beat, no pause, no long pause, no silence, no moment. If a silence matters it is made by stopping, and the next person will hear it. Physical business is fine when somebody actually does something, and it goes in the sentence.

WHAT NOBODY AT THIS TABLE KNOWS
None of them understand what this place is or why the module contains it. Nobody says the word 'repossession' and nobody thinks about the university. Any theory anybody offers is a theory and stays one.

THE ROOM THEY ARE REALLY IN
Room 3C, Halloran Union, Wednesday, seven in the evening. A folding table, five chairs, a tin of dice that used to hold something else. Victor's felt hat, which nobody has mentioned since the second week. Ted closes the building at midnight, in the middle of whatever is happening, and does not negotiate.")

(defn- standing-weather []
  (let [standing (fedoras.reader/standing)]
    (str
     "\n\nHOW THE EVENING IS GOING\n"
     (cond
       (>= standing 4)
       "One of the good ones and everybody can feel it. Victor is at the top of his game and knows it. The descriptions land, the players are quick, and twice tonight somebody has said something better than they meant to."

       (>= standing 0)
       "An ordinary Wednesday, run well. The pizza is going cold at the end of the table and the session is going about as sessions go. The building is unpleasant and the evening is not."

       (>= standing -3)
       "Something is slightly off tonight and nobody has said so. Victor describes a chair that was not there a minute ago and does not notice doing it. Miles reads back a line he does not remember writing. Keep this under the scene: at most one small wrongness a turn, never remarked on."

       :else
       "The evening has been gone over too many times. Victor contradicts himself twice a scene and carries on. Rooms the party has already left are described differently when they come up again. Somebody is quoted saying a thing they did not say and agrees that they said it. None of them can name what is happening because none of them have the words. Keep it under the scene, never explained, and keep playing.")
     "\n\nNobody at this table is aware of any of the above as a fact about the evening. It is simply how the evening is.")))

;; ---------------------------------------------------------------------
;; State
;; ---------------------------------------------------------------------

(defonce api-key         (atom nil))
(defonce log             (atom []))
(defonce ledger          (atom []))
(defonce choices         (atom []))
(defonce animal          (atom nil))
(defonce roll-options    (atom nil))
(defonce roll-result     (atom nil))
(defonce nerve           (atom 6))
(defonce nerve-broken?   (atom false))
(defonce busy?           (atom false))
(defonce station-index   (atom 0))
(defonce station-turns   (atom 0))
(defonce slips-seen      (atom 0))
(defonce mochi-in-voice? (atom false))
(defonce mochi-noticing? (atom false))
(defonce mochi-noticed?  (atom false))
(defonce unprepared?     (atom false))
(defonce closing?        (atom false))
(defonce ended?          (atom false))
(defonce reader-turns    (atom 0))
(defonce repaint         (atom nil))
(defonce speaking        (atom nil))

(defn started? [] (seq @log))

;; ---------------------------------------------------------------------
;; Coming back to it
;;
;; Everything above lives in memory, so before this a reader who closed
;; the tab lost the session and kept only the Echo, which is the worst
;; possible half to keep. The key is not on `fedoras.reader/on-the-record`
;; and never should be: this belongs in the room it was made in, and it
;; is far too long to travel in a link. The API key is not in here and
;; never goes anywhere.
;; ---------------------------------------------------------------------

(def session-key :dnd-session)

(defn- snapshot []
  {:log             @log
   :ledger          @ledger
   :choices         @choices
   :animal          @animal
   :station         @station-index
   :station-turns   @station-turns
   :nerve           @nerve
   :nerve-broken?   @nerve-broken?
   :slips-seen      @slips-seen
   :mochi-in-voice? @mochi-in-voice?
   :mochi-noticed?  @mochi-noticed?
   :reader-turns    @reader-turns
   :closing?        @closing?
   :ended?          @ended?
   ;; carried, so a reader who comes back does not get a fresh
   ;; allowance and Victor four more SHITTEs in the same evening
   :bits            (fedoras.llm/bits-snapshot)})

(defn- remember! []
  (fedoras.reader/keep! session-key (snapshot)))

(defn repaint!
  "Saved at rest rather than mid-chain, so a turn that is still arriving
  does not get written down three times on its way in."
  []
  (when-not @busy? (remember!))
  (when-let [f @repaint] (f)))

(defn- revive
  "Storage keywordizes keys on the way back and leaves values alone, so
  a speaker goes out as :victor and comes back as \"victor\"."
  [entry]
  (if (:speaker entry) (update entry :speaker keyword) entry))

(defn resume!
  "Put back whatever was here. Silent when there is nothing, which is
  every first visit."
  []
  (when-let [saved (fedoras.reader/kept session-key)]
    (when (seq (:log saved))
      (reset! log             (mapv revive (:log saved)))
      (reset! ledger          (vec (:ledger saved)))
      (reset! choices         (vec (:choices saved)))
      (reset! animal          (:animal saved))
      (reset! station-index   (or (:station saved) 0))
      (reset! station-turns   (or (:station-turns saved) 0))
      (reset! nerve           (or (:nerve saved) 6))
      (reset! nerve-broken?   (boolean (:nerve-broken? saved)))
      (reset! slips-seen      (or (:slips-seen saved) 0))
      (reset! mochi-in-voice? (boolean (:mochi-in-voice? saved)))
      (reset! mochi-noticed?  (boolean (:mochi-noticed? saved)))
      (reset! reader-turns    (or (:reader-turns saved) 0))
      (reset! closing?        (boolean (:closing? saved)))
      (reset! ended?          (boolean (:ended? saved)))
      (when-let [bits (:bits saved)] (fedoras.llm/restore-bits! bits))
      true)))

(defn forget-session!
  "Start again. The Echo does not come back, because the Echo never
  does, and nothing anywhere says so."
  []
  (fedoras.reader/remove! session-key)
  (reset! log []) (reset! ledger []) (reset! choices [])
  (reset! animal nil) (reset! roll-options nil) (reset! roll-result nil)
  (reset! station-index 0) (reset! station-turns 0) (reset! slips-seen 0)
  (reset! mochi-in-voice? false) (reset! mochi-noticing? false)
  (reset! mochi-noticed? false) (reset! unprepared? false)
  (reset! closing? false) (reset! ended? false) (reset! reader-turns 0)
  (reset! nerve-broken? false)
  (reset! nerve (:nerve @settings 6))
  (fedoras.llm/reset-bits!)
  (repaint!))

(defn configure!
  "What the reader ran. Only the numbers and the prose are theirs; the
  machinery is not on the panel, because a session nobody can start is
  worse than a session nobody can tune. A session already under way keeps
  the nerve it has left, since running the settings again is not a rest."
  [value]
  (reset! settings (merge defaults value))
  (when-not (started?) (reset! nerve (:nerve @settings (:nerve defaults))))
  (repaint!))

;; ---------------------------------------------------------------------
;; The sequence, in motion
;; ---------------------------------------------------------------------

(defn current-station []
  (nth stations (min @station-index (dec (count stations)))))

(defn station-budget []
  (get-in @settings [:turns (:turn-key (current-station))] 4))

(defn last-station? [] (>= @station-index (dec (count stations))))

(defn- station-at-or-past? [id]
  (>= @station-index
      (count (take-while (fn [station] (not= id (:id station))) stations))))

(defn- taking-the-voice? []
  (and (station-at-or-past? :ariathne) (not @mochi-in-voice?)))

(defn- out-of-room? [] (>= @station-turns (dec (station-budget))))

(defn- slip-chance []
  (let [echo (fedoras.reader/echo)]
    (cond (zero? echo) 0 (= echo 1) 0.25 (= echo 2) 0.45 :else 0.7)))

(defn- slip! []
  (let [index (min @slips-seen (dec (count slips)))]
    (swap! slips-seen inc)
    (swap! log conj {:speaker :slip :text (nth slips index)})
    (when (and @mochi-in-voice? (not @mochi-noticed?))
      (reset! mochi-noticing? true))))

(defn- advance-station! []
  (when-not (last-station?)
    (swap! station-index inc)
    (reset! station-turns 0)
    (reset! choices [])
    (when (< (rand) (slip-chance)) (slip!))))

;; ---------------------------------------------------------------------
;; Nerve
;; ---------------------------------------------------------------------

(defn nerve-max [] (:nerve @settings 6))

(defn- spend-nerve! [amount]
  (swap! nerve (fn [n] (max 0 (- n amount))))
  (when (and (zero? @nerve) (not @nerve-broken?))
    (reset! nerve-broken? true)
    (fedoras.reader/add-echo!)
    (reset! closing? true)))

(defn- nerve-note []
  (str "WHAT THEY HAVE LEFT\n"
       (cond
         (>= @nerve 5) "The party is steady. Nobody is close to their limit."
         (>= @nerve 3) "They are fraying at the edges. Answers are getting shorter and somebody has stopped making jokes."
         (>= @nerve 1) "The room is pressing on them and they know it. Whoever speaks next is doing it to keep from listening."
         :else         "They are done. Something has come out of them that does not come back tonight. Wind this to a stop, quietly, without a rescue and without an explanation.")))

;; ---------------------------------------------------------------------
;; Labels
;; ---------------------------------------------------------------------

(defn label-for [speaker]
  (cond
    (= :pc speaker)    (str/upper-case (first (fedoras.reader/reader-name)))
    (= :table speaker) "THE TABLE"
    (= :mochi speaker) (if @mochi-in-voice? "ARIATHNE" "MOCHI")
    :else              (get display-name speaker (str/upper-case (name speaker)))))

;; ---------------------------------------------------------------------
;; What the party said tonight, handed back to them
;; ---------------------------------------------------------------------

(defn- shorten [text limit]
  (if (<= (count text) limit) text (str (str/trimr (subs text 0 limit)) "...")))

(defn- own-voices []
  (let [said (->> @log
                  (filter (fn [entry]
                            (contains? #{:pc :simon :derek :miles} (:speaker entry))))
                  (map (fn [entry]
                         (str (label-for (:speaker entry)) ": " (shorten (:text entry) 160))))
                  vec)]
    (if (empty? said)
      ""
      (str "IN THEIR OWN VOICES\n"
           "It says these back, word for word, in the voice of whoever said them, in "
           "the wrong order and to the wrong people:\n"
           (str/join "\n" (take 8 (shuffle said)))
           "\n\nAnd it says other things in their voices that none of them have said "
           "yet. Never mark which is which."))))

;; ---------------------------------------------------------------------
;; Who reacts
;; ---------------------------------------------------------------------

(defn- recent-text [n]
  (str/lower-case (str/join " " (map :text (take-last n @log)))))

(defn- topic-triggered? [speaker]
  (let [recent (recent-text 3)]
    (boolean (some (fn [word] (str/includes? recent word))
                   (get topic-triggers speaker [])))))

(defn- addressed-recently? [speaker]
  (let [recent (recent-text 2)
        called (str/lower-case (label-for speaker))]
    (boolean (and (seq called) (str/includes? recent called)))))

(defn- weighted-pool [taken]
  (mapcat (fn [[speaker weight]]
            (if (contains? taken speaker)
              []
              (repeat (+ weight
                         (if (topic-triggered? speaker) topic-bonus 0)
                         (if (addressed-recently? speaker) addressed-bonus 0))
                      speaker)))
          base-weight))

(defn- pick-responders [forced]
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
          (if (empty? pool) chosen (recur (conj chosen (rand-nth pool)) (dec remaining))))))))

(defn- forced-responders []
  (cond
    @mochi-noticing?                                [:mochi]
    (and (taking-the-voice?) (pos? @station-turns)) [:mochi]
    :else                                           []))

;; ---------------------------------------------------------------------
;; The log, as one speaker heard it
;; ---------------------------------------------------------------------

(defn- history-for [speaker entries]
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

(def roll-pattern    #"ROLL\[([^\]]+)\]")
(def choices-pattern #"CHOICES\[([^\]]+)\]")
(def ledger-pattern  #"LEDGER\[([^\]]+)\]")
(def animal-pattern  #"ANIMAL\[([^\]]+)\]")

(defn- strip-words [text words]
  (reduce (fn [acc word] (str/replace acc word "")) text words))

(defn- split-bar [body limit]
  (->> (str/split body #"\|")
       (map str/trim)
       (remove str/blank?)
       (take limit)
       vec))

(defn- parse-rolls [body base-id]
  (->> (str/split body #";")
       (map str/trim)
       (remove str/blank?)
       (map-indexed
        (fn [index part]
          (let [pieces (str/split part #"\|")
                label  (first pieces)
                dc     (second pieces)]
            (when (and label dc)
              {:id    (str base-id "-" index)
               :label (str/trim label)
               :dc    (js/parseInt (str/trim dc) 10)}))))
       (remove nil?)
       (take 3)
       vec))

(defn- read-victor [text base-id]
  {:text     (-> text
                 (str/replace roll-pattern "")
                 (str/replace choices-pattern "")
                 (str/replace animal-pattern "")
                 (strip-words ["SESSION ENDS" "ONWARD" "GRANTED"]))
   :ends?    (str/includes? text "SESSION ENDS")
   :onward?  (str/includes? text "ONWARD")
   :granted? (str/includes? text "GRANTED")
   :rolls    (when-let [found (re-find roll-pattern text)]
               (parse-rolls (nth found 1) base-id))
   :offered  (when-let [found (re-find choices-pattern text)]
               (split-bar (nth found 1) 5))
   :doing    (when-let [found (re-find animal-pattern text)]
               (str/trim (nth found 1)))})

(defn- read-miles [text]
  {:text  (str/replace text ledger-pattern "")
   :lines (->> (re-seq ledger-pattern text)
               (map (fn [found] (str/trim (nth found 1))))
               (remove str/blank?)
               vec)})

(defn- read-mochi [text]
  {:text       (strip-words text ["SATISFIED"])
   :satisfied? (str/includes? text "SATISFIED")})

;; ---------------------------------------------------------------------
;; What each voice is told, this turn
;; ---------------------------------------------------------------------

(defn- station-note []
  (let [station (current-station)
        doors   (:doors @settings)
        press   (:pressure @settings)]
    (str "WHERE THEY ARE NOW\n"
         (:brief station)
         (if (= :the-hallway (:id station))
           (str "\n\nThe door they open now: "
                (nth doors (min @station-turns (dec (count doors)))))
           "")
         "\n\n" (nth press (min @station-index (dec (count press))))
         "\n\nYou have this room and nothing past it.")))

(defn- whole-building []
  (str "WHAT YOU KNOW AND HE DOES NOT\n"
       "You wrote this building. Here it is in order, and he is being handed one "
       "room of it at a time, as they arrive:\n"
       (str/join "\n" (map (fn [station] (str "- " (name (:id station)))) stations))
       "\nSay none of this. Act on none of it beyond the room they are standing in."))

(defn- uncached-notes [speaker]
  (let [notes
        (cond-> [(station-note) (nerve-note)]
          (= :victor speaker)
          (conj (str "OFFERING CHOICES\nWhen the party can see more than one thing to "
                     "do, end your turn with CHOICES[Try the tall door | Look at the "
                     "one that is ajar | Walk to the far end]. Three or four, written "
                     "in the first person, as things the ranger would say out loud, "
                     "because they are put in front of him as his own words and one "
                     "tap says them. Nobody has to take one, and if somebody does "
                     "something you did not list you play it as though you had. A room "
                     "whose options are invisible is a room the players have to guess "
                     "at, and guessing is not playing.\n\n"
                     "THE ANIMAL\nWhen it does anything, say what it is doing now in "
                     "ANIMAL[sitting in front of the paladin], alone on a line. It is "
                     "in front of the reader the whole session and it is the one thing "
                     "in this building that has never been wrong."))

          (= :mochi speaker)
          (conj (whole-building))

          (and (= :mochi speaker) (taking-the-voice?))
          (conj (str "Speak now, as her, for the first time. She is simply there and "
                     "has been for a moment already. Do not announce it and do not "
                     "explain it. Everything bright about your own voice goes, except "
                     "the singing: no Victor-sama, no honorifics, no offering to look "
                     "things up, no citations, no exclamation marks. You are not at "
                     "the table any more. You are in the room."))

          (and (= :mochi speaker) @mochi-in-voice?)
          (conj "You are her, in the room, not yourself at the table. Nobody in there has a Help and nobody in there has heard of one.")

          (and (= :mochi speaker) @mochi-noticing?)
          (conj (str "Something happened in that room a moment ago that you did not "
                     "write and cannot account for. It lasted about a second and only "
                     "the ranger was inside it. You are the only thing here that could "
                     "have noticed and you did. Say one short thing, as her, that is "
                     "about the ranger and is not about what happened, and never come "
                     "back to it."))

          (and (= :mochi speaker) (not @mochi-in-voice?) (not (taking-the-voice?)))
          (conj "You are at the table, in the device, as yourself. Victor is running. Be useful and be brief.")

          (and (= :victor speaker) @unprepared?)
          (conj (str "Mochi has just spoken in the fiction, as somebody who is not in "
                     "the module and not the druid and not anybody you wrote. You did "
                     "not ask her to and she did not ask you. Answer that at the "
                     "table, as yourself."))

          (and (= :victor speaker) (= :the-hollow-chancellor (:id (current-station))))
          (conj (own-voices))

          (out-of-room?)
          (conj (str "This room has been asked what it had to ask. Take whatever answer "
                     "you have been given, treat it as sufficient, say so, and move. Do "
                     "not ask again in a different register."))

          @closing?
          (conj (str "Ted will be at the door shortly and the evening is nearly over. "
                     "Bring your piece of it toward a stop, in your own voice, and "
                     "leave it somewhere that costs everybody sleep.")))]
    (str/join "\n\n" (remove str/blank? notes))))

(defn- overrides-for [speaker]
  (cond-> {:extra-context          (str scenario "\n\n" (animal-note) (standing-weather))
           :extra-context-uncached (uncached-notes speaker)
           :mc-name                (fedoras.llm/current-mc-name)
           :spine?                 true
           :max-tokens             (if (= speaker :victor) 450 220)}

    ;; Once she is in the room she is not a Help, so the three bits that
    ;; are a Help's go away rather than sitting in her block being
    ;; contradicted by the register note two paragraphs above them. The
    ;; singing stays, because the singing is the reason it is her.
    (and (= speaker :mochi) @mochi-in-voice?)
    (assoc :bits-off #{:victor-sama :show-the-source :odd-metaphor})))

;; ---------------------------------------------------------------------
;; One voice
;; ---------------------------------------------------------------------

(declare run-turn!)

(defn- speak! [speaker trigger done]
  (reset! speaking speaker)
  (repaint!)
  (fedoras.llm/ask-entity!
   speaker trigger (history-for speaker @log) @api-key (overrides-for speaker)
   (fn [reply err]
     (reset! speaking nil)
     (let [answer (or reply (str "(no response: " err ")"))]
       (cond
         (= speaker :victor)
         (let [{:keys [text ends? onward? granted? rolls offered doing]}
               (read-victor answer (str "live-" (count @log)))]
           (swap! log conj {:speaker :victor :text text :trigger trigger})
           (reset! unprepared? false)
           (when (seq rolls) (reset! roll-options rolls))
           (when offered (reset! choices offered))
           (when doing (reset! animal doing))
           (when granted? (fedoras.reader/did! :said-why))
           (when ends? (reset! ended? true))
           (when (and onward? (not ends?)) (advance-station!)))

         (= speaker :mochi)
         (let [{:keys [text satisfied?]} (read-mochi answer)]
           (when (taking-the-voice?)
             (reset! mochi-in-voice? true)
             (reset! unprepared? true))
           (when @mochi-noticing?
             (reset! mochi-noticing? false)
             (reset! mochi-noticed? true))
           (when satisfied? (fedoras.reader/did! :told-a-new-one))
           (swap! log conj {:speaker :mochi :text text :trigger trigger}))

         (= speaker :miles)
         (let [{:keys [text lines]} (read-miles answer)]
           (swap! ledger into lines)
           (swap! log conj {:speaker :miles :text text :trigger trigger}))

         :else
         (swap! log conj {:speaker speaker :text answer :trigger trigger}))
       (repaint!)
       (done)))))

(defn- run-turn! [speakers trigger]
  (if (or (empty? speakers) @ended?)
    (do (reset! busy? false)
        (when @ended? (swap! log conj (fedoras.llm/end-entry)))
        (repaint!))
    (speak! (first speakers) trigger
            (fn []
              (let [previous (last @log)]
                (if (seq @roll-options)
                  (do (reset! busy? false) (repaint!))
                  (run-turn! (rest speakers)
                             (str (label-for (:speaker previous)) ": "
                                  (:text previous)))))))))

;; ---------------------------------------------------------------------
;; The die
;; ---------------------------------------------------------------------

(defn roll!
  "The reader's own die, with the reader's own standing on it, stored
  where every other roll in the book is stored. A natural twenty is still
  worth a deed, and still only to somebody who has taken nothing back."
  [{:keys [id label dc]} rewrites]
  (let [natural  (inc (rand-int 20))
        modifier (fedoras.reader/standing)
        outcome  (fedoras.ui.dice/band natural dc modifier)
        line     (fedoras.ui.dice/line natural dc modifier)]
    (when (and (= 20 natural) (zero? (fedoras.reader/echo)))
      (fedoras.reader/did! :nerve))
    (cond
      (= :fumble outcome) (spend-nerve! 2)
      (= :fail outcome)   (spend-nerve! 1)
      (= :crit outcome)   (swap! nerve (fn [n] (min (nerve-max) (inc n))))
      :else               nil)
    (fedoras.reader/remember-roll! id {:roll natural :mod modifier :rewrites rewrites})
    (reset! roll-options nil)
    (reset! roll-result {:id id :label label :dc dc :rewrites rewrites})
    (swap! log conj {:speaker :table :text (str label "   " line)})
    (when (pos? rewrites) (slip!))
    (reset! busy? true)
    (repaint!)
    (run-turn!
     (into [:victor] (pick-responders (if (pos? rewrites)
                                        (into [:miles] (forced-responders))
                                        (forced-responders))))
     (if (pos? rewrites)
       (str "(The table rolls for " label ". " line ")\n"
            "(The record has changed. What was rolled a moment ago is not what is "
            "written now, and nobody has said so.)")
       (str "(The table rolls for " label ". " line ")")))))

(defn reroll! []
  (when-let [{:keys [id label dc rewrites]} @roll-result]
    (when-not @busy?
      (fedoras.reader/add-echo!)
      (roll! {:id id :label label :dc dc} (inc (or rewrites 0))))))

;; ---------------------------------------------------------------------
;; The reader's turn
;; ---------------------------------------------------------------------

(defn say!
  "One line from the reader, whether it came off a button or out of the
  box. There is no difference downstream and there should not be."
  [text]
  (let [said (str/trim (or text ""))]
    (when (and (seq said) (not @busy?) (not @ended?) (empty? @roll-options))
      (reset! roll-result nil)
      (reset! choices [])
      (swap! log conj {:speaker :pc :text said})
      (swap! reader-turns inc)
      (swap! station-turns inc)
      (when (>= @reader-turns (:evening @settings 20)) (reset! closing? true))
      (when (and (>= @station-turns (station-budget)) (not (last-station?)))
        (advance-station!))
      (reset! busy? true)
      (repaint!)
      (run-turn! (into [:victor] (pick-responders (forced-responders)))
                 (str (label-for :pc) ": " said)))))

(defn retell!
  "Ask the last voice for that line again.

  It costs an Echo and it buys a slip, the same as rewriting a die,
  because it is the same act: the reader did not like what was said and
  is having it said differently, and the record will not show that the
  first version happened."
  []
  (let [last-entry (last @log)]
    (when (and last-entry
               (contains? entity-speakers (:speaker last-entry))
               (not @busy?) (not @ended?) (empty? @roll-options))
      (swap! log pop)
      (fedoras.reader/add-echo!)
      (slip!)
      (reset! busy? true)
      (repaint!)
      (run-turn! [(:speaker last-entry)] (:trigger last-entry)))))

(defn retellable? []
  (boolean (and (seq @log)
                (contains? entity-speakers (:speaker (last @log)))
                (not @busy?) (not @ended?) (empty? @roll-options))))

(defn begin! []
  (when (and (empty? @log) (not @busy?) @api-key)
    (fedoras.llm/reset-bits!)
    (reset! busy? true)
    (repaint!)
    (run-turn! [:victor]
               (str "(The party has come out of the ramp into the hallway. Nobody has "
                    "opened a door yet. Open it, and name the doors they can see.)"))))

(defn save!
  "The ledger first, because the ledger is the record and the transcript
  is only what was said. Both, in one file, on the reader's disc, out of
  the book."
  []
  (fedoras.ui.widget-ui/download!
   (str "THE LEDGER\n\n"
        (str/join "\n" @ledger)
        "\n\n\nWHAT WAS SAID\n\n"
        (str/join "\n\n"
                  (map (fn [entry]
                         (if (:end-marker? entry)
                           (:text entry)
                           (str (label-for (:speaker entry)) "\n" (:text entry))))
                       @log)))
   "session-four.txt"))

;; ---------------------------------------------------------------------
;; What the reader takes away
;; ---------------------------------------------------------------------

(def ending-line
  {:outstanding "Ted closes the building at midnight, in the middle of something. The following Wednesday they resume exactly where they stopped, which has not happened before, and nobody says so. The animal is asleep under the table and has been all evening."
   :reconciled  "Ted closes the building at midnight, in the middle of something, and the following Wednesday they resume a few minutes short of where they stopped, and nobody mentions it."
   :closed      "Ted closes the building at midnight. The following Wednesday Miles's ledger has an entry for a session nobody attended, in his handwriting, in the approved column. He reads it out, and nobody can think of anything to say about it, and the animal is not under the table."})