(ns tracky.domain.entry-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.domain.entry :as SUT]))

(defn create-entry [{id :id log :log duration :duration start :start
                     :or {id "id" log "T123 | Desc" duration 100 start "2022-04-01T06:32:57+00:00"}}]
  (SUT/create-entry {:entry/id id
                     :entry/log log
                     :entry/duration duration
                     :entry/start start}))

(deftest tracky-domain-entry
  (testing "when calling 'create-entry'"
    (testing "given log 'task-id | desc' then return valid syncable entry"
      (let [entry (create-entry {:log "T123 | Desc"})]
        (is (= "T123" (:task-id entry)))
        (is (= "Desc" (:description entry)))
        (is (= "T123 | Desc" (:original-description entry)))
        (is (= true (:syncable entry)))))

    (testing "given log '[task-id] desc' then return valid syncable entry"
      (let [entry (create-entry {:log "[T123] Desc"})]
        (is (= "T123" (:task-id entry)))
        (is (= "Desc" (:description entry)))
        (is (= "[T123] Desc" (:original-description entry)))
        (is (= true (:syncable entry)))))

    (testing "given invalid log then return unsyncable entry"
      (let [entry (create-entry {:log "invalid desc"})]
        (is (= false (:syncable entry)))))

    (testing "given duration less than 60 seconds then return unsyncable"
      (let [entry (create-entry {:duration 1})]
        (is (= false (:syncable entry)))))

    (testing "given id is not string then throw exception"
      (is
       (thrown-with-msg?
        Exception
        #"Unprocessable entity"
        (create-entry {:id 1}))))

    (testing "given log is not string then throw exception"
      (is
       (thrown-with-msg?
        Exception
        #"Unprocessable entity"
        (create-entry {:log 1}))))

    (testing "given duration is not int then throw exception"
      (is
       (thrown-with-msg?
        Exception
        #"Unprocessable entity"
        (create-entry {:duration "string"}))))

    (testing "given start date is not iso 8601 format then throw exception"
      (is
       (thrown-with-msg?
        Exception
        #"Unprocessable entity"
        (create-entry {:start "not-iso"}))))))
