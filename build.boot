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
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :as boot-reload]
  '[boot.lein]
  '[crisptrutski.boot-cljs-test :as boot-cljs-test]
  '[deraen.boot-sass :refer [sass]]
  '[metosin.boot-alt-test :as boot-alt-test]
  '[wordroot-tasks.ide-integration :as wordroot-ide-integration])

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

(deftask run-frontend-tests
  []
  (comp
    (boot-cljs-test/test-cljs
      :keep-errors? true
      :js-env :slimer)
    (boot-cljs-test/report-errors!)))

(deftask watch-frontend-tests
  []
  (comp
    (watch)
    (speak)
    (run-frontend-tests)))

(deftask run-all-tests
  []
  (comp
    (run-backend-tests)
    (run-frontend-tests)))

(deftask data-readers
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
    (data-readers)
    (run-migrations)))

(deftask reset-and-seed-database
  []
  (fn [next-task]
    (fn [fileset]
      (require 'wordroot-tasks.db)
      (let [reset-and-seed! (resolve
                              'wordroot-tasks.db/reset-database-and-seed!)]
        (reset-and-seed!))
      (next-task fileset))))

(deftask get-build-ready-with-dummy-data
  []
  (comp
    (data-readers)
    (reset-and-seed-database)))

(deftask build-dev
  []
  (comp
    (data-readers)
    (sass
      :source-map true)
    (cljs
      :optimizations :none
      :source-map    true)
    (target :dir #{"target"})))

(deftask run-dev
  []
  (comp
    (watch)
    (boot-reload/reload
      :asset-path "public"
      :on-jsload 'wordroot.core/go)
    (cljs-repl :nrepl-opts {:port 9009})
    (build-dev)))

(deftask cider-config
  []
  (comp
    (wordroot-ide-integration/cider)))

(deftask dev
  "Launch App with Development Profile"
  []
  (comp
    (run-dev)
    (cider-config)))


(deftask package
  "Build the package"
  []
  (comp
    (run-all-tests)
    (sass :output-style :compressed)
    (cljs :optimizations :advanced
      :compiler-options {:reloads nil})
    (aot :namespace #{'wordroot.main})
    ;; (pom)
    (uber)
    (jar :main 'wordroot.main)
    (sift :include #{#".*\.jar"})
    (target)))
