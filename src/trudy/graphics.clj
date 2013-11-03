(ns trudy.graphics
  (:import [java.awt Font Graphics2D]
           [java.awt.font FontRenderContext TextLayout])
  (:require [crumpets.core :as color]
            [trudy.layout :as layout]
            [trudy.font :as font]
            trudy.ui))

(defprotocol Renderable
  (render [entity ^Graphics2D graphics region]))

(defprotocol Canvas
  (paint* [canvas painter]))

(defn paint [canvas entity]
  (paint* canvas (fn [g [w h]] (render entity g [0 0 w h]))))

(defn- set-color [^Graphics2D graphics color]
  (.setColor graphics (color/awt-color color)))

(defn- draw-text [^Graphics2D graphics content font [x y]]
  (let [[w h] (font/text-size content font)]
    (.setFont graphics (font/awt-font font))
    (.drawString graphics content x (+ y h))))

(defn- render-layout [layout graphics region]
  (doseq [[region child] (layout/child-regions layout region)]
    (render child graphics region)))

(extend-protocol Renderable
  trudy.ui.Text
  (render [text graphics [x y _ _]]
    (set-color graphics (:color text))
    (draw-text graphics (:content text) (:font text) [x y]))

  trudy.ui.Rect
  (render [rect graphics [x y w h]]
    (set-color graphics (:color rect))
    (.fillRect graphics x y w h))

  trudy.layout.Overlay
  (render [layout graphics region]
    (render-layout layout graphics region))

  trudy.layout.VBox
  (render [layout graphics region]
    (render-layout layout graphics region))

  trudy.layout.Center
  (render [layout graphics region]
    (render-layout layout graphics region))

  trudy.layout.Inline
  (render [layout graphics region]
    (render-layout layout graphics region)))
