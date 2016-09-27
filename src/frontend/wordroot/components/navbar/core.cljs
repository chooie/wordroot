(ns wordroot.components.navbar.core
  (:require
   (wordroot.constants :as constants)))

(defn link
  [href label]
  [:li
   [:a
    {:href (str "/#" href)}
    label]])

(defn navbar-component
  []
  [:div
   [:ul
    [link (:home constants/paths) "Home"]
    [link (:about constants/paths) "About"]]])
