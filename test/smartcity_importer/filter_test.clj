(ns smartcity-importer.filter-test
  (:require [clojure.test :refer :all]
            [smartcity-importer.filter :refer :all]))

(deftest filter-test
  (testing "greater-times with two values"
    (is (= (greater-times [{:t 0} {:t 10}] 5 :time-field :t)
           [{:t 10}])))
  (testing "select-fields with two values and three fields"
    (is (= (select-fields [{:a 1 :b 2 :c 3} {:a 2 :b 3 :c 4}] [:a :c])
           [{:c 3 :a 1} {:c 4 :a 2}]))))
