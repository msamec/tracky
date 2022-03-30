(ns tracky.presentation.spa.components.oauth2
  (:require ["react-simple-oauth2-login$default" :as OAuth2Login]
            [hodgepodge.core :refer [local-storage]]
            [reagent.core :as r]
            [lambdaisland.fetch :as fetch]))

(def authenticated (r/atom (:access-token local-storage)))

(def url (.. js/window -location -origin))

(defn get-token [code authenticated]
  (->
   (fetch/get "/auth-code" {:query-params {:code code}})
   (.then #(-> % :body (js->clj :keywordize-keys true) :access-token))
   (.then #(assoc! local-storage :access-token %))
   (.then #(reset! authenticated true))))

(defn onSuccess [response] (->
                            response
                            (js->clj :keywordize-keys true)
                            :code
                            (get-token authenticated)))

(defn onFailure [response] (.error js/console response))

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

(defn logout [] (reset! authenticated nil))
