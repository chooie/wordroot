(ns wordroot-tasks.test
  (:require
   [boot.core :as boot]
   [boot.task.built-in :as boot-task]
   [crisptrutski.boot-cljs-test :as boot-cljs-test]))

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
