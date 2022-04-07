(ns helpers.fixtures
  (:require [duct.core :as duct]
            ;[app.readers :refer [readers]]
            [integrant.core :as ig]
            [clojure.java.io :as io]))

(def state* (atom nil))

(def profiles [:duct.profile/test :duct.profile/prod])

(defn get-system []
  (@state* :system))

(defn read-config []
  (duct/read-config (io/resource "tracky/config.edn")))

(defn init-system! []
  (duct/load-hierarchy)
  (let [config (read-config)
        prepped-config (duct/prep-config config profiles)]
    (ig/init prepped-config)))

(defn halt-system! [system]
  (ig/halt! system))

(defn with-system! [test-fn]
  (try
    (reset! state* {:system (init-system!)})
    (test-fn)
    (catch Exception e
      (do
        (.printStackTrace e)
        (throw e)))
    (finally
      (when-let [state @state*]
        (halt-system! (:system state))
        (reset! state* nil)))))
