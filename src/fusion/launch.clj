(ns fusion.launch
  (:require [clojure.java.io :as io]
            [clj-jgit.porcelain :as jgit]

            [fusion.files :as file]
            [fusion.config :as config]))


(defn bootplugin-dir []
  (str @config/fusion-plugin-dir) "/" (-> @config/settings :fusion-bootplugin-dir))


(defn update-boot-plugin []
  (if (-> @config/settings :git-credentials :add-your-credentials)
    (jgit/git-pull (jgit/load-repo (bootplugin-dir)))
    (jgit/with-identity (-> @config/settings :git-credentials)
      (jgit/git-pull (jgit/load-repo (bootplugin-dir))))))


(defn clone-boot-plugin []
  (if (-> @config/settings :git-credentials :add-your-credentials)
    (jgit/git-clone-full (-> @config/settings :boot-plugin-repo) (bootplugin-dir))
    (jgit/with-identity (-> @config/settings :git-credentials)
      (jgit/git-clone-full (-> @config/settings :boot-plugin-repo) (bootplugin-dir)))))


(defn download-or-update-boot-plugin []
  (if (file/exists (bootplugin-dir))
    (update-boot-plugin)
    (clone-boot-plugin)))


(defn launch-boot-plugin [])


(defn start []
  (download-or-update-boot-plugin)
  (launch-boot-plugin))
