(ns tracky.application.sync-entry
  (:require [tracky.domain.entry-repository :as entry-repository]
            [tracky.domain.credential-repository :as credential-repository]
            [tracky.domain.worklog-repository :as worklog-repository]))

(defn execute! [task-id user-id]
  (let [credential (credential-repository/fetch! user-id)
        entry (entry-repository/one! task-id credential)]
    (if (true? (:syncable entry))
      (do
        (->
         entry
         (worklog-repository/save! credential))
        (entry-repository/add-tags! [task-id] credential)))))
