(ns tracky.presentation.spa.entries
  (:require [tracky.presentation.spa.components.settings :refer [Settings]]
            [tracky.presentation.spa.components.table :refer [Table]]
            [tracky.presentation.spa.components.oauth2 :refer [OAuth2 authenticated]]))

(defn Entries []
  [:main
   {:class "container mx-w-6xl mx-auto py-4"}
   [:div
    {:class "flex flex-col"}
    (if @authenticated
      [:<>
       [Table]
       [Settings]]
      [OAuth2])]])

