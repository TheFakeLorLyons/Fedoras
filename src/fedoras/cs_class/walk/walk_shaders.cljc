(ns fedoras.cs-class.walk.walk-shaders
  "The programs the graphics card runs, written as Clojure.

  Nothing in this file is a string. `fedoras.glsl` reads the forms
  below, works out the type of every expression in them, and prints
  GLSL. That happens once, here, on a JVM, when the book is built: a
  shader that does not typecheck fails the build in Clojure rather than
  arriving at somebody's driver as `ERROR: 0:39` with no indication of
  which of fifteen shaders was being read at the time.

  The browser never sees any of this. It is handed `programs` as
  finished GLSL through the widget's mount map, the same way the door
  and the window are handed over as pictures, and for the same reason:
  the page should carry the result and not the machinery that made it.

  `layouts` comes out of the same attribute vectors the shaders declare,
  so the buffer a mesh is packed into and the buffer the card reads are
  one piece of data and cannot drift apart.

  Every colour still comes out of walk.css. Nothing here knows what
  brick looks like; it knows that brick is whatever `--walk-brick-a`
  says it is this week."
  (:require [fedoras.glsl :as glsl]))

;; ---------------------------------------------------------------------
;; Shared by everything
;; ---------------------------------------------------------------------

(def ^:private precision-preamble
  "Fragment shaders have to be told how much precision they want. Ask
  for the good kind, take the other kind on hardware that has not got
  it, and never find out which happened."
  (str "#ifdef GL_FRAGMENT_PRECISION_HIGH\n"
       "precision highp float;\n"
       "#else\n"
       "precision mediump float;\n"
       "#endif"))

(def ^:private fog-function
  "How much of the haze a pixel has taken on.

  u_reach is (how far you can see, a number the program picks for
  itself, how quickly the haze takes hold). The third one is the reason
  raising the first used to feel like it did nothing: the fade was
  linear in distance over reach, so doubling the reach halved the fog
  everywhere at once and the near ground went pale along with the far.
  An exponent above one keeps what is near you clear and spends the
  whole fade out where the campus actually disappears."
  '[{:name fogAt :out :float :in [here :vec3 u_eye_at :vec3 reach :vec3]
     :body [(return (pow (clamp (/ (distance here u_eye_at) (:x reach))
                                0.0 1.0)
                         (:z reach)))]}])

(def ^:private common-functions
  "One repeatable number out of two, and the same rounded down to a
  whole number of steps. This is what makes a patch of grass the same
  shade every time you look at it, and it costs a few multiplies
  instead of a texture."
  '[{:name hash21 :out :float :in [seed :vec2]
     :body [(let [scattered (fract (* (vec3 (:xyx seed)) 0.1031))
                  folded (+ scattered (dot scattered (+ (:yzx scattered) 33.33)))]
              (return (fract (* (+ (:x folded) (:y folded)) (:z folded)))))]}

    {:name pick :out :float :in [cell :vec2 steps :float]
     :body [(return (floor (* (hash21 (floor cell)) steps)))]}])

(defn- vertex
  [shader]
  (glsl/compile-shader
   (update shader :functions
           (fn [own] (into (into (vec common-functions) fog-function) own)))))

(defn- fragment
  [shader]
  (glsl/compile-shader
   (-> shader
       (assoc :preamble precision-preamble)
       (update :functions
               (fn [own]
                 (into (into (vec common-functions) fog-function) own))))))

;; ---------------------------------------------------------------------
;; The sky
;;
;; A rectangle covering the screen, with no depth test, so it can never
;; hide anything. The vertex shader hands each corner a world-space ray
;; and the fragment shader asks that ray what colour it is looking at.
;; ---------------------------------------------------------------------

(def sky-attributes ["a_corner" :vec2])
(def ^:private sky-carried ["v_ray" :vec3])

(def sky-vertex
  (vertex
   {:attributes sky-attributes
    :uniforms ["u_right" :vec3 "u_up" :vec3 "u_forward" :vec3 "u_spread" :vec2]
    :varyings sky-carried
    :main
    '[(set! v_ray (+ u_forward
                     (* u_right (* (:x a_corner) (:x u_spread)))
                     (* u_up (* (:y a_corner) (:y u_spread)))))
      (set! gl_Position (vec4 a_corner 0.0 1.0))]}))

