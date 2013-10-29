(ns trudy.swing
  "Create graphics window with Swing."
  (:import [javax.swing JFrame JPanel]
           [java.awt Dimension Canvas Color Graphics2D RenderingHints]))

(defn canvas-size [^Canvas canvas]
  [(.getWidth canvas) (.getHeight canvas)])

(defn- set-font-hints! [^Graphics2D g]
  (.setRenderingHint g RenderingHints/KEY_TEXT_ANTIALIASING
                       RenderingHints/VALUE_TEXT_ANTIALIAS_GASP))

(defn paint-canvas [canvas painter]
  (let [strategy (.getBufferStrategy canvas)
        graphics (.getDrawGraphics strategy)]
    (set-font-hints! graphics)
    (painter graphics (canvas-size canvas))
    (.dispose graphics)
    (.show strategy)))

(defn- frame-sleep-time [frame-start frame-length]
  (let [duration (- (System/currentTimeMillis) frame-start)]
    (max 0 (- frame-length duration))))

(defn paint-loop
  ([canvas painter]
     (paint-loop canvas painter 60))
  ([canvas painter fps]
     (let [frame-length (/ 1000.0 fps)]
       (loop [time-prev nil]
         (let [time-start (System/currentTimeMillis)]
           (paint-canvas canvas painter)
           (Thread/sleep (frame-sleep-time time-start frame-length))
           (recur time-start))))))

(defn frame [title [width height] canvas]
  (let [frame (JFrame. title)
        panel (.getContentPane frame)]
    (.setPreferredSize panel (Dimension. width height))
    (.add panel canvas)
    (.pack frame)
    (.setVisible frame true)))

(defn run-app [painter {:keys [title size]}]
  (let [canvas (Canvas.)]
    (frame title size canvas)
    (doto canvas
      (.createBufferStrategy 2)
      (.setIgnoreRepaint true)
      (.requestFocus))
    (future
      (try
        (paint-loop canvas painter 60)
        (catch Exception ex
          (prn ex)
          (.printStackTrace ex))))))
