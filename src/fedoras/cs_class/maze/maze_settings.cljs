;; ---------------------------------------------------------------------
;; MAZE_SETTINGS.CLJS
;;
;; Change a number and press Run. 

;; Every Run begins again on the first floor.
;; The building may change its rules while you are standing in
;; it - that is its privilege, which it doesn't share.
;;
;; THE REQUIREMENTS ARE DATA. Add one to `:house-rules` and it goes on
;; the wall. Add one to `:amendments` and Zell may write it. There are
;; four verbs, and the floor can meet the first three by moving
;; furniture:
;;
;;   {:keep :apart    :a :box :b :door}   box shall not adjoin door
;;   {:keep :together :a :box :b :door}   box shall adjoin door
;;   {:keep :clear    :a :way-out}        nothing shall adjoin way-out
;;   {:keep :present  :a :warrant}        warrant shall be held on this floor
;;
;; Anything on the floor can be named: :box :cabinet :copier
;; :disturbance :pole :bear-trap :shield :key :door :doorway :warrant
;; :way-out :portal-a :portal-b :portal-in :portal-out. The door, the
;; key, the warrant, the way out and the portals are never moved, so a
;; requirement about one of them moves the other thing, and a
;; requirement about two of them is simply true or simply not.
;;
;; The building itself never puts anything that persists (furniture or a
;; portal) right beside a door or the way out, so that each can always
;; be reached. A requirement that asks for one there is met only if you
;; move it there yourself.
;; ---------------------------------------------------------------------

