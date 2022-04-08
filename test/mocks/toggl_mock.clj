(ns mocks.toggl-mock
  (:require [tracky.domain.entry-repository :refer [EntryRepository -all! -one! -add-tags!]]
            [integrant.core :as ig]
            [clj-http.client :as client]
            [tracky.infrastructure.services.toggl :as toggl]
            [clojure.data.json :as json]))

(def entry-single
  {:data
   {:description "ID-134 | CLJ",
    :start "2022-03-29T06:00:00+00:00",
    :duration 27000,
    :id "123"}})

(def entries-list
  [{:description "ID-134 | CLJ",
    :start "2022-03-29T06:00:00+00:00",
    :duration 27000,
    :id "123"}])

(defmulti parse-type (fn [type] type))
(defmethod parse-type "list-single"
  [_]
  {:status 200
   :body (json/write-str entries-list)})
(defmethod parse-type "empty"
  [_response]
  {:status 200
   :body (json/write-str [])})
(defmethod parse-type "error"
  [_response]
  {:status 500})

(defn all! [{:keys [toggl-api-key]}]
  (with-redefs [client/get (fn [_url _params] (parse-type toggl-api-key))]
    (toggl/all! [])))

(defn one! [_id _credential]
  {})

(defn add-tags! [_ids _credential]
  {})

(defmethod ig/init-key :mocks.toggl-mock/toggl [_ _]
  (reify EntryRepository
    (-all! [this credential] (all! credential))
    (-one! [this id credential] (one! id credential))
    (-add-tags! [this ids credential] (add-tags! ids credential))))
