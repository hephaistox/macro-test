(ns ch8-macros.01-evals
  (:require [clojure.test :refer [deftest is testing]]))

(deftest ee
  (testing "Integer are evaluated as integer"
    (is (= 42
           (eval 42))))
  (testing "Strings are evaluated as strings also"
    (is (= "foo"
           (eval "foo"))))

  (testing "List"
    (is (= [1 2]
           (eval '(list 1 2)))))
  (testing "(list 1 2) is evalued to (1 2), sending this to eval try to interpret 1 as a function, so an exception is thrown"
    (is (thrown? java.lang.ClassCastException
                 (eval (list 1 2)))))
  (testing "if the first result of the expression is a function everything is much better happening"
    (is (= 3
           (eval (list (symbol "+") 1 2))))))
