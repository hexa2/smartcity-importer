(ns smartcity-importer.transfert
  "From Smartcity to InfluxDB"
  (:require [capacitor.core :as influx]
            [smartcity-importer.source :as src]
            [smartcity-importer.time :as t]
            [smartcity-importer.filter :as f]
            [smartcity-importer.destination :as dest]))

(defn transfert-to-influx
  "Transfert smartcity data source to InfluxDB"
  [influx-client src-path dest-table src-time-field dest-fields & opts]
  (dest/write-new-points
    influx-client dest-table
    (f/select-fields
      (t/convert-dates
        (apply src/get-values src-path opts)
        :src-field src-time-field)
      dest-fields)))
