(ns trudy.ui
  "Visible user interface components."
  (:require [trudy.element :as element]
            [trudy.font :as font]
            [trudy.size :as size]
            [trudy.macros :refer (set-print-methods!)]))

(defrecord Text [content color font]
  element/Sized
  (size [_ [w _]] (font/text-size content font (size/max w)))
  Object
  (toString [r] (str "#trudy.ui/text " (pr-str (into {} r)))))

(defrecord Rect [color]
  element/Sized
  (size [_ bounds] bounds)
  Object
  (toString [r] (str "#trudy.ui/rect " (pr-str (into {} r)))))

(defn text [& {:as options}]
  (map->Text options))

(defn rect [& {:as options}]
  (map->Rect options))

(set-print-methods! Text)
(set-print-methods! Rect)
