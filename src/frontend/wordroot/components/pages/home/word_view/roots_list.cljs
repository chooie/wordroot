(ns wordroot.components.pages.home.word-view.roots-list)

(defn root-component
  [root-index root-to-show-index root]
  (let [component-key (:word root)]
    (when (= root-index root-to-show-index)
      ^{:key component-key}
      [:div
       [:ul
        [:li (:word root)]
        [:li (:meaning root)]
        [:li (:language root)]
        [:hr]]])))

(defn component
  [parts root-to-show-index-atom]
  (let [parts-with-roots   (filter
                             (fn [{:keys [root]}]
                               ((complement nil?) root))
                             parts)
        root-to-show-index @root-to-show-index-atom]
    [:div
     (map-indexed
       (fn [i part]
         (let [root (:root part)]
           (root-component i root-to-show-index root)))
       parts-with-roots)]))
