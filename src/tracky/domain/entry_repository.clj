(ns tracky.domain.entry-repository
  (:require [integrant.core :as ig])) ;TODO not a fan of this here, domain should not know about infrastructure but I'll leave it for now

(def all! nil)
(def one! nil)
(def add-tags! nil)

(defprotocol EntryRepository
  (-all! [this options])
  (-one! [this id options])
  (-add-tags! [this ids options]))

(defmethod ig/init-key :tracky.domain/entry-repository [_ {:keys [impl]}]
  (def all! (partial -all! impl))
  (def one! (partial -one! impl))
  (def add-tags! (partial -add-tags! impl)))
