(ns wordroot.db.config
  (:require
   [ragtime.jdbc]))

(defn get-ragtime-postgres-uri
  [postgres-db]
  (let [{:keys [dbtype dbname host user password]} postgres-db]
    (str
      "jdbc:" dbtype "://"
      host ":5432/" dbname "?"
      "user=" user "&"
      "password=" password)))

(defn create-ragtime-config
  [postgres-db]
  (println postgres-db)
  {:datastore  (ragtime.jdbc/sql-database
                 {:connection-uri (get-ragtime-postgres-uri postgres-db)})
   :migrations (ragtime.jdbc/load-resources "migrations")})
