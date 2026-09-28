^{:kindly/hide-code true
  :clay {:quarto {:title "Fedoras"}}}
(ns index
  (:require [fedoras.screenplay :as sp]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(sp/assets)

;; # Title {#title-heading .sidebar-title}

^:kindly/hide-code
(kind/hiccup
 [:div.title-page
  [:h1.title "FEDORAS"]
  [:p.subtitle "Book One"]])

^:kindly/hide-code
(sp/name-field)
