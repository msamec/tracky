(ns tracky.infrastructure.database.credential
  (:require [com.verybigthings.penkala.next-jdbc :refer [get-env insert! select-one!]]
            [com.verybigthings.penkala.relation :as r]
            [integrant.core :as ig]
            [tracky.domain.credenital :refer [create-credential]]
            [tracky.domain.credential-repository :refer [CredentialRepository -save! -fetch!]]))

(def credentials-spec {:name "credentials"
                       :columns ["id" "user_id" "options"]
                       :pk ["id"]})
(def credentials-rel (r/spec->relation credentials-spec))
(defn credentials-ins [db-credential] (r/->insertable (:credentials db-credential)))

(defn- build-credential
  [{:credentials/keys [id user-id options]}]
  (create-credential id user-id options))

(defn- insert [db-credential insert-data]
  (insert! db-credential (-> (credentials-ins db-credential) (r/on-conflict-do-update [:user-id] insert-data)) insert-data))

(defn fetch! [db-credential user-id]
  (when-let [credential (select-one! db-credential (-> credentials-rel (r/where [:= user-id :user-id])))]
    (build-credential credential)))

(defn save! [db-credential user-id options]
  (let [insert-data {:user-id user-id :options options}
        credential (insert db-credential insert-data)]
    (build-credential credential)))

(defmethod ig/init-key :tracky.infrastructure.database/credential [_ {:keys [db-uri]}]
  (let [db-credential (get-env db-uri)]
    (reify CredentialRepository
      (-save! [this user-id options] (save! db-credential user-id options))
      (-fetch! [this user-id] (fetch! db-credential user-id)))))
