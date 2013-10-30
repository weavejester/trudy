(ns trudy.graphics
  (:import [java.awt Font Graphics2D])
  (:require [crumpets.core :as color]
            [trudy.layout :as layout]
            trudy.ui))

(defprotocol Renderable
  (render [entity ^Graphics2D graphics x y w h]))

(defprotocol Canvas
  (paint* [canvas painter]))

(defn paint [canvas entity]
  (paint* canvas (fn [g [w h]] (render entity g 0 0 w h))))

(defn- set-color [^Graphics2D graphics color]
  (.setColor graphics (color/awt-color color)))

(def ^:private font-styles
  {:plain  Font/PLAIN
   :bold   Font/BOLD
   :italic Font/ITALIC})

(defn- font [{:keys [family style size]
              :or   {family "SansSerif", style :plain, size 10}}]
  (Font. family (font-styles style) size))

(defn- set-font [^Graphics2D graphics font-attrs]
  (.setFont graphics (font font-attrs)))

(extend-protocol Renderable
  trudy.ui.Text
  (render [text graphics x y w h]
    (set-color graphics (:color text))
    (set-font graphics (:font text))
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
