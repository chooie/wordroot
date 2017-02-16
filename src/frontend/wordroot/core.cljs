(ns wordroot.core
  (:require
   [com.stuartsierra.component :as component]
   [wordroot.ui.core :as ui]
   [wordroot.config :as config]
   [wordroot.routing :as routing]))

(enable-console-print!)

(defn wordroot-system
  [config]
  (let [system-map            (component/system-map
                                :routing (routing/new-routing
                                           (:host config)
                                           (:port config)
                                           :home)
                                :ui (ui/new-ui))
        system-dependency-map (component/system-using system-map
                                {:ui {:routing-component :routing}})]
    system-dependency-map))

(defn init!
  []
  (let [wordroot-config (config/get-config)]
    (component/start (wordroot-system wordroot-config))))