(def sky-fragment
  (fragment
   {:varyings sky-carried
    :uniforms ["u_sky" :vec3 "u_haze" :vec3 "u_sun" :vec3
               "u_sunward" :vec3 "u_size" :float]
    :main
    '[(let [ray (normalize v_ray)
            overhead (smoothstep 0.0 0.55 (clamp (:z ray) 0.0 1.0))
            ground-up (mix u_haze u_sky overhead)
            from-sun (acos (clamp (dot ray u_sunward) -1.0 1.0))
            disc from-sun]
        ;; The disc and its glare were always there. What was missing is
        ;; the wide warm part of the sky around them, which is most of
        ;; what makes a sky look lit rather than painted.
        (let [core (* u_size 0.0022)
              glare (- 1.0 (smoothstep core (* core 3.4) disc))
              glow (* 0.35 (pow (max (dot ray u_sunward) 0.0) 6.0))
              lit-sky (mix ground-up u_sun glow)]
          (set! gl_FragColor (vec4 (mix lit-sky u_sun glare) 1.0))))]}))

;; ---------------------------------------------------------------------
;; Points: cloud lobes and fountain mist
;;
;; Both are point sprites and both fade to nothing at the edge of the
;; sprite, so they share a fragment shader. What differs is entirely in
;; where the point goes, which is the vertex shader's business.
;; ---------------------------------------------------------------------

(def ^:private point-carried ["v_alpha" :float "v_seed" :float])

(def point-fragment
  (fragment
   {:varyings point-carried
    :uniforms ["u_tint" :vec3 "u_shape" :vec2]
    :main
    ;; u_shape is (how ragged, how flat-bottomed). A round sprite with a
    ;; smooth falloff reads as a cotton ball rather than a cloud, so the
    ;; edge is cut into nine wedges and each one pushed in or out by a
    ;; number that follows from the wedge and from which lobe this is.
    ;; The lower half is squashed toward the middle, which is what gives
    ;; a cumulus its flat bottom. Mist passes zero for both and gets the
    ;; plain soft dot it wants.
    '[(let [offset (- gl_PointCoord (vec2 0.5))
            bearing (atan (:y offset) (:x offset))
            turns (* (/ (+ bearing 3.1416) 6.2832) 9.0)
            wedge (floor turns)
            across (smoothstep 0.0 1.0 (fract turns))
            here (hash21 (vec2 (mod wedge 9.0) v_seed))
            next (hash21 (vec2 (mod (+ wedge 1.0) 9.0) v_seed))
            ragged (* (:x u_shape) (- (mix here next across) 0.5) 2.0)
            squashed (+ 1.0 (* (:y u_shape) (max 0.0 (:y offset)) 2.0))
            from-middle (/ (* (length offset) 2.0 squashed) (+ 1.0 ragged))
            soft (- 1.0 (smoothstep 0.3 1.0 from-middle))]
        (when (< (* soft v_alpha) 0.002) (discard))
        (set! gl_FragColor (vec4 u_tint (* v_alpha soft))))]}))

(def cloud-attributes ["a_lobe" :vec4])

(def cloud-vertex
  (vertex
   {:attributes cloud-attributes
    :uniforms ["u_camera" :mat4 "u_eye" :vec3 "u_now" :float
               "u_drift" :float "u_focal" :float "u_dome" :float
               "u_biggest" :float]
    :varyings point-carried
    :main
    '[(let [turn (+ (:x a_lobe) (* u_now u_drift))
            lift (:y a_lobe)
            bearing (normalize (vec3 (* (cos turn) (- 1.0 lift))
                                     (* (sin turn) (- 1.0 lift))
                                     lift))
            clip (* u_camera (vec4 (+ u_eye (* bearing u_dome)) 1.0))]
        (set! gl_Position clip)
        (set! gl_PointSize
              (clamp (/ (* (:z a_lobe) u_focal u_dome) (max (:w clip) 1.0))
                     2.0 u_biggest))
        (set! v_alpha (:w a_lobe))
        (set! v_seed (* (:x a_lobe) 13.0)))]}))

(def mist-attributes ["a_drop" :vec4])

