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
  (testing "Can get resource handler file asset path"
    (is (= (get-asset-path) "/wordroot-test-data"))))
