(ns fedoras.cs-class.shoot.shoot-ui)

(def settings
  (atom {:log-rows 6}))

(defonce record (atom []))

;; ---------------------------------------------------------------------
;; The throws
;; ---------------------------------------------------------------------

(def beats-what {:rock :scissors :paper :rock :scissors :paper})

(defn- judge [mine theirs]
  (cond
    (= mine theirs)              :draw
    (= theirs (beats-what mine)) :win
    :else                        :lose))

(defn- what-beats [x]
  (first (filter (fn [k] (= x (beats-what k))) (keys beats-what))))

;; ---------------------------------------------------------------------
;; The reader
;;
;; Everything it knows is in here, one round at a time, and it never
;; looks back further than the last one. It is not modeling you. It is
;; watching for the single moment you become predictable — right after
;; you lose — and doing nothing at all the rest of the time.
;; ---------------------------------------------------------------------

(defn- about-to-read? []
  (let [last-round (last @record)]
    (boolean (and last-round (= :lose (:their-result last-round))))))

(defn- reader []
  (if (about-to-read?)
    (what-beats (what-beats (:theirs (last @record))))
    (rand-nth [:rock :paper :scissors])))

;; ---------------------------------------------------------------------
;; The tally
;; ---------------------------------------------------------------------

(defn- tally [k]
  (count (filter (fn [r] (= k (:their-result r))) @record)))

(defn- reads []
  (count (filter :read? @record)))

(defn- caught []
  (count (filter (fn [r] (and (:read? r) (= :lose (:their-result r)))) @record)))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn paint! [node]
  (fedoras.reader/text!
   node ".shoot-tally"
   (str "you " (tally :win) "   ·   reader " (tally :lose)
        "   ·   drawn " (tally :draw)))

  (fedoras.reader/text!
   node ".shoot-reads"
   (str "reads: " (caught) " of " (reads) " landed"))

  ;; the log needs its own coloured tag on the read rounds, which
  ;; text! alone cannot give it — built by hand, same as the legend
  ;; chips are in the other one
  (when-let [el (fedoras.reader/q node ".shoot-log")]
    (set! (.-innerHTML el) "")
    (doseq [r (reverse (take-last (:log-rows @settings) @record))]
      (let [line (.createElement js/document "div")]
        (set! (.-className line) "shoot-log-line")
        (set! (.-textContent line)
              (str (name (:theirs r)) " v " (name (:mine r))
                   " — you " (name (:their-result r))))
        (when (:read? r)
          (let [tag (.createElement js/document "span")]
            (set! (.-className tag) "shoot-read")
            (set! (.-textContent tag) " · read")
            (.appendChild line tag)))
        (.appendChild el line)))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- play! [t]
  (let [read-now? (about-to-read?)
        mine      (reader)]
    (swap! record conj {:mine mine :theirs t
                        :their-result (judge t mine)
                        :read? read-now?})))

(defn- wire-controls! [node]
  (fedoras.reader/on-click! node ".shoot-rock"
                            (fn [_] (play! :rock) (paint! node)))
  (fedoras.reader/on-click! node ".shoot-paper"
                            (fn [_] (play! :paper) (paint! node)))
  (fedoras.reader/on-click! node ".shoot-scissors"
                            (fn [_] (play! :scissors) (paint! node))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current] (merge current (select-keys setting [:log-rows]))))
     (paint! node))))

(defn setup! [node]
  ;; a clean board every time it mounts, same as a fresh drone
  (reset! record [])
  (fedoras.ui.widget-ui/wire! node "controls" wire-controls!)
  (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n (.-id node))))
  (paint! node))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount! ::shoot (or id "shoot") setup! paint!))