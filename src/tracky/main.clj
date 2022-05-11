(ns tracky.main
  (:gen-class)
  (:require [duct.core :as duct]
            [tracky.infrastructure.readers :refer [readers]]))

(duct/load-hierarchy)

(defn -main [& args]
  (let [keys     (or (duct/parse-keys args) [:duct/daemon])
        profiles [:duct.profile/prod]]
    (-> (duct/resource "tracky/config.edn")
        (duct/read-config readers)
        (duct/exec-config profiles keys))
    (System/exit 0)))
