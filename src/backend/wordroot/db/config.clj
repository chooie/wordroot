(ns wordroot.db.config)

(def postgres-db
  {:dbtype   "postgresql"
   :dbname   "wordroot_database"
   :host     "localhost"
   :user     "wordroot_user"
   :password "weak_password"})

(def ragtime-postgres-uri
  (let [{:keys [dbtype dbname host user password]} postgres-db]
    (str
      "jdbc:" dbtype "://"
      host ":5432/" dbname "?"
      "user=" user "&"
      "password=" password)))
