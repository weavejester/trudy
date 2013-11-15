(ns trudy.layout
  (:require [trudy.element :as element]
            [trudy.size :as size]))

(defprotocol Layout
  (child-regions [layout parent-region]))

(defn- child-bounds [bounds children]
  (map #(element/size % bounds) children))

(defn- overlay-size [bounds children]
  (let [sizes (child-bounds bounds children)]
    [(size/overlay (map first sizes))
     (size/overlay (map second sizes))]))

(defrecord Overlay [content]
  Layout
  (child-regions [_ region]
    (for [child content] [region child]))
  element/Sized
  (size [_ bounds]
    (overlay-size bounds content)))

(defrecord Compact [content]
  Layout
  (child-regions [_ region]
    (for [child content] [region child]))
  element/Sized
  (size [_ bounds]
    (mapv size/min (overlay-size bounds content))))

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
  (size [_ bounds]
    (let [sizes (child-bounds bounds content)]
      [(size/overlay (map first sizes))
       (size/series (map second sizes))])))

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
    (overlay-size bounds content)))

(defn overlay [& elements]
  (Overlay. (vec elements)))

(defn compact [& elements]
  (Compact. (vec elements)))

(defn vbox [& elements]
  (VBox. (vec elements)))

(defn center [& elements]
  (Center. (vec elements)))
