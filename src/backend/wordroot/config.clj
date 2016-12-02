(ns wordroot.config
  (:require
   [immuconf.config :as immuconf]))

(defmacro get-config
  []
  (immuconf/load
    "resources/config.edn"
    "secret_config.edn"))
