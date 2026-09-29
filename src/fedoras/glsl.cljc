(ns fedoras.glsl
  "Shaders, written as Clojure, compiled to GLSL.

  A shader has to reach the graphics card as text, because that is the
  only thing `shaderSource` accepts. Nothing changes that. What can
  change is whether a person has to write that text, and the answer here
  is no: the forms below are Clojure data, they are checked before they
  are printed, and the GLSL is the output rather than the source.

  GLSL is statically typed and unusually strict about
  it: `1` is an int, `1.0` is a float, and adding one to the other is an
  error rather than a promotion. Multiplying a vec3 by a vec2 is an
  error. Taking `.xyz` off a vec2 is an error. Every one of those is
  reported here, with the form that caused it, at build time, in
  Clojure, rather than as `ERROR: 0:39` from a driver that will not 
  say which of fifteen shaders it was reading.

  Inference is also what makes `let` possible. GLSL has no `var`: every
  declaration states its type. Because the type of the right-hand side
  is known, the left-hand side does not have to be written down, and
  `(let [tuft (pick ...)] ...)` becomes `float tuft = pick(...);` on its
  own.

  It does not optimise, reorder, or  fold anything. The driver has an 
  optimiser and it is better at this than anything written here would 
  be. This is essentially a printer with opinions about types.

  Peviously: Zach Oakes wrote iglu for play-cljc and it is similar.
  This differs in three ways worth knowing: types are inferred rather
  than declared per function in a `:signatures` map, blocks are Clojure
  forms rather than strings keyed on `\"if\"` and `\"for\"`, and the
  declarations are the same vectors the vertex buffer layout is built
  from, so a shader and the buffer it reads cannot disagree."
  (:require [clojure.string :as str]
            #?(:clj [malli.core :as malli])
            #?(:clj [malli.error :as malli-error])))

;; ---------------------------------------------------------------------
;; The type system, such as it is
;; ---------------------------------------------------------------------

(def glsl-name
  "Every type this dialect has, and what GLSL calls it. `:picture` is
  sampler2D under another name, because `sampler2D` is a thing the
  hardware has and `picture` is the thing the campus has."
  {:void "void"
   :bool "bool"
   :int "int"
   :float "float"
   :vec2 "vec2" :vec3 "vec3" :vec4 "vec4"
   :ivec2 "ivec2" :ivec3 "ivec3" :ivec4 "ivec4"
   :bvec2 "bvec2" :bvec3 "bvec3" :bvec4 "bvec4"
   :mat2 "mat2" :mat3 "mat3" :mat4 "mat4"
   :picture "sampler2D"})

(def floats-in
  "How many numbers a vertex spends on one attribute of each shape. A
  matrix and a sampler are not vertex data and are absent on purpose."
  {:float 1 :vec2 2 :vec3 3 :vec4 4})

(def ^:private width
  "How many components a value of each type has, for the arithmetic
  rules below. Matrices are not in here; they have their own rules."
  {:bool 1 :int 1 :float 1
   :vec2 2 :vec3 3 :vec4 4
   :ivec2 2 :ivec3 3 :ivec4 4
   :bvec2 2 :bvec3 3 :bvec4 4})

(def ^:private float-vector {1 :float 2 :vec2 3 :vec3 4 :vec4})

(def ^:private floaty? #{:float :vec2 :vec3 :vec4 :mat2 :mat3 :mat4})

(def ^:private inty? #{:int :ivec2 :ivec3 :ivec4})

(def ^:private matrix-width {:mat2 2 :mat3 3 :mat4 4})

(def ^:private built-in-globals
  "The names the hardware supplies. Writing to one of these is how a
  shader says anything at all."
  {'gl_Position :vec4
   'gl_PointSize :float
   'gl_FragColor :vec4
   'gl_FragCoord :vec4
   'gl_PointCoord :vec2})

(def reserved
  "Every word GLSL keeps for itself, across every version of it. The ES
  1.00 list alone would be enough for this card and not enough for
  somebody else's: `patch`, `sample` and `buffer` are free here and
  reserved from GLSL 4.00 on, so a name that compiles on this machine
  and refuses on a reader's is the failure this list exists to stop."
  '#{asm class union enum typedef template this packed goto switch default
     inline noinline volatile public static extern external interface flat
     long short double half fixed unsigned superp input output sizeof cast
     namespace using in out inout struct invariant
     patch sample buffer shared coherent restrict readonly writeonly
     atomic_uint layout centroid smooth noperspective subroutine common
     partition active filter resource row_major
     attribute varying uniform const precision highp mediump lowp
     break continue do for while if else discard return void true false})

(defn- fail
  [message form]
  (throw (ex-info (str "fedoras.glsl: " message)
                  {:form form :message message})))

(defn identifier
  "A Clojure name as a GLSL one. Hyphens become underscores, because a
  hyphen in GLSL is a minus sign and `along-period` would compile to a
  subtraction of two things that do not exist. This is what lets a
  shader be written in the same case as the rest of the book.

  A name that GLSL has taken for itself is refused here rather than at
  the driver, which reports it as a syntax error on whatever line
  happens to follow it."
  [nm]
  (let [text (str nm)
        converted (str/replace text "-" "_")]
    (when (or (reserved (symbol text)) (reserved (symbol converted)))
      (fail (str "GLSL keeps `" converted "` for itself, so nothing can be "
                 "called that. It is a keyword or a reserved word in some "
                 "version of the language, and a name that compiles on one "
                 "card and refuses on another is worse than one that refuses "
                 "everywhere")
            nm))
    converted))

(defn- constructor
  "The type a constructor makes, when the head of a form names one."
  [head]
  (when (symbol? head)
    (let [as-keyword (keyword (name head))]
      (when (glsl-name as-keyword) as-keyword))))

;; ---------------------------------------------------------------------
;; Built-in functions
;;
;; A signature is a keyword saying how the return type follows from the
;; arguments, because almost every one of these is one of four shapes.
;; `:like-first` covers everything that works componentwise and gives
;; back what it was given.
;; ---------------------------------------------------------------------

(def ^:private built-ins
  {'radians :like-first 'degrees :like-first
   'sin :like-first 'cos :like-first 'tan :like-first
   'asin :like-first 'acos :like-first
   'exp :like-first 'log :like-first 'exp2 :like-first 'log2 :like-first
   'sqrt :like-first 'inversesqrt :like-first
   'abs :like-first 'sign :like-first
   'floor :like-first 'ceil :like-first 'fract :like-first
   'normalize :like-first
   'atan :like-first
   'pow :like-first
   'mod :like-first 'min :like-first 'max :like-first
   'clamp :like-first 'mix :like-first 'step :like-second
   'smoothstep :like-last
   'length :float 'distance :float 'dot :float
   'cross :vec3
   'reflect :like-first 'refract :like-first 'faceforward :like-first
   'texture2D :vec4 'textureCube :vec4})

;; ---------------------------------------------------------------------
;; Inference
;; ---------------------------------------------------------------------

(declare infer)

(defn- swizzle?
  [head]
  (and (keyword? head)
       (re-matches #"[xyzw]{1,4}|[rgba]{1,4}|[stpq]{1,4}" (name head))))

(defn- infer-swizzle
  [environment head args form]
  (let [source (first args)
        from (infer environment source)
        components (name head)
        available (or (width from)
                      (fail (str "cannot take ." components " off a "
                                 (glsl-name from))
                            form))
        slot {\x 1 \y 2 \z 3 \w 4 \r 1 \g 2 \b 3 \a 4 \s 1 \t 2 \p 3 \q 4}]
    (when-not (= 1 (count args))
      (fail (str "." components " takes one value, and was given "
                 (count args))
            form))
    (doseq [letter components]
      (when (> (slot letter) available)
        (fail (str "." components " reaches past the end of a "
                   (glsl-name from) ", which has " available " components")
              form)))
    (if (inty? from)
      (get {1 :int 2 :ivec2 3 :ivec3 4 :ivec4} (count components))
      (float-vector (count components)))))

(defn- infer-arithmetic
  [head kinds form]
  (let [[left right] kinds]
    (cond
      (nil? right) left

      (= left right) left

      ;; a matrix through a vector, or a matrix scaled
      (and (= '* head) (matrix-width left) (= (width right) (matrix-width left)))
      right

      (and (= '* head) (matrix-width right) (= (width left) (matrix-width right)))
      left

      (and (matrix-width left) (= :float right)) left
      (and (matrix-width right) (= :float left)) right

      ;; a scalar against a vector of the same family
      (and (= :float left) (floaty? right)) right
      (and (= :float right) (floaty? left)) left
      (and (= :int left) (inty? right)) right
      (and (= :int right) (inty? left)) left

      (and (floaty? left) (inty? right))
      (fail (str "cannot " head " a " (glsl-name left) " and a "
                 (glsl-name right) ". GLSL will not promote an int to a "
                 "float for you; write the literal as 1.0, or wrap it in "
                 "(float ...)")
            form)

      (and (inty? left) (floaty? right))
      (fail (str "cannot " head " an " (glsl-name left) " and a "
                 (glsl-name right) ". Write the int literal as a float, "
                 "or wrap it in (float ...)")
            form)

      :else
      (fail (str "cannot " head " a " (glsl-name left) " and a "
                 (glsl-name right))
            form))))

(defn- infer-call
  [environment head args form]
  (let [kinds (mapv (fn [each] (infer environment each)) args)]
    (cond
      (constructor head) (constructor head)

      ('#{+ - * /} head)
      (reduce (fn [carried next-kind]
                (infer-arithmetic head [carried next-kind] form))
              (first kinds) (rest kinds))

      ('#{< > <= >= == !=} head)
      (let [[left right] kinds]
        (when-not (= left right)
          (fail (str "cannot compare a " (glsl-name left) " with a "
                     (glsl-name right) ". GLSL compares like with like and "
                     "promotes nothing, so a whole number written against an "
                     "int wants (int 0) rather than 0")
                form))
        :bool)

      ('#{and or} head)
      (do (doseq [each kinds]
            (when-not (= :bool each)
              (fail (str "and and or take conditions, and this one was given a "
                         (glsl-name each))
                    form)))
          :bool)

      (= 'not head)
      (do (when-not (= :bool (first kinds))
            (fail (str "not takes a condition, and this one was given a "
                       (glsl-name (first kinds)))
                  form))
          :bool)

      (= 'if head)
      (let [[_ then otherwise] kinds]
        (when (and otherwise (not= then otherwise))
          (fail (str "the two arms of an if give back a " (glsl-name then)
                     " and a " (glsl-name otherwise) ", which cannot both "
                     "be the value of one expression")
                form))
        then)

      :else
      (case (get built-ins head)
        :like-first (first kinds)
        :like-second (second kinds)
        :like-last (last kinds)
        :float :float
        :vec3 :vec3
        :vec4 :vec4
        nil (or (get-in environment [:functions head])
                (fail (str "no such function: " head) form))))))

(defn infer
  "The type of an expression, from the declarations and whatever `let`
  has bound so far. Everything this file checks rests on this."
  [environment form]
  (cond
    (boolean? form) :bool
    (number? form) :float

    (symbol? form)
    (or (get-in environment [:names form])
        (get built-in-globals form)
        (fail (str "nothing named " form " is in scope here") form))

    (seq? form)
    (let [[head & args] form]
      (if (swizzle? head)
        (infer-swizzle environment head args form)
        (infer-call environment head args form)))

    :else (fail (str "cannot work out what this is: " (pr-str form)) form)))

;; ---------------------------------------------------------------------
;; Printing
;; ---------------------------------------------------------------------

(declare ->expression ->statements)

(def ^:private infix
  {'+ "+" '- "-" '* "*" '/ "/"
   '< "<" '> ">" '<= "<=" '>= ">=" '== "==" '!= "!="
   'and "&&" 'or "||"})

(defn- number->glsl
  "A bare number is always a float.

  GLSL does distinguish `1` from `1.0` and will not promote one to the
  other, so the obvious design is to carry Clojure's own distinction
  through. That design does not survive the trip: ClojureScript has one
  number type, `(integer? 1.0)` is true there, and the same source would
  then compile to different GLSL on a JVM and in a browser. One number
  kind and an explicit `(int 0)` where a whole number is wanted is the
  only version of this that means the same thing everywhere."
  [value]
  (let [printed (str (double value))]
    (if (or (str/includes? printed ".") (str/includes? printed "e")
            (str/includes? printed "E"))
      printed
      (str printed ".0"))))

(defn- ->expression
  [environment form]
  (cond
    (boolean? form) (str form)
    (number? form) (number->glsl form)
    (symbol? form) (identifier form)

    (seq? form)
    (let [[head & args] form]
      (cond
        (swizzle? head)
        (str (->expression environment (first args)) "." (name head))

        (and (= 'int head) (= 1 (count args)) (number? (first args)))
        (str (long (first args)))

        (constructor head)
        (str (glsl-name (constructor head)) "("
             (str/join ", " (map (partial ->expression environment) args))
             ")")

        (and (= 'not head) (= 1 (count args)))
        (str "!" (->expression environment (first args)))

        (and (= '- head) (= 1 (count args)))
        (str "-" (->expression environment (first args)))

        (infix head)
        (str "(" (str/join (str " " (infix head) " ")
                           (map (partial ->expression environment) args))
             ")")

        (= 'if head)
        (let [[test then otherwise] args]
          (str "(" (->expression environment test)
               " ? " (->expression environment then)
               " : " (->expression environment otherwise) ")"))

        :else
        (str (identifier head) "("
             (str/join ", " (map (partial ->expression environment) args))
             ")")))

    :else (fail (str "cannot print " (pr-str form)) form)))

(defn- indent
  [depth line]
  (str (apply str (repeat depth "  ")) line))

(defn- ->statement
  "One statement, and the environment the statements after it see. A
  `let` widens that environment, which is the only reason this gives
  back a pair rather than a string."
  [environment depth form]
  (let [[head & args] form]
    (case head
      let
      (let [[bindings & body] args
            [lines widened]
            (reduce (fn [[lines carried] [nm value]]
                      (let [kind (infer carried value)]
                        [(conj lines
                               (indent depth
                                       (str (glsl-name kind) " "
                                            (identifier nm) " = "
                                            (->expression carried value) ";")))
                         (assoc-in carried [:names nm] kind)]))
                    [[] environment]
                    (partition 2 bindings))]
        [(into lines (->statements widened depth body)) environment])

      set!
      (let [[target value] args
            wanted (infer environment target)
            given (infer environment value)]
        (when-not (= wanted given)
          (fail (str "cannot put a " (glsl-name given) " into " target
                     ", which is a " (glsl-name wanted))
                form))
        [[(indent depth (str (->expression environment target) " = "
                             (->expression environment value) ";"))]
         environment])

      if
      (let [[test then otherwise] args]
        [(concat
          [(indent depth (str "if (" (->expression environment test) ") {"))]
          (->statements environment (inc depth) [then])
          (if otherwise
            (concat [(indent depth "} else {")]
                    (->statements environment (inc depth) [otherwise])
                    [(indent depth "}")])
            [(indent depth "}")]))
         environment])

      when
      (let [[test & body] args]
        [(concat
          [(indent depth (str "if (" (->expression environment test) ") {"))]
          (->statements environment (inc depth) body)
          [(indent depth "}")])
         environment])

      dotimes
      (let [[[counter limit] & body] args
            inside (assoc-in environment [:names counter] :int)]
        (when-not (integer? limit)
          (fail (str "a dotimes bound has to be a literal whole number in "
                     "GLSL ES 1.00, and this one is " (pr-str limit))
                form))
        [(concat
          [(indent depth (let [named (identifier counter)]
                           (str "for (int " named " = 0; " named " < "
                                limit "; " named "++) {")))]
          (->statements inside (inc depth) body)
          [(indent depth "}")])
         environment])

      return
      [[(indent depth (if (seq args)
                        (str "return " (->expression environment (first args)) ";")
                        "return;"))]
       environment]

      discard
      [[(indent depth "discard;")] environment]

      ;; anything else is an expression standing on its own line
      [[(indent depth (str (->expression environment form) ";"))] environment])))

(defn- ->statements
  [environment depth body]
  (first
   (reduce (fn [[lines carried] form]
             (let [[emitted widened] (->statement carried depth form)]
               [(into lines emitted) widened]))
           [[] environment]
           body)))

;; ---------------------------------------------------------------------
;; Declarations
;; ---------------------------------------------------------------------

(defn- declare-block
  "A run of GLSL declarations from a flat vector of name and type. Flat
  and ordered rather than a map, because the order an attribute is
  declared in is the order it is packed in, and a map does not promise
  one."
  [kind named]
  (str/join
   (map (fn [[nm kind-of]]
          (str kind " " (glsl-name kind-of) " " (identifier nm) ";\n"))
        (partition 2 named))))

(defn layout
  "A vertex layout: each attribute's name and how many floats it spends,
  in packing order. The buffer builder works its stride and offsets out
  from this, so nothing anywhere counts floats by hand."
  [attributes]
  (mapv (fn [[nm kind]]
          [(identifier nm)
           (or (floats-in kind)
               (fail (str nm " is a " (glsl-name kind)
                          ", which is not something a vertex can carry")
                     attributes))])
        (partition 2 attributes)))

(defn- named->environment
  [& declaration-vectors]
  {:names (into {}
                (mapcat (fn [named]
                          (map (fn [[nm kind]] [(symbol nm) kind])
                               (partition 2 named))))
                declaration-vectors)})

;; ---------------------------------------------------------------------
;; What a shader looks like
;; ---------------------------------------------------------------------

#?(:clj
   (def shader-schema
     "The shape of the map `compile-shader` takes. Malli rather than a
  hand-rolled check because this part is data validation, which is what
  malli is for; the type checking above is a different job and no schema
  library can do it."
     [:map {:closed true}
      [:precision {:optional true} [:maybe :string]]
      [:attributes {:optional true} [:vector :any]]
      [:uniforms {:optional true} [:vector :any]]
      [:varyings {:optional true} [:vector :any]]
      [:constants {:optional true} [:vector :any]]
      [:functions {:optional true} [:vector :any]]
      [:preamble {:optional true} [:maybe :string]]
      [:main [:vector :any]]]))

(defn- check-shape!
  [shader]
  #?(:clj
     (when-not (malli/validate shader-schema shader)
       (throw (ex-info (str "fedoras.glsl: this is not a shader.\n"
                            (pr-str (malli-error/humanize
                                     (malli/explain shader-schema shader))))
                       {:shader shader})))
     :cljs nil))

(defn compile-shader
  "A shader map, as GLSL.

  `:attributes`, `:uniforms` and `:varyings` are flat vectors of name
  and type. `:constants` is name, type and value. `:functions` is a
  vector of {:name :out :in :body}, in the order they are to appear,
  because GLSL wants a function defined before it is called and saying
  so in the order is honest. `:main` is the body of main.

  `:preamble` is an escape hatch for GLSL that has no business being
  compiled from anything, which in practice means the precision
  directive and its ifdef."
  [{:keys [precision preamble attributes uniforms varyings
           constants functions main]
    :as shader}]
  (check-shape! shader)
  (let [declared (named->environment attributes uniforms varyings
                                     (vec (mapcat (fn [[nm kind _]] [nm kind])
                                                  (partition 3 constants))))
        environment (assoc declared
                           :functions
                           (into {} (map (fn [{:keys [name out]}] [name out]))
                                 functions))
        function-text
        (map (fn [{:keys [name out in body]}]
               (let [inside (update environment :names into
                                    (map (fn [[nm kind]] [nm kind])
                                         (partition 2 in)))]
                 (str (glsl-name out) " " (identifier name) "("
                      (str/join ", " (map (fn [[nm kind]]
                                            (str (glsl-name kind) " "
                                                 (identifier nm)))
                                          (partition 2 in)))
                      ") {\n"
                      (str/join "\n" (->statements inside 1 body))
                      "\n}\n")))
             functions)]
    (str (when preamble (str preamble "\n"))
         (when precision (str "precision " precision ";\n"))
         (declare-block "attribute" attributes)
         (declare-block "uniform" uniforms)
         (declare-block "varying" varyings)
         (when (seq constants) "\n")
         (str/join (map (fn [[nm kind value]]
                          (str "const " (glsl-name kind) " " (identifier nm)
                               " = " (number->glsl value) ";\n"))
                        (partition 3 constants)))
         (when (seq functions) "\n")
         (str/join "\n" function-text)
         (when (seq functions) "\n")
         "void main() {\n"
         (str/join "\n" (->statements environment 1 main))
         "\n}\n")))