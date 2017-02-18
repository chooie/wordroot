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
  [host port db]
  (compojure/routes
    (compojure/GET "/" []
      (->
        (http-response/ok (views/main-app host port))
        (response/content-type "text/html")))
    (compojure/GET "/words-index" []
      (http-response/ok (words/get-words-index (:connection db))))
    (compojure/GET "/words" []
      (http-response/ok (words/get-words (:connection db))))
    (compojure/GET "/words/:word" [word]
      (http-response/ok (words/get-word-by-word-name (:connection db) word)))
    (compojure-route/not-found (views/main-app host port))))

(defn make-handler
  [host port db]
  (->
    (compojure-handler/site (make-routes host port db))
    (resource/wrap-resource "public")
    (content-type/wrap-content-type)
    (not-modified/wrap-not-modified)
    (format/wrap-restful-format)))

(defrecord Handler [db host port]
  component/Lifecycle

  (start [component]
    (println "Starting handler")
    (assoc component :handler (make-handler host port db)))

  (stop [component]
    (println "Stopping handler")
    (assoc component :handler nil)))

(defn new-handler
  [host port]
  (map->Handler {:host host :port port}))
