;; ---------------------------------------------------------------------
;; ELI.CLJS
;; Connect your key, then speak to Eli. You may change the model or
;; the number of turns. The button labelled SAVE TO DISC will download
;; this conversation as a text file.
;; ---------------------------------------------------------------------

(def model "claude-sonnet-4-6")   ; you can change this
(def max-turns 100)               ; ~$1.00 maximum
(def style-note "")               ; optional, e.g. "Be gentle."
(def include-spine? true)         ; included by default; set false to opt this scene out

(def key-in-memory (atom nil))
(def log (atom []))
(def busy (atom false))
(def goals-met (atom false))       ; becomes true after GOALS MET
(def saved? (atom false))          ; becomes true after save

(declare draw)

(defn save! []
  (when-not @saved?
    (fedoras.llm/save-transcript! @log :eli)
    (reset! saved? true)
    (draw)))

(defn send! []
  (when (and (not @busy) (not @goals-met))
    (let [field (js/document.getElementById "say")
          text (.-value field)]
      (when (seq (.trim text))
        (swap! log conj {:role "user" :text text})
        (set! (.-value field) "")
        (reset! busy true)
        (fedoras.llm/ask-entity!
         :eli
         text
         @log
         @key-in-memory
         {:max-tokens 700
          :extra-context style-note
          :mc-name (fedoras.llm/current-mc-name)
          :spine? include-spine?}
         (fn [reply err]
           (reset! busy false)
           (let [answer (or reply err)]
             (swap! log conj {:role "assistant" :text answer})
             (when (and reply (clojure.string/includes? answer "GOALS MET"))
               (reset! goals-met true)
               (let [entity (get (.-FEDORAS_ENTITIES js/globalThis) :eli)]
                 (when-let [completion (:completion-message entity)]
                   (swap! log conj {:role "system" :text completion})))
               (swap! log conj (fedoras.llm/end-entry)))
             (draw))))))))

(defn draw []
  (fedoras.llm/stick-scroll!
   "eli-log"
   (fn []
     (html!
      (if (nil? @key-in-memory)
        (str "<p><strong>Connect your key.</strong> This conversation is with Eli Brooks, before Compleation. The key is kept in this tab only.</p>"
             "<div class='live-bar'>"
             "<input id='apikey' type='password' class='todo-input' style='flex:1' placeholder='sk-ant-...' autocomplete='off'>"
             "<button id='connect'>Connect</button></div>")
        (str "<div id='eli-log' style='max-height:24rem;overflow:auto'>"
             (apply str
                    (map (fn [m]
                           (if (:end-marker? m)
                             (str "<p style='text-align:center;opacity:0.6;letter-spacing:0.15em;margin-top:1em'>" (:text m) "</p>")
                             (str "<p class='" (if (= "user" (:role m)) "roll-cue" "roll-line") "'>"
                                  (if (= "user" (:role m)) "YOU" "ELI") "</p>"
                                  "<p style='white-space:pre-wrap'>" (:text m) "</p>")))
                         @log))
             (if @busy "<p><em>thinking</em></p>" "")
             "</div>"
             (cond
               @goals-met
               (str "<div class='live-bar'>"
                    "<p><em>There is no time for that. Grab the disc and go.</em></p>"
                    "<button id='save'>" (if @saved? "Saved" "Save to Disc") "</button>"
                    "</div>")
               :else
               (str "<div class='live-bar'>"
                    "<input id='say' class='todo-input' style='flex:1' placeholder='speak to him' spellcheck='false'>"
                    "<button id='send'>Send</button>"
                    "<button id='save'>Save to Disc</button>"
                    "</div>")))))))
  (if (nil? @key-in-memory)
    (on! "connect" (fn []
                     (let [key (.-value (js/document.getElementById "apikey"))]
                       (reset! key-in-memory (.trim key))
                       (set! (.-value (js/document.getElementById "apikey")) "")
                       (draw))))
    (do
      (when-not @goals-met
        (on! "send" send!)
        (let [f (js/document.getElementById "say")]
          (when f
            (.focus f)
            (set! (.-onkeydown f)
                  (fn [e] (when (= "Enter" (.-key e)) (send!)))))))
      (on! "save" save!))))

(draw)