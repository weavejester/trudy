(ns trudy.effect
  "Visual effects that can be applied to elements."
  (:require [trudy.element :as element]))

(defrecord Blur [radius content]
  element/Sized
  (size [_ area] (element/size content area)))

(defn blur [& {:as options}]
  (map->Blur options))
