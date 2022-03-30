(ns tracky.presentation.spa.components.loading
  (:require ["react-loading-overlay-ts$default" :as LoadingOverlay]
            [reagent.core :as r]))

(def loading (r/atom false))

(defn Overlay [content]
  [:> LoadingOverlay
   {:active @loading
    :text "Please wait..."}
   content])

(defn loading-on []
  (reset! loading true))

(defn loading-off []
  (reset! loading false))
