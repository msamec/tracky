(ns tracky.domain.entry
  (:require [clojure.string :as str]
            [tracky.domain.date :as date]
            [clojure.spec.alpha :as s]))

(defrecord Entry [id task-id description original-description duration start-date start-time syncable])

(defn split-by [log]
  (as-> log s
    (str/split s #"\|")
    (mapv str/trim s)))

(defn format-by-pipeline-character [log]
  (let [[task-id description] (split-by log)]
    (if-not (some nil? [task-id description])
      {:task-id task-id :description description :syncable true}
      nil)))

(def matcher (partial re-matcher #"(?<=\[).+?(?=\])"))
(defn format-by-square-brackets [log]
  (let [task-id (re-find (matcher log))
        description (str/replace log #"\[(.*?)\]" "")]
    (if-not (some nil? [task-id description])
      {:task-id task-id :description description :syncable true}
      nil)))

(defn format-fallback [_] {:task-id nil :description nil :syncable false})

(def formatters [format-by-pipeline-character format-by-square-brackets format-fallback])

(defn extract [log]
  (loop [formatter-functions formatters]
    (let [result ((first formatter-functions) log)]
      (if-not (nil? result)
        result
        (recur (rest formatters))))))

(defn create-entry
  "Creaste entry from id, log, duration, and start"
  [id log duration start]
  (let [{:keys [task-id description syncable]} (extract log)
        start-date (date/format-date start)
        start-time (date/format-time start)]
    (->Entry id task-id description log duration start-date start-time syncable)))

