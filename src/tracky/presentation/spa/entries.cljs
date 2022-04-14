(ns tracky.presentation.spa.entries
  (:require
   [tracky.presentation.spa.components.table :refer [Table refresh]]))

(defn Entries []
  (refresh)
  [Table])

