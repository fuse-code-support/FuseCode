(ns fusion.launch
  (:require [clojure.tools.logging  :as log]
            [clojure.java.io :as io]
            [clj-jgit.porcelain :as jgit]

            [fusion.files :as file]
            [fusion.config :as config]))


;; TODO : add logging
(defn bootplugin-dir []
  (str @config/fusion-plugin-dir) "/" (-> @config/settings :fusion-bootplugin-dir))


(defn git-credentials-present? []
  (if (-> @config/settings :git-credentials :add-your-credentials)
    (do
      (log/warn "No SSH credentials detected.  Edit " (config/fusion-configfilename) " and remove the :add-your-credentials key-value pair after adding your Github credentials.")
      (log/info "If you wish, you can fork repositories mentioned in " (config/fusion-configfilename) " and edit " (config/fusion-configfilename) " to point to your versions.")
      false)

    true))


(defn update-boot-plugin []
  (log/info @config/settings " directory found.  Updating.")

  (if (git-credentials-present?)
    (jgit/with-identity (-> @config/settings :git-credentials)
      (jgit/git-pull (jgit/load-repo (bootplugin-dir))))

    (jgit/git-pull (jgit/load-repo (bootplugin-dir)))))


(defn clone-boot-plugin []
  (log/info "No " @config/settings " directory found.  Initializing for first run.")

  (if (git-credentials-present?)
    (jgit/with-identity (-> @config/settings :git-credentials)
      (jgit/git-clone-full (-> @config/settings :boot-plugin-repo) (bootplugin-dir)))

    (jgit/git-clone-full (-> @config/settings :boot-plugin-repo) (bootplugin-dir))))


(defn download-or-update-boot-plugin []
  (if (file/exists (bootplugin-dir))
    (update-boot-plugin)
    (clone-boot-plugin)))


(defn launch-boot-plugin [])


(defn start []
  (download-or-update-boot-plugin)
  (launch-boot-plugin))
