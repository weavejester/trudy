(ns trudy.font
  "Functions for dealing with fonts and text."
  (:require [clojure.string :as str])
  (:import [java.awt Font]
           [java.awt.font FontRenderContext LineBreakMeasurer TextAttribute TextLayout]
           [java.text AttributedString]))

(def font-styles
  {:plain  Font/PLAIN
   :bold   Font/BOLD
   :italic Font/ITALIC})

(defn ^Font awt-font
  "Create a java.awt.Font object from a map."
  [{:keys [family style size] :or {family "SansSerif", style :plain, size 10}}]
  (Font. family (font-styles style) size))

(defn- font-render-context [^Font font]
  (FontRenderContext. (.getTransform font) (boolean true) (boolean true)))

(defn ^AttributedString attributed-string
  "Create an AttributedString from a string of text and a font."
  [text font]
  (AttributedString. text {TextAttribute/FONT (awt-font font)}))

(defn- ^LineBreakMeasurer line-break-measurer [text font]
  (LineBreakMeasurer.
   (.getIterator (attributed-string text font))
   (font-render-context (awt-font font))))

(defn- next-break [^LineBreakMeasurer lbm width]
  (.nextLayout lbm width)
  (.getPosition lbm))

(defn- line-breaks [text font width]
  (let [lbm (line-break-measurer text font)
        len (count text)]
    (->> (repeatedly #(next-break lbm width))
         (take-while #(< % len)))))

(defn split-text
  "Splits a text string into lines based on a font size and a maximum width."
  [text font width]
  (let [breaks (line-breaks text font width)]
    (->> (concat [0] breaks [(count text)])
         (partition 2 1)
         (map (fn [[s e]] (str/trim (subs text s e)))))))

(defn text-metrics
  "Return a map of the metrics of a text string rendered with the specified
  font."
  [text font]
  (let [font   (awt-font font)
        layout (TextLayout. text font (font-render-context font))
        bounds (.getBounds layout)]
    {:advance (.getAdvance layout)
     :ascent  (.getAscent layout)
     :descent (.getDescent layout)
     :leading (.getLeading layout)
     :visible-advance (.getVisibleAdvance layout)
     :bounds [(.getWidth bounds)
              (.getHeight bounds)]}))

(defn- line-metrics [text font width]
  (let [lines   (split-text text font width)]
    (map #(text-metrics % font) lines)))

(defn- line-height [metrics]
  (+ (:ascent metrics) (:descent metrics)))

(defn text-size
  "Find the width and height of a given string of text for a particular font and
  line width."
  [text font width]
  (let [metrics (line-metrics text font width)]
    [(int (apply max (map :visible-advance metrics)))
     (int (+ (apply + (map line-height metrics))
             (apply + (map :leading (butlast metrics)))))]))

(defn text-baselines
  "Find the vertical positions of the baselines of a string of text for a given
  font and line width."
  [text font width]
  (let [metrics (line-metrics text font width)]
    (->> (partition 2 1 metrics)
         (map (fn [[a b]] (+ (:descent a) (:leading a) (:ascent b))))
         (reductions + (:ascent (first metrics))))))
