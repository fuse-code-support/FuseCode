(ns fusion.launch
  (:require [clojure.tools.logging  :as log]
            [clojure.java.io :as io]
            [clj-jgit.porcelain :as jgit]

            [fusion.oo :refer [=>]]
            [fusion.patterns :refer [letfn-map]]
            [fusion.files :as file]
            [fusion.config :as config]))


(def git-provider
  (letfn-map
   [(plugin-dir [self] (str @config/fusion-plugin-dir) "/" (-> @config/settings :fusion-plugin-dir))

    (plugin-dir-exists [self] (file/exists (plugin-dir)))

    (secure-credentials-present?
     [self]
     (if (-> @config/settings :git-credentials :add-your-credentials)
       (do
         (log/warn "SSH credentials not yet configured.  Edit " (config/fusion-configfilename) " and remove the :add-your-credentials key-value pair after adding your Github credentials.")
         (log/info "If you wish, you can fork repositories mentioned in " (config/fusion-configfilename) " and edit " (config/fusion-configfilename) " to point to your versions.")
         false)

       true))

    (secure-credentials [self] (-> @config/settings :git-credentials))

    (install
     [self origin]
     (log/info "No " @config/settings " directory found.  Initializing for first run.")

     (if (secure-credentials-present?)
       (jgit/with-identity (secure-credentials)
         (jgit/git-clone-full origin (plugin-dir)))

       (jgit/git-clone-full origin (plugin-dir))))

    (update
     [self]
     (log/info @config/settings " directory found.  Updating.")

     (if (secure-credentials-present?)
       (jgit/with-identity (secure-credentials)
         (jgit/git-pull (jgit/load-repo (plugin-dir))))

       (jgit/git-pull (jgit/load-repo (plugin-dir)))))]))


(defn download-or-update-boot-plugin [plugin-manager]
  (if (=> plugin-manager :plugin-dir-exists)
    (=> plugin-manager :update)
    (=> plugin-manager :install (-> @config/settings :boot-plugin-repo))))


(defn launch-boot-plugin [])


(defn start []
  (download-or-update-boot-plugin git-provider)
  (launch-boot-plugin))
