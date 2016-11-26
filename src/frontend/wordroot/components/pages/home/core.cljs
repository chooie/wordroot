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

(defn menu-toggle
  [content menu-is-open?-atom]
  [:div.menu-toggle
   {:class         (when @menu-is-open?-atom
                     "active")
    :on-click      (fn [event]
                     (reset! menu-is-open?-atom (not @menu-is-open?-atom)))
    :on-mouse-down (fn [event]
                     (.preventDefault event))}
   content])

(defn words-menu-toggle
  [menu-is-open?-atom]
  (menu-toggle
    [:p (if @menu-is-open?-atom
          "X"
          "Words")]
    menu-is-open?-atom))

(defonce current-word-index-atom (reagent/atom 0))
(defonce menu-is-open?-atom      (reagent/atom false))

(defn home-page
  []
  (let [words (session/get :words)
        word  (get words @current-word-index-atom)]
    (if words
      [:div
       [words-navbar/component words
        current-word-index-atom
        menu-is-open?-atom]
       [:div.controls
        [words-menu-toggle menu-is-open?-atom]]
       [:h1"Home"]
       [word-parts-header (:parts word)]
       [:div.roots
        (map
          (fn [part]
            (let [root (:root part)]
              (when root
                [:div
                 [:ul
                  [:li (:word root)]
                  [:li (:meaning root)]
                  [:li (:language root)]]
                 [:hr]])))
          (:parts word))]
       [:div.center
        [:p (:meaning word)]]]
      [:div
       [:h1 "Loading..."]])))
