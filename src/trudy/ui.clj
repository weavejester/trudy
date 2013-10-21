(ns trudy.ui
  "Visible user interface components."
  (:require [trudy.macros :refer :all]))

(defrecord Text [content color]
  Object
  (toString [r] (str "#trudy.ui/text" (pr-str (into {} r)))))

(defrecord Rect [color]
  Object
  (toString [r] (str "#trudy.ui/rect" (pr-str (into {} r)))))

(set-print-methods! Text)
(set-print-methods! Rect)
