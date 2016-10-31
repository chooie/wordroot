-- :name insert-word! :! :n
-- :doc Insert a word
INSERT INTO words (word, meaning)
VALUES (:word, :meaning);

-- :name insert-words! :! :n
-- :doc Insert multiple words
INSERT INTO words (word, meaning)
VALUES :tuple*:words;

-- :name get-word :? :1
-- :doc Get word by name
SELECT * FROM words
WHERE word = :word;
