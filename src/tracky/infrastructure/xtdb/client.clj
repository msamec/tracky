(ns tracky.infrastructure.xtdb.client
  (:require [xtdb.api :as xt]
            [integrant.core :as ig]))
(def node (atom nil))

(defn put [docs]
  (xt/submit-tx @node (vec
                       (for [doc docs]
                         [::xt/put doc])))
  (xt/sync @node))

(defn query [query & args]
  (let [db (xt/db @node)]
    (apply (partial xt/q db query) args)))

(defmethod ig/init-key ::start [_ {:keys [node-config]}]
  (reset! node (xt/start-node node-config)))
