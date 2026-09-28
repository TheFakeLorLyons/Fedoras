(ns fedoras.widget
  "What a widget is made of.
 
   A widget is a directory named after it, holding files that share its
   name: `<name>.clj` emits markup, `<name>_ui.cljs` is the page side,
   `<name>.css` is the styling, and `<name>_settings.cljs` is the part the
   reader is invited to edit. Every path is derived from the widget's
   name, so a widget is added by making the directory, and there is no
   register anywhere that can fall out of step with the ones that exist.
 
   A widget can be more than one file: `<name>_ui.cljs` is the one the
   frame calls into, and it may sit beside as many siblings as it wants:
   `<name>_gl.cljs`, `<name>_world.cljs`, whatever the widget is large
   enough to need. Scittle has no classpath and resolves a require only
   against what has already been evaluated on the page, so the order the
   parts are emitted in is the whole of whether they load. That order is
   read out of the files themselves, below, for the same reason every
   path here is derived rather than listed: a hand-kept order is correct
   until the day somebody adds a require and does not think to update it."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [fedoras.state :as state]
            [scicloj.kindly.v4.kind :as kind]))
 
(def frame-styles "fedoras/ui/widget.css")

(def frame-runtime "fedoras/ui/widget_ui.cljs")

(def legend-styles "fedoras/ui/legend.css")

(def legend-runtime "fedoras/ui/legend.cljs")

(defn slug
  "A widget's name as a path segment. Underscores, because that is what a
  directory on a classpath is spelled with."
  [widget]
  (str/replace (name widget) "-" "_"))

(defn directory
  [widget]
  (str "fedoras/cs_class/" (slug widget) "/"))

(defn stylesheet
  [widget]
  (str (directory widget) (slug widget) ".css"))

(defn page-source
  [widget]
  (str (directory widget) (slug widget) "_ui.cljs"))

(defn settings-source
  [widget]
  (str (directory widget) (slug widget) "_settings.cljs"))

(defn mount-symbol
  "The page-side entry point. Every widget has one, it is in the `_ui`
  namespace beside it, and it is always called `mount!`."
  [widget]
  (symbol (str "fedoras.cs-class." (name widget) "." (name widget) "-ui")
          "mount!"))

;; ---------------------------------------------------------------------
;; The parts of a widget, in an order Scittle can load
;; ---------------------------------------------------------------------

(defn cljs-in
  "Every ClojureScript file directly inside a resources directory, as
  classpath-relative paths, sorted. One level, not recursive, since a
  widget is a flat directory and not a tree.
 
  Empty when the directory is not on the classpath, because asking what
  a directory holds is not a promise that it exists; the caller decides
  what to do about that. Works for an exploded directory on disk and not
  for one packed inside a jar, which is the same limitation the entity
  loader already accepts."
  [dir]
  (let [trimmed (if (str/ends-with? dir "/") (subs dir 0 (dec (count dir))) dir)]
    (if-let [url (io/resource trimmed)]
      (->> (.listFiles (io/file url))
           (filter (fn [f] (and (.isFile f) (str/ends-with? (.getName f) ".cljs"))))
           (mapv (fn [f] (str trimmed "/" (.getName f))))
           sort
           vec)
      [])))

(defn ns-form
  "The first form in a ClojureScript resource, which is its `ns`. Read
  rather than pattern-matched, so a docstring full of parentheses is a
  docstring and not a puzzle."
  [path]
  (with-open [reader (java.io.PushbackReader. (io/reader (io/resource path)))]
    (binding [*read-eval* false]
      (read reader))))

(defn required-namespaces
  "Every namespace an `ns` form requires. The name and the docstring are
  dropped and the rest is whatever clauses the form carries, of which
  only `:require` is of interest here."
  [form]
  (->> (drop 2 form)
       (filter (fn [clause] (and (seq? clause) (= :require (first clause)))))
       (mapcat rest)
       (map (fn [spec] (if (sequential? spec) (first spec) spec)))
       set))

