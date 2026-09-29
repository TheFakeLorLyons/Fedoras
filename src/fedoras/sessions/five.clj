(ns fedoras.sessions.five
  "SESSION FIVE — the last one, and the thing that could not be filed.

  The ledger reconciled. The professor is teaching. The class is full,
  the corridor is clean, and it has all started again with a different
  clerk and a different lecturer and the same line.

  Four people remember, and so does the animal, and the animal is the
  only object that came out of that corridor which Miles has never
  managed to get into a column.

  One passage per entry on the roster. None of them is a goodbye. Every
  one of them ends in September, because the reader keeps the animal
  and the campaign starts again with new characters and the animal is
  not a character."
  (:require [fedoras.sessions :as sessions]))

(def last-night
  {1  (str "It is on the table when Miles opens the ledger to write the last entry and it is "
           "on the table when he closes it, and there is no column in that ledger for a "
           "pigeon and there never has been. Victor rules that it goes back to the one roof "
           "it knows and comes back on its own. In September it comes back on its own, into "
           "a different room, and lands on a different table.")
   2  (str "It went under the boards in the second session and nobody has seen the whole of "
           "it since, and the party has been finding evidence of it all year, and not one "
           "piece of that evidence is a thing Miles is allowed to write down. Victor says it "
           "is in the fabric now. In September something moves under the floor of a new "
           "campaign and Derek says out loud that he knew it.")
   3  (str "Nobody can say when it arrived. It was in the pack, and the pack was on the "
           "ranger's back, and the ranger cannot tell anybody which day. Miles will not enter "
           "a thing without a date on it and Victor will not give him one, and it is the only "
           "line in nine months of minutes that is left blank. It is in the pack in September "
           "and it is still the size of a loaf.")
   4  (str "It is at the door. It was at the door before Victor said the campaign was over "
           "and it was at the door before anybody stood up. Miles asks what to put and Victor "
           "says put that it was at the door. In September it is at the door of a room it has "
           "never been in, waiting, and nobody in that room has met it.")
   5  (str "The last sound in that room is not anybody's voice. Victor makes the noise one "
           "more time with his own throat and the whole table joins in badly and the Janitor "
           "opens the door before he has been asked to. Miles writes noise, sustained, and "
           "rules a line under it. In September somebody makes the noise before Victor has "
           "sat down.")
   6  (str "Everything in that fiction was put back overnight and the hole in the floor was "
           "not, because nobody put the hole there on purpose and the correction only "
           "reverses things that were meant. Miles enters it in the column he ruled in "
           "October, which is HEARD, and it is the only physical object in that column. The "
           "hole is still there in September.")
   7  (str "It goes ahead. It has gone ahead into every room for nine months and it goes "
           "ahead out of this one, into the corridor, before anybody has said the campaign is "
           "over, and it does not come back to say whether there is anything in it. Victor "
           "lets it go. In September it is in the room before the party is.")
   8  (str "Victor says one final time that it has been following the ranger since before the "
           "campaign started, and Miles asks one final time for how long, and Victor says one "
           "final time that he is not going to say. It is at the edge of the light in "
           "September and the light is a different light and the distance is the same.")
   9  (str "It is not in the room when Victor finishes, because it is somewhere else doing "
           "the afterwards. Victor does not describe it this time, which the table notices "
           "and nobody mentions. Miles writes ongoing, and Victor lets him, and it is the "
           "only entry in the ledger written in the present tense.")
   10 (str "It is asleep under the table with its head on somebody's foot and it has been "
           "there two hours and whoever's foot it is has not moved. Miles writes the last "
           "entry of the year with a dog on his foot. In September it comes into the new room "
           "ahead of everybody and is glad to see all four of them and has forgotten "
           "nothing.")
   11 (str "It has not hurried at any point in nine months and it does not hurry now. It is "
           "on the beam. Victor says nothing about it at the end and does not need to, and "
           "nobody looks up. In September it is on a different beam and got there without "
           "anybody seeing it move.")
   12 (str "It is outside, with everything the party owns still on its back, and it will not "
           "come in, and nobody in nine months established why it would not enter the one "
           "room it would not enter. Miles has that as an open item and it stays open, and "
           "an open item carries forward. It is outside in September.")
   13 (str "It comes back with something. Victor takes about four seconds and then rules that "
           "it is the thing the party lost in the second session and had stopped mentioning "
           "by the fourth, and Derek says the name of it out loud before Victor does. In "
           "September it brings back something nobody has lost yet.")
   14 (str "There is a door in that fiction that everybody agreed was closed, and locked, and "
           "made good, and it is open. Victor rules that it will not shut and that nobody is "
           "going to be able to shut it. Miles asks whether that goes in and Victor says yes, "
           "and it is the only thing about the last night that does.")
   15 (str "It is at the gate, saddled, and in nine months nobody has established who saddles "
           "it. Victor returns to the question one final time, unprompted, at about half past "
           "eleven, and does not answer it. It is at the gate in September and the girth has "
           "been done up by somebody with two hands.")
   16 (str "It cannot turn round. It is facing the way it has faced since the corridor, which "
           "is out of the room, along the corridor, towards whatever is next, and there is "
           "nothing anybody can do about that and nothing anybody wants to. Simon says so out "
           "loud and is right, and Victor writes it on the notes rather than in the minutes.")
   17 (str "Still asleep. Still warm. Nine months and it has not opened its eyes since the "
           "night it arrived, and Victor has never once been drawn on what happens when it "
           "does. Miles asks whether to close the entry. Victor says leave it open. It is "
           "asleep in September and it is warmer.")
   18 (str "It is above all of it and has been the whole time, and the ranger has never had a "
           "way of asking it anything, which stopped being funny at about the fourth session. "
           "It sees the ledger reconcile and the class fill and the corridor come clean, and "
           "there is no way for any of that to reach the table. In September it is still up "
           "there.")
   19 (str "The ones the party was never allowed to ask about are not in the room. The one "
           "that is the companion is, and is between the ranger and the door, which is where "
           "it has been since the corridor. Victor is careful one final time to say that only "
           "one of them is the companion, and Simon asks about the rest, and gets the answer "
           "he has been getting since October.")
   20 (str "It is in the doorway. Victor cannot end a session with a bear in the doorway and "
           "ends it anyway, and nobody stands up, because nothing gets past it including the "
           "end of the campaign. Miles writes the last entry of the year sitting down. In "
           "September it is in a doorway again and it is a wider door and it is not wide "
           "enough.")})

(defn what-it-did-last
  "The passage for whichever animal the reader rolled."
  []
  (sessions/passage "four" last-night))