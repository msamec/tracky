(ns tracky.infrastructure.rules)

(defn logged-in? [request]
  (if (:current-user request)
    true
    false))
