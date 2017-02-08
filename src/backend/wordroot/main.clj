(ns wordroot.main
  (:require
   [com.stuartsierra.component :as component]
   [wordroot.config :as config]
   [wordroot.core :as wordroot])
  (:gen-class))

(defn -main [& args]
  (let [config          (config/get-config-with-profile :production)
        wordroot-system (wordroot/wordroot-system config)]
    (component/start wordroot-system)))
