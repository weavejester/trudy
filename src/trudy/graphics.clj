(ns trudy.graphics
  (:import [java.awt Font Graphics2D]
           [java.awt.font FontRenderContext TextLayout])
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

(defn- font
  [{:keys [family style size] :or {family "SansSerif", style :plain, size 10}}]
  (Font. family (font-styles style) size))

(defn- font-render-context [^Font font]
  (FontRenderContext. (.getTransform font) (boolean true) (boolean true)))

(defn- text-size [^String content ^Font font]
  (let [layout (TextLayout. content font (font-render-context font))
        bounds (.getBounds layout)]
    [(int (.getWidth bounds)) (int (.getHeight bounds))]))

(defn- draw-text [^Graphics2D graphics content font [x y]]
  (let [[w h] (text-size content font)]
    (.setFont graphics font)
    (.drawString graphics content x (+ y h))))

(extend-protocol Renderable
  trudy.ui.Text
  (render [text graphics x y w h]
    (set-color graphics (:color text))
    (draw-text graphics (:content text) (font (:font text)) [x y]))

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
