(ns wordroot.db.words.-words-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.stuartsierra.component :as component]
   [wordroot.config :as config]
   [wordroot.db.core :as wordroot-db]
   [wordroot.db.migration-management :as migration-management]
   [wordroot.db.words.example-words :as wordroot-db-example-words]
   [wordroot.db.words.words :as words]))

(deftest db-test
  (let [config               (config/get-config-with-profile :automated-tests)
        db-config            (:db config)
        db-component         (wordroot-db/new-db {:connection db-config})
        started-db-component (component/start db-component)
        db-connection        (:connection started-db-component)
        test-word            (get wordroot-db-example-words/words 0)]
    (migration-management/rollback! db-connection)
    (migration-management/migrate! db-connection)
    (testing "Can insert a word into the database and get it back again"
      (words/persist-word! db-connection test-word)
      (let [word-from-db (words/get-word-by-word-name
                           db-connection (:word test-word))]
        (is (= (:word test-word) (:word word-from-db)))
        (let [words (words/get-words db-connection)]
          (is (= (count words) 1)))))))
