(ns tracky.presentation.cljs.app
  (:require [reagent.core :as r]
            [reagent.dom  :as dom]))

(defn init []
  (println "App initialization"))

(defn Application []
  [:div "Hello"])

(dom/render [Application] (js/document.getElementById "app"))
