(ns wordroot.routing
  (:require
   [ajax.core :as ajax]
   [com.stuartsierra.component :as component]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [reagent.session :as session]
   [secretary.core :as secretary :include-macros true]
   [wordroot.config :as config]
   [wordroot.constants :as constants])
  (:import goog.History))

(defn get-words!
  [base-url]
  (let [words-url (str base-url "/words")]
    (ajax/GET words-url
      {:handler (fn [words]
                  (session/put! :words words))})))

(defn set-page-session-val!
  [page]
  (session/put! :page page))

(defn setup-routes
  [base-url]
  (secretary/defroute home-path (:home constants/paths)
    []
    (get-words! base-url)
    (set-page-session-val! :home))

  (secretary/defroute about-path (:about constants/paths)
    []
    (set-page-session-val! :about))

  (secretary/defroute "*"
    []
    (set-page-session-val! :error)))

(defn hook-browser-navigation!
  []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (when (not-empty (.-token event))
          (secretary/dispatch! (.-token event)))))
    (.setEnabled true)))

(defn init!
  [base-url]
  (secretary/set-config! :prefix constants/secretary-prefix)
  (hook-browser-navigation!)
  (setup-routes base-url)
  (secretary/dispatch! (:home constants/paths)))

(defrecord Routing [host port]
  component/Lifecycle
  (start [component]
    (assoc component :host host)
    (assoc component :port port)
    (assoc component :base-url (str "http://" host ":" port))
    (init! (:base-url component)))

  (stop [component]
    (assoc component :host nil)
    (assoc component :port nil)
    (assoc component :base-url nil)))

(defn new-routing
  [host port]
  (map->Routing {:host host :port port}))
