(ns wordroot.core
  (:require
   [com.stuartsierra.component :as component]
   [wordroot.components.core :as components]
   [wordroot.config :as config]
   [wordroot.routing :as routing]))

(enable-console-print!)

(defn wordroot-system
  [config]
  (let [system-map            (component/system-map
                                :routing (routing/new-routing
                                           (:host config)
                                           (:port config)))
        system-dependency-map (component/system-using system-map
                                {})]
    system-dependency-map))

(defn init!
  []
  (let [wordroot-config (config/get-config)]
    (component/start (wordroot-system wordroot-config))
    (components/mount-root!)))
