(ns tracky.infrastructure.middlewares.session
  (:require [clj-jwt.core :refer [str->jwt]]))

(defn inject-token-into-header [{:keys [] {:keys [oauth2]} :session :as request}]
  (assoc-in
   request
   [:headers "authorization"]
   (str "Bearer " oauth2)))

(defn extract-sub-from-token [{:keys [] {:keys [oauth2]} :session :as request}]
  (if (not (nil? oauth2))
    (let [sub (-> oauth2 str->jwt :claims :sub)]
      (assoc-in request [:session :sub] sub))
    request))

(defn handle [handler]
  (fn [request]
    (->
     request
     inject-token-into-header
     extract-sub-from-token
     handler)))
