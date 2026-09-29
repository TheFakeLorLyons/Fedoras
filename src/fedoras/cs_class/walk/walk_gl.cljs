(ns fedoras.cs-class.walk.walk-gl
  "The thin layer between ClojureScript and the graphics card.

  Nothing in here knows anything about a campus. It compiles a program,
  hands it a pile of numbers, and draws them, and it is deliberately the
  most boring file in the directory: everything interesting should be
  either in the plan or in a shader, and if it drifts in here somebody
  has put it in the wrong place.

  The constants are written out below rather than required from
  goog.webgl. This runs under Scittle, which is SCI in the browser and
  has no Closure library to require from, so goog.webgl is not a
  namespace that exists here. Reading them off the context instead,
  as `(.-ARRAY_BUFFER gl)`, would work, but it is a property lookup on
  a hot path every frame forever, and the values are frozen by the
  WebGL specification and cannot move."
  (:require [clojure.string :as str]))

;; ---------------------------------------------------------------------
;; The WebGL 1 enumerants this file uses, at their specified values
;; ---------------------------------------------------------------------

(def DEPTH_BUFFER_BIT 0x00000100)
(def COLOR_BUFFER_BIT 0x00004000)

(def POINTS 0x0000)
(def TRIANGLES 0x0004)

(def SRC_ALPHA 0x0302)
(def ONE_MINUS_SRC_ALPHA 0x0303)
(def ONE 1)

(def LESS 0x0201)

(def CULL_FACE 0x0B44)
(def DEPTH_TEST 0x0B71)
(def BLEND 0x0BE2)

(def UNSIGNED_BYTE 0x1401)
(def FLOAT 0x1406)

(def RGBA 0x1908)

(def NEAREST 0x2600)
(def LINEAR 0x2601)
(def LINEAR_MIPMAP_LINEAR 0x2703)
(def TEXTURE_MAG_FILTER 0x2800)
(def TEXTURE_MIN_FILTER 0x2801)
(def TEXTURE_WRAP_S 0x2802)
(def TEXTURE_WRAP_T 0x2803)
(def TEXTURE_2D 0x0DE1)
(def TEXTURE0 0x84C0)

(def CLAMP_TO_EDGE 0x812F)

(def ARRAY_BUFFER 0x8892)
(def STATIC_DRAW 0x88E4)

(def FRAGMENT_SHADER 0x8B30)
(def VERTEX_SHADER 0x8B31)
(def COMPILE_STATUS 0x8B81)
(def LINK_STATUS 0x8B82)
(def ACTIVE_UNIFORMS 0x8B86)
(def ACTIVE_ATTRIBUTES 0x8B89)

(def ALIASED_POINT_SIZE_RANGE 0x846D)
(def UNPACK_FLIP_Y_WEBGL 0x9240)

;; ---------------------------------------------------------------------
;; Getting a card at all
;; ---------------------------------------------------------------------

(defn context
  "A drawing context, or nil. Antialiasing is off on purpose: the canvas
  is drawn small and stretched with image-rendering: pixelated, and
  smoothing the edges before enlarging them would fight that."
  [canvas]
  (let [options #js {:alpha false
                     :antialias false
                     :depth true
                     :stencil false
                     :premultipliedAlpha false
                     :preserveDrawingBuffer false
                     :powerPreference "high-performance"}]
    (or (.getContext canvas "webgl" options)
        (.getContext canvas "experimental-webgl" options))))

(defn biggest-point
  "The largest point sprite this card will draw. Some will do a thousand
  and some will do sixty-four, and asking for more than it has is not an
  error, it is a silently wrong picture."
  [gl]
  (let [range (.getParameter gl ALIASED_POINT_SIZE_RANGE)]
    (if range (aget range 1) 64)))

;; ---------------------------------------------------------------------
;; Programs
;; ---------------------------------------------------------------------

(defn- numbered
  "Source with line numbers on it, so a card that says 0:39 is pointing
  at something you can read rather than at a line you have to count to."
  [source]
  (->> (str/split-lines source)
       (map-indexed (fn [i line] (str (inc i) "  " line)))
       (str/join "\n")))

(defn- compile-one
  "One shader, named. The card reports a line and a column and nothing
  about which of fifteen shaders it was reading, so the name and the
  numbered source are put beside the complaint here. Finding that out
  by elimination is an afternoon; printing it is four lines."
  [gl kind label source]
  (let [shader (.createShader gl kind)]
    (.shaderSource gl shader source)
    (.compileShader gl shader)
    (if (.getShaderParameter gl shader COMPILE_STATUS)
      shader
      (do (js/console.error (str "walk: " label " shader did not compile\n")
                            (.getShaderInfoLog gl shader)
                            (str "\n" (numbered source)))
          (.deleteShader gl shader)
          nil))))

