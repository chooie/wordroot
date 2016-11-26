(ns wordroot.components.pages.home.word-view.core
  (:require
   [wordroot.components.pages.home.word-view.header :as header]
   [wordroot.components.pages.home.word-view.roots-list :as roots-list]))

(defn component
  [word]
  [:div
   [header/component (:parts word)]
   [:div.roots
    [roots-list/component (:parts word)]]
   [:div.center
    [:p (:meaning word)]]])
