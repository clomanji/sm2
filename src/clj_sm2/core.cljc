(ns clj-sm2.core)

(def ^:private minimum-easiness-factor 130)

(def ^:private first-recall
  {:index 0
   :days-to-recall 1
   :easiness-factor 250})

(def ^:private second-recall
  {:index 1
   :days-to-recall 6})

(defn- first-response? [response]
  (nil? (:index response)))

(defn- second-response? [response]
  (= (:index response) 0))

(defn- days-to-recall [easiness-factor days-to-recall]
  (int (* (/ easiness-factor 100) days-to-recall)))

(defn- delta-easiness-factor [q]
  (int (+ (* q q (- 2)) (* q 28) (- 80))))

(defn- easiness-factor [{:keys [easiness-factor quality]}]
  (max minimum-easiness-factor
       (+ easiness-factor (delta-easiness-factor quality))))

(defn next-recall [response]
  (cond (first-response? response)
        first-recall

        (< (:quality response) 3)
        (assoc first-recall :easiness-factor (:easiness-factor response))

        (second-response? response)
        (assoc second-recall :easiness-factor (:easiness-factor response))

        :else
        (let [easiness-factor (easiness-factor response)]
          {:index (inc (:index response))
           :days-to-recall (days-to-recall easiness-factor (:days-to-recall response))
           :easiness-factor easiness-factor})))
