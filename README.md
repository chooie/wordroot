# Wordroot
Teaches the meaning of words and their roots.

## Setup
### Database
- Initialise the database
  * `initdb -D /usr/local/pgsql/data`
- Start database server
  * `pg_ctl -D /usr/local/pgsql/data -l logfile start`
- Connect to the database
  * `psql -d postgres`
- Create the database
  * `CREATE DATABASE wordroot_database;`
- Create the role for the database
  * `CREATE ROLE wordroot_user WITH LOGIN PASSWORD 'weak_password';`
- Run the migrations and seed the database
  * Start the interactive development application:
    - `boot dev`
  * Connect to the running repl (I use cider-connect in Emacs)
  * Run the migration and seed tasks
    - `(wordroot-db/reset-database-and-seed!)`
