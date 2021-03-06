(ns smartcity-importer.destination
  "Destination (InfluxDB time-series database) data writers"
  (:require [capacitor.core :as influx]
            [smartcity-importer.filter :as f]))

(defn last-timestamp
  "Gets the last timestamp registered in the table or a nil object if the table is empty."
  [influx-client table]
  (try
    (:time
      (first
        (influx/get-query influx-client
                          (str
                            "SELECT * FROM " table " "
                            "LIMIT 1"))))
    (catch Exception e 0)))

(defn write-new-points
  "Write only recent data to InfluxDB"
  [influx-client table points]
  (let [last-ts (last-timestamp influx-client table)
        values (f/greater-times points last-ts)]
    (influx/post-points influx-client table "ms" values)))
