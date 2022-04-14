(ns tracky.presentation.spa.api
  (:require [lambdaisland.fetch :as fetch]
            [hodgepodge.core :refer [local-storage]]
            [tracky.presentation.spa.components.loading :refer [loading-on loading-off]]
            [tracky.presentation.spa.components.alert :refer [danger]]
            [tracky.presentation.spa.authentication :refer [logout]]))

(defn meta-value [name]
  (.. js/document
      (querySelector (str "meta[name='" name "']"))
      (getAttribute "content")))

(defn options
  ([]
   (options {}))
  ([additional]
   (merge
    {:headers
     {:x-toggl-api-key (:toggl-key local-storage)
      :x-tempo-api-key (:tempo-key local-storage)
      :x-jira-account-id (:jira-account-id local-storage)
      :x-csrf-token (meta-value "csrf-token")
      :authorization (str "Bearer " (:access-token local-storage))}
     :accept :json
     :content-type :json}
    additional)))

(defn extract-body [response]
  (->
   response
   :body
   (js->clj :keywordize-keys true)))

(defn throw-error [message]
  (danger message)
  (throw (js/Error. message)))

(defmulti handle-response (fn [{:keys [status]}] status))
(defmethod handle-response 200
  [response]
  response)
(defmethod handle-response 401
  [response]
  (let [body (extract-body response)]
    (logout)
    (throw-error (:message body))))
(defmethod handle-response :default
  [response]
  (let [body (extract-body response)]
    (throw-error (:message body))))

(defn fetch-entries [entries]
  (->
   (fetch/get "/api/entries/list" (options))
   (.then #(handle-response %))
   (.then #(extract-body %))
   (.then #(reset! entries (:data %)))))

(defn fetch-entry [id entry]
  (loading-on)
  (->
   (fetch/get (str "/api/entries/single/" id) (options))
   (.then #(handle-response %))
   (.then #(extract-body %))
   (.then #(reset! entry (:data %)))
   (.finally #(loading-off))))

(defn sync [id]
  (loading-on)
  (->
   (fetch/post (str "/api/entries/sync/" id) (options))
   (.then #(handle-response %))
   (.then #(extract-body %))
   (.finally #(loading-off))))

(defn sync-all []
  (loading-on)
  (->
   (fetch/post "/api/entries/sync-all" (options))
   (.then #(handle-response %))
   (.then #(extract-body %))
   (.finally #(loading-off))))

(defn get-token [code]
  (->
   (fetch/get "/auth-code" (options {:query-params {:code code}}))
   (.then #(handle-response %))
   (.then #(extract-body %))))

(defn update-description [id description fn]
  (loading-on)
  (->
   (fetch/post (str "/api/entries/update-description/" id) (options {:body {:description description}}))
   (.then #(handle-response %))
   (.then #(extract-body %))
   (.then fn)
   (.finally #(loading-off))))
