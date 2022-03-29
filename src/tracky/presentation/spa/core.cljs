(ns tracky.presentation.spa.core
  (:require [reagent.core :as r]
            [reagent.dom  :as dom]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend.controllers :as rfc]
            [tracky.presentation.spa.entries :refer [Entries]]))

(defonce match (r/atom nil))

(def routes
  (rf/router
   ["/"
    [""
     {:name ::entries
      :view Entries}]]))

(defn init! []
  (rfe/start!
   routes
   (fn [new-match]
     (swap! match (fn [old-match]
                    (when new-match
                      (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))))))
   {:use-fragment true})
  (dom/render [Entries] (js/document.getElementById "app")))

(init!)
