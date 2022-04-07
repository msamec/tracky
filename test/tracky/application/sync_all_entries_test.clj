(ns tracky.application.sync-all-entries-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.application.sync-all-entries :as SUT]
            [mockfn.macros :refer [verifying]]
            [mockfn.matchers :refer [exactly]]
            [tracky.domain.entry-repository :as entry-repository]
            [tracky.domain.worklog-repository :as worklog-repository]))

(def syncable-entry {:syncable true :id "id"})
(def not-syncable-entry {:syncable false})

(deftest tracky-application-sync-entry
  (testing "when calling 'execute!'"
    (testing "check if all required methods are being called"
      (verifying
       [(entry-repository/all! "cred") (list syncable-entry not-syncable-entry syncable-entry) (exactly 1)
        (worklog-repository/save! syncable-entry "cred") nil (exactly 2)
        (entry-repository/add-tags! (list "id") "cred") nil (exactly 2)]
       (SUT/execute! "cred")
       (is true)))))
