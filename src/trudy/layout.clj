(ns trudy.layout
  (:require [trudy.macros :refer :all]))

(defprotocol Layout
  (child-regions [layout parent-region]))

(defrecord Overlay [content]
  Layout
  (child-regions [_ region]
    (for [child content] [region child]))
  Object
  (toString [_] (str "#trudy.layout/overlay " (pr-str (vec content)))))

(defrecord VBox [content]
  Layout
  (child-regions [_ [x y w h]]
    (let [ch (int (/ h (count content)))]
      (map-indexed
       (fn [i child] [[x (+ y (* i ch)) w ch] child])
       content)))
  Object
  (toString [_] (str "#trudy.layout/vbox " (pr-str (vec content)))))

(set-print-methods! Overlay)
(set-print-methods! VBox)
