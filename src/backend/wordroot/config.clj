(ns wordroot.config
  (:require
   [immuconf.config :as immuconf]))

(defmacro get-dev-config
  []
  (#'clojure.core/load-data-readers)
  (immuconf/load
    "resources/config.edn"
    "dev_config.edn"))
