(ns sm2.core)

(def ^:private minimum-easiness-factor 130)

(def ^:private initial-easiness-factor 250)

(def ^:private first-recall
  {:index 0
   :days-to-recall 1
   :easiness-factor initial-easiness-factor})

(def ^:private second-recall
  {:index 1
   :days-to-recall 6})

(defn- first-response? [response]
  (nil? (:index response)))

(defn- second-response? [response]
  (= (:index response) 0))

(defn- compute-days-to-recall [easiness-factor days-to-recall]
  (int (* (/ easiness-factor 100) days-to-recall)))

(defn- delta-easiness-factor [q]
  (int (+ (* q q (- 2)) (* q 28) (- 80))))

(defn- compute-easiness-factor [easiness-factor quality]
  (max minimum-easiness-factor
       (+ easiness-factor (delta-easiness-factor quality))))

(defn- add-if-should-be-repeated [previous-quality recall]
  (assoc recall :should-be-repeated (< previous-quality 4)))

(defn- create-next-recall [{:keys [easiness-factor quality index days-to-recall] :as response}]
  (cond (first-response? response)
        (assoc first-recall
               :easiness-factor
               (compute-easiness-factor initial-easiness-factor quality))

        (< quality 3)
        (assoc first-recall
               :easiness-factor
               (compute-easiness-factor easiness-factor quality))

        (second-response? response)
        (assoc second-recall
               :easiness-factor
               (compute-easiness-factor easiness-factor quality))

        :else
        (let [new-easiness-factor (compute-easiness-factor easiness-factor quality)]
          {:index (inc index)
           :days-to-recall (compute-days-to-recall new-easiness-factor days-to-recall)
           :easiness-factor new-easiness-factor})))

(defn next-recall [{:keys [quality] :as response}]
  (->> response
       create-next-recall
       (add-if-should-be-repeated quality)))
