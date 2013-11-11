(ns trudy.ui
  "Visible user interface components."
  (:require [trudy.element :as element]
            [trudy.font :as font]
            [trudy.macros :refer (set-print-methods!)]))

(defrecord Text [content color font]
  element/Sized
  (size [_ [w h]] (font/text-size content font w))
  Object
  (toString [r] (str "#trudy.ui/text " (pr-str (into {} r)))))

(defrecord Rect [color]
  element/Sized
  (size [_ area] area)
  Object
  (toString [r] (str "#trudy.ui/rect " (pr-str (into {} r)))))

(defn text [& {:as options}]
  (map->Text options))

(defn rect [& {:as options}]
  (map->Rect options))

(set-print-methods! Text)
(set-print-methods! Rect)
