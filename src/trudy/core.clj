(ns trudy.core
  (:import javax.swing.JFrame))

(defn frame [title [width height]]
  (doto (JFrame. title)
    (.setSize width height)
    (.setVisible true)))

(defn -main [& args]
  (frame "Demo" [200 200]))
