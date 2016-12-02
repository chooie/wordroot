(ns wordroot.config
  (:require
   [immuconf.config :as immuconf]))

(defmacro get-dev-config
  []
  (immuconf/load
    "resources/config.edn"
    "dev_config.edn"))
