(ns trudy.layout
  (:require [trudy.element :as element]
            [trudy.macros :refer (set-print-methods!)]
            [medley.core :refer (greatest)]))

(defprotocol Layout
  (child-regions [layout parent-region]))

(defrecord Overlay [content]
  Layout
  (child-regions [_ region]
    (for [child content] [region child]))
  element/Sized
  (size [_ area]
    (greatest (map #(element/size % area) content)))
  Object
  (toString [_] (str "#trudy.layout/overlay " (pr-str (vec content)))))

(defrecord VBox [content]
  Layout
  (child-regions [_ [x y w h]]
    (let [ch (int (/ h (count content)))]
      (map-indexed
       (fn [i child] [[x (+ y (* i ch)) w ch] child])
       content)))
  element/Sized
  (size [_ area] area)
  Object
  (toString [_] (str "#trudy.layout/vbox " (pr-str (vec content)))))

(defrecord Center [content]
  Layout
  (child-regions [_ [x y w h]]
    (for [child content]
      (let [[cw ch] (element/size child [w h])
            cx      (int (+ x (- (/ w 2) (/ cw 2))))
            cy      (int (+ y (- (/ h 2) (/ ch 2))))]
        [[cx cy cw ch] child])))
  element/Sized
  (size [_ area] area)
  Object
  (toString [_] (str "#trudy.layout/center " (pr-str (vec content)))))

(set-print-methods! Overlay)
(set-print-methods! VBox)
(set-print-methods! Center)
