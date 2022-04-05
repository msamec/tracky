(ns tracky.infrastructure.http.toggl
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [integrant.core :as ig]
            [tracky.domain.entry :refer [create-entry]]
            [tracky.domain.entry-repository :refer [EntryRepository -all! -one! -add-tags!]]))

(defmulti map-values (fn [data] (type data)))
(defmethod map-values clojure.lang.PersistentVector
  [data]
  (->>
   data
   (remove (fn [{:keys [tags]}] (some #{"synced"} tags)))
   (filter (fn [{:keys [duration]}] (pos? duration)))
   (mapv map-values)))
(defmethod map-values clojure.lang.PersistentHashMap
  [{:keys [id description duration start]}]
  (create-entry {:entry/id (str id)
                 :entry/log description
                 :entry/duration duration
                 :entry/start start}))
(defmethod map-values nil [_] '())

(defmulti parse-response (fn [response] (:status response)))
(defmethod parse-response 200
  [response]
  (->
   response
   :body
   (json/read-str :key-fn keyword)))
(defmethod parse-response :default
  [_response]
  nil) ;TODO throw exception which Ring should catch and handle properly

(defn send-put-request [body url api-key]
  (->
   url
   (client/put {:basic-auth [api-key "api_token"]
                :body (json/write-str body)})))

(defn get-response [url api-key]
  (->
   url
   (client/get {:basic-auth [api-key "api_token"]
                :throw-exceptions false})
   parse-response))

(defn all! [credential]
  (let [url "https://api.track.toggl.com/api/v8/time_entries"
        api-key (get-in credential [:toggl-api-key])]
    (->
     url
     (get-response api-key)
     map-values)))

(defn one! [id credential]
  (let [url (str "https://api.track.toggl.com/api/v8/time_entries/" id)
        api-key (get-in credential [:toggl-api-key])]
    (->
     url
     (get-response api-key)
     :data
     map-values)))

(defn add-tags! [ids credential]
  (let [url (str "https://api.track.toggl.com/api/v8/time_entries/" (str/join "," ids))
        api-key (get-in credential [:toggl-api-key])]
    (->
     {:time_entry {:tags ["synced"]
                   :tag_action "add"}}
     (send-put-request url api-key))))

(defmethod ig/init-key :tracky.infrastructure.http/toggl [_ _]
  (reify EntryRepository
    (-all! [this credential] (all! credential))
    (-one! [this id credential] (one! id credential))
    (-add-tags! [this ids credential] (add-tags! ids credential))))
