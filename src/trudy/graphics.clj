(ns trudy.graphics
  (:import java.awt.Graphics2D)
  (:require [crumpets.core :as color]
            [trudy.layout :as layout]
            trudy.ui))

(defprotocol Renderable
  (render [entity ^Graphics2D graphics x y w h]))

(defn- set-color [graphics color]
  (.setColor graphics (color/awt-color color)))

(extend-protocol Renderable
  trudy.ui.Text
  (render [text graphics x y w h]
    (set-color graphics (:color text))
    (.drawString graphics (:content text) x (+ y 10)))

  trudy.ui.Rect
  (render [rect graphics x y w h]
    (set-color graphics (:color rect))
    (.fillRect graphics x y w h))

  trudy.layout.Overlay
  (render [overlay graphics x y w h]
    (doseq [[[x y w h] child] (layout/child-regions overlay [x y w h])]
      (render child graphics x y w h)))

  trudy.layout.VBox
  (render [v-box graphics x y w h]
    (doseq [[[x y w h] child] (layout/child-regions v-box [x y w h])]
      (render child graphics x y w h))))
