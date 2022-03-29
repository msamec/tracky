(ns tracky.presentation.spa.components.table
  (:require [tracky.presentation.spa.helpers.date :refer [seconds->duration]]
            [clojure.string :as str]
            [reagent.core :as r]
            [tracky.presentation.spa.api :refer [sync sync-all fetch-entries]]))

(defonce entries (r/atom []))

(defn up-to-date []
  [:div
   {:class "container mx-auto"}
   [:div
    {:class "max-w-xl p-5 mx-auto my-10 bg-white rounded-md shadow-sm"}
    [:span
     {:class "inline-flex px-2"}
     "You are up to date! :)"]]])

(defn th [content]
  [:th
   {:class "text-sm font-medium text-gray-900 px-6 py-4 text-left"
    :scope "col"}
   content])

(defn thead []
  [:thead
   {:class "bg-white border-b"}
   [:tr
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
                       (.then #(fetch-entries entries))))}
         "Sync all"]]]])

(defn td [content]
  [:td
   {:class "text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap"}
   content])

(defn tbody []
  [:tbody
   (for [{:keys [id task-id description duration start-date syncable]} @entries]
     (let [bg-color (if syncable "bg-green-200" "bg-red-100")]
       [:tr
        {:key id
         :class (str/join " " ["border-b transition duration-300 ease-in-out" bg-color])}
        [td task-id]
        [td description]
        [td (seconds->duration duration)]
        [td start-date]
        [td (if syncable "Yes" "no")]
        [td [:button
             {:class "bg-green-500 hover:bg-green-700 text-white py-1 px-2 rounded"
              :value id
              :on-click (fn [e]
                          (let [id (-> e .-target .-value)]
                            (->
                             (sync id)
                             (.then #(fetch-entries entries)))))}
             "Sync"]]]))])

(defn Table []
  (fetch-entries entries)
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
