(ns tracky.infrastructure.services.tempo
  (:require [integrant.core :as ig]
            [tracky.domain.worklog-repository :refer [WorklogRepository -save!]]
            [tracky.infrastructure.http :as http]))

(defn prepare-entry [entry jira-accont-id]
  {:issueKey (-> entry :task-id)
   :timeSpentSeconds (-> entry :duration)
   :startDate (-> entry :start-date)
   :startTime (-> entry :start-time)
   :description (-> entry :description)
   :authorAccountId jira-accont-id})

(defn auth [credential]
  (let [api-key (get-in credential [:tempo-api-key])]
    {:headers {:Authorization (str "Bearer " api-key)
               :Content-Type "application/json"}}))

(defn save! [entry credential]
  (let [url "https://api.tempo.io/core/3/worklogs"
        jira-account-id (get-in credential [:jira-account-id])]
    (->
     url
     (http/send-post
      (auth credential)
      (prepare-entry entry jira-account-id)))))

(defmethod ig/init-key :tracky.infrastructure.services/tempo [_ _]
  (reify WorklogRepository
    (-save! [this entry credential] (save! entry credential))))
