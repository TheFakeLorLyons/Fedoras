(ns fedoras.continue
  "What the reader takes to the next book.

  Every field is a numbered entry, and the number is the field's identity
  on the wire, carried alongside a wire type that says how long the field
  is. A reader that meets a number it has never heard of reads its length
  and steps over it. So a code issued by a later edition of this book
  still opens in an earlier edition of the next one, and loses only what
  the earlier edition never knew about. A number is never reused and
  never changed. A field nobody wants any more stays in the schema and
  stops being written.
"
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; What happened, as opposed to what was done well
;;
;; A bit position, and therefore permanent. Append, never reorder, never
;; remove. Distinct from the deed roll on purpose: a deed is worth a
;; point of fortune, and a milestone is worth nothing at all except to
;; the next book, which may want to know whether the reader ever met Eli
;; before it puts somebody in a room who did.
;; ---------------------------------------------------------------------

(def milestone-roll
  [:met-eli
   :eli-goals-met
   :eli-saved
   :companion-rerolled      ;; the first one is behind a door somewhere
   :session-four-played
   :ariathne-spoke
   :reached-the-chancellor
   :nerve-broken
   :threshold-retrieved
   :registrar-unmasked])

;; ---------------------------------------------------------------------
;; The schema
;;
;; One through fifteen are the core set. Fifteen is held back for it.
;; Sixteen upward are notes: short facts somebody decided the next book
;; should know, each written by `fedoras.reader/note!` under its key.
;; ---------------------------------------------------------------------

(def schema
  [{:tag 1  :key :band           :type :enum  :values [:closed :reconciled :outstanding]}
   {:tag 2  :key :companion      :type :uint}
   {:tag 3  :key :companion-name :type :text  :limit 24}
   {:tag 4  :key :echo           :type :uint}
   {:tag 5  :key :deeds          :type :flags :over fedoras.reader/deed-roll}
   {:tag 6  :key :score          :type :uint}
   {:tag 7  :key :first-name     :type :text  :limit 32}
   {:tag 8  :key :last-name      :type :text  :limit 32}
   {:tag 9  :key :milestones     :type :flags :over milestone-roll}
   {:tag 10 :key :checks         :type :uint}
   {:tag 12 :key :rewrites       :type :uint}
   {:tag 13 :key :crits          :type :uint}
   {:tag 14 :key :fumbles        :type :uint}

   ;; notes. this one rides every link on every page until the code is
   ;; issued, because a note has to reach the last chapter on a book read
   ;; off a disk, so it is kept short.
   {:tag 16 :key :the-story      :type :text  :limit 60 :note? true}])

(def defaults
  "What an absent field means. A code is allowed to leave out anything it
  has nothing to say about, and this is what nothing was."
  {:echo 0 :deeds #{} :score 0 :milestones #{}
   :checks 0 :rewrites 0 :crits 0 :fumbles 0})

(def code-version 1)

(def ^:private alphabet "23456789ABCDEFGHJKLMNPQRSTUVWXYZ")

(def ^:private office "016.15")

(defn- seed
  "Keyed to the registration, and offset so that a carry token and a
  continue code can never be unmasked with each other's key by mistake."
  []
  (bit-xor (js/parseInt (fedoras.reader/registration) 10) 1609))

;; ---------------------------------------------------------------------
;; Bytes, as Clojure vectors of integers
;;
;; Typed arrays only at the two boundaries that need them: the text
;; encoder, and carry's mask, which is the one generator in the project.
;; ---------------------------------------------------------------------

(defn- typed [byte-vector]
  (let [out (js/Uint8Array. (count byte-vector))]
    (dotimes [i (count byte-vector)] (aset out i (nth byte-vector i)))
    out))

(defn- untyped [array]
  (mapv (fn [i] (aget array i)) (range (.-length array))))

(defn- utf8 [s] (untyped (.encode (js/TextEncoder.) (str s))))

(defn- from-utf8 [byte-vector] (.decode (js/TextDecoder. "utf-8") (typed byte-vector)))

(defn- masked [byte-vector] (untyped (fedoras.carry/mask (typed byte-vector) (seed))))

(defn- power-of-two [n] (reduce * 1 (repeat n 2)))

(defn- clip
  "At most `limit` characters, counted as characters, so a name with an
  accent or an emoji in it is cut between letters and not inside one."
  [s limit]
  (let [points (js/Array.from (str s))]
    (if (<= (.-length points) limit)
      (str s)
      (.join (.slice points 0 limit) ""))))

(defn- index-of
  "By value. Keywords are not interned in an interpreted build, so a
  search that compares identity finds nothing."
  [coll value]
  (first (keep-indexed (fn [i item] (when (= item value) i)) coll)))

;; ---------------------------------------------------------------------
;; Varints
;;
;; Seven bits a byte, the high bit saying there is more. Arithmetic
;; rather than bit operations, because JavaScript's bit operations are
;; thirty-two bits wide and a flags field on a long roll is not.
;; ---------------------------------------------------------------------

(defn- varint [n]
  (loop [n (max 0 (js/Math.floor n)) out []]
    (let [low  (mod n 128)
          rest (quot n 128)]
      (if (zero? rest)
        (conj out low)
        (recur rest (conj out (+ low 128)))))))

(defn- read-varint
  "The number starting at `pos`, and where the next thing starts. Runs off
  the end of a short code by throwing, which the caller catches."
  [bytes pos]
  (loop [pos pos scale 1 acc 0]
    (let [b (nth bytes pos)]
      (if (< b 128)
        [(+ acc (* b scale)) (inc pos)]
        (recur (inc pos) (* scale 128) (+ acc (* (- b 128) scale)))))))

;; ---------------------------------------------------------------------
;; Flags
;; ---------------------------------------------------------------------

(defn- flag-mask [roll held]
  (let [held (set held)]
    (reduce (fn [acc [index flag]]
              (if (contains? held flag) (+ acc (power-of-two index)) acc))
            0
            (map-indexed vector roll))))

(defn- flags-from [roll mask]
  (set (keep-indexed (fn [index flag]
                       (when (odd? (quot mask (power-of-two index))) flag))
                     roll)))

;; ---------------------------------------------------------------------
;; Fields
;;
;; A key is the tag times four plus a wire type. Nought is a varint and
;; one is a length followed by that many bytes. Two bits, so there is
;; room for two more kinds before the scheme has to change.
;; ---------------------------------------------------------------------

(def ^:private wire-type {:uint 0 :enum 0 :flags 0 :text 1})

(defn- field-key [{:keys [tag type]}] (+ (* tag 4) (wire-type type)))

(defn- encode-field [{:keys [type over values limit] :as spec} value]
  (into (varint (field-key spec))
        (case type
          :uint  (varint (or value 0))
          :enum  (varint (or (index-of values value) 0))
          :flags (varint (flag-mask over value))
          :text  (let [encoded (utf8 (clip value (or limit 32)))]
                   (into (varint (count encoded)) encoded)))))

(defn- decode-value [{:keys [type over values]} raw]
  (case type
    :uint  raw
    :enum  (nth values raw nil)
    :flags (flags-from over raw)
    :text  (from-utf8 raw)))

(defn- decode-fields
  "Every field the schema knows, and nothing it does not. An unknown
  number is stepped over by its wire type, which is the entire mechanism
  by which a newer code opens in an older book."
  [body]
  (let [by-tag (into {} (map (fn [spec] [(:tag spec) spec])) schema)]
    (loop [pos 0 out {}]
      (if (>= pos (count body))
        out
        (let [[k pos] (read-varint body pos)
              spec    (get by-tag (quot k 4))
              wire    (mod k 4)
              known?  (and spec (= wire (wire-type (:type spec))))]
          (case wire
            0 (let [[raw pos] (read-varint body pos)]
                (recur pos (if known? (assoc out (:key spec) (decode-value spec raw)) out)))
            1 (let [[n pos] (read-varint body pos)
                    chunk   (subvec body pos (+ pos n))]
                (recur (+ pos n)
                       (if known? (assoc out (:key spec) (decode-value spec chunk)) out)))
            (throw (ex-info "fedoras: a wire type this book has not heard of"
                            {:wire wire}))))))))

;; ---------------------------------------------------------------------
;; The check
;;
;; Fletcher's sixteen-bit sum, which notices a swapped pair of
;; characters, which is the mistake a person copying a code actually
;; makes.
;; ---------------------------------------------------------------------

(defn- fletcher [byte-vector]
  (reduce (fn [[a b] byte]
            (let [a (mod (+ a byte) 255)]
              [a (mod (+ b a) 255)]))
          [0 0]
          byte-vector))

;; ---------------------------------------------------------------------
;; Base thirty-two, over bytes of any length
;; ---------------------------------------------------------------------

(defn- to-base32 [byte-vector]
  (loop [remaining (seq byte-vector) buffer 0 bits 0 out []]
    (cond
      (>= bits 5)
      (let [shift (- bits 5)
            scale (power-of-two shift)]
        (recur remaining (mod buffer scale) shift
               (conj out (nth alphabet (quot buffer scale)))))

      remaining
      (recur (next remaining) (+ (* buffer 256) (first remaining)) (+ bits 8) out)

      (pos? bits)
      (apply str (conj out (nth alphabet (* buffer (power-of-two (- 5 bits))))))

      :else
      (apply str out))))

(defn- from-base32 [s]
  (loop [remaining (seq s) buffer 0 bits 0 out []]
    (if-not remaining
      out
      (let [buffer (+ (* buffer 32) (.indexOf alphabet (first remaining)))
            bits   (+ bits 5)]
        (if (>= bits 8)
          (let [shift (- bits 8)
                scale (power-of-two shift)]
            (recur (next remaining) (mod buffer scale) shift (conj out (quot buffer scale))))
          (recur (next remaining) buffer bits out))))))

(defn- groups [s]
  (str/join "-" (map (fn [chunk] (apply str chunk)) (partition-all 4 s))))

(defn- body-of
  "The part after the last separator, upper-cased and cut down to the
  alphabet, so a reader can paste the whole line with the office number
  on the front of it, or only the groups, or the groups with spaces in."
  [s]
  (let [text (str s)
        cut  (.lastIndexOf text "\u00b7")]
    (->> (str/upper-case (if (neg? cut) text (subs text (inc cut))))
         (filter (fn [c] (str/includes? alphabet c)))
         (apply str))))

;; ---------------------------------------------------------------------
;; The reader, as the schema sees them
;; ---------------------------------------------------------------------

(defn- present? [value]
  (cond
    (nil? value)    false
    (string? value) (not (str/blank? value))
    (coll? value)   (boolean (seq value))
    :else           true))

(defn- record
  "Everything the core fields describe, read out of fedoras.reader at the
  moment the code is issued."
  []
  (let [[first-name last-name] (fedoras.reader/reader-name)
        companion              (fedoras.reader/companion)
        rolls                  (vals (fedoras.reader/rolls))]
    (merge
     {:band       (fedoras.reader/ending-band)
      :echo       (fedoras.reader/echo)
      :deeds      (fedoras.reader/deeds)
      :score      (fedoras.reader/score)
      :first-name first-name
      :last-name  last-name
      :milestones (fedoras.reader/marks)
      :checks     (count rolls)
      :rewrites   (reduce + 0 (keep :rewrites rolls))
      :crits      (count (filter (fn [r] (= 20 (:roll r))) rolls))
      :fumbles    (count (filter (fn [r] (= 1 (:roll r))) rolls))}
     (when companion
       {:companion      (:n companion)
        :companion-name (when (:named? companion) (:given companion))}))))

(defn- encode [values notes]
  (let [body   (vec (mapcat (fn [spec]
                              (let [value (if (:note? spec)
                                            (get notes (:key spec))
                                            (get values (:key spec)))]
                                (when (present? value) (encode-field spec value))))
                            schema))
        framed (into [code-version] (masked body))
        [a b]  (fletcher framed)]
    (to-base32 (conj framed a b))))

(defn continue-code
  "The year, as a code."
  []
  (str office " \u00b7 " (fedoras.reader/registration) " \u00b7 "
       (groups (encode (record) (fedoras.reader/notes)))))

(defn read-continue
  "A continue code back to the record it describes, or nil. Nil for
  anything that does not reconcile: a character wrong, a code from
  somewhere else, an accession line, somebody's shopping list."
  [s]
  (try
    (let [bytes (from-base32 (body-of s))
          size  (count bytes)]
      (when (>= size 3)
        (let [framed (subvec bytes 0 (- size 2))
              [a b]  (fletcher framed)]
          (when (and (= a (nth bytes (- size 2))) (= b (nth bytes (dec size))))
            (merge defaults
                   (decode-fields (masked (subvec framed 1)))
                   {:version (first framed)})))))
    (catch :default _ nil)))

;; ---------------------------------------------------------------------
;; Taking a code in
;; ---------------------------------------------------------------------

(defn- adopt!
  "Last year, merged forward through the reader's own setters, so the
  one-atom rule holds and every watch on the page fires once it lands.

  Nothing is lowered. The Echo takes the larger. Deeds and milestones
  are unions. A name or an animal is taken only where there is not one
  already, and a note only where this browser has not written its own.
  The code itself is kept whole under :continued-code, and the next book
  reads the rest of the year out of that with `read-continue`, typed,
  rather than out of a JSON copy that has turned its sets into lists."
  [text record]
  (fedoras.reader/keep! :continued-code (str/trim (str text)))
  (when (and (nil? (fedoras.reader/stored :pc)) (:first-name record))
    (fedoras.reader/set-name! (:first-name record) (:last-name record)))
  (when (and (:companion record) (nil? (fedoras.reader/companion)))
    (fedoras.reader/set-companion! (:companion record) (or (:companion-name record) "")))
  (fedoras.reader/set-echo! (max (fedoras.reader/echo) (or (:echo record) 0)))
  (doseq [deed (:deeds record)] (fedoras.reader/did! deed))
  (doseq [milestone (:milestones record)] (fedoras.reader/mark! milestone))
  (let [held (fedoras.reader/notes)]
    (doseq [spec schema
            :when (:note? spec)
            :let [value (get record (:key spec))]
            :when (and (present? value) (not (contains? held (:key spec))))]
      (fedoras.reader/note! (:key spec) value))))

(defn- accession->record [{:keys [band companion echo deeds score-floor]}]
  {:band band :companion (when (pos? (or companion 0)) companion)
   :echo echo :deeds deeds :score score-floor})

(defn- token-in
  "A carry token, bare or still inside the link it was copied from."
  [text]
  (let [param (str (name fedoras.reader/carry-param) "=")
        cut   (.indexOf text param)]
    (if (neg? cut)
      (when (re-matches #"[A-Za-z0-9_-]{16,}" text) text)
      (first (str/split (subs text (+ cut (count param))) #"[&#\s]")))))

(defn- restore-token!
  "Hand the token to this page's own import by opening the page on it.
  Every rule `fedoras.reader/merge-token!` has about recency applies,
  including the one that refuses a link from before a withdrawal."
  [token]
  (let [url (js/URL. (.-href js/location))]
    (.set (.-searchParams url) (name fedoras.reader/carry-param) token)
    (set! (.-href js/location) (.toString url))))

(defn import!
  "Whatever the reader pasted, taken for what it turns out to be.

    a continue code    the year, whole
    an accession line  the short one, with less in it
    a carry token      the whole browser state, from another build or
                       another browser, handed to the page's own import

  Returns which it was, or nil for none of them."
  [s]
  (let [text  (str/trim (str s))
        token (token-in text)]
    (when-not (str/blank? text)
      (if-let [record (read-continue text)]
        (do (adopt! text record) :continue)
        (if-let [short (when (= 8 (count (body-of text))) (fedoras.reader/read-carry text))]
          (do (adopt! text (accession->record short)) :accession)
          (when (and token (fedoras.carry/unpack token))
            (restore-token! token)
            :token))))))

;; ---------------------------------------------------------------------
;; On the page
;; ---------------------------------------------------------------------

(defn- say! [node text] (fedoras.reader/text! node ".continue-said" text))

(defn- copy! [node text]
  (try
    (-> (.writeText (.-clipboard js/navigator) text)
        (.then (fn [_] (say! node "Copied.")))
        (.catch (fn [_] (say! node "Select it and copy it by hand."))))
    (catch :default _
      (say! node "Select it and copy it by hand."))))

(defn- download! [text filename]
  (let [blob   (js/Blob. #js [text] #js {:type "text/plain;charset=utf-8"})
        url    (js/URL.createObjectURL blob)
        anchor (.createElement js/document "a")]
    (set! (.-href anchor) url)
    (set! (.-download anchor) filename)
    (.click anchor)
    (.revokeObjectURL js/URL url)))

(defn mount-card!
  "The code, issued. Live: it is recomputed on every change to the
  reader, so a reader who does one more thing before leaving the page
  leaves with a code that knows about it."
  [{:keys [id]}]
  (fedoras.reader/widget
   (keyword "continue-card" id) id
   (fn [node]
     (let [code (continue-code)]
       (fedoras.reader/text! node ".continue-code" code)
       (fedoras.reader/on-click! node ".continue-copy" (fn [_] (copy! node code)))
       (fedoras.reader/on-click! node ".continue-save"
                                 (fn [_] (download! (str code "\n") "continued.txt")))))))

(defn mount-import!
  "Where a code goes in. The next book puts this on its first page. This
  one carries it too, for a reader who has moved to another browser, and
  for an author whose book keeps arriving at a different address."
  [{:keys [id]}]
  (fedoras.reader/widget
   (keyword "continue-import" id) id
   (fn [node]
     (fedoras.reader/on-click!
      node ".continue-take"
      (fn [_]
        (let [box (fedoras.reader/q node ".continue-input")]
          (say! node
                (case (import! (.-value box))
                  :continue  "016.15. Entry reconciled. Carried forward."
                  :accession "016.15. Entry reconciled from the short form. Carried forward."
                  :token     "016.15. Reinstating the record from the link."
                  "016.09. Entry does not reconcile. Nothing carried forward."))))))))

(defn watch-mark!
  "A milestone the reader reaches by reading to it. Recorded when this
  point in the page comes into view, because opening the last chapter to
  look at it is not the same as having got there."
  [{:keys [id milestone]}]
  (when-let [node (fedoras.reader/el id)]
    (if (.-IntersectionObserver js/window)
      (let [observer (js/IntersectionObserver.
                      (fn [entries self]
                        (when (.some entries (fn [entry] (.-isIntersecting entry)))
                          (fedoras.reader/mark! milestone)
                          (.disconnect self))))]
        (.observe observer node))
      (fedoras.reader/mark! milestone))))
