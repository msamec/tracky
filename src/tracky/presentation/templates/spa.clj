(ns tracky.presentation.templates.spa
  (:require [hiccup.page :refer [html5 include-js]]))

(defn render
  []
  (html5
   [:head
    [:title "Tracky"]
    (include-js "https://cdn.tailwindcss.com")]
   [:body
    {:class "relative antialiased bg-gray-100"}
    [:div {:id "app"}]
    (include-js "/app/js/main.js")]))
