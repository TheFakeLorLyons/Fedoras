;; ---------------------------------------------------------------------
;; TREE_SETTINGS.CLJS — what the rule is told.
;;
;; Change a number and press Run. Break it and the line underneath will
;; tell you what it thought you meant. Nothing you do here can lose the
;; original: `Put it back` is the file as it shipped.
;;
;; None of these are in the rule. The rule is four lines long and does
;; not mention trees, ferns, spirals, colour, or how big any of this is
;; going to be. Everything below is something the rule was handed.
;; ---------------------------------------------------------------------

{;; ---------------------------------------------------------------
 ;; WHICH ONE
 ;;
 ;; Every shape below comes out of the same four lines. The only
 ;; thing that differs is the list each one hands over: how far each
 ;; new line turns off the one it came from, and how much of it it
 ;; is.
 ;;
 ;;   :tree      — two, even, both a bit shorter. The original.
 ;;   :fern      — a spine that barely turns and keeps most of its
 ;;                length, with two short branches off it. Twenty
 ;;                deep before the screen runs out.
 ;;   :vine      — one child. It cannot branch, so it cannot be a
 ;;                tree, and it turns the same way every time and is
 ;;                a spiral, which nobody wrote down anywhere.
 ;;   :crook     — two, lopsided: one carries on, one goes off hard
 ;;                and gives up early.
 ;;   :trie      — three, close together, all short.
 ;;   :mirror    — two trunks, opposite ways, so it grows out of both
 ;;                ends of itself instead of standing on one.
 ;;   :snowflake — six trunks off one point, which is a hexagon
 ;;                before it is anything else.
 ;;   :rosette   — eight trunks, each of them a vine. The whole thing
 ;;                turns without anything in it turning.
 ;;   :levy      — turns of exactly half a right angle, each child
 ;;                one over root two of its parent. That is the Levy
 ;;                C curve, which somebody found in 1938, and it is
 ;;                not a tree in any sense: it folds in on itself
 ;;                into a solid block with a coastline.
 ;;   :comb      — a spine going almost straight on, with teeth off
 ;;                one side only. Nothing anywhere says the children
 ;;                have to be arranged around anything.
 ;;
 ;; :trunks is the second thing that generalised. The rule has not
 ;; been told about hexagons and never will be. It only gets asked
 ;; the same question from six directions at once, and a hexagon is
 ;; what that looks like from outside.
 ;;
 ;; Add your own. Any list of turns and ratios works. A ratio at or
 ;; above 1 means a child that never gets smaller and a drawing that
 ;; never finishes, so it is quietly held to 0.98.
 ;; ---------------------------------------------------------------
 :pattern :tree

 :patterns
 {:tree      {:children [{:turn -0.42 :ratio 0.72} {:turn 0.42 :ratio 0.72}]}
  :fern      {:children [{:turn 0.03 :ratio 0.87}
                        {:turn -0.65 :ratio 0.42}
                        {:turn 0.65 :ratio 0.42}]}
  :vine      {:children [{:turn 0.22 :ratio 0.94}]}
  :crook     {:children [{:turn -0.24 :ratio 0.79} {:turn 0.62 :ratio 0.55}]}
  :trie      {:children [{:turn -0.52 :ratio 0.58}
                         {:turn 0 :ratio 0.63}
                         {:turn 0.52 :ratio 0.58}]}
  :mirror    {:trunks 2
              :children [{:turn 0.05 :ratio 0.84}
                         {:turn -0.72 :ratio 0.44}
                         {:turn 0.72 :ratio 0.44}]}
  :snowflake {:trunks 6
              :children [{:turn -0.55 :ratio 0.56} {:turn 0.55 :ratio 0.56}]}
  :rosette   {:trunks 8
              :children [{:turn 0.3 :ratio 0.88}]}
  :levy      {:children [{:turn -0.7854 :ratio 0.7071}
                         {:turn 0.7854 :ratio 0.7071}]}
  :comb      {:children [{:turn 0 :ratio 0.93} {:turn 1.45 :ratio 0.3}]}}

 ;; ---------------------------------------------------------------
 ;; THE TWO DIALS
 ;;
 ;; These work on whichever pattern is loaded, because they are
 ;; multipliers rather than values. :spread-scale 2 doubles every
 ;; turn in the list; 0 flattens all of them and everything grows in
 ;; one straight line. :ratio-scale 1.1 makes every child keep more
 ;; of its parent, which makes the whole thing very much larger and
 ;; costs nothing, because the frame is fitted afterwards.
 ;;
 ;; :lean is added to every turn in the same direction, so the whole
 ;; thing curls. 0 is upright. 0.08 is a tree on a hill. On :vine it
 ;; is the difference between a spiral and a wider spiral.
 ;;
 ;; :jitter is how far each turn is allowed to wander off its mark,
 ;; drawn fresh every time. 0 is the machine drawing. 0.15 is
 ;; something that grew, and `Draw it again` never gives you the same
 ;; one twice.
 ;; ---------------------------------------------------------------
 :spread-scale 1
 :ratio-scale  1
 :lean         0
 :jitter       0
 :length       26

 ;; ---------------------------------------------------------------
 ;; THE POINTER
 ;;
 ;; Moving the mouse over it bends the rule while you are there:
 ;; left and right lean it, up and down open and close it. Nothing
 ;; is being pushed around — every line is regrown from a rule that
 ;; has been asked a slightly different question, sixty times a
 ;; second.
 ;;
 ;; Which is why it goes coarse while you do it. :sway-min-px is the
 ;; cutoff used only while the pointer is driving, because eight
 ;; thousand lines rebuilt every frame is not something a page is
 ;; going to manage; at 5 it is nearer two thousand, and the fine
 ;; version comes back the moment you leave.
 ;;
 ;; :sway 0 turns it off and the mouse only drags and zooms.
 ;; ---------------------------------------------------------------
 :sway        0.5
 :sway-min-px 5

 ;; ---------------------------------------------------------------
 ;; WHERE IT STOPS
 ;;
 ;; There is no depth here, because there is no depth in the rule.
 ;; It stops where a drawing stops: when the next line would be
 ;; thinner than :min-px pixels, and when everything still to come
 ;; from a point is somewhere the frame is not.
 ;;
 ;; Both of those are facts about your screen. Neither is a fact
 ;; about the rule. Which is why you can scroll into it and find
 ;; more, and why you will not find the bottom: every time you zoom
 ;; it throws away what has gone off the edges and grows what has
 ;; become large enough to see, so the count stays about the same
 ;; however far in you go.
 ;;
 ;; :min-px is the one number here that costs real time, and it does
 ;; not cost it smoothly. Every line is a fixed fraction of its
 ;; parent, so the whole tree gains or loses an entire generation at
 ;; once, and a generation is as many lines as everything above it
 ;; put together:
 ;;
 ;;   min-px 2.0 — 16383 lines
 ;;   min-px 2.5 —  8191
 ;;   min-px 3.5 —  4095   (this)
 ;;   min-px 5.0 —  2047
 ;;
 ;; The walk visits about twice those numbers, since it has to reach
 ;; a line before it can decide the line is too small to keep. At
 ;; 3.5 that is eight thousand visits, and every one of them is
 ;; interpreted in the page rather than compiled, which is the
 ;; difference between this appearing at once and appearing after a
 ;; pause.
 ;;
 ;; It matters less than it looks like it should, because the detail
 ;; you gave up is not gone. Scroll into it and it grows back.
 ;;
 ;; :max-depth and :max-lines are not part of any of that. They are
 ;; there so a mistake in the numbers above stops eventually instead
 ;; of taking the page with it. :vine legitimately goes sixty-six
 ;; deep, which is why the depth guard is not a small number.
 ;; ---------------------------------------------------------------
 :min-px    3.5
 :zoom-step 1.6
 :max-depth 150
 :max-lines 60000

 ;; Prints how long the fit and the growth took to the browser
 ;; console, which is the only way to find out rather than guess.
 ;; Set it false once you have stopped caring.
 :log-timing? true

 ;; ---------------------------------------------------------------
 ;; THE DRAWING OF IT
 ;;
 ;; :draw-seconds is how long the whole drawing takes, however many
 ;; lines that turns out to be. Set it to 30 and watch it go all the
 ;; way down one side before it comes back for the other, which is
 ;; not a decision anybody made about drawing. It is only the order
 ;; the rule reaches them in, and it is the same order every time.
 ;;
 ;; Zooming does not animate. It puts the new lines up at once,
 ;; because waiting four seconds after every notch of the wheel is
 ;; not exploring anything.
 ;;
 ;; :taper is how much thicker the trunk is than the tips, and
 ;; :tip-fade is how much of the page shows through at the far end.
 ;; The colour goes from --tree-trunk to --tree-tip, both in the
 ;; stylesheet — but not evenly, and :colour-gamma is why.
 ;;
 ;; Half of every one of these is at its deepest level. A quarter is
 ;; one above that. Seven eighths of all the lines are in the last
 ;; three depths, so a colour spread evenly across the depths spends
 ;; nearly all of itself on the four lines nearest the trunk, and
 ;; hands the eight thousand you can actually see a seventeenth of
 ;; the range, which reads as one flat colour and looks like the
 ;; feature is broken.
 ;;
 ;; 3.5 holds the trunk colour further out and puts the change where
 ;; the lines actually are. Set it to 1 for the even version and see
 ;; the problem. Higher pushes the whole gradient into the outermost
 ;; twigs.
 ;;
 ;; The rule does not know about any of the three. It does not know
 ;; how deep it is even while it is being it — the depth is
 ;; something counted from outside while it worked, and everything
 ;; here is painted on afterwards by someone who wanted it to look
 ;; like a tree.
 ;; ---------------------------------------------------------------
 :draw-seconds 4
 :taper        2.2
 :tip-fade     0.78
 :colour-gamma 3.5}