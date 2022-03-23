(ns tracky.domain.entry
  (:require [tracky.domain.service.formatter :as formatter]
            [tracky.domain.service.date :as date]
            [tracky.domain.service.validator :as validator]
            [clojure.spec.alpha :as s]))

(defrecord Entry [id task-id description original-description duration start-date start-time syncable])

(defn create-entry
  "Creaste entry from id, log, duration, and start"
  [id log duration start]
  (let [{:keys [task-id description]} (formatter/extract-task-id-description log)
        start-date (date/format-date start)
        start-time (date/format-time start)]
    (-> 
      (->Entry id task-id description log duration start-date start-time true)
      validator/validate-entry)))

