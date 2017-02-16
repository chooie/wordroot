(ns wordroot.ui.pages.home.core
  (:require
   [ajax.core :as ajax]
   [reagent.core :as reagent]
   [reagent.session :as session]
   [wordroot.ui.pages.home.word-view.core :as word-view]
   [wordroot.ui.pages.home.words-navbar :as words-navbar]))

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

(defonce words-atom (reagent/atom nil))
(defonce current-word-index-atom (reagent/atom 0))
(defonce menu-is-open?-atom (reagent/atom false))

(defn get-words!
  [base-url]
  (let [words-url (str base-url "/words")]
    (ajax/GET words-url
      {:handler (fn [words]
                  (reset! words-atom words))})))

(defn home-page
  [base-url]
  (get-words! base-url)
  (let [words @words-atom
        word  (get words @current-word-index-atom)]
    (if words
      [:div
       [words-navbar/component words
        current-word-index-atom
        menu-is-open?-atom]
       [:div.controls
        [words-menu-toggle menu-is-open?-atom]]
       [:h1"Home"]
       [:div.clear]
       [word-view/component word]]
      [:div
       [:h1 "Loading..."]])))
