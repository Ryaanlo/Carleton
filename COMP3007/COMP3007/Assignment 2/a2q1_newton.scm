; Ryan Lo (101117765)

; Cuberoot Function
; Input: Takes in a number
; Output: The cuberoot of the inputted number
(define (cuberoot x)
  (define (cube y) (* y y y))

  (define (goodenough? guess x)
    (< (abs (- (cube guess) x)) 0.001))

  (define (average x y) (/ (+ x y) 2))

  (define (improve guess x) (/ (+ (/ x (expt guess 2)) (* 2 guess)) 3))

  (define (cube-ite guess x)
    (if (goodenough? guess x)
         guess
         (cube-ite (improve guess x ) x)))

  (cube-ite 1.0 x)
)

(cuberoot 27)