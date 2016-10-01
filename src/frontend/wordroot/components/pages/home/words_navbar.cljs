(ns wordroot.components.pages.home.words-navbar)

(defn component
  [words current-word-index-atom]
  [:aside
   [:ul.words
    (map-indexed
      (fn [index word]
        ^{:key index}
        [:li.word
         {:on-click #(reset! current-word-index-atom index)}
         (apply str (:parts word))])
      words)]])
