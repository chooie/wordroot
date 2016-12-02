(ns wordroot.db.words.words
  (:require
   [clojure.java.jdbc :as jdbc]
   [com.stuartsierra.component :as component]
   [hugsql.core :as hugsql]))

(doseq [hugsql-path ["sql/words/words.sql"
                     "sql/words/languages.sql"
                     "sql/words/roots.sql"
                     "sql/words/word_parts.sql"
                     "sql/words/word_parts_roots.sql"]]
  (hugsql/def-db-fns hugsql-path))


(defn persist-language-and-return-id!
  [db-connection language]
  (let [language-result (insert-language! db-connection {:name language})]
    (:id language-result)))

(defn get-existing-language-id-or-persist-entry!
  [db-connection language]
  (let [language-result (get-language-by-name db-connection {:name language})]
    (if (nil? language-result)
      (persist-language-and-return-id! db-connection language)
      (:id language-result))))

(defn persist-root-and-return-id!
  [db-connection root]
  (let [root-result (insert-root! db-connection root)]
    (:id root-result)))

(defn get-existing-root-id-or-persist-root!
  [db-connection root]
  (let [root-result (get-root-by-word db-connection {:word (:word root)})]
    (if (nil? root-result)
      (persist-root-and-return-id! db-connection root)
      (:id root-result))))

(defn insert-part!
  [db-connection word-id index {:keys [part root] :as part-map}]
  (let [part-result (insert-word-part! db-connection
                      {:word_id        word-id
                       :part           part
                       :position_index index})]
    (if root
      (let [part-id                   (:id part-result)
            root-language-id          (get-existing-language-id-or-persist-entry!
                                        db-connection
                                        (:language root))
            root-less-language        (dissoc root :language)
            root-with-language-id     (assoc root-less-language
                                        :language_id root-language-id)
            root-id                   (get-existing-root-id-or-persist-root!
                                        db-connection
                                        root-with-language-id)
            root-part-relation-result (insert-word-part-and-root-association!
                                        db-connection
                                        {:word_part_id part-id
                                         :root_id      root-id})]
        root-part-relation-result)
      part-result)))

(defn insert-parts!
  [db-connection word-id parts]
  (doall
    (map-indexed
      (fn [index part]
        (insert-part! db-connection word-id index part))
      parts)))

(defn persist-word!
  [db-connection {:keys [word meaning parts]}]
  (jdbc/with-db-transaction [transaction-connection db-connection]
    (let [word-result (insert-word! transaction-connection
                        {:word word :meaning meaning})
          word-id     (:id word-result)]
      (insert-parts! db-connection word-id parts))))

(defn prepare-word
  [transaction-connection word-id word]
  (let [parts (get-word-parts-for-word-id transaction-connection
                {:word_id word-id})

        part-ids-to-root-ids
        (doall
          (map
            (fn [{:keys [id]}]
              (get-word-part-and-root-association-for-word-part-id
                transaction-connection
                {:word_part_id id}))
            parts))

        roots     (doall
                    (map (fn [{:keys [root_id]}]
                           (when root_id
                             (get-root-by-id
                               transaction-connection
                               {:id root_id})))
                      part-ids-to-root-ids))
        languages (doall
                    (map
                      (fn [{:keys [language_id]}]
                        (when language_id
                          (get-language-by-id
                            transaction-connection
                            {:id language_id})))
                      roots))

        roots-with-language-resolved
        (map (fn [root {:keys [name] :as language}]
               (when root
                 (let [root-less-lang-id (dissoc root :language_id)]
                   (assoc root-less-lang-id :language name))))
          roots languages)

        parts-with-roots          (mapv
                                    (fn [part root]
                                      (assoc part :root root))
                                    parts roots-with-language-resolved)
        word-with-parts-and-roots (assoc word :parts parts-with-roots)]
    word-with-parts-and-roots))

(defn get-word-by-word-name
  [db-connection word-name]
  (jdbc/with-db-transaction [transaction-connection db-connection]
    (let [word    (get-word transaction-connection {:word word-name})
          word-id (:id word)]
      (when word-id
        (prepare-word transaction-connection word-id word)))))

(defn get-words-index
  [db-connection]
  (map :word (get-word-names db-connection)))

(defn get-words
  [db-connection]
  (let [word-names (get-words-index db-connection)
        words      (doall
                     (mapv
                       (fn [word-name]
                         (get-word-by-word-name db-connection word-name))
                       word-names))]
    words))
