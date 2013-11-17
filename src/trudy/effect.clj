(ns trudy.effect
  "Visual effects that can be applied to elements."
  (:require [trudy.element :as element]))

(defrecord Blur [content radius]
  element/Sized
  (size [_ bounds] (element/size content bounds)))

(defrecord Scale [content]
  element/Sized
  (size [_ bounds] bounds))

(defn blur [content & {:as options}]
  (map->Blur (assoc options :content content)))

(defn scale [content]
  (Scale. content))
