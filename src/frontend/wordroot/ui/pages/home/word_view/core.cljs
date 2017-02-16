(ns wordroot.ui.pages.home.word-view.core
  (:require
   [reagent.core :as reagent]
   [wordroot.ui.pages.home.word-view.header :as header]
   [wordroot.ui.pages.home.word-view.roots-list :as roots-list]))

(defn component
  [word]
  (let [root-to-show-index-atom (reagent/atom nil)]
    [:div
     [header/component (:parts word) root-to-show-index-atom]
     [:div.roots.center
      [roots-list/component (:parts word) root-to-show-index-atom]]
     [:div.center
      [:p (:meaning word)]]]))
