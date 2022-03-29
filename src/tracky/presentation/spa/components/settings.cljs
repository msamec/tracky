(ns tracky.presentation.spa.components.settings
  (:require [reagent.core :as r]
            [hodgepodge.core :refer [local-storage]]))

(defonce credential (r/atom {:toggl-key (:toggl-key local-storage)
                             :tempo-key (:tempo-key local-storage)
                             :jira-account-id (:jira-account-id local-storage)}))
(defn field [key desc]
  [:div
   {:class "mb-6"}
   [:label
    {:for key
     :class "block mb-2 text-sm text-gray-600"}
    desc]
   [:input
    {:class "w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300"
     :required "required",
     :name key,
     :value (key @credential)
     :type "text"
     :on-change (fn [e]
                  (.preventDefault e)
                  (let [v (-> e .-target .-value)]
                    (assoc! local-storage key v)
                    (swap! credential assoc key v)))}]])

(defn Settings []
  [:div
   {:class "container mx-auto"}
   [:div
    {:class "max-w-xl p-5 mx-auto my-10 bg-white rounded-md shadow-sm"}
    [:div
     [:form
      [field :toggl-key "Toggl api key"]
      [field :tempo-key "Tempo api key"]
      [field :jira-account-id "Jira account id"]]]]])
