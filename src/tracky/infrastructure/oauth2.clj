(ns tracky.infrastructure.oauth2
  (:require [ring.util.request :as req]
            [clj-time.core :as time]
            [clj-http.client :as http]
            [ring.util.response :as resp]))

(defn- redirect-uri [config request]
  (-> (req/request-url request)
      (java.net.URI/create)
      (.resolve (:redirect-uri config))
      str))

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
  [{:keys [access-token-uri client-id client-secret] :as config} request]
  (format-access-token
   (http/post access-token-uri
              {:accept :json :as  :json,
               :form-params {:grant_type    "authorization_code"
                             :code          (get-authorization-code request)
                             :redirect_uri  (redirect-uri config request)
                             :client_id     client-id
                             :client_secret client-secret}
               :throw-exceptions false})))

(defn- state-mismatch [_]
  {:status 400
   :headers {"Content-type" "text/html"}
   :body "State mismatch"})

(defn- no-auth-code [_]
  {:status 400
   :headers {"Content-type" "text/html"}
   :body "No authorization code"})

(defn- state-matches? [request]
  (= (get-in request [:session ::state])
     (get-in request [:query-params "state"])))

(defn callback-handler [{:keys [session] :or {session {}} :as request} {:keys [landing-uri] :as config}]
  (cond
    (not (state-matches? request))
    (state-mismatch request)

    (nil? (get-authorization-code request))
    (no-auth-code request)

    :else
    (let [access-token (get-access-token config request)
          token (:id-token access-token)]
      (->
       (resp/redirect landing-uri)
       (assoc :session (-> session
                           (assoc-in [:oauth2] token)
                           (dissoc ::state)))))))
