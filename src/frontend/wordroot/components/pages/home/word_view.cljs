(ns wordroot.components.pages.home.word-view)

(def colour-classes ["colour-1" "colour-2" "colour-3"])
(def line-classes ["underline" "overline"])

(defn isNan?
  [n]
  (js/isNaN n))

(defn get-class-at-index
  [classes index]
  (let [modded-i (mod index (count classes))
        class    (get classes modded-i)]
    class))

(defn get-word-part-classes
  [colour-classes list-classes index]
  (let [colour-class (get-class-at-index colour-classes index)
        line-class   (get-class-at-index line-classes index)]
    (clojure.string/join " " [colour-class line-class])))

(defn prepare-part
  [part index]
  (if (= index 0)
    (clojure.string/capitalize part)
    part))

(defn word-part-component
  [colour-classes line-classes part index]
  ^{:key part}
  [:li.word-part
   {:class (get-word-part-classes colour-classes line-classes index)}
   (prepare-part part index)])

(defn word-parts-header
  [word-parts]
  {:pre [(> (count word-parts) 0)]}
  [:div.word-header.center
   [:ul.word-parts
    (map-indexed
      (fn [i word-part]
        (let [{:keys [part root]} word-part]
          (word-part-component colour-classes line-classes part i)))
      word-parts)]])

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
