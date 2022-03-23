(ns tracky.domain.service.entry-formatter
  (:require [clojure.string :as str]))

(defn create-data [task-id description]
  (if-not (some nil? [task-id description])
    {:task-id task-id :description description :syncable true}
    nil))

(defn split-by [log]
  (as-> log s
    (str/split s #"\|")
    (mapv str/trim s)))

(defn format-by-pipeline-character [log]
  (let [[task-id description] (split-by log)]
    (create-data task-id description)))

(def matcher (partial re-matcher #"(?<=\[).+?(?=\])"))

(defn format-by-square-brackets [log]
  (let [task-id (re-find (matcher log))
        description (str/replace log #"\[(.*?)\]" "")]
    (create-data task-id description)))

(def list-of-applicable-formatters
  [format-by-pipeline-character format-by-square-brackets])

(defn extract [log]
  (loop [function-list list-of-applicable-formatters]
    (if-not (empty? function-list)
      (let [result ((first function-list) log)]
        (if-not (nil? result)
          result
          (recur (rest function-list))))
      {:taks-id nil :description nil :syncable false})))
