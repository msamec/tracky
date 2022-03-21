(ns tracky.infrastructure.reitit
  (:require [integrant.core :as ig]
            [tracky.infrastructure.middlewares.session :as session]))

(defmethod ig/init-key :tracky.infrastructure.reitit/opts [_ {:keys [buddy access-rules]}]
  {:middleware [session/handle
                buddy
                access-rules]})
