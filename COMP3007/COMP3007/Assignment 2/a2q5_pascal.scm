; Ryan Lo (101117765)

; Pascal Function
; Input: the two index of the number in a pascal triangle
; Ouput: the number at the index
(define (pascals x y)
  (if (or (= (+ x 1) 1) (= (+ y 1) 1) (= x y)) 1
  (+ (pascals (- x 1) (- y 1)) (pascals (- x 1) y)))
)

(pascals 2 1)

; PrintTriangle Function
; Prints out the pascals triangle
(define (printTriangle x)
  (define (printrow n1)
    (if (< n1 x) (display (pascals n1 n2)) (printrow n1 (+ n2 1)))
  )

    
  (define (print n1 n2)
    (if (< n1 x)
        (print (+ n1 1) 0)
        (if (< n2 x)
            (display (pascals n1 n2))
            (print n1 (+ n2 1))
        )
    )
    
  )
  (if (> x 0)
      (print 0 0))
)
;(display (pascals 0 0))
;(newline)
;(display (pascals 1 0))
;(display (pascals 1 1))

(printTriangle 3)