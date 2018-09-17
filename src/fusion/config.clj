(ns fusion.config
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.logging :as log]

            [clojure.spec.alpha :as s]
            [orchestra.core :refer [defn-spec]]

            [fusion.files :as f]
            [fusion.patterns :refer [let-map]]

            [clojure.test :refer [with-test is]]))


;; The loader's own configuration is a resource
(def fusion-plugin-dir (atom "~/.fusion"))
(defn fusion-configfilename [] (str @fusion-plugin-dir "/config.edn"))

(def default-configfile (slurp (io/resource "config.edn")))

(def settings (atom {:error "No settings have been loaded!"}))

(s/def ::configfile-path (s/nilable (s/? string?)))


(defn-spec process-configfile-location-commandline-override any? [configfile-path ::configfile-path]
  (when (and configfile-path (string? (first configfile-path)))
    (log/info (str "Overriding default configuration file path with " (first configfile-path)))
    (reset! fusion-plugin-dir (first configfile-path))))


(defn-spec ensure-configfile-exists any? [fusion-config string?]
  (io/make-parents fusion-config)

  (when-not (.exists (io/file fusion-config))
    (log/info (str "First time run: Creating " fusion-config))
    (spit fusion-config default-configfile)))


(defn-spec create-or-read map?
  "Create if necessary, and read bootstrap-config. Returns a map containing the config file contents."
  [& configfile-path ::configfile-path]

  (process-configfile-location-commandline-override configfile-path)

  (let [fusion-config (fusion-configfilename)]
    (ensure-configfile-exists fusion-config)

    (reset! settings (edn/read-string (slurp fusion-config)))
    (log/info "Successfully read configuration file")
    @settings))
