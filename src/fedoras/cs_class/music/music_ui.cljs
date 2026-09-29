(ns fedoras.cs-class.music.music-ui
  "A piano roll with the timings on the horizontal, which is what it
  always was.

  Columns are sixteenths and rows are degrees of a scale, so a reader who
  clicks at random gets something that sounds deliberate. That is a
  decision about scales and not about the reader: the instrument is
  built so that it is difficult to be wrong on it, which is worth
  remembering when the same afternoon is loaded into it.

  THE SEAT. `seat` is the gaps between one person's keystrokes across an
  afternoon, in milliseconds, in the order they happened. Loading it
  places one note per keystroke, pitched by how fast the gap was and
  positioned by when it fell, so the roll is that afternoon at its own
  tempo. Two of the gaps are minutes long and they are the two places
  where somebody stopped.

  THE BAND is 60 to 400 milliseconds. Loading the seat through it drops
  every gap outside, which is every gap that was not typing, and the
  afternoon comes out a third of the length with nothing missing that
  anybody would call missing. Both rolls are on the same grid at the
  same tempo. The difference is the amount of paper.

  Audio is scheduled ahead of the clock rather than fired from a timer,
  because a JavaScript timer is not accurate enough to sound like music.
  A tick every 25ms schedules whatever falls in the next 100ms, and the
  audio clock does the rest."
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; The roll
;; ---------------------------------------------------------------------

(def max-steps 96)

(def default-roll
  {:steps 32
   :rows 15
   :scale :pentatonic
   :key :a
   :octave 3
   :spell :auto
   :root nil
   :bpm 120
   :band [60 400]
   :below 1
   :voice :bell
   :looping true
   :seed 4471
   :density 0.42
   :voices {}
   :phrase [1.0]
   :notes {}
   :note "Press Run above. Then click the grid, or press Fill."})

(defn step-seconds
  "One column, in seconds. A column is a sixteenth."
  [state]
  (/ 15.0 (:bpm state)))

(defn step-millis [state]
  (* 1000 (step-seconds state)))

(defn notes-at [state step]
  (keep (fn [[[s row] voice-id]] (when (= s step) [row voice-id]))
        (:notes state)))

(defn put-note [state step row voice-id]
  (assoc-in state [:notes [step row]] voice-id))

(defn erase-note [state step row]
  (update state :notes dissoc [step row]))

(defn clear-notes [state]
  (assoc state :notes {} :note "Cleared."))

(defn trim-to
  "Drop anything past the end of the roll, so shortening it does not
  leave notes sounding from a column nobody can see."
  [state]
  (update state :notes
          (fn [notes]
            (into {} (filter (fn [[[s row] _]]
                               (and (< s (:steps state)) (< row (:rows state))))
                             notes)))))

;; ---------------------------------------------------------------------
;; Pitch. No DOM and no audio in this section.
;; ---------------------------------------------------------------------

(def scales
  "Scales as the intervals between their degrees, which is the way a
  musician would say them, and which means the degrees themselves are
  computed rather than transcribed."
  [{:id :pentatonic :label "pentatonic" :steps [2 2 3 2 3]}
   {:id :major      :label "major"      :steps [2 2 1 2 2 2 1]}
   {:id :minor      :label "minor"      :steps [2 1 2 2 1 2 2]}
   {:id :whole      :label "whole tone" :steps [2 2 2 2 2 2]}
   {:id :chromatic  :label "chromatic"  :steps [1 1 1 1 1 1 1 1 1 1 1 1]}])

(def semitone-of
  "Every spelling the settings file accepts, in semitones above C.
  Sharps and flats both, because a reader in E flat should be able to
  write `:eb` and a reader in F sharp should be able to write `:f#`."
  {:c 0
   :c# 1  :db 1
   :d 2
   :d# 3  :eb 3
   :e 4
   :f 5
   :f# 6  :gb 6
   :g 7
   :g# 8  :ab 8
   :a 9
   :a# 10 :bb 10
   :b 11})

(def canonical-keys
  "One id per semitone. Whatever a reader writes is normalised to these,
  so the menu and the state always agree about which key is chosen."
  [:c :c# :d :d# :e :f :f# :g :g# :a :a# :b])

(def key-order
  "The keys, in the order the menu shows them. A first, because A is
  where this instrument started and 220 hertz is still its default."
  [:a :a# :b :c :c# :d :d# :e :f :f# :g :g#])

(def key-labels
  "Both spellings on the black keys, because a key menu that has decided
  for you is a key menu you have to translate."
  {:a "A"  :a# "A♯/B♭" :b "B"  :c "C"  :c# "C♯/D♭" :d "D"
   :d# "D♯/E♭" :e "E"  :f "F"  :f# "F♯/G♭" :g "G"  :g# "G♯/A♭"})

(def sharp-names ["C" "C♯" "D" "D♯" "E" "F" "F♯" "G" "G♯" "A" "A♯" "B"])

(def flat-names  ["C" "D♭" "D" "E♭" "E" "F" "G♭" "G" "A♭" "A" "B♭" "B"])

(def spells-flat
  "The keys written with flats, by semitone: D♭, E♭, F, A♭, B♭. The rest
  take sharps. F sharp and G flat are six of each and the convention
  here is F sharp.

  This is major-key convention applied to every scale, which is a
  simplification: a minor scale follows its relative major's signature
  and mostly agrees, and whole tone cannot be spelled correctly by any
  rule. Set `:spell` to override it."
  #{1 3 5 8 10})

(defn normalise-key
  "Any accepted spelling to the one id the rest of the program uses. An
  unknown name falls back to A."
  [k]
  (nth canonical-keys (get semitone-of k 9)))

(defn key-semitone [state]
  (get semitone-of (:key state) 9))

(defn scale-spec [id]
  (or (first (filter (fn [s] (= id (:id s))) scales))
      (first scales)))

(defn pitch-number
  "Semitones above C0, from a frequency. Anchored on 440 Hz, which is A4,
  which is the fifty-seventh semitone counting C0 as nothing.

  Derived from the frequency rather than from the row so that a reader
  who moves `:root` gets labels that follow it, including to roots that
  are not a note at all, which round to the nearest one and say so by
  being slightly wrong about the tuning and right about the letter."
  [freq]
  (+ 57 (js/Math.round (* 12 (/ (js/Math.log (/ freq 440))
                                (js/Math.log 2))))))

(defn scale-spec [id]
  (or (first (filter (fn [s] (= id (:id s))) scales))
      (first scales)))

(defn degrees
  "Semitones above the root, one per degree, from the interval pattern."
  [spec]
  (vec (butlast (reductions + 0 (:steps spec)))))

(defn spelling
  "Sharps or flats, from the setting or from the key."
  [state]
  (case (:spell state)
    :sharp :sharp
    :flat :flat
    (if (contains? spells-flat (key-semitone state)) :flat :sharp)))

(defn pitch-names [state]
  (if (= :flat (spelling state)) flat-names sharp-names))

(defn root-frequency
  "The bottom row of the grid, in hertz.

  `:root` overrides the key outright, which is the way in for a reader
  who wants a root that is not a note. Everything downstream is ratios,
  so an instrument tuned to 217 hertz is in tune with itself and with
  nothing else, and the labels will name the nearest letter and be
  quietly wrong about it."
  [state]
  (or (:root state)
      (let [number (+ (* 12 (:octave state)) (key-semitone state))]
        (* 440 (js/Math.pow 2 (/ (- number 57) 12))))))

(defn scale-size [state]
  (count (degrees (scale-spec (:scale state)))))

(defn root-row
  "Which row of the grid is the root. `:below` is in octaves, so a
  pentatonic grid with one octave below the root has five rows under it
  and a chromatic grid has twelve, which is what a musician means by an
  octave below and not what a fixed row count would give them."
  [state]
  (* (max 0 (:below state)) (scale-size state)))

(defn semitone
  "Semitones from the root, which may be negative. Row `(root-row state)`
  is the root, rows under it are the octaves below, and rows above carry
  on up as far as the grid goes."
  [state row]
  (let [ds (degrees (scale-spec (:scale state)))
        n (count ds)
        from-root (- row (root-row state))]
    (+ (nth ds (mod from-root n))
       (* 12 (js/Math.floor (/ from-root n))))))

(defn root-row?
  "True on every row where the pattern starts again, which for every
  scale here is an octave, counted from the root rather than from the
  bottom of the grid."
  [state row]
  (zero? (mod (- row (root-row state)) (scale-size state))))

(defn frequency-of [state row]
  (* (root-frequency state) (js/Math.pow 2 (/ (semitone state row) 12))))

(defn pitch-number
  "Semitones above C0, from a frequency. Anchored on 440 hertz, which is
  A4, which is the fifty-seventh semitone counting C0 as nothing.

  Derived from the frequency rather than from the row, so that whatever
  moves the root moves the labels with it."
  [freq]
  (+ 57 (js/Math.round (* 12 (/ (js/Math.log (/ freq 440))
                                (js/Math.log 2))))))

(defn pitch-at
  "The letter and octave for one row of the grid."
  [state row]
  (let [number (pitch-number (frequency-of state row))]
    {:pitch (nth (pitch-names state) (mod number 12))
     :octave (quot number 12)}))

;; ---------------------------------------------------------------------
;; Storage
;;
;; `fedoras.reader` stores through JSON, which has no vector keys, so
;; the notes travel as [column row voice] triples and are read back into
;; a map on the other side.
;; ---------------------------------------------------------------------

(def storage-key :music)

(defn to-storage
  [state]
  {:steps (:steps state)
   :rows (:rows state)
   :bpm (:bpm state)
   :scale (name (:scale state))
   :key (name (:key state))
   :octave (:octave state)
   :spell (name (:spell state))
   :root (:root state)
   :notes (mapv (fn [[[step row] voice-id]] [step row (name voice-id)])
                (:notes state))})

(defn from-storage
  [state saved]
  (if-not (map? saved)
    (assoc state :note "There is nothing kept.")
    (-> state
        (assoc :steps (or (:steps saved) (:steps state))
               :rows (or (:rows saved) (:rows state))
               :bpm (or (:bpm saved) (:bpm state))
               :scale (if (:scale saved) (keyword (:scale saved)) (:scale state))
               :key (if (:key saved) (normalise-key (keyword (:key saved))) (:key state))
               :octave (or (:octave saved) (:octave state))
               :spell (if (:spell saved) (keyword (:spell saved)) (:spell state))
               :root (:root saved)
               :notes (into {} (map (fn [[step row voice]]
                                      [[step row] (keyword voice)])
                                    (:notes saved)))
               :note "Restored.")
        trim-to)))

(defn keep!
  [state]
  (fedoras.reader/store! storage-key (to-storage state))
  (assoc state :note "Kept. It will be here next time you open the chapter."))

(defn as-text
  "The roll as something a reader can put in a file and read later."
  [state]
  (str ";; " (count (:notes state)) " notes, "
       (:steps state) " columns, " (:bpm state) " bpm, "
       (get key-labels (:key state)) " " (name (:scale state))
       ", seed " (:seed state) "\n"
       (pr-str (to-storage state)) "\n"))

;; ---------------------------------------------------------------------
;; Voices
;; ---------------------------------------------------------------------

(def voices
  [{:id :bell   :label "bell"   :wave "sine"     :attack 0.004 :length 0.90 :gain 0.30}
   {:id :flute  :label "flute"  :wave "triangle" :attack 0.045 :length 0.50 :gain 0.26}
   {:id :reed   :label "reed"   :wave "square"   :attack 0.015 :length 0.34 :gain 0.15
    :filter "lowpass" :cutoff 2200}
   {:id :string :label "string" :wave "sawtooth" :attack 0.080 :length 0.70 :gain 0.15
    :filter "lowpass" :cutoff 1700}
   {:id :block  :label "block"  :noise true      :attack 0.002 :length 0.13 :gain 0.55
    :filter "bandpass"}])

(defn voice-spec [id]
  (or (first (filter (fn [v] (= id (:id v))) voices))
      (first voices)))

;; ---------------------------------------------------------------------
;; A predictable die
;; ---------------------------------------------------------------------

(defonce seed-a (atom 1))

(defn- seed! [n]
  (reset! seed-a (bit-or 1 (bit-and n 0x7fffffff))))

(defn- rnd []
  (let [x @seed-a
        x (bit-and (bit-xor x (bit-shift-left x 13)) 0x7fffffff)
        x (bit-and (bit-xor x (unsigned-bit-shift-right x 17)) 0x7fffffff)
        x (bit-and (bit-xor x (bit-shift-left x 5)) 0x7fffffff)]
    (reset! seed-a x)
    (/ x 2147483647.0)))

;; ---------------------------------------------------------------------
;; Filling the paper
;;
;; Each voice walks its own register from left to right, stepping by
;; small intervals and leaping rarely, which is the whole of what makes
;; a random sequence of notes read as a line rather than as noise. The
;; registers do not overlap much, so the voices do not fight for the
;; same rows, and the phrase weights vary the density bar by bar, which
;; is where the sense of rhythm comes from.
;; ---------------------------------------------------------------------

(def bar-columns 16)

(defn- clamp [n low high]
  (max low (min high n)))

(defn- bar-weight
  "How busy this bar is. The phrase repeats, so a four-number phrase on a
  thirty-two column roll is heard twice, which is what makes it a phrase
  rather than a shape."
  [state step]
  (let [phrase (:phrase state)]
    (if (seq phrase)
      (nth phrase (mod (quot step bar-columns) (count phrase)))
      1.0)))

(defn- wander
  "The next row for a voice. Mostly a step, sometimes a leap, never out
  of the register. A voice with a leap of zero stays where it is, which
  is how the block ends up being percussion."
  [row [low high] leap]
  (if (zero? leap)
    row
    (let [move (- (js/Math.round (* (rnd) leap 2)) leap)]
      (clamp (+ row move) low high))))

(defn- fill-voice
  [state notes voice-id {:keys [register every weight leap]}]
  (let [[low high] [(clamp (first register) 0 (dec (:rows state)))
                    (clamp (second register) 0 (dec (:rows state)))]
        cadence (max 1 (or every 1))]
    (loop [step 0
           row (quot (+ low high) 2)
           acc notes]
      (if (>= step (:steps state))
        acc
        (let [chance (* (:density state) (or weight 1.0) (bar-weight state step))
              playing (< (rnd) chance)]
          (recur (+ step cadence)
                 (if playing (wander row [low high] (or leap 2)) row)
                 (if playing (assoc acc [step row] voice-id) acc)))))))

(defn fill
  "One seed, one piece of music. The voices are taken in sorted order so
  that the same seed gives the same result every time, which a map's own
  iteration order would not guarantee."
  [state]
  (seed! (:seed state))
  (assoc state
         :notes (reduce (fn [acc [voice-id spec]]
                          (fill-voice state acc voice-id spec))
                        {}
                        (sort-by (fn [[voice-id _]] (name voice-id))
                                 (:voices state)))
         :note (str "Filled from seed " (:seed state) ". Change it and fill again.")))

(defn fill-again
  "The next seed along. A reader who likes one writes the number down."
  [state]
  (fill (update state :seed inc)))

(defn from-settings
  "Take whatever the settings panel returned and keep the parts of it
  that are keys this instrument has. A reader who deletes a key gets the
  value that was there before, rather than a nil somewhere downstream.

  `:root` is the exception and is deliberately not sticky: deleting it
  means the key decides, which is the whole point of having a key."
  [state setting]
  (-> state
      (assoc :bpm     (or (:bpm setting) (:bpm state))
             :below   (or (:below setting) (:below state))
             :steps   (min max-steps (or (:columns setting) (:steps state)))
             :rows    (max 4 (or (:rows setting) (:rows state)))
             :scale   (or (:scale setting) (:scale state))
             :key     (normalise-key (or (:key setting) (:key state)))
             :octave  (or (:octave setting) (:octave state))
             :spell   (or (:spell setting) (:spell state))
             :root    (:root setting)
             :seed    (or (:seed setting) (:seed state))
             :density (or (:density setting) (:density state))
             :band    (or (:band setting) (:band state))
             :voices  (or (:voices setting) (:voices state))
             :phrase  (or (:phrase setting) (:phrase state)))
      trim-to))

;; ---------------------------------------------------------------------
;; The seat
;; ---------------------------------------------------------------------

(def seat
  "One seat, one afternoon. The gap between each keystroke and the next,
  in milliseconds. Two of these are not gaps between keystrokes. They are
  the times somebody got up."
  [112 98 140 121 105 133 96 118 2600 104 92 130 117 101 126 88 111
   5400 99 143 108 97 120 134 102 91 1150 128 113 106])

(def band
  "What counts as work. Everything outside it comes out at the front end."
  [60 400])

(defn within-band?
  [state gap]
  (let [[low high] (:band state)]
    (and (>= gap low) (<= gap high))))

(defn gap-row
  "Fast typing is a high note. Clamped at both ends, so a four-minute
  pause and a two-minute pause land on the same row and are
  indistinguishable once they are music."
  [state gap]
  (let [[low high] (:band state)
        span (max 1 (- high low))
        clamped (max low (min high gap))
        slowness (/ (- clamped low) (* 1.0 span))]
    (js/Math.round (* (dec (:rows state)) (- 1 slowness)))))

(defn load-gaps
  "Place one note per keystroke at the column its own timing puts it in,
  and stretch the roll to fit. The tempo is not adjusted to make it come
  out even, because the whole content of this is where the notes fall."
  [state gaps voice-id]
  (let [millis (step-millis state)
        placed (loop [remaining gaps elapsed 0 acc {}]
                 (if-let [gap (first remaining)]
                   (let [column (js/Math.round (/ elapsed millis))]
                     (recur (rest remaining)
                            (+ elapsed gap)
                            (if (< column max-steps)
                              (assoc acc [column (gap-row state gap)] voice-id)
                              acc)))
                   acc))
        width (min max-steps (inc (reduce max 0 (map first (keys placed)))))]
    (assoc state
           :notes placed
           :steps (max 16 width))))

(defn load-afternoon
  "Every gap, in the order it happened, at its own spacing."
  [state]
  (let [filled (load-gaps state seat :bell)]
    (assoc filled :note
           (str "The afternoon as it happened: " (:steps filled) " columns."))))

(defn load-typing
  "The same afternoon with everything outside the band dropped, which is
  everything that was not typing, which is the two times somebody got
  up."
  [state]
  (let [[low high] (:band state)
        kept (filter (fn [gap] (within-band? state gap)) seat)
        filled (load-gaps state kept :bell)]
    (assoc filled :note
           (str "The same afternoon with every gap outside " low " to " high
                " milliseconds dropped: " (:steps filled)
                " columns, and nothing missing that anybody would call missing."))))

;; ---------------------------------------------------------------------
;; Sound
;; ---------------------------------------------------------------------

(defonce audio (atom nil))
(defonce master (atom nil))
(defonce noise (atom nil))

(defn context
  "One AudioContext, made on the first gesture, because a browser will
  not let a page make noise before somebody asks it to.

  Built through `Reflect.construct` because the constructor is chosen at
  runtime, and `new` wants a name it can resolve rather than a value."
  []
  (or @audio
      (let [constructor (or (.-AudioContext js/window)
                            (.-webkitAudioContext js/window))
            ctx (js/Reflect.construct constructor #js [])
            out (.createGain ctx)]
        (set! (.-value (.-gain out)) 0.5)
        (.connect out (.-destination ctx))
        (reset! master out)
        (reset! audio ctx)
        ctx)))

(defn- noise-buffer [ctx]
  (or @noise
      (let [frames (js/Math.floor (* 0.4 (.-sampleRate ctx)))
            buffer (.createBuffer ctx 1 frames (.-sampleRate ctx))
            channel (.getChannelData buffer 0)]
        (dotimes [i frames]
          (aset channel i (- (* 2 (js/Math.random)) 1)))
        (reset! noise buffer)
        buffer)))

(defn strike!
  "One note, at an absolute time on the audio clock. Builds its own small
  graph and abandons it; the nodes are collected once the source stops,
  which is why nothing here is kept in an atom."
  [ctx spec freq at]
  (let [envelope (.createGain ctx)
        source (if (:noise spec)
                 (let [s (.createBufferSource ctx)]
                   (set! (.-buffer s) (noise-buffer ctx))
                   s)
                 (let [o (.createOscillator ctx)]
                   (set! (.-type o) (:wave spec))
                   (.setValueAtTime (.-frequency o) freq at)
                   o))
        colour (when (:filter spec)
                 (let [f (.createBiquadFilter ctx)]
                   (set! (.-type f) (:filter spec))
                   (.setValueAtTime (.-frequency f) (or (:cutoff spec) freq) at)
                   (set! (.-value (.-Q f)) (if (:noise spec) 7 1))
                   f))]
    (.setValueAtTime (.-gain envelope) 0.0001 at)
    (.exponentialRampToValueAtTime (.-gain envelope) (:gain spec) (+ at (:attack spec)))
    (.exponentialRampToValueAtTime (.-gain envelope) 0.0001 (+ at (:length spec)))
    (if colour
      (do (.connect source colour)
          (.connect colour envelope))
      (.connect source envelope))
    (.connect envelope @master)
    (.start source at)
    (.stop source (+ at (:length spec) 0.05))))

(defn audition!
  "Sound one note now, so placing a note is heard as it is placed."
  [state row voice-id]
  (let [ctx (context)]
    (.resume ctx)
    (strike! ctx (voice-spec voice-id) (frequency-of state row)
             (+ 0.01 (.-currentTime ctx)))))

;; ---------------------------------------------------------------------
;; Transport
;; ---------------------------------------------------------------------

(defonce roll (atom nil))
(defonce transport (atom {:playing false :timer nil :painter nil
                          :step 0 :next-time 0 :origin 0}))

(def lookahead 0.1)
(def tick-millis 25)

(defn- schedule!
  "Everything that falls inside the lookahead window. Reads the roll each
  time round, so an edit during playback is heard on the next pass rather
  than on the next press of Play."
  []
  (let [ctx @audio
        state @roll
        duration (step-seconds state)]
    (loop [guard 0]
      (let [{:keys [next-time step playing]} @transport]
        (when (and playing
                   (< guard 64)
                   (< next-time (+ (.-currentTime ctx) lookahead)))
          (doseq [[row voice-id] (notes-at state step)]
            (strike! ctx (voice-spec voice-id) (frequency-of state row) next-time))
          (let [following (inc step)
                wrapped (>= following (:steps state))]
            (if (and wrapped (not (:looping state)))
              (swap! transport assoc :playing false)
              (swap! transport
                     (fn [t]
                       (assoc t
                              :step (if wrapped 0 following)
                              :next-time (+ (:next-time t) duration)))))
            (recur (inc guard))))))))

(defn position
  "Where the playhead is, in columns, read off the audio clock rather than
  off the scheduler, which runs ahead of what anybody can hear."
  []
  (let [ctx @audio
        state @roll
        {:keys [playing origin]} @transport]
    (if (and ctx playing)
      (mod (/ (- (.-currentTime ctx) origin) (step-seconds state))
           (:steps state))
      0)))

(declare paint!)

(defn stop! [node]
  (let [{:keys [timer painter]} @transport]
    (when timer (js/clearInterval timer))
    (when painter (js/cancelAnimationFrame painter)))
  (swap! transport assoc :playing false :timer nil :painter nil :step 0)
  (paint! node))

(defn- move-playhead! [node]
  (when-let [bar (fedoras.reader/q node ".music-playhead")]
    (if (:playing @transport)
      (do (.setProperty (.-style bar) "--position" (str (position)))
          (.setProperty (.-style bar) "display" "block"))
      (.setProperty (.-style bar) "display" "none"))))

(defn- follow! [node]
  (letfn [(frame []
            (move-playhead! node)
            (if (:playing @transport)
              (swap! transport assoc :painter (js/requestAnimationFrame frame))
              (stop! node)))]
    (swap! transport assoc :painter (js/requestAnimationFrame frame))))

(defn play! [node]
  (if (:playing @transport)
    (stop! node)
    (let [ctx (context)]
      (.resume ctx)
      (let [start (+ 0.12 (.-currentTime ctx))]
        (swap! transport assoc
               :playing true
               :step 0
               :origin start
               :next-time start
               :timer (js/setInterval schedule! tick-millis))
        (schedule!)
        (follow! node)
        (paint! node)))))

;; ---------------------------------------------------------------------
;; Painting
;; ---------------------------------------------------------------------

(defonce shape (atom nil))

(defn- cell-class [state step row]
  (let [voice-id (get (:notes state) [step row])]
    (str "music-cell"
         (when (zero? (mod step 4)) " music-beat")
         (when (zero? (mod step 16)) " music-measure")
         (when (root-row? state row) " music-root")
         (when voice-id (str " music-on music-" (name voice-id))))))

(defn- grid-html [state]
  (str/join
   ""
   (for [row (reverse (range (:rows state)))
         step (range (:steps state))]
     (str "<button class='" (cell-class state step row) "'"
          " data-step='" step "' data-row='" row "'></button>"))))

(defn- voices-html [state]
  (str/join
   ""
   (for [{:keys [id label]} voices]
     (str "<button class='music-voice music-" (name id)
          (when (= id (:voice state)) " music-chosen") "'"
          " data-voice='" (name id) "'>" label "</button>"))))

(defn- keys-html [state]
  (str/join
   ""
   (for [k key-order]
     (str "<option value='" (name k) "'"
          (when (= k (:key state)) " selected") ">"
          (get key-labels k) "</option>"))))

(defn- scales-html [state]
  (str/join
   ""
   (for [{:keys [id label]} scales]
     (str "<option value='" (name id) "'"
          (when (= id (:scale state)) " selected") ">" label "</option>"))))
(defn- cells [node]
  (let [found (.querySelectorAll node ".music-cell")]
    (mapv (fn [i] (.item found i)) (range (.-length found)))))

(defn- rows-html
  "The gutter down the left of the paper. Every row is named, and the
  rows that begin an octave carry the number, so the grid can be read
  without counting up from the bottom."
  [state]
  (str/join
   ""
   (for [row (reverse (range (:rows state)))
         :let [{:keys [pitch octave]} (pitch-at state row)
               root (root-row? state row)]]
     (str "<div class='music-row" (when root " music-row-root") "'>"
          pitch
          (when root (str "<span class='music-octave'>" octave "</span>"))
          "</div>"))))

(defn- set-option!
  "Set a select to a value, if that value is one of its options. Loading
  the afternoon stretches the roll to whatever width the afternoon was,
  and that width is rarely one of the four on the menu."
  [control value]
  (let [wanted (str value)
        options (.-options control)
        found (some (fn [i] (= wanted (.-value (.item options i))))
                    (range (.-length options)))]
    (if found
      (set! (.-value control) wanted)
      (set! (.-selectedIndex control) -1))))

(defn- sync-controls!
  "Put the controls where the state is. They are set once at setup, and
  the settings panel moves the state under them afterwards."
  [node]
  (let [state @roll]
    (when-let [tempo (fedoras.reader/q node ".music-tempo")]
      (set! (.-value tempo) (str (:bpm state))))
    (when-let [chooser (fedoras.reader/q node ".music-scale")]
      (set-option! chooser (name (:scale state))))
    (when-let [chooser (fedoras.reader/q node ".music-key")]
      (set-option! chooser (name (:key state))))
    (when-let [chooser (fedoras.reader/q node ".music-length")]
      (set-option! chooser (:steps state)))))

(defn paint! [node]
  (let [state @roll
        layout [(:steps state) (:rows state) (:scale state)
                (:key state) (:octave state) (:spell state) (:root state)]]
    (if (= layout @shape)
      (doseq [cell (cells node)]
        (let [step (js/parseInt (.getAttribute cell "data-step") 10)
              row (js/parseInt (.getAttribute cell "data-row") 10)]
          (set! (.-className cell) (cell-class state step row))))
      (do (fedoras.ui.widget-ui/grid!
           node ".music-grid" (:steps state) (grid-html state))
          (fedoras.ui.widget-ui/html! node ".music-rows" (rows-html state))
          (reset! shape layout)))
    (fedoras.ui.widget-ui/html! node ".music-voices" (voices-html state))
    (fedoras.ui.widget-ui/state!
     node
     (str (count (:notes state)) " notes · "
          (get key-labels (:key state)) " "
          (:label (scale-spec (:scale state))) " · "
          (:steps state) " columns · "
          (:bpm state) " bpm"))
    (fedoras.reader/text! node ".music-status" (:note state))
    (fedoras.reader/enable! node ".music-play" true
                            (if (:playing @transport) "Stop" "Play"))
    (sync-controls! node)
    (move-playhead! node)))

;; ---------------------------------------------------------------------
;; Hands
;; ---------------------------------------------------------------------

(defonce painting (atom nil))


(defn- cell-at [event]
  (fedoras.ui.widget-ui/data event ".music-cell" "step" "row"))

(defn- touch!
  "Place or erase one cell. `erasing` is decided when the drag starts and
  holds for the whole drag, so dragging across a row does not toggle
  every note it crosses twice."
  [node step row erasing]
  (let [state @roll]
    (if erasing
      (reset! roll (erase-note state step row))
      (let [voice-id (:voice state)]
        (when-not (= voice-id (get (:notes state) [step row]))
          (audition! state row voice-id))
        (reset! roll (put-note state step row voice-id)))))
  (paint! node))

(defn- wire-grid! [node]
  (let [grid (fedoras.reader/q node ".music-grid")]
    (set! (.-onpointerdown grid)
          (fn [event]
            (when-let [[step row] (cell-at event)]
              (.preventDefault event)
              (let [erasing (or (.-shiftKey event)
                                (not (zero? (.-button event)))
                                (some? (get (:notes @roll) [step row])))]
                (reset! painting erasing)
                (touch! node step row erasing)))))
    (set! (.-onpointermove grid)
          (fn [event]
            (when (some? @painting)
              (when-let [[step row] (cell-at event)]
                (touch! node step row @painting)))))
    (set! (.-onpointerup grid) (fn [_] (reset! painting nil)))
    (set! (.-onpointerleave grid) (fn [_] (reset! painting nil)))
    (set! (.-oncontextmenu grid) (fn [event] (.preventDefault event) false))))

(defn- act!
  [node f & args]
  (swap! roll (fn [state] (apply f state args)))
  (paint! node))

(defn- wire-transport!
  [node]
  (fedoras.reader/on-click! node ".music-play" (fn [_] (play! node)))
  (fedoras.reader/on-click! node ".music-clear" (fn [_] (act! node clear-notes)))
  (fedoras.ui.widget-ui/keys! node {" " :play} (fn [_ _] (play! node))))

(defn- wire-fill!
  [node]
  (fedoras.reader/on-click! node ".music-fill"
                            (fn [_] (stop! node) (act! node fill)))
  (fedoras.reader/on-click! node ".music-fill-again"
                            (fn [_] (stop! node) (act! node fill-again))))

(defn- wire-seat!
  [node]
  (fedoras.reader/on-click! node ".music-afternoon"
                            (fn [_] (stop! node) (act! node load-afternoon)))
  (fedoras.reader/on-click! node ".music-typing"
                            (fn [_] (stop! node) (act! node load-typing))))

(defn- wire-keeping!
  [node]
  (fedoras.reader/on-click! node ".music-keep" (fn [_] (act! node keep!)))
  (fedoras.reader/on-click! node ".music-restore"
                            (fn [_]
                              (stop! node)
                              (act! node from-storage
                                    (fedoras.reader/stored storage-key))))
  (fedoras.reader/on-click! node ".music-download"
                            (fn [_]
                              (fedoras.ui.widget-ui/download!
                               (as-text @roll)
                               (str "roll-" (:seed @roll) ".edn")))))

(defn- wire-voices!
  [node]
  (when-let [picker (fedoras.ui.widget-ui/q! node ".music-voices")]
    (set! (.-onclick picker)
          (fn [event]
            (when-let [button (.closest (.-target event) ".music-voice")]
              (let [chosen (keyword (.getAttribute button "data-voice"))]
                (swap! roll assoc :voice chosen)
                (audition! @roll (js/Math.floor (/ (:rows @roll) 2)) chosen)
                (paint! node)))))))


(defn- wire-controls!
  [node]
  (when-let [tempo (fedoras.ui.widget-ui/q! node ".music-tempo")]
    (set! (.-oninput tempo)
          (fn [_]
            (swap! roll assoc :bpm (js/parseInt (.-value tempo) 10))
            (when (:playing @transport) (stop! node))
            (paint! node))))
  (when-let [chooser (fedoras.ui.widget-ui/q! node ".music-scale")]
    (set! (.-innerHTML chooser) (scales-html @roll))
    (set! (.-onchange chooser)
          (fn [_]
            (swap! roll assoc :scale (keyword (.-value chooser)))
            (paint! node))))
  (when-let [chooser (fedoras.ui.widget-ui/q! node ".music-key")]
    (set! (.-innerHTML chooser) (keys-html @roll))
    (set! (.-onchange chooser)
          (fn [_]
            (swap! roll (fn [state]
                          (assoc state
                                 :key (keyword (.-value chooser))
                                 :root nil)))
            (paint! node))))
  (when-let [chooser (fedoras.ui.widget-ui/q! node ".music-length")]
    (set! (.-onchange chooser)
          (fn [_]
            (swap! roll (fn [state]
                          (trim-to (assoc state :steps
                                          (js/parseInt (.-value chooser) 10)))))
            (stop! node)))))

(def fill-settings
  "The settings a filled roll is made from. A Run that changes any of
  these refills the paper. A Run that changes none of them leaves
  whatever the reader has drawn alone."
  [:rows :columns :scale :seed :density :voices :phrase])

(defonce filled-from (atom nil))

(defn apply-settings!
  "What Run does. The numbers land in the roll, and if any of the numbers
  the notes were made from has moved, the notes are made again."
  [node setting]
  (swap! roll from-settings setting)
  (let [signature (select-keys setting fill-settings)]
    (when (or (not= signature @filled-from) (empty? (:notes @roll)))
      (reset! filled-from signature)
      (swap! roll fill)))
  (when (:playing @transport) (stop! node))
  (paint! node))

(defn- wire-settings!
  [node id]
  (fedoras.ui.widget-ui/settings!
   node id
   (fn [setting] (apply-settings! node setting))))

(defn setup!
  "Once per element. Each control is wired on its own, so one that fails
  says so and takes nothing else with it."
  [node]
  (let [id (.-id node)]
    (fedoras.ui.widget-ui/wire! node "grid" wire-grid!)
    (fedoras.ui.widget-ui/wire! node "transport" wire-transport!)
    (fedoras.ui.widget-ui/wire! node "fill" wire-fill!)
    (fedoras.ui.widget-ui/wire! node "seat" wire-seat!)
    (fedoras.ui.widget-ui/wire! node "keeping" wire-keeping!)
    (fedoras.ui.widget-ui/wire! node "voices" wire-voices!)
    (fedoras.ui.widget-ui/wire! node "controls" wire-controls!)
    (fedoras.ui.widget-ui/wire! node "settings" (fn [n] (wire-settings! n id)))))

(defn mount!
  [{:keys [id]}]
  (when (nil? @roll)
    (reset! roll default-roll))
  (fedoras.ui.widget-ui/mount!
   ::music (or id "music") setup! paint!))