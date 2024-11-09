(define (stream-car stream)
  (car stream))
(define (stream-cdr stream)
  (force (cdr stream)))


(define-syntax stream-cons
  (syntax-rules ()
    ((stream-cons a b)(cons a (delay b)))))

(define (inf n)
  (stream-cons n (inf (expt (expt 2 (- n 1)) n))))

(stream-cdr (inf 5))