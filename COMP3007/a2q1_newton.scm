; Ryan Lo (101117765)

(define (bits n)
  (if (<= n 1) 1
      (+ 1 (bits (quotient n 2)))))

(bits 10)

(define (bits-ite n)
  (if (<= n 1) 1
      (bits 