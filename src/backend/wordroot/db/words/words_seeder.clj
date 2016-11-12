(ns wordroot.db.words.words-seeder
  (:require
   [clojure.java.jdbc :as jdbc]
   [wordroot.db.words.words :as words]))

(defn seed!
  [db-connection words]
  (jdbc/with-db-transaction [transaction-connection db-connection]
    (doall
      (map (fn [word]
             (words/persist-word! transaction-connection word))
        words))))
