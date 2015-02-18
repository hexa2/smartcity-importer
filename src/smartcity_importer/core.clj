(ns smartcity-importer.core
  (:gen-class)
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.calendar-interval :refer [schedule with-interval-in-minutes]]
            [smartcity-importer.jobs :as jobs]))

(defjob FetchDataJob
  [ctx]
  (comment "NOOP"))

(defn -main
  "Schedule and start the fetch data job"
  [& args]
  (let [s (-> (qs/initialize) qs/start)
        job (j/build
              (j/of-type FetchDataJob)
              (j/with-identity (j/key "jobs.fetch-data.1")))
        trigger (t/build
                  (t/with-identity (t/key "triggers.1"))
                  (t/start-now)
                  (t/with-schedule (schedule
                                     (with-interval-in-minutes 1))))]
    (qs/schedule s job trigger)))
