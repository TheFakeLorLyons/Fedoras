(ns fedoras.cs-class.slots.slots-ui
  (:require [clojure.string :as str]))

(def settings
  (atom {:strips
         {"CHERRY DELUXE" [:cherry :bell :bar :seven :plum :bell :cherry :bar]
          "TRIPLE CROWN"  [:seven :bell :bell :bar :cherry :bar :plum :bell :cherry :seven]
          "THE LONG SHOT" [:cherry :cherry :bell :bar :seven :plum :bar :bell :cherry :seven :bell :bar]}
         :reel-counts [3 3 3 3 4 5]
         :faces {:cherry "🍒" :bell "🔔" :bar "🍫" :seven "7️⃣" :plum "🍇"}
         :rigged? true
         :batch 100
         :start-money 25
         :stake 1
         :stakes [1 2 5 10]
         :returns 0.76
         :near-points 5
         :free-every 3}))

(defonce st     (atom nil))  ;; the machine currently in front of you
(defonce career (atom nil))  ;; everything that follows you between them

;; ---------------------------------------------------------------------
;; The reel
;;
;; Every machine has its own strip, so which symbol counts as 'one
;; along' is a property of the cabinet you happen to be standing at
;; and not of the game. Nobody walking the floor has any reason to
;; know that, and nothing on any of them says so.
;; ---------------------------------------------------------------------

(defn- index-of
  "Where a symbol first sits on a strip. Written out rather than handed
  to .indexOf, because a strip is a Clojure vector and that method
  belongs to JavaScript arrays -- and because a strip is allowed to
  hold numbers as easily as keywords, which is the whole point of
  there being two of these in the chapter."
  [coll x]
  (first (keep-indexed (fn [i v] (when (= v x) i)) coll)))

(defn- next-along-in [strip s]
  (nth strip (mod (inc (or (index-of strip s) 0)) (count strip))))

