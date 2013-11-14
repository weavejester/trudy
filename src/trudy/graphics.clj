(ns trudy.graphics
  (:import [java.awt Font Graphics2D RenderingHints]
           [java.awt.font FontRenderContext TextLayout]
           [java.awt.image BufferedImage])
  (:require [crumpets.core :as color]
            [trudy.layout :as layout]
            [trudy.effect :as effect]
            [trudy.font :as font]
            trudy.ui))

(defprotocol Renderable
  (render [entity ^Graphics2D graphics region]))

(defprotocol Canvas
  (paint* [canvas painter]))

(defn buffered-image
  "Create a blank BufferedImage instance of the supplied dimentions."
  [[width height]]
  (BufferedImage. (int width) (int height) BufferedImage/TYPE_INT_ARGB))

(extend-type BufferedImage
  Canvas
  (paint* [image painter]
    (painter (.getGraphics image) [(.getWidth image) (.getHeight image)])))

(defn- set-font-hints! [^Graphics2D g]
  (.setRenderingHint g RenderingHints/KEY_TEXT_ANTIALIASING
                       RenderingHints/VALUE_TEXT_ANTIALIAS_ON))

(defn paint [canvas entity]
  (paint* canvas (fn [g [w h]]
                   (set-font-hints! g)
                   (render entity g [0 0 w h]))))

(defn- set-color [^Graphics2D graphics color]
  (.setColor graphics (color/awt-color color)))

(defn- draw-line [^Graphics2D graphics content font [x y]]
  (.setFont graphics (font/awt-font font))
  (.drawString graphics content (int x) (int y)))

(defn- draw-text [graphics content font [x0 y0 w _]]
  (let [lines     (font/split-text content font w)
        baselines (font/text-baselines content font w)]
    (doseq [[line y] (map vector lines baselines)]
      (draw-line graphics line font [x0 y]))))

(defn- draw-image [^Graphics2D graphics image [x y w h]]
  (.drawImage graphics image x y w h nil))

(defn- render-layout [layout graphics region]
  (doseq [[region child] (layout/child-regions layout region)]
    (render child graphics region)))

(defn- render-effect [effect graphics [x y w h]]
  (let [buffer (buffered-image [w h])]
    (paint buffer (:content effect))
    (draw-image graphics (effect/apply-effect effect buffer) [x y w h])))

(extend-protocol Renderable
  trudy.ui.Text
  (render [{:keys [content font color]} graphics region]
    (set-color graphics color)
    (draw-text graphics content font region))

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

  trudy.effect.Blur
  (render [effect graphics region]
    (render-effect effect graphics region)))
