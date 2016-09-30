(ns wordroot.components.pages.home.core
  (:require [reagent.session :as session]))

(def classes ["colour-1" "colour-2" "colour-3"])

(defn isNan?
  [n]
  (js/isNaN n))

(defn get-colour-class-at-index
  [classes index]
  (let [modded-i     (mod index (count classes))
        colour-class (get classes modded-i)]
    colour-class))

(defn prepare-part
  [part index]
  (if (= index 0)
    (clojure.string/capitalize part)
    part))

(defn word-part
  [classes part index]
  ^{:key part}
  [:li.word-part
   {:class (get-colour-class-at-index classes index)}
   (prepare-part part index)])

(defn word-parts-header
  [word-parts]
  {:pre [(> (count word-parts) 0)]}
  [:div.header
   [:ul.word-parts
    (map-indexed
      (fn [i part]
        (word-part classes part i))
      word-parts)]])

(defn home-page
  []
  (let [word       (session/get :word)
        word-parts (:parts word)]
    [:div
     [:h1 "Home"]
     [:div
      [word-parts-header word-parts]]]))
