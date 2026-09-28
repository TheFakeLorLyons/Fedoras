(ns fedoras.build
  "Build entry points.

  Everything structural lives in clay.edn; this namespace only decides
  *which* build is asked for. Call these from the REPL while writing,
  or from the CLI:

      clojure -M:book          ; the whole book, HTML, into book/
      clojure -M:pdf           ; the whole book, PDF,  into book-pdf/
      clojure -M:clay          ; watch notebooks/, live-reload a browser
      clojure -M:clay -r       ; render everything as plain HTML, fast

  The distinction that matters day to day: `scene!` skips Quarto
  entirely and renders one file as bare HTML in about a second. That is
  the one bound to a key. `html!` runs Quarto over twenty-odd
  chapters and takes as long as it takes."
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [clojure.string :as str]
            [scicloj.clay.v2.api :as clay]
            [fedoras.screenplay :as sp]
            [fedoras.state :as state]
            [fedoras.winfix :as winfix])
  (:import [com.sun.net.httpserver HttpServer HttpHandler HttpExchange]
           [java.net InetSocketAddress]))

;; Windows path separators break Clay's generated HTML. See the
;; namespace docstring for the diagnosis and the upstream one-liner.
(winfix/install!)

(defn set-protagonist!
  "Change the DEFAULT name, project-wide, for every build from here on.
  Readers override it in the browser; this is the name that ships.

    (set-protagonist! \"Wendell\" \"Pike\")

  For a one-off build without touching the default, bind it instead:

    (binding [sp/*protagonist* {:first \"Wendell\" :last \"Pike\"}]
      (html!))"
  [first-name last-name]
  (alter-var-root #'sp/*protagonist* (constantly {:first first-name :last last-name})))

(defn open!
  "Open a URL, or a built file, in the OS browser.
 
  `:render true` tells Clay to batch-build and open nothing -- in
  Clay's own config, `render` implies `{:show false :browse false}`.
  That is what you want for a reproducible build and not what you want
  at 1am, so the build functions below call this when they finish.
 
  A `http://` argument is passed straight through; anything else is
  taken as a path and checked before the browser is bothered with it.
  A nil is silence rather than an error, because `serve!` returns nil
  when there was nothing to serve and has already said so, and a
  second complaint about a browser that was never opened is noise."
  ([] (open! "book/index.html"))
  ([target]
   (when (some? target)
     (let [url? (str/starts-with? target "http")
           f    (when-not url? (io/file target))]
       (if (and f (not (.exists f)))
         (println "Nothing at" (.getAbsolutePath f) "-- did the build finish?")
         (let [os   (str/lower-case (System/getProperty "os.name"))
               what (if url? target (.getAbsolutePath f))]
           (try
             (cond
               (str/includes? os "win") (shell/sh "cmd" "/c" "start" "" what)
               (str/includes? os "mac") (shell/sh "open" what)
               :else                    (shell/sh "xdg-open" what))
             (catch Exception e
               (println "Could not open a browser:" (ex-message e))))
           (println "Opened" what)
           what))))))
 
;; ---------------------------------------------------------------------
;; When the build has got away from you
;; ---------------------------------------------------------------------

(defn chapters
  "Every chapter in the spine, flattened out of the parts."
  []
  (->> (:source-path (edn/read-string (slurp "clay.edn")))
       (mapcat (fn [e] (if (map? e) (:chapters e) [e])))
       vec))

(defn qmd-sizes!
  "How big each written chapter is, largest first.

  Clay evaluates a chapter in milliseconds and then hands Quarto a
  .qmd. If one chapter emits a great deal more raw HTML than the
  others -- a generated SVG with two thousand elements in it, say --
  it will not show up as slow evaluation, it will show up here, and it
  will be the chapter pandoc is still chewing on.

  Run it after `evaluated!`, which writes the qmds without invoking
  Quarto at all."
  ([] (qmd-sizes! "book"))
  ([dir]
   (let [files (->> (file-seq (io/file dir))
                    (filter #(str/ends-with? (.getName %) ".qmd"))
                    (sort-by #(- (.length %))))]
     (if (empty? files)
       (println "No .qmd files in" dir "-- run (evaluated!) first.")
       (do (println (format "%d chapters, largest first" (count files)))
           (doseq [f files]
             (println (format "%8.1f KB  %s" (/ (.length f) 1024.0) (.getName f))))
           (println)
           (println (format "total %.1f MB"
                            (/ (reduce + (map #(.length %) files)) 1048576.0)))))
     files)))

(defn qmd-nodes!
  "Count the SVG elements each chapter emits.

  Size alone does not say why a chapter is big. This does: a scene that
  draws one element per data point shows up as thousands of <line> or
  <circle> tags, and pandoc has to walk every one. Anything over a few
  hundred wants collapsing into a single <path>.

  Run after `evaluated!`."
  ([] (qmd-nodes! "book"))
  ([dir]
   (let [tags ["<line" "<circle" "<rect" "<path" "<polygon" "<polyline"]
         rows (for [f (->> (file-seq (io/file dir))
                           (filter #(str/ends-with? (.getName %) ".qmd")))
                    :let [text (slurp f)
                          counts (into {} (for [t tags
                                                :let [n (count (re-seq (re-pattern t) text))]
                                                :when (pos? n)]
                                            [t n]))
                          total (reduce + (vals counts))]]
                [(.getName f) total counts])
         sorted (sort-by (comp - second) rows)]
     (println "SVG elements per chapter, worst first")
     (doseq [[name total counts] (take 12 sorted)]
       (println (format "%7d  %-46s %s"
                        total name
                        (str/join "  " (map (fn [[t n]] (str t "> x" n)) counts)))))
     (println)
     (let [bad (filter (fn [[_ total _]] (> total 200)) sorted)]
       (if (seq bad)
         (do (println (count bad) "chapter(s) draw more than 200 elements:")
             (doseq [[name total _] bad]
               (println (format "  %s  (%d) -- collapse into one <path>" name total))))
         (println "Nothing above 200. No chapter is drawing element-per-point.")))
     sorted)))

(defn timed!
  "Render every chapter through Quarto, one at a time, and print how
  long each took.

  A book build is one Quarto invocation over the whole spine, so when
  it hangs there is nothing to look at: no per-chapter timing, no last
  chapter named, just a subprocess that does not return. This does the
  same work the same way, separately, so the chapter costing the
  minutes has to say so.

  It is slower than a real build, because it starts Quarto once per
  chapter. Run it when something has gone wrong, not as a habit."
  []
  (let [spine (chapters)]
    (println (format "%d chapters, one Quarto render each" (count spine)))
    (println)
    (let [results (doall
                   (for [path spine]
                     (let [t0 (System/currentTimeMillis)
                           ok (try (clay/make! {:source-path path
                                                :format [:quarto :html]
                                                :base-target-path "temp-timing"})
                                   true
                                   (catch Throwable e
                                     (println "   FAILED" path (ex-message e))
                                     false))
                           ms (- (System/currentTimeMillis) t0)]
                       (println (format "%7d ms  %s%s" ms path (if ok "" "   <- FAILED")))
                       [path ms ok])))
          total (reduce + (map second results))]
      (println)
      (println (format "total %.1f s across %d chapters"
                       (/ total 1000.0) (count results)))
      (println "the five most expensive:")
      (doseq [[path ms _] (take 5 (sort-by (comp - second) results))]
        (println (format "  %7d ms  %s" ms path)))
      results)))

(defn evaluated!
  "Write every .qmd and stop, without invoking Quarto at all.

  Splits the build in two so you can see which half is costing you.
  If this is fast and the full build is not, the time is Quarto's and
  no amount of tidying the Clojure will help."
  []
  (let [t0 (System/currentTimeMillis)]
    (clay/make! {:format [:quarto]})
    (println (format "qmds written in %.1f s" (/ (- (System/currentTimeMillis) t0) 1000.0)))
    (println "now: cd book && quarto render --output-dir _clay")))

;; ---------------------------------------------------------------------
;; Serving it
;;
;; A book opened as a file has no origin. The browser reports `null`,
;; localStorage throws on every call, and `fedoras.reader/store!`
;; swallows it — so the name, the animal, the Echo and the to-do list
;; are all written to nowhere, silently, and the only thing that
;; survives a click is the token in the address bar. Readers were
;; getting a book that forgets them.
;;
;; One origin fixes all of it, and an origin is just a server. This is
;; the one in the JDK: no dependency, no config, forty lines. It serves
;; the built directory and nothing else, refuses anything that resolves
;; outside it, and lives on 127.0.0.1 where nobody else can reach it.
;;
;; The book still works opened as a file. It just works better served,
;; and there is no reason for the build to hand you the worse one.
;; ---------------------------------------------------------------------
 
(defonce ^:private server
  (atom nil))
 
(def ^:private content-types
  {"html" "text/html; charset=utf-8"
   "css"  "text/css; charset=utf-8"
   "js"   "text/javascript; charset=utf-8"
   "json" "application/json; charset=utf-8"
   "edn"  "text/plain; charset=utf-8"
   "txt"  "text/plain; charset=utf-8"
   "svg"  "image/svg+xml"
   "png"  "image/png"
   "jpg"  "image/jpeg"
   "jpeg" "image/jpeg"
   "gif"  "image/gif"
   "webp" "image/webp"
   "ico"  "image/x-icon"
   "woff" "font/woff"
   "woff2" "font/woff2"
   "ttf"  "font/ttf"})
 
(defn- content-type [^java.io.File f]
  (let [n (.getName f)
        i (.lastIndexOf n ".")]
    (or (when (pos? i) (content-types (str/lower-case (subs n (inc i)))))
        "application/octet-stream")))
 
(defn- respond!
  [^HttpExchange exchange status ^bytes body type]
  (.set (.getResponseHeaders exchange) "Content-Type" type)
  (.sendResponseHeaders exchange status (alength body))
  (with-open [out (.getResponseBody exchange)]
    (.write out body)))
 
(defn- static-handler
  "One directory, read-only. A request is a path under the root or it is
  a 404: the canonical path is checked against the canonical root, so
  `../../etc/passwd` resolves out of the tree and is refused rather
  than served."
  [^java.io.File root]
  (reify HttpHandler
    (handle [_ exchange]
      (try
        (let [path (.getPath (.getRequestURI exchange))
              path (java.net.URLDecoder/decode path "UTF-8")
              rel  (if (str/ends-with? path "/")
                     (str (subs path 1) "index.html")
                     (subs path 1))
              file (io/file root rel)]
          (if (and (.isFile file)
                   (str/starts-with? (.getCanonicalPath file)
                                     (.getCanonicalPath root)))
            (respond! exchange 200
                      (java.nio.file.Files/readAllBytes (.toPath file))
                      (content-type file))
            (respond! exchange 404
                      (.getBytes (str "nothing at " path) "UTF-8")
                      "text/plain; charset=utf-8")))
        (catch Throwable e
          (respond! exchange 500
                    (.getBytes (str "server: " (ex-message e)) "UTF-8")
                    "text/plain; charset=utf-8"))))))
 
(defn stop!
  "Shut the server down. Safe to call when there isn't one, which is
  what makes `serve!` safe to call twice from a REPL."
  []
  (when-let [^HttpServer s @server]
    (.stop s 0)
    (reset! server nil)
    (println "fedoras: server stopped"))
  nil)
 
(defn serve!
  "Serve a built directory on localhost and return the URL.
 
  Tries a few ports rather than failing on one: a REPL that has been
  open all afternoon may still be holding the last one, and being told
  `Address already in use` is not useful when the answer is simply the
  next number up."
  ([] (serve! "book" 7890))
  ([dir] (serve! dir 7890))
  ([dir port]
   (stop!)
   (let [root (.getCanonicalFile (io/file dir))]
     (if-not (.isDirectory root)
       (println "Nothing to serve at" (.getPath root) "-- did the build finish?")
       (loop [p port tries 0]
         (if (> tries 20)
           (println "fedoras: no free port between" port "and" (+ port 20))
           (if-let [s (try
                        (doto (HttpServer/create (InetSocketAddress. "127.0.0.1" p) 0)
                          (.createContext "/" (static-handler root))
                          (.setExecutor (java.util.concurrent.Executors/newFixedThreadPool 4))
                          (.start))
                        (catch java.io.IOException _ nil))]
             (let [url (str "http://localhost:" p "/")]
               (reset! server s)
               (println "fedoras: serving" (.getPath root) "at" url)
               url)
             (recur (inc p) (inc tries)))))))))
 
(defn- park!
  "Hold the JVM open so the server outlives the build.
 
  Only from the command line. `-main` used to exit as soon as the build
  finished, which is right for a build that produces files and wrong
  for one that produces a running server -- the exit would take the
  server down between the browser opening and the page asking for it."
  []
  (println "Ctrl-C to stop the server.")
  @(promise))

(defn scene!
  "Render a single chapter, fast, no Quarto. Path is relative to
  notebooks/.
 
    (scene! \"act1/s1_move_in_day.clj\")"
  ([path] (scene! path true))
  ([path show?]
   (clay/make! {:source-path path :format [:html]})
   (state/place-runtime! "book")
   (when show?
     (open! (serve! "book")))))

(defn chapter-quarto!
  "One chapter, but through Quarto, so you can check how it will
  actually look in the book."
  [path]
  (clay/make! {:source-path path
               :format [:quarto :html]
               :base-target-path "temp"}))

(defn html!
  "The full book, HTML, as a reader sees it: no beats, no notes, no
  status plates. Builds, then opens it."
  []
  (clay/make! {:render true
               :clean-up-target-dir true})
  (state/place-runtime! "book")
  (open! (serve! "book")))

(defn draft!
  "The full book with the drafting apparatus visible -- beats, notes,
  status plates. For you, not for anyone else."
  []
  (binding [sp/*show-beats* true]
    (clay/make! {:render true
                 :clean-up-target-dir true
                 :base-target-path "book-draft"})
    (state/place-runtime! "book-draft")
    (open! (serve! "book-draft" 7891))))

(defn reading-copy!
  "Alias for html!. The normal build is already the reading copy."
  []
  (html!))

(defn pdf!
  "The full book as PDF, via Quarto -> LaTeX. Requires the Quarto CLI
  and a TeX installation (`quarto install tinytex` if you'd rather not
  involve your system TeX)."
  []
  (binding [sp/*show-beats* false]
    (clay/make! {:aliases [:pdf]
                 :render true
                 :clean-up-target-dir true})
    (println "PDF build finished. Look in book-pdf/ for the .pdf.")))

(defn- repl?
  "Are we being called from a connected REPL rather than the CLI? nREPL
  evaluates on its own named thread, which is the cheapest reliable
  tell and costs no dependency."
  []
  (or (str/includes? (.getName (Thread/currentThread)) "nRepl")
      (some? (try (requiring-resolve 'nrepl.core/version)
                  (catch Throwable _ nil)))))

(defn -main
  "CLI entry point. Exits the JVM when it is done, which is right for
  `clojure -M:book` and fatal if you call it from an editor -- it
  takes the nREPL server down with it and you get `ECONNRESET` a
  fraction of a second after a perfectly successful build.
 
  So: from a REPL, call (html!), (reading-copy!) or (pdf!) directly.
  If you call -main anyway, the exit is skipped.
 
  html and draft do not exit at all from the command line, because both
  leave a server running and the book needs it. They park until Ctrl-C.
  pdf produces a file and nothing else, so it still exits."
  [& [target]]
  (case (or target "html")
    "html"  (do (html!)  (when-not (repl?) (park!)))
    "draft" (do (draft!) (when-not (repl?) (park!)))
    "pdf"   (do (pdf!)
                (when-not (repl?)
                  (shutdown-agents)
                  (System/exit 0)))
    (do (println "usage: -m fedoras.build [html|draft|pdf]")
        (when-not (repl?)
          (shutdown-agents)
          (System/exit 0)))))
  