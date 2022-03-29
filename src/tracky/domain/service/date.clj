(ns tracky.domain.service.date)

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
