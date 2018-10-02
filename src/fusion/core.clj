(ns fusion.core
  (:require [clojure.tools.logging  :as log]
            [orchestra.spec.test :as st]

            [fusion.config          :as config]
            [fusion.launch          :as launcher])
  (:gen-class))


(defn -main
  "Fusion Editor main method (for command-line execution)"
  [& args]

  (st/instrument)                       ; Enable type checking of type-annotated functions

  (log/info "Preparing to fuse atoms")
  (config/create-or-read)
  (launcher/start))
