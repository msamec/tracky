(ns tracky.domain.service.validator-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.domain.service.validator :as SUT]))

(deftest tracky-domain-service-validator
  (testing "when calling 'validate"
    (testing "given spec validation passes then return nil"
      (let [result (SUT/validate string? "string")]
        (is (nil? result))))

    (testing "given spec validation fails then throw exception"
      (is (thrown? Exception (SUT/validate string? 1))))))
