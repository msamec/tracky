(ns tracky.presentation.spa.components.table
  (:require [tracky.presentation.spa.helpers.date :refer [seconds->duration]]
            [clojure.string :as str]
            [reagent.core :as r]
            [reitit.frontend.easy :as rfe]
            [tracky.presentation.spa.api :refer [sync sync-all fetch-entries]]))

(defonce entries (r/atom []))

(defn refresh [] (fetch-entries entries))

(defn settings []
  [:a
   {:class "bg-yellow-300 hover:bg-yellow-500 text-black py-1 px-2 rounded"
    :href (rfe/href :settings {})}
   "Settings"])

(defn up-to-date []
  [:div
   {:class "container mx-auto"}
   [:div
    {:class "max-w-xl p-5 mx-auto bg-white rounded-md shadow-sm"}
    [:span
     {:class "inline-flex px-2"}
     "You are up to date! :)"]
    (settings)]])

(defn th [content]
  [:th
   {:class "text-sm font-medium text-gray-900 px-6 py-4 text-left"
    :scope "col"}
   content])

(defn thead []
  [:thead
   {:class "bg-white border-b"}
   [:tr
    [th (settings)]
    [th "Original description"]
    [th "Jira task id"]
    [th "Description"]
    [th "Duration"]
    [th "Start date"]
    [th "Syncable"]
    [th [:button
         {:class "bg-blue-500 hover:bg-blue-700 text-white py-1 px-2 rounded"
          :on-click (fn [_e]
                      (->
                       (sync-all)
                       (.then #(refresh))))}
         "Sync all"]]]])

(defn td [content]
  [:td
   {:class "text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap"}
   content])

(defn tbody []
  [:tbody
   (for [{:keys [id task-id description original-description duration start-date syncable]} @entries]
     (let [bg-color (if syncable "bg-green-200" "bg-red-100")]
       [:tr
        {:key id
         :class (str/join " " ["border-b transition duration-300 ease-in-out" bg-color])}
        [td [:a
             {:class "bg-yellow-300 hover:bg-yellow-500 text-black py-1 px-2 rounded"
              :href (rfe/href :update-entry {:id id})}
             "Edit"]]
        [:td
         {:class "text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap"}
         original-description]
        [td task-id]
        [td description]
        [td (seconds->duration duration)]
        [td start-date]
        [td (if syncable "Yes" "No")]
        [td (when syncable
              [:button
               {:class "bg-green-500 hover:bg-green-700 text-white py-1 px-2 rounded"
                :value id
                :on-click (fn [e]
                            (let [id (-> e .-target .-value)]
                              (->
                               (sync id)
                               (.then #(refresh)))))}
               "Sync"])]]))])

(defn Table []
  [:div
   {:class "overflow-x-auto sm:-mx-6 lg:-mx-8"}
   [:div
    {:class "py-2 inline-block min-w-full sm:px-6 lg:px-8"}
    [:div
     {:class "overflow-hidden"}
     (if (empty? @entries)
       [up-to-date]
       [:table
        {:class "min-w-full"}
        [thead]
        [tbody]])]]])
