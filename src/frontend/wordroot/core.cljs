(ns wordroot.core
  (:require
   [wordroot.components.core :as components]
   [wordroot.routing :as routing]))

(routing/init!)

(defn init!
  []
  (enable-console-print!)
  (components/mount-components))
