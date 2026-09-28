(ns fedoras.artifacts
  "The university, told through its own paperwork.

  A campus satire set among programmers is not obliged to stay inside
  sluglines and dialogue. These people communicate through Discord logs,
  commit messages, terminal output, syllabi, moderation actions, and
  arguments with a language model at four in the morning. Each of those
  is a rendering function here, so a scene can simply cut to one."
  (:require [clojure.string :as str]
            [fedoras.screenplay :as sp]
            [scicloj.kindly.v4.kind :as kind]))

(defn- line [s] (into [:span] (sp/interpolate s)))

(defn discord
  "A Discord log.

    (discord {:server \"FEDORA SOCIETY\" :channel \"#general\"}
             [{:who \"Victor\" :role \"SUPREME MODERATOR\" :at \"2:14 AM\"
               :text \"Agenda item one remains unresolved.\"}])"
  [{:keys [server channel]} messages]
  (kind/hiccup
   [:div.artifact.discord
    [:div.discord-head
     [:span.discord-server server]
     [:span.discord-channel channel]]
    (into [:div.discord-body]
          (for [{:keys [who role at text reacts]} messages]
            [:div.discord-msg
             [:p.discord-meta
              [:span.discord-who (line who)]
              (when role [:span.discord-role role])
              (when at [:span.discord-at at])]
             (into [:div.discord-text]
                   (map (fn [t] [:p (line t)])
                        (if (sequential? text) text [text])))
             (when reacts
               (into [:p.discord-reacts]
                     (map (fn [r] [:span.react r]) reacts)))]))]))

(defn modlog
  "A moderation log entry. Always play these completely straight."
  [& entries]
  (kind/hiccup
   (into [:div.artifact.modlog]
         (map (fn [e] [:p (line e)]) entries))))

(defn commit
  "A git commit. The commit message is where this book keeps its
  confessions."
  [{:keys [hash author date]} & message]
  (kind/hiccup
   [:div.artifact.commit
    [:p.commit-hash (str "commit " hash)]
    [:p.commit-meta (line (str "Author: " author))]
    (when date [:p.commit-meta (str "Date:   " date)])
    (into [:div.commit-msg] (map (fn [m] [:p (line m)]) message))]))

(defn terminal
  "Terminal output. `lines` beginning with \"$ \" render as input."
  [& lines]
  (kind/hiccup
   (into [:div.artifact.terminal]
         (map (fn [l]
                [:p {:class (if (str/starts-with? (str l) "$") "term-in" "term-out")}
                 (line l)])
              lines))))

(defn ai-chat
  "A conversation with an in-universe model. These are software, not
  characters: CAMPUSGPT, DeanBot, TA-9000, OfficeHoursAI. They answer
  literally, they answer instantly, and they are the only entities on
  campus with no interest whatsoever in what anybody thinks of them.

  `:system` is optional and, when supplied, renders as a folded block --
  the character's prompt IS the characterization."
  [{:keys [model system]} turns]
  (kind/hiccup
   [:div.artifact.aichat
    [:p.ai-model model]
    (when system
      [:details.ai-system
       [:summary "system prompt"]
       [:pre (line system)]])
    (into [:div.ai-body]
          (for [{:keys [role text]} turns]
            [:div {:class (str "ai-turn ai-" (name role))}
             [:p.ai-role (str/upper-case (name role))]
             (into [:div.ai-text]
                   (map (fn [t] [:p (line t)])
                        (if (sequential? text) text [text])))]))]))

(defn email
  "Mail from an administrator. Subject lines do most of the work."
  [{:keys [from to subject date]} & body]
  (kind/hiccup
   [:div.artifact.email
    [:table.email-head
     [:tbody
      [:tr [:td "From:"] [:td (line from)]]
      [:tr [:td "To:"] [:td (line to)]]
      (when date [:tr [:td "Date:"] [:td date]])
      [:tr [:td "Subject:"] [:td [:strong (line subject)]]]]]
    (into [:div.email-body] (map (fn [b] [:p (line b)]) body))]))

(defn handout
  "A document from the university: syllabus, form, flyer, exam page."
  [title & blocks]
  (kind/hiccup
   (into [:div.artifact.handout [:p.handout-title title]]
         (map (fn [b]
                (if (vector? b)
                  b
                  [:p (line b)]))
              blocks))))

(defn spec-table
  "Two-column rows for handouts: prerequisites, grading, office hours."
  [rows]
  [:table.handout-table
   (into [:tbody]
         (for [[k v] rows]
           [:tr [:td.k k] [:td.v (line v)]]))])
