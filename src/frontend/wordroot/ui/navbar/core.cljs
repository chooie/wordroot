(ns wordroot.ui.navbar.core
  (:require
   [accountant.core :as accountant]))

(defn make-active-when-active
  [current-route route-keyword]
  (when (= current-route route-keyword)
    "active"))

(defn link
  [current-route-atom route-prefix href label route-keyword]
  [:li.site-link
   [:button
    {:class    (make-active-when-active @current-route-atom route-keyword)
     :on-click #(accountant/navigate! href)}
    label]])

(defn make-link
  [current-route-atom route-prefix route-paths route-keyword]
  (let [route-map (get route-paths route-keyword)]
    [link
     current-route-atom
     route-prefix
     (get route-map :uri)
     (get route-map :label)
     route-keyword]))

(defn navbar-component
  [current-route-atom route-prefix route-paths]
  [:nav.navbar
   [:div.row
    [:ul.site-links
     [make-link current-route-atom route-prefix route-paths :home]
     [make-link current-route-atom route-prefix route-paths :about]]]])
