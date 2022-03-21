(ns tracky.infrastructure.reitit
  (:require [integrant.core :as ig]
            [tracky.infrastructure.middlewares.header :as header]))

(defmethod ig/init-key :tracky.infrastructure.reitit/opts [_ {:keys [buddy access-rules]}]
  {:middleware [header/inject-token-to-header
                buddy
                access-rules]})
