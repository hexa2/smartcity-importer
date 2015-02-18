(ns smartcity-importer.jobs
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [capacitor.core :as influx]))

(defn convert-dates
  "Convert string dates to Java dates"
  [date-key map-vec]
  (map #(assoc % date-key
               (.parse
                 (java.text.SimpleDateFormat. "yyyy-MM-dd kk:mm:ss")
                 (get % date-key)))
       map-vec))

(defn filter-dates
  "Filter last update dates"
  [date-key last-update map-vec]
  (filter #(.after (get % date-key last-update)) map-vec))
