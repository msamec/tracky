(ns tracky.domain.service.validator)

(defn make-unsyncable [entry]
  (assoc entry :syncable false))

(defn task-id-is-not-nil [{:keys [task-id] :as entry}]
  (if (nil? task-id)
    (make-unsyncable entry)
    entry))

(defn description-is-not-nil [{:keys [description] :as entry}]
  (if (nil? description)
    (make-unsyncable entry)
    entry))

(defn duration-is-greater-than-1-minute [{:keys [duration] :as entry}]
  (if (> 60 duration)
    (make-unsyncable entry)
    entry))

(defn validate-entry [entry]
  (->
   entry
   task-id-is-not-nil
   description-is-not-nil
   duration-is-greater-than-1-minute))

