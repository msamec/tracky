(ns tracky.infrastructure.xtdb.settings
  (:require [integrant.core :as ig]
            [tracky.domain.settings-repository :refer [SettingsRepository -save! -fetch!]]
            [tracky.infrastructure.xtdb :as xtdb]))

(defn save! [user-id {:keys [toggl-api-key tempo-api-key jira-account-id]}]
  (xtdb/put [{:xt/id user-id
              :settings/user-id user-id
              :settings/toggl-api-key toggl-api-key
              :settings/tempo-api-key tempo-api-key
              :settings/jira-account-id jira-account-id}]))

(defn fetch! [user-id]
  (ffirst (xtdb/query '[:find (pull ?e [:settings/toggl-api-key :settings/tempo-api-key :settings/jira-account-id])
                        :in $ ?user-id
                        :where [?e :settings/user-id ?user-id]] user-id)))

(defmethod ig/init-key :tracky.infrastructure.xtdb/settings [_ _]
  (reify SettingsRepository
    (-save! [this user-id settings] (save! user-id settings))
    (-fetch! [this user-id] (fetch! user-id))))
