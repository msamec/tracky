(ns tracky.application.fetch-entries
  (:require [tracky.domain.entry-repository :as entry-repository]))

(defn execute! [credential]
  (entry-repository/all! credential))

