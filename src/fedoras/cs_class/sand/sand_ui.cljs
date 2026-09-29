(ns fedoras.cs-class.sand.sand-ui
  "Grains, in the page.

  FOUR ARRAYS, one byte per square. What is there, how long it has left,
  what it was before it caught, and whether it has already moved this
  pass.

  THAT LAST ONE IS NOT AN OPTIMISATION. The pass runs bottom-up, so a
  falling grain moves into a square the loop has already been past and
  is left alone. A rising one moves into a square the loop has not
  reached yet, and without a mark would rise again, and again, and cross
  the whole board in a single frame. Steam is the reason that array
  exists.

  ELEVEN SUBSTANCES AND SIX VERBS. Fall, flow, rise, sit still, burn,
  grow. Everything on the palette is one of those with a different set
  of numbers against it, and the numbers are in the table below rather
  than in the code, so a twelfth substance is a row and not a function.

  DENSITY DECIDES THE LAYERS. Sand is heavier than water, water is
  heavier than oil, and everything is heavier than air. Nothing in here
  says sand sinks or oil floats. Pour all three into the same corner and
  the layers were not designed.

  AND WHAT BURNS IS A NUMBER, not a category. Oil catches easily and
  burns hot. Plant catches almost instantly and is gone. Wood will
  hardly catch at all and then smoulders for four hundred passes.
  Fungus catches badly and smokes, which is what a wet thing does."
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; The board
;; ---------------------------------------------------------------------

(def W 120)
(def H 72)
(def SIZE (* W H))

(def EMPTY 0) (def SAND 1)   (def WATER 2) (def OIL 3)
(def FIRE 4)  (def STEAM 5)  (def STONE 6) (def PLANT 7)
(def WOOD 8)  (def FUNGUS 9) (def ASH 10)  (def GLASS 11)

(defonce board (atom nil))
(defonce life  (atom nil))   ;; countdown, or generation, or age
(defonce was   (atom nil))   ;; what a flame was before it caught
(defonce moved (atom nil))
(defonce words (atom nil))
(defonce image (atom nil))
(defonce inks  (atom nil))

(defonce tool    (atom SAND))
(defonce holding (atom nil))
(defonce at-xy   (atom [0 0]))
(defonce wet-on  (atom false))
(defonce frames  (atom 0))

(def settings
  (atom {:brush 3 :smoke 0.35 :ash 0.5 :condense 0.25 :steam-life 160
         :growth 0.10 :reach 24 :spread 0.06 :vigour 90 :vitrify 0.02}))

;; ---------------------------------------------------------------------
;; What each one is
;;
;;   :dens    what it will sink through. 99 never moves.
;;   :move    :powder :liquid :gas :still
;;   :drag    how often a liquid declines to move. oil creeps.
;;   :burns   chance per pass of catching from a flame beside it
;;   :fuel    how long the flame lasts once it has
;;   :leaves  what is on the floor afterwards
;; ---------------------------------------------------------------------

(def substances
  [{:m SAND :label "sand" :var "--sand-sand" :dens 3 :move :powder
    :note "falls, and finds its own angle"
    :glyph "<circle cx='8' cy='9' r='1.6'/><circle cx='14' cy='7' r='1.6'/><circle cx='11' cy='14' r='1.6'/><circle cx='16' cy='15' r='1.6'/><circle cx='6' cy='16' r='1.6'/>"}

   {:m WATER :label "water" :var "--sand-water" :dens 2 :move :liquid
    :note "flows. sand sinks through it. plants want it."
    :glyph "<path d='M3 9c3-3 5 3 8 0s5 3 8 0M3 15c3-3 5 3 8 0s5 3 8 0'/>"}

   {:m OIL :label "oil" :var "--sand-oil" :dens 1 :move :liquid :drag 0.40
    :burns 0.20 :fuel 70
    :note "floats on water. catches easily."
    :glyph "<path d='M12 4c3 5 5 7 5 10a5 5 0 0 1-10 0c0-3 2-5 5-10z'/>"}

   {:m FIRE :label "fire" :var "--sand-fire" :dens 0 :move :fire
    :note "eats what is beside it. water puts it out."
    :glyph "<path d='M12 3c1 4-3 5-3 9a3 3 0 0 0 6 0c0-2-1-3-1-4 2 1 3 3 3 5a5 5 0 0 1-10 0c0-5 5-6 5-10z'/>"}

   {:m STEAM :label "steam" :var "--sand-steam" :dens 0 :move :gas
    :note "rises, and comes back as water"
    :glyph "<path d='M7 18c-2-2 0-4 2-5s2-3 0-4M13 19c-2-2 0-4 2-5s2-4 0-5M17 17c-1-1 0-2 1-3'/>"}

   {:m PLANT :label "plant" :var "--sand-plant" :dens 99 :move :grow
    :burns 0.60 :fuel 25 :leaves ASH
    :note "grows into water. goes up like paper."
    :glyph "<path d='M12 20V7M12 13c-4 0-6-2-6-5 3 0 6 1 6 5zM12 11c4 0 6-2 6-5-3 0-6 1-6 5z'/>"}

   {:m WOOD :label "wood" :var "--sand-wood" :dens 99 :move :still
    :burns 0.02 :fuel 420 :leaves ASH
    :note "will hardly catch. then it goes for hours."
    :glyph "<path d='M4 8h16v9H4zM4 12.5h16M9 8v4.5M14 12.5V17'/>"}

   {:m FUNGUS :label "fungus" :var "--sand-fungus" :dens 99 :move :fungus
    :burns 0.06 :fuel 35 :leaves ASH :eats #{WOOD PLANT}
    :note "takes wood and plant. white, then purple, then done."
    :glyph "<path d='M5 12a7 7 0 0 1 14 0zM10 12v6a2 2 0 0 0 4 0v-6'/>"}

   {:m ASH :label "ash" :var "--sand-ash" :dens 3 :move :powder
    :note "what is left. will not catch again."
    :glyph "<circle cx='7' cy='11' r='1.1'/><circle cx='13' cy='8' r='1.1'/><circle cx='16' cy='14' r='1.1'/><circle cx='10' cy='16' r='1.1'/>"}

   {:m STONE :label "stone" :var "--sand-stone" :dens 99 :move :still
    :note "does not move, and nothing moves it"
    :glyph "<path d='M4 8h16v10H4zM4 13h16M11 8v5M15 13v5'/>"}

   {:m GLASS :label "glass" :var "--sand-glass" :dens 99 :move :still
    :note "there is one way to get this and it is not the palette"
    :glyph "<path d='M5 5h14v14H5zM7 17l10-10M11 17l6-6'/>"}])

(def by-mat (into {} (map (fn [s] [(:m s) s])) substances))
(def dens   (into {EMPTY 0} (map (fn [s] [(:m s) (:dens s)])) substances))

;; ---------------------------------------------------------------------
;; Squares
;; ---------------------------------------------------------------------

(defn- inside? [x y] (and (>= x 0) (< x W) (>= y 0) (< y H)))
(defn- idx [x y] (+ x (* y W)))
(defn cell [b x y] (if (inside? x y) (aget b (idx x y)) STONE))

(defn- around [x y] [[(dec x) y] [(inc x) y] [x (dec y)] [x (inc y)]])
(defn- one-of [coll] (nth coll (int (* (js/Math.random) (count coll)))))

(defn- beside? [b x y m]
  (or (== m (cell b (dec x) y)) (== m (cell b (inc x) y))
      (== m (cell b x (dec y))) (== m (cell b x (inc y)))))

(defn- lighter?
  "Nothing swaps with a thing as heavy as itself, or two grains would
  trade places for ever and the heap would boil."
  [b x y d]
  (let [m (cell b x y)]
    (and (not (== m STONE)) (< (get dens m 0) d))))

(defn- move! [b l mv x y nx ny d]
  (when (and (inside? nx ny)
             (zero? (aget mv (idx nx ny)))
             (lighter? b nx ny d))
    (let [i (idx x y) j (idx nx ny) m (aget b i) a (aget l i)]
      (aset b i (aget b j)) (aset b j m)
      (aset l i (aget l j)) (aset l j a)
      (aset mv i 1) (aset mv j 1)
      true)))

;; ---------------------------------------------------------------------
;; The verbs
;; ---------------------------------------------------------------------

(defn- powder! [b l mv x y d]
  (let [s (if (< (js/Math.random) 0.5) -1 1)]
    (or (move! b l mv x y x (inc y) d)
        (move! b l mv x y (+ x s) (inc y) d)
        (move! b l mv x y (- x s) (inc y) d))))

(defn- liquid! [b l mv x y d]
  (let [s (if (< (js/Math.random) 0.5) -1 1)]
    (or (powder! b l mv x y d)
        (move! b l mv x y (+ x s) y d)
        (move! b l mv x y (- x s) y d))))

(defn- gas! [b l mv x y]
  (let [s (if (< (js/Math.random) 0.5) -1 1)]
    (or (move! b l mv x y x (dec y) 1)
        (move! b l mv x y (+ x s) (dec y) 1)
        (move! b l mv x y (+ x s) y 1))))

(defn- catch!
  "Set a square alight, remembering what it was, so that what is on the
  floor afterwards is a fact about the thing that burned."
  [b l wz nx ny s]
  (let [j (idx nx ny)]
    (aset wz j (aget b j))
    (aset b j FIRE)
    (aset l j (:fuel s 60))))

(defn- fire!
  "The only rule in here that is about anybody else."
  [b l wz mv x y]
  (let [i (idx x y)
        {:keys [smoke ash steam-life vitrify]} @settings]
    (if (beside? b x y WATER)
      (do (aset b i STEAM) (aset l i steam-life))
      (do
        ;; what is next to it, and how willing each of them is
        (doseq [[nx ny] (around x y)]
          (when (inside? nx ny)
            (let [m (cell b nx ny)
                  s (by-mat m)]
              (cond
                (and s (pos? (:burns s 0)) (< (js/Math.random) (:burns s)))
                (catch! b l wz nx ny s)

                ;; the one way to get glass, and it is not the palette
                (and (== m SAND) (< (js/Math.random) vitrify))
                (aset b (idx nx ny) GLASS)))))
        (let [left (dec (aget l i))]
          (if (pos? left)
            (do (aset l i left)
                (when (< (js/Math.random) 0.4) (gas! b l mv x y)))
            (let [leaves (:leaves (by-mat (aget wz i)))]
              (aset wz i EMPTY)
              (cond
                (and leaves (< (js/Math.random) ash))
                (do (aset b i leaves) (aset l i 0))
                (< (js/Math.random) smoke)
                (do (aset b i STEAM) (aset l i steam-life))
                :else (do (aset b i EMPTY) (aset l i 0))))))))))

(defn- steam! [b l mv x y]
  (let [i (idx x y) left (dec (aget l i))]
    (if (pos? left)
      (do (aset l i left) (gas! b l mv x y))
      (if (< (js/Math.random) (:condense @settings))
        (aset b i WATER)
        (do (aset b i EMPTY) (aset l i 0))))))

(defn- grow!
  "A plant is rooted, so it does not fall. It reaches into an empty
  square beside a drop of water, and drinks it.

  `life` is the generation. Every new cell is one further from wherever
  somebody put the first one, and growth stops at :reach, which is why a
  plant does not eventually become the whole board."
  [b l x y]
  (let [i (idx x y)
        {:keys [growth reach]} @settings]
    (when (and (< (aget l i) reach) (< (js/Math.random) growth))
      (let [[nx ny] (one-of (around x y))]
        (when (and (inside? nx ny)
                   (== EMPTY (cell b nx ny))
                   (beside? b nx ny WATER))
          ;; drink the drop it grew towards
          (let [[wx wy] (one-of (filterv (fn [[ax ay]] (== WATER (cell b ax ay)))
                                         (around nx ny)))]
            (aset b (idx wx wy) EMPTY))
          (aset b (idx nx ny) PLANT)
          (aset l (idx nx ny) (inc (aget l i))))))))

(defn- fungus!
  "It does not grow into nothing. It takes what is already there, which
  is a different verb from a plant's and is the whole of what makes it
  unpleasant.

  Only young fungus spreads, so a colony has a living edge and a dead
  middle, and `life` here is age rather than a countdown."
  [b l x y]
  (let [i (idx x y)
        age (aget l i)
        {:keys [spread vigour]} @settings
        eats (:eats (by-mat FUNGUS))]
    (aset l i (min 254 (inc age)))
    (when (and (< age vigour) (< (js/Math.random) spread))
      (let [[nx ny] (one-of (around x y))]
        (when (and (inside? nx ny) (contains? eats (cell b nx ny)))
          (aset b (idx nx ny) FUNGUS)
          (aset l (idx nx ny) 0))))))

;; ---------------------------------------------------------------------
;; One pass
;;
;; A cond on integers rather than a lookup in the table, because most of
;; the board is empty and this runs eight and a half thousand times a
;; frame under an interpreter. The table is for everything else.
;; ---------------------------------------------------------------------

(defn step! [b l wz mv saturated?]
  (.fill mv 0)
  (loop [i (dec SIZE)]
    (when (>= i 0)
      (let [m (aget b i)]
        (when (and (not (zero? m)) (zero? (aget mv i)))
          (let [x (rem i W) y (quot i W)]
            (cond
              (== m SAND)
              (let [wet (or (beside? b x y WATER) (beside? b x y OIL))]
                (cond
                  (and saturated? wet) (aset b i WATER)
                  wet                  (liquid! b l mv x y 3)
                  :else                (powder! b l mv x y 3)))

              (== m WATER)  (liquid! b l mv x y 2)
              (== m ASH)    (powder! b l mv x y 3)
              (== m OIL)    (when (> (js/Math.random) 0.40) (liquid! b l mv x y 1))
              (== m FIRE)   (fire! b l wz mv x y)
              (== m STEAM)  (steam! b l mv x y)
              (== m PLANT)  (grow! b l x y)
              (== m FUNGUS) (fungus! b l x y)))))
      (recur (dec i)))))

;; ---------------------------------------------------------------------
;; The brush
;; ---------------------------------------------------------------------

(defn- daub! [b l [x y] what]
  (let [r (:brush @settings)
        s (by-mat what)]
    (doseq [dy (range (- r) (inc r))
            dx (range (- r) (inc r))
            :when (<= (+ (* dx dx) (* dy dy)) (* r r))]
      (let [px (+ x dx) py (+ y dy)]
        (when (inside? px py)
          (let [i (idx px py)]
            (cond
              (nil? what) nil
              (== what EMPTY) (do (aset b i EMPTY) (aset l i 0))
              (== EMPTY (aget b i))
              (do (aset b i what)
                  (aset l i (cond (== what FIRE)  (:fuel s 60)
                                  (== what STEAM) (:steam-life @settings)
                                  :else 0))))))))))

;; ---------------------------------------------------------------------
;; Ink
;;
;; Read off the canvas rather than written in here, so the palette is a
;; stylesheet decision. Re-read twice a second, because the reader can
;; flip the scheme at any moment and a colour taken once is the wrong
;; colour from then on.
;; ---------------------------------------------------------------------

(defn- word-of [s]
  (let [t (str/trim (or s ""))
        [r g b] (if (str/starts-with? t "#")
                  [(js/parseInt (subs t 1 3) 16)
                   (js/parseInt (subs t 3 5) 16)
                   (js/parseInt (subs t 5 7) 16)]
                  (take 3 (concat (map js/parseInt (re-seq #"\d+" t))
                                  [128 128 128])))]
    (bit-or -16777216
            (bit-shift-left (bit-and b 255) 16)
            (bit-shift-left (bit-and g 255) 8)
            (bit-and r 255))))

(defn- read-inks! [canvas]
  (let [style (js/getComputedStyle canvas)
        get-var (fn [v] (word-of (.getPropertyValue style v)))]
    (reset! inks
            (assoc (into {EMPTY 0}
                         (map (fn [{:keys [m var]}] [m (get-var var)]))
                         substances)
                   :fungus-young (get-var "--sand-fungus")
                   :fungus-old   (get-var "--sand-fungus-old")
                   :fungus-dead  (get-var "--sand-fungus-dead")))))

(defn draw!
  "One putImageData for the whole board. The only square that has to be
  asked twice is fungus, which is three colours depending on how long it
  has been there."
  [ctx b l]
  (let [buf @words ink @inks
        young (:fungus-young ink) old (:fungus-old ink) dead (:fungus-dead ink)]
    (loop [i (dec SIZE)]
      (when (>= i 0)
        (let [m (aget b i)]
          (aset buf i
                (if (== m FUNGUS)
                  (let [a (aget l i)]
                    (cond (< a 60) young (< a 160) old :else dead))
                  (get ink m 0))))
        (recur (dec i))))
    (.putImageData ctx @image 0 0)))

;; ---------------------------------------------------------------------
;; The palette, which is also the key
;; ---------------------------------------------------------------------

(defn- palette-html []
  (str/join
   ""
   (concat
    (for [{:keys [m label var note]} substances]
      (str "<button class='legend-item sand-pick"
           (when (== m @tool) " sand-chosen") "'"
           " data-pick='" m "' style='color: var(" var ")'>"
           "<span class='legend-glyph'><svg viewBox='0 0 24 24'>"
           (:glyph (by-mat m)) "</svg></span>"
           "<span class='legend-label'>" label "</span>"
           "<span class='legend-note'>" note "</span></button>"))
    [(str "<button class='legend-item sand-pick"
          (when (== EMPTY @tool) " sand-chosen") "' data-pick='0'>"
          "<span class='legend-glyph'><svg viewBox='0 0 24 24'>"
          "<path d='M5 19h14M7 15l8-8 3 3-8 8z'/></svg></span>"
          "<span class='legend-label'>rubber</span>"
          "<span class='legend-note'>or hold the right button</span></button>")])))

(defn- tally [b]
  (loop [i (dec SIZE) acc {}]
    (if (neg? i) acc (recur (dec i) (update acc (aget b i) (fnil inc 0))))))

(defn paint! [node]
  (let [counts (tally @board)]
    (fedoras.ui.widget-ui/html! node ".sand-palette" (palette-html))
    (fedoras.ui.widget-ui/state!
     node
     (str (:label (by-mat @tool) {:label "rubber"})
          " · " (- SIZE (get counts EMPTY 0)) " squares in use"))
    (fedoras.reader/text!
     node ".sand-counts"
     (str/join " · " (for [{:keys [m label]} substances
                           :let [n (get counts m 0)] :when (pos? n)]
                       (str n " " label))))
    (fedoras.reader/enable! node ".sand-wet" true
                            (str "Saturated: " (if @wet-on "on" "off")))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- ->cell [cv e]
  [(int (* W (/ (.-offsetX e) (.-clientWidth cv))))
   (int (* H (/ (.-offsetY e) (.-clientHeight cv))))])

(defn- wire-canvas! [node]
  (when-let [cv (fedoras.ui.widget-ui/q! node ".sand-canvas")]
    ;; nothing comes out of the tap until somebody turns it
    (set! (.-onpointerdown cv)
          (fn [e]
            (.preventDefault e)
            (reset! holding (if (== 2 (.-button e)) EMPTY @tool))
            (reset! at-xy (->cell cv e))))
    (set! (.-onpointermove cv)
          (fn [e] (when (some? @holding) (reset! at-xy (->cell cv e)))))
    (set! (.-onpointerup cv)    (fn [_] (reset! holding nil)))
    (set! (.-onpointerleave cv) (fn [_] (reset! holding nil)))
    (set! (.-oncontextmenu cv)  (fn [e] (.preventDefault e) false))))

(defn- wire-palette! [node]
  (fedoras.ui.widget-ui/delegate!
   node ".sand-palette" ".sand-pick" ["pick"]
   (fn [_ [pick]] (reset! tool pick) (paint! node))))

(defn- wire-controls! [node]
  (fedoras.reader/on-click! node ".sand-clear"
                            (fn [_] (.fill @board EMPTY) (.fill @life 0) (.fill @was 0) (paint! node)))
  (fedoras.reader/on-click! node ".sand-wet"
                            (fn [_] (swap! wet-on not) (paint! node))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current
                     (select-keys setting
                                  [:brush :smoke :ash :condense :steam-life
                                   :growth :reach :spread :vigour :vitrify]))))
     (paint! node))))

(defn setup! [node]
  (let [id (.-id node)
        cv (fedoras.reader/q node ".sand-canvas")
        ctx (.getContext cv "2d")]
    (reset! board (js/Uint8Array. SIZE))
    (reset! life  (js/Uint8Array. SIZE))
    (reset! was   (js/Uint8Array. SIZE))
    (reset! moved (js/Uint8Array. SIZE))
    (reset! image (.createImageData ctx W H))
    (reset! words (js/Uint32Array. (.-buffer (.-data @image))))
    (set! (.-imageSmoothingEnabled ctx) false)
    (read-inks! cv)

    (fedoras.ui.widget-ui/wire! node "canvas"   wire-canvas!)
    (fedoras.ui.widget-ui/wire! node "palette"  wire-palette!)
    (fedoras.ui.widget-ui/wire! node "controls" wire-controls!)
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))

    (letfn [(frame []
              (when (some? @holding) (daub! @board @life @at-xy @holding))
              (step! @board @life @was @moved @wet-on)
              (draw! ctx @board @life)
              (swap! frames inc)
              (when (zero? (mod @frames 30))
                (read-inks! cv)
                (paint! node))
              (js/requestAnimationFrame frame))]
      (frame))))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount! ::sand (or id "sand") setup! paint!))