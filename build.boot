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
 :dependencies '[[coconutpalm/boot-boot     "LATEST" :scope "test"]

                 [clj-jgit                  "0.8.10"]

                 [adzerk/boot-jar2bin       "1.1.1" :scope "test"] ; https://github.com/adzerk-oss/boot-jar2bin
                 [org.clojure/tools.nrepl   "0.2.13" :scope "test"]]

 :resource-paths #{"resources" "src/clj"}
 :source-paths   #{"src/cljs" "src/hl"})


(require
 '[adzerk.boot-reload    :refer [reload]])


(deftask exe
  "Build an executable binary."
  []
  (comp aot
     pom
     uber
     jar
     bin))


(task-options!
 bin {:output-dir "bin"}
 exe {:output-dir "bin"})
