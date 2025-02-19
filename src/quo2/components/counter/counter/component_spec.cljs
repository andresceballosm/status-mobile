(ns quo2.components.counter.counter.component-spec
  (:require [quo2.components.counter.counter.view :as counter]
            [test-helpers.component :as h]))

(h/describe "counter component"
  (h/test "default render of counter component"
    (h/render [counter/view {} nil])
    (-> (h/expect (h/get-by-test-id :counter-component))
        (h/is-truthy)))

  (h/test "renders counter with a string value"
    (h/render [counter/view {} "1"])
    (-> (h/expect (h/get-by-text "1"))
        (h/is-truthy)))

  (h/test "renders counter with an integer value"
    (h/render [counter/view {} 1])
    (-> (h/expect (h/get-by-text "1"))
        (h/is-truthy)))

  (h/test "renders counter with max value 99+ by default"
    (h/render [counter/view {} 100])
    (-> (h/expect (h/get-by-text "99+"))
        (h/is-truthy)))

  (h/test "renders counter with custom max value when set to 150"
    (h/render [counter/view {:max-value 150} 151])
    (-> (h/expect (h/get-by-text "150+"))
        (h/is-truthy))))
