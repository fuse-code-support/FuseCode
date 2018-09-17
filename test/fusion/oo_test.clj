(ns fusion.oo-test
  (:require [clojure.test :refer [deftest is]]
            [fusion.patterns :refer [let-map letfn-map]]
            [fusion.oo :refer :all]))


(deftest =>-map-of-fns
  (let [o (letfn-map [(plusfive [self x] (+ x 5))])]

    (is 6 (=> o :plusfive 1))))


(defn person-name [first last]
  (let-map [first-name first
            last-name last

            methods (letfn-map
                     [(full-name [self] (str first-name " " last-name))])]))


(deftest =>-object-with-methods_uniform-property-and-method-access
  (let [o (person-name "Donald" "Duck")]
    (is "Donald" (=> o :first-name))
    (is "Donald Duck" (=> o :full-name))))
