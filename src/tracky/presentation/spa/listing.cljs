(ns tracky.presentation.spa.listing
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [tracky.presentation.spa.date :refer [seconds->duration]]
            [lambdaisland.fetch :as fetch]
            [hodgepodge.core :refer [local-storage]]))

(defonce entries (r/atom []))
(defonce credential (r/atom {:toggl-key (:toggl-key local-storage)
                             :tempo-key (:tempo-key local-storage)
                             :jira-account-id (:jira-account-id local-storage)}))

(defn meta-value [name]
  (.. js/document
      (querySelector (str "meta[name='" name "']"))
      (getAttribute "content")))

(defn options
  ([]
   (options {}))
  ([additional]
   (merge
    {:headers
     {:x-toggl-api-key (:toggl-key @credential)
      :x-tempo-api-key (:tempo-key @credential)
      :x-jira-account-id (:jira-account-id @credential)
      :x-csrf-token (meta-value "csrf-token")}
     :accept :json
     :content-type :json}
    additional)))

(defn fetch-entries []
  (->
   (fetch/get "/api/entries/list" (options))
   (.then #(-> % :body (js->clj :keywordize-keys true) :data))
   (.then #(reset! entries %))))

(defn sync [id]
  (->
   (fetch/post (str "/api/entries/sync/" id) (options))
   (.then #(fetch-entries))))

(defn sync-all []
  (->
   (fetch/post "/api/entries/sync-all" (options))))

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
         {:class "bg-blue-500 hover:bg-blue-700 text-white py-1 px-2 rounded"}
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
                            (sync id)))}
             "Sync"]]]))])

(defn table []
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
(defn settings []
  [:div
   {:class "container mx-auto"}
   [:div
    {:class "max-w-xl p-5 mx-auto my-10 bg-white rounded-md shadow-sm"}
    [:div
     [:form
      [field :toggl-key "Toggl api key"]
      [field :tempo-key "Tempo api key"]
      [field :jira-account-id "Jira account id"]]]]])

(defn Listing []
  (fetch-entries)
  [:main
   {:class "container mx-w-6xl mx-auto py-4"}
   [:div
    {:class "flex flex-col"}
    [table]
    [settings]]])

