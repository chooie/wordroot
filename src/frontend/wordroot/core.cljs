(ns wordroot.core
  (:require
   [wordroot.components.core :as components]
   [wordroot.routing :as routing]))

(enable-console-print!)
(routing/init!)

(defn init!
  []
  (components/mount-root!))
