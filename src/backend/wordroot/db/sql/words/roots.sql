-- :name insert-root! :insert
-- :doc Insert a root
INSERT INTO roots (word, meaning, language_id)
VALUES (:word, :meaning, :language_id);

-- :name get-root-by-word :? :1
-- :doc Get root id by the given word
SELECT * FROM roots
WHERE word = :word;
