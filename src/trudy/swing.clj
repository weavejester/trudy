(ns trudy.swing
  "Create graphics window with Swing."
  (:import [javax.swing JFrame JPanel]
           [java.awt Dimension]))

(defn window [title [width height] canvas]
  (let [frame (JFrame. title)
        panel (.getContentPane frame)]
    (.setPreferredSize panel (Dimension. width height))
    (.add panel canvas)
    (.pack frame)
    (.createBufferStrategy canvas 2)
    (.setIgnoreRepaint canvas true)
    (.requestFocus canvas)
    (.setVisible frame true)))
