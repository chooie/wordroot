(ns wordroot-tasks.dev
  (:require
   [boot.core :as boot]
   [clojure.tools.namespace.repl :as repl]
   [com.stuartsierra.component :as component]
   [wordroot.core :as wr]))

(def system (atom nil))

(defn- init
  []
  (reset! system (wr/wordroot-system)))

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
  (repl/refresh :after 'wordroot-tasks.dev/go))
