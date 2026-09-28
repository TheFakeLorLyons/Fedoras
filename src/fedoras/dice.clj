(ns fedoras.dice
  "A d20 the reader can actually roll, and a rewrite button that costs
  something.

  Every check has an id. Rolling stores the result. The
  reader may rewrite any result they like; that is the whole point of
  the button. Each rewrite raises the Echo by one, and the Echo is
  a running penalty on every check afterwards, in this scene and every
  scene after it, for as long as the reader keeps their browser.

  So the button is free the first time, cheap the second time, and by
  the fifth rewrite the reader is playing a worse game than the one they
  started with, and nothing has warned them except the number going up.

  Nobody in the book ever explains this. The campaign Victor is running
  is about exactly this and neither he nor anybody at the table
  notices.

  WHAT A PASSAGE MAY SAY. Outcome strings are rendered with the die in
  scope, so they can name the number that was actually rolled:

    {result}       17
    {result-word}  seventeen
    {result-mod}   17 + 1 = 18   (just `17` when standing is nought)
    {total}        18
    {total-word}   eighteen
    {mod}          +1            (empty when standing is nought)
    {dc}           13

  plus everything a scene has: {pc}, {pc-full}, {pc-last}, {ac},
  {ac-kind}, {ac-plural}, {ac-many}, {ac-other}. Case follows the token,
  so {Result-word} starts a sentence and {AC} writes a cue line.

  `@VICTOR` still works. `@:registrar` is looked up in
  `fedoras.cast` and comes out as `@REGISTRAR [VICTOR]`, so a roll
  passage names its speakers the same way a scene does and gets the same
  spelling.

  In the print edition, the widget is an extra. Every scene that uses it also
  states, in the screenplay, what the table actually rolled, so the PDF
  reads straight through."
  (:require [clojure.string :as str]
            [fedoras.cast :as cast]
            [fedoras.state :as state]
            [scicloj.kindly.v4.kind :as kind]))

(defn assets
  "Call once in any chapter that rolls. Ships the runtime for every roll
  box on the page.

  The file is `fedoras/ui/dice.cljs` and the namespace inside it is
  `fedoras.ui.dice-ui`. Those disagree on purpose -- the path is a path
  and the namespace avoids colliding with this file -- and `mount!`
  below has to be called by the namespace, not by the path. It was
  called by the path, which resolved to nothing, which is why the roll
  boxes did nothing at all: `state/call` logs `Calling ...` before it
  evaluates, so the console showed the call being made and then the
  failure, and the first line is the one you notice."
  []
  (state/source "fedoras/ui/dice_ui.cljs"))

(defn check
  [{:keys [id label dc fumble fail success crit]}]
  (kind/hiccup
   [:div
    [:div.roll {:id (str "roll-" id)}
     [:div.roll-head
      [:span.roll-label (str/upper-case (or label "CHECK"))]
      [:span.roll-dc (str "DC " dc)]]
     [:div.roll-bar
      [:button.roll-go "Roll d20"]
      [:span.roll-value]]
     [:p.roll-out]
     [:div.roll-bar
      [:button.roll-rewrite {:style {:display "none"}} "Rewrite history"]
      [:span.roll-echo]]
     [:p.roll-note]
     [:p.roll-foot
      "Echo: " [:span.echo-count "0"]
      "   ·   Score: " [:span.score-count "0"]]]
    (state/call 'fedoras.ui.dice-ui/mount!
                {:id id :dc dc
                 :outcomes {:fumble  (cast/expand (or fumble fail))
                            :fail    (cast/expand fail)
                            :success (cast/expand success)
                            :crit    (cast/expand (or crit success))}})]))