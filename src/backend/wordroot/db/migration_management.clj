(ns wordroot.db.migration-management
  (:require
   [ragtime.jdbc :as ragtime-jdbc]
   [ragtime.repl :as ragtime-repl]
   [wordroot.db.config :as db-config]))

(def ragtime-config
  {:datastore  (ragtime-jdbc/sql-database
                 {:connection-uri db-config/jdbc-url})
   :migrations (ragtime-jdbc/load-resources "migrations")})

(defn rollback!
  []
  (ragtime-repl/rollback ragtime-config))

(defn migrate!
  []
  (ragtime-repl/migrate ragtime-config))
