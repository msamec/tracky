(ns tracky.presentation.spa.components.alert
  (:require [reagent.core :as r]
            [clojure.string :as str]))

(defonce hidden (r/atom true))
(defonce alert-type (r/atom "red"))
(defonce message (r/atom ""))

(defn hidden-class [] (if (true? @hidden) "hidden" ""))

(defn danger [text]
  (reset! hidden false)
  (reset! alert-type "red")
  (reset! message text))

(defn Alert []
  [:div
   {:class (str/join " " ["max-w-xl mx-auto rounded-md shadow-sm" (hidden-class)])}
   [:div
    {:id "alert-additional-content-2"
     :class (str "p-4 mb-4 bg-" @alert-type "-100 rounded-lg dark:bg-" @alert-type "-200")
     :role "alert"}
    [:div
     {:class "flex items-center"}
     [:svg
      {:class (str "mr-2 w-5 h-5 text-" @alert-type "-700 dark:text-" @alert-type "-800")
       :fill "currentColor"
       :viewBox "0 0 20 20"
       :xmlns "http://www.w3.org/2000/svg"}
      [:path
       {:fill-rule "evenodd"
        :d "M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
        :clip-rule "evenodd"}]]
     [:h3
      {:class (str "text-lg font-medium text-" @alert-type "-700 dark:text-" @alert-type "-800")}
      "Alert"]]
    [:div
     {:class (str "mt-2 mb-4 text-sm text-" @alert-type "-700 dark:text-" @alert-type "-800")}
     @message]
    [:div
     {:class "flex"}
     [:button
      {:type "button"
       :class (str "text-" @alert-type "-700 bg-transparent border border-" @alert-type "-700 hover:bg-" @alert-type "-800 hover:text-white focus:ring-4 focus:outline-none focus:ring-" @alert-type "-300 font-medium rounded-lg text-xs px-3 py-1.5 text-center dark:border-" @alert-type "-800 dark:text-" @alert-type "-800 dark:hover:text-white")
       :aria-label "Close"
       :on-click (fn [_e] (reset! hidden true))}
      "Dismiss"]]]])