(def mist-vertex
  (vertex
   {:attributes mist-attributes
    :uniforms ["u_camera" :mat4 "u_middle" :vec3 "u_bell" :vec4
               "u_now" :float "u_focal" :float "u_biggest" :float]
    :varyings point-carried
    :main
    '[(let [life (:z u_bell)
            age (fract (/ (+ u_now (* (:z a_drop) life)) life))
            scatter (* 0.6 (pow age 1.6))
            stray (vec3 (* (- (hash21 (vec2 (:w a_drop) 1.0)) 0.5) 2.0 scatter)
                        (* (- (hash21 (vec2 (:w a_drop) 2.0)) 0.5) 2.0 scatter)
                        (* (- (hash21 (vec2 (:w a_drop) 3.0)) 0.5) scatter))
            outward (* (:x u_bell) (:y a_drop) age)
            ;; A stream that is barely thrown outward is thrown upward
            ;; instead. That is the whole of the fountain's vertical jet:
            ;; there is no geometry standing over the nozzle, only water
            ;; going up and coming back down, which is what a jet is.
            climb (* (:y u_bell) (mix 2.6 1.0 (clamp (:y a_drop) 0.0 1.0)))
            arc (- (* 2.0 age) (* 2.6 age age))
            world (+ (vec3 (+ (:x u_middle) (* outward (cos (:x a_drop))))
                           (+ (:y u_middle) (* outward (sin (:x a_drop))))
                           (+ (:z u_middle) (* climb arc)))
                     stray)
            from-middle (length (- (:xy world) (:xy u_middle)))
            edge (clamp (/ (- (:w u_bell) from-middle) (* (:w u_bell) 0.18))
                        0.0 1.0)
            fading (if (< age 0.8)
                     (+ 0.35 (* 0.44 age))
                     (* 0.7 (- 1.0 age) 5.0))
            clip (* u_camera (vec4 world 1.0))]
        (set! v_alpha (* fading edge))
        (set! v_seed (:w a_drop))
        (set! gl_Position clip)
        (set! gl_PointSize
              (clamp (/ (* (+ 0.10 (* 0.10 age)) u_focal) (max (:w clip) 0.5))
                     1.0 u_biggest)))]}))

;; ---------------------------------------------------------------------
;; The ground
;;
;; a_patch is (material, how far along this rectangle, how far off its
;; middle). The offset is signed rather than absolute, because a signed
;; offset interpolates across a triangle and an absolute one is a fold.
;; The shader takes the absolute value at the far end, where it costs
;; nothing.
;;
;; Colour comes out of a strip of texture two pixels tall: near shades
;; on the top row, far shades on the bottom, and the fog decides how far
;; between them a pixel sits.
;; ---------------------------------------------------------------------

(def ground-attributes ["a_position" :vec3 "a_patch" :vec4])
(def ^:private ground-carried ["v_patch" :vec4 "v_world" :vec3])

(def ground-vertex
  (vertex
   {:attributes ground-attributes
    :uniforms ["u_camera" :mat4]
    :varyings ground-carried
    :main
    '[(set! v_patch a_patch)
      (set! v_world a_position)
      (set! gl_Position (* u_camera (vec4 a_position 1.0)))]}))

