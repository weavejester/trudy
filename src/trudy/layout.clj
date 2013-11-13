(ns trudy.layout
  (:require [trudy.element :as element]
            [trudy.size :as size]
            [trudy.macros :refer (set-print-methods!)]))

(defprotocol Layout
  (child-regions [layout parent-region]))

(defn- child-bounds [bounds children]
  (map #(element/size % bounds) children))

(defn- min-fit [bounds]
  (size/min (size/fit bounds)))

(defn- overlay-size [bounds children]
  (let [sizes (child-bounds bounds children)]
    [(min-fit (map first sizes))
     (min-fit (map second sizes))]))

(defrecord Overlay [content]
  Layout
  (child-regions [_ region]
    (for [child content] [region child]))
  element/Sized
  (size [_ bounds]
    (overlay-size bounds content))
  Object
  (toString [_] (str "#trudy.layout/overlay " (pr-str (vec content)))))

(defn- vbox-heights [children w h]
  (let [bounds [w (size/range 1 h)]
        sizes  (child-bounds bounds children)]
    (size/pack (map second sizes) h)))

(defrecord VBox [content]
  Layout
  (child-regions [_ [x y w h]]
    (let [heights (vbox-heights content w h)
          offsets (reductions + 0 heights)]
      (map (fn [c h y] [[x y w h] c])
           content
           heights
           offsets)))
  element/Sized
  (size [_ bounds] bounds)
  Object
  (toString [_] (str "#trudy.layout/vbox " (pr-str (vec content)))))

(defn- center-child [[cw ch] [x y w h]]
  [(int (+ x (- (/ w 2) (/ cw 2))))
   (int (+ y (- (/ h 2) (/ ch 2))))])

(defrecord Center [content]
  Layout
  (child-regions [_ [x y w h]]
    (for [child content]
      (let [[cw ch] (map size/min (element/size child [w h]))
            [cx cy] (center-child [cw ch] [x y w h])]
        [[cx cy cw ch] child])))
  element/Sized
  (size [_ bounds]
    (overlay-size bounds content))
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
