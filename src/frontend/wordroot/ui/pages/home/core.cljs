(ns wordroot.ui.pages.home.core
  (:require
   [ajax.core :as ajax]
   [reagent.core :as reagent]
   [reagent.session :as session]
   [wordroot.ui.pages.home.word-view.core :as word-view]
   [wordroot.ui.pages.home.words-navbar :as words-navbar]))

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

(defonce words-atom (reagent/atom nil))
(defonce current-word-index-atom (reagent/atom 0))
(defonce menu-is-open?-atom (reagent/atom false))
(defonce get-words-has-been-run? (atom false))

#_(add-watch words-atom :watcher
    (fn [key atom old-state new-state]
      (.log js/console "-- Atom Changed --")
      (.log js/console "key" key)
      (.log js/console "atom" atom)
      (.log js/console "old-state" old-state)
      (.log js/console "new-state" new-state)))

(defn get-words!
  [base-url]
  (let [words-url (str base-url "/words")]
    (ajax/GET words-url
      {:handler       (fn [words]
                        (reset! words-atom words))
       :error-handler (fn [error]
                        (reset! words-atom :error-getting-words)
                        (println "Error getting words:" error))})))

(defn loading-component
  []
  [:div
   [:h1 "Loading..."]])

(defn words-error-component
  []
  [:div
   [:h1 "Error! We couldn't get any words :("]])

(defn render-words
  [words active-word]
  (if (= words :error-getting-words)
    words-error-component
    (if (= (count words) 0)
      [:h1 "There aren't any words to show..."]
      [:div
       [words-navbar/component words
        current-word-index-atom
        menu-is-open?-atom]
       [:div.controls
        [words-menu-toggle menu-is-open?-atom]]
       [:h1 "Home"]
       [:div.clear]
       [word-view/component active-word]])))

(defn home-page
  [base-url]
  (when (not @get-words-has-been-run?)
    (get-words! base-url)
    (reset! get-words-has-been-run? true))
  (let [words       @words-atom
        active-word (get words @current-word-index-atom)]
    (if (nil? words)
      [loading-component]
      [render-words words active-word])))
