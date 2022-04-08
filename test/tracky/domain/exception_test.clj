(ns tracky.domain.exception-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.domain.exception :as SUT]))

(deftest tracky-domain-exception
  (testing "when calling 'unprocessable-entity'"
    (testing "given list of errors then throw exception"
      (try
        (SUT/unprocessable-entity [{:msg "msg"}])
        (is false)
        (catch Exception e
          (is (= "Unprocessable entity" (ex-message e)))
          (is (= [{:msg "msg"}] (-> e ex-data :errors)))
          (is (= 422 (-> e ex-data :status)))))))

  (testing "when calling 'unauthorized'"
    (testing "then throw exception"
      (try
        (SUT/unauthorized)
        (is false)
        (catch Exception e
          (is (= "Unauthorized access" (ex-message e)))
          (is (= 401 (-> e ex-data :status)))))))

  (testing "when calling 'service-unavailable'"
    (testing "given cause then throw exception"
      (try
        (SUT/service-unavailable "cause")
        (is false)
        (catch Exception e
          (is (= "cause" (ex-message e)))
          (is (= 503 (-> e ex-data :status))))))))
