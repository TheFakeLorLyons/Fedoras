(ns check
  "Read every Clojure file in the project without evaluating it.

  This is the cheap half of `bb book` - it will not say whether a
  function is missing, but it will tell you, in well under a second,
  that scene 3 of Act III has one too many closing parens. Which, when
  you have twenty-two scene files open, is the failure you actually
  keep hitting."
  (:require [babashka.fs :as fs]))

(defn read-all
  "Read every form in a file. Returns nil on success, a message on
  failure."
  [file]
  (try
    (with-open [r (java.io.PushbackReader. (java.io.StringReader. (slurp file)))]
      (binding [*read-eval* false]
        (loop [n 0]
          (let [form (read {:eof ::eof :read-cond :allow} r)]
            (if (= ::eof form)
              nil
              (recur (inc n)))))))
    (catch Exception e
      (.getMessage e))))

(defn -main [& _]
  (let [files (->> (concat (fs/glob "src" "**.clj")
                           (fs/glob "notebooks" "**.clj")
                           (fs/glob "tools" "**.clj")
                           (fs/glob "resources" "**.cljs")
                           (fs/glob "src" "**.cljc"))
                   (map str)
                   sort)
        results (for [f files] [f (read-all f)])
        bad     (remove (comp nil? second) results)]
    (doseq [[f msg] bad]
      (println "FAIL" f)
      (println "     " msg))
    (println (format "%d files, %d ok, %d broken"
                     (count files)
                     (- (count files) (count bad))
                     (count bad)))
    (when (seq bad)
      (System/exit 1))))

;; ---------------------------------------------------------------------
;; The ClojureScript the reader is handed
;;
;; Every (sketch/cell ...) and (sketch/page ...) in the book carries a
;; string of ClojureScript that the reader can edit. Those strings are
;; not read by the build, so until now a stray paren in one of them
;; only turned up in a browser, in one chapter, if someone presses
;; Run. This reads every one of them.
;; ---------------------------------------------------------------------

(defn- cell-strings
  "Every string literal passed to a sketch cell, with where it came
  from. Read from the file's forms rather than by regex, because a
  regex over Clojure source is a way of being wrong later."
  [file]
  (let [forms (try (read-string (str "[" (slurp file) "]"))
                   (catch Exception _ nil))]
    (->> (tree-seq coll? seq forms)
         (filter #(and (seq? %)
                       (symbol? (first %))
                       (#{"cell" "page" "editable"} (name (first %)))))
         (keep (fn [form] (last (filter string? form))))
         (map (fn [s] [file s])))))

(defn check-cells
  "Read every embedded cell. Returns the ones that will not parse."
  []
  (let [files (map str (concat (fs/glob "notebooks" "**.clj")
                               (fs/glob "src" "**.clj")))
        cells (mapcat cell-strings files)
        bad   (keep (fn [[file code]]
                      (try (read-string (str "[" code "]")) nil
                           (catch Exception e
                             [(str file) (ex-message e)
                              (subs code 0 (min 60 (count code)))])))
                    cells)]
    (println (format "%d embedded cells, %d ok, %d broken"
                     (count cells) (- (count cells) (count bad)) (count bad)))
    (doseq [[file msg head] bad]
      (println "  BROKEN" file)
      (println "    " msg)
      (println "    " (str head "...")))
    (empty? bad)))