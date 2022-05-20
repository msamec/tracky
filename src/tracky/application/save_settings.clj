(ns tracky.application.save-settings
  (:require [tracky.domain.settings-repository :as settings-repository]))

(defn execute! [user-id settings]
  (settings-repository/save! user-id settings))
