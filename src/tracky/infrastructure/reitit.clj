(ns tracky.infrastructure.reitit
  (:require [integrant.core :as ig]
            [ring.middleware.session :refer [wrap-session]]))

(defmethod ig/init-key :tracky.infrastructure.reitit/opts [_ _]
  {:middleware [(wrap-session {:cookie-attrs {:same-site :lax}})]})
