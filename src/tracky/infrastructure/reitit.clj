(ns tracky.infrastructure.reitit
  (:require [integrant.core :as ig]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as rrc]
            [muuntaja.core :as m]
            [tracky.infrastructure.middlewares.exception-handler :as exception-handler]))

(defmethod ig/init-key :tracky.infrastructure.reitit/opts [_ {:keys [buddy access-rules]}]
  {:muuntaja (m/create)
   :middleware [muuntaja/format-middleware
                rrc/coerce-exceptions-middleware
                rrc/coerce-request-middleware
                rrc/coerce-response-middleware
                exception-handler/custom
                buddy
                access-rules]})
