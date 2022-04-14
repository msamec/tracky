(ns tracky.infrastructure.services.toggl
  (:require [clojure.string :as str]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [integrant.core :as ig]
            [tracky.domain.entry :refer [create-entry]]
            [tracky.domain.entry-repository :refer [EntryRepository -all! -one! -add-tags! -update-description!]]
            [tracky.infrastructure.http :as http]))

(defn- remove-synced [data]
  (remove (fn [{:keys [tags]}] (some #{"synced"} tags)) data))

(defn- remove-with-negative-duration [data]
  (filter (fn [{:keys [duration]}] (pos? duration)) data))

(defn- create [{:keys [id description duration start]}]
  (create-entry {:entry/id (str id)
                 :entry/log description
                 :entry/duration duration
                 :entry/start start}))

(defn- map-values [data]
  (->>
   data
   remove-synced
   remove-with-negative-duration
   (map create)))

(defn- auth [options]
  (let [api-key (get-in options [:toggl-api-key])]
    {:basic-auth [api-key "api_token"]}))

(def month-ago
  (let [now (t/now)
        one-month-ago (t/minus now (t/months 1))]
    (f/unparse (f/formatters :date-time) one-month-ago)))

(defn- start-date [{:keys [start-date] :or {start-date month-ago}}]
  start-date)

(defn all!
  [options]
  (let [url (str "https://api.track.toggl.com/api/v8/time_entries?start_date=" (start-date options))]
    (->
     url
     (http/send-get (auth options))
     map-values)))

(defn one! [id options]
  (let [url (str "https://api.track.toggl.com/api/v8/time_entries/" id)]
    (->
     url
     (http/send-get (auth options))
     :data
     list
     map-values
     first)))

(defn add-tags! [ids options]
  (let [url (str "https://api.track.toggl.com/api/v8/time_entries/" (str/join "," ids))]
    (->
     url
     (http/send-put
      (auth options)
      {:time_entry
       {:tags ["synced"]
        :tag_action "add"}}))))

(defn update-description! [id desc options]
  (let [url (str "https://api.track.toggl.com/api/v8/time_entries/" id)]
    (->
     url
     (http/send-put
      (auth options)
      {:time_entry
       {:description desc}}))))

(defmethod ig/init-key :tracky.infrastructure.services/toggl [_ _]
  (reify EntryRepository
    (-all! [this options] (all! options))
    (-one! [this id options] (one! id options))
    (-add-tags! [this ids options] (add-tags! ids options))
    (-update-description! [this id desc options] (update-description! id desc options))))
