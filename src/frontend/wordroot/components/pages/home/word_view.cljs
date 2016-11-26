(ns wordroot.components.pages.home.word-view)

(def colour-classes ["colour-1" "colour-2" "colour-3"])
(def line-classes ["underline" "overline"])

(defn get-class-at-index
  [classes index]
  (when index
    (let [modded-i (mod index (count classes))
          class    (get classes modded-i)]
      class)))

(defn get-word-part-classes
  [colour-classes list-classes index-in-word root-index]
  (let [line-class   (get-class-at-index line-classes root-index)
        colour-class (get-class-at-index colour-classes root-index)]
    (clojure.string/join " " [line-class colour-class])))

(defn prepare-part
  [part index]
  (if (= index 0)
    (clojure.string/capitalize part)
    part))

(defn word-part-component
  [colour-classes line-classes part index-in-word
   root-index]
  ^{:key part}
  [:li.word-part
   {:class (get-word-part-classes colour-classes line-classes index-in-word
             root-index)}
   (prepare-part part index-in-word)])

(defn word-parts-header
  [word-parts]
  {:pre [(> (count word-parts) 0)]}
  (let [next-index               (atom 0)
        words-with-roots-indexes (map
                                   (fn [word-part]
                                     (if (:root word-part)
                                       (do
                                         (swap! next-index inc)
                                         [word-part (dec @next-index)])
                                       [word-part nil]))
                                   word-parts)]
    [:div.word-header.center
     [:ul.word-parts
      (map-indexed
        (fn [index-in-word [word-part root-index]]
          (let [{:keys [part]} word-part]
            (word-part-component colour-classes line-classes part index-in-word
              root-index)))
        words-with-roots-indexes)]]))

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

(defn roots-list-component
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

(defn component
  [word]
  [:div
   [word-parts-header (:parts word)]
   [:div.roots
    [roots-list-component (:parts word)]]
   [:div.center
    [:p (:meaning word)]]])
