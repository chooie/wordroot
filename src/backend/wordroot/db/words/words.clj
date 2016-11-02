(ns wordroot.db.words.words
  (:require
   [wordroot.db.config :as db-config]
   [conman.core :as conman]
   [hugsql.core :as hugsql]))

(conman/bind-connection db-config/*db*
  "wordroot/db/sql/words/words.sql"
  "wordroot/db/sql/words/languages.sql"
  "wordroot/db/sql/words/roots.sql"
  "wordroot/db/sql/words/word_parts.sql"
  "wordroot/db/sql/words/word_parts_roots.sql")
