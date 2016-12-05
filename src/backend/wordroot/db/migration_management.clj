(ns wordroot.db.migration-management
  (:require
   [ragtime.repl :as ragtime-repl]
   [wordroot.db.config :as db-config]))

(defn rollback!
  [db-env-config]
  (ragtime-repl/rollback (db-config/create-ragtime-config
                           db-env-config)))

(defn migrate!
  [db-env-config]
  (ragtime-repl/migrate (db-config/create-ragtime-config
                          db-env-config)))
