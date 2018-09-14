(ns fusion.files
  (:require [clojure.string :as str]
            [clojure.test :refer [with-test is]])
  (:import [java.io File]))


(def home (System/getProperty "user.home"))

(with-test

  (defn expand-path
    "~ characters in p are substituted with (System/getProperty \"user.dir\") else p is returned
    unchanged."
    [p]
    (if (str/includes? p "~")
      (str/replace p "~" home)
      p))

  (is (= (str home "/dir") (expand-path "~/dir")) "Expected user.dir to be substituted for ~")
  (is (= "666888" (expand-path "666888"))         "Expected no substitutions"))


(defn exists
  "Returns truthy if the file specified by path p exists and falsey otherwise.  p must be a String."
  [p]
  (.exists (File. p)))
