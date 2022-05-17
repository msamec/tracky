(ns tracky.domain.settings-repository
  (:require [integrant.core :as ig])) ;TODO not a fan of this here, domain should not know about infrastructure but I'll leave it for now

(def save! nil)
(def fetch! nil)

(defprotocol SettingsRepository
  (-save! [this user-id settings])
  (-fetch! [this user-id]))

(defmethod ig/init-key :tracky.domain/settings-repository [_ {:keys [impl]}]
  (def save! (partial -save! impl))
  (def fetch! (partial -fetch! impl)))
