(ns fedoras.sessions.three
  "SESSION THREE. What the animal did when the Hollow Hall came.

  The party is in the registrar's house at the edge of the old road,
  the retrievers have been corrected, and something made of doors has
  come out of the trees for the key. The roll decides how close it
  gets. This decides what sends it away, and it is always the animal.
  One passage per animal, keyed by name as in sessions one and two.

  Nobody at the table asks why the Hall gives way to the animal. The
  druid half answers it, and nobody follows it up."
  (:require [fedoras.companions :as companions]
            [fedoras.sessions :as sessions]))

(def hall-by-animal
  {"Pigeon"
   (str "The pigeon walks out to meet the Hall with its head going, as it "
        "walked out to meet the hound. One of the doors swings open on the "
        "storage room in the Collegium: the shelves, the dust, the cage, and "
        "the bare wire on the front of the cage where the card used to be. The "
        "Hall is looking for the date. It leans over the pigeon. The pigeon "
        "looks back up at it with one orange eye. Miles says, quietly, that "
        "the date is in the pigeon. Victor says it is. The Hall waits a long "
        "time for the pigeon to produce it, and the pigeon, which has never "
        "once produced anything on request, sits down. Then the Hall folds "
        "itself back into the trees, one door at a time.")

   "Giant Centipede"
   (str "The centipede goes into the Hall by the first door that opens, and "
        "out of the next, and into the one after that, and Victor follows it "
        "door by door until it has been in and out of every door the Hall has. "
        "Then it keeps going, because there is more of it. The Hall tries to "
        "shut. It cannot shut a door with a centipede in it, and every door it "
        "has has a centipede in it. It stands in the clearing held open in "
        "every direction, and the table waits, and the centipede keeps coming. "
        "At last the Hall draws back into the dark, and the centipede slides "
        "out of it door after door, the whole length, like thread pulled out "
        "of a hem. Derek has had his feet off the floor since the first door.")

   "Marmot"
   (str "The marmot sits up on the step of the safehouse to look at the Hall. "
        "The looking goes badly, so it chirps, and at the table Victor chirps "
        "with it, the chirp he has practised. Every door in the Hall shuts at "
        "once. In Room 3C the door opens and somebody from next door puts "
        "their head round to ask whether everything is all right. Victor says "
        "it is. They go. In the forest the Hall stands with all its doors "
        "shut, like a corridor where somebody has shouted, and after a while "
        "it moves off into the trees without opening any of them. The marmot "
        "lies down on the step and goes to sleep.")

   "Cat"
   (str "The registrar says not to look at the Hall, so nobody looks at the "
        "Hall, and while nobody is looking at anything the cat goes. Victor "
        "describes the party's backs, the fire, the broken door. He does not "
        "mention the cat. When the Hall turns its doors towards the party, the "
        "cat is sitting in the one standing open, facing into it, with its "
        "tail over its feet, looking at whatever is behind it. The Hall cannot "
        "shut a door with a cat in it. Nobody can. After a while the Hall "
        "gives up that door and goes back into the trees without it, and "
        "leaves a single door standing in the forest with a cat sitting in it. "
        "The cat washes. Then it comes back.")

   "Turkey"
   (str "Every door in the Hall bangs as it shuts, and the turkey answers "
        "every one. A door shuts and the turkey gobbles. The door opens again, "
        "because a door that has been answered has not been shut, and shuts, "
        "and the turkey answers it. Its head goes red, and white, and blue. "
        "Victor does the doors with the flat of his hand on the folding table "
        "and the turkey with his mouth, faster and faster, until the Hall is "
        "opening and shutting everything it has at once and getting nowhere, "
        "and the turkey is shouting at the whole of it with its tail fanned. "
        "Then the doors hang still. The Hall backs away into the trees, and "
        "the turkey gets the last word.")

   "Giant Badger"
   (str "The badger goes under the Hall. Victor says so, and then describes "
        "the Hall as though there were nothing under it: the doors, the "
        "opening and shutting, the feeling behind each one. It is the worst "
        "thing he does all night. Then the ground gives. The Hall tilts, and "
        "every door along one side of it swings open at once onto earth, and "
        "something down there takes hold. There is a scraping, going away. "
        "When the badger comes up again beside the Ranger with soil in its "
        "fur, the Hall is still standing, lower and shorter, and it goes back "
        "into the trees at an angle, dragging. Miles does not ask where the "
        "rest of it is. He writes FILED before he is told.")

   "Giant Weasel"
   (str "The weasel dances in front of the Hall as it danced for the hound, "
        "sideways and backwards and straight up, twisting in the air. This "
        "time Victor stays in his chair. He describes it sitting down, and the "
        "hat stays where it is, and that is how the table knows. Every door in "
        "the Hall turns towards the weasel, and each opens a little wider at "
        "every leap, and on the last leap the weasel goes in through the "
        "nearest one. The door shuts. Nobody at the table breathes. Then every "
        "door opens at once, and the Hall empties the weasel out onto the "
        "forest floor and goes back into the trees at speed. The weasel sits "
        "up. It has the man's spectacles in its mouth, folded.")

   "Fox"
   (str "The fox is at the edge of the light when the Hall comes out of the "
        "trees, and it gets up and walks towards it. Victor says it knows the "
        "way. It goes to the nearest door and sits beside it and looks back at "
        "the Ranger, once. Then it goes through, and the door shuts. The table "
        "waits. A door on the far side of the Hall opens and the fox comes out "
        "of it with dust to the elbow, and trots back across the clearing, and "
        "sits down at the edge of the light again to clean its forelegs. "
        "Behind it the Hall folds itself up and goes. Miles asks whether the "
        "fox brought anything back. Victor says the fox knows where everything "
        "is.")

   "Wolverine"
   (str "The wolverine lies down across the step of the safehouse, facing the "
        "Hall, and decides. Victor gives the table the whole of the deciding, "
        "and it takes a while. Then it goes, all at once, with no thought for "
        "its size, in through the first door that opens, and the door shuts "
        "behind it. Victor describes the sound through the door, which is "
        "doors, and then something underneath the doors that doors ought not "
        "to have. Simon asks him to move on. When the wolverine comes back "
        "out, the Hall has fewer doors than it had, and it goes back into the "
        "trees without them. The wolverine lies down again across the step, "
        "and looks at the key in the Ranger's hand, and goes on looking.")

   "Dog"
   (str "The dog goes out to meet the Hall with its tail going, as it went out "
        "to meet the hound, and a yard short of it, it bows: elbows down, back "
        "end up. Victor describes the bow and stops. Nothing has ever bowed to "
        "the Hall. Its doors open and shut uncertainly, and then one of them "
        "opens and stays open, and the dog goes and puts its head against the "
        "frame of that door and leans, the way it leans on the Ranger's knee. "
        "The Hall stops. For a long moment it does nothing at all. Then it "
        "shuts that door gently, with the dog still leaning on it, and the dog "
        "steps back, and the Hall goes away into the trees. The dog comes back "
        "wagging, with the tag at its throat swinging, and Simon says oh.")

   "Python"
   (str "The python goes out across the clearing, and the pits along its lip "
        "go looking for the warmest thing in the Hall, and find nothing. "
        "Victor says the Hall has no temperature at all. The python wraps it "
        "anyway. It goes round the Hall once, and again, and Victor describes "
        "each coil and the sound the doors make as they are pressed shut one "
        "against the next, and then another coil, and another. Derek says you "
        "cannot fold a door. Victor says you can fold a hall. When the python "
        "lets go, the Hall is small enough to go back into the forest under "
        "the roots of a tree, and it does. The python comes back to the fire "
        "and lies down closer to it than anybody would.")

   "Mule"
   (str "The mule plants itself on the step of the safehouse, panniers on, "
        "facing out, and the Hall comes to it. The Hall opens a door in front "
        "of it. The mule looks at the door, and at the Ranger, and lowers its "
        "head slightly, and stays where it is. The Hall opens another door, "
        "closer. The mule stays where it is. Victor narrates each door in the "
        "same level voice, and the Hall goes through every door it has, and "
        "the mule declines every one, until there is nothing left for the Hall "
        "to offer. Then Victor rolls a die in front of the screen, in the "
        "open. He looks at it and does not say the number. He describes the "
        "kick. The Hall leaves the clearing by the far side, at some height "
        "above the trees. Miles does not need to be told what to write.")

   "Lynx"
   (str "In Victor's next sentence the lynx is on top of the Hall. He does not "
        "describe it getting there. It sits on the lintel of the highest door "
        "with its great feet folded under it, and looks down. Along the frame "
        "of every door, Victor now mentions, there is a line of small holes. "
        "The lynx tears along the first. The door comes away cleanly, a stub, "
        "and the lynx carries it down and sets it in front of the "
        "Documentarian, and in Victor's next sentence it is on top of the Hall "
        "again, tearing along the next. It brings the Documentarian four "
        "stubs. Then the Hall has had enough of being torn up for the records, "
        "and goes back into the trees with gaps in it, and the lynx is on the "
        "roof of the safehouse, washing a forepaw.")

   "Ape"
   (str "The ape takes the pencil from behind its ear. It walks up to the Hall "
        "and chooses a door, and on the frame of that door, at the height of "
        "its own eyes, it begins to write. Victor describes the tip of its "
        "tongue at the corner of its mouth and the pencil moving steadily. He "
        "does not describe what it writes. When it has finished it signs, and "
        "steps back, and the Hall reads it. Every door turns to read it. Then "
        "the Hall goes back into the trees, quickly and quietly, as a person "
        "leaves a room in which they have just been named. Miles asks what the "
        "ape wrote. Victor says it must have been true.")

   "Horse"
   (str "The horse walks out to the Hall with its head up and its ears flat, "
        "and Derek says the word before Victor does. The horse leaves the "
        "ground with all four feet at once, in a clearing this time with room "
        "for it, and at the top of the leap it kicks out behind with both hind "
        "legs, level. The doors on that side of the Hall go in like a drawer "
        "of files dropped down a stairwell. The Hall turns, and the horse "
        "comes down, and goes up again, and the other side goes in. What is "
        "left of the Hall goes back into the trees in no particular shape. The "
        "horse stands blowing in the clearing with the firelight on its "
        "saddle, and turns its head, and waits for the Ranger to get on.")

   "Moose"
   (str "The moose cannot turn round in the safehouse, so it goes out of the "
        "door backwards, the way it came out of its cage, and backs across the "
        "clearing towards the Hall without once looking at it. Derek starts "
        "the beep. Victor lets him. The Hall opens every door it has towards "
        "the moose, to show it what is behind them, and the moose, facing the "
        "other way, sees none of it and keeps backing. The antlers reach the "
        "first door, and the second, and the Hall learns that a door is no use "
        "at all against something that will not look at it. When the beeping "
        "stops the moose is standing where the Hall was, entirely calm, still "
        "facing the party, and the Hall is somewhere behind it in the trees, "
        "going.")

   "Wyrmling"
   (str "The pack on the Ranger's back gets warm, and then hot, and then the "
        "wyrmling puts its head out over his shoulder, awake, and looks at the "
        "Hall. Victor takes off the hat and sets it on the floor beside his "
        "chair, and Derek stops eating. The breath is short. It goes in at the "
        "nearest door, and for a moment every door in the Hall is lit from the "
        "inside at once, and the table can see into them, and Victor describes "
        "what is in them, one door at a time, and nobody interrupts him. Then "
        "the doors burn, and the Hall goes back into the trees with every "
        "doorway black and smoking. Derek asks what colour the fire was. "
        "Victor says it was hard to tell in that light.")

   "Falcon"
   (str "The registrar tells everybody not to look at the Hall, and everybody "
        "does as they are told. The falcon, which spent three years in a hood, "
        "looks at the Hall with both black eyes. It is the only one of them "
        "that does, and every door in the Hall turns towards it. Then it goes "
        "up, out of the firelight and above the trees, where there is no sky "
        "tonight, and for a while there is nothing. Then Victor makes the "
        "noise. It comes down faster than anything can fall and hits the Hall "
        "with a closed foot, and the Hall comes apart into doors, flat, that "
        "come down all over the clearing and slide away into the trees. The "
        "falcon is on the Ranger's arm. The talons go through the same holes "
        "in the sleeve.")

   "Wolf"
   (str "The wolf goes out and stands in front of the Hall with its head low, "
        "and waits. The Hall opens a door. From behind the door, Victor tells "
        "them, there is a sound, and he tells them what it is and how long it "
        "lasts: claws on stone, several sets of them, none in any hurry. It is "
        "coming from inside the Hall. The Hall hears it too. It shuts that "
        "door from the inside, and the next, and the sound comes on, and the "
        "Hall backs into the trees shutting doors as it goes, and the sound "
        "follows it. When the forest is quiet again the wolf comes back to the "
        "Ranger alone. Miles asks how many wolves there are now. Victor says "
        "one is on the sheet.")

   "Bear"
   (str "The bear goes out of the safehouse and stands up. It goes up and up "
        "until it is taller than the Hall, and it looks down, and the Hall, "
        "which is the size of a door, looks up. Victor is on his feet. Nobody "
        "saw him get up. He brings his hand down flat on the folding table, "
        "and the tin jumps, and the speaker in the corner cuts out. In the "
        "forest the bear brings its paw down on the Hall. When it lifts the "
        "paw the Hall is a single door lying flat on the ground, and it slides "
        "away under the trees like a letter pushed under a door. Victor sits. "
        "After a moment the ambient track comes back on, by itself.")})

(def hall
  "Face on the die to passage, derived from the roster so the two can
  never disagree about which animal is which."
  (into (sorted-map)
        (map (fn [[face {:keys [animal]}]]
               [face (get hall-by-animal animal)]))
        companions/roster))

(defn animals-without-hall-passages
  "Animals on the roster this session has nothing written for. Empty
  when the file is finished."
  []
  (->> (vals companions/roster)
       (map :animal)
       (remove #(contains? hall-by-animal %))
       vec))

(defn at-the-hall
  "The passage for whichever animal the reader rolled in Act I."
  []
  (sessions/passage "three" hall))