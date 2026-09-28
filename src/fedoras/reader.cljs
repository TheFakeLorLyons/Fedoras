(ns fedoras.reader
  "Everything the reader owns.

  One atom: containing Name, animal, echo, fortune, score, deeds. A widget
  reads it and never touches storage directly, and every change goes through
  one of the setters, which writes, re-reads, and lets the watches fire."
  (:require [clojure.string :as str]
            [fedoras.carry :as carry]))

;; ---------------------------------------------------------------------
;; Storage
;; ---------------------------------------------------------------------

(def prefix "fedoras.")

(def carry-param :f)

(def forget-param
  "A page that loads with this on it withdraws its own record and stops.
  It is a separate field from the token because it must work on a page
  that has never seen a token."
  :forget)

(def withdrawn-param
  "Survives exactly one reload, to say the thing was done. The state it
  would otherwise have been recorded in has just been deleted, which is
  the one place a confirmation cannot live."
  :withdrawn)

(def legacy-params
  "The fields the query string used to carry, one per fact. Stripped off
  any link that still has them. There was a second copy of this list
  called `carried-params` and the two had drifted."
  [:pc :pcl :ac :acn :echo :fortune :score])

(defn- read-json [s]
  (js->clj (js/JSON.parse s) :keywordize-keys true))

(defn- write-json [v]
  (js/JSON.stringify (clj->js v)))

(defn stored [key]
  (try
    (when-let [s (.getItem js/localStorage (str prefix (name key)))]
      (read-json s))
    (catch :default _ nil)))

(defn store! [key val]
  (try (.setItem js/localStorage (str prefix (name key)) (write-json val))
       (catch :default _ nil))
  val)

(defn remove!
  "Take something out rather than writing an empty one over it, because
  a stored nil and a missing key read back the same and only one of them
  is what was meant."
  [key]
  (try (.removeItem js/localStorage (str prefix (clojure.core/name key)))
       (catch :default _ nil)))

(defn storage-works?
  "Whether anything at all is going to be persisted.

  On a file:// path some browsers give every page its own opaque origin,
  which means localStorage throws and the whole book forgets the reader
  the moment they click anything. Worth knowing rather than guessing."
  []
  (try
    (.setItem js/localStorage (str prefix "probe") "1")
    (let [ok (= "1" (.getItem js/localStorage (str prefix "probe")))]
      (.removeItem js/localStorage (str prefix "probe"))
      ok)
    (catch :default _ false)))

;; ---------------------------------------------------------------------
;; The query string, which is a transport and nothing else
;; ---------------------------------------------------------------------

(defn- current-url [] (js/URL. (.-href js/location)))

(defn param [key]
  (try
    (let [v (.get (.-searchParams (current-url)) (name key))]
      (when-not (str/blank? v) v))
    (catch :default _ nil)))

(defn set-params! [m]
  (try
    (let [u (current-url) sp (.-searchParams u)]
      (doseq [[k v] m]
        (if (or (nil? v) (= "" v))
          (.delete sp (name k))
          (.set sp (name k) (str v))))
      (.replaceState js/history nil "" (.toString u)))
    (catch :default _ nil)))

(defn- int-or [v d]
  (let [n (js/parseInt (str v) 10)] (if (js/isNaN n) d n)))

;; ---------------------------------------------------------------------
;; The atom
;; ---------------------------------------------------------------------

(def default-name ["Owen" "Mercer"])

(def state (atom nil))

(def animal-names
  "Number to the animal's grammar: what it is called, what more than one
  of them is called, what a ledger would file it under. Filled in by
  `fedoras.state/assets` from a later script tag, so it is empty for the
  moment between this file being read and the roster arriving, and
  nothing here may treat an empty roster as a missing roll."
  (atom {}))

