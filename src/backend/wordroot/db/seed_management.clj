(ns wordroot.db.seed-management
  (:require [wordroot.db.words.words-seeder :as words-seeder]))

(defn seed-database!
  []
  (words-seeder/seed!))
