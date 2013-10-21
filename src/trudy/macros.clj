(ns trudy.macros
  (:require [clojure.string :as str]))

(defmacro set-print-methods!
  "Set the print methods of a class using its toString method."
  [class-name]
  `(do (defmethod print-method ~class-name [r# ^java.io.Writer w#]
         (.write w# (.toString r#)))
       (defmethod print-dup ~class-name [r# ^java.io.Writer w#]
         (.write w# (.toString r#)))))