(defn- as-grammar
  "One roster entry, whatever shape it turned up in.

  The page used to be handed {1 \"Pigeon\"} and is now handed
  {1 {:animal \"Pigeon\" :plural \"pigeons\" ...}}, and a book whose
  `names-form` has not caught up with its roster hands over one of each.
  A bare string is an animal with no grammar, which is precisely what it
  was before, so it becomes one here.

  This is normalised at the boundary rather than at each use because the
  alternative was a `merge` three functions away receiving a string,
  throwing `conj on a map takes map entries`, and taking down every
  render watch on the page — a build that is one function out of step
  should read a little worse, not stop."
  [v]
  (cond
    (map? v)    v
    (string? v) {:animal v}
    :else       {}))

(defn- read-companion []
  (let [s (stored :companion)
        n (int-or (first s) nil)]
    (when (and n (<= 1 n 20))
      (let [given  (or (nth s 2 nil) "")
            g      (as-grammar (get @animal-names n))
            ;; the animal's name is a convenience and the roster may not
            ;; have landed yet. the number is the fact.
            animal (or (:animal g) (nth s 1 nil) "")
            named? (not (str/blank? given))]
        (merge {:plural "" :taxonomy "" :collective "" :other-names []}
               g
               {:n           n
                :animal      animal
                :given       given
                :named?      named?
                :name        (cond named?                    (str/trim given)
                                   (not (str/blank? animal))  animal
                                   :else                      (str "d20 " n))})))))

(defn- read-name []
  (let [s (stored :pc)]
    [(or (first s) (first default-name))
     (or (second s) (second default-name))]))

(defn refresh!
  "Total on purpose. Every render watch in the book hangs off this atom,
  so anything that throws in here blanks the whole page, and what it
  reads is JSON that a previous version of this file wrote, which is not
  a thing to be optimistic about. A key that is the wrong shape reads as
  absent."
  []
  (reset! state
          {:name      (read-name)
           :companion (try (read-companion) (catch :default _ nil))
           :echo      (int-or (stored :echo) 0)
           :rolls     (let [r (stored :rolls)] (if (map? r) r {}))
           :deeds     (let [d (stored :deeds)]
                        (set (map keyword (if (sequential? d) d []))))
           :epoch     (int-or (stored :epoch) 0)
           :todo      (let [t (stored :todo)] (if (sequential? t) t []))
           :todo-seq  (int-or (stored :todo-seq) 0)}))

;; ---------------------------------------------------------------------
;; What a chapter may ask for
;; ---------------------------------------------------------------------

(defn reader-name [] (:name @state))
(defn companion   [] (:companion @state))
(defn echo        [] (:echo @state))
(defn deeds       [] (:deeds @state))
(defn fortune     [] (count (:deeds @state)))

(defn epoch
  "When this reader last withdrew the whole record, in seconds, or
  nought if they never have.

  It is not a clock and nothing displays it. It exists so that a
  withdrawal can be compared against a link: a token stamped earlier
  than this was written by somebody who has since been forgotten, and it
  is not allowed to put them back."
  []
  (:epoch @state))

(defn standing
  "Fortune less echo. Added to every roll for the rest of the book, and
  the number the last chapter reads to decide how it ends."
  []
  (- (fortune) (echo)))

(defn registration [] "3812")

;; ---------------------------------------------------------------------
;; And what it may change
;; ---------------------------------------------------------------------

(defn set-name! [f l]
  (store! :pc [(if (str/blank? f) (first default-name) (str/trim f))
               (if (str/blank? l) (second default-name) (str/trim l))])
  (refresh!))

(defn set-companion!
  "Three cells: the number, the animal's own name as a courtesy, and
  whatever the reader called it. The number is the only one that has to
  be right, because the other two are recovered from the roster."
  [n given]
  (store! :companion [n (get-in @animal-names [n :animal] "") (or given "")])
  (refresh!))

(defn set-echo! [n] (store! :echo (max 0 n)) (refresh!))

(defn add-echo!
  "One more thing taken back."
  []
  (set-echo! (inc (echo))))

(defn did!
  "Record a deed. A set, so doing the same thing twice is doing it once,
  and nothing anywhere says that it happened."
  [k]
  (store! :deeds (mapv name (conj (deeds) (keyword k))))
  (refresh!))

(defn animals!
  "The roster's grammar, from the page. Normalised on the way in, so the
  shape `fedoras.state/names-form` happens to be emitting this week is
  something only this function has an opinion about.

  Refreshes, because a companion read before this arrived is holding an
  empty animal name."
  [m]
  (reset! animal-names
          (if (map? m)
            (into {} (map (fn [[k v]] [k (as-grammar v)])) m)
            {}))
  (refresh!))

(defn rolls
  "Every check the reader has resolved, by id.

  Normalised on the way out. Storage keywordizes keys on read, and a
  check id is an arbitrary string rather than a name, so the keys come
  back as keywords and nothing matches them. And anything that is not a
  map at all becomes an empty one, because assoc on a vector with a
  string key takes the page down."
  []
  (let [r (:rolls @state)]
    (if (map? r)
      (into {} (map (fn [[k v]] [(name k) v])) r)
      {})))

(defn roll-entry [id]
  (when-let [e (get (rolls) id)]
    {:roll     (or (:roll e)     (get e "roll"))
     :mod      (or (:mod e)      (get e "mod") 0)
     :rewrites (or (:rewrites e) (get e "rewrites") 0)}))

(defn remember-roll! [id e]
  (store! :rolls (assoc (rolls) id e))
  (refresh!))

(defn score []
  (+ (reduce + 0 (keep :roll (vals (rolls))))
     (or (:n (companion)) 0)))

;; ---------------------------------------------------------------------
;; Import, once, on arrival
;; ---------------------------------------------------------------------

(def carries-epoch?
  "Whether the codec on this page round-trips a withdrawal stamp."
  (try
    (= 12345 (:epoch (carry/unpack
                      (carry/pack {:name ["Owen" "Mercer"] :epoch 12345}))))
    (catch :default _ false)))

(defn- wipe!
  "Everything under the prefix, and then one thing: when that happened."
  [at]
  (try
    (let [ks (vec (for [i (range (.-length js/localStorage))
                        :let [k (.key js/localStorage i)]
                        :when (and k (str/starts-with? k prefix))]
                    k))]
      (doseq [k ks] (.removeItem js/localStorage k)))
    (catch :default _ nil))
  (store! :epoch at))

(defn- merge-token!
  [{:keys [name companion echo rolls deeds epoch todo]}]
  (let [ours   (int-or (stored :epoch) 0)
        theirs (or epoch 0)]
    (cond
      ;; A link from before the reader withdrew the record. It is a true
      ;; account of somebody who does not exist any more, which is not
      ;; the same thing as an instruction to rebuild them. This is the
      ;; case that made `forget!` a lie: it emptied one page's storage
      ;; and every other page, and every entry in the back history, was
      ;; still holding a complete copy and handed it straight back.
      ;; A link from before the reader withdrew the record. It is a true
      ;; account of somebody who does not exist any more, which is not
      ;; the same thing as an instruction to rebuild them.
      ;;
      ;; Only when the codec can say so. A page that cannot read a stamp
      ;; off a token must not conclude from its absence that every link
      ;; in the book is stale.
      (and carries-epoch? (< theirs ours))
      (js/console.info
       (str "016.09 — the link this page was opened from was written before the record "
            "was withdrawn. Nothing carried forward. Reach this chapter from a page you "
            "have opened since, and whatever is current will arrive with you."))

      :else
      (do
        ;; They withdrew it somewhere this page has not heard about yet.
        ;; The erasure travels the way the state travels, which is the
        ;; only way anything travels in a book read off a disk.
        (when (and carries-epoch? (> theirs ours))
          (wipe! theirs)
          (js/console.info
           "016.09 — prior record withdrawn at the reader's request. Nothing carried forward."))

        ;; the name is the one thing a reader can retype in five seconds,
        ;; so the token is allowed to have an opinion about it
        (when (seq name)
          (store! :pc name))

        ;; The animal:
        ;;
        ;; This was write-once — take the token's only if there is
        ;; nothing here — which is right on one origin and wrong on a
        ;; folder of files, where every page has its own storage and the
        ;; token is the only thing that crosses. A reader who opened Act
        ;; II after rolling and before naming taught that page the horse,
        ;; permanently, and Act II could never afterwards learn that the
        ;; horse was called Horace.
        ;;
        ;; The Echo is the recency stamp. It only ever goes up, and a
        ;; reroll raises it, so a token carrying an Echo at least as
        ;; large as this page's is at least as new as this page and is
        ;; allowed to speak. One older, and it is a link from before the
        ;; reroll and is not.
        (let [have       (read-companion)
              here-echo  (int-or (stored :echo) 0)
              there-echo (or echo 0)]
          (when (and (:n companion)
                     (or (nil? have) (>= there-echo here-echo)))
            (store! :companion
                    [(:n companion) ""
                     ;; a blank name in a token is a reader who had not
                     ;; named it yet, not a reader who unnamed it
                     (if (str/blank? (:given companion))
                       (or (:given have) "")
                       (:given companion))])))

        ;; upward only, always. after the companion, which reads it.
        (store! :echo (max (int-or (stored :echo) 0) (or echo 0)))

        ;; a check is resolved once, and what is already in this browser
        ;; was rolled by this reader, so it wins every collision
        (when (seq rolls)
          (let [named (fn [m] (if (map? m)
                                (into {} (map (fn [[k v]] [(clojure.core/name k) v])) m)
                                {}))]
            (store! :rolls (merge (named rolls) (named (stored :rolls))))))

        ;; deeds are a set, and a set does not get smaller by being described
        (when (seq deeds)
          (let [held (let [d (stored :deeds)] (if (sequential? d) d []))]
            (store! :deeds (vec (into (set (map clojure.core/name held))
                                      (map clojure.core/name deeds))))))

        (when-let [t todo]
          (when (>= (:seq t 0) (int-or (stored :todo-seq) 0))
            (store! :todo (:items t))
            (store! :todo-seq (:seq t))))))))

(defn- import-token! []
  (if-let [t (param carry-param)]
    (if-let [m (try (fedoras.carry/unpack t) (catch :default _ nil))]
      (merge-token! m)
      (try (fedoras.carry/complain!) (catch :default _ nil)))
    ;; nothing to do, unless somebody has a link from before the token
    (do
      (when-let [pc (param :pc)]
        (store! :pc [pc (or (param :pcl) (second default-name))]))
      (when-let [ac (param :ac)]
        (let [n (int-or ac nil)]
          (when (and n (<= 1 n 20) (nil? (read-companion)))
            (store! :companion [n "" (or (param :acn) "")]))))
      (when-let [e (param :echo)]
        (store! :echo (max (int-or (stored :echo) 0) (int-or e 0)))))))

(defn- withdraw-if-asked!
  "A page loaded with a withdrawal stamp on it clears itself and does
  nothing else.

  This is how one button reaches twelve documents that cannot see each
  other. The page the reader clicked opens each of the others in a
  hidden frame with the stamp in the query string, and each one runs
  this on the way up. Nothing is scripted across the boundary — every
  file is its own origin and none of them can touch another's storage —
  but a document can always clear its own, and a query string is the one
  thing that crosses.

  Returns true if this load was a withdrawal, so boot knows to skip the
  rest of its work."
  []
  (when-let [at (some-> (param forget-param) (int-or nil))]
    (when (> at (int-or (stored :epoch) 0))
      (wipe! at))
    true))

;; ---------------------------------------------------------------------
;; Rendering
;; ---------------------------------------------------------------------

(defn on-render! [k f] (add-watch state k (fn [_ _ _ _] (f))) (f))

(defn el [id] (.getElementById js/document id))

(defn q [node selector] (when node (.querySelector node selector)))

(defn q-all
  "Every match, as a vector. One argument searches the document; two
  searches inside a node.

  Written the long way because a NodeList is not a Clojure collection
  and array-seq is not in Scittle's bindings."
  ([selector] (q-all js/document selector))
  ([node selector]
   (if-not node
     []
     (let [nodes (.querySelectorAll node selector)]
       (mapv (fn [i] (.item nodes i)) (range (.-length nodes)))))))

(defn text! [node selector s]
  (when-let [t (q node selector)] (set! (.-textContent t) s)))

(defn on-click! [node selector f]
  (when-let [b (q node selector)] (set! (.-onclick b) f)))

(defn enable! [node selector on? label]
  (when-let [b (q node selector)]
    (set! (.-disabled b) (not on?))
    (when label (set! (.-textContent b) label))))

(defn show! [node selector on?]
  (when-let [t (q node selector)]
    (set! (.-display (.-style t)) (if on? "flex" "none"))))

(defn widget [k id f]
  (on-render! k (fn [] (when-let [node (el id)] (f node)))))

;; ---------------------------------------------------------------------
;; Withdrawing the record, everywhere
;;
;; Twelve documents, each its own origin, none able to read another's
;; storage. Every shared channel is shut: localStorage and IndexedDB are
;; origin-scoped, window.name is cleared on the way across, cookies do
;; not apply to a file, and an iframe gets its own origin so the parent
;; cannot script it.
;;
;; It can still LOAD it. A hidden frame pointed at a sibling chapter,
;; with a stamp in the query string, runs that chapter's own copy of
;; this file, which clears that chapter's own storage. Nothing crosses
;; the boundary except the URL, which is the one thing that was ever
;; going to.
;; ---------------------------------------------------------------------

(defn- sibling-pages
  "Every other chapter this page links to.

  Read off the sidebar rather than configured, because Quarto already
  puts the whole book in it and a hand-kept list of filenames is a list
  that goes stale the first time a chapter is renamed."
  []
  (let [here (.-pathname js/location)
        dir  (subs here 0 (inc (.lastIndexOf here "/")))]
    (->> (q-all "a[href]")
         (keep (fn [a]
                 (let [h (.getAttribute a "href")]
                   (when (and h
                              (not (str/starts-with? h "#"))
                              (not (re-find #"(?i)^(https?:|mailto:|javascript:)" h)))
                     (try
                       (let [u (js/URL. (.-href a))
                             p (.-pathname u)]
                         (when (and (str/starts-with? p dir)
                                    (str/ends-with? (str/lower-case p) ".html")
                                    (not= p here))
                           (str (.-protocol u) "//" (.-host u) p)))
                       (catch :default _ nil))))))
         distinct
         vec)))

(defn- sweep!
  "Tell every other chapter to withdraw, then call `done` once.

  Once, whatever happens: a frame that never fires must not leave the
  reader looking at a button that has stopped meaning anything, so there
  is a deadline as well as a count."
  [at done]
  (let [pages  (sibling-pages)
        fired  (atom false)
        finish (fn [] (when-not @fired (reset! fired true) (done)))]
    (if (empty? pages)
      (finish)
      (let [box  (.createElement js/document "div")
            left (atom (count pages))
            one  (fn [] (when (zero? (swap! left dec)) (finish)))]
        (js/console.info "016.09 — withdrawing from" (count pages) "other chapters")
        (set! (.-style box)
              "position:absolute;width:0;height:0;overflow:hidden;border:0;visibility:hidden")
        (.appendChild (.-body js/document) box)
        (doseq [p pages]
          (let [f (.createElement js/document "iframe")]
            (set! (.-onload f)  (fn [_] (one)))
            (set! (.-onerror f) (fn [_] (one)))
            (set! (.-src f) (str p "?" (clojure.core/name forget-param) "=" at))
            (.appendChild box f)))
        (js/setTimeout finish 5000)))))

(defn forget!
  "Forget everything, not the six things somebody remembered to list,
  and not only on this page.

  Emptying this document's storage was never the whole job: the reader
  was also sitting, complete, in the query string of every link on every
  other chapter and in every entry of the back history, and the next
  navigation put them back. So this clears here, stamps when, and then
  visits every other chapter to say so. The stamp is what makes a link
  written before the withdrawal refuse to be believed afterwards.

  It also says so while it is happening. It used to do all of this in
  silence and then reload, which is indistinguishable from a button that
  does nothing except reload."
  []
  (let [at  (quot (.now js/Date) 1000)
        btn (q js/document ".pc-forget")]
    (when btn
      (set! (.-disabled btn) true)
      (set! (.-textContent btn) "Withdrawing the record…"))
    (wipe! at)
    (sweep! at
            (fn []
              (let [u (current-url)]
                (doseq [k (concat [carry-param forget-param] legacy-params)]
                  (.delete (.-searchParams u) (clojure.core/name k)))
                (.set (.-searchParams u) (clojure.core/name withdrawn-param) "1")
                (set! (.-href js/location) (.toString u)))))))

(defn- note-withdrawal!
  "One line, on the way back up, and then the button goes back to
  offering. The confirmation cannot be stored, because storing things is
  what has just been undone — so it is read off the query string once,
  before anything else in boot is allowed to tidy the query string away."
  [withdrew?]
  (when withdrew?
    (js/console.info
     "016.09 — record withdrawn from this chapter and from every chapter linked from it.")
    (when-let [b (q js/document ".pc-forget")]
      (set! (.-textContent b) "Record withdrawn")
      (js/setTimeout (fn [] (set! (.-textContent b) "Forget everything")) 5000))))

;; ---------------------------------------------------------------------
;; Export
;; ---------------------------------------------------------------------

(defn token
  "What this reader looks like from outside. Recomputed rather than
  stored, so it cannot disagree with the state it is describing.

  Guarded, because this runs on every render and inside `decorate-links!`,
  and a codec that throws on one unexpected key should cost the reader a
  shareable link and nothing else."
  []
  (try
    (let [{:keys [name companion echo rolls deeds todo todo-seq]} @state]
      (fedoras.carry/pack
       {:name      name
        :companion (when companion (select-keys companion [:n :given]))
        :echo      echo
        :rolls     rolls
        :deeds     deeds
        :epoch     (epoch)
        :todo (when (seq todo) {:seq (or todo-seq 0) :items todo})}))
    (catch :default e
      (js/console.warn "fedoras: could not pack the carry token —" e)
      nil)))

(defn decorate-links!
  "Every internal link carries one field.

  Two things were wrong before this and both of them mattered. It ran
  inline, at parse time, so any link below the script tag did not exist
  yet — which is every prev-and-next arrow Quarto puts in the footer.
  And it copied the query string rather than the state, so a page opened
  from storage handed on nothing."
  []
  (when-let [t (token)]
    (doseq [a (q-all "a[href]")]
      (let [h (.getAttribute a "href")]
        (when (and h
                   (not (str/starts-with? h "#"))
                   (not (re-find #"(?i)^(https?:|mailto:|javascript:)" h)))
          (try
            (let [lu (js/URL. (.-href a))
                  sp (.-searchParams lu)]
              (doseq [k legacy-params] (.delete sp (clojure.core/name k)))
              (.set sp (clojure.core/name carry-param) t)
              (set! (.-href a) (.toString lu)))
            (catch :default _ nil)))))))

(defn- rewrite-address!
  "Put the token in this page's own address bar and take the old fields
  off it, so a reader who arrived on a legacy link stops looking at
  their own Echo."
  []
  (when-let [t (token)]
    (set-params! (into {(clojure.core/name carry-param) t}
                       (map (fn [k] [k nil])
                            (concat [forget-param withdrawn-param] legacy-params))))))

;; ---------------------------------------------------------------------
;; Tokens
;;
;; It used to be a chain of str/replace calls, which is why {pc-last}
;; worked in a scene and rendered as the literal characters {pc-last}
;; inside a dice passage.
;;
;; Case follows the token: {ac} is `ted`, {Ac} is `Ted`, {AC} is `TED`,
;; so a passage can start a sentence or write a cue line without the
;; author reaching for a second token. Note that this makes {PC} in an
;; already-written scene render as OWEN rather than Owen; the old regex
;; was case-insensitive and threw the information away.
;;
;; An unkown token is passed over: A brace in a passage that is not
;; something this understands comes out exactly as it went in, so this
;; is safe to run over arbitrary prose.
;; ---------------------------------------------------------------------

(def ^:private token-re #"\{([A-Za-z]+)(-[A-Za-z-]+)?\}")

(defn- apply-case [head s]
  (cond
    (str/blank? s)                 s
    (= head (str/upper-case head)) (str/upper-case s)
    (= (subs head 0 1) (str/upper-case (subs head 0 1)))
    (str (str/upper-case (subs s 0 1)) (subs s 1))
    :else                          s))

(defn render
  "Replace every {token} the resolver knows. `resolve` is called with the
  lower-cased head and the lower-cased tail — \"ac\" and \"-plural\" for
  {ac-plural} — and returns a string or nil."
  [resolve s]
  (str/replace (or s "") token-re
               (fn [m]
                 (let [[whole head tail] (if (string? m) [m m nil] m)]
                   (or (some->> (resolve (str/lower-case head)
                                         (some-> tail str/lower-case))
                                (apply-case head))
                       whole)))))

(defn- string-hash
  "Stable across loads, which is the point: the same sentence picks the
  same alternative name every time the reader opens the page, so nothing
  reads as though it were rolled twice."
  [s]
  (reduce (fn [a i] (mod (+ (* 31 a) (.charCodeAt s i)) 1000003))
          7 (range (count s))))

(defn- an [s]
  (str (if (re-find #"(?i)^[aeiou]" (str s)) "an " "a ") s))

(defn- animal-tokens
  "What a passage may say about the animal. The reader's own name for it
  is a name; everything else is the species, because a reader who called
  a marmot Ted did not thereby decide that two of them are Teds."
  [source tail]
  (when-let [{:keys [animal plural taxonomy collective other-names name named?]}
             (companion)]
    (let [kind (str/lower-case (or animal ""))]
      (case (or tail "-name")
        "-name"     (if named? name (str "the " kind))
        "-kind"     kind
        "-a"        (an kind)
        "-plural"   (if (str/blank? plural) (str kind "s") (str/lower-case plural))
        "-many"     (if (str/blank? collective)
                      (str "several " (if (str/blank? plural) (str kind "s") plural))
                      (str (an collective) " of "
                           (if (str/blank? plural) (str kind "s") plural)))
        "-taxonomy" (str/lower-case (or taxonomy kind))
        "-given"    (if named? name "")
        "-other"    (if (seq other-names)
                      (nth other-names (mod (string-hash source) (count other-names)))
                      (str "the " kind))
        nil))))

(defn- reader-tokens [source]
  (let [[f l] (reader-name)]
    (fn [head tail]
      (case head
        "pc" (case (or tail "-first")
               "-first" f
               "-full"  (str f " " l)
               "-last"  l
               nil)
        "ac" (or (animal-tokens source tail)
                 ;; no roll yet, and a passage still has to read
                 (case (or tail "-name")
                   ("-name" "-kind" "-other") "the animal"
                   "-a"                        "an animal"
                   "-plural"                   "animals"
                   "-many"                     "several animals"
                   "-taxonomy"                 "animal"
                   "-given"                    ""
                   nil))
        nil))))

(defn fill-with
  "Put the reader into a passage, plus whatever else the caller knows.
  The dice box passes the die in this way."
  [extra s]
  (let [own (reader-tokens s)]
    (render (fn [h t] (or (extra h t) (own h t))) s)))

(defn fill
  "Put the reader into a passage. The only place any of these are
  resolved."
  [s]
  (fill-with (fn [_ _] nil) s))

(defn animal-label
  "Kept for anything still asking. The display name, as stored, not
  shouted — the old one upper-cased and every caller lower-cased it
  again somewhere else."
  []
  (when-let [c (companion)] (:name c)))

;; ---------------------------------------------------------------------
;; The carry-over
;;
;; What a reader takes to the next book.
;; ---------------------------------------------------------------------

(def on-the-record
  "What goes in the token. Everything else is furniture.

  A key is on this list because it is small and because losing it would
  lose the reader's year. Adding one is a decision about how long every
  link on every page is going to be, so it is a list and not a
  convention."
  #{:pc :companion :echo :rolls :deeds :todo})

(defn kept
  "Read something the reader left in a room. No widget that calls this
  needs to know anything about tokens."
  [k]
  (stored k))

(defn keep!
  "Leave something in a room. It stays where it was made."
  [k v]
  (store! k v)
  (when (contains? on-the-record k) (refresh!))
  v)

(def ^:private alphabet "23456789ABCDEFGHJKLMNPQRSTUVWXYZ")

(defn- b32 [n len]
  (loop [n n i 0 out ""]
    (if (= i len)
      out
      (recur (quot n 32) (inc i)
             (str (nth alphabet (mod n 32)) out)))))

;; ---------------------------------------------------------------------
;; The name and the animal, on every page
;; ---------------------------------------------------------------------

(defn- paint-name []
  (let [[f l] (reader-name)]
    (doseq [node (q-all ".pc-name")]
      (set! (.-textContent node)
            (apply-case (or (.getAttribute node "data-case") "pc")
                        (case (.getAttribute node "data-pc")
                          "-full" (str f " " l) "-last" l f))))
    (when-let [i (el "pc-first")] (when (str/blank? (.-value i)) (set! (.-value i) f)))
    (when-let [i (el "pc-last")]  (when (str/blank? (.-value i)) (set! (.-value i) l)))))

(defn- paint-companion
  "Each span carries the token that made it, so {ac-plural} written in a
  scene at build time still says `marmots` after the reader rolls a
  marmot. It used to carry nothing and every span got the same word."
  []
  (doseq [node (q-all ".ac-name")]
    (let [tail (or (.getAttribute node "data-ac") "-name")
          head (or (.getAttribute node "data-case") "ac")]
      (set! (.-textContent node)
            (or (some->> ((reader-tokens (or (.-textContent node) "")) "ac" tail)
                         (apply-case head))
                "the animal")))))

(defn- paint-tokens []
  (doseq [node (q-all ".tok")]
    (set! (.-textContent node) (fill (.getAttribute node "data-tok")))))

;; ---------------------------------------------------------------------
;; Ending / Standing
;; ---------------------------------------------------------------------

(def deed-roll
  [:nerve                ;; a natural twenty with the Echo at nought
   :named-it             ;; the animal was given a name and kept it
   :kept-the-programmes  ;; both sheets went in the bag and stayed there
   :no-name-for-the-box  ;; nothing was spoken into the undertaker's box
   :read-it-back         ;; the ledger was read against the room out loud
   :opened-the-cage
   :held-the-door
   :went-back-for-it
   :signed-nothing
   :walked-out
   :told-a-new-one       ;; Ariathne was paid in something that was theirs
   :said-why])           ;; the chancellor asked, and got a true answer

(def ^:private deed-bits 15)

(defn- deed-mask []
  (let [held (deeds)]
    (reduce (fn [acc [index deed]]
              (if (contains? held deed)
                (+ acc (bit-shift-left 1 index))
                acc))
            0
            (map-indexed vector deed-roll))))

(defn- deeds-from-mask [mask]
  (set (keep-indexed (fn [index deed]
                       (when (pos? (bit-and mask (bit-shift-left 1 index)))
                         deed))
                     deed-roll)))

(def band-code {:closed 0 :reconciled 1 :outstanding 2})
(def code-band {0 :closed 1 :reconciled 2 :outstanding})

(defn ending-band []
  (let [reader-standing (standing)]
    (cond
      (>= reader-standing 4) :outstanding
      (>= reader-standing 0) :reconciled
      :else                  :closed)))

(defn- paint-standing!
  "One class on the body, so a chapter that forks three ways does it in
  the stylesheet instead of in nine lines of DOM work per passage. A page
  with no class on it shows :reconciled, which is what the print edition
  gets and what the table actually got.

  This existed and was never wired to anything, which meant every
  `sp/by-standing` on every page was rendering all three endings at
  once, or none."
  []
  (when-let [body (.-body js/document)]
    (let [classes (.-classList body)]
      (doseq [old ["standing-closed" "standing-reconciled" "standing-outstanding"]]
        (.remove classes old))
      (.add classes (str "standing-" (clojure.core/name (ending-band)))))))

(def code-version 3)

(defn- check-char [body]
  (nth alphabet
       (mod (reduce + (map (fn [c] (.indexOf alphabet c)) body)) 32)))

(defn carry-code
  "What this reader looks like to the next book, as an accession line."
  []
  (let [n    (+ (* code-version 4294967296)
                (* (get band-code (ending-band) 1) 1073741824)
                (* (min 31 (or (:n (companion)) 0)) 33554432)
                (* (min 31 (echo)) 1048576)
                (* (deed-mask) 32)
                (min 31 (quot (score) 10)))
        body (b32 n 7)]
    (str "016.14 · " (registration) " · "
         (subs body 0 4) "-" (subs body 4) (check-char body))))

(defn read-carry
  "The other side of it, for the book after this one."
  [s]
  (let [cleaned (-> (str s) str/upper-case (str/replace #"[^2-9A-Z]" ""))
        tail    (subs cleaned (max 0 (- (count cleaned) 8)))]
    (when (= 8 (count tail))
      (let [body (subs tail 0 7)
            n    (reduce (fn [acc c] (+ (* 32 acc) (.indexOf alphabet c))) 0 body)]
        (when (= (nth tail 7) (check-char body))
          {:version     (quot n 4294967296)
           :band        (get code-band (mod (quot n 1073741824) 4))
           :companion   (mod (quot n 33554432) 32)
           :echo        (mod (quot n 1048576) 32)
           :deeds       (deeds-from-mask (mod (quot n 32) (bit-shift-left 1 deed-bits)))
           :score-floor (* 10 (mod n 32))})))))

;; ---------------------------------------------------------------------
;; Is any of this working
;;
;; Type fedoras.reader.diagnose_BANG_() in the console. Faster than
;; opening the storage inspector and reading JSON, and it says whether
;; the token agrees with the storage, which is the failure that cost an
;; animal.
;; ---------------------------------------------------------------------

(defn diagnose! []
  (let [t (param carry-param)]
    (js/console.log "storage works:  " (storage-works?))
    (js/console.log "origin:         " (.-origin js/location)
                    (if (= "file:" (.-protocol js/location))
                      "— every page may have its own storage"
                      "— one bucket for the whole book"))
    (js/console.log "name:           " (pr-str (reader-name)))
    (js/console.log "companion:      " (pr-str (companion)))
    (js/console.log "  raw storage:  " (pr-str (stored :companion)))
    (js/console.log "roster loaded:  " (count @animal-names) "animals")
    (js/console.log "echo / fortune: " (echo) "/" (fortune) " → standing" (standing))
    (js/console.log "withdrawn at:   " (if (pos? (epoch)) (epoch) "never")
                    (if carries-epoch?
                      ""
                      "— CODEC DROPS THE STAMP, carry.cljs is out of date"))
    (js/console.log "rolls:          " (pr-str (rolls)))
    (js/console.log "token on url:   " (or t "none"))
    (when t
      (js/console.log "  unpacks to:   "
                      (pr-str (try (fedoras.carry/unpack t)
                                   (catch :default e (str "THREW: " e))))))
    (js/console.log "token we'd emit:" (or (token) "none — pack failed"))
    :ok))

;; A handle on `window`, because Scittle keeps its namespaces inside the
;; SCI context and the browser console cannot see into it. There is no
;; `fedoras.reader.diagnose_BANG_()` to call — that is the shape a
;; compiled build would have, and this is an interpreted one.
;;
;;   fedoras.diagnose()
;;   fedoras.forget()
;;
;; and for anything else, scittle.core.eval_string("(fedoras.reader/...)")
(set! (.-fedoras js/window)
      (js-obj "diagnose" (fn [] (diagnose!))
              "state"    (fn [] (clj->js @state))
              "token"    (fn [] (token))
              "forget"   (fn [] (forget!))))

;; ---------------------------------------------------------------------
;; Boot
;; ---------------------------------------------------------------------

(if (withdraw-if-asked!)
  ;; a hidden frame, doing one job. no token, no links, no painting.
  (refresh!)
  ;; read before anything registers a watch: `on-render!` calls its
  ;; function once on the spot, and the links watch tidies the query
  ;; string, so the flag is gone by the time the DOM is ready
  (let [withdrew? (boolean (param withdrawn-param))]
    (when-not carries-epoch?
      (js/console.warn
       (str "fedoras: carry.cljs does not round-trip the withdrawal stamp. "
            "`forget!` will clear this chapter and every chapter linked from it, "
            "but a stale link will not be refused afterwards. "
            "Update resources/fedoras/carry.cljs — `pack` needs :epoch in its "
            "destructuring and :g in its payload, and `unpack` needs :epoch back out.")))
    (import-token!)
    (refresh!)

    (on-render! ::name paint-name)
    (on-render! ::companion paint-companion)
    (on-render! ::tokens paint-tokens)
    (on-render! ::standing paint-standing!)
    (on-render! ::links (fn [] (decorate-links!) (rewrite-address!)))

    (if (= "loading" (.-readyState js/document))
      (.addEventListener js/document "DOMContentLoaded"
                         (fn [_]
                           (note-withdrawal! withdrew?)
                           (decorate-links!)
                           (paint-standing!)))
      (do (note-withdrawal! withdrew?)
          (decorate-links!)))))