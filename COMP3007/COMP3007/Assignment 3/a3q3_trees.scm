;Ryan Lo (101117765)

(list? (car '(1 (2 3)((4 5) 6 (7)((8 (9)) 10)))))

(define (tree-reduce oper x L)
  (cond ((null? L) 0)
          ((list? (car L)) (oper (car L)
                      (tree-reduce oper x (cdr L))))
          (else (oper (car L)
                      (tree-reduce oper x (cdr L))))))

(tree-reduce + 0 '(1 (2 3)((4 5) 6 (7)((8 (9)) 10))))

(list? (car '(1 (2 3)((4 5) 6 (7)((8 (9)) 10)))))
(car '(1 (2 3)((4 5) 6 (7)((8 (9)) 10))))


(define (height L)
  (if (null? L)0
      (+ 1 (max (height (cadr L)) 
                  (height (caddr L))))))

(height 'a)
(height '(a))
(height '(a (b) c))
(height '(((((a(((b)))))))))

(define (flatten-tree L)
      (cond ((null? L) '())
          ((list? (car L)) (cons (flatten-tree (car L))
                                 (flatten-tree (cdr L))))
          (else (cons (f (car T))
                      (flatten-tree (cdr L))))))
  