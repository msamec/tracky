(ns tracky.presentation.handlers.settings
  (:require [integrant.core :as ig]))

(defmethod ig/init-key ::update [_ _]
  (fn [{:keys [_data]}]
    []))

(defmethod ig/init-key ::show [_ _]
  (fn [{:keys [_data]}]
    []))
