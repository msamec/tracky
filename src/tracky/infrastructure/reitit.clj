(ns tracky.infrastructure.reitit
  (:require [integrant.core :as ig]
            [tracky.infrastructure.middlewares.session :as session]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as rrc]
            [muuntaja.core :as m]
            [tracky.infrastructure.middlewares.exception :as exception]))

(defmethod ig/init-key :tracky.infrastructure.reitit/opts [_ {:keys [buddy access-rules]}]
  {:muuntaja (m/create)
   :middleware [muuntaja/format-middleware
                rrc/coerce-exceptions-middleware
                rrc/coerce-request-middleware
                rrc/coerce-response-middleware
                exception/custom
                session/handle
                buddy
                access-rules]})