(def ground-fragment
  (fragment
   {:varyings ground-carried
    :uniforms ["u_palette" :picture "u_grain" :vec4 "u_marks" :vec4
               "u_lines" :vec4 "u_edges" :vec4 "u_zebra" :vec2
               "u_reach" :vec3 "u_haze" :vec3 "u_eye" :vec3]
    :constants '[PALETTE_WIDTH :float 16.0
                 ROAD_SLOT :float 3.0
                 CENTRE_SLOT :float 5.0
                 PAINT_SLOT :float 9.0]
    :functions
    '[{:name shadeAt :out :vec3 :in [slot :float fog :float]
       :body [(let [along (/ (+ slot 0.5) PALETTE_WIDTH)]
                (return (mix (:rgb (texture2D u_palette (vec2 along 0.25)))
                             (:rgb (texture2D u_palette (vec2 along 0.75)))
                             fog)))]}

      {:name levelsOf :out :float :in [material :int]
       :body [(when (== material (int 0)) (return 5.0))
              (when (== material (int 1)) (return 4.0))
              (when (== material (int 2)) (return 4.0))
              (when (== material (int 4)) (return 3.0))
              (when (== material (int 6)) (return 3.0))
              (when (== material (int 7)) (return 4.0))
              (when (== material (int 8)) (return 3.0))
              (return 2.0)]}

      {:name levelAt :out :float
       :in [material :int world :vec2 run :float across :float style :float]
       :body
       [(when (== material (int 0))
          (let [tuft (pick (/ world (:z u_grain)) 3.0)
                blotch (pick (+ (/ world (:w u_grain)) 31.0) 3.0)]
            (return (min 4.0 (+ tuft blotch)))))

        (when (== material (int 1))
          (return (+ (* 2.0 (mod (floor (/ (:y world) (:x u_marks))) 2.0))
                     (pick (+ (/ world (:z u_grain)) 61.0) 2.0))))

        (when (== material (int 2))
          (let [cell (mod world (:x u_grain))]
            (when (or (< (:x cell) (:y u_grain)) (< (:y cell) (:y u_grain)))
              (return 0.0))
            (return (+ 1.0 (pick (+ (/ world (:x u_grain)) 97.0) 3.0)))))

        ;; u_lines is (how often a dash repeats, how much of that is
        ;; painted, half the width of a painted line, how far off the
        ;; middle the lane divider runs). u_edges is (the inner and
        ;; outer edge of the pair of lines down the middle, then the
        ;; inner and outer edge of a rail). u_zebra is (how often a
        ;; crosswalk bar repeats, how wide a bar is). All of it is in
        ;; feet and all of it is in the settings file, because a road
        ;; with two markings arguing over one lane is a thing you want
        ;; to move without recompiling anything.
        ;; A road carries two markings and they do not share a lane.
        ;; The pair of unbroken lines down the middle says do not cross,
        ;; and the dashed line out at :lane says this is where one lane
        ;; ends and the next begins. They used to be the same stripe
        ;; twice because the shuttle route was painted on top of the
        ;; road it ran along, and no road anywhere is marked like that.
        ;; Two kinds of road, and each wears one set of markings. A
        ;; through road gets the pair of unbroken lines down the middle
        ;; that says do not cross. A street between buildings gets the
        ;; single dashed line of somewhere you are about to turn off.
        ;; Wearing both at once, which is what every road on this campus
        ;; was doing, says nothing at all.
        (when (== material (int 3))
          (when (> style 0.5)
            (return (if (and (< across (:z u_lines))
                             (< (mod run (:x u_lines)) (:y u_lines)))
                      1.0 0.0)))
          (return (if (and (>= across (:x u_edges)) (< across (:y u_edges)))
                    2.0 0.0)))

        (when (== material (int 4))
          (when (and (< across (:z u_lines))
                     (< (mod run (:x u_lines)) (:y u_lines)))
            (return 2.0))
          (when (and (> across 2.0) (< (mod run (:y u_marks)) (:w u_lines)))
            (return 1.0))
          (return 0.0))

        (when (== material (int 6))
          (when (and (> across (:z u_edges)) (< across (:w u_edges)))
            (return 2.0))
          (return (if (< (mod run (:z u_marks)) 1.2) 1.0 0.0)))

        (when (== material (int 7))
          (return (pick (/ world 3.0) 4.0)))

        (when (== material (int 8))
          (return (pick (+ (/ world (vec2 5.0 12.0)) 13.0) 3.0)))

        (return (if (< (mod run (:x u_zebra)) (:y u_zebra)) 1.0 0.0))]}]
    :main
    '[(let [material (int (+ (:x v_patch) 0.5))
            across (abs (:z v_patch))
            ;; The ground takes its distance straight, without the
            ;; clarity curve every other program uses. The ground is the
            ;; horizon, and bending its fade squeezed the band where it
            ;; melts into the sky to half its width, which read as a hard
            ;; line under a sky that had not changed at all.
            far (clamp (/ (distance v_world u_eye) (:x u_reach)) 0.0 1.0)
            level (levelAt material (:xy v_world) (:y v_patch) across
                           (:w v_patch))
            slot (:x v_patch)
            painted false]
        (when (and (== material (int 3)) (> level 1.5))
          (set! slot CENTRE_SLOT)
          (set! painted true))
        (when (and (== material (int 3)) (> level 0.5) (< level 1.5))
          (set! slot PAINT_SLOT)
          (set! painted true))
        (when (and (== material (int 9)) (< level 0.5))
          (set! slot ROAD_SLOT)
          (set! painted true))
        (let [shade (shadeAt slot far)]
          (when (not painted)
            (set! shade (+ shade (* (/ (:w u_marks) 255.0)
                                    (- level (floor (* (levelsOf material) 0.5)))))))
          (set! shade (mix shade u_haze (smoothstep (:y u_reach) 1.0 far)))
          (set! gl_FragColor (vec4 shade 1.0))))]}))

;; ---------------------------------------------------------------------
;; Walls
;;
;; a_wall is (feet along this wall from the building's own corner, feet
;; up). Every brick is arithmetic on that pair, so the size of a brick
;; is free, the number of them is free, and the old bug where two wall
;; segments disagreed about where the courses start is inexpressible.
;;
;; The detail fades out with distance. A brick smaller than a pixel is a
;; moire pattern rather than a wall.
;; ---------------------------------------------------------------------

(def wall-attributes
  ["a_position" :vec3 "a_wall" :vec2 "a_normal" :vec2 "a_seed" :float])

(def ^:private wall-carried
  ["v_wall" :vec2 "v_normal" :vec2 "v_world" :vec3 "v_seed" :float])

(def wall-vertex
  (vertex
   {:attributes wall-attributes
    :uniforms ["u_camera" :mat4]
    :varyings wall-carried
    :main
    '[(set! v_wall a_wall)
      (set! v_normal a_normal)
      (set! v_world a_position)
      (set! v_seed a_seed)
      (set! gl_Position (* u_camera (vec4 a_position 1.0)))]}))

