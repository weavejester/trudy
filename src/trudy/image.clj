(ns trudy.image
  "Render Trudy elements to a BufferedImage."
  (:import [java.awt.image BufferedImage])
  (:require [trudy.graphics :as graphics]))

(defn buffered-image
  "Create a blank BufferedImage instance of the supplied dimentions."
  [[width height]]
  (BufferedImage. (int width) (int height) BufferedImage/TYPE_INT_ARGB))

(extend-type BufferedImage
  graphics/Canvas
  (paint* [image painter]
    (painter (.getGraphics image) [(.getWidth image) (.getHeight image)])))
