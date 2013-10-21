(ns trudy.layout
  (:require [trudy.macros :refer :all]))

(defprotocol Layout
  (child-regions [layout parent-region]))

(defrecord VBox [content]
  Layout
  (child-regions [_ [x y w h]]
    (let [ch (int (/ h (count content)))]
      (map-indexed
       (fn [i child] [[x (+ y (* i ch)) w ch] child])
       content)))
  Object
  (toString [_] (str "#trudy.layout/vbox " (vec content))))

(set-print-methods! VBox)
