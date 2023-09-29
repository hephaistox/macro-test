(ns ch8-macros.00-quote-syntax-quote-test
  (:require [clojure.test :refer [deftest is testing]]))

(def x
  "x is a var, root bounded to 9"
  9)

(deftest quote-test
  (testing "The quoting prevents the interpretation of the inner code, without it `1` will be interpreted as a function and will fail"
    (is (= '(1 2 3)
           (list 1 2 3))))
  (testing "The symbol `'` and quote functions are identical"
    (is (= 'x
           (quote x))))
  (testing "Quotes are returning symbols"
    (is (= (class (quote x))
           clojure.lang.Symbol)))
  (testing "Quote is returning the symbol itself, note it is not fully qualified"
    (is (= (quote x)
           (symbol 'x)))))

(deftest syntax-quote-test
  (testing "The syntax unquoting prevent to interpret the inner code, without it `1 will be interpreted as a function and will fail"
    (is (= `(1 2 3)
           (list 1 2 3))))
  (testing "As quoting, the syntax quoting returns a symbol"
    (is (= (class `x)
           clojure.lang.Symbol)))
  (testing "Unlike quoting, the symbol is fully qualified thanks to auto qualification mechanism, assyming the symbol is defined in the current namespace"
    (is (= `x
           (symbol 'ch8-macros.00-quote-syntax-quote-test/x))))
  (testing "Autoqualification happens with clojure functions"
    (is (= `map
           (symbol 'clojure.core/map))))
  (testing "Autoqualification happens with java functions"
    (is (= `Integer
           (symbol 'java.lang.Integer))))
  (testing "Autoqualification happens for the whole form"
    (is (= `(map even? [1 2 3])
           `(clojure.core/map clojure.core/even? [1 2 3]))))
  (testing "Autoqualification returns a hypothetic local symbol if not found elsewhere"
    (is (= `is-always-right
           (symbol 'ch8-macros.00-quote-syntax-quote-test/is-always-right)))))

(deftest quote-unquote-test
  (testing "Unquote is not compatible with quote, even if the name could say so. As quoting is just preserving any evaluation in it"
    (is (not (= '(~`+ 10 ~(* 3 2))
                `(+ 10 6))))))

(deftest unquote-test
  (testing "Quoting returns the whole form unevaluated"
    (is (= `(~`+ 10 (~`* 3 2))
           `(+ 10 (* 3 2)))))
  (testing "If you want to evaluate a subpart of it, use unquote `~`"
    (is (= `(~`+ 10 ~(* 3 2))
           `(+ 10 6))))
  (testing "Replacement happening whereever you need"
    (is (= '(1 2 3)
           (let [x 2]
             `(1 ~x 3)))))
  (testing "You can unqote anything, even list in this example"
    (is (= '(1 (2 3))
           (let [x '(2 3)]
             `(1 ~x)))))
  (testing "Unquote splicing is helping with flattening the result"
    (is (= '(1 2 3)
           (let [x '(2 3)]
             `(1 ~@x))))))
