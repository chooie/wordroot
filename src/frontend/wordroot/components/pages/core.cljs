(ns wordroot.components.pages.core
  (:require
   [reagent.session :as session]
   [wordroot.components.pages.about.core :as about]
   [wordroot.components.pages.error.core :as error]
   [wordroot.components.pages.home.core :as home]))

(def pages
  {:home  home/home-page
   :about about/about-page
   :error error/error-page})

(defn generate-page-from-session
  []
  (get pages (session/get :page)))

(defn page-container
  []
  [:div
   [(generate-page-from-session)]])
