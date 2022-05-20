(ns tracky.infrastructure.xtdb
  (:require [xtdb.api :as xt]))

(def db-uri (System/getenv "DB_URI"))

(def node
  (xt/start-node {:xtdb.jdbc/connection-pool {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
                                              :db-spec {:jdbcUrl db-uri}}
                  :xtdb/tx-log {:xtdb/module 'xtdb.jdbc/->tx-log
                                :connection-pool :xtdb.jdbc/connection-pool}
                  :xtdb/document-store {:xtdb/module 'xtdb.jdbc/->document-store
                                        :connection-pool :xtdb.jdbc/connection-pool}}))
(defn put [docs]
  (xt/submit-tx node (vec
                      (for [doc docs]
                        [::xt/put doc])))
  (xt/sync node))

(defn query [query & args]
  (let [db (xt/db node)]
    (apply (partial xt/q db query) args)))
