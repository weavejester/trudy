(ns trudy.core
  (:import [javax.swing JFrame JPanel]
           [java.awt Graphics2D]))

(defn drawable-panel [painter]
  (proxy [JPanel] []
    (paintComponent [graphics]
      (proxy-super paintComponent graphics)
      (painter graphics))))

(defn frame [title [width height] painter]
  (doto (JFrame. title)
    (.setSize width height)
    (.add (drawable-panel painter))
    (.setVisible true)))

(defn draw-example [^Graphics2D graphics]
  (.drawString graphics "Hello World" 10 10))

(defn -main [& args]
  (frame "Demo" [200 200] draw-example))
