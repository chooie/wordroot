(ns wordroot-tasks.db
  (:require
   [wordroot.config :as config]
   [wordroot.db.migration-management :as migration-management]
   [wordroot.db.seed-management :as seed-management]))

(defn run-migrations!
  []
  (let [config (config/get-config)]
    (migration-management/migrate! (:db config))
    (println "Ran migrations...")))

(defn rollback-migrations!
  []
  (let [config (config/get-config)]
    (migration-management/rollback! (:db config))
    (println "Migrations rolled back...")))

(defn seed-database!
  []
  (let [config (config/get-config)]
    (seed-management/seed-database! (:db config))
    (println "Database seeded...")))

(defn reset-database-and-seed!
  []
  (rollback-migrations!)
  (run-migrations!)
  (seed-database!))
