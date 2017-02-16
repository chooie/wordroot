(ns wordroot.ui.pages.home.words-navbar
  (:require [clojure.string :as string]))

(defn close-button
  [menu-is-open?-atom]
  [:div.right
   [:div.close-button-mobile
    {:on-click #(reset! menu-is-open?-atom false)}
    "X"]])

(defn words-list
  [words current-word-index-atom menu-is-open?-atom]
  (let [current-index @current-word-index-atom]
    [:ul.words
     (map-indexed
       (fn [index word]
         ^{:key index}
         [:li.word
          {:on-click (fn []
                       (when (not= current-index index)
                         (reset! current-word-index-atom index)
                         (reset! menu-is-open?-atom false)))
           :class    (when (= current-index index)
                       "active")}
          (string/capitalize (:word word))])
       words)]))

(defn component
  [words current-word-index-atom menu-is-open?-atom]
  (let [current-index @current-word-index-atom
        menu-is-open? @menu-is-open?-atom]
    [:aside.words-side-bar
     {:class (when (not menu-is-open?)
               "hidden")}
     [close-button menu-is-open?-atom]
     [words-list
      words
      current-word-index-atom
      menu-is-open?-atom]]))
