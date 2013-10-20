(ns trudy.ui
  "Visible user interface components."
  (:require [trudy.macros :refer :all]))

(defcomponent text [content color])
(defcomponent rect [color])

(set-print-methods! Text)
(set-print-methods! Rect)
