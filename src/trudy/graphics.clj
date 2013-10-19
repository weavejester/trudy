(ns trudy.graphics
  (:import java.awt.Graphics2D)
  (:require [crumpets.core :as color]
            trudy.ui))

(defn- set-color [graphics color]
  (.setColor graphics (color/awt-color color)))

(defprotocol Renderable
  (render [entity ^Graphics2D graphics x y w h]))

(extend-protocol Renderable
  trudy.ui.Text
  (render [text graphics x y w h]
    (set-color graphics (:color text))
    (.drawString graphics (:content text) x y))

  trudy.ui.Rect
  (render [rect graphics x y w h]
    (set-color graphics (:color rect))
    (.fillRect graphics x y w h)))