(def wall-fragment
  (fragment
   {:varyings wall-carried
    :uniforms ["u_brickA" :vec3 "u_brickB" :vec3 "u_brickC" :vec3
               "u_mortar" :vec3 "u_face" :vec3 "u_haze" :vec3 "u_eye" :vec3
               "u_brick" :vec4 "u_sunward" :vec3 "u_ambient" :float
               "u_reach" :vec3]
    :main
    '[(let [along-period (+ (:x u_brick) (:z u_brick))
            up-period (+ (:y u_brick) (:z u_brick))
            course (floor (/ (:y v_wall) up-period))
            stagger (* (mod course 2.0) along-period 0.5)
            column (floor (/ (- (:x v_wall) stagger) along-period))
            inside (vec2 (mod (- (:x v_wall) stagger) along-period)
                         (mod (:y v_wall) up-period))
            half-joint (* (:z u_brick) 0.5)
            in-mortar (or (< (:x inside) half-joint)
                          (> (:x inside) (- along-period half-joint))
                          (< (:y inside) half-joint)
                          (> (:y inside) (- up-period half-joint)))
            ;; v_seed is the face's own number. Without it a_wall starts
            ;; at zero on every wall on the campus and every wall is laid
            ;; brick for brick the same as the one round the corner.
            this-brick (hash21 (+ (vec2 column course) 7.0 v_seed))
            this-batch (hash21 (+ (floor (/ (vec2 column course) (:w u_brick)))
                                  23.0 v_seed))
            blend (+ (* 0.6 this-batch) (* 0.4 this-brick))
            baked (if (< blend 0.34)
                    u_brickA
                    (if (< blend 0.67) u_brickB u_brickC))
            depth (distance v_world u_eye)
            detail (- 1.0 (smoothstep (* (:y u_reach) 0.55) (:y u_reach) depth))
            shade (mix u_face (if in-mortar u_mortar baked) detail)
            lit (+ u_ambient
                   (* (- 1.0 u_ambient)
                      (max 0.0 (dot (normalize v_normal)
                                    (normalize (:xy u_sunward))))))
            fog (pow (clamp (/ depth (:x u_reach)) 0.0 1.0) (:z u_reach))]
        (set! gl_FragColor (vec4 (mix (* shade lit) u_haze fog) 1.0)))]}))

;; ---------------------------------------------------------------------
;; Doors and windows
;;
;; Textured quads standing a hair proud of the wall they are in. The old
;; renderer drew these with an affine transform on a 2D canvas, which
;; cannot make a trapezoid, so a door seen from an angle sheared. A
;; triangle interpolates its texture with perspective for free, so the
;; problem does not exist here to be solved.
;;
;; The picture's own alpha fades with the fog, which lets the correctly
;; hazed wall show through behind it.
;; ---------------------------------------------------------------------

(def panel-attributes ["a_position" :vec3 "a_uv" :vec2 "a_normal" :vec2])
(def ^:private panel-carried ["v_uv" :vec2 "v_normal" :vec2 "v_world" :vec3])

(def panel-vertex
  (vertex
   {:attributes panel-attributes
    :uniforms ["u_camera" :mat4]
    :varyings panel-carried
    :main
    '[(set! v_uv a_uv)
      (set! v_normal a_normal)
      (set! v_world a_position)
      (set! gl_Position (* u_camera (vec4 a_position 1.0)))]}))

(def panel-fragment
  (fragment
   {:varyings panel-carried
    :uniforms ["u_image" :picture "u_eye" :vec3 "u_sunward" :vec3
               "u_ambient" :float "u_reach" :vec3]
    :main
    '[(let [picture (texture2D u_image v_uv)
            fog (fogAt v_world u_eye u_reach)
            showing (* (:a picture) (- 1.0 fog))]
        (when (< showing 0.02) (discard))
        (let [lit (+ u_ambient
                     (* (- 1.0 u_ambient)
                        (max 0.0 (dot (normalize v_normal)
                                      (normalize (:xy u_sunward))))))]
          (set! gl_FragColor (vec4 (* (:rgb picture) lit) showing))))]}))

;; ---------------------------------------------------------------------
;; Trees and hedges
;;
;; Flat panels that turn to face you, expanded in the vertex shader from
;; the camera's own right vector so the buffers stay still while you
;; walk. The canopy is seven blobs evaluated per pixel in a square unit
;; space and stretched back out to feet at the last moment, which is
;; what lets one set of numbers fill both a tall narrow oak and a low
;; wide juniper without either being clipped by its own billboard.
;; ---------------------------------------------------------------------

