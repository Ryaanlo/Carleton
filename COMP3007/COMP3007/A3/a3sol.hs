module A2Solution where
    
{-
**** Assignment 3 Due Monday Nov 8 23:50 ****

This assignment should be reasonably short.  The sample solution has around 30 short lines of code.  However, you may not find it easy, depending on how comfortable you are with higher-order programming (i.e. writing functions that have functions as arguments).  All questions have equal weight.

** CODE RESTRICTIONS **

You can use any defined functions from the prelude.  You *cannot* use the following.

1. Recursion.  Any function you use anywhere has to be already defined, either in the Prelude, or earlier in your file.  This restriction is to steer you toward the style of programming this assignment is supposed to give experience with.

2. List comprehension.  Yes, it's convenient, but it is it's own programming style.

** HINTS **

You might find the following functions useful. They were the only ones I (dh)needed for the sample solution.  You don't need to use them.  They are all from the prelude (i.e. baked into Haskell).  For documentation see
https://hackage.haskell.org/package/base-4.15.0.0/docs/Prelude.html

- :, head, tail, reverse, replicate, length
- map, zip, zipWith 
- take
- foldl, foldr ()
- the data type Maybe, with constructors Just and Nothing
- not sure if there's a named function for this, 
  but [m..n] is handy e.g. [2..4] = [2,3,4]
- uncurry
- fmap (it's handy though perhaps a bit mysterious. It's easy to avoid using it,
  but if you do use it you will feel like a champion)

Of the above, the ones I used the most were map and the two folds.

One more tip: "$" is your friend!  It can save writing ugly parentheses.  E.g. you can write f $ g $ h 17 for f (g (h 17)).  Generally, f $ ... means everything after the $ is argument to f.  Also handy for avoiding parens in the function composition operator ".".  E.g. you could write the above example as (f . g . h) 17.  You're welcome.
-}

{- 
Question 1
(generateList n f x = [x, f x, f (f x), ...] with length = n
-}
generateList :: Int -> (a -> a) -> a -> [a]
generateList n f first = 
    reverse $ foldl g [first] (replicate (n-1) ())
    where g (x:l) _ = f x : x : l
          g [] _ = error "this case will never happen"

{- 
Question 2
(firstWhen p l) = Just v if v is the first member of l satisfying p (Nothing if there is no such v)
-}
firstWhen :: (a -> Bool) -> [a] -> Maybe a
firstWhen p = foldl f Nothing
    where f (Just v) _ = Just v
          f Nothing x = if p x then Just x else Nothing

{- 
Question 3
(index l i) is the element of l at position i, or Nothing if i is out of bounds.  Note: as in Python, we start counting at 0, i.e. (index l 0) gives the first element of the list.
-}
index :: [a] -> Int -> Maybe a
index l i =
    fst <$> firstWhen ((== i) . snd ) lIndexed
    where lIndexed = zip l $ [0 .. length l]

{-
Question 4
(reduceSequence m n a z f) = z `f` a(m) `f` a(m+1) `f` ... `f` a(n)
For convenience, assume f is associative and z is a zero for f, i.e.
(x `f` y) `f` z = x `f` (y `f` z) 
and
z `f` x = x `f` z = x 
-}
reduceSequence :: Int -> Int -> (Int -> a) -> a -> (a -> a -> a) -> a
reduceSequence m n a z f =
    foldl f z (map a [m..(n-1)])

{-
A data type of tables.  Rows are lists of elements, and tables are lists of rows. See below for an example.
-}
data Table a = Table [[a]] deriving (Show, Eq)
tableRep :: Table a -> [[a]]
tableRep (Table l) = l

{-
It looks more table-y with some newlines added:
[[1,2,3], 
 [4,5,6], 
 [7,8,9], 
 [10,11,12]
]
So, it's a table with four rows and three columns.
-}
aTable :: Table Integer
aTable = Table [[1,2,3], [4,5,6], [7,8,9], [10,11,12]]

{- 
Question 5
checkSquare (Table ll) is True exactly if there is some n such that ll has n rows and each row has length n (hence there are n columns).
-}
checkSquare :: Table a -> Bool
checkSquare (Table l) =
    all (\r -> length r == length l) l

{- 
Question 6
The length of the shortest row in the table.
-}
minRowLength :: Table a -> Int
minRowLength (Table ll) =
    foldr f (-1) ll
    where f x z = if z == -1 || length x < z then length x else z

{- 
Question 7
Apply f to each element of the table.
>>> mapTable (1+) aTable
Table [[2,3,4],[5,6,7],[8,9,10],[11,12,13]]
-}
mapTable :: (a -> b) -> Table a -> Table b
mapTable f (Table l) =
    Table $ map (map f) l

{- 
Question 8
Create a square table of size n where the j-th element of row i is (i,j) for all i,j with 1 <= i,j <= n.
>>> indexesTable 3
[[(0,0),(0,1),(0,2)],
 [(1,0),(1,1),(1,2)],
 [(2,0),(2,1),(2,2)]
]
-}
indexesTable :: Int -> Table (Int, Int)
indexesTable n =
    Table $ map f [0..(n-1)]
    where f i = map (\j -> (i,j)) [0..(n-1)]

{- 
Question 9
tableFromElements a n =
[[a 0 0, a 0 1, ..., a 0 (n-1)],
 [a 1 0, a 1 1, ..., a 1 (n-1)],
 ...
 [a (n-1) 0, a (n-2) 1, ..., a (n-1) (n-1)]
]
-}
tableFromElements :: (Int -> Int -> a) -> Int -> Table a
tableFromElements a n =
    mapTable (uncurry a) (indexesTable n)

{- 
Question 10
The largest square table such that it's possible to add rows and add to the end of rows to get m.
>>> squarifyTable aTable 
[[1,2,3],
 [4,5,6],
 [7,8,9]
]
-}
squarifyTable :: Table a -> Table a
squarifyTable m@(Table ll) =
    Table $ take n (map (take n) ll)
    where n = min (length ll) (minRowLength m)

{- 
Question 11
Assume Table is always square.
-}
-- 
firstColumn :: Table a -> [a]
firstColumn (Table ll) =
    map head ll

{- 
Question 12
Assume Table is always square.
The transpose of a table: the i-th row of the transpose is the i-th column of the input.
>>> indexesTable 2
Table [[(0,0),(0,1)],[(1,0),(1,1)]]
>>> transposeTable (indexesTable 2)
Table [[(0,0),(1,0)],[(0,1),(1,1)]]
>>> simplerTable = Table [[1,2],[3,4]]
>>> transposeTable simplerTable
Table [[1,3],[2,4]]
-}
transposeTable :: Table a -> Table a
transposeTable (Table ll) =
    Table (foldr f emptyRows ll)
    where f llRow rows = zipWith (:) llRow rows            
          emptyRows = replicate (length ll) []
