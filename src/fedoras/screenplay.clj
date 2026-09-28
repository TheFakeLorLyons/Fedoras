(ns fedoras.screenplay
  "A tiny screenplay DSL.

  Every function here returns plain Hiccup, except `scene`, which wraps a
  pile of Hiccup in a `kind/hiccup` so Clay renders it. That split is
  deliberate: the pieces stay composable and only the outermost call is
  Kindly-annotated.

  THE PROTAGONIST HAS NO NAME. Every string that passes through this
  namespace is interpolated for {pc}, {pc-full} and {pc-last}, which
  render as spans the reader can rewrite. Write scenes with the tokens,
  never with a literal name.

  NEITHER HAS THE ANIMAL. {ac} is whatever the reader rolled and
  whatever they called it. It has a grammar: {ac-kind}, {ac-plural},
  {ac-a}, {ac-many}, {ac-taxonomy}, {ac-other}. The plural is data
  rather than a rule, because the rule gets `mooses`.

  CASE FOLLOWS THE TOKEN. {ac} is `ted`, {Ac} is `Ted`, {AC} is `TED`.
  Note that this is a change: the old regex was case-insensitive, so a
  {PC} already written in a scene rendered as `Owen` and now renders as
  `OWEN`.

  WHO IS SPEAKING lives in `fedoras.cast`. A cue may be a registered
  keyword, and a literal string still passes through untouched."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [fedoras.cast :as cast]
            [fedoras.companions :as companions]
            [fedoras.state :as state]
            [scicloj.kindly.v4.kind :as kind]))

;; ---------------------------------------------------------------------
;; The protagonist
;; ---------------------------------------------------------------------

(def ^:dynamic *protagonist*
  "The default, which is only a default. The reader picks their own on
  page one and it follows them through the whole book."
  {:first "Owen" :last "Mercer"
   :student-id "Provisional #3182"})

(def ^:dynamic *companion*
  "The animal, for the print edition, as a roster entry. Readers roll
  their own and the page rewrites every {ac} to whatever they got; a PDF
  gets this one, which is the marmot, which is what Victor's table
  actually rolled."
  (get companions/roster 3))

;; ---------------------------------------------------------------------
;; Tokens
;;
;; The build-time half of `fedoras.reader/render`. The two are the same
;; rules written twice, once in Clojure for the PDF and once in
;; ClojureScript for the page, because the runtime is slurped as text
;; rather than required and there is no honest way to share a function
;; across that boundary without turning it into a .cljc and teaching the
;; build about it. If you change one, change the other.
;; ---------------------------------------------------------------------

