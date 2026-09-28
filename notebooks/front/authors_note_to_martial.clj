^{:kindly/hide-code true
  :clay {:quarto {:title "Author's note to Martial"}}}
(ns front.authors-note-to-martial
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [clojure.xml :as xml]
            [scicloj.kindly.v4.kind :as kind]))

;; # Author's note to Martial {#tribute .sidebar-title}

^:kindly/hide-code
(def front "notebooks/front/")

^:kindly/hide-code
(defn xml->hiccup
  "clojure.xml's element maps as hiccup vectors. Whitespace between elements is dropped
  by the parser; the text of a glyph cell, leading space and combining marks included,
  comes through whole."
  [node]
  (if (string? node)
    node
    (into [(:tag node) (or (:attrs node) {})]
          (map xml->hiccup (:content node)))))

^:kindly/hide-code
(defn qualify
  "One attribute with its ids suffixed, so two copies of the art on a page keep their
  own masks, filters and clip paths."
  [attribute value suffix]
  (cond
    (= :id attribute)
    (str value suffix)

    (and (string? value) (string/includes? value "url(#"))
    (string/replace value #"url\(#([^)]+)\)" (str "url(#$1" suffix ")"))

    (and (#{:href :xlink:href} attribute) (string? value) (string/starts-with? value "#"))
    (str value suffix)

    :else value))

^:kindly/hide-code
(defn art
  "The SVG read off disk as hiccup, with its ids kept to itself and the class the
  stylesheet and the hover code look for."
  [path suffix]
  (-> (with-open [stream (io/input-stream path)]
        (xml->hiccup (xml/parse stream)))
      (->> (walk/postwalk
            (fn [form]
              (if (map? form)
                (reduce-kv (fn [attributes attribute value]
                             (assoc attributes attribute (qualify attribute value suffix)))
                           {}
                           form)
                form))))
      (assoc-in [1 :class] "tribute-art")))

^:kindly/hide-code
(defn behaviour
  "The ClojureScript read off disk as one form. 
  A list inside kind/hiccup  prints the form into a scittle script tag and pulls in
  Scittle itself. Settings passed here replace the file's (def overrides {}), so any
  key of its defaults can be tuned from this namespace."
  ([path] (behaviour path {}))
  ([path settings]
   (with-open [reader (java.io.PushbackReader. (io/reader path))]
     (let [eof (Object.)
           forms (doall (take-while #(not (identical? eof %))
                                    (repeatedly #(read {:eof eof} reader))))]
       (apply list 'do
              (map (fn [form]
                     (if (and (seq? form) (= 'def (first form)) (= 'overrides (second form)))
                       (list 'def 'overrides settings)
                       form))
                   forms))))))

^:kindly/hide-code
(kind/hiccup
 [:div.tribute-page
  [:style (slurp (str front "martial.css"))]

  [:blockquote.tribute
   [:p.tribute-heading "§ 0.0 THE " [:em "AUTHOR"] " TO " [:em "MARTIAL"]]
   [:div.tribute-note
    (art (str front "martial.svg") "-t0")]]

  [:hr]

  [:div.tribute-meta
   [:p.tribute-date "Event Date: " [:em "20XX ????"]]
   [:p.tribute-attribution "— Lorelai Anandamayi Soma Dasi Lyons"]]

  ;; Any key from `defaults` in martial.cljs can be set here, for example
  ;; {:lit-ceiling 0.15 :falloff 30 :warp? false :page-hue-sway 0}
  (behaviour (str front "martial.cljs") {})])