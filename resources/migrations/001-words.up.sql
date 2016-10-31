CREATE TABLE words (
  id      SERIAL       PRIMARY KEY,
  word    VARCHAR(64)  NOT NULL UNIQUE,
  meaning VARCHAR(512) NOT NULL UNIQUE
);

CREATE TABLE languages (
  id   SERIAL      PRIMARY KEY,
  name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE roots (
  id          SERIAL       PRIMARY KEY,
  word        VARCHAR(64)  NOT NULL UNIQUE,
  meaning     VARCHAR(512) NOT NULL UNIQUE,
  language_id INTEGER      REFERENCES languages(id)
);

CREATE TABLE word_parts (
  id             SERIAL      PRIMARY KEY,
  word_id        INTEGER     REFERENCES words(id),
  part           VARCHAR(32) NOT NULL,
  position_index SMALLINT    NOT NULL,
  CONSTRAINT unique_word_position UNIQUE (word_id, position_index)
);

CREATE TABLE word_parts_roots (
  word_part_id INTEGER REFERENCES word_parts(id),
  root_id      INTEGER REFERENCES roots(id),
  UNIQUE (word_part_id, root_id)
);
