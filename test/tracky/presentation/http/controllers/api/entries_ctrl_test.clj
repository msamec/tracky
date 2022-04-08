(ns tracky.presentation.http.controllers.api.entries-ctrl-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [helpers.fixtures :refer [with-system! get-system]]
            [clj-http.client :as client]))

(use-fixtures :once (partial with-system!))

(defn init []
  (let [system (get-system)]
    system))

(deftest x
  (testing "x"
    (let [system (init)
          response (client/get "http://localhost:3000/api/entries/list" {:throw-exceptions false})]
      (is (= 401 (:status response))))))
