(ns wordroot.db
  (:require
   [ragtime.jdbc :as jdbc]
   [ragtime.repl :as ragtime-repl]))

(def db
  {:classname   "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname     "//localhost/wordroot_database"
   :username    "wordroot_user"
   :password    "weak_password"})

(def config
  {:datastore  (jdbc/sql-database {:connection-uri "jdbc:postgresql://localhost:5432/wordroot_database?user=wordroot_user&password=weak_password"})
   :migrations (jdbc/load-resources "migrations")})

(defn rollback!
  []
  (ragtime-repl/rollback config))

(defn migrate!
  []
  (ragtime-repl/migrate config))
