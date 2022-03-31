(ns tracky.domain.exception)

(defn unauthorized []
  (throw
   (ex-info "Unauthorized access" {:type ::unauthorized
                                   :status 401})))

(defn service-unavailable [cause]
  (throw
   (ex-info cause {:type ::service-unavailable
                   :status 503})))
