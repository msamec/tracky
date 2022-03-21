(ns tracky.presentation.http.controllers.user-ctrl
  (:require [tracky.presentation.templates.login :as login]
            [tracky.presentation.templates.index :as index]
            [integrant.core :as ig]
            [tracky.application.fetch-entries :as fetch-entries]
            [tracky.application.fetch-credential :as fetch-credential]))
(defn response
  [body]
  {:status  200
   :headers {"Content-type" "text/html"}
   :body    body})

(defmethod ig/init-key :tracky.presentation.http.controllers.user-ctrl/login [_ _]
  (->
   (login/render)
   response
   constantly))

(defmethod ig/init-key :tracky.presentation.http.controllers.user-ctrl/index [_ _]
  (fn [request]
    (let [user-id "1" ;TODO: this should be user id from jwt token
          entries (fetch-entries/execute! user-id)
          credential (fetch-credential/execute! user-id)]
      (->
       entries
       (index/render credential)
       response))))
