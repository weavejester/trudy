(ns trudy.core
  (:import java.awt.Graphics2D)
  (:require [crumpets.core :as color]
            [trudy.swing :as swing]
            [trudy.types :as t]
            [trudy.graphics :as g]))

(defn hello-world [^Graphics2D graphics]
  (g/render #trudy/text {:content "Hello World" :color #color/rgb "#ff0000"}
            graphics 10 10 100 40))

(defn -main [& args]
  (swing/run-app hello-world {:title "Demo" :size [200 200]}))
