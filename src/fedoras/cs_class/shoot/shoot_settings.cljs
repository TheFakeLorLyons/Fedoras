;; ---------------------------------------------------------------------
;; SHOOT_SETTINGS.CLJS — what the reader keeps in view.
;;
;; Change a number and press Run. `Put it back` is the file as it
;; shipped.
;; ---------------------------------------------------------------------

{;; How many past rounds stay on the board, under the tally. Six is
 ;; enough to see a pattern forming and not so many it starts looking
 ;; like a ledger.
 ;;
 ;; This is the only number here, because it is the only thing about
 ;; this that was ever adjustable. What it does with your last round
 ;; is not a setting, and never was.
 :log-rows 6}