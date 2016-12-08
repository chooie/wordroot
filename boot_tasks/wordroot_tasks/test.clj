(ns wordroot-tasks.test
  (:require
   [clojure.test :as test]))

(defn run-backend-tests
  []
  (test/run-all-tests #"^wordroot.*-test$"))
