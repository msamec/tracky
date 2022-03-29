(ns tracky.presentation.http.controllers.api.entries-ctrl
  (:require
   [tracky.application.fetch-entries :as fetch-entries]
   [tracky.application.sync-entry :as sync-entry]
   [tracky.application.sync-all-entries :as sync-all-entries]
   [integrant.core :as ig]
   [clojure.data.json :as json]))

(defn response
  [body]
  {:status  200
   :headers {"Content-type" "application/json"}
   :body    body})

(defmethod ig/init-key :tracky.presentation.http.controllers.api.entries-ctrl/list [_ _]
  (fn [{:keys [headers]}] ;TODO: should be extracted into single method
    (let [toggl-api-key (get headers "x-toggl-api-key")
          entries (fetch-entries/execute! {:toggl-api-key toggl-api-key})]
      (response (json/write-str {:data entries})))))

(defmethod ig/init-key :tracky.presentation.http.controllers.api.entries-ctrl/sync [_ _]
  (fn [{:keys [headers] :as request}]
    (let [{:keys [] {:keys [id]} :path-params} request
          toggl-api-key (get headers "x-toggl-api-key")
          tempo-api-key (get headers "x-tempo-api-key")
          jira-account-id (get headers "x-jira-account-id")]
      (sync-entry/execute! id {:toggl-api-key toggl-api-key
                               :tempo-api-key tempo-api-key
                               :jira-account-id jira-account-id})
      (response (json/write-str {})))))

(defmethod ig/init-key :tracky.presentation.http.controllers.api.entries-ctrl/sync-all [_ _]
  (fn [{:keys [headers]}]
    (let [toggl-api-key (get headers "x-toggl-api-key")
          tempo-api-key (get headers "x-tempo-api-key")
          jira-account-id (get headers "x-jira-account-id")]
      (sync-all-entries/execute! {:toggl-api-key toggl-api-key
                                  :tempo-api-key tempo-api-key
                                  :jira-account-id jira-account-id})
      (response (json/write-str {})))))
