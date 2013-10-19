(ns trudy.graphics
  (:require trudy.types)
  (:import java.awt.Graphics2D))

(defprotocol Renderable
  (render [entity ^Graphics2D graphics x y w h]))

(extend-type trudy.types.Text
  Renderable
  (render [text graphics x y w h]
    (.drawString graphics (:content text) x y)))
