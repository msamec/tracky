(ns tracky.presentation.spa.components.settings
  (:require [reagent.core :as r]
            [hodgepodge.core :refer [local-storage]]
            [reitit.frontend.easy :as rfe]
            [tracky.presentation.spa.components.alert :refer [info]]))

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
     :defaultValue (key local-storage)
     :type "text"
     :on-change (fn [e]
                (.preventDefault e)
                (let [v (-> e .-target .-value)]
                  (swap! credential assoc key v)))}]])

(defn Settings []
  [:div
   {:class "container mx-auto"}
   [:div
    {:class "max-w-xl p-5 mx-auto my-2 bg-white rounded-md shadow-sm"}
    [:div
     [:form
      [field :toggl-key "Toggl api key"]
      [field :tempo-key "Tempo api key"]
      [field :jira-account-id "Jira account id"]]]
    [:a
     {:class "bg-yellow-300 hover:bg-yellow-500 text-black py-2 px-2 rounded"
      :href (rfe/href :entries {})}
     "Back"]
    [:button
     {:class "bg-blue-500 hover:bg-blue-700 text-white py-1 px-2 rounded float-right"
      :on-click (fn [e]
                  (.preventDefault e)
                  (assoc! local-storage :toggl-key (:toggl-key @credential))
                  (assoc! local-storage :tempo-key (:tempo-key @credential))
                  (assoc! local-storage :jira-account-id (:jira-account-id @credential))
                  (info "Successfully saved!"))}
     "Save"]]])
