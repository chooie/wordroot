(ns wordroot.components.core-test
  (:require
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [reagent.core :as r]
   [wordroot.components.core :as wordroot-core]))

(deftest root-app
  (testing "Reagent element renders correctly"
    (let [document-body (.-body js/document)
          test-element  (.createElement js/document "div")
          _             (.appendChild document-body test-element)
          mounted-app   (r/render
                          [:div#app "Hey"]
                          test-element)
          application   (.getElementById js/document "app")]
      (is (= "Hey" (.-innerHTML application))))))
