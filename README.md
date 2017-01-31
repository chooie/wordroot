# Wordroot
Teaches the meaning of words and their roots.

## Deploy
- Use Docker with Docker Compose
- Build the images
    - `docker-compose build.`
- Start up containers
    - `docker-compose up -d`
- For the first deploy, you must initialise the database with some seed data
    - `docker exec -it wordroot_web_1 bash`
    - `boot get-build-ready-with-dummy-data`

## Setup
### Dev
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
    * Start the interactive development application
        - `boot dev`
    * Connect to the running repl (I use cider-connect in Emacs)
    * Run the migration and seed tasks
        - `(wordroot-db/reset-database-and-seed!)`
