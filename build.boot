(set-env!
  :source-paths #{"src/frontend" "src/backend"}
  :resource-paths #{"resources"}

  :dependencies '[[adzerk/boot-cljs "1.7.228-1"]
                  [adzerk/boot-cljs-repl "0.3.2"]
                  [adzerk/boot-reload "0.4.12"]
                  ;; [adzerk/boot-test "1.1.2" :scope "test"]
                  [com.cemerick/piggieback "0.2.1"]
                  [com.layerware/hugsql "0.4.7"]
                  [compojure "1.5.1"]
                  [conman "0.6.2"]
                  [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT"]
                  [deraen/sass4clj "0.3.0-SNAPSHOT"]
                  [deraen/boot-sass "0.3.0-SNAPSHOT"]
                  [hiccup "1.0.5"]
                  [javax.servlet/servlet-api "2.5"]
                  [metosin/ring-http-response "0.8.0"]
                  [org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [org.clojure/java.jdbc "0.6.2-alpha3"]
                  [org.clojure/tools.nrepl "0.2.12"]
                  [org.postgresql/postgresql "9.4.1211"]
                  [org.slf4j/slf4j-nop "1.7.13" :scope "test"]
                  [pandeiro/boot-http "0.7.3"]
                  [ragtime "0.6.3"]
                  [reagent "0.6.0"]
                  [reagent-utils "0.2.0"]
                  [secretary "1.0.3"]
                  [weasel "0.7.0"]])

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  ;; '[adzerk.boot-test :refer :all]
  '[crisptrutski.boot-cljs-test :refer [test-cljs]]
  '[deraen.boot-sass :refer [sass]]
  '[pandeiro.boot-http :refer [serve]]
  '[wordroot.db.config :as db-config]
  '[wordroot.db.migration-management :as migration-management]
  '[wordroot.db.seed-management :as seed-management])

(task-options!
  cljs {:optimizations :none
        :source-map    true}
  sass {:source-map true}
  reload {:on-jsload 'wordroot.core/init!}
  test-cljs {:js-env :phantom})

(deftask cider
  "CIDER profile"
  []
  (require 'boot.repl)
  (swap! @(resolve 'boot.repl/*default-dependencies*)
    concat '[[org.clojure/tools.nrepl "0.2.12"]
             [cider/cider-nrepl "0.13.0"]
             [refactor-nrepl "2.2.0"]])
  (swap! @(resolve 'boot.repl/*default-middleware*)
    concat '[cider.nrepl/cider-middleware
             refactor-nrepl.middleware/wrap-refactor])
  identity)

(deftask build
  []
  (comp
    (speak)
    (sass)
    (cljs)
    (target :dir #{"target"})))

(deftask run
  []
  (comp
    (serve
      :handler 'wordroot.core/app
      :reload true)
    (watch)
    (reload
      :asset-path "public"
      :on-jsload 'wordroot.core/init!)
    (cljs-repl)
    (build)))

(deftask run-frontend-tests
  []
  (comp
    (test-cljs)))

(deftask development
  []
  (comp
    (cider)
    (run-frontend-tests)))

(deftask dev
  "Launch App with Development Profile"
  []
  (comp
    (run)
    (development)))

(defn load-migration-config!
  []
  (println db-config/jdbc-url))

(deftask run-migrations!
  []
  (fn [next-handler]
    (fn [fileset]
      (load-migration-config!)
      (migration-management/migrate!)
      (println "Ran migrations...")
      (next-handler fileset))))

(deftask rollback-migrations!
  []
  (fn [next-handler]
    (fn [fileset]
      (load-migration-config!)
      (migration-management/rollback!)
      (println "Migrations rolled back...")
      (next-handler fileset))))

(deftask seed-database!
  []
  (fn [next-handler]
    (fn[fileset]
      (seed-management/seed-database!)
      (println "Database seeded...")
      (next-handler fileset))))
