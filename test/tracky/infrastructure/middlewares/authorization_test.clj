(ns tracky.infrastructure.middlewares.authorization-test
  ;(:import [buddy.auth.accessrules RuleSuccess RuleError])
  (:require [clojure.test :refer [is deftest testing]]
            [tracky.infrastructure.middlewares.authorization :as SUT]))

(comment (deftest tracky-infrastructure-middlewares-auhtorization
           (testing "when calling 'authenticated-access'"
             (testing "given identity exists in request then return success"
               (let [result (SUT/authenticated-access {:identity true})]
                 (is (instance? RuleSuccess result))))

             (testing "given identity does not exist in request then return error"
               (let [result (SUT/authenticated-access {})]
                 (is (instance? RuleError result)))))

           (testing "when calling 'on-error'"
             (testing "then throw exception"
               (is (thrown-with-msg?
                    Exception
                    #"Unauthorized access"
                    (SUT/on-error {} {})))))))
