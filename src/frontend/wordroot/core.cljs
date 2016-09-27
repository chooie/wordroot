(ns wordroot.core
  (:require
   [wordroot.components.core :as components]
   [wordroot.routing :as routing]))

(defn init!
  []
  (enable-console-print!)
  (routing/init!)
  (components/mount-components))
