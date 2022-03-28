(ns tracky.presentation.templates.spa
  (:require [hiccup.page :refer [html5 include-js]]
            [ring.middleware.anti-forgery :as anti-forgery]))

(defn render
  []
  (html5
   [:head
    [:meta {:name "csrf-token" :content anti-forgery/*anti-forgery-token*}]
    [:title "Tracky"]
    (include-js "https://cdn.tailwindcss.com")]
   [:body
    {:class "relative antialiased bg-gray-100"}
    [:div {:id "app"}]
    (include-js "/app/js/main.js")]))
