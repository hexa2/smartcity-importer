(ns smartcity-importer.destination
  "Destination (InfluxDB time-series database) data writers"
  (:require [capacitor.core :as influx]))

(defn last-timestamp
  "Gets the last timestamp registered in the database or a nil object if the database is empty."
  [influx-client table]
  (:time
    (first
      (influx/get-query influx-client
                        (str
                          "SELECT time FROM " table " "
                          "LIMIT 1")))))


