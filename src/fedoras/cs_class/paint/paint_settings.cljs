;; ---------------------------------------------------------------------
;; PAINT_SETTINGS.CLJS — what the filing system is set to.
;;
;; Change a number and press Run. Then draw something, and look at the
;; two counts under the picture.
;;
;; :tolerance is the one that matters. It is how far a point may sit
;; from the line before the line is judged to need it. At 0.6 a scribble
;; keeps about one point in twenty. At 0.05 it keeps most of them. At 4
;; it keeps the corners and nothing else.
;;
;; Nothing in this program deletes a mark. This number deletes most of
;; every mark you make, and there is no record anywhere of what it took.
;; ---------------------------------------------------------------------

{:tolerance     0.6
 :tolerance-min 0.02
 :tolerance-max 6.0
 :weight        1.4
 :weight-min    0.4
 :weight-max    4.0
 :strength-min  0.005
 :strength-max  0.4

 :ledger    6        ; how many rows of the file are shown at once
 :fill-alpha 0.35

 ;; The two inks. Withdrawn marks are drawn first and underneath, so
 ;; whatever you take off the picture is still on the picture.
 :inks [{:k :ink     :label "ink"    :var "--ink"}
        {:k :violet  :label "violet" :var "--violet"}
        {:k :gold    :label "gold"   :var "--gold"}
        {:k :coral   :label "coral"  :var "--coral"}
        {:k :green   :label "green"  :var "--green"}
        {:k :blurple :label "blue"   :var "--blurple"}]
 :withdrawn-ink "--ink-soft"

 ;; The Rubber Controls
 :reach    6.0
 :strength 0.05
 :spread   0.5
 :grain    1.5


 ;; Set this false and withdrawn marks stop being drawn at all. Nothing
 ;; else changes: the counts are the same, the file is the same, the
 ;; marks are all still there. Only the picture agrees with you.
 :show-withdrawn true
 :floor          0.12}