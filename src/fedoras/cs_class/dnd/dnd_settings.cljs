;; DND_SETTINGS.CLJS
;;
;; This is the module. It is a real file, it is on the classpath, and the
;; build reads it, which is why a stray paren in here fails the build
;; rather than the page. What is in the box below is what the session
;; actually runs on. Change it and press Run.
;;
;; The doors are the interesting part. Behind each one is a campaign that
;; was made and never played, and there is no register anywhere of which
;; ones exist. Add one. Nobody will check.

{;; How much the party has in them. Every failed check spends one, every
 ;; fumble spends two, every natural twenty gives one back. At nought the
 ;; building takes something and the evening stops where it stands.
 :nerve 6

 ;; Your turns before Ted is at the door.
 :evening 20

 ;; How long the session sits in each place before it moves whether or
 ;; not anybody has called it.
 :turns {:hallway    4
         :meeting    3
         :telling    4
         :walking    3
         :vault      3
         :chancellor 4}

 ;; One per door opened, in order, and the order is the joke turning into
 ;; the other thing. Run out and the hallway repeats the last one, which
 ;; is its own kind of correct.
 :doors
 ["A tavern that never got its party. Five stools at the bar and five character sheets face down on it, filled in, in five different hands. The ale is cold. Somebody set this up and waited."
  "A treasure room where the gold is painted wood. Every coin was painted separately, on both sides, and there are thousands of them, and whoever did it did not get bored."
  "A dungeon corridor with nothing in it. Alcoves, murder holes, a portcullis, a pressure plate, a drain: everything a corridor needs in order to be dangerous, and nothing was ever put in it."
  "A room with the battle map still out and the miniatures still standing on it, mid-turn, and it is one side's move. The dice by the map are showing numbers that were rolled and never read."
  "A room that is one character sheet the size of the wall, filled in completely, in a good hand, with the name left blank."
  "A room with nothing in it at all. No furniture, no marks on the floor, no dust. It smells like somebody's kitchen."]

 ;; The building noticing them. One line per place, and it never arrives.
 :pressure
 ["Nothing in this building has threatened anybody yet, and nothing does now."
  "The air is a degree cooler than it was and nobody has remarked on it."
  "Something further in has become aware that they are here. Not a sound. A change in what the air is doing."
  "It is aware of them and it is not approaching, and it has not needed to."
  "The awareness is mutual now, and being known sits differently in the chest than anything else in here has."
  "It is close. Do not show it, do not name it, and do not let it arrive tonight."]}