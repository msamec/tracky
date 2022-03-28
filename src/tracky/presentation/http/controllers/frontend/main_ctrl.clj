(ns tracky.presentation.http.controllers.frontend.main-ctrl
  (:require
   [tracky.presentation.templates.main :refer [index]]
   [integrant.core :as ig]))

(defmethod ig/init-key :tracky.presentation.http.controllers.frontend.main-ctrl/index [_ _]
  (fn [_request]
    {:status  200
     :headers {"Content-type" "text/html"}
     :body    (index)}))

