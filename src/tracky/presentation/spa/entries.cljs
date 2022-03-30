(ns tracky.presentation.spa.entries
  (:require [tracky.presentation.spa.components.settings :refer [Settings]]
            [tracky.presentation.spa.components.table :refer [Table refresh]]
            [tracky.presentation.spa.components.oauth2 :refer [OAuth2 authenticated]]
            [tracky.presentation.spa.components.loading :refer [Overlay]]))

(defn Entries []
  [Overlay
   [:main
    {:class "container mx-w-6xl mx-auto py-4"}
    [:div
     {:class "flex flex-col"}
     (if-not (nil? @authenticated)
       (do
         (refresh)
         [:<>
          [Table]
          [Settings]])
       [OAuth2])]]])

