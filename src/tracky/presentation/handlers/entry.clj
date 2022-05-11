(ns tracky.presentation.handlers.entry
  (:require [integrant.core :as ig]))

(defmethod ig/init-key ::list-entries [_ _]
  (fn [{:keys [_data]}]
    []))

(defmethod ig/init-key ::single-entry [_ _]
  (fn [{:keys [_data]}]
    []))

(defmethod ig/init-key ::sync-single-entry [_ _]
  (fn [{:keys [_data]}]
    []))

(defmethod ig/init-key ::sync-all-entries [_ _]
  (fn [{:keys [_data]}]
    []))

(defmethod ig/init-key ::update-entry [_ _]
  (fn [{:keys [_data]}]
    []))
