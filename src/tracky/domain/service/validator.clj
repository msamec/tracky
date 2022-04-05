(ns tracky.domain.service.validator
  (:require [clojure.spec.alpha :as s]
            [tracky.domain.exception :as exception]))

(defn- extract-path-val [problems]
  (reduce
   (fn [aggregator data]
     (let [{:keys [path val]} data]
       (if-not (empty? path)
         (conj aggregator {:property path
                           :value val})
         aggregator)))
   []
   problems))

(defn validate [spec x]
  (when-not (s/valid? spec x)
    (let [data (s/explain-data spec x)
          problems (:clojure.spec.alpha/problems data)
          errors (extract-path-val problems)]
      (exception/unprocessable-entity errors))))
