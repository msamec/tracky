(ns tracky.domain.service.date
  (:require [clojure.string :as str]))

(defn- parse-date [date]
  (.parse
   (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss")
   date))

(defn format-date [date]
  (->>
   date
   parse-date
   (.format
    (java.text.SimpleDateFormat. "yyyy-MM-dd"))))

(defn format-time [date]
  (->>
   date
   parse-date
   (.format
    (java.text.SimpleDateFormat. "HH:mm:ss"))))

(def seconds-in-minute 60)
(def seconds-in-hour (* 60 seconds-in-minute))
(def seconds-in-day (* 24 seconds-in-hour))
(def seconds-in-week (* 7 seconds-in-day))

(defn seconds->duration [seconds]
  {:pre [(int? seconds)]}
  (let [weeks   ((juxt quot rem) seconds seconds-in-week)
        wk      (first weeks)
        days    ((juxt quot rem) (last weeks) seconds-in-day)
        d       (first days)
        hours   ((juxt quot rem) (last days) seconds-in-hour)
        hr      (first hours)
        min     (quot (last hours) seconds-in-minute)
        sec     (rem (last hours) seconds-in-minute)]
    (str/join ", "
              (filter #(not (str/blank? %))
                      (conj []
                            (when (> wk 0) (str wk " wk"))
                            (when (> d 0) (str d " d"))
                            (when (> hr 0) (str hr " hr"))
                            (when (> min 0) (str min " min"))
                            (when (> sec 0) (str sec " sec")))))))
