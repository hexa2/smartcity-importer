(ns smartcity-importer.filter
  "Filter data in multiple ways")

(defn greater-times
  "Filter values with greater times than the reference"
  [values reference & {:keys [time-field]
                       :or [time-field :time]}]
  (filter #(> (get % time-field) reference) values))

(defn select-fields
  "Select fields for each value"
  [values fields]
  (map
    (fn [value]
      (reduce #(assoc %1 %2 (get value %2)) {} fields))
    values))
