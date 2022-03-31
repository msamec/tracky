(ns tracky.presentation.spa.authentication
  (:require [reagent.core :as r]
            [hodgepodge.core :refer [local-storage]]))

(def authenticated (r/atom (:access-token local-storage)))

(defn login [access-token]
  (assoc! local-storage :access-token access-token)
  (reset! authenticated true))

(defn logout []
  (reset! authenticated nil)
  (dissoc! local-storage :access-token))
