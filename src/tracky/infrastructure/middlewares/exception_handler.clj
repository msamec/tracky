(ns tracky.infrastructure.middlewares.exception-handler
  (:require [reitit.ring.middleware.exception :as middleware]
            [tracky.domain.exception :as exception]))

(defn handler [exception request]
  (let [data (ex-data exception)]
    {:status (or (:status data) 400)
     :body {:message (or (ex-message exception) "An error occurred")
            :exception (.getClass exception)
            :data data
            :uri (:uri request)}}))

(def custom
  (middleware/create-exception-middleware
   (merge
    middleware/default-handlers
    {::exception/unauthorized (partial handler)
     ::exception/service-unavailable (partial handler)
     ::exception/unprocessable-entity (partial handler)
     ::middleware/default (partial handler)})))
