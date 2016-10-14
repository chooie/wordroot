(ns wordroot.core
  (:require
   [compojure.core :as compojure]
   [compojure.route :as route]
   [compojure.handler :as handler]
   [ring.middleware.resource :as resource]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.not-modified :as not-modified]))

(compojure/defroutes main-routes
  (compojure/GET "/" [] "Hello")
  (route/not-found "Page not found"))

(def app
  (->
    (handler/site main-routes)
    (resource/wrap-resource "public")
    (content-type/wrap-content-type)
    (not-modified/wrap-not-modified)))
