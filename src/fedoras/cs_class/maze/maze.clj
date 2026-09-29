(ns fedoras.cs-class.maze.maze
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [fedoras.state :as state]
            [fedoras.widget :as w]
            [scicloj.kindly.v4.kind :as kind]))

(def settings-file "fedoras/cs_class/maze/maze_settings.cljs")

(def shipped
  "The settings as they ship, read the way the Run button reads them."
  (if-let [text (state/resource-text settings-file)]
    (edn/read-string text)
    (throw (ex-info "maze_settings.cljs is not on the classpath"
                    {:path settings-file}))))

(def warrant-rule
  "Always present, on every floor, and the only one that cannot be met
  by moving something."
  (:warrant-rule shipped))

(def house-rules
  "In the order the building introduces them. The order is not an
  accident: the first is the one you can see happening, the second
  teaches you that a barricade does not last, the third teaches you
  that the audit can be aimed, and the fourth is furniture."
  (:house-rules shipped))

(defn plan-for
  "What is on the wall on a given floor: as many of the house rules as
  the chosen row opens with, one more for each step the descent allows,
  and the warrant always."
  [level]
  (let [{:keys [difficulty difficulties descent]} shipped
        opening (get-in difficulties [difficulty :rules] 1)
        [every limit] (:rules descent)
        added (if (and every (pos? every)) (quot (dec level) every) 0)
        on-the-wall (min (count house-rules)
                         (max opening (min (or limit (count house-rules))
                                           (+ opening added))))]
    (conj (vec (take on-the-wall house-rules)) warrant-rule)))

(mapv (fn [n] [n (count (plan-for n))]) [1 2 3 4 5])

;; The verb under :keep is always bound to `rule`. Bound to `keep`, it
;; would shadow clojure.core/keep inside the very functions that want it.
(defn describe
  [{rule :keep a :a b :b}]
  (case rule
    :apart    (str (name a) " shall not adjoin " (name b) ".")
    :together (str (name a) " shall adjoin " (name b) ".")
    :clear    (str "Nothing shall adjoin " (name a) ".")
    :present  (str (name a) " shall be held on this floor.")
    (str (str/join " " (map name (remove nil? [a rule b]))) ".")))

(mapv describe (plan-for 4))

;; ---------------------------------------------------------------------
;; The audit
;; ---------------------------------------------------------------------

(defn rooms [floor] (set (keys (:plan floor))))

(defn fixture
  [floor coord]
  (let [v (get (:plan floor) coord)]
    (when-not (or (nil? v) (= v :empty)) v)))

(defn adjoining
  [floor [r c]]
  (filter (rooms floor) [[(dec r) c] [r (inc c)] [(inc r) c] [r (dec c)]]))

(defn holding
  [floor thing]
  (keep (fn [[coord v]] (when (= v thing) coord)) (:plan floor)))

(defn touching?
  [floor a b]
  (boolean (some (fn [coord]
                   (some #(= b (fixture floor %)) (adjoining floor coord)))
                 (holding floor a))))

(defn met?
  [floor {rule :keep a :a b :b}]
  (case rule
    :apart    (not (touching? floor a b))
    :together (touching? floor a b)
    :clear    (not-any? (fn [coord]
                          (some #(fixture floor %) (adjoining floor coord)))
                        (holding floor a))
    :present  (boolean (seq (holding floor a)))
    true))

(defn violations
  [floor]
  (remove #(met? floor %) (:requirements floor)))

(defn repairable?
  "Three of the four verbs can be met by moving something. One cannot,
  and what the floor does about that one is the whole endgame."
  [{rule :keep}]
  (contains? #{:apart :together :clear} rule))

(mapv (juxt describe repairable?) (plan-for 4))

^:kindly/hide-code
(kind/hiccup
 [:pre {:class "handout"}
  (str/join
   "\n"
   ["THE FLOOR IS CORRECT WHEN EVERY REQUIREMENT IS MET."
    "Where a Requirement is not met the floor is rearranged until it is."
    "Where a Requirement cannot be met the rooms that held it are released,"
    "  one at a time, beginning at the edges."
    "Rearrangement is not a component of review. Rearrangement is recorded."])])

;; ---------------------------------------------------------------------
;; The widget
;; ---------------------------------------------------------------------

(defn widget
  ([] (widget {}))
  ([{:keys [id label settings-rows]
     :or {id "maze"
          label "MAZE.CLJS · arrows or a click to walk · shift to push · space to take · 1 2 3 for powerups"
          settings-rows 30}}]
   (w/frame
    {:widget :maze
     :id id
     :label label
     :focus? true
     :settings-rows settings-rows
     :foot "Take the warrant and get out. The building rearranges itself to keep its rules."}
    [:div.maze-bar]
    [:div.maze-stage
     [:div.maze-grid]
     [:div.maze-side
      [:div.maze-status]
      [:div.maze-powers]
      [:div.maze-record]]]
    [:div.live-bar
     [:button.roll-go.maze-take "Take (space)"]
     [:button.roll-go.maze-push "Push (shift)"]
     [:button.roll-go.maze-restart "Restart floor (r)"]
     [:button.roll-rewrite.maze-again "Another floor (n)"]]
    [:p.maze-score]
    [:details.maze-key-panel
     [:summary.maze-heading "Key"]
     [:div.maze-legend]]
    [:div.maze-lower
     [:div.maze-wall
      [:p.maze-heading "Requirements"]
      [:div.maze-reqs]]
     [:div.maze-detail
      [:p.maze-heading "Details"]
      [:div.maze-about]]])))