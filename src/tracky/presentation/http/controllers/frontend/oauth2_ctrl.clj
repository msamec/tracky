(ns tracky.presentation.http.controllers.frontend.oauth2-ctrl
  (:require [integrant.core :as ig]
            [tracky.infrastructure.oauth2 :as oauth2]))

(defmethod ig/init-key :tracky.presentation.http.controllers.frontend.oauth2-ctrl/callback [_ {:keys [config]}]
  (fn [request]
    (oauth2/callback-handler request config)))

;NOTE: not 100% sure this is the best approach to have resuable configuration
(defmethod ig/init-key :tracky.presentation.http.controllers.frontend.oauth2-ctrl/config [_ config]
  config)
