(ns wordroot.db.core
  (:require
   [com.stuartsierra.component :as component]
   [clojure.java.jdbc :as jdbc]))

(defrecord Database [connection]
  component/Lifecycle

  (start [component]
    (println "Starting database")
    (assoc component :connection connection))

  (stop [component]
    (println "Stopping database")
    (assoc component :connection nil)))

(defn new-db
  [pool-spec]
  (->Database pool-spec))
