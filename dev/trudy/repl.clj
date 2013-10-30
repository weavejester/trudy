(ns trudy.repl
  (:require [crumpets.core :as color]
            [trudy.swing :as swing]
            [trudy.ui :as ui]
            [trudy.layout :as layout]
            [trudy.graphics :as g]))

(def hello-world
  #trudy.layout/vbox [
    #trudy.layout/overlay [
      #trudy.ui/rect {:color #color/rgb "#ff0000"}
      #trudy.ui/text {:color #color/rgb "#ffffff"
                      :font {:family "Times" :size 26 :style :bold}
                      :content "Hello World"}]
    #trudy.ui/rect {:color #color/rgb "#0000ff"}])

(defn show [entity]
  (let [canvas (swing/window-canvas {:title "REPL" :size [400 400]})]
    (Thread/sleep 50)
    (g/paint canvas entity)))
