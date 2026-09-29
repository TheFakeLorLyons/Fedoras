;; ---------------------------------------------------------------------
;; PUZZLE_SETTINGS.CLJS — the board it starts on.
;;
;; Change something and press Run. Break it and the line under the board
;; will tell you what it thought you meant. Nothing you do here can lose
;; the original: `Put it back` is the file as it shipped.
;; ---------------------------------------------------------------------

{;; ---------------------------------------------------------------
 ;; THE BOARD
 ;;
 ;; nil and it shuffles one. Otherwise sixteen entries: the numbers one
 ;; to fifteen in whatever order you like, and nil for the hole.
 ;;
 ;; HALF OF ALL ARRANGEMENTS CANNOT BE GOT INTO ORDER by sliding, and
 ;; there is nothing about looking at one that tells you which half it
 ;; is in. Put one of those in here and press Solve and it will say so,
 ;; which is four lines of arithmetic and is the only thing in this
 ;; program that knows something the board does not.
 ;;
 ;; This one can be done in six:
 ;;
 ;;   [1 2 3 4  5 6 7 8  9 10 11 12  13 nil 14 15]
 ;;
 ;; This one cannot be done at all, and is the same as order with two
 ;; tiles swapped:
 ;;
 ;;   [2 1 3 4  5 6 7 8  9 10 11 12  13 14 15 nil]
 ;; ---------------------------------------------------------------
 :board nil

 ;; How far it walks away from order when it shuffles. Two hundred is
 ;; as scrambled as it gets — past about eighty there is no further to
 ;; go and the numbers stop meaning anything.
 ;;
 ;; It walks rather than deals, so a shuffled board can always be got
 ;; back. A dealt one is a coin toss.
 :scramble 200

 ;; Milliseconds between moves while it solves itself. 90 is watchable.
 ;; 0 is instant and much less interesting.
 :speed 90

 ;; ---------------------------------------------------------------
 ;; THE VERDICT
 ;;
 ;; The Registrar's language, applied to a sliding puzzle by a man who
 ;; has been marked in it all term. Under the first number is the first
 ;; word, under the second is the second, and past that is :beyond.
 ;;
 ;; The solver does not get a verdict. It is not being reviewed.
 ;; ---------------------------------------------------------------
 :bands  [[65 "no action"] [80 "review"]]
 :beyond "repossessed"}