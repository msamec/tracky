(ns tracky.domain.exception)

(defn unauthorized []
  (throw 
    (ex-info "Unauthorized" {:type ::unauthorized})))
