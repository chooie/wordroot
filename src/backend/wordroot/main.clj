(ns wordroot.main
  (:require
   [com.stuartsierra.component :as component]
   [wordroot.config :as config]
   [wordroot.core :as wordroot])
  (:gen-class))

(defn -main [& args]
  (let [wordroot-production-system (wordroot/get-wordroot-system-with-profile
                                     :production)]
    (component/start wordroot-production-system)))
