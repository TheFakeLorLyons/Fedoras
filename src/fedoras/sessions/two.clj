(ns fedoras.sessions.two
  "SESSION TWO. What the animal does at the toll booth.

  The party comes down the south road to collect a toll keeper's missing
  returns, and the animal stops short of the booth and will not go
  nearer. One passage per animal, keyed by name as in the other
  sessions. None of them is a warning, because the animal cannot warn
  anybody about anything. It refuses, and the table is left to work out
  what it refused.

  Three things are inside or under the booth: a box of names, a girl
  behind a curtain, and the old road. The passages know about all three
  and name none of them.

  The lookup does not depend on the reader being on the page where they
  rolled: the animal rides in the URL and in storage, and
  `sessions/passage` takes it from either."
  (:require [fedoras.companions :as companions]
            [fedoras.sessions :as sessions]))

(def refusals-by-animal
  {"Pigeon"
   (str "The pigeon walks up to the booth with its head going, the way it "
        "walks up to everything, and stops a foot short of the step. It walks "
        "the length of the step, and back. Derek says Decimus Brutus sent "
        "pigeons out of Mutina with letters tied to their legs, and that "
        "nobody ever asked the pigeons. The Documentarian asks whether he "
        "should write down the number on its ring again, in case the booth "
        "wants one. Victor says NO. He says it very loudly. He says that "
        "nothing is to be written down within a hundred yards of this booth by "
        "anybody, and the pigeon settles on the road facing the shutter and "
        "tucks its ringed foot up under itself, where nobody can read it.")

   "Giant Centipede"
   (str "The centipede goes into the gap under the booth's door, which is what "
        "it does with doors, and comes back out faster than it went in. Then "
        "it goes round the booth instead, the whole of it, along the foot of "
        "the wall, until its head meets its own tail at the step and it has "
        "put a ring round the booth made of itself. It lies there. Derek says "
        "Caesar did that at Alesia, a wall round a wall, and that it worked, "
        "and that it took him a month. Victor says the centipede did it in a "
        "sentence. Simon says it is beautiful and is told, not for the first "
        "time, that the colour is a warning. Simon says he knows, the way a "
        "man says it when nothing he has been warned about has ever happened "
        "to him.")

   "Marmot"
   (str "The marmot sits up in the road in front of the booth to look at it. "
        "The looking goes badly, so it chirps, and the chirp goes round the "
        "stone hut and comes back from inside it: the same chirp, quieter, a "
        "little late, and then a cough. Victor stops. He had not planned to do "
        "the cough. He did the cough. He looks at his own hand as though it "
        "did it. The marmot lies down flat in the road with its paws over its "
        "nose and will not go any nearer, and Derek, who has been leaning "
        "back, sits forward and asks who else is in there. Victor says NOBODY "
        "IS IN THERE, DEREK, very loudly, which is how everybody at the table "
        "knows that somebody is.")

   "Cat"
   (str "The cat will not go near the booth while anybody is watching it, and "
        "everybody is watching it, so it sits in the road with its tail round "
        "its feet and looks at the shutter. The Paladin looks away first, at "
        "the road, which he has been told is dangerous and which has never "
        "been dangerous to him. When he looks back the cat is on the "
        "windowsill of the booth with its face against the shutter, listening, "
        "and its tail has gone as thick as a bottle brush. Then it is back in "
        "the road. Nobody saw it come down. Miles writes NOT APPROVED, and "
        "Victor asks what is not approved, and Miles says the cat, generally.")

   "Turkey"
   (str "The turkey stops at the step of the booth and its head goes through "
        "every colour it has and settles on one Victor has not used before, "
        "which he calls the colour of an overdue return. It gobbles at the "
        "shutter, and from somewhere inside the booth a kettle answers it, a "
        "thin whistle that rises and cuts off. The turkey gobbles again. "
        "Nothing answers. Victor looks at his notes, turns a page, turns it "
        "back, and says SHITTE, quietly, in the old way, to the notes. The "
        "turkey stays in the road with its tail fanned, facing the shutter, "
        "waiting for the kettle to try again.")

   "Giant Badger"
   (str "The badger will not go through the door of the booth, so it goes "
        "under it, as it goes under everything, and the ground under the booth "
        "turns out to go down a long way, towards the river. It comes back out "
        "almost at once, backwards, with water in its fur that is not from the "
        "rain and a smell on it that Victor describes as very old road. Then "
        "it digs at the foot of the step, hard, down and away from the booth. "
        "Derek says it is looking for the road under the road, because there "
        "is always a road under the road, the Romans built every one of theirs "
        "on top of somebody else's. Victor looks at him for a long time and "
        "writes something on the back of his hand.")

   "Giant Weasel"
   (str "The weasel goes at the booth in a straight line at full speed, as it "
        "goes at everything, and stops dead on the step so hard that its back "
        "end overtakes its front. It stands up and looks at the shutter. Then "
        "it begins the dance, sideways and backwards and straight up, and "
        "Victor, who danced it with the weasel the first night and knocked "
        "over the tin, gets up to dance it again. He gets as far as the first "
        "sidestep. The weasel stops dancing and sits down in the road with its "
        "back to the booth, and Victor stops too, and stands in Room 3C with "
        "one foot off the floor, and says SHITTE, in the old way, and sits "
        "down.")

   "Fox"
   (str "The fox is at the edge of the light, where it always is, and comes no "
        "nearer to the booth than that. It sits in the bend of the road with "
        "its back to the booth and looks at the bridge beyond it, and then at "
        "the Ranger, and then at the bridge again, the way a guest at a party "
        "looks at the door while the host tells a long story. Victor describes "
        "the booth, the stone, the shutter, the step. The fox yawns through "
        "all of it. When Victor gets to the bridge the fox gets up, walks to "
        "the rail, looks over at the water for a long time, and comes back and "
        "sits in the road again, having shown them.")

   "Wolverine"
   (str "The wolverine lies down across the step of the booth, facing the "
        "shutter, and decides. Victor gives the table the deciding. It takes a "
        "while, and the smell comes off it in the meantime thick enough that "
        "Derek puts down what he is eating. Then the wolverine gets up, walks "
        "once round the booth, and marks all four corners of it with great "
        "deliberation. Derek says Romulus did that, went round the whole city "
        "and marked it, and then it was his. Victor says Romulus used a "
        "plough. Derek says the principle is the same. Victor says THE "
        "PRINCIPLE IS NOT THE SAME, DEREK, and the wolverine lies back down "
        "across the step, facing the shutter, in possession of a toll booth.")

   "Dog"
   (str "The dog trots up to the booth wagging and stops at the step as though "
        "it has come to the end of its lead. It sits. It looks at the shutter, "
        "and at the Ranger, and at the shutter, the way a dog tied up outside "
        "a shop looks at the door when somebody is inside. Simon, who has "
        "never once been refused anything by a dog, crouches and tells it that "
        "it is a good boy and pats the step, and the dog looks at the patted "
        "step with enormous sympathy and does not move. Simon says it has "
        "never done that to him before. Derek says there is a first time for "
        "everything. Simon says not usually, not for him, and is puzzled about "
        "it for the rest of the evening.")

   "Python"
   (str "The python reaches the booth last, pouring down the road after the "
        "party, and lifts its head at the step, and the pits along its lip go "
        "looking for the warmest thing inside. Victor tells them what the "
        "python finds, which is nothing. No stove, no kettle, no lamp. The "
        "booth is the temperature of a church. Derek says Victor told them "
        "there would be tea. Victor says THERE WILL BE TEA. Derek says the "
        "snake says there isn't. Victor says the snake is not the Dungeon "
        "Master, and the python lays itself along the step, the whole length "
        "of it, with its head turned towards the bridge, where the pits have "
        "found something warm a long way below the road.")

   "Mule"
   (str "The mule plants itself in the road a cart's length from the booth, "
        "panniers on, and becomes part of the road. The Cleric pulls. The "
        "Paladin offers it a mint, which it eats, and it stays where it is. "
        "Miles turns back to MULE. DECLINED TO PROCEED. and asks whether he "
        "can write DITTO under it. Victor says ditto is not a word the "
        "Collegium recognises. Derek says it is Italian. Victor says THEN IT "
        "IS CERTAINLY NOT A WORD THE COLLEGIUM RECOGNISES, and the mule, which "
        "has heard all of this before, moves its weight from one hind leg to "
        "the other and waits for the party to come to its senses.")

   "Lynx"
   (str "In Victor's next sentence the lynx is on the roof of the booth. In "
        "the sentence after that it is on the far rail of the bridge, and then "
        "in the road again, and in none of Victor's sentences is it inside the "
        "booth or anywhere near the door. Derek asks why it will not go in. "
        "Victor looks at his notes. The lynx, in the road, looks at his notes "
        "too, from the other side of the folding table, and Victor puts his "
        "hand flat on them and keeps it there for the rest of the session.")

   "Ape"
   (str "The ape stops at the step of the booth and holds out its hand to the "
        "Ranger, palm up. It wants the pencil. The Ranger gives it the pencil, "
        "and the ape looks at the shutter for a while, turning the pencil over "
        "in its fingers, and then it puts the pencil behind its ear and sits "
        "down in the road with its arms folded. Miles points out that a toll "
        "booth is exactly the kind of place a pencil is needed. Victor says "
        "the ape knows that. Miles asks why, then. Victor says BECAUSE, MILES, "
        "THERE ARE PLACES YOU DO NOT SIGN, and sweeps his arm across the "
        "folding table, and the dice go everywhere, and Derek catches two of "
        "them.")

   "Horse"
   (str "The horse will not go past the bend. It stands in the road with its "
        "head up and looks at the booth, and on the stone over the booth's "
        "door there is a crest, worn nearly flat, and it is the crest on the "
        "horse's breastplate. The wrong one. Derek says so. Victor says it is "
        "the crest. The horse folds one foreleg and bows to the booth, nose to "
        "the road, as horses were once taught to bow to kings, and then it "
        "backs away from it, three steps, and comes no nearer. Simon, whose "
        "paladin still cannot call a horse of his own, says the horse has "
        "lovely manners, the way a man says it about somebody else's staff.")

   "Moose"
   (str "The moose cannot fit through the door of the booth, and nobody "
        "expects it to try. It turns round. That takes a long time on a narrow "
        "road, and Victor gives it the time, every scrape of antler on the "
        "booth's stones, until the moose is standing with the booth at its "
        "back and its face to the way they came, entirely calm. Derek does the "
        "reversing beep, from habit. The moose does not reverse. Victor says "
        "it has done all the reversing it means to do on this road. Simon "
        "says, pleasantly, that it is a lovely animal, and asks whether moose "
        "are expensive, and Victor stares at him until he stops smiling.")

   "Wyrmling"
   (str "At the step of the booth the pack on the Ranger's back goes cold. "
        "Everything on the road is cold already, so it takes a moment to "
        "notice that the one warm thing the Ranger has carried all week has "
        "stopped being warm. The Ranger opens the pack. The wyrmling is "
        "asleep, curled nose to tail as it always is, with its eyelids shut "
        "tight, the way a child shuts its eyes against a room it does not want "
        "to be in. Derek asks what colour it is now. Victor says it is the "
        "colour of the booth. Derek asks what colour the booth is. Victor says "
        "GREY, DEREK, IT IS A TOLL BOOTH, and the Ranger closes the pack and "
        "goes no nearer.")

   "Falcon"
   (str "The falcon will not go near the booth. It sits on the Ranger's "
        "forearm with its talons through the same holes in the sleeve and "
        "looks at the shutter, and at the slit of dark under it, and then it "
        "turns its whole head away and presses it into the Ranger's shoulder. "
        "Victor says it wants the hood. The Ranger has the hood in his pocket. "
        "He leaves it there. Victor asks whether he is sure. The Ranger is "
        "sure. Simon says he would have put the hood on, and Victor says YES, "
        "SIMON, OF COURSE YOU WOULD, and Simon says thank you, because he "
        "thinks it was a compliment.")

   "Wolf"
   (str "The wolf stops in the road short of the booth and lifts its head and "
        "howls, once. From under the bridge, a long way under, the others "
        "answer, more of them than the Ranger has heard before. Victor does "
        "the howl, and then, without being asked, does all the answers too, "
        "one after another, each a little further off, until he is howling "
        "into his own sleeve and Miles has to wait for him to finish before he "
        "can ask anything. The wolf lies down in the road facing the bridge. "
        "Miles asks how many wolves are on the sheet. Victor, hoarse, says "
        "one.")

   "Bear"
   (str "The bear walks up to the booth and puts its nose to the shutter and "
        "breathes in, once, the whole width of the booth, and whatever it "
        "smells makes it sit down. It sits in the road with its back against "
        "the stone wall, and the booth moves a little on its foundations. "
        "Victor is on his feet. Nobody saw him get up. He tells them the bear "
        "is sitting against the booth the way you would sit against a door you "
        "did not want opened from the inside. Then he sits down, and says, in "
        "a very small voice from behind the shutter, that he would be grateful "
        "if the bear could lean on something else. The bear does not lean on "
        "something else.")})

(def refusals
  "Face on the die to passage, derived from the roster so the two can
  never disagree about which animal is which."
  (into (sorted-map)
        (map (fn [[face {:keys [animal]}]]
               [face (get refusals-by-animal animal)]))
        companions/roster))

(defn animals-without-refusals
  "Animals on the roster this session has nothing written for. Empty
  when the file is finished."
  []
  (->> (vals companions/roster)
       (map :animal)
       (remove #(contains? refusals-by-animal %))
       vec))

(defn at-the-booth
  "The passage for whichever animal the reader rolled in Act I."
  []
  (sessions/passage "two" refusals))