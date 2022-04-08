(ns tracky.infrastructure.middlewares.exception-handler-test
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.infrastructure.middlewares.exception-handler :as SUT]))

(deftest tracky-infrastructure-middlewares-exception
  (testing "when calling 'handler'"
    (testing "then return map with status and body"
      (try
        (throw
         (ex-info "Unprocessable entity" {:type ::unprocessable-entity
                                          :status 422}))
        (catch Exception e
          (let [result (SUT/handler e {:uri "uri"})]
            (is (= 422 (:status result)))
            (is (= "Unprocessable entity" (-> result :body :message)))
            (is (= "uri" (-> result :body :uri)))))))))
