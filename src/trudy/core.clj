(ns trudy.core
  (:import java.awt.Graphics2D)
  (:require [trudy.swing :as swing]
            [trudy.types :as t]
            [trudy.graphics :as g]))

(defn hello-world [^Graphics2D graphics]
  (g/render (t/->Text "Hello World") graphics 10 10 100 40))

(defn -main [& args]
  (swing/run-app hello-world {:title "Demo" :size [200 200]}))
