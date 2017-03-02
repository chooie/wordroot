(ns wordroot-tasks.db
  (:require
   [boot.core :as boot]
   [wordroot.config :as config]
   [wordroot.db.migration-management :as migration-management]
   [wordroot.db.seed-management :as seed-management]
   [wordroot-tasks.util :as wordroot-util]))

(defn- run-migrations-fn
  [profile-key]
  (let [config (config/get-config-with-profile profile-key)]
    (migration-management/migrate! (:db config))
    (println "Ran migrations...")))

(defn- rollback-migrations-fn
  [profile-key]
  (let [config (config/get-config-with-profile profile-key)]
    (migration-management/rollback! (:db config))
    (println "Migrations rolled back...")))

(defn- seed-database-fn
  [profile-key]
  (let [config (config/get-config-with-profile profile-key)]
    (seed-management/seed-database! (:db config))
    (println "Database seeded...")))

(defn- reset-and-seed-database-fn
  [profile-key]
  (rollback-migrations-fn profile-key)
  (run-migrations-fn profile-key)
  (seed-database-fn profile-key))

(boot/deftask run-migrations!
  [e environment VAL kw "Environment to use #{dev production}"]
  (comp
    (wordroot-util/data-readers)
    (fn [next-task]
      (fn [fileset]
        (run-migrations-fn environment)
        (next-task fileset)))))

(boot/deftask reset-and-seed-database!
  [e environment VAL kw "Environment to use #{dev production}"]
  (comp
    (wordroot-util/data-readers)
    (fn [next-task]
      (fn [fileset]
        (reset-and-seed-database-fn environment)
        (next-task fileset)))))
