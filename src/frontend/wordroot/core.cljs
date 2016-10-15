(ns wordroot.core
  (:require
   [reagent.session :as session]
   [wordroot.components.core :as components]
   [wordroot.routing :as routing]))

(enable-console-print!)
(routing/init!)
(session/put! :words [{:parts       ["my" "test" "word"]
                       :description "This is my test word's description"}
                      {:parts       ["this" "is" "a" "word"]
                       :description "Just another word"}])

(defn init!
  []
  (components/mount-root!))
