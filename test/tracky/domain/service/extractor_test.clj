(ns tracky.domain.service.extractor-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.domain.service.extractor :as SUT]))

(deftest tracky-domain-service-extractor
  (testing "when calling 'extract-tsk-id-description'"
    (testing "given pipeline format then return valid map"
      (let [result (SUT/extract-task-id-description "ID | DESC")]
        (is (= "ID" (:task-id result)))
        (is (= "DESC" (:description result)))))

    (testing "given square bracket format left side then return valid map"
      (let [result (SUT/extract-task-id-description "[ID] DESC")]
        (is (= "ID" (:task-id result)))
        (is (= "DESC" (:description result)))))

    (testing "given square bracket format right side then return valid map"
      (let [result (SUT/extract-task-id-description "DESC [ID]")]
        (is (= "ID" (:task-id result)))
        (is (= "DESC" (:description result)))))

    (testing "given invalid format then return nil map"
      (let [result (SUT/extract-task-id-description "ID DESC")]
        (is (nil? (:task-id result)))
        (is (nil? (:description result)))))))
