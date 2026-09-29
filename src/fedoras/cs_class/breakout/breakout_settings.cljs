;; ---------------------------------------------------------------------
;; BREAKOUT_SETTINGS.CLJS — what the wall is set to.
;;
;; Change a number and press Run. Break it and the line underneath will
;; tell you what it thought you meant. Nothing you do here can lose the
;; original: `Put it back` is the file as it shipped.
;;
;; WHAT EACH DROP DOES IS NOT IN HERE. That is a table at the top of
;; breakout_ui.cljs, one row each, because what a thing does is a fact
;; about the thing and not a setting. How often they turn up is a
;; setting, and it is the first line below.
;; ---------------------------------------------------------------------

{;; How likely a brick is to have had something in it. 0 and the wall is
 ;; just a wall. 1 and every brick you break gives you something, and
 ;; two things in seven are worse than nothing, so it gets harder rather
 ;; than easier, which is worth finding out on purpose.
 :drop-chance 0.22

 ;; How long an effect lasts, in frames. Sixty is a second.
 :effect 600

 ;; How long the bat holds a ball before it lets go by itself. There are
 ;; three other ways out of that state and this is the one that does not
 ;; need anybody.
 :serve 120

 ;; THE PERMANENT ONES.
 ;;
 ;; :fixed-first is how many bricks in the first wall cannot be broken.
 ;; :fixed-each is how many more join them every time the wall comes
 ;; back — and the ones from last time are still there.
 ;;
 ;; At 1 and 1 the thirtieth wall is solid and there is nothing left to
 ;; hit. At 0 and 0 it is the game as he handed it in. At 1 and 3 you
 ;; will see the end of it in about ten minutes.
 ;;
 ;; Nothing anywhere calls this losing.
 :fixed-first 1
 :fixed-each  1

 ;; The ball, and the bat, before anything in the wall has had an
 ;; opinion about either.
 :speed 1.0
 :bat   10

 ;; :always, :learned or :never.
 ;;
 ;; :learned is the one he wrote. A drop is unmarked until you have
 ;; caught one, and red for ever after — he did not mark them, you did.
 ;; It is not the default because the key is the one place a player is
 ;; entitled to be told things.
 :mark :always}