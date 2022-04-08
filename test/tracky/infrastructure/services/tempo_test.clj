(ns tracky.infrastructure.services.tempo-test
  (:require [clojure.test :refer [is deftest testing]]
            [mockfn.macros :refer [verifying]]
            [tracky.infrastructure.services.tempo :as SUT]
            [mockfn.matchers :refer [exactly]]
            [tracky.infrastructure.http :as http]))

(def entry {:task-id "task-id"
            :duration 100
            :start-date "start-date"
            :start-time "start-time"
            :description "desc"})

(def prepped-entry {:issueKey (:task-id entry)
                    :timeSpentSeconds (:duration entry)
                    :startDate (:start-date entry)
                    :startTime (:start-time entry)
                    :description (:description entry)
                    :authorAccountId "id"})

(def api-key "api-key")

(defn auth [api-key]
  {:headers {:Authorization (str "Bearer " api-key)
             :Content-Type "application/json"}})

(deftest tracky-infrastructure-services-tempo
  (testing "when calling 'save!'"
    (testing "then make sure http post is being called"
      (verifying
       [(http/send-post "https://api.tempo.io/core/3/worklogs" (auth api-key) prepped-entry) nil (exactly 1)]
       (SUT/save! entry {:jira-account-id "id" :tempo-api-key api-key})
       (is true)))))
