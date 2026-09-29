;; ---------------------------------------------------------------------
;; LINE_SETTINGS.CLJS — what the tunnel is set to.
;;
;; Change a number and press Run. Break it and the line underneath will
;; tell you what it thought you meant. Nothing you do here can lose the
;; original: `Put it back` is the file as it shipped.
;;
;; Running this sends a fresh drone down onto a clean floor.
;; ---------------------------------------------------------------------

{;; The tunnel, and how fast a thing on tracks goes. :speed is frames
 ;; between moves — eight is about seven and a half squares a second,
 ;; and anything under four is a different game and not a better one.
 :size  20
 :speed 8

 ;; ---------------------------------------------------------------
 ;; THE ECONOMY, AND IT WORKS TWICE
 ;;
 ;; Feet of line at the start, what each kind of retrieval pays out,
 ;; and how much floor a retrieval puts beyond use.
 ;;
 ;; Everything you take out of that tunnel makes the tunnel worse in
 ;; two different ways, and neither of them can be undone, and both of
 ;; them came out of the same act.
 ;;
 ;; Set :find-pays and :silt-per-find both to 0 and the machine becomes
 ;; possible and stops being about anything. Try it once and put it
 ;; back.
 ;; ---------------------------------------------------------------
 :paid          4
 :find-pays     3
 :quest-pays    1
 :silt-per-find 2

 ;; The optional slow one, in frames, for a reader who wants the floor
 ;; to come up whether they are working or not. 0 is off and off is the
 ;; default, because the silt a drone raises itself is quite enough.
 ;;
 ;; Set it to 500 and go and do nothing for two minutes and you will see
 ;; what the man with the bucket has been looking at for forty-one
 ;; years.
 :silt-every 0

 ;; ---------------------------------------------------------------
 ;; THE SCORE
 ;;
 ;; The first square is worth one point. Every square after is worth
 ;; one more than the last — three squares comes to six, ten squares
 ;; comes to fifty-five — because the floor gets harder to work and the
 ;; number should say so.
 ;;
 ;; Each of the three pays a foot on its own, same as before. The quest
 ;; itself is catching all three before the floor takes one, or one
 ;; catches up with the floor — do that and :quest-multiplier times
 ;; whatever square you're on lands once, on top of everything else.
 ;; Early it is nothing much. Late it is most of the score, and getting
 ;; there gets close to impossible.
 ;; ---------------------------------------------------------------
 :quest-multiplier 500

 ;; ---------------------------------------------------------------
 ;; THE QUESTS
 ;;
 ;; Three of them, one to a floor, each its own colour, each fading
 ;; toward the colour of the floor as its time runs out. Each pair
 ;; below is a range in frames, not a fixed number — the actual time
 ;; is drawn fresh from it every floor, and which draw lands on which
 ;; colour is reshuffled too, so neither the pace nor the order repeats.
 ;; The legend above the field always shows that floor's order, fastest
 ;; to slowest, left to right.
 ;;
 ;; They move at :quest-speed, in frames between wander-steps — set to
 ;; match the tunnel's own :speed by default, so out of the box there's
 ;; one clock for everything down there, but it's its own number if you
 ;; want the quests faster or slower than the drone.
 ;;
 ;; Each one only ever lands somewhere that still has a way out from
 ;; it, so reaching one is never how the run ends outright. That gets
 ;; harder to promise as the floor fills in, and eventually it can't be
 ;; promised at all — which is its own kind of answer.
 ;;
 ;; They can be buried. If the floor comes up under one it is gone, and
 ;; nothing says so.
 ;; ---------------------------------------------------------------
 :quest-fast-min 160
 :quest-fast-max 220
 :quest-mid-min  260
 :quest-mid-max  340
 :quest-slow-min 400
 :quest-slow-max 520
 :quest-speed    8

 ;; ---------------------------------------------------------------
 ;; THE NUMBER
 ;;
 ;; What the counter was at when he found the machine. The first drone
 ;; anybody sends down is the one after this, and it does not reset, and
 ;; the eleven before it were not his.
 ;; ---------------------------------------------------------------
 :number-from 11}