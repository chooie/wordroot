(set-env!
  :source-paths #{"src/frontend"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [org.clojure/tools.nrepl "0.2.12"]
                  [adzerk/boot-cljs "1.7.228-1"]
                  [adzerk/boot-cljs-repl "0.3.2"]
                  [adzerk/boot-reload "0.4.12"]
                  [com.cemerick/piggieback "0.2.1"]
                  [deraen/sass4clj "0.3.0-SNAPSHOT"]
                  [deraen/boot-sass "0.3.0-SNAPSHOT"]
                  [pandeiro/boot-http "0.7.3"]
                  [reagent "0.6.0"]
                  [reagent-utils "0.2.0"]
                  [secretary "1.0.3"]
                  [weasel "0.7.0"]])

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[deraen.boot-sass :refer [sass]]
  '[pandeiro.boot-http :refer [serve]])

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

(deftask run []
  (comp
    (serve :dir "target")
    (watch)
    (reload)
    (cljs-repl)
    (build)))

(deftask development []
  (task-options!
    cljs {:optimizations :none
          :source-map    true}
    reload {:on-jsload 'wordroot.core/init!})
  (comp
    (cider)))

(deftask dev
  "Launch App with Development Profile"
  []
  (comp
    (development)
    (run)))
