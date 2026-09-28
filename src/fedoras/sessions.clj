(ns fedoras.sessions
  "What the animal did, session by session.

  Every session file used to carry its own copy of the same forty lines
  of JavaScript: a lookup, a paint function, and a guess at when the
  page was ready. That is how the blank-passage bug got in, and it would
  have got in again in session five.

  A session file is now a docstring, a map of twenty passages, and one
  call to `passage`. There is no JavaScript in it and no boot logic in
  it, because the runtime in `fedoras.state` owns both."
  (:require [fedoras.companions :as companions]
            [scicloj.kindly.v4.kind :as kind]))

(defn arrival
  "The shape of what came out, in the fiction, in the corridor. Reads
  whatever animal the reader actually rolled and gives back the
  passage written for it. Every session file has one of these."
  [n]
  (get-in companions/roster [n :arrival]))

(defn passage
  "Show the passage for whichever animal the reader rolled.

    (sessions/passage \"three\" encounters)

  `id` must be unique on the page. `entries` is a map of 1..20 to
  strings. No JavaScript and no boot logic: the body below is
  ClojureScript, registered as a watch on the reader's state, so it
  runs when the page exists and again whenever anything changes."
  [id entries]
  (let [node-id (str "session-" id)]
    (kind/hiccup
     [:div
      [:div.session {:id node-id}
       [:p.session-who]
       [:p.session-text]]
      [:script {:type "application/x-scittle"}
       (pr-str
        (list 'fedoras.reader/on-render! (keyword "session" id)
              (list 'fn []
                    (list 'when-let ['node (list 'fedoras.reader/el node-id)]
                          (list 'if-let ['c (list 'fedoras.reader/companion)]
                                (list 'do
                                      (list 'fedoras.reader/text! 'node ".session-who"
                                            (list 'clojure.string/upper-case (list :name 'c)))
                                      (list 'fedoras.reader/text! 'node ".session-text"
                                            (list 'get entries (list :n 'c) "")))
                                (list 'do
                                      (list 'fedoras.reader/text! 'node ".session-who" "")
                                      (list 'fedoras.reader/text! 'node ".session-text"
                                            "Roll for your animal in Act I, Scene 5, and it will be here.")))))))]])))
