;; ---------------------------------------------------------------------
;; SURVEY_SETTINGS.CLJS — the settings of the survey.
;;
;; Change a number and press Run. A new ground is laid out, because
;; changing the settings halfway through would be unfair, and that is a
;; standard this program holds itself to and the office does not.
;;
;; :resurvey-every is the one to play with. At 12 the ground is
;; trustworthy most of the time. At 4 nothing you deduce survives long
;; enough to be worth deducing. At 999 it never moves and this is
;; ordinary minesweeper, which is worth seeing once so that you know
;; what the difference costs you.
;; ---------------------------------------------------------------------

{:width  9
 :height 9

 :hazard-count 10       ; how many there are
 :rover-count  3        ; how many you may lose

 :resurvey-every 12     ; probes between one resurvey and the next

 ;; What the office may do when it moves them. Both of these are true
 ;; as shipped, and both are the reason it can say it has never put a
 ;; hazard under anybody.
 :protect-surveyed true ; no hazard on ground you have walked
 :pin-found        true ; a hazard you found stays where you found it

 ;; Set this true and the header says how many arrangements still fit.
 ;; It is the number the endgame is made of, and the office does not
 ;; publish it.
 :show-freedom false}