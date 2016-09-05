(ns clj-sm2.core)

(def ^:private first-recall
  {:index 0
   :days-to-recall 1
   :easiness-factor 2.5})

(def ^:private second-recall
  {:index 1
   :days-to-recall 6})

(defn- first-response? [response]
  (nil? (:index response)))

(defn- second-response? [response]
  (= (:index response) 0))

(defn- days-to-recall [{:keys [easiness-factor days-to-recall]}]
  (* easiness-factor days-to-recall))

(defn next-recall [response]
  (cond (first-response? response)
        first-recall

        (< (:quality response) 3)
        (assoc first-recall :easiness-factor (:easiness-factor response))

        (second-response? response)
        (assoc second-recall :easiness-factor (:easiness-factor response))

        :else
        {:index (inc (:index response))
         :days-to-recall (days-to-recall response)
         :easiness-factor 2.5}))
