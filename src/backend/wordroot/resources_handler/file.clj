(ns wordroot.resources-handler.file
  (:require
   [clojure.java.io :as io]))

(defn is-existing-directory?
  [path]
  (.isDirectory (io/file path)))

(defn delete-file
  [path]
  (io/delete-file (io/file path)))

(defn create-directory
  [^String path]
  (let [path (.toPath (io/file path))]
    (java.nio.file.Files/createDirectory
      path (into-array java.nio.file.attribute.FileAttribute []))))