(defn in-load-order
  "Widget parts sorted so that nothing is emitted before something it
  requires. A part that requires only namespaces from outside the widget
  is ready immediately; the rest wait for their siblings. `_ui` falls out
  last on its own, because it is the one that requires the others."
  [described]
  (let [known (into {} (map (fn [part] [(:ns part) part])) described)]
    (loop [waiting described
           placed []
           done #{}]
      (if (empty? waiting)
        placed
        (let [ready (filter (fn [part]
                              (every? (fn [needed] (or (done needed) (not (known needed))))
                                      (:needs part)))
                            waiting)]
          (if (empty? ready)
            (throw (ex-info (str "fedoras: the parts of the " (name (:widget (first waiting)))
                                 " widget require each other in a circle, so there is no "
                                 "order Scittle could load them in.")
                            {:left (mapv :path waiting)}))
            (recur (remove (set ready) waiting)
                   (into placed (map :path ready))
                   (into done (map :ns ready)))))))))

(defn page-sources
  "Every ClojureScript file that makes up a widget, in the order the page
  has to evaluate them.
 
  Falls back to the single conventional `_ui` file when the directory
  cannot be listed, so a jar build behaves as it always did rather than
  shipping a widget with no source at all."
  [widget]
  (let [settings (settings-source widget)
        parts (remove (fn [path] (= path settings)) (cljs-in (directory widget)))]
    (if (empty? parts)
      [(page-source widget)]
      (in-load-order
       (mapv (fn [path]
               (let [form (ns-form path)]
                 {:path path
                  :ns (second form)
                  :needs (required-namespaces form)
                  :widget widget}))
             parts)))))

;; ---------------------------------------------------------------------
;; The frame
;; ---------------------------------------------------------------------

(defn- source-panel
  "The editable settings, shown as what they are: a file, with its name on
  it. It comes before the widget because it comes before the widget: the
  program does not exist until it has been run."
  [widget rows]
  [:div.widget-source
   [:div.widget-source-head
    [:span.widget-source-name (str (slug widget) "_settings.cljs")]
    [:span.widget-source-lang "cljs"]]
   [:textarea.widget-settings {:spellcheck "false" :rows (or rows 18)}
    (state/resource-text (settings-source widget))]
   [:div.live-bar
    [:button.roll-go.widget-settings-run "Run"]
    [:button.roll-rewrite.widget-settings-reset "Put it back"]]
   [:p.widget-settings-said "Press Run."]])

(defn frame
  "Stylesheets, runtimes, markup, and the call that mounts it.
 
  The order on the page is header, source, widget, foot. A reader meets
  the settings before the thing the settings describe, and the thing does
  not appear until they have run them, because a program that is already
  running is a program nobody reads.
 
  `body` is the widget's own markup, and nils in it are dropped, so a
  `(when ...)` in a widget body is safe. `settings-rows` asks for the
  source panel and says how tall it is; a widget without one shows its
  body from the start.
 
  The frame's four shared files are emitted first and deduplicated by
  `fedoras.state`, so a chapter with four widgets ships one copy of each.
  The widget's own sources come next, all of them, in the order
  `page-sources` works out, and the mount call comes last because by then
  there is something to call."
  [{:keys [widget id label foot focus? settings-rows mount]} & body]
  (let [node-id (or id (name widget))
        attributes (cond-> {:id node-id
                            :class (str "widget widget-" (name widget))}
                     focus? (assoc :tabindex 0))
        head [:div.roll-head
              [:span.roll-label (or label (str/upper-case (name widget)))]
              [:span.widget-state (when settings-rows "press Run")]]
        panel (when settings-rows (source-panel widget settings-rows))
        shell-body (into [:div {:class (str "widget-body"
                                            (when-not settings-rows " widget-body-shown"))}]
                         (remove nil? body))
        parts (remove nil? [head panel shell-body
                            (when foot [:p.roll-foot foot])])]
    (kind/hiccup
     (-> [:div
          (state/styles frame-styles)
          (state/styles legend-styles)
          (state/source frame-runtime)
          (state/source legend-runtime)
          (state/styles (stylesheet widget))]
         (into (map state/source (page-sources widget)))
         (conj (into [:div attributes] parts))
         (conj (state/call (mount-symbol widget) (merge {:id node-id} mount)))))))