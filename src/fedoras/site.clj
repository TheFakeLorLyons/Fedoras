(ns fedoras.site
  "Editions, and staging one for a static host.

  There is one spine, in `clay.edn`, and it is the whole book. An
  edition is a filter over it.

  A build lands in a directory named after the edition, and `stage!`
  copies the HTML out of it into `public/`, which is the one path the
  deployment workflow has to know about."
  (:require  [clojure.edn :as edn]
             [clojure.java.io :as io]
             [clojure.string :as str]
             [scicloj.clay.v2.api :as clay]
             [fedoras.screenplay :as sp]
             [fedoras.winfix :as winfix]))

(winfix/install!)

;; ---------------------------------------------------------------------
;; What will be published
;; ---------------------------------------------------------------------

(def editions
  "Each edition names the parts it keeps, in the order `clay.edn` has
  them, plus what the book calls itself while somebody is reading it.

  `:parts :all` is the whole spine. Anything else is a set of part
  titles, matched exactly against `:part` in `clay.edn`, so a renamed
  act fails loudly here rather than quietly shipping a shorter book."
  {:full
   {:title  "Fedoras"
    :parts  :all
    :target "book"}

   :act-one
   {:title  "Fedoras — Act One"
    :parts  #{"Act I — Orientation" "Apparatus"}
    :target "book-act-one"
    :note   "The reading copy for first readers. Front matter, Act I, nothing else."}})

;; ---------------------------------------------------------------------
;; Slicing the spine
;; ---------------------------------------------------------------------

(defn spine
  "The whole book, as `clay.edn` has it."
  []
  (:source-path (edn/read-string (slurp "clay.edn"))))

(defn part-titles
  "Every part in the spine, in order. Useful when an edition will not
  build because a part has been renamed."
  []
  (into [] (comp (filter map?) (map :part)) (spine)))

