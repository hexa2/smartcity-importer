(ns smartcity-importer.time-test
  (:require [clojure.test :refer :all]
            [smartcity-importer.time :refer :all]))

(deftest test-time
  (testing "convert-date with some interesting values"
    (is (= (convert-date {:t "1970-01-01 00:00:00"}
                         :src-field :t
                         :dest-field :t)
           {:t 0}))
    (is (= (convert-date {:time "2015-02-18 19:38:56"})
           {:time 1424288336000})))
  (testing "convert-dates with non-standard parameters"
    (is (= (convert-dates
             [{:t "1970-01-01 00:00:00"}
              {:t "2015-02-18 19:38:56"}]
             :src-field :t
             :dest-field :t)
           [{:t 0}
            {:t 1424288336000}]))))
