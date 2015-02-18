(ns smartcity-importer.jobs-test
  (:require [clojure.test :refer :all]
            [smartcity-importer.jobs :refer :all]))

(deftest test-dates
  (let [converted (:d (first (convert-dates :d [{:d "2015-06-26 10:20:30"}])))]
    (is (= (+ 1900 (.getYear converted)) 2015))
    (is (= (.getMonth converted) 5)))) ; TODO: more testing on dates (but it's boring ... meeehh)
