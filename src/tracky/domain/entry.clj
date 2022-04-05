(ns tracky.domain.entry
  (:require [tracky.domain.service.formatter :as formatter]
            [tracky.domain.service.date :as date]
            [tracky.domain.service.validator :as validator]
            [clojure.spec.alpha :as s]))

(defrecord Entry [id task-id description original-description duration start-date start-time syncable])

(s/def :entry/id string?)
(s/def :entry/log string?)
(s/def :entry/duration (s/and int? #(>= % 60)))
(s/def :entry/start #(date/is-valid-iso-8601? %))
(s/def :entry/entry (s/keys :req [:entry/id :entry/log :entry/duration :entry/start]))

(defn- make-unsyncable-if-fails-checks [{:keys [task-id description] :as entry}]
  (if (or
       (nil? task-id)
       (nil? description))
    (assoc entry :syncable false)
    entry))

(defn create-entry
  "Creaste entry from id, log, duration, and start"
  [{:entry/keys [id log duration start] :as data}]
  (validator/validate :entry/entry data)
  (let [{:keys [task-id description]} (formatter/extract-task-id-description log)
        start-date (date/format-date start)
        start-time (date/format-time start)]
    (->
     (->Entry id task-id description log duration start-date start-time true)
     make-unsyncable-if-fails-checks)))



