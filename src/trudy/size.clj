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

(extend-type Number
  Bounds
  (min [x] x)
  (max [x] x))

(defn magnitude [bounds]
  (- (max bounds) (min bounds)))

(defn within? [bounds x]
  (<= (min bounds) x (max bounds)))

(defn- grow [values bounds]
  (if-let [[v & vs] (seq values)]
    (if (< v (max (first bounds)))
      (cons (inc v) vs)
      (lazy-seq (cons v (grow vs (rest bounds)))))))

(defn pack [bounds target]
  (let [maximum (apply + (map max bounds))]
    (loop [values (map min bounds)]
      (let [sum (apply + values)]
        (if (and (< sum maximum) (< sum target))
          (recur (grow values bounds))
          values)))))
