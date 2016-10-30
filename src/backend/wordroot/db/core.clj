(ns wordroot.db.core
  (:require
   [ragtime.jdbc :as jdbc]
   [ragtime.repl :as ragtime-repl]
   [wordroot.db.words :as words]))

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

(defn seed-words!
  []
  (words/insert-words!
    db
    {:words
     [["mytestword"
       "This is my test word's description"]
      ["thisisaword"
       "Just another word"]]}))
