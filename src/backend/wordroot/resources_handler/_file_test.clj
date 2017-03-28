(ns wordroot.resources-handler.-file-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.stuartsierra.component :as component]
   [wordroot.config :as config]
   [wordroot.resources-handler.core :as resources-handler]
   [wordroot.resources-handler.file :as file]))

(defn get-asset-path
  []
  (let [config                      (config/get-config-with-profile
                                      :automated-tests)
        resources-handler-component (resources-handler/new-handler
                                      (:resources-path config))

        started-resources-handler-component
        (component/start resources-handler-component)

        resources-path (:path started-resources-handler-component)]
    resources-path))

(deftest file-test
  (let [asset-path (get-asset-path)]
    (testing "Can get resource handler file asset path"
      (is (= asset-path "/usr/local/wordroot-test-data")))
    (testing "Checks that the path is not an existing directory"
      (when (file/is-existing-directory? asset-path)
        (file/delete-file asset-path))
      (is (= (file/is-existing-directory? asset-path) false)))
    (testing "Can create directory and check that exists"
      (file/create-directory asset-path)
      (is (= (file/is-existing-directory? asset-path) true)))))
