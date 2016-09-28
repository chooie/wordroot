(ns wordroot.components.navbar.core
  (:require
   (wordroot.constants :as constants)))

(defn link
  [href label]
  [:li.site-link
   [:a
    {:href (str "/" constants/secretary-prefix href)}
    label]])

(defn navbar-component
  []
  [:nav
   [:ul
    [link (:home constants/paths) "Home"]
    [link (:about constants/paths) "About"]]])
