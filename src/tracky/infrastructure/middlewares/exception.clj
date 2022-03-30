(ns tracky.infrastructure.middlewares.exception
  (:require [reitit.ring.middleware.exception :as middleware]
            [tracky.domain.exception :as exception]))

(defn handler [status message exception request]
  {:status status
   :body {:message message
          :exception (.getClass exception)
          :data (ex-data exception)
          :uri (:uri request)}})

(def custom
  (middleware/create-exception-middleware
    (merge
      middleware/default-handlers
      {::exception/unauthorized (partial handler 401 "Unauthorized")
       ::middleware/default (partial handler 400 "An error occurred")})))
