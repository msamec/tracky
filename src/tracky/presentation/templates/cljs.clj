(ns tracky.presentation.templates.cljs
  (:require [hiccup.page :refer [html5 include-js]]))

(defn render
  []
  (html5
   [:head
    [:title "Tracky"]
    [:body
     [:h1 "Hello World"]
     [:div {:id "app"}]
     (include-js "/app/js/main.js")]]))
