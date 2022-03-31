(ns tracky.application.sync-all-entries
  (:require [tracky.domain.entry-repository :as entry-repository]
            [tracky.domain.worklog-repository :as worklog-repository]))

(defn execute! [credential]
  (let [entries (entry-repository/all! credential)]
    (doall
     (map
      (fn [entry]
        (worklog-repository/save! entry credential)
        (entry-repository/add-tags! (list (:id entry)) credential))
      entries))))
