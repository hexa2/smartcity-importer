(ns smartcity-importer.jobs
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]))

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

(def get-map-vec (comp raw-to-map-vec get-raw-values))

(defn filter-dates
  "Filter last update dates"
  [date-key last-update map-vec]
  (filter
    (fn [values]
      (let [date (.parse
                    (java.text.SimpleDateFormat. "dd.MM.yyyy HH.mm.ss")
                    (get values date-key))]
        (.after date last-update)))
    map-vec))


(defn fetch-data
  [ctx]
  (println "bob"))
