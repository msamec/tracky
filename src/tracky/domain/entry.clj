(ns tracky.domain.entry
  (:require [clojure.string :as str]
            [tracky.domain.date :as date]))

(defrecord Entry [id task-id description duration start-date start-time])

(defn- extract-task-id-and-description [log]
  (as-> log s
    (str/split s #"\|")
    (map str/trim s)))

(defn create-entry
  "Creaste entry from id, log, duration, and start"
  [id log duration start]
  {:pre [(string? id)
         (string? log)
         (.contains log "|")
         (int? duration)
         (string? start)]} ;TODO consider validating against iso8601
  (let [[task-id description] (extract-task-id-and-description log)
        start-date (date/format-date start)
        start-time (date/format-time start)]
    (->Entry id (str/trim task-id) (str/trim description) duration start-date start-time)))

