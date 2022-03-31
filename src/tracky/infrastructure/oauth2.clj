(ns tracky.infrastructure.oauth2
  (:require [clj-time.core :as time]
            [clj-http.client :as http]
            [tracky.domain.exception :as exception]))

(defn- get-authorization-code [request]
  (get-in request [:query-params "code"]))

(defn- coerce-to-int [n]
  (if (string? n)
    (Integer/parseInt n)
    n))

(defn- format-access-token
  [{{:keys [access_token expires_in refresh_token id_token] :as body} :body}]
  (-> {:token access_token
       :extra-data (dissoc body :access_token :expires_in :refresh_token :id_token)}
      (cond-> expires_in (assoc :expires (-> expires_in
                                             coerce-to-int
                                             time/seconds
                                             time/from-now))
              refresh_token (assoc :refresh-token refresh_token)
              id_token (assoc :id-token id_token))))

(defn get-access-token
  [{:keys [access-token-uri client-id client-secret redirect-uri]} request]
  (try
    (->
     access-token-uri
     (http/post
      {:accept :json :as  :json,
       :form-params {:grant_type    "authorization_code"
                     :code          (get-authorization-code request)
                     :redirect_uri  redirect-uri
                     :client_id     client-id
                     :client_secret client-secret}
       :throw-exceptions false})
     (format-access-token))
    (catch Exception _e
      (exception/service-unavailable "Issues with OAuth2"))))
