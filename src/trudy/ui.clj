(ns trudy.ui
  "Visible user interface components."
  (:require [trudy.element :as element]
            [trudy.font :as font]
            [trudy.size :as size]
            [trudy.image :as img]))

(defrecord Text [content color font]
  element/Sized
  (size [_ [w _]]
    (font/text-size content font (size/max w))))

(defrecord Image [src]
  element/Sized
  (size [image _]
    (let [i (img/read-image src)]
      [(.getWidth i) (.getHeight i)])))

(defrecord Rect [color]
  element/Sized
  (size [_ bounds] bounds))

(defn text [& {:as options}]
  (map->Text options))

(defn image [& {:as options}]
  (map->Image options))

(defn rect [& {:as options}]
  (map->Rect options))
