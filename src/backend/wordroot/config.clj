(ns wordroot.config
  (:require
   [immuconf.config :as immuconf]))

(defn get-dev-config
  []
  (immuconf/load
    "resources/config.edn"
    "dev_config.edn"))
