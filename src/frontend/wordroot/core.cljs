(ns wordroot.core
  (:require
   [reagent.session :as session]
   [wordroot.components.core :as components]
   [wordroot.routing :as routing]))

(enable-console-print!)
(routing/init!)
(session/put! :word {:parts       ["my" "test" "word"]
                     :description "This is my test word's description"})

(defn init!
  []
  (components/mount-components))
