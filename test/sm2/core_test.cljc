(ns sm2.core-test
  (:require
   [sm2.core :refer [next-recall]]
   #?(:cljs [cljs.test :refer-macros [deftest is testing]]
      :clj [clojure.test :refer :all])))

(deftest recall-scheduling
  (testing "first recall"
    (is (= (next-recall {:quality 4})
           {:index 0
            :days-to-recall 1
            :easiness-factor 250
            :should-be-repeated false}))

    (is (= (next-recall {:quality 3})
           {:index 0
            :days-to-recall 1
            :easiness-factor 236
            :should-be-repeated true})))

  (testing "second recall"
    (is (= (next-recall {:index 0
                         :days-to-recall 1
                         :easiness-factor 250
                         :quality 4})
           {:index 1
            :days-to-recall 6
            :easiness-factor 250
            :should-be-repeated false}))

    (is (= (next-recall {:index 0
                         :days-to-recall 1
                         :easiness-factor 250
                         :quality 5})
           {:index 1
            :days-to-recall 6
            :easiness-factor 260
            :should-be-repeated false})))

  (testing "any recall when response quality was lower than 3"
    (is (= (next-recall {:index 1
                         :days-to-recall 6
                         :easiness-factor 250
                         :quality 2})
           {:index 0
            :days-to-recall 1
            :easiness-factor 218
            :should-be-repeated true})))

  (testing "third recall when response quality was 4 (no change in easiness factor)"
    (is (= (next-recall {:index 1
                         :days-to-recall 6
                         :easiness-factor 250
                         :quality 4})
           {:index 2
            :days-to-recall 15
            :easiness-factor 250
            :should-be-repeated false})))

  (testing "third recall when response quality was 5 (easiness factor grows)"
    (is (= (next-recall {:index 1
                         :days-to-recall 6
                         :easiness-factor 250
                         :quality 5})
           {:index 2
            :days-to-recall 15
            :easiness-factor 260
            :should-be-repeated false})))

  (testing "third recall when response quality was 3 (easiness factor decreases)"
    (is (= (next-recall {:index 1
                         :days-to-recall 15
                         :easiness-factor 250
                         :quality 3})
           {:index 2
            :days-to-recall 35
            :easiness-factor 236
            :should-be-repeated true})))

  (testing "minimum easiness factor is 130"
    (is (= (next-recall {:index 1
                         :days-to-recall 15
                         :easiness-factor 140
                         :quality 3})
           {:index 2
            :days-to-recall 19
            :easiness-factor 130
            :should-be-repeated true}))))
