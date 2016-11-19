(ns wordroot.handler
  (:require
   [com.stuartsierra.component :as component]
   [compojure.core :as compojure]
   [compojure.route :as compojure-route]
   [compojure.handler :as compojure-handler]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.not-modified :as not-modified]
   [ring.middleware.resource :as resource]
   [ring.middleware.format :as format]
   [ring.util.http-response :as http-response]
   [ring.util.response :as response]
   [wordroot.db.words.words :as words]
   [wordroot.views :as views]))

(defn make-routes
  [db]
  (compojure/routes
    (compojure/GET "/" []
      (->
        (http-response/ok views/index-page)
        (response/content-type "text/html")))
    (compojure/GET "/words" []
      (http-response/ok (words/get-words-index (:connection db))))
    (compojure/GET "/words/:word" [word]
      (http-response/ok (words/get-word-by-word-name (:connection db) word)))
    (compojure-route/not-found "Page not found")))

(defn make-handler
  [db]
  (->
    (compojure-handler/site (make-routes db))
    (resource/wrap-resource "public")
    (content-type/wrap-content-type)
    (not-modified/wrap-not-modified)
    (format/wrap-restful-format)))

(defrecord Handler [db]
  component/Lifecycle

  (start [component]
    (println "Starting handler")
    (assoc component :handler (make-handler db)))

  (stop [component]
    (println "Stopping handler")
    (assoc component :handler nil)))

(defn new-handler
  []
  (map->Handler {}))
