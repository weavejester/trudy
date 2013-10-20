(ns trudy.layout
  (:require [trudy.macros :refer :all]))

(defprotocol Layout
  (child-regions [layout parent-region]))

(defcomponent v-box [content]
  Layout
  (child-regions [_ [x y w h]]
    (let [ch (/ h (count content))]
      (map-indexed
       (fn [i child] [[x (+ y (* i ch)) w ch] child])
       content))))
