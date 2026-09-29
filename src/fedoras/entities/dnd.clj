;; =====================================================================
;; DND.CLJ — the cell, plus the two patches it needs.
;;
;; The first section is a file: src/fedoras/entities/dnd.clj.
;; The two below it are replacement functions for files that already
;; exist, marked with where they go. Nothing here is a fragment; each is
;; the whole function as it should end up.
;; =====================================================================

;; ---------------------------------------------------------------------
;; src/fedoras/entities/dnd.clj
;; ---------------------------------------------------------------------

(ns fedoras.entities.dnd
  (:require [fedoras.sketch :as sketch]
            [fedoras.state :as state]))

(defn cell
  "Session Four, the live half. Five entities and a reader.

  `fedoras/ui/dice.cljs` is sourced here rather than left to the
  chapter's `(dice/assets)`, because the cell calls into it for the roll
  arithmetic and must not depend on a sibling cell having been placed
  first. `state/source` is guarded per chapter, so asking twice costs
  nothing."
  [{:keys [id label rows] :or {id "dnd" rows 45}}]
  [:div
   (state/load-spine "fedoras/entities/spine.edn")
   (state/load-entities ["fedoras/entities/dnd.edn"])
   (state/source "fedoras/llm.cljs")
   (state/source "fedoras/ui/dice.cljs")
   (sketch/page
    {:id id :label label :rows rows}
    (state/resource-text "fedoras/cells/dnd.cljs"))])