(ns fedoras.campus
  "New Carthage University, as a plan."
  (:require [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

;; ---------------------------------------------------------------------
;; Colours
;; ---------------------------------------------------------------------

(def ink "var(--ink, #1c1f24)")
(def paper "var(--paper, #faf8f4)")
(def stone "var(--panel, rgba(128,128,128,0.28))")
(def stone-dark "var(--rule, rgba(128,128,128,0.5))")
(def green "rgba(111, 207, 155, 0.22)")
(def water "rgba(139, 149, 240, 0.25)")
(def lot "rgba(128, 128, 128, 0.12)")
(def meridian "var(--violet, #6c5ce7)")

(def glass
  "The library. Everything of it that is above the ground is a wall you
  can see through and a lid you cannot."
  "rgba(190, 214, 224, 0.30)")
(def loop-gold "var(--gold, #c8952a)")

;; ---------------------------------------------------------------------
;; The campus
;; ---------------------------------------------------------------------

(def buildings
  "id · name · x y w h · optional :fill and :note.

  Naming convention, such as it is: money, politics, and one building
  nobody can account for."
  [{:id :chapel    :name "THE OLD CHAPEL"          :x 340 :y 80  :w 100 :h 45
    :note "closed"}
   {:id :carter    :name "PRENTISS SCIENCE CENTER" :x 330 :y 175 :w 250 :h 75
    :note "chemistry · Carter's door"}
   {:id :sterling  :name "STERLING CENTER"         :x 615 :y 175 :w 145 :h 75
    :note "administration · fourteen vice deans"}
   {:id :b14       :name "BUILDING 14"             :x 795 :y 175 :w 115 :h 75
    :fill stone-dark :note "no listing"}
   {:id :meridian  :name "MERIDIAN HALL"           :x 615 :y 265 :w 145 :h 75
    :fill meridian :note "computer science"}
   {:id :annex     :name "MERIDIAN ANNEX"          :x 615 :y 350 :w 145 :h 45
    :fill meridian :note "no public entrance"}
   {:id :dorm-n    :name "NORTH HOUSE"             :x 795 :y 265 :w 115 :h 55}
   {:id :dorm-s    :name "SOUTH HOUSE"             :x 795 :y 330 :w 115 :h 55}
   {:id :dorm-e    :name "ASHCROFT HOUSE"          :x 795 :y 395 :w 115 :h 55
    :note "no relation, he says"}
   {:id :whitlock  :name "WHITLOCK HALL"           :x 330 :y 425 :w 155 :h 90
    :note "history · the big auditorium"}
   {:id :union     :name "HALLORAN UNION"          :x 500 :y 425 :w 70  :h 90
    :note "room 3C"}
   {:id :commons   :name "THE COMMONS"             :x 615 :y 425 :w 145 :h 90
    :note "dining · never closes"}
   {:id :field-h   :name "ASHCROFT FIELD HOUSE"    :x 795 :y 460 :w 115 :h 55
    :note "no relation, he says"}
   {:id :beckwith  :name "BECKWITH HALL"           :x 135 :y 265 :w 145 :h 85
    :note "english · history"}
   {:id :library   :name "OAKES LIBRARY"           :x 152 :y 362 :w 110 :h 110
    :shape :round :doors [:east :west]
    :fill glass :stroke stone-dark
    :note "open until"}
   {:id :plant     :name "PLANT SERVICES"          :x 135 :y 560 :w 145 :h 60
    :note "restoration shop"}])

(def grounds
  "Named ground. Two of these have a shape in the third dimension, and
  nothing here says which: fill is piled and water is dug, and the walk
  works that out from what each one is made of."
  [{:name "THE QUAD"      :surface :lawn  :x 330 :y 265 :w 250 :h 130 :fill green}
   {:name "CORLISS FIELD" :surface :field :x 615 :y 560 :w 145 :h 65  :fill green}
   {:name "THE FILL"      :surface :fill  :x 330 :y 665 :w 250 :h 85  :fill stone
    :note "reclamation · do not dig"}
   {:name "SPRING BROOK"  :surface :water :x 50  :y 90  :w 38  :h 630 :fill water}])

(def lots
  [{:name "LOT A" :surface :lot :x 330 :y 560 :w 130 :h 65}
   {:name "LOT B" :surface :lot :x 475 :y 560 :w 105 :h 65}
   {:name "LOT C" :surface :lot :x 135 :y 175 :w 145 :h 75}])

(def rail
  "The line. It was here before the campus and the campus was built to
  face away from it."
  "M 940 90 L 940 730")

(def roads
  "Two kinds, and they are marked differently because they are
  different. A :road is a through road: it runs the length of the
  campus or round the outside of it, it carries traffic that is not
  stopping here, and it has the pair of unbroken lines down the middle
  that says do not cross. A :street runs between buildings, is narrower,
  and has the single dashed line of somewhere you are expected to be
  turning off shortly.

  Everything on this campus used to be the first kind and carry the
  markings of both at once."
  [{:kind :road   :d "M 105 60 L 105 740"}
   {:kind :road   :d "M 105 150 L 960 150"}
   {:kind :road   :d "M 105 540 L 960 540"}
   {:kind :street :d "M 105 640 L 620 640"}
   {:kind :street :d "M 305 150 L 305 640"}
   {:kind :street :d "M 597 150 L 597 540"}
   {:kind :street :d "M 775 150 L 775 640"}])

(def shuttle-route
  "The bus. Driverless since before anyone enrolled.

  A route and not a surface. It used to be painted onto the campus as a
  narrow lane of its own, laid along the roads it followed, which meant
  those roads carried a shuttle lane's pair of lines and a road's dashed
  line in the same lane at the same time. No road anywhere is marked
  like that. What a shuttle actually has is a path, and the path runs
  down roads that were already there.

  Nothing draws this yet. It is here so that whatever eventually drives
  it has somewhere to go, and so that the roads under it stay roads."
  "M 305 150 L 775 150 L 775 540 L 305 540 Z")

(def help-posts
  "Authored where a post would stand: six feet off the kerb, out of the
  buildings, out of the lots. The walk still runs its own clearance
  pass over these, because somebody will move a building and not think
  to move the post beside it."
  [[200 131] [500 131] [700 131]
   [200 169] [640 169]
   [324 200] [324 300] [324 470]
   [286 380] [286 600]
   [578 300] [578 470] [616 410]
   [400 521] [660 521]
   [470 621] [250 659]
   [795 565] [128 480] [920 400]])

;; ---------------------------------------------------------------------
;; Drawing
;; ---------------------------------------------------------------------

(defn- wrap
  "Break a building name onto lines short enough for its box."
  [name w]
  (let [per (max 8 (int (/ w 7.2)))]
    (reduce (fn [lines word]
              (let [last-line (peek lines)]
                (if (and last-line (<= (+ (count last-line) 1 (count word)) per))
                  (conj (pop lines) (str last-line " " word))
                  (conj lines word))))
            []
            (str/split name #"\s+"))))

(defn- label [{:keys [name x y w h]} & {:keys [size fill]}]
  (let [lines (wrap name w)
        size  (or size 9)
        top   (+ y (* 0.5 h) (- (* 0.5 size (dec (count lines)))) (* 0.35 size))]
    (map-indexed
     (fn [i line]
       [:text {:x (+ x (* 0.5 w))
               :y (+ top (* i (* size 1.15)))
               :text-anchor "middle"
               :font-family "ui-monospace, monospace"
               :font-size size
               :letter-spacing "0.06em"
               :style {:fill (or fill ink)}}
        line])
     lines)))

(defn- rect
  "A building on the plate. A round one is drawn round, because a plan
  that shows the library as a box is a plan somebody will build from."
  [{:keys [x y w h fill stroke rx shape]}]
  (if (= :round shape)
    [:circle {:cx (+ x (/ w 2.0)) :cy (+ y (/ h 2.0))
              :r (/ (min w h) 2.0) :stroke-width 1
              :style {:fill (or fill stone) :stroke (or stroke ink)}}]
    [:rect {:x x :y y :width w :height h :rx (or rx 2) :stroke-width 1
            :style {:fill (or fill stone) :stroke (or stroke ink)}}]))

(defn svg
  "The whole plan, as hiccup."
  []
  (into
   [:svg {:viewBox "0 0 980 780" :width "100%"
          :xmlns "http://www.w3.org/2000/svg"
          :style {:max-width "980px" :background paper}}
    [:rect {:x 0 :y 0 :width 980 :height 780 :style {:fill paper}}]]
   (concat
    (mapcat (fn [g]
              (cons (rect (assoc g :stroke stone-dark))
                    (when (seq (:name g)) (label g :size 9 :fill "var(--ink-soft, #5d6b5a)"))))
            grounds)
    (mapcat (fn [l]
              (cons (rect (assoc l :fill lot :stroke stone-dark))
                    (label l :size 8 :fill "var(--ink-soft, #7a746a)")))
            lots)
    (map (fn [{:keys [d kind]}]
           [:path {:d d :stroke-width (if (= kind :street) 5 7)
                   :stroke-linecap "square"
                   :style {:stroke "var(--ink-soft, #8e8880)" :fill "none"}}])
         roads)
    (map (fn [{:keys [d kind]}]
           [:path {:d d :stroke-width 2
                   :stroke-dasharray (if (= kind :street) "10 10" "none")
                   :style {:stroke paper :fill "none"}}])
         roads)
    [[:path {:d rail :stroke ink :stroke-width 3 :fill "none"}]
     [:path {:d rail :stroke paper :stroke-width 1
             :stroke-dasharray "4 8" :fill "none"}]
     [:text {:x 952 :y 400 :fill "var(--ink-soft, #7a746a)" :font-size 9
             :font-family "ui-monospace, monospace"
             :transform "rotate(90 952 400)"} "RAIL"]]
    [[:path {:d shuttle-route :stroke-width 3 :stroke-dasharray "14 8"
             :style {:stroke loop-gold :fill "none"}}]]
    (mapcat (fn [b]
              (concat [(rect (assoc b :fill (or (:fill b) stone)))]
                      (label b :fill (if (:fill b) paper ink))))
            buildings)
    (map (fn [[x y]] [:circle {:cx x :cy y :r 3.5 :stroke-width 1
                               :style {:fill "var(--blurple, #2f6fb0)" :stroke paper}}])
         help-posts)
    [[:g {:transform "translate(640,665)"}
      [:rect {:x 0 :y 0 :width 300 :height 90 :style {:fill paper :stroke ink}}]
      [:text {:x 12 :y 20 :font-size 10 :font-family "ui-monospace, monospace"
              :letter-spacing "0.1em" :style {:fill ink}} "NEW CARTHAGE UNIVERSITY"]
      [:circle {:cx 18 :cy 38 :r 3.5 :style {:fill "var(--blurple, #2f6fb0)"}}]
      [:text {:x 30 :y 41 :font-size 9 :font-family "ui-monospace, monospace"
              :style {:fill ink}} "HELP POST"]
      [:path {:d "M 12 56 L 26 56" :stroke-width 3 :stroke-dasharray "6 4"
              :style {:stroke loop-gold}}]
      [:text {:x 30 :y 59 :font-size 9 :font-family "ui-monospace, monospace"
              :style {:fill ink}} "THE LOOP · automatic"]
      [:rect {:x 12 :y 70 :width 14 :height 9 :style {:fill meridian}}]
      [:text {:x 30 :y 78 :font-size 9 :font-family "ui-monospace, monospace"
              :style {:fill ink}} "MERIDIAN SYSTEMS"]]]
    [[:text {:x 120 :y 70 :font-size 17 :font-family "ui-monospace, monospace"
             :letter-spacing "0.22em" :style {:fill ink}} "NEW CARTHAGE UNIVERSITY"]
     [:text {:x 120 :y 86 :font-size 9 :font-family "ui-monospace, monospace"
             :letter-spacing "0.1em" :style {:fill "var(--ink-soft, #7a746a)"}}
      "PLAN OF THE CAMPUS · REVISED"]])))

(defn render [] (kind/hiccup (svg)))