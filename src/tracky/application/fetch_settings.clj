(ns tracky.application.fetch-settings
  (:require [tracky.domain.settings-repository :as settings-repository]))

(defn execute! [user-id]
  (settings-repository/fetch! user-id))
