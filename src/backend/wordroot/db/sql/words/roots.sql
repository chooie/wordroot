-- :name insert-root! :insert
-- :doc Insert a root
INSERT INTO roots (word, meaning, language_id)
VALUES (:word, :meaning, :language_id);
