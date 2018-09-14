(ns fusion.config
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.logging :as log]

            [fusion.files :as f]
            [fusion.patterns :refer [let-map]]

            [clojure.test :refer [with-test is]]))


;; The loader's own configuration is a resource
(def fusion-plugin-dir (atom "~/.fusion"))
(defn fusion-configfilename [] (str @fusion-plugin-dir "/config.edn"))

(def default-configfile (slurp (io/resource "config.edn")))

(def settings (atom {:error "No settings have been loaded!"}))


(defn create-or-read
  "Create if necessary, and read bootstrap-config. Returns a map containing the config file contents."
  [& configfile-path]

  (when (and configfile-path (string? (first configfile-path)))
    (reset! fusion-plugin-dir (first configfile-path)))

  (let [fusion-config (fusion-configfilename)]
    (io/make-parents fusion-config)

    (when-not (.exists (io/file fusion-config))
      (log/info (str "First time run: Creating " fusion-config))
      (spit fusion-config default-configfile))

    (reset! settings (edn/read-string (slurp fusion-config)))
    @settings))
