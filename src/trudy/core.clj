(ns trudy.core
  (:import java.awt.Graphics2D)
  (:require [crumpets.core :as color]
            [trudy.swing :as swing]
            [trudy.ui :as ui]
            [trudy.layout :as layout]
            [trudy.graphics :as g]))

(defn painter [layout]
  (fn [graphics [w h]]
    (g/render layout graphics 0 0 w h)))

(def hello-world
  #trudy.layout/overlay [
    #trudy.ui/rect {:color #color/rgb "#ff0000"}
    #trudy.ui/text {:color #color/rgb "#ffffff" :content "Hello World"}])

(defn -main [& args]
  (swing/run-app (painter hello-world)
                 {:title "Demo" :size [200 200]}))
