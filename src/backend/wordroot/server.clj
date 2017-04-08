(ns wordroot.server
  (:require
   [com.stuartsierra.component :as component]
   [org.httpkit.server :as httpkit-server]))

(defrecord WebServer [port handler]
  component/Lifecycle

  (start [component]
    (when (:server component)
      ((:server component)))
    (println (str "Starting server on port: " port))
    (->
      component
      (assoc :port port)
      (assoc :handler handler)
      (assoc :server (httpkit-server/run-server
                       (:handler handler)
                       {:port port}))))

  (stop [component]
    (when (:server component)
      (let [shutdown-fn (:server component)]
        (println "Shutting down server")
        (shutdown-fn)))
    (->
      component
      (assoc :port nil)
      (assoc :handler nil)
      (assoc :server nil))))

(defn new-web-server
  [port]
  (map->WebServer {:port port}))
