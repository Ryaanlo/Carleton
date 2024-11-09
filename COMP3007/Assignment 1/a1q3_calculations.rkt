;Ryan Lo (101117765)

; make it a whole number leaving one decimal place
; round to remove decimal place
; move decimal place back by dividing

; Offset Function used in standard-roundf
; Used to offset the number to round ending decimal place with 5 up instead of down
(define (offset x) (cond ((< x 0) (- x 0.1))
                         ((> x 0) (+ x 0.1))))

; Standard-roundf Function
; Input : x as float and y as int, x is the number to be rounded, y is the amount of decimal places desired
; Output : int rounded with the desired amount of decimal places
(define (standard-roundf x y)
  (/ (round (offset(* (expt 10 y) x))) (expt 10 y))
)

(standard-roundf -1.82593 2)
(standard-roundf 1.45593 1)
(standard-roundf -1.14593 3)

; Quadratic Equation Function
; Input : a b c as numbers
; Ouput : the + root of the quadratic equation
(define (quadratic a b c)
        (cond ((= a 0) #f)
        ((< (- (* b b) (* 4 a c)) 0) #f)
        (else (/ (+ (* b -1) (sqrt (- (* b b) (* 4 a c)))) (* 2 a)))
        )
  )

(quadratic 3 1 2)
(quadratic 1 4 2)
(quadratic 1 4 -2)

; Convert Function
; Input : x as the number to be converted, y as string and the starting units
;         z as string for the units to be converted to
; Output : the number xs converted from y to z
(define (convert x y z)
  (cond ((string=? y "KB")
         (cond ((string=? z "B") (* x 1000))
               ((string=? z "KiB") (/ x 1.024))
               (else #f)
               ))
         ((string=? y "B")
         (cond ((string=? z "KB") (/ x 1000.0))
               ((string=? z "KiB") (/ x 1024.0))
               (else #f)
               ))
         ((string=? y "KiB")
         (cond ((string=? z "B") (* x 1024))
                ((string=? z "KB") (* x 1.024))
                (else #f)
                ))
         (else #f)
   )
)
(convert 22 "KB" "KiB")
(convert 22 "KiB" "KB")
(convert 22 "B" "KiB")
(convert 22 "B" "KB")
(convert 42 "Bytess" "KiB")