(def ^:private token-re #"\{([A-Za-z]+)(-[A-Za-z-]+)?\}")

(defn- apply-case [head s]
  (cond
    (str/blank? s)                 s
    (= head (str/upper-case head)) (str/upper-case s)
    (= (subs head 0 1) (str/upper-case (subs head 0 1)))
    (str (str/upper-case (subs s 0 1)) (subs s 1))
    :else                          s))

(defn- pc-text [tail]
  (let [{f :first l :last} *protagonist*]
    (case (or tail "-first")
      "-first" f
      "-full"  (str f " " l)
      "-last"  l
      nil)))

(defn- an [s]
  (str (if (re-find #"(?i)^[aeiou]" (str s)) "an " "a ") s))

(defn- ac-text [source tail]
  (let [{:keys [animal plural taxonomy collective other-names]} *companion*
        kind (str/lower-case (or animal "animal"))]
    (case (or tail "-name")
      "-name"     (str "the " kind)
      "-kind"     kind
      "-a"        (an kind)
      "-plural"   (str/lower-case (or plural (str kind "s")))
      "-many"     (if collective
                    (str (an collective) " of " (or plural (str kind "s")))
                    (str "several " (or plural (str kind "s"))))
      "-taxonomy" (str/lower-case (or taxonomy kind))
      "-given"    ""
      ;; stable per passage, so the same sentence reads the same way in
      ;; every build and two nearby sentences do not both reach for the
      ;; same alternative
      "-other"    (if (seq other-names)
                    (nth other-names (mod (Math/abs (hash source)) (count other-names)))
                    (str "the " kind))
      nil)))

(defn interpolate
  "Split a string on protagonist and animal tokens, returning a seq of
  strings and rewritable spans. A token nothing here understands is left
  exactly as it was found, so this is safe over arbitrary prose."
  [s]
  (let [s (str s)
        m (re-matcher token-re s)]
    (loop [idx 0, out []]
      (if (.find m)
        (let [st   (.start m)
              en   (.end m)
              head (.group m 1)
              tail (some-> (.group m 2) str/lower-case)
              who  (str/lower-case head)
              span (case who
                     "pc" (when-let [t (pc-text tail)]
                            [:span.pc-name
                             {:data-pc (or tail "-first") :data-case head}
                             (apply-case head t)])
                     "ac" (when-let [t (ac-text s tail)]
                            [:span.ac-name
                             {:data-ac (or tail "-name") :data-case head}
                             (apply-case head t)])
                     nil)]
          (recur en (cond-> out
                      (< idx st) (conj (subs s idx st))
                      :always    (conj (or span (subs s st en))))))
        (if (< idx (count s))
          (conj out (subs s idx))
          out)))))

(defn- txt [tag & lines]
  (into [tag] (interpolate (str/join " " lines))))

;; ---------------------------------------------------------------------
;; Assets
;; ---------------------------------------------------------------------

(def scittle-version "0.6.22")

(def scittle-cdn
  (str "https://cdn.jsdelivr.net/npm/scittle@" scittle-version "/dist/scittle.js"))

(def scittle-resource
  "Where a vendored copy lives. Owned by `fedoras.state`, which also
  knows how to put it next to a build."
  state/scittle-resource)

(defn scittle-src
  "The vendored copy if the project has one, the CDN if not.

  Scittle is not an extra any more: it carries the reader's name, the
  animal they rolled, the dice and the Echo. A published book that
  fetches its own runtime from somebody else's server is one outage away
  from having none of that.

  The path is relative because a Quarto book writes every chapter into
  one flat directory, so `scittle.js` resolves from any page in it."
  []
  (if (io/resource scittle-resource) "scittle.js" scittle-cdn))

(defn- css [] (slurp (io/resource "fedoras/screenplay.css")))

(defn assets
  "Call once near the top of every chapter. Injects the stylesheet, the
  Scittle runtime for live cells, and the reader-side state.

  A Quarto book is a page per chapter, so this genuinely has to be
  per-chapter -- there is no shared preamble the way there is in LaTeX."
  []
  (kind/hiccup
   [:div {:style {:display "none"}}
    [:style (css)]
    [:script {:type "application/javascript" :src (scittle-src)}]
    (state/assets (companions/grammar))]))

(defn name-field
  "The reader names the protagonist. Put this on the first page. The
  choice persists across every chapter of the book."
  []
  (kind/hiccup
   [:div.name-field
    [:p.name-label "This is your freshman year. Name him, or don't — the default is fine."]
    [:div.name-inputs
     [:input {:id "pc-first" :type "text" :placeholder "Owen" :spellcheck "false"}]
     [:input {:id "pc-last" :type "text" :placeholder "Mercer" :spellcheck "false"}]
     [:button.live-run.pc-enroll "Enroll"]]
    (txt :p.name-confirm "Good luck, {pc-full}.")
    [:p.name-note
     [:button.roll-rewrite.pc-forget "Forget everything"]
     " — name, animal, rolls, Echo, list."]
    [:p.name-note "Every page updates. Victor will not remember it either way."]
    (state/on-render :enrolment
                     '(do
                        (when-let [b (.querySelector js/document ".pc-enroll")]
                          (set! (.-onclick b)
                                (fn [_]
                                  (fedoras.reader/set-name!
                                   (.-value (.getElementById js/document "pc-first"))
                                   (.-value (.getElementById js/document "pc-last"))))))
                        (when-let [b (.querySelector js/document ".pc-forget")]
                          (set! (.-onclick b) (fn [_] (fedoras.reader/forget!))))))]))

;; ---------------------------------------------------------------------
;; Elements
;; ---------------------------------------------------------------------

(defn header [{:keys [act scene title]}]
  [:div.scene-header
   [:p.act (str "ACT " act)]
   [:h2.scene-title (str "Scene " scene " — " title)]])

(defn slug "A slugline. Uppercased by the stylesheet, not by Clojure."
  [s] (txt :p.slug s))

(defn action
  "Action lines. Multiple arguments are joined with a space."
  [& lines] (apply txt :p.action lines))

(defn beat
  "One image, hard stop. The Jarry rhythm. Variadic like `action`, so a
  long beat can be broken across source lines without fighting the
  wrap -- but if a beat needs two source lines, look at it twice."
  [& lines] (apply txt :p.action.beat lines))

(defn dialogue
  "Dialogue. `character` may be a keyword registered in `fedoras.cast`,
  the keyword :pc, or a literal string. Text may be a string or a vector
  of strings (one per line).

    (dialogue :registrar \"You're late.\")
    (dialogue :the-girl \"Annoyed\" \"I know what I am.\")
    (dialogue \"VICTOR\" \"gravely\" [\"Exactly.\" \"That is when they get you.\"])"
  ([character text] (dialogue character nil text))
  ([character parenthetical text]
   [:div.dialogue
    (txt :p.character (cast/cue character))
    (when parenthetical (txt :p.paren (str "(" parenthetical ")")))
    (into [:div.lines]
          (map (fn [l] (txt :p.line l))
               (if (sequential? text) text [text])))]))

(defn off-screen
  "Same as `dialogue`, but the speaker is not in frame.

  This used to build its cue with `(str character \" (O.S.)\")`, which
  meant a keyword produced the literal characters `:pc (O.S.)` — the
  keyword branch only ever existed in `dialogue`."
  ([character text] (off-screen character nil text))
  ([character parenthetical text]
   (dialogue (str (cast/cue character) " (O.S.)") parenthetical text)))

(defn transition [s] (txt :p.transition s))

(defn sign
  "Text that exists in the world: a banner, a whiteboard, a sign taped to
  a folding table. The single most reliable joke delivery mechanism in
  this entire screenplay."
  [& lines]
  (into [:div.sign] (map (fn [l] (txt :p l)) lines)))

(defn whiteboard [title items]
  [:div.sign.whiteboard
   [:p.whiteboard-title title]
   (into [:ol] (map (fn [i] (txt :li i)) items))])

(defn stage
  "A parenthetical stage direction that applies to the whole scene.
  Variadic, like `action`."
  [& lines] (apply txt :p.direction lines))

(defn prose
  "Narrative prose outside the screenplay format: the code codas, the
  4am dorm-room paragraphs, anything between scenes.

  Use this rather than a `;;` markdown comment ANY TIME the protagonist
  is mentioned. Markdown comments go straight to Quarto untouched, so a
  {pc} token in one renders as the literal characters {pc}."
  [& paragraphs]
  (kind/hiccup
   (into [:div.prose] (map (fn [para] (txt :p para)) paragraphs))))

(def ^:private screenplay-classes
  "Classes that keep a hiccup node inside `.screenplay`. Anything else
  that appears in a `scene` body is hoisted out and rendered as a
  sibling, so a commit, a handout or an AI chat can sit between two
  action lines in a single call."
  #{"scene-header" "slug" "action" "beat" "dialogue" "character"
    "paren" "lines" "line" "transition" "direction" "sign"
    "whiteboard" "whiteboard-title" "prose" "by-standing"})

(defn- screenplay-element? [node]
  (and (vector? node)
       (keyword? (first node))
       (some screenplay-classes
             (rest (str/split (name (first node)) #"\.")))))

(defn scene
  "Wrap scene elements for rendering. nils are dropped, so `(when ...)`
  inside a scene body is safe; a `for` or `map` is spliced in place.

  Anything that is not a screenplay element -- an artefact, a bare
  `defn`, anything returning plain `kind/hiccup` without one of the
  screenplay classes -- is hoisted out and rendered as a sibling, so a
  single `scene` call can carry the whole passage."
  [& blocks]
  (let [blocks (into []
                     (comp (mapcat #(if (seq? %) % [%]))
                           (remove nil?))
                     blocks)]
    (kind/hiccup
     (into [:div.scene]
           (mapcat (fn [run]
                     (if (screenplay-element? (first run))
                       [(into [:div.screenplay] run)]
                       run)))
           (partition-by screenplay-element? blocks)))))

(defn with-cast
  "Register somebody for one scene. A wrapper on `cast/with-cast` so a
  session file only ever requires `sp`.

    (sp/with-cast {:chancellor [\"HOLLOW CHANCELLOR\" :victor]}
      (sp/scene ...))"
  [m body-fn]
  (binding [cast/*cast* (merge cast/*cast* m)] (body-fn)))

;; ---------------------------------------------------------------------
;; Drafting apparatus
;; ---------------------------------------------------------------------

(defn by-standing
  "One passage in three versions, keyed by ending band.

    (sp/by-standing
      {:closed      \"They lose, and the Collegium arrives, and it is quick.\"
       :reconciled  \"They win, and the Collegium arrives, and it is quick.\"
       :outstanding \"They win, and the Collegium arrives, and something does
                      not take.\"})

  A band the map does not cover falls back to `:reconciled`, so a passage
  that only forks one way is written with two keys instead of three.

  The stylesheet picks which one shows, from a class on the body that
  `fedoras.reader/paint-standing!` puts there. That function existed for
  a while without being wired to anything."
  [variants]
  (kind/hiccup
   (into [:div.by-standing]
         (map (fn [band]
                [:div {:class (str "ending ending-" (name band))}
                 (into [:p.action]
                       (interpolate (get variants band (get variants :reconciled ""))))])
              [:closed :reconciled :outstanding]))))

(def ^:private nothing
  "Clay renders whatever a top-level form returns, and a nil renders as
  the word nil. Suppressed apparatus returns this instead."
  (kind/hiccup [:span {:style {:display "none"}}]))

(def ^:dynamic *show-beats*
  "Beats, notes and status plates are author apparatus. They are OFF by
  default -- a normal build is a reading copy. `fedoras.build/draft!`
  binds this true when you want them."
  false)

(defn code
  "A code block that is displayed, not evaluated. The reader sees what
  the character typed; Clay does not run it."
  [s]
  (kind/hiccup
   [:div.scene-code
    [:pre [:code s]]]))

(defn beats
  "An outline block: what this scene still has to do. Your \\todo{} pile."
  [& items]
  (if-not *show-beats*
    nothing
    (kind/hiccup
     [:div.beats
      [:p.beats-title "BEATS"]
      (into [:ul] (map (fn [i] (txt :li i)) items))])))

(defn note [& lines]
  (if-not *show-beats*
    nothing
    (kind/hiccup
     [:div.beats.note
      [:p.beats-title "NOTE"]
      (apply txt :p lines)])))

(defn status [k]
  (if-not *show-beats*
    nothing
    (kind/hiccup
     [:p {:class (str "status status-" (name k))} (str/upper-case (name k))])))