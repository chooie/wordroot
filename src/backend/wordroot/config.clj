(ns wordroot.config
  (:require
   [immuconf.config :as immuconf]))

(def profile-configs-directory-path "secrets/profile-configs/")

(defn get-config-file-path-for-profile
  [profile-key]
  (let [get-path-to (fn [file-name]
                      (str profile-configs-directory-path file-name))]
    (case profile-key
      :production           (get-path-to "production.edn")
      :dev                  (get-path-to "dev.edn")
      :automated-tests      (get-path-to "automated-tests.edn")
      :unrecognised-profile (throw
                              (Exception. (str "Unrecognised profile, '"
                                            profile-key "'"))))))

(defn get-config-with-profile
  [profile-key]
  (immuconf/load
    "resources/config.edn"
    (get-config-file-path-for-profile profile-key)))
