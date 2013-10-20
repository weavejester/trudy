(ns trudy.macros
  (:require [clojure.string :as str]))

(defn- camel-case [s]
  (apply str (map str/capitalize (str/split s #"-"))))

(defmacro defcomponent [name keys & body]
  (let [class-name (symbol (camel-case (str name)))
        map->name  (symbol (str "map->" class-name))]
    `(defrecord ~class-name ~keys
       ~@body
       Object
       (toString [r#]
         (str "#" (ns-name *ns*) "/" ~(str name) " " (pr-str (into {} r#)))))))

(defmacro set-print-methods!
  "Set the print methods of a class using its toString method."
  [class-name]
  `(do (defmethod print-method ~class-name [r# ^java.io.Writer w#]
         (.write w# (.toString r#)))
       (defmethod print-dup ~class-name [r# ^java.io.Writer w#]
         (.write w# (.toString r#)))))
