(ns tracky.domain.service.date-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.domain.service.date :as SUT]))

(def valid-8601 "2022-04-01T06:32:57+00:00")
(def invalid-8601 "invalid")

(deftest tracky-domain-service-date
  (testing "when calling 'is-valid-iso-8601?'"
    (testing "given valid iso-8601 datetime then return true"
      (let [result (SUT/is-valid-iso-8601? valid-8601)]
        (is (= true result))))

    (testing "given invalid datetime then return false"
      (let [result (SUT/is-valid-iso-8601? invalid-8601)]
        (is (= false result)))))

  (testing "when calling 'format-date'"
    (testing "given valid iso-8601 datetime then return just date"
      (let [result (SUT/format-date valid-8601)]
        (is (= "2022-04-01" result))))

    (testing "given invalid iso-8601 datetime then throw exception"
      (is (thrown-with-msg?
           java.text.ParseException
           #"Unparseable date: \"invalid\""
           (SUT/format-date invalid-8601)))))

  (testing "when calling 'format-time'"
    (testing "given valid iso-8601 datetime then return just time"
      (let [result (SUT/format-time valid-8601)]
        (is (= "06:32:57" result))))

    (testing "given invalid iso-8601 datetime then throw exception"
      (is (thrown-with-msg?
           java.text.ParseException
           #"Unparseable date: \"invalid\""
           (SUT/format-time invalid-8601))))))
