(ns tracky.presentation.http.controllers.api.funicular-ctrl
  (:require
   [integrant.core :as ig]
   [tracky.infrastructure.funicular :as funicular]))

(defmethod ig/init-key ::funicular [_ {:keys [funicular]}]
  {:post (fn [{:keys [body-params]}]
           (let [res (funicular/execute funicular body-params {})]
             {:status 200
              :body res}))})

