(ns tracky.presentation.http.controllers.frontend.oauth2-ctrl
  (:require [integrant.core :as ig]
            [tracky.infrastructure.oauth2 :as oauth2]
            [ring.util.response :refer [response]]))

(defmethod ig/init-key :tracky.presentation.http.controllers.frontend.oauth2-ctrl/auth-code [_ config]
  (fn [request]
    (let [{:keys [id-token]} (oauth2/get-access-token config request)]
      (response {:access-token id-token}))))

