(ns wordroot.db.words
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "wordroot/db/sql/words.sql")
