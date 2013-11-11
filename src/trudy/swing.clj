(ns trudy.swing
  "Create graphics window with Swing."
  (:require [trudy.graphics :as graphics])
  (:import [javax.swing JFrame JPanel]
           [java.awt Canvas Component Dimension]
           [java.awt.event ComponentListener]))

(defn- canvas-size [^Canvas canvas]
  [(.getWidth canvas) (.getHeight canvas)])

(extend-type Canvas
  graphics/Canvas
  (paint* [canvas painter]
    (let [strategy (.getBufferStrategy canvas)
          graphics (.getDrawGraphics strategy)]
      (painter graphics (canvas-size canvas))
      (.dispose graphics)
      (.show strategy))))

(defn- frame [title [width height] canvas]
  (let [frame (JFrame. title)
        panel (.getContentPane frame)]
    (.setPreferredSize panel (Dimension. width height))
    (.add panel canvas)
    (.pack frame)
    (.setVisible frame true)))

(defn window-canvas [{:keys [title size]}]
  (let [canvas (Canvas.)]
    (frame title size canvas)
    (doto canvas
      (.createBufferStrategy 2)
      (.setIgnoreRepaint true)
      (.requestFocus))))

(defn on-resize [^Component component callback]
  (.addComponentListener
   component
   (reify ComponentListener
     (componentHidden [_ _])
     (componentShown [_ _])
     (componentMoved [_ _])
     (componentResized [_ _] (callback)))))
