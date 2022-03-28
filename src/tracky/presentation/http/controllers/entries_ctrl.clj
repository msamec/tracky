(ns tracky.presentation.http.controllers.entries-ctrl
  (:require
   [tracky.domain.entry-repository :as entry-repository]
   [integrant.core :as ig]
   [clojure.data.json :as json]))
(defn response
  [body]
  {:status  200
   :headers {"Content-type" "application/json"}
   :body    body})

(defmethod ig/init-key :tracky.presentation.http.controllers.entries-ctrl/list [_ _]
  (fn [{:keys [headers]}] ;TODO: should be extracted into single method
    (let [toggl-api-key (get headers "x-toggl-api-key")]
      (let [entries (entry-repository/all! {:options {:toggl-key toggl-api-key}})]
        (response (json/write-str {:data entries}))))))
