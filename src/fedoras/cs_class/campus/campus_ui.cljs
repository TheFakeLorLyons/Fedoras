(ns fedoras.cs-class.campus.campus-ui
  "The map, walked.

  While walking around, it will tell you one thing: where you are. It
  does not say that a direction did nothing, because `go` does not say
  that, because a building does not say that.

  The log keeps track of every step, whether it moved anyone or
  not, and pressing the button prints the lot with the ones that did
  nothing marked, and a count at the bottom.

  Almost every reader has more of those than they expect. That is not a
  trick and it is not a difficulty — it is what walking round a building
  is like, and the only unusual thing about this one is that somebody
  kept the tally."
  (:require [clojure.string :as str]
            [fedoras.reader :as read]
            [fedoras.ui.widget-ui :as wui]))

(def settings (atom {:plan {} :names {} :start :whitlock}))

(defonce at    (atom nil))
(defonce trail (atom []))
(defonce shown (atom nil))

;; ---------------------------------------------------------------------
;; The map
;; ---------------------------------------------------------------------

(defn exits [room] (get-in @settings [:plan room :exits] {}))

(defn go
  "One step. A direction that does not exist puts you back where you
  were and says nothing about it."
  [room dir]
  (or (get (exits room) dir) room))

(defn name-of [room]
  (or (get-in @settings [:names room]) (str/upper-case (name room))))

(defn reachable [from]
  (loop [seen #{from} todo [from]]
    (if-let [r (first todo)]
      (let [next (remove seen (vals (exits r)))]
        (recur (into seen next) (into (rest todo) next)))
      seen)))

(defn unreachable [from]
  (sort (remove (reachable from) (keys (:plan @settings)))))

;; ---------------------------------------------------------------------
;; Walking
;; ---------------------------------------------------------------------

(defn step!
  [dir]
  (let [from @at
        to   (go from dir)]
    (reset! at to)
    (swap! trail conj {:from from :dir dir :to to :moved (not= from to)})
    (reset! shown nil)))

(defn back!
  "The only direction that is not on the map. It undoes a step that
  moved somebody, and it counts as a step, and it is in the log."
  []
  (when-let [last-move (last (filter :moved @trail))]
    (let [from @at]
      (reset! at (:from last-move))
      (swap! trail conj {:from from :dir :back :to (:from last-move) :moved true})
      (reset! shown nil))))

(defn start! []
  (reset! at (:start @settings))
  (reset! trail [])
  (reset! shown nil))

;; ---------------------------------------------------------------------
;; The two things it will tell you if you ask
;; ---------------------------------------------------------------------

(defn- log-text []
  (let [rows @trail
        no-ops (count (remove :moved rows))
        pad (fn [s n] (str s (apply str (repeat (max 0 (- n (count (str s)))) " "))))]
    (if (empty? rows)
      "You have not been anywhere."
      (str/join
       "\n"
       (concat
        (map-indexed
         (fn [i {:keys [from dir to moved]}]
           (str (pad (inc i) 4)
                (pad (name from) 12)
                (pad (name dir) 8)
                (pad (name to) 12)
                (when-not moved "—")))
         rows)
        [""
         (str (count rows) " steps · " (- (count rows) no-ops) " moved · "
              no-ops " did nothing")
         (if (zero? no-ops)
           "Nothing you tried was refused."
           "Nothing told you at the time. Nothing was going to.")])))))

(defn- lost-text []
  (let [ids (unreachable (:start @settings))]
    (if (empty? ids)
      "There is nothing on this map that you cannot get to."
      (str/join
       "\n"
       (concat
        ["ON THE PLAN AND NOT ON THE MAP" ""]
        (map (fn [id] (str "  " (name-of id))) ids)
        [""
         (str (count ids) " of " (count (:plan @settings))
              ". Nothing in the map says which. They are the ones")
         "nothing leads to."])))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn paint! [node]
  (let [room @at
        ways (exits room)]
    (read/text! node ".campus-where" (name-of room))
    (wui/state!
     node (str (count @trail) " steps"))
    (read/text!
     node ".campus-note"
     (if (empty? ways)
       "There is nothing out of this room."
       ""))
    ;; every direction is shown, and one that goes nowhere looks exactly
    ;; like one that does, because that is the point of the whole thing
    (doseq [b (read/q-all node ".campus-go")]
      (set! (.-className b) "campus-go"))
    (read/text! node ".campus-log" (or @shown ""))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(def keymap
  {"ArrowUp" :north "w" :north "ArrowDown" :south "s" :south
   "ArrowLeft" :west "a" :west "ArrowRight" :east "d" :east
   "i" :in "o" :out "u" :up "n" :down})

(defn- hold-focus!
  "Give the keys back to the widget after a click, so that a button just
  pressed does not keep them. The reader is already here, so nothing
  scrolls."
  [node]
  (.focus node #js {:preventScroll true}))

(defn- typing?
  "Whether a key belongs to something being typed into, such as the
  settings."
  [element]
  (boolean (or (contains? #{"INPUT" "TEXTAREA" "SELECT"} (.-tagName element))
               (.-isContentEditable element))))

(defn- wire-pad!
  "The pad, and the keys. A key the map knows is the map's wherever the
  focus is in the widget, except in the settings, where it is being
  typed, and except with ctrl, alt or the command key, which belong to
  the browser."
  [node]
  (wui/delegate!
   node ".campus-pad" ".campus-go" []
   (fn [event _]
     (when-let [button (.closest (.-target event) ".campus-go")]
       (let [direction (keyword (.getAttribute button "data-dir"))]
         (if (= direction :back) (back!) (step! direction))
         (paint! node)
         (hold-focus! node)))))
  (when-not (.hasAttribute node "tabindex")
    (.setAttribute node "tabindex" "0"))
  (set! (.-onkeydown node)
        (fn [event]
          (let [pressed (.-key event)
                direction (get keymap pressed)
                target (.-target event)]
            (when (and (or direction (= "Backspace" pressed))
                       (not (or (.-ctrlKey event) (.-metaKey event) (.-altKey event)))
                       (not (typing? target)))
              (.preventDefault event)
              (when-not (identical? target node) (hold-focus! node))
              (if direction (step! direction) (back!))
              (paint! node))))))

(defn- wire-asks!
  [node]
  (let [then-focus (fn [work] (fn [_] (work) (paint! node) (hold-focus! node)))]
    (read/on-click! node ".campus-read" (then-focus (fn [] (reset! shown (log-text)))))
    (read/on-click! node ".campus-lost" (then-focus (fn [] (reset! shown (lost-text)))))
    (read/on-click! node ".campus-start" (then-focus start!))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current (select-keys setting [:plan :names :start]))))
     (start!)
     (paint! node))))

(defn setup!
  "Once per element. Nothing here takes the focus: a page that jumps to a
  widget the reader has not reached yet has made a decision on the
  reader's behalf. The widget takes the keys when it is first clicked or
  tabbed to."
  [node opts]
  (let [id (.-id node)]
    (swap! settings assoc :start (keyword (or (:start opts) "whitlock")))
    (start!)
    (wui/wire! node "pad"      wire-pad!)
    (wui/wire! node "asks"     wire-asks!)
    (wui/wire! node "settings" (fn [element] (wire-settings! element id)))))

(defn mount! [{:keys [id] :as opts}]
  (wui/mount!
   ::campus (or id "campus")
   (fn [node] (setup! node opts))
   paint!))