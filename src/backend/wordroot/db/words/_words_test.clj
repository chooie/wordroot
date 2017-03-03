(ns wordroot.db.words.-words-test
  (:require
   [clojure.test :refer [deftest is testing run-tests]]
   [com.stuartsierra.component :as component]
   [wordroot.config :as config]
   [wordroot.db.core :as wordroot-db]
   [wordroot.db.migration-management :as migration-management]
   [wordroot.db.words.example-words :as wordroot-db-example-words]
   [wordroot.db.words.words :as words]))

(def test-word
  {:word    "thisisareallylongword"
   :meaning "Some meaning about a really long word"
   :parts   [{:part "this"
              :root {:word     "root1"
                     :meaning  "meaning1"
                     :language "language1"}}
             {:part "is"}
             {:part "a"}
             {:part "really"
              :root {:word     "root2"
                     :meaning  "meaning2"
                     :language "language2"}}
             {:part "long"}
             {:part "word"
              :root {:word     "root3"
                     :meaning  "meaning3"
                     :language "language3"}}]})

(deftest db-test
  (let [config               (config/get-config-with-profile :automated-tests)
        db-config            (:db config)
        db-component         (wordroot-db/new-db {:connection db-config})
        started-db-component (component/start db-component)
        db-connection        (:connection started-db-component)]
    (migration-management/rollback! db-connection)
    (migration-management/migrate! db-connection)
    (testing "Can insert a word into the database and get it back again"
      (words/persist-word! db-connection test-word)
      (let [word-from-db (words/get-word-by-word-name
                           db-connection (:word test-word))]
        (is (= (:word test-word) (:word word-from-db)))))))
