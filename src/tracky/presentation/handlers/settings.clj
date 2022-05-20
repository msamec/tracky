(ns tracky.presentation.handlers.settings
  (:require [integrant.core :as ig]
            [tracky.application.save-settings :as save-settings]
            [tracky.application.fetch-settings :as fetch-settings]))

(defmethod ig/init-key ::update [_ _]
  (fn [{:keys [data current-user]}]
    (save-settings/execute! current-user (-> data (dissoc :tracky/jwt)))
    {}))

(defmethod ig/init-key ::show [_ _]
  (fn [{:keys [_data current-user]}]
    (fetch-settings/execute! current-user)))
