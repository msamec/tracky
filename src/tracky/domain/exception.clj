(ns tracky.domain.exception)

(defn unprocessable-entity [errors]
  (throw
   (ex-info "Unprocessable entity" {:type ::unprocessable-entity
                                    :status 422
                                    :errors errors})))
(defn unauthorized []
  (throw
   (ex-info "Unauthorized access" {:type ::unauthorized
                                   :status 401})))

(defn service-unavailable [cause]
  (throw
   (ex-info cause {:type ::service-unavailable
                   :status 503})))
