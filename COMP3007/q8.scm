(define (filter pred seq) ;lec notes 4.2
  (cond ((null? seq) '())
        ((pred (car seq)) (cons (car seq) (filter pred (cdr seq))))
        (else (filter pred (cdr seq)))))
  
(define (collaspe L)
  (if (null? L) '()
      (cons (car L) (collaspe (filter (lambda (x) (not (equal? x (car L))))
                                      (cdr L))))))

(collaspe '(1 1 2 2 3 4 4 3))