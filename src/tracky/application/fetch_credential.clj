(ns tracky.application.fetch-credential
  (:require [tracky.domain.credential-repository :as credential-repository]
            ;[tracky.domain.mailer :as mailer]
            ;[tracky.domain.message :refer [to-map]]
            ;[tracky.domain.messages.send-reminder :refer [->SendReminderMessage]]
            ))

(defn execute! [user-id]
  (credential-repository/fetch! user-id))
