(ns fedoras.companions
  "The ranger's animal.

  Victor rolled it on a d20 at the table and wrote the result on the
  COMPANION line himself, and the reader gets to roll their own. The
  entry that comes up is what comes out of the cage in the storage
  room, and the table's reaction is part of it.

  The order is whatever Victor thought of first, which is why a mule is
  above a python and a wyrmling is below a falcon."
  (:require [clojure.string :as str]
            [fedoras.state :as state]
            [scicloj.kindly.v4.kind :as kind]))

(def roster
  "Twenty animals in the order Victor thought of them. The face on the
  die is the position in this list, counted from one, so an entry's
  number is where it stands and nothing else."
  (into (sorted-map)
        (map-indexed (fn [position entry] [(inc position) entry]))
        [{:animal "Pigeon"
          :plural "pigeons"
          :taxonomy "bird"
          :collective "kit"
          :other-names ["dusty wings" "coooo"]
          :intro (str "The cage is large enough for a man to stand up in, and "
                      "Victor lets the table take in the size of it before the "
                      "door swings open. A pigeon walks out. It is the "
                      "ordinary grey kind, the colour of the pavement it came "
                      "from, with one foot worse than the other, and it walks "
                      "with its head bobbing, stopping at the Ranger's boot, "
                      "and looking him up and down with one orange eye. There is a "
                      "ring on its leg. Simon asks if his character can read the ring. Victor "
                      "nods his assent, like a pidgeon; cooing loudly enough for the whole building to hear"
                      " \"- a letter and then six figures\", Simon says. "
                      "Miles jots it down with something resembling a twisted sort of joy."
                      "Derek says pigeons can tell one human face from another and keep a face in mind for years."
                      "Then the pigeon eats the card off the "
                      "front of the cage, which Victor allows, and then Miles asks what "
                      "the date was, and Victor says it is in the pigeon now."
                      "Miles, slightly unsatisfied, sneers at {pc} while Victor continues on...")}

         {:animal "Giant Centipede"
          :plural "centipedes"
          :taxonomy "arthropod"
          :collective "colony"
          :other-names ["clicker" "legs gooo"]
          :intro (str "The cage is a wire box about the size of a large carryon. "
                      "Victor, who has spent a good portion of the evening describing "
                      "dust, spends one sentence this 'cage'. Something in it "
                      "moves when the door opens. Twitching. A head comes out,  "
                      "antennae, the first pair of legs, and then a "
                      "segment. Another, and another, the box would seemingly have to be empty "
                      "long before the centipede has finished leaving it, yet "
                      "Victor keeps going. It keeps coming, the colour of a "
                      "varnished floor, the legs moving down the length of it "
                      "in mesmerising waves, until the front end is at the door of the "
                      "storage room and the back end is still coming out of a "
                      "box with nothing left in it. Simon "
                      "says that the Paladin thinks that its beautiful. Victor says the colour is a "
                      "warning. Simon says he knows.")}

         {:animal "Marmot"
          :plural "marmots"
          :taxonomy "rodent"
          :collective "madness"
          :other-names ["the alarm" "the fat rat"]
          :intro (str "The cage opens and nothing comes out of it for long "
                      "enough that Derek asks whether it is empty. Then a "
                      "marmot comes out. It is the size, and roughly the shape, "
                      "of a loaf - brown and going blond at the edges, and "
                      "although it has been in a cage in a locked room for "
                      "three years it has plainly eaten well the entire time and seems well rested. "
                      "It sits up on its hind legs to look at the Ranger. "
                      "Sitting up, it is still quite short. Miles asks what a "
                      "marmot is good for. Victor says it is for sitting up and "
                      "looking at things, and for chirping when the looking "
                      "goes badly, and demonstrates the chirp. It is piercing "
                      "and lasts too long. He has practised it. Someone from "
                      "the room next door puts their head round to ask whether "
                      "everything is all right, and Victor says it is nothing to worry about. "
                      "They go, and Miles writes down what a marmot is for.")}

         {:animal "Cat"
          :plural "cats"
          :taxonomy "felid"
          :collective "clowder"
          :other-names ["the cat" "cat"]
          :intro (str "The cage opens and the cat stays at the back of it. "
                      "Head low, Victor describes it deciding on its next move cautiously. "
                      "It is a black cat, fully grown and with a long and slender "
                      "body, yet short in temper - with an ear that "
                      "has seen at least one argument. It stays where it is for "
                      "as long as anybody is watching. The Paladin looks away "
                      "first. When he looks back the cat is out, and up on a "
                      "shelf, sitting with its tail over its feet as though it "
                      "has been there all evening and the party is late. The "
                      "Ranger holds out a hand. The cat considers the hand at "
                      "length. Miles writes NOT APPROVED and does not say by "
                      "whom. Twice before the end of the scene the cat gets "
                      "down, goes to the door of the storage room, and sits "
                      "facing the empty corridor with its ears forward, seemingly"
                      "listening for something that {pc} fails to perceive.")}

         {:animal "Turkey"
          :plural "turkeys"
          :taxonomy "fowl"
          :collective "rafter"
          :other-names ["thanksgiving dinner for non vegans" "gobbler"]
          :intro (str "The cage opens and the turkey comes out of it already "
                      "displaying. It is enormous, bronze and black and bald at "
                      "the head with a gigantic bouncing red comb. While Victor is "
                      "describing the head it goes blue (which is what a "
                      "turkey's head does when its feelings change - a "
                      "turkey's feelings change all the time), and it warbles at "
                      "the cage door as the door swings open. It gobbles at the "
                      "man on the floor. When Derek sets his mug down on the "
                      "folding table in Room 3C, Victor, in character, warbles "
                      "at the mug. Derek picks the mug up again. Miles asks "
                      "what it is for. Victor says it answers every sound it "
                      "hears, so anything that wants to come along the "
                      "corridor quietly will have to do it with a turkey "
                      "answering. Miles writes TURKEY (RESPOND WITH WARBLES).")}

         {:animal "Giant Badger"
          :plural "badgers"
          :taxonomy "mustelid"
          :collective "cete"
          :other-names ["angry grandpa" "stripes"]
          :intro (str "The cage door swings open and the badger ignores it, "
                      "having dug out through the bottom of the cage instead. "
                      "Victor describes all of it: the claws, the wire mesh "
                      "peeling back like foil. Then the floorboards and "
                      "whatever was under them. The open door stands "
                      "a foot to the badger's left throughout. The Paladin points "
                      "out the door. Victor says the badger is aware of their "
                      "presence It comes up again a yard in front of the Ranger, "
                      "broad and low and striped like a road, shakes the dust "
                      "off its back, and looks at him with  contempt reserved for an "
                      "animal that has always come and gone by its own "
                      "arrangements. It eventually leaves and goes back down the hole. The "
                      "storage room has one more way out than it did, and "
                      "Victor leaves it off the pizza box. Miles draws it in "
                      "himself, small, in the corner of his sheet, smirking to himself"
                      "with a sense of misplaced pride.")}

         {:animal "Giant Weasel"
          :plural "weasels"
          :taxonomy "mustelid"
          :collective "sneak"
          :other-names ["the scout" "slenderman" "the ermine"]
          :intro (str "The cage opens and the weasel is out before the door "
                      "has finished swinging: into the shelves and out, "
                      "up the man on the floor and down his other side, "
                      "twice round the Ranger and back to the cage, in the "
                      "time it takes Victor to say that the door is open. It "
                      "is long, brown on top and white underneath, built like "
                      "a draught excluder with teeth. It has the man's "
                      "spectacles. Nobody saw it take them, yet the man sits "
                      "blinking at a room he has been staring at for the last three years and "
                      "can no longer see. The weasel lays the spectacles "
                      "at the Ranger's feet with great ceremony, and backs "
                      "off, watching to see whether he is pleased. Miles "
                      "asks whether they can give them back. Victor says they "
                      "can try but will need to roll for it. Miles writes that"
                      "down, but does not elect to return the glasses, and instead"
                      "also writes those down as well.")}

         {:animal "Fox"
          :plural "foxes"
          :taxonomy "canid"
          :collective "skulk"
          :other-names ["foxxxy" "spry foot"]
          :intro (str "The Ranger lifts the latch and it comes up easily, too "
                      "easily, and Victor stops to say so. The inside of the "
                      "latch is worn bright against the rust. It has been "
                      "lifted before, many times, from the inside, and let "
                      "fall again. The fox steps out as though the evening had "
                      "been arranged for it. It is red and narrower than a fox "
                      "in a picture, white at the tip of the tail, black "
                      "stockings to the knee, and it walks once round the "
                      "Ranger at the distance of a guest who has been "
                      "introduced to him at a party and has not yet decided. "
                      "Then it goes and sits at the edge of the light from the "
                      "doorway, where the dust begins, and looks back at him. "
                      "Derek asks why it never left. Victor says it could have "
                      "gone any time it liked. Simon asks what it was waiting "
                      "for, and Victor looks for a moment at the line near the "
                      "bottom of {pc}'s sheet, and says that they'll have to"
                      "figure that out. Miles says the Documentarian logs it as "
                      "an anomaly.")}

         {:animal "Wolverine"
          :plural "wolverines"
          :taxonomy "mustelid"
          :collective "gang"
          :other-names ["the small trouble"]
          :intro (str "The bars of the cage have been chewed bright on the "
                      "inside, every one of them, all the way round, as high "
                      "as something could reach standing up. There are tooth "
                      "marks in the lock. The cage has held, and Victor growls "
                      "as loudly as he can. When the door opens the wolverine "
                      "stays where it is and looks at the Ranger through the "
                      "gap: low and broad, with a pale band down each side "
                      "like a saddle and the face of a small bear that has "
                      "been told something it did not like. Victor says it is "
                      "deciding whether he is the one who put it there. "
                      "It comes out, walks past him, and marks the corner of "
                      "the storage room - the smell covering a 30ft circle "
                      "according to Victor. Derek says that the Cleric pours out "
                      "his drink. The wolverine lies down across the doorway with "
                      "its chin on its paws, and Victor says it has decided to "
                      "accompany them after all.")}

         {:animal "Dog"
          :plural "dogs"
          :taxonomy "canid"
          :collective "pack"
          :other-names ["the dog" "the ordinary dog"]
          :intro (str "The cage opens and a dog comes out of it wagging. An "
                      "ordinary dog, brown and white, middling in size, its "
                      "ears undecided. It has been in a locked room for three "
                      "years and it comes out at a run anyway, and skids to a "
                      "stop in front of the Ranger, and sits, very correctly, "
                      "the way somebody once taught it to. Simon says \"aww\". It "
                      "has a collar, and a tag that has been filed flat on the "
                      "side where the name was. Victor lets them turn it over. "
                      "On the back is a line that says IF FOUND, and after "
                      "that the metal has been filed smooth as well."
                      "Miles notes that the documentarian logs the missing"
                      "dog name as an anomaly and suggests an interim name of"
                      "Marcus, to which Derek's ear's perk up and he shouts:"
                      "\"you ought to be one of those who, in a sense, are "
                      "unconscious of the good they do!!!!\" The documentarian "
                      "writes that down as well, under \"miscellania\"")}

         {:animal "Python"
          :plural "pythons"
          :taxonomy "serpent"
          :collective "knot"
          :other-names ["ssSSssSsss" "coilssss"]
          :intro (str "There are coils, and coils under those. It is some time "
                      "before anybody can say where the python begins, and by "
                      "then it has begun to come out. It pours over the lip of "
                      "the cage and onto the floor, and Victor describes it at the "
                      "speed it happens, so the table waits a long time for "
                      "the end of the sentence. The man on the floor says it "
                      "has eaten once. Miles asks when. The man says in the "
                      "first year. At last the head comes up, and turns. "
                      "The labial pits along its lip go looking for the warmest "
                      "thing nearby. It sinks its teeth into a shadowy form in"
                      "the shape of a dog that Victor describes as materializing "
                      "as it latches on. It buys them a free round in the coming "
                      "encounter. Victor says a python keeps what it takes.")}

         {:animal "Mule"
          :plural "mules"
          :taxonomy "equid"
          :collective "barren"
          :other-names ["dapple" "HeeHaw :("]
          :intro (str "The cage is plainly too small for a mule, and yet there "
                      "it is: a mule. Victor offers no explanation. The mule "
                      "offers a small, reluctant bray, which Victor reproduces "
                      "by screeching through pursed lips. The Ranger opens the "
                      "door and stands aside, and the mule looks at the open "
                      "door, and at the Ranger, and lowers its head slightly, "
                      "and stays where it is. The Cleric pulls. The Paladin "
                      "offers it something from his pocket, which Victor rules "
                      "is a carrot The mule eats the carrot and stays where it "
                      "is. Miles asks whether a companion that will not leave "
                      "its cage counts as a companion, and Victor says it counts "
                      "from the moment the door opens. When everybody has "
                      "given up and turned round, the mule is standing behind "
                      "them in the middle of the storage room with panniers "
                      "on, and in the panniers include everything that was on "
                      "the shelves. It has been in a locked room for three "
                      "years, and at some point in those three years it packed.")}

         {:animal "Lynx"
          :plural "lynxes"
          :taxonomy "felid"
          :collective "clowder"
          :other-names ["tufts" "yelper"]
          :intro (str "The cage opens and the lynx steps out, long in the leg, "
                      "with feet far too big for it, the size of a man's hands, "
                      "and tufts on its ears. It is thin. Victor says it is at "
                      "the bottom of its cycle. Miles, of all people, knows what "
                      "that means, and explains it with his pencil. For about a "
                      "hundred years a fur company kept account books of every "
                      "pelt it bought, and when somebody finally drew the lynx "
                      "pelts against the hare pelts, the lynx rose and fell every "
                      "ten years or so, a little behind the hares, like a tide. "
                      "The most famous animal in ecology was found in a ledger. "
                      "Miles is visibly moved. The lynx looks at the ledger in "
                      "his hands as though it knows exactly what ledgers are for. "
                      "Miles writes LYNX, and the date, and underneath, for the "
                      "cycle, and makes a small mark in the margin to be checked "
                      "in ten years.")}

         {:animal "Ape"
          :plural "apes"
          :taxonomy "primate"
          :collective "shrewdness"
          :other-names ["treecrawler"]
          :intro (str "The cage opens and an arm comes out first, a long one, "
                      "and the hand on the end of it takes hold of the door "
                      "frame and pulls the rest of the ape out after it. It is "
                      "big and red-brown and unhurried, with the face of an "
                      "uncle who expected more of you "

                      "Then it looks at the Documentarian. Victor holds his "
                      "hand out across the table. Miles, after a long moment, "
                      "takes one of his two pencils and puts it in Victor's "
                      "hand, and Victor passes it to {pc} and says the ape has "
                      "it now, and that it knows how to write. Miles says "
                      "that only the documentarian is permitted to write. "
                      "Victor agrees that this has always been the rule, but "
                      "still does not ask for the pencil back, and Miles marks "
                      "it in the Ranger's Companion's inventory."

                      "Simon says there was an orangutan in a zoo that kept "
                      "picking its lock with a length of wire and hid the wire in "
                      "its mouth so the keepers could never find it. The ape "
                      "opens its mouth. There is a bent wire in its cheek. Victor "
                      "lets that land. Then the ape gets up and puts one very "
                      "long hand on the Ranger's shoulder, as if it has been "
                      "waiting for somebody going the same way, and Miles writes "
                      "APE (HAS WIRE).")}

         {:animal "Horse"
          :plural "horses"
          :taxonomy "equid"
          :collective "harras"
          :other-names ["rocinante" "the one nobody rides"]
          :intro (str "The cage opens, a horse comes out of it, and the "
                      "storage room is taller than it was. Victor accounts for "
                      "neither. It is a grey, tall and old, gone white at the "
                      "muzzle and the knees, and it is saddled. The leather is "
                      "dark with oil, the buckles are bright, and the girth is "
                      "done up to the right hole. Somebody has been looking "
                      "after this saddle for three years in a locked room. On "
                      "the breastplate is the crest of the Collegium. The "
                      "Cleric leans in to look, and Derek says that is the "
                      "wrong crest. Victor says it is the crest. Derek says "
                      "the motto is wrong. Victor reads the motto out in a "
                      "language none of them recognize, and moves on. The horse "
                      "comes to the Ranger, folds one foreleg, and bows with "
                      "its nose to the floor, as horses were once taught to "
                      "bow to kings. Simon, whose paladin cannot call a horse "
                      "of his own until fifth level (which he is painfully "
                      "aware of), says nothing, and thoughtfully lines his "
                      "dice up again by size. "

                      "Victor calls it by a name nobody at the table has heard. "
                      "Derek asks where the name is from. Victor says \"an approved "
                      "book\" Derek asks which book, and Victor says it is not of"
                      "their concern, and moves on so quickly that Miles has to ask "
                      "him to repeat the name, and Victor declines, so Miles writes "
                      "HORSE (NAME WITHHELD).")}

         {:animal "Moose"
          :plural "moose"
          :taxonomy "cervid"
          :collective "herd"
          :other-names ["its a giant moose"]
          :intro (str "The cage opens and there is a moose in it, facing the "
                      "back. Victor sets out the difficulty: the antlers are "
                      "wider than the door of the cage. They are wider than "
                      "the door of the storage room. The moose cannot turn "
                      "round in the cage, and if it could, it still could not get out "
                      "of it, and thereafter, even if it got out - it still couldn't leave. "
                      "Victor explains, without being asked, that somebody \"must have built the "
                      "cage round it\". The table sets to work. Derek rolls a "
                      "sixteen for Strength and the Cleric takes the cage "
                      "apart. The Paladin measures the doorway with his arms. "
                      "Miles draws the moose on the pizza box to scale, and it "
                      "covers four squares and the door, and he looks at that "
                      "for a long while. While they are still arguing about "
                      "the door, the moose tips its head sideways, as moose do "
                      "among trees, and backs out of the cage, and backs out "
                      "of the storage room, and stands in the corridor looking "
                      "in at them with enormous calm.")}

         {:animal "Wyrmling"
          :plural "wyrmlings"
          :taxonomy "draconic"
          :collective "clutch"
          :other-names ["the wyrmling" "scales"]
          :intro (str "The cage is the only thing in the storage room with no "
                      "dust on it. Victor says so before the Ranger opens it "
                      "and waits for somebody to ask why. Nobody does, so the "
                      "Ranger opens it. Heat comes out, and nothing else. The "
                      "wyrmling is asleep, curled nose to tail on the floor of "
                      "the cage, about the size of a spaniel, and the card on "
                      "the front of the cage has gone brown at the edges. "
                      "Derek asks what colour it is. Everybody at the table "
                      "knows why he is asking. Victor says it is prismatic and "
                      "difficult to tell in this light. Derek asks again, "
                      "differently. Victor says its scales are the colour of "
                      "whatever light is on them, and there is not much light "
                      "at present. Before anybody has decided how to lift it, "
                      "it crawls out of the cage in its sleep and into the "
                      "Ranger's pack, and settles against his back, and he is "
                      "warm for the first time since the corridor. For the "
                      "rest of the evening Derek sits at the far end of the "
                      "table from {pc}, and asks about the colour at "
                      "intervals, and gets the same answer.")}

         {:animal "Falcon"
          :plural "falcons"
          :taxonomy "raptor"
          :collective "cast"
          :other-names ["the raptor" "true sight"]
          :intro (str "On a perch at the back of the cage there is a falcon in "
                      "a hood. The Ranger takes it off, and Victor says that the "
                      "falcon doesn't look at him. It steps out onto the top of "
                      "the cage, and stretches both wings as farrrrr as possible "
                      "(as Victor emulates)"
                      "straight out to the sides, and holds them there. Forty-two "
                      "inches, tip to tip. Miles found that number earlier because "
                      "Victor made him look it up, and Miles reads it off his sheet "
                      "now without being asked, and Victor does not thank him for "
                      "it, which Miles notes.\n\n"
                      "The Ranger holds out his arm. He has not done this before and "
                      "isn't sure of the best way to approach it. The "
                      "falcon looks at the arm, looks at him, steps onto it, and "
                      "closes its talons through the sleeve and into the forearm "
                      "underneath. Victor SCREAMS like a falcon (he has clearly"
                      "been practising) - long, shrill, and piercing, "
                      "and holds it for a full three seconds. Simon laughs. "
                      "Miles does not. Derek looks at the floor. The falcon does "
                      "not make the noise back. It is busy. It is holding on.")}

         {:animal "Wolf"
          :plural "wolves"
          :taxonomy "canid"
          :collective "pack"
          :other-names ["the wolf" "the packleader"]
          :intro  (str "The cage opens, and the wolf a wolf comes bounding out without , "
                       "hesitation, going straight past the Ranger to the door of the storage "
                       "room, and and stands with its head low, and a growl to match."

                       "It ranks them, pacing between each of them in turn, deciding where they stand, "
                       "and Victor narrates the deciding: the man on the floor "
                       "first, then the Ranger, then the Paladin, then the Cleric, "
                       "and the Documentarian last of all. Miles objects. Derek "
                       "says that the whole business of wolves ranking one another "
                       "came from a study of wolves kept in captivity, and that "
                       "the man who made it famous spent years asking people to "
                       "stop repeating it, because wolves in the wild live in "
                       "families and do nothing of the kind. Victor points out "
                       "that this wolf has been in a cage for three years. Miles "
                       "asks whether that means the ranking stands. Victor says in "
                       "here it does, and the wolf positions itself between the"
                       "Ranger and the door."

                       "It doesn't pay particular attention to them, because "
                       "it has already decided that they are not the ones to be paying "
                       "attention to.  Miles asks \"What it is doing?\", and Victor "
                       "says it is waiting for something on the other side of the "
                       "door. After a moment he adds that whatever it is waiting for "
                       "has been waiting too, and that the wolf has known about it "
                       "longer than any of them have.\n\n"

                       "The wolf howls, and Victor howls with it - a long one, well "
                       "practiced - thrown back into the room so that the whole building "
                       "has to hear it, then stops, and cups his hand to his ear, "
                       "and looks at Simon, and says: \"Well?\" ... No response. "

                       "Victor refuses to continue the game until everyone acquiesces "
                       "to howling as a pack. After enough back and forth, the whole table"
                       "grugingly accepts and low effort howls finally reverberated "
                       "back, to Victor's delight.")}

         {:animal "Bear"
          :plural "bears"
          :taxonomy "ursid"
          :collective "sloth"
          :other-names ["the bear" "the citadel"]
          :intro (str "The floor of the cage is worn. There is a groove in it in "
                      "the shape of an eight, and the bear is walking the groove, "
                      "round and across and round, and when the door opens it "
                      "keeps walking. Victor traces the eight on the pizza box "
                      "with his finger and keeps tracing it, round and across and "
                      "round, while he talks. Derek says bears in cages do that, "
                      "and that some of them go on doing it after they are let "
                      "out. The bear comes out of the cage still walking the "
                      "eight and walks it on the floor of the storage room, round "
                      "the man on the floor and across and round the Ranger, and "
                      "every time it passes the Ranger it slows, a little, and "
                      "puts its nose to his hand. Simon asks whether it will "
                      "stop. Victor keeps his finger going and says not tonight.")}]))

