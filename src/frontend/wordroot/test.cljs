(ns wordroot.test
  (:require
   [cljs.test :refer-macros [run-all-tests run-tests]]
   [wordroot.components.navbar.core-test]))

(defn run-frontend-tests
  "Note: Check the browser console for test output, dummy!"
  []
  ;; TODO figure out how to get test result output to print out
  ;; at the REPL
  (enable-console-print!)
  (run-all-tests #"^wordroot.*-test$"))
