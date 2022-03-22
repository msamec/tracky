(ns tracky.domain.worklog-repository
  (:require [integrant.core :as ig])) ;TODO not a fan of this here, domain should not know about infrastructure but I'll leave it for now

(def save! nil)

(defprotocol WorklogRepository
  (-save! [this entry credential]))

(defmethod ig/init-key :tracky.domain/worklog-repository [_ {:keys [impl]}]
  (def save! (partial -save! impl)))
