(ns wordroot.server
  (:require
   [com.stuartsierra.component :as component]
   [org.httpkit.server :as httpkit-server]))

(defrecord WebServer [port resources-path handler]
  component/Lifecycle

  (start [component]
    (when (nil? (:server component))
      (println (str "Starting server on port: " port))
      (assoc component :server (httpkit-server/run-server
                                 (:handler handler)
                                 {:port port}))))

  (stop [component]
    (when (:server component)
      (let [shutdown-fn (:server component)]
        (println "Shutting down server")
        (shutdown-fn))
      (assoc component :server nil))))

(defn new-web-server
  [port resources-path]
  (map->WebServer {:port           port
                   :resources-path resources-path}))
