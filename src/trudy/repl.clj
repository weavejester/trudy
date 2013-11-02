(ns trudy.repl
  "Utility functions for REPL-based development."
  (:require [crumpets.core :as color]
            [trudy.swing :as swing]
            [trudy.ui :as ui]
            [trudy.layout :as layout]
            [trudy.graphics :as g]))

(defn show
  "Pop up a window showing the rendered entity."
  [entity]
  (let [canvas (swing/window-canvas {:title "REPL" :size [400 400]})]
    (swing/on-resize canvas #(g/paint canvas entity))
    (Thread/sleep 50)
    (g/paint canvas entity)))
