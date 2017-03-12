(ns wordroot.resources-handler.core
  (:require
   [com.stuartsierra.component :as component]))

(defrecord Resources-Handler [path]
  component/Lifecycle
  (start [component]
    (println "Starting resources")
    (assoc component :path path))

  (stop [component]
    (println "Stopping resources")
    (assoc component :path nil)))

(defn new-handler
  [path]
  (map->Resources-Handler {:path path}))
