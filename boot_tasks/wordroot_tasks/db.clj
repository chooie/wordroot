(ns wordroot-tasks.db
  (:require
   [wordroot.config :as config]
   [wordroot.db.migration-management :as migration-management]
   [wordroot.db.seed-management :as seed-management]))

(defn run-migrations!
  [profile-key]
  (let [config (config/get-config-with-profile profile-key)]
    (migration-management/migrate! (:db config))
    (println "Ran migrations...")))

(defn rollback-migrations!
  [profile-key]
  (let [config (config/get-config-with-profile profile-key)]
    (migration-management/rollback! (:db config))
    (println "Migrations rolled back...")))

(defn seed-database!
  [profile-key]
  (let [config (config/get-config-with-profile profile-key)]
    (seed-management/seed-database! (:db config))
    (println "Database seeded...")))

(defn reset-and-seed-database!
  [profile-key]
  (rollback-migrations! profile-key)
  (run-migrations! profile-key)
  (seed-database! profile-key))
