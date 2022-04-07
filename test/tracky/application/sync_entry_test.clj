(ns tracky.application.sync-entry-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.application.sync-entry :as SUT]
            [mockfn.macros :refer [verifying]]
            [mockfn.matchers :refer [exactly]]
            [tracky.domain.entry-repository :as entry-repository]
            [tracky.domain.worklog-repository :as worklog-repository]))

(def syncable-entry {:syncable true})
(def not-syncable-entry {:syncable false})

(deftest tracky-application-sync-entry
  (testing "when calling 'execute!'"
    (testing "given entry is syncable check if all required methods are being called"
      (verifying
       [(entry-repository/one! "id" "cred") syncable-entry (exactly 1)
        (worklog-repository/save! syncable-entry "cred") nil (exactly 1)
        (entry-repository/add-tags! (list "id") "cred") nil (exactly 1)]
       (SUT/execute! "id" "cred")
       (is true)))

    (testing "given entry is not syncable check if all required methods are being called"
      (verifying
       [(entry-repository/one! "id" "cred") not-syncable-entry (exactly 1)
        (worklog-repository/save! not-syncable-entry "cred") nil (exactly 0)
        (entry-repository/add-tags! (list "id") "cred") nil (exactly 0)]
       (SUT/execute! "id" "cred")
       (is true)))))
