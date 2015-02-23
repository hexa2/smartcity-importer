(ns smartcity-importer.transfert-test
  (:require [capacitor.core :as influx]
            [clojure.test :refer :all]
            [smartcity-importer.transfert :refer :all]))

(def db-name "velov")
(def velov-path "/jcd_jcdecaux.jcdvelov/all.json")

(deftest transfert-test
  (let [client (influx/make-client {:db db-name})]
    (try (influx/delete-db client) (catch Exception e))
    (influx/create-db client)
    (influx/post-points client db-name [{:time 100 :a 1}])
    (testing "transfert-to-influx with velov data"
      (transfert-to-influx
        client
        velov-path db-name :last_update
        [:number
         :time
         :available_bike_stands
         :available_bikes]))
    (influx/delete-db client)))
