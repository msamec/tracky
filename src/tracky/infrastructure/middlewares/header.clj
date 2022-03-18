(ns tracky.infrastructure.middlewares.header)

(defn inject-token-to-header [handler]
  (fn [request]
    (->
     request
     (assoc-in [:headers "authorization"] (str "Bearer " (-> request :session :oauth2)))
     (handler))))
