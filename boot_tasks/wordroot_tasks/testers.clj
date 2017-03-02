(ns wordroot-tasks.testers
  (:require
   [boot.core :as boot]
   [boot.task.built-in :as boot-task]
   [crisptrutski.boot-cljs-test :as boot-cljs-test]
   [metosin.boot-alt-test :as boot-alt-test]))

(boot/deftask run-frontend-tests
  []
  (comp
    (boot-cljs-test/test-cljs
      :keep-errors? true
      :verbosity 3
      :js-env :phantom)
    (boot-cljs-test/report-errors!)))

(boot/deftask watch-frontend-tests
  []
  (comp
    (boot-task/watch)
    (boot-task/speak)
    (run-frontend-tests)))

(boot/deftask run-backend-tests
  []
  (boot-alt-test/alt-test))

(boot/deftask watch-backend-tests
  []
  (comp
    (boot-task/watch)
    (boot-task/speak)
    (run-backend-tests)))

(boot/deftask run-all-tests
  []
  (comp
    (run-backend-tests)
    (run-frontend-tests)))
