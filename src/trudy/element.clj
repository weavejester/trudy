(ns trudy.element
  "Protocols for describing elements.")

(defprotocol Sized
  (size [element bounds]))
