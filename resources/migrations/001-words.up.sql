CREATE TABLE words (
id SERIAL PRIMARY KEY,
word VARCHAR(64) NOT NULL UNIQUE,
meaning VARCHAR(512) NOT NULL
);

CREATE TABLE languages (
id SERIAL PRIMARY KEY,
name VARCHAR(64) NOT NULL
);

CREATE TABLE roots (
id SERIAL PRIMARY KEY,
word VARCHAR(64) NOT NULL,
meaning VARCHAR(512) NOT NULL,
language_id INTEGER REFERENCES languages(id)
);

CREATE TABLE word_parts (
id SERIAL PRIMARY KEY,
part VARCHAR(32) NOT NULL,
word_id INTEGER REFERENCES words(id),
position_index smallint NOT NULL
);

CREATE TABLE word_parts_roots (
word_part_id INTEGER REFERENCES word_parts(id),
root_id INTEGER REFERENCES roots(id)
);
