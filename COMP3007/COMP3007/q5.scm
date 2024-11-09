; Ryan Lo (101117765)

(define (bits n)
  (if (<= n 1) 1
      (+ 1 (bits (quotient n 2)))))

(bits 5)

(define (bits-ite n c)
  (if (<= n 1) (+ c 1)
      (bits-ite (quotient n 2) (+ 1 c))))

(bits-ite 5 0)