(ns fedoras.carry
  "What the reader carries between chapters."
  (:require [clojure.string :as str]))

(def ^:private book-key
  "The registration on the front of the file. It is not a secret and it
  is not pretending to be one — it is the number this is keyed to, and
  it is the same number a reader has been looking at since August."
  3812)

;; ---------------------------------------------------------------------
;; Bytes
;; ---------------------------------------------------------------------

(defn- to-bytes [s] (.encode (js/TextEncoder.) s))

(defn- from-bytes [b] (.decode (js/TextDecoder. "utf-8") b))

(defn- bytes->str [b]
  (loop [i 0 acc ""]
    (if (>= i (.-length b))
      acc
      (recur (inc i) (str acc (js/String.fromCharCode (aget b i)))))))

(defn- str->bytes [s]
  (let [out (js/Uint8Array. (count s))]
    (dotimes [i (count s)] (aset out i (.charCodeAt s i)))
    out))

;; ---------------------------------------------------------------------
;; The keystream
;;
;; The same small xorshift the dice and the floors use, so there is one
;; generator in this project and not four.
;; ---------------------------------------------------------------------

(defn- keystream [n seed]
  (let [out (js/Uint8Array. n)]
    (loop [i 0 x (bit-or 1 (bit-and seed 0x7fffffff))]
      (if (>= i n)
        out
        (let [x (bit-and (bit-xor x (bit-shift-left x 13)) 0x7fffffff)
              x (bit-and (bit-xor x (unsigned-bit-shift-right x 17)) 0x7fffffff)
              x (bit-and (bit-xor x (bit-shift-left x 5)) 0x7fffffff)]
          (aset out i (bit-and x 0xff))
          (recur (inc i) x))))))

(defn- mask [b seed]
  (let [k (keystream (.-length b) seed)
        out (js/Uint8Array. (.-length b))]
    (dotimes [i (.-length b)] (aset out i (bit-xor (aget b i) (aget k i))))
    out))

;; ---------------------------------------------------------------------
;; Base64, url-safe
;; ---------------------------------------------------------------------

(defn- b64 [b]
  (-> (js/btoa (bytes->str b))
      (str/replace "+" "-")
      (str/replace "/" "_")
      (str/replace "=" "")))

(defn- un-b64 [s]
  (let [t   (-> s (str/replace "-" "+") (str/replace "_" "/"))
        pad (case (mod (count t) 4) 2 "==" 3 "=" "")]
    (str->bytes (js/atob (str t pad)))))

;; ---------------------------------------------------------------------
;; Rolls, short
;; ---------------------------------------------------------------------

(defn- pack-rolls
  "[roll mod rewrites], shortest form. A roll with no modifier and no
  rewrites is one number."
  [rolls]
  (into {}
        (for [[id {:keys [roll mod rewrites]}] rolls
              :when roll]
          [id (cond
                (pos? (or rewrites 0))   [roll (or mod 0) rewrites]
                (not (zero? (or mod 0))) [roll mod]
                :else                    [roll])])))

(defn- unpack-rolls
  "Every shape this has ever emitted: a bare number, the short vector,
  and the verbose map that went out while `pack-rolls` was sitting here
  unused. Tokens from before this change still read."
  [m]
  (into {}
        (for [[id v] m]
          [(clojure.core/name id)
           (cond
             (number? v) {:roll v :mod 0 :rewrites 0}
             (map? v)    {:roll     (:roll v)
                          :mod      (or (:mod v) 0)
                          :rewrites (or (:rewrites v) 0)}
             :else       {:roll     (nth v 0)
                          :mod      (nth v 1 0)
                          :rewrites (nth v 2 0)})])))

;; ---------------------------------------------------------------------
;; The token
;; ---------------------------------------------------------------------

(defn- checksum [b]
  (loop [i 0 s 7]
    (if (>= i (.-length b))
      (mod s 251)
      (recur (inc i) (mod (+ (* s 31) (aget b i)) 251)))))

(defn pack
  "A map to a token. The keys are one letter each because this goes in
  a query string thirty times a page."
  [{:keys [name companion echo rolls deeds epoch todo]}]
  (try
    (let [payload (js/JSON.stringify
                   (clj->js
                    (cond-> {}
                      name      (assoc :n name)
                      companion (assoc :a [(:n companion) (:given companion)])
                      (pos? (or echo 0)) (assoc :e echo)
                      (seq rolls) (assoc :r (pack-rolls rolls))
                      (seq deeds) (assoc :d (mapv clojure.core/name deeds))
                      ;; when the reader last withdrew the whole record.
                      ;; it travels because the alternative is that only
                      ;; the state travels, and then a link from before
                      ;; the withdrawal quietly reinstates it.
                      (pos? (or epoch 0)) (assoc :g epoch)
                      (seq todo) (assoc :t [(:seq todo 0) (:items todo)]))))
          body    (mask (to-bytes payload) book-key)
          out     (js/Uint8Array. (inc (.-length body)))]
      (aset out 0 (checksum body))
      (dotimes [i (.-length body)] (aset out (inc i) (aget body i)))
      (b64 out))
    (catch :default _ nil)))

(defn unpack
  "A token back to a map, or nil if anybody has been at it.

  An absent field comes back empty rather than missing, which is fine
  and is not a licence: the caller merges this forward and knows that an
  empty Echo means the token is old, not that the Echo is nought."
  [s]
  (try
    (when-not (str/blank? s)
      (let [raw  (un-b64 s)
            want (aget raw 0)
            body (.slice raw 1)]
        (when (= want (checksum body))
          (let [m (js->clj (js/JSON.parse (from-bytes (mask body book-key)))
                           :keywordize-keys true)]
            {:name      (:n m)
             :companion (when-let [a (:a m)] {:n (first a) :given (or (second a) "")})
             :echo      (or (:e m) 0)
             :rolls     (unpack-rolls (or (:r m) {}))
             :deeds     (mapv keyword (or (:d m) []))
             :epoch     (or (:g m) 0)
             :todo (when-let [t (:t m)] {:seq (first t) :items (or (second t) [])})}))))
    (catch :default _ nil)))

;; ---------------------------------------------------------------------
;; When it does not reconcile
;; ---------------------------------------------------------------------

(defn complain! []
  (js/console.warn
   "016.09 — entry does not reconcile and has not been carried forward. Retained."))