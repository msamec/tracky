(ns tracky.presentation.http.controllers.oauth2-ctrl
  (:require [integrant.core :as ig]
            [tracky.infrastructure.oauth2 :as oauth2]
            [tracky.presentation.templates.login :as login]))

(defmethod ig/init-key :tracky.presentation.http.controllers.oauth2-ctrl/login [_ _]
  (->
   {:status 200
    :headers {"Content-type" "text/html"}
    :body (login/render)}
   constantly))

(defmethod ig/init-key :tracky.presentation.http.controllers.oauth2-ctrl/authorize [_ {:keys [config]}]
  (fn [request]
    (oauth2/authorize-handler request config)))

(defmethod ig/init-key :tracky.presentation.http.controllers.oauth2-ctrl/callback [_ {:keys [config]}]
  (fn [request]
    (oauth2/callback-handler request config)))

;NOTE: not 100% sure this is the best approach to have resuable configuration
(defmethod ig/init-key :tracky.presentation.http.controllers.oauth2-ctrl/config [_ config]
  config)
