(ns tracky.infrastructure.middlewares.authorization
  (:require [integrant.core :as ig]
            [buddy.auth.accessrules :refer [wrap-access-rules success error]]
            [ring.util.response :as resp]))

(defn authenticated-access
  [request]
  (if (:identity request)
    (success)
    (error)))

(def rules [{:pattern #"^(?!\/(auth|spa|api)).*$"
             :handler authenticated-access}])

(defn on-error
  [_request _value]
  (resp/redirect "/auth/login"))

(defmethod ig/init-key :tracky.infrastructure.middlewares/authorization [_ _]
  (fn [handler]
    (wrap-access-rules handler {:rules rules
                                :on-error on-error})))
