(ns tracky.application.fetch-entry-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.application.fetch-entry :as SUT]
            [mockfn.macros :refer [verifying]]
            [mockfn.matchers :refer [exactly]]
            [tracky.domain.entry-repository :as entry-repository]))

(deftest tracky-application-fetch-entry
  (testing "when calling 'execute!'"
    (testing "check if `entry-repository/one` has been called"
      (verifying [(entry-repository/one! "id" []) nil (exactly 1)]
                 (is (nil? (SUT/execute! "id" [])))))))
