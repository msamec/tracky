(ns tracky.presentation.http.controllers.user-ctrl
  (:require [tracky.presentation.templates.index :as index]
            [integrant.core :as ig]
            [tracky.application.fetch-entries :as fetch-entries]
            [tracky.application.fetch-credential :as fetch-credential]
            [ring.util.response :refer [redirect]]
            [tracky.application.update-credential :as update-credential]))
(defn response
  [body]
  {:status  200
   :headers {"Content-type" "text/html"}
   :body    body})

(defmethod ig/init-key :tracky.presentation.http.controllers.user-ctrl/listing [_ _]
  (fn [{:keys [] {:keys [user-id]} :session}]
    (let [entries (fetch-entries/execute! user-id)
          credential (fetch-credential/execute! user-id)]
      (->
       entries
       (index/render credential)
       response))))

(defmethod ig/init-key :tracky.presentation.http.controllers.user-ctrl/sync [_ _]
  (fn [{:keys [] {:keys [user-id]} :session}]
    (let [entries (fetch-entries/execute! user-id)
          credential (fetch-credential/execute! user-id)]
      (->
       entries
       (index/render credential)
       response))))

(defmethod ig/init-key :tracky.presentation.http.controllers.user-ctrl/sync-all [_ _]
  (fn [{:keys [] {:keys [user-id]} :session}]
    (let [entries (fetch-entries/execute! user-id)
          credential (fetch-credential/execute! user-id)]
      (->
       entries
       (index/render credential)
       response))))

(defmethod ig/init-key :tracky.presentation.http.controllers.user-ctrl/update-credentials [_ _]
  (fn [request]
    (let [{:keys [] {:keys [user-id]} :session} request
          {:keys [] {:keys [options]} :params} request]
      (update-credential/execute! user-id options)
      (redirect "/"))))
