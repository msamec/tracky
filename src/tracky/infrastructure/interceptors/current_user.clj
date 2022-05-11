(ns tracky.infrastructure.interceptors.current-user
  (:require [integrant.core :as ig]))

(defmethod ig/init-key ::assoc [_ {:keys [authfn]}]
  (fn [ctx]
    (if-let [jwt (get-in ctx [:request :data :tracky/jwt])]
      (if-let [user-id (authfn {} jwt)]
        (assoc-in ctx [:request :current-user] user-id)
        ctx)
      ctx)))
