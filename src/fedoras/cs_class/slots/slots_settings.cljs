;; ---------------------------------------------------------------------
;; SLOTS_SETTINGS.CLJS — what the floor is set to.
;;
;; Change a number and press Run. Break it and the line underneath will
;; tell you what it thought you meant. Nothing you do here can lose the
;; original: `Put it back` is the file as it shipped.
;;
;; Running this walks you out and starts the count over.
;; ---------------------------------------------------------------------

{;; ---------------------------------------------------------------
 ;; THE FLOOR
 ;;
 ;; Every machine on it has its own strip and its own number of
 ;; reels, drawn when you sit down at it. Which symbol counts as 'one
 ;; along' is therefore a property of the cabinet and not of the
 ;; game, and nothing on any of them says so.
 ;;
 ;; :reel-counts is drawn from as written, so four threes and one
 ;; each of four and five means most machines are ordinary and now
 ;; and then you find one that is not. Add strips freely. A strip of
 ;; plain numbers works and prints itself, since only symbols with a
 ;; face get one.
 ;; ---------------------------------------------------------------
 :strips
 {"CHERRY DELUXE" [:cherry :bell :bar :seven :plum :bell :cherry :bar]
  "TRIPLE CROWN"  [:seven :bell :bell :bar :cherry :bar :plum :bell :cherry :seven]
  "THE LONG SHOT" [:cherry :cherry :bell :bar :seven :plum :bar :bell :cherry :seven :bell :bar]}

 :reel-counts [3 3 3 3 4 5]
 :faces {:cherry "🍒" :bell "🔔" :bar "🍫" :seven "7️⃣" :plum "🍇"}

 ;; ---------------------------------------------------------------
 ;; THE LAST REEL
 ;;
 ;; This is the setting. Everything else here is furniture.
 ;;
 ;; True, and when every reel but the last one matches, the last one
 ;; checks them before it lands. It pays at exactly the rate the
 ;; strip says it should — that part is not a lie and you can measure
 ;; it — and when it does not pay, it stops one position along. Close
 ;; enough to see. Close enough to feel like it nearly happened.
 ;;
 ;; False, and the last reel picks like the others do, and the
 ;; machine is honest.
 ;;
 ;; Spin a few thousand of each. What it pays does not move. How
 ;; often the front reels match and the last does not move either.
 ;; Both machines are identical on every number anybody thinks to ask
 ;; for.
 ;;
 ;; The one number that moves is the last one: how often the losing
 ;; reel stops exactly one position along instead of anywhere. Now
 ;; and again when the machine is honest, because it has to happen by
 ;; accident. Every single time when it is not.
 ;;
 ;; There is no regulation anywhere requiring that number on the side
 ;; of the cabinet.
 ;; ---------------------------------------------------------------
 :rigged? true

 ;; ---------------------------------------------------------------
 ;; THE MONEY
 ;;
 ;; :returns is the whole pay table. Nobody designs one of these by
 ;; deciding what the sevens are worth; they decide what fraction of
 ;; the money goes back out, and then solve for what every line has
 ;; to pay to land on exactly that. So the table is different on
 ;; every cabinet, and a symbol paying ten times another tells you
 ;; nothing except how rare it is on that particular strip.
 ;;
 ;; 0.76 is a hard machine. Real ones sit nearer 0.90 and are
 ;; required to say so somewhere. Set it to 1.0 and the floor breaks
 ;; even forever; above that it pays you to stand there, which no
 ;; machine has ever done and none ever will.
 ;;
 ;; Raising the stake raises what a win pays by exactly the same
 ;; factor, so it does not improve or worsen your odds by any amount
 ;; at all. It only changes how long the twenty-five dollars lasts.
 ;; ---------------------------------------------------------------
 :returns     0.76
 :start-money 25
 :stake       1
 :stakes      [1 2 5 10]

 ;; ---------------------------------------------------------------
 ;; THE POINTS
 ;;
 ;; Points are not money. You cannot spin them, you cannot spend
 ;; them, and nothing on this floor will ever convert one into the
 ;; other. They exist so that the number you are watching goes up
 ;; while the number you are spending goes down, and they follow you
 ;; from machine to machine, because you do.
 ;;
 ;; A spin is one point. A near miss is :near-points more, so the
 ;; machine is at its most generous exactly when it has taken your
 ;; money and given you nothing, and it is at its most generous most
 ;; often when :rigged? is true.
 ;;
 ;; Every :free-every near misses buys a free spin — the machine
 ;; settling up in the only currency it prints itself. They need not
 ;; be consecutive; the machine is counting all of them, always, and
 ;; is the only party to this arrangement that is. 0 turns it off,
 ;; and a run gets about a third shorter, which is the point of it
 ;; being there.
 ;; ---------------------------------------------------------------
 :near-points 5
 :free-every  3

 ;; How many spins the second button does at once. It stops early
 ;; when the machine in front of you is empty, which it usually is
 ;; before it gets there.
 :batch 100}