(def billboard-attributes ["a_root" :vec3 "a_corner" :vec2 "a_shape" :vec3])
(def ^:private billboard-carried
  ["v_local" :vec2 "v_size" :vec2 "v_seed" :float "v_world" :vec3])

(def billboard-vertex
  (vertex
   {:attributes billboard-attributes
    :uniforms ["u_camera" :mat4 "u_right" :vec3]
    :varyings billboard-carried
    :main
    '[(let [world (+ a_root
                     (* u_right (* (:x a_corner) (:x a_shape)))
                     (vec3 0.0 0.0 (* (:y a_corner) (:y a_shape))))]
        (set! v_local (vec2 (* (:x a_corner) (:x a_shape))
                            (* (:y a_corner) (:y a_shape))))
        (set! v_size (:xy a_shape))
        (set! v_seed (:z a_shape))
        (set! v_world world)
        (set! gl_Position (* u_camera (vec4 world 1.0))))]}))

(def billboard-fragment
  (fragment
   {:varyings billboard-carried
    :uniforms ["u_leaf" :vec3 "u_leafLit" :vec3 "u_bark" :vec3
               "u_haze" :vec3 "u_eye" :vec3 "u_reach" :vec3 "u_trunk" :vec4]
    :functions
    '[{:name toSegment :out :float :in [place :vec2 from :vec2 to :vec2]
       :body [(let [reach (- place from)
                    run (- to from)
                    along (clamp (/ (dot reach run) (max (dot run run) 0.0001))
                                 0.0 1.0)]
                (return (length (- reach (* run along)))))]}]
    :main
    ;; u_trunk is (half a trunk at the root, how far up the fork is, how
    ;; much narrower the trunk gets by the fork, how far out the limbs
    ;; reach). A hedge passes zeroes and gets none of it.
    '[(let [top (:y v_size)
            fork (* top (:y u_trunk))
            canopy (* (- top fork) 0.5)
            unit (vec2 (max (:x v_size) 0.001) (max canopy 0.001))
            middle (vec2 0.0 (+ fork (* canopy 0.95)))
            place (/ (- v_local middle) unit)
            up (clamp (/ (:y v_local) (max (:y middle) 0.001)) 0.0 1.0)
            shade u_leaf
            standing false]
        ;; A trunk narrows as it goes up and puts out two limbs toward
        ;; the canopy. A rectangle of bark with leaves balanced on it is
        ;; a lollipop, and the way you can tell is that every tree looks
        ;; like the same lollipop.
        (let [taper (* (:x u_trunk) (mix 1.0 (:z u_trunk) up))
              lean (* (:w u_trunk) (:x v_size))
              limb (min (toSegment v_local (vec2 0.0 (* fork 0.75))
                                   (vec2 lean (:y middle)))
                        (toSegment v_local (vec2 0.0 (* fork 0.9))
                                   (vec2 (- lean) (* (:y middle) 0.86))))
              grain (hash21 (vec2 (floor (* (:y v_local) 3.0)) v_seed))]
          (when (and (< (:y v_local) (:y middle))
                     (or (< (abs (:x v_local)) taper)
                         (< limb (* (:x u_trunk) 0.55))))
            (set! shade (mix u_bark (* u_bark 1.25) grain))
            (set! standing true)))
        (dotimes [blob 7]
          (let [which (float blob)
                turn (hash21 (vec2 (+ v_seed (* which 3.7)) 1.0))
                outward (hash21 (vec2 (+ v_seed (* which 5.3)) 2.0))
                grow (hash21 (vec2 (+ v_seed (* which 7.1)) 3.0))
                ring (if (== blob (int 0)) 0.0 (+ 0.20 (* 0.25 outward)))
                at (vec2 (* (cos (* turn 6.2832)) ring)
                         (* (sin (* turn 6.2832)) ring 0.75))
                spread (if (== blob (int 0)) 0.52 (+ 0.26 (* 0.20 grow)))
                into (- spread (length (- place at)))]
            (when (> into 0.0)
              ;; How far inside its own blob a pixel sits, and where the
              ;; blob sits in the canopy, decide the shade together.
              ;; Picking one of two greens per blob made a tree out of
              ;; flat coins.
              (let [lift (clamp (+ (* 0.55 (- 0.5 (:y at)))
                                   (* 0.5 (- 1.0 (/ into spread)))
                                   (* 0.35 grow))
                                0.0 1.0)
                    dapple (hash21 (+ (floor (* place 9.0)) v_seed))]
                (set! shade (mix u_leafLit u_leaf
                                 (clamp (+ lift (* 0.22 (- dapple 0.5)))
                                        0.0 1.0)))
                (set! standing true)))))
        (when (not standing) (discard))
        (let [fog (fogAt v_world u_eye u_reach)]
          (set! gl_FragColor (vec4 (mix shade u_haze fog) 1.0))))]}))

