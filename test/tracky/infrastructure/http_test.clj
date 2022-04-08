(ns tracky.infrastructure.http-test
  (:require [clojure.test :refer [is deftest testing]]
            [mockfn.macros :refer [verifying]]
            [tracky.infrastructure.http :as SUT]
            [mockfn.matchers :refer [exactly]]
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(def url "url")
(def body {})

(deftest tracky-infrastructure-http
  (testing "when calling 'send-get'"
    (testing "given no params then make sure client/get is being called"
      (verifying
       [(client/get url {:throw-exceptions false}) {:status 200
                                                    :body "{}"} (exactly 1)]
       (SUT/send-get url)
       (is true)))

    (testing "given params then make sure client/get is being called"
      (verifying
       [(client/get url {:throw-exceptions false
                         :param 1}) {:status 200
                                     :body "{}"} (exactly 1)]
       (SUT/send-get url {:param 1})
       (is true)))

    (testing "given status response is not 200"
      (verifying
       [(client/get url {:throw-exceptions false}) {:status 400
                                                    :body "{}"} (exactly 1)]
       (is (thrown-with-msg?
            Exception
            #"Issues with API!"
            (SUT/send-get url))))))

  (testing "when calling 'send-put'"
    (testing "given no params and body then make sure client/put is being called"
      (verifying
       [(client/put url {:throw-exceptions false
                         :body (json/write-str body)}) nil (exactly 1)]
       (SUT/send-put url {} body)
       (is true)))

    (testing "given params and body then make sure client/put is being called"
      (verifying
       [(client/put url {:throw-exceptions false
                         :body (json/write-str body)
                         :param 1}) nil (exactly 1)]
       (SUT/send-put url {:param 1} body)
       (is true))))

  (testing "when calling 'send-post'"
    (testing "given no params and body then make sure client/post is being called"
      (verifying
       [(client/post url {:throw-exceptions false
                          :body (json/write-str body)}) nil (exactly 1)]
       (SUT/send-post url {} body)
       (is true)))

    (testing "given params and body then make sure client/post is being called"
      (verifying
       [(client/post url {:throw-exceptions false
                          :body (json/write-str body)
                          :param 1}) nil (exactly 1)]
       (SUT/send-post url {:param 1} body)
       (is true)))))

