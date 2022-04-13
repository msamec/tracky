(ns tracky.infrastructure.services.toggl-test
  (:require [clojure.test :refer [is deftest testing]]
            [mockfn.macros :refer [providing]]
            [tracky.infrastructure.services.toggl :as SUT]
            [tracky.infrastructure.http :as http]
            [mocks.toggl-mock :as toggl-mock]))

(defn auth [api-key] {:basic-auth [api-key "api_token"]})

(def now "2022-03-13T08:40:32.882Z")

(deftest tracky-infrastructure-services-toggl
  (testing "when calling 'all!'"
    (testing "given entries exists then reutrn list of entries"
      (let [type "entries-list"]
        (providing
         [(http/send-get (str "https://api.track.toggl.com/api/v8/time_entries?start_date=" now) (auth type)) toggl-mock/entries-list]
         (let [result (SUT/all! {:toggl-api-key type
                                 :start-date now})
               entry (first result)]
           (is (= 1 (count result)))
           (is (instance? tracky.domain.entry.Entry entry))))))

    (testing "given entries do not exist then return empty list"
      (let [type "empty"]
        (providing
         [(http/send-get (str "https://api.track.toggl.com/api/v8/time_entries?start_date=" now) (auth type)) []]
         (let [result (SUT/all! {:toggl-api-key type
                                 :start-date now})]
           (is (= 0 (count result))))))))

  (testing "when calling 'one!'"
    (testing "given entry exists then reutrn single entry"
      (let [type "entry-single"]
        (providing
         [(http/send-get "https://api.track.toggl.com/api/v8/time_entries/id" (auth type)) toggl-mock/entry-single]
         (let [entry (SUT/one! "id" {:toggl-api-key type})]
           (is (instance? tracky.domain.entry.Entry entry))))))))
