--
-- chain3 f g h is a function which, given an input x,
-- first applies f to x, then g to the result of that,
-- then h.
type N = Integer
chain3 :: (N -> N) -> (N -> N) -> (N -> N) -> N -> N
chain3 f g h = \x -> h(g(f x))