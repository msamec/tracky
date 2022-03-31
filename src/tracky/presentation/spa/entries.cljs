(ns tracky.presentation.spa.entries
  (:require [tracky.presentation.spa.components.settings :refer [Settings]]
            [tracky.presentation.spa.components.table :refer [Table refresh]]
            [tracky.presentation.spa.components.oauth2 :refer [OAuth2]]
            [tracky.presentation.spa.authentication :refer [authenticated]]
            [tracky.presentation.spa.components.loading :refer [Overlay]]
            [tracky.presentation.spa.components.alert :refer [Alert]]))

(defn Entries []
  [Overlay
   [:main
    {:class "container mx-w-6xl mx-auto py-4"}
    [Alert]
    [:div
     {:class "flex flex-col"}
     (if-not (nil? @authenticated)
       (do
         (refresh)
         [:<>
          [Table]
          [Settings]])
       [OAuth2])]]])

