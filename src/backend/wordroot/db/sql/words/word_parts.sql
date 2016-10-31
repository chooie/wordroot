-- :name insert-word-part! :! :n
-- :doc Insert a word part
INSERT INTO word_parts (word_id, part, position_index)
VALUES (:word_id, :part, :position_index);

-- :name get-word-parts-for-word-id :? :n
-- :doc Get words parts associated with the given word-id
SELECT * FROM word_parts
WHERE word_id = :word_id
ORDER BY position_index ASC;
