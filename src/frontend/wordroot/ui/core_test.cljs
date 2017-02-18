(ns wordroot.ui.core-test
  (:require
   [com.stuartsierra.component :as component]
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [reagent.core :as r]
   [wordroot.routing :as routing]
   [wordroot.ui.core :as wordroot-core]))

(def routing-component
  (routing/new-routing "localhost" "8000" :home))

(defn create-test-element
  []
  (let [document-body (.-body js/document)
        test-element  (.createElement js/document "div")]
    (.setAttribute test-element "id" "app")
    (.appendChild document-body test-element)
    test-element))

(deftest root-app
  (testing "Frontend can be rendered"
    (let [test-element (create-test-element)
          mounted-app  (r/render
                         (wordroot-core/application-component
                           routing-component)
                         test-element)
          application  (.getElementById js/document "app")]
      (is ((complement nil?) (.-innerHTML application))))))
