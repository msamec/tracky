(ns tracky.infrastructure.middlewares.authorization
  (:require [integrant.core :as ig]
            [buddy.auth.accessrules :refer [wrap-access-rules success error]]
            [clojure.data.json :as json]))

(defn authenticated-access
  [request]
  (if (:identity request)
    (success)
    (error)))

(def rules [{:pattern #"^/api.*$"
             :handler authenticated-access}])

(defn on-error
  [_request _value]
  {:status  401
   :headers {"Content-type" "application/json"}
   :body    (json/write-str {:error :unauthorized})})

(defmethod ig/init-key :tracky.infrastructure.middlewares/authorization [_ _]
  (fn [handler]
    (wrap-access-rules handler {:rules rules
                                :on-error on-error})))
