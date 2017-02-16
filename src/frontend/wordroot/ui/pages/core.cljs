(ns wordroot.ui.pages.core
  (:require
   [com.stuartsierra.component :as component]
   [wordroot.ui.pages.about.core :as about]
   [wordroot.ui.pages.error.core :as error]
   [wordroot.ui.pages.home.core :as home]))

(defn generate-page-from-current-route
  [routing-component]
  (let [current-route-atom (:current-route-atom routing-component)
        current-route      @current-route-atom]
    (case current-route
      :home  (home/home-page (:base-url routing-component))
      :about (about/about-page)
      :error (error/error-page))))

(defn page-container
  [routing-component]
  [:div.page-container
   (generate-page-from-current-route routing-component)])
