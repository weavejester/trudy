(ns trudy.core
  (:import [java.awt Canvas Graphics2D])
  (:require [trudy.swing :as swing]))

(defn window [title size]
  (let [canvas (Canvas.)]
    (swing/window title size canvas)
    (doto canvas
      (.createBufferStrategy 2)
      (.setIgnoreRepaint true)
      (.requestFocus))
    (let [strategy (.getBufferStrategy canvas)
          graphics (.getDrawGraphics strategy)]
      (.drawString graphics "Hello World" 10 10)
      (.dispose graphics)
      (.show strategy))))

(defn -main [& args]
  (window "Demo" [200 200]))
