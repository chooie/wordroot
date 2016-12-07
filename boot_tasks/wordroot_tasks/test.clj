(ns wordroot-tasks.test
  (:require
   [boot.core :as boot]
   [crisptrutski.boot-cljs-test :as boot-cljs-test]
   [clojure.test :as test]))

(defn run-backend-tests
  []
  (test/run-all-tests #"^wordroot.*-test$"))

;; TODO: Figure out how to use this without it being unbearably slow
;; Figure out how to use this without wiping out the fileset
(boot/deftask run-frontend-tests
  []
  (comp
    (boot-cljs-test/test-cljs
      :js-env :phantom)))
