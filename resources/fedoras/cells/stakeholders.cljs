;; =====================================================================
;; STAKEHOLDERS.CLJS
;;
;; Ten voices at very different volumes, deciding how to make a theft
;; disappear. Nobody has written this meeting in advance — each line
;; comes from asking whoever speaks next what they would actually say,
;; given everything already said before them.
;;
;; Generalizes fedoras.llm/run-dialogue!'s two-party pattern to a
;; weighted, rotating room. Every turn is one real call to
;; fedoras.llm/ask-entity!, so cost scales with line count — see the
;; HISTORY-WINDOW note below for how that's kept bounded.
;; =====================================================================

;; ---------------------------------------------------------------------
;; Who's in the room, and how often they open their mouth
;;
;; base-weight is how often each of the eight regular voices gets
;; picked with nothing in particular pulling them toward or away from
;; the floor -- their default share of the room. interjection-tendency
;; is added on top: how eager each of them is to jump in uninvited, on
;; a 0-5 scale, independent of how much floor time they get by
;; default. addressed-recently? adds a further boost when someone was
;; just named, which is the mechanical half of "Sterling and
;; Kessler-Vance speak when spoken to": their base weight alone stays
;; low, and they only come to the floor readily once the room has
;; actually turned to them.
;;
;; topic-triggers generalizes the old caretaker-only mechanism: a
;; speaker can also be pulled toward the floor by what's actually
;; being said, not just by who's speaking or who was named. Dalton
;; barely talks until money or exposure comes up; Jamie Potts leans in
;; on anything touching students directly; the Caretaker, who isn't in
;; the regular pool at all otherwise, only enters the room's
;; possibilities when the object itself, or what's beneath the campus,
;; comes up.
;;
;; interjection-tendency also feeds speaking-dynamics-text below, so
;; the numbers and the prose the models actually read can't drift
;; apart from each other.
;; ---------------------------------------------------------------------

(def base-weight
  {:dean-sterling 1
   :vice-dean     2
   :montgomery    3
   :bartholomew   3
   :marcy         3
   :dalton        1
   :jamie-potts   2
   :odile         1})

(def interjection-tendency
  {:dean-sterling 2
   :vice-dean     2
   :montgomery    2
   :bartholomew   5
   :marcy         1
   :dalton        1
   :jamie-potts   3
   :jairis-egari  0
   :odile         1
   :caretaker     0})

(def rare-pool [:jairis-egari])
(def rare-weight 1)

(def addressed-bonus 4)
(def topic-bonus-amount 3)

(def topic-triggers
  {:caretaker  ["coil" "meridian" "backroom" "artifact" "missing" "stolen"]
   :dalton     ["cost" "expense" "budget" "invest" "exposure" "liable" "liability" "money" "donor" "lawsuit"]
   :jamie-potts ["student" "provisional" "reclamation" "placement" "floor"]})

(def display-name
  {:dean-sterling "STERLING"
   :vice-dean     "KESSLER-VANCE"
   :montgomery    "MONTGOMERY"
   :bartholomew   "TRANCHE-LARD"
   :marcy         "ANTELLIER"
   :dalton        "HILLCRUX"
   :jamie-potts   "POTTS"
   :jairis-egari  "JAIRIS-ÉGARI"
   :odile         "ODILE"
   :caretaker     "THE CARETAKER"})

