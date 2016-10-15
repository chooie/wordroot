(ns wordroot.core
  (:require
   [compojure.core :as compojure]
   [compojure.route :as route]
   [compojure.handler :as handler]
   [ring.middleware.resource :as resource]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.not-modified :as not-modified]
   [ring.util.response :as response]))

(compojure/defroutes main-routes
  (compojure/GET "/" []
    (let [index-file-response (response/resource-response
                                "index.html"
                                {:root "public"})]
      (response/content-type
        index-file-response
        "text/html")))
  (route/not-found "Page not found"))

(def app
  (->
    (handler/site main-routes)
    (resource/wrap-resource "public")
    (content-type/wrap-content-type)
    (not-modified/wrap-not-modified)))
