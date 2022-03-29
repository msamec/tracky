(ns tracky.presentation.spa.api
  (:require [lambdaisland.fetch :as fetch]
            [hodgepodge.core :refer [local-storage]]
            [tracky.presentation.spa.components.oauth2 :refer [logout]]))

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

(defmulti handle-response (fn [{:keys [status]}] status))
(defmethod handle-response 200
  [response]
  (-> response :body (js->clj :keywordize-keys true)))
(defmethod handle-response 401
  [_response]
  (logout)
  (throw (js/Error. "Unauthorized")))
(defmethod handle-response :default
  [_response]
  (throw (js/Error. "Error"))) ;TODO: improve error handling

(defn fetch-entries [entries]
  (->
   (fetch/get "/api/entries/list" (options))
   (.then #(handle-response %))
   (.then #(reset! entries (:data %)))))

(defn sync [id]
  (->
   (fetch/post (str "/api/entries/sync/" id) (options))
   (.then #(handle-response %))))

(defn sync-all []
  (->
   (fetch/post "/api/entries/sync-all" (options))
   (.then #(handle-response %))))


