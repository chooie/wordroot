(ns wordroot.test
  (:require
   [cljs.test :refer-macros [run-all-tests run-tests]]
   [wordroot.components.navbar.core-test]))

(defn run-frontend-tests
  []
  (enable-console-print!)
  (run-all-tests #"^wordroot.*-test$"))
