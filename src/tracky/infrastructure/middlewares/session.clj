(ns tracky.infrastructure.middlewares.session
  (:require [clj-jwt.core :refer [str->jwt]]))

(defn extract-sub-from-token [{:keys [] {:keys [oauth2]} :session :as request}]
  (if (not (nil? oauth2))
    (let [sub (-> oauth2 str->jwt :claims :sub)]
      (assoc-in request [:session :user-id] sub))
    request))

(defn handle [handler]
  (fn [request]
    (->
     request
     extract-sub-from-token
     handler)))
