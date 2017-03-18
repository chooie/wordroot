(ns wordroot-tasks.dev
  (:require
   [adzerk.boot-cljs :as boot-cljs]
   [adzerk.boot-cljs-repl :as boot-cljs-repl]
   [adzerk.boot-reload :as boot-reload]
   [boot.core :as boot]
   [boot.task.built-in :as boot-task]
   [clojure.tools.namespace.repl :as repl]
   [com.stuartsierra.component :as component]
   [deraen.boot-sass :as boot-sass]
   [wordroot.config :as config]
   [wordroot.core :as wr]
   [wordroot-tasks.ide-integration :as wordroot-ide-integration]
   [wordroot-tasks.util :as wordroot-util]))

(repl/disable-reload!)

(reloaded.repl/set-init!
  (fn []
    (let [config (config/get-config-with-profile :dev)]
      (wr/wordroot-system config))))

(boot/deftask build-dev
  []
  (comp
    (wordroot-util/data-readers)
    (boot-sass/sass
      :source-map true)
    (boot-cljs/cljs
      :optimizations :none
      :source-map    true
      :ids #{"public/js/main"})
    (boot-task/target :dir #{"target"})))

(boot/deftask start-development
  []
  (comp
    (wordroot-ide-integration/cider)
    (boot-task/watch)
    (boot-reload/reload
      :port 33215
      :ws-port 33215
      :asset-path "public"
      :on-jsload 'wordroot.core/go)
    (boot-cljs-repl/cljs-repl
      :nrepl-opts {:port 9009}
      :ids #{"public/js/main"})
    (build-dev)))
