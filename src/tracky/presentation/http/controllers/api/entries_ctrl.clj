(ns tracky.presentation.http.controllers.api.entries-ctrl
  (:require
   [tracky.application.fetch-entries :as fetch-entries]
   [tracky.application.fetch-entry :as fetch-entry]
   [tracky.application.sync-entry :as sync-entry]
   [tracky.application.sync-all-entries :as sync-all-entries]
   [tracky.application.update-entry-description :as update-entry-description]
   [integrant.core :as ig]
   [ring.util.response :refer [response]]))

(defmethod ig/init-key :tracky.presentation.http.controllers.api.entries-ctrl/list [_ _]
  (fn [{:keys [headers]}] ;TODO: should be extracted into single method
    (let [toggl-api-key (get headers "x-toggl-api-key")
          entries (fetch-entries/execute! {:toggl-api-key toggl-api-key})]
      (response {:data entries}))))

(defmethod ig/init-key :tracky.presentation.http.controllers.api.entries-ctrl/single [_ _]
  (fn [{headers :headers {id :id} :path-params}]
    (let [toggl-api-key (get headers "x-toggl-api-key")
          entry (fetch-entry/execute! id {:toggl-api-key toggl-api-key})]
      (response {:data entry}))))

(defmethod ig/init-key :tracky.presentation.http.controllers.api.entries-ctrl/sync [_ _]
  (fn [{:keys [headers] :as request}]
    (let [{:keys [] {:keys [id]} :path-params} request
          toggl-api-key (get headers "x-toggl-api-key")
          tempo-api-key (get headers "x-tempo-api-key")
          jira-account-id (get headers "x-jira-account-id")]
      (sync-entry/execute! id {:toggl-api-key toggl-api-key
                               :tempo-api-key tempo-api-key
                               :jira-account-id jira-account-id})
      (response {}))))

(defmethod ig/init-key :tracky.presentation.http.controllers.api.entries-ctrl/sync-all [_ _]
  (fn [{:keys [headers]}]
    (let [toggl-api-key (get headers "x-toggl-api-key")
          tempo-api-key (get headers "x-tempo-api-key")
          jira-account-id (get headers "x-jira-account-id")]
      (sync-all-entries/execute! {:toggl-api-key toggl-api-key
                                  :tempo-api-key tempo-api-key
                                  :jira-account-id jira-account-id})
      (response {}))))

(defmethod ig/init-key :tracky.presentation.http.controllers.api.entries-ctrl/update-description [_ _]
  (fn [{headers :headers {description :description} :body-params {id :id} :path-params}]
    (let [toggl-api-key (get headers "x-toggl-api-key")]
      (update-entry-description/execute! id description {:toggl-api-key toggl-api-key})
      (response {}))))
