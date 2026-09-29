;; ---------------------------------------------------------------------
;; SAND_SETTINGS.CLJS — what the world is set to.
;;
;; Change a number and press Run. Break it and the line underneath will
;; tell you what it thought you meant. Nothing you do here can lose the
;; original: `Put it back` is the file as it shipped.
;;
;; NOTHING IN THIS FILE IS ABOUT SAND. Sand asks three questions and
;; none of them has a number in it, which is why it is the one substance
;; you cannot tune, and why the angle a heap stands at is not adjustable
;; and never was.
;;
;; What each substance does with fire is not here either. That is in the
;; table at the top of sand_ui.cljs, one row each, because how willingly
;; a thing catches is a fact about the thing and not a setting.
;; ---------------------------------------------------------------------

{;; THE BRUSH. How wide a dab is, in squares. 1 is a grain at a time,
 ;; which is a different program and worth five minutes.
 :brush 3

 ;; WHAT IS LEFT. When a flame goes out, this much of the time it leaves
 ;; whatever that thing leaves — ash, for everything that leaves
 ;; anything. Failing that, this much of the time, smoke. Failing that,
 ;; nothing at all, which is what oil does.
 :ash   0.5
 :smoke 0.35

 ;; STEAM. How long it rises for, and how likely it is to come back as
 ;; water rather than as nothing.
 ;;
 ;; Set :condense to 1 and the board conserves water. A fire under a
 ;; tank will empty it into the ceiling and rain it back down for as
 ;; long as you care to watch.
 :steam-life 160
 :condense 0.25

 ;; PLANT. It does not fall, because it is rooted. It reaches into an
 ;; empty square next to water and drinks the drop it grew towards, so
 ;; a plant with no water is a plant that has stopped.
 ;;
 ;; :reach is how many squares from where you put it a plant may get.
 ;; Raise it to 200 and one seed in a wet corner will take the room.
 :growth 0.10
 :reach  24

 ;; FUNGUS. It does not grow into nothing. It takes wood and plant, and
 ;; only young fungus takes anything, which is why a colony has a living
 ;; white edge and a spent black middle.
 ;;
 ;; :vigour is how many passes a square keeps spreading for. Set it to 4
 ;; and it makes a ring and stops. Set it to 250 and it does not stop.
 :spread 0.06
 :vigour 90

 ;; GLASS. Sand beside a flame, this often, per pass. There is no glass
 ;; on the palette and there is not going to be. It is the only thing in
 ;; this program you have to find out.
 :vitrify 0.02}