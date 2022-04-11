(ns tracky.presentation.http.controllers.frontend.main-ctrl-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [helpers.fixtures :refer [with-system!]]
            [clj-http.client :as client]))

(use-fixtures :once (partial with-system!))

(deftest tracky-presentation-http-controllers-frontent-main-ctrl
  (testing "when calling `/`"
    (testing "then it should return html page"
      (let [response (client/get "http://localhost:3000")]
        (is (re-find #"Tracky" (:body response)))
        (is (= 200 (:status response)))))))

