(ns tracky.presentation.http.controllers.frontend.oauth2-ctrl
  (:require [integrant.core :as ig]
            [tracky.infrastructure.oauth2 :as oauth2]
            [clojure.data.json :as json]))

(defmethod ig/init-key :tracky.presentation.http.controllers.frontend.oauth2-ctrl/auth-code [_ config]
  (fn [request]
    (let [{:keys [id-token]} (oauth2/get-access-token config request)]
      {:status  200
       :headers {"Content-type" "application/json"}
       :body    (json/write-str {:access-token id-token})})))

