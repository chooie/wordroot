(set-env!
  :source-paths #{"boot_tasks" "src/backend" "src/frontend"}
  :resource-paths #{"resources"}

  :dependencies '[[adzerk/boot-cljs "1.7.228-1"]
                  [adzerk/boot-cljs-repl "0.3.2"]
                  [adzerk/boot-reload "0.4.12"]
                  [cljs-ajax "0.5.8"]
                  [com.cemerick/piggieback "0.2.1"]
                  [com.layerware/hugsql "0.4.7"]
                  [com.stuartsierra/component "0.3.1"]
                  [compojure "1.5.1"]
                  [crisptrutski/boot-cljs-test "0.3.0"]
                  [deraen/boot-sass "0.3.0"]
                  [deraen/sass4clj "0.3.0"]
                  [hiccup "1.0.5"]
                  [http-kit "2.2.0"]
                  [javax.servlet/servlet-api "2.5"]
                  [levand/immuconf "0.1.0"]
                  [metosin/boot-alt-test "0.3.0"]
                  [metosin/ring-http-response "0.8.0"]
                  [onetom/boot-lein-generate "0.1.3"]
                  [org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [org.clojure/java.jdbc "0.6.2-alpha3"]
                  [org.clojure/tools.namespace "0.3.0-alpha3"]
                  [org.clojure/tools.nrepl "0.2.12"]
                  [org.postgresql/postgresql "9.4.1211"]
                  [org.slf4j/slf4j-nop "1.7.13"]
                  [ragtime "0.6.3"]
                  [reagent "0.6.0"]
                  [reagent-utils "0.2.0"]
                  [ring "1.5.0"]
                  [ring-middleware-format "0.7.0"]
                  [secretary "1.0.3"]
                  [venantius/accountant "0.1.8"]
                  [weasel "0.7.0"]])

(require
  '[adzerk.boot-cljs :as boot-cljs]
  '[boot.lein]
  '[deraen.boot-sass :as boot-sass]
  '[metosin.boot-alt-test :as boot-alt-test]
  '[wordroot-tasks.db :as wordroot-db]
  '[wordroot-tasks.dev :as wordroot-dev]
  '[wordroot-tasks.test :as wordroot-test]
  '[wordroot-tasks.util :as wordroot-util])

(boot.lein/generate)

(deftask run-backend-tests
  []
  (boot-alt-test/alt-test))

(deftask watch-backend-tests
  []
  (comp
    (watch)
    (speak)
    (run-backend-tests)))

(deftask run-all-tests
  []
  (comp
    (run-backend-tests)
    (wordroot-test/run-frontend-tests)))

(deftask run-migrations
  []
  (fn [next-task]
    (fn [fileset]
      (require 'wordroot-tasks.db)
      (let [run-migrations! (resolve 'wordroot-tasks.db/run-migrations!)]
        (run-migrations!))
      (next-task fileset))))

(deftask get-build-ready
  []
  (comp
    (wordroot-util/data-readers)
    (run-migrations)))

#_(deftask reset-and-seed-database!
    []
    (comp
      (wordroot-util/data-readers)
      (reset-and-seed-database)))

(deftask dev
  "Launch App with Development Profile"
  []
  (comp
    (wordroot-dev/start-development)))


(deftask package
  "Build the package"
  []
  (comp
    (run-all-tests)
    (boot-sass/sass :output-style :compressed)
    (boot-cljs/cljs :optimizations :advanced
      :compiler-options {:reloads nil})
    (aot :namespace #{'wordroot.main})
    ;; (pom)
    (uber)
    (jar :main 'wordroot.main)
    (sift :include #{#".*\.jar"})
    (target)))
