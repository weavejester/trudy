(ns trudy.layout
  (:require [trudy.element :as element]
            [trudy.macros :refer (set-print-methods!)]
            [medley.core :refer (greatest)]))

(defprotocol Layout
  (child-regions [layout parent-region]))

(defrecord Overlay [content]
  Layout
  (child-regions [_ region]
    (for [child content] [region child]))
  element/Sized
  (size [_ area]
    (greatest (map #(element/size % area) content)))
  Object
  (toString [_] (str "#trudy.layout/overlay " (pr-str (vec content)))))

(defrecord VBox [content]
  Layout
  (child-regions [_ [x y w h]]
    (let [ch (/ h (count content))]
      (map-indexed (fn [i child] [[x (+ y (int (* i ch))) w ch] child])
                   content)))
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

(defn- line-regions [elements]
  (let [sizes  (map #(element/size % [1 1]) elements)
        widths (map first sizes)]
    (map (fn [[w h] x e] [[x 0 w h] e])
         sizes
         (reductions + 0 widths)
         elements)))

(defn- move-regions [[dx dy] regions]
  (for [[[x y w h] e] regions]
    [[(+ x dx) (+ y dy) w h] e]))

(defn- wrap-regions [width regions]
  (if (seq regions)
    (let [[head tail]  (split-with (fn [[[x _ w _] _]] (< (+ x w) width)) regions)
          height       (reduce max 0 (map (fn [[[_ _ _ h] _]] h) head))
          next-regions (move-regions [(first tail) height] tail)]
      (lazy-seq
       (concat head (wrap-regions width next-regions))))))

(defrecord Inline [content]
  Layout
  (child-regions [_ [x y w h]]
    (->> (line-regions content)
         (wrap-regions w)
         (move-regions [x y])))
  element/Sized
  (size [layout [w h]]
    (let [regions     (map first (child-regions layout [0 0 w h]))
          [cx _ cw _] (last (sort-by first regions))
          [_ cy _ ch] (last (sort-by second regions))]
      [(+ cx cw) (+ cy ch)]))
  Object
  (toString [_] (str "#trudy.layout/inline " (pr-str (vec content)))))

(defn overlay [& elements]
  (Overlay. (vec elements)))

(defn vbox [& elements]
  (VBox. (vec elements)))

(defn center [& elements]
  (Center. (vec elements)))

(defn inline [& elements]
  (Inline. (vec elements)))

(set-print-methods! Overlay)
(set-print-methods! VBox)
(set-print-methods! Center)
