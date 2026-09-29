(ns fedoras.cs-class.life.life-ui
  "Life, with more than one kind of alive in it.

  THE SUBSTRATE IS CONWAY. A cell is alive or it is not, and what decides
  that is how many of its neighbours are. Everything else here is a
  refinement of the counting, which is why the first species in the
  settings file is plain Conway: the rest can be read as one line changed.

  A SPECIES IS A COLOUR WITH AN OPINION. Its own birth and survival
  counts, how far it can see, whether it likes a crowd, which other
  species it counts as friends and which it counts against itself. A
  cell born into a mixed neighbourhood goes to whichever species has the
  most claim on it, and claim is neighbours times pull.

  THE GRID IS TWO FLAT ARRAYS. Species by cell, zero for empty, one array
  read and one written, swapped at the end of a generation. A vector of
  vectors would be more readable and would allocate a hundred thousand
  objects a second, and this runs at twelve generations a second on a
  hundred and twenty by seventy-two grid, which the other version does
  not."
  (:require [clojure.string :as str]))

(defonce book (atom nil))
(defonce grid (atom nil))
(defonce back (atom nil))
(defonce shape (atom nil))
(defonce brush (atom 1))
(defonce running (atom false))
(defonce generation (atom 0))
(defonce tally (atom nil))
(defonce last-tick (atom 0))
(defonce loop-id (atom 0))
(defonce palette (atom nil))
(defonce painting (atom nil))
(defonce chosen-pattern (atom nil))

