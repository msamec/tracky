(ns tracky.domain.entry-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.domain.entry :as SUT]))

(defn valid-entry
  ([] (valid-entry {}))
  ([overwrite]
   (merge
    (SUT/map->Entry
     {:id "id"
      :task-id "T123"
      :description "Desc"
      :original-description "T123 | Desc"
      :duration 100
      :start-date "2022-04-01"
      :start-time "06:32:57"
      :syncable true}) overwrite)))

(defn create-entry [id log duration start]
  (SUT/create-entry {:entry/id id
                     :entry/log log
                     :entry/duration duration
                     :entry/start start}))

(deftest entry
  (testing "create syncable entry"
    (testing "with format 'task-id | desc'"
      (let [entry (create-entry "id" "T123 | Desc" 100 "2022-04-01T06:32:57+00:00")]
        (is (= entry (valid-entry)))))

    (testing "with format '[task-id] desc"
      (let [entry (create-entry "id" "[T123] Desc" 100 "2022-04-01T06:32:57+00:00")]
        (is (= entry (valid-entry {:original-description "[T123] Desc"}))))))

  (testing "create unsyncable entry"

    (testing "with invalid format"
      (let [entry (create-entry "id" "invalid desc" 100 "2022-04-01T06:32:57+00:00")]
        (is (= false (:syncable entry)))))

    (testing "with duration less than 60 seconds"
      (let [entry (create-entry "id" "T123 | Desc" 1 "2022-04-01T06:32:57+00:00")]
        (is (= false (:syncable entry))))))

  (testing "entry cration fails"

    (testing "given id is not string"
      (is
       (thrown-with-msg?
        Exception
        #"Unprocessable entity"
        (create-entry 1 "[T123] Desc" 100 "2022-04-01T06:32:57+00:00"))))

    (testing "given log is not string"
      (is
       (thrown-with-msg?
        Exception
        #"Unprocessable entity"
        (create-entry "ID" 1 100 "2022-04-01T06:32:57+00:00"))))

    (testing "given duration is not int"
      (is
       (thrown-with-msg?
        Exception
        #"Unprocessable entity"
        (create-entry "ID" "[T123] Desc" "100" "2022-04-01T06:32:57+00:00"))))

    (testing "given start date is not iso 8601 format"
      (is
       (thrown-with-msg?
        Exception
        #"Unprocessable entity"
        (create-entry "ID" "[T123] Desc" 100 "non"))))))
