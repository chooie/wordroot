(ns wordroot-tasks.util
  (:require
   [boot.core :as boot]))

(boot/deftask data-readers
  []
  (fn [next-task]
    (fn [fileset]
      (#'clojure.core/load-data-readers)
      (with-bindings {#'*data-readers* (.getRawRoot #'*data-readers*)}
        ;; All these namespaces require the use of environment variables
        (require
          '[wordroot-tasks.dev :as wordroot-dev]
          '[wordroot-tasks.db :as wordroot-db])
        (next-task fileset)))))
