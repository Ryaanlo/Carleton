import Distribution.Simple.Utils (xargs)
type N = Integer

p1 :: N -> N
p1 x = x+1

p2 :: N -> N
p2 x = p1 (p1 x)

p2_alt :: N -> N
p2_alt = p1 . p1


add :: N -> (N -> N)
add x y = x+y

f :: N->N
f x = (add 3) x

g :: N -> N
g = add 3

h :: N -> (N -> N)
h x = add x

h2 :: Num a => a -> a -> a
h2 x = (x+)

fact n = if n==0 then 1 else fact (n-1)