(ns wordroot.components.navbar.core-test
  (:require
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [wordroot.components.navbar.core :as nav]))

(deftest test-utils
  (testing "Returns 'active' string when page matches label"
    (is (= "active" (nav/make-active-when-active :matches "matches")))
    (is (= nil (nav/make-active-when-active :test "no-match")))))