(defn grammar
  "The roster minus the arrival passages: what an animal is called and
  what the language does to it. This is what goes to the page, because
  every widget needs the grammar and only one widget needs the twenty
  paragraphs."
  []
  (into (sorted-map)
        (map (fn [[face entry]] [face (dissoc entry :intro)]))
        roster))

(defn animal-names
  "Kept so that session files written against the old API still compile.
  `fedoras.state` already emits the grammar on every page, so calling
  this is harmless and unnecessary."
  []
  (->> roster
       (map (fn [[face {:keys [animal]}]] (str face ":" (pr-str animal))))
       (str/join ",")
       (format "window.FEDORAS_ANIMAL_NAMES={%s};")))

(defn table
  "The roll. The reader gets whatever the reader gets, and it is kept."
  []
  (kind/hiccup
   [:div
    (state/source "fedoras/ui/companions.cljs")
    [:div.roll {:id "companion"}
     [:div.roll-head
      [:span.roll-label "COMPANION"]
      [:span.roll-dc "d20"]]
     [:div.roll-bar
      [:button.roll-go.companion-go "Roll for your animal"]
      [:button.roll-rewrite.companion-reroll {:disabled true}
       "Roll again — rewrite history"]
      [:span.roll-value.companion-roll]]
     [:p.companion-name]
     [:p.companion-kind]
     [:p.companion-intro]
     [:div.companion-naming.name-inputs {:style {:display "none"}}
      [:input.companion-input {:type "text" :placeholder "name it" :spellcheck "false"}]
      [:button.roll-go.companion-name-go "Write it on the sheet"]]
     [:p.roll-foot "Written on the sheet in ink."]]
    (state/call 'fedoras.ui.companions/mount! roster)]))