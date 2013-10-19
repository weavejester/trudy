(ns trudy.core
  (:import java.awt.Graphics2D)
  (:require [crumpets.core :as color]
            [trudy.swing :as swing]
            [trudy.types :as t]
            [trudy.graphics :as g]))

(defn painter [layout]
  (fn [graphics [w h]]
    (g/render layout graphics 0 0 w h)))

(def hello-world
  #trudy/rect {:color #color/rgb "#ff0000"})

(defn -main [& args]
  (swing/run-app (painter hello-world)
                 {:title "Demo" :size [200 200]}))
