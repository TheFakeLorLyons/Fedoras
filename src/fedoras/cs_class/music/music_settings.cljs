;; ---------------------------------------------------------------------
;; MUSIC_SETTINGS.CLJS — everything the instrument is made of.
;;
;; Change a number and press Run. Break it and the line underneath will
;; tell you what it thought you meant. Nothing you do here can lose the
;; original: `Put it back` is the file as it shipped.
;; ---------------------------------------------------------------------

{:band [60 400]
 :bpm     120           ; 60 to 200
 :rows    15            ; how tall the grid is, in rows
 :below   1             ; octaves of rows under the root. 0 puts the
                        ; root on the bottom row, which is where this
                        ; started.
 :columns 32            ; how much paper. 16, 32, 64, 96.

 :scale   :pentatonic   ; :pentatonic :major :minor :whole :chromatic
 :key     :a            ; :a :a# :b :c :c# :d :d# :e :f :f# :g :g#
                         ; flats work too: :bb :db :eb :gb :ab
 :octave  3             ; :a at 3 is A3, 220 hertz, where this started
 :spell   :auto         ; :auto :sharp :flat — how the rows are named
 :root    220           ; hertz. the bottom row of the grid.

 ;; FILL. One seed is one piece of music, and the same seed always
 ;; gives the same piece back, so a seed worth keeping is worth
 ;; writing down.
 :seed    4471
 :density 0.42          ; 0 to 1. how much of the paper gets used.

 ;; Each voice keeps to its own register, so they are not all
 ;; competing for the same rows and the result reads as several
 ;; instruments rather than one confused one.
 ;;
 ;;   register  [lowest-row highest-row]
 ;;   every     how often it may play. 1 is sixteenths, 4 is quarters.
 ;;   weight    how likely it is to take a chance it is offered.
 ;;   leap      how far it may move at once. 0 never moves.
 :voices
 {:string {:register [0 6]   :every 8 :weight 0.75 :leap 2}
  :reed   {:register [4 11]  :every 4 :weight 0.60 :leap 2}
  :bell   {:register [10 17] :every 2 :weight 1.00 :leap 3}
  :flute  {:register [14 21] :every 2 :weight 0.55 :leap 4}
  :block  {:register [0 2]   :every 4 :weight 0.70 :leap 0}}

 ;; The shape of a phrase, one number per bar, multiplied into the
 ;; density as the fill moves across the paper. Even density is not
 ;; rhythm. This is where the rhythm comes from.
 :phrase [1.0 0.55 0.85 0.35]}