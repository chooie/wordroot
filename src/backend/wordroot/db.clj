(ns wordroot.db
  (:require [clojure.java.jdbc :as jdbc]))

#_(str
    "postgresql://wordroot_user:weak_password"
    "@localhost:5432/wordroot_database")

(def db
  {:classname   "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname     "//localhost/wordroot_database"
   :username    "wordroot_user"
   :password    "weak_password"})

(def postgres-uri
  {:connection-uri "jdbc:postgresql://localhost:5432/wordroot_database?user=wordroot_user&password=weak_password"})

#_(println (jdbc/query db ["SELECT * FROM foo"]))
