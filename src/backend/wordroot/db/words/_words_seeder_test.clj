(ns wordroot.db.words.-words-seeder-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.stuartsierra.component :as component]
   [wordroot.config :as config]
   [wordroot.db.core :as wordroot-db]
   [wordroot.db.migration-management :as migration-management]
   [wordroot.db.words.example-words :as example-words]
   [wordroot.db.words.words :as words]
   [wordroot.db.words.words-seeder :as words-seeder]))

(defn get-word
  [words index]
  (:word (get words index)))

(deftest words-seeder-test
  (testing "Can properly seed the database"
    (let [config               (config/get-config-with-profile :automated-tests)
          db-config            (:db config)
          db-component         (wordroot-db/new-db {:connection db-config})
          started-db-component (component/start db-component)
          db-connection        (:connection started-db-component)
          test-words           example-words/words]
      (migration-management/rollback! db-connection)
      (migration-management/migrate! db-connection)
      (words-seeder/seed! db-connection test-words)
      (let [words (words/get-words db-connection)]
        (is (= (get-word test-words 0) (get-word words 0)))
        (is (= (get-word test-words 1) (get-word words 1)))
        (is (= (get-word test-words 2) (get-word words 2)))))))
