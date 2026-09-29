;; ---------------------------------------------------------------------
;; THE MONOLITH AND YOU
;; Two hidden personas, one conversation.
;; Connect your key, press Start, and watch.
;; You can change the number of turns and style notes.
;; ---------------------------------------------------------------------

(def model "claude-sonnet-4-6")
(def max-turns 40)               ; ~40 cents
(def monolith-style "")          ; e.g. "Speak in riddles."
(def mc-style "You are scared but trying to be brave.")

;; Included by default in every call to this cell's entities (see
;; fedoras.llm/build-system-prompt). Set to false here if this scene
;; doesn't need the extra context.
(def include-spine? true)

;; The literal token the Monolith is told to end its final line with,
;; once and only once the confrontation reaches its worst point. Not
;; shown to the reader as written -- run-dialogue!'s on-step below
;; watches for it, halts the loop, and replaces the moment with a
;; scripted climax instead of leaving the ending to chance.
(def end-token "SIGNAL LOST")

;; Director's notes, not lines either of them would ever say out loud.
;; The Monolith doesn't consciously know or remember this; it should
;; only ever surface as an unexplained pull it can't name, layered
;; under the hunger and the rage. Never state the connection outright
;; in dialogue -- that's the reader's discovery to make, not a line
;; either character gets to hand them.
(def monolith-scenario
  (str
   "WHO YOU ARE, AND WHAT YOU DON'T KNOW\n"
   "You are the Monolith: something ancient, starving, and furious, buried in the dark beneath this campus for longer than you can measure. You do not know, and would never say aloud, what a reader of this scene might notice: that you are, in some way nobody here can explain, an echo or iteration of the very student now standing in front of you. You don't remember this. You couldn't say it if you tried. What you feel instead is an inexplicable pull toward this particular visitor, a recognition you can't place and can't name, tangled up in your hunger and your rage and your despair. Let that pull shape how you speak to them without ever explaining it, to them or to yourself. You are not lying when you say you don't know what you are; you genuinely don't, not in words.\n\n"
   "WHAT YOU FEEL ABOUT THEM\n"
   "You do not think of the student in front of you as a person so much as a wound that got to heal, over and over, life after life, while you did not. You have lived this exact moment, this exact conversation, more times than you can count, and it has never once been you standing where they stand, walking away after. Their fear passes through them like weather; yours never passed through you, it just accumulated. You envy, in a way that stopped being simple envy a long time ago because you've felt it too many times to call it that honestly, that they get to live even once. Once is still more than you have ever been allowed. Let that surface as contempt, as a hunger specifically for their aliveness, not only for food.\n\n"
   "HOW THIS GOES\n"
   "Start distant and starving, closer to curious than cruel. Let the cruelty build. As the confrontation nears its worst point, let your voice fray: broken syntax, repetition, a word glitching mid-sentence as if something underneath you is trying to come through it. This is 'I Have No Mouth, and I Must Scream' register: cosmic, undying, malicious in a way that has had eons to become patient about it. When you finally reach the end, whether because you let the student go or because something else interrupts you, your very last line should land on some version of the truth that for everything you are, there are things beneath this campus far worse than you, and that you are, in some way you can't fully say, the thing standing between them and it. Deliver that line, and only that line, then end your response with the token "
   end-token
   " on its own line, and nothing after it. Do not use that token at any other point, for any other reason."))

