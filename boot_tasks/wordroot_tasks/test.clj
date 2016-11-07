(ns wordroot-tasks.test
  (:require
   [adzerk.boot-test :as boot-clj-test]
   [boot.core :as boot]
   [crisptrutski.boot-cljs-test :as boot-cljs-test]))

(boot/deftask run-backend-tests
  []
  (comp
    (boot-clj-test/test
      :filters '#{ (.contains (str (.-ns %)) "-test") })))

(boot/deftask run-frontend-tests
  []
  (boot/task-options!
    boot-cljs-test/test-cljs {:js-env :phantom})
  (comp
    (boot-cljs-test/test-cljs)))
