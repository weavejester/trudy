(ns trudy.size
  (:refer-clojure :exclude [min max range])
  (:require [clojure.core :as core]
            [trudy.macros :refer (set-print-methods!)]))

(defprotocol Bounds
  (min [x])
  (max [x]))

(deftype Range [start end]
  Bounds
  (min [_] start)
  (max [_] end)
  clojure.lang.Seqable
  (seq [_] (core/range start end))
  Object
  (toString [_] (str "#range " (pr-str [start end]))))

(set-print-methods! Range)

(defn range [start end]
  (Range. start end))

(defn into-range [[start end]]
  (range start end))

(extend-type Number
  Bounds
  (min [x] x)
  (max [x] x))

(defn magnitude [bounds]
  (- (max bounds) (min bounds)))

(defn within? [bounds x]
  (<= (min bounds) x (max bounds)))

(defn- next-indexes [indexes bounds values]
  (drop-while #(>= (values %) (max (bounds %))) indexes))

(defn pack [bounds target]
  (let [bounds  (vec bounds)
        maximum (apply + (mapv max bounds))
        target  (core/min target maximum)]
    (loop [values  (mapv min bounds)
           indexes (cycle (core/range (count bounds)))]
      (if (< (apply + values) target)
        (let [indexes (next-indexes indexes bounds values)]
          (recur (update-in values [(first indexes)] inc)
                 (next indexes)))
        values))))

(defn overlay [bounds]
  (range (apply core/max (map min bounds))
         (apply core/max (map max bounds))))

(defn series [bounds]
  (range (apply + (map min bounds))
         (apply + (map max bounds))))
