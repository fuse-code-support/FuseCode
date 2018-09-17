(ns fusion.core
  (:require [adzerk.boot-logservice :as log-service]
            [clojure.tools.logging  :as log]
            [orchestra.spec.test :as st]

            [fusion.config          :as config]
            [fusion.launch          :as launch])
  (:gen-class))


(def log-config
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


(defn configure-logging [] (alter-var-root #'log/*logger-factory* (constantly (log-service/make-factory log-config))))


(defn -main
  "Fusion Editor main method (for command-line execution)"
  [& args]

  (st/instrument)                       ; Enable type checking of type-annotated functions

  (configure-logging)
  (log/info "Preparing to fuse atoms")
  (config/create-or-read)
  (launch/start))
