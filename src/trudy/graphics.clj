(ns trudy.graphics
  (:import java.awt.Graphics2D)
  (:require [crumpets.core :as color]
            trudy.types))

(defprotocol Renderable
  (render [entity ^Graphics2D graphics x y w h]))

(extend-type trudy.types.Text
  Renderable
  (render [text graphics x y w h]
    (.setColor graphics (color/awt-color (:color text)))
    (.drawString graphics (:content text) x y)))