(def default-book
  {:grid {:cols 120 :rows 72 :scale 6 :wrap true}
   :speed 12
   :seed 4471
   :scatter 0.18
   :palettes [{:id :ink :name "ink" :dead "--panel"
               :colours ["#e6e4df" "#9d8dff" "#d0a44c" "#6fcf9b" "#e2707c"]}]
   :species [{:id :plain :name "PLAIN" :born #{3} :survives #{2 3}}]
   :patterns [{:id :dot :name "one cell" :cells [[0 0]]}]})

;; ---------------------------------------------------------------------
;; A predictable die
;; ---------------------------------------------------------------------

(defonce seed-a (atom 1))

(defn- seed! [n] (reset! seed-a (bit-or 1 (bit-and n 0x7fffffff))))

(defn- rnd []
  (let [x @seed-a
        x (bit-and (bit-xor x (bit-shift-left x 13)) 0x7fffffff)
        x (bit-and (bit-xor x (unsigned-bit-shift-right x 17)) 0x7fffffff)
        x (bit-and (bit-xor x (bit-shift-left x 5)) 0x7fffffff)]
    (reset! seed-a x)
    (/ x 2147483647.0)))

;; ---------------------------------------------------------------------
;; The board
;; ---------------------------------------------------------------------

(defn cols [] (get-in @book [:grid :cols]))
(defn rows [] (get-in @book [:grid :rows]))
(defn wrap? [] (get-in @book [:grid :wrap]))

(defn species-count [] (count (:species @book)))

(defn spec
  "One species by its one-based index, which is what the grid stores.
  Zero means empty and never reaches here."
  [n]
  (nth (:species @book) (dec n) (first (:species @book))))

(defn build-grid!
  "Two arrays the size of the board. Kept apart from the settings so that
  changing the palette does not throw the board away."
  []
  (let [size (* (cols) (rows))]
    (reset! grid (js/Uint8Array. size))
    (reset! back (js/Uint8Array. size))
    (reset! generation 0)))

(defn at
  "The species at a cell, or 0. Off the edge is empty, or the other side
  of the board if the settings say the world wraps."
  [board c r]
  (let [w (cols) h (rows)]
    (if (wrap?)
      (aget board (+ (* (mod r h) w) (mod c w)))
      (if (or (neg? c) (neg? r) (>= c w) (>= r h))
        0
        (aget board (+ (* r w) c))))))

(defn put!
  [board c r n]
  (let [w (cols) h (rows)]
    (when (and (>= c 0) (>= r 0) (< c w) (< r h))
      (aset board (+ (* r w) c) n))))

;; ---------------------------------------------------------------------
;; One generation
;; ---------------------------------------------------------------------

(defn neighbours
  "How many of each species are within reach of a cell, as an array
  indexed by species number. Index 0 holds the total."
  [board c r reach]
  (let [counts (js/Uint8Array. (inc (species-count)))]
    (loop [dr (- reach)]
      (when (<= dr reach)
        (loop [dc (- reach)]
          (when (<= dc reach)
            (when-not (and (zero? dc) (zero? dr))
              (let [n (at board (+ c dc) (+ r dr))]
                (when (pos? n)
                  (aset counts n (inc (aget counts n)))
                  (aset counts 0 (inc (aget counts 0))))))
            (recur (inc dc))))
        (recur (inc dr))))
    counts))

(defn friendly
  "How many neighbours this species counts as being on its side: its own,
  plus anything it has allied with, minus anything it is hostile to.

  A species that is hostile to another does not merely ignore it. The
  neighbour counts against survival, which means a border between two
  hostile species is thinner than either of them would like."
  [counts n]
  (let [{:keys [allied hostile]} (spec n)
        own (aget counts n)
        friends (reduce (fn [acc id]
                          (+ acc (aget counts (inc (or (first
                                                        (keep-indexed
                                                         (fn [i s]
                                                           (when (= id (:id s)) i))
                                                         (:species @book)))
                                                       -1)))))
                        0
                        (or allied []))
        foes (reduce (fn [acc id]
                       (+ acc (aget counts (inc (or (first
                                                     (keep-indexed
                                                      (fn [i s]
                                                        (when (= id (:id s)) i))
                                                      (:species @book)))
                                                    -1)))))
                     0
                     (or hostile []))]
    (- (+ own friends) foes)))

(defn survives?
  "Whether a living cell of species n stays.

  `:crowd` is added to the count for every neighbour of its own kind past
  four, so a species with a positive crowd can hold ground that would
  otherwise overpopulate, and one with a negative crowd smothers itself.
  Which is the only rule here that is about anything."
  [counts n]
  (let [{:keys [survives crowd decay]} (spec n)
        own (aget counts n)
        base (friendly counts n)
        crowded (if (and crowd (> own 4))
                  (+ base (* crowd (- own 4)))
                  base)]
    (and (contains? (or survives #{2 3}) (max 0 (js/Math.round crowded)))
         (or (nil? decay) (>= (* 100 (rnd)) decay)))))

(defn claims
  "Which species, if any, is born into an empty cell.

  Every species asks whether the count of its own kind is one of its
  birth numbers, and the ones that say yes bid with their own count times
  their pull. The highest bid takes the cell. A tie goes to the lower
  number, which is to say the one written first in the settings file,
  which is as good a rule as any and at least it is stated."
  [counts]
  (loop [n 1 best 0 best-bid 0]
    (if (> n (species-count))
      best
      (let [{:keys [born pull]} (spec n)
            own (aget counts n)
            bid (* own (or pull 1))]
        (if (and (contains? (or born #{3}) own) (> bid best-bid))
          (recur (inc n) n bid)
          (recur (inc n) best best-bid))))))

(defn step!
  "One generation, from `grid` into `back`, and then swap them.

  The reach is per species, so a cell that can see two squares is counted
  differently from its neighbour that can see one. That costs a second
  pass over the neighbourhood for those species and is the reason
  `:spread 2` is worth having as a setting rather than as a default."
  []
  (let [from @grid
        to @back
        w (cols)
        h (rows)]
    (dotimes [r h]
      (dotimes [c w]
        (let [here (aget from (+ (* r w) c))]
          (if (pos? here)
            (let [reach (or (:spread (spec here)) 1)
                  counts (neighbours from c r reach)]
              (aset to (+ (* r w) c) (if (survives? counts here) here 0)))
            (let [counts (neighbours from c r 1)
                  wide (when (some (fn [s] (= 2 (:spread s))) (:species @book))
                         (neighbours from c r 2))
                  born (claims counts)
                  born (if (and (zero? born) wide) (claims wide) born)]
              (aset to (+ (* r w) c) born))))))
    (reset! grid to)
    (reset! back from)
    (swap! generation inc)))

;; ---------------------------------------------------------------------
;; Filling it
;; ---------------------------------------------------------------------

(defn clear!
  "Both boards, not just the one being read.

  `step!` swaps `grid` and `back` at the end of a generation, so clearing
  only the front one leaves the whole previous state sitting in the back
  one, and it comes straight back on the next tick. Which is a good deal
  more unsettling than a bug ought to be."
  []
  (.fill @grid 0)
  (.fill @back 0)
  (reset! running false)
  (reset! generation 0))

(defn scatter!
  "Random cells, from the seed, so the same seed gives the same start."
  []
  (seed! (:seed @book))
  (let [board @grid
        density (:scatter @book)
        kinds (species-count)]
    (dotimes [i (.-length board)]
      (aset board i (if (< (rnd) density)
                      (inc (min (dec kinds) (int (* (rnd) kinds))))
                      0)))
    (reset! generation 0)))

(defn stamp!
  "Put a pattern down, in the species the brush is set to."
  [pattern c r]
  (let [board @grid
        n @brush]
    (doseq [[dc dr] (:cells pattern)]
      (put! board (+ c dc) (+ r dr) n))))

(defn census
  "How many of each there are. Read once a paint rather than once a
  generation, because nobody is watching it that closely."
  []
  (let [board @grid
        counts (js/Uint32Array. (inc (species-count)))]
    (dotimes [i (.-length board)]
      (let [n (aget board i)]
        (when (pos? n) (aset counts n (inc (aget counts n))))))
    counts))

;; ---------------------------------------------------------------------
;; Colour
;; ---------------------------------------------------------------------

(defn- css-var [node nm fallback]
  (or (try
        (let [v (.getPropertyValue (js/getComputedStyle node) nm)]
          (when-not (str/blank? v) (str/trim v)))
        (catch :default _ nil))
      fallback))

(defn build-palette!
  "The chosen palette, resolved once. A colour beginning with two dashes
  is a custom property and is read off the page, so a palette can borrow
  the book's own ink."
  [node]
  (let [chosen (or (first (filter (fn [p] (= (:id p) @palette)) (:palettes @book)))
                   (first (:palettes @book)))
        resolve (fn [c]
                  (if (str/starts-with? (str c) "--")
                    (css-var node c "#888888")
                    c))]
    (reset! tally {:dead (resolve (:dead chosen))
                   :colours (mapv resolve (:colours chosen))})))

(defn colour-of
  [n]
  (let [colours (:colours @tally)]
    (nth colours (mod (dec n) (max 1 (count colours))) "#888888")))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn- scale [] (get-in @book [:grid :scale]))

(defn draw!
  [cv ctx]
  (let [board @grid
        w (cols)
        h (rows)
        px (scale)]
    (set! (.-fillStyle ctx) (:dead @tally))
    (.fillRect ctx 0 0 (* w px) (* h px))
    ;; one pass per species, so the fill colour is set five times rather
    ;; than once per living cell
    (dotimes [n (species-count)]
      (let [id (inc n)]
        (set! (.-fillStyle ctx) (colour-of id))
        (dotimes [r h]
          (dotimes [c w]
            (when (= id (aget board (+ (* r w) c)))
              (.fillRect ctx (* c px) (* r px) px px))))))))

(defn- species-html []
  (str/join
   ""
   (map-indexed
    (fn [i s]
      (let [n (inc i)]
        (str "<button class='life-pick" (when (= n @brush) " life-chosen") "'"
             " data-species='" n "'"
             " style='border-color:" (colour-of n) "'>"
             "<span class='life-swatch' style='background:" (colour-of n) "'></span>"
             (:name s)
             (when (:note s)
               (str "<span class='life-note-small'>" (:note s) "</span>"))
             "</button>")))
    (:species @book))))

(defn- tally-html []
  (let [counts (census)]
    (str/join
     " · "
     (keep-indexed
      (fn [i s]
        (let [n (inc i)
              c (aget counts n)]
          (when (pos? c)
            (str "<span style='color:" (colour-of n) "'>" (:name s) "</span> " c))))
      (:species @book)))))

(defn- select-html [items current]
  (str/join
   ""
   (map (fn [item]
          (str "<option value='" (name (:id item)) "'"
               (when (= (:id item) current) " selected") ">"
               (:name item) "</option>"))
        items)))

(defn paint! [node]
  (let [cv (fedoras.reader/q node ".life-canvas")
        want [(cols) (rows) (scale)]
        pattern (first (filter (fn [p] (= (:id p) @chosen-pattern))
                               (:patterns @book)))]
    (when cv
      (when (not= want @shape)
        (set! (.-width cv) (* (cols) (scale)))
        (set! (.-height cv) (* (rows) (scale)))
        (reset! shape want))
      (draw! cv (.getContext cv "2d")))
    (fedoras.ui.widget-ui/state!
     node (str "generation " @generation
               " · " (if @running "running" "stopped")))
    (fedoras.ui.widget-ui/html! node ".life-species" (species-html))
    (fedoras.reader/text!
     node ".life-note"
     (str "drawing " (:name (spec @brush))
          " · " (:name pattern)
          " · shift-drag to rub out"))
    (fedoras.ui.widget-ui/html! node ".life-tally" (tally-html))
    (fedoras.reader/enable! node ".life-run" true (if @running "Stop" "Run"))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- cell-at
  [cv event]
  (let [px (scale)
        w (.-clientWidth cv)
        sx (/ (* (cols) px) w)]
    [(int (/ (* (.-offsetX event) sx) px))
     (int (/ (* (.-offsetY event) sx) px))]))

(defn- touch!
  [node cv event erasing]
  (let [[c r] (cell-at cv event)
        pattern (first (filter (fn [p] (= (:id p) @chosen-pattern))
                               (:patterns @book)))]
    (if erasing
      (put! @grid c r 0)
      (if (and pattern (not= :dot (:id pattern)))
        (stamp! pattern c r)
        (put! @grid c r @brush)))
    (paint! node)))

(defn- wire-canvas! [node]
  (when-let [cv (fedoras.ui.widget-ui/q! node ".life-canvas")]
    (set! (.-onpointerdown cv)
          (fn [event]
            (.preventDefault event)
            (let [erasing (or (.-shiftKey event) (not (zero? (.-button event))))]
              (reset! painting erasing)
              (touch! node cv event erasing))))
    (set! (.-onpointermove cv)
          (fn [event]
            (when (some? @painting)
              ;; a drag places single cells whatever the pattern is, or
              ;; one stroke would stamp forty pulsars
              (let [[c r] (cell-at cv event)]
                (put! @grid c r (if @painting 0 @brush))
                (paint! node)))))
    (set! (.-onpointerup cv) (fn [_] (reset! painting nil)))
    (set! (.-onpointerleave cv) (fn [_] (reset! painting nil)))
    (set! (.-oncontextmenu cv) (fn [e] (.preventDefault e) false))))

(defn- wire-species! [node]
  (when-let [box (fedoras.ui.widget-ui/q! node ".life-species")]
    (set! (.-onclick box)
          (fn [event]
            (when-let [pressed (.closest (.-target event) ".life-pick")]
              (reset! brush (js/parseInt (.getAttribute pressed "data-species") 10))
              (paint! node))))))

(defn- wire-buttons! [node]
  (fedoras.reader/on-click! node ".life-run"
                            (fn [_] (swap! running not) (paint! node)))
  (fedoras.reader/on-click! node ".life-step"
                            (fn [_] (step!) (paint! node)))
  (fedoras.reader/on-click! node ".life-seed"
                            (fn [_] (scatter!) (paint! node)))
  (fedoras.reader/on-click! node ".life-clear"
                            (fn [_] (clear!) (paint! node)))
  (fedoras.ui.widget-ui/keys!
   node {" " :run "s" :step "n" :scatter "c" :clear}
   (fn [_ what]
     (case what
       :run (swap! running not)
       :step (step!)
       :scatter (scatter!)
       :clear (clear!))
     (paint! node))))

(defn- wire-controls! [node]
  (when-let [speed (fedoras.reader/q node ".life-speed")]
    (set! (.-value speed) (str (:speed @book)))
    (set! (.-oninput speed)
          (fn [_]
            (swap! book assoc :speed (js/parseInt (.-value speed) 10))
            (paint! node))))
  (when-let [size (fedoras.reader/q node ".life-scale")]
    (set! (.-value size) (str (scale)))
    (set! (.-oninput size)
          (fn [_]
            (swap! book assoc-in [:grid :scale] (js/parseInt (.-value size) 10))
            (paint! node))))
  (when-let [chooser (fedoras.reader/q node ".life-palette")]
    (set! (.-innerHTML chooser) (select-html (:palettes @book) @palette))
    (set! (.-onchange chooser)
          (fn [_]
            (reset! palette (keyword (.-value chooser)))
            (build-palette! node)
            (paint! node))))
  (when-let [chooser (fedoras.reader/q node ".life-pattern")]
    (set! (.-innerHTML chooser) (select-html (:patterns @book) @chosen-pattern))
    (set! (.-onchange chooser)
          (fn [_] (reset! chosen-pattern (keyword (.-value chooser)))))))

(defn- start-loop! [node cv ctx]
  (let [mine (swap! loop-id inc)]
    (letfn [(frame [now]
              (when (= mine @loop-id)
                (try
                  (when (and @running
                             (fedoras.ui.widget-ui/running? (.-id node))
                             (> (- now @last-tick) (/ 1000 (max 1 (:speed @book)))))
                    (reset! last-tick now)
                    (step!)
                    (draw! cv ctx)
                    (fedoras.ui.widget-ui/state!
                     node (str "generation " @generation " · running"))
                    (fedoras.ui.widget-ui/html! node ".life-tally" (tally-html)))
                  (catch :default e
                    (js/console.error "life: frame" e)))
                (js/requestAnimationFrame frame)))]
      (js/requestAnimationFrame frame))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (reset! book (merge default-book setting))
     (when (nil? @palette) (reset! palette (:id (first (:palettes @book)))))
     (when (nil? @chosen-pattern) (reset! chosen-pattern (:id (first (:patterns @book)))))
     (build-grid!)
     (build-palette! node)
     (reset! shape nil)
     (reset! running false)
     ;; blank, unless the settings file asks otherwise. a board that
     ;; fills itself the moment you run it is a board you watch, and the
     ;; point of this one is that you draw on it first
     (when (:scatter-on-run @book) (scatter!))
     (wire-controls! node)
     (paint! node))))

(defn setup!
  [node]
  (let [id (.-id node)]
    (fedoras.ui.widget-ui/wire! node "canvas" wire-canvas!)
    (fedoras.ui.widget-ui/wire! node "species" wire-species!)
    (fedoras.ui.widget-ui/wire! node "buttons" wire-buttons!)
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))
    (fedoras.ui.widget-ui/wire!
     node "loop"
     (fn [n]
       (when-let [cv (fedoras.reader/q n ".life-canvas")]
         (start-loop! n cv (.getContext cv "2d")))))))

(defn mount!
  [{:keys [id]}]
  (when (nil? @book) (reset! book default-book))
  (when (nil? @grid) (build-grid!))
  (fedoras.ui.widget-ui/mount!
   ::life (or id "life") setup! paint!))