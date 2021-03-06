(ns trudy.image
  "Functions for loading and creating images."
  (:require [clojure.java.io :as io])
  (:import [javax.imageio ImageIO]
           [java.awt.image BufferedImage]))

(defn ^BufferedImage buffered-image
  "Create a blank BufferedImage instance of the supplied dimentions."
  [[width height]]
  (BufferedImage. (int width) (int height) BufferedImage/TYPE_INT_ARGB))

(defn size
  "Return the width and height of an image."
  [^BufferedImage image]
  [(.getWidth image) (.getHeight image)])

(def ^BufferedImage read-image
  "Read an image from a source URL or file. Returns a BufferedImage instance."
  (memoize
   (fn [source]
     (ImageIO/read (io/input-stream source)))))
