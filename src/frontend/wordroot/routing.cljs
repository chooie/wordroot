(ns wordroot.routing
  (:require
   [com.stuartsierra.component :as component]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [secretary.core :as secretary :include-macros true]
   [reagent.core :as r]
   [wordroot.config :as config])
  (:import goog.History))

(defn setup-routes
  [base-url current-route-atom routing-paths]
  (secretary/defroute home-path (:uri (:home routing-paths))
    []
    (reset! current-route-atom :home))

  (secretary/defroute about-path (:uri (:about routing-paths))
    []
    (reset! current-route-atom :about))

  (secretary/defroute "*"
    []
    (reset! current-route-atom :error)))

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
  [base-url route-prefix current-route-atom routing-paths]
  (secretary/set-config! :prefix route-prefix)
  (hook-browser-navigation!)
  (setup-routes base-url current-route-atom routing-paths)
  (secretary/dispatch! (:uri (get routing-paths @current-route-atom))))

(defrecord Routing [host port base-url route-prefix current-route-atom
                    routing-paths]
  component/Lifecycle
  (start [component]
    (let [set-component (->
                          (assoc component :host host)
                          (assoc :port port)
                          (assoc :base-url base-url)
                          (assoc :route-prefix "#")
                          (assoc :current-route-atom current-route-atom)
                          (assoc :routing-paths routing-paths))]
      (init!
        (:base-url set-component)
        (:route-prefix set-component)
        (:current-route-atom set-component)
        (:routing-paths set-component))
      set-component))

  (stop [component]
    (->
      (assoc component :host nil)
      (assoc :port nil)
      (assoc :base-url nil)
      (assoc :current-route-atom nil)
      (assoc :route-prefix nil)
      (assoc :routing-paths nil))))

(defn new-routing
  [host port starting-route]
  (map->Routing {:host               host
                 :port               port
                 :base-url           (str "http://" host ":" port)
                 :route-prefix       "#"
                 :current-route-atom (r/atom starting-route)
                 :routing-paths      {:home  {:uri   "/"
                                              :label "Home"}
                                      :about {:uri   "/about"
                                              :label "About"}}}))
