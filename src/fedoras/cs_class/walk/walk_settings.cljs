;; ---------------------------------------------------------------------
;; WALK_SETTINGS.CLJS. The quad, and what you are on it.
;;
;; Change a number and press Run. Arrows to turn and to walk, A and D to
;; sidestep.
;;
;; The picture is drawn by the graphics card now, so most of the numbers
;; that used to be about affording something are gone. There is no
;; longer a cost to a smaller brick or a larger campus, and the ones
;; that used to buy you frames now only decide what it looks like.
;;
;; :resolution is still the one to reach for if it runs badly. The
;; canvas is drawn at that size and stretched to fit, so halving it
;; quarters the work and costs you nothing but edges.
;; ---------------------------------------------------------------------

{:resolution [420 236]   ; the size it is drawn at, not the size it is
                         ; [640 360] is now affordable and much cleaner
                         ; [320 180] is the old look, cheap and blocky

 :fov        1.05        ; radians across. 1.05 is about sixty degrees.
 :eye-height 5.6         ; feet. you are a person of average height.
 :draw       520         ; feet. past this it is all haze.
 :bound      1000        ; feet to the edge of the world

 :walk-speed 2.6         ; feet a frame forward
 :back-speed 2.0
 :side-speed 2.0
 :turn-speed 0.045       ; radians a frame

 ;; THE SUN. An azimuth in radians and a height above the horizon, zero
 ;; at the skyline and one straight overhead. It lights the faces that
 ;; point at it and it does nothing else: there are no shadows on this
 ;; quad, which is a decision about the frame budget and not about the
 ;; weather.
 :sun {:azimuth 2.2 :height 0.30 :size 44 :ambient 0.62}

 ;; THE GROUND. Everything here is in feet, so a slab is a slab and not
 ;; a number of pixels, and it stays that size as you walk at it.
 ;; HOW FAR YOU CAN SEE. :draw up there is the distance, in feet, and
 ;; :clarity is how the haze is spread across it for anything standing
 ;; up: walls, trees, the fountain, the library. At 1 the fade is even.
 ;; Above 1 keeps what is near you clear and spends the fade out where
 ;; the campus ends. The ground does not take :clarity at all; it is the
 ;; horizon, and it fades evenly to :draw so that it meets the sky
 ;; softly. :horizon is how far into that fade it gives up and becomes
 ;; sky.
 :haze {:clarity 1.6 :horizon 0.82}

 :ground {:slab 4        ; paving slabs, in feet
          :joint 0.5     ; and the gaps between them
          :tuft 4        ; grass grain
          :patch 17      ; and the larger patches it varies in
          :grain 12      ; how far the shades wander
          :mow 9         ; how wide a mown stripe on Corliss Field is
          :bay 9         ; parking bays, in feet
          :tie 2.4       ; sleepers on the rail
          :lift 0.05}    ; how far each layer of ground sits above the
                         ; one under it. a crosswalk really is on a road
                         ; and a road really is on the lawn, and this is
                         ; the whisker that lets the card tell which is
                         ; which. raise it and the campus delaminates.

 ;; THE FILL. The one piece of ground with a shape. It is a landfill and
 ;; you can climb it. :peak is how high it gets in the middle, :step is
 ;; how many feet of it one triangle covers, and :roughness is how much
 ;; of the height is lumps rather than a smooth dome.
 :fill {:show true :peak 16 :roughness 0.9 :step 5}

 ;; SPRING BROOK. The same arithmetic as the Fill with the sign turned
 ;; round: a brook is a hill somebody dug rather than piled. :depth is
 ;; how far down the middle goes, :bank is how many feet it takes to
 ;; climb back to the lawn, and the grass is cut away above it rather
 ;; than laid over it, which is what it used to be.
 ;; :depth is how far down the middle goes and :bank is how many feet it
 ;; takes to climb back to the lawn. :waterline is where the surface
 ;; sits, which is below the grass because a brook is not a puddle on
 ;; it. :swell and :wavelength move that surface; :breath is how many
 ;; seconds you get before the campus puts you back where you started.
 :water {:show true
         :depth 5
         :bank 14
         :step 6
         :waterline -0.6
         :alpha 0.72
         :swell 0.35
         :wavelength 9
         :breath 12}

 ;; ROAD MARKINGS. All in feet.
 ;;
 ;; There are two kinds of road and each wears one set of markings. A
 ;; through road gets the pair of unbroken lines between :centre-inner
 ;; and :centre-outer, which says do not cross. A street between
 ;; buildings gets the single dashed line of somewhere you are about to
 ;; turn off: :dash is how often it repeats and :on is how much of that
 ;; is painted, so 18 and 10 is ten feet of paint and eight of nothing.
 ;; Which road is which is decided in campus.clj, not here.
 ;;
 ;; :width is half the thickness of a painted line and :bay-width is the
 ;; thickness of a parking bay stripe. A rail's steel runs between
 ;; :rail-inner and :rail-outer, and a crosswalk lays a :bar wide bar
 ;; every :zebra feet.
 :lines {:dash 18 :on 10 :width 0.4 :bay-width 0.7
         :centre-inner 0.3 :centre-outer 0.9
         :rail-inner 1.9 :rail-outer 2.5
         :zebra 5 :bar 2}

 ;; THE BRICK. Worked out per pixel from how far along a wall and how
 ;; far up it that pixel is, so the size of a brick is free and the
 ;; number of them is free. :streak is how many bricks share a rough
 ;; batch colour. :detail is how far away a brick stops being drawn as a
 ;; brick, in feet, because a brick smaller than a pixel is a moire
 ;; pattern and not a wall.
 :brick {:width 2.2      ; feet along the wall
         :height 0.9     ; feet, one course
         :mortar 0.14    ; feet, the joint
         :streak 9
         :detail 150}

 ;; DOORS AND WINDOWS. One face of each building has a door. Which face
 ;; is worked out from where the building sits, unless the plan says
 ;; otherwise. Windows are placed from the building's own name, so the
 ;; same building has the same windows every time.
 ;;
 ;; There is no filename here. The pictures are inlined into the page by
 ;; the build, the same as every other page-side file in this book,
 ;; because a book opened from disc cannot fetch its own assets and an
 ;; image that was fetched from disc cannot be put on a graphics card.
 ;; Sizes are in feet and are yours to change.
 ;; :sill is where the bottom edge of the door picture sits, in feet off
 ;; the lawn, and it is below it on purpose: the picture has transparent
 ;; space along its own bottom edge and nothing in the program can
 ;; measure how much. Sinking it and putting a stone step in front of it
 ;; is the answer a building would have given anyway. If the door still
 ;; hovers, lower :sill further and raise :height by as much.
 :doors   {:show true :width 7 :height 12 :sill -0.8
           :step {:show true :rise 0.6 :tread 3.0 :spread 1.5}}
 :windows {:show true :width 3.4 :height 3.4}

 :buildings {:height 30}

 ;; OAKES LIBRARY. The only round thing on the campus with a door in it.
 ;; :wall is how tall the glass stands and :dome how much higher the lid
 ;; goes above it. :facets is how many flat pieces the circle is made of
 ;; and :rings how many bands the dome has; both are cheap and both
 ;; show. :glass is how solid the wall is, from 0 to 1.
 ;;
 ;; There are four blocks inside and they are the reason the glass is
 ;; worth having. A building you can see clean through is a fish tank.
 ;; Walk round it and what you can see of the inside changes, which is
 ;; the only thing a glass wall is for.
 ;;
 ;; :door is how wide each doorway is, and it is a gap in the wall
 ;; rather than a hole cut in it afterwards. Two of them, east and west,
 ;; which is decided in campus.clj and not here.
 :rotunda {:show true
           :wall 15
           :dome 12
           :facets 48
           :rings 7
           :glass 0.26
           :door 9}

 ;; THE FOUNTAIN. :animate keeps the water moving while you stand still.
 ;; The mist is a few hundred points whose whole path is a function of
 ;; the clock, so it costs the same standing as it does walking.
 :fountain {:show true
            :animate true
            :ripples 3
            :x 455 :y 330
            :radius 16
            :rim 1.4
            :water 1.6
            :pedestal {:radius 2.4
                       :height 4.5}
            ;; :jets is how much of the spray goes straight up instead
            ;; of out. There is no geometry over the nozzle: a stream
            ;; that is barely thrown outward is thrown upward instead,
            ;; so the jet is water going up and coming back down rather
            ;; than a cone standing there pretending to.
            :spray {:width 1.0
                    :height 1.0
                    :density 1.0
                    :jets 1.0}}

 ;; TREES AND HEDGES. Both are flat panels that turn to face you, grown
 ;; out of overlapping blobs. :spread is how much wider than its own
 ;; canopy the panel is; :trunk is half a trunk at the root, in feet;
 ;; :fork is how far up the trunk the leaves start. :taper is how much
 ;; narrower the trunk is by the fork than at the root, and :limbs is
 ;; how far out the two branches reach. Zero for both gives back the
 ;; lollipop this used to be.
 :trees  {:show true :spread 1.15 :trunk 0.75 :fork 0.34
          :taper 0.45 :limbs 0.5}
 :bushes {:show true :spread 1.15}

 ;; THE EMERGENCY POSTS. Twenty of them, and until recently the only
 ;; thing on this campus that did nothing at all. Walk within :reach feet
 ;; of one and it answers, and its lamp changes colour. There is no
 ;; puzzle, no order to do them in, and no reward for finishing beyond
 ;; finding out that all twenty of them work.
 :posts {:show true :height 8 :radius 0.4 :lamp 1.2 :reach 9}

 ;; WHATEVER IT IS. It walks at :speed feet a frame, which is very
 ;; slightly under yours, and it never stops. :wake is how far off it
 ;; starts being drawn at all, :touch is how close it has to get, and
 ;; :start is where it goes back to afterwards. Nothing happens when it
 ;; reaches you except that you are put back on the quad. Set :show to
 ;; false and the campus is a campus again.
 ;; It is off. Turn it on when you have had a look round.
 ;;
 ;; :speed is feet a frame, against your own :walk-speed of 2.6 up
 ;; there. At 1.7 you can open a gap and hold it if you keep moving; put
 ;; it over 2.6 and there is no point running at all.
 ;;
 ;; :quicken is what ties it to the posts: every post you answer makes it
 ;; that much faster, so the only thing on this campus that gives you a
 ;; reason to walk about is also the thing that makes walking about
 ;; worse. Set it to 0 and answering posts costs you nothing.
 ;;
 ;; THE NOISE. Also off. With no :file it is a square wave at :pitch
 ;; hertz, which needs nothing off a disc and is already unpleasant; the
 ;; pitch climbs as it closes. Name a .wav or .mp3 in :file and that
 ;; plays instead, on a loop. Either way it is silent at :wake, loudest
 ;; when it reaches you, and :level is the loudest it is ever allowed to
 ;; be, from 0 to 1.
 ;;
 ;; A browser will not make a sound until you have pressed a key or
 ;; clicked the picture. That is not something this can work around and
 ;; it is not worth wanting to.
 :wraith {:show false
          :speed 1.7
          :quicken 0.35
          :width 2.4
          :height 7.5
          :wake 260
          :touch 6
          :start [900 700]
          :sound {:on false
                  :level 0.35
                  :pitch 70
                  :file nil}}

 ;; CLOUDS. Each one is :lobes soft points overlapping, sitting on a
 ;; dome around you, drifting. :cover is what fraction of the sky has
 ;; any; :rows is how many bands of them there are between overhead and
 ;; the skyline.
 ;; :ragged cuts each lobe's edge into wedges and pushes them in and
 ;; out, because a soft round dot reads as cotton wool. :flat squashes
 ;; the underside, which is what gives a cumulus its flat bottom. Zero
 ;; for either gives back the plain dot.
 :clouds {:show true
          :cover 0.42
          :drift 0.00004
          :rows 9
          :size 0.19
          :lobes 10
          :ragged 0.22
          :flat 0.6}}