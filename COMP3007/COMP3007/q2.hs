--
-- shift f n produces a function that first adds
-- n to its input then applies f. For example, if
-- sq n = n*n, then shift sq 0 3 = 9 and
-- shift sq 1 3 = 16
type N = Integer
sq :: Num a => a -> a
sq n = n*n

shift :: (N->N) -> N -> N -> N
--shift f n x = f(n+x)
shift f n = \x -> f(n+x)