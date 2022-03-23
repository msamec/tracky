(ns tracky.domain.entry
  (:require [tracky.domain.service.entry-formatter :as entry-formatter]
            [tracky.domain.service.date :as date]
            [clojure.spec.alpha :as s]))

(defrecord Entry [id task-id description original-description duration start-date start-time syncable])

(defn create-entry
  "Creaste entry from id, log, duration, and start"
  [id log duration start]
  (let [{:keys [task-id description syncable]} (entry-formatter/extract log)
        start-date (date/format-date start)
        start-time (date/format-time start)]
    (->Entry id task-id description log duration start-date start-time syncable)))

