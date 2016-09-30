(ns wordroot.routing
  (:require
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [reagent.session :as session]
   [secretary.core :as secretary :refer-macros [defroute]]
   [wordroot.constants :as constants])
  (:import goog.History))

(secretary/set-config! :prefix constants/secretary-prefix)

(defn set-page-session-val!
  [page]
  (session/put! :page page))

(defroute home-path (:home constants/paths)
  []
  (set-page-session-val! :home))

(defroute about-path (:about constants/paths)
  []
  (set-page-session-val! :about))

(defroute "*"
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
