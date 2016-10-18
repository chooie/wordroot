(set-env!
  :source-paths #{"src/frontend" "src/backend"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [org.clojure/java.jdbc "0.6.2-alpha3"]
                  [org.clojure/tools.nrepl "0.2.12"]
                  [adzerk/boot-cljs "1.7.228-1"]
                  [adzerk/boot-cljs-repl "0.3.2"]
                  [adzerk/boot-reload "0.4.12"]
                  ;; [adzerk/boot-test "1.1.2" :scope "test"]
                  [com.cemerick/piggieback "0.2.1"]
                  [compojure "1.5.1"]
                  [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT"]
                  [deraen/sass4clj "0.3.0-SNAPSHOT"]
                  [deraen/boot-sass "0.3.0-SNAPSHOT"]
                  [hiccup "1.0.5"]
                  [javax.servlet/servlet-api "2.5"]
                  [mbuczko/boot-ragtime "0.2.0"]
                  [metosin/ring-http-response "0.8.0"]
                  [org.clojure/java.jdbc "0.6.2-alpha3"]
                  [org.postgresql/postgresql "9.4.1211.jre7"]
                  [org.slf4j/slf4j-nop "1.7.13" :scope "test"]
                  [pandeiro/boot-http "0.7.3"]
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
  '[mbuczko.boot-ragtime :refer [ragtime]]
  '[pandeiro.boot-http :refer [serve]]
  '[wordroot.db :as db])

(task-options!
  ragtime {:database (:connection-uri db/postgres-uri)})

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

(deftask development
  []
  (task-options!
    cljs {:optimizations :none
          :source-map    true}
    reload {:on-jsload 'wordroot.core/init!}
    test-cljs {:js-env :phantom}
    sass {:source-map true})
  (comp
    (cider)))

(deftask run-tests
  []
  (comp
    (test-cljs)))

(deftask dev
  "Launch App with Development Profile"
  []
  (comp
    (run)
    (development)))

(defn load-migration-config!
  []
  (println (:connection-uri db/postgres-uri)))

(deftask run-migrations!
  []
  (load-migration-config!)
  (comp
    (ragtime :migrate true)))
