(ns tracky.infrastructure.reitit
  (:require [integrant.core :as ig]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as rrc]
            [muuntaja.core :as m]
            [tracky.infrastructure.middlewares.exception-handler :as exception-handler]
            [com.verybigthings.funicular.transit :as funicular-transit]))

(def muuntaja-instance
  (m/create
   (-> m/default-options
       (assoc-in [:formats "application/transit+json" :decoder-opts] funicular-transit/read-handlers)
       (assoc-in [:formats "application/transit+json" :encoder-opts] funicular-transit/write-handlers))))

(defmethod ig/init-key :tracky.infrastructure.reitit/opts [_ {:keys [buddy access-rules]}]
  {:muuntaja muuntaja-instance
   :middleware [muuntaja/format-middleware
                rrc/coerce-exceptions-middleware
                rrc/coerce-request-middleware
                rrc/coerce-response-middleware
                exception-handler/custom
                buddy
                access-rules]})
