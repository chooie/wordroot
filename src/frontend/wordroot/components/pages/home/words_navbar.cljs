(ns wordroot.components.pages.home.words-navbar)

(defn component
  [words current-word-index-atom menu-is-open?-atom]
  (let [current-index @current-word-index-atom
        menu-is-open? @menu-is-open?-atom]
    [:aside.words-side-bar
     {:class (when (not menu-is-open?)
               "hidden")}
     [:div.right
      [:div.close-button-mobile
       {:on-click #(reset! menu-is-open?-atom false)}
       "X"]]
     [:ul.words
      (map-indexed
        (fn [index word]
          ^{:key index}
          [:li.word
           {:on-click (fn []
                        (when (not= current-index index)
                          (reset! current-word-index-atom index)
                          (reset! menu-is-open?-atom false)))
            :class    (when (= current-index index)
                        "active")}
           (apply str (:parts word))])
        words)]]))
