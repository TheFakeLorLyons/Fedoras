(ns fedoras.cast
  "Who is speaking, and who is doing the voice."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; The register
;; ---------------------------------------------------------------------

(def base
  "Keyword to layers. A layer is a string, or another keyword, whose own
  layers are spliced in whole — so :druid-girl is written once and
  follows :the-girl if the girl ever stops being Victor's."
  {;; the table
   :victor        ["VICTOR"]
   :derek         ["DEREK"]
   :miles         ["MILES"]
   :simon         ["SIMON"]
   :mochi         ["MOCHI"]

   :miles-help    ["MILES'S HELP"]
   :dereks-help   ["DEREK'S HELP"]
   :simons-help   ["SIMON'S HELP"]

   ;; the party.
   :ranger        ["RANGER" "{pc}"]
   :cleric        ["CLERIC" :derek]
   :paladin       ["PALADIN" :simon]
   :documentarian ["DOCUMENTARIAN" :miles]

   ;; Victor's masks
   :registrar     ["REGISTRAR" :victor]
   :the-girl      ["THE GIRL" :victor]
   :druid         ["DRUID" :victor]
   :druid-girl    ["DRUID" :the-girl] ; :the-girl then expands...
   :guildmaster   ["GUILDMASTER" :victor]
   :supreme-moderator ["SUPREME MODERATOR" :victor]

   ;; aliases
   :victor-registrar [:registrar]
   :victor-the-girl  [:the-girl]
   :victor-druid     [:druid]

   ;; Mochi
   :ariathne ["ARIATHNE" :mochi]})

(def ^:dynamic *cast* base)

(defn register!
  "Add to the register for the rest of the JVM.

  Clay renders every chapter in one process, so this is genuinely
  global: a `register!` at the top of session three is still in force
  when session five renders, and two sessions registering the same
  keyword differently means the last one loaded wins for both,
  retroactively. Fine for the standing cast. For anyone who only exists
  in one session, use `with-cast`."
  [m]
  (alter-var-root #'*cast* merge m))

(defmacro with-cast
  "Register somebody for the length of one form and no longer.

    (cast/with-cast {:chancellor [\"HOLLOW CHANCELLOR\" :victor]}
      (sp/scene ...))"
  [m & body]
  `(binding [*cast* (merge *cast* ~m)] ~@body))

;; ---------------------------------------------------------------------
;; Taking a cue apart
;; ---------------------------------------------------------------------

(def ^:private bracketed #"\s*\[([^\]]*)\]")

(defn- split-cue
  "Take \"DRUID [THE GIRL] [VICTOR]\" apart into its layers, so a cue
  pasted in from an already-written scene registers as three things
  rather than as one thing with punctuation inside it.

  This is the only reason the register cannot emit a nested bracket. Not
  because the join is careful — it is one bracket, once, and it could
  not be anything else — but because nothing that reaches the join has a
  bracket left in it."
  [s]
  (into [(str/trim (str/replace s bracketed ""))]
        (comp (map (fn [[_ inner]] (str/trim inner)))
              (remove str/blank?))
        (re-seq bracketed s)))

(def ^:private pc-cues
  {:pc "{pc}" :pc-full "{pc-full}" :pc-last "{pc-last}"})

(defn- near
  "Registered keywords that look like the one asked for, because the
  mistake is nearly always a typo and the error should say so."
  [k]
  (let [s    (name k)
        stem (subs s 0 (min 4 (count s)))]
    (->> (keys *cast*)
         (filter (fn [c] (or (str/includes? (name c) stem)
                             (str/includes? s (name c)))))
         sort
         (take 5))))

(defn layers
  "The cue as a vector of strings, outermost first. Repeats are dropped —
  `distinct` rather than `dedupe`, because a mask three deep can reach
  the same player by two routes and the repeat is not always adjacent."
  [x]
  (cond
    (string? x)  (split-cue x)
    (vector? x)  (into [] (comp (mapcat layers) (distinct)) x)
    (keyword? x) (or (some-> (get pc-cues x) vector)
                     (some-> (get *cast* x) layers)
                     (throw (ex-info (str "fedoras: nobody is registered as " x "."
                                          (when-let [n (seq (near x))]
                                            (str " Did you mean "
                                                 (str/join ", " n) "?")))
                                     {:unknown x :registered (sort (keys *cast*))})))
    :else        [(str x)]))

(defn cue
  "The cue line as the page shows it.

  A plain string passes through untouched, so every scene already
  written renders byte-identically and a one-off never needs
  registering. The splitting above applies to what is in the register,
  not to what is at a call site."
  [x]
  (if (string? x)
    x
    (let [[head & rest] (layers x)]
      (str head (when (seq rest) (str " [" (str/join "] [" rest) "]"))))))

(def ^:private cue-token #"@:([A-Za-z0-9*+!_?<>=-]+)")

(defn expand
  "Turn @:registrar into @REGISTRAR [VICTOR] wherever it appears, so a
  dice passage names its speakers the same way a scene does. Leaves
  @VICTOR alone: no colon, no match."
  [s]
  (when s
    (str/replace (str s) cue-token (fn [[_ k]] (str "@" (cue (keyword k)))))))

;; ---------------------------------------------------------------------
;; Apparatus
;; ---------------------------------------------------------------------

(defn roster
  "The register, rendered. For looking at."
  []
  (->> (keys *cast*)
       sort
       (map (fn [k] (format "  %-20s %s" k (cue k))))
       (str/join "\n")))

(defn unregistered
  "Every literal cue string in the book that is not something the
  register would have produced. Run it before a build."
  []
  (let [known (set (map cue (keys *cast*)))]
    (->> (file-seq (io/file "src"))
         (filter (fn [f] (and (.isFile f) (str/ends-with? (.getName f) ".clj"))))
         (mapcat (fn [f]
                   (keep (fn [[_ s]] (when-not (known s) [(.getPath f) s]))
                         (re-seq #"\(sp/(?:dialogue|off-screen)\s+\"([^\"]+)\""
                                 (slurp f)))))
         distinct
         sort
         vec)))

(defn report
  "What `unregistered` found, grouped by cue rather than by file, since
  a cue typed wrong once is a typo and a cue typed wrong in four places
  is somebody who should be in the register."
  []
  (let [found (unregistered)]
    (if (empty? found)
      (println "fedoras: every cue in the book is in the register.")
      (doseq [[s uses] (sort-by (comp - count val) (group-by second found))]
        (println (format "  %-32s %d × — %s"
                         s (count uses)
                         (str/join ", " (distinct (map first uses)))))))))
