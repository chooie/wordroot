(ns wordroot.ui.core
  (:require
   [com.stuartsierra.component :as component]
   [reagent.core :as r]
   [wordroot.ui.navbar.core :as navbar]
   [wordroot.ui.pages.core :as pages]))

(defn get-application-element
  []
  (js/document.getElementById "application"))

(defn application-component
  [routing-component]
  [:div.app
   [navbar/navbar-component
    (:current-route-atom routing-component)
    (:route-prefix routing-component)
    (:routing-paths routing-component)]
   [pages/page-container routing-component]])

(defn mount-root!
  [routing-component]
  (r/render
    (application-component routing-component)
    (get-application-element)))

(defrecord UI [routing-component]
  component/Lifecycle
  (start [component]
    (let [set-component (assoc component :routing-component routing-component)]
      (mount-root! (:routing-component set-component))
      set-component))
  (stop [component]
    (assoc component :routing-component nil)))

(defn new-ui
  []
  (map->UI {}))
