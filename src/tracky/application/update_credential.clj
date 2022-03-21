(ns tracky.application.update-credential
  (:require [tracky.domain.credential-repository :as credential-repository]))

(defn execute! [user-id options]
  (credential-repository/save! user-id options))
