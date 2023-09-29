(ns ch8-macros.02-syntax-quote
  (:require [clojure.test :refer [deftest is testing]]))

(defn contextual-eval
  "Evaluate the expression with `ctx` as a context map, meaning all keys will be replaced with their value in the `expr`
  Note this is a function, and a macro yet
  An interesting feature is the `'~v"
  [ctx expr]
  (eval
   `(let [~@(mapcat (fn [[k v]] [k `'~v]) ctx)]
      ~expr)))

(deftest context-eval-test
  (testing "contextual eval is working as expected"
    (is (= 3
           (contextual-eval '{a 1, b 2}
                            '(+ a b)))))
  (testing "binding in `ctx` are superseeding the context"
    (is (= 1001
           (contextual-eval '{a 1, b 2}
                            '(let [b 1000] (+ a b)))))))

(def x
  9)

(def y
  '(- x))

(deftest eval-test
  (testing "The syntax quote returns the symbol"
    (is (= clojure.lang.Symbol
           (class `y)))
    (is (= 'ch8-macros.02-syntax-quote/y
           `y)))
  (testing "Doubled syntax quote is returning"
    (is (= `(quote ch8-macros.02-syntax-quote/y)
           ``y)))
  (testing "Doubled syntax quote is returning"
    (is (= clojure.lang.Symbol
           (class ``~y)))
    (is (= 'ch8-macros.02-syntax-quote/y
           ``~y)))
  (testing "Paired syntax-quote an unquote are finally returning the content of the var"
    (is (= clojure.lang.PersistentList
           (class ``~~y)))
    (is (= '(-x)
           ``~~y)))
  (testing "That expression can be used later on and nothing bad happen in the contextual-event function which is using unquote and quotings"
    (is (= -36
           (contextual-eval {'x 36} ``~~y)))))
