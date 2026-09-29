;; ---------------------------------------------------------------------
;; CAMPUS_SETTINGS.CLJS — the map.
;;
;; Not a copy of the map. The map. This is what the widget walks, and
;; editing a line here and pressing Run changes what is joined to what.
;;
;; ADD A DOOR SOMEWHERE THERE ISN'T ONE and you can go and stand in it.
;; There is nothing in this program that will stop you and nothing that
;; will comment on it, and what you find when you get there is a name.
;; ---------------------------------------------------------------------

{:start :whitlock

 :plan
 {;; the middle of it
  :quad     {:exits {:north :prentiss :south :whitlock :east :meridian
                     :west :beckwith :in :fountain}}
  :fountain {:exits {:out :quad}}

  ;; north
  :prentiss {:exits {:south :quad :east :sterling}}
  :sterling {:exits {:west :prentiss :south :meridian}}
  :chapel   {:exits {}}

  ;; west
  :beckwith {:exits {:east :quad :south :library}}
  :library  {:exits {:north :beckwith :south :plant}}
  :plant    {:exits {:north :library}}

  ;; east
  :meridian {:exits {:west :quad :north :sterling :east :dorm-n :down :basement}}
  :annex    {:exits {:west :meridian}}
  :dorm-n   {:exits {:west :meridian :south :dorm-s}}
  :dorm-s   {:exits {:north :dorm-n :south :dorm-e}}
  :dorm-e   {:exits {:north :dorm-s :south :field-h}}
  :field-h  {:exits {:north :dorm-e}}
  :b14      {:exits {}}

  ;; south
  :whitlock {:exits {:north :quad :east :union}}
  :union    {:exits {:west :whitlock :east :commons}}
  :commons  {:exits {:west :union :north :meridian :south :corliss}}
  :corliss  {:exits {:north :commons}}

  ;; under
  :basement {:exits {:up :meridian}}

  ;; ground
  :fill     {:exits {}}}

 ;; ---------------------------------------------------------------
 ;; What each of them is called when you are standing in it. Rooms
 ;; missing from here are shown by their key, which is what happens to
 ;; anything anybody adds.
 ;; ---------------------------------------------------------------
 :names
 {:quad     "THE QUAD"
  :fountain "THE FOUNTAIN"
  :prentiss "PRENTISS SCIENCE CENTER"
  :sterling "STERLING CENTER"
  :chapel   "THE OLD CHAPEL"
  :beckwith "BECKWITH HALL"
  :library  "OAKES LIBRARY"
  :plant    "PLANT SERVICES"
  :meridian "MERIDIAN HALL"
  :annex    "MERIDIAN ANNEX"
  :dorm-n   "NORTH HOUSE"
  :dorm-s   "SOUTH HOUSE"
  :dorm-e   "ASHCROFT HOUSE"
  :field-h  "ASHCROFT FIELD HOUSE"
  :b14      "BUILDING 14"
  :whitlock "WHITLOCK HALL"
  :union    "HALLORAN UNION"
  :commons  "THE COMMONS"
  :corliss  "CORLISS FIELD"
  :basement "UNDER MERIDIAN HALL"
  :fill     "THE FILL"}}

;; ---------------------------------------------------------------------
;; FOUR ROOMS HAVE NOTHING LEADING TO THEM.
;;
;; The button says which. It does not work them out from a list — there
;; is no list — it searches out from where you started and reports what
;; the search did not touch.
;;
;; Three of the four say why on the plan. The chapel is closed. The
;; annex has no public entrance. The Fill is a piece of ground with a
;; sign on it.
;;
;; The annex has an exit, incidentally. You could leave it.
;; ---------------------------------------------------------------------