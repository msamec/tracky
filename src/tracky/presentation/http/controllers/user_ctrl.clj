(ns tracky.presentation.http.controllers.user-ctrl
  (:require [tracky.presentation.templates.login :as login]
            [integrant.core :as ig]
            [buddy.auth :refer [authenticated? throw-unauthorized]]))
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
    (->
     "index"
     response)))
