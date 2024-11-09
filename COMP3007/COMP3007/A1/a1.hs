-- COMP3007 Assignment 1
-- Ryan Lo
-- 101117765
{-
The Haskell subset specified in the other document, which includes the following three functions and one defined later, is all you are allowed 
to use in your code in this file.
-}

type N = Integer

-- The type below is polymorphic: you could substitute any type
-- you want for the variable a and you would still have a valid 
-- type for if_zero.
if_zero :: N -> a -> a -> a
if_zero x y z = if x==0 then y else z

-- "successor" of a natural number
s :: N -> N
s x = x+1

-- "predecessor" of a natural number.  Note: the predecessor of 0 
-- defined to be 0.
p :: N -> N
p x = if_zero x 0 (x-1)

{- 
Question 4. Complete the function definition below.  Remember that nothing else can go on the left of the "=".  Specification: the value of add x y is the sum of x and y.
-}
add :: N -> N -> N
add = \x y -> if_zero (p y) (s x) (add (s x) (p y)) 

{- 
Question 5.  Specification: the value of sub x y is 0 if y>x else it is x-y.  
-}
sub :: N -> N -> N
sub = \x y -> if_zero (p y) (p x) (sub (p x) (p y)) 

{-
Question 6.  The value of iter f x n is f applied n times to x, i.e. 
f(f(....(f x)...)).  E.g. iter (s . s) 2 3 = 8. (s . s is s composed with s, 
so (s . s)x = s(s x))
-}

iter :: (a->a) -> a -> N -> a
iter = \f x n -> if_zero (p n) (f x) (f (iter f x (p n))) 

{- 
Question 7.  The value of mul x y is x times y. Write this WITHOUT using
recursion in the definition, i.e. mul cannot appear on the right hand side 
of the definition. No using "helper" functions where you just do the 
recursion elsewhere.
-}
mul :: N -> N -> N
mul = \x y -> if_zero (p y) (add x x) (iter (add x) x (p y)) 

{-
Below are two definitions for use in the last two questions.
It may not be obvious, but the type NPair represents pairs of 
natural numbers.
-}
type NPair = (N -> N -> N) -> N

pair :: N -> N -> NPair
pair = \x y -> \f -> f x y

{-
Question 8.  Define first and second so that first (pair x y) = x and 
second (pair x y) = y.
-}

first :: NPair -> N
first = \x -> x (\x y -> x)
second :: NPair -> N
second = \x -> x (\x y -> y)

{- 
Question 9. You might find this one to be a challenge.  Give an alternate definition of p that does *not* use any of the functions defined above except for s, iter, pair, first and second.  Specification: the value of my_p x is x minus one.
-}
my_p :: N -> N
my_p = \x -> iter s (first (pair 0 1)) x