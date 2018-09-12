(def task-options
  {:project 'fusion-editor/fusion-main
   :version "0.1.0"
   :project-name "fusion-main"
   :project-openness :open-source

   :description "A programmers' editor intended to be embedded inside of build tools.  This is the main launcher/build-tool plugin."

   :scm-url "https://github.com/fusion-editor/fusion-main"

   :test-sources "test"
   :test-resources nil})

(set-env!
 :resource-paths #{"resources"}
 :source-paths   #{"src" "test"}

 :dependencies '[[org.clojure/clojure       "1.9.0"]
                 [clojure-future-spec       "LATEST"]
                 [clj-jgit                  "0.8.10"]

                 [coconutpalm/boot-boot     "LATEST" :scope "test"]])


;; Require boot-boot tasks
(require
 '[nightlight.boot     :refer [nightlight]]
 '[adzerk.boot-jar2bin :refer [bin]]
 '[clj-boot.core       :refer :all]
 '[clojure.java.io     :as io])


;; Temporary until I can fix uberbin upstream
(deftask makebin [] (comp (aot) (uberbin)))


(set-task-options! task-options)

(task-options!
 bin {:output-dir "bin"}
 jar {:main 'fusion.core}
 aot {:namespace #{'fusion.core}})