{:rows 10             ; five to twenty, and the same for columns
 :cols 10
 :density 0.78        ; how much of the grid is rooms. under about 0.6
                      ; it falls into islands and only the largest remains
 :seed nil            ; nil is a new building every floor. a number is
                      ; the same buildings in the same order every time,
                      ; and the seed under the floor is the one to copy

 :difficulty :clerk   ; :visitor :clerk :auditor :inspector

 ;; Each row represents a collection of map constraints.
 ;;
 ;;   rules    requirements on the wall on the first floor, besides the
 ;;            warrant, which is always there
 ;;   locks    locked doors across the floor, each with a key somewhere
 ;;            on your side of it. three at most, fewer in a building too
 ;;            small to take them
 ;;   entities how many come for you on the first floor
 ;;   pace     entities move once every this many turns. 1 is your speed
 ;;   release  once you carry the warrant, a room goes every this many
 ;;            turns. nil, and nothing goes
 ;;   writes   Zell adds a requirement every this many turns. nil, never
 ;;   hp       what you start with, and the most you can hold
 ;;   steps    how many moves a floor allows
 ;;   drops    the most of each powerup that can appear on one floor
 ;;   entity-powerups
 ;;            whether entities pick up powerups and use them on you. one
 ;;            goes for a powerup only when it is nearer to it than you
 ;;            are, and uses it only when it would reach you
 :difficulties
 {:visitor   {:rules 1 :locks 0 :entities 1 :pace 3 :release 5 :writes 14
              :hp 4 :steps 120 :drops 2 :entity-powerups false}
  :clerk     {:rules 1 :locks 1 :entities 2 :pace 2 :release 4 :writes 10
              :hp 3 :steps 90  :drops 1 :entity-powerups false}
  :auditor   {:rules 3 :locks 2 :entities 3 :pace 2 :release 3 :writes 8
              :hp 4 :steps 100 :drops 1 :entity-powerups true}
  :inspector {:rules 4 :locks 2 :entities 4 :pace 2 :release 2 :writes 6
              :hp 2 :steps 90  :drops 1 :entity-powerups true}}

 ;; THE WAY DOWN. What each floor after the first does to the row above.
 ;; Each is [every limit]: every that many floors, one step worse, and
 ;; never worse than the limit. A row that starts past a limit stays
 ;; where it started.
 :descent {:rules    [1 4]     ; one more requirement a floor, up to four
           :locks    [3 3]     ; one more lock every third floor, up to three
           :entities [2 4]     ; one more entity every second floor
           :release  [3 2]     ; a room goes a turn sooner every third floor
           :writes   [1 6]}    ; Zell writes a turn sooner every floor

 :writes-from 2       ; the first floor he writes on. the first floor is
                      ; quiet, so the first amendment anybody sees is an
                      ; event rather than weather

 ;; Powerups: Each turn there is a chance one appears on an empty tile,
 ;; indicating the number of turns it has left written over it. Walk into
 ;; it to pick it up. Using one takes no turn, so they can be used together.
 ;;
 ;;   vanquish  1  ends an entity within reach
 ;;   teleport  2  moves you a few rooms, never past a locked door
 ;;   stun      3  stops everything near you for a few turns
 ;;
 ;; Where the difficulty row allows it, entities pick them up too. An
 ;; entity's vanquish costs you a hit from as far off as yours reaches,
 ;; its stun holds you for half as long as yours holds them, and its
 ;; teleport brings it nearer.
 :powerups {:chance 0.08          ; each turn, the chance one appears
            :lasts 12             ; turns before it fades
            :vanquish-range 5     ; how far away the one you end can be
            :teleport-range 4     ; how far you can go
            :stun-radius 2        ; how close entities have to be
            :stun-turns 4}        ; and how long they stay stopped

 ;; Score: Completing a floor is worth `:floor` before you move. Turns and hits
 ;; take from it, and ending or stunning entities adds to it. Getting out
 ;; adds up to `:quick` more: all of it for the shortest way round, none
 ;; of it at `:slow` times that. Dying loses the floor you are on and
 ;; keeps the ones already banked.
 :score {:floor 1000
         :step 10
         :hit 50
         :vanquish 150
         :stun 40
         :quick 500
         :slow 2}

 ;; Floor tiles: Sand, ice and fire, each gathered in one large mass
 ;; toward a corner away from where you start, with a little of it
 ;; scattered off the edge. Floor one is usually bare and never has more
 ;; than one kind. After that, one more kind becomes possible every
 ;; `:new-kind-every` floors, though the die now and then gives a floor
 ;; one fewer. Every kind brings its boots. The boots for a harsher kind
 ;; lie in a pocket of a gentler one when the floor has one, fire boots
 ;; in the ice and ice boots in the sand; otherwise they lie near the
 ;; edge of their own ground, on your side. No floor is laid that cannot
 ;; be finished without walking through fire unshod.
 ;;
 ;;   sand   every step into it takes two turns. entities cross it at
 ;;          their usual pace
 ;;   ice    you slide the way you were going until the ice ends or
 ;;          something stops you. entities slide too, and so does
 ;;          anything you push onto it
 ;;   fire   every step into it singes you, and enough singes are a hit.
 ;;          it burns entities the same way, and they go round it when
 ;;          they can
 :terrain {:first-floor-bare 0.6 ; the chance floor one has no ground at all
           :new-kind-every 3     ; floors between one more kind of ground and the next
           :one-fewer 0.35       ; the chance a later floor has one kind fewer than it could
           :share 0.28           ; about how much of the floor one kind covers alone
           :burns-per-hit 3}     ; singes that add up to a hit

 ;; SPECIAL ENTITIES. Every floor that `:every` divides has one, the
 ;; kinds taking turns.
 ;;
 ;;   fast     moves two rooms at a time, one step after the other, so
 ;;            it still cannot pass a wall
 ;;   strong   takes two blows to end: a hot shield and a vanquish, or
 ;;            two of either
 ;;   thrower  throws at you from within `:throw-range` rooms, walls or
 ;;            no walls, then has to move before it can throw again
 :specials {:every 3 :kinds [:fast :strong :thrower] :throw-range 2}

 ;; Furniture: Counts for a building ten rooms by ten, scaled to the size
 ;; above, then multiplied by `:clutter`. It goes anywhere except right
 ;; beside a door or the way out.
 :clutter 1.0         ; 0 is an empty building. 2 is a storeroom
 :furniture {:box 2 :cabinet 1 :copier 1 :disturbance 1 :pole 1}

 ;; These are counted exactly and spawn a short distance from where
 ;; you start. A portal may cross a lock, and a one-way portal that
 ;; does can leave you where nothing opens the way back. No portal is
 ;; ever right beside a door or the way out, and no ice right beside a
 ;; portal.
 :traps 2
 :shields 1
 :portals true        ; a pair: step into either end, come out the other
 :one-way true        ; a portal that goes somewhere and does not return

 :shield-turns 3      ; for this long after you pick it up, the shield
                      ; ends anything it meets. after that it takes one
                      ; hit and is gone
 :trap-holds 2        ; turns a trap holds an entity
 :trap-holds-you 1    ; turns a trap holds you

 ;; Requirements: The warrant is on it on every floor.
 :warrant-rule {:keep :present :a :warrant}

 ;; In the order the building introduces them. The first is the one you
 ;; can see happening, the second teaches you that a barricade does not
 ;; last, the third that the audit can be aimed, and the fourth is
 ;; furniture.
 :house-rules
 [{:keep :clear    :a :way-out}
  {:keep :apart    :a :box       :b :door}
  {:keep :together :a :bear-trap :b :cabinet}
  {:keep :apart    :a :shield    :b :disturbance}]

 ;; What Zell may write, one at a time and never the same one twice.
 :amendments
 [{:keep :apart    :a :box     :b :way-out}
  {:keep :together :a :cabinet :b :copier}
  {:keep :clear    :a :warrant}
  {:keep :apart    :a :shield  :b :way-out}
  {:keep :together :a :box     :b :disturbance}]}