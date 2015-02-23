(ns smartcity-importer.core
  (:gen-class)
  (:require [capacitor.core :as influx]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.conversion :as qc]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.calendar-interval :refer [schedule with-interval-in-minutes]]
            [smartcity-importer.transfert :as transfert]))

(def client
  (influx/make-client
    {:host (or (System/getenv "INFLUX_HOST") "localhost")
     :scheme (or (System/getenv "INFLUX_SCHEME") "http")
     :port (read-string (or (System/getenv "INFLUX_PORT") "8086"))
     :username (or (System/getenv "INFLUX_USERNAME") "root")
     :password (or (System/getenv "INFLUX_PASSWORD") "root")
     :db (or (System/getenv "INFLUX_DB") "default-db")}))

(def job-data ; todo: make it configurable (json/yaml or something)
  [{:src-path "/jcd_jcdecaux.jcdvelov/all.json"
    :dest-table "velov"
    :src-time-field :last_update
    :dest-fields [:number
                  :time
                  :available_bike_stands
                  :available_bikes]}])

(defjob TransfertDataJob
  [ctx]
  (let [m (qc/from-job-data ctx)]
    (transfert/transfert-to-influx
      client (get m "src-path") (get m "dest-table")
      (get m "src-time-field") (get m "dest-fields"))))

(defn -main
  "Schedule and start the fetch data job"
  [& args]
  (let [s (-> (qs/initialize) qs/start)]
    (mapv
      (fn [data]
        (let [job (j/build
                    (j/of-type TransfertDataJob)
                    (j/with-identity (j/key (str "jobs.fetch-data." (:dest-table data))))
                    (j/using-job-data data))
              trigger (t/build
                        (t/with-identity (t/key "triggers.1"))
                        (t/start-now)
                        (t/with-schedule (schedule
                          (with-interval-in-minutes 1))))]
          (qs/schedule s job trigger)))
      job-data)))
