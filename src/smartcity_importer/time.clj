(ns smartcity-importer.time
  "Time-related data transformations"
  (:require [clj-time.coerce :as c]
            [clj-time.format :as f]))

(defn convert-date
  "Convert a date field in a map to a timestamp"
  [m & {:keys [src-field dest-field date-formatter]
        :or {src-field :time
             dest-field :time
             date-formatter "yyyy-MM-dd HH:mm:ss"}}]
  (let [formatter (f/formatter date-formatter)]
    (assoc m dest-field
           (c/to-long
             (f/parse formatter (get m src-field))))))

(defn convert-dates
  "Convert dates in a list of data"
  [coll & opts]
  (map #(apply convert-date % opts) coll))
