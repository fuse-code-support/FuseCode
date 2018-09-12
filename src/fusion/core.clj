(ns fusion.core
  (:gen-class))

(defn a-fn [a] (* a a))

(a-fn 3)

(defn -main
  "Fusion Editor main method (for command-line execution)"
  [& args]

  (println "Fusing atoms"))
