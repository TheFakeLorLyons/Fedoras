(ns fedoras.state
  "Ships the reader-side runtime to the page.

  There is almost nothing here on purpose. The browser half of the book
  is `resources/fedoras/reader.cljs`, which is real ClojureScript, read
  as Clojure by the build, and evaluated in the page by Scittle. This
  namespace slurps it, hands it the roster, and gets out of the way.

  Why Scittle rather than a string of JavaScript: the interactive cells
  already need it on every page, an atom with watches on it is a better
  answer to `when is the page ready` than any amount of DOM-event
  guesswork, and a stray paren now fails the build instead of failing
  silently in somebody's browser.

  Scittle evaluates its script tags on DOMContentLoaded, in document
  order, which is why the ordering problem disappears rather than being
  worked around."
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

(defn- element-ids
  "Every element id the book asks for, by file. Widgets and sketch cells
  share one namespace of ids per chapter, because `getElementById` does,
  and a collision between them is invisible at runtime: the widget is
  handed the sketch's element and every selector inside it misses without
  anything being thrown."
  []
  (->> (file-seq (io/file "src"))
       (filter (fn [f] (.isFile f)))
       (filter (fn [f] (str/ends-with? (.getName f) ".clj")))
       (mapcat (fn [f]
                 (map (fn [[_ id]] [(.getPath f) id])
                      (re-seq #":id\s+\"([^\"]+)\"" (slurp f)))))
       vec))

(defn duplicate-ids
  "Ids claimed in more than one place. Not proof of a collision, since two
  chapters may each use `life` without ever meeting, but every real
  collision is in here."
  []
  (->> (element-ids)
       (group-by second)
       (filter (fn [[_ uses]] (> (count uses) 1)))
       (mapv (fn [[id uses]] [id (mapv first uses)]))))

(def reader-candidates
  "Location of the reader."
  ["fedoras/reader.cljs"])

(defn- classpath-roots []
  (->> (str/split (System/getProperty "java.class.path") #"[:;]")
       (remove #(str/ends-with? % ".jar"))
       (str/join ", ")))

(defn find-resource
  "Return the first candidate that is actually on the classpath, or nil."
  [candidates]
  (some (fn [p] (when (io/resource p) p)) candidates))

(defn resource-text
  "Read a resource, and say something useful if it is not there.

  `io/resource` returns nil for a path that does not exist and `slurp`
  will happily try to read nil, which is where the eleven-line stack
  trace came from. This turns that into one sentence naming the file and
  the directories that were searched."
  [path]
  (if-let [url (io/resource path)]
    (slurp url)
    (throw
     (ex-info (str "fedoras: cannot find " path " on the classpath.\n"
                   "Looked in: " (classpath-roots) "\n"
                   "Either the file is somewhere else, or that directory "
                   "is not in :paths in deps.edn.")
              {:path path
               :classpath-roots (classpath-roots)}))))

(defn- reader-source []
  (if-let [found (find-resource reader-candidates)]
    (resource-text found)
    (throw
     (ex-info (str "fedoras: the reader runtime is missing.\n"
                   "Looked for: " (str/join ", " reader-candidates) "\n"
                   "In: " (classpath-roots) "\n"
                   "Nothing on the page will work without it: no name, no "
                   "animal, no dice, no Echo.")
              {:candidates reader-candidates
               :classpath-roots (classpath-roots)}))))

(defn- names-form
  "The roster's grammar, as ClojureScript, so a chapter can decline the
  animal it rolled without shipping the twenty arrival passages.
 
  Handed over as Clojure data rather than through a JavaScript global:
  one boundary fewer, and the numbers stay numbers. This used to project
  out `:animal` and send a map of bare strings, which was right when the
  only question a page could ask was what the thing is called."
  [grammar]
  (pr-str (list 'fedoras.reader/animals! (into (sorted-map) grammar))))

(def scittle-resource
  "Where a vendored Scittle lives, if the project has one."
  "fedoras/vendor/scittle.js")

(defn place-runtime!
  "Put the vendored runtime next to the built HTML.

  The pages ask for it by a relative name, so it has to be beside them,
  and it has to happen for every kind of build. It used to happen only
  in `fedoras.site`, which meant the staged copy worked and the local
  one said `ClojureScript runtime unavailable` on every page."
  [dir]
  (when-let [res (io/resource scittle-resource)]
    (let [target (io/file dir "scittle.js")]
      (.mkdirs (io/file dir))
      (with-open [in (io/input-stream res)]
        (io/copy in target))
      (println "fedoras: placed scittle.js in" (.getPath (io/file dir)))
      target)))

(def ^:dynamic *emitted*
  "What has already been inlined on the page being built.

  `source` is called once per widget, so a chapter with three sketch
  cells used to ship three copies of the same namespace and Scittle
  evaluated it three times. Clay renders one notebook at a time, and
  `assets` resets this, so the set is per chapter."
  (atom #{}))

(defn assets
  "Emitted once per chapter, before anything that renders.
 
  Order matters and it is the only place in this project where it does:
  carry defines the codec, reader uses it at load, and the roster lands
  last because it only affects what an animal is called."
  [roster]
  (reset! *emitted* #{})
  (kind/hiccup
   [:div {:style {:display "none"}}
    [:script {:type "application/x-scittle"} (resource-text "fedoras/carry.cljs")]
    [:script {:type "application/x-scittle"} (reader-source)]
    [:script {:type "application/x-scittle"} (names-form roster)]]))

(defn- edn-files-in
  "Every `.edn` file directly inside a resources directory, as
  classpath-relative paths ready for resource-text — one level, not
  recursive, since an entity folder is a flat cast, not a tree.

  Resolved through `io/resource`, the same classpath lookup
  `resource-text` already uses to find a single file — not a raw
  `(io/file \"resources\" dir)`, which is relative to whatever the JVM's
  current working directory happens to be when the build runs. That
  distinction is the whole fix: a single known file can be found either
  way if you happen to be running from the project root, but a build
  tool is under no obligation to be, and `io/resource` doesn't care
  either way. Sorted, because File/listFiles makes no promise about
  order and a build should not depend on the filesystem's mood.

  Only works for an exploded resources directory on disk, not one
  packed inside a jar — the same limitation element-ids and
  source-calls already accept for walking `src`, so it's consistent
  with how this file already treats dev-time discovery."
  [dir]
  (if-let [url (io/resource dir)]
    (let [root  (io/file url)
          files (->> (.listFiles root)
                     (filter (fn [f] (and (.isFile f) (str/ends-with? (.getName f) ".edn"))))
                     (mapv (fn [f] (str dir "/" (.getName f))))
                     sort
                     vec)]
      (when (empty? files)
        (throw
         (ex-info (str "fedoras: " (.getPath root) " exists but has no .edn files in it.")
                  {:dir dir})))
      files)
    (throw
     (ex-info (str "fedoras: no such entities directory on the classpath: " dir "\n"
                   "Looked in: " (classpath-roots) "\n"
                   "Either the directory is somewhere else, or that part of "
                   "the tree is not in :paths in deps.edn.")
              {:dir dir :classpath-roots (classpath-roots)}))))

(defn- format-beat
  "One beat, as a bullet with its notes indented under it."
  [beat]
  (str "    - " (name (:id beat)) " (" (name (:type beat)) "): " (:summary beat)
       (when (seq (:notes beat))
         (str "\n      " (str/join "\n      " (map (fn [n] (str "· " n)) (:notes beat)))))))

(defn- format-scene
  [scene]
  (str "  Scene " (:scene scene) " — " (:title scene) "\n"
       (str/join "\n" (map format-beat (:beats scene)))))

(defn- format-act
  [act]
  (str "ACT " (:act act) " — " (:title act) "\n"
       (when (seq (:notes act))
         (str (str/join "\n" (map (fn [n] (str "  · " n)) (:notes act))) "\n"))
       (str/join "\n\n" (map format-scene (:scenes act)))))

(defn spine-text
  "Render spine.edn's shape ({:title ... :acts [...]}) as a readable
  outline for an LLM system prompt, rather than shipping raw EDN
  syntax across the wire and asking Scittle to format it in the
  browser on every page load."
  [spine]
  (str/join "\n\n" (map format-act (:acts spine))))

(defn load-spine
  "Read `path` (an EDN resource shaped like spine.edn) once, and
  expose it to the page as window.FEDORAS_SPINE, already rendered to
  a readable outline string. A cell decides for itself whether to
  actually use it, via fedoras.llm/spine-context and its own
  include-spine? toggle -- this only makes the outline available,
  it doesn't force anything to read it."
  [path]
  (let [spine (edn/read-string (resource-text path))
        text (spine-text spine)]
    (kind/hiccup
     [:script {:type "application/x-scittle"}
      (str "(set! (.-FEDORAS_SPINE js/window) " (pr-str text) ")")])))

(defn- entities-in
  "One EDN file's worth of cast.
 
  A file is either one entity, which is a map with an `:id`, or a
  bundle, which is a map of id to entity. The bundle case is decided by
  looking at the values rather than by trusting the filename, so a
  malformed file is an error at build time and names itself."
  [path]
  (let [parsed (edn/read-string (resource-text path))]
    (cond
      (:id parsed)
      [parsed]

      (and (map? parsed) (seq parsed) (every? (fn [[_ v]] (:id v)) parsed))
      (vec (vals parsed))

      :else
      (throw
       (ex-info (str "fedoras: " path " is neither an entity nor a bundle.\n"
                     "An entity is a map with an :id. A bundle is a map of id to "
                     "entity, every value of which has an :id.")
                {:path path})))))

(defn load-entities
  "Load entity EDN and expose it as a Scittle script that merges into
  `window.FEDORAS_ENTITIES`.
 
  `paths` is either a vector of classpath-relative EDN paths, or a single
  string naming a directory relative to `resources/`, walked for every
  `.edn` file in it. Each file holds one entity or a bundle of them; see
  `entities-in`.
 
  Merges rather than replaces, because a page can have more than one
  widget that loads entities, Scittle runs their script tags in document
  order, and an unconditional `set!` meant the last one to run silently
  discarded everything the earlier ones had loaded."
  [paths]
  (let [paths      (if (string? paths) (edn-files-in paths) paths)
        entities   (vec (mapcat entities-in paths))
        entity-map (into {} (map (fn [e] [(:id e) e]) entities))]
    (kind/hiccup
     [:script {:type "application/x-scittle"}
      (str "(set! (.-FEDORAS_ENTITIES js/window) "
           "(merge (or (.-FEDORAS_ENTITIES js/window) {}) "
           (pr-str entity-map) "))")])))

(defn styles
  "Inline a widget's stylesheet, once per chapter.

  Inlined for the same reason sources are: a book that renders to file://
  cannot fetch its own assets. Guarded by the same set, because a page
  with three sketch cells on it should not carry three copies of
  anything."
  [path]
  (if (contains? @*emitted* path)
    (kind/hiccup [:span {:style {:display "none"}}])
    (do (swap! *emitted* conj path)
        (kind/hiccup [:style (resource-text path)]))))

(defn source
  "Inline a ClojureScript resource as a Scittle block. Inlined rather
  than linked because a book that renders to file:// cannot fetch its
  own sources.

  UI namespaces live under `fedoras/ui/` and are named to
  match: `fedoras/ui/dice.cljs` is `fedoras.ui.dice`. They are kept
  out of `fedoras/` proper so that no namespace name is claimed twice --
  `src/fedoras/dice.clj` and a `resources/fedoras/dice.cljs` would both
  be `fedoras.dice` on one classpath, which the JVM would tolerate and
  every editor would complain about."
  [path]
  (if (contains? @*emitted* path)
    (kind/hiccup [:span {:style {:display "none"}}])
    (do (swap! *emitted* conj path)
        (kind/hiccup
         [:script {:type "application/x-scittle"} (resource-text path)]))))


(defn call
  "Emit one ClojureScript call, with Clojure data as its arguments.
 
  Resolved at runtime because a symbol whose namespace is not on the 
  page is an ANALYSIS error in SCI: it is raised before anything runs, 
  so a `try` around the call does not catch it, and it escapes Scittle's
  tag loop and takes every later script tag on the page with it.
 
  It also used to log `Calling ...` before evaluating, so the console
  showed the call being made and then the failure, and the first line is
  the one you notice."
  [f & args]
  (kind/hiccup
   [:script {:type "application/x-scittle"}
    (pr-str
     (list 'if-let ['mount (list 'resolve (list 'quote f))]
           (apply list 'mount args)
           (list 'js/console.error
                 (str "fedoras: " f " is not on this page. Either the file that "
                      "declares it was never shipped, or the namespace inside it "
                      "does not match the path it was shipped from."))))]))
 

(defn on-render
  "Register a renderer from a chapter or a widget. `body` is
  ClojureScript source, as a string or as forms.

    (state/on-render :companion
      '(let [c (fedoras.reader/companion)] ...))"
  [k & body]
  (kind/hiccup
   [:script {:type "application/x-scittle"}
    (format "(fedoras.reader/on-render! %s (fn [] %s))"
            (pr-str k)
            (str/join "\n" (map (fn [f] (if (string? f) f (pr-str f))) body)))]))

(defn- source-calls
  "Every resource path the project asks for, found by reading the source
  rather than by remembering. A `(state/source \"...\")` or a
  `(state/styles \"...\")` anywhere in `src` is a promise that a file
  exists, and this collects the promises."
  []
  (->> (file-seq (io/file "src"))
       (filter (fn [f] (.isFile f)))
       (filter (fn [f] (str/ends-with? (.getName f) ".clj")))
       (mapcat (fn [f]
                 (map (fn [[_ _ path]] [(.getPath f) path])
                      (re-seq #"\(state/(source|styles)\s+\"([^\"]+)\"\)"
                              (slurp f)))))
       vec))

(defn- declared-ns
  "The namespace a page-side source actually declares, read out of the
  file rather than assumed from its path."
  [path]
  (some-> (resource-text path)
          (->> (re-find #"\(ns\s+([A-Za-z0-9._*+!?<>=-]+)"))
          second))

(defn- shipped-namespaces
  "Namespace to path, for every .cljs the project promises to inline."
  []
  (into {}
        (for [[_ path] (source-calls)
              :when (str/ends-with? path ".cljs")
              :let [n (try (declared-ns path) (catch Exception _ nil))]
              :when n]
          [n path])))

(defn- mount-calls
  "Every `(state/call 'some.ns/fn ...)` in the project, with the file
  that makes the promise. A call is a promise that a namespace will be
  on the page, and this collects the promises so they can be checked
  against `shipped-namespaces`, which collects what is actually there."
  []
  (->> (file-seq (io/file "src"))
       (filter (fn [f] (and (.isFile f) (str/ends-with? (.getName f) ".clj"))))
       (mapcat (fn [f]
                 (map (fn [[_ sym]] [(.getPath f) sym])
                      (re-seq #"\(state/call\s+'([A-Za-z0-9._*+!?<>=-]+)/[^\s)]+"
                              (slurp f)))))
       distinct
       vec))

(defn verify
  "Check that every page-side file the book asks for is reachable, and
  that every function the book calls is in a namespace that will be
  there when it calls it.
 
  A missing resource is a runtime failure in every chapter at once, so
  there is no reason to find out one chapter at a time. Run it before a
  build."
  []
  (let [reader   (find-resource reader-candidates)
        asks     (source-calls)
        missing  (remove (fn [[_ path]] (io/resource path)) asks)
        shipped  (shipped-namespaces)
        calls    (mount-calls)
        unmet    (remove (fn [[_ n]] (contains? shipped n)) calls)]

    (doseq [[id files] (duplicate-ids)]
      (println "  ID     " id "claimed in" (str/join ", " files)))

    (println "fedoras: checking" (inc (count asks)) "page-side sources")
    (if reader
      (println "  ok   the reader runtime  (" reader ")")
      (println "  MISSING  the reader runtime — tried"
               (str/join ", " reader-candidates)))
    (doseq [[file path] asks]
      (println (if (io/resource path) "  ok  " "  MISSING") path
               (str "(asked for by " file ")")))

    (println "fedoras: checking" (count calls) "mount calls")
    (doseq [[file n] calls]
      (if-let [path (get shipped n)]
        (println "  ok   " n "  (" path ")")
        (println "  UNMET " n (str "(called by " file ")"))))

    (when (seq unmet)
      (println)
      (println "  A call to a namespace that is not on the page is not a quiet")
      (println "  failure. SCI raises it during analysis, it escapes Scittle's")
      (println "  tag loop, and every script tag after it on that page never")
      (println "  runs. Namespaces that ARE shipped:")
      (doseq [[n path] (sort shipped)]
        (println "   " n "  ←" path)))

    (when (or (nil? reader) (seq missing))
      (println)
      (println "classpath roots:" (classpath-roots)))

    {:reader  reader
     :asked   (map second asks)
     :missing (map second missing)
     :shipped shipped
     :unmet   (map second unmet)}))