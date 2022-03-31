(ns tracky.presentation.spa.components.oauth2
  (:require ["react-simple-oauth2-login$default" :as OAuth2Login]
            [tracky.presentation.spa.api :refer [get-token]]
            [tracky.presentation.spa.authentication :refer [login]]))

(def url (.. js/window -location -origin))

(defn onSuccess [response]
  (let [code (-> response (js->clj :keywordize-keys true) :code)]
    (->
     (get-token code)
     (.then #(login (:access-token %))))))

(defn onFailure [_response])

(defn OAuth2 []
  [:div
   {:class "mx-w-6xl mx-auto py-4"}
   [:> OAuth2Login {:authorizationUrl "https://accounts.google.com/o/oauth2/v2/auth"
                    :responseType "code"
                    :clientId "645484976867-vb1qfocg5aaf9pkmo5750hqlkp92kvmf.apps.googleusercontent.com"
                    :redirectUri (str url "/")
                    :onSuccess onSuccess
                    :onFailure onFailure
                    :scope "openid"
                    :className "bg-blue-500 hover:bg-blue-700 text-white py-1 px-2 rounded"}]])
