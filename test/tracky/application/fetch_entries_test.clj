(ns tracky.application.fetch-entries-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.application.fetch-entries :as SUT]
            [mockfn.macros :refer [verifying]]
            [mockfn.matchers :refer [exactly]]
            [tracky.domain.entry-repository :as entry-repository]))

(deftest tracky-application-fetch-entries
  (testing "when calling 'execute!'"
    (testing "check if `entry-repository/all` has been called"
      (verifying [(entry-repository/all! []) nil (exactly 1)]
                 (is (nil? (SUT/execute! [])))))))