;; ---------------------------------------------------------------------
;; Whatever it is that follows you
;;
;; One quad, placed by a uniform rather than by anything in a buffer,
;; because there is one of it and it never stops moving. It borrows the
;; screen mesh's two corners, which are already the four corners of a
;; square, so it costs no geometry at all.
;;
;; It is drawn out of horizontal bands that slip sideways and go missing,
;; which is a cheap thing to do and reads as something the renderer is
;; getting wrong rather than something the campus contains. That is the
;; intention. Nothing about it is meant to look like it was modelled.
;; ---------------------------------------------------------------------

(def wraith-attributes ["a_corner" :vec2])

(def ^:private wraith-carried ["v_local" :vec2 "v_world" :vec3])

(def wraith-vertex
  (vertex
   {:attributes wraith-attributes
    :uniforms ["u_camera" :mat4 "u_right" :vec3 "u_root" :vec3 "u_size" :vec2]
    :varyings wraith-carried
    :main
    '[(let [up (* (+ (:y a_corner) 1.0) 0.5)
            world (+ u_root
                     (* u_right (* (:x a_corner) (:x u_size)))
                     (vec3 0.0 0.0 (* up (:y u_size))))]
        (set! v_local (vec2 (:x a_corner) up))
        (set! v_world world)
        (set! gl_Position (* u_camera (vec4 world 1.0))))]}))

(def wraith-fragment
  (fragment
   {:varyings wraith-carried
    :uniforms ["u_tint" :vec3 "u_haze" :vec3 "u_eye" :vec3 "u_reach" :vec3
               "u_glitch" :vec3 "u_now" :float]
    :main
    ;; u_glitch is (how far the bands slip, how many of them survive, how
    ;; solid the whole thing is). All three climb as it gets closer, so
    ;; the nearer it is the more of it there is to see, which is the only
    ;; warning you get.
    '[(let [along (:x v_local)
            up (:y v_local)
            shoulder (- 1.0 (* 0.62 (abs (- up 0.62))))
            waist (* 0.5 shoulder (- 1.0 (* 0.35 (smoothstep 0.86 1.0 up))))
            band (floor (* up 30.0))
            frame (floor (* u_now 24.0))
            slip (* (:x u_glitch)
                    (- (hash21 (vec2 band frame)) 0.5)
                    2.0)
            edge (- waist (abs (- along slip)))]
        (when (< edge 0.0) (discard))
        (let [torn (hash21 (vec2 (+ band 91.0) frame))]
          (when (> torn (:y u_glitch)) (discard))
          (let [fog (fogAt v_world u_eye u_reach)
                shade (mix u_tint u_haze (* fog 0.7))]
            (set! gl_FragColor
                  (vec4 shade (* (:z u_glitch)
                                 (smoothstep 0.0 0.06 edge)
                                 (- 1.0 (* 0.5 fog))))))))]}))

;; ---------------------------------------------------------------------
;; Everything with real thickness: roofs, posts, the fountain, the water
;;
;; a_water is (how far apart the ripples are, how solid this vertex is).
;; A ripple spacing of nothing means no ripples, which is what a roof
;; and a lamp post want. The alpha rides along per-vertex, which is what
;; lets the bell of water fade out as it falls away from the nozzle
;; while the stone it lands on stays solid.
;; ---------------------------------------------------------------------

(def solid-attributes
  ["a_position" :vec3 "a_colour" :vec3 "a_normal" :vec3 "a_water" :vec2])

(def ^:private solid-carried
  ["v_colour" :vec3 "v_normal" :vec3 "v_world" :vec3 "v_water" :vec2])

(def solid-vertex
  (vertex
   {:attributes solid-attributes
    :uniforms ["u_camera" :mat4 "u_now" :float "u_swell" :vec2]
    :varyings solid-carried
    :main
    ;; A negative ripple spacing means this vertex is the top of a body
    ;; of water rather than a thing standing in one, and it rides two
    ;; crossing waves. Two rather than one because a single sine is a
    ;; corrugated roof, and the pair never quite repeats.
    ;;
    ;; The surface used to rise and fall with its normal pointing
    ;; straight up the whole time, so it moved and was lit like a table.
    ;; The slope of each wave is its cosine, and the pair of slopes is
    ;; the normal, for a handful of multiplies a vertex.
    '[(let [place a_position
            facing a_normal
            phase (* u_now 6.2832)
            height (:x u_swell)
            length (:y u_swell)]
        (when (< (:x a_water) 0.0)
          (let [long-wave (+ (/ (+ (:x place) (:y place)) length) phase)
                short-wave (- (/ (- (:x place) (* (:y place) 1.7))
                                 (* length 0.6))
                              phase)
                long-slope (* (/ height length) (cos long-wave))
                short-slope (* (/ (* height 0.5) (* length 0.6)) (cos short-wave))]
            (set! place
                  (+ place
                     (vec3 0.0 0.0
                           (+ (* height (sin long-wave))
                              (* height 0.5 (sin short-wave))))))
            (set! facing
                  (normalize (vec3 (- (+ long-slope short-slope))
                                   (- (- long-slope (* 1.7 short-slope)))
                                   1.0)))))
        (set! v_colour a_colour)
        (set! v_normal facing)
        (set! v_world place)
        (set! v_water a_water)
        (set! gl_Position (* u_camera (vec4 place 1.0))))]}))

