(ns trudy.effect
  "Visual effects that can be applied to elements."
  (:import [java.awt.image BufferedImage BufferedImageOp]
           [com.jhlabs.image BlurFilter])
  (:require [trudy.element :as element]
            [trudy.macros :refer (set-print-methods!)]))

(defprotocol Effect
  (apply-effect [effect image]))

(defrecord Blur [content]
  Effect
  (apply-effect [_ image] (.filter (BlurFilter.) image nil))
  element/Sized
  (size [_ area] (element/size (first content) area))
  Object
  (toString [r] (str "#trudy.effect/blur " (pr-str (vec content)))))

(defn blur [& elements]
  (Blur. (vec elements)))

(set-print-methods! Blur)
