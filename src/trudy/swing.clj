(ns trudy.swing
  "Create graphics window with Swing."
  (:import [javax.swing JFrame JPanel]))

(defn- drawable-panel [painter]
  (proxy [JPanel] []
    (paintComponent [graphics]
      (proxy-super paintComponent graphics)
      (painter graphics))))

(defn frame [title [width height] painter]
  (doto (JFrame. title)
    (.setSize width height)
    (.add (drawable-panel painter))
    (.setVisible true)))
