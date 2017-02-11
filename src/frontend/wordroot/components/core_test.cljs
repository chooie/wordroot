(ns wordroot.components.core-test
  (:require
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [reagent.core :as r]
   [wordroot.components.core :as wordroot-core]))

(defn create-test-element
  []
  (let [document-body (.-body js/document)
        test-element  (.createElement js/document "div")]
    (.appendChild document-body test-element)
    test-element))

(deftest root-app
  (testing "Reagent element renders correctly"
    (let [test-element (create-test-element)
          mounted-app  (r/render
                         [:div#app "Hey"]
                         test-element)
          application  (.getElementById js/document "app")]
      (is (= "Hey" (.-innerHTML application))))))
