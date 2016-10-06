(ns wordroot.components.pages.home.words-navbar)

(defn component
  [words current-word-index-atom menu-is-open?-atom]
  (let [current-index @current-word-index-atom
        menu-is-open? @menu-is-open?-atom]
    [:aside.words-side-bar
     {:class (doall (when (not menu-is-open?)
                      "hidden"))}
     [:ul.words
      (map-indexed
        (fn [index word]
          ^{:key index}
          [:li.word
           {:on-click #(reset! current-word-index-atom index)
            :class    (doall (when (= current-index index)
                               "active"))}
           (apply str (:parts word))])
        words)]]))
