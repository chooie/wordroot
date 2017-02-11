(ns wordroot.config)

(defn- get-host
  []
  (.-host js/env))

(defn- get-port
  []
  (.-port js/env))

(defn get-config
  []
  {:host (get-host)
   :port (get-port)})
