(ns tracky.infrastructure.oauth2-test
  (:require [clojure.test :refer [is deftest testing]]
            [mockfn.macros :refer [providing]]
            [tracky.infrastructure.oauth2 :as SUT]
            [clj-http.client :as client]))

(def config {:access-token-uri ""
             :client-id ""
             :client-secret ""
             :redirect-uri ""})

(def code "123")

(defn header [code]
  {:accept :json :as  :json,
   :form-params {:grant_type    "authorization_code"
                 :code          code
                 :redirect_uri  (:redirect-uri config)
                 :client_id     (:client-id config)
                 :client_secret (:client-secret config)}
   :throw-exceptions false})

(def response {:body {:access_token
                      "ya29.A0ARrdaM_OTx0xvEBvZi09TNqefqOBnwjtY0ZEZIAlTowNHcu248A_ZQZM5gR01Q5Fq9exBNmGFobjggWamvuo-KohtKlXqYG0w-zQ4uqv1idAo9_YVm0LpIeNSVNyO0gOSBXI-IwSwLXEgHfJvS3ZEpnOgvFAqcI",
                      :expires_in 3599,
                      :scope "openid",
                      :token_type "Bearer",
                      :id_token
                      "eyJhbGciOiJSUzI1NiIsImtpZCI6ImNlYzEzZGViZjRiOTY0Nzk2ODM3MzYyMDUwODI0NjZjMTQ3OTdiZDAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2NDU0ODQ5NzY4NjctdmIxcWZvY2c1YWFmOXBrbW81NzUwaHFsa3A5Mmt2bWYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2NDU0ODQ5NzY4NjctdmIxcWZvY2c1YWFmOXBrbW81NzUwaHFsa3A5Mmt2bWYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTE2NzQ4MzAyNTE2MjY4NTA3NTkiLCJhdF9oYXNoIjoid3g4cnpicGxRcVU2Qm1USnhRZy1idyIsImlhdCI6MTY0OTQxMzcxMywiZXhwIjoxNjQ5NDE3MzEzfQ.QNN_2hLiIVsN3PT2hwy3GDjL9TeVr3RshptYtj5b6aLFNAIN5SJtGkYTJkXWRyJdmuSsZHvya2qVVQgya-JsfMhy14bd_HstW81tq_HaU_ynmlGoNdk-E5l88Ei6CkbIiM_LI9JP8AHySHS-IkAFgL-oQURe5QTxgPof_4DL9l9PayjkYsf8O7HcBIBiR844XEhk_xykrc6SZM1VMMDtrJT9_dimq9ZPDhw9FwM9cw0g9ThRJNMdn9XuKc0vH_sgLm-mLcCIHK_jV00TtN7QDqHbtSouBKsYhxqvU2ZI18fo4Ov4fEMb2-lhMhjnDz3yrww24KFUExsNnLkS9AsECA"}})

(deftest tracky-infrastructure-oauth2
  (testing "when calling 'get-access-token'"
    (testing "given valid auth code return access token"
      (providing
       [(client/post (:access-token-uri config) header) response]
       (let [result (SUT/get-access-token config {:query-params {"code" code}})]
         (is (= (-> response :body :id_token) (-> result :id-token))))))))

