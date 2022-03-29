(ns tracky.presentation.spa.api
  (:require [lambdaisland.fetch :as fetch]
            [hodgepodge.core :refer [local-storage]]))

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

(defn fetch-entries [entries]
  (->
   (fetch/get "/api/entries/list" (options))
   (.then #(-> % :body (js->clj :keywordize-keys true) :data))
   (.then #(reset! entries %))))

(defn sync [id]
  (->
   (fetch/post (str "/api/entries/sync/" id) (options))))

(defn sync-all []
  (->
   (fetch/post "/api/entries/sync-all" (options))))

(defn get-token [code authenticated]
  (->
   (fetch/get "/auth-code" (options {:query-params {:code code}}))
   (.then #(-> % :body (js->clj :keywordize-keys true) :access-token))
   (.then #(assoc! local-storage :access-token %))
   (.then #(reset! authenticated true))))
