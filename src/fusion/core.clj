(ns fusion.core
  "Where everything install and command-line related starts."
  (:require [clojure.tools.logging  :as log]
            [orchestra.spec.test :as st]

            [fusion.config          :as config]
            [fusion.launch          :as launcher])
  (:import  [java.net InetAddress])
  (:gen-class))


(def cli-options
  [["-p" "--port" "Server port number"
    :default 7000
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]

   ["-h" "--host" "Server host if connecting to an existing remote fusecode server"
    :default (InetAddress/getByName "localhost")
    :default-desc "localhost"
    :parse-fn #(InetAddress/getByName %)]

   ["-f" "--fusecode-dir" "Override default FuseCode plugin repository directory"
    :default "~/.fusecode"
    :parse-fn identity]

   ["-t" "--tasks" "A string of Task(s), optionally with parameters, to override the (boot <task> :arg val ...) call."
    :default "web-dev"
    :parse-fn identity]

   ["-o" "--offline" "Use whatever is cached locally and don't ask the Internet for anything"
    :default false
    :update-fn #(constantly true)]

   ["-h" "--help"]])


(defn usage [options-summary]
  (->> ["Fuse coding, running, and debugging into a single conherent whole.  https://github.com/fuse-code"
        ""
        "Usage: fusecode [options] file1 [file2 ...]"
        ""
        "Options:"
        options-summary]))


(defn -main
  "Fusion Editor main method (for command-line execution)"
  [& args]

  (st/instrument)                       ; Enable type checking of type-annotated functions

  (log/info "Preparing to fuse code")
  (config/create-or-read)
  (launcher/start))
