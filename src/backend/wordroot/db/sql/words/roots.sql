-- :name insert-root! :! :n
-- :doc Insert a root
INSERT INTO roots (word, meaning, language_id)
VALUES (:word, :meaning, :language_id);
