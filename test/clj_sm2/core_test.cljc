(ns clj-sm2.core-test
  (:require
    [clj-sm2.core :refer [next-recall]]

    #?(:cljs [cljs.test :refer-macros [deftest is testing]]
       :clj [clojure.test :refer :all])))

(deftest recall-scheduling
  (testing "first recall"
    (is (= (next-recall {:interval 1
                         :easiness-factor 2.5
                         :quality 4})
           {:interval 6
            :easiness-factor 2.5}))))
