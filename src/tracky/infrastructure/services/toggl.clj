(ns tracky.infrastructure.services.toggl
  (:require [clojure.string :as str]
            [integrant.core :as ig]
            [tracky.domain.entry :refer [create-entry]]
            [tracky.domain.entry-repository :refer [EntryRepository -all! -one! -add-tags!]]
            [tracky.infrastructure.http :as http]))

(defn remove-synced [data]
  (remove (fn [{:keys [tags]}] (some #{"synced"} tags)) data))

(defn remove-with-negative-duration [data]
  (filter (fn [{:keys [duration]}] (pos? duration)) data))

(defn create [{:keys [id description duration start]}]
  (create-entry {:entry/id (str id)
                 :entry/log description
                 :entry/duration duration
                 :entry/start start}))

(defn map-values [data]
  (->>
   data
   remove-synced
   remove-with-negative-duration
   (map create)))

(defn auth [credential]
  (let [api-key (get-in credential [:toggl-api-key])]
    {:basic-auth [api-key "api_token"]}))

(defn all! [credential]
  (let [url "https://api.track.toggl.com/api/v8/time_entries"]
    (->
     url
     (http/send-get (auth credential))
     map-values)))

(defn one! [id credential]
  (let [url (str "https://api.track.toggl.com/api/v8/time_entries/" id)]
    (->
     url
     (http/send-get (auth credential))
     :data
     list
     map-values
     first)))

(defn add-tags! [ids credential]
  (let [url (str "https://api.track.toggl.com/api/v8/time_entries/" (str/join "," ids))]
    (->
     url
     (http/send-put
      (auth credential)
      {:time_entry
       {:tags ["synced"]
        :tag_action "add"}}))))

(defmethod ig/init-key :tracky.infrastructure.services/toggl [_ _]
  (reify EntryRepository
    (-all! [this credential] (all! credential))
    (-one! [this id credential] (one! id credential))
    (-add-tags! [this ids credential] (add-tags! ids credential))))
