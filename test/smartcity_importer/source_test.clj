(ns smartcity-importer.source-test
  (:require [clojure.test :refer :all]
            [smartcity-importer.source :refer :all]))

(def velov-path "/jcd_jcdecaux.jcdvelov/all.json")

(deftest test-source
  (testing "get-raw with velov data source"
    (let [vals (get-raw velov-path)]
      (is (= (first (get vals "fields")) "number"))
      (is (= (count (get vals "values")) (get vals "nb_results")))))
  (testing "raw-to-values with a simple sample set"
    (let [map-vec (raw-to-values {"fields" ["a" "b" "c"]
                                  "values" [[1 2 3] [4 5 6]]})]
      (is (= map-vec [{:a 1 :b 2 :c 3}
                      {:a 4 :b 5 :c 6}]))))
  (testing "get-values with velov data source"
    (let [vals (get-values velov-path)]
      (is (> (count vals) 100))
      (is (= (set (keys (first vals))) (set [:status :gid :lng :bike_stands :last_update :name :commune :nmarrond :available_bikes :availability :address2 :last_update_fme :lat :available_bike_stands :bonus :pole :number :address :availabilitycode :banking]))))))
