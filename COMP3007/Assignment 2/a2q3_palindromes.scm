; Ryan Lo (101117765)

; Palindrome Function
; Input: takes in aa string
; Output: True or false whether or not the string is a palindrome
(define (palindrome? s)
  (define (test s i n)
    (cond ((not (char=? (string-ref s 0) (string-ref s (- n 1)))) #f)
          (else (palindrome? (substring s 1 (- n 1))))
    )
  )
  (cond ((or (= (string-length s) 0) (= (string-length s) 1)) #t)
        (else (test s (string-ref s 0) (string-length s))))
)

(palindrome? "tacocat")
(palindrome? "taco cat")

; K-Palindrome Function
; Input: Takes in a string and a number of chars to ignore
; Output:  True or false whether or not the string is a k-palindrome
(define (k-palindrome? s x)
  (define (test s i n)
    (cond ((not (char=? (string-ref s 0) (string-ref s (- n 1)))) (k-palindrome? (substring s 1 (- n 1)) (- x 1)))
          (else (k-palindrome? (substring s 1 (- n 1)) x))
    )
  )
  (cond ((< x 0) #f)
        ((or (= (string-length s) 0) (= (string-length s) 1)) #t)
        (else (test s (string-ref s 0) (string-length s))))
)

(k-palindrome? "tgtth" 1)