(ns wordroot.views
  (:require
   [hiccup.core :refer [html]]
   [hiccup.page :refer [html5 include-js include-css]]))

(def index-page
  (html
    (html5
      [:head
       [:title "Wordroot"]
       [:meta {:charset "utf-8"}]
       [:meta {:http-equiv "X-UA-Compatible"
               :content    "IE=edge"}]
       [:meta {:name    "viewport"
               :content "width=device-width, initial-scale=1.0"}]
       [:link {:rel  "shortcut icon"
               :type "image/x-icon"
               :href "/favicon.ico?v=1"}]
       (include-css "styles/reset.css")
       (include-css "styles/scss/main.css")]
      [:body
       [:div#application]
       (include-js "js/main.js")])))
