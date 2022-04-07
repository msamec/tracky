(ns tracky.domain.exception-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.domain.exception :as SUT]))

(deftest tracky-domain-exception
  (testing "when calling 'unprocessable-entity'"
    (testing "given list of errors then throw exception"
      (let [exception (SUT/unprocessable-entity [{:msg "msg"}])] 
        (clojure.pprint/pprint exception)
        (is (thrown-with-msg? Exception #"Unprocessable entity" (SUT/unprocessable-entity [{:msg "msg"}])))))))
