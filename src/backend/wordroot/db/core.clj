(ns wordroot.db.core
  (:require
   [conman.core :as conman]
   [wordroot.db.config :as db-config]
   [wordroot.db.words.words :as words]))

(defn persist-language-and-return-id!
  [language]
  (let [language-result (words/insert-language! {:name language})]
    (:id language-result)))

(defn get-existing-language-id-or-persist-entry
  [language]
  (let [language-id (words/get-language-id-by-name {:name language})]
    (if (nil? language-id)
      (persist-language-and-return-id! language)
      language-id)))
(defn insert-part!
  [word-id index {:keys [part root]}]
  (let [part-result (words/insert-word-part!
                      {:word_id        word-id
                       :part           part
                       :position_index index})]
    (if root
      (let [root-language-id          (get-existing-language-id-or-persist-entry
                                        (:language root))
            root-result               (words/insert-root!
                                        {:word        (:word root)
                                         :meaning     (:meaning root)
                                         :language_id root-language-id})
            part-id                   (:id part-result)
            root-id                   (:id root-result)
            root-part-relation-result (words/insert-word-part-and-root-association!
                                        {:word_part_id part-id
                                         :root_id      root-id})]
        root-part-relation-result)
      part-result)))

(defn insert-parts!
  [word-id parts]
  (map-indexed
    (fn [index part]
      (insert-part! word-id index part))
    parts))

(defn insert-word!
  [{:keys [word meaning parts]}]
  (conman/with-transaction [db-config/*db*]
    (let [word-result (words/insert-word! {:word word :meaning meaning})
          word-id     (:id word-result)]
      (insert-parts! word-id parts))))

(defn seed-words!
  []
  (insert-word!
    {:word    "thisisareallylongword"
     :meaning "Some meaning about a really long word"
     :parts   [{:part "this"
                :root {:word     "root1"
                       :meaning  "meaning1"
                       :language "language1"}}
               {:part "is"}
               {:part "a"}
               {:part "really"
                :root {:word     "root2"
                       :meaning  "meaning2"
                       :language "language2"}}
               {:part "long"}
               {:part "word"
                :root {:word     "root3"
                       :meaning  "meaning3"
                       :language "language3"}}]}))
