(ns wordroot.dev.core
  (:require
   [wordroot.core :as wr]
   [boot.core :as boot]))

(defn hey
  []
  (println "hey!")
  (wr/foo))

(boot/deftask my-task
  []
  (boot/with-pass-thru fileset
    (println "hey")))
