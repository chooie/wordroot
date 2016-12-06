(ns wordroot-tasks.test
  (:require
   [clojure.test :as test]))

(defn run-backend-tests
  []
  (test/run-all-tests #"^wordroot.*-test$"))

;; TODO: Figure out how to use this without it being unbearably slow
#_(boot/deftask run-frontend-tests
    []
    (comp
      (boot-cljs-test/test-cljs
        :js-env :phantom)))
