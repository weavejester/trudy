(ns trudy.core
  (:import java.awt.Graphics2D)
  (:require [trudy.swing :as swing]))

(defn hello-world [^Graphics2D graphics]
  (.drawString graphics "Hello World" 10 10))

(defn -main [& args]
  (swing/run-app hello-world {:title "Demo" :size [200 200]}))
