; Ryan Lo (101117765)

; The product of two numbers using recursion
(define (product x y)
  (if (> x y) 1
      (* x (product (+ x 1) y)))
)

(product 1 5)

; The product of two numbers using iteration 
(define (product-it x y)
  (define (iterate n1 n2 count)
    (if (> count y) n1
        (iterate (* n1 n2) (+ n2 1) (+ count 1))))
  ;(if (<= x 1) x
  (iterate 1 x 1)
)

(product-it 1 5)

;PI notations
;i=2 to 20, i^3 - i^2

(define (pi1 x y)
  (if (> x y) 1
      (* (- (* x x x) (* x x)) (pi1 (+ x 1) y)))
)

(pi1 2 20)

(define (pi2 x y)
  (if (> x y) 1
      (* (* (+ (* 2 x) 1) (+ (* 2 x) 1)) (pi2 (+ x 1) y)))
)

(pi2 1 15)

(define (pi3 x y z)
  (if (> x y) 1
      (if (> y z) 1
          (* (* 3 x (* y y)) (pi3 (+ x 1) y z))
      )
  )
)

(pi3 1 5 6)