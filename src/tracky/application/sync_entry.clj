(ns tracky.application.sync-entry
  (:require [tracky.domain.entry-repository :as entry-repository]
            [tracky.domain.credential-repository :as credential-repository]
            [tracky.domain.worklog-repository :as worklog-repository]))

(defn execute! [task-id user-id]
  (let [credential (credential-repository/fetch! user-id)]
    (->
     task-id
     (entry-repository/one! credential)
     (worklog-repository/save! credential))
    (entry-repository/add-tags! [task-id] credential)))
