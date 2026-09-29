^{:kindly/hide-code true
  :clay {:quarto {:title "Scene 4 — Made Good"}}}
(ns act5.s4-made-good
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]
            [fedoras.artifacts :as art]
            [fedoras.cs-class.rogue.rogue :as rogue]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(sp/assets)

;; # Scene 4 — Made Good

^:kindly/hide-code
(sp/status :draft)

^:kindly/hide-code
(hb/epigraph 15)

;; ---

^:kindly/hide-code
(sp/scene
 (sp/slug "INT. PLANT SERVICES — TUESDAY 14 APRIL, 6:10 A.M.")

 (sp/action "One mug. Hollis has been in since five because the sheet says one"
            "day a week and Hollis has never in his ` been in for part of a"
            "day.")

 (sp/dialogue :pc
              "I want to take the tray out.")

 (sp/beat "Hollis does not look up.")

 (sp/dialogue "HOLLIS"
              "That's a fitting on a works order.")

 (sp/dialogue :pc
              "I know.")

 (sp/dialogue "HOLLIS"
              ["Then you know it isn't yours to take out and it"
               "isn't mine either, and I've been here since before"
               "your mother and it still isn't mine."])

 (sp/dialogue :pc
              "The water's going into the fill.")

 (sp/dialogue "HOLLIS"
              "I know where the water's going.")

 (sp/beat "He drinks.")

 (sp/dialogue "HOLLIS"
              "How long have you known?")

 (sp/dialogue :pc
              "April.")
 
 (sp/dialogue "HOLLIS"
              ["Right. I've known since February and I put a note"
               "in and the note went where the notes go, and I"
               "have been sat here since about the middle of March"
               "deciding whether I was going to say anything else"
               "about it to anybody, and I hadn't decided."])

 (sp/action "He gets up. It takes a moment.")

 (sp/dialogue "HOLLIS"
              "Now I have.")

 (sp/transition "Cut to:")

 (sp/slug "INT. PLANT SERVICES — 6:25 A.M.")

 (sp/action "He does two things and does them in this order.")

 (sp/action "First he takes the defect book off the shelf, which is a carbon"
            "book with a soft cover, and writes a defect notice, in full, in"
            "the hand he has used for forty-one years, with the fitting"
            "described and the works order quoted and the reason set out in"
            "four lines.")

 (sp/action "He tears out the top copy and puts it in the tray by the door,"
            "which is emptied on Thursdays, and which he has been putting"
            "notes in since before the tray by the door was that tray.")

 (sp/beat "He knows exactly what will happen to it.")

 (sp/action "Then he goes to the bench and gets a spanner and puts it in"
            "{pc}'s hand.")

 (sp/dialogue "HOLLIS"
              ["Nineteen. It'll be a nineteen and it'll be seized,"
               "and you'll want the long one and you'll want a bar"
               "on the end of the long one, and the bar's under"
               "the bench in a bit of pipe."])

 (sp/dialogue :pc
              "You're not coming?")

 (sp/dialogue "HOLLIS"
              "I'm signing you in. That's the part I've got.")

 (sp/transition "Cut to:")

;; ---

 (rogue/widget)

 (art/handout
  "PLANT SERVICES · DEFECT NOTICE · 14 April · 06:31 · H. HOLLIS"
  "FITTING: collection tray, Meridian basement, under stair pipe. Installed under works order 9-1140, February, described therein as permanent solution."
  "DEFECT: the tray discharges by short pipe into the slab and not to the drain in the corner of the same room, which is nine feet away and is the drain the room was built with."
  "The ground under that slab is fill. It is not soil and it has never been soil and there are men in this department who could have told anybody that."
  "This fitting has discharged into that fill since February. Prior to February the water was removed by hand from this building daily and had been for as long as there has been a department."
  "REQUESTED: removal of the fitting and reinstatement of collection by hand."
  "This is the fourth notice I have raised on this room. The others are in the book above this one."
  "Defect notices are not a component of review. Defect notices are recorded.")

 (sp/action "It goes in the tray by the door and the tray is emptied on"
            "Thursdays and nothing happens to it, and Hollis wrote it anyway,"
            "in full, at half past six in the morning, and that is the only"
            "reason there is a record of any of this anywhere on this campus.")

;; ---

 (sp/slug "INT. MERIDIAN HALL — BASEMENT — 6:50 A.M.")

 (sp/action "The tray. Four bolts. A short pipe out of the corner into the"
            "floor.")

 (sp/action "The concrete round the pipe is the colour concrete goes and gives"
            "under a thumb.")

 (sp/action "{pc} puts the light on, which is a thing he has never done down"
            "here, because there is a light and it works and he has used a"
            "torch since October out of a habit nobody gave him.")

 (sp/beat "The room is smaller with the light on.")

 (sp/action "He puts the spanner on the first bolt.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE BASEMENT — 7:20 A.M.")

 (sp/action "The first bolt takes eleven minutes and comes out with a noise.")

 (sp/action "The second takes four and comes out clean and he is briefly,"
            "stupidly pleased about it.")

 (sp/action "The third shears.")

 (sp/beat "The head comes off in the spanner and the shank stays in the floor"
          "and there is nothing to grip and nothing to do about it.")

 (sp/action "He sits on the wet floor with the spanner across his knees for a"
            "while.")

 (sp/action "Then he takes the fourth one out, which takes twenty minutes and"
            "a bar and both hands and one foot on the wall, and then he stands"
            "on the corner of the tray by the sheared bolt and rocks it until"
            "the slab lets it go.")

 (sp/beat "It comes up with about four inches of the short pipe still in the"
          "hole.")

 (sp/action "He is wet through to the shoulder and his hands are shaking and"
            "there is nothing dramatic about any of it.")

 (sp/action "Then he goes and gets a galvanised bucket off the stack by the"
            "door in Plant Services, and carries it down, and puts it under"
            "the pipe.")

 (sp/beat "It is the least interesting thing that happens in this book.")

 (sp/transition "Cut to:")

;; ---

 (sp/slug "INT. THE BASEMENT — 8:05 A.M.")

 (sp/action "A man comes down the stair in a clean high-vis with a clipboard"
            "and a phone in a case on his belt, and stops at the bottom of it,"
            "and looks at the tray on the floor and the bucket under the pipe"
            "and a first year sitting against the wall.")

 (sp/beat "It is the contractor. He has been coming since February.")

 (sp/dialogue "THE CONTRACTOR"
              "You Plant?")

 (sp/dialogue :pc
              "I'm with Plant.")

 (sp/dialogue "THE CONTRACTOR"
              ["Right. Because I've got that as a completion and"
               "I've had it as a completion since February and"
               "it's been sat on my list."])

 (sp/dialogue :pc
              "It's out.")

 (sp/dialogue "THE CONTRACTOR"
              ["I can see it's out. I'm asking whose it is now,"
               "because if it's out and it's mine I've got to come"
               "back, and if it's out and it's yours I've got to"
               "close it."])

 (sp/beat "That is the whole of the conversation and it is not a conversation"
          "about a building.")

 (sp/action "HOLLIS comes down the stair behind him. He has come down a stair"
            "to be here, which he has not done for this room in two years, and"
            "he is not quick about it.")

 (sp/dialogue "HOLLIS"
              "It's ours.")

 (sp/dialogue "THE CONTRACTOR"
              "You Hollis?")

 (sp/dialogue "HOLLIS"
              "I am.")

 (sp/dialogue "THE CONTRACTOR"
              ["I've been trying to hand this over since the"
               "middle of February. I've had four dates and"
               "nobody's ever here."])

 (sp/dialogue "HOLLIS"
              ["I'm here one day a week and it isn't the day"
               "you've been coming."])

 (sp/beat "Both of them take that in and neither of them says the obvious"
          "thing about it, because both of them have worked for somebody for a"
          "long time.")

 (sp/dialogue "THE CONTRACTOR"
              "So do you want it closed or do you want me back?")

 (sp/dialogue "HOLLIS"
              "Closed.")

 (sp/action "He turns the clipboard round and holds a pen out.")

 (sp/dialogue "THE CONTRACTOR"
              ["Condition, and then sign it, and put the date on"
               "it, and I'm off your list."])

 (sp/beat "Hollis reads the line. There is a box on it that says CONDITION ON"
          "HANDOVER and it is about an inch and a half long.")

 (sp/action "He writes two words in it.")

 (sp/beat "MADE GOOD.")

 (sp/action "He signs it and dates it and gives the pen back, and the"
            "contractor tears off a copy and hands it over and goes up the"
            "stair, and says cheers on the way, and means it.")

 (sp/transition "Cut to:")

 (sp/slug "INT. THE BASEMENT — 8:20 A.M.")

 (sp/action "The two of them and a bucket.")

 (sp/dialogue :pc
              "It'll be full by tonight.")

 (sp/dialogue "HOLLIS"
              "It'll be full by four.")

 (sp/dialogue :pc
              "And then it's every day.")

 (sp/dialogue "HOLLIS"
              ["It's every day. It was every day for forty-one"
               "years and it was every day before that, and the"
               "only thing that ever changed about it is that"
               "somebody stopped."])

 (sp/action "He looks at the tray on the floor with its four inches of pipe"
            "and its one sheared bolt.")

 (sp/dialogue "HOLLIS"
              ["Term's out in five weeks. I'm out at the end of"
               "it."])

 (sp/dialogue :pc
              "I know.")

 (sp/dialogue "HOLLIS"
              "Right.")

 (sp/beat "That is as close as either of them gets to it and neither of them"
          "goes any closer.")

 (sp/transition "Blackout.")

;; ---

 (art/handout
  "MERIDIAN HALL · BASEMENT · WORKS ORDER 9-1140 · CLOSED"
  "Fitting: collection tray, under stair pipe."
  "Installed: February. Described as permanent solution."
  "CONDITION ON HANDOVER: MADE GOOD."
  "Signed, H. HOLLIS, 14 April."
  "Handover accepted. Contractor released from this order."
  "This order is closed and will not be reopened without a further order.")

 (sp/action "Made good. Two words in a box an inch and a half long, in a hand"
            "that has been writing in boxes on this campus for forty-one"
            "years.")

 (sp/action "There is a book in the Collegium that says MADE GOOD, NORTH WALL"
            "eleven times in one hand over ninety years, and nobody in this"
            "story ever gets to find out whether the two facts are related,"
            "and neither does anybody in the next one.")

;; ---

 (art/discord
  {:server "FEDORA SOCIETY" :channel "#general"}
  [{:who "Victor" :role "PRESIDENT" :at "15 Apr, 11:40 PM" :text "Attendance three of five."}
   {:who "Miles" :at "15 Apr, 11:41 PM" :text "victor"}
   {:who "Victor" :role "PRESIDENT" :at "15 Apr, 11:42 PM" :text "Three of five. It is in the minutes and it is going in the letter."}
   {:who "Derek" :at "15 Apr, 11:58 PM" :text "is simon alright"}
   {:who "Miles" :at "16 Apr, 12:14 AM" :text "hes on the docket thursday. hes on it every week"}
   {:who "Derek" :at "16 Apr, 12:15 AM" :text "thats not what i asked"}
   {:who "Miles" :at "16 Apr, 12:31 AM" :text "i know"}]))