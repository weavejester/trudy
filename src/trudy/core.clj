(ns trudy.core
  (:import [java.awt Canvas Graphics2D])
  (:require [trudy.swing :as swing]))

(defn -main [& args]
  (let [canvas (Canvas.)]
    (swing/window "Demo" [200 200] canvas)
    (let [strategy (.getBufferStrategy canvas)
          graphics (.getDrawGraphics strategy)]
      (.drawString graphics "Hello World" 10 10)
      (.show strategy))))
