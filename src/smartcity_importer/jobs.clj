(ns smartcity-importer.jobs
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [capacitor.core :as influx]))

(defn get-raw-values
  "Get values directly from the smart-city system"
  [path & {:keys [url-base] :or {url-base "https://download.data.grandlyon.com/ws/smartdata"}}]
  (let [{:keys [body]} @(http/get (str url-base path))]
    (json/read-str body)))

(defn raw-to-map-vec
  "Exchange raw values to a map vector"
  [raw]
  (let [fields (get raw "fields")
        values-list (get raw "values")]
    (map
      (fn [values]
        (reduce #(assoc %1 (keyword (:k %2)) (:v %2)) {}
                (map #(identity {:k %1 :v %2}) fields values)))
      values-list)))

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

(defn pull-and-push
  "Pull data from smartcity and send it to InfluxDB"
  [i-client src-path dest-table]
  (influx/post-points i-client dest-table
    (-> (get-raw-values src-path)
        raw-to-map-vec
        convert-dates
        filter-dates)))
