(ns wordroot.core
  (:require
   [com.stuartsierra.component :as component]
   [wordroot.config :as config]
   [wordroot.db.core :as db]
   [wordroot.db.words.words :as words]
   [wordroot.handler :as handler]
   [wordroot.resources-handler.core :as resources-handler]
   [wordroot.server :as server]
   [wordroot.views :as views]))

(defn ^:private wordroot-system
  [config]
  (let [system-map (component/system-map
                     :db (db/new-db
                           {:connection (:db config)})
                     :handler (handler/new-handler
                                (:host config)
                                (:port config))
                     :server (server/new-web-server (:port config))

                     :resources-handler
                     (resources-handler/new-handler (:resources-path config)))

        system-dependency-map (component/system-using system-map
                                {:handler {:db :db}
                                 :server  {:handler :handler}})]
    system-dependency-map))

(defn get-wordroot-system-with-profile
  [profile]
  (let [config (config/get-config-with-profile profile)]
    (wordroot-system config)))
