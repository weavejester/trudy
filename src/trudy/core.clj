(ns trudy.core
  (:import [java.awt Canvas Graphics2D])
  (:require [trudy.swing :as swing]))

(defn- frame-sleep-time [frame-start frame-length]
  (let [duration (- (System/currentTimeMillis) frame-start)]
    (max 0 (- frame-length duration))))

(defn paint-canvas [canvas painter]
  (let [strategy (.getBufferStrategy canvas)
        graphics (.getDrawGraphics strategy)]
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
