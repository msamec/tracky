(ns tracky.infrastructure.datomic
  (:require [datomic.api :as d]
            [integrant.core :as ig]))

(def db-uri (System/getenv "DATOMIC_URI"))

(def conn (d/connect db-uri))

(def settings-schema [{:db/ident :settings/user-id
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/unique :db.unique/identity
                       :db/doc "User id"}
                      {:db/ident :settings/toggl-api-key
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "Toggl api key"}
                      {:db/ident :settings/tempo-api-key
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "Tempo api key"}
                      {:db/ident :settings/jira-account-id
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "Jira account id"}])

(defn transact [data]
  @(d/transact conn data))

(defn query [query & args]
  (let [db (d/db conn)]
    (apply (partial d/q query db) args)))

(defmethod ig/init-key ::init [_ _]
  (d/create-database db-uri)
  @(d/transact conn settings-schema))
