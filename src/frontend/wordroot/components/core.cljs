(ns wordroot.components.core
  (:require
   [reagent.core :as r]
   [wordroot.components.navbar.core :as navbar]
   [wordroot.components.pages.core :as pages]
   [wordroot.constants :as constants]))

(defn application-component
  []
  [:div.app
   [navbar/navbar-component]
   [pages/page-container]])

(defn mount-application-component
  []
  (r/render application-component constants/application-element))

(defn mount-components
  []
  (mount-application-component))
