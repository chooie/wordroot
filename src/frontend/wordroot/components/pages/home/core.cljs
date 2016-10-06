(ns wordroot.components.pages.home.core
  (:require
   [reagent.core :as reagent]
   [reagent.session :as session]
   [wordroot.components.pages.home.words-navbar :as words-navbar]))

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

(defn word-part
  [colour-classes line-classes part index]
  ^{:key part}
  [:li.word-part
   {:class (get-word-part-classes colour-classes line-classes index)}
   (prepare-part part index)])

(defn word-parts-header
  [word-parts]
  {:pre [(> (count word-parts) 0)]}
  [:div.header
   [:ul.word-parts
    (map-indexed
      (fn [i part]
        (word-part colour-classes line-classes part i))
      word-parts)]])

(defn menu-toggle
  [menu-is-open?-atom]
  [:div.menu-toggle
   {:on-click #(reset! menu-is-open?-atom (not @menu-is-open?-atom))}
   [:p "Words"]])

(defn home-page
  []
  (let [current-word-index-atom (reagent/atom 0)
        menu-is-open?-atom      (reagent/atom false)]
    (fn []
      (let [words (session/get :words)
            word  (get words @current-word-index-atom)]
        [:div
         [words-navbar/component words
          current-word-index-atom
          menu-is-open?-atom]
         [menu-toggle menu-is-open?-atom]
         [:p @menu-is-open?-atom]
         [:h1"Home"]
         [:div.center
          [word-parts-header (:parts word)]]
         [:div.center
          [:p (:description word)]]]))))
