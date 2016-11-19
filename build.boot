(set-env!
  :source-paths #{"boot_tasks" "src/backend" "src/frontend"}
  :resource-paths #{"resources"}

  :dependencies '[[adzerk/boot-cljs "1.7.228-1"]
                  [adzerk/boot-cljs-repl "0.3.2"]
                  [adzerk/boot-reload "0.4.12"]
                  [adzerk/boot-test "1.1.2" :scope "test"]
                  [cljs-ajax "0.5.8"]
                  [com.cemerick/piggieback "0.2.1"]
                  [com.layerware/hugsql "0.4.7"]
                  [com.stuartsierra/component "0.3.1"]
                  [compojure "1.5.1"]
                  [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT"]
                  [deraen/sass4clj "0.3.0-SNAPSHOT"]
                  [deraen/boot-sass "0.3.0-SNAPSHOT"]
                  [hiccup "1.0.5"]
                  [http-kit "2.2.0"]
                  [javax.servlet/servlet-api "2.5"]
                  [metosin/ring-http-response "0.8.0"]
                  [org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [org.clojure/java.jdbc "0.6.2-alpha3"]
                  [org.clojure/tools.namespace "0.2.11"]
                  [org.clojure/tools.nrepl "0.2.12"]
                  [org.postgresql/postgresql "9.4.1211"]
                  [org.slf4j/slf4j-nop "1.7.13" :scope "test"]
                  [ragtime "0.6.3"]
                  [reagent "0.6.0"]
                  [reagent-utils "0.2.0"]
                  [ring "1.5.0"]
                  [ring-middleware-format "0.7.0"]
                  [secretary "1.0.3"]
                  [weasel "0.7.0"]])

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[deraen.boot-sass :refer [sass]]
  '[wordroot-tasks.dev :as wordroot-dev]
  '[wordroot-tasks.db :as wordroot-db]
  '[wordroot-tasks.ide-integration :as wordroot-ide-integration]
  '[wordroot-tasks.test :as wordroot-test])

(deftask build
  []
  (comp
    (speak)
    (sass
      :source-map true)
    (cljs
      :optimizations :none
      :source-map    true)
    (target :dir #{"target"})))

(deftask run
  []
  (comp
    (watch)
    (reload
      :asset-path "public"
      :on-jsload 'wordroot.core/init!)
    (cljs-repl)
    (build)))

(deftask development
  []
  (comp
    (wordroot-ide-integration/cider)
    (wordroot-test/run-frontend-tests)))

(deftask dev
  "Launch App with Development Profile"
  []
  (comp
    (run)
    (development)))
