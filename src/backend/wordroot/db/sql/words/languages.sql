-- :name insert-language! :! :n
-- :doc Insert a language
INSERT INTO languages (name)
VALUES (:name);

-- :name get-language-by-id :? :1
-- :doc Get language name by id
SELECT * FROM languages
WHERE id = :id

-- :name get-language-id-by-name :? :1
-- :doc Get language id with the given name
SELECT * FROM languages
WHERE name = :name
