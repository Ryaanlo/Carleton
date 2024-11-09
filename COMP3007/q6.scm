(let ((a (let ((a 1)(b 2))(* a ( + b a)))) (b (* 3 (+ 4 2)))) (+ a (* b a)))

((lambda (a b)(* a ( + b a))) 1 2)

(lambda (a b) ((lambda (a b)(* a ( + b a))) 1 2)(b (* 3 (+ 4 2))))(+ a (* b a))


(lambda (a b) (let ((a 1)(b 2))(* a ( + b a))) (b (* 3 (+ 4 2))) (+ a (* b a)))