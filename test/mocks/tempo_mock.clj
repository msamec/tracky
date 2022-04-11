(ns mocks.tempo-mock
  (:require [tracky.domain.worklog-repository :refer [WorklogRepository -save!]]
            [integrant.core :as ig]))

(defn save! [entry credential])

(defmethod ig/init-key :mocks.tempo-mock/tempo [_ _]
  (reify WorklogRepository
    (-save! [this entry credential] (save! entry credential))))
