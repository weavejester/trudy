(ns trudy.effect
  "Visual effects that can be applied to elements."
  (:import [java.awt.image BufferedImage BufferedImageOp]
           [com.jhlabs.image BoxBlurFilter])
  (:require [trudy.element :as element]))

(defprotocol Effect
  (apply-effect [effect image]))

(defrecord Blur [radius content]
  Effect
  (apply-effect [blur image]
    (let [[rx ry] radius]
      (.filter (BoxBlurFilter. rx ry (:iterations blur 3)) image nil)))
  element/Sized
  (size [_ area] (element/size content area)))

(defn blur [& {:as options}]
  (map->Blur options))
