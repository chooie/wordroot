(ns wordroot.core
  (:require
   [com.stuartsierra.component :as component]
   [reagent.core :as r]
   [wordroot.ui.core :as ui]
   [wordroot.config :as config]
   [wordroot.routing :as routing]))

(enable-console-print!)

(defn wordroot-system
  [config]
  (let [system-map            (component/system-map
                                :routing (routing/new-routing
                                           (:host config)
                                           (:port config))
                                :ui (ui/new-ui))
        system-dependency-map (component/system-using system-map
                                {:ui {:routing-component :routing}})]
    system-dependency-map))

(defonce system (atom nil))

(defn- init
  []
  (let [wordroot-config (config/get-config)]
    (reset! system (wordroot-system wordroot-config))))

(defn- start
  []
  (reset! system (component/start @system)))

(defn- stop
  []
  (when @system
    (reset! system (component/stop @system))))

(defn go
  []
  (stop)
  (init)
  (start))
