(ns trudy.font
  "Functions for dealing with fonts and text."
  (:import [java.awt Font]
           [java.awt.font FontRenderContext TextLayout]))

(def font-styles
  {:plain  Font/PLAIN
   :bold   Font/BOLD
   :italic Font/ITALIC})

(defn awt-font
  "Create a java.awt.Font object from a map."
  [{:keys [family style size] :or {family "SansSerif", style :plain, size 10}}]
  (Font. family (font-styles style) size))

(defn- font-render-context [^Font font]
  (FontRenderContext. (.getTransform font) (boolean true) (boolean true)))

(defn text-size
  "Find the width and height of a given string of text for a particular font."
  [content font]
  (let [font   (awt-font font)
        layout (TextLayout. content font (font-render-context font))
        bounds (.getBounds layout)]
    [(int (.getWidth bounds))
     (int (.getHeight bounds))]))

