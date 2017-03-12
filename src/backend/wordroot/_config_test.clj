(ns wordroot.-config-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [wordroot.config :as config]))

(deftest config-test
  (testing "Config for resource paths"
    (let [test-config (config/get-config-with-profile :automated-tests)]
      (is (= "/wordroot-test-data" (:resources-path test-config))))))
