(ns trudy.size
  (:refer-clojure :exclude [min max range])
  (:require [clojure.core :as core]))

(defprotocol Bounds
  (min [x])
  (max [x]))

(deftype Range [start end]
  Bounds
  (min [_] start)
  (max [_] end)
  clojure.lang.Seqable
  (seq [_] (core/range start end)))

(defn range [start end]
  (Range. start end))

(extend-type Number
  Bounds
  (min [x] x)
  (max [x] x))

(defn magnitude [bounds]
  (- (max bounds) (min bounds)))

(defn within? [bounds x]
  (<= (min bounds) x (max bounds)))

(defn- next-index [bounds values]
  (let [indexes (core/range (count values))]
    (->> (map vector indexes bounds values)
         (filter (fn [[_ b v]] (< v (max b))))
         (sort-by peek)
         (ffirst))))

(defn pack [bounds target]
  (let [maximum (apply + (map max bounds))
        target  (core/min target maximum)]
    (loop [values (vec (map min bounds))]
      (if (< (apply + values) target)
        (recur (update-in values [(next-index bounds values)] inc))
        values))))

(defn overlay [bounds]
  (range (apply core/max (map min bounds))
         (apply core/max (map max bounds))))

(defn series [bounds]
  (range (apply + (map min bounds))
         (apply + (map max bounds))))