(def solid-fragment
  (fragment
   {:varyings solid-carried
    :uniforms ["u_haze" :vec3 "u_eye" :vec3 "u_foam" :vec3 "u_middle" :vec3
               "u_sunward" :vec3 "u_ambient" :float "u_now" :float
               "u_reach" :vec3 "u_sky" :vec3 "u_sun" :vec3]
    :main
    ;; u_now arrives already wrapped into 0..1 rather than as a clock in
    ;; milliseconds, because a mediump fragment shader loses the low bits
    ;; of a number that large and the ripples would step rather than move.
    '[(let [shade v_colour]
        (when (> (:x v_water) 0.001)
          (let [from-middle (distance (:xy v_world) (:xy u_middle))
                phase (fract (- (/ from-middle (:x v_water)) u_now))]
            (set! shade (mix shade u_foam (* 0.5 (pow (- 1.0 phase) 4.0))))))
        (let [surface (normalize v_normal)
              lit (+ u_ambient
                     (* (- 1.0 u_ambient) (max 0.0 (dot surface u_sunward))))
              fog (fogAt v_world u_eye u_reach)
              body (* shade lit)
              solidity (:y v_water)]
          ;; The top of a body of water is mostly what it reflects. Seen
          ;; from above it is the colour of the water; seen along its
          ;; length it is the colour of the sky, and more solid than it
          ;; looks from above. That is Schlick's approximation of the
          ;; fresnel term, and the sun's glint off each wave is the
          ;; reflected sun direction against the eye. Everything else
          ;; drawn with this program skips all of it.
          (when (< (:x v_water) 0.0)
            (let [toward-eye (normalize (- u_eye v_world))
                  grazing (pow (- 1.0 (max (dot toward-eye surface) 0.0)) 5.0)
                  fresnel (+ 0.04 (* 0.96 grazing))
                  bounced (reflect (- toward-eye) surface)
                  sky-seen (mix u_haze u_sky
                                (smoothstep 0.0 0.55 (max (:z bounced) 0.0)))
                  glint (pow (max (dot (reflect (- u_sunward) surface) toward-eye)
                                  0.0)
                             90.0)]
              (set! body (+ (mix body sky-seen fresnel) (* u_sun glint)))
              (set! solidity (mix solidity 1.0 fresnel))))
          (set! gl_FragColor (vec4 (mix body u_haze fog) solidity))))]}))

;; ---------------------------------------------------------------------
;; What the renderer asks for
;; ---------------------------------------------------------------------

(def programs
  "Every program, compiled, by the name the renderer calls it. This is
  what travels to the page."
  {:sky {:vertex sky-vertex :fragment sky-fragment}
   :cloud {:vertex cloud-vertex :fragment point-fragment}
   :mist {:vertex mist-vertex :fragment point-fragment}
   :ground {:vertex ground-vertex :fragment ground-fragment}
   :wall {:vertex wall-vertex :fragment wall-fragment}
   :panel {:vertex panel-vertex :fragment panel-fragment}
   :billboard {:vertex billboard-vertex :fragment billboard-fragment}
   :wraith {:vertex wraith-vertex :fragment wraith-fragment}
   :solid {:vertex solid-vertex :fragment solid-fragment}})

(def layouts
  "Every program's vertex buffer: each attribute's name and how many
  floats it spends, in packing order, straight off the same vectors the
  shaders declare. The mesh builders pack to this and the card reads
  it, and there is nowhere for the two to disagree."
  {:sky (glsl/layout sky-attributes)
   :cloud (glsl/layout cloud-attributes)
   :mist (glsl/layout mist-attributes)
   :ground (glsl/layout ground-attributes)
   :wall (glsl/layout wall-attributes)
   :panel (glsl/layout panel-attributes)
   :billboard (glsl/layout billboard-attributes)
   :wraith (glsl/layout wraith-attributes)
   :solid (glsl/layout solid-attributes)})