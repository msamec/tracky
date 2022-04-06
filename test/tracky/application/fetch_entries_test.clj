(ns tracky.application.fetch-entries-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [helpers.fixtures :refer [with-system!]]
            [tracky.application.fetch-entries :as SUT]))

(use-fixtures :once (partial with-system!))

(deftest tracky-application-fetch-entries
  (testing "when calling fetch entries"
    (testing "given there are entries then return single entry in a list"
      (let [result (SUT/execute! "list-single")
            entry (first result)]
        (is (= 1 (count result)))
        (is (= "123" (:id entry)))
        (is (= 27000 (:duration entry)))
        (is (= "CLJ" (:description entry)))
        (is (= true (:syncable entry)))
        (is (= "ID-134" (:task-id entry)))))

    (testing "given there are no entries then return empty entry list"
      (let [result (SUT/execute! "empty")]
        (is (= 0 (count result)))))

    (testing "given API error occurred then throw exception"
      (is (thrown-with-msg?
           Exception
           #"Issues with API!"
           (SUT/execute! "error"))))))
