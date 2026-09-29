;; ---------------------------------------------------------------------
;; TODO_SETTINGS.CLJS — what was on it to begin with.
;;
;; Change a line and press Run. Break it and the line underneath will
;; tell you what it thought you meant. Nothing you do here can lose the
;; original: `Put it back` is the file as it shipped.
;;
;; THE SEED ONLY LANDS ON AN EMPTY LIST. Editing it will not overwrite
;; anything you have written, and running this with items on the list
;; does nothing to them. Clear the list and press Run and you will get
;; whatever is in here now, which is the only way to get his back once
;; you have taken it off.
;; ---------------------------------------------------------------------

{;; What was on it in September. Four of these are his and the last one
 ;; is the one that is never marked done.
 :seed ["hand in set two"
        "ask Hollis what the buckets are for"
        "vapour tables — one copy, Beckwith 2"
        "tell Maya about the fourth one"
        "page 219"]

 ;; How many rows before it starts scrolling rather than growing. The
 ;; widget does not get taller than this and the list does not get
 ;; shorter, which is the arrangement most lists end up in.
 :rows 8

 ;; Ticked things sink to the bottom. Off by default, because a list
 ;; where the done things stay where you put them is a list you can
 ;; still read as a record of the order you did them in.
 ;;
 ;; Turn it on and it becomes a much better to-do list and a much worse
 ;; account of a term.
 :sink-done false

 ;; What the empty box says.
 :placeholder "add a thing"}