(ns clj-sm2.core-test
  (:require
    [clj-sm2.core :refer [next-recall]]

    #?(:cljs [cljs.test :refer-macros [deftest is testing]]
       :clj [clojure.test :refer :all])))

(deftest recall-scheduling
  (testing "first recall when response quality quality was 4 (no change in easiness factor)"
    (is (= (next-recall {:quality 4})
           {:index 0
            :days-to-recall 1
            :easiness-factor 2.5})))

  (testing "second recall when response quality quality was 4 (no change in easiness factor)"
    (is (= (next-recall {:index 0
                         :days-to-recall 1
                         :easiness-factor :some-easines-factor
                         :quality 4})
           {:index 1
            :days-to-recall 6
            :easiness-factor :some-easines-factor})))

  (testing "any recall when response quality was lower than 3"
    (is (= (next-recall  {:index 1
                          :days-to-recall 6
                          :easiness-factor :some-easines-factor
                          :quality 2})
           {:index 0
            :days-to-recall 1
            :easiness-factor :some-easines-factor})))

  (testing "third recall when response quality quality was 4 (no change in easiness factor)"
    (is (= (next-recall {:index 1
                         :days-to-recall 6
                         :easiness-factor 2.5
                         :quality 4})
           {:index 2
            :days-to-recall 15
            :easiness-factor 2.5}))))
