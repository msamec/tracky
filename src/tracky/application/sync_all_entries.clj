(ns tracky.application.sync-all-entries
  (:require [tracky.domain.entry-repository :as entry-repository]
            [tracky.domain.credential-repository :as credential-repository]
            [tracky.domain.worklog-repository :as worklog-repository]))

(defn execute! [user-id]
  (let [credential (credential-repository/fetch! user-id)
        entries (entry-repository/all! credential)
        entry-ids (map (fn [{:keys [id]}] id) entries)]
    (doall (map #(worklog-repository/save! % credential) entries))
    (entry-repository/add-tags! entry-ids credential)))