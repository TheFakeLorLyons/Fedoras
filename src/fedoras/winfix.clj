(ns fedoras.winfix
  "Workarounds for two Clay nuances that make every build fail on Windows.

  ONE -- support file paths become URIs.

  Clay writes its support files (jQuery and friends) next to the output
  page and references them from the generated HTML by a relative path.
  It builds that path with babashka.fs and calls str on the result,
  which on Windows gives a backslash separator:

      index_files\\md-default0.js

  That string goes to hiccup.page/include-js, which calls
  hiccup.util/to-uri, which is literally (URI. s). A backslash is not
  legal in a URI path, so:

      URISyntaxException: Illegal character in path at index 11

  Index 11 is the backslash. index_files is eleven characters.

  TWO -- the book chapter list.

  scicloj.clay.v2.make/spec->quarto-book-chapters-config turns each
  target path into a chapter entry for _quarto.yml by stripping the
  output directory off the front with a regex anchored on a forward
  slash. On Windows the path is book\\index.html, the pattern ^book/
  never matches, the prefix survives, and _quarto.yml gets a chapter
  called book\\index.qmd. Quarto runs inside book/, looks for a file by
  that literal name, and fails:

      ERROR: Book chapter 'book\\index.qmd' not found

  The same assumption breaks make/index-target-path?, which splits on a
  forward slash to decide whether an index chapter already exists. On
  Windows it always says no, so Clay prepends a second index.qmd.

  Everything here resolves its target vars at runtime, so this
  namespace compiles against any Clay version, including ones where the
  functions it patches do not exist."
  (:require [clojure.string :as str]))

(defn windows? []
  (str/includes? (str/lower-case (or (System/getProperty "os.name") "")) "win"))

(defn- posix [x]
  (if (string? x) (str/replace x "\\" "/") x))

(defn- resolve-quietly [sym]
  (try (requiring-resolve sym) (catch Exception _ nil)))

;; --- Bug one: paths that become URIs ---------------------------------

(defn- wrap-include [f]
  (fn [& args] (apply f (map posix args))))

(defn- patch-include-map! []
  (when-let [v (resolve-quietly 'scicloj.clay.v2.page/include)]
    (alter-var-root v (fn [m] (update-vals m wrap-include)))
    :page/include))

(defn- patch-relative-path! []
  (when-let [v (resolve-quietly 'scicloj.clay.v2.files/relative-path)]
    (alter-var-root v (fn [f] (fn [& args] (posix (apply f args)))))
    :files/relative-path))

;; --- Bug two: the book chapter list -----------------------------------

(defn- strip-base [base s]
  (let [base (posix (str base))]
    (if (and (seq base) (str/starts-with? s (str base "/")))
      (subs s (inc (count base)))
      s)))

(defn- fix-chapter [base v]
  (cond
    (string? v)     (strip-base base (posix v))
    (map? v)        (update v :chapters #(mapv (partial fix-chapter base) %))
    (sequential? v) (mapv (partial fix-chapter base) v)
    :else           v))

(defn- dedupe-chapters
  "Clay may have prepended its own index.qmd because index-target-path?
  could not recognize a backslashed path. Once normalized, that
  duplicate becomes visible and removable."
  [chapters]
  (second (reduce (fn [[seen out] c]
                    (if (and (string? c) (seen c))
                      [seen out]
                      [(cond-> seen (string? c) (conj c)) (conj out c)]))
                  [#{} []]
                  chapters)))

(defn- patch-book-chapters! []
  (when-let [v (resolve-quietly 'scicloj.clay.v2.make/spec->quarto-book-chapters-config)]
    (alter-var-root v (fn [f]
                        (fn [spec]
                          (->> (f spec)
                               (fix-chapter (:base-target-path spec))
                               dedupe-chapters))))
    :make/book-chapters))

(defn- patch-index-detection! []
  (when-let [v (resolve-quietly 'scicloj.clay.v2.make/index-target-path?)]
    (alter-var-root v (fn [f] (fn [p] (f (posix p)))))
    :make/index-target-path?))

;; --- Installation ------------------------------------------------------

(defonce ^:private state (atom #{}))

(def ^:private patches
  {:page/include            patch-include-map!
   :files/relative-path     patch-relative-path!
   :make/book-chapters      patch-book-chapters!
   :make/index-target-path? patch-index-detection!})

(defn install!
  "Patch Clay's path handling if we are on Windows. Idempotent.
  Returns the set of things patched, or :not-needed.

  (install! true) forces it on any platform -- useful for checking that
  the patch itself is not what is breaking the build."
  ([] (install! (windows?)))
  ([force?]
   (if-not force?
     :not-needed
     (let [applied (->> patches
                        (remove (fn [[k _]] (@state k)))
                        (keep (fn [[_ f]] (f)))
                        set)
           total   (swap! state into applied)]
       (if (seq total)
         total
         (do (println "fedoras.winfix: found nothing to patch. Clay's internals"
                      "have moved -- check whether the upstream bugs are fixed.")
             :nothing-to-patch))))))
