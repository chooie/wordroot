(ns wordroot-tasks.db
  (:require
   [boot.core :as boot]
   [wordroot.db.migration-management :as migration-management]
   [wordroot.db.seed-management :as seed-management]))

(boot/deftask run-migrations!
  []
  (fn [next-handler]
    (fn [fileset]
      (migration-management/migrate!)
      (println "Ran migrations...")
      (next-handler fileset))))

(boot/deftask rollback-migrations!
  []
  (fn [next-handler]
    (fn [fileset]
      (migration-management/rollback!)
      (println "Migrations rolled back...")
      (next-handler fileset))))

(boot/deftask seed-database!
  []
  (fn [next-handler]
    (fn[fileset]
      (seed-management/seed-database!)
      (println "Database seeded...")
      (next-handler fileset))))

(boot/deftask reset-database-and-seed!
  []
  (comp
    (rollback-migrations!)
    (run-migrations!)
    (seed-database!)))
