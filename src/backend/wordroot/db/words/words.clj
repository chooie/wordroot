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

(defn persist-language-and-return-id!
  [language]
  (let [language-result (insert-language! {:name language})]
    (:id language-result)))

(defn get-existing-language-id-or-persist-entry!
  [language]
  (let [language-result (get-language-by-name {:name language})]
    (if (nil? language-result)
      (persist-language-and-return-id! language)
      (:id language-result))))

(defn persist-root-and-return-id!
  [root]
  (let [root-result (insert-root! root)]
    (:id root-result)))

(defn get-existing-root-id-or-persist-root!
  [root]
  (let [root-result (get-root-by-word {:word (:word root)})]
    (if (nil? root-result)
      (persist-root-and-return-id! root)
      (:id root-result))))

(defn insert-part!
  [word-id index {:keys [part root]}]
  (let [part-result (insert-word-part!
                      {:word_id        word-id
                       :part           part
                       :position_index index})]
    (if root
      (let [part-id                   (:id part-result)
            root-language-id          (get-existing-language-id-or-persist-entry!
                                        (:language root))
            root-less-language        (dissoc root :language)
            root-with-language-id     (assoc root-less-language
                                        :language_id root-language-id)
            root-id                   (get-existing-root-id-or-persist-root!
                                        root-with-language-id)
            root-part-relation-result (insert-word-part-and-root-association!
                                        {:word_part_id part-id
                                         :root_id      root-id})]
        root-part-relation-result)
      part-result)))

(defn insert-parts!
  [word-id parts]
  (doall
    (map-indexed
      (fn [index part]
        (insert-part! word-id index part))
      parts)))

(defn persist-word!
  [{:keys [word meaning parts]}]
  (conman/with-transaction [db-config/*db*]
    (let [word-result (insert-word! {:word word :meaning meaning})
          word-id     (:id word-result)]
      (insert-parts! word-id parts))))
