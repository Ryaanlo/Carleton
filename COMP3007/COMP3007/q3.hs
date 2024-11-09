type N = Integer 
ifz :: N -> N -> N -> N
ifz n x y = if n==0 then x else y

-- attempt t d c i: if the result of applying t to i is
-- not zero, apply c to it, else return d.
attempt :: (N -> N) -> N -> (N->N) -> N -> N
attempt t d c i = if t(i) /= 0 then c(i) else d
attempt t d c i = ifz (t(i)) d (c(i))