(defn- named-attributes
  "Every attribute the linked program actually kept, by name. Asked of
  the program rather than listed by hand, so a uniform that stops being
  used cannot leave a stale location behind."
  [gl program]
  (into {}
        (for [i (range (.getProgramParameter gl program ACTIVE_ATTRIBUTES))
              :let [nm (.-name (.getActiveAttrib gl program i))]]
          [nm (.getAttribLocation gl program nm)])))

(defn- named-uniforms
  [gl program]
  (into {}
        (for [i (range (.getProgramParameter gl program ACTIVE_UNIFORMS))
              :let [nm (.-name (.getActiveUniform gl program i))]]
          [nm (.getUniformLocation gl program nm)])))

(defn program
  "Compile, link, and find out what the result answers to. Gives back
  {:program :attributes :uniforms}, or nil if anything refused, having
  said which one refused and why."
  ([gl vertex-source fragment-source]
   (program gl "unnamed" vertex-source fragment-source))
  ([gl label vertex-source fragment-source]
   (let [vertex (compile-one gl VERTEX_SHADER (str label " vertex") vertex-source)
         fragment (compile-one gl FRAGMENT_SHADER (str label " fragment") fragment-source)]
     (when (and vertex fragment)
       (let [linked (.createProgram gl)]
         (.attachShader gl linked vertex)
         (.attachShader gl linked fragment)
         (.linkProgram gl linked)
         (.deleteShader gl vertex)
         (.deleteShader gl fragment)
         (if (.getProgramParameter gl linked LINK_STATUS)
           {:program linked
            :attributes (named-attributes gl linked)
            :uniforms (named-uniforms gl linked)}
           (do (js/console.error (str "walk: " label " did not link")
                                 (.getProgramInfoLog gl linked))
               (.deleteProgram gl linked)
               nil)))))))

;; ---------------------------------------------------------------------
;; Numbers on the card
;; ---------------------------------------------------------------------

