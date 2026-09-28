^{:kindly/hide-code true
  :clay {:quarto {:title "Appendix A — The Fedora Handbook"}}}
(ns appendix.fedora-handbook
  (:require [fedoras.screenplay :as sp]
            [fedoras.handbook :as hb]))

^:kindly/hide-code
(sp/assets)

;; # Appendix A — The Fedora Handbook

;; *Unpublished. Self-edited. Distributed as a stapled document with a
;; cover page. The cover page has a seal. The seal is a fedora.*

;; The numbering is Victor's, which is to say it has gaps he refuses to
;; explain.

^:kindly/hide-code
(hb/all)

;; ---

;; ## Drafting apparatus

;; The rules live in `src/fedoras/handbook.clj` as a map from number to
;; text. Scene files call `(hb/epigraph n)`, this appendix calls
;; `(hb/all)`:

(hb/unused)

(hb/summary)