(ns ch8-macros.03-simple-macro
  (:require [clojure.walk :as walk]
            [clojure.test :refer [deftest is testing]]))

(defmacro do-until [& clauses]
  (when clauses
    (list 'clojure.core/when (first clauses)
          (if (next clauses)
            (second clauses)
            (throw (IllegalArgumentException. "do-until requires an even number of forms")))
          (cons 'do-until (nnext clauses)))))

(deftest do-until-test
  (testing "show what one level expansion produces"
    (is (= '(clojure.core/when true (prn 1) (do-until false (prn 2)))
           (macroexpand-1
            '(do-until true (prn 1)
                       false (prn 2))))))
  (testing "show what full expansion produces"
    (is (= '(if true (do (prn 1) (if false (do (prn 2) nil))))
           (walk/macroexpand-all
            '(do-until true (prn 1)
                       false (prn 2)))))))
