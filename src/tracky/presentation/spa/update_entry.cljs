(ns tracky.presentation.spa.update-entry
  (:require [tracky.presentation.spa.components.alert :refer [success]]
            [reitit.frontend.easy :as rfe]
            [reagent.core :as r]
            [tracky.presentation.spa.api :refer [sync fetch-entry]]
            [tracky.presentation.spa.api :refer [update-description]]))

(def entry (r/atom {:original-description nil}))

(defn form []
  [:div
   {:class "container mx-auto"}
   [:div
    {:class "max-w-xl p-5 mx-auto my-10 bg-white rounded-md shadow-sm"}
    [:div
     [:div
      {:class "mb-6"}
      [:label
       {:for :description
        :class "block mb-2 text-sm text-gray-600"}
       "Description"]
      [:input
       {:class "w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300"
        :required "required",
        :name :description ,
        :value (:original-description @entry)
        :type "text"
        :on-change (fn [e]
                     (.preventDefault e)
                     (swap! entry assoc :original-description (-> e .-target .-value)))}]]
     [:div
      {:class "mb-6"}
      [:button
       {:class "bg-green-500 hover:bg-green-700 text-white py-1 px-2 mx-2 rounded"
        :on-click (fn [e]
                    (.preventDefault e)
                    (update-description
                     (:id @entry)
                     (:original-description @entry)
                     (fn []
                       (success "Description updated!")
                       (rfe/push-state :entries))))}

       "Save"]
      [:button
       {:class "bg-red-500 hover:bg-red-700 text-white py-1 px-2 mx-2 rounded"
        :on-click (fn [e]
                    (.preventDefault e)
                    (rfe/push-state :entries))}
       "Cancel"]]]]])

(defn UpdateEntry [{{id :id} :path-params}]
  (reset! entry nil)
  (fetch-entry id entry)
  [form])

