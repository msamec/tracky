(ns tracky.application.sync-entry
  (:require [tracky.domain.entry-repository :as entry-repository]
            [tracky.domain.worklog-repository :as worklog-repository]))

(defn execute! [task-id credential]
  (let [entry (entry-repository/one! task-id credential)]
    (when (true? (:syncable entry))
      (worklog-repository/save! entry credential)
      (entry-repository/add-tags! (list task-id) credential))))
