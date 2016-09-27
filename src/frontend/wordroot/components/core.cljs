(ns wordroot.components.core
  (:require
   [reagent.core :as r]
   [wordroot.components.navbar.core :as navbar]
   [wordroot.components.pages.core :as pages]
   [wordroot.constants :as constants]))

(defn mount-components
  []
  (r/render [navbar/navbar-component] constants/navbar-element)
  (r/render [pages/page-container] constants/application-element))
