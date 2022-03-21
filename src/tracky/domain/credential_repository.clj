(ns tracky.domain.credential-repository
  (:require [integrant.core :as ig]))

(def save! nil)
(def fetch! nil)

(defprotocol CredentialRepository
  (-save! [this user-id options])
  (-fetch! [this user-id]))

(defmethod ig/init-key :tracky.domain/credential-repository [_ {:keys [impl]}]
  (def save! (partial -save! impl))
  (def fetch! (partial -fetch! impl)))
