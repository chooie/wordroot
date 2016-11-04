(ns wordroot.db.words.words-seeder
  (:require
   [conman.core :as conman]
   [wordroot.db.config :as db-config]
   [wordroot.db.words.example-words :as example-words]
   [wordroot.db.words.words :as words]))

(defn seed!
  []
  (conman/with-transaction [db-config/*db*]
    (doall
      (map words/persist-word! example-words/words))))
