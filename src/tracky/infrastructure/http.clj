(ns tracky.infrastructure.http
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [tracky.domain.exception :as exception]))

(defmulti handle-response (fn [response] (:status response)))
(defmethod handle-response 200
  [response]
  (->
   response
   :body
   (json/read-str :key-fn keyword)))
(defmethod handle-response :default
  [_response]
  (exception/service-unavailable "Issues with API!"))

(defn add-default [params]
  (merge
   {:throw-exceptions false}
   params))

(defn add-body [params body]
  (merge
   {:body (json/write-str body)}
   params))

(defn send-get
  ([url] (send-get url {}))
  ([url params]
   (->
    url
    (client/get (add-default params))
    (handle-response))))

(defn send-put [url params body]
  (->
   url
   (client/put
    (-> params add-default (add-body body)))))

(defn send-post [url params body]
  (->
   url
   (client/post
    (-> params add-default (add-body body)))))
