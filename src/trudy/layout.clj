(ns trudy.layout
  (:require [trudy.element :as element]
            [trudy.macros :refer (set-print-methods!)]))

(defprotocol Layout
  (child-regions [layout parent-region]))

(defrecord Overlay [content]
  Layout
  (child-regions [_ region]
    (for [child content] [region child]))
  element/Sized
  (size [_ area]
    (let [sizes (map #(element/size % area) content)]
      [(apply max (map first sizes))
       (apply max (map second sizes))]))
  Object
  (toString [_] (str "#trudy.layout/overlay " (pr-str (vec content)))))

(defn- minima [f start end]
  (if (= start end)
    start
    (let [step (int (/ (- end start) 2))]
      (if (< (f start) (f end))
        (recur f start (+ start step))
        (recur f (- end step) end)))))

(defn- box-optimum [elements sizef target]
  (letfn [(total-size [x]  (apply + (map #(sizef % x) elements)))
          (overflow [size] (Math/abs (- target size)))]
    (minima (comp overflow total-size) 0 target)))

(defrecord VBox [content]
  Layout
  (child-regions [_ [x y w h]]
    (let [sizef   #(second (element/size %1 [w %2]))
          optimum (box-optimum content sizef h)
          heights (map #(sizef % optimum) content)
          offsets (reductions + y heights)]
      (map (fn [child y h] [[x y w h] child])
           content
           offsets
           heights)))
  element/Sized
  (size [_ area] area)
  Object
  (toString [_] (str "#trudy.layout/vbox " (pr-str (vec content)))))

(defn- center-child [[cw ch] [x y w h]]
  [(int (+ x (- (/ w 2) (/ cw 2))))
   (int (+ y (- (/ h 2) (/ ch 2))))])

(defrecord Center [content]
  Layout
  (child-regions [_ [x y w h]]
    (for [child content]
      (let [[cw ch] (element/size child [w h])
            [cx cy] (center-child [cw ch] [x y w h])]
        [[cx cy cw ch] child])))
  element/Sized
  (size [_ area] area)
  Object
  (toString [_] (str "#trudy.layout/center " (pr-str (vec content)))))

(defn overlay [& elements]
  (Overlay. (vec elements)))

(defn vbox [& elements]
  (VBox. (vec elements)))

(defn center [& elements]
  (Center. (vec elements)))

(set-print-methods! Overlay)
(set-print-methods! VBox)
(set-print-methods! Center)
