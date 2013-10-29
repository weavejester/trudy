(ns trudy.core
  (:import java.awt.Graphics2D)
  (:require [crumpets.core :as color]
            [trudy.swing :as swing]
            [trudy.ui :as ui]
            [trudy.layout :as layout]
            [trudy.graphics :as g]))

(def hello-world
  #trudy.layout/vbox [
    #trudy.layout/overlay [
      #trudy.ui/rect {:color #color/rgb "#ff0000"}
      #trudy.ui/text {:color #color/rgb "#ffffff" :content "Hello World"}]
    #trudy.ui/rect {:color #color/rgb "#0000ff"}])

(defn -main [& args]
  (let [canvas (swing/window-canvas {:title "Demo" :size [200 200]})]
    (g/paint canvas hello-world)))
