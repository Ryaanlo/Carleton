;Ryan Lo (101117765)

; Exponent Function used in sci-exponent
; Counts the number of exponents to change x into scientific notation
(define (exponent x y)
   (cond ((and (< x 1) (> x 0)) (exponent (* x 10.0) (- y 1)))
         ((> x 10) (exponent (/ x 10.0) (+ y 1)))
         ((< x 0) (exponent (/ (abs x) 10.0) (+ y 1)))
         (else y)
   )
)

; Sci-exponent Function
; Input : x as number
; Output : exponent number if were in scientific notation
(define (sci-exponent x)
   (exponent x 0)
)
(sci-exponent -12354)
(sci-exponent 12354)
(sci-exponent -12.354)
(sci-exponent 123.54)
(sci-exponent 0.012354)

; Sci-coefficient Function
; Input: Takes in a number that's to be turned into scientific notation
; Output: coefficient of the number in scientific notation
(define (sci-coefficient x)
  (cond
        ; Case where number is positive
        ((and (< x 1) (> x 0)) (sci-coefficient (* x 10.0)))
        ((> x 10) (sci-coefficient (/ x 10.0)))
        ; Case where number is negative
        ((and (> x -1) (< x 0)) (sci-coefficient (* x 10.0)))
        ((< x -10) (sci-coefficient (/ x 10.0)))
        (else x)
   )
)
(sci-coefficient -1234)
(sci-coefficient 123.4)
(sci-coefficient -1234.0)
(sci-coefficient 0.001234)

; Sci-num Function
; Input : Takes in a number
; Output : a number in scientific notation
(define (sci-num x)
  (cond ((not (number? x)) #f)
        (else (string-append (string-append (number->string (sci-coefficient x)) "x10^") (number->string (sci-exponent x))))
  )

)
(sci-num 123.34)
(sci-num 0.00123)
(sci-num -123.324)
(sci-num -123)
