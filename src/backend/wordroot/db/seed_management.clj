(ns wordroot.db.seed-management
  (:require
   [wordroot.db.config :as db-config]
   [wordroot.db.words.example-words :as example-words]
   [wordroot.db.words.words-seeder :as words-seeder]))

(defn seed-database!
  []
  (words-seeder/seed!
    db-config/postgres-db
    example-words/words))
