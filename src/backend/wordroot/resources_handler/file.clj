(ns wordroot.resources-handler.file
  (:require
   [clojure.java.io :as io]))

(defn is-existing-directory?
  [^String path]
  (.isDirectory (io/file path)))

(defn file-exists?
  [^String path]
  (.exists (io/as-file path)))

(defn delete-file
  [^String path]
  (io/delete-file (io/file path)))

(defn delete-recursively
  [^String path]
  (let [recursive-delete-fn (fn [recursive-delete-fn file]
                              (when (.isDirectory file)
                                (doseq [next-file (.listFiles file)]
                                  (recursive-delete-fn recursive-delete-fn
                                    next-file)))
                              (clojure.java.io/delete-file file))]
    (recursive-delete-fn recursive-delete-fn
      (clojure.java.io/file path))))

(defn create-directory
  [^String path]
  (let [path (.toPath (io/file path))]
    (java.nio.file.Files/createDirectory
      path (into-array java.nio.file.attribute.FileAttribute []))))

(defn create-file-at
  [^String path contents]
  (spit path contents))
