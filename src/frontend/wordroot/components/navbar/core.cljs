(ns wordroot.components.navbar.core
  (:require
   (reagent.session :as session)
   (wordroot.constants :as constants)))

(defn make-active-when-active
  [page-keyword label]
  (let [page-string (name page-keyword)
        to-lower    clojure.string/lower-case
        page        (to-lower page-string)
        label       (to-lower label)]
    (when (= page label)
      "active")))

(defn link
  [href label]
  [:li.site-link
   [:a
    {:href  (str constants/secretary-prefix href)
     :class (make-active-when-active (session/get :page) label)}
    label]])

(defn navbar-component
  []
  [:nav.navbar
   [:div.row
    [:ul.site-links
     [link (:home constants/paths) "Home"]
     [link (:about constants/paths) "About"]]]])
