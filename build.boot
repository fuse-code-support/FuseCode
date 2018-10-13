(def task-options
  {:project 'fusion-editor/fusion-main
   :version "0.1.0"
   :project-name "fusion-main"
   :project-openness :open-source

   :description "A programmers' editor intended to be embedded inside of build tools.  This is the main launcher/build-tool plugin."

   :scm-url "https://github.com/fusion-editor/fusion-main"

   :test-sources "test"
   :test-resources nil})

;; Dependency management: Use this?
;; https://github.com/borkdude/boot-bundle

(set-env!
 :resource-paths #{"resources"}
 :source-paths   #{"src" "java" "test"}

 :dependencies '[[org.clojure/clojure            "1.9.0"]
                 [org.clojure/tools.cli          "0.4.1"]
                 [orchestra                      "2018.09.10-1"]
                 [http-kit                       "2.3.0"]

                 [org.clojure/tools.logging      "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]

                 [clj-jgit                       "0.8.10"]

                 [cpmcdaniel/boot-copy           "1.0" :scope "test"]
                 [coconutpalm/boot-boot          "LATEST" :scope "test"]])


;; Require boot-boot tasks
(require
 '[nightlight.boot      :refer [nightlight]]
 '[adzerk.boot-jar2bin  :refer [bin]]
 '[clj-boot.core        :refer :all]

 '[clojure.java.io      :as io])


(deftask copy-bootstrap-to-resources
  "copy bootstrap.jar from the target folder to the bin folder"
  []
  (fn [next-handler]
    (fn [fileset]
      (next-handler fileset)
      (io/copy (io/file "target/bootstrap.jar") (io/file "resources/bootstrap.jar")))))


(deftask remove-bootstrap-binary
  "Remove bin/bootstrap since it's now embedded inside the \"fuse\" binary."
  []
  (fn [next-handler]
    (fn [fileset]
      (next-handler fileset)
      (io/delete-file "bin/bootstrap" true))))


(deftask boot-bin []
  (comp
   (javac)
   (jar :file "bootstrap.jar" :main 'boot.Boot)
   (target)
   (copy-bootstrap-to-resources)))


(deftask fuse-bin []
  (comp
   (uberbin)
   (remove-bootstrap-binary)))


(deftask all [] (comp (boot-bin) (fuse-bin) (remove-bootstrap-binary)))


(set-task-options! task-options)

(task-options!
 bin {:output-dir "bin"}
 jar {:main 'fusion.core :file "fuse.jar"}
 aot {:namespace #{'fusion.core}})
