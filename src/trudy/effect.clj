(ns trudy.effect
  "Visual effects that can be applied to elements."
  (:require [trudy.element :as element]
            [crumpets.core :as color]))

(defrecord Blur [content radius]
  element/Sized
  (size [_ bounds] (element/size content bounds)))

(defrecord Scale [content]
  element/Sized
  (size [_ bounds] bounds))

(defrecord Mask [content mask]
  element/Sized
  (size [_ bounds] (element/size content bounds)))

(defn blur [content & {:as options}]
  (map->Blur (assoc options :content content)))

(defn scale [content]
  (Scale. content))

(defn mask
  ([content] (mask content (color/rgba 0 0 0 255)))
  ([content mask-color] (Mask. content mask-color)))
