(ns tracky.application.update-entry-description
  (:require [tracky.domain.entry-repository :as entry-repository]))

(defn execute! [id desc credential]
  (entry-repository/update-description! id desc credential))
