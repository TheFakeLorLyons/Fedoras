;; ---------------------------------------------------------------------
;; LIFE_SETTINGS.CLJS — the rules, and who follows them.
;;
;; Change a number and press Run.
;;
;; A SPECIES is a colour with its own opinion about neighbours.
;;
;;   :born      how many live neighbours a dead cell needs to become one
;;   :survives  how many a live one needs to stay
;;
;; Conway is :born #{3} :survives #{2 3}, and it is the first one below,
;; so you can see what everything else is a departure from.
;;
;; WHEN THERE IS MORE THAN ONE KIND OF ALIVE, a cell born into a mixed
;; neighbourhood goes to whichever species has the most neighbours there,
;; weighted by `:pull`. A species with high pull takes ground it did not
;; earn, which is what pull means everywhere else too.
;;
;;   :pull      weight when claiming a newborn cell. 1 is fair.
;;   :hostile   set of species ids whose neighbours count against
;;              survival rather than for it
;;   :allied    set of species ids whose neighbours count as its own
;;   :crowd     added to the survival count per neighbour of its own
;;              kind beyond four. positive likes a crowd, negative
;;              cannot bear one.
;;   :spread    how far it reaches. 1 is the eight squares around it.
;;              2 is the twenty-four, and a species that reaches two
;;              squares will eat one that reaches one.
;;   :decay     chance in a hundred of dying for no reason at all
;;
;; Leave a key out and it does nothing.
;; ---------------------------------------------------------------------

{:grid {:cols 120 :rows 72 :scale 6 :wrap true}

 :speed 12              ; generations a second, roughly
 :seed 4471
 :scatter 0.18          ; how much of the grid Scatter fills
 :scatter-on-run false  ; true and it fills itself when you press Run

 ;; -------------------------------------------------------------------
 ;; PALETTES. The first is used until you pick another.
 ;; -------------------------------------------------------------------
 :palettes
 [{:id :ink     :name "ink"
   :dead "--panel"
   :colours ["#e6e4df" "#9d8dff" "#d0a44c" "#6fcf9b" "#e2707c"]}
  {:id :rust    :name "rust"
   :dead "#1a1614"
   :colours ["#d9743f" "#8c3b2a" "#e6b877" "#5c6b52" "#2f4858"]}
  {:id :bloom   :name "bloom"
   :dead "#101318"
   :colours ["#7ee0d0" "#f2779a" "#ffd166" "#8b95f0" "#b5e48c"]}
  {:id :paper   :name "paper"
   :dead "#f2efe6"
   :colours ["#1c1f24" "#7a5c3e" "#4a6b39" "#8a3b31" "#3f5a7a"]}]

 ;; -------------------------------------------------------------------
 ;; SPECIES. Up to five. The first is Conway, unaltered, so the rest
 ;; can be read as what happens when you change one line of it.
 ;; -------------------------------------------------------------------
 :species
 [{:id :plain  :name "PLAIN"
   :born #{3} :survives #{2 3}}

  {:id :moss   :name "MOSS"
   :born #{3 6} :survives #{2 3 4 5}
   :crowd 1 :pull 0.8
   :note "spreads slowly, holds ground"}

  {:id :spark  :name "SPARK"
   :born #{2} :survives #{2 3}
   :pull 1.6 :decay 4
   :note "takes ground fast, does not keep it"}

  {:id :rot    :name "ROT"
   :born #{3} :survives #{1 2 3 4 5 6 7 8}
   :pull 0.6 :decay 12 :hostile #{:moss}
   :note "survives anything, dies of nothing in particular"}

  {:id :reach  :name "REACH"
   :born #{4 5} :survives #{3 4 5 6}
   :spread 2 :pull 1.2 :crowd -1
   :note "sees further, cannot bear company"}]

 ;; -------------------------------------------------------------------
 ;; PATTERNS. Stamped from the menu, in whichever species is selected.
 ;; Coordinates are offsets from where you click.
 ;; -------------------------------------------------------------------
 :patterns
 [{:id :glider :name "glider"
   :cells [[1 0] [2 1] [0 2] [1 2] [2 2]]}
  {:id :blinker :name "blinker"
   :cells [[0 0] [1 0] [2 0]]}
  {:id :block :name "block"
   :cells [[0 0] [1 0] [0 1] [1 1]]}
  {:id :ship :name "lightweight ship"
   :cells [[0 0] [3 0] [4 1] [0 2] [4 2] [1 3] [2 3] [3 3] [4 3]]}
  {:id :pulsar :name "pulsar"
   :cells [[2 0] [3 0] [4 0] [8 0] [9 0] [10 0]
           [0 2] [5 2] [7 2] [12 2]
           [0 3] [5 3] [7 3] [12 3]
           [0 4] [5 4] [7 4] [12 4]
           [2 5] [3 5] [4 5] [8 5] [9 5] [10 5]
           [2 7] [3 7] [4 7] [8 7] [9 7] [10 7]
           [0 8] [5 8] [7 8] [12 8]
           [0 9] [5 9] [7 9] [12 9]
           [0 10] [5 10] [7 10] [12 10]
           [2 12] [3 12] [4 12] [8 12] [9 12] [10 12]]}
  {:id :acorn :name "acorn"
   :cells [[1 0] [3 1] [0 2] [1 2] [4 2] [5 2] [6 2]]}
  {:id :dot :name "one cell"
   :cells [[0 0]]}]}