(defn- share-in
  "How much of the strip is this symbol. A symbol that appears twice on
  an eight-symbol strip comes up a quarter of the time, and the last
  reel is honest about that even while it is being dishonest about
  everything else."
  [strip s]
  (/ (count (filter #{s} strip)) (count strip)))

(defn- strip [] (:strip @st))
(defn- pick [] (rand-nth (strip)))
(defn- next-along [s] (next-along-in (strip) s))
(defn- share [s] (share-in (strip) s))

;; ---------------------------------------------------------------------
;; The last one
;;
;; Every reel but the last lands wherever it lands. The last one looks
;; at them first.
;;
;; When they disagree there is nothing to arrange, so it does not
;; bother, and the pick is honest. When they all agree, it pays at
;; exactly the rate the strip says it should -- and when it doesn't
;; pay, it does not land just anywhere. It lands one along. Close
;; enough to see.
;;
;; The rate is not the lie. The rate is real, it is measurable, and it
;; matches an honest machine exactly -- so does how often the front
;; reels match and the last one doesn't. The lie is only ever in where
;; the losing reel stops, and it takes a test somebody had to already
;; suspect to go looking for.
;;
;; :rigged? false takes this out and picks fair every time. The money
;; comes out the same. So does almost everything else.
;; ---------------------------------------------------------------------

(defn- spin []
  (let [n     (:reels @st)
        front (vec (repeatedly (dec n) pick))
        a     (first front)
        last-one (if (and (:rigged? @settings) (apply = front))
                   (if (< (rand) (share a)) a (next-along a))
                   (pick))]
    (conj front last-one)))

;; ---------------------------------------------------------------------
;; Counting
;; ---------------------------------------------------------------------

(defn- paid? [row] (apply = row))

(defn- one-off?
  "Every reel but the last showing the same thing, and the last one
  stopped exactly one position along the strip from them.

  Not merely 'the front reels match and the last doesn't', which is
  the obvious way to write this and measures nothing: a fair machine
  and a rigged one produce that at the same rate, because the rig pays
  at exactly the rate an honest pick would have paid. Every number a
  regulator would think to check comes out identical.

  Where they differ is where the losing reel stops, and only this
  stricter test can see it. Once in a while on a fair machine, since
  it has to happen by chance. Every single near miss on a rigged one."
  [row]
  (let [front (butlast row)
        a     (first front)
        c     (last row)]
    (and (apply = front) (not= c a) (= c (next-along a)))))

(defn- round-nice
  "Two significant figures, because a pay table with 48.64 on it has
  never existed and never will."
  [x]
  (if (< x 10)
    (max 1 (js/Math.round x))
    (let [mag (js/Math.pow 10 (- (js/Math.floor (/ (js/Math.log x) (js/Math.log 10))) 1))]
      (* mag (js/Math.round (/ x mag))))))

(defn- pay-table
  "Derived from the strip, not written down anywhere.

  A machine is not designed by choosing what the sevens pay. It is
  designed by choosing what fraction of the money goes back out --
  :returns, and seventy-six cents is a hard machine; the real ones sit
  nearer ninety and have to say so somewhere -- and then solving for
  what each line must pay to hit exactly that number.

  Which is why this comes out different on every cabinet on the floor,
  and why a symbol paying ten times more than another one tells you
  precisely nothing about the machine except how rare that symbol is
  on this particular strip."
  [strip reels]
  (let [n (count strip)
        returns (:returns @settings)]
    (into {}
          (map (fn [s]
                 (let [p (share-in strip s)]
                   [s (round-nice (/ returns (js/Math.pow p (dec reels))))]))
               (distinct strip)))))

(defn- payout-for [s stake]
  (* stake (get (:pays @st) s 0)))

;; ---------------------------------------------------------------------
;; The floor
;;
;; Money is a property of the machine you are at. Points, spins, and
;; every number underneath them are properties of you, and they do not
;; reset when you stand up, because you don't either.
;;
;; A new machine is a new strip, possibly a different number of reels,
;; and — if you had nothing left at the last one — another twenty-five
;; dollars into the slot, which the floor counts even if you have
;; stopped.
;; ---------------------------------------------------------------------

(defn- roll-machine []
  (let [[nm strip] (rand-nth (vec (:strips @settings)))
        reels (rand-nth (:reel-counts @settings))
        strip (vec strip)
        a     (first strip)]
    {:name nm
     :strip strip
     :reels reels
     :pays (pay-table strip reels)
     ;; a near miss on the glass before anybody has touched it. it has
     ;; not been spun. it is just what the machine would like you to
     ;; think it does.
     :row (conj (vec (repeat (dec reels) a)) (next-along-in strip a))
     :stake (:stake @settings)
     :money (:start-money @settings)
     :machine-points 0
     :near-run 0 :free-spins 0 :last-win 0 :broke? false}))

(defn- fresh-career []
  {:points 0 :spins 0 :pays 0 :one-off 0
   :machines (or (fedoras.ui.widget-ui/kept :slots-machines) 1)
   :fed-in (:start-money @settings)
   :best (or (fedoras.ui.widget-ui/kept :slots-best) 0)})

(defn- can-spin? [s]
  (or (pos? (:free-spins s)) (>= (:money s) (:stake s))))

(defn- take-spin! []
  (when (can-spin? @st)
    (let [free?      (pos? (:free-spins @st))
          stake      (:stake @st)
          row        (spin)
          won?       (paid? row)
          near?      (one-off? row)
          win-amount (if won? (payout-for (first row) stake) 0)
          gained     (+ 1 (if near? (:near-points @settings) 0) win-amount)
          every      (:free-every @settings)]
      (swap! st
             (fn [s]
               (let [near-run (if near? (inc (:near-run s)) (:near-run s))
                     earned?  (and near? (pos? every) (>= near-run every))
                     s' (assoc s
                               :row row
                               :last-win win-amount
                               :near-run (if earned? 0 near-run)
                               :money (+ (:money s) (if free? 0 (- stake)) win-amount)
                               :free-spins (max 0 (+ (if free?
                                                       (dec (:free-spins s))
                                                       (:free-spins s))
                                                     (if earned? 1 0)))
                               :machine-points (+ (:machine-points s) gained))]
                 (assoc s' :broke? (not (can-spin? s'))))))
      (swap! career
             (fn [c]
               (-> c
                   (update :spins inc)
                   (update :points + gained)
                   (update :pays (fn [n] (if won? (inc n) n)))
                   (update :one-off (fn [n] (if near? (inc n) n)))))))))

(defn- remember-best! []
  (let [p (:machine-points @st)]
    (when (> p (:best @career))
      (swap! career assoc :best p)
      (fedoras.ui.widget-ui/keep! :slots-best p))))

(defn- new-machine!
  "Stand up, walk to another one. Everything you have counted so far
  comes with you. If there was nothing left in the last one, another
  twenty-five goes in, and that gets counted too."
  []
  (let [broke? (:broke? @st)
        left   (:money @st)]
    (swap! career (fn [c] (-> c
                              (update :machines inc)
                              (update :fed-in + (if broke? (:start-money @settings) 0)))))
    (fedoras.ui.widget-ui/keep! :slots-machines (:machines @career))
    (reset! st (roll-machine))
    ;; you take your money with you; you only feed the slot again when
    ;; there was nothing to take
    (when-not broke?
      (swap! st assoc :money left))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defn- face
  "The emoji for a symbol, or the symbol itself when there isn't one --
  which is how a strip of plain numbers still runs on the same floor
  without needing anything of its own."
  [s]
  (or (get (:faces @settings) s) (str s)))

(defn- pct [n d]
  (if (zero? d) "—" (str (.toFixed (* 100 (/ n d)) 1) "%")))

(defn paint! [node]
  (let [{:keys [row money stake free-spins last-win broke? name reels]} @st
        {:keys [points spins pays one-off machines fed-in best]} @career]
    (fedoras.reader/text!
     node ".slots-row"
     (str/join " " (map face row)))

    (fedoras.reader/text!
     node ".slots-purse"
     (str "$" money " · " points " points"))

    (fedoras.reader/text!
     node ".slots-career"
     (str name " · " reels " reels · machine " machines " · $" fed-in " in"
          (when (pos? best) (str " · best machine " best))))

    (fedoras.reader/text!
     node ".slots-tally"
     (str spins " spins · " pays " paid · " one-off " one short"
          (when (pos? free-spins) (str " · " free-spins " free"))
          (when (pos? last-win) (str " · won $" last-win))))

    (fedoras.reader/text!
     node ".slots-rates"
     (cond
       broke?        "nothing left in this one · there are others"
       (zero? spins) "nothing spun yet"
       :else (str "pays " (pct pays spins) " · one short " (pct one-off spins))))

    ;; the buttons say what they will actually do, since the stake and
    ;; the batch size are both settings and a label that lies about
    ;; either is worse than no label
    (fedoras.reader/text! node ".slots-go" (str "Spin $" stake))
    (fedoras.reader/text! node ".slots-batch" (str "Spin " (:batch @settings)))
    (fedoras.reader/text! node ".slots-stake" (str "Stake $" stake " ▸"))))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defn- spin-once! []
  (take-spin!)
  (remember-best!))

(defn- spin-batch!
  "Stops the moment there is nothing left in this machine, rather than
  running the remaining ninety spins against an empty one."
  []
  (dotimes [_ (:batch @settings)]
    (when-not (:broke? @st) (take-spin!)))
  (remember-best!))

(defn- cycle-stake!
  "Steps through the stakes in order and wraps. Raising it raises what
  a win pays by exactly the same factor, so the odds do not move at
  all -- only how fast you find out."
  []
  (swap! st (fn [s]
              (let [ss (:stakes @settings)
                    i  (or (index-of ss (:stake s)) 0)]
                (assoc s :stake (nth ss (mod (inc i) (count ss))))))))

(defn- wire-controls! [node]
  (fedoras.reader/on-click! node ".slots-go"
                            (fn [_] (spin-once!) (paint! node)))
  (fedoras.reader/on-click! node ".slots-batch"
                            (fn [_] (spin-batch!) (paint! node)))
  (fedoras.reader/on-click! node ".slots-stake"
                            (fn [_] (cycle-stake!) (paint! node)))
  (fedoras.reader/on-click! node ".slots-next"
                            (fn [_] (new-machine!) (paint! node)))
  ;; the only thing on this widget that actually forgets anything
  (fedoras.reader/on-click! node ".slots-walk"
                            (fn [_]
                              (fedoras.ui.widget-ui/keep! :slots-machines 1)
                              (reset! career (assoc (fresh-career) :machines 1))
                              (reset! st (roll-machine))
                              (paint! node))))

(defn- wire-settings! [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting]
     (swap! settings
            (fn [current]
              (merge current
                     (select-keys setting
                                  [:strips :reel-counts :faces :rigged? :batch
                                   :start-money :stake :stakes :returns
                                   :near-points :free-every]))))
     (reset! career (fresh-career))
     (reset! st (roll-machine))
     (paint! node))))

(defn setup! [node]
  (reset! career (fresh-career))
  (reset! st (roll-machine))
  (fedoras.ui.widget-ui/wire! node "controls" wire-controls!)
  (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n (.-id node))))
  (paint! node))

(defn mount! [{:keys [id]}]
  (fedoras.ui.widget-ui/mount! ::slots (or id "slots") setup! paint!))