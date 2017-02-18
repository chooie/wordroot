(ns wordroot.views
  (:require
   [hiccup.core :refer [html]]
   [hiccup.element :refer [javascript-tag]]
   [hiccup.page :refer [html5 include-js include-css]]))

(defn index-page
  [host port]
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
       (javascript-tag
         (str
           "env = {"
           "host: " "'" host "',\n"
           "port: " "'" port "'"
           "};"))
       (include-js "js/main.js")])))

(defn error-page
  [host port]
  (html
    (html5
      [:head
       [:title "Wordroot - 404"]
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
       [:h1 "404 - Page not found"]
       [:a
        {:href (str "http://" host ":" port)}
        "Go Home"]])))
