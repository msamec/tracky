(ns tracky.infrastructure.http.tempo
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [integrant.core :as ig]
            [tracky.domain.worklog-repository :refer [WorklogRepository -save!]]))

(defn prepare-entry [entry jira-accont-id]
  {:issueKey (-> entry :task-id)
   :timeSpentSeconds (-> entry :duration)
   :startDate (-> entry :start-date)
   :startTime (-> entry :start-time)
   :description (-> entry :description)
   :authorAccountId jira-accont-id})

(defn send-request [body url api-key]
  (->
   url
   (client/post {:headers {:Authorization (str "Bearer " api-key)
                           :Content-Type "application/json"}
                 :body (json/write-str body)})))

(defn save! [entry credential]
  (let [url "https://api.tempo.io/core/3/worklogs"
        api-key (get-in credential [:tempo-api-key])
        jira-account-id (get-in credential [:jira-account-id])]
    (->
     entry
     (prepare-entry jira-account-id)
     (send-request url api-key))))

(defmethod ig/init-key :tracky.infrastructure.http/tempo [_ _]
  (reify WorklogRepository
    (-save! [this entry credential] (save! entry credential))))