(defn mesh
  "A pile of vertex numbers uploaded once and left there.

  `layout` names the attributes each vertex carries and how many floats
  each of them is, in the order they are packed: [[\"a_position\" 3]
  [\"a_patch\" 3]] is six floats per vertex. The stride and the offsets
  fall out of that, which is why nothing else in this codebase ever
  writes a byte offset down."
  [gl layout numbers]
  (let [width (reduce + 0 (map second layout))
        buffer (.createBuffer gl)]
    (.bindBuffer gl ARRAY_BUFFER buffer)
    (.bufferData gl ARRAY_BUFFER numbers STATIC_DRAW)
    {:buffer buffer
     :layout layout
     :width width
     :count (js/Math.floor (/ (.-length numbers) width))}))

(defn drop-mesh!
  [gl {:keys [buffer]}]
  (when buffer (.deleteBuffer gl buffer)))

(defn- bind-mesh!
  [gl {:keys [attributes]} {:keys [buffer layout width]}]
  (.bindBuffer gl ARRAY_BUFFER buffer)
  (let [stride (* 4 width)]
    (loop [offset 0 remaining layout]
      (when-let [[nm size] (first remaining)]
        (let [where (get attributes nm)]
          (when (and where (>= where 0))
            (.enableVertexAttribArray gl where)
            (.vertexAttribPointer gl where size FLOAT false stride offset)))
        (recur (+ offset (* 4 size)) (next remaining))))))

;; ---------------------------------------------------------------------
;; Uniforms
;;
;; One entry point that works out what it was handed. A Float32Array of
;; sixteen is a matrix, a vector is however many floats long it is, and
;; anything else is a single number.
;; ---------------------------------------------------------------------

(defn uniforms!
  [gl {:keys [uniforms]} values]
  (doseq [[nm value] values]
    (when-let [where (get uniforms nm)]
      (cond
        (instance? js/Float32Array value)
        (.uniformMatrix4fv gl where false value)

        (number? value)
        (.uniform1f gl where value)

        (= 2 (count value))
        (.uniform2f gl where (nth value 0) (nth value 1))

        (= 3 (count value))
        (.uniform3f gl where (nth value 0) (nth value 1) (nth value 2))

        (= 4 (count value))
        (.uniform4f gl where (nth value 0) (nth value 1) (nth value 2) (nth value 3))

        :else nil))))

(defn sampler!
  "Point a named sampler at a texture on a given unit.

  The program is made current first. A uniform is set on whichever
  program is bound at the time, so setting one before using its program
  writes it into whatever was drawn last. Every sampler here happens to
  want unit zero, which is also the default, so the mistake would have
  cost nothing and stayed hidden until the day something wanted unit
  one."
  [gl {:keys [program uniforms]} nm unit texture]
  (when-let [where (get uniforms nm)]
    (.useProgram gl program)
    (.activeTexture gl (+ TEXTURE0 unit))
    (.bindTexture gl TEXTURE_2D texture)
    (.uniform1i gl where unit)))

;; ---------------------------------------------------------------------
;; Textures
;; ---------------------------------------------------------------------

(defn- clamp!
  [gl smooth?]
  (let [filtering (if smooth? LINEAR NEAREST)]
    (.texParameteri gl TEXTURE_2D TEXTURE_WRAP_S CLAMP_TO_EDGE)
    (.texParameteri gl TEXTURE_2D TEXTURE_WRAP_T CLAMP_TO_EDGE)
    (.texParameteri gl TEXTURE_2D TEXTURE_MIN_FILTER filtering)
    (.texParameteri gl TEXTURE_2D TEXTURE_MAG_FILTER filtering)))

(defn bytes-texture
  "A texture out of raw bytes. Used for the palette, which is one
  rectangle of colour two pixels tall: near shades on one row, far
  shades on the other. Sampled with no smoothing, so a material never
  bleeds into the one filed next to it."
  [gl w h data]
  (let [texture (.createTexture gl)]
    (.bindTexture gl TEXTURE_2D texture)
    (.pixelStorei gl UNPACK_FLIP_Y_WEBGL false)
    (.texImage2D gl TEXTURE_2D 0 RGBA w h 0 RGBA UNSIGNED_BYTE data)
    (clamp! gl false)
    texture))

(defn picture-texture
  "A texture out of anything the browser can draw: an image, or a canvas
  somebody has already rasterized an SVG onto. Flipped on upload so that
  the bottom of the picture is the bottom of the wall, and mipmapped so
  a door seen from two hundred feet is a small door rather than a
  handful of surviving pixels."
  [gl source power-of-two?]
  (let [texture (.createTexture gl)]
    (.bindTexture gl TEXTURE_2D texture)
    (.pixelStorei gl UNPACK_FLIP_Y_WEBGL true)
    (.texImage2D gl TEXTURE_2D 0 RGBA RGBA UNSIGNED_BYTE source)
    (if power-of-two?
      (do (.texParameteri gl TEXTURE_2D TEXTURE_WRAP_S CLAMP_TO_EDGE)
          (.texParameteri gl TEXTURE_2D TEXTURE_WRAP_T CLAMP_TO_EDGE)
          (.texParameteri gl TEXTURE_2D TEXTURE_MIN_FILTER LINEAR_MIPMAP_LINEAR)
          (.texParameteri gl TEXTURE_2D TEXTURE_MAG_FILTER LINEAR)
          (.generateMipmap gl TEXTURE_2D))
      (clamp! gl true))
    texture))

;; ---------------------------------------------------------------------
;; Matrices
;;
;; Sixteen numbers in column order, which is what WebGL wants and what
;; every article about this gets wrong once before getting it right.
;; ---------------------------------------------------------------------

(defn perspective
  "A left-handed projection, looking down positive z in eye space.

  Left-handed because the plan is: x runs east, y runs *south* the way
  it does on any drawing with an origin in the top corner, and z runs
  up. That is a left-handed world, and the honest thing is to build a
  projection that matches it rather than mirror the map to suit a
  convention nobody on this campus has ever heard of.

  `fov` is measured across, not up, because that is what :fov meant
  when it was a raycaster and changing what a number means underneath
  somebody is worse than a slightly unusual projection. `aspect` is
  width over height and lands on the vertical scale for that reason."
  [fov aspect near far]
  (let [f (/ 1.0 (js/Math.tan (/ fov 2)))
        span (- far near)]
    (js/Float32Array.
     #js [f 0 0 0
          0 (* f aspect) 0 0
          0 0 (/ (+ far near) span) 1
          0 0 (/ (* -2 far near) span) 0])))

(defn look
  "Where the eye is and which way it is pointed, as a matrix. Sideways
  is [-sin cos], which is south when you are facing east, which is what
  south is when the map has y going down."
  [[ex ey ez] facing]
  (let [c (js/Math.cos facing)
        s (js/Math.sin facing)
        rx (- s) ry c
        fx c fy s]
    (js/Float32Array.
     #js [rx 0 fx 0
          ry 0 fy 0
          0  1 0  0
          (- (+ (* rx ex) (* ry ey)))
          (- ez)
          (- (+ (* fx ex) (* fy ey)))
          1])))

(defn times
  "One matrix through another, in column order."
  [a b]
  (let [out (js/Float32Array. 16)]
    (dotimes [col 4]
      (dotimes [row 4]
        (aset out (+ (* col 4) row)
              (+ (* (aget a row) (aget b (* col 4)))
                 (* (aget a (+ row 4)) (aget b (+ (* col 4) 1)))
                 (* (aget a (+ row 8)) (aget b (+ (* col 4) 2)))
                 (* (aget a (+ row 12)) (aget b (+ (* col 4) 3)))))))
    out))

;; ---------------------------------------------------------------------
;; Drawing
;; ---------------------------------------------------------------------

(defn draw!
  "Use a program, set its uniforms, point it at a mesh, draw the lot."
  ([gl program mesh values] (draw! gl program mesh values TRIANGLES))
  ([gl program mesh values mode]
   (when (and program mesh (pos? (:count mesh)))
     (.useProgram gl (:program program))
     (uniforms! gl program values)
     (bind-mesh! gl program mesh)
     (.drawArrays gl mode 0 (:count mesh)))))