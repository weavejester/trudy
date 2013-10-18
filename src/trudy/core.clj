(ns trudy.core
  (:import [java.awt Graphics2D])
  (:require [trudy.swing :as swing]))

(defn draw-example [^Graphics2D graphics]
  (.drawString graphics "Hello World" 10 10))

(defn -main [& args]
  (swing/frame "Demo" [200 200] draw-example))
