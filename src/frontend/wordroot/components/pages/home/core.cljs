(ns wordroot.components.pages.home.core
  (:require
   [reagent.core :as reagent]
   [reagent.session :as session]
   [wordroot.components.pages.home.word-view.core :as word-view]
   [wordroot.components.pages.home.words-navbar :as words-navbar]))

(defn menu-toggle
  [content menu-is-open?-atom]
  [:div.menu-toggle
   {:class         (when @menu-is-open?-atom
                     "active")
    :on-click      (fn [event]
                     (reset! menu-is-open?-atom (not @menu-is-open?-atom)))
    :on-mouse-down (fn [event]
                     (.preventDefault event))}
   content])

(defn words-menu-toggle
  [menu-is-open?-atom]
  (menu-toggle
    [:p (if @menu-is-open?-atom
          "X"
          "Words")]
    menu-is-open?-atom))

(defonce current-word-index-atom (reagent/atom 0))
(defonce menu-is-open?-atom      (reagent/atom false))

(defn home-page
  []
  (let [words (session/get :words)
        word  (get words @current-word-index-atom)]
    (if words
      [:div
       [words-navbar/component words
        current-word-index-atom
        menu-is-open?-atom]
       [:div.controls
        [words-menu-toggle menu-is-open?-atom]]
       [:h1"Home"]
       [word-view/component word]]
      [:div
       [:h1 "Loading..."]])))
