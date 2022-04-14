(ns tracky.application.update-entry-description-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.application.update-entry-description :as SUT]
            [mockfn.macros :refer [verifying]]
            [mockfn.matchers :refer [exactly]]
            [tracky.domain.entry-repository :as entry-repository]))

(deftest tracky-application-update-entry-description
  (testing "when calling 'execute!'"
    (testing "check if 'update-description!' is being called"
      (verifying
       [(entry-repository/update-description! "id" "desc" "cred") nil (exactly 1)
        nil (exactly 1)]
       (SUT/execute! "id" "desc" "cred")
       (is true)))))
