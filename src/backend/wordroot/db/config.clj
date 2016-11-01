(ns wordroot.db.config
  (:require [conman.core :as conman]))

(def pool-spec
  {:init-size  1
   :min-idle   1
   :max-idle   4
   :max-active 32
   :jdbc-url   "jdbc:postgresql://localhost:5432/wordroot_database?user=wordroot_user&password=weak_password"})

(def ^:dynamic *db* (conman/connect! pool-spec))
