(ns tracky.infrastructure.services.toggl-test
  (:require [clojure.test :refer [is deftest testing]]
            [mockfn.macros :refer [providing verifying]]
            [mockfn.matchers :refer [exactly]]
            [tracky.infrastructure.services.toggl :as SUT]
            [clojure.string :as str]
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
           (is (instance? tracky.domain.entry.Entry entry)))))))

  (testing "when calling 'add-tags!'"
    (testing "then make sure http/put is called"
      (let [ids ["1"]]
        (verifying
         [(http/send-put
           (str "https://api.track.toggl.com/api/v8/time_entries/" (str/join "," ids))
           (auth "")
           {:time_entry {:tags ["synced"] :tag_action "add"}})
          nil
          (exactly 1)]
         (SUT/add-tags! ids {:toggl-api-key ""})
         (is true))))))
