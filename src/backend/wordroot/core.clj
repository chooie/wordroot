(ns wordroot.core
  (:require
   [com.stuartsierra.component :as component]
   [compojure.core :as compojure]
   [compojure.route :as route]
   [compojure.handler :as handler]
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

(defn wordroot-system
  []
  (let [system-map            (component/system-map
                                :db (db/new-db db-config/postgres-db))
        system-dependency-map (component/system-using system-map
                                {})]
    system-dependency-map))
