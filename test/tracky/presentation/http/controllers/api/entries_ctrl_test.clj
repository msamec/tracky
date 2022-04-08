(ns tracky.presentation.http.controllers.api.entries-ctrl-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [helpers.fixtures :refer [with-system!]]
            [clj-http.client :as client]
            [clojure.data.json :as json]))

(use-fixtures :once (partial with-system!))

(def url "http://localhost:3000")

(def list-entries "/api/entries/list")

(def default {:throw-exceptions false})

(def token "eyJhbGciOiJSUzI1NiIsImtpZCI6ImNlYzEzZGViZjRiOTY0Nzk2ODM3MzYyMDUwODI0NjZjMTQ3OTdiZDAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2NDU0ODQ5NzY4NjctdmIxcWZvY2c1YWFmOXBrbW81NzUwaHFsa3A5Mmt2bWYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2NDU0ODQ5NzY4NjctdmIxcWZvY2c1YWFmOXBrbW81NzUwaHFsa3A5Mmt2bWYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTE2NzQ4MzAyNTE2MjY4NTA3NTkiLCJhdF9oYXNoIjoid3g4cnpicGxRcVU2Qm1USnhRZy1idyIsImlhdCI6MTY0OTQxMzcxMywiZXhwIjoxNjQ5NDE3MzEzfQ.QNN_2hLiIVsN3PT2hwy3GDjL9TeVr3RshptYtj5b6aLFNAIN5SJtGkYTJkXWRyJdmuSsZHvya2qVVQgya-JsfMhy14bd_HstW81tq_HaU_ynmlGoNdk-E5l88Ei6CkbIiM_LI9JP8AHySHS-IkAFgL-oQURe5QTxgPof_4DL9l9PayjkYsf8O7HcBIBiR844XEhk_xykrc6SZM1VMMDtrJT9_dimq9ZPDhw9FwM9cw0g9ThRJNMdn9XuKc0vH_sgLm-mLcCIHK_jV00TtN7QDqHbtSouBKsYhxqvU2ZI18fo4Ov4fEMb2-lhMhjnDz3yrww24KFUExsNnLkS9AsECA")

(deftest tracky-presentation-http-controllers-api-entries-ctrl
  (testing "when calling `/api/entries/list"
    (testing "given no access token then response should return 401 status"
      (let [response (client/get (str url list-entries) default)]
        (is (= 401 (:status response)))))

    (testing "given there are entries then response should list of entries"
      (let [headers {:headers {:authorization (str "Bearer " token)
                               :x-toggl-api-key "list-single"}}
            response (client/get (str url list-entries) (merge default headers))
            entries (-> response :body (json/read-str :key-fn keyword) :data)
            entry (first entries)]
        (is (= 200 (:status response)))
        (is (= 1 (count entries)))
        (is (= "CLJ" (:description entry)))
        (is (= 27000 (:duration entry)))
        (is (= "123" (:id entry)))
        (is (= "ID-134 | CLJ" (:original-description entry)))
        (is (= "2022-03-29" (:start-date entry)))
        (is (= "06:00:00" (:start-time entry)))
        (is (= true (:syncable entry)))
        (is (= "ID-134" (:task-id entry)))))

    (testing "given there are no entries then response should empty list"
      (let [headers {:headers {:authorization (str "Bearer " token)
                               :x-toggl-api-key "empty"}}
            response (client/get (str url list-entries) (merge default headers))
            entries (-> response :body (json/read-str :key-fn keyword) :data)]
        (is (= 200 (:status response)))
        (is (= 0 (count entries)))))))
