(ns tracky.presentation.http.controllers.frontend.oauth2-ctrl-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [helpers.fixtures :refer [with-system!]]
            [mockfn.macros :refer [verifying]]
            [mockfn.matchers :refer [exactly]]
            [clj-http.client :as client]
            [clojure.data.json :as json]))

(use-fixtures :once (partial with-system!))

(def mock-response {:body {:access_token
                           "ya29.A0ARrdaM8PQgGGXLcWq0FHdiJu7bf80mK_GCF97bxI4SEHixmtdyl7BiOYWsCAsaddDDm2ml9hwklK1xkF988mZ9Xjr-ERzsW5CooLN54u5tUr3s7JJRL_aGDLl9tTeS9yGDu75XxeRZl76rYad70lUWjh4iqQzN8",
                           :expires_in 3599,
                           :scope "openid",
                           :token_type "Bearer",
                           :id_token
                           "eyJhbGciOiJSUzI1NiIsImtpZCI6ImYxMzM4Y2EyNjgzNTg2M2Y2NzE0MDhmNDE3MzhhN2I0OWU3NDBmYzAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2NDU0ODQ5NzY4NjctdmIxcWZvY2c1YWFmOXBrbW81NzUwaHFsa3A5Mmt2bWYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2NDU0ODQ5NzY4NjctdmIxcWZvY2c1YWFmOXBrbW81NzUwaHFsa3A5Mmt2bWYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTE2NzQ4MzAyNTE2MjY4NTA3NTkiLCJhdF9oYXNoIjoiVkZpZThPLWE5OXJxbk9tT3pPdWFiQSIsImlhdCI6MTY0OTY3MDc3MywiZXhwIjoxNjQ5Njc0MzczfQ.AG8OAdmBNAGT7BeVQA0lV4MsXHb8UaFv0lOtrLkDCPrDu2U5THJ60iy-Iw1BIZIzyut9vgBfi-_ENHeJikI090ot0LrexMODYzi-ONpRuzeVg0IKAB4CZZsx5nfiJpWLnSmVLZg6OEXh8fBXiqGPo_Hjwwdjs3wD8lc3hOpJZyZWCtagAqXTc9fh81a9HsSIB7fu97M22AHHMOEhqIRhqrU2AtkkAUl2CBOIoG2INzAc9ScLxgSw0Xp4D8V0CT0RVoUP9rWebtkYxJ-nzQCLGyXTOFG88zh8ch2JstR5OIL_EQvybIdtHssLuU7c4hgUO-BT0B_mJ18FAuLVSv3xfw"}})

(deftest tracky-presentation-http-controllers-frontent-main-ctrl
  (testing "when calling `/auth-code`"
    (testing "then it should return html page"

      (with-redefs [client/post (fn [_url _params] mock-response)]
        (let [response (client/get "http://localhost:3000/auth-code")]
          (is (= 200 (:status response)))
          (is (= (-> mock-response :body :id_token) (-> response :body (json/read-str :key-fn keyword) :access-token))))))))

