(ns smartcity-importer.destination-test
  (:require [clojure.test :refer :all]
            [smartcity-importer.destination :refer :all]
            [capacitor.core :as influx]))

(def db-name "test")

(deftest destination-test
  (testing "last-timestamp with a fake evolving dataset"
    (let [client (influx/make-client {:db db-name})]
      (try (influx/delete-db client) (catch Exception e))
      (influx/create-db client)
      (influx/post-points client db-name [{:time 100 :a 1}])
      (is (= (last-timestamp client db-name) 100))
      (influx/post-points client db-name [{:time 1000 :a 2}])
      (is (= (last-timestamp client db-name) 1000))
      (influx/post-points client db-name [{:time 500 :a 3}])
      (is (= (last-timestamp client db-name) 1000))
      (influx/delete-db client))))
