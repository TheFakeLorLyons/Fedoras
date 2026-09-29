;; ---------------------------------------------------------------------
;; YOUR HELP
;; Connect your key, then ask it anything.
;; You can change the model, the number of tokens, and add a style note.
;; The Help's full persona is hidden in the institution's files.
;; ---------------------------------------------------------------------

(def model "claude-sonnet-4-6")   ; change this if you want
(def max-tokens 700)              ; change this
(def style-note "")               ; e.g. "Answer in two or three sentences."
(def include-spine? true)         ; included by default; set false to opt this scene out

(def key-in-memory (atom nil))
(def log (atom []))
(def busy (atom false))

(declare draw)

(defn save! []
  (fedoras.llm/save-transcript! @log :campusgpt))

(defn send! []
  (let [field (js/document.getElementById "say")
        text (.-value field)]
    (when (and (seq (.trim text)) (not @busy))
      (swap! log conj {:role "user" :text text})
      (set! (.-value field) "")
      (reset! busy true)
      (fedoras.llm/ask-entity!
       :campusgpt
       text
       @log
       @key-in-memory
       {:max-tokens max-tokens
        :extra-context style-note
        :mc-name (fedoras.llm/current-mc-name)
        :spine? include-spine?}
       (fn [reply err]
         (reset! busy false)
         (swap! log conj {:role "assistant" :text (or reply err)})
         (draw))))))

(defn draw []
  (fedoras.llm/stick-scroll!
   "campusgpt-log"
   (fn []
     (html!
      (if (nil? @key-in-memory)
        (str "<p><strong>Connect your Help.</strong> This asks for an "
             "Anthropic API key. It is kept in a variable in this tab and "
             "sent from your browser straight to Anthropic. It is not "
             "written to storage, not put in the address bar, and not sent "
             "anywhere else. Close the tab and it is gone, and you will "
             "have to paste it again next time. You are paying for the "
             "calls.</p>"
             "<div class='live-bar'>"
             "<input id='apikey' type='password' class='todo-input' style='flex:1' placeholder='sk-ant-...' autocomplete='off'>"
             "<button id='connect'>Connect</button></div>")
        (str "<div id='campusgpt-log' style='max-height:22rem;overflow:auto'>"
             (apply str
               (map (fn [m]
                      (str "<p class='" (if (= "user" (:role m)) "roll-cue" "roll-line") "'>"
                           (if (= "user" (:role m)) "YOU" "HELP") "</p>"
                           "<p style='white-space:pre-wrap'>" (:text m) "</p>"))
                    @log))
             (if @busy "<p><em>thinking</em></p>" "")
             "</div>"
             "<div class='live-bar'>"
             "<input id='say' class='todo-input' style='flex:1' placeholder='ask it something' spellcheck='false'>"
             "<button id='send'>Ask</button>"
             "<button id='save'>Save to Disc</button>"
             "<button id='forget'>Disconnect</button></div>")))))

  (if (nil? @key-in-memory)
    (on! "connect" (fn []
                     (reset! key-in-memory (.trim (.-value (js/document.getElementById "apikey"))))
                     (set! (.-value (js/document.getElementById "apikey")) "")
                     (draw)))
    (do
      (on! "send" send!)
      (on! "save" save!)
      (on! "forget" (fn [] (reset! key-in-memory nil) (reset! log []) (draw)))
      (let [f (js/document.getElementById "say")]
        (when f
          (.focus f)
          (set! (.-onkeydown f) (fn [e] (when (= "Enter" (.-key e)) (send!)))))))))

(draw)