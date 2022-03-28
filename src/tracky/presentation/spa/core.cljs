(ns tracky.presentation.spa.core
  (:require [reagent.core :as r]
            [reagent.dom  :as dom]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend.controllers :as rfc]
            [tracky.presentation.spa.listing :refer [Listing]]))

(defonce match (r/atom nil))

(def routes
  (rf/router
   ["/"
    [""
     {:name ::listing
      :view Listing}]]))

(defn init! []
  (rfe/start!
   routes
   (fn [new-match]
     (swap! match (fn [old-match]
                    (when new-match
                      (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))))))
   {:use-fragment true})
  (dom/render [Listing] (js/document.getElementById "app")))

(init!)
