(ns wordroot.routing
  (:require
   [ajax.core :as ajax]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [reagent.session :as session]
   [secretary.core :as secretary :include-macros true]
   [wordroot.config :as config]
   [wordroot.constants :as constants])
  (:import goog.History))

(def base-url (let [config (config/get-config)]
                (str
                  "http://"
                  (:host config)
                  ":"
                  (:port config))))

(def words-url
  (str
    base-url
    "/words"))

(defn get-words!
  []
  (ajax/GET words-url
    {:handler (fn [words]
                (session/put! :words words))}))

(secretary/set-config! :prefix constants/secretary-prefix)

(defn set-page-session-val!
  [page]
  (session/put! :page page))


(secretary/defroute home-path (:home constants/paths)
  []
  (get-words!)
  (set-page-session-val! :home))

(secretary/defroute about-path (:about constants/paths)
  []
  (set-page-session-val! :about))

(secretary/defroute "*"
  []
  (set-page-session-val! :error))

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
  []
  (hook-browser-navigation!)
  (secretary/dispatch! (:home constants/paths)))
