(ns tracky.presentation.spa.components.oauth2
  (:require ["react-simple-oauth2-login$default" :as OAuth2Login]
            [tracky.presentation.spa.api :refer [get-token]]
            [hodgepodge.core :refer [local-storage]]
            [reagent.core :as r]))

(defonce authenticated (r/atom 
                         (if (nil? (:access-token local-storage))
                           false
                           true)))

(def url (.. js/window -location -origin))

(defn onSuccess [response] (->
                            response
                            (js->clj :keywordize-keys true)
                            :code
                            (get-token authenticated)))

(defn onFailure [response] (.error js/console response))

(defn OAuth2 []
  [:> OAuth2Login {:authorizationUrl "https://accounts.google.com/o/oauth2/v2/auth"
                   :responseType "code"
                   :clientId "645484976867-vb1qfocg5aaf9pkmo5750hqlkp92kvmf.apps.googleusercontent.com"
                   :redirectUri (str url "/")
                   :onSuccess onSuccess
                   :onFailure onFailure
                   :scope "openid"}])

