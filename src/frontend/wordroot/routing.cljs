(ns wordroot.routing
  (:require
   [accountant.core :as accountant]
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

  (secretary/defroute error-path "*"
    []
    (reset! current-route-atom :error)))

(defonce configure-navigation-has-been-run (atom false))

(defn configure-navigation
  "Only run this once, otherwise multiple navigation handlers will be created.
  Might not be an issue, although it could cause memory leak issues down the
  road when working in a reloadable environment"
  []
  (accountant/configure-navigation!
    {:nav-handler  (fn [path]
                     (secretary/dispatch! path))
     :path-exists? (fn [path]
                     (secretary/locate-route path))}))

(defn init!
  [base-url route-prefix current-route-atom routing-paths]
  (secretary/set-config! :prefix route-prefix)
  (setup-routes base-url current-route-atom routing-paths)
  (when-not @configure-navigation-has-been-run
    (reset! configure-navigation-has-been-run true)
    (configure-navigation))
  (accountant/dispatch-current!))

(defrecord Routing [host port base-url route-prefix current-route-atom
                    routing-paths]
  component/Lifecycle
  (start [component]
    (.log js/console "Starting Routing Component")
    (let [set-component (->
                          (assoc component :host host)
                          (assoc :port port)
                          (assoc :base-url base-url)
                          (assoc :route-prefix route-prefix)
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
      component
      (assoc :host nil)
      (assoc :port nil)
      (assoc :base-url nil)
      (assoc :current-route-atom nil)
      (assoc :route-prefix nil)
      (assoc :routing-paths nil))))

(defonce current-route-atom (r/atom :home))

(defn new-routing
  [host port]
  (map->Routing {:host               host
                 :port               port
                 :base-url           (str "http://" host ":" port)
                 :route-prefix       ""
                 :current-route-atom current-route-atom
                 :routing-paths      {:home  {:uri   "/"
                                              :label "Home"}
                                      :about {:uri   "/about"
                                              :label "About"}}}))
