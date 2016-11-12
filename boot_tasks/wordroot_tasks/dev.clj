(ns wordroot-tasks.dev
  (:require
   [boot.core :as boot]
   [clojure.tools.namespace.repl :as repl]
   [com.stuartsierra.component :as component]
   [wordroot.core :as wr]))

(def system (atom nil))

(defn init
  []
  (reset! system (wr/wordroot-system)))

(defn start
  []
  (component/start @system))

(defn stop
  []
  (when @system
    (component/stop @system)))

(defn go
  []
  (init)
  (start))

(defn reset
  []
  (stop)
  (repl/refresh :after go))
