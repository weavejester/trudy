(ns trudy.effect
  "Visual effects that can be applied to elements."
  (:import [java.awt.image BufferedImage BufferedImageOp]
           [com.jhlabs.image BoxBlurFilter])
  (:require [trudy.element :as element]
            [trudy.macros :refer (set-print-methods!)]
            [medley.core :refer (mapply)]))

(defprotocol Effect
  (apply-effect [effect image]))

(defrecord Blur [radius content]
  Effect
  (apply-effect [blur image]
    (let [[rx ry] radius]
      (.filter (BoxBlurFilter. rx ry (:iterations blur 3)) image nil)))
  element/Sized
  (size [_ area] (element/size content area))
  Object
  (toString [r] (str "#trudy.effect/blur " (pr-str (into {} r)))))

(defn blur [& {:as options}]
  (map->Blur options))

(set-print-methods! Blur)
