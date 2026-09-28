^{:kindly/hide-code true
  :clay {:quarto {:title "Plan of the Campus"}}}
(ns appendix.campus-map
  (:require [fedoras.screenplay :as sp]
            [fedoras.campus :as campus]))

^:kindly/hide-code
(sp/assets)

;; # Plan of the Campus

^:kindly/hide-code
(campus/render)

^:kindly/hide-code
(sp/scene
 (sp/action
  "Issued to every freshman. Folded once, lost by October."))