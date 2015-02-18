(ns smartcity-importer.jobs-test
  (:require [clojure.test :refer :all]
            [smartcity-importer.jobs :refer :all]))

(deftest test-raw-values
  (let [vals (get-raw-values "/jcd_jcdecaux.jcdvelov/all.json")]
    (is (= (first (get vals "fields")) "number"))
    (is (= (count (get vals "values")) (get vals "nb_results"))))
  (let [map-vec (raw-to-map-vec {"fields" ["a" "b" "c"]
                                 "values" [[1 2 3] [4 5 6]]})]
    (is (= (first map-vec) {:a 1 :b 2 :c 3}))
    (is (= (last map-vec) {:a 4 :b 5 :c 6}))))

(deftest test-dates
  (let [converted (:d (first (convert-dates :d [{:d "2015-06-26 10:20:30"}])))]
    (is (= (+ 1900 (.getYear converted)) 2015))
    (is (= (.getMonth converted) 5)))) ; TODO: more testing on dates (but it's boring ... meeehh)
