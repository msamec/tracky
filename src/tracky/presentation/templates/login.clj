(ns tracky.presentation.templates.login
  (:require [tracky.presentation.templates.base :refer [base]]
            [hiccup.element :refer [link-to]]))

(defn render []
  (base
   [:main.container.mx-w-6xl.mx-auto.py-4
    (link-to {:class "bg-blue-500 hover:bg-blue-700 text-white py-1 px-2 rounded"} "/auth/oauth2" "Google login")
    ]))
