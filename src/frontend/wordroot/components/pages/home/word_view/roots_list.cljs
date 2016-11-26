(ns wordroot.components.pages.home.word-view.roots-list)

(defn root-component
  [root]
  (let [component-key (:word root)]
    ^{:key component-key}
    [:div
     [:ul
      [:li (:word root)]
      [:li (:meaning root)]
      [:li (:language root)]
      [:hr]]]))

(defn component
  [parts]
  (let [parts-with-roots (filter
                           (fn [{:keys [root]}]
                             ((complement nil?) root))
                           parts)]
    [:div
     (map
       (fn [part]
         (let [root (:root part)]
           (root-component root)))
       parts-with-roots)]))
