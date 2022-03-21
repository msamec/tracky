(ns tracky.application.fetch-entries
  (:require [tracky.domain.entry-repository :as entry-repository]
            [tracky.domain.credential-repository :as credential-repository]))

(defn execute! [user-id]
  (let [credential (credential-repository/fetch! user-id)]
    (entry-repository/all! credential)))
