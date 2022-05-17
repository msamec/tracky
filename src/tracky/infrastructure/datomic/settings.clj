(ns tracky.infrastructure.datomic.settings
  (:require [integrant.core :as ig]
            [tracky.domain.settings-repository :refer [SettingsRepository -save! -fetch!]]
            [tracky.infrastructure.datomic :as datomic]))

(defn save! [user-id {:keys [toggl-api-key tempo-api-key jira-account-id]}]
  (datomic/transact [{:settings/user-id user-id
                      :settings/toggl-api-key toggl-api-key
                      :settings/tempo-api-key tempo-api-key
                      :settings/jira-account-id jira-account-id}]))

(defn fetch! [user-id]
  (ffirst
   (datomic/query '[:find (pull ?e [:settings/toggl-api-key :settings/tempo-api-key :settings/jira-account-id])
                    :in $ ?user-id
                    :where
                    [?e :settings/user-id ?user-id]] user-id)))

(defmethod ig/init-key :tracky.infrastructure.datomic/settings [_ _]
  (reify SettingsRepository
    (-save! [this user-id settings] (save! user-id settings))
    (-fetch! [this user-id] (fetch! user-id))))
