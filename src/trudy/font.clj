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
  [text {:keys [family size]}]
  (AttributedString. text {TextAttribute/FONT (awt-font family)
                           TextAttribute/SIZE (float size)}))

(defn- ^LineBreakMeasurer line-break-measurer [text font]
  (LineBreakMeasurer.
   (.getIterator (attributed-string text font))
   (font-render-context (awt-font font))))

(defn- next-break [^LineBreakMeasurer lbm width]
  (.nextLayout lbm width)
  (.getPosition lbm))

(defn- line-breaks
  [text font width]
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

(defn text-size
  "Find the width and height of a given string of text for a particular font."
  [text font]
  (let [font   (awt-font font)
        layout (TextLayout. text font (font-render-context font))
        bounds (.getBounds layout)]
    [(int (.getWidth bounds))
     (int (.getHeight bounds))]))

