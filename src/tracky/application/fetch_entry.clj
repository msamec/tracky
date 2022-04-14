(ns tracky.application.fetch-entry
  (:require [tracky.domain.entry-repository :as entry-repository]))

(defn execute! [id credential]
  (entry-repository/one! id credential))

