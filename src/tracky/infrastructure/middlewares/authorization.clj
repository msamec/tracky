(ns tracky.infrastructure.middlewares.authorization
  (:require [integrant.core :as ig]
            [buddy.auth.accessrules :refer [wrap-access-rules success error]]
            [tracky.domain.exception :as exception]))

(defn authenticated-access
  [request]
  (if (:identity request)
    (success)
    (error)))

(def rules [{:pattern #"^/api.*$"
             :handler authenticated-access}])

(defn on-error
  [_request _value]
  (exception/unauthorized))

(defmethod ig/init-key :tracky.infrastructure.middlewares/authorization [_ _]
  (fn [handler]
    (wrap-access-rules handler {:rules rules
                                :on-error on-error})))
