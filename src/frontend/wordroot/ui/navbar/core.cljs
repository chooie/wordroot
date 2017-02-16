(ns wordroot.ui.navbar.core)

(defn make-active-when-active
  [current-route route-keyword]
  (when (= current-route route-keyword)
    "active"))

(defn link
  [current-route-atom route-prefix href label route-keyword]
  [:li.site-link
   [:a
    {:href     (str route-prefix href)
     :class    (make-active-when-active @current-route-atom route-keyword)
     :on-click #(reset! current-route-atom route-keyword)}
    label]])

(defn navbar-component
  [current-route-atom route-prefix route-paths]
  [:nav.navbar
   [:div.row
    [:ul.site-links
     (let [home (:home route-paths)]
       [link
        current-route-atom
        route-prefix
        (get home :uri)
        (get home :label)
        :home])
     (let [about (:about route-paths)]
       [link
        current-route-atom
        route-prefix
        (get about :uri)
        (get about :label)
        :about])]]])
