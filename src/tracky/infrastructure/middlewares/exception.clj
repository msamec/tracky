(ns tracky.infrastructure.middlewares.exception
  (:require [reitit.ring.middleware.exception :as middleware]
            [tracky.domain.exception :as exception]))

(defn handler [exception request]
  (let [data (ex-data exception)]
    {:status (:status data)
     :body {:message (ex-message exception)
            :exception (.getClass exception)
            :data data
            :uri (:uri request)}}))

(def custom
  (middleware/create-exception-middleware
   (merge
    middleware/default-handlers
    {::exception/unauthorized (partial handler)
     ::exception/service-unavailable (partial handler)
     ::middleware/default (partial handler 400 "An error occurred")})))