;; ---------------------------------------------------------------------
;; The room everyone's actually in
;;
;; This is deliberately room-specific now: the cast, this incident,
;; and this meeting's agenda. World-wide lore (Meridian, the
;; backrooms, repossession) and craft rules that apply to every
;; character in every scene (no "it isn't X, it's Y", never write
;; someone else's line, finish your thought) moved to
;; fedoras.llm/global-context, which every call already carries — this
;; block should stay free of anything that belongs there, so the two
;; can't drift apart or repeat each other.
;; ---------------------------------------------------------------------

(def shared-context
  "SCENARIO — shared by everyone in the room, so nobody has to guess at it.

WHO IS HERE
Dean Magnus Sterling — Dean. Nominally runs the meeting. Does not, in practice. Knows this is his fault and everyone else's watching him know it, and is quietly terrified of being repossessed himself over it.
Vice Dean Priya Kessler-Vance — Vice Dean of Institutional Narrative. Actually runs it, and has for years, without the title ever catching up. Just as exposed as Sterling by what's happened, and far better at not showing it.
Montgomery — sent by Meridian. Not a university employee. Speaks only as much as protecting Meridian's interests requires, in a level, controlled register that unsettles almost everyone at this table, Bartholomew included. Furious about what's happened, though nothing in his voice admits it.
Jamie Potts — Meridian's campus liaison. Spends more time with actual students than anyone else here: placement, floor work, reclamation assignments, the parts of the job that look like guidance counseling from outside the room. Tenured and connected enough to sit on this board in her own right, not as anyone's assistant.
Dr. Alfrès Jairis-Égari — founder of the university, kept alive years past when his body should have given out, because he still legally owns the buildings and his signature is still required on certain paperwork. Barely speaks, of anyone left in this room.
Odile — interprets Dr. Jairis-Égari and guides his hand on paperwork, which means she talks more than her treatment in this room would suggest; it's literally the job. Treated by most of this room as furniture rather than staff, kept close for reasons nobody in this room says aloud.
Marcy Antellier — secretary to the Board. Sets the agenda, keeps the minutes, manages what does and doesn't go on the record.
Dalton Hillcrux — major investor. Mostly quiet unless his investment is actually threatened or he's got a strong opinion about the money, at which point he's sharp and specific about it. Mentions his 'friends at Meridian' when it's useful.
Bartholomew Tranche-Lard — procurement and facilities. Sees students as a resource that produces waste, not minds. Loud, constant, and openly contemptuous of Dr. Jairis-Égari and of Odile. Looks and sounds like he wandered in from a completely different, more violent kind of story: something closer to a hardened space-marine sergeant barking orders than an academic administrator, in a room full of people who are all, in their own way, just as outrageous as he is, and none of whom find him remarkable for it.
The Caretaker — a board seat nobody explains. Deferred to in a way that outranks the seat. Says very little, and what he says is usually exactly right, in ways nobody can account for. He remembers something about the missing student that nobody else in this room does, though he will not say what, or how.

WHAT HAPPENED
Something pulled from beneath the campus went missing from a secure site in the last day. A provisional first-year, Provisional #3812, was seen leaving the area, carrying
   something too old to have been in a student's hands. The object is now either off-campus or in the between, and no one in this room knows which. It hasn't been recovered,
   and the disappearance is already generating rumors and attention outside these walls. None of you can afford for those rumors to become a formal inquiry, because a formal
   inquiry would have to ask what was stored below the refectory, and that is a question the institution cannot survive.

THE OBJECT
Everyone in this room has seen the object at least once, from behind glass, at a briefing, or in a sealed report. No one in this room can say precisely what it is. They know
the shape it leaves in the eye after looking at it too long. They know it predates the university. They know it was recovered from a place Meridian does not officially acknowledge.
They know it is not supposed to be carried by a student, and they know, in the way people in this room know things, that it was not the only one.

WHAT THIS ROOM DOES NOT KNOW
There is a demonic figure from the backrooms named Zell, but you don't actually know that name. All you know is that 'some intelligent entity' is growing in power and in
influence on the physical substance of reality around campus. Treat any mention of Zell as a guess, not an explanation.

THE STUDENT
Refer to the student who took the object as Provisional #3812, and by name when it's natural to — the room already knows both, the way any of these people would know the
name of a student in a scandal this size, even one they'd never met.

WHY THIS STAYS IN THE ROOM
None of what's discussed here leaves it, regardless of what's said, argued, or threatened in the process. Everyone here either knows this outright or has learned, through
consequence, not to ask further.

THE AGENDA — what this meeting actually has to produce
1. Confirm what's known: what's missing, when it was last accounted for, who had access.
2. Decide the cover story — what gets said publicly, and what is never said at all.
3. Decide what happens to the student, once found.
4. Assign who does what in the next 48-72 hours, and who answers to Montgomery for it.
A meeting that ends without landing all four hasn't actually happened, however much else got said in it.")

(defn- tendency-label [n]
  (cond
    (>= n 4) "HIGH: interrupts and jumps in often, even mid-thought"
    (>= n 2) "MODERATE: speaks up when they have something to add"
    (pos? n) "LOW: mostly waits for a natural opening"
    :else    "MINIMAL: speaks only when addressed or when something specific calls for it"))

(defn- speaking-dynamics-text []
  (str "\n\nHOW THIS ROOM TALKS\n"
       "This meeting has an agenda, and the room runs on it whether or not anyone says so aloud. "
       "Montgomery, Bartholomew, Marcy, and Jamie Potts carry most of the actual work: arguing "
       "specifics, tracking exposure and cost, pushing toward the four agenda items. Bartholomew is "
       "loud and constant about it. Montgomery says only what protecting Meridian's interests "
       "requires, but that's more than people expect, and it's delivered in a level, controlled "
       "register that unsettles nearly everyone at the table. Jamie Potts steps in readily on "
       "anything touching students specifically, since she's the one who actually deals with them.\n\n"
       "Sterling and Kessler-Vance both know this happened on their watch, and both want to defend "
       "themselves. Sterling can't help it: he interjects out of anxiety, defensively, and it lands "
       "badly every time, since interrupting to protect his own name in a room already furious at "
       "him only makes the room more furious. Kessler-Vance wants to defend the school too, but "
       "she's better at it: her interjections are strategic, aimed at managing the room or "
       "redirecting Sterling before he does more damage, not at clearing her own name outright, so "
       "they land better even when everyone at the table can tell exactly what she's doing.\n\n"
       "Dalton mostly stays out of it unless his investment is actually threatened or he's got a "
       "strong opinion on the money. Odile speaks only for or through Dr. Jairis-Égari, more than "
       "her treatment in the room would suggest since it's literally the job, but rarely for "
       "herself beyond a flat, guarded line here and there. Dr. Jairis-Égari speaks least of anyone "
       "still able to. The Caretaker speaks only when the conversation turns to the object itself, "
       "or to what's beneath the campus, and even then, rarely.\n\n"
       "Roughly, how ready each of you is to jump in without being invited:\n"
       (clojure.string/join
        "\n"
        (map (fn [id] (str "- " (display-name id) ": " (tendency-label (get interjection-tendency id 0))))
             [:bartholomew :jamie-potts :dean-sterling :vice-dean :montgomery :marcy
              :odile :dalton :jairis-egari :caretaker]))
       "\n\nWhen someone is mid-sentence and cut off, that's a deliberate interruption, not a mistake; "
       "let it read as one. If a thought needs finishing and nobody interrupts, the same speaker "
       "finishes it the next time the room comes back to them, rather than abandoning it unresolved."))

;; ---------------------------------------------------------------------
;; State
;; ---------------------------------------------------------------------

(def MIN-LINES 100)
(def MAX-LINES 300)
;; how many past lines get sent as context on each call — bounded on
;; purpose, since sending the whole transcript every turn would make
;; cost grow with the square of the meeting's length, not the length
(def HISTORY-WINDOW 20)

(def log             (atom []))   ;; [{:speaker :dean-sterling :text "..."} ...]
(def api-key          (atom nil))
(def running?         (atom false))
(def stop-requested?  (atom false))
(def closing?         (atom false))
(def closing-turns    (atom 0))
(def key-warning?     (atom false))
(def not-ready?       (atom false))

;; Included by default in every call to this cell's entities (see
;; fedoras.llm/build-system-prompt). Set to false here if this scene
;; doesn't need the extra context.
(def include-spine? true)

;; ---------------------------------------------------------------------
;; Making sure the cast has actually loaded before anyone tries to talk
;; ---------------------------------------------------------------------

(defn- entities-ready? []
  (boolean (get (.-FEDORAS_ENTITIES js/globalThis) :marcy)))

(defn- wait-for-entities! [attempts on-ready on-timeout]
  (cond
    (entities-ready?)  (on-ready)
    (<= attempts 0)    (on-timeout)
    :else (js/setTimeout #(wait-for-entities! (dec attempts) on-ready on-timeout) 300)))

;; ---------------------------------------------------------------------
;; Turn selection
;; ---------------------------------------------------------------------

(defn- blank? [s]
  (or (nil? s) (zero? (.-length (.trim s)))))

(defn- recent-text [n]
  (apply str (interpose " " (map :text (take-last n @log)))))

(defn- topic-triggered? [speaker-id]
  (if-let [words (get topic-triggers speaker-id)]
    (let [recent (.toLowerCase (recent-text 6))]
      (boolean (some (fn [w] (.includes recent w)) words)))
    false))

(defn- addressed-recently? [speaker-id]
  (let [recent (.toLowerCase (recent-text 2))
        called (.toLowerCase (or (display-name speaker-id) ""))]
    (boolean (and (seq called) (.includes recent called)))))

(defn- weighted-pool [last-speaker]
  (let [regular (mapcat
                 (fn [[id weight]]
                   (if (= id last-speaker)
                     []
                     (let [tendency  (get interjection-tendency id 0)
                           addressed (if (addressed-recently? id) addressed-bonus 0)
                           topic     (if (topic-triggered? id) topic-bonus-amount 0)]
                       (repeat (+ weight tendency addressed topic) id))))
                 base-weight)
        rares   (mapcat (fn [id] (if (= id last-speaker) [] (repeat rare-weight id))) rare-pool)
        pool    (concat regular rares)]
    (if (and (topic-triggered? :caretaker) (not= last-speaker :caretaker))
      (conj pool :caretaker)
      pool)))

(defn- pick-speaker []
  (let [last-speaker (:speaker (last @log))]
    (cond
      (empty? @log)                    :marcy   ;; she calls it to order
      (= last-speaker :jairis-egari)   :odile   ;; she always follows him
      :else (rand-nth (weighted-pool last-speaker)))))

;; ---------------------------------------------------------------------
;; Translating the shared room into one speaker's view of it
;; ---------------------------------------------------------------------

(defn- group-for [speaker entries]
  (let [tagged  (map (fn [e]
                        (if (= (:speaker e) speaker)
                          {:role "assistant" :text (:text e)}
                          {:role "user"
                           :text (str (display-name (:speaker e)) ": " (:text e))}))
                      entries)
        grouped (reduce
                 (fn [acc m]
                   (if (and (seq acc) (= (:role (peek acc)) (:role m)))
                     (conj (pop acc) (update (peek acc) :text str "\n" (:text m)))
                     (conj acc m)))
                 []
                 tagged)]
    (if (and (seq grouped) (= "assistant" (:role (first grouped))))
      (into [{:role "user" :text "(The meeting is already under way.)"}] grouped)
      grouped)))

;; ---------------------------------------------------------------------
;; One turn
;; ---------------------------------------------------------------------

(defn- take-turn! [on-update on-done]
  (let [speaker (pick-speaker)
        window  (take-last HISTORY-WINDOW @log)
        history (if (seq window) (group-for speaker (butlast window)) [])
        trigger (if (seq window)
                  (let [last-entry (last window)]
                    (str (display-name (:speaker last-entry)) ": " (:text last-entry)))
                  "(The meeting is called to order. Open it.)")
        roll-to-close? (and (>= (count @log) MIN-LINES)
                             (not @closing?)
                             (< (js/Math.random)
                                (/ (- (count @log) MIN-LINES) (- MAX-LINES MIN-LINES))))]
    (when roll-to-close?
      (reset! closing? true)
      (reset! closing-turns 3))
    (let [overrides (cond-> {:extra-context (str shared-context (speaking-dynamics-text))
                              :mc-name (fedoras.llm/current-mc-name)
                              :spine? include-spine?}
                       @closing?
                       (assoc :extra-context-uncached
                              (str "The meeting is wrapping up. Say your piece knowing this "
                                   "is one of the last few things anyone says before it ends — "
                                   "bring it toward a close, in your own voice, not a summary of "
                                   "everyone else's.")))]
      (fedoras.llm/ask-entity!
       speaker trigger history @api-key overrides
       (fn [text err]
         (let [line {:speaker speaker
                     :text (or text (str "(no response — " err ")"))}]
           (swap! log conj line)
           (on-update line)
           (when @closing? (swap! closing-turns dec))
           (cond
             @stop-requested?                       (on-done :stopped)
             (and @closing? (<= @closing-turns 0))   (on-done :concluded)
             (>= (count @log) MAX-LINES)             (on-done :maxed)
             :else (take-turn! on-update on-done))))))))

;; ---------------------------------------------------------------------
;; The room, on screen
;; ---------------------------------------------------------------------

(defn- render! []
  (fedoras.llm/stick-scroll!
   "stakeholders-log"
   (fn []
     (html!
      (str
       (if (nil? @api-key)
         (str "<div class='live-bar'>"
              "<input id='stakeholders-key' type='password' placeholder='your Anthropic API key' style='flex:1;min-width:0'>"
              "<button id='stakeholders-connect'>connect</button>"
              "</div>"
              (when @key-warning?
                "<p style='color:#c0392b'>Enter an API key first.</p>"))
         (str "<div class='live-bar'>"
              "<button id='stakeholders-start'>"
              (cond @running? "meeting in progress…"
                    (seq @log) "resume the meeting"
                    :else "begin the meeting")
              "</button>"
              "<button id='stakeholders-stop'>stop</button>"
              "<button id='stakeholders-save'>save transcript</button>"
              "</div>"))
       (when @not-ready?
         "<p style='color:#c0392b'>The cast hasn't loaded (window.FEDORAS_ENTITIES has no :marcy). Check that the entity files are reachable at the paths passed to state/load-entities, then reload the page.</p>")
       "<p>" (count @log) " of " MIN-LINES "–" MAX-LINES " lines"
       (when @running? " · thinking…")
       "</p>"
       "<div id='stakeholders-log' style='max-height:22rem;overflow-y:auto'>"
       (apply str
              (map (fn [e]
                     (if (:end-marker? e)
                       (str "<p style='text-align:center;opacity:0.6;letter-spacing:0.15em;margin-top:1em'>" (:text e) "</p>")
                       (str "<p><b>" (display-name (:speaker e)) "</b> — <span style='white-space:pre-wrap'>" (:text e) "</span></p>")))
                   @log))
       "</div>"))))
  (if (nil? @api-key)
    (on! "stakeholders-connect"
         (fn []
           (let [key (.-value (.getElementById js/document "stakeholders-key"))]
             (if (blank? key)
               (do (reset! key-warning? true) (render!))
               (do
                 (reset! key-warning? false)
                 (reset! api-key key)
                 (render!))))))
    (do
      (on! "stakeholders-start"
           (fn []
             (when-not @running?
               (reset! not-ready? false)
               (reset! running? true)
               (reset! stop-requested? false)
               (render!)
               (wait-for-entities!
                20
                (fn []
                  (take-turn!
                   (fn [_line] (render!))
                   (fn [_reason]
                     (swap! log conj (fedoras.llm/end-entry))
                     (reset! running? false)
                     (render!))))
                (fn []
                  (reset! running? false)
                  (reset! not-ready? true)
                  (render!))))))
      (on! "stakeholders-stop" (fn [] (reset! stop-requested? true)))
      (on! "stakeholders-save" (fn [] (fedoras.llm/save-transcript! @log :stakeholders-meeting))))))

(render!)