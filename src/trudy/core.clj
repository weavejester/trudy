(ns trudy.core
  (:import [java.awt Canvas Color Graphics2D RenderingHints])
  (:require [trudy.swing :as swing]))

(defn- frame-sleep-time [frame-start frame-length]
  (let [duration (- (System/currentTimeMillis) frame-start)]
    (max 0 (- frame-length duration))))

(defn- clear-canvas! [^Canvas canvas ^Graphics2D graphics]
  (let [w (.getWidth canvas)
        h (.getHeight canvas)]
    (.setColor graphics Color/white)
    (.fillRect graphics 0 0 w h)
    (.setColor graphics Color/black)))

(defn paint-canvas [canvas painter]
  (let [strategy (.getBufferStrategy canvas)
        graphics (.getDrawGraphics strategy)]
    (clear-canvas! canvas graphics)
    (painter graphics)
    (.dispose graphics)
    (.show strategy)))

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

(defn window [title size painter]
  (let [canvas (Canvas.)]
    (swing/window title size canvas)
    (doto canvas
      (.createBufferStrategy 2)
      (.setIgnoreRepaint true)
      (.requestFocus))
    (future
      (paint-loop canvas painter 60))))

(defn hello-world [^Graphics2D graphics]
  (.drawString graphics "Hello World" 10 10))

(defn -main [& args]
  (window "Demo" [200 200] hello-world))
