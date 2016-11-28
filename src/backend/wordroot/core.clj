(ns wordroot.core
  (:require
   [com.stuartsierra.component :as component]
   [wordroot.db.config :as db-config]
   [wordroot.db.core :as db]
   [wordroot.db.words.words :as words]
   [wordroot.handler :as handler]
   [wordroot.server :as server]
   [wordroot.views :as views]))

(defn wordroot-system
  [config]
  (let [system-map            (component/system-map
                                :db (db/new-db
                                      {:connection (:db config)})
                                :handler (handler/new-handler)
                                :server (server/new-web-server (:port config)))
        system-dependency-map (component/system-using system-map
                                {:handler {:db :db}
                                 :server  {:handler :handler}})]
    system-dependency-map))