(defn slice
  "Keep the loose front-matter entries and whichever parts this edition
  asks for.

  Front matter is every string at the top level: the cover, the cast,
  the tribute. It is in every edition, because a book that opens on Act
  I Scene 1 with no cover is a draft rather than an edition."
  [source-path parts]
  (if (= :all parts)
    source-path
    (let [available (into #{} (comp (filter map?) (map :part)) source-path)
          unknown   (remove available parts)
          kept      (filterv (fn [entry]
                               (or (string? entry)
                                   (contains? parts (:part entry))))
                             source-path)]
      (when (seq unknown)
        (throw (ex-info (str "fedoras: no such part: " (str/join ", " unknown)
                             "\nThe spine has: " (str/join ", " available))
                        {:asked-for parts :available available})))
      kept)))

;; ---------------------------------------------------------------------
;; Building one
;; ---------------------------------------------------------------------

(defn- find-index
  "Clay hands the qmds to Quarto, and Quarto writes the HTML into a
  subdirectory whose name depends on the version and the config. Rather
  than hard-coding that, find the shallowest index.html under the
  target and treat its directory as the site."
  [target]
  (->> (file-seq (io/file target))
       (filter #(= "index.html" (.getName %)))
       (sort-by #(count (str/split (.getPath %) #"[/\\\\]")))
       first
       (#(some-> % .getParentFile))))

(defn- place-runtime!
  "Put the vendored runtime next to the HTML, because the pages ask for
  it by a relative name. Silent when there is nothing vendored, since
  in that case the pages are asking the CDN."
  [dir]
  (when-let [res (io/resource sp/scittle-resource)]
    (let [dest (io/file dir "scittle.js")]
      (with-open [in (io/input-stream res)]
        (io/copy in dest))
      (println "fedoras: placed scittle.js in" (.getPath (io/file dir)))
      dest)))

(defn build!
  "Render one edition. Beats and notes are off, because an edition is
  for somebody else."
  [edition-key]
  (let [{:keys [title parts target] :as edition}
        (or (get editions edition-key)
            (throw (ex-info (str "fedoras: no such edition: " edition-key)
                            {:available (keys editions)})))
        chapters (slice (spine) parts)]
    (println (format "fedoras: building %s -> %s/  (%d entries)"
                     (name edition-key) target (count chapters)))
    (binding [sp/*show-beats* false]
      (clay/make! {:source-path chapters
                   :base-target-path target
                   :book {:title title
                          :author "Lorelai Lyons"
                          :search false}
                   :render true
                   :clean-up-target-dir true}))
    ;; The pages ask for the runtime by a relative name, so it has to be
    ;; beside them here as well, not only in public/. Otherwise opening
    ;; the local build looks broken in a way the staged one is not.
    (when-let [dir (find-index target)]
      (place-runtime! dir))
    (assoc edition :chapters chapters)))

;; ---------------------------------------------------------------------
;; Staging it for a host
;; ---------------------------------------------------------------------

(defn vendor!
  "Fetch Scittle once and keep it in the repository.

  Run this after a version bump and commit the result. It is about
  three hundred kilobytes and it means a published book does not depend
  on somebody else's CDN being up while a stranger is reading it."
  []
  (let [target (io/file "resources" "fedoras" "vendor" "scittle.js")]
    (.mkdirs (.getParentFile target))
    (println "fedoras: fetching" sp/scittle-cdn)
    (with-open [in (io/input-stream sp/scittle-cdn)]
      (io/copy in target))
    (println (format "fedoras: vendored %,d bytes -> %s"
                     (.length target) (.getPath target)))
    (println "fedoras: commit that file. Every page will now load it locally.")
    (.getPath target)))

(defn- copy-tree! [^java.io.File from ^java.io.File to]
  (.mkdirs to)
  (doseq [f (.listFiles from)]
    (let [dest (io/file to (.getName f))]
      (if (.isDirectory f)
        (copy-tree! f dest)
        (io/copy f dest)))))

(defn- cdn-requests
  "Every jsdelivr address each page asks for, keyed by the page's file
  name. Pages that ask for nothing are left out."
  [html-files]
  (into (sorted-map)
        (keep (fn [f]
                (let [found (distinct (re-seq #"https://cdn\.jsdelivr\.net/[^\"'\s]+" (slurp f)))]
                  (when (seq found) [(.getName f) found]))))
        html-files))


(defn check!
  "Refuse to hand over a directory that will not work once it is
  somewhere else. Each of these has broken a published Quarto site
  before, and each is invisible until a stranger opens the link.
 
  Only Scittle from the CDN is a failure, because Scittle is the runtime
  that carries the reader's name, the animal, the dice and the Echo.
  Anything else a page borrows from the CDN is reported by name, so it
  is a decision and never a surprise."
  [dir]
  (let [out      (io/file dir)
        index    (io/file out "index.html")
        html     (->> (file-seq out)
                      (filter #(str/ends-with? (.getName %) ".html")))
        borrowed (cdn-requests html)
        runtime  (into (sorted-map)
                       (keep (fn [[page urls]]
                               (let [scittle (filter #(str/includes? % "/npm/scittle") urls)]
                                 (when (seq scittle) [page scittle]))))
                       borrowed)
        vendored (io/resource sp/scittle-resource)
        fails    (cond-> []
                   (not (.exists index))
                   (conj "no index.html: the host will show a file listing")

                   (not (.exists (io/file out ".nojekyll")))
                   (conj "no .nojekyll: Jekyll will drop every _ directory")

                   (and vendored (not (.exists (io/file out "scittle.js"))))
                   (conj "scittle is vendored but scittle.js is not in the output")

                   (and vendored (seq runtime))
                   (conj (str "scittle is vendored but these pages still load it from the CDN: "
                              (str/join ", " (keys runtime)) ". Rebuild.")))]
    (println (format "fedoras: checked %d pages in %s" (count html) dir))
    (doseq [[page urls] borrowed
            :when (not (contains? runtime page))]
      (println "  note   " page "borrows from the CDN:" (str/join " " urls)))
    (if (seq fails)
      (do (doseq [f fails] (println "  PROBLEM " f))
          (throw (ex-info "fedoras: this build is not safe to publish."
                          {:problems fails :dir dir})))
      (do (when-not vendored
            (println "  note    scittle comes from the CDN."
                     "Run (fedoras.site/vendor!) to stop that."))
          (println "  ok      index.html, .nojekyll, runtime")
          true))))

(defn stage!
  "Put a built edition in `public/`, which is the only path the
  deployment workflow knows about.

  Also writes `.nojekyll`. Without it GitHub Pages runs the output
  through Jekyll, which ignores every file and directory beginning with
  an underscore, and Quarto's output is full of them, so the book
  deploys and every stylesheet 404s."
  ([] (stage! :full))
  ([edition-key]
   (let [{:keys [target]} (get editions edition-key)
         src (find-index target)
         out (io/file "public")]
     (when-not src
       (throw (ex-info (str "fedoras: no index.html under " target
                            " -- did the build finish?")
                       {:target target})))
     (when (.exists out)
       (doseq [f (reverse (file-seq out))] (.delete f)))
     (copy-tree! src out)

     ;; GitHub Pages runs uploads through Jekyll, which ignores every
     ;; file and directory beginning with an underscore, and Quarto's
     ;; output is full of them. Without this the book deploys and every
     ;; stylesheet is a 404.
     (spit (io/file out ".nojekyll") "")

     (place-runtime! out)
     (println "fedoras: staged" (.getPath src) "->" (.getPath out))
     (check! out)
     (.getPath out))))

(defn publish!
  "Build an edition and stage it. This is what the workflow runs.

    clojure -X:site :edition :act-one"
  [{:keys [edition] :or {edition :act-one}}]
  (let [k (keyword (name edition))]
    (build! k)
    (stage! k)
    (println "fedoras:" (name k) "ready in public/")))

(defn -main
  "CLI form, for a workflow that would rather pass an argument than a
  map: `clojure -M:site act-one`."
  [& [edition]]
  (publish! {:edition (or edition :act-one)})
  (shutdown-agents)
  (System/exit 0))
