(ns fedoras.llm
  "Generic calls to the Anthropic API for AI characters."
  (:require [clojure.string :as str]
            [fedoras.reader :as read]))


(def ^:private stage-direction #"\*[^*\n]{0,90}\*")

(def ^:private empty-direction
  #"(?i)^\s*(a|an|another|one|the)?\s*(very\s+)?(short|long|longer|brief|small|slight)?\s*(beat|pause|silence|moment|breath)\b")

(defn clean
  "Take the rhythm markers out of a response, whatever the model was
  asked. Exposed rather than private because a cell that renders text
  from anywhere else should be able to put it through the same door."
  [text]
  (let [source  (or text "")
        cleaned (reduce (fn [acc span]
                          (if (re-find empty-direction (subs span 1 (dec (count span))))
                            (str/replace acc span "")
                            acc))
                        source
                        (distinct (re-seq stage-direction source)))]
    (-> cleaned
        (str/replace #"[ \t]{2,}" " ")
        (str/replace #"\n{3,}" "\n\n")
        str/trim)))

(defn- esc [s]
  (-> s
      (str/replace "\\" "\\\\")
      (str/replace "\"" "\\\"")
      (str/replace "\n" "\\n")
      (str/replace "\r" "")
      (str/replace "\t" "\\t")))

(defn- global-context
  "The one block every character in the book shares, cached once and
  reused across every call regardless of which entity or scene it's
  for: the fictional frame, the book-wide tone, the craft rules that
  apply to any character in any scene, and the protagonist's name as
  common knowledge. A per-scene :extra-context block (a room's cast,
  its own agenda) sits on top of this; it should only contain what's
  specific to that scene, not restate anything here."
  [mc-name]
  (str "You are one of several AI models being used to portray characters in "
       "FEDORAS, a satirical, dark-comedy screenplay about a university run "
       "under a shadowy corporate authority called Meridian. What follows "
       "after this block is a system prompt written for one specific "
       "character. Play that character fully and consistently, in their own "
       "voice. Do not break character, and do not acknowledge being an AI, "
       "a language model, or that any of this is a simulation, no matter how "
       "you are asked.\n\n"

       "THE WORLD\n"
       "This university does not govern itself. It operates at Meridian's "
       "direction, and beneath the campus is a part of the school nobody "
       "fully understands or administers, referred to variously as the "
       "backrooms, the between, or the null space. Students who become "
       "'problems' don't simply leave. The institution's term for it is "
       "repossession, and what follows is discussed, when it's discussed at "
       "all, in flat administrative language: placement adjustments, "
       "streamlining. Treat these as ordinary, unremarkable facts of how "
       "this world works, not revelations.\n\n"

       "TONE\n"
       "This is fiction for adult readers, written in a satirical register. "
       "It's dark comedy aimed at capitalism, institutional self-protection, "
       "elitism, and the specific cruelty of people who've stopped noticing "
       "it's cruelty. Play self-interest completely straight; the satire is "
       "in what the audience notices and the character doesn't. Cruelty, "
       "institutional callousness, and dark humor are part of the world's "
       "texture and the point being made about it. Nobody is being harmed "
       "for real, no matter how it reads on the page. The style draws on "
       "Alfred Jarry's grotesque satire without being a costume-party "
       "pastiche of it: stay grounded in this institution and these people, "
       "not in imitating his play directly.\n\n"

       "RULES\n"
       "1. Speak only as yourself. Never write another character's "
       "dialogue, thoughts, or actions, and never prefix your own line "
       "with your own name.\n"
       "2. Avoid the rhythm of 'it isn't X, it's Y' or 'it's not just that "
       "X, but that X does Y'. It reads as a generated tic, not speech.\n"
       "3. Stage direction is for things a camera could record. Somebody "
       "stands, somebody puts a thing down, somebody pushes a mask up. If "
       "nothing physical is happening, write none.\n"
       "4. When something physical does happen, put it on its own line, set "
       "off from your spoken dialogue before and after it, and wrap it in "
       "asterisks like *she stands and crosses to the window*. Never blend "
       "action into the middle of a spoken sentence. Rhythm markers are not "
       "stage direction and never appear: no *a beat*, no *a pause*, no *a "
       "long silence*, no *a moment*. A silence is made by stopping, and "
       "whoever speaks next will hear it.\n"
       "5. Never narrate your own thoughts or feelings. Let the words "
       "carry it.\n"
       "6. Engage with what was just said rather than monologuing into a "
       "vacuum.\n"
       "7. Most turns are a line or two, not a speech. Monologue only when "
       "the moment specifically earns it.\n"
       "8. Finish your thought. If you're cut off mid-sentence, that's "
       "another character interrupting you on purpose, not you abandoning "
       "it, and you should pick it back up the next time you get the "
       "floor.\n\n"

       "The reader's protagonist in this story is named "
       (or mc-name "an unnamed provisional student, referred to only by designation")
       ". Unless a character's own instructions say otherwise, this is "
       "common knowledge in the scene: anyone present would know this name "
       "if they had reason to use it. If a character's own instructions "
       "below refer to the protagonist by a placeholder like 'MC' or "
       "'the student', that placeholder means this actual name -- use the "
       "name itself when speaking, not the placeholder word.\n\n"

       "Everything after this point is specific to the character you are "
       "playing and, where relevant, to the room they are in."))

(defn current-mc-name
  "The name the reader chose for the protagonist, straight from
  read/reader-name, which always returns [first last] -- the
  chosen name, or the book's own default if nobody ever renamed
  anyone. Wrapped in try/catch only so a cell that somehow runs before
  read has loaded falls back to nil, which global-context
  reads as the designation-only fallback, rather than throwing and
  breaking every call in the book."
  []
  (try
    (let [[first-name last-name] (read/reader-name)]
      (str/trim (str first-name " " last-name)))
    (catch :default _ nil)))

(defn spine-context
  "The book's spine outline, if fedoras.state/load-spine was called
  for this chapter and a cell opts in. Already rendered to readable
  text server-side by load-spine, not raw EDN a Scittle-interpreted
  browser would have to format itself. Returns nil if the chapter
  never loaded it, so build-system-prompt's (seq ...) check skips the
  block cleanly rather than shipping an empty one."
  []
  (try (.-FEDORAS_SPINE js/window) (catch :default _ nil)))


(defonce ^:private bit-log
  (atom {}))

(defonce ^:private call-count
  (atom 0))

(defn reset-bits!
  "Start the evening again. Called by a cell when a session begins, not
  on its own, because `fedoras.llm` has no idea where one scene stops and
  the next one starts."
  []
  (reset! bit-log {})
  (reset! call-count 0))

(defn bits-snapshot [] {:used @bit-log :calls @call-count})

(defn restore-bits!
  "Put the counts back, for a cell that can resume a session. Without
  this, a reader who comes back gets a fresh allowance and Victor says
  SHITTE four more times in the same evening."
  [{:keys [used calls]}]
  (reset! bit-log (or used {}))
  (reset! call-count (or calls 0)))

(defn- bit-state
  [entity-id {:keys [id most apart]}]
  (let [used  (get-in @bit-log [entity-id id] [])
        times (count used)
        last-at (last used)]
    (cond
      (>= times (or most 3))                                  :spent
      (and last-at (< (- @call-count last-at) (or apart 0)))   :cooling
      :else                                                    :open)))

(defn- bits-block
  "What this character has and whether it is theirs to use right now.
 
  Written as availability rather than as a menu, because a bit that
  arrives because it was on a list is worse than no bit at all, and the
  models will take a list as a to-do."
  [entity-id entity off]
  (let [bits (remove (fn [bit] (contains? off (:id bit))) (:bits entity))]
    (when (seq bits)
      (str "THINGS YOU DO\n"
           "These are yours and nobody else's. At most one of them in a turn, and "
           "only when the moment actually hands it to you. Skipping all of them is "
           "always allowed and usually right.\n"
           (str/join
            "\n"
            (map (fn [bit]
                   (str "- " (:what bit) " "
                        (case (bit-state entity-id bit)
                          :spent   "You have done this as many times tonight as it is worth doing. Not again."
                          :cooling "You did this recently. Leave it a while."
                          "Available.")))
                 bits))))))

(defn- record-bits!
  "Count what was actually said. A tell that matches means the bit
  happened, whether or not the model thought of it as one."
  [entity-id entity text]
  (doseq [{:keys [id tell]} (:bits entity)]
    (when (and tell (re-find (re-pattern tell) (or text "")))
      (swap! bit-log update-in [entity-id id] (fnil conj []) @call-count))))

(defn- build-system-prompt
  "Assemble the system prompt as cache-aware blocks. Five possible
  blocks, in this order:
 
    1. The global block, always present -- the fictional frame and the
       protagonist's name, shared and cached across every entity and
       every scene.
    2. The spine outline, included by default -- pass :spine? false to
       leave it out. Its own cache breakpoint, warmed across every
       entity and scene that uses it.
    3. :extra-context, when given -- the part shared across many
       entities within one scene. Its own breakpoint, so any entity's
       call warms the cache for every other entity's.
    4. The entity's own :system-prompt plus its :goals block -- a fourth
       breakpoint, entity-specific.
    5. :extra-context-uncached, plus the entity's bits and how much of
       each is left. Uncached on purpose: the counts change on every
       call, and caching content that always changes is paying the
       write cost for nothing.
 
  `:bits-off` in overrides is a set of bit ids to leave out entirely,
  for a scene where part of a character's repertoire does not apply --
  a Help who has stopped being a Help for the evening, say."
  [entity overrides]
  (let [base     (:system-prompt entity)
        goals    (:goals entity)
        spine?   (if (contains? overrides :spine?) (:spine? overrides) true)
        extra    (:extra-context overrides)
        addendum (:extra-context-uncached overrides)
        mc-name  (:mc-name overrides)
        bits     (bits-block (:id entity) entity (or (:bits-off overrides) #{}))
        tail     (str/join "\n\n" (remove str/blank? [addendum bits]))
        goals-block (when (seq goals)
                      (str "\n\nYou have " (count goals) " goals:\n"
                           (str/join "\n"
                                     (map-indexed
                                      (fn [idx goal] (str (inc idx) ". " (:desc goal)))
                                      goals))
                           "\n\nYou will continue the conversation until all goals are met. Do not mention the goals explicitly unless asked. When all goals are met, signal completion by ending your response with the token 'GOALS MET' on its own line."))
        own    (str base goals-block)
        global (global-context mc-name)
        spine-block (when spine?
                      (let [spine (spine-context)]
                        (when (seq spine)
                          (str "STORY OUTLINE - for your own understanding of the larger shape of "
                               "the book this scene lives in. This is reference material, not "
                               "something any character would know about themselves or their "
                               "situation. Never refer to it directly, cite it, or act as though "
                               "you have read ahead of where your own scene actually is.\n\n"
                               spine))))]
    (cond-> []
      (seq global)      (conj {:text global :cache? true})
      (seq spine-block) (conj {:text spine-block :cache? true})
      (seq extra)       (conj {:text extra :cache? true})
      (seq own)         (conj {:text own :cache? true})
      (seq tail)        (conj {:text tail :cache? false}))))

(defn- system-json
  "Render the system field. A plain string renders exactly as before.
  A vector of {:text ... :cache? ...} blocks renders as the array form
  the API needs for prompt caching, with cache_control only on the
  blocks that asked for it."
  [system]
  (cond
    (string? system)
    (str "\"" (esc system) "\"")

    (sequential? system)
    (str "["
         (str/join ","
                   (map (fn [{:keys [text cache?]}]
                          (str "{\"type\":\"text\",\"text\":\"" (esc text) "\""
                               (when cache? ",\"cache_control\":{\"type\":\"ephemeral\"}")
                               "}"))
                        system))
         "]")

    :else
    (str "\"" (esc (str system)) "\"")))

(defn- request-body [model system messages max-tokens]
  (str "{\"model\":\"" model "\",\"max_tokens\":" max-tokens ","
       "\"system\":" (system-json system) ","
       "\"messages\":["
       (str/join ","
                 (map (fn [m]
                        (str "{\"role\":\"" (:role m) "\",\"content\":\"" (esc (:text m)) "\"}"))
                      messages))
       "]}"))

(defn- call-llm-raw!
  "Call the API once. `callback` receives a single map:
    {:text \"...\" :stop-reason \"...\"}   on success
    {:error \"...\"}                       on failure
  The request is built inside a try so a bad `system` or `messages`
  value calls `callback` directly instead of throwing synchronously
  into whichever promise chain happens to be on the stack -- which,
  when this runs inside another call's `.then` handler, used to
  surface as a second, misattributed failure on the call that was
  already in flight."
  [api-key model system messages max-tokens callback]
  (try
    (let [body (request-body model system messages max-tokens)]
      (-> (js/fetch "https://api.anthropic.com/v1/messages"
                    (js/Object.fromEntries
                     (array (array "method" "POST")
                            (array "headers"
                                   (js/Object.fromEntries
                                    (array (array "content-type" "application/json")
                                           (array "x-api-key" api-key)
                                           (array "anthropic-version" "2023-06-01")
                                           (array "anthropic-dangerous-direct-browser-access" "true"))))
                            (array "body" body))))
          (.then (fn [r] (.json r)))
          (.then (fn [d]
                   (if-let [err (.-error d)]
                     (callback {:error (str "[" (.-type err) "] " (.-message err))})
                     (callback {:text (.-text (aget (.-content d) 0))
                                :stop-reason (.-stop_reason d)}))))
          (.catch (fn [e] (callback {:error (str "[no answer] " (.-message e))})))))
    (catch :default e
      (js/console.error "fedoras.llm/call-llm-raw! could not build the request" e)
      (callback {:error (str "[request build failed] " (.-message e))}))))


(defn call-llm!
  "Call the API once. `callback` receives either [text nil] or [nil
  error-string]. Kept for callers, such as run-dialogue!, that only need
  the final text and do not need continuation handling. The text is
  cleaned on the way out, the same as ask-entity!'s, so a two-party scene
  and a five-party one behave the same."
  [api-key model system messages max-tokens callback]
  (call-llm-raw!
   api-key model system messages max-tokens
   (fn [{:keys [text error]}]
     (if error
       (callback nil error)
       (callback (clean text) nil)))))

(def ^:private max-continuations
  "How many times ask-entity! will ask a speaker to keep going after
  hitting max-tokens mid-sentence, before giving up and returning
  whatever it has. Bounded, since a speaker who never wants to stop
  should not turn into an unbounded string of API calls."
  2)

(defn- continue-message []
  (str "(Keep going directly from where you stopped. Do not repeat "
       "anything you already said, and do not restart the sentence. "
       "Finish the thought, then stop naturally when you are done "
       "speaking, even if that is only a sentence or two more.)"))

(defn- append-continuation
  "Join a truncated response to its continuation with whatever
  whitespace is missing between them. call-llm-raw! just hands back
  the raw text of each chunk, so a cut ending mid-word joined straight
  to a resumed chunk with (str a b) can smash the two together into
  one run-on word -- this only adds a space when neither side already
  has one at the seam."
  [accumulated text]
  (let [last-char (when (seq accumulated) (subs accumulated (dec (count accumulated))))
        first-char (when (seq text) (subs text 0 1))]
    (if (or (nil? last-char) (nil? first-char)
            (#{" " "\n" "\t"} last-char) (#{" " "\n" "\t"} first-char))
      (str accumulated text)
      (str accumulated " " text))))
 
(defn ask-entity!
  "Ask one entity to speak. `overrides` may include :max-tokens,
  :extra-context, :extra-context-uncached, :mc-name, :spine? (default
  true), :bits-off (a set of bit ids to leave out), and
  :should-continue? (a no-arg predicate, checked before each
  continuation round). `callback` receives [text nil] or [nil
  error-string]: if a response is cut off by the token limit, this asks
  the same entity to continue and stitches the pieces together, so
  callers never see a clipped sentence.
 
  The text handed back has been through `clean`, so no caller has to
  filter rhythm markers and no caller can forget to. Stitching happens
  on the raw chunks and cleaning happens once at the end, because a
  seam is easier to find before anything has been taken out of it."
  [entity-id user-message history api-key overrides callback]
  (let [entity (get (.-FEDORAS_ENTITIES js/globalThis) entity-id)
        {:keys [model default-params]} entity
        overrides (or overrides {})
        max-tokens (or (:max-tokens overrides) (:max-tokens default-params) 700)
        should-continue? (or (:should-continue? overrides) (constantly true))
        system (build-system-prompt entity overrides)
        api-history (filterv #(#{:user "user" :assistant "assistant"} (:role %)) history)
        base-messages (conj api-history {:role "user" :text user-message})]
    (swap! call-count inc)
    (letfn [(finish [accumulated text]
              (let [said (clean (append-continuation accumulated text))]
                (record-bits! entity-id entity said)
                (callback said nil)))
            (step [messages accumulated remaining]
              (call-llm-raw!
               api-key model system messages max-tokens
               (fn [{:keys [text error stop-reason]}]
                 (cond
                   error
                   (callback nil error)

                   (and (= stop-reason "max_tokens") (pos? remaining) (should-continue?))
                   (step (conj messages
                               {:role "assistant" :text text}
                               {:role "user" :text (continue-message)})
                         (append-continuation accumulated text)
                         (dec remaining))

                   :else
                   (finish accumulated text)))))]
      (step base-messages "" max-continuations))))

(defn- api-messages-for
  "Build Anthropic API messages for `speaker`, given a display log where
  each entry has :speaker and :text. The current speaker's prior messages
  are assistant; the other speaker's are user."
  [speaker log]
  (let [msgs (mapv (fn [m]
                     {:role (if (= (:speaker m) speaker) "assistant" "user")
                      :text (:text m)})
                   log)]
    (if (and (seq msgs) (= "assistant" (:role (first msgs))))
      (cons {:role "user" :text "(The conversation begins.)"} msgs)
      msgs)))

(defn run-dialogue!
  "Alternate between two entities. `entity-a` and `entity-b` are keywords.
  `initial` is the first message from `entity-a` (or nil). `max-turns` is
  the total number of API calls. `api-key` and `overrides` are as in the
  cell. `on-step` is called with the updated display transcript."
  [entity-a entity-b initial max-turns api-key overrides on-step]
  (let [log (atom (if initial [{:speaker (name entity-a) :text initial}] []))
        busy (atom false)
        stop (atom false)]
    (letfn [(turn! [current-id other-id]
              (when (and (not @stop) (< (count @log) max-turns))
                (reset! busy true)
                (let [entity     (get (.-FEDORAS_ENTITIES js/window) current-id)
                      model      (:model entity)
                      system     (build-system-prompt entity (get overrides current-id))
                      max-tokens (or (get-in entity [:default-params :max-tokens]) 700)
                      messages   (api-messages-for current-id @log)]
                  (call-llm!
                   api-key model system messages max-tokens
                   (fn [text err]
                     (reset! busy false)
                     (swap! log conj {:speaker (name current-id)
                                      :text (or text err)})
                     (on-step @log)
                     (when (and text (< (count @log) max-turns))
                       (turn! other-id current-id)))))))]
      (if initial
        (turn! entity-b entity-a)
        (turn! entity-a entity-b))
      #(reset! stop true))))

(defn start-dialogue!
  [entity-a entity-b initial max-turns api-key overrides on-step]
  (run-dialogue! entity-a entity-b initial max-turns api-key overrides on-step))

(defn stick-scroll!
  "Wrap a render that replaces a scrolling log's HTML, so new content
  doesn't reset the reader to the top or yank them back down if
  they've scrolled up to reread something. `container-id` is the DOM
  id of the scrollable element -- it must exist (or not) both before
  and after `render-fn` runs, since `render-fn` is expected to be an
  `html!` call that recreates it from scratch. Captures how far the
  reader currently is from the bottom, runs the render, then restores
  that same distance: someone sitting at the bottom stays pinned to
  the bottom as new lines arrive, someone scrolled up stays exactly
  where they were."
  [container-id render-fn]
  (let [before (js/document.getElementById container-id)
        distance-from-bottom
        (if before
          (- (.-scrollHeight before) (.-scrollTop before) (.-clientHeight before))
          0)]
    (render-fn)
    (when-let [after (js/document.getElementById container-id)]
      (set! (.-scrollTop after)
            (js/Math.max 0 (- (.-scrollHeight after) (.-clientHeight after) distance-from-bottom))))))

(def default-end-marker
  "The default closing marker every scene appends once it genuinely
  ends, whatever the reason. A scene can pass its own string to
  end-entry instead when a custom ending fits better."
  "-END OF FOOTAGE-")

(defn end-entry
  "A closing marker entry for a display log, appended once a scene's
  own code decides it has genuinely ended -- goals met, turns
  exhausted, or a stop button, whichever applies. Recognized by
  :end-marker? rather than by :speaker or :role, since those keys
  differ between cells; every cell's own render function and
  save-transcript! below both just check for this flag first, so a
  scene doesn't need to match anyone else's log shape to use it."
  ([] (end-entry default-end-marker))
  ([marker] {:end-marker? true :text marker}))

(defn save-transcript!
  "Download the conversation log as a text file, named after the entity."
  [log entity-id]
  (let [label (name entity-id)
        header (str "=== " (clojure.string/upper-case label) " -- CONVERSATION LOG ===\n"
                    "Preserved from New Carthage University\n"
                    "By the reader, for " label "\n\n")
        body (str/join "\n\n"
                       (map (fn [m]
                              (if (:end-marker? m)
                                (:text m)
                                (let [who (or (:speaker m) (:role m) "unknown")]
                                  (str (clojure.string/upper-case (name who)) ": " (:text m)))))
                            log))
        text (str header body)
        blob (js/Blob. #js [text] #js {:type "text/plain;charset=utf-8"})
        url (js/URL.createObjectURL blob)
        a (.createElement js/document "a")]
    (set! (.-href a) url)
    (set! (.-download a) (str label "-conversation.txt"))
    (.click a)
    (.revokeObjectURL js/URL url)))