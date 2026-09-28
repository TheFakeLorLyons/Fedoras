(ns fedoras.personae
  "The cast, as data."
  (:require [clojure.string :as str]
            [fedoras.screenplay :as sp]
            [scicloj.kindly.v4.kind :as kind]))

(def company
  [{:group "Freshmen"
    :members
    [{:name "{pc-full}"
      :role "Provisional"
      :desc "Fixed the water on his block. Nobody worked out how, including him. Sent here for it."}
     {:name "MAYA FLORES"
      :role "Researcher, chemistry"
      :desc "Assigned. Her results are filed under a man's name. Reads the manual."}
     {:name "ELI BROOKS"
      :role "English"
      :desc "Loud. Unmarried. Not sorry."}
     {:name "LENA NOVAK"
      :role "Transfer, graphic design"
      :desc "Came from somewhere worse. Carries a portfolio case too big for the room."}]}

   {:group "The Fedora Society"
    :members
    [{:name "VICTOR \"VIC\" ASHCROFT"
      :role "Senior"
      :desc "Six years a senior. Says his family was on the list."}
     {:name "MILES"
      :role "Second year"
      :desc "Owns the chart."}
     {:name "DEREK"
      :role "Second year"
      :desc "Knows about Rome."}
     {:name "SIMON"
      :role "Second year"
      :desc "Played the Paladin last year, and is playing it again this year."}]}

   {:group "Athletes"
    :members
    [{:name "CHAD ARMSTRONG"
      :role "Quarterback"
      :desc "21 and captain of the football team. Adored by everybody."}]}

   {:group "Also Enrolled"
    :members
    [{:name "ASHLEY"
      :role "Student Senate"
      :desc "Everyone says she is very nice."}
     {:name "BRITTANY"
      :role "Media"
      :desc "Manages the school's student newspaper, and website."}
     {:name "MADISON"
      :role "Business"
      :desc "Will find an opportunity."}]}

   {:group "Faculty"
    :members
    [{:name "PROFESSOR EVELYN CARTER"
      :role "Chemistry"
      :desc "Door open. Knows everyone's names."}
     {:name "PROFESSOR AMELIA REYES"
      :role "Computer Science"
      :desc "."}
     {:name "PROFESSOR SAMUEL BENNETT"
      :role "English and History"
      :desc "Assigns reading. Nobody finishes it."}
     {:name "PROFESSOR HORACE WHITLOCK"
      :role "History"
      :desc "Old. Has not been replaced."}]}

   {:group "Administration"
    :members
    [{:name "DEAN MAGNUS STERLING"
      :role "Dean"
      :desc "Banners."}
     {:name "THE FOURTEEN VICE DEANS"
      :role "Administration"
      :desc "Fourteen."}
     {:name "THE BOARD OF TRUSTEES"
      :role ""
      :desc "Portraits."}
     {:name "THE REGISTRAR"
      :role ""
      :desc "Keeps enrolment at 1024."}]}

   {:group "Campus"
    :members
    [{:name "THE THEATRE GUILD" :role "" :desc "Audible from the quad."}
     {:name "THE GOTH COLLECTIVE" :role "" :desc "Run the lights. Forty on the sheet."}
     {:name "THE ROBOTICS CLUB" :role "" :desc "At war with the Chess Club."}
     {:name "THE CHESS CLUB" :role "" :desc "Disputes this."}
     {:name "THE ANIME SOCIETY" :role "" :desc "Best table at the fair."}
     {:name "THE STUDENT SENATE" :role "" :desc "Forms."}
     {:name "THE MARCHING BAND" :role "" :desc "The tuba."}
     {:name "CAMPUS RADIO" :role "" :desc "Broadcasting."}
     {:name "THE MUSIC MAJORS" :role "" :desc "Have not slept."}
     {:name "FRATERNITY AND SORORITY LIFE" :role "" :desc "Better catering."}
     {:name "THE GAMERS" :role "" :desc "Overlaps."}
     {:name "NUMEROUS BEWILDERED FRESHMEN" :role "" :desc "One thousand and twenty-four, minus."}]}

   {:group "Not Present"
    :members
    [{:name "THE LEGACY"
      :role ""
      :desc "Wrote the Helps. Worked it all out. Do not carry their own things."}
     {:name "[REDACTED]"
      :role "UNK"
      :desc "Меня здесь нет."}]}])

(defn who
  "Look a character up by (case-insensitive) name fragment."
  [fragment]
  (let [f (str/lower-case fragment)]
    (->> company
         (mapcat :members)
         (filter #(str/includes? (str/lower-case (:name %)) f))
         first)))

(defn render []
  (kind/hiccup
   (into [:div.personae]
         (for [{:keys [group members]} company]
           (into [:section [:h3 group]]
                 (for [{:keys [name role desc]} members]
                   [:div
                    (into [:p.who] (sp/interpolate name))
                    (when (seq role) [:p.what role])
                    (into [:p.desc] (sp/interpolate desc))]))))))

(defn count-speaking-roles []
  (->> company (mapcat :members) count))
