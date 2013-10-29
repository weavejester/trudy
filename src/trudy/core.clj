(ns trudy.core
  (:import java.awt.Graphics2D)
  (:require [crumpets.core :as color]
            [trudy.swing :as swing]
            [trudy.ui :as ui]
            [trudy.layout :as layout]
            [trudy.graphics :as g]))

(defn painter [layout]
  (let [prev (atom nil)]
    (fn [graphics [w h]]
      (when (not= @prev [layout w h])
        (g/render layout graphics 0 0 w h)
        (reset! prev [layout w h])))))

(def hello-world
  #trudy.layout/vbox [
    #trudy.layout/overlay [
      #trudy.ui/rect {:color #color/rgb "#ff0000"}
      #trudy.ui/text {:color #color/rgb "#ffffff" :content "Hello World"}]
    #trudy.ui/rect {:color #color/rgb "#0000ff"}])

(defn -main [& args]
  (swing/run-app (painter hello-world)
                 {:title "Demo" :size [200 200]}))
