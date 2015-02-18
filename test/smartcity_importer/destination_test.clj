(ns smartcity-importer.destination-test
  (:require [clojure.test :refer :all]
            [smartcity-importer.destination :refer :all]
            [capacitor.core :as influx]))

(def db-name "test")

(deftest destination-test
  (let [client (influx/make-client {:db db-name})]
    (try (influx/delete-db client) (catch Exception e))
    (influx/create-db client)
    (influx/post-points client db-name [{:time 100 :a 1}])
    (testing "last-timestamp with a fake evolving dataset"
      (is (= (last-timestamp client db-name) 100))
      (influx/post-points client db-name [{:time 1000 :a 2}])
      (is (= (last-timestamp client db-name) 1000))
      (influx/post-points client db-name [{:time 500 :a 3}])
      (is (= (last-timestamp client db-name) 1000)))
    (testing "write-new-points with some data"
      (write-new-points client db-name [{:time 10 :a 0}
                                        {:time 10000 :a 0}])
      (let [results (influx/get-query client (str "SELECT * FROM " db-name))]
        (is (= (:time (last results)) 100))
        (is (= (:time (first results)) 10000))))
    (influx/delete-db client)))
