(ns wordroot.core
  (:require
   [com.stuartsierra.component :as component]
   [compojure.core :as compojure]
   [compojure.route :as route]
   [compojure.handler :as handler]
   [org.httpkit.server :as httpkit-server]
   [ring.middleware.resource :as resource]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.not-modified :as not-modified]
   [ring.util.http-response :as http-response]
   [ring.util.response :as response]
   [wordroot.db.config :as db-config]
   [wordroot.db.core :as db]
   [wordroot.db.words.words :as words]
   [wordroot.views :as views]))

(compojure/defroutes main-routes
  (compojure/GET "/" []
    (->
      (http-response/ok views/index-page)
      (response/content-type "text/html")))
  (route/not-found "Page not found"))

(def app
  (->
    (handler/site main-routes)
    (resource/wrap-resource "public")
    (content-type/wrap-content-type)
    (not-modified/wrap-not-modified)))

(defrecord WebServer [port]
  component/Lifecycle

  (start [component]
    (when (nil? (:server component))
      (println (str "Starting server on port: " port))
      (assoc component :server (httpkit-server/run-server #'app {:port port}))))

  (stop [component]
    (when (:server component)
      (let [shutdown-fn (:server component)]
        (println "Shutting down server")
        (shutdown-fn)
        (assoc component :server nil)))))

(defn get-web-server
  [port]
  (->WebServer port))

(defn wordroot-system
  []
  (let [system-map            (component/system-map
                                :db (db/new-db db-config/postgres-db)
                                :server (get-web-server 8000))
        system-dependency-map (component/system-using system-map
                                {})]
    system-dependency-map))
