(ns trudy.effect
  "Visual effects that can be applied to elements."
  (:require [trudy.element :as element]))

(defrecord Blur [radius content]
  element/Sized
  (size [_ bounds] (element/size content bounds)))

(defrecord Scale [content]
  element/Sized
  (size [_ bounds] bounds))

(defn blur [& {:as options}]
  (map->Blur options))

(defn scale [content]
  (Scale. content))
