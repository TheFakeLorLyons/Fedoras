(ns fedoras.sessions.four
  "SESSION FOUR. What the animal does at the foot of the stairs.

  The party has gone up out of the basement of the tower and come out in
  another basement. The stairs are still there for about one paragraph.
  This is that paragraph: whatever the animal does with a staircase
  that goes up and arrives underneath itself. Then the stairs are gone.

  Every passage is the animal testing the building in its own way, and
  every one of them comes back with the same answer, which nobody at the
  table says out loud."
  (:require [fedoras.companions :as companions]
            [fedoras.sessions :as sessions]))

(def stairs-by-animal
  {"Pigeon"
   (str "The pigeon does what a pigeon does in a strange building and goes up "
        "to get its bearings. It flies up the stairs and out of the lamplight, "
        "and a moment later it walks out at the foot of the same stairs, "
        "behind the party, head going. It looks up the stairs with one orange "
        "eye. It flies up again. It walks out at the bottom again. Victor "
        "narrates it four times, the same way each time, the flight up and the "
        "walk in, and the fourth time the pigeon does not go up at all. It "
        "settles on the bottom step with its feet tucked under, facing the "
        "room, and waits for somebody else to try.")

   "Giant Centipede"
   (str "The centipede goes up the stairs, and while its tail is still going "
        "up, its head comes out at the foot of the stairs behind the party. It "
        "keeps coming. For a while it is a ring: up the stairs, out at the "
        "bottom, across the floor and up the stairs again, head following "
        "tail. Victor looks for the place where the head meets the tail, out "
        "loud, segment by segment, and does not find it. Simon asks which end "
        "is the front now. Victor says the centipede would like to know. It "
        "stops, coiled round the foot of the stairs in a closed loop, and "
        "Derek lifts both his feet off the floor of Room 3C and keeps them "
        "there.")

   "Marmot"
   (str "The marmot sits up at the foot of the stairs to look at the room. The "
        "looking goes badly, so it chirps. The chirp comes back from above "
        "them, and from below them, and from somewhere across the room where "
        "there is no more room, all at the same moment and exactly on time. No "
        "echo is that punctual. The marmot gets down off its hind legs, lies "
        "flat on the bottom step with its paws over its nose, and does not "
        "look at anything else. Victor, who demonstrated the chirp in the "
        "first week, does not demonstrate this one.")

   "Cat"
   (str "When the party steps out into the second basement the cat is already "
        "there, up on a shelf with its tail over its feet, as though it has "
        "been waiting some time and they are late. Nobody saw it go ahead of "
        "them. Simon asks whether it came up with them. Victor says it came. "
        "Derek asks which way. Victor says the cat knows which way. The cat "
        "gets down, goes to the foot of the stairs, and sits facing them with "
        "its ears forward, the way it sat in the storage room facing the empty "
        "corridor. It does that twice. The second time, Derek stops eating.")

   "Turkey"
   (str "Somewhere in the tower, the bell on the desk rings. Nobody is at the "
        "desk. The turkey answers it. The bell rings again and the turkey "
        "answers again, louder, and its head goes blue to white to red in the "
        "time it takes Victor to say so. On the third ring Derek says the "
        "thing the whole table has noticed, which is that the bell is below "
        "them. The turkey answers the bell until the bell gives up. Then it "
        "stands at the foot of the stairs with its tail fanned, facing the "
        "floor, and waits for the floor to try again.")

   "Giant Badger"
   (str "The badger digs, as it digs everywhere, straight down through the "
        "floor at the foot of the stairs, and Victor describes it going: the "
        "flags, the soil, the claws, the dark. Then he describes it coming "
        "through the ceiling. It drops onto a shelf in a shower of plaster, "
        "sneezes, and looks up at the hole it came out of and down at the hole "
        "it went into, which are one directly above the other. Miles gets up "
        "and draws both holes on Victor's whiteboard tower, one over the "
        "other, and joins them with a line, and then stands with the marker "
        "over the line for a long time, because he does not know which end to "
        "put the arrow on.")

   "Giant Weasel"
   (str "The weasel goes up the stairs and round the room and up the stairs "
        "again, faster each time, and Victor calls its position as it goes. "
        "Somewhere in the third lap he calls it coming out at the foot of the "
        "stairs while it is also going up them. For a moment there are two of "
        "it. Then there is one. It comes to the Ranger and lays the man's "
        "spectacles at his feet, with great ceremony, as it did the first "
        "night, and backs off, and watches to see whether he is pleased. "
        "Everything in the basement is damp. The spectacles are dry.")

   "Fox"
   (str "The fox has its nose to the dust at the foot of the stairs, where "
        "there is a set of prints: small, narrow, going up and coming down, "
        "many times over. Victor lets the table look at them for a while. Then "
        "he says they are the fox's. Miles asks when it made them. Victor says "
        "the prints are not new. The fox sits down beside them and looks at "
        "the Ranger the way a guest at a party looks at somebody who has "
        "finally turned up, and waits for him to ask it the way.")

   "Wolverine"
   (str "The wolverine walks the second basement end to end, slowly, and marks "
        "the corners, and the smell reaches the table before Victor has "
        "finished describing it. Then it goes up the stairs. It comes out at "
        "the foot of them a moment later, behind the party, and walks to the "
        "first corner, and smells it, and stops. It is its own mark, a minute "
        "old. Victor lets the wolverine think about that, and then lets the "
        "table think about it, and Derek pushes the soup away. The wolverine "
        "lies down across the foot of the stairs with its chin on its paws. "
        "Victor says it has decided that nothing else is going up them.")

   "Dog"
   (str "The dog takes the stairs two at a time, delighted, because stairs are "
        "a game, and comes bounding out at the foot of them behind the party, "
        "and does it again, faster, with the whole back half of it going, "
        "until the table is laughing. On the fifth time it does not come back "
        "out. Victor waits. The table waits. Then the dog comes down the "
        "stairs from the top, slowly, one step at a time, which it has not "
        "done once, and comes to the Ranger, and puts its head against his "
        "knee, and leans, and does not stop leaning.")

   "Python"
   (str "The python keeps well away from the stairs. It lies along the foot of "
        "the wall with its head raised and the pits along its lip working, and "
        "Victor says it is looking for the warmest thing in the room. It turns "
        "its head up. It turns its head down. The warmest thing in the room, "
        "Victor says, is directly above them, and also directly below them, "
        "and it is the same thing. Derek asks what it is. Victor says it is "
        "about the size of a person, and it is lying still. The python lays "
        "its head on its coils where it can watch the ceiling and the floor at "
        "once.")

   "Mule"
   (str "The mule is at the foot of the stairs when the party comes out, "
        "panniers on, head down. It would not climb the first staircase, on "
        "the ground floor, and they left it there with the chairs, and Miles "
        "wrote it down. Miles finds the entry and reads it out: MULE. DECLINED "
        "TO PROCEED. Ground floor. Victor says the entry is correct. The mule "
        "has not moved. It looks at the party with the patience of an animal "
        "that has known where it was the whole time, and waits for them to "
        "catch up.")

   "Lynx"
   (str "In Victor's next sentence the lynx is at the top of the stairs. In "
        "the sentence after that it is at the bottom. He does not describe it "
        "moving, and nobody asks him to, because nobody at the table can say "
        "any more which of the two is higher. The lynx sits at the foot of the "
        "stairs and washes a forepaw. Then it looks across the table at "
        "Victor's notes, and Victor turns the page face down. The lynx goes on "
        "looking at the back of the page, and Victor, for once, is the one who "
        "looks away.")

   "Ape"
   (str "The ape takes the pencil from behind its ear and makes a mark on the "
        "wall at the foot of the stairs, at the height of its own eyes. Then "
        "it climbs. Victor describes the long arms and the unhurried climb, "
        "and then the ape coming out at the foot of the stairs behind the "
        "party. It goes to the wall and looks at its mark, and makes a second "
        "mark beside it. It does the whole thing again. When it comes out the "
        "third time there are three marks on the wall, and a fourth it did not "
        "make, in the same pencil, in the same hand. The ape sits down under "
        "the marks and folds its arms.")

   "Horse"
   (str "The horse is in the second basement before them, standing with its "
        "head bowed under a ceiling too low for it, with plaster in its mane. "
        "Derek says it cannot have come up those stairs. Victor says it did "
        "not. It is saddled, the girth done up to the right hole, and the "
        "leather is dark with fresh oil, and nobody in the party has oiled it. "
        "Somebody has been looking after this saddle again. The horse comes to "
        "the Ranger, folds one foreleg, and bows, nose to the flagstones, as "
        "it bowed to him the first night.")

   "Moose"
   (str "The moose does not fit in the second basement, and Victor sets out "
        "the difficulty as he did the first night: the antlers are wider than "
        "the stairs, wider than the room, wider than the door at the front of "
        "the tower. The moose is in the room. It is facing the stairs. Asked, "
        "Victor says it has faced the stairs since the ground floor and has "
        "not turned round once, going up or coming down. Miles draws it to "
        "scale over Victor's tower on the whiteboard, and it covers the whole "
        "basement and the stairs, and he looks at that for a long while. Derek "
        "does not make the reversing noise. Nobody asks him to.")

   "Wyrmling"
   (str "The pack on the Ranger's back has been getting cooler since the "
        "ground floor, and Victor, asked, says it has. The Ranger opens the "
        "pack. The wyrmling is asleep, curled nose to tail, and it is cold, "
        "and Victor is quick to say that it is breathing. The Ranger puts his "
        "hand on it and keeps it there, and after a while the wyrmling is warm "
        "again, and so is his hand, and the rest of the party stands round him "
        "the way people stand round a fire. Derek asks what colour it is. "
        "Victor says it is the colour of the lamps.")

   "Falcon"
   (str "The falcon goes up the stairwell as it went up at the first sign of "
        "the hound, out of the lamplight, and there is the long pause the "
        "table has learned to wait through. Then Victor makes the noise, high "
        "and coming down, and the falcon comes out at the foot of the stairs "
        "behind the party, level, at the height of a man's head, and lands on "
        "the Ranger's arm with the talons through the same holes in the "
        "sleeve. It went up, and it came in at the bottom still stooping. It "
        "looks at the ceiling, and at the floor, and at the ceiling again. "
        "Then it lowers its head against the Ranger's chest and asks for the "
        "hood.")

   "Wolf"
   (str "The wolf stands at the foot of the stairs with its head low and "
        "howls, once. The others answer. Victor tells them where from, which "
        "is above: up in the tower, where the ground floor was, and the "
        "tavern, and the sound comes down the stairwell in no hurry. The wolf "
        "listens with its head on one side. Then it howls again, and this time "
        "the same voices answer from below. Miles asks how many wolves are on "
        "the sheet. Victor says one. The wolf lies down at the foot of the "
        "stairs, between the party and whichever way that is now.")

   "Bear"
   (str "The bear goes up the stairs, because the bear goes where it likes, "
        "and the stairs complain all the way up, and then the ceiling "
        "complains. Victor describes the bear walking about on the floor above "
        "them: the weight, the pauses, the sound of a shelf going over. Beside "
        "the Documentarian, in the basement, a shelf goes over. Nobody touched "
        "it. Then the stairs complain again, and the bear comes out at the "
        "foot of them behind the party with dust on its shoulders, and sits "
        "down beside the Ranger the way a hill would sit down if it could. The "
        "Documentarian picks the shelf up and puts it back. At the table, "
        "Miles does not write anything.")})

(def stairs
  "Face on the die to passage, derived from the roster so the two can
  never disagree about which animal is which."
  (into (sorted-map)
        (map (fn [[face {:keys [animal]}]]
               [face (get stairs-by-animal animal)]))
        companions/roster))

(defn animals-without-stair-passages
  "Animals on the roster this session has nothing written for. Empty
  when the file is finished."
  []
  (->> (vals companions/roster)
       (map :animal)
       (remove #(contains? stairs-by-animal %))
       vec))

(defn at-the-stairs
  "The passage for whichever animal the reader rolled in Act I."
  []
  (sessions/passage "four" stairs))