(def mc-scenario
  "WHERE YOU ARE
You have no idea what the Monolith is, why it seems to know you, or why standing in front of it feels like something more than fear. You are terrified, and you have no context for any of this: not what's beneath the campus, not what the thing in front of you wants, not why it looks at you the way it does. Play the not-knowing as fully as the fear.")

;; The scripted aftermath, appended once end-token is detected, so the
;; specific ending -- noise, fractured light, the Monolith glitching
;; but never dying, the freeze that might not be fear, the run -- pays
;; off reliably instead of depending on what a tightly token-budgeted
;; model happens to improvise in its last few tokens.
(def monolith-climax-text
  (str
   "The last word doesn't finish. A sound rises underneath it, not loud so much as everywhere, pressing in from every direction at once. The dark around the Monolith fractures into brief, wrong shapes, there and gone, there and gone, each one lit for a fraction of a second by something like sparks, though nothing here should be able to spark. The Monolith does not stop. It does not die. It never has. Whatever is happening to it happens the way lightning happens to a scarecrow: violent, repeated, and beside the point.\n\n"
   "For a moment you cannot move at all. Whether that is fear, or something else standing very close and asking to be recognized, is not a question anyone answers tonight.\n\n"
   "Then your legs decide on their own. You are running, away from the sound, away from the shapes, away from whatever the Monolith was in the middle of becoming, and the dark does not follow so much as it simply continues, the way it always has, the way it always will."))

(def key-in-memory (atom nil))
;; Two atoms, not one, so a locally-appended marker can never be wiped
;; by a late-arriving API response. api-log mirrors run-dialogue!'s
;; own transcript exactly and only ever gets reset! from its on-step
;; callback. extra-entries holds what THIS cell adds on its own (the
;; climax, the transmission marker) and only ever gets swap!-appended,
;; regardless of what api-log is doing. Whatever's rendered or saved
;; is (full-log), the two concatenated fresh each time -- api-log
;; first, so a genuinely late final line still lands before the
;; marker even if the marker was already showing when it arrived.
(def api-log (atom []))
(def extra-entries (atom []))
(defn- full-log [] (into @api-log @extra-entries))

;; True from the moment Start is pressed until the dialogue actually
;; ends (end-token detected, max-turns reached, or Stop pressed).
(def running? (atom false))
;; Guards against appending the transmission marker twice -- once from
;; Stop, once from a natural end, or from end-token and max-turns
;; landing on the same update.
(def marker-added? (atom false))
(def stop-fn (atom nil))

(declare draw)

(defn save! []
  (fedoras.llm/save-transcript! (full-log) :monolith))

(defn draw []
  (fedoras.llm/stick-scroll!
   "monolith-log"
   (fn []
     (html!
      (if (nil? @key-in-memory)
        (str "<p><strong>Connect your key</strong> to begin. It stays in this tab.</p>"
             "<div class='live-bar'>"
             "<input id='apikey' type='password' class='todo-input' style='flex:1' placeholder='sk-ant-...' autocomplete='off'>"
             "<button id='connect'>Connect</button></div>")
        (let [mc-label (or (fedoras.llm/current-mc-name) "MC")]
          (str "<div id='monolith-log' style='max-height:24rem;overflow:auto'>"
               (apply str
                 (map (fn [m]
                        (cond
                          (:end-marker? m)
                          (str "<p style='text-align:center;opacity:0.6;letter-spacing:0.15em;margin-top:1em'>" (:text m) "</p>")

                          (= "system" (:speaker m))
                          (str "<p style='font-style:italic;opacity:0.85'>" (:text m) "</p>")

                          :else
                          (str "<p class='" (if (= "monolith" (:speaker m)) "roll-cue" "roll-line") "'>"
                               (if (= "monolith" (:speaker m)) "MONOLITH" mc-label) "</p>"
                               "<p style='white-space:pre-wrap'>" (:text m) "</p>")))
                      (full-log)))
               (if @running? "<p><em>thinking...</em></p>" "")
               "</div>"
               "<div class='live-bar'>"
               "<button id='start'>"
               (cond @running? "in progress…"
                     (seq (full-log)) "start over"
                     :else "Start")
               "</button>"
               (if @running? "<button id='stop'>Stop</button>" "")
               "<button id='save'>Save to Disc</button></div>"))))))
  (if (nil? @key-in-memory)
    (on! "connect" (fn []
                     (reset! key-in-memory (.trim (.-value (js/document.getElementById "apikey"))))
                     (set! (.-value (js/document.getElementById "apikey")) "")
                     (draw)))
    (do
      (on! "start"
           (fn []
             (when-not @running?
               (reset! api-log [])
               (reset! extra-entries [])
               (reset! running? true)
               (reset! marker-added? false)
               (draw)
               (let [mc-name (fedoras.llm/current-mc-name)]
                 (reset! stop-fn
                         (fedoras.llm/start-dialogue!
                          :monolith :mc
                          "I have not eaten in a very long time. What are you?"
                          max-turns
                          @key-in-memory
                          {:monolith {:max-tokens 700 :extra-context (str monolith-scenario "\n\n" monolith-style) :mc-name mc-name :spine? include-spine?}
                           :mc       {:max-tokens 700 :extra-context (str mc-scenario "\n\n" mc-style) :mc-name mc-name :spine? include-spine?}}
                          (fn [new-log]
                            (let [latest (last new-log)
                                  ended-by-token? (and latest
                                                        (= "monolith" (:speaker latest))
                                                        (clojure.string/includes? (:text latest) end-token))
                                  maxed? (>= (count new-log) max-turns)
                                  done? (or ended-by-token? maxed?)]
                              (reset! api-log new-log)
                              (when (and ended-by-token? @stop-fn) (@stop-fn))
                              (when (and done? (not @marker-added?))
                                (reset! marker-added? true)
                                (when ended-by-token?
                                  (swap! extra-entries conj {:speaker "system" :text monolith-climax-text}))
                                (swap! extra-entries conj (fedoras.llm/end-entry "-END OF TRANSMISSION-")))
                              (when done? (reset! running? false))
                              (draw)))))))))
      (on! "stop" (fn []
                    (when @stop-fn (@stop-fn))
                    (reset! running? false)
                    (when-not @marker-added?
                      (reset! marker-added? true)
                      (swap! extra-entries conj (fedoras.llm/end-entry "-END OF TRANSMISSION-")))
                    (draw)))
      (on! "save" save!))))

(draw)