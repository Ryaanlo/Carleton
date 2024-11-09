;Ryan Lo (101117765)

(define square (lambda(x)(* x x)))
(define double (lambda(x)(+ x x)))

(define (compose f g)
  (lambda (x) (f (g x))))

((compose square double) 3)



(define (divisibleBy y)
  (lambda (x) (integer? (/ x y))))

(define mod3 (divisibleBy 3))

(mod3 7)
(mod3 9)


(define (newmap f)
  (lambda (x) (if (null? x) '()
                  (cons (f (car x))
                        (cdr x)))))

(define double-mapper (newmap (lambda(x)(* x 2))))
(double-mapper '(1 2 3 4))
(double-mapper '(10 20 30))


(define (newfilter f)
  (lambda (x) ))


(define evens-filter (newfilter (divisibleBy 2)))
