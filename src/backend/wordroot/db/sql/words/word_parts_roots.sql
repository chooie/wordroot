-- :name insert-word-part-and-root-association! :insert
-- :doc Insert an association between a word part and root
INSERT INTO word_parts_roots (word_part_id, root_id)
VALUES (:word_part_id, :root_id);

-- :name get-word-part-and-root-association-for-word-part-id :? :1
-- :doc Get word part and root association for record that matches word part id
SELECT * FROM word_parts_roots
WHERE word_part_id = :word_part_id;
