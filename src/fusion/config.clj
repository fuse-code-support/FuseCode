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


(def default-log-config
  [:configuration {:scan true, :scanPeriod "10 seconds"}
   [:appender {:name "FILE" :class "ch.qos.logback.core.rolling.RollingFileAppender"}
    [:encoder [:pattern "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"]]
    [:rollingPolicy {:class "ch.qos.logback.core.rolling.TimeBasedRollingPolicy"}
     [:fileNamePattern "logs/%d{yyyy-MM-dd}.%i.log"]
     [:timeBasedFileNamingAndTriggeringPolicy {:class "ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP"}
      [:maxFileSize "64 MB"]]]
    [:prudent true]]
   [:appender {:name "STDOUT" :class "ch.qos.logback.core.ConsoleAppender"}
    [:encoder [:pattern "%-5level %logger{36} - %msg%n"]]
    [:filter {:class "ch.qos.logback.classic.filter.ThresholdFilter"}
     [:level "INFO"]]]
   [:root {:level "INFO"}
    [:appender-ref {:ref "FILE"}]
    [:appender-ref {:ref "STDOUT"}]]
   [:logger {:name "user" :level "ALL"}]
   [:logger {:name "boot.user" :level "ALL"}]])


#_(defn write-logback-xml!
  [dir xml]
    (when xml
      (require '[clojure.data.xml :refer [emit-str sexp-as-element emit]])
      (spit (io/file dir "logback.xml")
            (emit-str (sexp-as-element ~xml)))))


;; The loader's own configuration is a resource
(def fusion-plugin-dir (atom (f/expand-path "~/.fusecode")))
(defn fusion-configfilename [] (str @fusion-plugin-dir "/_config.edn"))

(def default-configfile (slurp (io/resource "config.edn")))

(def settings (atom {:error "No settings have been loaded!"}))


(s/def ::file-path string?)
(s/def ::configfile-settings (s/keys :opt-un [::file-path])) ; A Map spec with optional keys!


(defn-spec process-configfile-location-commandline-override any? [configfile-path (s/nilable ::file-path)]
  (when (and configfile-path (string? configfile-path))
    (let [expanded-path (f/expand-path configfile-path)]
      (log/info (str "Overriding default configuration file path with " expanded-path))
      (reset! fusion-plugin-dir expanded-path))))


(defn-spec ensure-configfile-exists any? [fusion-config string?]
  (io/make-parents fusion-config)

  (when-not (.exists (io/file fusion-config))
    (log/info (str "First time run: Creating " fusion-config))
    (spit fusion-config default-configfile)))


(defn-spec create-or-read map?
  "Create if necessary, and read bootstrap-config. Returns a map containing the config file contents."
  [& configfile-path ::configfile-settings]

  (process-configfile-location-commandline-override (:file-path configfile-path))

  (let [fusion-config (fusion-configfilename)]
    (ensure-configfile-exists fusion-config)

    (reset! settings (edn/read-string (slurp fusion-config)))
    (log/info "Successfully read configuration file")
    @settings))
