(ns fedoras.ui.widget-ui
  "What every widget's page side does the same way.

  Called fully qualified from the widgets, the way `fedoras.reader` is,
  because the frame emits this in an earlier Scittle tag and Scittle
  evaluates tags in document order. Nothing here depends on a require
  crossing that boundary."
  (:require [clojure.string :as str]))

(defonce mounted
  (atom #{}))

(defonce running
  (atom #{}))

(defonce shipped
  (atom {}))

(defn running? [id] (contains? @running id))

(defn reveal!
  "Show the widget's body. Called on the first successful run, and never
  undone: a reader who breaks the setting afterwards keeps the thing that
  was already working."
  [node id]
  (when-let [body (fedoras.reader/q node ".widget-body")]
    (.add (.-classList body) "widget-body-shown"))
  (swap! running conj id))

(defn state!
  "Write the plate in the header."
  [node text]
  (fedoras.reader/text! node ".widget-state" text))

(defn kept
  "What this widget left here last time, or nil."
  [k]
  (fedoras.reader/kept k))

(defn keep!
  "Leave it here. Whether it also travels is the reader's business and
  not this widget's."
  [k v]
  (fedoras.reader/keep! k v))

(defn did!
  "Award the reader a point of fortune, once, silently.
 
  A widget calls this when somebody has actually done the thing —
  finished the conversation, raked the garden, written on the card. It
  is a set, so it cannot be given twice, and nothing anywhere tells the
  reader it happened.
 
  Keep the keys in fedoras.endings.standing/deeds so there is one place
  to see how much fortune a reader can actually get. Six to ten is the
  ceiling: past that the modifier swamps the die."
  [k]
  (fedoras.reader/did! k))

(defn q!
  "An element inside a widget, or a complaint in the header.

  `fedoras.reader/q` returns nil for a selector that matches nothing,
  which then fails as a null dereference three functions away from the
  typo that caused it. This says which selector was wrong, in the one
  place a reader is already looking."
  [node selector]
  (or (fedoras.reader/q node selector)
      (do (state! node (str "missing: " selector)) nil)))

(defn wire!
  "One control, wired in isolation.

  Widgets used to set up every handler in one function, so the first
  thing that threw took every handler after it with it, silently. Each
  call here stands alone and says so if it fails."
  [node what f]
  (try
    (f node)
    (catch :default e
      (js/console.error (str "widget: " what " did not wire") e)
      (state! node (str what " did not wire")))))

(defn evaluate
  "Evaluate ClojureScript source, in the page, and hand back what it
  returned. This is Scittle evaluating the same way it evaluates the
  book's own script tags, so what a reader types is not a lesser kind of
  code than what shipped."
  [source]
  (if-let [core (some-> (.-scittle js/window) (.-core))]
    (.eval_string core source)
    (throw (js/Error. "the ClojureScript runtime is not on this page"))))

(defn- say!
  [node message ok?]
  (when-let [line (fedoras.reader/q node ".widget-settings-said")]
    (set! (.-textContent line) message)
    (.toggle (.-classList line) "widget-settings-broke" (not ok?))))

(defn reveal!
  "Show the widget's body. Called on the first successful run, and never
  undone: a reader who breaks the settings afterwards keeps the thing
  that was already working."
  [node id]
  (when-let [body (fedoras.reader/q node ".widget-body")]
    (.add (.-classList body) "widget-body-shown"))
  (swap! running conj id))

(defn run-settings!
  "Evaluate the panel and hand the result to `f`. A reader who breaks it
  gets the message the runtime gave, under the box, and the widget goes
  on running on the last settings that worked."
  [node id f]
  (when-let [box (fedoras.reader/q node ".widget-settings")]
    (try
      (let [value (evaluate (.-value box))]
        (if (map? value)
          (do (reveal! node id)
              (f value)
              (say! node (str "Running on " (count value) " settings.") true)
              (fedoras.reader/enable! node ".widget-settings-run" true "Run again"))
          (say! node "That did not come out as a map, so nothing was changed." false)))
      (catch :default e
        (say! node (str "It says: " (.-message e)) false)))))

(defn settings!
  "Wire the source panel. Nothing runs until the reader presses Run,
  which is the whole point of putting the file above the program."
  [node id f]
  (when-let [box (fedoras.reader/q node ".widget-settings")]
    (swap! shipped assoc id (.-value box))
    (fedoras.reader/on-click! node ".widget-settings-run"
                              (fn [_] (run-settings! node id f)))
    (fedoras.reader/on-click! node ".widget-settings-reset"
                              (fn [_]
                                (set! (.-value box) (get @shipped id))
                                (run-settings! node id f)
                                (say! node "Put back as it shipped, and run again." true)))))

(defn mount!
  "Register a widget. `setup!` runs once for a given element, `paint!`
  runs on every render once the widget is running.

  The node is checked because `getElementById` returns the first match in
  the document, so a stale cell earlier in a chapter carrying the same id
  silently hands a widget somebody else's element. Every selector inside
  it then misses, nothing throws, and the widget reads as a Run button
  that does nothing."
  [k id setup! paint!]
  (fedoras.reader/widget
   k id
   (fn [node]
     (if-not (.contains (.-classList node) "widget")
       (js/console.error "widget:" id "is not a widget element. Another"
                         "element on this page claims that id." node)
       (do
         (when-not (contains? @mounted [k id])
           (swap! mounted conj [k id])
           (when-not (fedoras.reader/q node ".widget-settings")
             (reveal! node id))
           (setup! node))
         (when (running? id) (paint! node)))))))

(defn download!
  "Hand the reader a file. Theirs, on their disc, out of the book."
  [text filename]
  (let [blob (js/Blob. #js [text] #js {:type "text/plain;charset=utf-8"})
        url (js/URL.createObjectURL blob)
        anchor (.createElement js/document "a")]
    (set! (.-href anchor) url)
    (set! (.-download anchor) filename)
    (.click anchor)
    (.revokeObjectURL js/URL url)))

(defn html!
  "Replace the contents of one element inside a widget. Silent if it is
  not there, because a renderer runs on every chapter and most chapters
  do not have most widgets."
  [node selector markup]
  (when-let [target (fedoras.reader/q node selector)]
    (set! (.-innerHTML target) markup)))

(defn grid!
  "Repaint a grid: the column count as a custom property, the cells as
  markup. Two lines that every grid widget in the book was writing out."
  [node selector cols markup]
  (when-let [target (fedoras.reader/q node selector)]
    (.setProperty (.-style target) "--cols" (str cols))
    (set! (.-innerHTML target) markup)))

(defn data
  "Integer data attributes off the nearest element matching `selector`,
  or nil if the event did not land on one.

    (data event \".floor-room\" \"r\" \"c\")  =>  [3 7]

  This is how a grid gets its clicks: one handler on the container that
  reads the coordinates back off the element, rather than a handler per
  cell that has to be rewired every time the grid is repainted."
  [event selector & names]
  (when-let [element (.closest (.-target event) selector)]
    (mapv (fn [n] (js/parseInt (.getAttribute element (str "data-" n)) 10))
          names)))

(defn delegate!
  "One click handler on a container, resolving to whichever child was
  hit. `f` is called with the event and the vector `data` returned."
  [node container-selector child-selector names f]
  (when-let [container (fedoras.reader/q node container-selector)]
    (set! (.-onclick container)
          (fn [event]
            (when-let [found (apply data event child-selector names)]
              (f event found))))))

(defn keys!
  "Wire a keymap onto the widget itself. The shell carries the tabindex,
  so the keys work when the widget has focus and nowhere else, and the
  page still scrolls when it does not."
  [node keymap f]
  (set! (.-onkeydown node)
        (fn [event]
          (when-let [what (get keymap (.-key event))]
            (.preventDefault event)
            (f event what)))))