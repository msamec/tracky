(ns tracky.presentation.spa.core
  (:require [reagent.core :as r]
            [reagent.dom  :as dom]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend.controllers :as rfc]
            [schema.core :as s]
            [tracky.presentation.spa.entries :refer [Entries]]
            [tracky.presentation.spa.update-entry :refer [UpdateEntry]]
            [tracky.presentation.spa.components.loading :refer [Overlay]]
            [tracky.presentation.spa.components.alert :refer [Alert]]
            [tracky.presentation.spa.components.oauth2 :refer [OAuth2]]
            [tracky.presentation.spa.authentication :refer [authenticated]]
            [tracky.presentation.spa.components.settings :refer [Settings]]))

(defonce match (r/atom nil))

(def routes
  (rf/router
   ["/"
    [""
     {:name :entries
      :view Entries}]
    ["update-entry/:id"
     {:name :update-entry
      :parameters {:path {:id s/Str}}
      :view UpdateEntry}]]))

(defn current-page []
  [Overlay
   [:main
    {:class "container mx-w-6xl mx-auto py-4"}
    [Alert]
    [:div
     {:class "flex flex-col"}
     (if-not (nil? @authenticated)
       (when @match
         (let [view (:view (:data @match))]
           [:<>
            [view @match]
            [Settings]]))
       [OAuth2])]]])

(defn init! []
  (rfe/start!
   routes
   (fn [new-match]
     (swap! match (fn [old-match]
                    (when new-match
                      (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))))))
   {:use-fragment true})
  (dom/render [current-page] (js/document.getElementById "app")))

(init!)
