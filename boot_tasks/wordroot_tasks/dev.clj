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

(defonce system (atom nil))

(defn- init
  []
  (let [config (config/get-config-with-profile :dev)]
    (reset! system (wr/wordroot-system config))))

(defn- start
  []
  (reset! system (component/start @system)))

(defn- stop
  []
  (when @system
    (reset! system (component/stop @system))))

(defn go
  []
  (stop)
  (init)
  (start))

(defn reset
  []
  (stop)
  (repl/refresh-all :after 'wordroot-tasks.dev/go))

(boot/deftask build-dev
  []
  (comp
    (wordroot-util/data-readers)
    (boot-sass/sass
      :source-map true)
    (boot-cljs/cljs
      :optimizations :none
      :source-map    true)
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
    (boot-cljs-repl/cljs-repl :nrepl-opts {:port 9009})
    (build-dev)))
