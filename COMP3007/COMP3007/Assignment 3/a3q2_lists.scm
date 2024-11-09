;Ryan Lo (101117765)

(define (repeat x n)
  (if (= n 0) '()
        (cons x
              (repeat x (- n 1)))))

(repeat 'a 5)


(define (alternate list1 list2)
  (if (null? list1)
      (if (null? list2) '()
          (cons (car list2)
              (alternate list1 (cdr list2))))
        (cons (car list1)
              (alternate (cdr list2) list1))))

(alternate '(0 0 0 0) '(1 1 1 1 1 1))



(define (count x L)
  (if (null? L) 0
        (if (equal? x (car L)) (+ 1 (count x (cdr L))) (count x (cdr L)))))

(count 3 '(1 4 3 6 2 3 3 1 4 3 5 7))
(count 'b '(4 b a 3 2 c b 1 b 2 d a))



;(define (mode L)
 ; ())

;(mode '(a b a c a d d a b c a b)) ;→ a
;(mode '(2 b a 3 2 c b 1 b 2 d a)) ;→ 2

(define (decrease L)
  (if (null? (cdr L)) (cons (car L) '())
      (if (> (car L) (car (cdr L)))
      (cons (car L) (decrease (cdr L)))
      (cons (car L) '()))))

(decrease '(3 6 8 9 7 4 8 6 3))

(define (decreasing L)
  (if (null? L) '()
      (if (null? (cdr L)) '()
          (if (> (car L) (car (cdr L)))
              (cons (decrease L) (decreasing (cdr L)))
              (decreasing (cdr L))))))

(decreasing '(3 6 8 9 7 4 8 6 3)) ;→ ((9 7 4) (7 4) (8 6 3) (6 3))
(decreasing '(7 6 5 4 8 5 2 5 1 5 2 1)) ;→ ((7 6 5 4) (6 5 4) (5 4) (8 5 2) (5 2) (5 1) (5 2 1) (2 1))
(decreasing '(1 2 3 3 3 4 5)) ;→ ()