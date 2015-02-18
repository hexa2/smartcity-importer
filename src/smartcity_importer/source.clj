(ns smartcity-importer.source
  "Source (Grand Lyon's Smartcity) data getters"
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]))

(defn get-raw
  "Get raw data from source"
  [path & {:keys [url-base]
           :or {url-base "https://download.data.grandlyon.com/ws/smartdata"}}]
  (let [{:keys [body]} @(http/get (str url-base path))]
    (json/read-str body)))

(defn raw-to-values
  "Change raw data from service to a list of maps"
  [raw]
  (let [fields (get raw "fields")
        raw-values (get raw "values")]
    (map
      (fn [raw-tuple]
        (reduce #(assoc %1 (keyword (:k %2)) (:v %2)) {}
                (map #(identity {:k %1 :v %2}) fields raw-tuple)))
      raw-values)))

(defn get-values
  "Get last values from server as a list of maps"
  [path & opts]
  (-> (apply get-raw path opts) raw-to-values))
