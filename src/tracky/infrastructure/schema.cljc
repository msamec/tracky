(ns tracky.infrastructure.schema
  (:require [malli.core :as m]
            [malli.util :as u]))

(def registry
  (merge
   (m/default-schemas)
   (u/schemas)
   {:entry/id [:string]
    :entry/task-id [:string]
    :entry/description [:string]
    :entry/original-description [:string]
    :entry/duration [:int]
    :entry/start-date [:string]
    :entry/start-time [:string]
    :entry/syncable [:boolean]

    :tracky/entry [:map
                   :entry/id
                   :entry/task-id
                   :entry/description
                   :entry/original-description
                   :entry/duration
                   :entry/start-date
                   :entry/start-time
                   :entry/syncable]
    :tracky/entries [:vector :tracky/entry]
    :tracky.entry/update [:map
                          [:description :entry/description]]

    :settings/user-id [:string]
    :settings/toggl-api-key [:string]
    :settings/tempo-api-key [:string]
    :settings/jira-account-id [:string]

    :tracky/settings [:map
                      :settings/toggl-api-key
                      :settings/tempo-api-key
                      :settings/jira-account-id]
    :tracky.settings/update [:map
                             [:toggl-api-key :settings/toggl-api-key]
                             [:tempo-api-key :settings/tempo-api-key]
                             [:jira-account-id :settings/jira-account-id]]
    :tracky/jwt [:string]}))
