(ns tracky.infrastructure.xtdb.node
  (:require [integrant.core :as ig]))

(defmethod ig/init-key ::config [_ {:keys [db-uri]}]
  {:xtdb.jdbc/connection-pool
   {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
    :db-spec {:jdbcUrl db-uri}}
   :xtdb/tx-log {:xtdb/module 'xtdb.jdbc/->tx-log
                 :connection-pool :xtdb.jdbc/connection-pool}
   :xtdb/document-store {:xtdb/module 'xtdb.jdbc/->document-store
                         :connection-pool :xtdb.jdbc/connection-pool}})

