(ns wordroot.db
  (:require [clojure.java.jdbc :as j]))

(def postgres-uri
  {:connection-uri (str
                     "postgresql://wordroot_user:weak_password"
                     "@localhost:5432/wordroot_database")})
